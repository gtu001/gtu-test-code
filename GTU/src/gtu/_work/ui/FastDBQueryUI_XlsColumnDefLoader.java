package gtu._work.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import gtu.file.FileUtil;
import gtu.poi.hssf.ExcelUtil_Xls97;
import gtu.runtime.DesktopUtil;
import gtu.string.StringUtil_;
import gtu.swing.util.JTableUtil;

public class FastDBQueryUI_XlsColumnDefLoader {

    List<XlsColumnDefClz> configLst;
    File customDir;

    XlsColumnDefClz columnDef;
    XlsColumnDefClz chineseDef;
    XlsColumnDefClz pkDef;

    public FastDBQueryUI_XlsColumnDefLoader(File customDir) {
        this.customDir = customDir;
    }

    public List<String> getPkList(final String tableName) {
        final TableDef tb = getTable(tableName);
        if (tb == null) {
            System.out.println("查無資料表欄位定義Table : " + tableName);
            return null;
        }
        List<String> pkLst = new ArrayList<String>();
        for (Map<Integer, String> map : tb.columnLst) {
            String column = getPkColumn(map);
            if (StringUtils.isNotBlank(column)) {
                pkLst.add(column);
            }
        }
        return pkLst;
    }

    private String getPkColumn(Map<Integer, String> map) {
        String pk = "";
        if (pkDef != null && pkDef.index != -1) {
            String pkText = StringUtils.trimToEmpty(map.get(pkDef.index));
            String pkLabel = pkDef.label;
            if (StringUtils.isBlank(pkLabel)) {
                pkLabel = pkText;
            }
            if (StringUtils.isNotBlank(pkDef.containText)) {
                if (pkText.toLowerCase().contains(StringUtils.trimToEmpty(pkDef.containText).toLowerCase())) {
                    pk = map.get(columnDef.index);
                }
            } else {
                pk = map.get(columnDef.index);
            }
        }
        return pk;
    }

    public void setMappingConfig(List<XlsColumnDefClz> configLst) {
        this.configLst = configLst;
        for (XlsColumnDefClz c1 : this.configLst) {
            if (c1.type == XlsColumnDefType.CHINESE) {
                chineseDef = c1;
            } else if (c1.type == XlsColumnDefType.COLUMN) {
                columnDef = c1;
            } else if (c1.type == XlsColumnDefType.PK) {
                pkDef = c1;
            }
        }
    }

    public String getDBColumnChinese(final String column, final String tableName) {
        final TableDef tb = getTable(tableName);
        if (tb == null) {
            System.out.println("查無資料表欄位定義Table : " + tableName);
            return null;
        }
        for (Map<Integer, String> map : tb.columnLst) {
            if (StringUtils.equalsIgnoreCase(StringUtils.trimToEmpty(map.get(columnDef.index)), StringUtils.trimToEmpty(column))) {
                return getTooltipFormat(map);
            }
        }
        System.out.println("查無資料表欄位定義 : " + tableName + "." + column);
        return null;
    }

    private String getTagString(XlsColumnDefClz pkDef, Map<Integer, String> map) {
        String pk = "";
        if (pkDef != null && pkDef.index != -1) {
            String pkText = StringUtils.trimToEmpty(map.get(pkDef.index));
            String pkLabel = pkDef.label;
            if (StringUtils.isBlank(pkLabel)) {
                pkLabel = pkText;
            }
            if (StringUtils.isNotBlank(pkDef.containText)) {
                if (pkText.toLowerCase().contains(StringUtils.trimToEmpty(pkDef.containText).toLowerCase())) {
                    pk = "<font color='" + pkDef.color + "'>　" + pkLabel + "</font>";
                }
            } else {
                pk = "<font color='" + pkDef.color + "'>　" + pkLabel + "</font>";
            }
        }
        return pk;
    }

    private String getTooltipFormat(Map<Integer, String> map) {
        String chinese = map.get(chineseDef.index);
        if (StringUtils.isBlank(chinese)) {
            return null;
        }
        String pk = getTagString(pkDef, map);
        StringBuffer sb = new StringBuffer();
        for (XlsColumnDefClz c1 : configLst) {
            if (c1.type == XlsColumnDefType.LABEL) {
                sb.append(getTagString(c1, map));
            }
        }
        return String.format("<html>%s</html>", chinese + pk + sb);
    }

