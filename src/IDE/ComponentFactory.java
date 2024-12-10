package IDE;

import javax.swing.*;
import java.awt.*;

/**
 * GUI 요소 생성 팩토리 클래스
 */
public class ComponentFactory {
    public static JPanel createPathPanel(JTextField pathField) {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(pathField);
        return topPanel;
    }


    public static JMenuBar createMenuBar(JMenu[] menus) {
        JMenuBar menuBar = new JMenuBar();
        for (JMenu menu : menus) {
            menuBar.add(menu);
        }
        return menuBar;
    }


    public static JMenu createMenu(String title, String[] menuItemNames) {
        JMenu menu = new JMenu(title);
        for (String menuItemName : menuItemNames) {
            JMenuItem menuItem = new JMenuItem(menuItemName);
            menu.add(menuItem);
        }
        return menu;
    }


    public static JTabbedPane createTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        return tabbedPane;
    }


    public static JTextArea createTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        return textArea;
    }


    public static JSplitPane createSplitPanel(JComponent topPane, JComponent bottomPane) {
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPane, bottomPane);
        splitPane.setResizeWeight(0.8);
        return splitPane;
    }
}