package gtu._work.etc;

import gtu.file.FileUtil;
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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

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
public class GoogleContactUI extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;
    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JTable googleTable;
    private JPanel jPanel2;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GoogleContactUI inst = new GoogleContactUI();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }

    public GoogleContactUI() {
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
                    BorderLayout jPanel1Layout = new BorderLayout();
                    jPanel1.setLayout(jPanel1Layout);
                    jTabbedPane1.addTab("jPanel1", null, jPanel1, null);
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel1.add(jScrollPane1, BorderLayout.CENTER);
                        {

                            DefaultTableModel model = JTableUtil.createModel(false, googleColumns);
                            model.addRow(new Object[googleColumns.length]);
                            googleTable = new JTable();
                            jScrollPane1.setViewportView(googleTable);
                            googleTable.setModel(model);

                            googleTable.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    JTableUtil.newInstance(googleTable).defaultToolTipText(evt);
                                    googleTableMouseClicked(evt);
                                }
                            });
                            JTableUtil.defaultSetting(googleTable);
                            JTableUtil.newInstance(googleTable).showColumnByHeaderValue(defaultShow_googleColumns);
                        }
                    }
                }
                {
                    jPanel2 = new JPanel();
                    BorderLayout jPanel2Layout = new BorderLayout();
                    jPanel2.setLayout(jPanel2Layout);
                    jTabbedPane1.addTab("jPanel2", null, jPanel2, null);
                }
            }
            pack();
            this.setSize(642, 405);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static Object[] googleColumns = new String[] { "Name", "Given Name", "Additional Name", "Family Name", "Yomi Name", "Given Name Yomi", "Additional Name Yomi", "Family Name Yomi", "Name Prefix",
            "Name Suffix", "Initials", "Nickname", "Short Name", "Maiden Name", "Birthday", "Gender", "Location", "Billing Information", "Directory Server", "Mileage", "Occupation", "Hobby",
            "Sensitivity", "Priority", "Subject", "Notes", "Group Membership", "E-mail 1 - Type", "E-mail 1 - Value" };

    static Object[] defaultShow_googleColumns = { //
    "Name", "Given Name", "Family Name", //
            "Birthday", "Gender", "Location", "Occupation", "Hobby", "Subject", "Notes", //
            "Group Membership", "E-mail 1 - Type", "E-mail 1 - Value" };

    static String encode = Charset.forName("BIG5").displayName();

    static Charset GOOGLE_CVS_ENCODE = Charset.forName("UTF-16LE");

    static void errorMessage(Object message) {
        JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog(message, "ERROR");
    }

    void googleTableMouseClicked(MouseEvent evt) {
        try {
            JPopupMenuUtil popupUtil = JPopupMenuUtil.newInstance(googleTable).applyEvent(evt);

            //CHANGE ENCODE
            popupUtil.addJMenuItem("set encode", new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        String code = StringUtils.defaultString(JOptionPaneUtil.newInstance().iconPlainMessage().showInputDialog("input file encode", "ENCODE"), "UTF8");
                        encode = Charset.forName(code).displayName();
                    } catch (Exception ex) {
                        JCommonUtil.handleException(ex);
                    }
                    System.err.println("encode : " + encode);
                }
            });

            //SIMPLE LOAD GOOGLE CSV FILE
            popupUtil.addJMenuItem("open Google CSV file", new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    File file = JFileChooserUtil.newInstance().selectFileOnly().addAcceptFile("csv", ".csv").showOpenDialog().getApproveSelectedFile();
                    if (file == null) {
                        errorMessage("file is not correct!");
                        return;
                    }
                    try {
                        if (file.getName().endsWith(".csv")) {
                            DefaultTableModel model = (DefaultTableModel) googleTable.getModel();
                            LineNumberReader reader = new LineNumberReader(new InputStreamReader(new FileInputStream(file), GOOGLE_CVS_ENCODE));
                            for (String line = null; (line = reader.readLine()) != null;) {
                                if (reader.getLineNumber() == 1) {
                                    continue;
                                }
                                model.addRow(line.split(","));
                            }
                            reader.close();
                            googleTable.setModel(model);
                            JTableUtil.newInstance(googleTable).hiddenAllEmptyColumn();
                        }
                    } catch (Exception ex) {
                        JCommonUtil.handleException(ex);
                    }
                }
            });

            //SAVE CSV FILE FOR GOOGLE
            popupUtil.addJMenuItem("save to Google CVS file", new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    File file = JFileChooserUtil.newInstance().selectFileOnly().addAcceptFile(".csv", ".csv").showSaveDialog().getApproveSelectedFile();
                    if (file == null) {
                        errorMessage("file is not correct!");
                        return;
                    }
                    file = FileUtil.getIndicateFileExtension(file, ".csv");
                    try {
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), GOOGLE_CVS_ENCODE));
                        StringBuilder sb = new StringBuilder();
                        for (Object title : googleColumns) {
                            sb.append(title + ",");
                        }
                        sb.deleteCharAt(sb.length() - 1);
                        System.out.println(sb);
                        writer.write(sb.toString());
                        writer.newLine();
                        DefaultTableModel model = (DefaultTableModel) googleTable.getModel();
                        for (int row = 0; row < model.getRowCount(); row++) {
                            sb = new StringBuilder();
                            for (int col = 0; col < model.getColumnCount(); col++) {
                                String colVal = StringUtils.defaultString((String) model.getValueAt(row, col), "");
                                if (colVal.equalsIgnoreCase("null")) {
                                    colVal = "";
                                }
                                sb.append(colVal + ",");
                            }
                            sb.deleteCharAt(sb.length() - 1);
                            System.out.println(sb);
                            writer.write(sb.toString());
                            writer.newLine();
                        }
                        writer.flush();
                        writer.close();
                    } catch (Exception ex) {
                        JCommonUtil.handleException(ex);
                    }
                }
            });

            //PASTE CLIPBOARD
            popupUtil.addJMenuItem("paste clipboard", new ActionListener() {
                public void actionPerformed(ActionEvent paramActionEvent) {
                    JTableUtil.newInstance(googleTable).pasteFromClipboard_multiRowData(true);
                }
            });

            popupUtil.addJMenuItem("paste clipboard to selected cell", new ActionListener() {
                public void actionPerformed(ActionEvent paramActionEvent) {
                    JTableUtil.newInstance(googleTable).pasteFromClipboard_singleValueToSelectedCell();
                }
            });

            JMenuItem addEmptyRowItem = JTableUtil.newInstance(googleTable).jMenuItem_addRow(false, "add row count?");
            addEmptyRowItem.setText("add row");
            JMenuItem removeColumnItem = JTableUtil.newInstance(googleTable).jMenuItem_removeColumn(null);
            removeColumnItem.setText("remove column");
            JMenuItem removeRowItem = JTableUtil.newInstance(googleTable).jMenuItem_removeRow(null);
            removeRowItem.setText("remove row");
            JMenuItem removeAllRowItem = JTableUtil.newInstance(googleTable).jMenuItem_removeAllRow("remove all row?");
            removeAllRowItem.setText("remove all row");
            JMenuItem clearSelectedCellItem = JTableUtil.newInstance(googleTable).jMenuItem_clearSelectedCell("are you sure clear selected area?");
            clearSelectedCellItem.setText("clear selected area");
            popupUtil.addJMenuItem(addEmptyRowItem, removeColumnItem, removeRowItem, removeAllRowItem, clearSelectedCellItem);
            popupUtil.show();
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }
}
