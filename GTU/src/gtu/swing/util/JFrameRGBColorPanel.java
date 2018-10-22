package gtu.swing.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import gtu.swing.util.JColorUtil.LinearGradientColor;

public class JFrameRGBColorPanel {
    private Thread checkFocusOwnerThread;
    private boolean isStop = false;
    private final Container container;
    private ActionListener afterProcessEvent;
    private List<Component> ignoreLst;
    private LinearGradientColor linearGradientColor = new LinearGradientColor(100);

    public JFrameRGBColorPanel(final Container container) {
        this.container = container;
    }

    public void stop() {
        isStop = true;
        try {
            Thread.sleep(200L);
        } catch (Exception e) {
        }
        checkFocusOwnerThread = null;
        isStop = false;
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
            if (isIgnore(com)) {
                continue;
            }
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

    private boolean isIgnore(Component component) {
        if (ignoreLst == null || ignoreLst.isEmpty()) {
            return false;
        }
        if (ignoreLst.contains(component)) {
            return true;
        }
        return false;
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

    public void setIgnoreLst(Container container) {
        List<Component> lst = new ArrayList<Component>();
        for (Field f : container.getClass().getDeclaredFields()) {
            try {
                Object com = FieldUtils.readDeclaredField(container, f.getName(), true);
                if (com != null && //
                        com instanceof Component && //
                        ArrayUtils.contains(new Class[] { //
                                JTextField.class, //
                                JTextArea.class, //
                                JList.class, //
                                JTable.class, //
                                JComboBox.class,//
                        }, com.getClass())//
                ) {
                    lst.add((Component) com);
                    System.out.println("add Ignore : " + f.getName());
                }
            } catch (Exception e) {
            }
        }
        this.ignoreLst = lst;
    }

    public void setIgnoreLst(List<Component> ignoreLst) {
        this.ignoreLst = ignoreLst;
    }

    public void addIgnoreLst(Component component) {
        if (ignoreLst == null) {
            ignoreLst = new ArrayList<Component>();
        }
        if (ignoreLst.contains(component)) {
            return;
        }
        ignoreLst.add(component);
    }

    public JToggleButton getToggleButton() {
        final JToggleButton tgBtn = new JToggleButton("變色");
        tgBtn.setSelected(true);
        tgBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tgBtn.setText(tgBtn.isSelected() ? "變色啟動" : "變色暫停");
                if (tgBtn.isSelected()) {
                    start();
                } else {
                    stop();
                }
            }
        });
        return tgBtn;
    }
}