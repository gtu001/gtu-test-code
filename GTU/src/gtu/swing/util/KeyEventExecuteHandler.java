package gtu.swing.util;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.commons.lang3.StringUtils;

public class KeyEventExecuteHandler {
    private AtomicBoolean isPrecedingExeucte = new AtomicBoolean(false);
    private Runnable runnable;
    private JFrame self;
    private String title;
    private KeyEventExecuteHandlerDoExecute doExecuteEvent;

    public static KeyEventExecuteHandler newInstance(JFrame self, String title, KeyEventExecuteHandlerDoExecute doExecuteEvent, Runnable runnable) {
        return new KeyEventExecuteHandler(self, title, doExecuteEvent, runnable);
    }

    private KeyEventExecuteHandler(JFrame self, String title, KeyEventExecuteHandlerDoExecute doExecuteEvent, Runnable runnable) {
        this.runnable = runnable;
        this.self = self;
        this.title = StringUtils.defaultIfEmpty(title, "執行中...");
        this.doExecuteEvent = doExecuteEvent;
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

    public void setDoExecuteEvent(KeyEventExecuteHandlerDoExecute doExecuteEvent) {
        this.doExecuteEvent = doExecuteEvent;
    }

    /**
     * 預設為 F5 or Alt+Enter
     */
    public interface KeyEventExecuteHandlerDoExecute {
        boolean isExecute(KeyEvent e);
    }

    private void doExecuteSqlButtonClick(KeyEvent e) {
        boolean doExecute = false;
        if (doExecuteEvent == null) {
            if ((e.getModifiers() & KeyEvent.ALT_MASK) != 0 && e.getKeyCode() == KeyEvent.VK_ENTER) {
                doExecute = true;
            } else if (e.getKeyCode() == KeyEvent.VK_F5) {
                doExecute = true;
            }
        } else {
            doExecute = doExecuteEvent.isExecute(e);
        }
        if (doExecute) {
            if (!isPrecedingExeucte.get()) {
                isPrecedingExeucte.set(true);
            } else {
                return;
            }

            final JProgressBarHelper proHelper = JProgressBarHelper.newInstance(self, title);
            proHelper.indeterminate(true);
            proHelper.modal(false);
            proHelper.build();
            proHelper.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        runnable.run();
                    } catch (Throwable ex) {
                        JCommonUtil.handleException(ex);
                    } finally {
                        isPrecedingExeucte.set(false);
                    }
                    proHelper.dismiss();
                }
            }).start();
        }
    }
}