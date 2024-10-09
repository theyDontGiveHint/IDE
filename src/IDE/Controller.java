package IDE;

import java.util.Objects;
import java.io.*;

/**
 * ProcessBuilder 명령어를 제어하는 클래스입니다.
 */
public class Controller {
    public Controller() {
        this.processBuilder = new ProcessBuilder();
    }

    private final ProcessBuilder processBuilder;

    private String stdOut;
    private String stdErr;

    protected boolean isCompiled = false;
    protected boolean isError = false;

    /**
     * 업로드 된 .java 파일을 컴파일합니다.
     *
     * @param file 업로드 파일
     */
    public void compile(UploadFile file) throws IOException, InterruptedException {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                this.processBuilder.command("cmd.exe", "/c", "javac", file.filePath);
            } else {
                this.processBuilder.command("sh", "-c", String.format("javac %s", file.filePath));
            }

            Process process = this.processBuilder.start();
            process.waitFor();

            BufferedReader stdErrReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder stdErrBuilder = new StringBuilder();

            String line;
            while ((line = stdErrReader.readLine()) != null) {
                stdErrBuilder.append(line).append("\n");
            }

            this.stdErr = stdErrBuilder.toString();

            // 컴파일 성공 여부 확인 (표준 에러 스트림이 비어 있으면 성공)
            if (this.stdErr.isEmpty()) {
                this.isCompiled = true;
                System.out.println("컴파일이 성공적으로 완료되었습니다.");
            } else {
                System.err.println("컴파일 중 오류가 발생했습니다.");
            }

        } catch (IOException | InterruptedException e) {
            throw e;
        }
    }


    /**
     * 컴파일 된 .class 파일을 실행합니다.
     *
     * @param file 컴파일 된 업로드 파일
     */
    public void run(UploadFile file) {
        // 컴파일이 성공했는지 확인 (컴파일이 실패한 경우 실행을 차단)
        if (!this.isCompiled) {
            System.err.println("컴파일 오류가 발생한 파일은 실행할 수 없습니다. 5. Show Compile Error 에서 오류를 확인하세요");
            return;  // 실행 중단
        }

        try {
            // 패키지 경로가 포함된 클래스 이름 설정 (컴파일된 클래스 파일의 이름에서 .class 확장자 제거)
            String className = file.compiledFileName.replace(".class", "");

            // 패키지 경로가 있을 경우 디렉토리 구분자를 . 으로 변환 (예: IDE/Main -> IDE.Main)
            if (className.contains(File.separator)) {
                className = className.replace(File.separator, ".");
            }

            // 실행할 디렉터리 설정 (컴파일된 클래스 파일이 위치한 디렉터리)
            this.processBuilder.directory(new File(file.fileDirectory));

            // 운영체제에 맞는 명령어 설정
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                this.processBuilder.command("cmd.exe", "/c", "java", className);  // java 명령어로 클래스 실행
            } else {
                this.processBuilder.command("sh", "-c", String.format("java %s", className));  // UNIX 계열
            }

            // 프로세스 시작 및 출력 결과 처리
            Process process = this.processBuilder.start();

            // 표준 출력 읽기
            BufferedReader stdOutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String outputLine;
            while ((outputLine = stdOutReader.readLine()) != null) {
                System.out.println(outputLine);  // 실행 결과 출력
            }

            // 에러 출력 읽기
            BufferedReader stdErrReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String errorLine;
            while ((errorLine = stdErrReader.readLine()) != null) {
                System.err.println(errorLine);  // 에러 출력
            }

            // 프로세스 종료 코드 확인
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("프로그램 실행 중 오류가 발생했습니다. 종료 코드: " + exitCode);
            }

        } catch (Exception e) {
            System.err.println("에러! 프로그램 실행에 실패했습니다: " + e.getMessage());
        }
    }



    /**
     * 컴파일 시 발생한 오류를 출력합니다.
     */
    public void showError() {
        System.out.println(Objects.requireNonNullElse(this.stdErr, "컴파일 오류가 발생하지 않았습니다."));
    }
}