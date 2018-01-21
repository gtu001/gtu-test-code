package _temp;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import gtu.db.DbConstant;
import gtu.db.simple_dao_gen.GenDaoAllMain;
import gtu.file.FileUtil;
import gtu.poi.hssf.ExcelUtil;

public class Test_FucoCreateTable {

    public static void main(String[] args) throws Exception {
        Test_FucoCreateTable t = new Test_FucoCreateTable();
        File file = new File("C:\\Users\\gtu00\\OneDrive\\Desktop\\後台廣告上稿資料表.xls");
        HSSFWorkbook wb = ExcelUtil.getInstance().readExcel(file);

        for (int ii = 0; ii < wb.getNumberOfSheets(); ii++) {
            t.createSqlProcess(wb.getSheetAt(ii));
        }
    }

    private void createSqlProcess(HSSFSheet sheet) {
        String table = "";

        int startPos = -1;

        List<Col> lst = new ArrayList<Col>();

        for (int ii = 0; ii <= sheet.getLastRowNum(); ii++) {
            HSSFRow row = sheet.getRow(ii);

            String val = ExcelUtil.getInstance().readHSSFCell(ExcelUtil.getInstance().getCellChk(row, 0));
            if (val.equals("表名稱")) {
                table = ExcelUtil.getInstance().readHSSFCell(ExcelUtil.getInstance().getCellChk(row, 1));
            }

            if (val.equals("表名稱")) {
                table = ExcelUtil.getInstance().readHSSFCell(ExcelUtil.getInstance().getCellChk(row, 1));
            }

            if (StringUtils.trim(val).equals("PK")) {
                startPos = ii + 1;
            }

            if (ii >= startPos && startPos != -1) {
                // PK 欄位 資料類型 說明 代碼說明
                Col col = new Col();
                col.pk = ExcelUtil.getInstance().readHSSFCell(ExcelUtil.getInstance().getCellChk(row, 0));
                col.col = ExcelUtil.getInstance().readHSSFCell(ExcelUtil.getInstance().getCellChk(row, 1));
                col.type = getTypeParse(ExcelUtil.getInstance().readHSSFCell(ExcelUtil.getInstance().getCellChk(row, 2)));
                col.comment = ExcelUtil.getInstance().readHSSFCell(ExcelUtil.getInstance().getCellChk(row, 3));
                col.desc = ExcelUtil.getInstance().readHSSFCell(ExcelUtil.getInstance().getCellChk(row, 4));

                if (StringUtils.isBlank(col.col)) {
                    continue;
                }

                lst.add(col);
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("create table " + table + " ( \n");
        for (int ii = 0; ii < lst.size(); ii++) {
            Col col = lst.get(ii);
            String nullable = "null";
            if (StringUtils.isNotBlank(col.pk)) {
                nullable = "not null";
            }

            String comma = ",";
            if (ii == lst.size() - 1) {
                comma = "";
            }

            sb.append(col.col + " " + col.type + " " + nullable + comma + "\n");
        }
        sb.append(");\n\n");

        // create unique index id on hr.emp(xid);

        List<String> pkLst = new ArrayList<String>();
        for (int ii = 0; ii < lst.size(); ii++) {
            Col col = lst.get(ii);
            if (StringUtils.isNotBlank(col.pk)) {
                pkLst.add(col.col);
            }
        }

        sb.append("create unique index pk_" + table + " on " + table + "(" + StringUtils.join(pkLst, ",") + ");\n");

//        System.out.println(sb);
        
        
        getDAO(table, pkLst);
    }
    
    private void getDAO(String table, List<String> pkLst) {
        Connection conn =  null;
        try {
            conn = DbConstant.getTestDataSource_FucoOracle().getConnection();
            
            GenDaoAllMain t = new GenDaoAllMain();
            List<String> columnList = t.getColumnList(table, DbConstant.getTestDataSource_FucoOracle().getConnection());
            String txt = t.execute(table, columnList, pkLst, conn);
            
            FileUtil.saveToFile(new File(FileUtil.DESKTOP_DIR, table+".txt"), txt, "utf8");
        }catch(Exception ex) {
            ex.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
    }

    private String getTypeParse(String val) {
        //nvarchar -> varchar2
        Pattern ptn = Pattern.compile("nvarchar\\((\\d+)\\)");
        Matcher mth = ptn.matcher(val);
        if(mth.find()) {
            String len = mth.group(1);
            return "varchar2("+len+")";
        }
        
        //datetime -> timestamp
        if("datetime".equalsIgnoreCase(val)) {
            return "timestamp";
        }
        
        //text -> clob
        if("text".equalsIgnoreCase(val)) {
            return "clob";
        }
        return val;
    }
    
    private static class Col {
        String pk;
        String col;
        String type;
        String comment;
        String desc;
    }
}
