package gtu.swing.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

public class HistoryComboBox extends PlainDocument {
    JComboBox comboBox;
    ComboBoxModel model;
    JTextComponent editor;
    // flag to indicate if setSelectedItem has been called
    // subsequent calls to remove/insertString should be ignored

    // 輸入不符合項目時要做的處理
    ActionListener addNewChoiceListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox box = (JComboBox) e.getSource();
            String currentSelect = (String) box.getSelectedItem();
            DefaultComboBoxModel model = (DefaultComboBoxModel) (box.getModel());
            if (box.getSelectedIndex() == -1) {
                model.insertElementAt(currentSelect, 0);
                box.setSelectedItem(currentSelect);
            }
        }
    };

    public JTextComponent getTextComponent() {
        return (JTextComponent) comboBox.getEditor().getEditorComponent();
    }

    private HistoryComboBox(final JComboBox comboBox) {
        this.comboBox = comboBox;
        model = comboBox.getModel();
        editor = (JTextComponent) comboBox.getEditor().getEditorComponent();
        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addNewChoiceListener.actionPerformed(e);
            }
        });
        editor.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (comboBox.isDisplayable())
                    comboBox.setPopupVisible(true);
            }
        });
    }

    public static HistoryComboBox applyComboBox(JComboBox comboBox, List<String> lst) {
        DefaultComboBoxModel m1 = new DefaultComboBoxModel();
        for (String s : lst) {
            m1.addElement(s);
        }
        comboBox.setModel(m1);
        comboBox.setEditable(true);
        JTextComponent editor = (JTextComponent) comboBox.getEditor().getEditorComponent();
        HistoryComboBox autoComboBox = new HistoryComboBox(comboBox);
        editor.setDocument(autoComboBox);
        return autoComboBox;
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                List<String> searchList = new ArrayList<String>();
                for (int ii = (int) 'a'; ii <= (int) 'z'; ii++) {
                    String v = String.valueOf((char) ii);
                    searchList.add(v + v + v + "1");
                    searchList.add(v + v + v + "2");
                    searchList.add(v + v + v + "3");
                }

                JComboBox comboBox = new JComboBox();
                HistoryComboBox.applyComboBox(comboBox, searchList);

                // create and show a window containing the combo box
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(3);
                frame.getContentPane().add(comboBox);
                frame.pack();
                 gtu.swing.util.JFrameUtil.setVisible(true,frame);
            }
        });
    }
}
