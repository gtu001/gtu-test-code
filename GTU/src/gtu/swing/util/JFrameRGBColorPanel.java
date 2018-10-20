package gtu.swing.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import gtu.swing.util.JColorUtil.LinearGradientColor;

public class JFrameRGBColorPanel {
    private Thread checkFocusOwnerThread;
    private boolean isStop = false;
    private final Container container;
    private ActionListener afterProcessEvent;

    public JFrameRGBColorPanel(final Container container) {
        this.container = container;
    }

    public void reset() {
        stop();
        try {
            Thread.sleep(1000L);
        } catch (Exception e) {
        }
        checkFocusOwnerThread = null;
        isStop = false;
    }

    public void stop() {
        isStop = true;
    }

    private Color getForeground(Color color) {
        int r = Math.abs(255 - color.getRed());
        int g = Math.abs(255 - color.getGreen());
        int b = Math.abs(255 - color.getBlue());
        return new Color(r, g, b);
    }

    private void setBackground(Container container, Color background, Color foreground) {
        container.setBackground(background);
        container.setForeground(foreground);
        for (Component com : container.getComponents()) {
            com.setBackground(background);
            com.setForeground(foreground);
            if (com instanceof Container) {
                setBackground((Container) com, background, foreground);
            }
        }
    }

    private void triggerAfterEvent() {
        if (afterProcessEvent != null) {
            ActionEvent event = new ActionEvent(JFrameRGBColorPanel.this, -1, "");
            afterProcessEvent.actionPerformed(event);
        }
    }

    public void start() {
        if (checkFocusOwnerThread == null || checkFocusOwnerThread.getState() == Thread.State.TERMINATED) {
            checkFocusOwnerThread = new Thread(new Runnable() {
                LinearGradientColor linearGradientColor = new LinearGradientColor(100);

                @Override
                public void run() {
                    while (isStop == false) {
                        try {
                            Color background = linearGradientColor.get();
                            Color foreground = getForeground(background);
                            setBackground(container, background, foreground);

                            triggerAfterEvent();
                            Thread.sleep(50L);
                        } catch (Exception e) {
                            JCommonUtil.handleException(e);
                        }
                    }
                }
            });
            checkFocusOwnerThread.setDaemon(true);
            checkFocusOwnerThread.start();
        }
    }

    public boolean isStop() {
        return isStop;
    }

    public void setStop(boolean isStop) {
        this.isStop = isStop;
    }

    public void setAfterProcessEvent(ActionListener afterProcessEvent) {
        this.afterProcessEvent = afterProcessEvent;
    }
}