package gtu.swing.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

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

    public static <T> List<T> getLeadSelectionArry(JList jList1) {
        if (jList1.getLeadSelectionIndex() == -1) {
            return null;
        }
        int[] posArry = jList1.getSelectedIndices();
        List<T> lst = new ArrayList<T>();
        for (int pos : posArry) {
            lst.add((T) jList1.getModel().getElementAt(pos));
        }
        return lst;
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

    public static boolean removeElement(JList jlist, Object value) {
        return ((DefaultListModel) jlist.getModel()).removeElement(value);
    }

    public DefaultListModel getModel() {
        if (!(jList1.getModel() instanceof DefaultListModel)) {
            DefaultListModel model = new DefaultListModel();
            for (int ii = 0; ii < jList1.getModel().getSize(); ii++) {
                Object val = jList1.getModel().getElementAt(ii);
                model.addElement(val);
            }
            jList1.setModel(model);
        }
        return (DefaultListModel) jList1.getModel();
    }

    public <T> JListUtil defaultJListKeyPressed(EventObject event) {
        return defaultJListKeyPressed(event, true);
    }

    /**
     * 清單 - 上移(按上) 下移(按下) 移除(按del)
     * 
     * @param evt
     */
    @SuppressWarnings("unchecked")
    public <T> JListUtil defaultJListKeyPressed(EventObject event, boolean isDeleteKeyRemove) {
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
                if (isDeleteKeyRemove) {
                    model.removeElementAt(index);
                }
                break;
            }
        }
        return this;
    }

    public interface ItemColorTextHandler {
        public Pair<String, Color> setColorAndText(Object value);
    }

    public JListUtil setItemColorTextProcess(final ItemColorTextHandler itemColorHandler) {
        jList1.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (itemColorHandler != null) {
                    Pair<String, Color> handler = itemColorHandler.setColorAndText(value);
                    if (handler != null) {
                        if (StringUtils.isNotBlank(handler.getLeft())) {
                            setText(StringUtils.trimToEmpty(handler.getLeft()));
                        }
                        if (handler.getRight() != null) {
                            setBackground(handler.getRight());
                        }
                    }
                    if (isSelected) {
                        setBackground(getBackground().darker());
                    }
                }
                return c;
            }
        });
        return this;
    }

    /**
     * 設定選項hover事件
     */
    public void applyOnHoverEvent(final ActionListener listener) {
        jList1.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (listener == null) {
                    return;
                }
                final int i = jList1.locationToIndex(e.getPoint());
                if (i > -1) {
                    final Rectangle bounds = jList1.getCellBounds(i, i + 1);
                    if (bounds.contains(e.getPoint())) {
                        Object hoverObj = jList1.getModel().getElementAt(i);
                        listener.actionPerformed(new ActionEvent(hoverObj, i, "selectIndex"));
                    }
                }
            }
        });
    }

    /**
     * 設定選全部
     */
    public void setSelectedAll() {
        List<Integer> lst = new ArrayList<Integer>();
        for (int ii = 0; ii < jList1.getModel().getSize(); ii++) {
            lst.add(ii);
        }
        jList1.setSelectedIndices(ArrayUtils.toPrimitive(lst.toArray(new Integer[0])));
    }

    public static void setUnselected(JList jList1) {
        jList1.clearSelection();
    }
}
