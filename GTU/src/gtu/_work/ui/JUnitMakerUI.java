package gtu._work.ui;

import gtu._work.classmaker.ApiMatcher;
import gtu._work.classmaker.JUnitMaker;
import gtu.swing.util.JFileChooserUtil;
import gtu.swing.util.JOptionPaneUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.commons.lang.StringUtils;
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
public class JUnitMakerUI extends javax.swing.JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JUnitMakerUI inst = new JUnitMakerUI();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }

    public JUnitMakerUI() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            GroupLayout thisLayout = new GroupLayout((JComponent) getContentPane());
            getContentPane().setLayout(thisLayout);
            this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            {
                jTextField1 = new JTextField();
            }
            {
                jLabel1 = new JLabel();
                jLabel1.setText("Class");
            }
            {
                jTextField2 = new JTextField();
            }
            {
                jLabel2 = new JLabel();
                jLabel2.setText("Package");
            }
            {
                jTextField3 = new JTextField();
            }
            {
                jLabel3 = new JLabel();
                jLabel3.setText("fieldName");
            }
            {
                jTextArea1 = new JTextArea();
            }
            {
                jButton1 = new JButton();
                jButton1.setText("execute");
                jButton1.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        jButton1ActionPerformed(evt);
                    }
                });
            }
            {
                jButton3 = new JButton();
                jButton3.setText("validate");
                jButton3.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        jButton3ActionPerformed(evt);
                    }
                });
            }
            {
                jTextField4 = new JTextField();
            }
            {
                jLabel4 = new JLabel();
                jLabel4.setText("dest");
            }
            {
                jButton2 = new JButton();
                jButton2.setText("choice");
                jButton2.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        jButton2ActionPerformed(evt);
                    }
                });
            }
            thisLayout.setVerticalGroup(thisLayout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addGroup(
                            thisLayout
                                    .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField1, GroupLayout.Alignment.BASELINE,
                                            GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE,
                                            GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                            thisLayout
                                    .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField2, GroupLayout.Alignment.BASELINE,
                                            GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE,
                                            GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                            thisLayout
                                    .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField3, GroupLayout.Alignment.BASELINE,
                                            GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE,
                                            GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                            thisLayout
                                    .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField4, GroupLayout.Alignment.BASELINE,
                                            GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE,
                                            GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton2, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE,
                                            GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jTextArea1, GroupLayout.PREFERRED_SIZE, 164, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                            thisLayout
                                    .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton1, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE,
                                            GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addGroup(
                                            GroupLayout.Alignment.LEADING,
                                            thisLayout
                                                    .createSequentialGroup()
                                                    .addComponent(jButton3, GroupLayout.PREFERRED_SIZE, 24,
                                                            GroupLayout.PREFERRED_SIZE).addGap(0, 0, Short.MAX_VALUE)))
                    .addGap(6));
            thisLayout
                    .setHorizontalGroup(thisLayout
                            .createSequentialGroup()
                            .addContainerGap()
                            .addGroup(
                                    thisLayout
                                            .createParallelGroup()
                                            .addGroup(
                                                    thisLayout
                                                            .createSequentialGroup()
                                                            .addGroup(
                                                                    thisLayout
                                                                            .createParallelGroup()
                                                                            .addComponent(jLabel4,
                                                                                    GroupLayout.Alignment.LEADING,
                                                                                    GroupLayout.PREFERRED_SIZE, 85,
                                                                                    GroupLayout.PREFERRED_SIZE)
                                                                            .addComponent(jLabel3,
                                                                                    GroupLayout.Alignment.LEADING,
                                                                                    GroupLayout.PREFERRED_SIZE, 85,
                                                                                    GroupLayout.PREFERRED_SIZE)
                                                                            .addComponent(jLabel2,
                                                                                    GroupLayout.Alignment.LEADING,
                                                                                    GroupLayout.PREFERRED_SIZE, 85,
                                                                                    GroupLayout.PREFERRED_SIZE)
                                                                            .addComponent(jLabel1,
                                                                                    GroupLayout.Alignment.LEADING,
                                                                                    GroupLayout.PREFERRED_SIZE, 85,
                                                                                    GroupLayout.PREFERRED_SIZE))
                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                            .addGroup(
                                                                    thisLayout
                                                                            .createParallelGroup()
                                                                            .addGroup(
                                                                                    GroupLayout.Alignment.LEADING,
                                                                                    thisLayout
                                                                                            .createSequentialGroup()
                                                                                            .addComponent(
                                                                                                    jTextField4,
                                                                                                    GroupLayout.PREFERRED_SIZE,
                                                                                                    200,
                                                                                                    GroupLayout.PREFERRED_SIZE)
                                                                                            .addPreferredGap(
                                                                                                    LayoutStyle.ComponentPlacement.RELATED)
                                                                                            .addComponent(
                                                                                                    jButton2,
                                                                                                    GroupLayout.PREFERRED_SIZE,
                                                                                                    73,
                                                                                                    GroupLayout.PREFERRED_SIZE))
                                                                            .addGroup(
                                                                                    thisLayout
                                                                                            .createSequentialGroup()
                                                                                            .addComponent(
                                                                                                    jTextField3,
                                                                                                    GroupLayout.PREFERRED_SIZE,
                                                                                                    279,
                                                                                                    GroupLayout.PREFERRED_SIZE))
                                                                            .addGroup(
                                                                                    thisLayout
                                                                                            .createSequentialGroup()
                                                                                            .addComponent(
                                                                                                    jTextField2,
                                                                                                    GroupLayout.PREFERRED_SIZE,
                                                                                                    279,
                                                                                                    GroupLayout.PREFERRED_SIZE))
                                                                            .addGroup(
                                                                                    thisLayout
                                                                                            .createSequentialGroup()
                                                                                            .addComponent(
                                                                                                    jTextField1,
                                                                                                    GroupLayout.PREFERRED_SIZE,
                                                                                                    279,
                                                                                                    GroupLayout.PREFERRED_SIZE)))
                                                            .addGap(0, 0, Short.MAX_VALUE))
                                            .addGroup(
                                                    thisLayout
                                                            .createSequentialGroup()
                                                            .addComponent(jTextArea1, GroupLayout.PREFERRED_SIZE, 376,
                                                                    GroupLayout.PREFERRED_SIZE)
                                                            .addGap(0, 0, Short.MAX_VALUE))
                                            .addGroup(
                                                    GroupLayout.Alignment.LEADING,
                                                    thisLayout
                                                            .createSequentialGroup()
                                                            .addGap(78)
                                                            .addComponent(jButton3, GroupLayout.PREFERRED_SIZE, 82,
                                                                    GroupLayout.PREFERRED_SIZE)
                                                            .addGap(73)
                                                            .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 82,
                                                                    GroupLayout.PREFERRED_SIZE)
                                                            .addGap(0, 61, Short.MAX_VALUE))).addContainerGap(17, 17));
            this.setSize(421, 380);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JTextField jTextField1;// class
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JButton jButton1;
    private JButton jButton2;
    private JLabel jLabel4;
    private JButton jButton3;
    private JTextField jTextField4;// dest
    private JTextArea jTextArea1;// api
    private JTextField jTextField3;// fieldName
    private JTextField jTextField2;// Package
    private JLabel jLabel1;

    private JUnitMaker junitMaker;

    /**
     * execute
     * 
     * @param evt
     */
    private void jButton1ActionPerformed(ActionEvent evt) {
        try {
            long begin = System.currentTimeMillis();
            junitMaker.execute();//
            long between = System.currentTimeMillis() - begin;
            System.out.println("during : " + between);
            System.out.println("done...");
            Runtime.getRuntime().exec("cmd /c start " + junitMaker.getGenerateFile().getAbsolutePath());
            junitMaker = null;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog(e.getMessage(), "error");
        }
    }

    /**
     * choice
     * 
     * @param evt
     */
    private void jButton2ActionPerformed(ActionEvent evt) {
        File file = JFileChooserUtil.newInstance().selectDirectoryOnly().showOpenDialog().getApproveSelectedFile();
        jTextField4.setText(file.getAbsolutePath());
    }

    /**
     * validate
     * 
     * @param evt
     */
    private void jButton3ActionPerformed(ActionEvent evt) {
        try {
            Validate.notEmpty(jTextField2.getText(), "Package is empty");
            Validate.notEmpty(jTextField1.getText(), "Class is empty");
            String clz = jTextField1.getText();
            String api = jTextArea1.getText();
            String fieldName = StringUtils.defaultString(jTextField3.getText(), "test");
            String pkg = jTextField2.getText();
            String writeToDir = jTextField4.getText();
            Map<String, String> apiMap = ApiMatcher.newInstance().loadString(api).execute().getApiMap();
            junitMaker = JUnitMaker.newInstance(Class.forName(clz), pkg)//
                    .setFieldName(fieldName)//
                    .setPrefix("\\t")//
                    .apiMap(apiMap)//
                    .setIgnore(true)//
                    .allowPackage(false)//
                    .allowPrivate(false)//
                    .allowProtected(false)//
                    .allowOther(false)//
            ;
            if (StringUtils.isNotBlank(writeToDir)) {
                junitMaker.setWriteTo(new File(writeToDir));
            } else {
                jTextField4.setText(junitMaker.getWriteTo().getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog(e.getMessage(), "error");
        }
    }

}
