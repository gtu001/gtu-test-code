package gtu.swing.util;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JToggleButton;
import javax.swing.UIManager;

import org.apache.commons.lang3.StringUtils;

import gtu.image.ImageUtil;
import gtu.javafx.traynotification.NotificationType;
import gtu.javafx.traynotification.TrayNotificationHelper;
import gtu.javafx.traynotification.animations.AnimationType;

public class HideInSystemTrayHelper {

    private static boolean isWindows = false;

    static {
        if (System.getProperty("os.name").startsWith("Windows")) {
            isWindows = true;
        } else if ("Linux".equals(System.getProperty("os.name"))) {
            isWindows = false;
        }
    }

    public static void main(String[] args) {
        HideInSystemTrayHelper inst = HideInSystemTrayHelper.newInstance();
        inst.apply();
        inst.displayMessage("caption", "message", MessageType.INFO);
        // inst.removeTray();
        System.out.println("done...");
    }

    public static HideInSystemTrayHelper newInstance() {
        return new HideInSystemTrayHelper();
    }

    TrayIcon trayIcon;
    SystemTray tray;
    ActionListener openAction;
    ActionListener closeAction;
    List<MenuItem> items = new ArrayList<MenuItem>();
    HideToTrayListener hideToTrayListener;
    JFrame jframe;
    private TrayIconHandler trayIconHandler = new TrayIconHandler();

    public void apply() {
        apply(null, "TEMP", null);
    }

    public void apply(final JFrame jframe) {
        String trayTitle = jframe.getClass().getSimpleName();
        if (StringUtils.isNotBlank(jframe.getTitle())) {
            trayTitle = jframe.getTitle();
        }
        apply(jframe, trayTitle, null);
    }

    private Image getSysTrayIcon(String imagePath) {
        if (imagePath.endsWith(".ico")) {
            return ImageUtil.getInstance().getIcoImage(imagePath);
        }
        return ImageUtil.getInstance().getDefaultImage(imagePath);
    }

