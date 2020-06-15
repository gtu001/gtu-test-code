package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import gtu._work.ui.FastDBQueryUI_CrudDlgUI.DataType;
import gtu.collection.MapUtil;
import gtu.db.JdbcDBUtil;
import gtu.db.jdbc.util.DBDateUtil;
import gtu.db.sqlMaker.DbSqlCreater.TableInfo;
import gtu.swing.util.JButtonGroupUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JTableUtil;
import gtu.swing.util.JTextAreaUtil;

public class FastDBQueryUI_RowCompareDlg_Ver2 extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTable importRowTable;
    private JTabbedPane tabbedPane;
    private FastDBQueryUI _parent;
    private JTable queryResultTable;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        FastDBQueryUI_RowCompareDlg_Ver2 dialog = new FastDBQueryUI_RowCompareDlg_Ver2();
        try {
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static FastDBQueryUI_RowCompareDlg_Ver2 newInstance(List<String> titleLst, String row1Label, String row2Label, List<Object> row1, List<Object> row2, final ActionListener onCloseListener,
            FastDBQueryUI _parent) {
        final FastDBQueryUI_RowCompareDlg_Ver2 dialog = new FastDBQueryUI_RowCompareDlg_Ver2();
        try {
            dialog._parent = _parent;
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
            DefaultTableModel model = dialog.initImportRowTable(row1Label, row2Label);
            dialog.initTab1(titleLst, row1, row2, model);

            dialog.addWindowListener(new WindowAdapter() {
                public void windowClosed(WindowEvent e) {
                    if (onCloseListener != null) {
                        onCloseListener.actionPerformed(new ActionEvent(dialog, -1, "close"));
                    }
                }

                public void windowClosing(WindowEvent e) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialog;
    }

    /**
     * Create the dialog.
     */
    public FastDBQueryUI_RowCompareDlg_Ver2() {
        setTitle("兩筆資料比對");
        setBounds(100, 100, 790, 477);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        {
            tabbedPane = new JTabbedPane(JTabbedPane.TOP);
            contentPanel.add(tabbedPane, BorderLayout.CENTER);
            {
                JPanel panel = new JPanel();
                tabbedPane.addTab("兩筆資料比對", null, panel, null);
                panel.setLayout(new BorderLayout(0, 0));
                {
                    JPanel panel_1 = new JPanel();
                    panel.add(panel_1, BorderLayout.NORTH);

                    panel_1.setLayout(new BorderLayout(0, 0));

                    JPanel panel_11 = new JPanel();
                    panel_1.add(panel_11, BorderLayout.CENTER);
                    panel_11.setLayout(new BorderLayout(0, 0));
                    {
                        JPanel panel_12 = new JPanel();
                        panel_11.add(panel_12, BorderLayout.EAST);
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
                    }
                }
                {
                    JPanel panel_1 = new JPanel();
                    panel.add(panel_1, BorderLayout.EAST);
                }
                {
                    importRowTable = new JTable();
                    panel.add(JCommonUtil.createScrollComponent(importRowTable), BorderLayout.CENTER);
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

    private DefaultTableModel initImportRowTable(String row1Label, String row2Label) {
        DefaultTableModel model = JTableUtil.createModel(false, "欄位", row1Label, row2Label);
        importRowTable.setModel(model);
        JTableUtil.setColumnWidths_Percent(importRowTable, new float[] { 25, 30, 30 });

        // TableColumn sportColumn4 =
        // importRowTable.getColumnModel().getColumn(3);
        // sportColumn4.setCellEditor(new DefaultCellEditor(new JCheckBox()));
        // TableColumn ignoreColumn =
        // importRowTable.getColumnModel().getColumn(4);
        // ignoreColumn.setCellEditor(new DefaultCellEditor(new JCheckBox()));

        JTableUtil.newInstance(importRowTable).setColumnColor_byCondition(0, new JTableUtil.TableColorDef() {
            public Pair<Color, Color> getTableColour(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JTableUtil util = JTableUtil.newInstance(importRowTable);
                Object v1 = util.getRealValueAt(row, 1);
                Object v2 = util.getRealValueAt(row, 2);
                if (ObjectUtils.notEqual(v1, v2)) {
                    return Pair.of(Color.RED, null);
                }
                return null;
            }
        });

        JTableUtil.newInstance(importRowTable).setColumnColor_byCondition(1, new JTableUtil.TableColorDef() {
            public Pair<Color, Color> getTableColour(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JTableUtil util = JTableUtil.newInstance(importRowTable);
                Object v1 = util.getRealValueAt(row, 1);
                Object v2 = util.getRealValueAt(row, 2);
                if (ObjectUtils.notEqual(v1, v2) && StringUtils.isNotBlank(String.valueOf(v1))) {
                    return Pair.of(Color.GREEN, null);
                }
                return null;
            }
        });

        JTableUtil.newInstance(importRowTable).setColumnColor_byCondition(2, new JTableUtil.TableColorDef() {
            public Pair<Color, Color> getTableColour(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JTableUtil util = JTableUtil.newInstance(importRowTable);
                Object v1 = util.getRealValueAt(row, 1);
                Object v2 = util.getRealValueAt(row, 2);
                if (ObjectUtils.notEqual(v1, v2) && StringUtils.isNotBlank(String.valueOf(v2))) {
                    return Pair.of(Color.GREEN, null);
                }
                return null;
            }
        });
        return model;
    }

    static class ColumnConf {
        String columnName;
        Object value1;
        Object value2;
        DataType dtype;
        boolean isPk;
        boolean isIgnore;
        boolean isModify = false;

        Object[] toArry() {
            Object[] arry = new Object[] { columnName, value1, value2, isPk, isIgnore };
            System.out.println(Arrays.toString(arry));
            return arry;
        }
    }

    private void initTab1(List<String> titleLst, List<Object> row1, List<Object> row2, DefaultTableModel model) {
        Validate.isTrue((titleLst.size() == row1.size()) && (titleLst.size() == row2.size()), "資料欄位數目應該相同!");
        for (int ii = 0; ii < titleLst.size(); ii++) {
            String col = titleLst.get(ii);
            Object value1 = row1.get(ii);
            Object value2 = row2.get(ii);
            model.addRow(new Object[] { col, value1, value2 });
        }
        JTableUtil.newInstance(importRowTable).setRowHeightByFontSize();
        System.out.println("-------------init size : " + model.getRowCount());
    }
}
