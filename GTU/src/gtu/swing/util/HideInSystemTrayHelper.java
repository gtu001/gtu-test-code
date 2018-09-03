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
import javax.swing.UIManager;

import org.apache.commons.lang3.StringUtils;

import gtu.image.ImageUtil;

public class HideInSystemTrayHelper {

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

        try {
            System.out.println("setting look and feel");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Unable to set LookAndFeel");
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
                            tray.add(trayIcon);
                            jframe.setVisible(false);
                            System.out.println("added to SystemTray");
                        } catch (AWTException ex) {
                            System.out.println("unable to add to tray");
                        }
                    }
                    if (e.getNewState() == 7) {
                        try {
                            tray.add(trayIcon);
                            jframe.setVisible(false);
                            System.out.println("added to SystemTray");
                        } catch (AWTException ex) {
                            System.out.println("unable to add to system tray");
                        }
                    }
                    if (e.getNewState() == JFrame.MAXIMIZED_BOTH) {
                        tray.remove(trayIcon);
                        jframe.setVisible(true);
                        System.out.println("Tray icon removed");
                    }
                    if (e.getNewState() == JFrame.NORMAL) {
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
            for (int ii = 0; ii < tray.getTrayIcons().length; ii++) {
                TrayIcon icon = tray.getTrayIcons()[ii];
                if (icon == trayIcon) {
                    return true;
                }
            }
            return false;
        }

        private void addIfNeed() {
            if (!exists()) {
                try {
                    tray.add(trayIcon);
                } catch (AWTException e) {
                    e.printStackTrace();
                }
            }
        }

        @Deprecated
        private void removeIfExists() {
            if (exists()) {
                tray.remove(trayIcon);
            }
        }
    }

    public void displayMessage(String caption, String text, MessageType messageType) {
        trayIconHandler.addIfNeed();
        System.out.println("displayMessage : " + caption + " - " + text + " - " + messageType);
        trayIcon.displayMessage(caption, text, messageType);
        // trayIconHandler.removeIfExists();
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
