package gtu.swing.util;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.Callable;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class JComboBoxUtil {

    private JComboBox comboBox;

    private JComboBoxUtil(JComboBox comboBox) {
        this.comboBox = comboBox;
    }

    public static <T> DefaultComboBoxModel createModel() {
        return new DefaultComboBoxModel();
    }

    public static <T> DefaultComboBoxModel createModel(T... arry) {
        DefaultComboBoxModel<T> model = new DefaultComboBoxModel<T>();
        for (int ii = 0; ii < arry.length; ii++) {
            model.addElement(arry[ii]);
        }
        return model;
    }

    public static <T> DefaultComboBoxModel createModel(List<T> arry) {
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

    public static JComboBox createLookAndFeelComboBox(final Callable fetchJFrame) {
        final JComboBox comboBox = new JComboBox();
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            model.addElement(info.getName());
        }
        comboBox.setModel(model);
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                        String value = (String) comboBox.getSelectedItem();
                        if (StringUtils.equals(info.getName(), value)) {
                            javax.swing.UIManager.setLookAndFeel(info.getClassName());
                            JFrame jframe = (JFrame) fetchJFrame.call();
                            SwingUtilities.updateComponentTreeUI(jframe);
                            jframe.pack();
                            return;
                        }
                    }
                } catch (Exception e1) {
                    JCommonUtil.handleException(e1);
                }
            }
        });
        return comboBox;
    }
}
