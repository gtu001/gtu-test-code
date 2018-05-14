package gtu.swing.util;

import java.awt.im.InputContext;
import java.util.Locale;

@Deprecated
public class JChangeInputMethodUtil {

    public static void main(String[] args) throws Exception {
    }

    public static boolean toChineseTaiwan() {
        boolean result = InputContext.getInstance().selectInputMethod(Locale.TAIWAN);
        System.out.println("toChineseTaiwan - " + result);
        return result;
    }

    public static boolean toEnglish() {
        boolean result = InputContext.getInstance().selectInputMethod(Locale.ENGLISH);
        System.out.println("toEnglish - " + result);
        return result;
    }
}
