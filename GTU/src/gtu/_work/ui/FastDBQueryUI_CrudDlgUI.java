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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
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
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Row;

import gtu._work.ui.FastDBQueryUI_ColumnSearchFilter.FindTextHandler;
import gtu._work.ui.JMenuBarUtil.JMenuAppender;
import gtu.binary.StringUtil4FullChar;
import gtu.db.JdbcDBUtil;
import gtu.db.jdbc.util.DBDateUtil;
import gtu.db.jdbc.util.DBDateUtil.DBDateFormat;
import gtu.db.sqlMaker.DbSqlCreater.FieldInfo4DbSqlCreater;
import gtu.db.sqlMaker.DbSqlCreater.TableInfo;
import gtu.file.FileUtil;
import gtu.poi.hssf.ExcelUtil_Xls97;
import gtu.poi.hssf.ExcelWriter;
import gtu.poi.hssf.ExcelWriter.CellStyleHandler;
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
import gtu.swing.util.S2T_And_T2S_EventHandler;

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

    private AtomicReference<LinkedMapIgnoreCase<String, ColumnConf>> rowMap = new AtomicReference<LinkedMapIgnoreCase<String, ColumnConf>>();
    private JRadioButton rdbtnOthers;
    private FastDBQueryUI _parent;
    private JCheckBox applyAllQueryResultCheckBox;
    private JFrameRGBColorPanel jFrameRGBColorPanel;
    private JRadioButton rdbtnSelect;
    private List<String> columnsLst;
    private List<String> addFromCustomTableColumnLst = new ArrayList<String>();
    private DBTypeFormatHandler dBTypeFormatHandler;
    private KeyEventExecuteHandler keyEventExecuteHandler;
    private RecordsHandler mRecordsHandler;

    private static final String KEY_DBDateFormat = FastDBQueryUI_CrudDlgUI.class.getSimpleName() + "_" + DBDateFormat.class.getSimpleName();
    private JButton previousRecordBtn;
    private JButton nextRecordBtn;
    private JLabel recordsLbl;
    private JButton resetRecordBtn;
    private JCheckBox forAllQueryResultCheckBox;
    private JTabbedPane masterJTabbedPane;
    private JPanel sqlAppendPanel;
    private JPanel panel_1;
    private JPanel panel_2;
    private JPanel panel_3;
    private JPanel panel_4;
    private JLabel lblSql;
    private JTextArea sqlTextArea;
    private JButton sqlQueryBtn;
    private JTable sqlTable;
    private MySearchHandler mMySearchHandler;
    private JTextField sqlSearchText;
    private JCheckBox sqlDistinctCheckbox;
    private JButton sqlQuerySetbackBtn;
    private JCheckBox sqlColumnCheckAllChk;
    private JButton sqlQueryPreviousBtn;
    private JButton sqlQueryNextBtn;
    private JLabel sqlQueryCountLbl;
    private SqlQueryHolder mSqlQueryHolder;
    private JButton exportExcelBtn;

    private enum ColumnOrderDef {
        columnName("欄位", 25, false), //
        value("值", 25, true), //
        currentLength("現在長度", 5, false), //
        maxLength("最大長度", 5, false), //
        dtype("資料類型", 25, true), //
        isPk("過濾條件", 13, true), //
        isIgnore("省略", 12, true), //
        ;

        final int width;
        final String label;
        final boolean editable;

        ColumnOrderDef(String label, int width, boolean editable) {
            this.label = label;
            this.width = width;
            this.editable = editable;
        }

        private static DefaultTableModel createDefaultTableModel() {
            List<String> dlst = new ArrayList<String>();
            List<Integer> editableLst = new ArrayList<Integer>();
            for (ColumnOrderDef d : ColumnOrderDef.values()) {
                dlst.add(d.label);
                if (d.editable) {
                    editableLst.add(d.ordinal());
                }
            }
            return JTableUtil.createModel(ArrayUtils.toPrimitive(editableLst.toArray(new Integer[0])), dlst.toArray(new String[0]));
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

        String bakupColumnName;
        boolean isAddFromCustomTableName = false;

        boolean IsModifyGo() {
            String v1 = value != null ? String.valueOf(value) : null;
            String v2 = orignValue != null ? String.valueOf(orignValue) : null;
            return !StringUtils.equals(v1, v2);
        }

        Object[] toArry() {
            int currentLength = 0;
            if (value != null) {
                currentLength = StringUtils.defaultString(String.valueOf(value)).length();
                if (dtype == DataType.NULL && StringUtils.equals((String) value, "null")) {
                    currentLength = 0;
                }
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
        bool(Boolean.class) {
            protected void applyDataChange(Object value, JTable table, int row, FastDBQueryUI_CrudDlgUI self) {
                System.out.println("-------" + value + " -> " + value.getClass());
                table.setValueAt(value, row, ColumnOrderDef.value.ordinal());
            }
        }, //
        NULL(void.class) {
            protected void applyDataChange(Object value, JTable table, int row, FastDBQueryUI_CrudDlgUI self) {
                System.out.println("-------" + value + " -> " + value.getClass());
                table.setValueAt("null", row, ColumnOrderDef.value.ordinal());
            }
        }, //
        UNKNOW(void.class) {
            protected void applyDataChange(Object value, JTable table, int row, FastDBQueryUI_CrudDlgUI self) {
                System.out.println("############################################################");
                System.out.println("#                       UNKNOW                             #");
                System.out.println("-------" + value + " -> " + value.getClass());
                table.setValueAt(value, row, ColumnOrderDef.value.ordinal());
                System.out.println("#                                                          #");
                System.out.println("############################################################");
            }
        },//
        ;

        final Class<?>[] clz;

        DataType(Class<?>... clz) {
            this.clz = clz;
        }

        static DataType isTypeOfClass(Class clz) {
            if (clz == null) {
                return NULL;
            }
            for (DataType e : DataType.values()) {
                for (Class<?> c : e.clz) {
                    if (c == clz) {
                        return e;
                    }
                }
            }
            for (DataType e : DataType.values()) {
                for (Class<?> c : e.clz) {
                    if (c.isAssignableFrom(clz)) {
                        return e;
                    }
                }
            }
            return UNKNOW;
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

    public static FastDBQueryUI_CrudDlgUI newInstance(final List<Map<String, Pair<Object, Class>>> rowMapLst, final String tableNSchema,
            final Triple<List<String>, List<Class<?>>, List<Object[]>> queryList, final ActionListener onCloseListener, final FastDBQueryUI _parent) {
        try {
            final FastDBQueryUI_CrudDlgUI dialog = new FastDBQueryUI_CrudDlgUI(_parent);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);

            JTable rowTable = dialog.rowTable;

            final JTableUtil tableUtil = JTableUtil.newInstance(rowTable);

            dialog.columnsLst = new ArrayList<String>();

            dialog.mRecordsHandler = dialog.new RecordsHandler(rowMapLst, queryList);

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

                        List<Map<String, String>> maybeMultiRowLst = new ArrayList<Map<String, String>>();

                        System.out.println("Process Init Start ==================================");

                        // 第一筆的處理
                        for (String columnName : dialog.rowMap.get().keySet()) {
                            columnName = StringUtils.trimToEmpty(columnName);
                            ColumnConf df = dialog.rowMap.get().get(columnName);
                            String value = df.value != null ? String.valueOf(df.value) : null;
                            DataType dtype = df.dtype;
                            boolean isPk = df.isPk;
                            if (df.isIgnore) {
                                ignoreSet.add(columnName);
                                System.out.println("\t ignore : " + columnName);
                            }
                            if (isPk) {
                                pkColumns.add(columnName);
                                noNullsCol.add(columnName);
                                System.out.println("\t pk : " + columnName);
                            }
                            if (dtype == DataType.date) {
                                dateCol.add(columnName);
                            } else if (dtype == DataType.timestamp) {
                                timestampCol.add(columnName);
                            } else if (dtype == DataType.number) {
                                numberCol.add(columnName);
                            }
                        }

                        System.out.println("Process Init End   ==================================");

                        // 其他筆的處理
                        if (dialog.mRecordsHandler.size() > 0) {
                            boolean isAllRecord = dialog.forAllQueryResultCheckBox.isSelected();
                            maybeMultiRowLst.addAll(dialog.mRecordsHandler.getAllRecoreds(isAllRecord));
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

                        System.out.println("maybeMultiRowLst size : " + this.maybeMultiRowLst.size());
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
                            // List<Map<String, String>> qlst =
                            // process.getAllRecoreds();
                            List<Map<String, String>> qlst = dialog.mRecordsHandler.getAllRecoreds(true);

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

        JTableUtil.newInstance(rowTable).setColumnColor_byCondition(ColumnOrderDef.columnName.ordinal(), new JTableUtil.TableColorDef() {
            public Pair<Color, Color> getTableColour(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JTableUtil util = JTableUtil.newInstance(rowTable);
                String columnName = (String) util.getRealValueAt(row, ColumnOrderDef.columnName.ordinal());
                LinkedMapIgnoreCase<String, ColumnConf> conf = rowMap.get();
                if (conf.containsKey(columnName)) {
                    ColumnConf cf = conf.get(columnName);
                    if (cf.isAddFromCustomTableName) {
                        return Pair.of(Color.LIGHT_GRAY, null);
                    }
                }
                return null;
            }
        });

        JTableUtil.newInstance(rowTable).setColumnColor_byCondition(ColumnOrderDef.value.ordinal(), new JTableUtil.TableColorDef() {
            public Pair<Color, Color> getTableColour(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JTableUtil util = JTableUtil.newInstance(rowTable);
                String columnName = (String) util.getRealValueAt(row, ColumnOrderDef.columnName.ordinal());
                // String v1 = (String) util.getRealValueAt(row,
                // ColumnOrderDef.value.ordinal());
                LinkedMapIgnoreCase<String, ColumnConf> conf = rowMap.get();
                if (conf.containsKey(columnName)) {
                    ColumnConf cf = conf.get(columnName);
                    String orignValue = "";
                    String value1 = "";
                    if (cf.orignValue != null) {
                        orignValue = String.valueOf(cf.orignValue);
                    }
                    if (cf.value != null) {
                        value1 = String.valueOf(cf.value);
                    }
                    if (!StringUtils.equals(orignValue, value1)) {
                        return Pair.of(Color.GREEN, null);
                    }
                }
                return null;
            }
        });

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
        sportColumn.setCellEditor(new JTableUtil.OnBlurCellEditor(comboBox, false) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onblur(int row, int col, Object value) {
                if (value == DataType.NULL) {
                    System.out.println("lastestRow----" + row);
                    JTableUtil.newInstance(rowTable).setValueAt(false, "null", row, ColumnOrderDef.value.ordinal());
                }
            }
        });

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
                    int currentLength = getCurrentValueLength(currentOrignValue);
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

        // set value mouse event
        JTextField editJTextField = new JTextField();
        editJTextField.addMouseListener(new S2T_And_T2S_EventHandler(editJTextField).getEvent());
        JTableUtil.newInstance(rowTable).columnIsComponent(ColumnOrderDef.value.ordinal(), editJTextField);
        return tableUtil.getModel();
    }

    private int getCurrentValueLength(Object currentOrignValue) {
        String valueStr = currentOrignValue == null ? "" : String.valueOf(currentOrignValue);
        int currentLength = 0;
        if ("null".equals(valueStr)) {
            currentLength = 0;
        } else {
            currentLength = StringUtil4FullChar.length(valueStr);
        }
        System.out.println(" currentLength : " + valueStr + " -> " + currentLength);
        return currentLength;
    }

    // 自動設定pk
    private void tableAndSchemaText_focusLost_action(JTextField tableAndSchemaText) {
        try {
            TableInfo tableInfo = new TableInfo();

            String tableAndSchema = StringUtils.trimToEmpty(tableAndSchemaText.getText());
            if (StringUtils.isNotBlank(tableAndSchema)) {
                boolean confirm = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("是否要重設 " + tableAndSchema + " 的 PK", "重設?");
                if (!confirm) {
                    return;
                }

                JCommonUtil.isBlankErrorMsg(tableAndSchema, "輸入表名稱!");

                DBDateUtil.DBDateFormat dbDateDateFormat = (DBDateUtil.DBDateFormat) dbTypeComboBox.getSelectedItem();
                tableInfo.setDbDateDateFormat(dbDateDateFormat);
                tableInfo.execute(String.format(" select * from %s where 1!=1 ", tableAndSchema), _parent.getDataSource().getConnection());
                if (StringUtils.isBlank(tableInfo.getTableName())) {
                    tableInfo.setTableName(tableAndSchema);
                }
            }

            Set<String> success = new LinkedHashSet<String>();
            Set<String> failed = new LinkedHashSet<String>();

            Map<String, ColumnConf> columnPkConf = new HashMap<String, ColumnConf>();

            if (columnsLst == null || columnsLst.isEmpty()) {
                Validate.isTrue(false, "資料欄位尚未初始化！[columnsLst-123]");
            }

            for (String columnName : columnsLst) {
                ColumnConf df = new ColumnConf();
                df.isPk = false;
                df.isAddFromCustomTableName = false;
                df.bakupColumnName = "";
                df.columnName = columnName;
                columnPkConf.put(columnName, df);
            }

            List<String> finOkColumnLst = new ArrayList<String>();

            for (String rowColumnName : columnPkConf.keySet()) {
                boolean findOk = false;
                C: for (String columnName : tableInfo.getColumnInfo().keySet()) {
                    boolean useRealColumn = false;
                    if (StringUtils.equalsIgnoreCase(rowColumnName, columnName)) {
                        findOk = true;
                        finOkColumnLst.add(columnName);
                    }
                    if (StringUtils.equalsIgnoreCase(rowColumnName, StringUtilForDb.dbFieldToJava(columnName))) {
                        findOk = true;
                        useRealColumn = true;
                        finOkColumnLst.add(rowColumnName);
                        finOkColumnLst.add(columnName);
                    }
                    if (findOk) {
                        FieldInfo4DbSqlCreater info = tableInfo.getColumnInfo().get(columnName);
                        columnPkConf.get(rowColumnName).maxLength = info.getColumnDisplaySize();
                        if (useRealColumn) {
                            columnPkConf.get(rowColumnName).bakupColumnName = columnName;
                        }
                        break C;
                    }
                }
                if (!findOk) {
                    columnPkConf.get(rowColumnName).maxLength = null;
                }
            }

            addFromCustomTableColumnLst.clear();
            for (String columnName : tableInfo.getColumnInfo().keySet()) {
                if (!finOkColumnLst.contains(columnName)) {
                    ColumnConf df = new ColumnConf();
                    df.columnName = columnName;
                    df.isAddFromCustomTableName = true;
                    FieldInfo4DbSqlCreater info = tableInfo.getColumnInfo().get(columnName);
                    df.maxLength = info.getColumnDisplaySize();
                    columnPkConf.put(columnName, df);
                    addFromCustomTableColumnLst.add(columnName);
                }
            }

            for (String columnName : tableInfo.getNoNullsCol()) {
                if (columnPkConf.containsKey(columnName)) {
                    columnPkConf.get(columnName).isPk = true;
                    success.add(columnName);
                } else {
                    C: for (String columnName2 : columnPkConf.keySet()) {
                        String bakupColumnName = StringUtilForDb.javaToDbField(columnName2);
                        if (StringUtils.equalsIgnoreCase(columnName, bakupColumnName)) {
                            columnPkConf.get(columnName).isPk = true;
                            success.add(columnName);
                            break C;
                        } else {
                            failed.add(columnName);
                            break C;
                        }
                    }
                }
            }

            System.out.println("Debug Start ===========================================");
            for (String columnName : columnPkConf.keySet()) {
                ColumnConf col = columnPkConf.get(columnName);
                if (col.isPk || col.isIgnore) {
                    System.out.println("\t" + columnName + ": pk :" + col.isPk + " , ignore : " + col.isIgnore + " , isAddFromCustomTable : " + col.isAddFromCustomTableName + " , bakColumn : "
                            + col.bakupColumnName);
                }
            }
            System.out.println("Debug End   ===========================================");

            // 重設 pk 與 忽略
            for (String columnName : rowMap.get().keySet()) {
                ColumnConf conf = rowMap.get().get(columnName);
                conf.isPk = false;
                conf.isIgnore = false;
            }

            // 重新設定 pk
            mRecordsHandler.setColumnPkConf(columnPkConf);
            mRecordsHandler.mergePkConfig();

            // 刷新table
            searchTextFilter();

            if (StringUtils.isNotBlank(tableAndSchema)) {
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

        List<String> columnsLst2 = new ArrayList<String>();
        columnsLst2.addAll(columnsLst);
        columnsLst2.addAll(addFromCustomTableColumnLst);

        B: for (String columnName : columnsLst2) {
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

        setBounds(100, 100, 750, 531);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        masterJTabbedPane = new JTabbedPane();
        getContentPane().add(masterJTabbedPane, BorderLayout.CENTER);
        masterJTabbedPane.add(contentPanel, "CRUD");
        contentPanel.setLayout(new BorderLayout(0, 0));
        {
            JPanel panel = new JPanel();
            contentPanel.add(panel, BorderLayout.NORTH);
            {
                previousRecordBtn = new JButton("<");
                previousRecordBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        mRecordsHandler.previousRecordBtnAction();
                    }
                });
                {
                    recordsLbl = new JLabel("");
                    panel.add(recordsLbl);
                }
                panel.add(previousRecordBtn);
            }
            {
                nextRecordBtn = new JButton(">");
                nextRecordBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        mRecordsHandler.nextRecordBtnAction();
                    }
                });
                panel.add(nextRecordBtn);
            }
            {
                resetRecordBtn = new JButton("reset");
                resetRecordBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        mRecordsHandler.resetRecordBtnAction();
                    }
                });
                panel.add(resetRecordBtn);
            }
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
                forAllQueryResultCheckBox = new JCheckBox("全部");
                panel.add(forAllQueryResultCheckBox);
            }
            {
                applyAllQueryResultCheckBox = new JCheckBox("匯出SQL");
                panel.add(applyAllQueryResultCheckBox);
            }
        }
        {
            rowTable = new JTable();
            JTableUtil.newInstance(rowTable).applyOnHoverEvent(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() != null) {
                        Pair<Integer, Integer> pair = (Pair<Integer, Integer>) e.getSource();
                        int row = pair.getLeft();
                        int col = pair.getRight();
                        if (col == ColumnOrderDef.columnName.ordinal()) {
                            String column = (String) JTableUtil.newInstance(rowTable).getValueAt(false, row, ColumnOrderDef.columnName.ordinal());
                            rowTable.setToolTipText(_parent.mTableColumnDefTextHandler.getChinese(column, null));
                            return;
                        }
                    }
                    rowTable.setToolTipText(null);
                }
            });
            JTableUtil.defaultSetting(rowTable);
            contentPanel.add(JCommonUtil.createScrollComponent(rowTable), BorderLayout.CENTER);
            rowTable.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (JMouseEventUtil.buttonRightClick(1, e)) {
                        final AtomicReference<String> columnHolder = new AtomicReference<String>();
                        final AtomicReference<String> valueHolder = new AtomicReference<String>();
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
                                columnHolder.set(column);

                                String value = (String) rowTable.getValueAt($rowPos, ColumnOrderDef.value.ordinal());
                                valueHolder.set(value);

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

                            inst.addJMenuItem("全部用此值", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    applyThisValueToAll(columnHolder.get(), valueHolder.get());
                                }
                            });
                            // 日期時間
                            {
                                JMenu menu1 = JMenuAppender.newInstance("日期時間")//
                                        .addMenuItem("日期", new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                String dateStrVal = DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd");
                                                rowTable.setValueAt(dateStrVal, rowPos.get(), ColumnOrderDef.value.ordinal());
                                            }
                                        })//
                                        .addMenuItem("日期時間", new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                String dateStrVal = DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss");
                                                rowTable.setValueAt(dateStrVal, rowPos.get(), ColumnOrderDef.value.ordinal());
                                            }
                                        })//
                                        .addMenuItem("日期[台]", new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                String str = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                                                String dateStrVal = String.valueOf(Integer.parseInt(str.substring(0, 4)) - 1911) + str.substring(4);
                                                rowTable.setValueAt(dateStrVal, rowPos.get(), ColumnOrderDef.value.ordinal());
                                            }
                                        })//
                                        .addMenuItem("日期時間[台]", new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                String str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                                                String dateStrVal = String.valueOf(Integer.parseInt(str.substring(0, 4)) - 1911) + str.substring(4);
                                                rowTable.setValueAt(dateStrVal, rowPos.get(), ColumnOrderDef.value.ordinal());
                                            }
                                        })//
                                        .addMenuItem("時間", new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                String dateStrVal = DateFormatUtils.format(System.currentTimeMillis(), "HH:mm:ss");
                                                rowTable.setValueAt(dateStrVal, rowPos.get(), ColumnOrderDef.value.ordinal());
                                            }
                                        })//
                                        .getMenu();
                                inst.addJMenuItem(menu1);
                            }
                        }
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
                {
                    exportExcelBtn = new JButton("匯出excel");
                    exportExcelBtn.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            exportExcelBtnAction();
                        }
                    });
                    buttonPane.add(exportExcelBtn);
                }
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

        // =================================================================================================================
        // =================================================================================================================

        sqlAppendPanel = new JPanel();
        masterJTabbedPane.add(sqlAppendPanel, "SQL");
        sqlAppendPanel.setLayout(new BorderLayout(0, 0));
        {
            panel_1 = new JPanel();
            sqlAppendPanel.add(panel_1, BorderLayout.NORTH);
            {
                sqlColumnCheckAllChk = new JCheckBox("");
                panel_1.add(sqlColumnCheckAllChk);
                sqlColumnCheckAllChk.setToolTipText("全選");
                sqlColumnCheckAllChk.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boolean check = sqlColumnCheckAllChk.isSelected();
                        DefaultTableModel model = (DefaultTableModel) sqlTable.getModel();
                        for (int ii = 0; ii < model.getRowCount(); ii++) {
                            model.setValueAt(check, ii, 0);
                        }
                    }
                });
            }
            {
                lblSql = new JLabel("SQL");
                panel_1.add(lblSql);
            }
            {
                sqlTextArea = new JTextArea();
                sqlTextArea.setColumns(60);
                sqlTextArea.setRows(2);
                panel_1.add(JCommonUtil.createScrollComponent(sqlTextArea));
            }
            {
                sqlQueryBtn = new JButton("Query");
                panel_1.add(sqlQueryBtn);

                sqlQueryBtn.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            mSqlQueryHolder = new SqlQueryHolder();
                            mSqlQueryHolder.query();
                        } catch (Exception ex) {
                            JCommonUtil.handleException(ex);
                        }
                    }
                });

            }

            {
                panel_2 = new JPanel();
                sqlAppendPanel.add(panel_2, BorderLayout.WEST);
            }
            {
                panel_3 = new JPanel();
                sqlAppendPanel.add(panel_3, BorderLayout.EAST);
            }
            {
                panel_4 = new JPanel();
                sqlAppendPanel.add(panel_4, BorderLayout.SOUTH);
                {
                    sqlQueryPreviousBtn = new JButton("<");
                    sqlQueryPreviousBtn.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            try {
                                if (mSqlQueryHolder != null) {
                                    mSqlQueryHolder.previous();
                                }
                            } catch (Exception ex) {
                                JCommonUtil.handleException(ex);
                            }
                        }
                    });
                    {
                        sqlQueryCountLbl = new JLabel("      ");
                        panel_4.add(sqlQueryCountLbl);
                    }
                    panel_4.add(sqlQueryPreviousBtn);
                }
                {
                    sqlQueryNextBtn = new JButton(">");
                    sqlQueryNextBtn.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            try {
                                if (mSqlQueryHolder != null) {
                                    mSqlQueryHolder.next();
                                }
                            } catch (Exception ex) {
                                JCommonUtil.handleException(ex);
                            }
                        }
                    });
                    panel_4.add(sqlQueryNextBtn);
                }
                {
                    sqlDistinctCheckbox = new JCheckBox("");
                    panel_4.add(sqlDistinctCheckbox);
                    sqlDistinctCheckbox.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent arg0) {
                            if (!sqlDistinctCheckbox.isSelected()) {
                                mMySearchHandler.initTable();
                                return;
                            }
                            JTableUtil.newInstance(sqlTable).setRowFilter(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent arg0) {
                                    arg0.setSource(mMySearchHandler.handleRow(arg0.getID()));
                                }
                            });
                        }
                    });

                    sqlSearchText = new JTextField();
                    panel_4.add(sqlSearchText);
                    sqlSearchText.setColumns(30);
                    {
                        sqlQuerySetbackBtn = new JButton("設定勾選項目");
                        sqlQuerySetbackBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                mMySearchHandler.doSetbackAction();
                            }
                        });
                        panel_4.add(sqlQuerySetbackBtn);
                    }

                    sqlSearchText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
                        @Override
                        public void process(DocumentEvent event) {
                            mMySearchHandler.process();
                        }
                    }));
                }
            }
            {
                sqlTable = new JTable();
                JTableUtil.defaultSetting(sqlTable);
                sqlAppendPanel.add(JCommonUtil.createScrollComponent(sqlTable), BorderLayout.CENTER);

                mMySearchHandler = new MySearchHandler();
                mMySearchHandler.initTable();
                mMySearchHandler.process();
            }
        }

        // ================================================================================================================
        // ================================================================================================================
        // ================================================================================================================

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
        LinkedMapIgnoreCase<String, ColumnConf> colDef = this.rowMap.get();
        for (String col : colDef.keySet()) {
            ColumnConf def = colDef.get(col);
            if (def.isModify) {
                _parent.getEditColumnConfig().addColumnDef(col, def.value);
            }
        }
        _parent.getEditColumnConfig().store();
    }

    private class LinkedMapIgnoreCase<K, V> extends TreeMap<K, V> {

        public LinkedMapIgnoreCase() {
            super((Comparator<? super K>) String.CASE_INSENSITIVE_ORDER);
        }

        Set<K> keys = new LinkedHashSet<K>();

        public V put(K key, V value) {
            keys.add(key);
            super.put(key, value);
            return value;
        }

        public Set<K> keySet() {
            return keys;
        }
    }

    private class RecordsHandler {
        List<Map<String, Pair<Object, Class>>> rowMapLst;
        Map<Integer, LinkedMapIgnoreCase> rowMapLstHolder = new TreeMap<Integer, LinkedMapIgnoreCase>();
        int index = 0;
        Map<String, ColumnConf> columnPkConf = Collections.EMPTY_MAP;
        Triple<List<String>, List<Class<?>>, List<Object[]>> queryList;

        ValueFixHandler valueFixHandler = new ValueFixHandler();

        private void setColumnPkConf(Map<String, ColumnConf> columnPkConf) {
            this.columnPkConf = columnPkConf;
        }

        private void mergePkConfig() {
            if (this.columnPkConf != null && !this.columnPkConf.isEmpty()) {
                for (String columnName : this.columnPkConf.keySet()) {
                    ColumnConf c1 = this.columnPkConf.get(columnName);
                    ColumnConf c2 = rowMap.get().get(columnName);
                    if (c1 == null) {
                        System.out.println("mergePkConfig ==null pk setting== : " + columnName);
                        continue;
                    }
                    if (!rowMap.get().containsKey(columnName)) {
                        ColumnConf c11 = new ColumnConf();
                        c11.columnName = c1.columnName;
                        c11.isPk = c1.isPk;
                        c11.maxLength = c1.maxLength;
                        c11.isAddFromCustomTableName = c1.isAddFromCustomTableName;
                        c2 = c11;
                        rowMap.get().put(columnName, c2);
                    }
                    c2.isPk = c1.isPk;
                    c2.maxLength = c1.maxLength;
                    c2.isAddFromCustomTableName = c1.isAddFromCustomTableName;
                    c2.bakupColumnName = c1.bakupColumnName;
                }
            }
        }

        private void init(boolean reset) {
            String recordLblText = (index + 1) + "/" + rowMapLst.size();
            recordsLbl.setText(recordLblText);

            DefaultTableModel model = initRowTable();
            JTableUtil.newInstance(rowTable).setRowHeightByFontSize();

            Map<String, Pair<Object, Class>> rowMapZ = rowMapLst.get(index);

            LinkedMapIgnoreCase<String, ColumnConf> rowMapForBackup = new LinkedMapIgnoreCase<String, ColumnConf>();
            if (rowMapLstHolder.containsKey(index)) {
                rowMapForBackup = rowMapLstHolder.get(index);
            } else {
                rowMapLstHolder.put(index, rowMapForBackup);
            }
            rowMap.set(rowMapForBackup);

            for (String col : rowMapZ.keySet()) {
                ColumnConf df = null;
                if (rowMapForBackup.containsKey(col) && !reset) {
                    df = rowMapForBackup.get(col);
                } else {
                    Pair<Object, Class> pair = rowMapZ.get(col);
                    Object value = pair.getLeft();
                    Class type = pair.getRight();
                    value = valueFixHandler.getValueFix(value);

                    df = new ColumnConf();
                    df.columnName = col;
                    df.dtype = DataType.isTypeOfClass(type);
                    df.value = value;
                    df.orignValue = value;

                    rowMapForBackup.put(col, df);
                }
            }

            // 將表設定值拉進來
            mergePkConfig();

            List<String> columnsLst222 = new ArrayList<String>();
            for (String col : rowMap.get().keySet()) {
                ColumnConf df = rowMap.get().get(col);
                model.addRow(df.toArry());
                columnsLst222.add(col);
            }

            if (reset) {
                if (columnsLst != null && !columnsLst.isEmpty()) {
                    // xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                }
                columnsLst = Collections.unmodifiableList(columnsLst222);
            }

            for (String col : addFromCustomTableColumnLst) {
                ColumnConf df = rowMap.get().get(col);
                model.addRow(df.toArry());
            }

            System.out.println("-------------init size : " + rowMap.get().size());
        }

        private RecordsHandler(List<Map<String, Pair<Object, Class>>> rowMapLst, Triple<List<String>, List<Class<?>>, List<Object[]>> queryList2) {
            this.rowMapLst = rowMapLst;
            this.queryList = queryList2;
            init(true);
        }

        private void previousRecordBtnAction() {
            if (index > 0) {
                index--;
            }
            init(false);
            // 刷新table
            searchTextFilter();
        }

        private void nextRecordBtnAction() {
            if (index < this.rowMapLst.size() - 1) {
                index++;
            }
            init(false);
            // 刷新table
            searchTextFilter();
        }

        private void reflush() {
            init(false);
            // 刷新table
            searchTextFilter();
        }

        private void resetRecordBtnAction() {
            init(true);
            // 刷新table
            searchTextFilter();
        }

        private int size() {
            return rowMapLst.size();
        }

        private void applyThisValueToAll(boolean reset, String column, Object value1) {
            for (int ii = 0; ii < rowMapLst.size(); ii++) {
                Map<String, Pair<Object, Class>> rowMapZ = rowMapLst.get(ii);

                LinkedMapIgnoreCase<String, ColumnConf> rowMapForBackup = new LinkedMapIgnoreCase<String, ColumnConf>();
                if (rowMapLstHolder.containsKey(ii)) {
                    rowMapForBackup = rowMapLstHolder.get(ii);
                } else {
                    rowMapLstHolder.put(ii, rowMapForBackup);
                }
                // rowMap.set(rowMapForBackup);

                for (String col : rowMapZ.keySet()) {
                    ColumnConf df = null;
                    if (rowMapForBackup.containsKey(col) && !reset) {
                        df = rowMapForBackup.get(col);
                    } else {
                        Pair<Object, Class> pair = rowMapZ.get(col);
                        Object value = pair.getLeft();
                        Class type = pair.getRight();
                        value = valueFixHandler.getValueFix(value);

                        df = new ColumnConf();
                        df.columnName = col;
                        df.dtype = DataType.isTypeOfClass(type);
                        df.value = value;
                        df.orignValue = value;

                        rowMapForBackup.put(col, df);
                    }

                    // fix value here ↓↓↓↓↓↓
                    if (StringUtils.equalsIgnoreCase(column, col)) {
                        df.value = value1;
                        df.isModify = true;
                    }
                    // fix value here ↑↑↑↑↑↑
                }
            }
            System.out.println("fix value done...");
        }

        private List<Map<String, String>> getAllRecoreds(boolean isAllData) {
            Map<Integer, Map<String, String>> rtnMap = new LinkedHashMap<Integer, Map<String, String>>();
            // List<String> cols = queryList.getLeft();
            if (isAllData) {
                for (int iii = 0; iii < rowMapLst.size(); iii++) {
                    Map<String, Pair<Object, Class>> map = rowMapLst.get(iii);
                    Map<String, String> map2 = new LinkedHashMap<String, String>();
                    for (String key : map.keySet()) {
                        String strVal = null;
                        Pair<Object, Class> pair = map.get(key);
                        Object val = pair.getLeft();
                        if (val != null) {
                            strVal = String.valueOf(val);
                        }
                        // ↓↓↓↓↓↓ 使用真column
                        if (columnPkConf.containsKey(key)) {
                            ColumnConf df = columnPkConf.get(key);
                            if (StringUtils.isNotBlank(df.bakupColumnName)) {
                                key = df.bakupColumnName;
                            }
                        }
                        // ↑↑↑↑↑↑ 使用真column
                        map2.put(key, strVal);
                    }
                    rtnMap.put(iii, map2);
                }
            }
            for (Integer index : rowMapLstHolder.keySet()) {
                LinkedMapIgnoreCase<String, ColumnConf> confMap = rowMapLstHolder.get(index);
                Map<String, String> map = new LinkedHashMap<String, String>();
                boolean hasModify = false;
                for (String col : columnPkConf.keySet()) {
                    ColumnConf df = confMap.get(col);
                    String strVal = "";
                    if (df != null) {
                        if (df.isModify) {
                            hasModify = true;
                            if (df.value != null) {
                                strVal = String.valueOf(df.value);
                            }
                        } else {
                            if (df.orignValue != null) {
                                strVal = String.valueOf(df.orignValue);
                            }
                        }
                    }
                    // ↓↓↓↓↓↓ 使用真column
                    if (StringUtils.isNotBlank(df.bakupColumnName)) {
                        col = df.bakupColumnName;
                    }
                    // ↑↑↑↑↑↑ 使用真column
                    map.put(col, strVal);
                }
                if (hasModify) {
                    rtnMap.put(index, map);
                }
            }
            return new ArrayList<Map<String, String>>(rtnMap.values());
        }
    }

    private void applyThisValueToAll(String column, String value) {
        mRecordsHandler.applyThisValueToAll(false, column, value);
    }

    private class MySearchHandler {

        List<String> titleLst;
        Map<String, String> titleMap;

        protected Pair<String, List<Pattern>> filterPattern(String filterText) {
            Pattern ptn = Pattern.compile("\\/(.*?)\\/");
            Matcher mth = ptn.matcher(filterText);
            StringBuffer sb = new StringBuffer();
            List<Pattern> lst = new ArrayList<Pattern>();
            while (mth.find()) {
                String temp = mth.group(1);
                Pattern tmpPtn = null;
                if (StringUtils.isNotBlank(temp)) {
                    try {
                        tmpPtn = Pattern.compile(temp, Pattern.CASE_INSENSITIVE);
                    } catch (Exception ex) {
                    }
                }
                if (tmpPtn != null) {
                    lst.add(tmpPtn);
                    mth.appendReplacement(sb, "");
                } else {
                    mth.appendReplacement(sb, mth.group(0));
                }
            }
            mth.appendTail(sb);
            return Pair.of(sb.toString(), lst);
        }

        private boolean handleRow(int rowIdx) {
            JTableUtil util = JTableUtil.newInstance(sqlTable);

            for (int jj = 0; jj < sqlTable.getColumnCount(); jj++) {
                Object val = util.getValueAt(false, rowIdx, jj);
                if (val instanceof String) {
                    String strVal = (String) val;
                    if (textLst != null) {
                        for (String txt : textLst) {
                            if (strVal.toLowerCase().contains(txt)) {
                                return true;
                            }
                        }
                    }
                    if (mthPtn != null) {
                        for (Pattern pp : mthPtn.getRight()) {
                            if (pp != null && pp.matcher(strVal).find()) {
                                return true;
                            }
                        }
                    }
                }
            }

            // Object val = util.getValueAt(false, rowIdx, 1);
            // if (val instanceof String) {
            // String column = (String) val;
            // for (String column2 : rowMap.get().keySet()) {
            // ColumnConf conf = rowMap.get().get(column2);
            // if (StringUtils.isNotBlank(conf.bakupColumnName)) {
            // if (StringUtils.equalsIgnoreCase(conf.bakupColumnName, column)) {
            // return true;
            // }
            // }
            // if (StringUtils.equalsIgnoreCase(column2, column)) {
            // return true;
            // }
            // }
            // }
            return false;
        }

        List<String> textLst;
        Pair<String, List<Pattern>> mthPtn;

        private void process() {
            try {
                Map<Integer, List<Integer>> changeColorMap = new HashMap<Integer, List<Integer>>();
                if (StringUtils.isBlank(sqlSearchText.getText())) {
                    JTableUtil.newInstance(sqlTable).setCellBackgroundColor(Color.green.brighter(), changeColorMap, Arrays.asList(0));
                    return;
                }

                mthPtn = filterPattern(sqlSearchText.getText());

                String text1 = StringUtils.trimToEmpty(mthPtn.getLeft());
                String text = text1.toLowerCase();
                textLst = new ArrayList<String>();
                for (String t : text1.split("\\^", -1)) {
                    t = StringUtils.trimToEmpty(t).toLowerCase();
                    if (StringUtils.isNotBlank(t)) {
                        textLst.add(t);
                    }
                }

                JTableUtil util = JTableUtil.newInstance(sqlTable);
                DefaultTableModel model = util.getModel();

                B: for (int ii = 0; ii < model.getRowCount(); ii++) {
                    List<Integer> lst = new ArrayList<Integer>();
                    changeColorMap.put(ii, lst);

                    /*
                     * A: for (int jj = 0; jj < sqlTable.getColumnCount(); jj++)
                     * { Object val = util.getValueAt(false, ii, jj); if (val
                     * instanceof String) { String strVal = (String) val; for
                     * (String txt : textLst) { if
                     * (strVal.toLowerCase().contains(txt)) { lst.add(jj);
                     * continue A; } } for (Pattern pp : mthPtn.getRight()) { if
                     * (pp != null && pp.matcher(strVal).find()) { lst.add(jj);
                     * continue A; } } } }
                     */

                    Object val = util.getValueAt(false, ii, 1);
                    if (val instanceof String) {
                        String column = (String) val;

                        for (String column2 : rowMap.get().keySet()) {
                            ColumnConf conf = rowMap.get().get(column2);
                            if (StringUtils.isNotBlank(conf.bakupColumnName)) {
                                if (StringUtils.equalsIgnoreCase(conf.bakupColumnName, column)) {
                                    lst.add(1);
                                    continue B;
                                }
                            }

                            if (StringUtils.equalsIgnoreCase(column2, column)) {
                                lst.add(1);
                                continue B;
                            }
                        }
                    }
                }
                JTableUtil.newInstance(sqlTable).setCellBackgroundColor(Color.green.brighter(), changeColorMap, Arrays.asList(0));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        private void doSetbackAction() {
            try {
                JTableUtil util = JTableUtil.newInstance(sqlTable);
                DefaultTableModel model = util.getModel();

                B: for (int ii = 0; ii < model.getRowCount(); ii++) {
                    Boolean check = (Boolean) util.getValueAt(false, ii, 0);
                    if (check != null && check) {
                        Object refColumn = util.getValueAt(false, ii, 1);
                        Object selectVal = util.getValueAt(false, ii, 2);
                        if (refColumn instanceof String) {
                            String column = (String) refColumn;

                            for (String column2 : rowMap.get().keySet()) {
                                ColumnConf conf = rowMap.get().get(column2);
                                if (StringUtils.isNotBlank(conf.bakupColumnName)) {
                                    if (StringUtils.equalsIgnoreCase(conf.bakupColumnName, column)) {
                                        conf.value = selectVal;
                                        continue B;
                                    }
                                }

                                if (StringUtils.equalsIgnoreCase(column2, column)) {
                                    conf.value = selectVal;
                                    continue B;
                                }
                            }
                        }
                    }
                }

                mRecordsHandler.reflush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public void initTable() {
            if (titleMap != null) {
                DefaultTableModel model = JTableUtil.createModel(new int[] { 0 }, new Object[] { "勾選", "項目", "說明" }, new Class[] { Boolean.class, String.class, String.class });
                sqlTable.setModel(model);
                for (String column : titleMap.keySet()) {
                    model.addRow(new Object[] { false, column, StringUtils.trimToEmpty(titleMap.get(column)) });
                }
            }
            if (titleMap != null) {
                Map<String, Object> preferences = new HashMap<String, Object>();
                Map<Integer, Integer> presetColumns = new HashMap<Integer, Integer>();
                presetColumns.put(0, 20);
                preferences.put("presetColumns", presetColumns);
                JTableUtil.setColumnWidths_ByDataContent(sqlTable, preferences, getInsets(), false);
            }
        }
    }

    private class SqlQueryHolder {
        int currentIdx = 0;
        List<Map<String, Object>> queryLst;

        private void _process() {
            sqlQueryCountLbl.setText((currentIdx + 1) + "/" + queryLst.size());
            Map<String, Object> titleMap = queryLst.get(currentIdx);
            Map<String, String> titleMap2 = new LinkedHashMap<String, String>();
            for (String col : titleMap.keySet()) {
                Object val = titleMap.get(col);
                String value = null;
                if (val != null) {
                    value = String.valueOf(val);
                }
                titleMap2.put(col, value);
            }
            mMySearchHandler.titleMap = titleMap2;
            mMySearchHandler.initTable();
        }

        public void next() {
            if (queryLst != null && !queryLst.isEmpty()) {
                if (currentIdx >= queryLst.size() - 1) {
                } else {
                    currentIdx++;
                    _process();
                }
            }
        }

        public void previous() {
            if (queryLst != null && !queryLst.isEmpty()) {
                if (currentIdx <= 0) {
                } else {
                    currentIdx--;
                    _process();
                }
            }
        }

        public void query() {
            try {
                String sql = JCommonUtil.isBlankErrorMsg(sqlTextArea, "必須填入sql");
                queryLst = JdbcDBUtil.queryForList(sql, new Object[0], _parent.getDataSource().getConnection(), true);
                if (queryLst.isEmpty()) {
                    mMySearchHandler.titleMap = Collections.EMPTY_MAP;
                    mMySearchHandler.initTable();
                    JCommonUtil._jOptionPane_showMessageDialog_error("無資料");
                    return;
                }
                currentIdx = 0;
                _process();
            } catch (Exception ex) {
                mMySearchHandler.titleMap = Collections.EMPTY_MAP;
                mMySearchHandler.initTable();
                JCommonUtil.handleException(ex);
            }
        }
    }

    private void exportExcelBtnAction() {
        ExcelUtil_Xls97 xlsUtil = ExcelUtil_Xls97.getInstance();
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("欄位定義");

        int rowIdx = 0;

        HSSFRow titleRow = sheet.createRow(rowIdx++);

        xlsUtil.getCellChk(titleRow, 0).setCellValue("欄位名");
        xlsUtil.getCellChk(titleRow, 1).setCellValue("原始值");
        xlsUtil.getCellChk(titleRow, 2).setCellValue("類型");
        xlsUtil.getCellChk(titleRow, 3).setCellValue("是否為PK");
        xlsUtil.getCellChk(titleRow, 4).setCellValue("是否忽略");
        xlsUtil.getCellChk(titleRow, 5).setCellValue("是否修改");
        xlsUtil.getCellChk(titleRow, 6).setCellValue("當前長度");
        xlsUtil.getCellChk(titleRow, 7).setCellValue("最大長度");
        xlsUtil.getCellChk(titleRow, 8).setCellValue("原始欄位名");
        xlsUtil.getCellChk(titleRow, 9).setCellValue("是否為擴增欄位");

        Set<String> columns = rowMap.get().keySet();
        for (String column : columns) {
            ColumnConf conf = rowMap.get().get(column);

            if (StringUtils.isBlank(tableAndSchemaText.getText()) && conf.isAddFromCustomTableName) {
                continue;
            }

            Row row = xlsUtil.getRowChk(sheet, rowIdx++);
            xlsUtil.getCellChk(row, 0).setCellValue(conf.columnName);
            xlsUtil.getCellChk(row, 1).setCellValue(String.valueOf(conf.orignValue));
            xlsUtil.getCellChk(row, 2).setCellValue(conf.dtype != null ? conf.dtype.name() : "NA");
            xlsUtil.getCellChk(row, 3).setCellValue(conf.isPk ? "Y" : "N");
            xlsUtil.getCellChk(row, 4).setCellValue(conf.isIgnore ? "Y" : "N");
            xlsUtil.getCellChk(row, 5).setCellValue(conf.isModify ? "Y" : "N");
            xlsUtil.getCellChk(row, 6).setCellValue(getCurrentValueLength(conf.orignValue));
            xlsUtil.getCellChk(row, 7).setCellValue(conf.maxLength != null ? String.valueOf(conf.maxLength) : "NA");
            xlsUtil.getCellChk(row, 8).setCellValue(StringUtils.trimToEmpty(conf.bakupColumnName));
            xlsUtil.getCellChk(row, 9).setCellValue(conf.isAddFromCustomTableName ? "Y" : "N");
        }

        xlsUtil.autoCellSize(sheet);

        CellStyleHandler leftChangeCs = ExcelWriter.CellStyleHandler.newInstance(wb.createCellStyle())//
                .setForegroundColor(new HSSFColor.SEA_GREEN());
        leftChangeCs.setSheet(sheet);
        leftChangeCs.applyStyle(0, 0, 0, 9);

        String name = FastDBQueryUI_CrudDlgUI.class.getSimpleName() + "_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss") + ".xls";
        File file = new File(FileUtil.DESKTOP_DIR, name);
        xlsUtil.writeExcelConfirmDlg(file, wb, "欄位定義匯出檔");
    }
}