    public Transformer getTableTitleTransformer(final String tableName) {
        final TableDef tb = getTable(tableName);
        if (tb == null) {
            System.out.println("查無資料表欄位定義Table : " + tableName);
            return null;
        }
        return new Transformer() {
            @Override
            public Object transform(Object input) {
                Pair<Integer, Object> p = (Pair<Integer, Object>) input;
                String column = (String) p.getRight();
                for (Map<Integer, String> map : tb.columnLst) {
                    if (StringUtils.equalsIgnoreCase(StringUtils.trimToEmpty(map.get(columnDef.index)), StringUtils.trimToEmpty(column))) {
                        return getTooltipFormat(map);
                    }
                }
                System.out.println("查無資料表欄位定義 : " + tableName + "." + column);
                return null;
            }
        };
    }

    private TableDef getTable(String tableName) {
        tableName = StringUtils.trimToEmpty(tableName);
        for (TableDef tb : tabLst) {
            if (StringUtils.equalsIgnoreCase(StringUtils.trimToEmpty(tb.table), tableName)) {
                return tb;
            }
        }
        return null;
    }

    List<TableDef> tabLst = new ArrayList<TableDef>();

    public void execute() {
        File dir = new File(FileUtil.DESKTOP_DIR, "FastColumnDef");
        if (customDir != null) {
            dir = customDir;
        }
        File[] lst = dir.listFiles();
        if (lst == null) {
            System.out.println("欄位定義目錄不存在 : " + dir);
        } else {
            for (File f : lst) {
                if (f.getName().endsWith(".xls")) {
                    HSSFWorkbook wk = ExcelUtil_Xls97.getInstance().readExcel(f);
                    for (int ii = 0; ii < wk.getNumberOfSheets(); ii++) {
                        HSSFSheet sheet = wk.getSheetAt(ii);
                        String table = sheet.getSheetName();
                        TableDef tb = new TableDef();
                        tb.file = f;
                        tb.table = table;
                        Object[] objs = getColumnDefMap(sheet);
                        tb.columnLst = (List<Map<Integer, String>>) objs[0];
                        tb.columnLstMappingToRowIndexMap = (Map<Map<Integer, String>, Integer>) objs[1];
                        tabLst.add(tb);
                    }
                }
            }
            System.out.println("欄位定義目錄找到table數 : " + tabLst.size());
        }
    }

    private Object[] getColumnDefMap(HSSFSheet sheet) {
        Map<Map<Integer, String>, Integer> columnLstMappingToRowIndexMap = new HashMap<Map<Integer, String>, Integer>();
        List<Map<Integer, String>> colLst = new ArrayList<Map<Integer, String>>();
        for (int ii = 0; ii <= sheet.getLastRowNum(); ii++) {
            Row row = sheet.getRow(ii);
            if (row == null) {
                continue;
            }
            Map<Integer, String> map = new HashMap<Integer, String>();
            for (int jj = 0; jj < row.getLastCellNum(); jj++) {
                String value = ExcelUtil_Xls97.getInstance().readCell(row.getCell(jj));
                map.put(jj, value);
            }
            colLst.add(map);
            columnLstMappingToRowIndexMap.put(map, ii);
        }
        return new Object[] { colLst, columnLstMappingToRowIndexMap };
    }

    private static class TableDef {
        File file;
        String table;
        List<Map<Integer, String>> columnLst = new ArrayList<Map<Integer, String>>();
        Map<Map<Integer, String>, Integer> columnLstMappingToRowIndexMap = new HashMap<Map<Integer, String>, Integer>(); // 設定實際excel的第幾row
                                                                                                                         // mapping
        Set<Integer> qryMatchMarkLst = new TreeSet<Integer>();
    }

    public enum XlsColumnDefType {
        COLUMN("", 0, "", "BLACK"), CHINESE("", 1, "", "BLACK"), PK("", -1, "", "RED"), LABEL("", -1, "", "BLACK");

        String label;
        int index;
        String containText;
        String color;

