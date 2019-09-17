package gtu.swing.util;

import javax.swing.JTabbedPane;

public class JTabbedPaneUtil {

    private JTabbedPane tabbedPane;

    private JTabbedPaneUtil(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }

    public static JTabbedPaneUtil newInst(JTabbedPane tabbedPane) {
        return new JTabbedPaneUtil(tabbedPane);
    }

    public boolean isSelectedTitle(String title, boolean ignoreCase) {
        return getTitleIndex(title, ignoreCase) == tabbedPane.getSelectedIndex();
    }

    public int getTitleIndex(String title, boolean ignoreCase) {
        title = title != null ? title.trim() : "";
        for (int ii = 0; ii < tabbedPane.getTabCount(); ii++) {
            if (ignoreCase && title.equalsIgnoreCase(tabbedPane.getTitleAt(ii))) {
                return ii;
            } else if (!ignoreCase && title.equals(tabbedPane.getTitleAt(ii))) {
                return ii;
            }
        }
        return -1;
    }

    public void setSelectedIndexByTitle(String title) {
        int index = this.getTitleIndex(title);
        if (index != -1) {
            tabbedPane.setSelectedIndex(index);
        } else {
            throw new RuntimeException("找不到title:" + title);
        }
    }
}
