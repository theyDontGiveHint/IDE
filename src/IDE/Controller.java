package IDE;

import java.io.*;
import java.util.Objects;

/**
 * ProcessBuilder 명령 제어 클래스
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
     * @exception IOException StreamBuilder 발생 예외
     * @exception InterruptedException StreamBuilder 발생 예외
     */
    public void compile(UploadFile file) throws IOException, InterruptedException {
        try {
            this.processBuilder.directory(new File(file.fileDirectory));
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                this.processBuilder.command("cmd.exe", "/c", "javac *.java");
            } else {
                this.processBuilder.command("sh", "-c", "javac *.java");
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
            if (this.stdErr.isEmpty()) {
                this.isCompiled = true;
                Interface.clearScreen();
                System.out.println("컴파일에 성공했습니다.\n");
            } else {
                this.isError = true;
                Interface.clearScreen();
                System.out.println("컴파일에 실패했습니다.\nShow Compile Error에서 문제를 확인해 주세요.\n");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 컴파일 된 .class 파일을 실행합니다.
     *
     * @param file 업로드 파일
     * @exception IOException StreamBuilder 발생 예외
     * @exception InterruptedException StreamBuilder 발생 예외
     */
    public void run(UploadFile file) throws IOException, InterruptedException {
        if (this.isError) {
            System.out.println("컴파일에 실패했습니다.\nShow Compile Error에서 문제를 확인해 주세요.");
            return;
        }

        try {
            this.processBuilder.directory(new File(file.packageDirectory));
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                if (Objects.equals(file.packageName, "src")) {
                    this.processBuilder.directory(new File(file.fileDirectory));
                    this.processBuilder.command("cmd.exe", "/c", String.format("java %s", file.compiledFileName));
                } else {
                    this.processBuilder.command("cmd.exe", "/c", String.format("java %s.%s", file.packageName, file.compiledFileName));
                }
            } else {
                if (Objects.equals(file.packageName, "src")) {
                    this.processBuilder.directory(new File(file.fileDirectory));
                    this.processBuilder.command("sh", "-c", String.format("java %s", file.compiledFileName));
                } else {
                    this.processBuilder.command("sh", "-c", String.format("java %s.%s", file.packageName, file.compiledFileName));
                }
            }

            Process process = this.processBuilder.start();
            process.waitFor();

            BufferedReader stdOutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder stdOutBuilder = new StringBuilder();

            String line;
            while ((line = stdOutReader.readLine()) != null) {
                stdOutBuilder.append(line).append("\n");
            }

            Interface.clearScreen();
            this.stdOut = stdOutBuilder.toString();
            System.out.println(this.stdOut);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 컴파일 시 발생한 오류를 출력합니다.
     */
    public void showError() {
        if (this.isError) {
            System.out.println(this.stdErr + "\n");
        } else {
            System.out.println("컴파일 오류가 발생하지 않았습니다.\n");
        }
    }
}