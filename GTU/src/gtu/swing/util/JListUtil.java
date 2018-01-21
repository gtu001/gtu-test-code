package gtu.swing.util;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.JList;

public class JListUtil {

    private JList jList1;

    private JListUtil(JList jList1) {
        this.jList1 = jList1;
    }

    public static JListUtil newInstance(JList jList1) {
        return new JListUtil(jList1);
    }

    public void fixScrollBar() {
        jList1.setPreferredSize(null);
    }

    public static <T> T getLeadSelectionObject(JList jList1) {
        int pos = -1;
        if ((pos = jList1.getLeadSelectionIndex()) == -1) {
            return null;
        }
        return (T) jList1.getModel().getElementAt(pos);
    }

    public boolean isCorrectMouseClick(EventObject event) {
        if (jList1.getLeadSelectionIndex() == -1) {
            return false;
        }
        if (((MouseEvent) event).getClickCount() != 2) {
            return false;
        }
        return true;
    }

    public void defaultMouseClickOpenFile(EventObject event) {
        if (!isCorrectMouseClick(event)) {
            return;
        }
        Object obj = getLeadSelectionObject(jList1);
        if (obj != null) {
            try {
                Runtime.getRuntime().exec("cmd /c call \"" + obj + "\"");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public JListUtil addElement(Object... objects) {
        DefaultListModel model = (DefaultListModel) jList1.getModel();
        for (Object obj : objects) {
            model.addElement(obj);
        }
        return this;
    }

    public JListUtil addElement(Iterator<?> iterator) {
        DefaultListModel model = (DefaultListModel) jList1.getModel();
        for (; iterator.hasNext();) {
            model.addElement(iterator.next());
        }
        return this;
    }

    public JListUtil addElement(Enumeration<?> enu) {
        DefaultListModel model = (DefaultListModel) jList1.getModel();
        for (; enu.hasMoreElements();) {
            model.addElement(enu.nextElement());
        }
        return this;
    }

    public static DefaultListModel createModel(Object... objects) {
        DefaultListModel model = new DefaultListModel();
        for (Object obj : objects) {
            model.addElement(obj);
        }
        return model;
    }

    public static DefaultListModel createModel(Iterator<?> iterator) {
        DefaultListModel model = new DefaultListModel();
        for (; iterator.hasNext();) {
            model.addElement(iterator.next());
        }
        return model;
    }

    public static DefaultListModel createModel(Enumeration<?> enu) {
        DefaultListModel model = new DefaultListModel();
        for (; enu.hasMoreElements();) {
            model.addElement(enu.nextElement());
        }
        return model;
    }

    public static DefaultListModel createModel() {
        return new DefaultListModel();
    }

    public DefaultListModel getModel() {
        return (DefaultListModel) jList1.getModel();
    }

    /**
     * 清單 - 上移(按上) 下移(按下) 移除(按del)
     * 
     * @param evt
     */
    @SuppressWarnings("unchecked")
    public <T> JListUtil defaultJListKeyPressed(EventObject event) {
        T[] objects = (T[]) jList1.getSelectedValues();
        if (objects == null || objects.length == 0) {
            return this;
        }
        KeyEvent evt = (KeyEvent) event;
        DefaultListModel model = (DefaultListModel) jList1.getModel();
        int lastIndex = model.getSize() - 1;
        T swap = null;
        for (T current : objects) {
            int index = model.indexOf(current);
            switch (evt.getKeyCode()) {
            case 38:// up
                if (index != 0) {
                    swap = (T) model.getElementAt(index - 1);
                    model.setElementAt(swap, index);
                    model.setElementAt(current, index - 1);
                }
                break;
            case 40:// down
                if (index != lastIndex) {
                    swap = (T) model.getElementAt(index + 1);
                    model.setElementAt(swap, index);
                    model.setElementAt(current, index + 1);
                }
                break;
            case 127:// del
                model.removeElementAt(index);
                break;
            }
        }
        return this;
    }
}
