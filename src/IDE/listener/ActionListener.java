package IDE;

import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ActionListener extends Component {
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

    public void saveAs(JTextField filePathField, JTextArea textArea, JTextArea resultTextArea) {
        // 저장할 파일 경로를 선택하는 JFileChooser
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("파일 저장");  // 다이얼로그 제목 설정
        int result = fileChooser.showSaveDialog(null);  // 저장 다이얼로그 표시

        // 사용자가 OK를 클릭했는지 확인
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();  // 선택한 파일 경로

            // 선택한 파일이 이미 존재하는지 확인
            if (file.exists()) {
                // 파일 덮어쓸지 묻는 확인 메시지
                int confirm = JOptionPane.showConfirmDialog(null,
                        "파일이 이미 존재합니다. 덮어쓰시겠습니까?", "파일 덮어쓰기 확인",
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                // 사용자가 덮어쓰기를 선택하면 저장
                if (confirm == JOptionPane.NO_OPTION) {
                    resultTextArea.setText("파일 덮어쓰기를 취소했습니다.");
                    return;  // 덮어쓰기를 취소하면 메서드 종료
                }
            }

            // 파일을 저장할지 묻는 확인 메시지
            int confirm = JOptionPane.showConfirmDialog(null,
                    "파일을 저장하시겠습니까?", "파일 저장 확인",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

            // 사용자가 OK를 클릭하면 파일을 저장
            if (confirm == JOptionPane.OK_OPTION) {
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(textArea.getText());  // textArea의 내용을 파일에 저장
                    resultTextArea.setText(file.getName() + " 파일을 저장했습니다.");
                    filePathField.setText(file.getAbsolutePath());  // 저장된 파일 경로를 JTextField에 표시
                } catch (IOException e) {
                    resultTextArea.setText("파일 저장 중 문제가 발생했습니다: " + e.getMessage());
                }
            } else {
                resultTextArea.setText("파일 저장을 취소했습니다.");
            }
        } else {
            resultTextArea.setText("파일 저장을 취소했습니다.");
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

    public void saveFile(JTextField filePathField, JTextArea textArea, JTextArea resultTextArea) {
        // 파일 경로가 없거나 uploadFile이 null이면 경고 메시지 표시
        if (this.uploadFile == null || this.uploadFile.filePath == null) {
            resultTextArea.setText("파일을 선택하여 저장해 주세요.");
            return;
        }

        // 현재 파일 경로에 덮어쓰기를 진행
        try (FileWriter writer = new FileWriter(this.uploadFile.filePath)) {
            writer.write(textArea.getText());  // textArea의 내용을 파일에 저장
            resultTextArea.setText("파일을 저장했습니다: ");
            filePathField.setText(this.uploadFile.filePath);  // 저장된 파일 경로를 JTextField에 표시
        } catch (IOException e) {
            resultTextArea.setText("파일 저장 중 문제가 발생했습니다: " + e.getMessage());
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

    public void Quit(){
        EventQueue.invokeLater(() -> System.exit(0));
    }
}