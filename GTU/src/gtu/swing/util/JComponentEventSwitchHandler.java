package gtu.swing.util;

import java.lang.reflect.Field;

import javax.swing.JComponent;
import javax.swing.event.EventListenerList;

import org.apache.commons.lang.reflect.FieldUtils;

public class JComponentEventSwitchHandler {
    Object[] tempListenerArry;
    JComponent comp;

    public JComponentEventSwitchHandler(JComponent comp) {
        this.comp = comp;
    }

    public void removeListener() {
        try {
            Field listenerField = FieldUtils.getDeclaredField(JComponent.class, "listenerList", true);
            EventListenerList listener2 = (EventListenerList) listenerField.get(this.comp);
            Field listenerField2 = FieldUtils.getDeclaredField(EventListenerList.class, "listenerList", true);
            tempListenerArry = (Object[]) listenerField2.get(listener2);
            listenerField2.set(listener2, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addBackListener() {
        try {
            Field listenerField = FieldUtils.getDeclaredField(JComponent.class, "listenerList", true);
            EventListenerList listener2 = (EventListenerList) listenerField.get(this.comp);
            Field listenerField2 = FieldUtils.getDeclaredField(EventListenerList.class, "listenerList", true);
            listenerField2.set(listener2, tempListenerArry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}