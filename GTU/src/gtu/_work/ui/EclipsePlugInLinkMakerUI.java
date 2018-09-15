package gtu._work.ui;

import gtu._work.eclipse.plugin.EclipsePlugInLinkMaker;
import gtu.swing.util.JFileChooserUtil;
import gtu.swing.util.JOptionPaneUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

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
/**
 * @author Troy
 * 
 *         2012/1/6
 * 
 */
public class EclipsePlugInLinkMakerUI extends javax.swing.JFrame {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private JButton btnChoicePlugInDir;
    private JButton btnExecute;
    private JTextField textEclipseDir;
    private JButton btnChoiceEclipseDir;
    private JTextField textPlugInDir;

    private EclipsePlugInLinkMaker service;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                EclipsePlugInLinkMakerUI inst = new EclipsePlugInLinkMakerUI();
                inst.service = new EclipsePlugInLinkMaker();
                inst.setLocationRelativeTo(null);
                 gtu.swing.util.JFrameUtil.setVisible(true,inst);
            }
        });
    }

    public EclipsePlugInLinkMakerUI() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            GroupLayout thisLayout = new GroupLayout((JComponent) getContentPane());
            getContentPane().setLayout(thisLayout);
            this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            btnChoicePlugInDir = new JButton();
            btnChoicePlugInDir.setText("\u9078\u64c7PlugIn\u76ee\u9304");
            btnChoicePlugInDir.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    btnChoicePlugInDirActionPerformed(evt);
                }
            });
            textPlugInDir = new JTextField();
            btnExecute = new JButton();
            btnExecute.setText("\u7522\u751fLink\u6587\u4ef6");
            btnExecute.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    btnExecuteActionPerformed(evt);
                }
            });
            btnChoiceEclipseDir = new JButton();
            btnChoiceEclipseDir.setText("\u9078\u64c7Eclipse\u76ee\u9304");
            btnChoiceEclipseDir.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    btnChoiceEclipseDirActionPerformed(evt);
                }
            });
            textEclipseDir = new JTextField();
            thisLayout.setVerticalGroup(thisLayout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addGroup(
                            thisLayout
                                    .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnChoicePlugInDir, GroupLayout.Alignment.BASELINE,
                                            GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(textPlugInDir, GroupLayout.Alignment.BASELINE,
                                            GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                    .addGap(20)
                    .addGroup(
                            thisLayout
                                    .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnChoiceEclipseDir, GroupLayout.Alignment.BASELINE,
                                            GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(textEclipseDir, GroupLayout.Alignment.BASELINE,
                                            GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(btnExecute, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap());
            thisLayout.setHorizontalGroup(thisLayout
                    .createSequentialGroup()
                    .addContainerGap(26, 26)
                    .addGroup(
                            thisLayout
                                    .createParallelGroup()
                                    .addComponent(textEclipseDir, GroupLayout.Alignment.LEADING,
                                            GroupLayout.PREFERRED_SIZE, 332, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(textPlugInDir, GroupLayout.Alignment.LEADING,
                                            GroupLayout.PREFERRED_SIZE, 332, GroupLayout.PREFERRED_SIZE)
                                    .addGroup(
                                            GroupLayout.Alignment.LEADING,
                                            thisLayout
                                                    .createSequentialGroup()
                                                    .addGap(167)
                                                    .addComponent(btnExecute, GroupLayout.PREFERRED_SIZE, 137,
                                                            GroupLayout.PREFERRED_SIZE).addGap(28)))
                    .addGap(26)
                    .addGroup(
                            thisLayout
                                    .createParallelGroup()
                                    .addGroup(
                                            thisLayout
                                                    .createSequentialGroup()
                                                    .addGap(0, 0, Short.MAX_VALUE)
                                                    .addComponent(btnChoiceEclipseDir, GroupLayout.PREFERRED_SIZE, 137,
                                                            GroupLayout.PREFERRED_SIZE))
                                    .addComponent(btnChoicePlugInDir, GroupLayout.Alignment.LEADING, 0, 137,
                                            Short.MAX_VALUE)).addContainerGap());
            this.setSize(548, 179);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btnChoiceEclipseDirActionPerformed(ActionEvent evt) {
        File file = JFileChooserUtil.newInstance().selectDirectoryOnly().showOpenDialog().getApproveSelectedFile();
        if (file != null) {
            textEclipseDir.setText(file.getAbsolutePath());
        }
    }

    private void btnChoicePlugInDirActionPerformed(ActionEvent evt) {
        File file = JFileChooserUtil.newInstance().selectDirectoryOnly().showOpenDialog().getApproveSelectedFile();
        if (file != null) {
            textPlugInDir.setText(file.getAbsolutePath());
        }
    }

    private void btnExecuteActionPerformed(ActionEvent evt) {
        try {
            service.setEclipseDir(new File(textEclipseDir.getText()));
            service.setPlugInDir(new File(textPlugInDir.getText()));
            service.execute();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "錯誤", JOptionPane.ERROR_MESSAGE);
        }
        JOptionPaneUtil.newInstance().iconInformationMessage().showMessageDialog(service.getLinkLog(), "匯出完成");
        System.out.println("done...");
    }
}
