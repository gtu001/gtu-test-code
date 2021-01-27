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

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;

import org.apache.commons.lang.StringUtils;

import gtu._work.ui.JMenuBarUtil.JMenuAppender;

public class JPopupMenuUtil {

    JPopupMenu jPopupMenu1;
    Component component;
    EventObject event;
    Rectangle rect;
    List<JMenuItemHolder> menuList;

    private class JMenuItemHolder {
        JMenuItem item;
        boolean isSeparator;
        Component component;
    }

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
        menuList = new ArrayList<JMenuItemHolder>();
    }

    public JPopupMenuUtil addJMenuItem(JMenuItem... items) {
        if (items == null || items.length == 0) {
            return this;
        }
        List<JMenuItemHolder> lst = new ArrayList<JMenuItemHolder>();
        for (JMenuItem item : items) {
            JMenuItemHolder item2 = new JMenuItemHolder();
            item2.item = item;
            lst.add(item2);
        }
        menuList.addAll(lst);
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
            appendToJPopupMenu1();
            jPopupMenu1.show(component, e1.getX(), e1.getY());
        } else if (rect != null) {
            appendToJPopupMenu1();
            jPopupMenu1.show(component, (int) rect.getX(), (int) rect.getY());
        } else {
            appendToJPopupMenu1();
            jPopupMenu1.show(component, 0, 0);
        }
        return this;
    }

    private void appendToJPopupMenu1() {
        for (JMenuItemHolder menu : menuList) {
            if (menu.component != null) {
                jPopupMenu1.add(menu.component);
                continue;
            }

            if (menu.isSeparator) {
                jPopupMenu1.addSeparator();
                continue;
            }

            jPopupMenu1.add(menu.item);
        }
    }

    public JPopupMenuUtil addSeparator() {
        return addJMenuItem("", false, true, null, null);
    }

    public JPopupMenuUtil addJMenuItem(String text) {
        return addJMenuItem(text, true, false, null, null);
    }

    public JPopupMenuUtil addJMenuItem(String text, boolean enabled) {
        return addJMenuItem(text, enabled, false, null, null);
    }

    public JPopupMenuUtil addJMenuItem(String text, ActionListener actionListener) {
        return addJMenuItem(text, true, false, actionListener, null);
    }

    public JPopupMenuUtil addJMenuItem(Component component1) {
        return addJMenuItem("", true, false, null, component1);
    }

    public JPopupMenuUtil addJMenuItem(String text, boolean enabled, boolean isSeparator, ActionListener actionListener, Component component1) {
        if (actionListener == null) {
            actionListener = DO_THING_ACTIONLISTENER;
        }
        JMenuItem item = new JMenuItem();
        item.setText(text);
        item.addActionListener(actionListener);
        item.setEnabled(enabled);
        JMenuItemHolder item2 = new JMenuItemHolder();
        item2.item = item;
        item2.isSeparator = isSeparator;
        item2.component = component1;
        menuList.add(item2);
        return this;
    }

    public JPopupMenuUtil addJMenuItem(Collection<JMenuItem> list) {
        if (list == null || list.isEmpty()) {
            return this;
        }
        List<JMenuItemHolder> lst = new ArrayList<JMenuItemHolder>();
        for (JMenuItem item : list) {
            JMenuItemHolder item2 = new JMenuItemHolder();
            item2.item = item;
            lst.add(item2);
        }
        menuList.addAll(lst);
        return this;
    }

    public static JPopupMenuUtil newInstance(Component component, boolean isScrollable) {
        return new JPopupMenuUtil(component, isScrollable);
    }

    public static JPopupMenuUtil newInstance(Component component) {
        return new JPopupMenuUtil(component, false);
    }

    public List<JMenuItem> getMenuList() {
        List<JMenuItem> lst = new ArrayList<JMenuItem>();
        for (JMenuItemHolder item : menuList) {
            lst.add(item.item);
        }
        return lst;
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
