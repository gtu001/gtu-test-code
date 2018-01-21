package gtu.jpa.hibernate;
import gtu._work.Rcdf002eExcelMaker;
import gtu.file.FileUtil;
import gtu.log.Log;
import gtu.log.PrintStreamAdapter;
import gtu.swing.util.JCommonUtil;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;



/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class Rcdf002eDBUI extends javax.swing.JFrame {
    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JTextField processDoAllText;
    private JButton porcessDoAllBtn;
    private JPanel jPanel5;
    private JButton doAllBtn;
    private JPanel jPanel4;
    private JButton makeExcelBtn;
    private JLabel jLabel2;
    private JTextField srcDirText;
    private JPanel jPanel3;
    private JLabel jLabel1;
    private JTextField xmlFileText;
    private JButton executeReportBtn;
    private JToggleButton isDebug;
    private JTextArea logArea;
    private JPanel jPanel2;
    private JButton executeBtn;

    /**
    * Auto-generated main method to display this JFrame
    */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Rcdf002eDBUI inst = new Rcdf002eDBUI();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }
    
    public Rcdf002eDBUI() {
        super();
        initGUI();
    }
    
    private void initGUI() {
        try {
            BorderLayout thisLayout = new BorderLayout();
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            getContentPane().setLayout(thisLayout);
            {
                jTabbedPane1 = new JTabbedPane();
                getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
                {
                    jPanel1 = new JPanel();
                    jTabbedPane1.addTab("取得資料", null, jPanel1, null);
                    {
                        executeBtn = new JButton();
                        jPanel1.add(executeBtn);
                        executeBtn.setText("\u5f9e2e\u7522\u751fxml\u6a94");
                        executeBtn.setPreferredSize(new java.awt.Dimension(115, 36));
                        executeBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                executeBtnActionPerformed(evt);
                            }
                        });
                    }
                    {
                        isDebug = new JToggleButton();
                        jPanel1.add(isDebug);
                        isDebug.setPreferredSize(new java.awt.Dimension(232, 38));
                        JCommonUtil.setJToggleButtonText(isDebug, new String[]{"連SIT", "外面內政部"});
                    }
                    {
                        jLabel1 = new JLabel();
                        jPanel1.add(jLabel1);
                        jLabel1.setText("\u8a2d\u5b9axml\u8def\u5f91");
                    }
                    {
                        xmlFileText = new JTextField();
                        jPanel1.add(xmlFileText);
                        xmlFileText.setPreferredSize(new java.awt.Dimension(250, 24));
                    }
                    {
                        executeReportBtn = new JButton();
                        jPanel1.add(executeReportBtn);
                        executeReportBtn.setText("\u81eaxml\u6a94\u7522\u751f\u7d71\u8a08\u6a94");
                        executeReportBtn.setPreferredSize(new java.awt.Dimension(227, 64));
                        executeReportBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                executeReportBtnActionPerformed(evt);
                            }
                        });
                    }
                }
                {
                    jPanel2 = new JPanel();
                    BorderLayout jPanel2Layout = new BorderLayout();
                    jPanel2.setLayout(jPanel2Layout);
                    jTabbedPane1.addTab("log", null, jPanel2, null);
                    jTabbedPane1.addTab("產生excel", null, getJPanel3(), null);
                    jTabbedPane1.addTab("一鍵完成", null, getJPanel4(), null);
                    jTabbedPane1.addTab("時間到執行全部", null, getJPanel5(), null);
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel2.add(jScrollPane1, BorderLayout.CENTER);
                        jScrollPane1.setPreferredSize(new java.awt.Dimension(407, 228));
                        {
                            logArea = new JTextArea();
                            jScrollPane1.setViewportView(logArea);
                        }
                    }
                }
            }
            pack();
            this.setSize(420, 282);
            initLog();
        } catch (Exception e) {
            //add your error handling code here
            e.printStackTrace();
        }
    }
    
    PrintStream print;
    private void initLog(){
        File baseDir = new File(FileUtil.DESKTOP_PATH, "output_rcdf002e");
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
        srcDirText.setText(baseDir.getAbsolutePath());
        File logFile = new File(baseDir, "exeucteLog.txt");
        try {
            print = new PrintStream(new FileOutputStream(logFile), true);
        } catch (Exception e) {
            throw new RuntimeException("無法建立log黨" + logFile);
        }
    }
    
    private void executeBtnActionPerformed(ActionEvent evt) {
        try{
            final JdbcFastQuery exe = new JdbcFastQuery();
            JdbcFastQuery.debugMode = isDebug.isSelected();
            Log.Setting.NORMAL.apply();
            exe.setOut(new PrintStream(new PrintStreamAdapter("big5") {
                @Override
                public void println(String message) {
                    Log.debug(message);
                    if(StringUtils.length(logArea.getText()) > 500){
                        logArea.setText("");
                    }
                    logArea.append(message+"\n");
                }
            }));
            setTitle("執行中.......");
            Thread thread = new Thread(Thread.currentThread().getThreadGroup(), new Runnable(){
                @Override
                public void run() {
                    try {
                        long current = System.currentTimeMillis();
                        exe.execute();
                        current = System.currentTimeMillis() - current;
                        setTitle("寫xml檔 : 執行完成");
                        JCommonUtil._jOptionPane_showMessageDialog_info("寫xml檔 : 執行完成, 時間:" + current + "\n目錄產生於桌面!");
                        xmlFileText.setText(getBulkFile().getAbsolutePath());
                    } catch (Exception ex) {
                        setTitle("執行失敗:" + ex.getMessage());
                        JCommonUtil.handleException(ex);
                    }
                }}, "xxxxxxxxxxxxxxxxxcxccx");
            thread.setDaemon(true);
            thread.start();
        }catch(Exception ex){
            setTitle("執行失敗:" + ex.getMessage());
            JCommonUtil.handleException(ex);
        }
    }
    
    private void executeReportBtnActionPerformed(ActionEvent evt) {
        try{
            final LoadToDBAndWriteFile exe = new LoadToDBAndWriteFile();
            if(StringUtils.isNotBlank(xmlFileText.getText())){
                File file = new File(xmlFileText.getText());
                if(file.exists()){
                    exe.bulkTxt = file;
                }
            }else if(getBulkFile()!=null){
                xmlFileText.setText(getBulkFile().getAbsolutePath());
            }
            Log.Setting.NORMAL.apply();
            exe.setOut(new PrintStream(new PrintStreamAdapter("big5") {
                @Override
                public void println(String message) {
                    Log.debug(message);
                    if(StringUtils.length(logArea.getText()) > 500){
                        logArea.setText("");
                    }
                    logArea.append(message+"\n");
                }
            }));
            setTitle("執行中.......");
            Thread thread = new Thread(Thread.currentThread().getThreadGroup(), new Runnable(){
                @Override
                public void run() {
                    try {
                        long current = System.currentTimeMillis();
                        exe.execute();
                        current = System.currentTimeMillis() - current;
                        setTitle("寫統計檔 : 執行完成");
                        JCommonUtil._jOptionPane_showMessageDialog_info("寫統計檔 : 完成,時間:" + current + "\n目錄產生於桌面!");
                    } catch (Exception ex) {
                        setTitle("執行失敗:" + ex.getMessage());
                        JCommonUtil.handleException(ex);
                    }
                }}, "xxxxxxxxxxxxxxxxxcxccx");
            thread.setDaemon(true);
            thread.start();
        }catch(Exception ex){
            setTitle("執行失敗:" + ex.getMessage());
            JCommonUtil.handleException(ex);
        }
    }
    
    private File getBulkFile(){
        File baseDir = new File(FileUtil.DESKTOP_PATH, "output_rcdf002e");
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
        File ff =  new File(baseDir, "bulk_rcdf002e.txt");
        if(ff.exists()){
            return ff;
        }
        return null;
    }
    
    private JPanel getJPanel3() {
        if(jPanel3 == null) {
            jPanel3 = new JPanel();
            jPanel3.add(getJLabel2());
            jPanel3.add(getSrcDirText());
            jPanel3.add(getMakeExcelBtn());
        }
        return jPanel3;
    }
    
    private JTextField getSrcDirText() {
        if(srcDirText == null) {
            srcDirText = new JTextField();
            JCommonUtil.jTextFieldSetFilePathMouseEvent(srcDirText, true);
            srcDirText.setPreferredSize(new java.awt.Dimension(209, 24));
        }
        return srcDirText;
    }
    
    private JLabel getJLabel2() {
        if(jLabel2 == null) {
            jLabel2 = new JLabel();
            jLabel2.setText("\u7d71\u8a08\u6587\u4ef6\u76ee\u9304");
        }
        return jLabel2;
    }
    
    private JButton getMakeExcelBtn() {
        if(makeExcelBtn == null) {
            makeExcelBtn = new JButton();
            makeExcelBtn.setText("\u7522\u751fexcel");
            makeExcelBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    try{
                        File file = JCommonUtil.filePathCheck(srcDirText.getText(), "統計檔目錄", true);
                        final Rcdf002eExcelMaker exe = new Rcdf002eExcelMaker();
                        exe.setBaseDir(file);
                        Log.Setting.NORMAL.apply();
                        exe.setOut(new PrintStream(new PrintStreamAdapter("big5") {
                            @Override
                            public void println(String message) {
                                Log.debug(message);
                                if(StringUtils.length(logArea.getText()) > 500){
                                    logArea.setText("");
                                }
                                logArea.append(message+"\n");
                            }
                        }));
                        setTitle("執行中.......");
                        Thread thread = new Thread(Thread.currentThread().getThreadGroup(), new Runnable(){
                            @Override
                            public void run() {
                                try {
                                    long current = System.currentTimeMillis();
                                    exe.execute();
                                    current = System.currentTimeMillis() - current;
                                    setTitle("excel產生完成");
                                    JCommonUtil._jOptionPane_showMessageDialog_info("excel產生 : 完成,時間:" + current + "\n目錄產生於桌面!");
                                } catch (Exception ex) {
                                    setTitle("執行失敗:" + ex.getMessage());
                                    JCommonUtil.handleException(ex);
                                }
                            }}, "xxxxxxxxxxxxxxxxxcxccx");
                        thread.setDaemon(true);
                        thread.start();
                    }catch(Exception ex){
                        setTitle("執行失敗:" + ex.getMessage());
                        JCommonUtil.handleException(ex);
                    }
                }
            });
        }
        return makeExcelBtn;
    }
    
    private JPanel getJPanel4() {
        if(jPanel4 == null) {
            jPanel4 = new JPanel();
            jPanel4.add(getJButton1());
        }
        return jPanel4;
    }
    
    private JButton getJButton1() {
        if(doAllBtn == null) {
            doAllBtn = new JButton();
            doAllBtn.setText("\u4e00\u6b21\u5b8c\u6210");
            doAllBtn.setPreferredSize(new java.awt.Dimension(227, 64));
            doAllBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    oneClickDoAll();
                }
            });
        }
        return doAllBtn;
    }
        
    private void oneClickDoAll(){
        try{
            Log.Setting.NORMAL.apply();
            //1.---------------------------------------------------
            final JdbcFastQuery exe1 = new JdbcFastQuery();
            JdbcFastQuery.debugMode = isDebug.isSelected();
            exe1.setOut(JCommonUtil.getNewPrintStream2JTextArea(logArea, 500, true));
            //2.---------------------------------------------------
            final LoadToDBAndWriteFile exe2 = new LoadToDBAndWriteFile();
            if(StringUtils.isNotBlank(xmlFileText.getText())){
                File file = new File(xmlFileText.getText());
                if(file.exists()){
                    exe2.bulkTxt = file;
                }
            }else if(getBulkFile()!=null){
                xmlFileText.setText(getBulkFile().getAbsolutePath());
            }
            exe2.setOut(JCommonUtil.getNewPrintStream2JTextArea(logArea, 500, true));
            //3.---------------------------------------------------
            File file = JCommonUtil.filePathCheck(srcDirText.getText(), "統計檔目錄", true);
            final Rcdf002eExcelMaker exe3 = new Rcdf002eExcelMaker();
            exe3.setBaseDir(file);
            Log.Setting.NORMAL.apply();
            exe3.setOut(JCommonUtil.getNewPrintStream2JTextArea(logArea, 500, true));
            //---------------------------------------------------
            Thread thread = new Thread(Thread.currentThread().getThreadGroup(), new Runnable(){
                @Override
                public void run() {
                    try {
                        long current = System.currentTimeMillis();
                        setTitle("[第1段寫xml]執行中.......");
                        exe1.execute();
                        xmlFileText.setText(getBulkFile().getAbsolutePath());
                        setTitle("[第2段彙整統計檔]執行中.......");
                        exe2.execute();
                        setTitle("[第3段產生excel]執行中.......");
                        exe3.execute();
                        current = System.currentTimeMillis() - current;
                        setTitle("完成!!,總耗時:" + current);
                    } catch (Exception ex) {
                        JCommonUtil.handleException(ex);
                        setTitle("執行失敗......" + ex.getMessage());
                    }
                }}, "xxxxxxxxxxxxxxxxxcxccx");
            thread.setDaemon(true);
            thread.start();
        }catch(Exception ex){
            JCommonUtil.handleException(ex);
            setTitle("執行失敗......" + ex.getMessage());
        }
    }
    
    private JPanel getJPanel5() {
        if(jPanel5 == null) {
            jPanel5 = new JPanel();
            jPanel5.add(getProcessDoAllText());
            jPanel5.add(getJButton1x());
        }
        return jPanel5;
    }
    
    private JButton getJButton1x() {
        if(porcessDoAllBtn == null) {
            porcessDoAllBtn = new JButton();
            porcessDoAllBtn.setText("\u6392\u7a0b\u57f7\u884c");
            porcessDoAllBtn.setPreferredSize(new java.awt.Dimension(80, 54));
            porcessDoAllBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    try{
                        String dateTime = processDoAllText.getText();
                        Validate.notBlank(dateTime, "未輸入時間");
                        Validate.isTrue(StringUtils.isNumeric(dateTime), "必須全為數值");
                        Validate.isTrue(StringUtils.isNumeric(dateTime), "必須全為數值");
                        Validate.isTrue(dateTime.length() == 14, "日期必須為14馬");
                        
                        SimpleDateFormat sdf = new SimpleDateFormat();
                        sdf.applyPattern("yyyyMMddHHmmss");
                        Date newDate = sdf.parse(dateTime);
                        
                        Validate.isTrue(newDate.after(new Date()), "無法排成過去時間");
                        
                        setTitle(DateFormatUtils.format(newDate, "yyyy/MM/dd HH:mm:ss") + "已加入排成...");
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                oneClickDoAll();
                            }
                        }, newDate);
                    }catch(Exception ex){
                        JCommonUtil.handleException(ex);
                        setTitle("執行失敗......" + ex.getMessage());
                    }
                }
            });
        }
        return porcessDoAllBtn;
    }
    
    private JTextField getProcessDoAllText() {
        if(processDoAllText == null) {
            processDoAllText = new JTextField();
            processDoAllText.setText("yyyyMMddHHmmss");
            processDoAllText.setPreferredSize(new java.awt.Dimension(154, 24));
        }
        return processDoAllText;
    }
}
