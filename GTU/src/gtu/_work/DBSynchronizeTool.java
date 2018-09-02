package gtu._work;

import gtu.db.DbConstant;
import gtu.db.JdbcDBUtil;
import gtu.poi.hssf.ExcelUtil;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;

public class DBSynchronizeTool {

    private static final String TAG_TABLE = "#tablename";
    private static final String TAG_COLUMN = "#column";
    private static final String TAG_DATA = "#data";

    public static void main(String[] args) throws Exception {
//        File file = new File("C:/Users/gtu001/Desktop/test001.xls");
//        File file = new File("C:/Users/gtu001_5F/Desktop/a090.xls");
//        File file = new File("C:/Users/gtu001_5F/Desktop/資料案例/dbaC020.xls");
//        File file = new File("D:/gtu001Dropbox/Dropbox/guava/秀娟工作/資料案例/MktA080.xls");
//        File file = new File("C:/Users/gtu001_5F/Desktop/資料案例/dbaC025.xls");
//        File file = new File("D:/gtu001Dropbox/Dropbox/guava/秀娟工作/資料案例/DbaC020.xls");
//        File file = new File("D:/gtu001Dropbox/Dropbox/guava/秀娟工作/資料案例/MktA080_1.xls");
        File file = new File("C:/Users/gtu001/Dropbox/guava/秀娟工作/資料案例/MktA080_1.xls");
        new DBSynchronizeTool().execute(file);
        System.out.println("done...");
    }
    
    public void execute(File file) throws Exception{
        ExcelUtil util = ExcelUtil.getInstance();
        HSSFWorkbook book = util.readExcel(file);

        HSSFSheet sheet = book.getSheetAt(0);

        List<TableInfo> tabList = readInfo(sheet);

        for (TableInfo tab : tabList) {
            String tableName = tab.tableName;
            System.out.println("tableName = " + tableName);

            gtu.db.sqlMaker.DbSqlCreater.TableInfo info = new gtu.db.sqlMaker.DbSqlCreater.TableInfo();
            info.setTableName(tableName);
            info.execute("select * from " + tableName + " where rownum = 0 ", getConnection());

            tab.execute();
            List<Map<String, String>> dataList = tab.dataList;
            List<Map<String, String>> pkList = tab.pkList;
            for (int ii = 0; ii < dataList.size(); ii++) {

                Map<String, String> param = dataList.get(ii);
                Map<String, String> pkParam = new HashMap<String, String>();
                
                if (pkList.size() > ii) {
                    pkParam = pkList.get(ii);
                }

                info.setPkColumns(pkParam.keySet());

                String selectSql = info.createSelectSql(pkParam);
                System.out.println(selectSql);

                List<Map<String, Object>> queryList = JdbcDBUtil.queryForList(selectSql, getConnection(), true);
                if (queryList.isEmpty()) {
                    Validate.isTrue(!pkParam.isEmpty(), "pkParam 不可為空");
                    String updateSql = info.createUpdateSql(param, pkParam, true);
                    int result = JdbcDBUtil.modify(updateSql, null, getConnection(), true);
                    System.out.println("更新結果 :" + result);

                    if (result == 0) {
                        String insertSql = info.createInsertSql(param);
                        result = JdbcDBUtil.modify(insertSql, null, getConnection(), true);
                        System.out.println("新增結果 :" + result);
                    }
                } else {
                    Validate.isTrue(!pkParam.isEmpty(), "pkParam 不可為空");
                    System.out.println("已存在資料!");
                    String updateSql = info.createUpdateSql(param, pkParam, true);
                    int result = JdbcDBUtil.modify(updateSql, null, getConnection(), true);
                    System.out.println("更新結果 :" + result);
                }
            }
        }
    }
    
    private String url;
    private String userName;
    private String password;
    
    private Connection getConnection() throws SQLException {
       // DataSource bds = DbConstant.getTestDataSource_Oracle_HP_new();
        
         BasicDataSource bds = new BasicDataSource();
         bds.setUrl(url);
         bds.setUsername(userName);
         bds.setPassword(password);
         bds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        
//        BasicDataSource bds = new BasicDataSource();
//        bds.setUrl("jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=122.116.167.154)(PORT=1521))(CONNECT_DATA=(SERVICE_NAME=IBTDCS1)))");
//        bds.setUsername("sysadm");
//        bds.setPassword("123456");
//        bds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        
        return bds.getConnection();
    }

    private List<TableInfo> readInfo(HSSFSheet sheet) {
        List<TableInfo> tableList = new ArrayList<TableInfo>();
        TableInfo tab = null;
        for (int ii = 0; ii <= sheet.getLastRowNum(); ii++) {
            HSSFRow row = sheet.getRow(ii);
            if (row == null) {
                if (tab != null) {
                    tableList.add(tab);
                    tab = null;
                }
                continue;
            }

            int COLUMN_START = 1;// 資料讀取開始位置
            String tagDef = ExcelUtil.getInstance().readCell(row.getCell(0));// 第一欄位TAG定義藍
            if (TAG_TABLE.equals(tagDef)) {
                if (tab != null) {
                    tableList.add(tab);
                    tab = null;
                }
                tab = new TableInfo();
                tab.tableName = ExcelUtil.getInstance().readCell(row.getCell(1));
            } else if (TAG_COLUMN.equals(tagDef) && tab != null) {

                for (int jj = COLUMN_START; jj < row.getLastCellNum(); jj++) {
                    HSSFCell cell = row.getCell(jj);
                    String value = ExcelUtil.getInstance().readCell(cell);
                    if(StringUtils.isBlank(value)){
                        throw new RuntimeException("定義欄位 " + jj + " 為空 ");
                    }
                    tab.columnList.add(value.trim().toLowerCase());
                }
            } else if (TAG_DATA.equals(tagDef) && tab != null) {

                List<String> dataList = new ArrayList<String>();
                for (int jj = COLUMN_START; jj < row.getLastCellNum(); jj++) {
                    HSSFCell cell = row.getCell(jj);
                    String value = ExcelUtil.getInstance().readCell(cell);
                    dataList.add(value);
                }

                tab.valueList.add(dataList);
            }
        }

        if (tab != null) {
            tableList.add(tab);
        }

        return tableList;
    }

    private static class TableInfo {
        String tableName;
        List<String> columnList = new ArrayList<String>();
        List<List<String>> valueList = new ArrayList<List<String>>();

        List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
        List<Map<String, String>> pkList = new ArrayList<Map<String, String>>();

        public void execute() {
            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            List<Map<String, String>> pklist = new ArrayList<Map<String, String>>();
            for (List<String> valList : valueList) {
                Map<String, String> map = new LinkedHashMap<String, String>();
                Map<String, String> pkmap = new LinkedHashMap<String, String>();
                for (int ii = 0; ii < columnList.size(); ii++) {
                    String col = columnList.get(ii);
                    String val = "";
                    try{
                        val = valList.get(ii);
                    }catch(Exception ex){
                    }
                    
                    // 若定義欄位為#開頭則為pk
                    if (col.startsWith("#")) {
                        pkmap.put(col.replaceFirst("#", ""), val);
                    } else {
                        map.put(col, val);
                    }
                }
                list.add(map);
                pklist.add(pkmap);
                
                //若pkmap有但map沒有 就用pkmap
                for(String key : pkmap.keySet()){
                    if(StringUtils.isBlank(map.get(key))){
                        map.put(key, pkmap.get(key));
                    }
                }
                System.out.println("PK -- " + pkmap);
                System.out.println("Param -- " + map);
            }
            dataList = list;
            pkList = pklist;
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
