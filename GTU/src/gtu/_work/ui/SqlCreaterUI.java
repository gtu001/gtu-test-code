package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import gtu.poi.hssf.ExcelUtil;
import gtu.swing.util.JCommonUtil;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class SqlCreaterUI extends javax.swing.JFrame {
    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JButton firstRowMakeInsertSqlBtn;
    private JLabel jLabel3;
    private JTextField excelFilePathText2;
    private JPanel jPanel3;
    private JTextArea logArea;
    private JLabel jLabel2;
    private JTextField excelFilePathText;
    private JLabel jLabel1;
    private JTextArea sqlArea;
    private JButton executeBtn;
    private JPanel jPanel2;
    private JLabel jLabel4;
    private JTextField tableNameText;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                SqlCreaterUI inst = new SqlCreaterUI();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }

    public SqlCreaterUI() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            BorderLayout thisLayout = new BorderLayout();
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            getContentPane().setLayout(thisLayout);
            {
                jTabbedPane1 = new JTabbedPane();
                getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
                {
                    jPanel1 = new JPanel();
                    BorderLayout jPanel1Layout = new BorderLayout();
                    jPanel1.setLayout(jPanel1Layout);
                    jTabbedPane1.addTab("jPanel1", null, jPanel1, null);
                    {
                        jPanel2 = new JPanel();
                        jPanel1.add(jPanel2, BorderLayout.NORTH);
                        jPanel2.setPreferredSize(new java.awt.Dimension(582, 112));
                        {
                            jLabel1 = new JLabel();
                            jPanel2.add(jLabel1);
                            jLabel1.setText("SQL");
                        }
                        {
                            JScrollPane jScrollPane2 = new JScrollPane();
                            jPanel2.add(jScrollPane2);
                            jScrollPane2.setPreferredSize(new java.awt.Dimension(524, 57));
                            {
                                sqlArea = new JTextArea();
                                jScrollPane2.setViewportView(sqlArea);
                            }
                        }
                        {
                            jLabel2 = new JLabel();
                            jPanel2.add(jLabel2);
                            jLabel2.setText("\u4f86\u6e90\u6a94\u8def\u5f91");
                        }
                        {
                            excelFilePathText = new JTextField();
                            JCommonUtil.jTextFieldSetFilePathMouseEvent(excelFilePathText, false);
                            jPanel2.add(excelFilePathText);
                            excelFilePathText.setPreferredSize(new java.awt.Dimension(455, 22));
                        }
                    }
                    {
                        executeBtn = new JButton();
                        jPanel1.add(executeBtn, BorderLayout.SOUTH);
                        executeBtn.setText("\u7522\u751f");
                        executeBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                executeBtnPreformed();
                            }
                        });
                    }
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel1.add(jScrollPane1, BorderLayout.CENTER);
                        jScrollPane1.setPreferredSize(new java.awt.Dimension(582, 221));
                        {
                            JScrollPane jScrollPane3 = new JScrollPane();
                            jScrollPane1.setViewportView(jScrollPane3);
                            {
                                logArea = new JTextArea();
                                jScrollPane3.setViewportView(logArea);
                            }
                        }
                    }
                }
                {
                    jPanel3 = new JPanel();
                    FlowLayout jPanel3Layout = new FlowLayout();
                    jTabbedPane1.addTab("jPanel3", null, jPanel3, null);
                    jPanel3.setLayout(jPanel3Layout);
                    {
                        jLabel4 = new JLabel();
                        jPanel3.add(jLabel4);
                        jLabel4.setText("Table\u540d\u7a31");
                    }
                    {
                        tableNameText = new JTextField();
                        jPanel3.add(tableNameText);
                        tableNameText.setPreferredSize(new java.awt.Dimension(463, 23));
                    }
                    {
                        jLabel3 = new JLabel();
                        jPanel3.add(jLabel3);
                        jLabel3.setText("\u4f86\u6e90\u6a94\u8def\u5f91");
                    }
                    {
                        excelFilePathText2 = new JTextField();
                        JCommonUtil.jTextFieldSetFilePathMouseEvent(excelFilePathText2, false);
                        jPanel3.add(excelFilePathText2);
                        excelFilePathText2.setPreferredSize(new java.awt.Dimension(455, 22));
                    }
                    {
                        firstRowMakeInsertSqlBtn = new JButton();
                        jPanel3.add(firstRowMakeInsertSqlBtn);
                        firstRowMakeInsertSqlBtn.setText("\u4ee5\u7b2c\u4e00\u5217\u70ba\u6b04\u4f4d\u540d\u7a31\u7522\u751fInsert SQL");
                        firstRowMakeInsertSqlBtn.setPreferredSize(new java.awt.Dimension(251, 22));
                        firstRowMakeInsertSqlBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                firstRowMakeInsertSqlBtn(evt);
                            }
                        });
                    }
                }
            }
            pack();
            this.setSize(595, 409);
        } catch (Exception e) {
            // add your error handling code here
            e.printStackTrace();
        }
    }

    private void executeBtnPreformed() {
        try {
            logArea.setText("");
            File srcFile = JCommonUtil.filePathCheck(excelFilePathText.getText(), "來源黨", false);
            if (srcFile == null) {
                return;
            }
            if (!srcFile.getName().endsWith(".xlsx")) {
                JCommonUtil._jOptionPane_showMessageDialog_error("必須為excel黨");
                return;
            }
            if (StringUtils.isBlank(sqlArea.getText())) {
                return;
            }
            File saveFile = JCommonUtil._jFileChooser_selectFileOnly_saveFile();
            if (saveFile == null) {
                JCommonUtil._jOptionPane_showMessageDialog_error("選擇目的檔錯誤");
                return;
            }

            String sqlText = sqlArea.getText();

            StringBuffer sb = new StringBuffer();
            Map<Integer, String> refMap = new HashMap<Integer, String>();
            Pattern sqlPattern = Pattern.compile("\\$\\{(\\w+)\\}", Pattern.MULTILINE);
            Matcher matcher = sqlPattern.matcher(sqlText);
            while (matcher.find()) {
                String val = StringUtils.trim(matcher.group(1)).toUpperCase();
                refMap.put(ExcelUtil.cellEnglishToPos(val), val);
                matcher.appendReplacement(sb, "\\$\\{" + val + "\\}");
            }
            matcher.appendTail(sb);
            appendLog(refMap.toString());

            sqlText = sb.toString();
            sqlArea.setText(sqlText);

            Configuration cfg = new Configuration();
            StringTemplateLoader stringTemplatge = new StringTemplateLoader();
            stringTemplatge.putTemplate("aaa", sqlText);
            cfg.setTemplateLoader(stringTemplatge);
            cfg.setObjectWrapper(new DefaultObjectWrapper());
            Template temp = cfg.getTemplate("aaa");

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(saveFile), "utf8"));

            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(srcFile));
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(bis);
            Sheet sheet = xssfWorkbook.getSheetAt(0);
            for (int j = 0; j < sheet.getPhysicalNumberOfRows(); j++) {
                Row row = sheet.getRow(j);
                if (row == null) {
                    continue;
                }
                Map<String, Object> root = new HashMap<String, Object>();
                for (int index : refMap.keySet()) {
                    root.put(refMap.get(index), formatCellType(row.getCell(index)));
                }
                appendLog(root.toString());

                StringWriter out = new StringWriter();
                temp.process(root, out);
                out.flush();
                String writeStr = out.getBuffer().toString();
                appendLog(writeStr);

                writer.write(writeStr);
                writer.newLine();
            }
            bis.close();

            writer.flush();
            writer.close();

            JCommonUtil._jOptionPane_showMessageDialog_info("檔案寫入成功 : \n" + saveFile);
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void appendLog(String message) {
        logArea.append(message + "\n");
    }

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd");

    private String formatCellType(Cell cell) {
        String returnVal = StringUtils.EMPTY;
        if (cell == null) {
            return "";
        }
        if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                Date d = cell.getDateCellValue();
                returnVal = " to_date('" + SDF.format(d) + "', 'yyyymmdd' ) ";
            } else {
                final NumberFormat formatter = new DecimalFormat("##");
                returnVal = formatter.format(Float.valueOf(cell.toString()));
            }
        } else {
            returnVal = cell.toString();
        }
        return returnVal;
    }

    private void firstRowMakeInsertSqlBtn(ActionEvent evt) {
        try {
            String tableName = Validate.notBlank(tableNameText.getText(), "資料表名稱為輸入");
            File srcFile = JCommonUtil.filePathCheck(excelFilePathText2.getText(), "來源黨", "xlsx");
            File saveFile = JCommonUtil._jFileChooser_selectFileOnly_saveFile();
            if (saveFile == null) {
                JCommonUtil._jOptionPane_showMessageDialog_error("選擇目的檔錯誤");
                return;
            }

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(saveFile), "utf8"));

            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(srcFile));
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(bis);
            Sheet sheet = xssfWorkbook.getSheetAt(0);

            LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
            for (int ii = 0; ii < sheet.getRow(0).getLastCellNum(); ii++) {
                valueMap.put(formatCellType(sheet.getRow(0).getCell(ii)), "");
            }

            for (int j = 0; j < sheet.getPhysicalNumberOfRows(); j++) {
                Row row = sheet.getRow(j);
                LinkedHashMap<String, String> valueMap2 = (LinkedHashMap<String, String>) valueMap.clone();
                int ii = 0;
                for (String key : valueMap2.keySet()) {
                    valueMap2.put(key, formatCellType(row.getCell(ii)));
                    ii++;
                }
                appendLog("" + valueMap2);
                String insertSql = this.fetchInsertSQL(tableName, valueMap2);
                appendLog("" + insertSql);
                writer.write(insertSql);
                writer.newLine();
            }
            bis.close();

            writer.flush();
            writer.close();

            JCommonUtil._jOptionPane_showMessageDialog_info("檔案寫入成功 : \n" + saveFile);
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private String fetchInsertSQL(String tableName, Map<String, String> wkDataObjectMapCopy) {
        List<String> insertFieldList = new ArrayList<String>();
        List<String> insertValueList = new ArrayList<String>();
        for (String key : wkDataObjectMapCopy.keySet()) {
            insertFieldList.add(key);
            insertValueList.add(wkDataObjectMapCopy.get(key));
        }
        String inf = insertFieldList.toString();
        inf = inf.replaceAll(" ", "");
        inf = inf.substring(1, inf.length() - 1);
        String inv = insertValueList.toString();
        inv = inv.replaceAll(" ", "");
        inv = inv.replaceAll(",", "','");
        inv = inv.substring(1, inv.length() - 1);
        inv = "'" + inv + "'";
        return "insert into " + tableName + " (" + inf + ") values (" + inv + ");";
    }
}
