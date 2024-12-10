package IDE.listener;

import IDE.IDEFile;

import java.io.File;
import java.io.IOException;

import java.awt.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class IDEActionListener extends Component {
    public IDEFile openFile(JFrame frame, JTextField pathField,  JTextArea resultArea) {
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


    public void saveFile(IDEFile file, JTextArea textArea, JTextArea resultArea) {
        try {
            file.writeContent(textArea.getText());
            resultArea.setText("파일을 저장했습니다.");
        } catch (IOException e) {
            resultArea.setText("파일 저장 중 문제가 발생했습니다.");
        }
    }


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


    public void quit() {
        EventQueue.invokeLater(() -> System.exit(0));
    }
}