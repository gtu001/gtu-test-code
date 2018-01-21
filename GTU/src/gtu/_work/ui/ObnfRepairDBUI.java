package gtu._work.ui;
import gtu._work.ObnfRepairDBBatch;
import gtu.string.StringCompressUtil;
import gtu.swing.util.JCommonUtil;
import gtu.xml.xstream.iisi.XmlParserImpl;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;

import tw.gov.moi.ae.jms.JmsMessageNew;


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
public class ObnfRepairDBUI extends javax.swing.JFrame {
    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JScrollPane jScrollPane2;
    private JLabel jLabel1;
    private JTextField domainJarText;
    private JButton readInfoBtn;
    private JButton clearAllBtn;
    private JPanel jPanel4;
    private JScrollPane jScrollPane3;
    private JScrollPane jScrollPane1;
    private JTextArea jTextArea2;
    private JTextArea jTextArea1;
    private JPanel jPanel2;

    /**
    * Auto-generated main method to display this JFrame
     * @throws DocumentException 
    */
    public static void main(String[] args) throws DocumentException {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ObnfRepairDBUI inst = new ObnfRepairDBUI();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }
    
    static class ObnfDTO {
        String wkProcessSeqNo;
        String wkNoticeType;
        String wkChgType;
        String wkSenderSiteId;
        String wkReceiverSiteId;
        String wkTableName;
        String wkNoticeDateTime;
        String wkSystemCode;
        String wkNoticeObjectMap;
        String wkOperationCode;
        Map<String,String> wkKeyMap;
        Map<String,String> wkDataObjectMap;
        @Override
        public String toString() {
            return "ObnfDTO [wkProcessSeqNo=" + wkProcessSeqNo + ", wkNoticeType=" + wkNoticeType + ", wkChgType="
                    + wkChgType + ", wkSenderSiteId=" + wkSenderSiteId + ", wkReceiverSiteId=" + wkReceiverSiteId
                    + ", wkTableName=" + wkTableName + ", wkNoticeDateTime=" + wkNoticeDateTime + ", wkSystemCode="
                    + wkSystemCode + ", wkNoticeObjectMap=" + wkNoticeObjectMap + ", wkOperationCode="
                    + wkOperationCode + ", wkKeyMap=" + wkKeyMap + ", wkDataObjectMap=" + wkDataObjectMap + "]";
        }
    }
    
    public ObnfRepairDBUI() {
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
                    BorderLayout jPanel1Layout = new BorderLayout();
                    jPanel1.setLayout(jPanel1Layout);
                    jTabbedPane1.addTab("貼上obnf內容", null, jPanel1, null);
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel1.add(jScrollPane1, BorderLayout.CENTER);
                        {
                            jTextArea1 = new JTextArea();
                            jScrollPane1.setViewportView(jTextArea1);
                            jTextArea1.setText("");
                        }
                    }
                    {
                        jPanel4 = new JPanel();
                        jPanel1.add(jPanel4, BorderLayout.NORTH);
                        jPanel4.setPreferredSize(new java.awt.Dimension(581, 62));
                        {
                            jLabel1 = new JLabel();
                            jPanel4.add(jLabel1);
                            jLabel1.setText("domainJar");
                        }
                        {
                            domainJarText = new JTextField();
                            ObnfRepairDBBatch test = new ObnfRepairDBBatch();
                            domainJarText.setText(test.fetchDomainJar());
                            JCommonUtil.jTextFieldSetFilePathMouseEvent(domainJarText, false);
                            jPanel4.add(domainJarText);
                            domainJarText.setPreferredSize(new java.awt.Dimension(400, 22));
                        }
                        {
                            readInfoBtn = new JButton();
                            jPanel4.add(readInfoBtn);
                            readInfoBtn.setText("\u8b80\u53d6xml");
                            readInfoBtn.setPreferredSize(new java.awt.Dimension(179, 22));
                            readInfoBtn.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    readInfo();
                                }
                            });
                        }
                        {
                            clearAllBtn = new JButton();
                            jPanel4.add(clearAllBtn);
                            clearAllBtn.setText("\u6e05\u9664\u5168\u90e8");
                            clearAllBtn.setPreferredSize(new java.awt.Dimension(179, 22));
                            clearAllBtn.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    jTextArea2.setText("");
                                    jTextArea1.setText("");
                                }
                            });
                        }
                    }
                }
                {
                    jPanel2 = new JPanel();
                    BorderLayout jPanel2Layout = new BorderLayout();
                    jPanel2.setLayout(jPanel2Layout);
                    jTabbedPane1.addTab("顯示訊息", null, jPanel2, null);
                    {
                        jScrollPane2 = new JScrollPane();
                        jPanel2.add(jScrollPane2, BorderLayout.CENTER);
                        {
                            jScrollPane3 = new JScrollPane();
                            jScrollPane2.setViewportView(jScrollPane3);
                            {
                                jTextArea2 = new JTextArea();
                                jScrollPane3.setViewportView(jTextArea2);
                                jTextArea2.setText("");
                            }
                        }
                    }
                }
            }
            pack();
            this.setSize(594, 436);
        } catch (Exception e) {
            //add your error handling code here
            e.printStackTrace();
        }
    }
    
    private void readInfo(){
        if(StringUtils.isBlank(jTextArea1.getText())){
            JCommonUtil._jOptionPane_showMessageDialog_error("沒訊息!");
            return;
        }
        try{
            ObnfRepairDBBatch test = new ObnfRepairDBBatch();
            if(StringUtils.isNotBlank(domainJarText.getText())){
                File domainJarFile = new File(domainJarText.getText());
                if(domainJarFile.exists() && domainJarFile.isFile() && domainJarFile.getName().endsWith(".jar")){
                    test.setDomainJar(domainJarFile);
                }
            }
            String message = jTextArea1.getText();
            if(message.startsWith("H4sI")){
                XmlParserImpl xmlParserImpl = new XmlParserImpl();
                final String jmsMessageXML = StringCompressUtil.uncompress(message);
                JmsMessageNew jmsMessageNew = (JmsMessageNew)xmlParserImpl.parseToObj(jmsMessageXML);
                message = jmsMessageNew.getMessageXML();
            }
            jTextArea2.setText(test.executeSingleXml(message));
            System.out.println("done...");
        }catch(Exception ex){
            JCommonUtil.handleException(ex);
        }
    }
}
