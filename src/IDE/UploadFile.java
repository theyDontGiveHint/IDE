package IDE;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 업로드 파일 클래스
 */
public class UploadFile {
    public UploadFile(String path) {
        File file = new File(path);

        this.filePath = path;
        this.fileDirectory = file.getParent();

        this.isOpened = false;
    }

    public String filePath;
    public String fileDirectory;
    public boolean isOpened;

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
     * 파일 경로가 존재하는지 확인합니다.
     *
     * @return 존재 여부
     */
    public boolean exists() {
        return Files.exists(Paths.get(this.filePath));
    }

    /**
     * 파일을 삭제합니다.
     *
     * @return 파일을 삭제합니다.
     */
    public boolean delete() {
        File file = new File(this.filePath);
        return file.delete();
    }

    /**
     * 컴파일 진행 후 결과를 반환합니다.
     *
     * @return 오류 출력
     * @throws IOException StreamBuilder 발생 예외
     * @throws InterruptedException StreamBuilder 발생 예외
     */
    public String compile() throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(this.fileDirectory));
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            processBuilder.command("javac", this.filePath);
        } else {
            processBuilder.command("javac", this.filePath);
        }

        Process process = processBuilder.start();
        process.waitFor();

        BufferedReader stdErrReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        StringBuilder stdErrBuilder = new StringBuilder();

        String line;
        while ((line = stdErrReader.readLine()) != null) {
            stdErrBuilder.append(line).append("\n");
        }

        return stdErrBuilder.toString();
    }

    /**
     * 오류를 파일로 저장합니다.
     *
     * @param error 오류 내용
     * @throws IOException FileWriter 발생 예외
     */
    public void saveErrors(String error) throws IOException {
        String errorFilePath = this.filePath + ".error";
        try (FileWriter fileWriter = new FileWriter(errorFilePath)) {
            fileWriter.write(error);
        }
    }
}