package gtu.swing.util;

import java.awt.Font;

import javax.swing.JComponent;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.MethodUtils;

public class JTextWidthFitter {

    private static final JTextWidthFitter _INST = new JTextWidthFitter();

    public static JTextWidthFitter getInstance() {
        return _INST;
    }

    private JTextWidthFitter() {
    }

    public void setFitTextWidth(JComponent component, String origenText, int fontSize, int boundryWidth) {
        FontWidthHandler handler = new FontWidthHandler(component, origenText, fontSize, boundryWidth);
        handler.apply();
    }

    private class FontWidthHandler {
        private JComponent component;
        private String content;
        private Font font;
        private int fontSize;
        private int boundryWidth;

        private FontWidthHandler(JComponent component, String content, int fontSize, int boundryWidth) {
            content = StringUtils.trimToEmpty(content);
            this.component = component;
            this.content = content;
            this.font = new Font("新細明體", Font.PLAIN, fontSize);
            this.fontSize = fontSize;
            this.boundryWidth = boundryWidth;
        }

        public void apply() {
            for (; getFontMetricsFontWidth(content, fontSize) > boundryWidth;) {
                if (content.length() <= 0) {
                    break;
                }
                content = StringUtils.substring(content, 0, -1);
            }
            try {
                MethodUtils.invokeMethod(this.component, "setText", content);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private int getFontMetricsFontWidth(String content, int fontSize) {
            return component.getFontMetrics(font).stringWidth(content);
        }

        private int getGraphicsFontWidth(String content, int fontSize) {
            return component.getGraphics().getFontMetrics(font).stringWidth(content);
        }
    }
}
