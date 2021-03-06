package gtu.swing.util;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.commons.lang3.StringUtils;

public class KeyEventExecuteHandler {
    private AtomicBoolean isPrecedingExeucte = new AtomicBoolean(false);
    private AtomicLong executeStartTime = new AtomicLong(-1);
    private Runnable runnable;
    private Window self;
    private String title;
    private Transformer isDoExecuteEvent;
    private Component[] ignoreComps;
    private Predicate isIgnoreComps;

    public static KeyEventExecuteHandler newInstance(Window self, String title, Transformer isDoExecuteEvent, Runnable runnable) {
        return new KeyEventExecuteHandler(self, title, isDoExecuteEvent, runnable, null, null);
    }

    public static KeyEventExecuteHandler newInstance(Window self, String title, Transformer isDoExecuteEvent, Runnable runnable, Component[] ignoreComps) {
        return new KeyEventExecuteHandler(self, title, isDoExecuteEvent, runnable, ignoreComps, null);
    }

    public static KeyEventExecuteHandler newInstance(Window self, String title, Transformer isDoExecuteEvent, Runnable runnable, Predicate isIgnoreComps) {
        return new KeyEventExecuteHandler(self, title, isDoExecuteEvent, runnable, null, isIgnoreComps);
    }

    private KeyEventExecuteHandler(Window self, String title, Transformer isDoExecuteEvent, Runnable runnable, Component[] ignoreComps, Predicate isIgnoreComps) {
        this.runnable = runnable;
        this.self = self;
        this.title = StringUtils.defaultIfEmpty(title, "執行中...");
        this.isDoExecuteEvent = isDoExecuteEvent;
        this.ignoreComps = ignoreComps;
        this.isIgnoreComps = isIgnoreComps;
        A: for (Field f : self.getClass().getDeclaredFields()) {
            if (JComponent.class.isAssignableFrom(f.getType())) {
                try {
                    JComponent j = (JComponent) FieldUtils.readDeclaredField(self, f.getName(), true);

                    if (this.ignoreComps != null) {
                        for (Component c : this.ignoreComps) {
                            if (c == j) {
                                continue A;
                            }
                        }
                    }

                    if (this.isIgnoreComps != null) {
                        if (isIgnoreComps.evaluate(j)) {
                            continue A;
                        }
                    }

                    if (j == null) {
                        System.err.println("!!! 未初始化 : " + f.getName());
                        continue;
                    }

                    j.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyPressed(KeyEvent e) {
                            doExecuteKeyPressProcess(e);
                        }
                    });
                } catch (Exception e) {
                    System.err.println("AddKeyEvent Failed : " + f.getName());
                    e.printStackTrace();
                }
            }
        }
    }

    private void doExecuteKeyPressProcess(KeyEvent e) {
        boolean doExecute = false;
        if (isDoExecuteEvent == null) {
            if ((e.getModifiers() & KeyEvent.ALT_MASK) != 0 && e.getKeyCode() == KeyEvent.VK_ENTER) {
                doExecute = true;
            } else if (e.getKeyCode() == KeyEvent.VK_F5) {
                doExecute = true;
            }
        } else {
            doExecute = (Boolean) isDoExecuteEvent.transform(e);
        }
        if (doExecute) {
            if (!isPrecedingExeucte.get()) {
                isPrecedingExeucte.set(true);
                executeStartTime.set(System.currentTimeMillis());
            } else {
                return;
            }

            final AtomicReference<JFrame> relativeFrame = new AtomicReference<JFrame>();
            if (self instanceof JFrame) {
                relativeFrame.set((JFrame) self);
            }

            final AtomicReference<JProgressBarHelper> proHelper = new AtomicReference<JProgressBarHelper>();
            if (StringUtils.isNotBlank(title)) {
                proHelper.set(JProgressBarHelper.newInstance(relativeFrame.get(), title));
                proHelper.get().indeterminate(true);
                proHelper.get().modal(false);
                proHelper.get().build();
                proHelper.get().show();
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("===========================================");
                    System.out.println("= KeyEventExecuteHandler start            =");
                    System.out.println("===========================================");
                    try {
                        System.out.println("--------------------------A");
                        runnable.run();
                        System.out.println("--------------------------B");
                    } catch (Throwable ex) {
                        JCommonUtil.handleException(ex);
                    } finally {
                        System.out.println("===========================================");
                        System.out.println("= KeyEventExecuteHandler END              =");
                        System.out.println("===========================================");

                        System.out.println("--------------------------1");
                        if (proHelper.get() != null) {
                            System.out.println("--------------------------2");
                            proHelper.get().dismiss();
                            System.out.println("--------------------------3");
                        }
                        System.out.println("--------------------------4");
                        isPrecedingExeucte.set(false);
                        System.out.println("--------------------------5");
                        
                        System.out.println("KeyEventExecuteHandler ... exe done!!");
                    }
                }
            }).start();
        }
    }

    public boolean isPrecedingExeucte() {
        return isPrecedingExeucte.get();
    }

    public long getExecuteStartTime() {
        return executeStartTime.get();
    }
}