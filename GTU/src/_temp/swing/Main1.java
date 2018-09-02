package _temp.swing;

import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.ActionEvent;
//from   w w w . java  2s . com
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Main1 {
    public static void main(String[] args) {
        MainPanelGen mainPanelGen = new MainPanelGen();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanelGen.getMainPanel());
        frame.pack();
        frame.setVisible(true);
    }
}

class MainPanelGen {
    JPanel mainPanel = new JPanel();
    JTextField field = new JTextField(10);
    JButton btn = new JButton(new BtnActn());
    JDialog dialog;
    DialogPanel dialogPanel = new DialogPanel();

    public MainPanelGen() {
        mainPanel.add(field);
        mainPanel.add(btn);
        field.setEditable(false);
        field.setFocusable(false);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private class BtnActn extends AbstractAction {
        BtnActn() {
            super("Button");
        }

        @Override
        public void actionPerformed(ActionEvent arg0) {
            if (dialog == null) {
                Window win = SwingUtilities.getWindowAncestor(mainPanel);
                if (win != null) {
                    dialog = new JDialog(win, "My Dialog", Dialog.ModalityType.APPLICATION_MODAL);
                    dialog.getContentPane().add(dialogPanel);
                    dialog.pack();
                    dialog.setLocationRelativeTo(null);
                }
            }
            dialog.setVisible(true); // here the modal dialog takes over
            System.out.println(dialogPanel.getFieldText());
            field.setText(dialogPanel.getFieldText());
        }
    }
}

class DialogPanel extends JPanel {
    private JTextField field = new JTextField(10);
    private JButton exitBtn = new JButton(new ExitBtnAxn("Exit"));

    public DialogPanel() {
        add(field);
        add(exitBtn);
    }

    public String getFieldText() {
        return field.getText();
    }

    class ExitBtnAxn extends AbstractAction {

        public ExitBtnAxn(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent arg0) {
            Window win = SwingUtilities.getWindowAncestor(DialogPanel.this);
            if (win != null) {
                win.dispose();
            }
        }
    }
}