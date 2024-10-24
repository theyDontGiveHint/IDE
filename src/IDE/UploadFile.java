package IDE;

import java.io.File;

/**
 * 업로드 파일 클래스
 */
public class UploadFile {
    protected UploadFile(String path) {
        File file = new File(path);

        this.fileName = file.getName();
        this.filePath = path;
        this.fileDirectory = file.getParent();

        this.compiledFileName = this.fileName.replace(".java", "");

        File parentDirectory = new File(this.fileDirectory);
        this.packageName = parentDirectory.getName();
        this.packageDirectory = parentDirectory.getParent();
    }

    protected String fileName;
    protected String filePath;
    protected String fileDirectory;
    protected String packageName;
    protected String packageDirectory;
    protected String compiledFileName;
}
