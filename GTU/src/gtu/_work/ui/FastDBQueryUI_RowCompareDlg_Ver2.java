package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import gtu._work.ui.FastDBQueryUI_CrudDlgUI.DataType;
import gtu._work.ui.FastDBQueryUI_XlsColumnDefLoader.RegExpAndTextFilter;
import gtu.file.FileUtil;
import gtu.poi.hssf.ExcelUtil_Xls97;
import gtu.poi.hssf.ExcelWriter;
import gtu.poi.hssf.ExcelWriter.CellStyleHandler;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JCommonUtil.HandleDocumentEvent;
import gtu.swing.util.JTableUtil;

public class FastDBQueryUI_RowCompareDlg_Ver2 extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTable importRowTable;
    private JTabbedPane tabbedPane;
    private FastDBQueryUI _parent;
    List<String> titleLst;
    String row1Label;
    String row2Label;
    List<Object> row1;
    List<Object> row2;
    private JTextField columnFilterText;

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

            dialog.titleLst = titleLst;
            dialog.row1Label = row1Label;
            dialog.row2Label = row2Label;
            dialog.row1 = row1;
            dialog.row2 = row2;

            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
            DefaultTableModel model = dialog.initImportRowTable(row1Label, row2Label);
            dialog.initTab1(titleLst, row1, row2, null, model);

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
                    panel_11.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
                    {
                        JLabel lblNewLabel = new JLabel("過濾");
                        panel_11.add(lblNewLabel);
                    }
                    {
                        columnFilterText = new JTextField();
                        panel_11.add(columnFilterText);
                        columnFilterText.setColumns(50);

                        columnFilterText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
                            @Override
                            public void process(DocumentEvent event) {
                                String text = StringUtils.trimToEmpty(columnFilterText.getText());
                                DefaultTableModel model = initImportRowTable(row1Label, row2Label);

                                Triple<Boolean, String[], Pattern[]> condition = FastDBQueryUI_XlsColumnDefLoader.RegExpAndTextFilter.toSearchCondition(text);
                                initTab1(titleLst, row1, row2, condition, model);
                            }
                        }));
                    }
                    {
                        JPanel panel_12 = new JPanel();
                        panel_11.add(panel_12);
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
                    JTableUtil.defaultSetting(importRowTable);
                    panel.add(JCommonUtil.createScrollComponent(importRowTable), BorderLayout.CENTER);
                }
            }
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton excelExportBtn = new JButton("匯出excel");
                excelExportBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent arg0) {
                        exportExcelAction();
                    }
                });
                buttonPane.add(excelExportBtn);
            }
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

    private String fixValue(Object v1) {
        String v11 = v1 == null ? "" : String.valueOf(v1);
        if ("null".equals(v11)) {
            return "";
        }
        return v11;
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
                String v11 = fixValue(v1);
                String v22 = fixValue(v2);
                if (!StringUtils.equals(v11, v22)) {
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
                String v11 = fixValue(v1);
                String v22 = fixValue(v2);
                if (!StringUtils.equals(v11, v22)) {
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
                String v11 = fixValue(v1);
                String v22 = fixValue(v2);
                if (!StringUtils.equals(v11, v22)) {
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

    private void initTab1(List<String> titleLst, List<Object> row1, List<Object> row2, Triple<Boolean, String[], Pattern[]> condition, DefaultTableModel model) {
        Validate.isTrue((titleLst.size() == row1.size()) && (titleLst.size() == row2.size()), "資料欄位數目應該相同!");
        for (int ii = 0; ii < titleLst.size(); ii++) {
            String col = titleLst.get(ii);
            Object value1 = row1.get(ii);
            Object value2 = row2.get(ii);

            boolean findOk = false;
            if (!RegExpAndTextFilter.isEmptyCondition(condition)) {
                if (RegExpAndTextFilter.isTextContain(condition.getLeft(), col, condition.getMiddle()) || //
                        RegExpAndTextFilter.isTextContain(condition.getLeft(), String.valueOf(value1), condition.getMiddle()) || //
                        RegExpAndTextFilter.isTextContain(condition.getLeft(), String.valueOf(value2), condition.getMiddle())) {
                    findOk = true;
                }
                if (!findOk && (FastDBQueryUI_XlsColumnDefLoader.RegExpAndTextFilter.isRegexMatch(condition.getLeft(), col, condition.getRight()) || //
                        FastDBQueryUI_XlsColumnDefLoader.RegExpAndTextFilter.isRegexMatch(condition.getLeft(), String.valueOf(value1), condition.getRight()) || //
                        FastDBQueryUI_XlsColumnDefLoader.RegExpAndTextFilter.isRegexMatch(condition.getLeft(), String.valueOf(value2), condition.getRight()) //
                )) {
                    findOk = true;
                }
            } else {
                findOk = true;
            }

            if (findOk) {
                model.addRow(new Object[] { col, value1, value2 });
            }
        }
        JTableUtil.newInstance(importRowTable).setRowHeightByFontSize();
        System.out.println("-------------init size : " + model.getRowCount());
    }

    private void exportExcelAction() {
        try {
            List<String> titleLst = this.titleLst;
            String row1Label = this.row1Label;
            String row2Label = this.row2Label;
            List<Object> row1 = this.row1;
            List<Object> row2 = this.row2;

            ExcelUtil_Xls97 util = ExcelUtil_Xls97.getInstance();
            HSSFWorkbook wb = new HSSFWorkbook();

            {
                HSSFSheet sheet = wb.createSheet("比對結果");

                CellStyleHandler row0Cs = ExcelWriter.CellStyleHandler.newInstance(wb.createCellStyle())//
                        .setForegroundColor(new HSSFColor.LAVENDER());
                CellStyleHandler leftChangeCs = ExcelWriter.CellStyleHandler.newInstance(wb.createCellStyle())//
                        .setForegroundColor(new HSSFColor.SEA_GREEN());
                CellStyleHandler leftCs = ExcelWriter.CellStyleHandler.newInstance(wb.createCellStyle())//
                        .setForegroundColor(new HSSFColor.AQUA());
                CellStyleHandler changeCs = ExcelWriter.CellStyleHandler.newInstance(wb.createCellStyle())//
                        .setForegroundColor(new HSSFColor.YELLOW());
                CellStyleHandler nonChangeCs = ExcelWriter.CellStyleHandler.newInstance(wb.createCellStyle())//
                        .setForegroundColor(new HSSFColor.WHITE());

                row0Cs.setSheet(sheet);
                leftCs.setSheet(sheet);
                changeCs.setSheet(sheet);
                leftChangeCs.setSheet(sheet);
                nonChangeCs.setSheet(sheet);

                Row row0 = sheet.createRow(0);
                util.getCellChk(row0, 0).setCellValue("欄位");
                util.getCellChk(row0, 1).setCellValue(row1Label);
                util.getCellChk(row0, 2).setCellValue(row2Label);
                row0Cs.applyStyle(0, 0);
                row0Cs.applyStyle(0, 1);
                row0Cs.applyStyle(0, 2);

                for (int ii = 0; ii < titleLst.size(); ii++) {
                    String title = titleLst.get(ii);
                    String value1 = String.valueOf(row1.get(ii));
                    String value2 = String.valueOf(row2.get(ii));

                    Row rowx = sheet.createRow(ii + 1);
                    util.getCellChk(rowx, 0).setCellValue(title);
                    util.getCellChk(rowx, 1).setCellValue(value1);
                    util.getCellChk(rowx, 2).setCellValue(value2);

                    String v11 = fixValue(value1);
                    String v22 = fixValue(value2);

                    if (!StringUtils.equals(v11, v22)) {
                        leftChangeCs.applyStyle(ii + 1, 0);
                        changeCs.applyStyle(ii + 1, 1);
                        changeCs.applyStyle(ii + 1, 2);
                    } else {
                        leftCs.applyStyle(ii + 1, 0);
                        nonChangeCs.applyStyle(ii + 1, 1);
                        nonChangeCs.applyStyle(ii + 1, 2);
                    }
                }

                util.setSheetWidth(sheet, new short[] { 8000, 13000, 13000 });
            }

            {
                HSSFSheet sheet = wb.createSheet("比對結果2");

                CellStyleHandler row0Cs = ExcelWriter.CellStyleHandler.newInstance(wb.createCellStyle())//
                        .setForegroundColor(new HSSFColor.LAVENDER());
                CellStyleHandler leftChangeCs = ExcelWriter.CellStyleHandler.newInstance(wb.createCellStyle())//
                        .setForegroundColor(new HSSFColor.SEA_GREEN());
                CellStyleHandler leftCs = ExcelWriter.CellStyleHandler.newInstance(wb.createCellStyle())//
                        .setForegroundColor(new HSSFColor.AQUA());
                CellStyleHandler changeCs = ExcelWriter.CellStyleHandler.newInstance(wb.createCellStyle())//
                        .setForegroundColor(new HSSFColor.YELLOW());
                CellStyleHandler nonChangeCs = ExcelWriter.CellStyleHandler.newInstance(wb.createCellStyle())//
                        .setForegroundColor(new HSSFColor.WHITE());

                row0Cs.setSheet(sheet);
                leftCs.setSheet(sheet);
                changeCs.setSheet(sheet);
                leftChangeCs.setSheet(sheet);
                nonChangeCs.setSheet(sheet);

                Row sheetRow0 = sheet.createRow(0);
                Row sheetRow1 = sheet.createRow(1);
                Row sheetRow2 = sheet.createRow(2);

                util.getCellChk(sheetRow0, 0).setCellValue("欄位");
                util.getCellChk(sheetRow1, 0).setCellValue(row1Label);
                util.getCellChk(sheetRow2, 0).setCellValue(row2Label);

                row0Cs.applyStyle(util.getCellChk(sheetRow0, 0));
                row0Cs.applyStyle(util.getCellChk(sheetRow1, 0));
                row0Cs.applyStyle(util.getCellChk(sheetRow2, 0));

                // title
                for (int ii = 0; ii < titleLst.size(); ii++) {
                    String titleValue = titleLst.get(ii);
                    Cell c0 = util.getCellChk(sheetRow0, ii + 1);
                    c0.setCellValue(titleValue);
                    row0Cs.applyStyle(c0);
                }

                for (int ii = 0; ii < titleLst.size(); ii++) {
                    String value1 = String.valueOf(row1.get(ii));
                    String value2 = String.valueOf(row2.get(ii));

                    Cell c1 = util.getCellChk(sheetRow1, ii + 1);
                    Cell c2 = util.getCellChk(sheetRow2, ii + 1);

                    c1.setCellValue(value1);
                    c2.setCellValue(value2);

                    String v11 = fixValue(value1);
                    String v22 = fixValue(value2);

                    if (!StringUtils.equals(v11, v22)) {
                        leftChangeCs.applyStyle(0, ii + 1);
                        changeCs.applyStyle(c1);
                        changeCs.applyStyle(c2);
                    } else {
                        leftCs.applyStyle(0, ii + 1);
                        nonChangeCs.applyStyle(c1);
                        nonChangeCs.applyStyle(c2);
                    }
                }

                util.autoCellSize(sheet);
            }

            String filename1 = FastDBQueryUI.class.getSimpleName() + "_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss");
            filename1 = JCommonUtil._jOptionPane_showInputDialog("輸入檔名", filename1);
            if (StringUtils.isBlank(filename1)) {
                return;
            }
            String filename = filename1 + ".xls";
            File xlsFile = new File(FileUtil.DESKTOP_DIR, filename);
            util.writeExcel(xlsFile, wb);
            JCommonUtil._jOptionPane_showMessageDialog_info("產生比對檔:" + filename);
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }
}
