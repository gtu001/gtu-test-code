package gtu._work.ui;
import gtu.ftp.FtpUtil;
import gtu.ftp.FtpUtil.FtpFileInfo;
import gtu.swing.util.JCommonUtil;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;


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
public class ObnfCheckPDFErrorUI extends javax.swing.JFrame {
    private JTabbedPane jTabbedPane1;
    private JScrollPane jScrollPane2;
    private JButton checkBtn;
    private JTextArea logArea;
    private JPanel jPanel2;

    /**
    * Auto-generated main method to display this JFrame
    */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ObnfCheckPDFErrorUI inst = new ObnfCheckPDFErrorUI();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }
    
    public ObnfCheckPDFErrorUI() {
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
                    jPanel2 = new JPanel();
                    BorderLayout jPanel2Layout = new BorderLayout();
                    jPanel2.setLayout(jPanel2Layout);
                    jTabbedPane1.addTab("log", null, jPanel2, null);
                    {
                        jScrollPane2 = new JScrollPane();
                        jPanel2.add(jScrollPane2, BorderLayout.CENTER);
                        {
                            logArea = new JTextArea();
                            jScrollPane2.setViewportView(logArea);
                        }
                    }
                    {
                        checkBtn = new JButton();
                        jPanel2.add(checkBtn, BorderLayout.SOUTH);
                        checkBtn.setText("\u6aa2\u67e5");
                        checkBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                checkBtnActionPerformed();
                            }
                        });
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
    
    private void checkBtnActionPerformed(){
        try{
            for(FtpSite ftpSite : FtpSite.values()){
                FtpUtil ftpUtil = new FtpUtil();
                ftpUtil.connect(ftpSite.ip, ftpSite.port, ftpSite.userId, ftpSite.password, false);
                FTPClient ftp = ftpUtil.getFtp();
                logArea.append("取得" + ftpSite.label + "\n");
                ftp.enterLocalPassiveMode();
                FTPFile[] ftpFiles = ftp.listFiles(ftpSite.path1);
                logArea.append("-->" + ftpSite.path1 + "\n");
                for (int i = 0; i < ((ftpFiles == null) ? 0 : ftpFiles.length); i++) {
                    FTPFile ftpFile = ftpFiles[i];
                    if (ftpFile.isDirectory()) {
                        String p = ftpSite.path1 + "/" + ftpFile.getName() + "/" + ftpSite.path2 + "/";
                        logArea.append("-->" + p + "\n");
                        List<FtpFileInfo> fileList = new ArrayList<FtpFileInfo>();
                        ftpUtil.scanFindFile(p, ".*", fileList, ftp);
                        for(FtpFileInfo f : fileList){
                            logArea.append("-->" + f.getAbsolutePath() + "--" + f.getSize() + "\n");
                            if(!f.isDirectory() && f.getSize() == 0){
                                logArea.append("##" + ftpSite.label + "\t" + f.getAbsolutePath() + "\n");
                            }
                        }
                    }
                }

                ftpUtil.disconnect();
                logArea.append("完成:" + ftpSite.label);
            }
            
            JCommonUtil._jOptionPane_showMessageDialog_info("掃完畢!");
        }catch(Exception ex){
            JCommonUtil.handleException(ex);
        }
    }

    enum FtpSite {
        ReadObj6 ("195.19.8.13", 21, "srismapp", "Sth!aix1", "台中 RL", "/MHNFS/DATA/RL/","/APPLY/Incoming/"),//
        ReadObj7 ("193.0.3.13", 21, "srismapp", "Sth!aix1", "台北市 RL", "/MHNFS/DATA/RL/","/APPLY/Incoming/"),//
        ReadObj8 ("195.11.31.13", 21, "srismapp", "Sth!aix1", "台南縣 RL", "/MHNFS/DATA/RL/","/APPLY/Incoming/"),//
        ReadObj8X ("195.14.1.13", 21, "srismapp", "Sth!aix1", "台東縣 RL", "/MHNFS/DATA/RL/","/APPLY/Incoming/"),//
        ReadObj9 ("195.2.1.13", 21, "srismapp", "Sth!aix1", "宜蘭縣 RL", "/MHNFS/DATA/RL/","/APPLY/Incoming/"),//
        ReadObj10 ("195.15.1.13", 21, "srismapp", "Sth!aix1", "花蓮縣 RL", "/MHNFS/DATA/RL/","/APPLY/Incoming/"),//
        ReadObj11 ("196.20.100.13", 21, "srismapp", "Sth!aix1", "金門縣 RL", "/MHNFS/DATA/RL/","/APPLY/Incoming/"),//
        ReadObj12 ("195.13.1.13", 21, "srismapp", "Sth!aix1", "屏東 RL", "/MHNFS/DATA/RL/","/APPLY/Incoming/"),//
        ReadObj13 ("195.5.1.13", 21, "srismapp", "Sth!aix1", "苗栗縣 RL", "/MHNFS/DATA/RL/","/APPLY/Incoming/"),//
        ReadObj14 ("195.3.8.13", 21, "srismapp", "Sth!aix1", "桃園縣 RL", "/MHNFS/DATA/RL/","/APPLY/Incoming/"),//
        ReadObj16 ("194.0.205.13", 21, "srismapp", "Sth!aix1", "高雄市 RL", "/MHNFS/DATA/RL/","/APPLY/Incoming/"),//
        ReadObj17 ("195.17.5.14", 21, "srismapp", "Sth!aix1", "基隆 RL", "/MHNFS/DATA/RL/","/APPLY/Incoming/"),//
        ReadObj18 ("195.9.1.13", 21, "srismapp", "Sth!aix1", "雲林 RL", "/MHNFS/DATA/RL/","/APPLY/Incoming/"),//
        ReadObj20 ("195.1.102.13", 21, "srismapp", "Sth!aix1", "新北市 RL", "/MHNFS/DATA/RL/","/APPLY/Incoming/"),//
        ReadObj21 ("195.18.1.13", 21, "srismapp", "Sth!aix1", "新竹市 RL", "/MHNFS/DATA/RL/","/APPLY/Incoming/"),//
        ReadObj2X ("195.4.1.13", 21, "srismapp", "Sth!aix1", "新竹縣 RL", "/MHNFS/DATA/RL/","/APPLY/Incoming/"),//
        ReadObj22 ("194.0.205.13", 21, "srismapp", "Sth!aix1", "新站台", "/MHNFS/DATA/RL/","/APPLY/Incoming/"),//
        ReadObj23 ("195.10.7.13", 21, "srismapp", "Sth!aix1", "嘉義縣 RL", "/MHNFS/DATA/RL/","/APPLY/Incoming/"),//
        ReadObj24 ("195.7.1.13", 21, "srismapp", "Sth!aix1", "彰化縣 RL", "/MHNFS/DATA/RL/","/APPLY/Incoming/"),//
        ReadObj25 ("195.16.1.13", 21, "srismapp", "Sth!aix1", "澎湖縣 RL", "/MHNFS/DATA/RL/","/APPLY/Incoming/"),//
        ReadObjRL1("196.7.100.13",21,"srismapp","Sth!aix1","連江縣RL","/MHNFS/DATA/RL/","/APPLY/Incoming/"),//
        ReadObjRL2("195.8.1.13",21,"srismapp","Sth!aix1","南投縣RL","/MHNFS/DATA/RL/","/APPLY/Incoming/"),//
        ReadObjRL3("195.9.1.13",21,"srismapp","Sth!aix1","雲林縣RL","/MHNFS/DATA/RL/","/APPLY/Incoming/"),//
        ReadObjRL4("195.13.1.13",21,"srismapp","Sth!aix1","屏東縣RL","/MHNFS/DATA/RL/","/APPLY/Incoming/"),//
        ReadObjRL5("195.17.5.13",21,"srismapp","Sth!aix1","基隆市RL","/MHNFS/DATA/RL/","/APPLY/Incoming/"),//
        ReadObjRL6("195.20.1.13",21,"srismapp","Sth!aix1","嘉義市RL","/MHNFS/DATA/RL/","/APPLY/Incoming/"),//
        ReadObjRL7("195.19.8.13",21,"srismapp","Sth!aix1","台中市RL","/MHNFS/DATA/RL/","/APPLY/Incoming/"),//
        ReadObjRL8("195.11.31.13",21,"srismapp","Sth!aix1","台南市RL","/MHNFS/DATA/RL/","/APPLY/Incoming/"),//
        ;
        String ip; int port; String userId; String password; String label; String path1; String path2;
        FtpSite(String ip, int port, String userId, String password, String label, String path1, String path2){
            this.ip = ip;
            this.port = port;
            this.userId = userId;
            this.password = password;
            this.label = label;
            this.path1 = path1;
            this.path2 = path2;
        }
        public String toString(){
            return label;
        }
    }
}
