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
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
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
                    jframe.setVisible(true);
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
                        jframe.setVisible(true);
                    if (openAction != null) {
                        openAction.actionPerformed(e);
                    }
                }
            });
        } else {
            System.out.println("system tray not supported");
        }
        if (jframe != null) {
            jframe.addWindowStateListener(new WindowStateListener() {
                public void windowStateChanged(WindowEvent e) {
                    if (e.getNewState() == JFrame.ICONIFIED) {
                        try {
                            if (tray != null && trayIcon != null)
                                tray.add(trayIcon);
                            jframe.setVisible(false);
                            System.out.println("added to SystemTray");
                        } catch (AWTException ex) {
                            System.out.println("unable to add to tray");
                        }
                    }
                    if (e.getNewState() == 7) {
                        try {
                            if (tray != null && trayIcon != null)
                                tray.add(trayIcon);
                            jframe.setVisible(false);
                            System.out.println("added to SystemTray");
                        } catch (AWTException ex) {
                            System.out.println("unable to add to system tray");
                        }
                    }
                    if (e.getNewState() == JFrame.MAXIMIZED_BOTH) {
                        if (tray != null && trayIcon != null)
                            tray.remove(trayIcon);
                        jframe.setVisible(true);
                        System.out.println("Tray icon removed");
                    }
                    if (e.getNewState() == JFrame.NORMAL) {
                        if (tray != null && trayIcon != null)
                            tray.remove(trayIcon);
                        jframe.setVisible(true);
                        System.out.println("Tray icon removed");
                    }
                }
            });
            jframe.setIconImage(image);
            jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

    public void displayMessage(String caption, String text, MessageType messageType) {
        trayIconHandler.addIfNeed();
        System.out.println("displayMessage : " + caption + " - " + text + " - " + messageType);

        try {
            trayIcon.displayMessage(caption, text, messageType);
        } catch (Exception ex) {
            displayMessage4Linux(caption, text, messageType);
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