        XlsColumnDefType(String label, int index, String containText, String color) {
            this.label = label;
            this.index = index;
            this.containText = containText;
            this.color = color;
        }

        public String toString() {
            return name();
        }

        public XlsColumnDefClz getConfig() {
            XlsColumnDefClz c1 = new XlsColumnDefClz();
            c1.type = this;
            c1.label = this.label;
            c1.index = this.index;
            c1.containText = this.containText;
            c1.color = this.color;
            return c1;
        }
    }

    private class QueryHandler {
        String[] tableQryText;
        String[] columnQryText;
        String[] otherText;

        List<TableDef> findTabLst;
        DefaultTableModel model;
        int findRowCount = 0;

        Pattern p1 = Pattern.compile("\\/(.*?)\\/");

        private boolean isRegexMatch(String target, String search) {
            try {
                Matcher mth = p1.matcher(search);
                if (mth.find()) {
                    String ptn = mth.group(1);
                    Pattern ptn2 = Pattern.compile(ptn, Pattern.CASE_INSENSITIVE);
                    Matcher mth2 = ptn2.matcher(target);
                    if (mth2.find()) {
                        return true;
                    }
                }
            } catch (Exception ex) {
                System.out.println("ERR(QueryHandler.isRegexMatch) : " + ex.getMessage());
            }
            return false;
        }

