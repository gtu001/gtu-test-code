package gtu.swing.util;

import java.awt.event.KeyEvent;

import org.apache.commons.lang.StringUtils;

public class KeyEventUtil {

    /**
     * 判斷Mask按鈕是否有按
     */
    public static boolean isMaskKeyPress(KeyEvent e, String type) {
        type = StringUtils.trimToEmpty(type);
        boolean shiftChk = type.toLowerCase().contains("s") ? (e.getModifiers() & KeyEvent.SHIFT_MASK) != 0 : true;
        boolean ctrlChk = type.toLowerCase().contains("c") ? (e.getModifiers() & KeyEvent.CTRL_MASK) != 0 : true;
        boolean altChk = type.toLowerCase().contains("a") ? (e.getModifiers() & KeyEvent.ALT_MASK) != 0 : true;
        return shiftChk && ctrlChk && altChk;
    }
}
