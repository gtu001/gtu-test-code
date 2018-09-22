package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.HashMap;
import java.util.HashSet;
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
import org.apache.commons.lang3.Validate;

import gtu.binary.StringUtil4FullChar;
import gtu.collection.MapUtil;
import gtu.db.JdbcDBUtil;
import gtu.db.jdbc.util.DBDateUtil;
import gtu.db.sqlMaker.DbSqlCreater.TableInfo;
import gtu.swing.util.JButtonGroupUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JCommonUtil.HandleDocumentEvent;
import gtu.swing.util.JTableUtil;

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

    private static class ColumnConf {
        String columnName;
        Object value;
        DataType dtype;
        boolean isPk;

        Object[] toArry() {
            return new Object[] { columnName, value, dtype, isPk };
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

        protected void applyDataChange(Object value, JTable table, int row, FastDBQueryUI_CrudDlgUI self) {
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

    public static void newInstance(final Map<String, Object> rowMap, final DataSource dataSource) {
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
                        Map<String, String> map = new HashMap<String, String>();

                        for (String columnName : dialog.rowMap.get().keySet()) {
                            ColumnConf df = dialog.rowMap.get().get(columnName);
                            String value = df.value != null ? String.valueOf(df.value) : null;
                            DataType dtype = df.dtype;
                            boolean isPk = df.isPk;
                            map.put(columnName, value);
                            if (isPk) {
                                pkColumns.add(columnName);
                            }
                        }

                        if (pkColumns.isEmpty()) {
                            Validate.isTrue(false, "勾選where pk!!");
                        }
                        tableInfo.setPkColumns(pkColumns);

                        AbstractButton btn = JButtonGroupUtil.getSelectedButton(dialog.btnGroup);
                        String sql = "";
                        if (btn == dialog.rdbtnInsert) {
                            sql = tableInfo.createInsertSql(map);
                        } else if (btn == dialog.rdbtnUpdate) {
                            sql = tableInfo.createDeleteSql(map);
                        } else if (btn == dialog.rdbtnUpdate) {
                            sql = tableInfo.createUpdateSql(map, map, false);
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
            });
        } catch (Exception e) {
            JCommonUtil.handleException(e);
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
            String value = String.valueOf(tableUtil.getRealValueAt(ii, 1));
            boolean isPk = (Boolean) tableUtil.getRealValueAt(ii, 3);

            DataType dtype = null;
            try {
                Object dtypeVal = tableUtil.getRealValueAt(ii, 2);
                if (dtypeVal instanceof String) {
                    dtype = DataType.valueOf((String) dtypeVal);
                } else {
                    dtype = (DataType) dtypeVal;
                }
            } catch (Exception ex) {
                throw new RuntimeException("欄位:" + columnName + ",未設定正確！ : " + ex.getMessage(), ex);
            }

            ColumnConf df = new ColumnConf();
            if (this.rowMap.get().containsKey(columnName)) {
                df = this.rowMap.get().get(columnName);
            }
            df.columnName = columnName;
            df.value = value;
            df.isPk = isPk;
            df.dtype = dtype;
            this.rowMap.get().put(columnName, df);
        }
    }

    private void searchTextFilter() {
        DefaultTableModel model = initRowTable();
        rowTable.setModel(model);
        String text = StringUtils.trimToEmpty(searchText.getText()).toLowerCase();
        JTableUtil tableUtil = JTableUtil.newInstance(rowTable);
        for (String columnName : rowMap.get().keySet()) {
            ColumnConf df = rowMap.get().get(columnName);
            if (StringUtils.isBlank(text) || //
                    columnName.toLowerCase().contains(text) || //
                    String.valueOf(df.value).contains(text)) {
                model.addRow(df.toArry());
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
                JLabel label = new JLabel("db Type");
                panel.add(label);
            }
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
                tableAndSchemaText.setColumns(20);
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
            btnGroup = JButtonGroupUtil.createRadioButtonGroup(rdbtnInsert, rdbtnUpdate, rdbtnDelete);
        }
        {
            rowTable = new JTable();
            JTableUtil.defaultSetting(rowTable);
            contentPanel.add(JCommonUtil.createScrollComponent(rowTable), BorderLayout.CENTER);
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
    }
}
