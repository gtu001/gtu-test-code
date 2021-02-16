package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import gtu.swing.util.JCommonUtil;

public class SwingTransparentDlg extends JDialog {

    private static final long serialVersionUID = 3618609891018335257L;
    private final JPanel contentPanel = new JPanel();
    private Dimension dialogOrignSize;
    private Point dialogLocation;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            SwingTransparentDlg dialog = new SwingTransparentDlg();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SwingTransparentDlg newInstance() {
        SwingTransparentDlg dialog = new SwingTransparentDlg();
        try {
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialog;
    }

    private JPanel createJDialogResize(final int width, final int height, final char nswe) {
        JPanel resizePanel = new JPanel();
        resizePanel.setPreferredSize(new Dimension(width, height));
        DragJDialogResizeListener mDragJDialogResizeListener = new DragJDialogResizeListener(this, nswe);
        resizePanel.addMouseMotionListener(mDragJDialogResizeListener);
        resizePanel.addMouseListener(mDragJDialogResizeListener);
        return resizePanel;
    }

    /**
     * Create the dialog.
     */
    public SwingTransparentDlg() {
        this.applyOnTopUndecorated(this);

        setBounds(100, 100, 800, 350);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setLayout(new BorderLayout());

        contentPanel.add(new AlphaContainer(createJDialogResize(0, 5, 'n'), null), BorderLayout.NORTH);
        contentPanel.add(new AlphaContainer(createJDialogResize(0, 5, 's'), null), BorderLayout.SOUTH);
        contentPanel.add(new AlphaContainer(createJDialogResize(5, 0, 'w'), null), BorderLayout.WEST);
        contentPanel.add(new AlphaContainer(createJDialogResize(5, 0, 'e'), null), BorderLayout.EAST);

        // contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        {
            contentPanel.add(JCommonUtil.createScrollComponent(new JLabel()), BorderLayout.CENTER);
        }

        getContentPane().add(new AlphaContainer(contentPanel, this), BorderLayout.CENTER);
        {
            final JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(new AlphaContainer(buttonPane, this), BorderLayout.SOUTH);
            {
                final JButton cancelButton = new JButton("隱藏");
                cancelButton.setActionCommand("hide");
                buttonPane.add(cancelButton);

                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        JDialog dialog = SwingTransparentDlg.this;
                        Component comp = (Component) arg0.getSource();
                        if (dialogOrignSize == null && dialogLocation == null) {
                            cancelButton.setText("隱藏");
                            int PAD_SIZE = 5;
                            dialogLocation = dialog.getLocationOnScreen();
                            dialogOrignSize = dialog.getSize();
                            Point compLoc = comp.getLocationOnScreen();
                            int x = (int) compLoc.getX() - PAD_SIZE;
                            int y = (int) compLoc.getY() - PAD_SIZE;
                            int width = (int) (dialogOrignSize.getWidth() - ((compLoc.getX() - PAD_SIZE) - dialogLocation.getX()));
                            int height = (int) (dialogOrignSize.getHeight() - ((compLoc.getY() - PAD_SIZE) - dialogLocation.getY()));
                            dialog.setLocation(x, y);
                            dialog.setSize(new Dimension(width, height));
                        } else {
                            cancelButton.setText("顯示");
                            dialog.setLocation(dialogLocation);
                            dialog.setSize(dialogOrignSize);
                            dialogOrignSize = null;
                            dialogLocation = null;
                        }
                    }
                });
            }
            {
                JButton okButton = new JButton("關閉");
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);

                okButton.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent paramActionEvent) {
                        dispose();
                    }
                });
            }
        }

        JCommonUtil.setLocationToRightBottomCorner(this);
    }

    private void applyOnTopUndecorated(JDialog dialog) {
        dialog.setUndecorated(true);
        dialog.getRootPane().setOpaque(false);
        dialog.getContentPane().setBackground(new Color(0, 0, 0, 64));
        dialog.setBackground(new Color(0, 0, 0, 0));
        // dialog.setModal(true);
        dialog.setAlwaysOnTop(true);
        // dialog.pack();
    }

    private class AlphaContainer extends JComponent {
        private JComponent component;

        public AlphaContainer(JComponent component) {
            this(component, null);
        }

        public AlphaContainer(JComponent component, JDialog dialog) {
            this.component = component;
            this.component.setBackground(new Color(0, 0, 0, 64));// 0,0,0,0 全透明

            if (dialog != null) {
                DragJDialogListener drag = new DragJDialogListener(dialog);
                this.component.addMouseListener(drag);
                this.component.addMouseMotionListener(drag);
            }

            setLayout(new BorderLayout());
            setOpaque(false);
            component.setOpaque(false);
            add(component);
        }

        @Override
        public void paintComponent(Graphics g) {
            g.setColor(component.getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private class DragJDialogListener extends MouseInputAdapter {
        Point location;
        MouseEvent pressed;
        JDialog dialog;

        DragJDialogListener(JDialog dialog) {
            this.dialog = dialog;
        }

        public void mousePressed(MouseEvent me) {
            pressed = me;
        }

        public void mouseDragged(MouseEvent me) {
            location = dialog.getLocation();
            int x = location.x - pressed.getX() + me.getX();
            int y = location.y - pressed.getY() + me.getY();
            dialog.setLocation(x, y);

            applyDiff(x, y);
        }

        private void applyDiff(int finX, int finY) {
            if (dialogOrignSize != null && dialogLocation != null) {
                dialogLocation.x = finX - (dialogOrignSize.width - dialog.getSize().width);
                dialogLocation.y = finY - (dialogOrignSize.height - dialog.getSize().height);
            }
        }
    }

    private class DragListener extends MouseInputAdapter {
        Point location;
        MouseEvent pressed;

        public void mousePressed(MouseEvent me) {
            pressed = me;
        }

        public void mouseDragged(MouseEvent me) {
            Component component = me.getComponent();
            location = component.getLocation(location);
            int x = location.x - pressed.getX() + me.getX();
            int y = location.y - pressed.getY() + me.getY();
            component.setLocation(x, y);
        }
    }

    private class DragJDialogResizeListener extends MouseInputAdapter {
        Point location;
        MouseEvent pressed;
        JDialog dialog;
        char nswe;

        DragJDialogResizeListener(JDialog dialog, char nswe) {
            this.dialog = dialog;
            this.nswe = nswe;
        }

        public void mousePressed(MouseEvent me) {
            pressed = me;
        }

        public void mouseDragged(MouseEvent e) {
            Component component = e.getComponent();
            location = dialog.getLocation();
            int x1 = location.x - pressed.getX() + e.getX();
            int y1 = location.y - pressed.getY() + e.getY();
            Dimension orign = dialog.getSize();

            int x2 = (pressed.getX() - e.getX());
            int y2 = (pressed.getY() - e.getY());

            switch (nswe) {
            case 'n':
                dialog.setLocation(location.x, y1);
                dialog.setSize(new Dimension(orign.width, orign.height + (y2)));
                break;
            case 's':
                dialog.setSize(new Dimension(orign.width, orign.height + (y2 * -1)));
                break;
            case 'w':
                dialog.setLocation(x1, location.y);
                dialog.setSize(new Dimension(orign.width + (x2), orign.height));
                break;
            case 'e':
                dialog.setSize(new Dimension(orign.width + (x2 * -1), orign.height));
                break;
            }
        }
    }
}
