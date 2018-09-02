package gtu.keyboard_mouse;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import gtu.swing.util.JCommonUtil;

public class KeyboardEventTest {

    public static void main(String[] args) throws AWTException {
        KeyboardEventTest t = new KeyboardEventTest();
        t.new TestKeyListener();
        t.new TestKeyListener2();
        System.out.println("done...");
    }
    
    private class TestKeyListener implements NativeKeyListener {

        TestKeyListener() {
            initialize();
        }
        
        private void initialize() {
            try {
                if (!GlobalScreen.isNativeHookRegistered()) {
                    GlobalScreen.registerNativeHook();
                }
            } catch (NativeHookException e) {
                JCommonUtil.handleException(e);
                throw new RuntimeException(e);
            }
            GlobalScreen.addNativeKeyListener(this);
        }

        public void close() {
            if (!GlobalScreen.isNativeHookRegistered()) {
                GlobalScreen.removeNativeKeyListener(this);
            }
        }

        @Override
        public void nativeKeyTyped(NativeKeyEvent paramNativeKeyEvent) {
        }

        @Override
        public void nativeKeyPressed(NativeKeyEvent paramNativeKeyEvent) {
        }

        @Override
        public void nativeKeyReleased(NativeKeyEvent e) {
            if ((e.getModifiers() & NativeInputEvent.CTRL_MASK) != 0 && //
                    e.getKeyCode() == NativeKeyEvent.VC_1) {
                System.out.println("111111111111111111111");
            }
        }
    }

    private class TestKeyListener2 implements NativeKeyListener {

        TestKeyListener2() {
            initialize();
        }
        
        private void initialize() {
            try {
                if (!GlobalScreen.isNativeHookRegistered()) {
                    GlobalScreen.registerNativeHook();
                }
            } catch (NativeHookException e) {
                JCommonUtil.handleException(e);
                throw new RuntimeException(e);
            }
            GlobalScreen.addNativeKeyListener(this);
        }

        public void close() {
            if (!GlobalScreen.isNativeHookRegistered()) {
                GlobalScreen.removeNativeKeyListener(this);
            }
        }

        @Override
        public void nativeKeyTyped(NativeKeyEvent paramNativeKeyEvent) {
        }

        @Override
        public void nativeKeyPressed(NativeKeyEvent paramNativeKeyEvent) {
        }

        @Override
        public void nativeKeyReleased(NativeKeyEvent e) {
            if ((e.getModifiers() & NativeInputEvent.CTRL_MASK) != 0 && //
                    e.getKeyCode() == NativeKeyEvent.VC_2) {
                System.out.println("222222222222222222222");
            }
        }
    }
}
