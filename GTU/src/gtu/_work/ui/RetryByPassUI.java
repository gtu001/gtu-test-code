package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.commons.lang.StringUtils;


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
public class RetryByPassUI extends javax.swing.JFrame {
    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JTextArea messageIdArea;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JTextArea executeLogArea;
    private JPanel jPanel3;
    private JPanel jPanel2;
    private JButton sendBtn;
    private JComboBox passRetryComboBox;
    private JComboBox countryComboBox;

    /**
    * Auto-generated main method to display this JFrame
    */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                RetryByPassUI inst = new RetryByPassUI();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }
    
    public RetryByPassUI() {
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
                    FlowLayout jPanel1Layout = new FlowLayout();
                    jPanel1.setLayout(jPanel1Layout);
                    jTabbedPane1.addTab("選功能", null, jPanel1, null);
                    {
                        DefaultComboBoxModel countryComboBoxModel = 
                                new DefaultComboBoxModel();
                        for(CountyEnum e : CountyEnum.values()){
                            countryComboBoxModel.addElement(e.countyLabel);
                        }
                        countryComboBox = new JComboBox();
                        jPanel1.add(countryComboBox);
                        countryComboBox.setModel(countryComboBoxModel);
                    }
                    {
                        ComboBoxModel passRetryComboBoxModel = 
                                new DefaultComboBoxModel(
                                        new String[] { "byPass", "reTry", "status" });
                        passRetryComboBox = new JComboBox();
                        jPanel1.add(passRetryComboBox);
                        passRetryComboBox.setModel(passRetryComboBoxModel);
                    }
                    {
                        sendBtn = new JButton();
                        jPanel1.add(sendBtn);
                        sendBtn.setText("\u9001\u51fa");
                        sendBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                //TODO add your code for sendBtn.actionPerformed
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        readMessageId();
                                    }
                                }).start();
                            }
                        });
                    }
                }
                {
                    jPanel2 = new JPanel();
                    BorderLayout jPanel2Layout = new BorderLayout();
                    jPanel2.setLayout(jPanel2Layout);
                    jTabbedPane1.addTab("貼messageId", null, jPanel2, null);
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel2.add(jScrollPane1, BorderLayout.CENTER);
                        jScrollPane1.setPreferredSize(new java.awt.Dimension(604, 375));
                        {
                            messageIdArea = new JTextArea();
                            jScrollPane1.setViewportView(messageIdArea);
                        }
                    }
                }
                {
                    jPanel3 = new JPanel();
                    BorderLayout jPanel3Layout = new BorderLayout();
                    jPanel3.setLayout(jPanel3Layout);
                    jTabbedPane1.addTab("執行結果", null, jPanel3, null);
                    {
                        jScrollPane2 = new JScrollPane();
                        jPanel3.add(jScrollPane2, BorderLayout.CENTER);
                        jScrollPane2.setPreferredSize(new java.awt.Dimension(604, 375));
                        {
                            executeLogArea = new JTextArea();
                            jScrollPane2.setViewportView(executeLogArea);
                        }
                    }
                }
            }
            pack();
            this.setSize(617, 429);
        } catch (Exception e) {
            //add your error handling code here
            e.printStackTrace();
        }
    }
    
    private void showMessage(String message){
        JOptionPane.showMessageDialog(null, message, "ERROR", JOptionPane.ERROR_MESSAGE);
    }
    
    private void readMessageId() {
        StringBuilder sb = new StringBuilder();
        try {
            String county = (String)countryComboBox.getSelectedItem();
            if(StringUtils.isBlank(county)){
                showMessage("未選擇連線單位");
            }
            String type = (String)passRetryComboBox.getSelectedItem();
            if(StringUtils.isBlank(type)){
                showMessage("未處理處理方式");
            }
            String messageIdAreaStr = messageIdArea.getText();
            if(StringUtils.isBlank(messageIdAreaStr)){
                showMessage("未輸入messageId");
            }
            String ip = "";
            String region = "";
            String port = "";
            for(CountyEnum e : CountyEnum.values()){
                if(e.countyLabel.equals(county)){
                    ip = e.ip;
                    region = e.region;
                    port = e.port;
                    break;
                }
            }
            
            List<String> messageIdList = new ArrayList<String>();
            StringTokenizer tokenizer = new StringTokenizer(messageIdAreaStr);
            for(;tokenizer.hasMoreElements();){
                String val = (String)tokenizer.nextElement();
                messageIdList.add(val);
            }
            
            for(String messageId : messageIdList){
                String urlStr = String.format("http://%1$s:%2$s/%3$s/version?MQ=%4$s:%5$s", ip, port, region, type, messageId);
                setLog("URL ==> " + urlStr, sb);
                
                URL url_address = new URL(urlStr);
                BufferedReader br = new BufferedReader(new InputStreamReader(url_address.openStream(), "UTF-8"));
                for(String line = null; (line = br.readLine())!=null ;){
                    setLog(line, sb);
                }
                setLog("***************************************************************", sb);
                setLog("***************************************************************", sb);
                br.close();
            }
            
            showMessage("執行完成!!");
        } catch (Exception e) {
//            JCommonUtil.handleException(e);
            showMessage(e.toString());
        }
    }
    
    private void setLog(String str, StringBuilder sb){
        sb.append(str + "\n");
        executeLogArea.setText(sb.toString());
    }
    
    enum CountyEnum {
        RL_CountySIT0("SIT RL新北","192.168.10.18","rl","6280"),//
        RL_CountySIT01("SIT RL嘉義","192.168.10.18","rl","6180"),//
        RL_CountySIT1("SIT RR新北","192.168.10.18","rr","6280"),//
        RL_CountySIT11("SIT RR嘉義","192.168.10.18","rr","6180"),//
        RL_CountySIT2("SIT RC","192.168.10.18","rc","6180"),//
        RL_CountyUAT0("UAT RL新北","140.92.86.155","rl","5103"),//
        RL_CountyUAT01("UAT RL嘉義","140.92.86.173","rl","5103"),//
        RL_CountyUAT1("UAT RR新北","140.92.86.155","rr","5103"),//
        RL_CountyUAT11("UAT RR嘉義","140.92.86.173","rr","5103"),//
        RL_CountyUAT2("UAT RC","140.92.86.173","rc","5103"),//
        RL_County1("連江縣RL","196.7.100.13","rl","4103"),//
        RL_County2("金門縣RL","196.20.100.13","rl","4103"),//
        RL_County3("宜蘭縣RL","195.2.1.13","rl","4103"),//
        RL_County4("桃園縣RL","195.3.8.13","rl","4103"),//
        RL_County5("新竹縣RL","195.4.1.13","rl","4103"),//
        RL_County6("苗栗縣RL","195.5.1.13","rl","4103"),//
        RL_County7("彰化縣RL","195.7.1.13","rl","4103"),//
        RL_County8("南投縣RL","195.8.1.13","rl","4103"),//
        RL_County9("雲林縣RL","195.9.1.13","rl","4103"),//
        RL_County10("嘉義縣RL","195.10.7.13","rl","4103"),//
        RL_County11("屏東縣RL","195.13.1.13","rl","4103"),//
        RL_County12("台東縣RL","195.14.1.13","rl","4103"),//
        RL_County13("花蓮縣RL","195.15.1.13","rl","4103"),//
        RL_County14("澎湖縣RL","195.16.1.13","rl","4103"),//
        RL_County15("基隆市RL","195.17.5.13","rl","4103"),//
        RL_County16("新竹市RL","195.18.1.13","rl","4103"),//
        RL_County17("嘉義市RL","195.20.1.13","rl","4103"),//
        RL_County18("台北市RL","193.0.3.13","rl","4103"),//
        RL_County19("高雄市RL","194.0.205.13","rl","4103"),//
        RL_County20("新北市RL","195.1.102.13","rl","4103"),//
        RL_County21("台中市RL","195.19.8.13","rl","4103"),//
        RL_County22("台南市RL","195.11.31.13","rl","4103"),//
        RR_County1("連江縣 RR","196.7.0.13","rr","4103"),//
        RR_County2("金門縣 RR","196.20.0.13","rr","4103"),//
        RR_County3("宜蘭縣 RR","195.2.0.13","rr","4103"),//
        RR_County4("桃園縣 RR","195.3.0.13","rr","4103"),//
        RR_County5("新竹縣 RR","195.4.0.13","rr","4103"),//
        RR_County6("苗栗縣 RR","195.5.0.13","rr","4103"),//
        RR_County7("彰化縣 RR","195.7.0.13","rr","4103"),//
        RR_County8("南投縣 RR","195.8.0.13","rr","4103"),//
        RR_County9("雲林縣 RR","195.9.0.13","rr","4103"),//
        RR_County10("嘉義縣 RR","195.10.0.13","rr","4103"),//
        RR_County11("屏東縣 RR","195.13.0.13","rr","4103"),//
        RR_County12("台東縣 RR","195.14.0.13","rr","4103"),//
        RR_County13("花蓮縣 RR","195.15.0.13","rr","4103"),//
        RR_County14("澎湖縣 RR","195.16.0.13","rr","4103"),//
        RR_County15("基隆市 RR","195.17.0.13","rr","4103"),//
        RR_County16("新竹市 RR","195.18.0.13","rr","4103"),//
        RR_County17("嘉義市 RR","195.20.0.13","rr","4103"),//
        RR_County18("台北市 RR","193.0.0.13","rr","4103"),//
        RR_County19("高雄市 RR","194.0.0.13","rr","4103"),//
        RR_County20("新北市 RR","195.1.0.13","rr","4103"),//
        RR_County21("台中市 RR","195.19.0.13","rr","4103"),//
        RR_County22("台南市 RR","195.21.0.13","rr","4103"),//
        RC_County("內政部 RC","192.1.0.83","rc","4103"),//
        ;
        final String countyLabel;
        final String ip;
        final String region;
        final String port;
        CountyEnum(String countyLabel, String ip, String region, String port){
            this.countyLabel = countyLabel;
            this.ip = ip;
            this.region = region;
            this.port = port;
        }
    }
}
