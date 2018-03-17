package _temp;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import gtu.db.DbConstant;
import gtu.db.simple_dao_gen.GenDaoAllMain;
import gtu.file.FileUtil;
import gtu.poi.hssf.ExcelUtil;

public class Test_FucoCreateTable {

    public static void main(String[] args) throws Exception {
        Test_FucoCreateTable t = new Test_FucoCreateTable();
        File file = new File("C:\\Users\\gtu00\\OneDrive\\Desktop\\SZRCB_DB物件说明表.xlsx");
        XSSFWorkbook wb = ExcelUtil.getInstance().readExcel_xlsx(file);

        for (int ii = 0; ii < wb.getNumberOfSheets(); ii++) {
            t.createSqlProcess(wb.getSheetAt(ii));
        }
    }

    private void createSqlProcess(Sheet sheet) {
        String table = "";

        int startPos = -1;

        List<Col> lst = new ArrayList<Col>();

        for (int ii = 0; ii <= sheet.getLastRowNum(); ii++) {
            Row row = sheet.getRow(ii);

            String val = ExcelUtil.getInstance().readCell(ExcelUtil.getInstance().getCellChk(row, 1));
            if (val.equals("表名称")) {
                table = ExcelUtil.getInstance().readCell(ExcelUtil.getInstance().getCellChk(row, 2));
            }

            // 编号 字段 中文名称 类型 主键 是否为空 代码含义 备注

            if (StringUtils.trim(val).equals("字段")) {
                startPos = ii + 1;
            }

            if (ii >= startPos && startPos != -1) {
                // PK 欄位 資料類型 說明 代碼說明
                Col col = new Col();
                col.num = ExcelUtil.getInstance().readCell(ExcelUtil.getInstance().getCellChk(row, 0));
                col.col = ExcelUtil.getInstance().readCell(ExcelUtil.getInstance().getCellChk(row, 1));
                col.chinese = ExcelUtil.getInstance().readCell(ExcelUtil.getInstance().getCellChk(row, 2));
                col.type = getTypeParse(ExcelUtil.getInstance().readCell(ExcelUtil.getInstance().getCellChk(row, 3)));
                col.pk = ExcelUtil.getInstance().readCell(ExcelUtil.getInstance().getCellChk(row, 4));
                col.nullable = ExcelUtil.getInstance().readCell(ExcelUtil.getInstance().getCellChk(row, 5));
                col.meaning = ExcelUtil.getInstance().readCell(ExcelUtil.getInstance().getCellChk(row, 6));
                col.comment = ExcelUtil.getInstance().readCell(ExcelUtil.getInstance().getCellChk(row, 7));

                if (col.col.contains("废弃")) {
                    continue;
                }
                if (StringUtils.isBlank(col.col)) {
                    continue;
                }

                // System.out.println(ReflectionToStringBuilder.toString(col));

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
            // if (ii == lst.size() - 1) {
            // comma = "";
            // }

            sb.append(col.col + " " + col.type + " " + nullable + comma + "\n");
        }

        List<String> pkLst = new ArrayList<String>();
        for (int ii = 0; ii < lst.size(); ii++) {
            Col col = lst.get(ii);
            if (StringUtils.isNotBlank(col.pk)) {
                pkLst.add(col.col);
            }
        }

        sb.append("PRIMARY KEY (" + StringUtils.join(pkLst, ",") + ")\n");

        sb.append(");\n\n");

        // create unique index id on hr.emp(xid);
//        sb.append("create unique index pk_" + table + " on " + table + "(" + StringUtils.join(pkLst, ",") + ");\n");

        System.out.println(sb);

        // getDAO(table, pkLst);
    }

    private void getDAO(String table, List<String> pkLst) {
        Connection conn = null;
        try {
            conn = DbConstant.getTestDataSource_FucoOracle().getConnection();

            GenDaoAllMain t = new GenDaoAllMain();
            List<String> columnList = t.getColumnList(table, DbConstant.getTestDataSource_FucoOracle().getConnection());
            String txt = t.execute(table, columnList, pkLst, conn);

            FileUtil.saveToFile(new File(FileUtil.DESKTOP_DIR, table + ".txt"), txt, "utf8");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
    }

    
    private String getTypeParse(String val) {
        // nvarchar -> varchar2
        Pattern ptn = Pattern.compile("NVARCHAR2?\\((\\d+)\\)", Pattern.CASE_INSENSITIVE);
        Matcher mth = ptn.matcher(val);
        if (mth.find()) {
            String len = mth.group(1);
            return "varchar(" + len + ")";
        }

        // datetime -> timestamp
        if ("datetime".equalsIgnoreCase(val)) {
            return "timestamp";
        }

        // text -> clob
        if ("text".equalsIgnoreCase(val)) {
            return "clob";
        }
        
        if("number".equalsIgnoreCase(val)) {
            return "int";
        }
        return val;
    }

    private static class Col {
        String pk;
        String col;
        String type;
        String comment;
        String chinese;
        String nullable;
        String num;
        String meaning;
    }
}
