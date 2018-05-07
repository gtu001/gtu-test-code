package gtu.swing.util;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class JColorUtil {

    public static Color rgb(int r, int g, int b) {
        float[] arry = Color.RGBtoHSB(r, g, b, null);
        return Color.getHSBColor(arry[0], arry[1], arry[2]);
    }

    public static Color rgb(String colorStr) {
        Pattern ptn = Pattern.compile("^\\#*(\\w+)$");
        Matcher mth = ptn.matcher(colorStr);
        mth.find();
        colorStr = mth.group(1);
        String r_str = StringUtils.substring(colorStr, 0, 2);
        String g_str = StringUtils.substring(colorStr, 2, 4);
        String b_str = StringUtils.substring(colorStr, 4);

        int r = Integer.valueOf(r_str, 16);
        int g = Integer.valueOf(g_str, 16);
        int b = Integer.valueOf(b_str, 16);

        return rgb(r, g, b);
    }
}
