package gtu.swing.util;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class JTextFieldUtil {

    public static void setText(JTextField textField, String strValue) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                textField.setText(strValue);
            }
        });
    }
}
