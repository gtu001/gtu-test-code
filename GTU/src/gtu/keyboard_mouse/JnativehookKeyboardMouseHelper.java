package gtu.keyboard_mouse;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;

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
}
