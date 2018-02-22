package gtu.javafx.traynotification;

import java.awt.event.ActionListener;
import java.util.Random;

import gtu.javafx.traynotification.animations.AnimationType;
import javafx.scene.image.Image;

public class TrayNotificationHelper {
    private TrayNotificationHelper() {
    }

    public static TrayNotificationHelper newInstance() {
        return new TrayNotificationHelper();
    }

    private static javafx.embed.swing.JFXPanel _INST4SHOW;
    private String title;
    private String message;
    private NotificationType notificationType;
    private AnimationType animationType;
    private ActionListener onPanelClickCallback;
    private Image image;
    private String rectangleFill;

    public TrayNotificationHelper title(String title) {
        this.title = title;
        return this;
    }

    public TrayNotificationHelper message(String message) {
        this.message = message;
        return this;
    }

    public TrayNotificationHelper notificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
        return this;
    }

    public TrayNotificationHelper animationType(AnimationType animationType) {
        this.animationType = animationType;
        return this;
    }

    public TrayNotificationHelper onPanelClickCallback(ActionListener onPanelClickCallback) {
        this.onPanelClickCallback = onPanelClickCallback;
        return this;
    }

    public TrayNotificationHelper image(Image image) {
        this.image = image;
        return this;
    }

    public TrayNotificationHelper rectangleFill(String rectangleFill) {
        this.rectangleFill = rectangleFill;
        return this;
    }

    public void show(final long dismissTime) {
        try {
            // init javafx toolkit
            if (_INST4SHOW == null) {
                _INST4SHOW = new javafx.embed.swing.JFXPanel();
            }
            // keep the thread alive
            javafx.application.Platform.setImplicitExit(false);
            javafx.application.Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    final TrayNotification tray = new TrayNotification();
                    if (title != null) {
                        tray.setTitle(title);
                    }
                    if (message != null) {
                        tray.setMessage(message);
                    }
                    if (notificationType != null) {
                        tray.setNotificationType(notificationType);
                    }
                    if (animationType != null) {
                        tray.setAnimationType(animationType);
                    }
                    if (onPanelClickCallback != null) {
                        tray.setOnPanelClickCallBack(onPanelClickCallback);
                    }
                    if (rectangleFill != null) {
                        tray.setRectangleFill(javafx.scene.paint.Paint.valueOf(rectangleFill));
                    }
                    if (image != null) {
                        tray.setImage(image);
                    }

                    if (dismissTime <= 0) {
                        tray.showAndWait();
                    } else {
                        tray.showAndDismiss(javafx.util.Duration.valueOf(dismissTime + "ms"));
                    }
                }
            });
        } catch (Throwable ex) {
            throw new RuntimeException("JavaFx not support!", ex);
        }
    }

    public static class RandomColorFill {
        char[] charArry;

        private RandomColorFill() {
            charArry = "0123456789ABCDEF".toCharArray();
        }

        public static RandomColorFill getInstance() {
            return new RandomColorFill();
        }

        public String get() {
            StringBuilder sb = new StringBuilder("#");
            for (int ii = 0; ii < 6; ii++) {
                sb.append(charArry[new Random().nextInt(charArry.length)]);
            }
            return sb.toString();
        }
    }
}