package IDE;

import IDE.listener.IDEActionListener;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

/**
 * IDE App 클래스
 */
public class App extends JFrame {
    private App() {
        // 창 설정
        setTitle("IDE - They Don't Give Hint");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 800);

        // 상단 경로 Panel 생성 및 컴포넌트 추가
        this.pathField = new JTextField(50);
        this.pathPanel = ComponentFactory.createPathPanel(this.pathField);

        // 메인 컴포넌트 생성 및 초기화
        this.tabArea = ComponentFactory.createTabbedPane();
        this.resultArea = ComponentFactory.createTextArea();

        // 탭 변경 이벤트 연결
        this.tabArea.addChangeListener(_ -> {
            Component selectedComponent = this.tabArea.getSelectedComponent();
            IDEFile currentFile = this.fileMap.get(selectedComponent);
            if (currentFile != null) {
                this.pathField.setText(currentFile.filePath);
            } else {
                this.pathField.setText("");
            }
        });

        // 수평 분할 화면 생성
        JSplitPane splitPane = ComponentFactory.createSplitPanel(this.tabArea, new JScrollPane(this.resultArea));

        // 메인 패널 생성 및 구성
        this.mainPanel = new JPanel(new BorderLayout());
        this.mainPanel.add(this.pathPanel, BorderLayout.NORTH);
        this.mainPanel.add(splitPane, BorderLayout.CENTER);

        // 메인 화면 구성
        setContentPane(mainPanel);

        // 메뉴바 생성
        setJMenuBar(createMenuBar());

