package gtu.swing.util;

import java.awt.Dimension;

import javax.swing.JComboBox;

public class JComboBoxUtil {

    private JComboBox comboBox;

    private JComboBoxUtil(JComboBox comboBox) {
        this.comboBox = comboBox;
    }

    public static JComboBoxUtil newInstance(JComboBox comboBox) {
        return new JComboBoxUtil(comboBox);
    }

    public void setWidth(int width) {
        comboBox.setPreferredSize(new Dimension(width, 25));
        comboBox.setMinimumSize(comboBox.getPreferredSize());
        comboBox.setMaximumSize(comboBox.getPreferredSize());
    }

    public void setWidthByValue() {
        Object value = comboBox.getSelectedItem();
        if (value != null) {
            comboBox.setPrototypeDisplayValue(value);
        }
    }
}
