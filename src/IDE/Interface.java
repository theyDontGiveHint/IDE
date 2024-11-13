package IDE;

import javax.swing.*;
import java.awt.*;

public class Interface extends JFrame {
    private JButton browseButton = new JButton("Browse");
    private JButton openButton = new JButton("Open");
    private JButton compileButton = new JButton("Compile");
    private JButton saveButton = new JButton("Save");
    private JButton saveErrorsButton = new JButton("Save Errors");
    private JButton deleteButton = new JButton("Delete");
    private JButton clearButton = new JButton("Clear");

    public Interface() {
        setTitle("Java Editor");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        return button;
    }
}