        // 단축키 이벤트 초기화
        setUpKeyBindings();
    }

    private static App instance;
    private final IDEActionListener actionListener = new IDEActionListener();

    public JPanel mainPanel;
    public JPanel pathPanel;
    public JTextField pathField;
    public JTabbedPane tabArea;
    public JTextArea resultArea;

    private final Map<Component, IDEFile> fileMap = new HashMap<>();
    private final Map<Component, JTextArea> textAreaMap = new HashMap<>();

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


    private Component getFocusedTabComponent() {
        int index = this.tabArea.getSelectedIndex();
        if (index == -1) {
            return null;
        }
        return this.tabArea.getComponentAt(index);
    }


    private void addFileTab(IDEFile file) {
        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Tab 생성
        this.tabArea.addTab(file.fileName, scrollPane);

        // File 및 Tab 등록
        this.fileMap.put(scrollPane, file);
        this.textAreaMap.put(scrollPane, textArea);

        // Tab 파일 데이터 추가
        try {
            textArea.setText(file.readContent());
            this.resultArea.setText(file.fileName + " 파일을 열었습니다.");
        } catch (IOException e) {
            this.resultArea.setText("파일을 여는 중 문제가 발생했습니다.");
        }

        // 생성한 Tab으로 전환 및 경로 표시
        this.tabArea.setSelectedComponent(scrollPane);
        this.pathField.setText(file.filePath);
    }


    private JMenuBar createMenuBar() {
        String[] fileMenuItemNames = {"Open", "Save", "Save As", "Close", "Quit"};
        JMenu fileMenu = ComponentFactory.createMenu("File", fileMenuItemNames);

        String[] runMenuItemNames = {"Compile"};
        JMenu runMenu = ComponentFactory.createMenu("Run", runMenuItemNames);

        // Open 이벤트 연결
        fileMenu.getItem(0).addActionListener(_ -> {
            IDEFile file = this.actionListener.openFile(this, this.pathField, this.resultArea);
            if (file != null) {
                // 동일 파일 열림 확인
                boolean isOpened = this.fileMap.values().stream().anyMatch(openFile -> openFile.filePath.equals(file.filePath));
                if (isOpened) {
                    this.resultArea.setText("이미 열려있는 파일입니다.");
                } else {
                    addFileTab(file);
                }
            }
        });

        // Save 이벤트 연결
        fileMenu.getItem(1).addActionListener(_ -> {
            checkAndSaveFile();
        });

        // Save As 이벤트 연결
        fileMenu.getItem(2).addActionListener(_ -> {
            Component focusedComponent = getFocusedTabComponent();
            // 탭 오류 감지
            if (focusedComponent == null) {
                this.resultArea.setText("선택된 탭이 없습니다.");
                return;
            }

            IDEFile file = this.fileMap.get(focusedComponent);
            JTextArea currentTextArea = this.textAreaMap.get(focusedComponent);

            // 파일명 입력 후 처리
            String newFileName = JOptionPane.showInputDialog(this, "파일명을 입력하세요.", "다른 이름으로 저장", JOptionPane.PLAIN_MESSAGE);
            if (newFileName == null || newFileName.trim().isEmpty()) {
                this.resultArea.setText("잘못된 파일명입니다.");
                return;
            } else {
                if (!newFileName.endsWith(".java")) {
                    newFileName += ".java";
                }
            }

            // 파일 생성
            File chosenFile = new File(file.fileDirectory, newFileName);

            // 중복 파일 확인
            if (chosenFile.exists()) {
                JOptionPane.showMessageDialog(this, "파일이 이미 존재합니다.", "경고", JOptionPane.WARNING_MESSAGE);
                this.resultArea.setText("파일이 이미 존재합니다.");
                return;
            }

            // 파일 저장 호출
            IDEFile newFile = new IDEFile(chosenFile);
            this.actionListener.saveFile(newFile, currentTextArea, this.resultArea);
        });

        // Close 이벤트 연결
        fileMenu.getItem(3).addActionListener(_ -> {
            Component focusedComponent = getFocusedTabComponent();
            if (focusedComponent == null) {
                this.resultArea.setText("선택된 탭이 없습니다.");
                return;
            }

            int index = this.tabArea.indexOfComponent(focusedComponent);
            if (index != -1) {
                // 탭 제거
                this.tabArea.remove(index);

                // 파일 및 편집창 제거
                this.fileMap.remove(focusedComponent);
                this.textAreaMap.remove(focusedComponent);

                this.resultArea.setText("파일을 닫았습니다.");
            } else {
                this.resultArea.setText("탭을 닫는데 문제가 발생했습니다.");
            }
        });

        // Quit 이벤트 연결
        fileMenu.getItem(4).addActionListener(_ -> this.actionListener.quit());

        // Compile 이벤트 연결
        runMenu.getItem(0).addActionListener(_ -> saveAndCompileFile());

        return ComponentFactory.createMenuBar(new JMenu[]{fileMenu, runMenu});
    }


    private void checkAndSaveFile() {
        Component focusedComponent = getFocusedTabComponent();
        // 탭 오류 감지
        if (focusedComponent == null) {
            this.resultArea.setText("열린 파일이 없습니다.");
            return;
        }

        IDEFile file = this.fileMap.get(focusedComponent);
        JTextArea currentTextArea = this.textAreaMap.get(focusedComponent);

        // 확인 후 저장
        int option = JOptionPane.showConfirmDialog(this, "저장하시겠습니까?", "저장", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            this.actionListener.saveFile(file, currentTextArea, this.resultArea);
        } else {
            this.resultArea.setText("파일 저장을 취소했습니다.");
        }
    }


    private void saveAndCompileFile() {
        Component focusedComponent = getFocusedTabComponent();
        // 탭 오류 감지
        if (focusedComponent == null) {
            this.resultArea.setText("선택된 탭이 없습니다.");
            return;
        }

        // 컴파일 호출
        IDEFile file = this.fileMap.get(focusedComponent);
        this.actionListener.saveFile(file, this.textAreaMap.get(focusedComponent), this.resultArea);
        this.actionListener.compileFile(file, this.resultArea);
    }


    /**
     * 지정한 키에 대해 액션을 바인딩합니다.
     *
     * @param keyStrokeString 키 스트로크를 나타내는 문자열 (예: "ctrl S")
     * @param actionName      액션을 식별하기 위한 이름
     * @param action          키 입력 시 실행될 동작을 정의한 Action 객체
     */
    private void bindKey(String keyStrokeString, String actionName, Action action) {
        KeyStroke keyStroke = KeyStroke.getKeyStroke(keyStrokeString);

        JComponent component = this.getRootPane();
        component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, actionName);
        component.getActionMap().put(actionName, action);
    }


    /**
     * 특정 키에 대해 액션을 연결합니다.
     * 키와 해당 동작을 매핑하는 맵을 생성하고, 이를 bindKey 메서드를 통해 바인딩합니다.
     */
    private void setUpKeyBindings() {
        Map<String, Runnable> keyActionMap = getActionMap();

        // 단축키 이벤트 연결
        keyActionMap.forEach((keyStrokeString, actionRunnable) -> {
            String actionName = keyStrokeString.replaceAll("\\s+", "_");
            bindKey(keyStrokeString, actionName, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    actionRunnable.run();
                }
            });
        });
    }


    private Map<String, Runnable> getActionMap() {
        Map<String, Runnable> keyActionMap = new HashMap<>();

        // Compile 이벤트 생성
        keyActionMap.put("ctrl R", this::saveAndCompileFile);

        // Save 이벤트 생성
        keyActionMap.put("ctrl S", this::checkAndSaveFile);

        // Quit 이벤트 생성
        keyActionMap.put("ctrl Q", this.actionListener::quit);

        return keyActionMap;
    }
}