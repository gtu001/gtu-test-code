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

    int columnIdx;
    int chineseIdx;
    int pkIdx;
    int fkIdx;
    File customDir;

    public FastDBQueryUI_XlsColumnDefLoader(File customDir) {
        this.customDir = customDir;
    }

    public void setMappingIndex(int columnIdx, int chineseIdx, int pkIdx, int fkIdx) {
        this.columnIdx = columnIdx;
        this.chineseIdx = chineseIdx;
        this.pkIdx = pkIdx;
        this.fkIdx = fkIdx;
    }

    public String getDBColumnChinese(final String column, final String tableName) {
        final TableDef tb = getTable(tableName);
        if (tb == null) {
            System.out.println("查無資料表欄位定義Table : " + tableName);
            return null;
        }
        for (Map<Integer, String> map : tb.columnLst) {
            if (StringUtils.equalsIgnoreCase(map.get(columnIdx), column)) {
                return getTooltipFormat(map.get(chineseIdx), map.get(pkIdx), map.get(fkIdx));
            }
        }
        System.out.println("查無資料表欄位定義 : " + tableName + "." + column);
        return null;
    }

    private String getTooltipFormat(String chinese, String pk, String fk) {
        pk = StringUtils.trimToEmpty(pk);
        if (StringUtils.isNotBlank(pk)) {
            pk = "　" + pk;
        }
        fk = StringUtils.trimToEmpty(fk);
        if (StringUtils.isNotBlank(fk)) {
            fk = "　" + fk;
        }
        return String.format("<html>%s<font color='red'>%s</font><font color='blue'>%s</font></html>", chinese, pk, fk);
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
                    if (StringUtils.equalsIgnoreCase(map.get(columnIdx), column)) {
                        return getTooltipFormat(map.get(chineseIdx), map.get(pkIdx), map.get(fkIdx));
                    }
                }
                System.out.println("查無資料表欄位定義 : " + tableName + "." + column);
                return null;
            }
        };
    }

    private TableDef getTable(String tableName) {
        for (TableDef tb : tabLst) {
            if (StringUtils.equalsIgnoreCase(tb.table, tableName)) {
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
}
