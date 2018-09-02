package gtu.swing.util;

import java.awt.Component;
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

import org.apache.commons.lang.StringUtils;

public class JPopupMenuUtil {

    JPopupMenu jPopupMenu1;
    Component component;
    MouseEvent event;
    List<JMenuItem> menuList;

    public static final ActionListener DO_THING_ACTIONLISTENER = new ActionListener() {
        public void actionPerformed(ActionEvent paramActionEvent) {
            System.out.println("JPopupMenuUtil.DO_THING_ACTIONLISTENER => do nothing!!");
        }
    };

    private JPopupMenuUtil(Component component) {
        this.component = component;
        jPopupMenu1 = new JPopupMenu();
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
        this.event = (MouseEvent) event;
        return this;
    }

    public JPopupMenuUtil show() {
        if (event.getButton() == 3) {
            for (JMenuItem menu : menuList) {
                jPopupMenu1.add(menu);
            }
            jPopupMenu1.show(component, event.getX(), event.getY());
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
        if(actionListener == null){
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

    public static JPopupMenuUtil newInstance(Component component) {
        return new JPopupMenuUtil(component);
    }
    
    public List<JMenuItem> getMenuList() {
        return menuList;
    }
}
