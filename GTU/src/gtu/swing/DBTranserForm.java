/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DBTranserForm.java
 *
 * Created on 2011/11/29, 下午 02:24:53
 */
package gtu.swing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * 
 * @author 1101526
 */
public class DBTranserForm extends javax.swing.JDialog {

    /** Creates new form DBTranserForm */
    public DBTranserForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        init();

    }

    private void init() {
        List<String> tables = null;
        try {
            tables = new ArrayList<String>();
            tables.add("test1");
            tables.add("test2");
        } catch (Exception ex) {
            Logger.getLogger(DBTranserForm.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "資料庫讀取失敗", "DBTransfer Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        cbTable.setModel(new DefaultComboBoxModel(tables.toArray()));
    }

    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        cbTable = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        tfFilePath = new javax.swing.JTextField();
        btSelect = new javax.swing.JButton();
        btTrans = new javax.swing.JButton();
        btEnd = new javax.swing.JButton();
        tfPackage = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel1.setText("資料表名稱：");

        cbTable.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "item", "item1" }));

        jLabel2.setText("存放路徑：");

        tfFilePath.setEditable(false);

        btSelect.setText("選擇路徑");
        btSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSelectActionPerformed(evt);
            }
        });

        btTrans.setText("轉換");
        btTrans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTransActionPerformed(evt);
            }
        });

        btEnd.setText("離開");
        btEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEndActionPerformed(evt);
            }
        });

        jLabel3.setText("Package：");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(
                        layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(
                                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel1)
                                                .addGroup(
                                                        layout.createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                                .addComponent(jLabel3,
                                                                        javax.swing.GroupLayout.Alignment.LEADING,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        Short.MAX_VALUE)
                                                                .addComponent(jLabel2,
                                                                        javax.swing.GroupLayout.Alignment.LEADING,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(
                                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(tfPackage, javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(
                                                        layout.createSequentialGroup()
                                                                .addComponent(tfFilePath)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(btSelect))
                                                .addComponent(cbTable, javax.swing.GroupLayout.Alignment.LEADING, 0,
                                                        387, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38,
                                        Short.MAX_VALUE)
                                .addGroup(
                                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(btTrans).addComponent(btEnd)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(cbTable, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btTrans)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 19,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(
                                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(tfFilePath,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btSelect).addComponent(btEnd))
                                        .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(tfPackage, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel3))
                        .addContainerGap(22, Short.MAX_VALUE)));

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 604) / 2, (screenSize.height - 195) / 2, 604, 195);
    }

    private void btSelectActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btSelectActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int ans = fileChooser.showOpenDialog(this);
        if (ans != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File selFile = fileChooser.getSelectedFile();
        tfFilePath.setText(selFile.getPath());

    }

    private void btEndActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btEndActionPerformed
        System.exit(0);
    }

    private void btTransActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btTransActionPerformed
        String filePath = tfFilePath.getText();
        if (filePath == null || filePath.length() == 0) {
            JOptionPane.showMessageDialog(this, "請選擇存放目錄");
            return;
        }
        String tableName = (String) cbTable.getSelectedItem();
        String packageStr = tfPackage.getText();

        try {

        } catch (Exception ex) {
            Logger.getLogger(DBTranserForm.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "作業發生錯誤" + ex.getMessage(), "DBTransfer Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "作業完成", "DBTransfer", JOptionPane.INFORMATION_MESSAGE);
    }// GEN-LAST:event_btTransActionPerformed

    /**
     * @param args
     *            the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed"
        // desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase
         * /tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DBTranserForm.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DBTranserForm.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DBTranserForm.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DBTranserForm.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        }
        // </editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                DBTranserForm dialog = new DBTranserForm(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });

                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btEnd;
    private javax.swing.JButton btSelect;
    private javax.swing.JButton btTrans;
    private javax.swing.JComboBox cbTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField tfFilePath;
    private javax.swing.JTextField tfPackage;
    // End of variables declaration//GEN-END:variables
}
