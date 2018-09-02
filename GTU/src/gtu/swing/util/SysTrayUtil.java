package gtu.swing.util;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.plaf.metal.MetalIconFactory;

import gtu.image.ImageUtil;

public class SysTrayUtil {

    public static void main(String[] args) {
    }

    TrayIcon trayIcon;
    SystemTray tray;
    TrayIconHandler trayIconHandler = new TrayIconHandler();

    public TrayIcon getTrayIcon() {
        return trayIcon;
    }

    public void createDefaultTray() {
        if (trayIcon != null) {
            return;
        }
        MenuItem menuItem = new MenuItem();
        menuItem.setLabel("test menuItem");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                System.out.println("nothing happen!");
            }
        });
        createDefaultTray("測試", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("good Exit!!");
            }
        }, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("good Exit!!");
            }
        }, menuItem);
    }

    public void createDefaultTray(final String title, final ActionListener iconClickActionListener, final ActionListener defaultExitActionListener, final MenuItem... menuItems) {
        if (SystemTray.isSupported() && trayIcon == null) {
            tray = SystemTray.getSystemTray();

            // trayIcon = new TrayIcon(getImage(), title);
            trayIcon = new TrayIcon(getSysTrayIcon("resource/images/ico/janna.ico"), title);

            PropertyChangeListener propListener = new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    TrayIcon oldTray[] = (TrayIcon[]) evt.getOldValue();
                    TrayIcon newTray[] = (TrayIcon[]) evt.getNewValue();
                    System.out.println(oldTray.length + " / " + newTray.length);
                }
            };
            tray.addPropertyChangeListener("trayIcons", propListener);

            trayIcon.setImageAutoSize(true);
            if (iconClickActionListener != null) {
                trayIcon.addActionListener(iconClickActionListener);
            }

            final PopupMenu jPopupMenu1 = new PopupMenu();
            if (menuItems != null && menuItems.length > 0) {
                for (MenuItem item : menuItems) {
                    jPopupMenu1.add(item);
                }
            }

            if (defaultExitActionListener != null) {
                MenuItem defaultExit = new MenuItem();
                defaultExit.setLabel("Exit");
                defaultExit.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            trayIcon.displayMessage(title, title + " exiting...", TrayIcon.MessageType.INFO);
                            try {
                                defaultExitActionListener.actionPerformed(e);
                                Thread.sleep(500);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                            tray.remove(trayIcon);
                        }catch(Throwable ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                jPopupMenu1.add(defaultExit);
            }

            trayIcon.setPopupMenu(jPopupMenu1);
            trayIcon.setImageAutoSize(true);

            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println("TrayIcon could not be added.");
            }
        }
    }

    private class TrayIconHandler {
        private boolean exists() {
            if (tray == null) {
                return false;
            }
            for (int ii = 0; ii < tray.getTrayIcons().length; ii++) {
                TrayIcon icon = tray.getTrayIcons()[ii];
                if (icon == trayIcon) {
                    return true;
                }
            }
            return false;
        }

        private void addIfNeed() {
            if (tray == null) {
                return;
            }
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
            if (tray == null) {
                return;
            }
            if (exists()) {
                tray.remove(trayIcon);
            }
        }
    }

    public static void removeTrayIcon(TrayIcon icon) {
        try {
            SystemTray.getSystemTray().remove(icon);
        } catch (java.awt.HeadlessException ex) {
        }
    }

    public void displayMessage(String caption, String text, MessageType messageType) {
        try {
            System.out.println("displayMessage : " + caption + " - " + text + " - " + messageType);
            if (tray == null) {
                return;
            }
            trayIconHandler.addIfNeed();
            trayIcon.displayMessage(caption, text, messageType);
            // trayIconHandler.removeIfExists();
        } catch (java.awt.HeadlessException ex) {
            System.err.println("Non UI Mode !!");
        }
    }

    public static Image createTrayImage() {
        Image bi = Toolkit.getDefaultToolkit().getImage("nicePic.JPG");
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            Dimension size = tray.getTrayIconSize();
            bi = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
            Graphics g = bi.getGraphics();
            g.setColor(Color.blue);
            g.fillRect(0, 0, size.width, size.height);
            g.setColor(Color.yellow);
            int ovalSize = (size.width < size.height) ? size.width : size.height;
            ovalSize /= 2;
            g.fillOval(size.width / 4, size.height / 4, ovalSize, ovalSize);
        }
        return bi;
    }

    private Image getSysTrayIcon(String imagePath) {
        if (imagePath.endsWith(".ico")) {
            return ImageUtil.getInstance().getIcoImage(imagePath);
        }
        return ImageUtil.getInstance().getDefaultImage(imagePath);
    }

    private Image getImage() throws HeadlessException {
        Icon defaultIcon = MetalIconFactory.getTreeHardDriveIcon();
        Image img = new BufferedImage(defaultIcon.getIconWidth(), defaultIcon.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        defaultIcon.paintIcon(new Panel(), img.getGraphics(), 0, 0);
        return img;
    }

    private SysTrayUtil() {
    }

    public static SysTrayUtil newInstance() {
        return new SysTrayUtil();
    }
}
