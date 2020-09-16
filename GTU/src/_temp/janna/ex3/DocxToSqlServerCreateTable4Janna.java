package _temp.janna.ex3;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import gtu.file.FileUtil;

public class DocxToSqlServerCreateTable4Janna {

    public static void main(String[] args) throws IOException {
        File file = new File("/home/gtu001/下載/sql/RLDFON12B0.docx");
        FileInputStream fis = new FileInputStream(file);
        XWPFDocument document = new XWPFDocument(fis);
        XWPFTable table = document.getTables().get(0);
        List<XWPFTableRow> tableRows = table.getRows();
        for (int row = 0; row < tableRows.size(); row++) {
            System.out.println("row index --- " + row);
            XWPFTableRow tableRow = tableRows.get(row);
            List<XWPFTableCell> tableCells = tableRow.getTableCells();
            for (int ii = 0; ii < tableCells.size(); ii++) {
                System.out.println(ii + "---" + tableCells.get(ii).getText());
            }
            System.out.println("==================");
        }

        DocxToSqlServerCreateTable4Janna t = new DocxToSqlServerCreateTable4Janna();
        DoxTableProcess mDoxTableProcess = t.new DoxTableProcess(file);
        mDoxTableProcess.createSql();
        mDoxTableProcess.writeFile();
        System.out.println("done...");
    }

    // public static void main(String[] args) throws IOException {
    // File dir = new File("/home/gtu001/下載/sql");
    //
    // List<File> fileLst = new ArrayList<File>();
    // FileUtil.searchFilefind(dir, ".*\\.docx", fileLst);
    //
    // DocxToSqlServerCreateTable4Janna t = new
    // DocxToSqlServerCreateTable4Janna();
    //
    // for (File docx : fileLst) {
    // DoxTableProcess mDoxTableProcess = t.new DoxTableProcess(docx);
    // mDoxTableProcess.writeFile();
    // }
    // System.out.println("done...");
    // }

    private class DocColumeDef {
        // 0---key
        // 1---欄位名稱
        // 2---中文名稱
        // 3---格式
        // 4---預設值
        // 5---允許Null
        // 6---說明
        // --------------------------------
        // 0---PK
        // 1---STATISTIC_YYY
        // 2---統計年
        // 3---CHAR(3)
        // 4---
        // 5---N
        // 6---
        boolean isPk = false;
        String column;
        String columnChs;
        String type;
        String defaultVal;
        boolean nullable = false;
        String desc;

        DocColumeDef(XWPFTableRow tableRow) {
            List<XWPFTableCell> tableCells = tableRow.getTableCells();
            for (int ii = 0; ii < tableCells.size(); ii++) {
                String text = tableCells.get(ii).getText();
                switch (ii) {
                case 0:
                    if ("PK".equalsIgnoreCase(text)) {
                        isPk = true;
                    }
                    break;
                case 1:
                    column = text.toLowerCase();
                    break;
                case 2:
                    columnChs = text.toLowerCase();
                    break;
                case 3:
                    type = text;
                    break;
                case 4:
                    defaultVal = text;
                    break;
                case 5:
                    if ("N".equalsIgnoreCase(text)) {
                        nullable = true;
                    }
                    break;
                case 6:
                    desc = text;
                    break;
                }
            }
        }
    }

    private class DoxTableProcess {
        String tableName;
        List<DocColumeDef> columns = new ArrayList<DocColumeDef>();

        private String getTableName(String text) {
            Pattern ptn = Pattern.compile("[a-zA-Z0-9]+");
            Matcher mth = ptn.matcher(text);
            if (mth.find()) {
                return mth.group().toLowerCase();
            }
            throw new RuntimeException("無法取得：" + text);
        }

        DoxTableProcess(File file) throws IOException {
            FileInputStream fis = new FileInputStream(file);
            XWPFDocument document = new XWPFDocument(fis);
            XWPFTable table = document.getTables().get(0);
            List<XWPFTableRow> tableRows = table.getRows();

            for (int row = 0; row < tableRows.size(); row++) {
                System.out.println("row index --- " + row);

                XWPFTableRow tableRow = tableRows.get(row);
                List<XWPFTableCell> tableCells = tableRow.getTableCells();

                if (row == 0) {
                    String text = tableCells.get(0).getText();
                    tableName = getTableName(text);
                } else if (row == 1) {
                    continue;
                } else {
                    columns.add(new DocColumeDef(tableRow));
                }
            }
        }

        private void writeFile() {
            String sql = createSql();
            File file = new File(FileUtil.DESKTOP_DIR, "janna_" + tableName + ".sql");
            System.out.println("writefile : " + file);
            FileUtil.saveToFile(file, sql, "UTF8");
        }

        private String createSql() {
            List<String> pkLst = new ArrayList<String>();

            StringBuilder sb = new StringBuilder();
            sb.append("isql $1<<!  \n");
            sb.append("DROP TABLE " + tableName + "  \n");
            sb.append("CREAT TABLE " + tableName + " ( \n");
            for (int ii = 0; ii < columns.size(); ii++) {
                DocColumeDef column = columns.get(ii);
                String suffix = ",";
                if (ii == columns.size() - 1) {
                    suffix = "";
                }
                String line = "        " + column.column + " " + column.type + " " + (column.nullable ? "NOT NULL" : "") + suffix;
                sb.append(line + " \n");

                if (column.isPk) {
                    pkLst.add(column.column);
                }
            }
            sb.append(")  \n");
            sb.append("in $2 extent size $3 next size $4  \n");
            sb.append("lock mode row;  \n");

            // pk======
            sb.append(" create unique index " + tableName + "_p_key on  \n");
            sb.append("" + tableName + "(" + StringUtils.join(pkLst, ",") + ");  \n");

            //
            // create unique index rsdfz221y_p_key on
            // rsdfz221y(statistic_yyy,region,admin_office_code,site_id,village,gender,education,living,age,mrg_status);
            // create index rsdfz221y_s1_key on
            // rsdfz221y(statistic_yyy,region,education);
            // create index rsdfz221y_s2_key on
            // rsdfz221y(statistic_yyy,region,age);
            // create index rsdfz221y_s3_key on
            // rsdfz221y(statistic_yyy,region,mrg_status);
            // !

            System.out.println(sb);
            return sb.toString();
        }
    }
}
