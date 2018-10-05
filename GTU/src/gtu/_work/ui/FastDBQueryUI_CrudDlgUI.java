package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import javax.sql.DataSource;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.Validate;

import gtu._work.ui.FastDBQueryUI.FindTextHandler;
import gtu.binary.StringUtil4FullChar;
import gtu.collection.MapUtil;
import gtu.db.JdbcDBUtil;
import gtu.db.jdbc.util.DBDateUtil;
import gtu.db.sqlMaker.DbSqlCreater.TableInfo;
import gtu.string.StringUtilForDb;
import gtu.swing.util.JButtonGroupUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JCommonUtil.HandleDocumentEvent;
import gtu.swing.util.JPopupMenuUtil;
import gtu.swing.util.JTableUtil;
import gtu.swing.util.JTableUtil.JTableUtil_DefaultJMenuItems_Mask;

public class FastDBQueryUI_CrudDlgUI extends JDialog {

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

    private static class ColumnConf {
        String columnName;
        Object value;
        DataType dtype;
        boolean isPk;

        Object[] toArry() {
            Object[] arry = new Object[] { columnName, value, dtype, isPk };
            System.out.println(Arrays.toString(arry));
            return arry;
        }
    }

    private enum DataType {
        varchar(String.class) {
        }, //
        date(java.sql.Date.class) {
            protected void applyDataChange(Object value, JTable table, int row, FastDBQueryUI_CrudDlgUI self) {
                System.out.println("-------" + value + " -> " + value.getClass());
                String val = (String) value;
                java.sql.Date newVal = java.sql.Date.valueOf(val);
                table.setValueAt(newVal, row, 1);
            }
        }, //
        timestamp(java.sql.Timestamp.class) {
            protected void applyDataChange(Object value, JTable table, int row, FastDBQueryUI_CrudDlgUI self) {
                System.out.println("-------" + value + " -> " + value.getClass());
                String val = (String) value;
                java.sql.Timestamp newVal = java.sql.Timestamp.valueOf(val);
                table.setValueAt(newVal, row, 1);
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

        private static DataType isTypeOf(Object value) {
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

    public static FastDBQueryUI_CrudDlgUI newInstance(final Map<String, Object> rowMap, final DataSource dataSource) {
        try {
            final FastDBQueryUI_CrudDlgUI dialog = new FastDBQueryUI_CrudDlgUI();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);

            JTable rowTable = dialog.rowTable;
            DefaultTableModel model = dialog.initRowTable();

            final JTableUtil tableUtil = JTableUtil.newInstance(rowTable);

            Map<String, ColumnConf> rowMapForBackup = MapUtil.createIngoreMap();
            dialog.rowMap.set(rowMapForBackup);
            for (String col : rowMap.keySet()) {
                Object value = rowMap.get(col);

                ColumnConf df = new ColumnConf();
                df.columnName = col;
                df.dtype = DataType.isTypeOf(value);
                df.isPk = false;
                df.value = value;
                rowMapForBackup.put(col, df);

                model.addRow(df.toArry());
            }
            System.out.println("-------------init size : " + dialog.rowMap.get().size());

            dialog.searchText.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    try {
                        dialog.updateJTableToRowMap();
                        dialog.searchTextFilter();
                        JTableUtil.setColumnWidths_Percent(dialog.rowTable, new float[] { 25, 25, 25, 25 });
                    } catch (Exception ex) {
                        JCommonUtil.handleException(ex);
                    }
                }
            });

            dialog.okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        TableInfo tableInfo = new TableInfo();
                        String tableAndSchema = dialog.tableAndSchemaText.getText();
                        JCommonUtil.isBlankErrorMsg(tableAndSchema, "輸入表名稱!");

                        DBDateUtil.DBDateFormat dbDateDateFormat = (DBDateUtil.DBDateFormat) dialog.dbTypeComboBox.getSelectedItem();
                        tableInfo.setDbDateDateFormat(dbDateDateFormat);

                        tableInfo.execute(String.format(" select * from %s where 1!=1 ", tableAndSchema), dataSource.getConnection());

                        Set<String> pkColumns = new HashSet<String>();
                        Set<String> noNullsCol = new HashSet<String>();
                        Set<String> numberCol = new HashSet<String>();
                        Set<String> dateCol = new HashSet<String>();
                        Set<String> timestampCol = new HashSet<String>();

                        Map<String, String> map = new HashMap<String, String>();

                        for (String columnName : dialog.rowMap.get().keySet()) {
                            columnName = StringUtils.trimToEmpty(columnName.toUpperCase());
                            ColumnConf df = dialog.rowMap.get().get(columnName);
                            String value = df.value != null ? String.valueOf(df.value) : null;
                            DataType dtype = df.dtype;
                            boolean isPk = df.isPk;
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

                        AbstractButton btn = JButtonGroupUtil.getSelectedButton(dialog.btnGroup);
                        String sql = "";
                        if (btn == dialog.rdbtnInsert) {
                            sql = tableInfo.createInsertSql(map);
                        } else if (btn == dialog.rdbtnDelete) {
                            sql = tableInfo.createDeleteSql(map);
                        } else if (btn == dialog.rdbtnUpdate) {
                            sql = tableInfo.createUpdateSql(map, map, false);
                        } else if (btn == dialog.rdbtnOthers) {
                            rdbtnOthersAction(tableInfo, map);
                            return;
                        } else {
                            Validate.isTrue(false, "請選sql類型");
                        }

                        String promptLabel = StringUtils.join(StringUtil4FullChar.fixLength(sql, 50), "\n");
                        String realSql = JCommonUtil._jOptionPane_showInputDialog(promptLabel, sql);
                        boolean confirm = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("確定執行 ： " + realSql + " ? ", "確認！！！");
                        if (confirm) {
                            int updateResult = JdbcDBUtil.executeUpdate(realSql, new Object[0], dataSource.getConnection());
                            JCommonUtil._jOptionPane_showMessageDialog_info("update result = " + updateResult);
                        }
                    } catch (Exception e1) {
                        JCommonUtil.handleException(e1);
                    }
                }

