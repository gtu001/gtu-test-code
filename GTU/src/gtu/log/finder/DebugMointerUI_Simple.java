package gtu.log.finder;

import gtu.swing.util.JOptionPaneUtil;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class DebugMointerUI_Simple extends JFrame {

    // UI ----------------------------------------------------------
    private JPanel contentPane;
    private JTextField classFileText;
    private JTextField classNameText;
    private JTextField methodNameText;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    DebugMointerUI_Simple frame = new DebugMointerUI_Simple();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public DebugMointerUI_Simple() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 514, 356);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("New tab", null, panel, null);

        JLabel lblNewLabel = new JLabel("路徑");
        panel.add(lblNewLabel, "2, 2, right, default");

        classFileText = new JTextField();
        panel.add(classFileText, "4, 2, fill, default");
        classFileText.setColumns(10);

        JLabel label = new JLabel("類別");
        panel.add(label, "2, 4, right, default");

        classNameText = new JTextField();
        panel.add(classNameText, "4, 4, fill, default");
        classNameText.setColumns(10);

        JLabel label_1 = new JLabel("方法");
        panel.add(label_1, "2, 6, right, default");

        methodNameText = new JTextField();
        panel.add(methodNameText, "4, 6, fill, default");
        methodNameText.setColumns(10);

        JButton executeBtn = new JButton("載入並執行");
        executeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (!validateEmpty()) {
                    return;
                }
                File classFile = new File(classFileText.getText());
                String className = classNameText.getText();
                String methodName = methodNameText.getText();

                try {
                    loadExternalClass(classFile, className, methodName);
                    checkPropConfigSave();
                } catch (Exception e) {
                    handleException(e.getMessage(), e, true);
                }
            }
        });
        panel.add(executeBtn, "4, 8");

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("New tab", null, panel_1, null);
    }

    private boolean validateEmpty() {
        if (isBlank(classFileText.getText())) {
            JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("路徑為空", "欄位為空");
            return false;
        }
        if (isBlank(classNameText.getText())) {
            JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("類別為空", "欄位為空");
            return false;
        }
        if (isBlank(methodNameText.getText())) {
            JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("方法為空", "欄位為空");
            return false;
        }
        File classFile = new File(classFileText.getText());
        if (!classFile.exists()) {
            JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("路徑不存在", "欄位為空");
            return false;
        }
        return true;
    }

    // ----------------------------------------------------------
    private final static String DESKTOP_PATH;
    private static File propFile;
    static {
        String destopPath = System.getProperty("user.home") + "\\Desktop\\";
        if (System.getProperty("os.name").equals("Windows XP")) {
            destopPath = System.getProperty("user.home") + "\\桌面\\";
        }
        DESKTOP_PATH = destopPath;
        propFile = new File(DESKTOP_PATH, "DebugMointerUI_Simple.properties");
        if(!propFile.exists()){
            try {
                propFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private Properties getConfig(){
        Properties configProp = new Properties();
        try {
            configProp.load(new FileInputStream(propFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return configProp;
    }

    public static <T> T startWith(Object... lookupArray) {
        try {
            DebugMointerUI_Simple frame = new DebugMointerUI_Simple();
            frame.checkPropConfigExist();
            frame.lookupArray = lookupArray;
            frame.setVisible(true);
            while (frame.isVisible()) {
                Thread.sleep(100);
            }
            return (T) frame.returnObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    Object[] lookupArray;
    Object returnObject = null;
    
    private void checkPropConfigExist(){
        Properties prop = getConfig();
        String classFileText_ = prop.getProperty("classFileText");
        String classNameText_ = prop.getProperty("classNameText");
        String methodNameText_ = prop.getProperty("methodNameText");
        if(isNotBlank(classFileText_)){
            classFileText.setText(classFileText_);
        }
        if(isNotBlank(classNameText_)){
            classNameText.setText(classNameText_);
        }
        if(isNotBlank(methodNameText_)){
            methodNameText.setText(methodNameText_);
        }
    }
    
    private void checkPropConfigSave() {
        Properties prop = getConfig();
        String classFileText_ = classFileText.getText();
        String classNameText_ = classNameText.getText();
        String methodNameText_ = methodNameText.getText();
        prop.setProperty("classFileText", classFileText_);
        prop.setProperty("classNameText", classNameText_);
        prop.setProperty("methodNameText", methodNameText_);
        try {
            prop.store(new FileOutputStream(propFile), "save");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadExternalClass(File classpathFile, String className, String methodName) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException,
            NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        long startTime = System.currentTimeMillis();
        URLClassLoader loader = new URLClassLoader(new URL[] { classpathFile.toURL() }, Thread.currentThread().getContextClassLoader());
        Class<?> clz = loader.loadClass(className);
        Object newObject = clz.newInstance();
        //loader.close();

        Method method = newObject.getClass().getMethod(methodName, new Class[] { Object[].class });
        returnObject = method.invoke(newObject, new Object[]{lookupArray});

        long endTime = System.currentTimeMillis() - startTime;
        JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("執行時間:" + endTime, "執行完成");
    }

    // ----------------------------------------------------------
    private static boolean isNotBlank(String value) {
        if (value == null || value.length() == 0) {
            return false;
        }
        for (char c : value.toCharArray()) {
            if (c != ' ') {
                return true;
            }
        }
        return false;
    }

    private static boolean isBlank(String value) {
        return !isNotBlank(value);
    }

    private static File handleException(Object message, Throwable ex, boolean writeFile) {
        String title = (ex == null) ? "執行發生錯誤" : ex.getMessage();
        String messageStr = "";
        File writeIfNeed = null;
        if (ex != null) {
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
                JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog(ex.getMessage(), "欄位輸入錯誤");
                return null;
            }

            List<String> messageList = new ArrayList<String>();
            if (message != null && isNotBlank(String.valueOf(message))) {
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
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                    writeIfNeed = new File(DESKTOP_PATH, "swing_error_" + sdf.format(new Date()) + ".log");
                    PrintWriter pw = new PrintWriter(writeIfNeed);
                    pw.write("案發時間:" + sdf.format(new Date()) + "\n");
                    ex.printStackTrace(pw);
                    pw.flush();
                    pw.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (isBlank(messageStr)) {
            messageStr = String.valueOf(message);
            if (isBlank(messageStr)) {
                messageStr = "不知名錯誤!";
            }
        }
        JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog(messageStr, title);
        return writeIfNeed;
    }
}
