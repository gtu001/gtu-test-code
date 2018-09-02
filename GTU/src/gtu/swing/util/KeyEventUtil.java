package gtu.swing.util;

import java.awt.event.KeyEvent;

public class KeyEventUtil {

    /**
     * 判斷Mask按鈕是否有按
     */
    public static boolean isMaskKeyPress(KeyEvent e, String type) {
        boolean shiftChk = type.contains("s") ? (e.getModifiers() & KeyEvent.SHIFT_MASK) != 0 : true;
        boolean ctrlChk = type.contains("c") ? (e.getModifiers() & KeyEvent.CTRL_MASK) != 0 : true;
        boolean altChk = type.contains("a") ? (e.getModifiers() & KeyEvent.ALT_MASK) != 0 : true;
        return shiftChk && ctrlChk && altChk;
    }
}
