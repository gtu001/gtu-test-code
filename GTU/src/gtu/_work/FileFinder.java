package gtu._work;

import gtu.file.FileUtil;
import gtu.swing.util.JFileChooserUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

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
public class FileFinder extends javax.swing.JFrame {
    private static final long serialVersionUID = 3589632118229792571L;

    private JTextField jTextField1;
    private JButton jButton1;
    private JLabel status;
    private JScrollPane jScrollPane1;
    private ButtonGroup buttonGroup2;
    private JRadioButton findRadio;
    private JRadioButton matchRadio;
    private JList jList1;
    private JTextField jTextField2;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                FileFinder inst = new FileFinder();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }

    public FileFinder() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            GroupLayout thisLayout = new GroupLayout((JComponent) getContentPane());
            getContentPane().setLayout(thisLayout);
            this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            this.setTitle("\u6a94\u6848\u641c\u5c0b");
            {
                jTextField1 = new JTextField();
            }
            {
                jButton1 = new JButton();
                jButton1.setText("Open");
                jButton1.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        jButton1ActionPerformed(evt);
                    }
                });
            }
            {
                jTextField2 = new JTextField();
                // 文字修改時觸發事件
                jTextField2.getDocument().addDocumentListener(new DocumentListener() {
                    public void insertUpdate(DocumentEvent paramDocumentEvent) {
                        jTextField2DocumentDocumentListener(paramDocumentEvent);
                    }

                    public void removeUpdate(DocumentEvent paramDocumentEvent) {
                        jTextField2DocumentDocumentListener(paramDocumentEvent);
                    }

                    public void changedUpdate(DocumentEvent paramDocumentEvent) {
                        jTextField2DocumentDocumentListener(paramDocumentEvent);
                    }
                });
            }
            {
                jScrollPane1 = new JScrollPane();
                {
                    ListModel jList1Model = new DefaultComboBoxModel(new String[] {});
                    jList1 = new JList();
                    jScrollPane1.setViewportView(jList1);
                    jList1.addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent evt) {
                            jList1MouseClicked(evt);
                        }
                    });
                    jList1.setModel(jList1Model);
                }
            }
            {
                status = new JLabel();
                status.setBackground(new java.awt.Color(192, 192, 192));
                status.setForeground(new java.awt.Color(255, 255, 255));
            }
            {
                findRadio = new JRadioButton();
                findRadio.setText("find");
                findRadio.setSelected(true);
            }
            {
                matchRadio = new JRadioButton();
                matchRadio.setText("match");
            }
            thisLayout.setVerticalGroup(thisLayout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addGroup(
                            thisLayout
                                    .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton1, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE,
                                            GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField1, GroupLayout.Alignment.BASELINE,
                                            GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                            GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                            thisLayout
                                    .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField2, GroupLayout.Alignment.BASELINE,
                                            GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                            GroupLayout.PREFERRED_SIZE)
                                    .addComponent(findRadio, GroupLayout.Alignment.BASELINE,
                                            GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                            GroupLayout.PREFERRED_SIZE)
                                    .addComponent(matchRadio, GroupLayout.Alignment.BASELINE,
                                            GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                            GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 319, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(status, 0, 21, Short.MAX_VALUE));
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
                                                                            .addGroup(
                                                                                    GroupLayout.Alignment.LEADING,
                                                                                    thisLayout
                                                                                            .createSequentialGroup()
                                                                                            .addComponent(
                                                                                                    jTextField2,
                                                                                                    GroupLayout.PREFERRED_SIZE,
                                                                                                    334,
                                                                                                    GroupLayout.PREFERRED_SIZE)
                                                                                            .addPreferredGap(
                                                                                                    LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                            .addComponent(
                                                                                                    matchRadio,
                                                                                                    GroupLayout.PREFERRED_SIZE,
                                                                                                    63,
                                                                                                    GroupLayout.PREFERRED_SIZE))
                                                                            .addGroup(
                                                                                    GroupLayout.Alignment.LEADING,
                                                                                    thisLayout
                                                                                            .createSequentialGroup()
                                                                                            .addComponent(
                                                                                                    jTextField1,
                                                                                                    GroupLayout.PREFERRED_SIZE,
                                                                                                    397,
                                                                                                    GroupLayout.PREFERRED_SIZE)
                                                                                            .addGap(12)))
                                                            .addGroup(
                                                                    thisLayout
                                                                            .createParallelGroup()
                                                                            .addGroup(
                                                                                    GroupLayout.Alignment.LEADING,
                                                                                    thisLayout
                                                                                            .createSequentialGroup()
                                                                                            .addComponent(
                                                                                                    findRadio,
                                                                                                    GroupLayout.PREFERRED_SIZE,
                                                                                                    GroupLayout.PREFERRED_SIZE,
                                                                                                    GroupLayout.PREFERRED_SIZE)
                                                                                            .addGap(0, 6,
                                                                                                    Short.MAX_VALUE))
                                                                            .addGroup(
                                                                                    thisLayout
                                                                                            .createSequentialGroup()
                                                                                            .addComponent(
                                                                                                    jButton1,
                                                                                                    GroupLayout.PREFERRED_SIZE,
                                                                                                    GroupLayout.PREFERRED_SIZE,
                                                                                                    GroupLayout.PREFERRED_SIZE)
                                                                                            .addGap(0, 0,
                                                                                                    Short.MAX_VALUE))))
                                            .addGroup(
                                                    GroupLayout.Alignment.LEADING,
                                                    thisLayout
                                                            .createSequentialGroup()
                                                            .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE,
                                                                    451, GroupLayout.PREFERRED_SIZE)
                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 0,
                                                                    Short.MAX_VALUE))
                                            .addGroup(
                                                    GroupLayout.Alignment.LEADING,
                                                    thisLayout
                                                            .createSequentialGroup()
                                                            .addComponent(status, GroupLayout.PREFERRED_SIZE, 451,
                                                                    GroupLayout.PREFERRED_SIZE)
                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 0,
                                                                    Short.MAX_VALUE))).addContainerGap());

            // 群組radio
            buttonGroup2 = new ButtonGroup();
            buttonGroup2.add(findRadio);
            buttonGroup2.add(matchRadio);
            matchRadio.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent evt) {
                    matchRadioItemStateChanged(evt);
                }
            });

            this.setSize(494, 462);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 搜尋 TODO
     * 
     * @param paramDocumentEvent
     */
    private void jTextField2DocumentDocumentListener(DocumentEvent doc) {
        searchFile();
    }

    private void searchFile() {
        try {
            File file = new File(jTextField1.getText());
            if (!(file.exists() && file.isDirectory())) {
                status.setText("檔案目錄不存在");
                return;
            }

            // 取得輸入字串
            // String inputStr = doc.getDocument().getText(0,
            // doc.getDocument().getLength());
            String inputStr = jTextField2.getText();

            List<File> listFile = new ArrayList<File>();

            // 取得所選radio
            AbstractButton abstractButton = getSelectedRadio();
            if (abstractButton == findRadio) {
                FileUtil.searchFilefind(file, inputStr, listFile);
            }
            if (abstractButton == matchRadio) {
                FileUtil.searchFileMatchs(file, inputStr, listFile);
            }

            // 重組下拉清單
            DefaultListModel listModel = new DefaultListModel();
            jList1.setModel(listModel);
            for (File f : listFile) {
                listModel.addElement(new SelectFile(f));
            }
            status.setText("符合檔案 : " + listFile.size());

        } catch (Exception ex) {
            status.setText("錯誤 : " + ex.getMessage());
        }
    }

    private class SelectFile {
        private String fileName;
        private File file;

        public SelectFile(File file) {
            super();
            this.fileName = file.getName();
            this.file = file;
        }

        public String toString() {
            return fileName;
        }
    }

    private AbstractButton getSelectedRadio() {
        for (Enumeration<AbstractButton> enu = this.buttonGroup2.getElements(); enu.hasMoreElements();) {
            AbstractButton btn = enu.nextElement();
            if (btn.isSelected()) {
                return btn;
            }
        }
        return null;
    }

    /**
     * 開檔
     * 
     * @param evt
     */
    private void jButton1ActionPerformed(ActionEvent evt) {
        File file = JFileChooserUtil.newInstance().selectDirectoryOnly().showOpenDialog().getApproveSelectedFile();
        jTextField1.setText(file.getAbsolutePath());
    }

    private void jList1MouseClicked(MouseEvent evt) {
        // 滑鼠點兩下的事件
        if (evt.getClickCount() == 2) {
            SelectFile selectFile = (SelectFile) jList1.getSelectedValue();
            try {
                String ultraEdit = "C:\\Program Files (x86)\\IDM Computer Solutions\\UltraEdit\\Uedit32.exe ";
                if (selectFile.file.getAbsolutePath().endsWith(".doc")) {
                    ultraEdit = "C:\\Program Files (x86)\\Microsoft Office\\Office12\\WINWORD.EXE ";
                }
                if (selectFile.file.getAbsolutePath().endsWith(".xlsx")) {
                    ultraEdit = "C:\\Program Files (x86)\\Microsoft Office\\Office12\\EXCEL.EXE ";
                }
                Runtime.getRuntime().exec(ultraEdit + selectFile.file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * radio改變
     * 
     * @param evt
     */
    private void matchRadioItemStateChanged(ItemEvent evt) {
        searchFile();
    }
}
