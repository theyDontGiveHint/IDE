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
    public void compile(UploadFile file) {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                this.processBuilder.command("cmd.exe", "/c", "dir", "/?", file.filePath);
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
        } catch (IOException | InterruptedException e) {
            System.err.print(e.getMessage());
        }
    }

    /**
     * 컴파일 된 .class 파일을 실행합니다.
     *
     * @param file 컴파일 된 업로드 파일
     */
    public void run(UploadFile file) {
        try {
            // UploadFile에서 이미 컴파일된 클래스 파일의 이름을 사용
            String className = file.compiledFileName;

            // 실행할 디렉터리 설정 (컴파일된 클래스 파일이 위치한 디렉터리)
            this.processBuilder.directory(new File(file.fileDirectory));

            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                this.processBuilder.command("cmd.exe", "/c", "dir", "/?", className);
            } else {
                this.processBuilder.command("sh", "-c", String.format("javac %s", className));
            }

            Process process = this.processBuilder.start();

            BufferedReader stdOutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String outputLine;
            while ((outputLine = stdOutReader.readLine()) != null) {
                System.out.println(outputLine);
            }

        } catch (Exception e) {
            System.err.print(e.getMessage());
        }
    }

    /**
     * 컴파일 시 발생한 오류를 출력합니다.
     */
    public void showError() {
        System.out.println(Objects.requireNonNullElse(this.stdErr, "컴파일 오류가 발생하지 않았습니다."));
    }
}