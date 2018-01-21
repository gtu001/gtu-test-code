package gtu.swing.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.event.ListDataListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

import org.apache.commons.lang.StringUtils;


/**
 * 最終成功版
 */
public class AutoComboBox extends PlainDocument {
    JComboBox comboBox;
    ComboBoxModel model;
    JTextComponent editor;
    // flag to indicate if setSelectedItem has been called
    // subsequent calls to remove/insertString should be ignored
    
    private RemoveLinisterController removeLinisterController = new RemoveLinisterController();

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

    private AutoComboBox(final JComboBox comboBox) {
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

    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        String currentText = getText(0, getLength());
            
        // insert the string into the document
        super.insertString(offs, str, a);

        // lookup and select a matching item
        Object item = lookupItem(currentText);
        if (item != null) {
            
            removeLinisterController.removeListener(comboBox);
            
            model.setSelectedItem(item);
            
            removeLinisterController.addBackListener(comboBox);
            
        } else {
            // keep old item selected if there is no match
            item = comboBox.getSelectedItem();

            System.out.println("currentText -- " + currentText);

            // imitate no insert (later on offs will be incremented by
            // str.length(): selection won't move forward)
            offs = offs - str.length();
            // provide feedback to the user that his input has been received but
            // can not be accepted
            comboBox.getToolkit().beep(); // when available use:
                                          // UIManager.getLookAndFeel().provideErrorFeedback(comboBox);
        }

        if (item != null && item instanceof String && StringUtils.equals(currentText, (String) item)) {
            setText(item.toString());
        } 
    }

    private void setText(String text) throws BadLocationException {
        // remove all text and insert the completed string
        super.remove(0, getLength());
        super.insertString(0, text, null);
    }

    private Object lookupItem(String pattern) {
        Object selectedItem = model.getSelectedItem();
        // only search for a different item if the currently selected does not
        // match
        if (selectedItem != null && startsWithIgnoreCase(selectedItem.toString(), pattern)) {
            return selectedItem;
        } else {
            // iterate over all items
            for (int i = 0, n = model.getSize(); i < n; i++) {
                Object currentItem = model.getElementAt(i);
                // current item starts with the pattern?
                if (startsWithIgnoreCase(currentItem.toString(), pattern)) {
                    return currentItem;
                }
            }
        }
        // no item starts with the pattern => return null
        return null;
    }

    // checks if str1 starts with str2 - ignores case
    private boolean startsWithIgnoreCase(String str1, String str2) {
        return str1.toUpperCase().startsWith(str2.toUpperCase());
    }
    
    private class RemoveLinisterController {
        ListDataListener[] tempListeners;
        
        private void removeListener(JComboBox combox) {
            DefaultComboBoxModel model  = (DefaultComboBoxModel)combox.getModel();
            tempListeners = model.getListDataListeners();
            for( int ii = 0 ; ii < tempListeners.length ; ii ++) {
                model.removeListDataListener(tempListeners[ii]);
            }
        }
        
        private void addBackListener(JComboBox combox) {
            DefaultComboBoxModel model  = (DefaultComboBoxModel)combox.getModel();
            for( int ii = 0 ; ii < tempListeners.length ; ii ++) {
                model.addListDataListener(tempListeners[ii]);
            }
        }
    }

    public static AutoComboBox applyAutoComboBox(JComboBox comboBox, List<String> lst) {
        DefaultComboBoxModel m1 = new DefaultComboBoxModel();
        for (String s : lst) {
            m1.addElement(s);
        }
        comboBox.setModel(m1);
        comboBox.setEditable(true);
        JTextComponent editor = (JTextComponent) comboBox.getEditor().getEditorComponent();
        AutoComboBox autoComboBox = new AutoComboBox(comboBox);
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
                AutoComboBox.applyAutoComboBox(comboBox, searchList);

                // create and show a window containing the combo box
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(3);
                frame.getContentPane().add(comboBox);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
