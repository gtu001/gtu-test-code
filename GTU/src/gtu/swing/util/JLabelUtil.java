package gtu.swing.util;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

public class JLabelUtil {

    public static int STAR_MARK_FULLCHAR = 1 << 0;
    public static int STAR_MARK_SIMPLE = 1 << 1;

    public static int FONT_STYLE_Italic = 1 << 2;// 斜體
    public static int FONT_STYLE_Emphasized = 1 << 3;// 斜體
    public static int FONT_STYLE_Small = 1 << 4;// 小
    public static int FONT_STYLE_Subscript = 1 << 5; // 下
    public static int FONT_STYLE_Superscript = 1 << 6;// 上

    public static String getText(String text) {
        return getText(text, FONT_STYLE_Italic | STAR_MARK_FULLCHAR);
    }

    public static String getText(String text, int type) {
        // -------------------------------------------
        String star = "";
        if ((STAR_MARK_FULLCHAR & type) != 0) {
            star = "＊";
        }
        if ((STAR_MARK_SIMPLE & type) != 0) {
            star = "*";
        }

        // -------------------------------------------
        if ((FONT_STYLE_Italic & type) != 0) {
            text = String.format("<i>%s</i> ", text);
        }
        if ((FONT_STYLE_Emphasized & type) != 0) {
            text = String.format("<em>%s</em> ", text);
        }
        if ((FONT_STYLE_Small & type) != 0) {
            text = String.format("<small>%s</small> ", text);
        }
        if ((FONT_STYLE_Subscript & type) != 0) {
            text = String.format("<sub>%s</sub> ", text);
        }
        if ((FONT_STYLE_Superscript & type) != 0) {
            text = String.format("<sup>%s</sup> ", text);
        }

        String format = "<html><font color='red'>%1$s</font>%2$s</html>";
        return String.format(format, star, text);
    }

    /**
     * 建立唯獨但可圈選的JLabel
     */
    public static JTextArea createReadonlyJLabel(boolean isJTextField) {
        JTextArea f = new JTextArea();
        f.setEditable(false); // as before
        f.setBackground(null); // this is the same as a JLabel
        f.setBorder(null); // remove the border

        if (isJTextField) {
            f.setBackground(Color.WHITE);
            f.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
            f.setForeground(Color.GRAY);
        }
        return f;
    }

    public static void main(String[] args) {
        String lblText = JLabelUtil.getText("XXXXXX", //
                STAR_MARK_FULLCHAR | STAR_MARK_SIMPLE);

        JFrame frame = JFrameUtil.createSimpleFrame(JLabelUtil.class);
        JLabel lbl = new JLabel();
        lbl.setText(lblText);
        frame.add(lbl);
        frame.pack();
        gtu.swing.util.JFrameUtil.setVisible(true, frame);

        System.out.println("done...");
    }
}
