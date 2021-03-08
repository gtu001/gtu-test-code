package gtu.swing.util;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class DraggableTabbedPane extends JTabbedPane {

    private static final long serialVersionUID = 1L;
    private boolean dragging = false;
    private Image tabImage = null;
    private Point currentMouseLocation = null;
    private int draggedTabIndex = 0;
    private DraggableTabbedPaneMove draggableTabbedPaneMove = new DraggableTabbedPaneMove() {
        @Override
        public void beforeMoveFromTo(int fromIndex, int toIndex) {
            System.out.println("MoveFromTo : " + fromIndex + " -> " + toIndex);
        }

        @Override
        public void afterMoveFromTo(int fromIndex, int toIndex) {
            System.out.println("MoveFromTo : " + fromIndex + " -> " + toIndex);
        }
    };

    public DraggableTabbedPane() {
        super();
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {

                if (!dragging) {
                    // Gets the tab index based on the mouse position
                    int tabNumber = getUI().tabForCoordinate(DraggableTabbedPane.this, e.getX(), e.getY());

                    if (tabNumber >= 0) {
                        draggedTabIndex = tabNumber;
                        Rectangle bounds = getUI().getTabBounds(DraggableTabbedPane.this, tabNumber);

                        // Paint the tabbed pane to a buffer
                        Image totalImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
                        Graphics totalGraphics = totalImage.getGraphics();
                        totalGraphics.setClip(bounds);
                        // Don't be double buffered when painting to a static
                        // image.
                        setDoubleBuffered(false);
                        paintComponent(totalGraphics);

                        // Paint just the dragged tab to the buffer
                        tabImage = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_ARGB);
                        Graphics graphics = tabImage.getGraphics();
                        graphics.drawImage(totalImage, 0, 0, bounds.width, bounds.height, bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height, DraggableTabbedPane.this);

                        dragging = true;
                        repaint();
                    }
                } else {
                    currentMouseLocation = e.getPoint();

                    // Need to repaint
                    repaint();
                }

                super.mouseDragged(e);
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (dragging && checkPageRemoveTabAble(draggedTabIndex)) {
                    int tabNumber = getUI().tabForCoordinate(DraggableTabbedPane.this, e.getX(), 10);

                    if (tabNumber >= 0) {
                        // 更改 tab index
                        if (draggableTabbedPaneMove != null) {
                            draggableTabbedPaneMove.beforeMoveFromTo(draggedTabIndex, tabNumber);
                        }

                        Component comp = getComponentAt(draggedTabIndex);
                        String title = getTitleAt(draggedTabIndex);

                        removeTabAt(draggedTabIndex);
                        insertTab(title, null, comp, null, tabNumber);

                        // 更改 tab index
                        if (draggableTabbedPaneMove != null) {
                            draggableTabbedPaneMove.afterMoveFromTo(draggedTabIndex, tabNumber);
                        }
                    }
                }

                dragging = false;
                tabImage = null;
            }
        });
    }

    private boolean checkPageRemoveTabAble(int index) {
        try {
            Field f1 = JTabbedPane.class.getDeclaredField("pages");
            f1.setAccessible(true);
            java.util.List lst = (java.util.List) f1.get(this);
            if (lst.size() > index && lst.size() > this.getSelectedIndex()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public interface DraggableTabbedPaneMove {
        void beforeMoveFromTo(int fromIndex, int toIndex);

        void afterMoveFromTo(int fromIndex, int toIndex);
    }

    public void setEventOfMove(DraggableTabbedPaneMove draggableTabbedPaneMove) {
        this.draggableTabbedPaneMove = draggableTabbedPaneMove;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Are we dragging?
        if (dragging && currentMouseLocation != null && tabImage != null) {
            // Draw the dragged tab
            g.drawImage(tabImage, currentMouseLocation.x, currentMouseLocation.y, this);
        }
    }

    public static void main(String[] args) {
        JFrame test = new JFrame("Tab test");
        test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        test.setSize(400, 400);

        DraggableTabbedPane tabs = new DraggableTabbedPane();
        tabs.addTab("One", new JButton("One"));
        tabs.addTab("Two", new JButton("Two"));
        tabs.addTab("Three", new JButton("Three"));
        tabs.addTab("Four", new JButton("Four"));

        test.add(tabs);
        test.setVisible(true);
    }
}