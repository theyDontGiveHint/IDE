package IDE.listener;

import IDE.UploadFile;

import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ActionListener {
    private String stdErr;
    private UploadFile uploadFile;

    /**
     * JFileChooser를 통해 파일을 선택하고 경로를 JTextField에 표시합니다.
     *
     * @param filePathField 파일 경로 표시 JTextField
     * @param frame 최상위 JFrame
     */
    public void browseFile(JTextField filePathField, JFrame frame) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(".java", "java");
        chooser.setFileFilter(filter);
        int result = chooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            filePathField.setText(file.getAbsolutePath());
            this.uploadFile = new UploadFile(file.getAbsolutePath());
        }
    }

    /**
     * 선택된 파일을 열고 내용을 JTextArea에 표시합니다.
     *
     * @param textArea 파일 내용 표시 JTextArea
     * @param resultTextArea 작업 결과 표시 JTextArea
     */
    public void openFile(JTextArea textArea, JTextArea resultTextArea) {
        if (this.uploadFile == null) {
            resultTextArea.setText("파일을 선택해 주세요.");
            return;
        }

        if (this.uploadFile.exists()) {
            try {
                textArea.setText(this.uploadFile.readContent());
                this.uploadFile.isOpened = true;
                resultTextArea.setText("파일을 열었습니다.");
            } catch (IOException e) {
                resultTextArea.setText("파일을 여는데 문제가 발생했습니다. " + e.getMessage());
            }
        } else {
            resultTextArea.setText("파일이 존재하지 않습니다.");
        }
    }

    /**
     * 파일 내용을 저장합니다. 파일이 없을 경우 새 파일을 생성합니다.
     *
     * @param filePathField 파일 경로 표시 JTextField
     * @param textArea 파일 내용 표시 JTextArea
     * @param resultTextArea 작업 결과 표시 JTextArea
     */
    public void saveFile(JTextField filePathField, JTextArea textArea, JTextArea resultTextArea) {
        if (this.uploadFile == null) {
            String fileName = filePathField.getText();
            this.uploadFile = new UploadFile(System.getProperty("user.dir") + File.separator + fileName);
            this.uploadFile.isOpened = true;
            filePathField.setText(this.uploadFile.filePath);
        } else if (!this.uploadFile.isOpened) {
            resultTextArea.setText("파일을 열어주세요.");
            return;
        }

        try {
            this.uploadFile.writeContent(textArea.getText());
            resultTextArea.setText("파일을 저장했습니다.");
        } catch (IOException e) {
            resultTextArea.setText("파일 저장 중 문제가 발생했습니다. " + e.getMessage());
        }
    }

    /**
     * 선택된 파일을 컴파일합니다.
     *
     * @param resultTextArea 작업 결과 표시 JTextArea
     */
    public void compileFile(JTextArea resultTextArea) {
        if (this.uploadFile == null) {
            resultTextArea.setText("파일을 선택해 주세요.");
            return;
        }

        try {
            this.stdErr = this.uploadFile.compile();
            if (this.stdErr.isEmpty()) {
                resultTextArea.setText("컴파일에 성공했습니다.");
            } else {
                resultTextArea.setText("컴파일에 실패했습니다.\n" + this.stdErr);
            }
        } catch (IOException | InterruptedException e) {
            resultTextArea.setText("컴파일 중 문제가 발생했습니다. " + e.getMessage());
        }
    }

    /**
     * 컴파일 오류를 파일로 저장합니다.
     *
     * @param resultTextArea 작업 결과 표시 JTextArea
     */
    public void saveErrors(JTextArea resultTextArea) {
        if (this.stdErr == null || this.stdErr.isEmpty()) {
            resultTextArea.setText("컴파일 오류가 없습니다.");
            return;
        }

        try {
            this.uploadFile.saveErrors(this.stdErr);
            resultTextArea.setText("오류를 저장했습니다.");
        } catch (IOException e) {
            resultTextArea.setText("오류 저장 중 문제가 발생했습니다." + e.getMessage());
        }
    }

    /**
     * 선택된 파일을 삭제합니다.
     *
     * @param filePathField 파일 경로 표시 JTextField
     * @param textArea 파일 내용 표시 JTextArea
     * @param resultTextArea 작업 결과 표시 JTextArea
     */
    public void deleteFile(JTextField filePathField, JTextArea textArea, JTextArea resultTextArea) {
        if (this.uploadFile == null) {
            resultTextArea.setText("파일을 선택해 주세요.");
            return;
        }

        if (this.uploadFile.delete()) {
            resultTextArea.setText("파일을 삭제했습니다.");
            filePathField.setText("");
            textArea.setText("");
            this.uploadFile = null;
        } else {
            resultTextArea.setText("파일 삭제 중 문제가 발생했습니다.");
        }
    }

    /**
     * 모든 필드를 초기화합니다.
     *
     * @param filePathField 파일 경로 표시 JTextField
     * @param textArea 파일 내용 표시 JTextArea
     * @param resultTextArea 작업 결과 표시 JTextArea
     */
    public void clear(JTextField filePathField, JTextArea textArea, JTextArea resultTextArea) {
        filePathField.setText("");
        textArea.setText("");
        resultTextArea.setText("");
        this.uploadFile = null;
    }
}