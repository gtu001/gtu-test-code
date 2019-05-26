package gtu.swing.util;

import java.awt.Dimension;
import java.awt.MouseInfo;

import javax.swing.JToolTip;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

public class JTooltipUtil {

    public static String escapeHtml(String orignText) {
        String tmpStr = StringUtils.defaultString(orignText);
        tmpStr = StringEscapeUtils.escapeHtml4(tmpStr);
        tmpStr = tmpStr.replaceAll(" ", "&nbsp;");
        tmpStr = tmpStr.replaceAll("    ", "&nbsp;&nbsp;&nbsp;&nbsp;");
        return StringUtils.join(tmpStr.split("\n"), "<br/>");
    }

    public static JToolTip createToolTip(Float widthPercent, Float heightPercent) {
        if (widthPercent == null || widthPercent > 1) {
            widthPercent = 0.7F;
        }
        if (heightPercent == null || heightPercent > 1) {
            heightPercent = 0.4F;
        }
        final java.awt.Dimension scr_size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        final Dimension maxBound = new Dimension((int) (scr_size.width * widthPercent), (int) (scr_size.height * heightPercent));
        JToolTip t = new JToolTip() {
            @Override
            public Dimension getPreferredSize() {
                int mouseRelativeWidth = scr_size.width - MouseInfo.getPointerInfo().getLocation().x;
                
                Dimension d = super.getPreferredSize();
                int newWidth = d.width;
                int newHeight = d.height;
                
                if (d.width > maxBound.width) {
                    newWidth = maxBound.width;
                }
                if(newWidth > mouseRelativeWidth) {
                    newWidth = mouseRelativeWidth;
                }
                if (d.height > maxBound.height) {
                    newHeight = maxBound.height;
                }
                return new Dimension(newWidth, newHeight);
            }
        };
        return t;
    }
}
