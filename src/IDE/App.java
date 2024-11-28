package IDE;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

/**
 * IDE App 클래스
 */
public class App extends JFrame {
    // 생성자
    private App() {
        setTitle("They Don't Give Hint");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 800);

        this.filePathField = new JTextField(30);

        // 메뉴바 생성
        setJMenuBar(createMenuBar());

        // 텍스트 필드 포함 패널 생성
        JPanel textFieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        textFieldPanel.add(this.filePathField);

        // 요소 생성 및 초기화
        this.textArea = new JTextArea();
        this.resultTextArea = new JTextArea();
        this.resultTextArea.setEditable(false);

        // 분할 구역 생성
        JScrollPane topPane = ComponentFactory.createTopPane(this.textArea);
        JScrollPane bottomPane = ComponentFactory.createBottomPane(this.resultTextArea);

        // 수평 분할 화면 생성
        JSplitPane splitPane = ComponentFactory.createSplitPanel(topPane, bottomPane);

        // 메인 패널 생성 및 구성
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(textFieldPanel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        setContentPane(mainPanel);

        // Ctrl+R 단축키로 컴파일 실행
        setUpKeyBindings();
    }

    /**
     * 싱글톤 인스턴스를 반환하는 메소드입니다.
     *
     * @return App 인스턴스
     */
    public static App getInstance() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }

    /**
     * IDE 메인 실행 메소드
     */
    public void run() {
        setVisible(true);
    }

    private static App instance;
    private final ActionListener actionListener = new ActionListener();
    public JTextField filePathField;
    public JTextArea textArea;
    public JTextArea resultTextArea;

    /**
     * 메뉴 항목을 생성하고 반환합니다.
     *
     * @return 생성된 메뉴 항목 배열
     */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar(); // 메뉴바 생성

        // File 메뉴 생성
        JMenu fileMenu = new JMenu("File");
        String[] fileMenuItemNames = {"Browse", "Open", "Save", "Save As", "Delete", "Clear", "Quit"};
        JMenuItem[] fileMenuItems = new JMenuItem[fileMenuItemNames.length];

        for (int i = 0; i < fileMenuItemNames.length; i++) {
            fileMenuItems[i] = new JMenuItem(fileMenuItemNames[i]); // 메뉴 아이템 생성
            fileMenu.add(fileMenuItems[i]); // File 메뉴에 추가
        }

        // File 메뉴 아이템 이벤트 추가
        fileMenuItems[0].addActionListener(e -> actionListener.browseFile(this.filePathField, this));
        fileMenuItems[1].addActionListener(e -> actionListener.openFile(this.textArea, this.resultTextArea));
        fileMenuItems[2].addActionListener(e -> actionListener.saveFile(this.filePathField, this.textArea, this.resultTextArea));
        fileMenuItems[3].addActionListener(e -> actionListener.saveAs(this.filePathField, this.textArea, this.resultTextArea));
        fileMenuItems[4].addActionListener(e -> actionListener.deleteFile(this.filePathField, this.textArea, this.resultTextArea));
        fileMenuItems[5].addActionListener(e -> actionListener.clear(this.filePathField, this.textArea, this.resultTextArea));
        fileMenuItems[6].addActionListener(e -> actionListener.Quit());

        // Run 메뉴 생성
        JMenu runMenu = new JMenu("Run");
        JMenuItem compileMenuItem = new JMenuItem("Compile");
        compileMenuItem.addActionListener(e -> actionListener.compileFile(this.resultTextArea)); // Compile 이벤트 추가
        runMenu.add(compileMenuItem); // Run 메뉴에 Compile 추가

        // 메뉴바에 File과 Run 메뉴 추가
        menuBar.add(fileMenu);
        menuBar.add(runMenu);

        return menuBar; // 메뉴바 반환
    }

    /**
     * Ctrl+R 단축키로 컴파일을 실행하는 메소드
     */
    private void setUpKeyBindings() {
        // Ctrl+R 단축키로 컴파일 실행
        KeyStroke compileKeyStroke = KeyStroke.getKeyStroke("control R");
        textArea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(compileKeyStroke, "compile");
        textArea.getActionMap().put("compile", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionListener.compileFile(resultTextArea);
            }
        });
    }
}
