package _temp.swing;

/*from  ww  w  . j ava2 s  . c om*/
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class Main3 {
    public static void main(final String args[]) {
        JButton button = new JButton("Text Button");
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                String command = actionEvent.getActionCommand();
                System.out.println("Selected: " + command);
            }
        };

        button.setActionCommand("First");
        button.addActionListener(actionListener);

        JOptionPane.showMessageDialog(null, button);
    }
}