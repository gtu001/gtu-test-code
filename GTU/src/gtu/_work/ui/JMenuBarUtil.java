package gtu._work.ui;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import gtu.swing.util.JCommonUtil;

public class JMenuBarUtil {

    private JMenuBar jMenuBar = new JMenuBar();
    private List<JMenu> items = new ArrayList<JMenu>();

    public static class JMenuAppender {
        private JMenu menu = new JMenu();

        public static JMenuAppender newInstance(String text) {
            return new JMenuAppender(text);
        }

        private JMenuAppender(String text) {
            menu.setText(text);
        }

        public JMenuAppender addMenuItem(String text, ActionListener actionListener) {
            JMenuItem m1 = new JMenuItem();
            m1.setText(text);
            if (actionListener != null) {
                m1.addActionListener(actionListener);
            }
            menu.add(m1);
            return this;
        }

        public JMenuAppender addChildrenMenu(JMenu menuChild) {
            menu.add(menuChild);
            return this;
        }

        public JMenuAppender addSeparator() {
            menu.addSeparator();
            return this;
        }

        public JMenu getMenu() {
            return menu;
        }
    }

    private JMenuBarUtil() {
    }

    public static JMenuBarUtil newInstance() {
        return new JMenuBarUtil();
    }

    public JMenuBarUtil addMenu(JMenu menu) {
        items.add(menu);
        return this;
    }

    public void apply(JFrame jframe) {
        for (JMenu m : items) {
            jMenuBar.add(m);
        }
        jframe.setJMenuBar(jMenuBar);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();

        JMenu menu1 = JMenuAppender.newInstance("test2").addMenuItem("3333", null).addMenuItem("4444", null).getMenu();
        JMenu menu2 = JMenuAppender.newInstance("test1").addMenuItem("1111", null).addMenuItem("22222", null).addChildrenMenu(menu1).getMenu();

        JMenuBarUtil.newInstance().addMenu(menu2).apply(frame);
        
        JCommonUtil.setJFrameCenter(frame);
        frame.pack();
         gtu.swing.util.JFrameUtil.setVisible(true,frame);
    }
}
