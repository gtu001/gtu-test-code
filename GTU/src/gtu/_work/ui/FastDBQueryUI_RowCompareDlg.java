package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import gtu._work.ui.FastDBQueryUI_CrudDlgUI.DataType;
import gtu.collection.MapUtil;
import gtu.db.JdbcDBUtil;
import gtu.db.jdbc.util.DBDateUtil;
import gtu.db.sqlMaker.DbSqlCreater.TableInfo;
import gtu.swing.util.JButtonGroupUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JTableUtil;
import gtu.swing.util.JTextAreaUtil;

public class FastDBQueryUI_RowCompareDlg extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTable importRowTable;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
    }

    public static void newInstance(String schemaTable, int selectRowIndex, Pair<List<String>, List<Object[]>> excelImportLst, FastDBQueryUI _parent) {
        try {
            FastDBQueryUI_RowCompareDlg dialog = new FastDBQueryUI_RowCompareDlg();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
            dialog.initTab1(schemaTable, selectRowIndex, excelImportLst, _parent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public FastDBQueryUI_RowCompareDlg() {
        setBounds(100, 100, 685, 463);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        {
            JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
            contentPanel.add(tabbedPane, BorderLayout.CENTER);
            {
                JPanel panel = new JPanel();
                tabbedPane.addTab("Import Row", null, panel, null);
                panel.setLayout(new BorderLayout(0, 0));
                {
                    JPanel panel_1 = new JPanel();
                    panel.add(panel_1, BorderLayout.NORTH);
                    {
                        JLabel lblNewLabel = new JLabel("查詢條件");
                        panel_1.add(lblNewLabel);
                    }
                    {
                        queryConditionArea = new JTextArea();
                        queryConditionArea.setRows(2);
                        queryConditionArea.setColumns(50);
                        JCommonUtil.createScrollComponent(queryConditionArea);
                        panel_1.add(JCommonUtil.createScrollComponent(queryConditionArea));
                    }
                    {
                        JButton syncQueryConditionBtn = new JButton("同步SQL");
                        syncQueryConditionBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                syncQueryConditionBtnAction(true);
                            }
                        });
                        panel_1.add(syncQueryConditionBtn);
                    }
                    {
                        JButton queryBtn = new JButton("執行SQL");
                        queryBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                queryBtnAction();
                            }
                        });
                        panel_1.add(queryBtn);
                    }
                }
                {
                    JPanel panel_1 = new JPanel();
                    panel.add(panel_1, BorderLayout.WEST);
                }
                {
                    JPanel panel_1 = new JPanel();
                    panel.add(panel_1, BorderLayout.SOUTH);
                    {
                        dbTypeComboBox = new JComboBox();
                        DefaultComboBoxModel model = new DefaultComboBoxModel();
                        for (DBDateUtil.DBDateFormat e : DBDateUtil.DBDateFormat.values()) {
                            model.addElement(e);
                        }
                        dbTypeComboBox.setModel(model);
                        panel_1.add(dbTypeComboBox);
                    }
                    {
                        onlyNotEqualChk = new JCheckBox("只更新不同");
                        onlyNotEqualChk.setSelected(true);
                        panel_1.add(onlyNotEqualChk);
                    }
                    {
                        importRowToCompareRowRadio = new JRadioButton("轉 Import Row");
                        importRowToCompareRowRadio.setSelected(true);
                        panel_1.add(importRowToCompareRowRadio);
                    }
                    {
                        otherRowToImportRowRadio = new JRadioButton("轉 Compare Row");
                        panel_1.add(otherRowToImportRowRadio);
                    }
                    {
                        createRadioButtonGroup = JButtonGroupUtil.createRadioButtonGroup(importRowToCompareRowRadio, otherRowToImportRowRadio);
                    }
                    {
                        JButton importRowToOtherRowBtn = new JButton("執行update SQL");
                        importRowToOtherRowBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                importRowToOtherRowBtnAction();
                            }
                        });
                        panel_1.add(importRowToOtherRowBtn);
                    }
                }
                {
                    JPanel panel_1 = new JPanel();
                    panel.add(panel_1, BorderLayout.EAST);
                }
                {
                    importRowTable = new JTable();
                    initImportRowTable();
                    panel.add(JCommonUtil.createScrollComponent(importRowTable), BorderLayout.CENTER);
                }
            }
            {
                JPanel panel = new JPanel();
                tabbedPane.addTab("Other Rows", null, panel, null);
                panel.setLayout(new BorderLayout(0, 0));
                {
                    JPanel panel_1 = new JPanel();
                    panel.add(panel_1, BorderLayout.NORTH);
                }
                {
                    JPanel panel_1 = new JPanel();
                    panel.add(panel_1, BorderLayout.WEST);
                }
                {
                    JPanel panel_1 = new JPanel();
                    panel.add(panel_1, BorderLayout.SOUTH);
                }
                {
                    JPanel panel_1 = new JPanel();
                    panel.add(panel_1, BorderLayout.EAST);
                }
                {
                    queryResultTable = new JTable();
                    JTableUtil.defaultSetting(queryResultTable);
                    queryResultTable.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            queryResultTableAction();
                        }
                    });
                    panel.add(JTableUtil.getScrollPane(queryResultTable), BorderLayout.CENTER);
                }
            }
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                });
            }
        }
        {
            JCommonUtil.setJFrameCenter(this);
        }
    }

    private DefaultTableModel initImportRowTable() {
        DefaultTableModel model = JTableUtil.createModel(false, "欄位", "Import Row", "Compare Row", "where Condition");
        importRowTable.setModel(model);
        JTableUtil.setColumnWidths_Percent(importRowTable, new float[] { 30, 30, 30, 5 });

        // column = "where condition"
        TableColumn sportColumn4 = importRowTable.getColumnModel().getColumn(3);
        sportColumn4.setCellEditor(new DefaultCellEditor(new JCheckBox()));

        JTableUtil.newInstance(importRowTable).setColumnColor_byCondition(0, new JTableUtil.TableColorDef() {
            public Color getTableBackgroundColour(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JTableUtil util = JTableUtil.newInstance(importRowTable);
                Object v1 = util.getRealValueAt(row, 1);
                Object v2 = util.getRealValueAt(row, 2);
                if (ObjectUtils.notEqual(v1, v2)) {
                    return Color.RED;
                }
                return null;
            }
        });

        JTableUtil.newInstance(importRowTable).setColumnColor_byCondition(1, new JTableUtil.TableColorDef() {
            public Color getTableBackgroundColour(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JTableUtil util = JTableUtil.newInstance(importRowTable);
                Object v1 = util.getRealValueAt(row, 1);
                Object v2 = util.getRealValueAt(row, 2);
                if (ObjectUtils.notEqual(v1, v2) && StringUtils.isNotBlank(String.valueOf(v1))) {
                    return Color.GREEN;
                }
                return null;
            }
        });

        JTableUtil.newInstance(importRowTable).setColumnColor_byCondition(2, new JTableUtil.TableColorDef() {
            public Color getTableBackgroundColour(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JTableUtil util = JTableUtil.newInstance(importRowTable);
                Object v1 = util.getRealValueAt(row, 1);
                Object v2 = util.getRealValueAt(row, 2);
                if (ObjectUtils.notEqual(v1, v2) && StringUtils.isNotBlank(String.valueOf(v2))) {
                    return Color.GREEN;
                }
                return null;
            }
        });
        return model;
    }

    private TableInfo tableInfo = new TableInfo();
    private JComboBox dbTypeComboBox;
    private AtomicReference<Map<String, ColumnConf>> rowMap = new AtomicReference<Map<String, ColumnConf>>();
    private JTextArea queryConditionArea;
    private FastDBQueryUI _parent;
    private Pair<List<String>, List<Object[]>> queryList;
    private JTable queryResultTable;
    private JRadioButton importRowToCompareRowRadio;
    private JRadioButton otherRowToImportRowRadio;
    private ButtonGroup createRadioButtonGroup;
    private JCheckBox onlyNotEqualChk;

    static class ColumnConf {
        String columnName;
        Object value1;
        Object value2;
        DataType dtype;
        boolean isPk;
        boolean isIgnore;
        boolean isModify = false;

        Object[] toArry() {
            Object[] arry = new Object[] { columnName, value1, value2, isPk };
            System.out.println(Arrays.toString(arry));
            return arry;
        }
    }

    private String getImportValue(String column, int selectRowIndex, Pair<List<String>, List<Object[]>> excelImportLst) {
        Object[] rowArry = excelImportLst.getRight().get(selectRowIndex);
        for (int ii = 0; ii < excelImportLst.getLeft().size(); ii++) {
            String col = excelImportLst.getLeft().get(ii);
            if (StringUtils.equalsIgnoreCase(column, col)) {
                String val = rowArry[ii] == null ? null : String.valueOf(rowArry[ii]);
                val = "null".equals(val) ? null : val;
                return val;
            }
        }
        throw new RuntimeException("資料建立錯誤  colum : " + column + " -> " + excelImportLst.getLeft() + " , " + Arrays.toString(rowArry));
    }

    private boolean isPK(String column, TableInfo tableInfo) {
        if (tableInfo.getPkColumns().contains(column)) {
            return true;
        } else {
            return tableInfo.getNoNullsCol().contains(column);
        }
    }

    private void initTab1(String schemaTable, int selectRowIndex, Pair<List<String>, List<Object[]>> excelImportLst, FastDBQueryUI _parent) throws SQLException {
        DefaultTableModel model = initImportRowTable();

        this._parent = _parent;

        JCommonUtil.isBlankErrorMsg(schemaTable, "輸入表名稱!");

        DBDateUtil.DBDateFormat dbDateDateFormat = (DBDateUtil.DBDateFormat) dbTypeComboBox.getSelectedItem();
        tableInfo.setDbDateDateFormat(dbDateDateFormat);

        tableInfo.execute(String.format(" select * from %s where 1!=1 ", schemaTable), _parent.getDataSource().getConnection());
        System.out.println(tableInfo.getColumns());

        Map<String, ColumnConf> rowMapForBackup = MapUtil.createIngoreCaseMap();
        rowMap.set(rowMapForBackup);

        for (String col : tableInfo.getColumns()) {
            Object value = getImportValue(col, selectRowIndex, excelImportLst);

            ColumnConf df = new ColumnConf();
            df.columnName = col;
            df.dtype = DataType.isTypeOf(value);
            df.isPk = isPK(col, tableInfo);
            df.value1 = value;
            df.value2 = null;
            rowMapForBackup.put(col, df);

            model.addRow(df.toArry());
        }
        System.out.println("-------------init size : " + rowMap.get().size());
    }

    private void syncQueryConditionBtnAction(boolean isExcelData) {
        Map<String, String> whereMap = new LinkedHashMap<String, String>();
        Set<String> pkColumns = new LinkedHashSet<String>();
        JTableUtil util = JTableUtil.newInstance(importRowTable);
        for (int row = 0; row < util.getModel().getRowCount(); row++) {
            String column = (String) util.getRealValueAt(row, 0);
            String value1 = (String) util.getRealValueAt(row, 1);
            String value2 = (String) util.getRealValueAt(row, 2);
            String realValue = isExcelData ? value1 : value2;
            boolean isPk = (Boolean) util.getRealValueAt(row, 3);
            whereMap.put(column, realValue);
            if (isPk) {
                pkColumns.add(column);
            }
        }
        tableInfo.setPkColumns(pkColumns);
        String whereSQL = tableInfo.createSelectSql(whereMap);
        queryConditionArea.setText(whereSQL);
    }

    private void queryBtnAction() {
        try {
            queryList = //
                    JdbcDBUtil.queryForList_customColumns(queryConditionArea.getText(), //
                            new String[0], _parent.getDataSource().getConnection(), true, 1000);
            DefaultTableModel model = JTableUtil.createModel(true, queryList.getLeft().toArray());
            queryResultTable.setModel(model);
            for (int ii = 0; ii < queryList.getRight().size(); ii++) {
                model.addRow(queryList.getRight().get(ii));
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void queryResultTableAction() {
        try {
            int selectRow = queryResultTable.getSelectedRow();
            if (selectRow == -1) {
                return;
            }

            JTableUtil util = JTableUtil.newInstance(importRowTable);
            for (int row = 0; row < util.getModel().getRowCount(); row++) {
                String column = (String) util.getRealValueAt(row, 0);
                int colPos = util.getRealColumnPos(2, importRowTable);
                String value = getImportValue(column, selectRow, queryList);
                importRowTable.setValueAt(value, row, colPos);
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void importRowToOtherRowBtnAction() {
        try {
            AbstractButton btn = JButtonGroupUtil.getSelectedButton(createRadioButtonGroup);

            Map<String, String> valueMap = new LinkedHashMap<String, String>();
            Map<String, String> pkMap = new LinkedHashMap<String, String>();
            Set<String> pkColumns = new LinkedHashSet<String>();
            JTableUtil util = JTableUtil.newInstance(importRowTable);

            for (int row = 0; row < util.getModel().getRowCount(); row++) {
                String column = (String) util.getRealValueAt(row, 0);
                String value1 = (String) util.getRealValueAt(row, 1);
                String value2 = (String) util.getRealValueAt(row, 2);
                boolean isPk = (Boolean) util.getRealValueAt(row, 3);

                String realValue = null;
                String pkValue = null;
                if (btn == importRowToCompareRowRadio) {
                    realValue = value1;
                    pkValue = value2;
                } else if (btn == otherRowToImportRowRadio) {
                    realValue = value2;
                    pkValue = value2;
                }

                if (onlyNotEqualChk.isSelected()) {
                    if (!StringUtils.equals(value1, value2)) {
                        valueMap.put(column, realValue);
                    }
                } else {
                    valueMap.put(column, realValue);
                }

                if (isPk) {
                    pkColumns.add(column);
                    pkMap.put(column, pkValue);
                }
            }

            tableInfo.setPkColumns(pkColumns);
            String updateSQL = tableInfo.createUpdateSql(valueMap, pkMap, false, null);
            updateSQL = JCommonUtil._jOptionPane_showInputDialog("執行SQL", updateSQL);

            if (StringUtils.isNotBlank(updateSQL)) {
                int updateResult = JdbcDBUtil.executeUpdate(updateSQL, new Object[0], _parent.getDataSource().getConnection());
                JCommonUtil._jOptionPane_showMessageDialog_info("update result : " + updateResult);
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }
}