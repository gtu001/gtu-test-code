package gtu.swing.util;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;

public class TextAreaTabUtil {

    Map<String, JPanel> panelMap = new HashMap<String, JPanel>();
    final JTabbedPane jTabbedPane1;

    public static TextAreaTabUtil newInstance(JTabbedPane jTabbedPane1) {
        return new TextAreaTabUtil(jTabbedPane1);
    }

    private TextAreaTabUtil(JTabbedPane jTabbedPane1) {
        this.jTabbedPane1 = jTabbedPane1;
    }

    public TextAreaTabUtil removeAllPanel() {
        for (String key : panelMap.keySet()) {
            jTabbedPane1.remove(panelMap.get(key));
        }
        return this;
    }

    public TextAreaTabUtil addPanel(String panelName) {
        JPanel jPanel2 = new JPanel();
        BorderLayout jPanel2Layout = new BorderLayout();
        jPanel2.setLayout(jPanel2Layout);
        jTabbedPane1.addTab(panelName, null, jPanel2, null);
        {
            JScrollPane jScrollPane1 = new JScrollPane();
            jPanel2.add(jScrollPane1, BorderLayout.CENTER);
            JTextArea textArea = new JTextArea();
            jScrollPane1.setViewportView(textArea);
        }
        panelMap.put(panelName, jPanel2);
        return this;
    }

    public JTextArea getTextArea(String panelName) {
        JPanel panel = panelMap.get(panelName);
        JScrollPane scroll = (JScrollPane) panel.getComponent(0);
        JViewport viewport = (JViewport) scroll.getComponent(0);
        JTextArea area = (JTextArea) viewport.getComponent(0);
        return area;
    }
}