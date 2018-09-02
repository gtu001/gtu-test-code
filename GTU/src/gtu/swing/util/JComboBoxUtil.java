package gtu.swing.util;

import java.awt.Dimension;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

public class JComboBoxUtil {

    private JComboBox comboBox;

    private JComboBoxUtil(JComboBox comboBox) {
        this.comboBox = comboBox;
    }

    public static <T> DefaultComboBoxModel<T> createModel() {
        return new DefaultComboBoxModel<T>();
    }

    public static <T> DefaultComboBoxModel<T> createModel(T[] arry) {
        DefaultComboBoxModel<T> model = new DefaultComboBoxModel<T>();
        for (int ii = 0; ii < arry.length; ii++) {
            model.addElement(arry[ii]);
        }
        return model;
    }

    public static <T> DefaultComboBoxModel<T> createModel(List<T> arry) {
        DefaultComboBoxModel<T> model = new DefaultComboBoxModel<T>();
        for (int ii = 0; ii < arry.size(); ii++) {
            model.addElement(arry.get(ii));
        }
        return model;
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
