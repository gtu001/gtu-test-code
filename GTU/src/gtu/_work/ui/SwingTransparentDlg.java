package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputAdapter;

public class SwingTransparentDlg extends JDialog {

    private static final long serialVersionUID = 3618609891018335257L;
    private final JPanel contentPanel = new JPanel();

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

    /**
     * Create the dialog.
     */
    public SwingTransparentDlg() {
        this.applyOnTopUndecorated(this);

        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setLayout(new FlowLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        getContentPane().add(new AlphaContainer(contentPanel, this), BorderLayout.CENTER);
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(new AlphaContainer(buttonPane, this), BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
            }
        }
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
}
