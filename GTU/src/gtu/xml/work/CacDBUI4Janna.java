package gtu.xml.work;

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

import org.apache.commons.lang.Validate;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class CacDBUI4Janna extends javax.swing.JFrame {
    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JComboBox tableNameCombo;
    private JLabel jLabel3;
    private JTextField destFileNameText;
    private JLabel jLabel4;
    private JTextField dmvText;
    private JButton executeBtn;
    private JTextField srcFileText;
    private JLabel jLabel2;
    private JLabel jLabel1;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                CacDBUI4Janna inst = new CacDBUI4Janna();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }

    public CacDBUI4Janna() {
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
                    GridLayout jPanel1Layout = new GridLayout(10, 2, 5, 5);
                    jPanel1.setLayout(jPanel1Layout);
                    jTabbedPane1.addTab("jPanel1", null, jPanel1, null);
                    {
                        jLabel1 = new JLabel();
                        jPanel1.add(jLabel1);
                        jLabel1.setText("\u8cc7\u6599\u8868\u540d\u7a31");
                    }
                    {
                        ComboBoxModel tableNameComboModel = new DefaultComboBoxModel(new String[] { "cac_fcase",
                                "cac_ecase", "cac_fcase_count" });
                        tableNameCombo = new JComboBox();
                        jPanel1.add(tableNameCombo);
                        tableNameCombo.setModel(tableNameComboModel);
                    }
                    {
                        jLabel2 = new JLabel();
                        jPanel1.add(jLabel2);
                        jLabel2.setText("xml\u6a94\u6848\u4f86\u6e90");
                    }
                    {
                        srcFileText = new JTextField();
                        JCommonUtil.jTextFieldSetFilePathMouseEvent(srcFileText, false);
                        jPanel1.add(srcFileText);
                    }
                    {
                        jLabel3 = new JLabel();
                        jPanel1.add(jLabel3);
                        jLabel3.setText("\u7522\u751fsql\u6a94\u540d");
                    }
                    {
                        destFileNameText = new JTextField();
                        jPanel1.add(destFileNameText);
                    }
                    {
                        jLabel4 = new JLabel();
                        jPanel1.add(jLabel4);
                        jLabel4.setText("dmv\u6b04\u4f4d\u503c");
                    }
                    {
                        dmvText = new JTextField();
                        jPanel1.add(dmvText);
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
            this.setSize(541, 361);
        } catch (Exception e) {
            // add your error handling code here
            e.printStackTrace();
        }
    }

    private void executeBtnActionPerformed(ActionEvent evt) {
        try {
            String tableName = (String) tableNameCombo.getSelectedItem();
            String destFileName = destFileNameText.getText();
            String dmvValue = dmvText.getText();
            File srcFile = JCommonUtil.filePathCheck(srcFileText.getText(), "xml來源檔", "xml");
            Validate.notEmpty(tableName, "表格名稱未輸入");
            Validate.notEmpty(destFileName, "要產生的目的檔名未輸入");
            Validate.isTrue(srcFile != null && srcFile.exists(), "來源檔不存在!");

            DB4Janna janna = null;
            if ("cac_fcase".equals(tableName)) {
                janna = new ReadXmlToSQL_CacFcase_4Janna();
            } else if ("cac_ecase".equals(tableName)) {
                janna = new ReadXmlToSQL_CacEcase_4Janna();
            } else if ("cac_fcase_count".equals(tableName)) {
                janna = new ReadXmlToSQL_CacFcaseCourt_4Janna();
            }
            janna.execute(dmvValue, srcFile, destFileName);
            JCommonUtil._jOptionPane_showMessageDialog_info("產生完成!");
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }
}
