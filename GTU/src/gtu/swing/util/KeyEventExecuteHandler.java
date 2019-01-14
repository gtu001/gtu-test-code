package gtu.swing.util;

import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JComponent;

import org.apache.commons.lang.reflect.FieldUtils;

public class KeyEventExecuteHandler {
    private AtomicBoolean isPrecedingExeucte = new AtomicBoolean(false);
    private Runnable runnable;

    public static KeyEventExecuteHandler newInstance(Window self, Runnable runnable) {
        return new KeyEventExecuteHandler(self, runnable);
    }

    private KeyEventExecuteHandler(Window self, Runnable runnable) {
        this.runnable = runnable;
        for (Field f : self.getClass().getDeclaredFields()) {
            if (JComponent.class.isAssignableFrom(f.getType())) {
                try {
                    JComponent j = (JComponent) FieldUtils.readDeclaredField(self, f.getName(), true);
                    j.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyPressed(KeyEvent e) {
                            doExecuteSqlButtonClick(e);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void doExecuteSqlButtonClick(KeyEvent e) {
        boolean execute = false;
        if ((e.getModifiers() & KeyEvent.ALT_MASK) != 0 && e.getKeyCode() == KeyEvent.VK_ENTER) {
            execute = true;
        } else if (e.getKeyCode() == KeyEvent.VK_F5) {
            execute = true;
        }
        if (execute) {
            if (!isPrecedingExeucte.get()) {
                isPrecedingExeucte.set(true);
            } else {
                return;
            }
            try {
                this.runnable.run();
            } catch (Throwable ex) {
                JCommonUtil.handleException(ex);
            } finally {
                isPrecedingExeucte.set(false);
            }
        }
    }
}