    public void apply(final JFrame jframe, String sysTrayTitle, String imagePath) {
        System.out.println("creating instance");

        this.jframe = jframe;

        // 決定icon
        Image image = null;
        if (StringUtils.isNotBlank(imagePath)) {
            image = getSysTrayIcon(imagePath);
        } else if (jframe != null && jframe.getIconImage() != null) {
            image = jframe.getIconImage();
        } else {
            image = getSysTrayIcon("resource/images/ico/janna.ico");
        }

        if (isWindows) {
            try {
                System.out.println("setting look and feel");
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Throwable e) {
                System.out.println("Unable to set LookAndFeel");
            }
        }

        if (SystemTray.isSupported()) {
            System.out.println("system tray supported");
            tray = SystemTray.getSystemTray();

            PopupMenu popup = new PopupMenu();
            MenuItem defaultItem = new MenuItem("Open");
            defaultItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    gtu.swing.util.JFrameUtil.setVisible(true, jframe);
                    jframe.setExtendedState(JFrame.NORMAL);
                    if (openAction != null) {
                        openAction.actionPerformed(e);
                    }
                }
            });
            popup.add(defaultItem);

            if (this.getItems() != null) {
                for (MenuItem e : this.getItems()) {
                    popup.add(e);
                }
            }

            defaultItem = new MenuItem("Exit");
            defaultItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Exiting....");
                    if (closeAction != null) {
                        closeAction.actionPerformed(e);
                    }
                    System.runFinalization();
                    System.exit(0);
                }
            });
            popup.add(defaultItem);
            trayIcon = new TrayIcon(image, sysTrayTitle, popup);
            trayIcon.setImageAutoSize(true);

            trayIcon.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    tray.remove(trayIcon);
                    if (jframe != null)
                        gtu.swing.util.JFrameUtil.setVisible(true, jframe);
                    if (openAction != null) {
                        openAction.actionPerformed(e);
                    }
                }
            });
        } else {
            System.out.println("system tray not supported");
        }
        if (jframe != null) {

            // 啟動Hide in tray
            this.setHideEventOnOff(true);

            jframe.addWindowStateListener(hideToTrayListener);
            jframe.setIconImage(image);
            jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }

    /**
     * 啟動 hide in tray
     * 
     * @param isOn
     */
    public void setHideEventOnOff(boolean isOn) {
        if (jframe == null) {
            return;
        }
        if (hideToTrayListener == null) {
            hideToTrayListener = new HideToTrayListener(jframe);
        }
        if (isOn) {
            if (!hideToTrayListener.hasEvent()) {
                hideToTrayListener.addEvent();
                System.out.println("----add hideToTrayListener");
            }
        } else {
            if (hideToTrayListener.hasEvent()) {
                hideToTrayListener.removeEvent();
                System.out.println("----remove hideToTrayListener");
            }
        }
    }

    /**
     * 加入是否隱藏道系統列
     * 
     * @return
     */
    public JToggleButton getToggleButton(boolean isSelected) {
        final JToggleButton tgBtn = new JToggleButton();
        tgBtn.setSelected(true);
        tgBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tgBtn.setText(tgBtn.isSelected() ? "縮小至系統列" : "縮小至工作列");
                if (tgBtn.isSelected()) {
                    setHideEventOnOff(true);
                } else {
                    setHideEventOnOff(false);
                }
            }
        });
        tgBtn.setSelected(isSelected);
        JCommonUtil.triggerButtonActionPerformed(tgBtn);
        return tgBtn;
    }

    public boolean isStop() {
        return hideToTrayListener.hasEvent();
    }

    private class HideToTrayListener implements WindowStateListener {
        JFrame jframe;

        HideToTrayListener(JFrame jframe) {
            this.jframe = jframe;
        }

        public boolean removeEvent() {
            boolean findOk = false;
            if (jframe.getWindowStateListeners() != null) {
                for (int ii = 0; ii < jframe.getWindowStateListeners().length; ii++) {
                    if (jframe.getWindowStateListeners()[ii] == this) {
                        jframe.removeWindowStateListener(this);
                        findOk = true;
                        ii--;
                    }
                }
            }
            return findOk;
        }

        public boolean hasEvent() {
            if (jframe.getWindowStateListeners() != null) {
                for (int ii = 0; ii < jframe.getWindowStateListeners().length; ii++) {
                    if (jframe.getWindowStateListeners()[ii] == this) {
                        return true;
                    }
                }
            }
            return false;
        }

        public void addEvent() {
            jframe.addWindowStateListener(this);
        }

        public void windowStateChanged(WindowEvent e) {
            if (tray == null && trayIcon == null) {
                System.out.println("[2] system tray not supported");
                return;
            }
            // if (e.getNewState() == JFrame.ICONIFIED) {
            // try {
            // tray.add(trayIcon);
            // gtu.swing.util.JFrameUtil.setVisible(false, jframe);
            // System.out.println("added to SystemTray");
            // } catch (AWTException ex) {
            // System.out.println("unable to add to tray");
            // }
            // }
            if (e.getNewState() == 7) {
                try {
                    tray.add(trayIcon);
                    gtu.swing.util.JFrameUtil.setVisible(false, jframe);
                    System.out.println("added to SystemTray");
                } catch (AWTException ex) {
                    System.out.println("unable to add to system tray");
                }
            }
            if (e.getNewState() == JFrame.MAXIMIZED_BOTH) {
                tray.remove(trayIcon);
                gtu.swing.util.JFrameUtil.setVisible(true, jframe);
                System.out.println("Tray icon removed");
            }
            if (e.getNewState() == JFrame.NORMAL) {
                tray.remove(trayIcon);
                gtu.swing.util.JFrameUtil.setVisible(true, jframe);
                System.out.println("Tray icon removed");
            }
        }
    }

    private class TrayIconHandler {
        private boolean exists() {
            if (tray != null) {
                for (int ii = 0; ii < tray.getTrayIcons().length; ii++) {
                    TrayIcon icon = tray.getTrayIcons()[ii];
                    if (icon == trayIcon) {
                        System.out.println("--findTrayIcon ok!");
                        return true;
                    }
                }
            }
            return false;
        }

        private void addIfNeed() {
            if (!exists()) {
                try {
                    if (tray != null)
                        tray.add(trayIcon);
                } catch (AWTException e) {
                    e.printStackTrace();
                }
            }
        }

        @Deprecated
        private void removeIfExists() {
            if (exists()) {
                if (tray != null) {
                    System.out.println("--removeTrayIcon ok!");
                    tray.remove(trayIcon);
                }
            }
        }
    }

    @Deprecated
    public void removeTray(final long removeTime) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(removeTime);
                } catch (InterruptedException e) {
                }
                trayIconHandler.removeIfExists();
            }
        }).start();
    }

    public void displayMessage(final String caption, final String text, final MessageType messageType) {
        trayIconHandler.addIfNeed();
        System.out.println("displayMessage : " + caption + " - " + text + " - " + messageType);

        try {
            trayIcon.displayMessage(caption, text, messageType);
        } catch (Exception ex) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        displayMessage4Linux(caption, text, messageType);
                    } catch (Exception ex2) {
                        ex2.printStackTrace();
                    }
                }
            }).start();
        }
        // trayIconHandler.removeIfExists();
    }

    private void displayMessage4Linux(String caption, String text, MessageType messageType) {
        NotificationType mappingType = null;
        switch (messageType) {
        case ERROR:
            mappingType = NotificationType.ERROR;
            break;
        case WARNING:
            mappingType = NotificationType.WARNING;
            break;
        case INFO:
            mappingType = NotificationType.INFORMATION;
            break;
        case NONE:
            mappingType = NotificationType.INFORMATION;
            break;
        }
        try {
            TrayNotificationHelper.newInstance()//
                    .title(caption)//
                    .message(text)//
                    .notificationType(mappingType)//
                    .rectangleFill(TrayNotificationHelper.RandomColorFill.getInstance().get())//
                    .animationType(AnimationType.FADE)//
                    .onPanelClickCallback(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                        }
                    }).show(1500);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public TrayIcon getTrayIcon() {
        return trayIcon;
    }

    public void setCloseAction(ActionListener closeAction) {
        this.closeAction = closeAction;
    }

    public void setOpenAction(ActionListener openAction) {
        this.openAction = openAction;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public void setItems(List<MenuItem> items) {
        this.items = items;
    }
}
