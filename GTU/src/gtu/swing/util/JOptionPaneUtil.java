package gtu.swing.util;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

/**
 * 簡化JOptionPane
 * 
 * 2012/1/6
 * 
 * @author Troy
 */
public class JOptionPaneUtil {

    public static void main(String[] args) {
        System.out.println("done...");
    }

    private int messageType = JOptionPane.INFORMATION_MESSAGE;
    private int confirmBtn = JOptionPane.DEFAULT_OPTION;

    private JOptionPaneUtil() {
    }

    public static JOptionPaneUtil newInstance() {
        return new JOptionPaneUtil();
    }

    /**
     * 顯示JOptionPane.showConfirmDialog
     */
    public ComfirmDialogResult showConfirmDialog(Object message, String title) {
        int result = JOptionPane.showConfirmDialog(null, message, title, confirmBtn, messageType, null);
        return ComfirmDialogResult.VALUE_TO_ENUM.get(result);
    }

    /**
     * 顯示JOptionPane.showInputDialog
     * 
     * @return
     */
    public String showInputDialog(Object message, String title) {
        return JOptionPane.showInputDialog(null, message, title, messageType);
    }

    /**
     * 顯示JOptionPane.showInputDialog
     * 
     * @return
     */
    public Object showInputDialog(Object message, String title, Object defaultValue) {
        return JOptionPane.showInputDialog(null, message, title, messageType, null, null, defaultValue);
    }

    /**
     * 顯示JOptionPane.showInputDialog
     * 
     * @return
     */
    public Object showInputDialog_drowdown(Object message, String title, Object[] drowdown, Object defaultValue) {
        return JOptionPane.showInputDialog(null, message, title, messageType, null, drowdown, defaultValue);
    }

    /**
     * 顯示JOptionPane.showMessageDialog
     */
    public void showMessageDialog(Object message, String title) {
        JOptionPane.showMessageDialog(null, message, title, messageType);
    }

    /**
     * confirmDialog 按鈕為 JOptionPane.DEFAULT_OPTION
     * 
     * @return
     */
    public JOptionPaneUtil confirmButtonDefault() {
        this.confirmBtn = JOptionPane.DEFAULT_OPTION;
        return this;
    }

    /**
     * confirmDialog 按鈕為 JOptionPane.YES_NO_OPTION
     * 
     * @return
     */
    public JOptionPaneUtil confirmButtonYesNo() {
        this.confirmBtn = JOptionPane.YES_NO_OPTION;
        return this;
    }

    /**
     * confirmDialog 按鈕為 JOptionPane.YES_NO_CANCEL_OPTION
     * 
     * @return
     */
    public JOptionPaneUtil confirmButtonYesNoCancel() {
        this.confirmBtn = JOptionPane.YES_NO_CANCEL_OPTION;
        return this;
    }

    /**
     * confirmDialog 按鈕為 JOptionPane.OK_CANCEL_OPTION
     * 
     * @return
     */
    public JOptionPaneUtil confirmButtonOkCancel() {
        this.confirmBtn = JOptionPane.OK_CANCEL_OPTION;
        return this;
    }

    public static enum ComfirmDialogResult {
        YES_OK_OPTION(JOptionPane.YES_OPTION), //
        NO_OPTION(JOptionPane.NO_OPTION), //
        CANCEL_OPTION(JOptionPane.CANCEL_OPTION), //
        CLOSED_OPTION(JOptionPane.CLOSED_OPTION),//
        ;

        private static Map<Integer, ComfirmDialogResult> VALUE_TO_ENUM = new HashMap<Integer, ComfirmDialogResult>();
        static {
            for (ComfirmDialogResult c : ComfirmDialogResult.values()) {
                VALUE_TO_ENUM.put(c.value, c);
            }
        }
        private int value;

        ComfirmDialogResult(int value) {
            this.value = value;
        }
    }

    /**
     * ERROR_MESSAGE
     * 
     * @return
     */
    public JOptionPaneUtil iconErrorMessage() {
        messageType = JOptionPane.ERROR_MESSAGE;
        return this;
    }

    /**
     * INFORMATION_MESSAGE
     * 
     * @return
     */
    public JOptionPaneUtil iconInformationMessage() {
        messageType = JOptionPane.INFORMATION_MESSAGE;
        return this;
    }

    /**
     * WARNING_MESSAGE
     * 
     * @return
     */
    public JOptionPaneUtil iconWaringMessage() {
        messageType = JOptionPane.WARNING_MESSAGE;
        return this;
    }

    /**
     * QUESTION_MESSAGE
     * 
     * @return
     */
    public JOptionPaneUtil iconQuestionMessage() {
        messageType = JOptionPane.QUESTION_MESSAGE;
        return this;
    }

    /**
     * PLAIN_MESSAGE
     * 
     * @return
     */
    public JOptionPaneUtil iconPlainMessage() {
        messageType = JOptionPane.PLAIN_MESSAGE;
        return this;
    }
}
