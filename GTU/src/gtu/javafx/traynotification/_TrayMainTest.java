package gtu.javafx.traynotification;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import gtu.javafx.traynotification.animations.AnimationType;
import gtu.swing.util.JCommonUtil;

public class _TrayMainTest {

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    TrayNotificationHelper.newInstance()//
                            .title("title")//
                            .message("message")//
                            .notificationType(NotificationType.INFORMATION)//
//                            .rectangleFill("#FF0000")//
                            .rectangleFill(TrayNotificationHelper.RandomColorFill.getInstance().get())
                            .animationType(AnimationType.FADE)//
                            .onPanelClickCallback(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    System.exit(1);
                                }
                            }).show(1500);
                    try {
                        Thread.sleep(10000L);
                    } catch (InterruptedException e1) {
                    }
                }
            }
        }).start();
    }
}
