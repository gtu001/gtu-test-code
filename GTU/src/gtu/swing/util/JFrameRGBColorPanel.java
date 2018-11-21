package gtu.swing.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
    private Color background;
    private Color foreground;

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
        {
            int r = Math.abs(255 - color.getRed());
            int g = Math.abs(255 - color.getGreen());
            int b = Math.abs(255 - color.getBlue());
            // return new Color(r, g, b);
        }
        if (((color.getRed() + color.getGreen() + color.getBlue()) / 3) > 128) {
            return new Color(0, 0, 0);
        } else {
            return new Color(255, 255, 255);
        }
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
            __specialProcess(com, background, foreground);
            if (com instanceof Container) {
                setBackground((Container) com, background, foreground);
            }
        }
    }

    private void __specialProcess(Component com, Color background, Color foreground) {
        if (com instanceof JCheckBox) {
            ((JCheckBox) com).setOpaque(true);// 目前無作用 但保留
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
                            background = linearGradientColor.get();
                            foreground = getForeground(background);
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

    public JToggleButton getToggleButton(boolean isSelected) {
        final JToggleButton tgBtn = new JToggleButton();
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
        tgBtn.setSelected(isSelected);
        JCommonUtil.triggerButtonActionPerformed(tgBtn);
        return tgBtn;
    }

    public static void main(String[] args) {
        JFrame frame = JFrameUtil.createSimpleFrame(JFrameRGBColorPanel.class);
        final JFrameRGBColorPanel jFrameRGBColorPanel = new JFrameRGBColorPanel(frame);
        final JLabel lbl = new JLabel();
        frame.add(lbl);
        final JLabel lbl2 = new JLabel();
        frame.add(lbl2);
        jFrameRGBColorPanel.setAfterProcessEvent(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lbl.setText("bg : " + String.valueOf(jFrameRGBColorPanel.background));
                lbl2.setText("fg : " + String.valueOf(jFrameRGBColorPanel.foreground));
            }
        });
        // 
        frame.add(jFrameRGBColorPanel.getToggleButton(true));
        frame.pack();
        frame.setVisible(true);
    }
}