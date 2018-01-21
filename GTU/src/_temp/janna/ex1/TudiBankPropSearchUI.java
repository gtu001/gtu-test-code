package _temp.janna.ex1;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.sun.scenario.effect.Blend.Mode;

import gtu.class_.ClassPathUtil;
import gtu.poi.hssf.ExcelUtil;
import gtu.swing.util.AutocompleteJComboBox;
import gtu.swing.util.AutocompleteJComboBox.StringSearchable;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JTableUtil;

public class TudiBankPropSearchUI extends JFrame {

    private JPanel contentPane;
    private JTable resultTable;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TudiBankPropSearchUI frame = new TudiBankPropSearchUI();
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
    public TudiBankPropSearchUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 518, 399);
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
        panel_2.setPreferredSize(new Dimension(0, 75));
        panel.add(panel_2, BorderLayout.NORTH);

        searchCombo = new JComboBox();
        searchCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent paramActionEvent) {
                try {
                } catch (Exception e) {
                    JCommonUtil.handleException(e);
                }
            }
        });
        panel_2.add(searchCombo);

        searchTextField = new JTextField();
        searchTextField.setToolTipText("查詢代碼名稱");
        searchTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent paramActionEvent) {
                initComboList();
            }
        });
        panel_2.add(searchTextField);
        searchTextField.setColumns(20);

        JButton button = new JButton("顯示");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent paramActionEvent) {
                showDataInTable();
            }
        });
        panel_2.add(button);

        codeValueText = new JTextField();
        codeValueText.setToolTipText("查詢code或value");
        codeValueText.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent paramActionEvent) {
                showDataInTable();
            }
        });
        codeValueText.setColumns(20);
        panel_2.add(codeValueText);

        resultTable = new JTable();
        JTableUtil.defaultSetting_AutoResize(resultTable);
        // panel.add(resultTable, BorderLayout.CENTER);
        JCommonUtil.createScrollComponent(panel, resultTable);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("New tab", null, panel_1, null);

        initLoad();

        JCommonUtil.defaultToolTipDelay();
    }

    private void showDataInTable() {
        RowDef r = (RowDef) searchCombo.getSelectedItem();
        if (r == null) {
            JCommonUtil._jOptionPane_showMessageDialog_info("請選下拉選單");
            return;
        }
        String codeValueStr = StringUtils.defaultString(codeValueText.getText()).toLowerCase();
        DefaultTableModel model = JTableUtil.createModel(true, new String[] {"代碼檔", "code", "value" });

        if (r.isNothing == false) {
            for (Pair<String, String> p : r.codeList) {
                String code = p.getKey();
                String value = p.getValue();
                if (StringUtils.isNotBlank(codeValueStr)) {
                    if (code.toLowerCase().contains(codeValueStr) || value.toLowerCase().contains(codeValueStr)) {
                        model.addRow(new String[] {r.codeId + r.codeName, code, value });
                    }
                } else {
                    model.addRow(new String[] {r.codeId + r.codeName, code, value });
                }
            }
        }else{
            ComboBoxModel model2 = searchCombo.getModel();
            for(int ii = 0; ii < model2.getSize(); ii ++){
                RowDef r2 = (RowDef)model2.getElementAt(ii);
                for (Pair<String, String> p : r2.codeList) {
                    String code = p.getKey();
                    String value = p.getValue();
                    if (StringUtils.isNotBlank(codeValueStr)) {
                        if (code.toLowerCase().contains(codeValueStr) || value.toLowerCase().contains(codeValueStr)) {
                            model.addRow(new String[] {r2.codeId + r2.codeName, code, value });
                        }
                    } else {
                        model.addRow(new String[] {r2.codeId + r2.codeName, code, value });
                    }
                }
            }
        }
        resultTable.setModel(model);
    }

    private void initLoad() {
        try {
            File file = new File(ClassPathUtil.getJarCurrentPath(getClass()), "config.xls");
            file = new File("D:/my_tool/config.xls");

            if (!file.exists()) {
                Validate.isTrue(false, "找不到excel : " + file);
            }

            HSSFWorkbook book = ExcelUtil.getInstance().readExcel(file);
            HSSFSheet sheet = book.getSheet("代碼明細資料");

            loadFirstRow(sheet);
            loadCodeValueDef(sheet);
            initComboList();
        } catch (Exception e) {
            JCommonUtil.handleException(e);
        }
    }

    private void initComboList() {
        String searhText = StringUtils.defaultString(searchTextField.getText());
        DefaultComboBoxModel<RowDef> model = new DefaultComboBoxModel<RowDef>();
        RowDef d1 = new RowDef();
        d1.isNothing = true;
        model.addElement(d1);
        for (int ii = 0; ii < rowDefList.size(); ii++) {
            if (StringUtils.isNotBlank(searhText)) {
                if (rowDefList.get(ii).toString().contains(searhText)) {
                    model.addElement(rowDefList.get(ii));
                }
            } else {
                model.addElement(rowDefList.get(ii));
            }
        }
        searchCombo.setModel(model);
    }

    Pattern ptn1 = Pattern.compile("([a-zA-Z]{1}\\d+)(.*)");
    List<RowDef> rowDefList = new ArrayList<RowDef>();
    private JComboBox searchCombo;
    private JTextField searchTextField;
    private JTextField codeValueText;

    private void loadFirstRow(HSSFSheet sheet) {
        HSSFRow row = sheet.getRow(0);
        HSSFCell cell = null;
        String value = null;

        RowDef tmp = null;
        for (int jj = 0; jj < row.getLastCellNum(); jj++) {
            cell = row.getCell(jj);
            if (cell != null) {
                value = ExcelUtil.getInstance().readHSSFCell(cell);
                if (value != null) {
                    Matcher mth = ptn1.matcher(value);
                    if (mth.find()) {
                        String codeId = mth.group(1);
                        String codeName = mth.group(2);

                        if (tmp != null) {
                            tmp.valueTo = jj - 1;
                            rowDefList.add(tmp);
                            tmp = null;
                        }

                        RowDef v1 = new RowDef();
                        v1.codeId = codeId;
                        if (StringUtils.isNotBlank(codeName)) {
                            v1.codeName = codeName;
                        }
                        v1.code = jj;
                        v1.valueFrom = jj + 1;
                        tmp = v1;
                    } else if (StringUtils.isNotBlank(value) && tmp != null) {
                        if (StringUtils.isBlank(tmp.codeName)) {
                            tmp.codeName = "";
                        }
                        tmp.codeName += value;
                    }
                }
            }
        }

        Collections.sort(rowDefList, new Comparator<RowDef>() {
            @Override
            public int compare(RowDef o1, RowDef o2) {
                return o1.codeId.compareTo(o2.codeId);
            }
        });
    }

    private void loadCodeValueDef(HSSFSheet sheet) {
        for (RowDef r : rowDefList) {
            if (r.code == -1) {
                throw new RuntimeException("def code = -1");
            }
            if (r.valueFrom == -1) {
                throw new RuntimeException("def code = -1");
            }

            if (r.valueTo == -1) {
                r.valueTo = r.valueFrom;
            }

            for (int ii = 1; ii < sheet.getLastRowNum() + 1; ii++) {
                HSSFRow row = sheet.getRow(ii);

                String code = ExcelUtil.getInstance().readHSSFCell(row.getCell(r.code));

                StringBuilder sb = new StringBuilder();
                for (int jj = r.valueFrom; jj <= r.valueTo; jj++) {
                    String value = ExcelUtil.getInstance().readHSSFCell(row.getCell(jj));
                    sb.append(value);
                }

                if (StringUtils.isBlank(code) && StringUtils.isBlank(sb.toString())) {
                    continue;
                }
                r.codeList.add(Pair.of(code, sb.toString()));
            }
        }
    }

    private class RowDef {
        String codeId;
        String codeName;

        boolean isNothing = false;

        int code = -1;
        int valueFrom = -1;
        int valueTo = -1;

        List<Pair> codeList = new ArrayList<Pair>();

        @Override
        public String toString() {
            if (isNothing) {
                return "請選擇";
            }
            return codeId + codeName;
        }
    }
}
