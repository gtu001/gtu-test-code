package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import gtu._work.ui.FastDBQueryUI_ColumnSearchFilter.FindTextHandler;
import gtu._work.ui.JMenuBarUtil.JMenuAppender;
import gtu.binary.Base64JdkUtil;
import gtu.binary.StringUtil4FullChar;
import gtu.collection.MapUtil;
import gtu.db.JdbcDBUtil;
import gtu.db.jdbc.util.DBDateUtil;
import gtu.db.jdbc.util.DBDateUtil.DBDateFormat;
import gtu.db.sqlMaker.DbSqlCreater.FieldInfo4DbSqlCreater;
import gtu.db.sqlMaker.DbSqlCreater.TableInfo;
import gtu.file.FileUtil;
import gtu.string.StringUtilForDb;
import gtu.swing.util.JButtonGroupUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JCommonUtil.HandleDocumentEvent;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JMouseEventUtil;
import gtu.swing.util.JPopupMenuUtil;
import gtu.swing.util.JTableUtil;
import gtu.swing.util.JTextUndoUtil;
import gtu.swing.util.KeyEventExecuteHandler;

public class FastDBQueryUI_CrudDlgUI extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTable rowTable;
    private JButton okButton;
    private ButtonGroup btnGroup;
    private JTextField tableAndSchemaText;
    private JTextField searchText;
    private HandleDocumentEvent searchTextHanler;
    private JRadioButton rdbtnInsert;
    private JRadioButton rdbtnUpdate;
    private JRadioButton rdbtnDelete;
    private JComboBox dbTypeComboBox;
    private AtomicReference<Map<String, ColumnConf>> rowMap = new AtomicReference<Map<String, ColumnConf>>();
    private JRadioButton rdbtnOthers;
    private FastDBQueryUI _parent;
    private JCheckBox applyAllQueryResultCheckBox;
    private JFrameRGBColorPanel jFrameRGBColorPanel;
    private JRadioButton rdbtnSelect;
    private List<String> columnsLst;
    private DBTypeFormatHandler dBTypeFormatHandler;
    private KeyEventExecuteHandler keyEventExecuteHandler;

    private static final String KEY_DBDateFormat = FastDBQueryUI_CrudDlgUI.class.getSimpleName() + "_" + DBDateFormat.class.getSimpleName();

    private enum ColumnOrderDef {
        columnName("欄位", 25), //
        value("值", 25), //
        currentLength("現在長度", 5), //
        maxLength("最大長度", 5), //
        dtype("資料類型", 25), //
        isPk("過濾條件", 13), //
        isIgnore("省略", 12), //
        ;

        final int width;
        final String label;

        ColumnOrderDef(String label, int width) {
            this.label = label;
            this.width = width;
        }

        private static DefaultTableModel createDefaultTableModel() {
            List<String> dlst = new ArrayList<String>();
            for (ColumnOrderDef d : ColumnOrderDef.values()) {
                dlst.add(d.label);
            }
            return JTableUtil.createModel(false, dlst.toArray(new String[0]));
        }

        private static void resetColumnWidth(JTable rowTable) {
            List<Float> dlst = new ArrayList<Float>();
            for (ColumnOrderDef d : ColumnOrderDef.values()) {
                dlst.add((float) d.width);
            }
            JTableUtil.setColumnWidths_Percent(rowTable, ArrayUtils.toPrimitive(dlst.toArray(new Float[0])));
        }
    }

    static class ColumnConf {
        String columnName;
        Object value;
        Object orignValue;// 用來判斷是否改過
        DataType dtype;
        boolean isPk;
        boolean isIgnore;
        boolean isModify = false;
        Integer maxLength;

        Object[] toArry() {
            int currentLength = 0;
            if (value != null) {
                currentLength = StringUtils.defaultString(String.valueOf(value)).length();
            }
            String maxLengthStr = "";
            if (maxLength != null) {
                maxLengthStr = String.valueOf(maxLength);
            }
            Object[] arry = new Object[] { columnName, value, currentLength, maxLengthStr, dtype, isPk, isIgnore, };
            System.out.println(Arrays.toString(arry));
            return arry;
        }
    }

    enum DataType {
        varchar(String.class) {
        }, //
        date(java.sql.Date.class) {
            protected void applyDataChange(Object value, JTable table, int row, FastDBQueryUI_CrudDlgUI self) {
                System.out.println("-------" + value + " -> " + value.getClass());
                String val = (String) value;
                java.sql.Date newVal = java.sql.Date.valueOf(val);
                table.setValueAt(newVal, row, ColumnOrderDef.value.ordinal());
            }
        }, //
        timestamp(java.sql.Timestamp.class) {
            protected void applyDataChange(Object value, JTable table, int row, FastDBQueryUI_CrudDlgUI self) {
                System.out.println("-------" + value + " -> " + value.getClass());
                String val = (String) value;
                java.sql.Timestamp newVal = java.sql.Timestamp.valueOf(val);
                table.setValueAt(newVal, row, ColumnOrderDef.value.ordinal());
            }
        }, //
        number(Number.class) {
        }, //
        NULL(void.class) {
        }, //
        UNKNOW(void.class) {
        },//
        ;

        final Class<?>[] clz;

        DataType(Class<?>... clz) {
            this.clz = clz;
        }

        static DataType isTypeOf(Object value) {
            if (value == null) {
                return NULL;
            }
            for (DataType e : DataType.values()) {
                for (Class<?> c : e.clz) {
                    if (c == value.getClass()) {
                        return e;
                    }
                }
            }
            for (DataType e : DataType.values()) {
                for (Class<?> c : e.clz) {
                    if (c.isAssignableFrom(value.getClass())) {
                        return e;
                    }
                }
            }
            return UNKNOW;
        }
    }

    private class ValueFixHandler {
        DecimalFormat formatterN = new DecimalFormat("#.#############");

        private Object getValueFix(Object value) {
            if (value == null) {
                return value;
            }
            if (value.getClass() == BigDecimal.class) {
                return formatterN.format(value);
            }
            return value;
        }
    }

    public static FastDBQueryUI_CrudDlgUI newInstance(final List<Map<String, Object>> rowMapLst, final String tableNSchema, final Triple<List<String>, List<Class<?>>, List<Object[]>> queryList,
            ActionListener onCloseListener, final FastDBQueryUI _parent) {
        try {
            final FastDBQueryUI_CrudDlgUI dialog = new FastDBQueryUI_CrudDlgUI(_parent);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);

            JTable rowTable = dialog.rowTable;
            DefaultTableModel model = dialog.initRowTable();

            final JTableUtil tableUtil = JTableUtil.newInstance(rowTable);
            ValueFixHandler valueFixHandler = dialog.new ValueFixHandler();

            dialog.columnsLst = new ArrayList<String>();
            Map<String, Object> rowMap = rowMapLst.get(0);

            Map<String, ColumnConf> rowMapForBackup = MapUtil.createIngoreCaseMap();
            dialog.rowMap.set(rowMapForBackup);
            for (String col : rowMap.keySet()) {
                Object value = rowMap.get(col);
                value = valueFixHandler.getValueFix(value);

                ColumnConf df = new ColumnConf();
                df.columnName = col;
                df.dtype = DataType.isTypeOf(value);
                df.isPk = false;
                df.value = value;
                df.orignValue = value;
                rowMapForBackup.put(col, df);

                model.addRow(df.toArry());
                dialog.columnsLst.add(col);
            }

            JTableUtil.newInstance(rowTable).setRowHeightByFontSize();
            System.out.println("-------------init size : " + dialog.rowMap.get().size());

            dialog.searchText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
                @Override
                public void process(DocumentEvent event) {
                    try {
                        dialog.updateJTableToRowMap();
                        dialog.searchTextFilter();
                        ColumnOrderDef.resetColumnWidth(dialog.rowTable);
                    } catch (Exception ex) {
                        JCommonUtil.handleException(ex);
                    }
                }
            }));

            dialog.okButton.addActionListener(new ActionListener() {

                class Process {
                    TableInfo tableInfo;
                    List<Map<String, String>> maybeMultiRowLst;
                    Set<String> ignoreColumns;

                    private void initTableInfo() throws SQLException {
                        tableInfo = new TableInfo();

                        DBDateUtil.DBDateFormat dbDateDateFormat = (DBDateUtil.DBDateFormat) dialog.dbTypeComboBox.getSelectedItem();
                        tableInfo.setDbDateDateFormat(dbDateDateFormat);

                        String tableAndSchema = dialog.tableAndSchemaText.getText();
                        AbstractButton btn = JButtonGroupUtil.getSelectedButton(dialog.btnGroup);
                        if (btn != dialog.rdbtnOthers) {
                            JCommonUtil.isBlankErrorMsg(tableAndSchema, "輸入表名稱!");
                        }

                        if (StringUtils.isNotBlank(tableAndSchema)) {
                            tableInfo.execute(String.format(" select * from %s where 1!=1 ", tableAndSchema), _parent.getDataSource().getConnection());
                            if (StringUtils.isBlank(tableInfo.getTableAndSchema())) {
                                tableInfo.setTableName(tableAndSchema);
                            }
                        }
                    }

                    Process() throws SQLException {
                        initTableInfo();

                        Set<String> pkColumns = new HashSet<String>();
                        Set<String> noNullsCol = new HashSet<String>();
                        Set<String> numberCol = new HashSet<String>();
                        Set<String> dateCol = new HashSet<String>();
                        Set<String> timestampCol = new HashSet<String>();

                        Set<String> ignoreSet = new HashSet<String>();
                        Set<String> modifyColSet = new HashSet<String>();

                        List<Map<String, String>> maybeMultiRowLst = new ArrayList<Map<String, String>>();

                        // 第一筆的處理
                        Map<String, String> map = new LinkedHashMap<String, String>();
                        for (String columnName : dialog.rowMap.get().keySet()) {
                            columnName = StringUtils.trimToEmpty(columnName);
                            ColumnConf df = dialog.rowMap.get().get(columnName);
                            String value = df.value != null ? String.valueOf(df.value) : null;
                            DataType dtype = df.dtype;
                            boolean isPk = df.isPk;
                            if (df.isIgnore) {
                                ignoreSet.add(columnName);
                            }
                            map.put(columnName, value);
                            if (isPk) {
                                pkColumns.add(columnName);
                                noNullsCol.add(columnName);
                            }
                            if (dtype == DataType.date) {
                                dateCol.add(columnName);
                            } else if (dtype == DataType.timestamp) {
                                timestampCol.add(columnName);
                            } else if (dtype == DataType.number) {
                                numberCol.add(columnName);
                            }
                            if (df.isModify) {
                                modifyColSet.add(columnName);
                            }
                        }
                        maybeMultiRowLst.add(map);

                        // 其他筆的處理
                        if (rowMapLst.size() > 1) {
                            for (int ii = 1; ii < rowMapLst.size(); ii++) {
                                Map<String, Object> fromMap = rowMapLst.get(ii);
                                Map<String, String> toMap = new LinkedHashMap<String, String>();

                                for (String columnName : dialog.rowMap.get().keySet()) {
                                    columnName = StringUtils.trimToEmpty(columnName);
                                    ColumnConf df = dialog.rowMap.get().get(columnName);
                                    String valueOrign = fromMap.get(columnName) != null ? String.valueOf(fromMap.get(columnName)) : null;
                                    String valueClone = df.value != null ? String.valueOf(df.value) : null;

                                    // 只覆蓋有改的
                                    if (modifyColSet.contains(columnName)) {
                                        toMap.put(columnName, valueClone);
                                    } else {
                                        toMap.put(columnName, valueOrign);
                                    }
                                }

                                maybeMultiRowLst.add(toMap);
                            }
                        }

                        // ------------------------------------------------
                        if (pkColumns.isEmpty()) {
                            Validate.isTrue(false, "勾選where pk!!");
                        }
                        tableInfo.getNoNullsCol().addAll(noNullsCol);
                        tableInfo.getNumberCol().addAll(numberCol);
                        tableInfo.getPkColumns().addAll(pkColumns);
                        tableInfo.getDateCol().addAll(dateCol);
                        tableInfo.getTimestampCol().addAll(timestampCol);
                        // ------------------------------------------------

                        this.maybeMultiRowLst = maybeMultiRowLst;
                        ignoreColumns = ignoreSet;
                    }

                    List<Map<String, String>> getAllRecoreds() {
                        List<String> cols = queryList.getLeft();
                        List<Object[]> qlst = queryList.getRight();
                        List<Map<String, String>> rtnLst = new ArrayList<Map<String, String>>();
                        for (Object[] row : qlst) {
                            Map<String, String> map = new LinkedHashMap<String, String>();
                            for (int ii = 0; ii < cols.size(); ii++) {
                                String col = cols.get(ii);
                                String value = row[ii] != null ? String.valueOf(row[ii]) : null;
                                map.put(col, value);
                            }
                            rtnLst.add(map);
                        }
                        return rtnLst;
                    }
                }

                BufferedWriter writer = null;

                private void createWriter(File outputFile) throws UnsupportedEncodingException, FileNotFoundException {
                    if (writer == null) {
                        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF8"));
                    }
                }

                private void closeWriter() {
                    if (writer != null) {
                        try {
                            writer.flush();
                        } catch (Exception e) {
                        }
                        try {
                            writer.close();
                        } catch (Exception e) {
                        }
                    }
                    writer = null;
                }

                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        Process process = new Process();
                        if (!dialog.applyAllQueryResultCheckBox.isSelected()) {

                            List<String> updateSqlLst = new ArrayList<String>();
                            for (Map<String, String> singleRecordMap : process.maybeMultiRowLst) {
                                // 套用單筆資料
                                AbstractButton btn = JButtonGroupUtil.getSelectedButton(dialog.btnGroup);
                                String sql = "";
                                if (btn == dialog.rdbtnInsert) {
                                    sql = process.tableInfo.createInsertSql(singleRecordMap, process.ignoreColumns);
                                } else if (btn == dialog.rdbtnDelete) {
                                    sql = process.tableInfo.createDeleteSql(singleRecordMap);
                                } else if (btn == dialog.rdbtnUpdate) {
                                    sql = process.tableInfo.createUpdateSql(singleRecordMap, singleRecordMap, false, process.ignoreColumns);
                                } else if (btn == dialog.rdbtnSelect) {
                                    sql = process.tableInfo.createSelectSql(singleRecordMap);
                                } else if (btn == dialog.rdbtnOthers) {
                                    rdbtnOthersAction(process.tableInfo, singleRecordMap);
                                } else {
                                    Validate.isTrue(false, "請選sql類型");
                                }
                                if (StringUtils.isNotBlank(sql)) {
                                    updateSqlLst.add(sql);
                                }
                            }

                            if (updateSqlLst.isEmpty()) {
                                return;
                            }
                            final FastDBQueryUI_UpdateSqlArea updateDlg = FastDBQueryUI_UpdateSqlArea.newInstance("確定執行以下SQL:", updateSqlLst, _parent.getjFrameRGBColorPanel().isStop(),
                                    new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            System.out.println("## FastDBQueryUI_UpdateSqlArea close !!");
                                        }
                                    });
                            updateDlg.setConfirmDo(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    Exception ex1 = null;
                                    List<String> sqlLst = updateDlg.getSqlText();
                                    int successCount = 0;
                                    int failCount = 0;
                                    List<Integer> resultLst = new ArrayList<Integer>();
                                    for (String sql : sqlLst) {
                                        int updateResult = 0;
                                        try {
                                            updateResult = JdbcDBUtil.executeUpdate(sql, new Object[0], _parent.getDataSource().getConnection());
                                            _parent.updateLogger.debug(String.format("SQL : %d \t : %s", updateResult, sql));
                                        } catch (Exception ex) {
                                            ex1 = ex;
                                        }
                                        if (updateResult > 0) {
                                            successCount++;
                                        } else {
                                            failCount++;
                                        }
                                        resultLst.add(updateResult);
                                    }
                                    JCommonUtil._jOptionPane_showMessageDialog_info(String.format("成功:%d,失敗:%d,共:%d\n", successCount, failCount, (successCount + failCount)) + resultLst);

                                    if (ex1 != null) {
                                        ex1.printStackTrace();
                                        // JCommonUtil.handleException(ex1);
                                        _parent.handleExceptionForExecuteSQL(ex1);
                                    } else {
                                        // 更新欄位歷史紀錄
                                        dialog.updateColumnHistory();
                                    }
                                }
                            });
                        } else {
                            // 套用所有資料
                            AbstractButton btn = JButtonGroupUtil.getSelectedButton(dialog.btnGroup);
                            List<Map<String, String>> qlst = process.getAllRecoreds();

                            String filename = JCommonUtil._jOptionPane_showInputDialog("請輸入匯出檔名",
                                    FastDBQueryUI.class.getSimpleName() + "_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd_HHmmss") + ".sql");
                            if (StringUtils.isBlank(filename)) {
                                JCommonUtil._jOptionPane_showMessageDialog_error("檔名有誤!");
                                return;
                            }

                            File outputFile = new File(FileUtil.DESKTOP_DIR, filename);
                            if (outputFile.exists()) {
                                JCommonUtil._jOptionPane_showMessageDialog_error("檔案已存在!");
                                return;
                            }

                            try {
                                if (btn == dialog.rdbtnInsert) {
                                    createWriter(outputFile);
                                    for (Map<String, String> map : qlst) {
                                        String sql = process.tableInfo.createInsertSql(map, process.ignoreColumns);
                                        writer.write(sql + ";");
                                        writer.newLine();
                                    }
                                } else if (btn == dialog.rdbtnDelete) {
                                    createWriter(outputFile);
                                    for (Map<String, String> map : qlst) {
                                        String sql = process.tableInfo.createDeleteSql(map);
                                        writer.write(sql + ";");
                                        writer.newLine();
                                    }
                                } else if (btn == dialog.rdbtnUpdate) {
                                    createWriter(outputFile);
                                    for (Map<String, String> map : qlst) {
                                        String sql = process.tableInfo.createUpdateSql(map, map, false, process.ignoreColumns);
                                        writer.write(sql + ";");
                                        writer.newLine();
                                    }
                                } else if (btn == dialog.rdbtnSelect) {
                                    createWriter(outputFile);
                                    for (Map<String, String> map : qlst) {
                                        String sql = process.tableInfo.createSelectSql(map);
                                        writer.write(sql + ";");
                                        writer.newLine();
                                    }
                                } else if (btn == dialog.rdbtnOthers) {
                                    JCommonUtil._jOptionPane_showMessageDialog_error("不支援!");
                                    return;
                                } else {
                                    Validate.isTrue(false, "請選sql類型");
                                }
                            } catch (Exception ex) {
                                throw ex;
                            } finally {
                                closeWriter();
                            }
                            JCommonUtil._jOptionPane_showMessageDialog_info("匯出完成 : " + outputFile);
                        }
                    } catch (Exception e1) {
                        JCommonUtil.handleException(e1);
                    }
                }

                private void rdbtnOthersAction(TableInfo tableInfo, Map<String, String> dataMap) {
                    OthersDBColumnProcess selecting = (OthersDBColumnProcess) JCommonUtil._JOptionPane_showInputDialog("選擇腳本", "選擇腳本", OthersDBColumnProcess.values(), null);
                    String resultStr = "";
                    if (selecting != null) {
                        resultStr = selecting.apply(tableInfo, dataMap, dialog);
                    }
                    if (StringUtils.isNotBlank(resultStr)) {
                        JCommonUtil._jOptionPane_showInputDialog(selecting, resultStr);
                    } else {
                        JCommonUtil._jOptionPane_showMessageDialog_error("選擇失敗 : " + selecting);
                    }
                }
            });

            if (StringUtils.isNotBlank(tableNSchema)) {
                dialog.tableAndSchemaText.setText(StringUtils.trimToEmpty(tableNSchema));
                dialog.tableAndSchemaText_focusLost_action(dialog.tableAndSchemaText);
            }

            dialog.addWindowListener(new WindowAdapter() {
                public void windowClosed(WindowEvent e) {
                    if (onCloseListener != null) {
                        onCloseListener.actionPerformed(new ActionEvent(dialog, -1, "close"));
                    }
                }

                public void windowClosing(WindowEvent e) {
                }
            });

            return dialog;
        } catch (Exception e) {
            throw new RuntimeException("FastDBQueryUI_CrudDlgUI ERR : " + e.getMessage(), e);
        }
    }

    private DefaultTableModel initRowTable() {
        final JTableUtil tableUtil = JTableUtil.newInstance(rowTable);
        JTableUtil.defaultSetting(rowTable);

        DefaultTableModel model = ColumnOrderDef.createDefaultTableModel();
        rowTable.setModel(model);

        ColumnOrderDef.resetColumnWidth(rowTable);

        JTableUtil.setColumnAlign(rowTable, ColumnOrderDef.currentLength.ordinal(), JLabel.RIGHT);
        JTableUtil.setColumnAlign(rowTable, ColumnOrderDef.maxLength.ordinal(), JLabel.RIGHT);

        JTableUtil.newInstance(rowTable).setColumnColor_byCondition(ColumnOrderDef.currentLength.ordinal(), new JTableUtil.TableColorDef() {
            public Pair<Color, Color> getTableColour(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JTableUtil util = JTableUtil.newInstance(rowTable);
                Integer v1 = (Integer) util.getRealValueAt(row, ColumnOrderDef.currentLength.ordinal());
                String v2 = (String) util.getRealValueAt(row, ColumnOrderDef.maxLength.ordinal());
                if (StringUtils.isNotBlank(v2)) {
                    if (v1 > Integer.parseInt(v2)) {
                        return Pair.of(Color.RED, null);
                    }
                }
                return null;
            }
        });

        JTableUtil.newInstance(rowTable).setColumnColor_byCondition(ColumnOrderDef.maxLength.ordinal(), new JTableUtil.TableColorDef() {
            public Pair<Color, Color> getTableColour(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JTableUtil util = JTableUtil.newInstance(rowTable);
                String v2 = (String) util.getRealValueAt(row, ColumnOrderDef.maxLength.ordinal());
                if (StringUtils.isNotBlank(v2)) {
                    return Pair.of(Color.YELLOW, null);
                }
                return null;
            }
        });

        // column = "Data Type"
        TableColumn sportColumn = rowTable.getColumnModel().getColumn(ColumnOrderDef.dtype.ordinal());
        JComboBox comboBox = new JComboBox();
        for (DataType e : DataType.values()) {
            comboBox.addItem(e);
        }
        sportColumn.setCellEditor(new DefaultCellEditor(comboBox));

        // column = "where condition"
        TableColumn sportColumn4 = rowTable.getColumnModel().getColumn(ColumnOrderDef.isPk.ordinal());
        sportColumn4.setCellEditor(new DefaultCellEditor(new JCheckBox()));

        // onblur 修改
        rowTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

        model.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int col = e.getColumn();

                /*
                 * Perform actions only if the first column is the source of the
                 * change.
                 */
                /*
                 * Remember that if you change values here, add it directly to
                 * the data[][] array and not by calling setValueAt(...) or you
                 * will cause an infinite loop ...
                 */
                // etc... all your data processing...
                String valueStr = "ERR";
                Object currentOrignValue = null;
                try {
                    currentOrignValue = JTableUtil.newInstance(rowTable).getValueAt(false, row, col);
                    valueStr = currentOrignValue != null ? (currentOrignValue + " -> " + currentOrignValue.getClass()) : "null";
                } catch (Exception ex) {
                    ex.getMessage();
                }
                System.out.println(String.format("## table change -> row[%d], col[%d] -----> %s", row, col, valueStr));

                // 刷新table紀錄！！！ onBlur !!!!!
                updateJTableToRowMap();
                updateCurrentValueLength(currentOrignValue, row, col);
            }

            private void updateCurrentValueLength(Object currentOrignValue, int row, int col) {
                if (col == ColumnOrderDef.value.ordinal()) {
                    String valueStr = currentOrignValue == null ? "" : String.valueOf(currentOrignValue);
                    int currentLength = StringUtil4FullChar.length(valueStr);
                    System.out.println(" currentLength : " + valueStr + " -> " + currentLength);
                    JTableUtil.newInstance(rowTable).setValueAt(false, currentLength, row, ColumnOrderDef.currentLength.ordinal());
                }
            }
        });

        // column = "value"
        TableColumn valueColumn = rowTable.getColumnModel().getColumn(ColumnOrderDef.value.ordinal());
        JTextField valueText = new JTextField();
        JTextUndoUtil.applyUndoProcess1(valueText);
        valueColumn.setCellEditor(new DefaultCellEditor(valueText) {
            public boolean stopCellEditing() {
                Object s = getCellEditorValue();
                System.out.println("!!---" + s + " -> " + s.getClass());
                getComponent().setForeground(Color.red);
                // Toolkit.getDefaultToolkit().beep();
                // 刷新table紀錄
                // updateJTableToRowMap();
                return super.stopCellEditing();// true 表示修改成功
            }
        });
        return tableUtil.getModel();
    }

    // 自動設定pk
    private void tableAndSchemaText_focusLost_action(JTextField tableAndSchemaText) {
        try {
            String tableAndSchema = StringUtils.trimToEmpty(tableAndSchemaText.getText());
            if (StringUtils.isNotBlank(tableAndSchema)) {
                boolean confirm = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("是否要重設 " + tableAndSchema + " 的 PK", "重設?");
                if (!confirm) {
                    return;
                }

                TableInfo tableInfo = new TableInfo();
                JCommonUtil.isBlankErrorMsg(tableAndSchema, "輸入表名稱!");

                DBDateUtil.DBDateFormat dbDateDateFormat = (DBDateUtil.DBDateFormat) dbTypeComboBox.getSelectedItem();
                tableInfo.setDbDateDateFormat(dbDateDateFormat);
                tableInfo.execute(String.format(" select * from %s where 1!=1 ", tableAndSchema), _parent.getDataSource().getConnection());
                if (StringUtils.isBlank(tableInfo.getTableName())) {
                    tableInfo.setTableName(tableAndSchema);
                }

                List<String> success = new ArrayList<String>();
                List<String> failed = new ArrayList<String>();

                for (String columnName : rowMap.get().keySet()) {
                    ColumnConf df = rowMap.get().get(columnName);
                    df.isPk = false;
                }

                for (String rowColumnName : rowMap.get().keySet()) {
                    boolean findOk = false;
                    for (String columnName : tableInfo.getColumnInfo().keySet()) {
                        if (StringUtils.equals(rowColumnName, columnName)) {
                            FieldInfo4DbSqlCreater info = tableInfo.getColumnInfo().get(columnName);
                            rowMap.get().get(rowColumnName).maxLength = info.getColumnDisplaySize();
                            findOk = true;
                        }
                    }
                    if (!findOk) {
                        rowMap.get().get(rowColumnName).maxLength = null;
                    }
                }

                for (String columnName : tableInfo.getNoNullsCol()) {
                    if (rowMap.get().containsKey(columnName)) {
                        rowMap.get().get(columnName).isPk = true;
                        success.add(columnName);
                    } else {
                        failed.add(columnName);
                    }
                }

                // 刷新table
                searchTextFilter();

                JCommonUtil._jOptionPane_showMessageDialog_info("設定完成 \n 已設定:" + success + "\n 找不到 :" + failed);
            }
        } catch (Exception e) {
            JCommonUtil.handleException(e);
        }
    }

    private void updateJTableToRowMap() {
        JTableUtil tableUtil = JTableUtil.newInstance(rowTable);
        for (int ii = 0; ii < tableUtil.getModel().getRowCount(); ii++) {
            String columnName = (String) tableUtil.getRealValueAt(ii, ColumnOrderDef.columnName.ordinal());
            columnName = StringUtils.trimToEmpty(columnName);
            if (columnName == null || StringUtils.isBlank(columnName)) {
                columnName = "";
            }

            String value = String.valueOf(tableUtil.getRealValueAt(ii, ColumnOrderDef.value.ordinal()));

            DataType dtype = DataType.varchar;
            try {
                Object dtypeVal = tableUtil.getRealValueAt(ii, ColumnOrderDef.dtype.ordinal());
                if (dtypeVal instanceof String) {
                    dtype = DataType.valueOf((String) dtypeVal);
                } else {
                    dtype = (DataType) dtypeVal;
                }
            } catch (Exception ex) {
                System.out.println("dtype---ERR--" + ex.getMessage());
            }

            boolean isPk = false;
            try {
                isPk = (Boolean) tableUtil.getRealValueAt(ii, ColumnOrderDef.isPk.ordinal());
            } catch (Exception ex) {
                System.out.println("isPk---ERR--" + ex.getMessage());
            }

            boolean isIgnore = false;
            try {
                isIgnore = (Boolean) tableUtil.getRealValueAt(ii, ColumnOrderDef.isIgnore.ordinal());
            } catch (Exception ex) {
                System.out.println("isIgnore---ERR--" + ex.getMessage());
            }

            ColumnConf df = new ColumnConf();
            if (this.rowMap.get().containsKey(columnName)) {
                df = this.rowMap.get().get(columnName);
            }

            // 判斷欄位是否修改過
            if (("null".equals(String.valueOf(value)) && null == df.orignValue) || //
                    StringUtils.equals(value, String.valueOf(df.orignValue))) {
                df.isModify = false;
            } else {
                df.isModify = true;
            }

            df.columnName = columnName;
            df.value = value;
            df.isPk = isPk;
            df.isIgnore = isIgnore;
            df.dtype = dtype;

            this.rowMap.get().put(columnName, df);
        }
    }

    private void searchTextFilter() {
        DefaultTableModel model = initRowTable();
        rowTable.setModel(model);
        JTableUtil tableUtil = JTableUtil.newInstance(rowTable);

        FindTextHandler finder = new FindTextHandler(searchText.getText(), "^");
        boolean allMatch = finder.isAllMatch();

        B: for (String columnName : columnsLst) {
            ColumnConf df = rowMap.get().get(columnName);
            if (allMatch) {
                model.addRow(df.toArry());
                continue;
            }

            for (String text : finder.getArry()) {
                if (StringUtils.isBlank(text) || //
                        columnName.toLowerCase().contains(text) || //
                        String.valueOf(df.value).toLowerCase().contains(text)) {
                    model.addRow(df.toArry());
                    continue B;
                }
            }
        }
        JTableUtil.newInstance(rowTable).setRowHeightByFontSize();
        System.out.println("-------------searchTextFilter size = " + rowMap.get().size());
    }

    private enum OthersDBColumnProcess {
        MAP_PUT_STR("map.put(str)") {
            @Override
            String apply(TableInfo tableInfo, Map<String, String> dataMap, FastDBQueryUI_CrudDlgUI self) {
                StringBuilder sb = new StringBuilder();
                Set<String> columns = getTableColumns(tableInfo, self);
                for (String col : columns) {
                    String param = StringUtilForDb.dbFieldToJava_smartCheck(col);
                    sb.append("map.put(\"" + col + "\", " + getQuoteStringVal(col, dataMap) + ");\n");//
                }
                return sb.toString();
            }
        }, //
        MAP_PUT_STR_C("map.put(str)完整") {
            @Override
            String apply(TableInfo tableInfo, Map<String, String> dataMap, FastDBQueryUI_CrudDlgUI self) {
                StringBuilder sb = new StringBuilder();
                Set<String> columns = getTableColumns(tableInfo, self);
                for (String col : columns) {
                    String param = StringUtilForDb.dbFieldToJava_smartCheck(col);
                    sb.append("String " + param + " = " + getQuoteStringVal(col, dataMap) + ";\n");
                }
                for (String col : columns) {
                    String param = StringUtilForDb.dbFieldToJava_smartCheck(col);
                    sb.append("String " + param + " = " + "(String)map.get(\"" + col + "\");\n");//
                }
                for (String col : columns) {
                    String param = StringUtilForDb.dbFieldToJava_smartCheck(col);
                    sb.append("map.put(\"" + col + "\", " + param + ");\n");//
                }
                return sb.toString();
            }
        }, //
        MAP_PUT("map.put(orign)") {
            @Override
            String apply(TableInfo tableInfo, Map<String, String> dataMap, FastDBQueryUI_CrudDlgUI self) {
                StringBuilder sb = new StringBuilder();
                Set<String> columns = getTableColumns(tableInfo, self);
                for (String col : columns) {
                    String param = StringUtilForDb.dbFieldToJava_smartCheck(col);
                    String paramVal = getOrignVal(col, tableInfo, dataMap, self);
                    sb.append("map.put(\"" + col + "\", " + paramVal + ");\n");//
                }
                return sb.toString();
            }
        }, //
        MAP_PUT_C("map.put(orign)完整") {
            @Override
            String apply(TableInfo tableInfo, Map<String, String> dataMap, FastDBQueryUI_CrudDlgUI self) {
                StringBuilder sb = new StringBuilder();
                Set<String> columns = getTableColumns(tableInfo, self);
                for (String col : columns) {
                    String param = StringUtilForDb.dbFieldToJava_smartCheck(col);
                    String paramType = getOrignType(col, tableInfo, dataMap, self);
                    String paramVal = getOrignVal(col, tableInfo, dataMap, self);
                    sb.append(paramType + " " + param + " = " + paramVal + ";\n");
                }
                for (String col : columns) {
                    String param = StringUtilForDb.dbFieldToJava_smartCheck(col);
                    String paramType = getOrignType(col, tableInfo, dataMap, self);
                    String paramVal = getOrignVal(col, tableInfo, dataMap, self);
                    sb.append(paramType + " " + param + " = (" + paramType + ")map.get(\"" + col + "\");\n");//
                }
                for (String col : columns) {
                    String param = StringUtilForDb.dbFieldToJava_smartCheck(col);
                    sb.append("map.put(\"" + col + "\", " + param + ");\n");//
                }
                return sb.toString();
            }
        }, //
        VO_SETTER_STR("vo.setter(str)") {
            @Override
            String apply(TableInfo tableInfo, Map<String, String> dataMap, FastDBQueryUI_CrudDlgUI self) {
                StringBuilder sb = new StringBuilder();
                Set<String> columns = getTableColumns(tableInfo, self);
                for (String col : columns) {
                    String param = StringUtilForDb.dbFieldToJava_smartCheck(col);
                    sb.append("vo.set" + StringUtils.capitalize(param) + "(" + getQuoteStringVal(col, dataMap) + ");\n");
                }
                return sb.toString();
            }
        }, //
        VO_SETTER_STR_C("vo.setter(str)完整") {
            @Override
            String apply(TableInfo tableInfo, Map<String, String> dataMap, FastDBQueryUI_CrudDlgUI self) {
                StringBuilder sb = new StringBuilder();
                Set<String> columns = getTableColumns(tableInfo, self);
                for (String col : columns) {
                    String param = StringUtilForDb.dbFieldToJava_smartCheck(col);
                    sb.append("String " + param + " = " + getQuoteStringVal(col, dataMap) + ";\n");
                }
                for (String col : columns) {
                    String param = StringUtilForDb.dbFieldToJava_smartCheck(col);
                    String paramType = getOrignType(col, tableInfo, dataMap, self);
                    String paramVal = getOrignVal(col, tableInfo, dataMap, self);
                    sb.append("String " + param + " = " + "vo.get" + StringUtils.capitalize(param) + "()" + ";\n");
                }
                for (String col : columns) {
                    String param = StringUtilForDb.dbFieldToJava_smartCheck(col);
                    sb.append("vo.set" + StringUtils.capitalize(param) + "(" + param + ");\n");
                }
                return sb.toString();
            }
        }, //
        VO_SETTER("vo.setter(orign)") {
            @Override
            String apply(TableInfo tableInfo, Map<String, String> dataMap, FastDBQueryUI_CrudDlgUI self) {
                StringBuilder sb = new StringBuilder();
                Set<String> columns = getTableColumns(tableInfo, self);
                for (String col : columns) {
                    String param = StringUtilForDb.dbFieldToJava_smartCheck(col);
                    String paramVal = getOrignVal(col, tableInfo, dataMap, self);
                    sb.append("vo.set" + StringUtils.capitalize(param) + "(" + paramVal + ");\n");
                }
                return sb.toString();
            }
        }, //
        VO_SETTER_C("vo.setter(orign)完整") {
            @Override
            String apply(TableInfo tableInfo, Map<String, String> dataMap, FastDBQueryUI_CrudDlgUI self) {
                StringBuilder sb = new StringBuilder();
                Set<String> columns = getTableColumns(tableInfo, self);
                for (String col : columns) {
                    String param = StringUtilForDb.dbFieldToJava_smartCheck(col);
                    String paramType = getOrignType(col, tableInfo, dataMap, self);
                    String paramVal = getOrignVal(col, tableInfo, dataMap, self);
                    sb.append(paramType + " " + param + " = " + paramVal + ";\n");
                }
                for (String col : columns) {
                    String param = StringUtilForDb.dbFieldToJava_smartCheck(col);
                    String paramType = getOrignType(col, tableInfo, dataMap, self);
                    String paramVal = getOrignVal(col, tableInfo, dataMap, self);
                    sb.append(paramType + " " + param + " = " + "vo.get" + StringUtils.capitalize(param) + "()" + ";\n");
                }
                for (String col : columns) {
                    String param = StringUtilForDb.dbFieldToJava_smartCheck(col);
                    sb.append("vo.set" + StringUtils.capitalize(param) + "(" + param + ");\n");
                }
                return sb.toString();
            }
        }, //
           // ↓↓↓↓↓↓
           // 暫放------------------------------------------------------------------
        VO_SETTER_Cathay("vo.setter(orign) [不調整格式]") {
            @Override
            String apply(TableInfo tableInfo, Map<String, String> dataMap, FastDBQueryUI_CrudDlgUI self) {
                StringBuilder sb = new StringBuilder();
                Set<String> columns = getTableColumns(tableInfo, self);
                for (String col : columns) {
                    String param = StringUtilForDb.dbFieldToJava(col);
                    String paramVal = getOrignVal(col, tableInfo, dataMap, self);
                    sb.append("vo.set" + col + "(" + paramVal + ");\n");
                }
                return sb.toString();
            }
        }, //
        VO_SETTER_STR_Cathay("vo.setter(str) [不調整格式]") {
            @Override
            String apply(TableInfo tableInfo, Map<String, String> dataMap, FastDBQueryUI_CrudDlgUI self) {
                StringBuilder sb = new StringBuilder();
                Set<String> columns = getTableColumns(tableInfo, self);
                for (String col : columns) {
                    String param = StringUtilForDb.dbFieldToJava(col);
                    sb.append("vo.set" + col + "(" + getQuoteStringVal(col, dataMap) + ");\n");
                }
                return sb.toString();
            }
        }, //
           // ↑↑↑↑↑↑
           // 暫放------------------------------------------------------------------
        VO_Creater_Orign("vo(orign)") {//
            @Override
            String apply(TableInfo tableInfo, Map<String, String> dataMap, FastDBQueryUI_CrudDlgUI self) {
                StringBuilder sb = new StringBuilder();
                Set<String> columns = getTableColumns(tableInfo, self);
                for (String col : columns) {
                    String param = StringUtilForDb.dbFieldToJava_smartCheck(col);
                    String paramType = getOrignType(col, tableInfo, dataMap, self);
                    String paramVal = getOrignVal(col, tableInfo, dataMap, self);
                    sb.append("@JsonProperty(\"" + col + "\")\n");
                    sb.append("private " + paramType + " " + param + ";\n");
                }
                return sb.toString();
            }
        }, //
        VO_Creater_String("vo(string)") {//
            @Override
            String apply(TableInfo tableInfo, Map<String, String> dataMap, FastDBQueryUI_CrudDlgUI self) {
                StringBuilder sb = new StringBuilder();
                Set<String> columns = getTableColumns(tableInfo, self);
                for (String col : columns) {
                    String param = StringUtilForDb.dbFieldToJava_smartCheck(col);
                    String paramType = getOrignType(col, tableInfo, dataMap, self);
                    String paramVal = getOrignVal(col, tableInfo, dataMap, self);
                    sb.append("@JsonProperty(\"" + col + "\")\n");
                    sb.append("private " + "String" + " " + param + ";\n");
                }
                return sb.toString();
            }
        }, //
        Entity_Creater_Orign("entity(orign)") {//
            @Override
            String apply(TableInfo tableInfo, Map<String, String> dataMap, FastDBQueryUI_CrudDlgUI self) {
                StringBuilder sb = new StringBuilder();
                Set<String> columns = getTableColumns(tableInfo, self);
                for (String col : columns) {
                    String param = StringUtilForDb.dbFieldToJava_smartCheck(col);
                    String paramType = getOrignType(col, tableInfo, dataMap, self);
                    String paramVal = getOrignVal(col, tableInfo, dataMap, self);
                    String notNullFix = "";
                    if (tableInfo.getNoNullsCol().contains(col)) {
                        notNullFix = ", nullable = false";
                    }
                    sb.append("@Column(name=\"" + col + "\"" + notNullFix + ")\n");
                    sb.append("private " + paramType + " " + param + ";\n");
                }
                return sb.toString();
            }
        }, //
        Entity_And_Vo_Creater_Orign("entity(orign)與vo") {//
            @Override
            String apply(TableInfo tableInfo, Map<String, String> dataMap, FastDBQueryUI_CrudDlgUI self) {
                StringBuilder sb = new StringBuilder();
                Set<String> columns = getTableColumns(tableInfo, self);
                for (String col : columns) {
                    String param = StringUtilForDb.dbFieldToJava_smartCheck(col);
                    String paramType = getOrignType(col, tableInfo, dataMap, self);
                    String paramVal = getOrignVal(col, tableInfo, dataMap, self);
                    String notNullFix = "";
                    if (tableInfo.getNoNullsCol().contains(col)) {
                        notNullFix = ", nullable = false";
                    }
                    sb.append("@JsonProperty(\"" + col + "\")\n");
                    sb.append("@Column(name=\"" + col + "\"" + notNullFix + ")\n");
                    sb.append("private " + paramType + " " + param + ";\n");
                }
                return sb.toString();
            }
        }, //
        ;

        final String label;

        OthersDBColumnProcess(String label) {
            this.label = label;
        }

        Set<String> getTableColumns(TableInfo tableInfo, FastDBQueryUI_CrudDlgUI self) {
            Set<String> columns = new LinkedHashSet<String>();
            if (!tableInfo.getColumns().isEmpty() && StringUtils.isNotBlank(tableInfo.getTableAndSchema())) {
                columns = tableInfo.getColumns();
            } else {
                for (String columnName : self.rowMap.get().keySet()) {
                    columns.add(columnName);
                }
            }
            JTableUtil util = JTableUtil.newInstance(self.rowTable);
            if (self.rowTable.getSelectedRows() == null || self.rowTable.getSelectedRows().length == 0) {
                System.out.println("[getTableColumns]全部");
                return columns;
            }
            Set<String> newColSet = new LinkedHashSet<String>();
            for (int row : self.rowTable.getSelectedRows()) {
                String col = (String) util.getRealValueAt(row, 0);
                if (columns.contains(col)) {
                    newColSet.add(col);
                }
            }
            System.out.println("[getTableColumns] -> " + newColSet);
            return newColSet;
        }

        String getQuoteStringVal(String col, Map<String, String> dataMap) {
            // col = col.toUpperCase();
            String value = dataMap.get(col);
            if (value == null || "null".equals(value)) {
                return "null";
            }
            return String.format("\"%s\"", value);
        }

        String getOrignVal(String col, TableInfo tableInfo, Map<String, String> dataMap, FastDBQueryUI_CrudDlgUI self) {
            // col = col.toUpperCase();
            ColumnConf conf = self.rowMap.get().get(col);
            if ("null".equals(dataMap.get(col))) {
                return "null";
            }
            if (tableInfo.getDateCol().contains(col) || conf.dtype == DataType.date) {
                if (StringUtils.isBlank(dataMap.get(col))) {
                    return "null";
                }
                return "java.sql.Date.valueOf(\"" + dataMap.get(col) + "\")";
            } else if (tableInfo.getTimestampCol().contains(col) || conf.dtype == DataType.timestamp) {
                if (StringUtils.isBlank(dataMap.get(col))) {
                    return "null";
                }
                return "java.sql.Timestamp.valueOf(\"" + dataMap.get(col) + "\")";
            } else if (tableInfo.getNumberCol().contains(col) || conf.dtype == DataType.number) {
                if (StringUtils.isBlank(dataMap.get(col))) {
                    return "null";
                }
                String suffix = "L";
                String valuestr = StringUtils.defaultIfBlank(dataMap.get(col), "0");
                if (valuestr.matches("[\\-]?\\d+\\.\\d+") || valuestr.contains("E")) {
                    suffix = "D";
                }
                return String.format("java.math.BigDecimal.valueOf(%s)", valuestr + suffix);
            } else {
                return "\"" + dataMap.get(col) + "\"";
            }
        }

        String getOrignType(String col, TableInfo tableInfo, Map<String, String> dataMap, FastDBQueryUI_CrudDlgUI self) {
            // col = col.toUpperCase();
            ColumnConf conf = self.rowMap.get().get(col);
            if (tableInfo.getDateCol().contains(col) || conf.dtype == DataType.date) {
                return "java.sql.Date";
            } else if (tableInfo.getTimestampCol().contains(col) || conf.dtype == DataType.timestamp) {
                return "java.sql.Timestamp";
            } else if (tableInfo.getNumberCol().contains(col) || conf.dtype == DataType.number) {
                return "java.math.BigDecimal";
            } else {
                return "String";
            }
        }

        abstract String apply(TableInfo tableInfo, Map<String, String> dataMap, FastDBQueryUI_CrudDlgUI self);

        public String toString() {
            return this.label;
        }
    }

    // 邏輯在上=======================================================================================================

    /**
     * Create the dialog.
     */
    public FastDBQueryUI_CrudDlgUI(final FastDBQueryUI _parent) {
        this._parent = _parent;
        this.dBTypeFormatHandler = new DBTypeFormatHandler(_parent);

        this.setTitle("CRUD處理");

        setBounds(100, 100, 680, 463);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        {
            JPanel panel = new JPanel();
            contentPanel.add(panel, BorderLayout.NORTH);
            {
                JLabel label = new JLabel("搜尋");
                panel.add(label);
            }
            {
                searchText = new JTextField();
                for (final JTextField f : new JTextField[] { searchText }) {
                    f.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            if (JMouseEventUtil.buttonRightClick(1, e)) {
                                JPopupMenuUtil.newInstance(f).addJMenuItem("空白換成\"^\"", new ActionListener() {

                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        String[] texts = StringUtils.split(f.getText(), " ");
                                        List<String> arry = new ArrayList<String>();
                                        for (String x : texts) {
                                            x = StringUtils.trimToEmpty(x);
                                            if (StringUtils.isNotBlank(x)) {
                                                arry.add(x);
                                            }
                                        }
                                        f.setText(StringUtils.join(arry, "^"));
                                    }
                                }).applyEvent(e).show();
                            }
                        }
                    });
                }
                panel.add(searchText);
                searchText.setColumns(25);
                searchText.setToolTipText("分隔符號為\"^\"");
            }
            {
                DefaultComboBoxModel model = new DefaultComboBoxModel();
                for (DBDateUtil.DBDateFormat e : DBDateUtil.DBDateFormat.values()) {
                    model.addElement(e);
                }
            }
        }
        {
            JPanel panel = new JPanel();
            contentPanel.add(panel, BorderLayout.WEST);
        }
        {
            JPanel panel = new JPanel();
            contentPanel.add(panel, BorderLayout.EAST);
        }
        {
            JPanel panel = new JPanel();
            contentPanel.add(panel, BorderLayout.SOUTH);
            {
                dbTypeComboBox = new JComboBox();
                dbTypeComboBox.setToolTipText("資料庫類型");
                panel.add(dbTypeComboBox);
                DefaultComboBoxModel model = new DefaultComboBoxModel();
                for (DBDateUtil.DBDateFormat e : DBDateUtil.DBDateFormat.values()) {
                    model.addElement(e);
                }
                dbTypeComboBox.setModel(model);
                // 設定default DB Type
                dBTypeFormatHandler.setDefaultDBType();
            }
            {
                JLabel label = new JLabel("table");
                panel.add(label);
            }
            {
                tableAndSchemaText = new JTextField();
                tableAndSchemaText.setColumns(10);
                panel.add(tableAndSchemaText);
                tableAndSchemaText.addFocusListener(new FocusAdapter() {

                    @Override
                    public void focusLost(FocusEvent e) {
                        tableAndSchemaText_focusLost_action(tableAndSchemaText);
                    }
                });
            }
            {
                rdbtnInsert = new JRadioButton("insert");
                rdbtnInsert.setSelected(true);// 預設值
                panel.add(rdbtnInsert);
            }
            {
                rdbtnUpdate = new JRadioButton("update");
                panel.add(rdbtnUpdate);
            }
            {
                rdbtnDelete = new JRadioButton("delete");
                panel.add(rdbtnDelete);
            }
            {
                rdbtnSelect = new JRadioButton("select");
                panel.add(rdbtnSelect);
            }
            {
                rdbtnOthers = new JRadioButton("其他");
                panel.add(rdbtnOthers);
            }
            btnGroup = JButtonGroupUtil.createRadioButtonGroup(rdbtnInsert, rdbtnUpdate, rdbtnDelete, rdbtnOthers, rdbtnSelect);
            {
                applyAllQueryResultCheckBox = new JCheckBox("套全部");
                panel.add(applyAllQueryResultCheckBox);
            }
        }
        {
            rowTable = new JTable();
            JTableUtil.defaultSetting(rowTable);
            contentPanel.add(JCommonUtil.createScrollComponent(rowTable), BorderLayout.CENTER);
            rowTable.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (JMouseEventUtil.buttonRightClick(1, e)) {
                        final AtomicReference<List<String>> valueLst = new AtomicReference<List<String>>();
                        valueLst.set(new ArrayList<String>());
                        final AtomicInteger rowPos = new AtomicInteger(-1);
                        try {
                            int $rowPos = rowTable.getSelectedRow();
                            if ($rowPos != -1) {
                                $rowPos = JTableUtil.getRealRowPos($rowPos, rowTable);
                                rowPos.set($rowPos);
                                String column = (String) rowTable.getValueAt($rowPos, ColumnOrderDef.columnName.ordinal());
                                column = StringUtils.trimToEmpty(column);//
                                valueLst.set(_parent.getEditColumnConfig().getColumnValues(column));
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        /*
                         * List<JMenuItem> menuList =
                         * JTableUtil.newInstance(rowTable)
                         * .getDefaultJMenuItems_Mask(//
                         * JTableUtil_DefaultJMenuItems_Mask._加列 | //
                         * JTableUtil_DefaultJMenuItems_Mask._加多筆列 | //
                         * JTableUtil_DefaultJMenuItems_Mask._移除列 | //
                         * JTableUtil_DefaultJMenuItems_Mask._移除所有列 | //
                         * JTableUtil_DefaultJMenuItems_Mask._清除已選儲存格 | //
                         * JTableUtil_DefaultJMenuItems_Mask._貼上多行記事本 | //
                         * JTableUtil_DefaultJMenuItems_Mask._貼上單格記事本 // );
                         */
                        JPopupMenuUtil inst = JPopupMenuUtil.newInstance(rowTable);
                        // inst.addJMenuItem(menuList);
                        System.out.println("valueLst --- " + valueLst + " / " + valueLst.get().size());
                        if (valueLst.get().size() > 0) {
                            JMenuAppender chdMenu = JMenuAppender.newInstance("參考Value");
                            for (final String val : valueLst.get()) {
                                chdMenu.addMenuItem(val, new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        rowTable.setValueAt(val, rowPos.get(), ColumnOrderDef.value.ordinal());
                                    }
                                });
                            }
                            inst.addJMenuItem(chdMenu.getMenu());
                        }

                        {// base64 --- start
                            JMenuAppender chdMenu = JMenuAppender.newInstance("Base64");
                            chdMenu.addMenuItem("Encode", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    Object val = JTableUtil.newInstance(rowTable).getSelectedValue();
                                    if (val != null) {
                                        String strVal = String.valueOf(val);
                                        rowTable.setValueAt(Base64JdkUtil.encode(strVal), rowPos.get(), ColumnOrderDef.value.ordinal());
                                    }
                                }
                            });
                            chdMenu.addMenuItem("Decode", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    Object val = JTableUtil.newInstance(rowTable).getSelectedValue();
                                    if (val != null) {
                                        String strVal = String.valueOf(val);
                                        rowTable.setValueAt(Base64JdkUtil.decodeToString(strVal), rowPos.get(), ColumnOrderDef.value.ordinal());
                                    }
                                }
                            });
                            inst.addJMenuItem(chdMenu.getMenu());
                        } // base64 --- end
                        inst.applyEvent(e).show();
                    }
                }
            });
        }
        {
            addWindowListener(new WindowAdapter() {

                public void windowClosed(WindowEvent e) {
                    // 儲存default DB Type
                    dBTypeFormatHandler.storeCurrentDBType();
                }
            });
        }
        {

            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));

            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                okButton = new JButton("OK");
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    }
                });
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                // getRootPane().setDefaultButton(okButton);//取消調預設按鈕
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
                cancelButton.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        FastDBQueryUI_CrudDlgUI.this.dispose();
                    }
                });
            }
        }

        this.keyEventExecuteHandler = KeyEventExecuteHandler.newInstance(this, null, null, new Runnable() {

            @Override
            public void run() {
                JCommonUtil.triggerButtonActionPerformed(okButton);
            }
        }, new Component[] {});
        JCommonUtil.setJFrameCenter(this);
        JCommonUtil.defaultToolTipDelay();
        jFrameRGBColorPanel = new JFrameRGBColorPanel(this);
        jFrameRGBColorPanel.setStop(_parent.getjFrameRGBColorPanel().isStop());
    }

    private class DBTypeFormatHandler {
        FastDBQueryUI _parent;

        DBTypeFormatHandler(FastDBQueryUI _parent) {
            this._parent = _parent;
        }

        private void setDefaultDBType() {
            try {
                String DBDateFormatVal = _parent.getEtcConfig().getProperty(KEY_DBDateFormat);
                if (StringUtils.isNotBlank(DBDateFormatVal)) {
                    DBDateUtil.DBDateFormat defaultSelectVal = DBDateUtil.DBDateFormat.valueOf(DBDateFormatVal);
                    dbTypeComboBox.setSelectedItem(defaultSelectVal);
                }
            } catch (Exception ex) {
                JCommonUtil.handleException(ex);
            }
        }

        private void storeCurrentDBType() {
            try {
                DBDateUtil.DBDateFormat defaultSelectVal = (DBDateUtil.DBDateFormat) dbTypeComboBox.getSelectedItem();
                if (defaultSelectVal != null) {
                    _parent.getEtcConfig().setProperty(KEY_DBDateFormat, defaultSelectVal.name());
                    _parent.getEtcConfig().store();
                }
            } catch (Exception ex) {
                JCommonUtil.handleException(ex);
            }
        }

    }

    // 儲存 欄位編輯歷史紀錄
    private void updateColumnHistory() {
        Map<String, ColumnConf> colDef = this.rowMap.get();
        for (String col : colDef.keySet()) {
            ColumnConf def = colDef.get(col);
            if (def.isModify) {
                _parent.getEditColumnConfig().addColumnDef(col, def.value);
            }
        }
        _parent.getEditColumnConfig().store();
    }
}
