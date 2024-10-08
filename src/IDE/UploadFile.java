package IDE;

import java.io.File;

public class UploadFile {
    protected UploadFile(String path) {
        File file = new File(path);

        this.fileName = file.getName();
        this.filePath = path;
        this.fileDirectory = file.getParent();

        this.compiledFileName = this.fileName.replace(".java", ".class");
        this.compiledFilePath = this.filePath.replace(".java", ".class");
    }

    protected String fileName;
    protected String filePath;
    protected String fileDirectory;
    protected String compiledFileName;
    protected String compiledFilePath;
}
