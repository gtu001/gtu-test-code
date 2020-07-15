package gtu._work.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;
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
import gtu.regex.RegExpUtil;
import gtu.runtime.DesktopUtil;
import gtu.string.StringUtil_;
import gtu.swing.util.JTableUtil;

public class FastDBQueryUI_XlsColumnDefLoader {

    List<XlsColumnDefClz> configLst;
    File customDir;

    XlsColumnDefClz columnDef;
    XlsColumnDefClz chineseDef;
    XlsColumnDefClz pkDef;
    XlsColumnDefClz tableDef;

    List<TableDef> tabLst = new ArrayList<TableDef>();
    private ActionListener loadingInfoListener;

    public FastDBQueryUI_XlsColumnDefLoader(File customDir, List<XlsColumnDefClz> configLst) {
        this.customDir = customDir;
        if (configLst != null) {
            setMappingConfig(configLst);
        }
    }

    public List<String> getColumnLst(final String tableName) {
        final List<TableDef> tbs = getTable(tableName);
        if (tbs.isEmpty()) {
            System.out.println("查無資料表欄位定義Table : " + tableName);
            return null;
        }
        List<String> colLst = new ArrayList<String>();
        for (TableDef tb : tbs) {
            for (Map<Integer, String> map : tb.columnLst) {
                String column = map.get(columnDef.index);
                if (StringUtils.isNotBlank(column) && !colLst.contains(column)) {
                    if (isColumnValid(columnDef, map)) {
                        colLst.add(column);
                    }
                }
            }
        }
        return colLst;
    }

