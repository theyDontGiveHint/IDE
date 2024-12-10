package IDE.listener;

import IDE.IDEFile;

import java.io.File;
import java.io.IOException;

import java.awt.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * IDE ActionListener 클래스
 */
public class IDEActionListener extends Component {
    /**
     * 파일 선택창을 통해 사용자가 선택한 .java 파일을 IDEFile 객체로 반환합니다.
     *
     * @param frame      부모 JFrame
     * @param pathField  파일 경로 JTextField
     * @param resultArea 결과 메세지 표시 JTextArea
     * @return 선택 파일의 IDEFile 객체, 선택 취소 시 null
     */
    public IDEFile openFile(JFrame frame, JTextField pathField, JTextArea resultArea) {
        JFileChooser chooser = new JFileChooser();

        FileNameExtensionFilter filter = new FileNameExtensionFilter(".java", "java");
        chooser.setFileFilter(filter);

        int result = chooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            IDEFile openedFile = new IDEFile(file);
            pathField.setText(openedFile.filePath);

            resultArea.setText("파일을 열었습니다.");
            return openedFile;
        } else {
            return null;
        }
    }

    /**
     * 현재 열린 IDEFile에 대해 JTextArea의 내용을 저장합니다.
     *
     * @param file       저장할 IDEFile 객체
     * @param textArea   저장할 내용을 포함한 JTextArea
     * @param resultArea 결과 메세지 표시 JTextArea
     */
    public void saveFile(IDEFile file, JTextArea textArea, JTextArea resultArea) {
        try {
            file.writeContent(textArea.getText());
            resultArea.setText("파일을 저장했습니다.");
        } catch (IOException e) {
            resultArea.setText("파일 저장 중 문제가 발생했습니다.");
        }
    }

    /**
     * 지정된 IDEFile을 컴파일하고 결과를 표시합니다.
     *
     * @param file           컴파일할 IDEFile 객체
     * @param resultTextArea 컴파일 결과를 표시할 JTextArea
     */
    public void compileFile(IDEFile file, JTextArea resultTextArea) {
        try {
            String stdErr = file.compile();
            if (stdErr.isEmpty()) {
                resultTextArea.setText("컴파일에 성공했습니다.");
            } else {
                resultTextArea.setText("컴파일에 실패했습니다.\n" + stdErr);
            }
        } catch (IOException | InterruptedException e) {
            resultTextArea.setText("컴파일 중 문제가 발생했습니다. " + e.getMessage());
        }
    }

    /**
     * 프로그램을 종료합니다.
     */
    public void quit() {
        EventQueue.invokeLater(() -> System.exit(0));
    }
}