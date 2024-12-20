package IDE;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * JAVA 파일 클래스
 */
public class IDEFile {
    /**
     * 주어진 File 객체를 기반으로 IDEFile 인스턴스를 생성합니다.
     *
     * @param file IDE에서 관리하는 실제 파일 객체
     */
    public IDEFile(File file) {
        this.filePath = file.getAbsolutePath();
        this.fileDirectory = file.getParent();
        this.fileName = file.getName();
    }

    public String filePath;
    public String fileDirectory;
    public String fileName;

    /**
     * 자바 파일의 내용을 읽습니다.
     *
     * @return 파일 내용
     * @throws IOException Files 발생 예외
     */
    public String readContent() throws IOException {
        return new String(Files.readAllBytes(Paths.get(this.filePath)));
    }

    /**
     * 수정된 내용을 저장합니다.
     *
     * @param content 저장할 내용
     * @throws IOException FileWriter 발생 예외
     */
    public void writeContent(String content) throws IOException {
        try (FileWriter fileWriter = new FileWriter(this.filePath)) {
            fileWriter.write(content);
        }
    }

    /**
     * 컴파일 진행 후 결과를 반환합니다.
     *
     * @return 오류 출력
     * @throws IOException          StreamBuilder 발생 예외
     * @throws InterruptedException StreamBuilder 발생 예외
     */
    public String compile() throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(this.fileDirectory));
        processBuilder.command("javac", this.filePath);

        Process process;
        try {
            process = processBuilder.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            throw e;
        }

        try (BufferedReader stdErrReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            StringBuilder stdErrBuilder = new StringBuilder();

            String line;
            while ((line = stdErrReader.readLine()) != null) {
                stdErrBuilder.append(line).append("\n");
            }

            return stdErrBuilder.toString();
        } catch (IOException e) {
            throw e;
        }
    }

    public String run() throws IOException, InterruptedException {
        String classFileName = this.fileName.replace(".java", ".class");
        File classFile = new File(this.fileDirectory, classFileName);
        if (!classFile.exists()) {
            throw new FileNotFoundException("파일이 컴파일 되지 않았습니다.");
        }

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(this.fileDirectory));
        processBuilder.command("java", this.fileName.replace(".java", ""));

        Process process;
        try {
            process = processBuilder.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            throw e;
        }

        try (BufferedReader stdOutReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            StringBuilder stdOutBuilder = new StringBuilder();

            String line;
            while ((line = stdOutReader.readLine()) != null) {
                stdOutBuilder.append(line).append("\n");
            }

            return stdOutBuilder.toString();
        } catch (IOException e) {
            throw e;
        }
    }
}