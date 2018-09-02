package _temp.janna.ex0;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import _temp.janna.ex0.TradevanForJanna_MssqlGenerator.ColumnDef;
import _temp.janna.ex0.TradevanForJanna_MssqlGenerator.TableDef;
import gtu.file.FileUtil;
import gtu.spring.SimilarityUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JCommonUtil.HandleDocumentEvent;
import gtu.swing.util.JTableUtil;

public class TradevanForJanna_TableSearchUI extends JFrame {

    private JPanel contentPane;
    private JComboBox tableComboBox;
    private JTextField tableNameTextField;
    private JTextField columnTextField;
    private JTextField docSrcDirText;
    private JButton fileDirBtn;
    private JPanel panel_3;
    private JPanel panel_4;
    private JTextField tableNameLabelText;
    private JTable columnDefTable;

    Map<String, TableDef> allmap;
    private JTextField similarText;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TradevanForJanna_TableSearchUI frame = new TradevanForJanna_TableSearchUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public TradevanForJanna_TableSearchUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 577, 451);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("New tab", null, panel, null);
        panel.setLayout(new BorderLayout(0, 0));

        JPanel panel_2 = new JPanel();
        panel.add(panel_2, BorderLayout.NORTH);
        panel_2.setPreferredSize(new Dimension(0, 75));

        tableComboBox = new JComboBox();
        tableComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tableComboBoxChange();
            }
        });
        panel_2.add(tableComboBox);

        tableNameTextField = new JTextField();
        panel_2.add(tableNameTextField);
        tableNameTextField.setColumns(20);
        tableNameTextField.setToolTipText("輸入table名稱(中英皆可)");
        tableNameTextField.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
            @Override
            public void process(DocumentEvent event) {
                try {
                    initLoadAllTable();
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        }));

        columnTextField = new JTextField();
        columnTextField.setColumns(20);
        panel_2.add(columnTextField);
        columnTextField.setToolTipText("輸入欄位名稱(中英皆可)");

        similarText = new JTextField();
        similarText.setToolTipText("輸入欄位名稱(模糊比對)");
        similarText.setColumns(20);
        panel_2.add(similarText);
        similarText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
            @Override
            public void process(DocumentEvent event) {
                try {
                    similarCompareText();
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        }));

        columnTextField.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
            @Override
            public void process(DocumentEvent event) {
                try {
                    tableComboBoxChange();
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        }));

        panel_3 = new JPanel();
        panel.add(panel_3, BorderLayout.CENTER);
        panel_3.setLayout(new BorderLayout(0, 0));

        panel_4 = new JPanel();
        panel_3.add(panel_4, BorderLayout.NORTH);
        panel_4.setPreferredSize(new Dimension(0, 30));

        tableNameLabelText = new JTextField();
        tableNameLabelText.setEditable(false);
        panel_4.add(tableNameLabelText);
        tableNameLabelText.setColumns(50);

        columnDefTable = new JTable();
        // panel_3.add(columnDefTable, BorderLayout.CENTER);
        JCommonUtil.createScrollComponent(panel_3, columnDefTable);
        JTableUtil.defaultSetting_AutoResize(columnDefTable);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("New tab", null, panel_1, null);

        docSrcDirText = new JTextField();
        JCommonUtil.jTextFieldSetFilePathMouseEvent(docSrcDirText, true);
        panel_1.add(docSrcDirText);
        docSrcDirText.setColumns(40);

        // for Test
        docSrcDirText.setText("D:/workstuff/workspace/gtu-test-code/GTU/src/_temp/janna/J122-SDD-001-E22軟體設計規格書-資料庫設計-資產出租管理-已排版_0723.docx");

        fileDirBtn = new JButton("搜尋");
        fileDirBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                try {
                    File file = JCommonUtil.filePathCheck(docSrcDirText.getText(), "檔案路徑", false);
                    List<File> fileList = new ArrayList<File>();
                    if (file.isFile()) {
                        if (file.getName().endsWith(".docx")) {
                            fileList.add(file);
                        }
                    } else {
                        FileUtil.searchFilefind(file, ".*\\.docx", fileList);
                    }

                    if (fileList.isEmpty()) {
                        JCommonUtil._jOptionPane_showMessageDialog_error("查無檔案");
                        return;
                    }

                    allmap = new HashMap<String, TableDef>();
                    for (File f : fileList) {
                        TradevanForJanna_MssqlGenerator gen = new TradevanForJanna_MssqlGenerator();
                        gen.execute(f);
                        allmap.putAll(gen.tabMap);
                    }

                    if (allmap == null || allmap.isEmpty()) {
                        Validate.isTrue(false, "開檔失敗");
                    } else {
                        initLoadAllTable();
                    }
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });

        panel_1.add(fileDirBtn);
        docSrcDirText.setToolTipText("輸入文件路徑(目錄或檔案皆可)");
        docSrcDirText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
            @Override
            public void process(DocumentEvent event) {
                try {
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        }));

        JCommonUtil.defaultToolTipDelay();
    }

    private void initLoadAllTable() {
        String findtext = StringUtils.defaultString(tableNameTextField.getText());
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement(new TableDef2());
        List<String> keyset = new ArrayList<String>(allmap.keySet());
        Collections.sort(keyset);
        for (String table : keyset) {
            TableDef def = allmap.get(table);
            System.out.println(table + " ---- " + def);
            TableDef2 d2 = new TableDef2();
            d2.tableDef = def;
            if (StringUtils.isNotBlank(findtext) && d2.toString().toLowerCase().contains(findtext.toLowerCase())) {
                model.addElement(d2);
            } else if (StringUtils.isBlank(findtext)) {
                model.addElement(d2);
            }
        }
        tableComboBox.setModel(model);
    }

    private void tableComboBoxChange() {
        try {
            DefaultTableModel model = JTableUtil.createModel(true, "button01", "表", "欄位", "欄位中", "型態", "主建", "可空", "remark");
            final TableDef2 def = (TableDef2) tableComboBox.getSelectedItem();
            String findtxt = StringUtils.defaultString(columnTextField.getText());
            if (def.tableDef != null) {
                String tableName = def.tableDef.tabName;
                String tableChs = def.tableDef.tabNameChs;
                for (final ColumnDef c : def.tableDef.colList) {
                    JButton btn = new JButton();
                    btn.setText("複製");
                    btn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent paramActionEvent) {
                            String val = def.tableDef.tabName + "." + c.english + " " + c.chinese;
                            JCommonUtil._jOptionPane_showInputDialog(val, val);
                        }
                    });
                    
                    Object[] arry = new Object[] { btn, tableName + tableChs, c.english, c.chinese, c.type, c.key, c.nn, c.remark };

                    if (StringUtils.isNotBlank(findtxt)) {
                        if (c.english.toLowerCase().contains(findtxt.toLowerCase()) || c.chinese.toLowerCase().contains(findtxt.toLowerCase())) {
                            model.addRow(arry);
                        }
                    } else {
                        model.addRow(arry);
                    }
                }
            } else if (StringUtils.isNotBlank(findtxt)) {
                ComboBoxModel model2 = tableComboBox.getModel();
                for (int ii = 0; ii < model2.getSize(); ii++) {
                    final TableDef2 deff = (TableDef2) model2.getElementAt(ii);
                    if (deff.tableDef == null) {
                        continue;
                    }
                    final String tableName = deff.tableDef.tabName;
                    final String tableChs = deff.tableDef.tabNameChs;
                    for (final ColumnDef c : deff.tableDef.colList) {
                        JButton btn = new JButton();
                        btn.setText("複製");
                        btn.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent paramActionEvent) {
                                String val = deff.tableDef.tabName + "." + c.english + " " + c.chinese;
                                JCommonUtil._jOptionPane_showInputDialog(val, val);
                            }
                        });

                        Object[] arry = new Object[] { btn, tableName + tableChs, c.english, c.chinese, c.type, c.key, c.nn, c.remark };

                        if (StringUtils.isNotBlank(findtxt)) {
                            if (c.english.toLowerCase().contains(findtxt.toLowerCase()) || c.chinese.toLowerCase().contains(findtxt.toLowerCase())) {
                                model.addRow(arry);
                            }
                        } else {
                            model.addRow(arry);
                        }
                    }
                }
            }
            columnDefTable.setModel(model);
            JTableUtil.newInstance(columnDefTable).columnIsButton("button01");
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void similarCompareText() {
        try {
            DefaultTableModel model = JTableUtil.createModel(true, "button01", "表", "欄位", "欄位中", "型態", "主建", "可空", "remark");
            String text = StringUtils.defaultString(similarText.getText());
            if (StringUtils.isBlank(text)) {
                columnDefTable.setModel(model);
                return;
            }

            List<TableDef_forCol> list = new ArrayList<TableDef_forCol>();
            ComboBoxModel model2 = tableComboBox.getModel();
            for (int ii = 0; ii < model2.getSize(); ii++) {
                TableDef2 deff = (TableDef2) model2.getElementAt(ii);
                if (deff.tableDef == null) {
                    continue;
                }
                for (ColumnDef c : deff.tableDef.colList) {
                    TableDef_forCol c1 = new TableDef_forCol();
                    c1.tableName = deff.tableDef.tabName;
                    c1.tableNameChs = deff.tableDef.tabNameChs;
                    c1.column = c;
                    c1.simScore = SimilarityUtil.sim(text, c.chinese);
                    list.add(c1);
                }
            }

            Collections.sort(list, new Comparator<TableDef_forCol>() {
                @Override
                public int compare(TableDef_forCol o1, TableDef_forCol o2) {
                    return o1.simScore.compareTo(o2.simScore);
                }
            });
            Collections.reverse(list);
            for (final TableDef_forCol c : list) {
                if (c.simScore > 0) {
                    JButton btn = new JButton();
                    btn.setText("複製");
                    btn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent paramActionEvent) {
                            String val = c.tableName + "." + c.column.english + " " + c.column.chinese;
                            JCommonUtil._jOptionPane_showInputDialog(val, val);
                        }
                    });

                    Object[] arry = new Object[] { btn, c.tableName + c.tableNameChs, c.column.english, c.column.chinese, c.column.type, c.column.key, c.column.nn, c.column.remark };
                    model.addRow(arry);
                }
            }
            columnDefTable.setModel(model);
            JTableUtil.newInstance(columnDefTable).columnIsButton("button01");
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private class TableDef_forCol {
        ColumnDef column;
        String tableName;
        String tableNameChs;
        Double simScore;
    }

    private class TableDef2 {
        TableDef tableDef;

        public String toString() {
            if (tableDef != null) {
                return tableDef.tabName + " " + tableDef.tabNameChs;
            } else {
                return "請選擇";
            }
        }
    }
}
