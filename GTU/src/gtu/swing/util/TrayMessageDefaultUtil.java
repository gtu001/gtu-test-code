package gtu.swing.util;

import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;

import gtu.image.ImageUtil;

public class TrayMessageDefaultUtil {

    public static void main(String[] args) {
        TrayMessageDefaultUtil.newInstance().displayMessage("VVVVVV", "CCCCCCCC", MessageType.ERROR);
        System.out.println("done..");
    }

    private SystemTray tray;
    private TrayIcon trayIcon;
    private TrayIconHandler trayIconHandler;
    
    public static TrayMessageDefaultUtil newInstance() {
        return new TrayMessageDefaultUtil();
    }
    
    private TrayMessageDefaultUtil() {
        tray = SystemTray.getSystemTray();
        trayIcon = new TrayIcon(ImageUtil.getInstance().getIcoImage("resource/images/ico/janna.ico")); 
        trayIconHandler = new TrayIconHandler();
    }

    public void displayMessage(String caption, String text, MessageType messageType) {
        trayIconHandler.addIfNeed();
        System.out.println("displayMessage : " + caption + " - " + text + " - " + messageType);
        trayIcon.displayMessage(caption, text, messageType);
        trayIconHandler.removeIfExists();
    }

    private class TrayIconHandler {
        private boolean exists() {
            for (int ii = 0; ii < tray.getTrayIcons().length; ii++) {
                TrayIcon icon = tray.getTrayIcons()[ii];
                if (icon == trayIcon) {
//                    System.out.println("需要移除icon");
                    return true;
                }
            }
//            System.out.println("找步道icon");
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
}
