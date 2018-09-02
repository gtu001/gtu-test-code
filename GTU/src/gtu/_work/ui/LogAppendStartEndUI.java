package gtu._work.ui;
import gtu.log.LogAppendStartEnd;
import gtu.swing.util.JCommonUtil;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
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
public class LogAppendStartEndUI extends javax.swing.JFrame {
    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JButton executeBtn;
    private JComboBox onOffDebugSwitchComboBox;
    private JTextField methodStartText;
    private JTextField methodEndText;
    private JLabel jLabel2;
    private JTextField javaSrcFileText;
    private JLabel jLabel1;

    /**
    * Auto-generated main method to display this JFrame
    */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LogAppendStartEndUI inst = new LogAppendStartEndUI();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }
    
    public LogAppendStartEndUI() {
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
                    GridLayout jPanel1Layout = new GridLayout(10, 1);
                    jPanel1.setLayout(jPanel1Layout);
                    jTabbedPane1.addTab("jPanel1", null, jPanel1, null);
                    {
                        jLabel1 = new JLabel();
                        jPanel1.add(jLabel1);
                        jLabel1.setText("java\u6a94");
                    }
                    {
                        javaSrcFileText = new JTextField();
                        jPanel1.add(javaSrcFileText);
                        JCommonUtil.jTextFieldSetFilePathMouseEvent(javaSrcFileText, false);
                    }
                    {
                        jLabel2 = new JLabel();
                        jPanel1.add(jLabel2);
                        jLabel2.setText("method\u958b\u59cb");
                    }
                    {
                        methodStartText = new JTextField();
                        methodStartText.setText("\"#. ${method} .s\"");
                        jPanel1.add(methodStartText);
                    }
                    {
                        jLabel3 = new JLabel();
                        jPanel1.add(jLabel3);
                        jLabel3.setText("method\u7d50\u675f");
                    }
                    {
                        methodEndText = new JTextField();
                        methodEndText.setText("\"#. ${method} .e\"");
                        jPanel1.add(methodEndText);
                    }
                    {
                        jLabel4 = new JLabel();
                        jPanel1.add(jLabel4);
                        jLabel4.setText("\u7528\u684c\u9762\u8a2d\u5b9a\u6a94\u6c7a\u5b9adebug\u958b\u8d77\u6216\u95dc\u9589");
                    }
                    {
                        ComboBoxModel onOffDebugSwitchComboBoxModel = 
                                new DefaultComboBoxModel(
                                        new String[] {  "false", "true" });
                        onOffDebugSwitchComboBox = new JComboBox();
                        jPanel1.add(onOffDebugSwitchComboBox);
                        onOffDebugSwitchComboBox.setModel(onOffDebugSwitchComboBoxModel);
                    }
                    {
                        executeBtn = new JButton();
                        jPanel1.add(executeBtn);
                        executeBtn.setText("\u57f7\u884c");
                        executeBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                executeBtnActionPerformed(evt);
                            }
                        });
                    }
                }
            }
            pack();
            this.setSize(587, 404);
        } catch (Exception e) {
            //add your error handling code here
            e.printStackTrace();
        }
    }
    
    private void executeBtnActionPerformed(ActionEvent evt) {
        try{
            String methodStart = StringUtils.defaultIfEmpty(methodStartText.getText(), "xxxxxxxxxxxxxxxxx");
            String methodEnd = StringUtils.defaultIfEmpty(methodEndText.getText(), "xxxxxxxxxxxxxxxxx");
            File file = JCommonUtil.filePathCheck(javaSrcFileText.getText(), "java黨", "java");
            boolean onOffControl = Boolean.parseBoolean((String)onOffDebugSwitchComboBox.getSelectedItem());
            LogAppendStartEnd test = new LogAppendStartEnd(methodStart, methodEnd, onOffControl);
            test.execute(file);
            JCommonUtil._jOptionPane_showMessageDialog_info("檔案產生成功:\n" + file);
        }catch(Exception ex){
            JCommonUtil.handleException(ex);
        }
    }
}
