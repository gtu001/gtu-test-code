package gtu.swing.util;

import java.awt.Component;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import sun.awt.AppContext;

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
    
    public Object showInputDialog_drowdown_top(Object message, String title, Object[] drowdown, Object defaultValue) {
        return new __DropdownDialog().showInputDialog(null, message, title, messageType, null, drowdown, defaultValue);
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
    
    private class __DropdownDialog {

        private final Object sharedFrameKey = JOptionPane.class;
        private final Object sharedOwnerFrameKey = new StringBuffer("SwingUtilities.sharedOwnerFrame");

        public Frame getRootFrame() throws HeadlessException {
            Frame localFrame = (Frame) AppContext.getAppContext().get(sharedFrameKey);
            if (localFrame == null) {
                localFrame = getSharedOwnerFrame();
                AppContext.getAppContext().put(sharedFrameKey, localFrame);
            }
            return localFrame;
        }

        Frame getSharedOwnerFrame() throws HeadlessException {
            Frame localObject = (Frame) AppContext.getAppContext().get(sharedOwnerFrameKey);
            if (localObject == null) {
                localObject = new SharedOwnerFrame();
                AppContext.getAppContext().put(sharedOwnerFrameKey, localObject);
            }
            return localObject;
        }

        public final Object UNINITIALIZED_VALUE = "uninitializedValue";

        public Object showInputDialog(Component paramComponent, Object paramObject1, String paramString, int paramInt, Icon paramIcon, Object[] paramArrayOfObject, Object paramObject2)
                throws HeadlessException {
            JOptionPane localJOptionPane = new JOptionPane(paramObject1, paramInt, 2, paramIcon, null, null);
            localJOptionPane.setWantsInput(true);
            localJOptionPane.setSelectionValues(paramArrayOfObject);
            localJOptionPane.setInitialSelectionValue(paramObject2);
            localJOptionPane.setComponentOrientation((paramComponent == null ? getRootFrame() : paramComponent).getComponentOrientation());
            JDialog localJDialog = localJOptionPane.createDialog(paramComponent, paramString);
            localJOptionPane.selectInitialValue();
            localJDialog.show();
            JCommonUtil.setFrameAtop(localJDialog, true);
            localJDialog.dispose();
            Object localObject = localJOptionPane.getInputValue();
            if (localObject == UNINITIALIZED_VALUE) {
                return null;
            }
            return localObject;
        }

        private class SharedOwnerFrame extends Frame implements WindowListener {
            SharedOwnerFrame() {
            }

            public void addNotify() {
                super.addNotify();
                installListeners();
            }

            void installListeners() {
                Window[] arrayOfWindow1 = getOwnedWindows();
                for (Window localWindow : arrayOfWindow1) {
                    if (localWindow != null) {
                        localWindow.removeWindowListener(this);
                        localWindow.addWindowListener(this);
                    }
                }
            }

            public void windowClosed(WindowEvent paramWindowEvent) {
                synchronized (getTreeLock()) {
                    Window[] arrayOfWindow1 = getOwnedWindows();
                    for (Window localWindow : arrayOfWindow1) {
                        if (localWindow != null) {
                            if (localWindow.isDisplayable()) {
                                return;
                            }
                            localWindow.removeWindowListener(this);
                        }
                    }
                    dispose();
                }
            }

            public void windowOpened(WindowEvent paramWindowEvent) {
            }

            public void windowClosing(WindowEvent paramWindowEvent) {
            }

            public void windowIconified(WindowEvent paramWindowEvent) {
            }

            public void windowDeiconified(WindowEvent paramWindowEvent) {
            }

            public void windowActivated(WindowEvent paramWindowEvent) {
            }

            public void windowDeactivated(WindowEvent paramWindowEvent) {
            }

            public void show() {
            }

            public void dispose() {
                try {
                    getToolkit().getSystemEventQueue();
                    super.dispose();
                } catch (Exception localException) {
                }
            }
        }
    }
}
