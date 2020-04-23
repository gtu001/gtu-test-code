package gtu.swing.util;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EventObject;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;

import org.apache.commons.lang.StringUtils;

public class JPopupMenuUtil {

    JPopupMenu jPopupMenu1;
    Component component;
    EventObject event;
    Rectangle rect;
    List<JMenuItem> menuList;

    public static final ActionListener DO_THING_ACTIONLISTENER = new ActionListener() {
        public void actionPerformed(ActionEvent paramActionEvent) {
            System.out.println("JPopupMenuUtil.DO_THING_ACTIONLISTENER => do nothing!!");
        }
    };

    private JPopupMenuUtil(Component component, boolean isScrollable) {
        this.component = component;
        if (!isScrollable) {
            jPopupMenu1 = new JPopupMenu();
        } else {
            jPopupMenu1 = new JScrollPopupMenu();
        }
        menuList = new ArrayList<JMenuItem>();
    }

    public JPopupMenuUtil addJMenuItem(JMenuItem... items) {
        if (items == null || items.length == 0) {
            return this;
        }
        menuList.addAll(Arrays.asList(items));
        return this;
    }

    public JPopupMenuUtil applyEvent(EventObject event) {
        this.event = event;
        return this;
    }

    public JPopupMenuUtil applyEvent(Rectangle rect) {
        this.rect = rect;
        return this;
    }

    public JPopupMenuUtil show() {
        if (event instanceof MouseEvent) {
            MouseEvent e1 = (MouseEvent) event;
            for (JMenuItem menu : menuList) {
                jPopupMenu1.add(menu);
            }
            jPopupMenu1.show(component, e1.getX(), e1.getY());
        } else if (rect != null) {
            for (JMenuItem menu : menuList) {
                jPopupMenu1.add(menu);
            }
            jPopupMenu1.show(component, (int) rect.getX(), (int) rect.getY());
        } else {
            for (JMenuItem menu : menuList) {
                jPopupMenu1.add(menu);
            }
            jPopupMenu1.show(component, 0, 0);
        }
        return this;
    }

    public JPopupMenuUtil addJMenuItem(String text) {
        return addJMenuItem(text, true, null);
    }

    public JPopupMenuUtil addJMenuItem(String text, boolean enabled) {
        return addJMenuItem(text, enabled, null);
    }

    public JPopupMenuUtil addJMenuItem(String text, ActionListener actionListener) {
        return addJMenuItem(text, true, actionListener);
    }

    public JPopupMenuUtil addJMenuItem(String text, boolean enabled, ActionListener actionListener) {
        if (StringUtils.isEmpty(text)) {
            return this;
        }
        if (actionListener == null) {
            actionListener = DO_THING_ACTIONLISTENER;
        }
        JMenuItem item = new JMenuItem();
        item.setText(text);
        item.addActionListener(actionListener);
        item.setEnabled(enabled);
        menuList.add(item);
        return this;
    }

    public JPopupMenuUtil addJMenuItem(Collection<JMenuItem> list) {
        if (list == null || list.isEmpty()) {
            return this;
        }
        menuList.addAll(list);
        return this;
    }

    public static JPopupMenuUtil newInstance(Component component, boolean isScrollable) {
        return new JPopupMenuUtil(component, isScrollable);
    }

    public static JPopupMenuUtil newInstance(Component component) {
        return new JPopupMenuUtil(component, false);
    }

    public List<JMenuItem> getMenuList() {
        return menuList;
    }

    public void dismiss() {
        jPopupMenu1.setVisible(false);
    }

    public JPopupMenu getJPopupMenu() {
        return jPopupMenu1;
    }

    public void setLocation(Component component, int x, int y) {
        if (jPopupMenu1.isShowing()) {
            if (component != null) {
                jPopupMenu1.show(component, x, y);
            } else {
                jPopupMenu1.setLocation(x, y);
            }
        }
    }

    // ===============================================================================================================

    public static JMenuItem getCurrentSelectItem() {
        MenuElement[] path = MenuSelectionManager.defaultManager().getSelectedPath();
        if (path.length == 0) {
            System.out.println("No menus are opened or menu items selected");
        }
        for (int i = 0; i < path.length; i++) {
            Component c = path[i].getComponent();
            if (c instanceof JMenuItem) {
                JMenuItem mi = (JMenuItem) c;
                String label = mi.getText();
                System.out.println("-- select JMenuItem : " + label);
                return mi;
            }
        }
        return null;
    }

    public static void setCurrentSelectItem(JPopupMenu jPopupMenu1, Integer index, String text) {
        MenuElement[] me2 = jPopupMenu1.getSubElements();
        MenuElement selectItem = null;
        for (int ii = 0; ii < me2.length; ii++) {
            JMenuItem item = (JMenuItem) me2[ii].getComponent();
            if (index != null && index == ii) {
                selectItem = me2[ii];
                break;
            } else if (text != null && StringUtils.equalsIgnoreCase(text, item.getText())) {
                selectItem = me2[ii];
                break;
            }
        }
        if (selectItem != null) {
            MenuSelectionManager.defaultManager().setSelectedPath(new MenuElement[] { selectItem });
        } else {
            System.out.println("無法match所設定項目 : " + index + " / " + text);
        }
    }
}
