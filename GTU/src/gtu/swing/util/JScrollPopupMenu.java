package gtu.swing.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.MenuElement;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;

public class JScrollPopupMenu extends JPopupMenu {
    private static final long serialVersionUID = 4693231165732192396L;
    protected int maximumVisibleRows = 10;

    private MenuElement currentItem;

    public static void main(String[] args) {
        JFrame frame = JFrameUtil.createSimpleFrame(JScrollPopupMenu.class);
        JScrollPopupMenu menu = new JScrollPopupMenu();
        for (int ii = 0; ii < 20; ii++) {
            JMenuItem item = new JMenuItem("xx" + ii);
            menu.add(item);
        }
        frame.pack();
        frame.show();
        menu.show(frame, 0, 0);
    }

    public JScrollPopupMenu() {
        this(null);
    }

    public MenuElement getCurrentItem() {
        return this.currentItem;
    }

    public JScrollPopupMenu(String label) {
        super(label);
        setLayout(new ScrollPopupMenuLayout());

        super.add(getScrollBar());
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent event) {
                JScrollBar scrollBar = getScrollBar();
                int amount = (event.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) ? event.getUnitsToScroll() * scrollBar.getUnitIncrement()
                        : (event.getWheelRotation() < 0 ? -1 : 1) * scrollBar.getBlockIncrement();

                scrollBar.setValue(scrollBar.getValue() + amount);
                event.consume();
            }
        });

        addMenuKeyListener(new MenuKeyListener() {
            @Override
            public void menuKeyTyped(MenuKeyEvent arg0) {
            }

            @Override
            public void menuKeyReleased(MenuKeyEvent arg0) {
            }

            @Override
            public void menuKeyPressed(MenuKeyEvent arg0) {
                if (arg0.getKeyCode() == 38 || arg0.getKeyCode() == 40) {// 上下
                    setFocusable(true);
                    grabFocus();
                    MenuElement me = fixSelection(arg0);
                    if (me != null) {
                        System.out.println("currentItem = " + ((JMenuItem) me).getText());
                        currentItem = me;
                        getScrollBar().setValue(caculateScrollValue(me));
                    }
                } else {
                    setFocusable(false);
                }
            }

            private MenuElement fixSelection(MenuKeyEvent arg0) {
                MenuElement[] me = arg0.getMenuSelectionManager().getSelectedPath();
                if (me.length > 1) {
                    MenuElement[] me2 = JScrollPopupMenu.this.getSubElements();
                    for (int ii = 0; ii < me2.length; ii++) {
                        if (me2[ii] == me[1]) {
                            if (arg0.getKeyCode() == 38) {
                                int tmpPos = ii - 1;
                                if (tmpPos < 0) {
                                    tmpPos = me2.length - 1;
                                }
                                return me2[tmpPos];
                            } else if (arg0.getKeyCode() == 40) {
                                int tmpPos = ii + 1;
                                if (tmpPos >= me2.length) {
                                    tmpPos = 0;
                                }
                                return me2[tmpPos];
                            }
                        }
                    }
                }
                return null;
            }
        });
    }

    private int caculateScrollValue(MenuElement component) {
        List<JMenuItem> lst = new ArrayList<JMenuItem>();
        for (int ii = 0; ii < getAccessibleContext().getAccessibleChildrenCount(); ii++) {
            if (getAccessibleContext().getAccessibleChild(ii) instanceof JMenuItem) {
                JMenuItem m = (JMenuItem) getAccessibleContext().getAccessibleChild(ii);
                lst.add(m);
            }
        }
        int height = 0;
        for (int ii = 0; ii < lst.indexOf(component); ii++) {
            height += lst.get(ii).getHeight();
        }
        return height;
    }

    private JScrollBar popupScrollBar;

    protected JScrollBar getScrollBar() {
        if (popupScrollBar == null) {
            popupScrollBar = new JScrollBar(JScrollBar.VERTICAL);
            popupScrollBar.addAdjustmentListener(new AdjustmentListener() {
                @Override
                public void adjustmentValueChanged(AdjustmentEvent e) {
                    doLayout();
                    repaint();
                }
            });
            popupScrollBar.setVisible(false);
        }
        return popupScrollBar;
    }

    public int getMaximumVisibleRows() {
        return maximumVisibleRows;
    }

    public void setMaximumVisibleRows(int maximumVisibleRows) {
        this.maximumVisibleRows = maximumVisibleRows;
    }

    public void paintChildren(Graphics g) {
        Insets insets = getInsets();
        g.clipRect(insets.left, insets.top, getWidth(), getHeight() - insets.top - insets.bottom);
        super.paintChildren(g);
    }

    protected void addImpl(Component comp, Object constraints, int index) {
        super.addImpl(comp, constraints, index);

        if (maximumVisibleRows < getComponentCount() - 1) {
            getScrollBar().setVisible(true);
        }
    }

    public void remove(int index) {
        // can't remove the scrollbar
        ++index;

        super.remove(index);

        if (maximumVisibleRows >= getComponentCount() - 1) {
            getScrollBar().setVisible(false);
        }
    }

    public void show(Component invoker, int x, int y) {
        JScrollBar scrollBar = getScrollBar();
        if (scrollBar.isVisible()) {
            int extent = 0;
            int max = 0;
            int i = 0;
            int unit = -1;
            int width = 0;
            for (Component comp : getComponents()) {
                if (!(comp instanceof JScrollBar)) {
                    Dimension preferredSize = comp.getPreferredSize();
                    width = Math.max(width, preferredSize.width);
                    if (unit < 0) {
                        unit = preferredSize.height;
                    }
                    if (i++ < maximumVisibleRows) {
                        extent += preferredSize.height;
                    }
                    max += preferredSize.height;
                }
            }

            Insets insets = getInsets();
            int widthMargin = insets.left + insets.right;
            int heightMargin = insets.top + insets.bottom;
            scrollBar.setUnitIncrement(unit);
            scrollBar.setBlockIncrement(extent);
            scrollBar.setValues(0, heightMargin + extent, 0, heightMargin + max);

            width += scrollBar.getPreferredSize().width + widthMargin;
            int height = heightMargin + extent;

            setPopupSize(new Dimension(width, height));
        }

        super.show(invoker, x, y);
    }

    protected static class ScrollPopupMenuLayout implements LayoutManager {
        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            int visibleAmount = Integer.MAX_VALUE;
            Dimension dim = new Dimension();
            for (Component comp : parent.getComponents()) {
                if (comp.isVisible()) {
                    if (comp instanceof JScrollBar) {
                        JScrollBar scrollBar = (JScrollBar) comp;
                        visibleAmount = scrollBar.getVisibleAmount();
                    } else {
                        Dimension pref = comp.getPreferredSize();
                        dim.width = Math.max(dim.width, pref.width);
                        dim.height += pref.height;
                    }
                }
            }

            Insets insets = parent.getInsets();
            dim.height = Math.min(dim.height + insets.top + insets.bottom, visibleAmount);

            return dim;
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            int visibleAmount = Integer.MAX_VALUE;
            Dimension dim = new Dimension();
            for (Component comp : parent.getComponents()) {
                if (comp.isVisible()) {
                    if (comp instanceof JScrollBar) {
                        JScrollBar scrollBar = (JScrollBar) comp;
                        visibleAmount = scrollBar.getVisibleAmount();
                    } else {
                        Dimension min = comp.getMinimumSize();
                        dim.width = Math.max(dim.width, min.width);
                        dim.height += min.height;
                    }
                }
            }

            Insets insets = parent.getInsets();
            dim.height = Math.min(dim.height + insets.top + insets.bottom, visibleAmount);

            return dim;
        }

        @Override
        public void layoutContainer(Container parent) {
            Insets insets = parent.getInsets();

            int width = parent.getWidth() - insets.left - insets.right;
            int height = parent.getHeight() - insets.top - insets.bottom;

            int x = insets.left;
            int y = insets.top;
            int position = 0;

            for (Component comp : parent.getComponents()) {
                if ((comp instanceof JScrollBar) && comp.isVisible()) {
                    JScrollBar scrollBar = (JScrollBar) comp;
                    Dimension dim = scrollBar.getPreferredSize();
                    scrollBar.setBounds(x + width - dim.width, y, dim.width, height);
                    width -= dim.width;
                    position = scrollBar.getValue();
                }
            }

            y -= position;
            for (Component comp : parent.getComponents()) {
                if (!(comp instanceof JScrollBar) && comp.isVisible()) {
                    Dimension pref = comp.getPreferredSize();
                    comp.setBounds(x, y, width, pref.height);
                    y += pref.height;
                }
            }
        }
    }
}