package gtu.poi.hwpf.docx._work;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

public class MuginBa7Job_MergeWord {

    static File templateWord = new File("C:/workspace/GTU/src/gtu/poi/hwpf/_work/6-4-XLDF004M.docx");
    static File wordDir = new File("C:/Users/Troy/Desktop/xxxxxxx");
    static File copyToDir = new File("C:/Users/Troy/Desktop/AAAAAA");

    public static void main(String[] args) throws IOException {
        MuginBa7Job_MergeWord job = new MuginBa7Job_MergeWord();
        job.execute();
        System.out.println("done...");
    }

    void execute() throws IOException {
        Map<String, RowData> XLDF004M = null;
        {
            FileInputStream fis = new FileInputStream(templateWord);
            XWPFDocument document = new XWPFDocument(fis);
            List<XWPFTable> tables = document.getTables();
            XLDF004M = readWordTable(tables.get(0));
        }

        {
            Map<String, RowData> customMap = null;
            for (File f : wordDir.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.matches("6-4-TLDF.*\\.docx");
                }
            })) {
                System.out.println("# readWordTable ..");
                System.out.println("make : " + f.getName());
                FileInputStream fis = new FileInputStream(f);
                XWPFDocument document = new XWPFDocument(fis);

                XWPFTable table = document.getTables().get(0);
                customMap = readWordTable(table);

                RowData rowData = null;
                List<String> list = Arrays.asList("TRANSACTION_ID", "SEQUENCE_ID", "SERIAL_NO", "STATE", "ACTION", "LOCK_MODE", "SITE_ID", "SELECT_MODE");
                Collections.reverse(list);
                XWPFTableRow row = null;

                for (String col : customMap.keySet()) {
                    XWPFTableCell cell = customMap.get(col).row.getCell(0);
                    this.removeTableCellText(cell);
                }

                for (String val : list) {
                    rowData = XLDF004M.get(val);
                    if (!customMap.containsKey(val)) {
                        row = table.insertNewTableRow(2);
                        row.createCell().setText(rowData.key);
                        row.createCell().setText(rowData.name);
                        row.createCell().setText(rowData.chn);
                        row.createCell().setText(rowData.type);
                        row.createCell().setText(rowData.defaultVal);
                        row.createCell().setText(rowData.allowNull);
                        row.createCell().setText(rowData.desc);
                    }
                }

                writeOut(document, new File(copyToDir, f.getName()));
            }
        }
    }

    Map<String, RowData> readWordTable(XWPFTable table) throws IOException {
        List<XWPFTableRow> tableRows = table.getRows();
        Map<String, RowData> map = new LinkedHashMap<String, RowData>();
        for (int row = 0; row < tableRows.size(); row++) {
            XWPFTableRow tableRow = tableRows.get(row);
            List<XWPFTableCell> tableCells = tableRow.getTableCells();
            if (tableCells.size() != 7) {
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
            //            System.out.println(rowData);
            map.put(rowData.name, rowData);
        }
        return map;
    }

    void removeTableCellText(XWPFTableCell cell) {
        for (; cell.getParagraphs().size() > 0; cell.removeParagraph(0))
            ;
        cell.addParagraph();
    }

    void showXWPFTableInfo(XWPFTable table) {
        List<XWPFTableRow> tableRows = table.getRows();
        for (int row = 0; row < tableRows.size(); row++) {
            XWPFTableRow tableRow = tableRows.get(row);
            List<XWPFTableCell> tableCells = tableRow.getTableCells();
            System.out.format("row:%d", row);
            for (int column = 0; column < tableCells.size(); column++) {
                XWPFTableCell tableCell = tableCells.get(column);
                System.out.format("\t%d = %s", column, tableCell.getText());
            }
            System.out.println();
        }
    }

    public static class RowData {
        //        key 欄位名稱    中文名稱    格式  預設值 允許Null  說明
        String key;
        String name;
        String chn;
        String type;
        String defaultVal;
        String allowNull;
        String desc;
        XWPFTableRow row;

        public RowData(String key, String name, String chn, String type, String defaultVal, String allowNull, String desc, XWPFTableRow row) {
            super();
            this.key = key;
            this.name = name;
            this.chn = chn;
            this.type = type;
            this.defaultVal = defaultVal;
            this.allowNull = allowNull;
            this.desc = desc;
            this.row = row;
        }

        @Override
        public String toString() {
            return "RowData [name=" + name + ", chn=" + chn + "]";
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getChn() {
            return chn;
        }

        public void setChn(String chn) {
            this.chn = chn;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDefaultVal() {
            return defaultVal;
        }

        public void setDefaultVal(String defaultVal) {
            this.defaultVal = defaultVal;
        }

        public String getAllowNull() {
            return allowNull;
        }

        public void setAllowNull(String allowNull) {
            this.allowNull = allowNull;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public XWPFTableRow getRow() {
            return row;
        }

        public void setRow(XWPFTableRow row) {
            this.row = row;
        }
    }

    private void writeOut(XWPFDocument document, File output) throws FileNotFoundException {
        ByteArrayOutputStream ostream = new ByteArrayOutputStream();
        FileOutputStream out = new FileOutputStream(output);
        try {
            document.write(ostream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //输出字节流
        try {
            out.write(ostream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ostream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
