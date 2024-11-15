package IDE;

import javax.swing.*;
import java.awt.*;

/**
 * GUI 요소 생성 팩토리 클래스
 */
public class ComponentFactory {
    /**
     * JMenuItem 객체들로 JMenuBar를 생성합니다.
     *
     * @param items JMenuItem 객체 배열
     * @return 생성된 JMenuBar
     */
    public static JMenuBar createMenuBar(Object[] items) {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setLayout(new FlowLayout(FlowLayout.LEFT));

        for (Object item : items) {
            menuBar.add((JMenuItem) item);
        }
        return menuBar;
    }

    /**
     * 상단에 위치하는 JScrollPane을 생성합니다.
     *
     * @param textArea 포함할 JTextArea 객체
     * @return JTextArea가 포함된 JScrollPane
     */
    public static JScrollPane createTopPane(JTextArea textArea) {
        return new JScrollPane(textArea);
    }

    /**
     * 하단에 위치하는 JScrollPane을 생성합니다.
     *
     * @param textArea 포함할 JTextArea 객체
     * @return JTextArea가 포함된 JScrollPane
     */
    public static JScrollPane createBottomPane(JTextArea textArea) {
        return new JScrollPane(textArea);
    }

    /**
     * 상단과 하단을 나누는 JSplitPane을 생성합니다.
     *
     * @param topPane    상단 JScrollPane입니다.
     * @param bottomPane 하단 JScrollPane입니다.
     * @return 생성된 JSplitPane 객체
     */
    public static JSplitPane createSplitPanel(JScrollPane topPane, JScrollPane bottomPane) {
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPane, bottomPane);
        splitPane.setResizeWeight(0.8);
        return splitPane;
    }
}