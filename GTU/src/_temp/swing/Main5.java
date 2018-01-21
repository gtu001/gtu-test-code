package _temp.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
// ww w.j  a v a 2s .co  m
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main5 extends JPanel {
    static String DISABLE_DIALOG_COMPONENTS = "Disable Dialog Components";
    static String ENABLE_DIALOG_COMPONENTS = "Enable Dialog Components";
    static String DISABLE_DIALOG = "Disable Dialog";
    static String ENABLE_DIALOG = "Enable Dialog";
    static int LOC_SHIFT = 150;
    MyDialog analyzer;

    public Main5(JFrame frame) {
        analyzer = new MyDialog(frame);
        analyzer.pack();
        analyzer.setLocationRelativeTo(frame);
        Point location = analyzer.getLocation();
        location = new Point(location.x - LOC_SHIFT, location.y - LOC_SHIFT);
        analyzer.setLocation(location);
        analyzer.setVisible(true);

        add(new JButton(new AbstractAction(DISABLE_DIALOG_COMPONENTS) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                AbstractButton btn = (AbstractButton) evt.getSource();
                if (btn.getText().equals(DISABLE_DIALOG_COMPONENTS)) {
                    btn.setText(ENABLE_DIALOG_COMPONENTS);
                    analyzer.setComponentEnabled(false);
                } else {
                    btn.setText(DISABLE_DIALOG_COMPONENTS);
                    analyzer.setComponentEnabled(true);
                }
            }
        }));
        add(new JButton(new AbstractAction(DISABLE_DIALOG) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                AbstractButton btn = (AbstractButton) evt.getSource();
                if (btn.getText().equals(DISABLE_DIALOG)) {
                    btn.setText(ENABLE_DIALOG);
                    analyzer.setEnabled(false);
                } else {
                    btn.setText(DISABLE_DIALOG);
                    analyzer.setEnabled(true);
                }
            }
        }));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Main5 mainPanel = new Main5(frame);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }
}

class MyDialog extends JDialog {
    public MyDialog(JFrame frame) {
        super(frame, "Dialog", false);
        JButton but = new JButton("test");
        setLayout(new FlowLayout());

        add(but);
        setPreferredSize(new Dimension(200, 200));
    }

    public void setComponentEnabled(boolean enabled) {
        setComponentEnabled(enabled, getContentPane());
    }

    private void setComponentEnabled(boolean enabled, Component component) {
        component.setEnabled(enabled);
        if (component instanceof Container) {
            Component[] components = ((Container) component).getComponents();
            if (components != null && components.length > 0) {
                for (Component heldComponent : components) {
                    setComponentEnabled(enabled, heldComponent);
                }
            }
        }
    }
}