package gtu.keyboard_mouse;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyAdapter;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.mouse.NativeMouseAdapter;
import org.jnativehook.mouse.NativeMouseEvent;

import gtu.swing.util.JCommonUtil;

public class JnativehookKeyboardMouseHelper {

    private static final JnativehookKeyboardMouseHelper _INST = new JnativehookKeyboardMouseHelper();

    private JnativehookKeyboardMouseHelper() {
    }

    public static JnativehookKeyboardMouseHelper getInstance() {
        return _INST;
    }

    public void disableLogger() {
        System.out.println(">> disableLogger");
        // Get the logger for "org.jnativehook" and set the level to warning.
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);

        // Don't forget to disable the parent handlers.
        logger.setUseParentHandlers(false);
    }

    public interface MyNativeKeyAdapter {
        public void nativeKeyReleased(NativeKeyEvent e);

        public void nativeMouseReleased(NativeMouseEvent e);
    }

    public static void startNativeKeyboardAndMouse(MyNativeKeyAdapter mMyNativeKeyAdapter) {
        try {
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeMouseListener(new SimpleNativeMouseAdapter(mMyNativeKeyAdapter));
            GlobalScreen.addNativeKeyListener(new SimpleNativeKeyAdapter(mMyNativeKeyAdapter));
            JnativehookKeyboardMouseHelper.getInstance().disableLogger();
        } catch (NativeHookException ex) {
            JCommonUtil.handleException(ex);
        }
    }

    /**
     * 判斷Mask按鈕是否有按
     */
    public static boolean isMaskKeyPress(NativeKeyEvent e, String type) {
        type = StringUtils.trimToEmpty(type);
        boolean shiftChk = type.toLowerCase().contains("s") ? (e.getModifiers() & NativeKeyEvent.ALT_MASK) != 0 : true;
        boolean altChk = type.toLowerCase().contains("a") ? (e.getModifiers() & NativeKeyEvent.ALT_MASK) != 0 : true;
        boolean ctrlChk = type.toLowerCase().contains("c") ? (e.getModifiers() & NativeKeyEvent.CTRL_MASK) != 0 : true;
        return shiftChk && ctrlChk && altChk;
    }

    public static void removeNativeKeyboardAndMouse() {
        try {
            org.jnativehook.GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException e1) {
            e1.printStackTrace();
        }
    }

    private static class SimpleNativeKeyAdapter extends NativeKeyAdapter {
        MyNativeKeyAdapter mMyNativeKeyAdapter;

        private SimpleNativeKeyAdapter(MyNativeKeyAdapter mMyNativeKeyAdapter) {
            this.mMyNativeKeyAdapter = mMyNativeKeyAdapter;
        }

        public void nativeKeyReleased(NativeKeyEvent e) {
            mMyNativeKeyAdapter.nativeKeyReleased(e);
        }
    }

    private static class SimpleNativeMouseAdapter extends NativeMouseAdapter {
        MyNativeKeyAdapter mMyNativeKeyAdapter;

        private SimpleNativeMouseAdapter(MyNativeKeyAdapter mMyNativeKeyAdapter) {
            this.mMyNativeKeyAdapter = mMyNativeKeyAdapter;
        }

        @Override
        public void nativeMouseReleased(NativeMouseEvent paramNativeMouseEvent) {
            mMyNativeKeyAdapter.nativeMouseReleased(paramNativeMouseEvent);
        }
    }
}
