package gtu._work.ui;
import gtu.properties.PropertiesUtil;
import gtu.swing.util.JCommonUtil;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.commons.lang.StringUtils;

import _temp.TestForJermy;



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
public class JermyP100UI extends javax.swing.JFrame {
    private JLabel jLabel1;
    private JLabel label2;
//    private JTextField rcdfp100Text;
    private JButton executeBtn;
    private JTextField rcdfp100Text;
    private JTextField fileP100Text;

    /**
    * Auto-generated main method to display this JFrame
    */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JermyP100UI inst = new JermyP100UI();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }
    
    public JermyP100UI() {
        super();
        initGUI();
    }
    
    private void initGUI() {
        try {
            FlowLayout thisLayout = new FlowLayout();
            getContentPane().setLayout(thisLayout);
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            {
                jLabel1 = new JLabel();
                getContentPane().add(jLabel1);
                jLabel1.setText("FileP100.xls");
                jLabel1.setPreferredSize(new java.awt.Dimension(68, 15));
            }
            {
                fileP100Text = new JTextField();
                JCommonUtil.jTextFieldSetFilePathMouseEvent(fileP100Text, false);
                getContentPane().add(fileP100Text);
                fileP100Text.setPreferredSize(new java.awt.Dimension(340, 22));
            }
            {
                label2 = new JLabel();
                getContentPane().add(label2);
                getContentPane().add(label2);
                label2.setText("RCDFP100.xls");
                label2.setPreferredSize(new java.awt.Dimension(84, 15));
            }
            {
                rcdfp100Text = new JTextField();
                JCommonUtil.jTextFieldSetFilePathMouseEvent(rcdfp100Text, false);
                getContentPane().add(rcdfp100Text);
                rcdfp100Text.setPreferredSize(new java.awt.Dimension(340,22));
            }
            {
                executeBtn = new JButton();
                getContentPane().add(executeBtn);
                executeBtn.setText("\u7522\u751f\u532f\u51fa\u6a94");
                executeBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if(StringUtils.isBlank(rcdfp100Text.getText())){
                            JCommonUtil._jOptionPane_showMessageDialog_error("RCDFP100.xls檔案路徑錯誤");
                            return;
                        }
                        File rcdfp100File = new File(StringUtils.defaultString(rcdfp100Text.getText()));
                        if(!rcdfp100File.exists()){
                            JCommonUtil._jOptionPane_showMessageDialog_error("RCDFP100.xls檔案路徑錯誤");
                            return;
                        }
                        
                        if(StringUtils.isBlank(fileP100Text.getText())){
                            JCommonUtil._jOptionPane_showMessageDialog_error("FileP100.xls檔案路徑錯誤");
                            return;
                        }
                        File fileP100File = new File(StringUtils.defaultString(fileP100Text.getText()));
                        if(!rcdfp100File.exists()){
                            JCommonUtil._jOptionPane_showMessageDialog_error("FileP100.xls檔案路徑錯誤");
                            return;
                        }
                        
                        try{
                            TestForJermy test = new TestForJermy();
                            test.execute(fileP100File, rcdfp100File, PropertiesUtil.getJarCurrentPath(JermyP100UI.class));
                            File outputFile = test.getOutputCvs();
                            JCommonUtil._jOptionPane_showMessageDialog_info("產生匯出黨:\n" + outputFile.getAbsolutePath());
                        }catch(Exception ex){
                            JCommonUtil.handleException(ex);
                        }
                    }
                });
            }
            pack();
            this.setSize(457, 146);
        } catch (Exception e) {
            //add your error handling code here
            e.printStackTrace();
        }
    }

}
