package IDE;

import javax.swing.*;
import java.awt.*;

/**
 * GUI 컴포넌트를 생성하는 팩토리 클래스입니다.
 */
public class ComponentFactory {
    /**
     * 경로 텍스트 필드를 담는 패널을 생성합니다.
     *
     * @param pathField 경로 표시 JTextField
     * @return 경로 JTextField를 담은 JPanel
     */
    public static JPanel createPathPanel(JTextField pathField) {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(pathField);
        return topPanel;
    }

    /**
     * 메뉴들을 포함하는 메뉴 바를 생성합니다.
     *
     * @param menus JMenu 배열
     * @return 메뉴들이 포함된 JMenuBar
     */
    public static JMenuBar createMenuBar(JMenu[] menus) {
        JMenuBar menuBar = new JMenuBar();
        for (JMenu menu : menus) {
            menuBar.add(menu);
        }
        return menuBar;
    }

    /**
     * 제목과 메뉴 아이템 이름들로 구성된 메뉴를 생성합니다.
     *
     * @param title         메뉴 제목
     * @param menuItemNames 메뉴 아이템 이름 배열
     * @return 메뉴 아이템을 포함한 JMenu
     */
    public static JMenu createMenu(String title, String[] menuItemNames) {
        JMenu menu = new JMenu(title);
        for (String menuItemName : menuItemNames) {
            JMenuItem menuItem = new JMenuItem(menuItemName);
            menu.add(menuItem);
        }
        return menu;
    }

    /**
     * 스크롤이 가능한 탭 패널을 생성합니다.
     *
     * @return 스크롤 가능한 JTabbedPane
     */
    public static JTabbedPane createTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        return tabbedPane;
    }

    /**
     * 수정 불가능한 텍스트 영역을 생성합니다.
     *
     * @return 수정 JTextArea
     */
    public static JTextArea createTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        return textArea;
    }

    /**
     * 상단과 하단 컴포넌트를 나누는 JSplitPane을 생성합니다.
     *
     * @param topPane    상단 컴포넌트
     * @param bottomPane 하단 컴포넌트
     * @return 상단과 하단 컴포넌트를 포함하는 JSplitPane
     */
    public static JSplitPane createSplitPanel(JComponent topPane, JComponent bottomPane) {
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPane, bottomPane);
        splitPane.setResizeWeight(0.8);
        return splitPane;
    }
}