                private void rdbtnOthersAction(TableInfo tableInfo, Map<String, String> dataMap) {
                    OthersDBColumnProcess selecting = (OthersDBColumnProcess) JCommonUtil._JOptionPane_showInputDialog("選擇腳本", "選擇腳本", OthersDBColumnProcess.values(), null);
                    String resultStr = "";
                    if (selecting != null) {
                        resultStr = selecting.apply(tableInfo, dataMap);
                    }
                    if (StringUtils.isNotBlank(resultStr)) {
                        JCommonUtil._jOptionPane_showInputDialog(selecting, resultStr);
                    } else {
                        JCommonUtil._jOptionPane_showMessageDialog_error("選擇失敗 : " + selecting);
                    }
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

        DefaultTableModel model = JTableUtil.createModel(false, "column", "value", "data type", "where condition");
        rowTable.setModel(model);

        JTableUtil.setColumnWidths_Percent(rowTable, new float[] { 25, 25, 25, 25 });

        // column = "Data Type"
        TableColumn sportColumn = rowTable.getColumnModel().getColumn(2);
        JComboBox comboBox = new JComboBox();
        for (DataType e : DataType.values()) {
            comboBox.addItem(e);
        }
        sportColumn.setCellEditor(new DefaultCellEditor(comboBox));

        // column = "where condition"
        TableColumn sportColumn4 = rowTable.getColumnModel().getColumn(3);
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
                try {
                    Object value = JTableUtil.newInstance(rowTable).getRealValueAt(row, col);
                    valueStr = value != null ? (value + " -> " + value.getClass()) : "null";
                } catch (Exception ex) {
                    ex.getMessage();
                }
                System.out.println(String.format("## table change -> row[%d], col[%d] -----> %s", row, col, valueStr));

                // 刷新table紀錄！！！ onBlur !!!!!
                updateJTableToRowMap();
            }
        });

        rowTable.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(new JTextField()) {
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

    private void updateJTableToRowMap() {
        JTableUtil tableUtil = JTableUtil.newInstance(rowTable);
        for (int ii = 0; ii < tableUtil.getModel().getRowCount(); ii++) {
            String columnName = (String) tableUtil.getRealValueAt(ii, 0);
            if (columnName == null || StringUtils.isBlank(columnName)) {
                columnName = "";
            }
            String value = String.valueOf(tableUtil.getRealValueAt(ii, 1));
            boolean isPk = false;
            try {
                isPk = (Boolean) tableUtil.getRealValueAt(ii, 3);
            } catch (Exception ex) {
                System.out.println("isPk---ERR--" + ex.getMessage());
            }

            DataType dtype = DataType.varchar;
            try {
                Object dtypeVal = tableUtil.getRealValueAt(ii, 2);
                if (dtypeVal instanceof String) {
                    dtype = DataType.valueOf((String) dtypeVal);
                } else {
                    dtype = (DataType) dtypeVal;
                }
            } catch (Exception ex) {
                System.out.println("dtype---ERR--" + ex.getMessage());
            }

            ColumnConf df = new ColumnConf();
            if (this.rowMap.get().containsKey(columnName)) {
                df = this.rowMap.get().get(columnName);
            }
            df.columnName = columnName;
            df.value = value;
            df.isPk = isPk;
            df.dtype = dtype;
            System.out.println(">>>>>>>>>>>>>>>>>>>" + ReflectionToStringBuilder.toString(df, ToStringStyle.SHORT_PREFIX_STYLE));
            this.rowMap.get().put(columnName, df);
        }
    }

    private void searchTextFilter() {
        DefaultTableModel model = initRowTable();
        rowTable.setModel(model);
        JTableUtil tableUtil = JTableUtil.newInstance(rowTable);

        FindTextHandler finder = new FindTextHandler(searchText, "^");
        boolean allMatch = finder.isAllMatch();

        B: for (String columnName : rowMap.get().keySet()) {
            ColumnConf df = rowMap.get().get(columnName);
            if (allMatch) {
                model.addRow(df.toArry());
                continue;
            }

            for (String text : finder.getArry()) {
                if (StringUtils.isBlank(text) || //
                        columnName.toLowerCase().contains(text) || //
                        String.valueOf(df.value).contains(text)) {
                    model.addRow(df.toArry());
                    continue B;
                }
            }
        }
        System.out.println("-------------searchTextFilter size = " + rowMap.get().size());
    }

    /**
     * Create the dialog.
     */
    public FastDBQueryUI_CrudDlgUI() {
        setBounds(100, 100, 583, 409);
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
                panel.add(dbTypeComboBox);
                DefaultComboBoxModel model = new DefaultComboBoxModel();
                for (DBDateUtil.DBDateFormat e : DBDateUtil.DBDateFormat.values()) {
                    model.addElement(e);
                }
                dbTypeComboBox.setModel(model);
            }
            {
                JLabel label = new JLabel("table");
                panel.add(label);
            }
            {
                tableAndSchemaText = new JTextField();
                tableAndSchemaText.setColumns(10);
                panel.add(tableAndSchemaText);
            }
            {
                rdbtnInsert = new JRadioButton("insert");
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
                rdbtnOthers = new JRadioButton("其他");
                panel.add(rdbtnOthers);
            }
            btnGroup = JButtonGroupUtil.createRadioButtonGroup(rdbtnInsert, rdbtnUpdate, rdbtnDelete, rdbtnOthers);
        }
        {
            rowTable = new JTable();
            JTableUtil.defaultSetting(rowTable);
            contentPanel.add(JCommonUtil.createScrollComponent(rowTable), BorderLayout.CENTER);
            rowTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    List<JMenuItem> menuList = JTableUtil.newInstance(rowTable).getDefaultJMenuItems_Mask(//
                            JTableUtil_DefaultJMenuItems_Mask._加列 | //
                    JTableUtil_DefaultJMenuItems_Mask._加多筆列 | //
                    JTableUtil_DefaultJMenuItems_Mask._移除列 | //
                    JTableUtil_DefaultJMenuItems_Mask._移除所有列 | //
                    JTableUtil_DefaultJMenuItems_Mask._清除已選儲存格 | //
                    JTableUtil_DefaultJMenuItems_Mask._貼上多行記事本 | //
                    JTableUtil_DefaultJMenuItems_Mask._貼上單格記事本 //
                    );
                    JPopupMenuUtil.newInstance(rowTable).addJMenuItem(menuList).applyEvent(e).show();
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
                // getRootPane().setDefaultButton(okButton);
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
        JCommonUtil.setJFrameCenter(this);
        JCommonUtil.defaultToolTipDelay();
    }

    private enum OthersDBColumnProcess {
        VO_SETTER_STR("vo.setter(str)") {
            @Override
            String apply(TableInfo tableInfo, Map<String, String> dataMap) {
                StringBuilder sb = new StringBuilder();
                for (String col : tableInfo.getColumns()) {
                    String param = StringUtilForDb.dbFieldToJava(col);
                    sb.append("vo.set" + StringUtils.capitalize(param) + "(\"" + dataMap.get(col.toUpperCase()) + "\");\n");
                }
                return sb.toString();
            }
        }, //
        VO_SETTER_STR_C("vo.setter(str)完整") {
            @Override
            String apply(TableInfo tableInfo, Map<String, String> dataMap) {
                StringBuilder sb = new StringBuilder();
                for (String col : tableInfo.getColumns()) {
                    String param = StringUtilForDb.dbFieldToJava(col);
                    sb.append("String " + param + " = \"" + dataMap.get(col.toUpperCase()) + "\";\n");
                }
                for (String col : tableInfo.getColumns()) {
                    String param = StringUtilForDb.dbFieldToJava(col);
                    sb.append("vo.set" + StringUtils.capitalize(param) + "(" + param + ");\n");
                }
                return sb.toString();
            }
        }, //
        MAP_PUT_STR("map.put(str)") {
            @Override
            String apply(TableInfo tableInfo, Map<String, String> dataMap) {
                StringBuilder sb = new StringBuilder();
                for (String col : tableInfo.getColumns()) {
                    String param = StringUtilForDb.dbFieldToJava(col);
                    sb.append("map.put(\"" + col.toUpperCase() + "\", \"" + dataMap.get(col.toUpperCase()) + "\");\n");
                }
                return sb.toString();
            }
        }, //
        MAP_PUT_STR_C("map.put(str)完整") {
            @Override
            String apply(TableInfo tableInfo, Map<String, String> dataMap) {
                StringBuilder sb = new StringBuilder();
                for (String col : tableInfo.getColumns()) {
                    String param = StringUtilForDb.dbFieldToJava(col);
                    sb.append("String " + param + " = \"" + dataMap.get(col.toUpperCase()) + "\";\n");
                }
                for (String col : tableInfo.getColumns()) {
                    String param = StringUtilForDb.dbFieldToJava(col);
                    sb.append("map.put(\"" + col.toUpperCase() + "\", " + param + ");\n");
                }
                return sb.toString();
            }
        }, //
        VO_SETTER("vo.setter(orign)") {
            @Override
            String apply(TableInfo tableInfo, Map<String, String> dataMap) {
                StringBuilder sb = new StringBuilder();
                for (String col : tableInfo.getColumns()) {
                    String param = StringUtilForDb.dbFieldToJava(col);
                    String paramVal = getOrignVal(col, tableInfo, dataMap);
                    sb.append("vo.set" + StringUtils.capitalize(param) + "(" + paramVal + ");\n");
                }
                return sb.toString();
            }
        }, //
        VO_SETTER_C("vo.setter(orign)完整") {
            @Override
            String apply(TableInfo tableInfo, Map<String, String> dataMap) {
                StringBuilder sb = new StringBuilder();
                for (String col : tableInfo.getColumns()) {
                    String param = StringUtilForDb.dbFieldToJava(col);
                    String paramType = getOrignType(col, tableInfo, dataMap);
                    String paramVal = getOrignVal(col, tableInfo, dataMap);
                    sb.append(paramType + " " + param + " = " + paramVal + ";\n");
                }
                for (String col : tableInfo.getColumns()) {
                    String param = StringUtilForDb.dbFieldToJava(col);
                    sb.append("vo.set" + StringUtils.capitalize(param) + "(" + param + ");\n");
                }
                return sb.toString();
            }
        }, //
        MAP_PUT("map.put(orign)") {
            @Override
            String apply(TableInfo tableInfo, Map<String, String> dataMap) {
                StringBuilder sb = new StringBuilder();
                for (String col : tableInfo.getColumns()) {
                    String param = StringUtilForDb.dbFieldToJava(col);
                    String paramVal = getOrignVal(col, tableInfo, dataMap);
                    sb.append("map.put(\"" + col.toUpperCase() + "\", " + paramVal + ");\n");
                }
                return sb.toString();
            }
        }, //
        MAP_PUT_C("map.put(orign)完整") {
            @Override
            String apply(TableInfo tableInfo, Map<String, String> dataMap) {
                StringBuilder sb = new StringBuilder();
                for (String col : tableInfo.getColumns()) {
                    String param = StringUtilForDb.dbFieldToJava(col);
                    String paramType = getOrignType(col, tableInfo, dataMap);
                    String paramVal = getOrignVal(col, tableInfo, dataMap);
                    sb.append(paramType + " " + param + " = " + paramVal + ";\n");
                }
                for (String col : tableInfo.getColumns()) {
                    String param = StringUtilForDb.dbFieldToJava(col);
                    sb.append("map.put(\"" + col.toUpperCase() + "\", " + param + ");\n");
                }
                return sb.toString();
            }
        }, //
        VO_GET_AND_SET("vo.get&set(orign)完整") {
            @Override
            String apply(TableInfo tableInfo, Map<String, String> dataMap) {
                StringBuilder sb = new StringBuilder();
                for (String col : tableInfo.getColumns()) {
                    String param = StringUtilForDb.dbFieldToJava(col);
                    String paramType = getOrignType(col, tableInfo, dataMap);
                    String paramVal = getOrignVal(col, tableInfo, dataMap);
                    sb.append(paramType + " " + param + " = " + paramVal + ";\n");
                }
                for (String col : tableInfo.getColumns()) {
                    String param = StringUtilForDb.dbFieldToJava(col);
                    String paramType = getOrignType(col, tableInfo, dataMap);
                    String paramVal = getOrignVal(col, tableInfo, dataMap);
                    sb.append(paramType + " " + param + " = " + "vo.get" + StringUtils.capitalize(param) + "()" + ";\n");
                }
                for (String col : tableInfo.getColumns()) {
                    String param = StringUtilForDb.dbFieldToJava(col);
                    sb.append("vo.set" + StringUtils.capitalize(param) + "(" + param + ");\n");
                }
                return sb.toString();
            }
        },//
        ;
        final String label;

        OthersDBColumnProcess(String label) {
            this.label = label;
        }

        String getOrignVal(String col, TableInfo tableInfo, Map<String, String> dataMap) {
            col = col.toUpperCase();
            if (tableInfo.getDateCol().contains(col)) {
                return "java.sql.Date.valueOf(\"" + dataMap.get(col) + "\")";
            } else if (tableInfo.getTimestampCol().contains(col)) {
                return "java.sql.Timestamp.valueOf(\"" + dataMap.get(col) + "\")";
            } else if (tableInfo.getNumberCol().contains(col)) {
                return dataMap.get(col);
            } else {
                return "\"" + dataMap.get(col) + "\"";
            }
        }

        String getOrignType(String col, TableInfo tableInfo, Map<String, String> dataMap) {
            col = col.toUpperCase();
            if (tableInfo.getDateCol().contains(col)) {
                return "java.sql.Date";
            } else if (tableInfo.getTimestampCol().contains(col)) {
                return "java.sql.Timestamp";
            } else if (tableInfo.getNumberCol().contains(col)) {
                boolean isDouble = false;
                if (dataMap.containsKey(col)) {
                    isDouble = dataMap.get(col).matches("[\\-]?\\d+\\.\\d+");
                }
                return isDouble ? "Double" : "Integer";
            } else {
                return "String";
            }
        }

        abstract String apply(TableInfo tableInfo, Map<String, String> dataMap);

        public String toString() {
            return this.label;
        }
    }
}