    public List<String> getPkList(final String tableName) {
        final List<TableDef> tbs = getTable(tableName);
        if (tbs.isEmpty()) {
            System.out.println("查無資料表欄位定義Table : " + tableName);
            return null;
        }
        List<String> pkLst = new ArrayList<String>();
        for (TableDef tb : tbs) {
            for (Map<Integer, String> map : tb.columnLst) {
                String column = getPkColumn(map);
                if (StringUtils.isNotBlank(column)) {
                    pkLst.add(column);
                }
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
            if (isColumnValid(pkDef, map)) {
                pk = map.get(columnDef.index);
            }
        }
        return pk;
    }

    public void setMappingConfig(List<XlsColumnDefClz> configLst) {
        if (configLst == null) {
            configLst = Collections.EMPTY_LIST;
        }
        this.configLst = configLst;
        for (XlsColumnDefClz c1 : this.configLst) {
            if (c1.type == XlsColumnDefType.CHINESE) {
                chineseDef = c1;
            } else if (c1.type == XlsColumnDefType.COLUMN) {
                columnDef = c1;
            } else if (c1.type == XlsColumnDefType.PK) {
                pkDef = c1;
            } else if (c1.type == XlsColumnDefType.TABLE) {
                tableDef = c1;
            }
        }
    }

    public String getDBColumnChinese(final String column, boolean forTooltip, final String tableName) {
        final List<TableDef> tbs = getTable(tableName);
        if (tbs.isEmpty()) {
            System.out.println("查無資料表欄位定義Table : " + tableName);
            return null;
        }
        for (TableDef tb : tbs) {
            for (Map<Integer, String> map : tb.columnLst) {
                if (!isColumnValid(columnDef, map)) {
                    continue;
                }
                if (StringUtils.equalsIgnoreCase(StringUtils.trimToEmpty(map.get(columnDef.index)), StringUtils.trimToEmpty(column)) && //
                        StringUtils.isNotBlank(map.get(chineseDef.index))) {
                    return getTooltipFormat(map, forTooltip);
                }
            }
        }
        System.out.println("查無資料表欄位定義 : " + tableName + "." + column);
        return forTooltip ? null : "";
    }

    private String getTagString(XlsColumnDefClz pkDef, Map<Integer, String> map) {
        String pk = "";
        if (pkDef != null && pkDef.index != -1) {
            String pkText = StringUtils.trimToEmpty(map.get(pkDef.index));
            String pkLabel = pkDef.label;
            if (StringUtils.isBlank(pkLabel)) {
                pkLabel = pkText;
            }
            if (isColumnValid(pkDef, map)) {
                pk = "<font color='" + pkDef.color + "'>　" + pkLabel + "</font>";
            }
        }
        return pk;
    }

    private String getTooltipFormat(Map<Integer, String> map, boolean forTooltip) {
        String chinese = map.get(chineseDef.index);
        if (!forTooltip) {
            return StringUtils.trimToEmpty(chinese);
        }
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
        final List<TableDef> tbs = getTable(tableName);
        if (tbs.isEmpty()) {
            System.out.println("查無資料表欄位定義Table : " + tableName);
            return null;
        }
        return new Transformer() {
            @Override
            public Object transform(Object input) {
                Pair<Integer, Object> p = (Pair<Integer, Object>) input;
                String column = (String) p.getRight();
                for (TableDef tb : tbs) {
                    for (Map<Integer, String> map : tb.columnLst) {
                        if (StringUtils.equalsIgnoreCase(StringUtils.trimToEmpty(map.get(columnDef.index)), StringUtils.trimToEmpty(column)) && //
                        StringUtils.isNotBlank(map.get(chineseDef.index))) {
                            return getTooltipFormat(map, true);
                        }
                    }
                }
                System.out.println("查無資料表欄位定義 : " + tableName + "." + column);
                return null;
            }
        };
    }

    private List<TableDef> getTable(String tableName) {
        tableName = StringUtils.trimToEmpty(tableName);
        List<TableDef> rtnLst = new ArrayList<TableDef>();
        for (TableDef tb : tabLst) {
            if (StringUtils.equalsIgnoreCase(StringUtils.trimToEmpty(tb.table), tableName)) {
                rtnLst.add(tb);
            }
        }
        return rtnLst;
    }

    AtomicReference<String> loadingInfoHoldler = new AtomicReference<String>();

    private void setLoadingInfo(File file, String message) {
        String allMessage = "[setLoadingInfo] file : " + (file != null ? file.getName() : "NA") + " -- " + message;
        if (loadingInfoHoldler.get() == null || !StringUtils.equals(allMessage, loadingInfoHoldler.get())) {
            if (loadingInfoListener != null) {
                Map<String, Object> info = new HashMap<String, Object>();
                info.put("file", file);
                info.put("message", message);
                loadingInfoListener.actionPerformed(new ActionEvent(info, -1, "info"));
            }
            System.out.println(allMessage);
            loadingInfoHoldler.set(allMessage);
        }
    }

    public void execute() {
        if (configLst == null || configLst.isEmpty()) {
            System.out.println(this.getClass().getSimpleName() + " 初始化未完成！...");
            return;
        }
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
                    setLoadingInfo(f, "");
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
                        setLoadingInfo(f, "...表 : " + table);
                    }
                }
            }
            if (tableDef != null && tableDef.index >= 0) {
                for (File f : lst) {
                    if (f.getName().endsWith(".xls")) {
                        TempTableHolder mTempTableHolder = new TempTableHolder();
                        HSSFWorkbook wk = ExcelUtil_Xls97.getInstance().readExcel(f);
                        for (int ii = 0; ii < wk.getNumberOfSheets(); ii++) {
                            HSSFSheet sheet = wk.getSheetAt(ii);
                            // =====================================
                            String tableName = getTableNameFromSheet(sheet, tableDef);
                            setLoadingInfo(f, "...表 : " + StringUtils.trimToEmpty(tableName));
                            // =====================================
                            this.processColumnDefMap_ForTableDef(mTempTableHolder, sheet, tableDef, tableName);
                        }
                        for (String tableName : mTempTableHolder.getTables()) {
                            TableDef tb = new TableDef();
                            tb.file = f;
                            tb.table = tableName;
                            tb.columnLst = mTempTableHolder.getColLst(tableName);
                            tb.columnLstMappingToRowIndexMap = mTempTableHolder.getColumnLstMappingToRowIndexMap(tableName);
                            tabLst.add(tb);
                        }
                    }
                }
            }
            System.out.println("欄位定義目錄找到table數 : " + tabLst.size());
        }
    }

    private String getTableNameFromSheet(HSSFSheet sheet, XlsColumnDefClz tableDef) {
        if (StringUtils.isBlank(tableDef.containText)) {
            return null;
        }
        org.apache.poi.ss.usermodel.Cell cell = ExcelUtil_Xls97.getInstance().getCellByExcelPos(tableDef.containText, sheet);
        if (cell == null) {
            return null;
        }
        return StringUtils.trimToEmpty(cell.getStringCellValue());
    }

    private class TempTableHolder {
        public Set<String> getTables() {
            return colLstMap.keySet();
        }

        Map<String, Map<Map<Integer, String>, Integer>> columnLstMappingToRowIndexMapMap = new HashMap<String, Map<Map<Integer, String>, Integer>>();

        Map<String, List<Map<Integer, String>>> colLstMap = new HashMap<String, List<Map<Integer, String>>>();

        public Map<Map<Integer, String>, Integer> getColumnLstMappingToRowIndexMap(String tableName) {
            return columnLstMappingToRowIndexMapMap.get(tableName);
        }

        public List<Map<Integer, String>> getColLst(String tableName) {
            return colLstMap.get(tableName);
        }

        public void appendColLst(String tableName, Map<Integer, String> map) {
            List<Map<Integer, String>> colLst = new ArrayList<Map<Integer, String>>();
            if (colLstMap.containsKey(tableName)) {
                colLst = colLstMap.get(tableName);
            }
            // =====================================
            boolean addOk = false;
            if (columnDef != null && isColumnValid(columnDef, map)) {
                addOk = true;
            }
            // =====================================
            if (addOk) {
                colLst.add(map);
                colLstMap.put(tableName, colLst);
            }
        }

        public void appendColumnLstMappingToRowIndexMap(String tableName, Map<Integer, String> map, int index) {
            Map<Map<Integer, String>, Integer> columnLstMappingToRowIndexMap = new HashMap<Map<Integer, String>, Integer>();
            if (columnLstMappingToRowIndexMapMap.containsKey(tableName)) {
                columnLstMappingToRowIndexMap = columnLstMappingToRowIndexMapMap.get(tableName);
            }
            // =====================================
            boolean addOk = false;
            if (columnDef != null && isColumnValid(columnDef, map)) {
                addOk = true;
            }
            // =====================================
            if (addOk) {
                columnLstMappingToRowIndexMap.put(map, index);
                columnLstMappingToRowIndexMapMap.put(tableName, columnLstMappingToRowIndexMap);
            }
        }
    }

    private void processColumnDefMap_ForTableDef(TempTableHolder mTempTableHolder, HSSFSheet sheet, XlsColumnDefClz tableDef, String tableNameRef) {
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
            String tableName = tableNameRef;
            if (StringUtils.isBlank(tableName)) {
                tableName = map.get(tableDef.index);
            }
            if (StringUtils.isBlank(tableName)) {
                continue;
            }
            setLoadingInfo(null, "...表 : " + StringUtils.trimToEmpty(tableName));
            mTempTableHolder.appendColLst(tableName, map);
            mTempTableHolder.appendColumnLstMappingToRowIndexMap(tableName, map, ii);
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
            // =====================================
            boolean addOk = false;
            if (columnDef != null && isColumnValid(columnDef, map)) {
                addOk = true;
            }
            // =====================================
            if (addOk) {
                colLst.add(map);
                columnLstMappingToRowIndexMap.put(map, ii);
            }
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
        TABLE("", -1, "", "BLACK"), //
        COLUMN("", 0, "", "BLACK"), //
        CHINESE("", 1, "", "BLACK"), //
        PK("", -1, "", "RED"), //
        LABEL("", -1, "", "BLACK"),//
        ;

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

    // ==========================================================================================
    public static class RegExpAndTextFilter {
        private static final Pattern CONDITION_PTN = Pattern.compile("\\/(.*?)\\/");

        public static Pair<String[], Pattern[]> toSearchCondition(String text) {
            Matcher mth = CONDITION_PTN.matcher(text);
            List<Pattern> ptnLst = new ArrayList<Pattern>();
            List<String> paramLst = new ArrayList<String>();
            StringBuffer sb = new StringBuffer();
            while (mth.find()) {
                try {
                    ptnLst.add(Pattern.compile(mth.group(1), Pattern.CASE_INSENSITIVE));
                    mth.appendReplacement(sb, "");
                } catch (Exception ex) {
                }
            }
            mth.appendTail(sb);
            String[] parameters = sb.toString().split("\\^", -1);
            for (int ii = 0; ii < parameters.length; ii++) {
                if (StringUtils.isNotBlank(parameters[ii])) {
                    paramLst.add(parameters[ii]);
                }
            }
            return Pair.of(paramLst.toArray(new String[0]), ptnLst.toArray(new Pattern[0]));
        }

        public static boolean isEmptyCondition(Pair<String[], Pattern[]> condition) {
            if (condition == null) {
                return true;
            }
            if ((condition.getLeft() == null || condition.getLeft().length == 0) && //
                    (condition.getRight() == null || condition.getRight().length == 0)) {
                return true;
            }
            return false;
        }

        public static boolean isRegexMatch(String target, Pattern[] search) {
            if (search == null || search.length == 0) {
                return false;
            }
            if (search != null && search.length > 0) {
                for (Pattern ptn : search) {
                    if (ptn.matcher(target).find()) {
                        return true;
                    }
                }
            }
            return false;
        }

        public static boolean isTextContain(String text, String[] condition) {
            if (condition == null || condition.length == 0) {
                return false;
            }
            for (String txt : condition) {
                if (StringUtils.isNotBlank(txt)) {
                    if (text.toLowerCase().contains(txt.toLowerCase())) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
    // ==========================================================================================

    private class QueryHandler {
        String[] tableQryText;
        Pattern[] tableQryPtn;
        String[] columnQryText;
        Pattern[] columnQryPtn;
        String[] otherText;
        Pattern[] otherPtn;

        List<TableDef> findTabLst;
        DefaultTableModel model;
        int findRowCount = 0;
        boolean hasChinese;

        private int DEFAULT_EXCEL_TITLES_COUNT = 50;

        private boolean isConditionOk(String[] param, Pattern[] ptns) {
            if (param != null && param.length > 0) {
                return true;
            }
            if (ptns != null && ptns.length > 0) {
                return true;
            }
            return false;
        }

        private List<TableDef> filter() {
            List<TableDef> tabLst2 = new ArrayList<TableDef>();
            for (TableDef tab : tabLst) {
                tab.qryMatchMarkLst = new TreeSet<Integer>();
            }
            if (isConditionOk(tableQryText, tableQryPtn)) {
                A: for (TableDef tab : tabLst) {
                    for (String tabName : tableQryText) {
                        if (tab.table.toLowerCase().contains(tabName.toLowerCase())) {
                            tabLst2.add(tab);
                            continue A;
                        }
                    }
                    if (RegExpAndTextFilter.isRegexMatch(tab.table, tableQryPtn)) {
                        tabLst2.add(tab);
                        continue A;
                    }
                }
            } else {
                tabLst2.addAll(tabLst);
            }

            boolean isChk = false;

            if (isConditionOk(columnQryText, columnQryPtn)) {
                isChk = true;
                for (TableDef tab : tabLst2) {
                    A: for (int ii = 0; ii < tab.columnLst.size(); ii++) {
                        Map<Integer, String> map = tab.columnLst.get(ii);
                        String column = StringUtils.trimToEmpty(map.get(columnDef.index));

                        for (String columnText : columnQryText) {
                            if (StringUtils.isNotBlank(column)) {
                                if (column.toLowerCase().contains(columnText.toLowerCase())) {
                                    tab.qryMatchMarkLst.add(ii);
                                    continue A;
                                }
                            }
                        }
                        if (RegExpAndTextFilter.isRegexMatch(column, columnQryPtn)) {
                            tab.qryMatchMarkLst.add(ii);
                            continue A;
                        }
                    }
                }
            }

            if (isConditionOk(otherText, otherPtn)) {
                isChk = true;
                for (TableDef tab : tabLst2) {
                    A: for (int ii = 0; ii < tab.columnLst.size(); ii++) {
                        Map<Integer, String> map = tab.columnLst.get(ii);

                        for (Integer keyIdx : map.keySet()) {
                            if (keyIdx != columnDef.index) {
                                String other = StringUtils.trimToEmpty(map.get(keyIdx));

                                for (String otherTxt : otherText) {
                                    if (StringUtils.isNotBlank(other)) {
                                        if (other.toLowerCase().contains(otherTxt.toLowerCase())) {
                                            tab.qryMatchMarkLst.add(ii);
                                            continue A;
                                        }
                                    }
                                }
                                if (RegExpAndTextFilter.isRegexMatch(other, otherPtn)) {
                                    tab.qryMatchMarkLst.add(ii);
                                    continue A;
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
            Object[] titles = new Object[DEFAULT_EXCEL_TITLES_COUNT];
            titles[0] = "file";
            titles[1] = "sheet(table)";
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
                            StringUtils.isNotBlank(map.get(columnDef.index))) {
                        boolean addOk = false;
                        if (this.hasChinese && StringUtils.isNotBlank(map.get(chineseDef.index))) {
                            addOk = true;
                        } else if (!this.hasChinese) {
                            addOk = true;
                        }
                        if (addOk) {
                            model.addRow(arry.toArray());
                            findRowCount++;
                        }
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
            preferences.put("maxWidth", 500);
            JTableUtil.setColumnWidths_ByDataContent(jtable, preferences, jframe.getInsets());//
        }

        public QueryHandler(String tableQry, String columnQry, String otherQry, final boolean hasChinese, JTable jtable) {
            this.hasChinese = hasChinese;
            if (StringUtils.isNotBlank(tableQry)) {
                Pair<String[], Pattern[]> val = RegExpAndTextFilter.toSearchCondition(tableQry);
                tableQryText = val.getLeft();
                tableQryPtn = val.getRight();
            }
            if (StringUtils.isNotBlank(columnQry)) {
                Pair<String[], Pattern[]> val = RegExpAndTextFilter.toSearchCondition(columnQry);
                columnQryText = val.getLeft();
                columnQryPtn = val.getRight();
            }
            if (StringUtils.isNotBlank(otherQry)) {
                Pair<String[], Pattern[]> val = RegExpAndTextFilter.toSearchCondition(otherQry);
                otherText = val.getLeft();
                otherPtn = val.getRight();
            }
            findTabLst = filter();
            model = toModel();
        }
    }

    public Triple<DefaultTableModel, Integer, ActionListener> query(final String tableQry, final String columnQry, final String otherQry, final boolean hasChinese, final JTable jtable,
            final JFrame jframe) {
        final QueryHandler mQueryHandler = new QueryHandler(tableQry, columnQry, otherQry, hasChinese, jtable);
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
        int refIndex = -1;// ref index
        String containText = "";// 含有文字
        String color = "BLACK";// 顏色

        public String toConfig() {
            return type.name() + "^" + //
                    StringUtils.trimToEmpty(label) + "^" + //
                    String.valueOf(index) + "^" + //
                    StringUtils.trimToEmpty(containText) + "^" + //
                    StringUtils.trimToEmpty(color) + "^" + //
                    String.valueOf(refIndex) + "^" + //
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
                    StringUtils.trimToEmpty(color), //
                    refIndex, //
            };
        }

        public void fromConfig(String value) {
            String[] arry = StringUtils.trimToEmpty(value).split("\\^", -1);
            type = XlsColumnDefType.valueOf(getArryStr(arry, 0));
            label = getArryStr(arry, 1);
            index = Integer.parseInt(getArryStr(arry, 2));
            containText = getArryStr(arry, 3);
            color = getArryStr(arry, 4);
            refIndex = Integer.parseInt(getArryStr(arry, 2));
        }
    }

    private boolean isColumnValid(XlsColumnDefClz pkDef, Map<Integer, String> map) {
        if (StringUtils.isBlank(pkDef.containText)) {
            return true;
        } else {
            String pkText = StringUtils.trimToEmpty(map.get(pkDef.index));
            if (pkDef.refIndex != -1) {
                pkText = StringUtils.trimToEmpty(map.get(pkDef.refIndex));
            }
            String text = StringUtils.trimToEmpty(pkDef.containText);
            {
                // 下列欄位都不為空 則成立
                String hasText = RegExpUtil.find("isNot(?:Blank|Empty)\\((.*?)\\)", text, 1, false);
                if (hasText != null) {
                    String[] arry = hasText.split("\\,", -1);
                    for (String hText : arry) {
                        int excelPos = ExcelUtil_Xls97.cellEnglishToPos(hText);
                        if (StringUtils.isBlank(map.get(excelPos))) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            {
                // 下列欄位都為空 則成立
                String hasText = RegExpUtil.find("is(?:Blank|Empty)\\((.*?)\\)", text, 1, false);
                if (hasText != null) {
                    String[] arry = hasText.split("\\,", -1);
                    for (String hText : arry) {
                        int excelPos = ExcelUtil_Xls97.cellEnglishToPos(hText);
                        if (StringUtils.isNotBlank(map.get(excelPos))) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            {
                // 若有資料 則成立
                String hasText = RegExpUtil.find("has\\((.*?)\\)", text, 1, false);
                if (hasText != null) {
                    String[] arry = hasText.split("\\,", -1);
                    for (String hText : arry) {
                        if (StringUtils.isNotBlank(hText) && pkText.toLowerCase().contains(StringUtils.trimToEmpty(hText).toLowerCase())) {
                            return true;
                        }
                    }
                }
            }
            {
                // 若無資料 則成立
                String hasText = RegExpUtil.find("\\!has\\((.*?)\\)", text, 1, false);
                if (hasText != null) {
                    String[] arry = hasText.split("\\,", -1);
                    for (String hText : arry) {
                        if (StringUtils.isNotBlank(hText) && pkText.toLowerCase().contains(StringUtils.trimToEmpty(hText).toLowerCase())) {
                            return false;
                        }
                    }
                }
            }
            {
                // 若符合pattern 則成立
                String hasText = RegExpUtil.find("pattern\\((.*?)\\)", text, 1, false);
                if (hasText != null) {
                    try {
                        Pattern ptn = Pattern.compile(hasText, Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
                        Matcher mth = ptn.matcher(pkText);
                        if (mth.find()) {
                            return true;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            {
                if (pkText.toLowerCase().contains(StringUtils.trimToEmpty(text).toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setLoadingInfoListener(ActionListener loadingInfoListener) {
        this.loadingInfoListener = loadingInfoListener;
    }
}
