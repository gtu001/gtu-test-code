package gtu.poi.hwpf.docx._work;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import freemarker.template.TemplateException;
import gtu.freemarker.FreeMarkerSimpleUtil;
import gtu.poi.hwpf.docx._work.MuginBa7Job_MergeWord.RowData;

public class MuginBa7Job_CreateTableSchema {

    static File copyToDir = MuginBa7Job_MergeWord.copyToDir;
    static File ftl = new File("C:/workspace/GTU/src/gtu/poi/hwpf/_work/RLDE415W.ftl");
    static Pattern renamePattern = Pattern.compile("^6-4-TLDF(\\d+)M\\.docx$");

    static File sqlOuputDir = MuginBa7Job_MergeWord.copyToDir;

    public static void main(String[] args) throws IOException, TemplateException {
        MuginBa7Job_CreateTableSchema test = new MuginBa7Job_CreateTableSchema();
        test.execute();
        System.out.println("done...");
    }

    void execute() throws IOException, TemplateException {
        Matcher matcher = null;
        Map<String, Object> root = new HashMap<String, Object>();
        for (File f : copyToDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.matches("6-4-TLDF.*");
            }
        })) {
            root.clear();
            System.out.println("# readWordTable ..");
            System.out.println("make : " + f.getName());
            FileInputStream fis = new FileInputStream(f);
            XWPFDocument document = new XWPFDocument(fis);

            XWPFTable table = document.getTables().get(0);
            List<RowData> list = readWordTable(table);

            matcher = renamePattern.matcher(f.getName());
            matcher.find();
            String tableName = String.format("XLDF%sMT", matcher.group(1));

            List<RowData> pks = new ArrayList<RowData>();
            for (RowData row : list) {
                if (row.key.trim().equalsIgnoreCase("pk")) {
                    pks.add(row);
                }
            }

            root.put("table_name", tableName);
            root.put("row", list);
            root.put("pks", pks);

            createSql(root, tableName + ".sql");
        }
    }

    void createSql(Map<String, Object> root, String filename) throws IOException, TemplateException {
        //        Map<String, Object> row = new HashMap<String, Object>();
        //        row.put("name", "NAME1");
        //        row.put("type", "CHAR(10000)");
        //        Map<String, Object> row2 = new HashMap<String, Object>();
        //        row2.put("name", "NAME2");
        //        row2.put("type", "CHAR(10002)");
        //
        //        Map<String, Object> root = new HashMap<String, Object>();
        //        root.put("table_name", "TABLE_NAME");
        //        root.put("row", new Object[] { row, row2 });
        FreeMarkerSimpleUtil.replace(ftl, root, new FileOutputStream(new File(sqlOuputDir, filename)));
    }

    List<RowData> readWordTable(XWPFTable table) throws IOException {
        List<XWPFTableRow> tableRows = table.getRows();
        List<RowData> list = new ArrayList<RowData>();
        for (int row = 0; row < tableRows.size(); row++) {
            XWPFTableRow tableRow = tableRows.get(row);
            List<XWPFTableCell> tableCells = tableRow.getTableCells();
            if (tableCells.size() != 7) {
                continue;
            }
            if (tableCells.get(1).getText().equals("欄位名稱")) {
                continue;
            }
            RowData rowData = new RowData(//
                    tableCells.get(0).getText(),//
                    tableCells.get(1).getText(),//
                    tableCells.get(2).getText(),//
                    tableCells.get(3).getText(),//
                    tableCells.get(4).getText(),//
                    tableCells.get(5).getText(),//
                    tableCells.get(6).getText(),//
                    tableRow);//
            System.out.println(rowData);
            list.add(rowData);
        }
        return list;
    }
}
