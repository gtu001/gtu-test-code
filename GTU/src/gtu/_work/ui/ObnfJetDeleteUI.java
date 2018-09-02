package gtu._work.ui;
import gtu._work.ObnfRepairDBBatch_forJet1;
import gtu.swing.util.JCommonUtil;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
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
public class ObnfJetDeleteUI extends javax.swing.JFrame {
    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JTextArea excludeArea;
    private JButton executeBtn;
    private JLabel jLabel1;
    private JTextField srcDirText;

    /**
    * Auto-generated main method to display this JFrame
    */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ObnfJetDeleteUI inst = new ObnfJetDeleteUI();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }
    
    public ObnfJetDeleteUI() {
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
                    jTabbedPane1.addTab("jPanel1", null, jPanel1, null);
                    {
                        jLabel1 = new JLabel();
                        jPanel1.add(jLabel1);
                        jLabel1.setText("\u8acb\u586b\u5165\u76ee\u9304");
                    }
                    {
                        srcDirText = new JTextField();
                        JCommonUtil.jTextFieldSetFilePathMouseEvent(srcDirText, true);
                        jPanel1.add(srcDirText);
                        srcDirText.setPreferredSize(new java.awt.Dimension(288, 22));
                    }
                    {
                        executeBtn = new JButton();
                        jPanel1.add(executeBtn);
                        executeBtn.setText("\u57f7\u884c");
                        executeBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                executeBtnPerformed();
                            }
                        });
                    }
                    {
                        excludeArea = new JTextArea();
                        jPanel1.add(excludeArea);
                        excludeArea.setPreferredSize(new java.awt.Dimension(418, 238));
                    }
                }
            }
            pack();
            this.setSize(511, 339);
        } catch (Exception e) {
            //add your error handling code here
            e.printStackTrace();
        }
    }

    private void executeBtnPerformed(){
        try{
            String excludePersonId = StringUtils.defaultString(excludeArea.getText());
            File file = JCommonUtil.filePathCheck(srcDirText.getText(), "exceptionLog黨", false);
            ObnfRepairDBBatch_forJet1 test = new ObnfRepairDBBatch_forJet1();
            test.execute(excludePersonId, file);
            JCommonUtil._jOptionPane_showMessageDialog_info("執行完成, 請於xml黨所在目錄尋找 outputXXX檔案");
        }catch(Exception ex){
            JCommonUtil.handleException(ex);
        }
    }
}
