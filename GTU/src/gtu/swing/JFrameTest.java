package gtu.swing;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import gtu.swing.util.JCommonUtil;

public class JFrameTest {

    public static void main(String args[]) {
        JFrameTest test = new JFrameTest();
        JTextArea textArea = new JTextArea();

        JCommonUtil.defaultToolTipDelay();

        JFrameTest.simpleTestComponent(textArea);
    }

    public static JFrame simpleTestComponent(JComponent comp) {
        JFrame jframe = new JFrame("測試元件");
        jframe.setSize(500, 300);// 寬 高
        jframe.setLocationRelativeTo(null);
        jframe.setLayout(new BorderLayout());
        jframe.add(JCommonUtil.createScrollComponent(comp), BorderLayout.CENTER);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setVisible(true);
        return jframe;
    }
}
