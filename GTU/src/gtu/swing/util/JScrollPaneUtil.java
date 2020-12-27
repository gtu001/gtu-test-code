package gtu.swing.util;

import java.awt.Dimension;

import javax.swing.JScrollPane;

public class JScrollPaneUtil {

    public static Dimension getVisibleSize(JScrollPane scrollPane) {
        int height = scrollPane.getViewport().getSize().height;
        int width = scrollPane.getViewport().getSize().width;
        return new Dimension(width, height);
    }
}
