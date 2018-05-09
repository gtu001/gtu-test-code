/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.swing.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import gtu.clipboard.ClipboardUtil;

public class JTableUtil {

    JTable table;

    public JTableUtil(JTable table) {
        this.table = table;
    }

    public void debugShowData() {
        List<Integer> xlist = getTableColumnModelIndex();
        List<Integer> ylist = getTableColumnModelIndex_realRowIndex();
        System.out.println("X=>" + xlist);
        System.out.println("Y=>" + ylist);
        for (int yy = 0; yy < ylist.size(); yy++) {
            int y = ylist.get(yy);
            System.out.format("y[%d]", y);
            for (int xx = 0; xx < xlist.size(); xx++) {
                int x = xlist.get(xx);
                System.out.format("\tx%d[%s]", x, table.getValueAt(y, x));
            }
            System.out.println();
        }
    }

    /**
     * @param table
     */
    public static void defaultSetting(JTable table) {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setAutoscrolls(true);
        table.setAutoCreateRowSorter(true);
        table.setUpdateSelectionOnSort(true);
        table.setAutoCreateColumnsFromModel(true);
        table.setColumnSelectionAllowed(true);
        table.getTableHeader().setAutoscrolls(true);
    }

    /**
     * @param table
     */
    public static void defaultSetting_AutoResize(JTable table) {
        defaultSetting(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    /**
     * @param table
     */
    public static void defaultSetting_Indicate(JTable table, int autoResizeMode) {
        defaultSetting(table);
        table.setAutoResizeMode(autoResizeMode);
    }

    /**
     * 若第一列為checkbox
     */
    public void columnOneIsCheckbox() {
        // 定義column name
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        Vector<String> vec = new Vector<String>();
        for (int ii = 0; ii < model.getColumnCount(); ii++) {
            if (ii == 0) {
                vec.add("select");
            } else {
                vec.add("column" + ii);
            }
        }
        model.setColumnIdentifiers(vec);

        // 定義checkbox
        class MyCheckBoxRenderer extends JCheckBox implements TableCellRenderer {
            private static final long serialVersionUID = 1L;

            public MyCheckBoxRenderer() {
                this.setBorderPainted(true);
            }

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                return this;
            }
        }
        final MyCheckBoxRenderer check = new MyCheckBoxRenderer();
        table.getColumn("select").setHeaderRenderer(check);

        // 設定checkbox效果
        table.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (table.getColumnModel().getColumnIndexAtX(e.getX()) == 0) {// 如果點擊的是第0列，即checkbox這一列
                    JCheckBox checkbox = (JCheckBox) check;
                    boolean b = !check.isSelected();
                    check.setSelected(b);
                    table.getTableHeader().repaint();
                    for (int i = 0; i < table.getRowCount(); i++) {
                        table.getModel().setValueAt(b, i, 0);// 把這一列都設成和表頭一样
                    }
                }
            }
        });
    }

    /**
     * 設定某欄為button
     */
    public void columnIsButton(String columnName) {
        TableColumn columConfig = table.getColumn(columnName);
        System.out.println("columnIsButton " + columConfig.getHeaderValue());
        columConfig.setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value == null || !(value instanceof JButton)) {
                    throw new RuntimeException("無法被設定為Button : " + value);
                }
                JButton button = (JButton) value;
                return button;
            }
        });
        boolean findOk = false;
        for (int ii = 0; ii < table.getMouseListeners().length; ii++) {
            if (table.getMouseListeners()[ii] instanceof _ColumnButtonMouseAdapter) {
                findOk = true;
                break;
            }
        }
        if (findOk == false) {
            table.addMouseListener(new _ColumnButtonMouseAdapter(table));
        }
    }

    public void setRowHeight(int rowPos, int height) {
        table.setRowHeight(rowPos, height);
    }

    public void columnIsComponent(int index, JComponent component) {
        TableColumn comboCol1 = table.getColumnModel().getColumn(index);
        if (component instanceof JComboBox) {
            comboCol1.setCellEditor(new DefaultCellEditor((JComboBox) component));
        } else if (component instanceof JCheckBox) {
            comboCol1.setCellEditor(new DefaultCellEditor((JCheckBox) component));
        } else if (component instanceof JTextField) {
            comboCol1.setCellEditor(new DefaultCellEditor((JTextField) component));
        } else {
            throw new RuntimeException("必須是JComboBox, JCheckBox, JTextField");
        }
    }

    public void defaultToolTipText(MouseEvent event) {
        ToolTipManager toolTipManager = ToolTipManager.sharedInstance();
        toolTipManager.setInitialDelay(0);
        String content = this.getSelectedValue() == null ? "empty" : this.getSelectedValue().toString();
        if (event.getButton() == 1) {
            table.setToolTipText(content);
        }
    }

    public static DefaultTableModel createModel(final boolean readonly, Object... header) {
        DefaultTableModel model = new DefaultTableModel(new Object[][] {}, header) {
            private static final long serialVersionUID = 1L;

            // 設定column class
            @Override
            public Class<?> getColumnClass(int c) {
                try {
                    Object value = getValueAt(0, c);
                    if (value != null) {
                        return value.getClass();
                    } else {
                        return super.getClass();
                    }
                } catch (Exception ex) {
                    return super.getClass();
                }
            }

            // 設定可否編輯
            public boolean isCellEditable(int row, int column) {
                if (readonly) {
                    return false;
                } else {
                    return super.isCellEditable(row, column);
                }
            }
        };
        return model;
    }

    public static DefaultTableModel createModel(final int[] editableColumns, Object... header) {
        DefaultTableModel model = new DefaultTableModel(new Object[][] {}, header) {
            private static final long serialVersionUID = 1L;

            // 設定column class
            @Override
            public Class<?> getColumnClass(int c) {
                try {
                    Object value = getValueAt(0, c);
                    if (value != null) {
                        return value.getClass();
                    } else {
                        return super.getClass();
                    }
                } catch (Exception ex) {
                    return super.getClass();
                }
            }

            // 設定可否編輯
            public boolean isCellEditable(int row, int column) {
                if (!ArrayUtils.contains(editableColumns, column)) {
                    return false;
                } else {
                    return super.isCellEditable(row, column);
                }
            }
        };
        return model;
    }

    /**
     * 設定最大寬度 , 若設-1則無限制
     * 
     * @param maxWidth
     * @return
     */
    public JTableUtil setMaxWidth(int... maxWidth) {
        DefaultTableColumnModel model1 = new DefaultTableColumnModel();
        TableColumn tableColumn = null;
        for (int ii = 0; ii < maxWidth.length; ii++) {
            tableColumn = new TableColumn();
            tableColumn.setHeaderValue(table.getModel().getColumnName(ii));
            if (maxWidth[ii] != -1) {
                tableColumn.setMaxWidth(maxWidth[ii]);
                tableColumn.setPreferredWidth(maxWidth[ii]);
            }
            tableColumn.setModelIndex(ii);
            tableColumn.setResizable(true);
            model1.addColumn(tableColumn);
        }
        table.setColumnModel(model1);
        return this;
    }

    /**
     * 可垂直與橫向卷軸的Scroll
     */
    public static JScrollPane getScrollPane(JTable myTable) {
        JScrollPane scroll = new JScrollPane(myTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        myTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        return scroll;
    }

    public static JTableUtil newInstance(JTable table) {
        return new JTableUtil(table);
    }

    /**
     * if all column's cell is empty , hidden this column
     */
    public void hiddenAllEmptyColumn() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        Set<String> colName = new HashSet<String>();
        for (int col = 0; col < model.getColumnCount(); col++) {
            for (int row = 0; row < model.getRowCount(); row++) {
                if (StringUtils.isNotBlank((String) model.getValueAt(row, col))) {
                    colName.add(model.getColumnName(col));
                    break;
                }
            }
        }
        for (Enumeration<TableColumn> enu = table.getColumnModel().getColumns(); enu.hasMoreElements();) {
            TableColumn column = enu.nextElement();
            if (!colName.contains(column.getHeaderValue())) {
                table.removeColumn(column);
            }
        }
    }

    public void hiddenColumn(String columnTitle) {
        TableColumnManager tcm = new TableColumnManager(table);
        tcm.hideColumn(columnTitle);
    }

    public void showColumnByHeaderValue(Object... title) {
        List<TableColumn> list = new ArrayList<TableColumn>();
        TableColumnModel columnModel = table.getColumnModel();
        for (int ii = 0; ii < columnModel.getColumnCount(); ii++) {
            if (!ArrayUtils.contains(title, columnModel.getColumn(ii).getHeaderValue())) {
                list.add(columnModel.getColumn(ii));
            }
        }
        for (TableColumn col : list) {
            table.removeColumn(col);
        }
    }

    public void sizeWidthToFitHeader() {
        for (Enumeration<TableColumn> enu = table.getColumnModel().getColumns(); enu.hasMoreElements();) {
            TableColumn column = enu.nextElement();
            if (column.getHeaderRenderer() == null) {
                column.setHeaderRenderer(DEFAULT_TABLE_CELL_RENDERER);
            }
            column.sizeWidthToFit();
        }
    }

    static final DefaultTableCellRenderer DEFAULT_TABLE_CELL_RENDERER = createDefaultTableCellRenderer();

    private static DefaultTableCellRenderer createDefaultTableCellRenderer() {
        DefaultTableCellRenderer label = new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (table != null) {
                    JTableHeader header = table.getTableHeader();
                    if (header != null) {
                        setForeground(header.getForeground());
                        setBackground(header.getBackground());
                        setFont(header.getFont());
                    }
                }

                setText((value == null) ? "" : value.toString());
                setBorder(UIManager.getBorder("TableHeader.cellBorder"));
                return this;
            }
        };
        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
    }

    public DefaultTableModel getModel() {
        if (table.getRowSorter() == null) {
            return (DefaultTableModel) table.getModel();
        }
        return (DefaultTableModel) table.getRowSorter().getModel();
    }

    /**
     * 已修正 pos
     */
    public Object getSelectedValue() {
        int row = JTableUtil.getRealRowPos(getSelectedRow(), table);
        int col = JTableUtil.getRealColumnPos(getSelectedColumn(), table);
        if (row == -1 || col == -1) {
            return null;
        }
        return getModel().getValueAt(row, col);
    }

    /**
     * 已修正 pos
     */
    public JTableUtil setValueAtSelectedCell(Object value) {
        int row = JTableUtil.getRealRowPos(getSelectedRow(), table);
        int col = JTableUtil.getRealColumnPos(getSelectedColumn(), table);
        System.out.format("setValueAtSelectedCell - row : %d, col : %d\n", row, col);
        if (row == -1 || col == -1) {
            return this;
        }
        getModel().setValueAt(value, row, col);
        return this;
    }

    /**
     * 未變更原始col
     */
    public int getSelectedColumn() {
        return table.getSelectedColumn();
    }

    /**
     * 已修正col (記得傳入未修正 pos , 否則反而錯誤)
     */
    public static int getRealColumnPos(int colPos, JTable table) {
        System.out.println("getRealColumnPos ==> colPos == " + colPos);
        return table.convertColumnIndexToModel(colPos);
    }

    /**
     * 已修正 row (記得傳入未修正 pos , 否則反而錯誤)
     */
    public static int getRealRowPos(int rowPos, JTable table) {
        if (rowPos == -1) {
            System.out.println("getRealRowPos => " + rowPos);
            return rowPos;
        }
        if (table.getRowSorter() == null) {
            System.out.println("getRealRowPos[no sort] => " + rowPos);
            return rowPos;
        }
        int fixRowPos = table.getRowSorter().convertRowIndexToModel(rowPos);
        System.out.println(String.format("getRealRowPos[fix] => before[%d], after[%d]", rowPos, fixRowPos));
        return fixRowPos;
    }

    /**
     * 未變更原始row
     */
    public int getSelectedRow() {
        return table.getSelectedRow();
    }

    /**
     * (記得傳入未修正 pos , 否則反而錯誤)
     */
    public Object getRealValueAt(int rowPos, int colPos) {
        rowPos = getRealRowPos(rowPos, table);
        colPos = getRealColumnPos(colPos, table);
        return getModel().getValueAt(rowPos, colPos);
    }

    /**
     * 取得已修正row arry
     */
    public int[] getSelectedRows() {
        int[] rowPos = table.getSelectedRows();
        if (table.getRowSorter() == null) {
            return rowPos;
        }
        int[] row2 = new int[rowPos.length];
        for (int ii = 0; ii < rowPos.length; ii++) {
            row2[ii] = JTableUtil.getRealRowPos(rowPos[ii], table);
        }
        return row2;
    }

    public void addRow(Object[] data) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.addRow(data);
    }

    /**
     * 取得真實被選擇的欄
     */
    public List<Integer> getTableColumnModelIndex_selectedColumns() {
        int[] tabSelectColumns = table.getSelectedColumns();
        List<Integer> list = new ArrayList<Integer>();
        for (int col : tabSelectColumns) {
            list.add(col);
        }
        return list;
    }

    /**
     * 取得Table列Row的真實index
     */
    public List<Integer> getTableColumnModelIndex_realRowIndex() {
        List<Integer> rowList = new ArrayList<Integer>();
        for (int ii = 0; ii < getModel().getRowCount(); ii++) {
            if (table.getRowSorter() != null) {
                rowList.add(table.getRowSorter().convertRowIndexToModel(ii));
            } else {
                rowList.add(ii);
            }
        }
        return rowList;
    }

    /**
     * 取得Table的column的index
     */
    public List<Integer> getTableColumnModelIndex() {
        List<Integer> list = new ArrayList<Integer>();
        Map<Object, TableColumn> tableCModel = getTableColumnModel();
        for (Object key : tableCModel.keySet()) {
            list.add(tableCModel.get(key).getModelIndex());
        }
        return list;
    }

    /**
     * 取得Table的column的 定義檔 key=欄名,value=定義檔
     */
    public Map<Object, TableColumn> getTableColumnModel() {
        TableColumn col = null;
        TableColumn clone = null;
        Map<Object, TableColumn> tableCModel = new LinkedHashMap<Object, TableColumn>();
        for (int ii = 0; ii < table.getColumnModel().getColumnCount(); ii++) {
            col = table.getColumnModel().getColumn(ii);
            clone = new TableColumn();
            clone.setModelIndex(-1);
            clone.setHeaderValue(col.getHeaderValue());
            tableCModel.put(col.getHeaderValue(), clone);
        }
        TableColumnModel tableColumnModel = table.getTableHeader().getColumnModel();
        for (int ii = 0; ii < tableColumnModel.getColumnCount(); ii++) {
            for (Object key : tableCModel.keySet()) {
                int index = tableColumnModel.getColumnIndex(key);
                tableCModel.get(key).setModelIndex(index);
                // System.out.println(key + "..." + index);
            }
        }
        return tableCModel;
    }

    public <T> KeyAdapter defaultKeyAdapter() {
        return new KeyAdapter() {
            T temp = null;

            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
                    pasteFromObject_singleValueToSelectedCell(null);
                }
                if (evt.getModifiers() == KeyEvent.CTRL_MASK && evt.getKeyCode() == KeyEvent.VK_C) {
                    temp = (T) getSelectedValue();
                }
                if (evt.getModifiers() == KeyEvent.CTRL_MASK && evt.getKeyCode() == KeyEvent.VK_V) {
                    pasteFromObject_singleValueToSelectedCell(temp);
                }
                if (evt.getModifiers() == KeyEvent.CTRL_MASK && evt.getKeyCode() == KeyEvent.VK_X) {
                    temp = (T) getSelectedValue();
                    setValueAtSelectedCell(null);
                }
            }
        };
    }

    // XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    // paste function
    public JTableUtil pasteFromClipboard_multiRowData(boolean autoRowExtendReadch) {
        return pasteFromClipboard_multiRowData(autoRowExtendReadch, defaultTransformer);
    }

    static Transformer defaultTransformer = new Transformer() {
        public Object transform(Object paramObject) {
            return paramObject;
        }
    };

    @SuppressWarnings("unchecked")
    public JTableUtil pasteFromClipboard_multiRowData(boolean autoRowExtendReadch, Transformer transformer) {
        List<Integer> colRealArray = getTableColumnModelIndex();
        List<Integer> rowRealArray = getTableColumnModelIndex_realRowIndex();
        int startColPos = table.getSelectedColumn();
        int realColumnCount = -1;
        try {
            realColumnCount = ((Vector<?>) (getModel().getDataVector().get(0))).size();
        } catch (Exception ex) {
        }

        try {
            BufferedReader reader = new BufferedReader(new StringReader(ClipboardUtil.getInstance().getContents()));
            StringTokenizer tok = null;
            List<Object> list = new ArrayList<Object>();
            Vector<Object> rowVector = null;

            int rowPos = table.getSelectedRow();
            for (String line = null; (line = reader.readLine()) != null;) {
                list.clear();
                tok = new StringTokenizer(line);
                for (; tok.hasMoreElements();) {
                    list.add(transformer.transform(tok.nextElement()));
                }

                if (rowPos >= rowRealArray.size()) {
                    if (!autoRowExtendReadch) {
                        break;
                    } else {
                        if (realColumnCount != -1) {
                            getModel().addRow(new Object[realColumnCount]);
                            rowRealArray = getTableColumnModelIndex_realRowIndex();
                        }
                    }
                }

                rowVector = (Vector<Object>) getModel().getDataVector().get(rowRealArray.get(rowPos));
                for (int ii = startColPos, index = 0; ii < colRealArray.size(); ii++, index++) {
                    if (index == list.size()) {
                        break;
                    }
                    // System.out.format("set %d = %s\n", ii, list.get(index));
                    rowVector.set(colRealArray.get(ii), list.get(index));
                }
                rowPos++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public JTableUtil pasteFromClipboard_singleValueToSelectedCell() {
        int rowPos = -1;
        int colPos = -1;
        String value = ClipboardUtil.getInstance().getContents();
        DefaultTableModel model = (DefaultTableModel) getModel();
        for (int row : table.getSelectedRows()) {
            if (table.getRowSorter() != null) {
                rowPos = table.getRowSorter().convertRowIndexToModel(row);
            } else {
                rowPos = row;
            }
            for (int col : table.getSelectedColumns()) {
                colPos = table.convertColumnIndexToModel(col);
                model.setValueAt(value, rowPos, colPos);
            }
        }
        return this;
    }

    public JTableUtil pasteFromObject_singleValueToSelectedCell(Object value) {
        int rowPos = -1;
        int colPos = -1;
        DefaultTableModel model = (DefaultTableModel) getModel();
        for (int row : table.getSelectedRows()) {
            if (table.getRowSorter() != null) {
                rowPos = table.getRowSorter().convertRowIndexToModel(row);
            } else {
                rowPos = row;
            }
            for (int col : table.getSelectedColumns()) {
                colPos = table.convertColumnIndexToModel(col);
                model.setValueAt(value, rowPos, colPos);
            }
        }
        return this;
    }

    // xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    // jmenu_item

    public List<JMenuItem> getDefaultJMenuItems_NoAddRemoveColumn() {
        JMenuItem a3 = jMenuItem_addRow(true, "");
        JMenuItem a3_1 = jMenuItem_addRow(true, "input row count!");
        JMenuItem a4 = jMenuItem_removeRow("");
        JMenuItem a5 = jMenuItem_removeAllRow("");
        JMenuItem a6 = jMenuItem_clearSelectedCell("");
        JMenuItem a7 = jMenuItem_pasteFromClipboard_multiRowData(true);
        JMenuItem a8 = jMenuItem_pasteFromClipboard_singleValueToSelectedCell();
        a3_1.setText("add multi row");
        return Arrays.asList(a3, a3_1, a4, a5, a6, a7, a8);
    }

    public List<JMenuItem> getDefaultJMenuItems() {
        JMenuItem a1 = jMenuItem_addColumn("");
        JMenuItem a2 = jMenuItem_removeColumn("");
        JMenuItem a3 = jMenuItem_addRow(true, "");
        JMenuItem a3_1 = jMenuItem_addRow(true, "input row count!");
        JMenuItem a4 = jMenuItem_removeRow("");
        JMenuItem a5 = jMenuItem_removeAllRow("");
        JMenuItem a6 = jMenuItem_clearSelectedCell("");
        JMenuItem a7 = jMenuItem_pasteFromClipboard_multiRowData(true);
        JMenuItem a8 = jMenuItem_pasteFromClipboard_singleValueToSelectedCell();
        a3_1.setText("add multi row");
        return Arrays.asList(a1, a2, a3, a3_1, a4, a5, a6, a7, a8);
    }

    public JMenuItem jMenuItem_pasteFromClipboard_multiRowData(final boolean autoRowExtendReadch) {
        JMenuItem menuItem = new JMenuItem();
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pasteFromClipboard_multiRowData(autoRowExtendReadch);
            }
        });
        menuItem.setText("paste from clipboard (multi row data)");
        return menuItem;
    }

    public JMenuItem jMenuItem_pasteFromClipboard_singleValueToSelectedCell() {
        JMenuItem menuItem = new JMenuItem();
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pasteFromClipboard_singleValueToSelectedCell();
            }
        });
        menuItem.setText("paste from clipboard (single value to selected cell)");
        return menuItem;
    }

    public JMenuItem jMenuItem_addColumn(final String confirmMessage) {
        JMenuItem menuItem = new JMenuItem();
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String columnName = UUID.randomUUID().toString();
                if (StringUtils.isNotEmpty(confirmMessage)) {
                    columnName = JOptionPaneUtil.newInstance().iconPlainMessage().showInputDialog(confirmMessage, "ADD COLUMN");
                    if (StringUtils.isEmpty(columnName)) {
                        JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("column title can't be empty!", "ERROR");
                        return;
                    }
                }
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.addColumn(columnName);
            }
        });
        menuItem.setText("add column");
        return menuItem;
    }

    public JMenuItem jMenuItem_removeColumn(final String confirmMessage) {
        JMenuItem menuItem = new JMenuItem();
        final int colPos = table.getSelectedColumn();
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (StringUtils.isNotBlank(confirmMessage) && //
                JOptionPaneUtil.ComfirmDialogResult.YES_OK_OPTION != //
                JOptionPaneUtil.newInstance().iconWaringMessage().confirmButtonYesNo().showConfirmDialog(confirmMessage, "REMOVE ROW")) {
                    return;
                }
                TableColumn tableColumn = null;
                StringBuilder sb = new StringBuilder();
                for (int colPos : table.getSelectedColumns()) {
                    tableColumn = table.getColumnModel().getColumn(colPos);
                    sb.append(String.format("%d[%s],", tableColumn.getModelIndex(), tableColumn.getHeaderValue()));
                    table.removeColumn(tableColumn);
                }
                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                }
                System.out.println(sb);
            }
        });
        if (colPos == -1) {
            menuItem.setEnabled(false);
        }
        menuItem.setText("remove column");
        return menuItem;
    }

    public JMenuItem jMenuItem_addRow(final boolean insertBeforeCurrentRow, final String confirmMessage) {
        JMenuItem menuItem = new JMenuItem();
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int rowPos = table.getSelectedRow();
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                if (!insertBeforeCurrentRow) {
                    rowPos++;
                }
                int rowCount = 1;
                if (StringUtils.isNotBlank(confirmMessage)) {
                    try {
                        rowCount = Integer.parseInt(JOptionPaneUtil.newInstance().iconInformationMessage().showInputDialog(confirmMessage, "ADD ROWS"));
                    } catch (Exception ex) {
                        JCommonUtil.handleException(ex);
                        return;
                    }
                }
                for (int ii = 0; ii < rowCount; ii++) {
                    String[] emptyArry = new String[model.getRowCount()];
                    for (int gg = 0; gg < emptyArry.length; gg++) {
                        emptyArry[gg] = "";
                    }
                    if (rowPos == -1) {
                        model.addRow(emptyArry);
                        continue;
                    }
                    model.insertRow(rowPos, emptyArry);
                }
            }
        });
        menuItem.setText("add row");
        return menuItem;
    }

    public JMenuItem jMenuItem_removeRow(final String confirmMessage) {
        JMenuItem delRowItem = new JMenuItem();
        final int rowPos = table.getSelectedRow();
        delRowItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (StringUtils.isNotBlank(confirmMessage) && //
                JOptionPaneUtil.ComfirmDialogResult.YES_OK_OPTION != //
                JOptionPaneUtil.newInstance().iconWaringMessage().confirmButtonYesNo().showConfirmDialog(confirmMessage, "REMOVE ROW")) {
                    return;
                }
                DefaultTableModel model = getModel();
                for (int rowPos : table.getSelectedRows()) {
                    int realPos = rowPos;
                    if (table.getRowSorter() != null) {
                        realPos = table.getRowSorter().convertRowIndexToModel(rowPos);
                    }
                    model.removeRow(realPos);
                }
            }
        });
        if (rowPos == -1) {
            delRowItem.setEnabled(false);
        }
        delRowItem.setText("remove row");
        return delRowItem;
    }

    public JMenuItem jMenuItem_removeAllRow(final String confirmMessage) {
        JMenuItem delRowItem = new JMenuItem();
        final int rowPos = table.getSelectedRow();
        delRowItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (StringUtils.isNotBlank(confirmMessage) && //
                JOptionPaneUtil.ComfirmDialogResult.YES_OK_OPTION != //
                JOptionPaneUtil.newInstance().iconWaringMessage().confirmButtonYesNo().showConfirmDialog(confirmMessage, "REMOVE ROW")) {
                    return;
                }
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                for (; model.getRowCount() > 0;) {
                    model.removeRow(0);
                }
            }
        });
        if (rowPos == -1) {
            delRowItem.setEnabled(false);
        }
        delRowItem.setText("remove all row");
        return delRowItem;
    }

    public JMenuItem jMenuItem_clearSelectedCell(final String confirmMessage) {
        JMenuItem delRowItem = new JMenuItem();
        final int rowPos = table.getSelectedRow();
        delRowItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (StringUtils.isNotBlank(confirmMessage) && //
                JOptionPaneUtil.ComfirmDialogResult.YES_OK_OPTION != //
                JOptionPaneUtil.newInstance().iconWaringMessage().confirmButtonYesNo().showConfirmDialog(confirmMessage, "REMOVE ROW")) {
                    return;
                }
                int rowPos = -1;
                int colPos = -1;
                DefaultTableModel model = (DefaultTableModel) table.getRowSorter().getModel();
                for (int row : table.getSelectedRows()) {
                    rowPos = table.getRowSorter().convertRowIndexToModel(row);
                    for (int col : table.getSelectedColumns()) {
                        colPos = table.convertColumnIndexToModel(col);
                        // System.out.format("%d %d = %s\n", rowPos, colPos,
                        // table.getRowSorter().getModel().getValueAt(rowPos,
                        // colPos));
                        model.setValueAt("", rowPos, colPos);
                    }
                }

            }
        });
        if (rowPos == -1) {
            delRowItem.setEnabled(false);
        }
        delRowItem.setText("clear selected cell");
        return delRowItem;
    }

    // xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

    /**
     * 全部row的cell寬度來計算寬度
     */
    public static int calculateColumnWidth(JTable table, int columnIndex) {
        int width = 0; // The return value
        int rowCount = table.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            TableCellRenderer renderer = table.getCellRenderer(i, columnIndex);
            Component comp = renderer.getTableCellRendererComponent(table, table.getValueAt(i, columnIndex), false, false, i, columnIndex);
            int thisWidth = comp.getPreferredSize().width;
            if (thisWidth > width) {
                width = thisWidth;
            }
        }
        return width;
    }

    /**
     * 用column title計算寬度
     */
    public static int calculateColumnWidth_1(JTable table, int pos) {
        for (int ii = 0; ii < table.getColumnModel().getColumnCount(); ii++) {
            TableColumn col = table.getColumnModel().getColumn(ii);
            if (ii == pos) {
                return col.getHeaderValue().toString().getBytes().length * 10;
            }
        }
        throw new RuntimeException("error!!");
    }

    public static void setColumnWidths_Percent(JTable table, float[] widthPercentArry) {
        int columnCount = table.getColumnCount();
        if (columnCount != widthPercentArry.length) {
            throw new RuntimeException("陣列長度必須為 : " + columnCount);
        }
        TableColumnModel tcm = table.getColumnModel();
        double wholeSize = table.getSize().getWidth();
        if (table.getParent() instanceof JViewport) {
            wholeSize = ((JViewport) table.getParent()).getSize().getWidth() - 5;
        }
        System.out.println("table wholeSize : " + wholeSize);
        for (int i = 0; i < columnCount; i++) {
            int width = (int) (wholeSize * (widthPercentArry[i] / 100));
            TableColumn column = tcm.getColumn(i);
            column.setPreferredWidth(width);

            boolean setMinimum = true;// 設最小
            boolean setMaximum = false;// 設最大

            if (setMinimum == true) {
                column.setMinWidth(width);
            }
            if (setMaximum == true) {
                column.setMaxWidth(width);
            }
        }
    }

    /**
     * 設定蘭寬
     * 
     * @param table
     * @param insets
     * @param setMinimum
     * @param setMaximum
     */
    public static void setColumnWidths(JTable table, Insets insets) {
        int columnCount = table.getColumnCount();
        TableColumnModel tcm = table.getColumnModel();
        int spare = (insets == null ? 0 : insets.left + insets.right);
        for (int i = 0; i < columnCount; i++) {
            int width = calculateColumnWidth_1(table, i);
            width += spare;
            TableColumn column = tcm.getColumn(i);
            column.setPreferredWidth(width + 1);

            boolean setMinimum = true;// 設最小
            boolean setMaximum = false;// 設最大

            if (setMinimum == true) {
                column.setMinWidth(width);
            }
            if (setMaximum == true) {
                column.setMaxWidth(width);
            }
        }
    }

    /**
     * 設定蘭寬
     * 
     * @param table
     * @param insets
     * @param setMinimum
     * @param setMaximum
     */
    public static void setColumnWidths_ByDataContent(JTable table, Insets insets) {
        int columnCount = table.getColumnCount();
        TableColumnModel tcm = table.getColumnModel();
        int spare = (insets == null ? 0 : insets.left + insets.right);

        Map<Integer, Integer> widthPosMap = new HashMap<Integer, Integer>();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int rowPos = 0; rowPos < table.getRowCount(); rowPos++) {
            for (int colPos = 0; colPos < table.getColumnCount(); colPos++) {
                int realColPos = table.convertColumnIndexToModel(colPos);
                String val = String.valueOf(model.getValueAt(rowPos, realColPos));
                int width = val.toString().getBytes().length * 10;

                Integer currentWidth = 0;
                if (widthPosMap.containsKey(realColPos)) {
                    currentWidth = widthPosMap.get(realColPos);
                }
                currentWidth = Math.max(currentWidth, width);
                widthPosMap.put(realColPos, currentWidth);
            }
        }

        System.out.println("---" + widthPosMap);
        if (widthPosMap.isEmpty()) {
            return;
        }

        for (int i = 0; i < columnCount; i++) {
            int width = widthPosMap.get(i);
            width += spare;
            TableColumn column = tcm.getColumn(i);
            column.setPreferredWidth(width + 1);

            System.out.println("set preferedWidth - " + width);
        }
    }

    /**
     * 設定欄被景色
     */
    public void setColumnColor(Color color, int colPos) {
        TableColumnModel columnmodel = table.getColumnModel();
        DefaultTableCellRenderer cr00 = new DefaultTableCellRenderer();
        cr00.setBackground(color);
        columnmodel.getColumn(colPos).setCellRenderer(cr00);
    }

    /**
     * 設定欄被景色
     */
    public void setColumnColor_byCondition(int colPos, TableColorDef tableColorDef) {
        TableColumn column = table.getColumnModel().getColumn(colPos);
        column.setCellRenderer(new MyCellRenderer(table, colPos, tableColorDef));
    }

    private class MyCellRenderer implements TableCellRenderer {
        protected TableCellRenderer wrappedRenderer;
        protected TableColorDef tableColorDef;

        public MyCellRenderer(JTable table, int columPos, TableColorDef tableColorDef) {
            TableColumn column = table.getColumnModel().getColumn(columPos);
            this.wrappedRenderer = column.getCellRenderer();
            if (wrappedRenderer == null) {
                wrappedRenderer = new DefaultTableCellRenderer();
            }
            this.tableColorDef = tableColorDef;
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component ret = wrappedRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            Color color = tableColorDef.getTableBackgroundColour(table, value, isSelected, hasFocus, row, column);
            ret.setBackground(color);
            return ret;
        }
    }

    /**
     * 設定欄被景色
     */
    public interface TableColorDef {
        Color getTableBackgroundColour(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column);
    }

    /**
     * 定義欄位為按鈕用
     */
    private static class _ColumnButtonMouseAdapter extends MouseAdapter {
        JTable table;

        private _ColumnButtonMouseAdapter(JTable table) {
            this.table = table;
        }

        public void mouseClicked(MouseEvent e) {
            int column = table.getColumnModel().getColumnIndexAtX(e.getX());
            int row = e.getY() / table.getRowHeight();
            System.out.println(String.format("tableClick row[%d],col[%d]", row, column));
            if (row < table.getRowCount() && row >= 0 && column < table.getColumnCount() && column >= 0) {
                Object value = table.getValueAt(row, column);
                if (value instanceof JButton) {
                    ((JButton) value).doClick();
                }
            }
        }
    }
}
