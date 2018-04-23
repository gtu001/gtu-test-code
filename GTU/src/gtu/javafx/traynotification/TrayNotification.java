package gtu.javafx.traynotification;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.collections.Predicate;

import gtu.javafx.traynotification.animations.AnimationProvider;
import gtu.javafx.traynotification.animations.AnimationType;
import gtu.javafx.traynotification.animations.FadeAnimation;
import gtu.javafx.traynotification.animations.PopupAnimation;
import gtu.javafx.traynotification.animations.SlideAnimation;
import gtu.javafx.traynotification.animations.TrayAnimation;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public final class TrayNotification {

    @FXML
    private Label lblTitle, lblMessage, lblClose;
    @FXML
    private ImageView imageIcon;
    @FXML
    private Rectangle rectangleColor;
    @FXML
    private AnchorPane rootNode;

    private CustomStage stage;
    private NotificationType notificationType;
    private AnimationType animationType;
    private EventHandler<ActionEvent> onDismissedCallBack, onShownCallback;
    private ActionListener onPanelClickCallBack;
    private TrayAnimation animator;
    private AnimationProvider animationProvider;

    /**
     * Initializes an instance of the tray notification object
     * 
     * @param title
     *            The title text to assign to the tray
     * @param body
     *            The body text to assign to the tray
     * @param img
     *            The image to show on the tray
     * @param rectangleFill
     *            The fill for the rectangle
     */
    public TrayNotification(String title, String body, Image img, Paint rectangleFill) {
        initTrayNotification(title, body, NotificationType.CUSTOM);

        setImage(img);
        setRectangleFill(rectangleFill);
    }

    /**
     * Initializes an instance of the tray notification object
     * 
     * @param title
     *            The title text to assign to the tray
     * @param body
     *            The body text to assign to the tray
     * @param notificationType
     *            The notification type to assign to the tray
     */
    public TrayNotification(String title, String body, NotificationType notificationType) {
        initTrayNotification(title, body, notificationType);
    }

    /**
     * Initializes an empty instance of the tray notification
     */
    public TrayNotification() {
        initTrayNotification("", "", NotificationType.CUSTOM);
    }

    private void initTrayNotification(String title, String message, NotificationType type) {
        try {
            // javafx.application.Application.launch();

            URL fxmlUrl = getClass().getClassLoader().getResource("gtu/javafx/traynotification/resources/TrayNotification.fxml");
            // System.out.println(fxmlUrl);
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);

            fxmlLoader.setController(this);
            fxmlLoader.load();

            initStage();
            initAnimations();

            setTray(title, message, type);

        } catch (Exception e) {
            throw new RuntimeException("initTrayNotification ERR " + e.getMessage(), e);
        }
    }

    private void initAnimations() {

        animationProvider = new AnimationProvider(new FadeAnimation(stage), new SlideAnimation(stage), new PopupAnimation(stage));

        // Default animation type
        setAnimationType(AnimationType.SLIDE);
    }

    private void initStage() {

        stage = new CustomStage(rootNode, StageStyle.UNDECORATED);
        stage.setScene(new Scene(rootNode));
        stage.setAlwaysOnTop(true);
        stage.setLocation(stage.getBottomRight());

        rootNode.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (onPanelClickCallBack != null) {
                    onPanelClickCallBack.actionPerformed(new java.awt.event.ActionEvent(TrayNotification.this, -1, "onPanelClickCallBack"));
                }
            }
        });

        lblClose.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                dismiss();
            }
        });
    }

    public void setNotificationType(NotificationType nType) {

        notificationType = nType;

        URL imageLocation = null;
        String paintHex = null;

        switch (nType) {

        case INFORMATION:
            imageLocation = getClass().getClassLoader().getResource("gtu/javafx/traynotification/resources/info.png");
            paintHex = "#2C54AB";
            break;

        case NOTICE:
            imageLocation = getClass().getClassLoader().getResource("gtu/javafx/traynotification/resources/notice.png");
            paintHex = "#8D9695";
            break;

        case SUCCESS:
            imageLocation = getClass().getClassLoader().getResource("gtu/javafx/traynotification/resources/success.png");
            paintHex = "#009961";
            break;

        case WARNING:
            imageLocation = getClass().getClassLoader().getResource("gtu/javafx/traynotification/resources/warning.png");
            paintHex = "#E23E0A";
            break;

        case ERROR:
            imageLocation = getClass().getClassLoader().getResource("gtu/javafx/traynotification/resources/error.png");
            paintHex = "#CC0033";
            break;

        case CUSTOM:
            return;
        }

        setRectangleFill(Paint.valueOf(paintHex));
        setImage(new Image(imageLocation.toString()));
        setTrayIcon(imageIcon.getImage());
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setTray(String title, String message, NotificationType type) {
        setTitle(title);
        setMessage(message);
        setNotificationType(type);
    }

    public void setTray(String title, String message, Image img, Paint rectangleFill, AnimationType animType) {
        setTitle(title);
        setMessage(message);
        setImage(img);
        setRectangleFill(rectangleFill);
        setAnimationType(animType);
    }

    public boolean isTrayShowing() {
        return animator.isShowing();
    }

    /**
     * Shows and dismisses the tray notification
     * 
     * @param dismissDelay
     *            How long to delay the start of the dismiss animation
     */
    public void showAndDismiss(Duration dismissDelay) {

        if (isTrayShowing()) {
            dismiss();
        } else {
            stage.show();
            
            if(stage.isFocused()) {
                stage.setFocused(false);
            }

            onShown();
            animator.playSequential(dismissDelay);
        }

        onDismissed();
    }

    /**
     * Displays the notification tray
     */
    public void showAndWait() {

        if (!isTrayShowing()) {
            stage.show();

            animator.playShowAnimation();

            onShown();
        }
    }

    /**
     * Dismisses the notifcation tray
     */
    public void dismiss() {

        if (isTrayShowing()) {
            animator.playDismissAnimation();
            onDismissed();
        }
    }

    private void onShown() {
        if (onShownCallback != null)
            onShownCallback.handle(new ActionEvent());
    }

    private void onDismissed() {
        if (onDismissedCallBack != null)
            onDismissedCallBack.handle(new ActionEvent());
    }

    /**
     * Sets an action event for when the tray has been dismissed
     * 
     * @param event
     *            The event to occur when the tray has been dismissed
     */
    public void setOnDismiss(EventHandler<ActionEvent> event) {
        onDismissedCallBack = event;
    }

    /**
     * Sets an action event for when the tray has been shown
     * 
     * @param event
     *            The event to occur after the tray has been shown
     */
    public void setOnShown(EventHandler<ActionEvent> event) {
        onShownCallback = event;
    }

    /**
     * Sets a new task bar image for the tray
     * 
     * @param img
     *            The image to assign
     */
    public void setTrayIcon(Image img) {
        stage.getIcons().clear();
        stage.getIcons().add(img);
    }

    public Image getTrayIcon() {
        return stage.getIcons().get(0);
    }

    /**
     * Sets a title to the tray
     * 
     * @param txt
     *            The text to assign to the tray icon
     */
    public void setTitle(String txt) {
        lblTitle.setText(txt);
    }

    public String getTitle() {
        return lblTitle.getText();
    }

    /**
     * Sets the message for the tray notification
     * 
     * @param txt
     *            The text to assign to the body of the tray notification
     */
    public void setMessage(String txt) {
        lblMessage.setText(txt);
    }

    public String getMessage() {
        return lblMessage.getText();
    }

    public void setImage(Image img) {
        imageIcon.setImage(img);

        setTrayIcon(img);
    }

    public Image getImage() {
        return imageIcon.getImage();
    }

    public void setRectangleFill(Paint value) {
        rectangleColor.setFill(value);
    }

    public Paint getRectangleFill() {
        return rectangleColor.getFill();
    }

    public void setOnPanelClickCallBack(ActionListener event) {
        this.onPanelClickCallBack = event;
    }

    public void setAnimationType(final AnimationType type) {
        // animator = animationProvider.findFirstWhere(a -> a.getAnimationType()
        // == type);
        animator = animationProvider.findFirstWhere(new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                return ((TrayAnimation) object).getAnimationType() == type;
            }
        });
    }

    public AnimationType getAnimationType() {
        return animationType;
    }
}