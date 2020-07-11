package gtu.swing.util;

import java.awt.Color;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class JColorUtil {

    public static Color randomColor() {
        int[] vals = new int[3];
        for (int ii = 0; ii < vals.length; ii++) {
            vals[ii] = new Random().nextInt(256);
        }
        return new Color(vals[0], vals[1], vals[2]);
    }

    public static String toHtmlColor(Color newColor) {
        System.out.println("R" + newColor.getRed() + " G" + newColor.getGreen() + " B" + newColor.getBlue());
        return StringUtils.leftPad(StringUtils.substring(Integer.toHexString(newColor.getRed()), 0, 2), 2, "0") + //
                StringUtils.leftPad(StringUtils.substring(Integer.toHexString(newColor.getGreen()), 0, 2), 2, "0") + //
                StringUtils.leftPad(StringUtils.substring(Integer.toHexString(newColor.getBlue()), 0, 2), 2, "0");
    }

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

    public static class LinearGradientColor {
        Color colorStart = JColorUtil.randomColor();
        Color colorEnd = JColorUtil.randomColor();
        int steps = 30;
        int i = 0;

        public LinearGradientColor() {
        }

        public LinearGradientColor(int steps) {
            this.steps = steps;
        }

        private void progress() {
            i++;
            if (i > steps) {
                i = 0;
                colorStart = colorEnd;
                colorEnd = JColorUtil.randomColor();
            }
        }

        public Color get() {
            float ratio = (float) i / (float) steps;
            int red = (int) (colorEnd.getRed() * ratio + colorStart.getRed() * (1 - ratio));
            int green = (int) (colorEnd.getGreen() * ratio + colorStart.getGreen() * (1 - ratio));
            int blue = (int) (colorEnd.getBlue() * ratio + colorStart.getBlue() * (1 - ratio));
            Color stepColor = new Color(red, green, blue);
            progress();
            return stepColor;
        }
    }
}