        private List<TableDef> filter() {
            List<TableDef> tabLst2 = new ArrayList<TableDef>();
            for (TableDef tab : tabLst) {
                tab.qryMatchMarkLst = new TreeSet<Integer>();
            }
            if (tableQryText != null && tableQryText.length > 0) {
                A: for (TableDef tab : tabLst) {
                    for (String tabName : tableQryText) {
                        if (tab.table.toLowerCase().contains(tabName.toLowerCase())) {
                            tabLst2.add(tab);
                            continue A;
                        } else if (isRegexMatch(tab.table, tabName)) {
                            tabLst2.add(tab);
                            continue A;
                        }
                    }
                }
            } else {
                tabLst2.addAll(tabLst);
            }
            boolean isChk = false;
            if (columnQryText != null && columnQryText.length > 0) {
                for (TableDef tab : tabLst2) {
                    for (String columnText : columnQryText) {
                        isChk = true;
                        A: for (int ii = 0; ii < tab.columnLst.size(); ii++) {
                            Map<Integer, String> map = tab.columnLst.get(ii);
                            String column = StringUtils.trimToEmpty(map.get(columnDef.index));
                            if (StringUtils.isNotBlank(column)) {
                                if (column.toLowerCase().contains(columnText.toLowerCase())) {
                                    tab.qryMatchMarkLst.add(ii);
                                    continue A;
                                } else if (isRegexMatch(column, columnText)) {
                                    tab.qryMatchMarkLst.add(ii);
                                    continue A;
                                }
                            }
                        }
                    }
                }
            }
            if (otherText != null && otherText.length > 0) {
                for (TableDef tab : tabLst2) {
                    for (String otherTxt : otherText) {
                        isChk = true;
                        A: for (int ii = 0; ii < tab.columnLst.size(); ii++) {
                            Map<Integer, String> map = tab.columnLst.get(ii);
                            for (Integer keyIdx : map.keySet()) {
                                if (keyIdx != columnDef.index) {
                                    String other = StringUtils.trimToEmpty(map.get(keyIdx));
                                    if (StringUtils.isNotBlank(other)) {
                                        if (other.toLowerCase().contains(otherTxt.toLowerCase())) {
                                            tab.qryMatchMarkLst.add(ii);
                                            continue A;
                                        } else if (isRegexMatch(other, otherTxt)) {
                                            tab.qryMatchMarkLst.add(ii);
                                            continue A;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (!isChk) {
                for (TableDef tab : tabLst2) {
                    for (int ii = 0; ii < tab.columnLst.size(); ii++) {
                        tab.qryMatchMarkLst.add(ii);
                    }
                }
            }
            return tabLst2;
        }

        private DefaultTableModel toModel() {
            Object[] titles = new Object[150];
            titles[0] = "file";
            titles[1] = "sheet";
            titles[2] = "row";
            for (int ii = 3; ii < titles.length; ii++) {
                titles[ii] = ExcelUtil_Xls97.getInstance().cellEnglishToPos(ii - 3 + 1);
            }
            DefaultTableModel model = JTableUtil.createModel(true, titles);
            for (final TableDef tab : findTabLst) {
                for (Integer colIdx : tab.qryMatchMarkLst) {
                    Map<Integer, String> map = tab.columnLst.get(colIdx);
                    LinkedList<Object> arry = new LinkedList<Object>(map.values());
                    arry.addFirst(tab.columnLstMappingToRowIndexMap.get(map) + 1); // 設定實際excel的第幾row
                                                                                   // mapping
                    arry.addFirst(tab.table);
                    JButton btn = new JButton(tab.file.getName());
                    btn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            DesktopUtil.browseFileDirectory(tab.file);
                        }
                    });
                    arry.addFirst(btn);
                    if (!StringUtil_.hasChineseWord2(tab.table) && //
                            StringUtils.isNotBlank(map.get(columnDef.index)) && //
                            StringUtils.isNotBlank(map.get(chineseDef.index))) {
                        model.addRow(arry.toArray());
                        findRowCount++;
                    }
                }
            }
            return model;
        }

        private void afterSetToModel(JTable jtable, JFrame jframe) {
            JTableUtil.newInstance(jtable).columnIsButton("file");

            Map<String, Object> preferences = new HashMap<String, Object>();
            Map<Integer, Integer> presetColumns = new HashMap<Integer, Integer>();
            presetColumns.put(0, 150);
            preferences.put("presetColumns", presetColumns);
            JTableUtil.setColumnWidths_ByDataContent(jtable, preferences, jframe.getInsets());
        }

        public QueryHandler(String tableQry, String columnQry, String otherQry, JTable jtable) {
            if (StringUtils.isNotBlank(tableQry)) {
                tableQryText = StringUtils.trimToEmpty(tableQry).split("\\^", -1);
            }
            if (StringUtils.isNotBlank(columnQry)) {
                columnQryText = StringUtils.trimToEmpty(columnQry).split("\\^", -1);
            }
            if (StringUtils.isNotBlank(otherQry)) {
                otherText = StringUtils.trimToEmpty(otherQry).split("\\^", -1);
            }
            findTabLst = filter();
            model = toModel();
        }
    }

    public Triple<DefaultTableModel, Integer, ActionListener> query(final String tableQry, final String columnQry, final String otherQry, final JTable jtable, final JFrame jframe) {
        final QueryHandler mQueryHandler = new QueryHandler(tableQry, columnQry, otherQry, jtable);
        final ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mQueryHandler.afterSetToModel(jtable, jframe);
            }
        };
        return Triple.of(mQueryHandler.model, mQueryHandler.findRowCount, listener);
    }

    public static class XlsColumnDefClz {
        XlsColumnDefType type;// 類型
        String label = ""; // 標籤字
        int index = -1;// index
        String containText = "";// 含有文字
        String color = "BLACK";// 顏色

        public String toConfig() {
            return type.name() + "^" + //
                    StringUtils.trimToEmpty(label) + "^" + //
                    String.valueOf(index) + "^" + //
                    StringUtils.trimToEmpty(containText) + "^" + //
                    StringUtils.trimToEmpty(color) + //
                    "";
        }

        private String getArryStr(String[] arry, int index) {
            if (index < arry.length) {
                return StringUtils.trimToEmpty(arry[index]);
            }
            return "";
        }

        public Object[] toArray() {
            return new Object[] { //
                    type, //
                    StringUtils.trimToEmpty(label), //
                    index, //
                    StringUtils.trimToEmpty(containText), //
                    StringUtils.trimToEmpty(color) //
            };
        }

        public void fromConfig(String value) {
            String[] arry = StringUtils.trimToEmpty(value).split("\\^", -1);
            type = XlsColumnDefType.valueOf(getArryStr(arry, 0));
            label = getArryStr(arry, 1);
            index = Integer.parseInt(getArryStr(arry, 2));
            containText = getArryStr(arry, 3);
            color = getArryStr(arry, 4);
        }
    }
}
