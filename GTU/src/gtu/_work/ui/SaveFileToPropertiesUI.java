package gtu._work.ui;

import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFileChooserUtil;
import gtu.swing.util.JOptionPaneUtil;
import gtu.swing.util.JPopupMenuUtil;
import gtu.swing.util.JTableUtil;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Properties;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.commons.lang.StringUtils;

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
public class SaveFileToPropertiesUI extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;
    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JButton openFile;
    private JButton appendTextAreaToProps;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JButton clearProps;
    private JPanel jPanel4;
    private JComboBox openUnknowFilecharSet;
    private JButton savePropsToFile;
    private JTable propsTable;
    private JPanel jPanel3;
    private JButton addPropsFile;
    private JPanel jPanel2;
    private JTextArea textArea;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                SaveFileToPropertiesUI inst = new SaveFileToPropertiesUI();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }

    public SaveFileToPropertiesUI() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            BorderLayout thisLayout = new BorderLayout();
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            getContentPane().setLayout(thisLayout);
            this.setTitle("save file to properties");
            {
                jTabbedPane1 = new JTabbedPane();
                getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
                {
                    jPanel1 = new JPanel();
                    BorderLayout jPanel1Layout = new BorderLayout();
                    jPanel1.setLayout(jPanel1Layout);
                    jTabbedPane1.addTab("load", null, jPanel1, null);
                    {
                        jScrollPane2 = new JScrollPane();
                        jPanel1.add(jScrollPane2, BorderLayout.CENTER);
                        {
                            textArea = new JTextArea();
                            jScrollPane2.setViewportView(textArea);
                        }
                    }
                    {
                        jPanel2 = new JPanel();
                        jPanel1.add(jPanel2, BorderLayout.NORTH);
                        jPanel2.setPreferredSize(new java.awt.Dimension(400, 40));
                        {
                            addPropsFile = new JButton();
                            jPanel2.add(addPropsFile);
                            addPropsFile.setText("load properties from file");
                            addPropsFile.setPreferredSize(new java.awt.Dimension(234, 30));
                            addPropsFile.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    File file = JFileChooserUtil.newInstance().selectFileOnly().showOpenDialog().getApproveSelectedFile();
                                    if (file == null) {
                                        JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("file is not correct!", "ERROR");
                                        return;
                                    }
                                    try {
                                        props.load(new InputStreamReader(new FileInputStream(file), (String) openUnknowFilecharSet.getSelectedItem()));
                                        reloadPropertiesTable();
                                    } catch (Exception e) {
                                        JCommonUtil.handleException(e);
                                    }
                                }
                            });
                        }
                        {
                            openFile = new JButton();
                            jPanel2.add(openFile);
                            openFile.setText("open unknow file");
                            openFile.setPreferredSize(new java.awt.Dimension(204, 30));
                            openFile.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    File file = JFileChooserUtil.newInstance().selectFileOnly().showOpenDialog().getApproveSelectedFile();
                                    if (file == null) {
                                        JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("file is not correct!", "ERROR");
                                        return;
                                    }
                                    try {
                                        String encode = (String) openUnknowFilecharSet.getSelectedItem();
                                        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encode));
                                        StringBuilder sb = new StringBuilder();
                                        for (String line = null; (line = reader.readLine()) != null;) {
                                            sb.append(line + "\n");
                                        }
                                        reader.close();
                                        textArea.setText(textArea.getText() + "\n" + sb);
                                    } catch (IOException e) {
                                        JCommonUtil.handleException(e);
                                    }
                                }
                            });
                        }
                        {
                            ComboBoxModel openUnknowFilecharSetModel = new DefaultComboBoxModel(new String[] { "BIG5", "UTF8" });
                            openUnknowFilecharSet = new JComboBox();
                            jPanel2.add(openUnknowFilecharSet);
                            openUnknowFilecharSet.setModel(openUnknowFilecharSetModel);
                            openUnknowFilecharSet.setPreferredSize(new java.awt.Dimension(73, 24));
                        }
                        {
                            appendTextAreaToProps = new JButton();
                            jPanel2.add(appendTextAreaToProps);
                            appendTextAreaToProps.setText("append textarea to properties");
                            appendTextAreaToProps.setPreferredSize(new java.awt.Dimension(227, 30));
                            appendTextAreaToProps.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    if (StringUtils.isBlank(textArea.getText())) {
                                        JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("textArea is empty", "ERROR");
                                        return;
                                    }
                                    try {
                                        BufferedReader reader = new BufferedReader(new StringReader(textArea.getText()));
                                        int pos = -1;
                                        String key = null;
                                        String value = null;
                                        for (String line = null; (line = reader.readLine()) != null;) {
                                            if ((pos = line.lastIndexOf("=")) != -1) {
                                                key = line.substring(0, pos);
                                                value = line.substring(pos + 1);
                                                props.put(key, value);
                                            }
                                        }
                                        reader.close();
                                        JOptionPaneUtil.newInstance().iconInformationMessage().showMessageDialog("append success!", "SUCCESS");
                                        reloadPropertiesTable();
                                    } catch (IOException e) {
                                        JCommonUtil.handleException(e);
                                    }
                                }
                            });
                        }
                    }
                }
                {
                    jPanel3 = new JPanel();
                    BorderLayout jPanel3Layout = new BorderLayout();
                    jPanel3.setLayout(jPanel3Layout);
                    jTabbedPane1.addTab("props edit", null, jPanel3, null);
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel3.add(jScrollPane1, BorderLayout.CENTER);
                        jScrollPane1.setPreferredSize(new java.awt.Dimension(629, 361));
                        {
                            TableModel propsTableModel = new DefaultTableModel();
                            propsTable = new JTable();
                            jScrollPane1.setViewportView(propsTable);
                            propsTable.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    if (propsTable.getRowCount() == 0) {
                                        return;
                                    }
                                    int rowPos = JTableUtil.newInstance(propsTable).getSelectedRow();
                                    Object key = propsTable.getValueAt(rowPos, 0);
                                    Object value = propsTable.getValueAt(rowPos, 1);

                                    JMenuItem insertRowItem = JTableUtil.newInstance(propsTable).jMenuItem_addRow(false, null);
                                    insertRowItem.setText("inert row...");

                                    String rowInfo = "delete row : [" + key + "] = [" + value + "]";
                                    JMenuItem delRowItem = JTableUtil.newInstance(propsTable).jMenuItem_removeRow("are you sure remove row : \n" + rowInfo);
                                    delRowItem.setText(rowInfo);

                                    JPopupMenuUtil.newInstance(propsTable).applyEvent(evt).addJMenuItem(insertRowItem, delRowItem).show();
                                }
                            });
                            propsTable.setModel(propsTableModel);
                            JTableUtil.defaultSetting(propsTable);
                        }
                    }
                    {
                        jPanel4 = new JPanel();
                        jPanel3.add(jPanel4, BorderLayout.SOUTH);
                        jPanel4.setPreferredSize(new java.awt.Dimension(629, 45));
                        {
                            clearProps = new JButton();
                            jPanel4.add(clearProps);
                            clearProps.setText("clear properties");
                            clearProps.setPreferredSize(new java.awt.Dimension(182, 36));
                            clearProps.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    props.clear();
                                    reloadPropertiesTable();
                                }
                            });
                        }
                        {
                            savePropsToFile = new JButton();
                            jPanel4.add(savePropsToFile);
                            savePropsToFile.setText("save properties to file");
                            savePropsToFile.setPreferredSize(new java.awt.Dimension(182, 36));
                            savePropsToFile.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    File file = JFileChooserUtil.newInstance().selectFileOnly().showSaveDialog().getApproveSelectedFile();
                                    if (file == null) {
                                        JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("file is not correct!", "ERROR");
                                        return;
                                    }
                                    try {
                                        props.clear();
                                        DefaultTableModel model = (DefaultTableModel) propsTable.getModel();
                                        Object key = null;
                                        Object value = null;
                                        for (int ii = 0; ii < model.getRowCount(); ii++) {
                                            key = model.getValueAt(ii, 0);
                                            value = model.getValueAt(ii, 1);
                                            props.put(key, value);
                                        }
                                        props.store(new FileOutputStream(file), SaveFileToPropertiesUI.class.getSimpleName());
                                        JOptionPaneUtil.newInstance().iconInformationMessage().showMessageDialog("save completed!\n" + file, "SUCCESS");
                                    } catch (IOException e) {
                                        JCommonUtil.handleException(e);
                                    }
                                }
                            });
                        }
                    }
                }
            }
            pack();
            this.setSize(798, 505);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void reloadPropertiesTable() {
        DefaultTableModel model = JTableUtil.createModel(false, "key", "value");
        for (Object key : props.keySet()) {
            model.addRow(new Object[] { key, props.get(key) });
        }
        propsTable.setModel(model);
    }

    static Properties props = new Properties();
}
