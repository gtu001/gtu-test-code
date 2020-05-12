package gtu._work.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import gtu.file.FileUtil;
import gtu.poi.hssf.ExcelUtil_Xls97;

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
                        tb.columnLst = getColumnDefMap(sheet);
                        tabLst.add(tb);
                    }
                }
            }
            System.out.println("欄位定義目錄找到table數 : " + tabLst.size());
        }
    }

    private List<Map<Integer, String>> getColumnDefMap(HSSFSheet sheet) {
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
        }
        return colLst;
    }

    private static class TableDef {
        File file;
        String table;
        List<Map<Integer, String>> columnLst = new ArrayList<Map<Integer, String>>();
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
