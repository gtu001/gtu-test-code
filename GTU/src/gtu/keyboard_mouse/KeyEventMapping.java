package gtu.keyboard_mouse;

import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.reflect.FieldUtils;
import org.jnativehook.keyboard.NativeKeyEvent;

public class KeyEventMapping {
    
    public static void main(String[] args) {
        SwingKeyEventToNativeKeyMapping t = new SwingKeyEventToNativeKeyMapping();
        System.out.println(t.getMap());
    }

    public static class NativeKeyEventToSwingKeyMapping {
        private Map<String, Integer> awtKeyMap = new HashMap<String, Integer>();

        public NativeKeyEventToSwingKeyMapping() {
            for (Field f : KeyEvent.class.getDeclaredFields()) {
                if (Modifier.isStatic(f.getModifiers()) && f.getName().startsWith("VK")) {
                    try {
                        int keycode = (Integer) FieldUtils.readDeclaredStaticField(KeyEvent.class, f.getName());
                        String keyText = KeyEvent.getKeyText(keycode);
                        awtKeyMap.put(keyText, keycode);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public Map<String,Integer> getMap(){
            return awtKeyMap;
        }
        
        public int getKeyCodeFromNativeKeyEvent(int keycode) {
            String val = NativeKeyEvent.getKeyText(keycode);
            if (awtKeyMap.containsKey(val)) {
                return awtKeyMap.get(val);
            }
            throw new KeyCodeNotFoundException("無法判斷的keycode : " + keycode);
        }
        
        public int getKeyCodeFromKeyText(String keyText) {
            if (awtKeyMap.containsKey(keyText)) {
                return awtKeyMap.get(keyText);
            }
            throw new KeyCodeNotFoundException("無法判斷的keycode : " + keyText);
        }
        
        public String getKeyTextFromKeyCode(int keyCode) {
            for(String k : awtKeyMap.keySet()) {
                int val = awtKeyMap.get(k);
                if(val == keyCode) {
                    return k;
                }
            }
            throw new KeyCodeNotFoundException("無法判斷的keycode : " + keyCode);
        }
    }
    
    public static class SwingKeyEventToNativeKeyMapping {
        private Map<String, Integer> nativeKeyMap = new HashMap<String, Integer>();
        
        public SwingKeyEventToNativeKeyMapping(){
            for(Field f : NativeKeyEvent.class.getDeclaredFields()) {
                if (Modifier.isStatic(f.getModifiers()) && f.getName().startsWith("VC")) {
                    try {
                        int keycode = (Integer) FieldUtils.readDeclaredStaticField(NativeKeyEvent.class, f.getName());
                        String keyText = NativeKeyEvent.getKeyText(keycode);
                        nativeKeyMap.put(keyText, keycode);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        
        public Map<String,Integer> getMap(){
            return nativeKeyMap;
        }
        
        public int getKeyCodeFromSwingKeyEvent(int keycode) {
            String val = KeyEvent.getKeyText(keycode);
            if (nativeKeyMap.containsKey(val)) {
                return nativeKeyMap.get(val);
            }
            throw new KeyCodeNotFoundException("無法判斷的keycode : " + keycode);
        }
        
        public int getKeyCodeFromKeyText(String keyText) {
            if (nativeKeyMap.containsKey(keyText)) {
                return nativeKeyMap.get(keyText);
            }
            throw new KeyCodeNotFoundException("無法判斷的keycode : " + keyText);
        }
        
        public String getKeyTextFromKeyCode(int keyCode) {
            for(String k : nativeKeyMap.keySet()) {
                int val = nativeKeyMap.get(k);
                if(val == keyCode) {
                    return k;
                }
            }
            throw new KeyCodeNotFoundException("無法判斷的keycode : " + keyCode);
        }
    }
    
    public static class KeyCodeNotFoundException extends RuntimeException {
        KeyCodeNotFoundException(String message) {
            super(message);
        }
    }
}
