package IDE;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;

public class JavaEditor extends JFrame {
    private JTextArea editingWindow;
    private JTextArea resultWindow;
    private JTextField fileNameField;

    public JavaEditor() {
        setTitle("Java Editor");
        createMenu(); // 메뉴 생성, 프레임에 삽입
        setSize(1100, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 상단 파일 이름 입력 필드 패널
        JPanel topPanel = new JPanel();
        fileNameField = new JTextField(20);
        topPanel.add(new JLabel("File Name:"));
        topPanel.add(fileNameField);
        add(topPanel, BorderLayout.NORTH);

        // Editing Window with scroll bars
        editingWindow = new JTextArea();
        editingWindow.setBorder(BorderFactory.createTitledBorder("Editing Window"));
        JScrollPane editingScrollPane = new JScrollPane(editingWindow);

        // Result Window with scroll bars
        resultWindow = new JTextArea();
        resultWindow.setEditable(false);
        resultWindow.setBorder(BorderFactory.createTitledBorder("Result Window"));
        JScrollPane resultScrollPane = new JScrollPane(resultWindow);

        // JSplitPane을 사용하여 에디팅 창과 결과 창을 분할
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, editingScrollPane, resultScrollPane);
        splitPane.setResizeWeight(0.7);  // 상단 창에 70%의 공간을 할당
        add(splitPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void createMenu() {
        JMenuBar mb = new JMenuBar();
        mb.setLayout(new FlowLayout(FlowLayout.LEFT));
        String[] itemTitle = {"Browse", "Open", "Compile", "Save", "SaveError", "Delete", "Clear"};
        JMenuItem[] menuItems = new JMenuItem[itemTitle.length];

        MenuActionListener listener = new MenuActionListener();
        for (int i = 0; i < menuItems.length; i++) {
            menuItems[i] = new JMenuItem(itemTitle[i]);
            menuItems[i].addActionListener(listener);
            mb.add(menuItems[i]);
        }

        setJMenuBar(mb);
    }

    class MenuActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            switch (cmd) {
                case "Browse":
                    JFileChooser fileChooser = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("Java and Text Files", "java", "txt");
                    fileChooser.setFileFilter(filter);
                    int result = fileChooser.showOpenDialog(JavaEditor.this);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        fileNameField.setText(file.getAbsolutePath());
                    }
                    break;

                case "Open":
                    String fileName = fileNameField.getText();
                    if (fileName.isEmpty()) {
                        resultWindow.setText("Please enter a file name.");
                        return;
                    }

                    File file = new File(fileName);
                    if (file.exists()) {
                        try {
                            editingWindow.setText(new String(Files.readAllBytes(file.toPath())));
                            resultWindow.setText("File opened successfully.");
                        } catch (IOException ex) {
                            resultWindow.setText("Error reading file: " + ex.getMessage());
                        }
                    } else {
                        resultWindow.setText("File does not exist.");
                    }
                    break;

                case "Compile":
                    fileName = fileNameField.getText();
                    if (fileName.isEmpty()) {
                        resultWindow.setText("Please enter a file name.");
                        return;
                    }

                    try {
                        ProcessBuilder processBuilder = new ProcessBuilder("javac", fileName);
                        processBuilder.redirectErrorStream(true);
                        Process process = processBuilder.start();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        StringBuilder output = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            output.append(line).append("\n");
                        }

                        process.waitFor();

                        if (output.length() > 0) {
                            resultWindow.setText(output.toString());
                        } else {
                            resultWindow.setText("Compilation successful.");
                        }
                    } catch (Exception ex) {
                        resultWindow.setText("Compilation error: " + ex.getMessage());
                    }
                    break;

                case "Save":
                    fileName = fileNameField.getText();
                    if (fileName.isEmpty()) {
                        fileName = "Untitled.java";  // Default name if none provided
                    }

                    file = new File(fileName);
                    try (FileWriter writer = new FileWriter(file)) {
                        writer.write(editingWindow.getText());
                        resultWindow.setText("File saved successfully.");
                    } catch (IOException ex) {
                        resultWindow.setText("Error saving file: " + ex.getMessage());
                    }
                    break;

                case "SaveError":
                    fileName = fileNameField.getText();

                    if (fileName.isEmpty()) {
                        resultWindow.setText("Please enter a file name.");
                        return;
                    }

                    String errorText = resultWindow.getText();

                    if (errorText == null || errorText.trim().isEmpty() || errorText.contains("Compilation successful")) {
                        resultWindow.setText("No errors to save.");
                        return;
                    }

                    String errorFileName = fileName + ".error";
                    try (FileWriter writer = new FileWriter(errorFileName)) {
                        writer.write(errorText);
                        resultWindow.setText("Errors saved to " + errorFileName);
                    } catch (IOException ex) {
                        resultWindow.setText("Error saving errors: " + ex.getMessage());
                    }
                    break;

                case "Delete":
                    fileName = fileNameField.getText();
                    if (fileName.isEmpty()) {
                        resultWindow.setText("Please enter a file name.");
                        return;
                    }

                    file = new File(fileName);
                    if (file.delete()) {
                        resultWindow.setText("File deleted successfully.");
                    } else {
                        resultWindow.setText("Error deleting file.");
                    }
                    break;

                case "Clear":
                    editingWindow.setText("");
                    resultWindow.setText("");
                    fileNameField.setText("");
                    break;
            }
        }
    }

    public static void main(String[] args) {
        new JavaEditor();
    }
}