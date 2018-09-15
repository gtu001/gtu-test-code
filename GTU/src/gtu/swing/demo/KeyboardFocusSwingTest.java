package gtu.swing.demo;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

//http://www.coderanch.com/t/342205/GUI/java/Tab-order-swing-components
public class KeyboardFocusSwingTest {

    private static final long serialVersionUID = 1L;
    private Component[] focusList;
    private int focusNumber = 0;
    private JFrame frame;

    public KeyboardFocusSwingTest() {
        JTextField tf1 = new JTextField(5);
        JTextField tf2 = new JTextField(5);
        JTextField tf3 = new JTextField(5);
        JButton b1 = new JButton("B1");
        JButton b2 = new JButton("B2");
        tf2.setEnabled(false);
        focusList = new Component[] { tf1, b1, tf2, b2, tf3 };
        JPanel panel = new JPanel(new GridLayout(5, 1));
        panel.add(tf1);
        panel.add(b1);
        panel.add(tf2);
        panel.add(b2);
        panel.add(tf3);
        frame = new JFrame();
        frame.setFocusTraversalPolicy(new MyFocusTraversalPolicy());
        frame.add(panel);
        frame.pack();
        frame.setLocation(150, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         gtu.swing.util.JFrameUtil.setVisible(true,frame);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

            public boolean dispatchKeyEvent(KeyEvent ke) {
                if (ke.getID() == KeyEvent.KEY_PRESSED) {
                    if (ke.getKeyCode() == KeyEvent.VK_TAB) {
                        Component comp = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
                        if (comp.isEnabled() == false) {
                            if (ke.isShiftDown()) {
                                KeyboardFocusManager.getCurrentKeyboardFocusManager().focusPreviousComponent();
                            } else {
                                KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
                            }
                        }
                    }
                }
                return false;
            }
        });
    }

    private class MyFocusTraversalPolicy extends FocusTraversalPolicy {

        public Component getComponentAfter(Container focusCycleRoot, Component aComponent) {
            focusNumber = (focusNumber + 1) % focusList.length;
            return focusList[focusNumber];
        }

        public Component getComponentBefore(Container focusCycleRoot, Component aComponent) {
            focusNumber = (focusList.length + focusNumber - 1) % focusList.length;
            return focusList[focusNumber];
        }

        public Component getDefaultComponent(Container focusCycleRoot) {
            return focusList[0];
        }

        public Component getLastComponent(Container focusCycleRoot) {
            return focusList[focusList.length - 1];
        }

        public Component getFirstComponent(Container focusCycleRoot) {
            return focusList[0];
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                KeyboardFocusSwingTest testing = new KeyboardFocusSwingTest();
            }
        });
    }
}
