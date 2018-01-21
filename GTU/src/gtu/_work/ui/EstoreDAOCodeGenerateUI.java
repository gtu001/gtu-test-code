package gtu._work.ui;

import gtu.file.FileUtil;
import gtu.swing.util.JCommonUtil;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.Validate;

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
public class EstoreDAOCodeGenerateUI extends javax.swing.JFrame {
    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JScrollPane jScrollPane2;
    private JScrollPane jScrollPane1;
    private JTextArea xmlConfigArea;
    private JPanel jPanel2;
    private JButton updateBtn;
    private JTextField daoInterfaceText;
    private JTextField daoImplText;
    private JTextField entityNameText;
    private JLabel jLabel1;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                EstoreDAOCodeGenerateUI inst = new EstoreDAOCodeGenerateUI();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }

    public EstoreDAOCodeGenerateUI() {
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
                jTabbedPane1.setPreferredSize(new java.awt.Dimension(717, 582));
                {
                    jPanel1 = new JPanel();
                    GridLayout jPanel1Layout = new GridLayout(15, 1);
                    jPanel1.setLayout(jPanel1Layout);
                    jTabbedPane1.addTab("基本設定", null, jPanel1, null);
                    {
                        jLabel1 = new JLabel();
                        jPanel1.add(jLabel1);
                        jLabel1.setText("entity\u540d\u7a31");
                    }
                    {
                        entityNameText = new JTextField();
                        jPanel1.add(entityNameText);
                    }
                    {
                        jLabel4 = new JLabel();
                        jPanel1.add(jLabel4);
                        jLabel4.setText("DAOImpl\u540d\u7a31");
                    }
                    {
                        daoImplText = new JTextField();
                        jPanel1.add(daoImplText);
                    }
                    {
                        jLabel5 = new JLabel();
                        jPanel1.add(jLabel5);
                        jLabel5.setText("DAOInterface\u540d\u7a31");
                    }
                    {
                        daoInterfaceText = new JTextField();
                        jPanel1.add(daoInterfaceText);
                    }
                    {
                        updateBtn = new JButton();
                        jPanel1.add(updateBtn);
                        updateBtn.setText("\u66f4\u65b0");
                        updateBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                updateBtnActionPerformed(evt);
                            }
                        });
                    }
                    {
                        makeFileBtn = new JButton();
                        jPanel1.add(makeFileBtn);
                        makeFileBtn.setText("\u7522\u751f\u6a94\u6848");
                        makeFileBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                try {
                                    makeFileBtnActionPerformed(evt);
                                } catch (IOException e) {
                                    JCommonUtil.handleException(e);
                                }
                            }
                        });
                    }
                }
                {
                    jPanel2 = new JPanel();
                    BorderLayout jPanel2Layout = new BorderLayout();
                    jPanel2.setLayout(jPanel2Layout);
                    jTabbedPane1.addTab("xml設定", null, jPanel2, null);
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel2.add(jScrollPane1, BorderLayout.CENTER);
                        jScrollPane1.setPreferredSize(new java.awt.Dimension(712, 458));
                        {
                            jScrollPane2 = new JScrollPane();
                            jScrollPane1.setViewportView(jScrollPane2);
                            {
                                xmlConfigArea = new JTextArea();
                                jScrollPane2.setViewportView(xmlConfigArea);
                            }
                        }
                    }
                }
            }
            pack();
            this.setSize(733, 525);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private JButton makeFileBtn;
    
    private static final String DAO_IMPL;
    private static final String DAO_INTERFACE;
    private static final String DAO_SPRING_XML;
    static {
        StringBuilder sb = new StringBuilder();
//        sb.append(" package com.sti.estore.dao;                                                              \n");
        sb.append(" package %2$s;                                                              \n");
        sb.append("                                                                                          \n");
        sb.append(" import com.sti.estore.model.%1$s;                                                        \n");
        sb.append("                                                                                          \n");
        sb.append(" public interface %1$sDAO extends BaseDAO<%1$s, Long>{                                    \n");
        sb.append(" }                                                                                        \n");
        DAO_INTERFACE = sb.toString();
        
        sb = new StringBuilder();
//        sb.append(" package com.sti.estore.dao.impl;                                                         \n");
        sb.append(" package %2$s;                                                         \n");
        sb.append("                                                                                          \n");
        sb.append(" import java.util.List;                                                                   \n");
        sb.append("                                                                                          \n");
        sb.append(" import org.apache.commons.lang.StringUtils;                                              \n");
        sb.append(" import org.hibernate.Criteria;                                                           \n");
        sb.append(" import org.hibernate.Query;                                                              \n");
        sb.append(" import org.hibernate.criterion.Restrictions;                                             \n");
        sb.append("                                                                                          \n");
        sb.append(" import com.sti.estore.dao.%1$sDAO;                                                       \n");
        sb.append(" import com.sti.estore.model.%1$s;                                                        \n");
        sb.append(" import com.sti.estore.model.RenewalRatePlan;                                             \n");
        sb.append("                                                                                          \n");
        sb.append(" public class %1$sDAOImpl extends AbstractBaseDAO<%1$s, Long> implements %1$sDAO{         \n");
        sb.append(" }                                                                                        \n");
        DAO_IMPL = sb.toString();
        
        sb = new StringBuilder();
        sb.append("    <bean id=\"%2$sDAO\" class=\"%3$s.%1$sDAOImpl\">                 \n");
        sb.append("        <property name=\"sessionFactory\" ref=\"sessionFactory\" />  \n");
        sb.append("    </bean>                                                      \n");
        DAO_SPRING_XML = sb.toString();
    }
    
    private void updateBtnActionPerformed(ActionEvent evt) {
        String entityName = entityNameText.getText();
        Validate.notBlank(entityName, "類別名稱必田");
        daoImplText.setText(String.format("src/main/java/com/sti/estore/dao/impl/%1$sDAOImpl.java", entityName));
        daoInterfaceText.setText(String.format("src/main/java/com/sti/estore/dao/%1$sDAO.java", entityName));
    }
    
    private void makeFileBtnActionPerformed(ActionEvent evt) throws IOException {
        String entityName = entityNameText.getText();
        String daoImplName = StringUtils.defaultString(daoImplText.getText());
        String daoInterfaceName = StringUtils.defaultString(daoInterfaceText.getText());
        Validate.notBlank(entityName, "類別名稱必田");
        Validate.notBlank(daoImplName, "daoImpl必田");
        Validate.notBlank(daoInterfaceName, "daoInterface必田");
        
        String daoImplPackage = daoImplName.replaceFirst("src/main/java/", "").replaceFirst(".java", "").replace('/', '.');
        String daoInterfacePackage = daoInterfaceName.replaceFirst("src/main/java/", "").replaceFirst(".java", "").replace('/', '.');
        daoImplPackage = daoImplPackage.substring(0, daoImplPackage.lastIndexOf("."));
        daoInterfacePackage = daoInterfacePackage.substring(0, daoInterfacePackage.lastIndexOf("."));
        System.out.println("daoImplName = " + daoImplName);
        System.out.println("daoInterfaceName = " + daoInterfaceName);
        System.out.println("daoImplPackage = " + daoImplPackage);
        System.out.println("daoInterfacePackage = " + daoInterfacePackage);
        
        String littleEntityName = entityName.substring(0,1).toLowerCase() + entityName.substring(1);
        
        String daoImplTxt = String.format(DAO_IMPL, entityName, daoImplPackage);
        String daoInterfaceTxt = String.format(DAO_INTERFACE, entityName, daoInterfacePackage);
        String daoSpringXmlTxt = String.format(DAO_SPRING_XML, entityName, littleEntityName, daoImplPackage);
        
        xmlConfigArea.setText(daoSpringXmlTxt);
        
        File daoImplFile = new File(FileUtil.DESKTOP_DIR, daoImplName);
        File daoInterfaceFile = new File(FileUtil.DESKTOP_DIR, daoInterfaceName);
        daoImplFile.getParentFile().mkdirs();
        daoInterfaceFile.getParentFile().mkdirs();
        
        FileUtils.write(daoImplFile, daoImplTxt, "utf8");
        FileUtils.write(daoInterfaceFile, daoInterfaceTxt, "utf8");
        JCommonUtil._jOptionPane_showMessageDialog_info("產生檔案於桌面!");
    }
}
