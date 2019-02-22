package gtu.swing.util;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import gtu.date.DateUtil;
import gtu.file.FileUtil;
import gtu.image.ImageUtil;

/**
 * @author gtu001
 *
 */
public class JCommonUtil {

    private JCommonUtil() {
    }

    // 預設 button.setBackground 的顏色
    public static final ColorUIResource DEFULAT_BTN_COLOR = new ColorUIResource(238, 238, 238);

    // static final Font DEFAULT_FONT = new Font("Consolas", 0, 10);
    public static final Font DEFAULT_FONT = new java.awt.Font("細明體", 0, 12);
    public static final Font DEFAULT_FONT_Consolas = new java.awt.Font("Consolas", 0, 12);

    /**
     * 設定panel的title
     */
    public static void setPanelBorderTitle(String title, JPanel panel) {
        panel.setBorder(new TitledBorder(title));
        // BorderFactory.createTitledBorder("Bottom Panel")
    }

    /**
     * 設定panel帶寬度邊框
     */
    public static void setPanelBorderWidth(int eb, JPanel panel) {
        panel.setBorder(BorderFactory.createEmptyBorder(eb, eb, eb, eb));
    }

    /**
     * 設定滑鼠指標
     * 
     * @param component
     */
    public static void setCursorToMouseHover(Component component, Integer cursor) {
        if (cursor == null) {
            cursor = Cursor.HAND_CURSOR;
        }
        component.setCursor(Cursor.getPredefinedCursor(cursor));
    }

    /*
     * 取得滑鼠位置component
     */
    public static Component getComponentAt(MouseEvent e) {
        return SwingUtilities.getDeepestComponentAt(e.getComponent(), e.getX(), e.getY());
    }

    /**
     * 設定全畫面
     */
    public static void changeDisplaySize(int width, int height, JFrame frame) {
        frame.setUndecorated(true);
        frame.setResizable(false);
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        device.setFullScreenWindow(frame);
        device.setDisplayMode(new DisplayMode(width, height, 32, 60));
    }

    /**
     * 設定元件字形
     */
    public static void setFont(JComponent... component) {
        for (JComponent c : component) {
            c.setFont(DEFAULT_FONT);
        }
    }

    public static void setUIFont() {
        FontUIResource f = new javax.swing.plaf.FontUIResource("新細明體", Font.PLAIN, 12);
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value != null && value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }

    /**
     * 設定視窗致中
     */
    public static void setJFrameCenter(Window frame) {
        // 非視窗系統不做事
        if (GraphicsEnvironment.isHeadless() == true) {
            return;
        }

        // 方法一
        java.awt.Dimension scr_size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((scr_size.width - frame.getWidth()) / 2, //
                (scr_size.height - frame.getHeight()) / 2);
        // 方法2
        // frame.setLocationRelativeTo(null);
    }

