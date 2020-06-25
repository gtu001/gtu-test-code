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
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;

import gtu._work.ui.FastDBQueryUI.S2T_And_T2S_EventHandler;
import gtu._work.ui.FastDBQueryUI_ColumnSearchFilter.FindTextHandler;
import gtu.db.jdbc.util.DBDateUtil;
import gtu.db.sqlMaker.DbSqlCreater.FieldInfo4DbSqlCreater;
import gtu.db.sqlMaker.DbSqlCreater.TableInfo;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JCommonUtil.HandleDocumentEvent;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JMouseEventUtil;
import gtu.swing.util.JPopupMenuUtil;
import gtu.swing.util.JTableUtil;
import gtu.swing.util.KeyEventExecuteHandler;

public class FastDBQueryUI_TwoTableDlgUI extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTable rowTable;
    private JButton okButton;
    private JTextField tableAndSchemaText;
    private JTextField searchText;
    private FastDBQueryUI _parent;
    private JFrameRGBColorPanel jFrameRGBColorPanel;

    private JLabel lbltable;
    private JTextField tableAndSchemaText2;
    private ColumnAllConfig mColumnAllConfig;

    public static FastDBQueryUI_TwoTableDlgUI newInstance(String firstTableName, final ActionListener onCloseListener, final FastDBQueryUI _parent) {
        try {
            final FastDBQueryUI_TwoTableDlgUI dialog = new FastDBQueryUI_TwoTableDlgUI(_parent);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);

            JTable rowTable = dialog.rowTable;
            DefaultTableModel model = dialog.initRowTable();

            final JTableUtil tableUtil = JTableUtil.newInstance(rowTable);

            JTableUtil.newInstance(rowTable).setRowHeightByFontSize();

            dialog.searchText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
                @Override
                public void process(DocumentEvent event) {
                    try {
                        dialog.searchTextFilter();
                    } catch (Exception ex) {
                        JCommonUtil.handleException(ex);
                    }
                }
            }));

            dialog.okButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                    } catch (Exception e1) {
                        JCommonUtil.handleException(e1);
                    }
                }
            });

            dialog.addWindowListener(new WindowAdapter() {
                public void windowClosed(WindowEvent e) {
                    if (onCloseListener != null) {
                        onCloseListener.actionPerformed(new ActionEvent(dialog, -1, "close"));
                    }
                }

                public void windowClosing(WindowEvent e) {
                }
            });

            if (StringUtils.isNotBlank(firstTableName)) {
                dialog.tableAndSchemaText.setText(StringUtils.trimToEmpty(firstTableName));
                dialog.tableAndSchemaText_focusLost_action(dialog.tableAndSchemaText, 1);
            }

            return dialog;
        } catch (Exception e) {
            throw new RuntimeException("FastDBQueryUI_CrudDlgUI ERR : " + e.getMessage(), e);
        }
    }

    private class ColumnConf {
        boolean notNull1;
        String column1;
        int length1;
        String type1;
        boolean notNull2;
        String column2;
        int length2;
        String type2;

        Object[] toArry() {
            return new Object[] { notNull1, column1, length1, type1, notNull2, column2, length2, type2 };
        }
    }

    private enum ColumnOrder {
        NotNull1("非空", 3), //
        Column1("表一欄位", 30), //
        Length1("長度", 5), //
        Type1("類型", 10), //
        NotNull2("非空", 3), //
        Column2("表二欄位", 30), //
        Length2("長度", 5), //
        Type2("類型", 10),//
        ;

        String label;
        float width;

        ColumnOrder(String lbl2, float width) {
            this.label = lbl2;
            this.width = width;
        }

        static String[] getColumns() {
            List<String> lst = new ArrayList<String>();
            for (ColumnOrder e : ColumnOrder.values()) {
                lst.add(e.label);
            }
            return lst.toArray(new String[0]);
        }

        static float[] getWidths() {
            List<Float> lst = new ArrayList<Float>();
            for (ColumnOrder e : ColumnOrder.values()) {
                lst.add(e.width);
            }
            return ArrayUtils.toPrimitive(lst.toArray(new Float[0]));
        }
    }

    private DefaultTableModel initRowTable() {
        final JTableUtil tableUtil = JTableUtil.newInstance(rowTable);
        JTableUtil.defaultSetting(rowTable);

        DefaultTableModel model = JTableUtil.createModel(false, ColumnOrder.getColumns());
        rowTable.setModel(model);

        JTableUtil.setColumnWidths_Percent(rowTable, ColumnOrder.getWidths());

        JTableUtil.setColumnAlign(rowTable, ColumnOrder.Type1.ordinal(), JLabel.RIGHT);
        JTableUtil.setColumnAlign(rowTable, ColumnOrder.Type2.ordinal(), JLabel.RIGHT);
        JTableUtil.setColumnAlign(rowTable, ColumnOrder.Length1.ordinal(), JLabel.RIGHT);
        JTableUtil.setColumnAlign(rowTable, ColumnOrder.Length2.ordinal(), JLabel.RIGHT);

        JTableUtil.newInstance(rowTable).setColumnColor_byCondition(ColumnOrder.Length1.ordinal(), new JTableUtil.TableColorDef() {
            public Pair<Color, Color> getTableColour(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JTableUtil util = JTableUtil.newInstance(rowTable);
                Integer v1 = (Integer) util.getRealValueAt(row, ColumnOrder.Length1.ordinal());
                Integer v2 = (Integer) util.getRealValueAt(row, ColumnOrder.Length2.ordinal());
                if (v1 != v2) {
                    return Pair.of(Color.YELLOW, null);
                }
                return null;
            }
        });

        JTableUtil.newInstance(rowTable).setColumnColor_byCondition(ColumnOrder.Length2.ordinal(), new JTableUtil.TableColorDef() {
            public Pair<Color, Color> getTableColour(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JTableUtil util = JTableUtil.newInstance(rowTable);
                Integer v1 = (Integer) util.getRealValueAt(row, ColumnOrder.Length1.ordinal());
                Integer v2 = (Integer) util.getRealValueAt(row, ColumnOrder.Length2.ordinal());
                if (v1 != v2) {
                    return Pair.of(Color.YELLOW, null);
                }
                return null;
            }
        });

        JTableUtil.newInstance(rowTable).setColumnColor_byCondition(ColumnOrder.Type1.ordinal(), new JTableUtil.TableColorDef() {
            public Pair<Color, Color> getTableColour(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JTableUtil util = JTableUtil.newInstance(rowTable);
                String v1 = (String) util.getRealValueAt(row, ColumnOrder.Type1.ordinal());
                String v2 = (String) util.getRealValueAt(row, ColumnOrder.Type2.ordinal());
                if (!StringUtils.equalsIgnoreCase(v1, v2)) {
                    return Pair.of(Color.YELLOW, null);
                }
                return null;
            }
        });

        JTableUtil.newInstance(rowTable).setColumnColor_byCondition(ColumnOrder.Type2.ordinal(), new JTableUtil.TableColorDef() {
            public Pair<Color, Color> getTableColour(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JTableUtil util = JTableUtil.newInstance(rowTable);
                String v1 = (String) util.getRealValueAt(row, ColumnOrder.Type1.ordinal());
                String v2 = (String) util.getRealValueAt(row, ColumnOrder.Type2.ordinal());
                if (!StringUtils.equalsIgnoreCase(v1, v2)) {
                    return Pair.of(Color.YELLOW, null);
                }
                return null;
            }
        });

        JTableUtil.newInstance(rowTable).setColumnColor_byCondition(ColumnOrder.Column1.ordinal(), new JTableUtil.TableColorDef() {
            public Pair<Color, Color> getTableColour(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JTableUtil util = JTableUtil.newInstance(rowTable);
                String v1 = (String) util.getRealValueAt(row, ColumnOrder.Column1.ordinal());
                String v2 = (String) util.getRealValueAt(row, ColumnOrder.Column2.ordinal());
                if (!StringUtils.equalsIgnoreCase(v1, v2)) {
                    return Pair.of(Color.YELLOW, null);
                }
                return null;
            }
        });

        JTableUtil.newInstance(rowTable).setColumnColor_byCondition(ColumnOrder.Column2.ordinal(), new JTableUtil.TableColorDef() {
            public Pair<Color, Color> getTableColour(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JTableUtil util = JTableUtil.newInstance(rowTable);
                String v1 = (String) util.getRealValueAt(row, ColumnOrder.Column1.ordinal());
                String v2 = (String) util.getRealValueAt(row, ColumnOrder.Column2.ordinal());
                if (!StringUtils.equalsIgnoreCase(v1, v2)) {
                    return Pair.of(Color.YELLOW, null);
                }
                return null;
            }
        });
        return tableUtil.getModel();
    }

    private class ColumnAllConfig {
        TableInfo tableInfo1;
        TableInfo tableInfo2;
        List<ColumnConf> columnRows = new ArrayList<ColumnConf>();

        private FieldInfo4DbSqlCreater getConf(String column) {
            for (String column2 : tableInfo2.getColumns()) {
                if (StringUtils.equals(column2, column)) {
                    return tableInfo2.getColumnInfo().get(column2);
                }
            }
            return null;
        }

        private void processTable() {
            if (tableInfo1 == null && tableInfo2 == null) {
                columnRows = new ArrayList<ColumnConf>();
                return;
            }

            columnRows = new ArrayList<ColumnConf>();

            List<String> sameColumns = new ArrayList<String>();
            for (String column : tableInfo1.getColumns()) {
                ColumnConf df = new ColumnConf();
                FieldInfo4DbSqlCreater column1 = tableInfo1.getColumnInfo().get(column);
                FieldInfo4DbSqlCreater column2 = null;
                if (tableInfo2 != null) {
                    column2 = this.getConf(column);
                    if (column2 != null) {
                        sameColumns.add(column2.getColumnLabel());
                    }
                }
                df.notNull1 = column1.isNullableBool();
                df.column1 = column1.getColumnLabel();
                df.length1 = column1.getColumnDisplaySize();
                df.type1 = column1.getColumnTypeName();
                if (column2 != null) {
                    df.notNull2 = column1.isNullableBool();
                    df.column2 = column1.getColumnLabel();
                    df.length2 = column1.getColumnDisplaySize();
                    df.type2 = column1.getColumnTypeName();
                }
                columnRows.add(df);
            }
            if (tableInfo2 != null) {
                for (String column : tableInfo2.getColumns()) {
                    if (sameColumns.contains(column)) {
                        continue;
                    }
                    ColumnConf df = new ColumnConf();
                    FieldInfo4DbSqlCreater column1 = tableInfo2.getColumnInfo().get(column);
                    df.notNull2 = column1.isNullableBool();
                    df.column2 = column1.getColumnLabel();
                    df.length2 = column1.getColumnDisplaySize();
                    df.type2 = column1.getColumnTypeName();
                    columnRows.add(df);
                }
            }
        }
    }

    // 自動設定pk
    private void tableAndSchemaText_focusLost_action(JTextField tableAndSchemaText, int idx) {
        try {
            TableInfo tableInfo = new TableInfo();

            String tableAndSchema = StringUtils.trimToEmpty(tableAndSchemaText.getText());
            if (StringUtils.isNotBlank(tableAndSchema)) {
                JCommonUtil.isBlankErrorMsg(tableAndSchema, "輸入表名稱!");
                tableInfo.execute(String.format(" select * from %s where 1!=1 ", tableAndSchema), _parent.getDataSource().getConnection());
                if (StringUtils.isBlank(tableInfo.getTableName())) {
                    tableInfo.setTableName(tableAndSchema);
                }
            }

            if (mColumnAllConfig == null) {
                mColumnAllConfig = new ColumnAllConfig();
            }

            if (idx == 1) {
                mColumnAllConfig.tableInfo1 = tableInfo;
            } else {
                mColumnAllConfig.tableInfo2 = tableInfo;
            }

            mColumnAllConfig.processTable();

            // 刷新table
            searchTextFilter();
        } catch (Exception e) {
            JCommonUtil.handleException(e);
        }
    }

    private void searchTextFilter() {
        DefaultTableModel model = initRowTable();
        rowTable.setModel(model);
        JTableUtil tableUtil = JTableUtil.newInstance(rowTable);

        FindTextHandler finder = new FindTextHandler(searchText.getText(), "^");
        boolean allMatch = finder.isAllMatch();

        if (mColumnAllConfig == null) {
            mColumnAllConfig = new ColumnAllConfig();
        }

        B: for (ColumnConf df : mColumnAllConfig.columnRows) {
            if (allMatch) {
                model.addRow(df.toArry());
                continue;
            }

            for (String text : finder.getArry()) {
                if (StringUtils.isBlank(text) || //
                        String.valueOf(df.column1).toLowerCase().contains(text) || //
                        String.valueOf(df.column2).toLowerCase().contains(text)) {
                    model.addRow(df.toArry());
                    continue B;
                }
            }
        }
        JTableUtil.newInstance(rowTable).setRowHeightByFontSize();
        System.out.println("-------------searchTextFilter size = " + mColumnAllConfig.columnRows.size());
    }

    // 邏輯在上=======================================================================================================

    /**
     * Create the dialog.
     */
    public FastDBQueryUI_TwoTableDlgUI(final FastDBQueryUI _parent) {
        this._parent = _parent;

        this.setTitle("兩個Table處理");

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
            }
            {
                JLabel label = new JLabel("table");
                panel.add(label);
            }
            {
                tableAndSchemaText = new JTextField();
                tableAndSchemaText.setColumns(10);
                panel.add(tableAndSchemaText);
                {
                    lbltable = new JLabel("<-->table");
                    panel.add(lbltable);
                }
                {
                    tableAndSchemaText2 = new JTextField();
                    tableAndSchemaText2.setColumns(10);
                    panel.add(tableAndSchemaText2);

                    tableAndSchemaText2.addFocusListener(new FocusAdapter() {

                        @Override
                        public void focusLost(FocusEvent e) {
                            tableAndSchemaText_focusLost_action(tableAndSchemaText2, 2);
                        }
                    });
                }
                tableAndSchemaText.addFocusListener(new FocusAdapter() {

                    @Override
                    public void focusLost(FocusEvent e) {
                        tableAndSchemaText_focusLost_action(tableAndSchemaText, 1);
                    }
                });
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
                        // if (col == ColumnOrderDef.columnName.ordinal()) {
                        // String column = (String)
                        // JTableUtil.newInstance(rowTable).getValueAt(false,
                        // row, ColumnOrderDef.columnName.ordinal());
                        // rowTable.setToolTipText(_parent.mTableColumnDefTextHandler.getChinese(column));
                        // return;
                        // }
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
                    }
                }
            });
        }
        {
            addWindowListener(new WindowAdapter() {

                public void windowClosed(WindowEvent e) {
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
                        FastDBQueryUI_TwoTableDlgUI.this.dispose();
                    }
                });
            }
        }

        JCommonUtil.setJFrameCenter(this);
        JCommonUtil.defaultToolTipDelay();
        jFrameRGBColorPanel = new JFrameRGBColorPanel(this);
        jFrameRGBColorPanel.setStop(_parent.getjFrameRGBColorPanel().isStop());
    }
}
