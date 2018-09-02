package gtu.swing.util;

import java.awt.event.MouseEvent;
import java.util.EventObject;

public class JMouseEventUtil {

    public static boolean buttonLeftClick(int clickTime, EventObject event_) {
        try {
            MouseEvent event = (MouseEvent) event_;
            if (!Button.LEFT.isClick(event)) {
                return false;
            }
            if (event.getClickCount() == clickTime) {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static boolean buttonMiddleClick(int clickTime, EventObject event_) {
        try {
            MouseEvent event = (MouseEvent) event_;
            if (!Button.MIDDLE.isClick(event)) {
                return false;
            }
            if (event.getClickCount() == clickTime) {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static boolean buttonRightClick(int clickTime, EventObject event_) {
        try {
            MouseEvent event = (MouseEvent) event_;
            if (!Button.RIGHT.isClick(event)) {
                return false;
            }
            if (event.getClickCount() == clickTime) {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    enum Button {
        LEFT(MouseEvent.BUTTON1), //
        MIDDLE(MouseEvent.BUTTON2), //
        RIGHT(MouseEvent.BUTTON3);

        final int btn;

        Button(int btn) {
            this.btn = btn;
        }

        boolean isClick(MouseEvent event) {
            if (event.getButton() == btn) {
                return true;
            }
            return false;
        }
    }
}