    /**
     * 設定視窗於右下角
     */
    public static void setLocationToRightBottomCorner(Component frame) {
        // height of the task bar
        int taskBarSize = 0;
        try {
            Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration());
            taskBarSize = scnMax.bottom;
        } catch (Exception ex) {
        }
        java.awt.Dimension scr_size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(scr_size.width - frame.getWidth(), //
                scr_size.height - frame.getHeight() - taskBarSize);
    }

    /**
     * 設定視窗icon
     */
    public static void setJFrameIcon(Window frame, String resourcePath) {
        Image img = ImageUtil.getInstance().getImageAutoChoice(resourcePath);
        frame.setIconImage(img);
    }

    /**
     * 預設設定
     */
    public static void setJFrameDefaultSetting(JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setJFrameCenter(frame);
    }

    /**
     * 設定tool tip反應時間為0
     */
    public static void defaultToolTipDelay() {
        ToolTipManager.sharedInstance().setInitialDelay(0);
    }

    /**
     * 設定元件字形
     */
    public static void setFontAll(JComponent component) {
        Component comp = null;
        for (int ii = 0; ii < component.getComponentCount(); ii++) {
            comp = component.getComponent(ii);
            comp.setFont(DEFAULT_FONT);
            if (comp instanceof JComponent) {
                setFontAll((JComponent) comp);
            }
        }
    }

    /**
     * textField 的事件處理
     */
    public static abstract class HandleDocumentEvent implements EventListener {
        public abstract void process(DocumentEvent event);

        public String getDocText(DocumentEvent doc) {
            return JCommonUtil.getDocumentText(doc);
        }
    }

    /**
     * textField 取得字串
     */
    public static String getDocumentText(DocumentEvent doc) {
        try {
            return doc.getDocument().getText(0, doc.getDocument().getEndPosition().getOffset());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * textField 的事件處理
     * textField.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new
     * HandleDocumentEvent() {
     * 
     * @Override public void process(DocumentEvent event) { String text =
     *           JCommonUtil.getDocumentText(event); } }));
     */
    public static DocumentListener getDocumentListener(final HandleDocumentEvent handleDocumentEvent) {
        return new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                handleDocumentEvent.process(e);
            }

            public void removeUpdate(DocumentEvent e) {
                handleDocumentEvent.process(e);
            }

            public void changedUpdate(DocumentEvent e) {
                handleDocumentEvent.process(e);
            }
        };
    }

    /**
     * 視窗關閉事件
     */
    public static void frameCloseConfirm(final JFrame jframe) {
        frameCloseConfirm(jframe, true, null);
    }

    /**
     * 視窗關閉事件
     */
    public static void frameCloseConfirm(final JFrame jframe, final boolean confirm, final ActionListener action) {
        jframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        jframe.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.out.println("windowClosing");
                if (confirm) {
                    int closeOption = JOptionPane.showConfirmDialog(null, "確定關閉?");
                    System.out.println("關閉選項" + closeOption);
                    if (closeOption == JOptionPane.YES_OPTION) {
                        if (action != null) {
                            action.actionPerformed(new ActionEvent(jframe, -1, "close"));
                        }
                        gtu.swing.util.JFrameUtil.setVisible(false, jframe);
                        jframe.dispose();
                        System.exit(0);
                    }
                } else {
                    if (action != null) {
                        action.actionPerformed(new ActionEvent(jframe, -1, "close"));
                    }
                    gtu.swing.util.JFrameUtil.setVisible(false, jframe);
                    jframe.dispose();
                    System.exit(0);
                }
            }

            public void windowClosed(WindowEvent e) {
                System.out.println("windowClosed");
            }

            public void windowOpened(WindowEvent e) {
                System.out.println("windowOpened");
            }

            public void windowIconified(WindowEvent e) {
                System.out.println("windowIconified");
            }

            public void windowDeiconified(WindowEvent e) {
                System.out.println("windowDeiconified");
            }

            public void windowActivated(WindowEvent e) {
                System.out.println("windowActivated");
            }

            public void windowDeactivated(WindowEvent e) {
                System.out.println("windowDeactivated");
            }

            public void windowStateChanged(WindowEvent e) {
                System.out.println("windowStateChanged");
            }

            public void windowGainedFocus(WindowEvent e) {
                System.out.println("windowGainedFocus");
            }

            public void windowLostFocus(WindowEvent e) {
                System.out.println("windowLostFocus");
            }
        });
    }

    /**
     * 視窗關閉事件
     */
    public static void frameCloseDo(final JFrame jframe, WindowAdapter windowAdapter) {
        jframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        if (windowAdapter == null) {
            jframe.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent paramWindowEvent) {
                    // TODO
                    gtu.swing.util.JFrameUtil.setVisible(false, jframe);
                    jframe.dispose();
                }
            });
        } else {
            jframe.addWindowListener(windowAdapter);
        }
    }

    /**
     * 顯示prompt視窗
     */
    public static String _jOptionPane_showInputDialog(Object message, String defaultValue) {
        return (String) JOptionPaneUtil.newInstance().showInputDialog(message, "INPUT", defaultValue);
    }

    /**
     * 顯示prompt視窗
     */
    public static String _jOptionPane_showInputDialog(Object message) {
        return JOptionPaneUtil.newInstance().showInputDialog(message, "INPUT");
    }

    /**
     * 顯示alert視窗
     */
    public static void _jOptionPane_showMessageDialog_error(Object message) {
        JOptionPane.showMessageDialog(null, message, "ERROR", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * 顯示alert視窗
     */
    public static void _jOptionPane_showMessageDialog_InvokeLater(final Object message, final String title, final boolean isError) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int icon = isError ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE;
                JOptionPane.showMessageDialog(null, message, title, icon);
            }
        });
    }

    public static void _jOptionPane_showMessageDialog_InvokeLater_Html(final Object message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                String strMessage = message == null ? "" : String.valueOf(message);
                strMessage = strMessage.replaceAll("\n", "<br/>");
                strMessage = "<html>" + strMessage + "</html>";
                JOptionPane.showMessageDialog(null, strMessage);
            }
        });
    }

    /**
     * 顯示alert視窗
     */
    public static void _jOptionPane_showMessageDialog_error_NonUICompatible(Object message) {
        try {
            JOptionPane.showMessageDialog(null, message, "ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (java.awt.HeadlessException ex) {
            System.out.println("<<showMessageDialog>> " + "ERROR" + " : " + message);
            System.err.println("Non UI Mode!");
        }
    }

    /**
     * 顯示alert視窗
     */
    public static void _jOptionPane_showMessageDialog_error(Object message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * 顯示alert視窗
     */
    public static void _jOptionPane_showMessageDialog_error_NonUICompatible(Object message, String title) {
        try {
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
        } catch (java.awt.HeadlessException ex) {
            System.out.println("<<showMessageDialog>>[ERR] " + title + " : " + message);
            System.err.println("Non UI Mode!");
        }
    }

    /**
     * 顯示alert視窗
     */
    public static void _jOptionPane_showMessageDialog_info(Object message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void _jOptionPane_showMessageDialog_info_NonUICompatible(Object message, String title) {
        try {
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
        } catch (java.awt.HeadlessException ex) {
            System.out.println("<<showMessageDialog>>[INFO] " + title + " : " + message);
            System.err.println("Non UI Mode!");
        }
    }

    /**
     * 顯示alert視窗
     */
    public static void _jOptionPane_showMessageDialog_info(Object message) {
        JOptionPane.showMessageDialog(null, message, "INFO", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void _jOptionPane_showMessageDialog_info_NonUICompatible(Object message) {
        try {
            JOptionPane.showMessageDialog(null, message, "INFO", JOptionPane.INFORMATION_MESSAGE);
        } catch (java.awt.HeadlessException ex) {
            System.out.println("<<showMessageDialog>> " + "INFO" + " : " + message);
            System.err.println("Non UI Mode!");
        }
    }

    /**
     * 顯示alert視窗
     */
    public static void _jOptionPane_showMessageDialog_info_byComponent(Component component, Object message) {
        JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(component), message);
    }

    /**
     * 顯示開啟檔案室窗, 只能選目錄
     */
    public static File _jFileChooser_selectDirectoryOnly() {
        return JFileChooserUtil.newInstance().selectDirectoryOnly().showOpenDialog().getApproveSelectedFile();
    }

    /**
     * 顯示開啟檔案室窗, 只能選檔案
     */
    public static File _jFileChooser_selectFileOnly() {
        return JFileChooserUtil.newInstance().selectFileOnly().showOpenDialog().getApproveSelectedFile();
    }

    /**
     * 顯示另存檔案室窗
     */
    public static File _jFileChooser_selectFileOnly_saveFile() {
        return JFileChooserUtil.newInstance().selectFileOnly().showSaveDialog().getApproveSelectedFile();
    }

    /**
     * 顯示開啟檔案室窗 , 可選檔案或目錄
     */
    public static File _jFileChooser_selectFileAndDirectory() {
        return JFileChooserUtil.newInstance().selectFileAndDirectory().showOpenDialog().getApproveSelectedFile();
    }

    /**
     * 顯示confirm視窗, 按OK回傳true
     */
    public static boolean _JOptionPane_showConfirmDialog_yesNoOption(Object message, String title) {
        return JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null);
    }

    /**
     * 顯示輸入下拉項目
     */
    public static Object _JOptionPane_showInputDialog(Object message, String title, Object[] drowdown, Object defaultValue) {
        return JOptionPaneUtil.newInstance().showInputDialog_drowdown(message, title, drowdown, defaultValue);
    }

    public static Object _JOptionPane_showInputDialog_top(Object message, String title, Object[] drowdown, Object defaultValue) {
        return JOptionPaneUtil.newInstance().showInputDialog_drowdown_top(message, title, drowdown, defaultValue);
    }

    /**
     * 錯誤處理
     */
    public static File handleException(Throwable ex, boolean writeFile) {
        return JCommonUtil.handleException(ex.getMessage(), ex, writeFile, "", "yyyyMMdd", false, false);// true寫檔
    }

    /**
     * 錯誤處理
     */
    public static File handleException(Throwable ex) {
        return JCommonUtil.handleException(ex.getMessage(), ex, true, "", "yyyyMMdd", false, false);// true寫檔
    }

    /**
     * 錯誤處理
     */
    public static File handleException(Object message, Throwable ex) {
        return JCommonUtil.handleException(message, ex, true, "", "yyyyMMdd", false, false);// true寫檔
    }

    /**
     * 錯誤處理
     */
    public static File handleExceptionDetails(Object message, Throwable ex) {
        return JCommonUtil.handleException(message, ex, true, "", "yyyyMMdd.HHmmss.SSS", false, false);// true寫檔
    }

    /**
     * 錯誤處理
     */
    public static File handleException(Object message, Throwable ex, boolean writeFile) {
        return handleException(message, ex, writeFile, "", "yyyyMMdd", false, false);
    }

    /**
     * 錯誤處理
     */
    public static File handleException(Object message, final Throwable ex, boolean writeFile, String fileNameSuffix, String dateFormat, boolean silent, boolean useHtml) {
        String title = (ex == null) ? "執行發生錯誤" : ex.getMessage();
        String messageStr = "";
        File writeIfNeed = null;
        if (ex != null) {
            System.err.println("<<Custom Error Message>> : " + message);
            ex.printStackTrace();

            StringWriter writer = new StringWriter();
            boolean validateFindOk = false;
            int count = 0;
            for (StackTraceElement s : ex.getStackTrace()) {
                if ("org.apache.commons.lang.Validate".equals(s.getClassName())) {
                    validateFindOk = true;
                    break;
                }
                if ("org.apache.commons.lang3.Validate".equals(s.getClassName())) {
                    validateFindOk = true;
                    break;
                }
                writer.write(s.toString() + "\r\n");
                if (count == 9) {
                    break;
                }
                count++;
            }
            if (validateFindOk) {
                try {
                    if (!silent) {
                        JCommonUtil._jOptionPane_showMessageDialog_InvokeLater(ex.getMessage(), "欄位輸入錯誤", true);
                    }
                } catch (java.awt.HeadlessException uiError) {
                }
                return null;
            }

            List<String> messageList = new ArrayList<String>();
            if (message != null && StringUtils.isNotBlank(String.valueOf(message))) {
                messageList.add(String.valueOf(message));
            }
            messageList.add(ex.getMessage() + "=" + ex.getClass().getName());
            for (Throwable tempEx = ex; (tempEx = tempEx.getCause()) != null;) {
                messageList.add(tempEx.getMessage() + "=" + tempEx.getClass().getName());
            }
            StringBuilder errSb = new StringBuilder();
            errSb.append("SWING ERROR GET_CAUSE -----------------------↓↓↓↓↓↓\n");
            for (String errorMessage : messageList) {
                errSb.append("\t" + errorMessage + "\n");
            }
            errSb.append("SWING ERROR GET_CAUSE -----------------------↑↑↑↑↑↑\n");
            messageStr += errSb + "\r\n" + writer.getBuffer().toString();
            if (writeFile) {
                try {
                    String dateStr = "";
                    for (String formatStr : new String[] { dateFormat, "yyyyMMdd" }) {
                        if (StringUtils.isNotBlank(dateFormat)) {
                            try {
                                dateStr = new SimpleDateFormat(formatStr).format(new Date());
                                break;
                            } catch (Exception ex1) {
                            }
                        }
                    }

                    fileNameSuffix = StringUtils.trimToEmpty(fileNameSuffix);

                    if (GraphicsEnvironment.isHeadless() == false) {
                        // 使用ui模式
                        writeIfNeed = new File(FileUtil.DESKTOP_DIR, "swing_error_" + fileNameSuffix + dateStr + ".log");
                    } else {
                        // 使用非ui模式
                        writeIfNeed = new File(System.getProperty("user.dir"), "swing_error_" + fileNameSuffix + dateStr + ".log");
                        System.out.println("Error log File Position : " + writeIfNeed);
                    }

                    PrintWriter pw = new PrintWriter(writeIfNeed);
                    pw.write("案發時間:" + DateUtil.getCurrentDateTime(true) + "\n");
                    if (message != null) {
                        pw.write("自訂Message : " + message);
                    }
                    ex.printStackTrace(pw);
                    pw.flush();
                    pw.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (StringUtils.isBlank(messageStr)) {
            messageStr = String.valueOf(message);
            if (StringUtils.isBlank(messageStr)) {
                messageStr = "不知名錯誤!";
            }
        }
        try {
            if (!silent) {
                if (!useHtml) {
                    JCommonUtil._jOptionPane_showMessageDialog_InvokeLater(messageStr, title, true);
                } else {
                    JCommonUtil._jOptionPane_showMessageDialog_InvokeLater_Html(messageStr);
                }
            }
        } catch (java.awt.HeadlessException uiError) {
        }
        return writeIfNeed;
    }

    /**
     * 錯誤處理
     */
    public static File handleException_getFile() {
        File writeIfNeed = null;
        if (GraphicsEnvironment.isHeadless() == false) {
            // 使用ui模式
            writeIfNeed = FileUtil.DESKTOP_DIR;
        } else {
            // 使用非ui模式
            writeIfNeed = new File(System.getProperty("user.dir"));
        }
        Long maxDate = 0L;
        Map<Long, File> fileMap = new HashMap<Long, File>();
        for (File f : writeIfNeed.listFiles()) {
            if (f.getName().matches("swing_error_(.*).log")) {
                long v1 = f.lastModified();
                fileMap.put(v1, f);
                maxDate = Math.max(maxDate, v1);
            }
        }
        return fileMap.get(maxDate);
    }

    /**
     * 未輸入值要顯示錯誤訊息 外層要包 try catch(){ JCommonUtil.handleException }
     */
    public static void isBlankErrorMsg(String text, String errMsg) {
        Validate.isTrue(StringUtils.isNotBlank(text), errMsg);
    }

    /**
     * 顯示元件清單
     */
    public static void showJComponentInfo(JComponent component) {
        for (int ii = 0; ii < component.getComponentCount(); ii++) {
            System.out.format("\t%d == %s\n", ii, component.getComponent(ii));
        }
    }

    /**
     * 將幾個RadioButton設定為一起互動
     */
    public static ButtonGroup createRadioButtonGroup(AbstractButton... btn) {
        ButtonGroup btnGroup = new ButtonGroup();
        for (AbstractButton b : btn) {
            btnGroup.add(b);
        }
        return btnGroup;
    }

    /**
     * 文字方塊點兩下 開啟選擇檔案 或目錄的方塊
     * 
     * @param jTextField1
     * @param fileAndDir
     *            true可選檔案或目錄, false只可選檔案
     */
    public static void jTextFieldSetFilePathMouseEvent(final JTextComponent jTextField1, final boolean fileAndDir) {
        jTextFieldSetFilePathMouseEvent(jTextField1, fileAndDir, null);
    }

    public static void jTextFieldSetFilePathMouseEvent(final JTextComponent jTextField1, final boolean fileAndDir, final ActionListener callBack) {
        JTextFieldUtil.setupDragDropFilePath(jTextField1, null);
        jTextField1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (JMouseEventUtil.buttonLeftClick(2, evt)) {
                    File file = null;
                    if (fileAndDir) {
                        file = JCommonUtil._jFileChooser_selectFileAndDirectory();
                    } else {
                        file = JCommonUtil._jFileChooser_selectFileOnly();
                    }
                    if (file != null) {
                        jTextField1.setText(file.getAbsolutePath());
                        if (callBack != null) {
                            callBack.actionPerformed(new ActionEvent(file, -1, "ok"));
                        }
                    } else {
                        JCommonUtil._jOptionPane_showMessageDialog_error("檔案選擇錯誤");
                    }
                }
            }
        });
    }

    /**
     * 設定外觀
     */
    public static void defaultLookAndFeel() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                System.out.println("LookAndFeel:\t" + info.getName() + "\t" + info.getClassName());
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 檔案路徑檢查
     * 
     * @param filePath
     *            路徑
     * @param label
     *            標籤
     * @param dirOnly
     *            只允許目錄
     * @return
     */
    public static File filePathCheck(String filePath, String label, boolean dirOnly) {
        return filePathCheck(filePath, label, "", dirOnly);
    }

    /**
     * 檔案路徑檢查
     * 
     * @param filePath
     *            路徑
     * @param label
     *            標籤
     * @param subName
     *            必須符合的副檔名
     * @return
     */
    public static File filePathCheck(String filePath, String label, String subName) {
        return filePathCheck(filePath, label, subName, false);
    }

    /**
     * 檔案路徑檢查
     * 
     * @param filePath
     *            路徑
     * @param label
     *            標籤
     * @param subName
     *            必須符合的副檔名
     * @param dirOnly
     *            只允許目錄
     * @return
     */
    private static File filePathCheck(String filePath, String label, String subName, boolean dirOnly) {
        label = StringUtils.defaultString(label);
        if (StringUtils.isBlank(filePath)) {
            Validate.isTrue(false, label + "路徑不可為空");
        }
        File file = new File(filePath);
        if (!file.exists()) {
            Validate.isTrue(false, label + "路徑不存在");
        }
        if (StringUtils.isNotBlank(subName)) {
            if (subName.startsWith(".")) {
                subName = subName.replaceFirst(".", "");
            }
            if (!StringUtils.equalsIgnoreCase(FileUtil.getSubName(file), subName)) {
                Validate.isTrue(false, label + "檔案格式不正確,必須為:" + subName + "檔");
            }
        }
        if (dirOnly && file.isFile()) {
            Validate.isTrue(false, label + "路徑必須為目錄");
        }
        return file;
    }

    /**
     * 設定jToggleButton
     * 
     * @param jToggleButton
     * @param showtext
     *            [0]以選顯示文字, [1]未選顯示文字
     */
    public static void setJToggleButtonText(final JToggleButton jToggleButton, final String[] showtext) {
        Validate.isTrue(showtext != null && showtext.length == 2, "長度必須為2(0已選,1未選)");
        jToggleButton.setText(jToggleButton.isSelected() ? showtext[0] : showtext[1]);
        jToggleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                // 按下去
                if (jToggleButton.isSelected()) {
                    jToggleButton.setText(showtext[0]);
                    // 凸起來
                } else {
                    jToggleButton.setText(showtext[1]);
                }
            }
        });
    }

    /**
     * 將PrintStream物件寫入的值 直接寫到JTextArea
     * 
     * @param logArea
     *            JTextArea物件
     * @param limit
     *            長度大於多少要清空
     * @param logDebug
     *            是否顯示於console
     * @return
     */
    @Deprecated
    public static PrintStream getNewPrintStream2JTextArea(final JTextArea logArea, final int limit, final boolean logDebug) {
        return JTextAreaUtil.getNewPrintStream2JTextArea(logArea, limit, logDebug);
    }

    /**
     * 將jPanel加入帶有卷軸的元件
     * 
     * @param jPanel2
     * @param jcommponent
     */
    @Deprecated
    public static void createScrollComponent(JPanel jPanel2, JComponent jcommponent) {
        JScrollPane jScrollPane1 = new JScrollPane();
        jPanel2.add(jScrollPane1, BorderLayout.CENTER);
        // jScrollPane1.setPreferredSize(new java.awt.Dimension(411, 262));
        jScrollPane1.setViewportView(jcommponent);
    }

    public static JScrollPane createScrollComponent(JComponent jcommponent) {
        return createScrollComponent(jcommponent, true, true);
    }

    /**
     * 建立卷軸物件
     * 
     * @param jcommponent
     * @param horizonEnabled
     *            水平啟用
     * @param verticalEnabled
     *            垂直啟用
     * @return
     */
    public static JScrollPane createScrollComponent(JComponent jcommponent, boolean horizonEnabled, boolean verticalEnabled) {
        JScrollPane jScrollPane1 = new JScrollPane();
        // jScrollPane1.setPreferredSize(new java.awt.Dimension(411, 262));
        jScrollPane1.setViewportView(jcommponent);

        if (!horizonEnabled) {
            jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);// 水平卷軸關閉
        }
        if (!verticalEnabled) {
            jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);// 垂直卷軸關閉
        }
        return jScrollPane1;
    }

    /**
     * 將視窗帶到最上層顯示
     */
    public static void setFrameAtop(Window window, boolean alaysOnTop) {
        if (window instanceof JDialog) {
            JDialog d = (JDialog) window;
            d.setModal(true);
        }

        // 設定顯示
        window.setVisible(true);
        // 設定至最前
        window.toFront();
        window.repaint();

        if (window instanceof JFrame) {
            JFrame f = (JFrame) window;
            f.setState(java.awt.Frame.NORMAL);// 回復原狀
        }

        // 鎖定最上層
        window.requestFocus();
        window.setAlwaysOnTop(alaysOnTop);
        window.setAutoRequestFocus(true);
    }

    public static boolean focusComponent(JComponent jcomponent, boolean robotFocus, ActionListener afterRobotFocus) {
        jcomponent.setRequestFocusEnabled(true);
        jcomponent.setFocusable(true);
        jcomponent.grabFocus();
        jcomponent.requestFocusInWindow();
        jcomponent.requestFocus();
        boolean useRobotFocus = false;
        if (!jcomponent.isFocusOwner() && robotFocus) {
            try {
                Point p = MouseInfo.getPointerInfo().getLocation();
                Robot robot = new Robot();
                // 先release滑鼠
                robot.mouseRelease(InputEvent.BUTTON1_MASK);
                robot.mouseRelease(InputEvent.BUTTON2_MASK);
                // robot.mouseRelease(InputEvent.BUTTON3_MASK);

                int x = jcomponent.getLocationOnScreen().x;
                int y = jcomponent.getLocationOnScreen().y;
                robot.mouseMove(x, y);
                robot.mousePress(InputEvent.BUTTON1_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_MASK);
                robot.mouseMove(p.x, p.y);
                useRobotFocus = true;
                System.out.println(">> useRobotFocus !!");
                if (afterRobotFocus != null) {
                    afterRobotFocus.actionPerformed(new ActionEvent(jcomponent, -1, "useRobotFocus"));
                }
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
        return useRobotFocus;
    }

    /**
     * 發出關閉視窗event 會觸發WindowListener.windowClosing
     */
    public static void sendWindowClosingEvent(Window window) {
        window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
    }

    /**
     * 縮小化視窗
     */
    public static void setFrameMinimize(JFrame jframe) {
        jframe.setState(java.awt.Frame.ICONIFIED);
    }

    /**
     * 強制使用UI
     */
    public static void forceUIMode(boolean isUseUIMode) {
        boolean headLess = !isUseUIMode;
        System.setProperty("java.awt.headless", String.valueOf(headLess));// 使用ui為false
        if (!GraphicsEnvironment.isHeadless()) {
            try {
                FieldUtils.writeDeclaredStaticField(GraphicsEnvironment.class, "headless", headLess, true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("<<< java.awt.headless - " + GraphicsEnvironment.isHeadless());
    }

    /**
     * 是否支援UI
     */
    public static boolean isUIMode() {
        return !GraphicsEnvironment.isHeadless();
    }

    /**
     * 觸發actionPerformed事件
     */
    public static void triggerButtonActionPerformed(JComponent btn) {
        try {
            ActionListener[] listeners = (ActionListener[]) MethodUtils.invokeExactMethod(btn, "getActionListeners");
            for (ActionListener a : listeners) {
                a.actionPerformed(new ActionEvent(btn, ActionEvent.ACTION_PERFORMED, null) {
                    private static final long serialVersionUID = 1L;
                    // Nothing need go here, the actionPerformed method (with
                    // the
                    // above arguments) will trigger the respective listener
                });
            }
        } catch (Exception e) {
            throw new RuntimeException("triggerButtonActionPerformed ERR :" + e.getMessage(), e);
        }
    }

    /**
     * 取得底下的 component focus Ps : 記得拿掉原本JDialog的 default button -->
     * getRootPane().setDefaultButton(okButton);
     */
    public static void setFoucsToChildren(final Component root, final Component target) {
        root.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent ce) {
                target.requestFocusInWindow();
            }
        });
    }

    /*
     * 設定 拖曳檔案
     */
    public static void applyDropFiles(Component jcomp, final ActionListener listener) {
        jcomp.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    if (listener != null) {
                        listener.actionPerformed(new ActionEvent(droppedFiles, -1, "files"));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
    }
}
