package gtu.db.sqlMaker;

import gtu.db.sqlMaker.DbSqlCreater.TableInfo;
import gtu.file.FileUtil;
import gtu.poi.hssf.ExcelUtil;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class LoadXlsToSQL {

    static File excel = new File("C:/Users/gtu001/Desktop/workspace/新增資料夾/親等舊系統測試資料/");

    public static void main(String[] args) throws Exception {
        LoadXlsToSQL load = new LoadXlsToSQL();
        for (File file : excel.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.matches(".*\\.xls");
            }
        })) {
            HSSFWorkbook book = ExcelUtil.getInstance().readExcel(file);
            HSSFSheet sheet = book.getSheetAt(0);
            System.out.println("SheetName : " + sheet.getSheetName());
            Connection conn = DriverManager.getConnection("jdbc:hsqldb:file:/c:/kfrApplet_db/testdb;shutdown=true", "SA", "");
            TableInfo info = new TableInfo();
            String tableName = file.getName().replaceAll(".xls", "");
            System.out.println("tableName ========" + tableName);
            info.execute("SELECT * FROM " + tableName, conn);
            String sqlTemp = null;
            StringBuilder sb = new StringBuilder();
            for (int ii = 1; ii <= sheet.getLastRowNum(); ii++) {
                Map<String, String> dataMap = load.getHorizontalDataMap(0, ii, sheet);
                sqlTemp = info.createInsertSql(dataMap);
                System.out.println(sqlTemp);
                sb.append(sqlTemp + "\n");
            }
            FileUtil.saveToFile(FileUtil.getIndicateFileExtension(file, ".sql"), sb.toString(), "BIG5");
        }

        System.out.println("done...");
    }

    private Map<String, String> getHorizontalDataMap(int keyPos, int valuePos, HSSFSheet sheet) {
        HSSFCell cell = null;
        String key = null;
        String value = null;
        Map<String, String> map = new LinkedHashMap<String, String>();
        HSSFRow keyRow = sheet.getRow(keyPos);
        HSSFRow valueRow = sheet.getRow(valuePos);
        for (int ii = 0; ii < keyRow.getLastCellNum(); ii++) {
            cell = keyRow.getCell(ii);
            if (cell != null) {
                key = ExcelUtil.getInstance().readHSSFCell(cell);
            }
            cell = valueRow.getCell(ii);
            if (cell != null) {
                value = ExcelUtil.getInstance().readHSSFCell(cell);
            }
            map.put(key, value);
        }
        return map;
    }

    private Map<String, String> getVerticalDataMap(int keyPos, int valuePos, HSSFSheet sheet) {
        HSSFRow row = null;
        HSSFCell cell = null;
        String key = null;
        String value = null;
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (int ii = 0; ii <= sheet.getLastRowNum(); ii++) {
            row = sheet.getRow(ii);
            cell = row.getCell(keyPos);
            if (cell != null) {
                key = ExcelUtil.getInstance().readHSSFCell(cell);
            } else {
                System.err.format("KEY[row:%d, col:%d], cell is null\n", ii, keyPos);
                continue;
            }
            if (map.containsKey(key)) {
                System.err.format("KEY[row:%d, col:%d], key is duplicate\n", ii, keyPos);
            }

            cell = row.getCell(valuePos);
            if (cell != null) {
                value = ExcelUtil.getInstance().readHSSFCell(cell);
            }

            // XXX
            key = key.toLowerCase();
            // XXX
            map.put(key, value);
        }
        return map;
    }
}
