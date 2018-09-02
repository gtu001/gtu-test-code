package gtu._work.ui;
import gtu._work.ObnfRepairDBBatch;
import gtu.file.FileUtil;
import gtu.ftp.FtpUtil;
import gtu.ftp.FtpUtil.FtpFileInfo;
import gtu.log.PrintStreamAdapter;
import gtu.swing.util.JCommonUtil;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.commons.lang3.StringUtils;


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
public class ObnfExceptionLogDownloadUI extends javax.swing.JFrame {
    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JComboBox siteFtpComboBox;
    private JScrollPane jScrollPane2;
    private JTextField domainJarText;
    private JLabel jLabel2;
    private JButton makeReportBtn;
    private JScrollPane jScrollPane1;
    private JTextArea logArea;
    private JTextArea messageIdArea;
    private JTextField exportTextField;
    private JLabel jLabel1;
    private JPanel jPanel2;
    private JButton downloadBtn;

    /**
    * Auto-generated main method to display this JFrame
    */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ObnfExceptionLogDownloadUI inst = new ObnfExceptionLogDownloadUI();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }
    
    public ObnfExceptionLogDownloadUI() {
        super();
        initGUI();
    }
    
    private void initGUI() {
        try {
            JCommonUtil.frameCloseConfirm(this);
            BorderLayout thisLayout = new BorderLayout();
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            getContentPane().setLayout(thisLayout);
            {
                jTabbedPane1 = new JTabbedPane();
                getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
                {
                    jPanel1 = new JPanel();
                    FlowLayout jPanel1Layout = new FlowLayout();
                    jTabbedPane1.addTab("下載", null, jPanel1, null);
                    jPanel1.setLayout(jPanel1Layout);
                    {
                        jLabel1 = new JLabel();
                        jPanel1.add(jLabel1);
                        jLabel1.setText("\u532f\u51fa\u76ee\u9304");
                    }
                    {
                        exportTextField = new JTextField();
                        JCommonUtil.jTextFieldSetFilePathMouseEvent(exportTextField, true);
                        jPanel1.add(exportTextField);
                        exportTextField.setPreferredSize(new java.awt.Dimension(187, 22));
                    }
                    {
                        jLabel2 = new JLabel();
                        jPanel1.add(jLabel2);
                        jLabel2.setText("domainJar");
                        jLabel2.setPreferredSize(new java.awt.Dimension(56, 15));
                    }
                    {
                        domainJarText = new JTextField();
                        ObnfRepairDBBatch batch = new ObnfRepairDBBatch();
                        domainJarText.setText(batch.fetchDomainJar());
                        JCommonUtil.jTextFieldSetFilePathMouseEvent(domainJarText, false);
                        jPanel1.add(domainJarText);
                        domainJarText.setPreferredSize(new java.awt.Dimension(185, 22));
                    }
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel1.add(jScrollPane1);
                        jScrollPane1.setPreferredSize(new java.awt.Dimension(512, 262));
                        {
                            messageIdArea = new JTextArea();
                            jScrollPane1.setViewportView(messageIdArea);
                        }
                    }
                    {
                        DefaultComboBoxModel jComboBox1Model = new DefaultComboBoxModel();
                        for(FtpSite f : FtpSite.values()){
                            jComboBox1Model.addElement(f);
                        }
                        siteFtpComboBox = new JComboBox();
                        jPanel1.add(siteFtpComboBox);
                        siteFtpComboBox.setModel(jComboBox1Model);
                    }
                    {
                        downloadBtn = new JButton();
                        jPanel1.add(downloadBtn);
                        downloadBtn.setText("\u4e0b\u8f09");
                        downloadBtn.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                downloadBtnAction();
                            }
                        });
                    }
                    {
                        makeReportBtn = new JButton();
                        jPanel1.add(makeReportBtn);
                        makeReportBtn.setText("\u7522\u751f\u5831\u8868");
                        makeReportBtn.setPreferredSize(new java.awt.Dimension(102, 22));
                        makeReportBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                makeReportBtnPerformed();
                            }
                        });
                    }
                }
                {
                    jPanel2 = new JPanel();
                    BorderLayout jPanel2Layout = new BorderLayout();
                    jPanel2.setLayout(jPanel2Layout);
                    jTabbedPane1.addTab("log", null, jPanel2, null);
                    {
                        jScrollPane2 = new JScrollPane();
                        jPanel2.add(jScrollPane2, BorderLayout.CENTER);
                        jScrollPane2.setPreferredSize(new java.awt.Dimension(529, 340));
                        {
                            logArea = new JTextArea();
                            jScrollPane2.setViewportView(logArea);
                        }
                    }
                }
            }
            pack();
            this.setSize(542, 394);
        } catch (Exception e) {
            //add your error handling code here
            e.printStackTrace();
        }
    }
    
    private void downloadBtnAction(){
        try{
            FtpSite ftpSite = (FtpSite)siteFtpComboBox.getSelectedItem();
            if(ftpSite == null){
                JCommonUtil._jOptionPane_showMessageDialog_error("未選擇戶所");
                return;
            }
            String messageIdAreaText = messageIdArea.getText();
            if(StringUtils.isBlank(messageIdAreaText)){
                JCommonUtil._jOptionPane_showMessageDialog_error("未輸入MessageId");
                return;
            }
            String destDirStr = exportTextField.getText();
            if(StringUtils.isBlank(destDirStr)){
                JCommonUtil._jOptionPane_showMessageDialog_error("未輸入匯出黨目錄");
                return;
            }
            File destDir = new File(destDirStr);
            if(!destDir.exists() || !destDir.isDirectory()){
                JCommonUtil._jOptionPane_showMessageDialog_error("目錄不存在或不是目錄");
                return;
            }
            List<String> findList = new ArrayList<String>();
            StringTokenizer tok = new StringTokenizer(messageIdAreaText);
            while(tok.hasMoreElements()){
                String messageId = (String)tok.nextElement();
                findList.add(messageId);
            }
            logArea.setText("");
            FtpUtil ftpUtil = new FtpUtil();
            ftpUtil.connect(ftpSite.ip, ftpSite.port, ftpSite.userId, ftpSite.password, false);
            
            List<FtpFileInfo> fileList = new ArrayList<FtpFileInfo>();
            ftpUtil.scanFindFile(ftpSite.path, ".*", fileList, ftpUtil.getFtp());
            for(FtpFileInfo f : fileList){
                System.out.println("===>" + f.getAbsolutePath());
            }
            
            List<FtpFileInfo> findOkList = new ArrayList<FtpFileInfo>();
            for(FtpFileInfo f : fileList){
                for(int ii = 0; ii < findList.size() ; ii ++){
                    String messageId = findList.get(ii);
                    if(f.getName().contains(messageId)){
                        findOkList.add(f);
                        findList.remove(ii);
                        ii--;
                        break;
                    }
                }
            }

            StringBuffer sb = new StringBuffer();
            for(FtpFileInfo f : findOkList){
                File downloadFile = new File(destDir, f.getName());
                sb.append("ftp路徑:" + f.getAbsolutePath() + "\n");
                ftpUtil.getFile(f.getAbsolutePath(), new FileOutputStream(downloadFile));
                sb.append("下載:" + downloadFile + "\n");
            }
            ftpUtil.disconnect();
            
            for(String messageId : findList){
                sb.append(messageId + " - 找不到\n");
            }
            
            logArea.setText(sb.toString());
            JCommonUtil._jOptionPane_showMessageDialog_info("下載成功, 請看log");
        }catch(Exception ex){
            JCommonUtil.handleException(ex);
            File f = new File(FileUtil.DESKTOP_DIR, "test_log_001.log");
            try {
                PrintWriter pw = new PrintWriter(new FileOutputStream(f));
                ex.printStackTrace(pw);
                pw.flush();
                pw.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void makeReportBtnPerformed() {
        try{
            final String messageIdAreaText = StringUtils.defaultString(messageIdArea.getText());
            String destDirStr = exportTextField.getText();
            if(StringUtils.isBlank(destDirStr)){
                JCommonUtil._jOptionPane_showMessageDialog_error("未輸入匯出檔案或目錄");
                return;
            }
            final File destDir = new File(destDirStr);
            if(!destDir.exists()){
                JCommonUtil._jOptionPane_showMessageDialog_error("檔案或目錄不存在");
                return;
            }
            final ObnfRepairDBBatch batch = new ObnfRepairDBBatch();
            if(StringUtils.isNotBlank(domainJarText.getText())){
                File domainJarFile = new File(domainJarText.getText());
                if(domainJarFile.exists() && domainJarFile.isFile() && domainJarFile.getName().endsWith(".jar")){
                    batch.setDomainJar(domainJarFile);
                }
            }
            batch.setOut(new PrintStream(new PrintStreamAdapter("big5") {
                @Override
                public void println(String message) {
                    logArea.append(message + "\n");
                }
            }));
            new Thread(Thread.currentThread().getThreadGroup(), new Runnable() {
                @Override
                public void run() {
                    try{
                        if(destDir.isFile()){
                            batch.executeDecrypt(destDir);
                        }else{
                            batch.execute(messageIdAreaText, destDir);
                        }
                        JCommonUtil._jOptionPane_showMessageDialog_info("執行完成, 請看log");
                    }catch(Throwable ex){
                        JCommonUtil.handleException(ex);
                    }
                }
            }, "xxxxxxxxxxxx").start();
        }catch(Throwable ex){
            JCommonUtil.handleException(ex);
        }
    }

    enum FtpSite {
//        ReadTEST1 ("ftp.fileswap.com", 21, "gtu001@gmail.com", "3JMSYtjcyaTvzd", "測試用", "/"),//
//        ReadTEST2 ("ftp.speed.hinet.net", 21, "ftp", "ftp", "HINET", "/"),//
//        ReadObj1 ("192.168.9.184", 22, "weblogic", "weblogic", "linux", "/MHNFS/DATA/RL/ExceptionLog"),//
//        ReadObj2 ("192.168.9.184", 22, "root", "hwroot", "linux_root", "/MHNFS/DATA/RL/ExceptionLog"),//
//        ReadObj3 ("192.168.9.184", 22, "srisftp", "srisftp", "linux_srisftp", "/MHNFS/DATA/RL/ExceptionLog"),//
//        ReadObj4 ("192.168.56.101", 22, "user", "pass", "ubuntu", "/MHNFS/DATA/RL/ExceptionLog"),//
//        ReadObj5 ("192.168.10.10", 22, "robertlee", "iisi@222114", "戶政", "/MHNFS/DATA/RL/ExceptionLog"),//
        ReadObj15 ("140.92.86.173", 21, "srismapp", "ris31123", "SIT 高雄市(173)", "/MHNFS/DATA/RL/ExceptionLog"),//
        ReadObj19 ("140.92.86.155", 21, "srismapp", "ris31123", "SIT 新北市(155)", "/MHNFS/DATA/RL/ExceptionLog"),//
        ReadObj26 ("192.1.0.83", 21, "srismapp", "Sth!aix1", "內政部 RC", "/MHNFS/DATA/RC/ExceptionLog"),//
        ReadObj6 ("195.19.8.13", 21, "srismapp", "Sth!aix1", "台中 RL", "/MHNFS/DATA/RL/ExceptionLog"),//
        ReadObj7 ("193.0.3.13", 21, "srismapp", "Sth!aix1", "台北市 RL", "/MHNFS/DATA/RL/ExceptionLog"),//
        ReadObj8 ("195.11.31.13", 21, "srismapp", "Sth!aix1", "台南縣 RL", "/MHNFS/DATA/RL/ExceptionLog"),//
        ReadObj8X ("195.14.1.13", 21, "srismapp", "Sth!aix1", "台東縣 RL", "/MHNFS/DATA/RL/ExceptionLog"),//
        ReadObj9 ("195.2.1.13", 21, "srismapp", "Sth!aix1", "宜蘭縣 RL", "/MHNFS/DATA/RL/ExceptionLog"),//
        ReadObj10 ("195.15.1.13", 21, "srismapp", "Sth!aix1", "花蓮縣 RL", "/MHNFS/DATA/RL/ExceptionLog"),//
        ReadObj11 ("196.20.100.13", 21, "srismapp", "Sth!aix1", "金門縣 RL", "/MHNFS/DATA/RL/ExceptionLog"),//
        ReadObj12 ("195.13.1.13", 21, "srismapp", "Sth!aix1", "屏東 RL", "/MHNFS/DATA/RL/ExceptionLog"),//
        ReadObj13 ("195.5.1.13", 21, "srismapp", "Sth!aix1", "苗栗縣 RL", "/MHNFS/DATA/RL/ExceptionLog"),//
        ReadObj14 ("195.3.8.13", 21, "srismapp", "Sth!aix1", "桃園縣 RL", "/MHNFS/DATA/RL/ExceptionLog"),//
        ReadObj16 ("194.0.205.13", 21, "srismapp", "Sth!aix1", "高雄市 RL", "/MHNFS/DATA/RL/ExceptionLog"),//
        ReadObj17 ("195.17.5.14", 21, "srismapp", "Sth!aix1", "基隆 RL", "/MHNFS/DATA/RL/ExceptionLog"),//
        ReadObj18 ("195.9.1.13", 21, "srismapp", "Sth!aix1", "雲林 RL", "/MHNFS/DATA/RL/ExceptionLog"),//
        ReadObj20 ("195.1.102.13", 21, "srismapp", "Sth!aix1", "新北市 RL", "/MHNFS/DATA/RL/ExceptionLog"),//
        ReadObj21 ("195.18.1.13", 21, "srismapp", "Sth!aix1", "新竹市 RL", "/MHNFS/DATA/RL/ExceptionLog"),//
        ReadObj2X ("195.4.1.13", 21, "srismapp", "Sth!aix1", "新竹縣 RL", "/MHNFS/DATA/RL/ExceptionLog"),//
        ReadObj22 ("194.0.205.13", 21, "srismapp", "Sth!aix1", "新站台", "/MHNFS/DATA/RL/ExceptionLog"),//
        ReadObj23 ("195.10.7.13", 21, "srismapp", "Sth!aix1", "嘉義縣 RL", "/MHNFS/DATA/RL/ExceptionLog"),//
        ReadObj24 ("195.7.1.13", 21, "srismapp", "Sth!aix1", "彰化縣 RL", "/MHNFS/DATA/RL/ExceptionLog"),//
        ReadObj25 ("195.16.1.13", 21, "srismapp", "Sth!aix1", "澎湖縣 RL", "/MHNFS/DATA/RL/ExceptionLog"),//
        ReadObjRL1("196.7.100.13",21,"srismapp","Sth!aix1","連江縣RL","/MHNFS/DATA/RL/ExceptionLog"),//
        ReadObjRL2("195.8.1.13",21,"srismapp","Sth!aix1","南投縣RL","/MHNFS/DATA/RL/ExceptionLog"),//
        ReadObjRL3("195.9.1.13",21,"srismapp","Sth!aix1","雲林縣RL","/MHNFS/DATA/RL/ExceptionLog"),//
        ReadObjRL4("195.13.1.13",21,"srismapp","Sth!aix1","屏東縣RL","/MHNFS/DATA/RL/ExceptionLog"),//
        ReadObjRL5("195.17.5.13",21,"srismapp","Sth!aix1","基隆市RL","/MHNFS/DATA/RL/ExceptionLog"),//
        ReadObjRL6("195.20.1.13",21,"srismapp","Sth!aix1","嘉義市RL","/MHNFS/DATA/RL/ExceptionLog"),//
        ReadObjRL7("195.19.8.13",21,"srismapp","Sth!aix1","台中市RL","/MHNFS/DATA/RL/ExceptionLog"),//
        ReadObjRL8("195.11.31.13",21,"srismapp","Sth!aix1","台南市RL","/MHNFS/DATA/RL/ExceptionLog"),//
        ReadObj30("196.7.0.13", 21, "srismapp", "Sth!aix1", "連江縣 RR", "/MHNFS/DATA/RR/ExceptionLog"), //
        ReadObj31("196.20.0.13", 21, "srismapp", "Sth!aix1", "金門縣 RR", "/MHNFS/DATA/RR/ExceptionLog"), //
        ReadObj32("195.2.0.13", 21, "srismapp", "Sth!aix1", "宜蘭縣 RR", "/MHNFS/DATA/RR/ExceptionLog"), //
        ReadObj33("195.3.0.13", 21, "srismapp", "Sth!aix1", "桃園縣 RR", "/MHNFS/DATA/RR/ExceptionLog"), //
        ReadObj34("195.4.0.13", 21, "srismapp", "Sth!aix1", "新竹縣 RR", "/MHNFS/DATA/RR/ExceptionLog"), //
        ReadObj35("195.5.0.13", 21, "srismapp", "Sth!aix1", "苗栗縣 RR", "/MHNFS/DATA/RR/ExceptionLog"), //
        ReadObj36("195.7.0.13", 21, "srismapp", "Sth!aix1", "彰化縣 RR", "/MHNFS/DATA/RR/ExceptionLog"), //
        ReadObj37("195.8.0.13", 21, "srismapp", "Sth!aix1", "南投縣 RR", "/MHNFS/DATA/RR/ExceptionLog"), //
        ReadObj38("195.9.0.13", 21, "srismapp", "Sth!aix1", "雲林縣 RR", "/MHNFS/DATA/RR/ExceptionLog"), //
        ReadObj39("195.10.0.13", 21, "srismapp", "Sth!aix1", "嘉義縣 RR", "/MHNFS/DATA/RR/ExceptionLog"), //
        ReadObj40("195.13.0.13", 21, "srismapp", "Sth!aix1", "屏東縣 RR", "/MHNFS/DATA/RR/ExceptionLog"), //
        ReadObj41("195.14.0.13", 21, "srismapp", "Sth!aix1", "台東縣 RR", "/MHNFS/DATA/RR/ExceptionLog"), //
        ReadObj42("195.15.0.13", 21, "srismapp", "Sth!aix1", "花蓮縣 RR", "/MHNFS/DATA/RR/ExceptionLog"), //
        ReadObj43("195.16.0.13", 21, "srismapp", "Sth!aix1", "澎湖縣 RR", "/MHNFS/DATA/RR/ExceptionLog"), //
        ReadObj44("195.17.0.13", 21, "srismapp", "Sth!aix1", "基隆市 RR", "/MHNFS/DATA/RR/ExceptionLog"), //
        ReadObj45("195.18.0.13", 21, "srismapp", "Sth!aix1", "新竹市 RR", "/MHNFS/DATA/RR/ExceptionLog"), //
        ReadObj46("195.20.0.13", 21, "srismapp", "Sth!aix1", "嘉義市 RR", "/MHNFS/DATA/RR/ExceptionLog"), //
        ReadObj47("193.0.0.13", 21, "srismapp", "Sth!aix1", "台北市 RR", "/MHNFS/DATA/RR/ExceptionLog"), //
        ReadObj48("194.0.0.13", 21, "srismapp", "Sth!aix1", "高雄市 RR", "/MHNFS/DATA/RR/ExceptionLog"), //
        ReadObj49("195.1.0.13", 21, "srismapp", "Sth!aix1", "新北市 RR", "/MHNFS/DATA/RR/ExceptionLog"), //
        ReadObj50("195.19.0.13", 21, "srismapp", "Sth!aix1", "台中市 RR", "/MHNFS/DATA/RR/ExceptionLog"), //
        ReadObj51("195.21.0.13", 21, "srismapp", "Sth!aix1", "台南市 RR", "/MHNFS/DATA/RR/ExceptionLog"), //
        ;
        String ip; int port; String userId; String password; String label; String path;
        FtpSite(String ip, int port, String userId, String password, String label, String path){
            this.ip = ip;
            this.port = port;
            this.userId = userId;
            this.password = password;
            this.label = label;
            this.path = path;
        }
        public String toString(){
            return label;
        }
    }
}
