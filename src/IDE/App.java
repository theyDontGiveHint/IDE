package IDE;

import IDE.listener.ActionListener;

import javax.swing.*;
import java.awt.*;

/**
 * IDE App 클래스
 */
public class App {
    private static App instance;
    private UploadFile uploadFile;
    private Controller controller;

    private App() {}

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
    private Object[] createMenuItems() {
        Object[] components = new Object[7];

        // 메뉴 아이템 객체 추가
        String[] menuItemNames = {"Browse", "Open", "Save", "Compile", "Save Errors", "Delete", "Clear"};
        JMenuItem[] menuItems = new JMenuItem[menuItemNames.length];
        for (int i = 0; i < menuItemNames.length; i++) {
            menuItems[i] = new JMenuItem(menuItemNames[i]);
            components[i] = menuItems[i];
        }

        // 텍스트 필드 객체 추가
        this.filePathField = new JTextField(30);

        // 이벤트 추가
        menuItems[0].addActionListener(_ -> actionListener.browseFile(this.filePathField, this));
        menuItems[1].addActionListener(_ -> actionListener.openFile(this.textArea, this.resultTextArea));
        menuItems[2].addActionListener(_ -> actionListener.saveFile(this.filePathField, this.textArea, this.resultTextArea));
        menuItems[3].addActionListener(_ -> actionListener.compileFile(this.resultTextArea));
        menuItems[4].addActionListener(_ -> actionListener.saveErrors(this.resultTextArea));
        menuItems[5].addActionListener(_ -> actionListener.deleteFile(this.filePathField, this.textArea, this.resultTextArea));
        menuItems[6].addActionListener(_ -> actionListener.clear(this.filePathField, this.textArea, this.resultTextArea));

        return components;
    }
}