package _temp.janna.ex0;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

//import gtu.poi.hwpf.docx.DocxReaderUtil;

public class TradevanForJanna_MssqlGenerator {

    public static void main(String[] args) {
        File file = new File("C:/Users/gtu001/Desktop/J122-SDD-001(資料庫及檔案規格)_V1.0_出租管理作業_20170524_1.docx");
        TradevanForJanna_MssqlGenerator test = new TradevanForJanna_MssqlGenerator();
        test.execute(file);
    }

    public void execute(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            XWPFDocument document = new XWPFDocument(fis);
            for (int ii = 0; ii < document.getBodyElements().size(); ii++) {
                IBodyElement ele = document.getBodyElements().get(ii);
                System.out.println("## index = " + ii);
                if (ele instanceof XWPFParagraph) {
                    showParagraph((XWPFParagraph) ele, ii);
                } else if (ele instanceof XWPFTable) {

                    parseColumnDef((XWPFTable) ele);
                    // showTable((XWPFTable) ele);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    Map<String, TableDef> tabMap = new LinkedHashMap<String, TableDef>();

    private void parseColumnDef(XWPFTable table) {
        List<XWPFTableRow> tableRows = table.getRows();
        TableDef d2 = new TableDef();
        int startRow = -1;
        A : for (int row = 0; row < tableRows.size(); row++) {
            System.out.println("row index --- " + row);
            XWPFTableRow tableRow = tableRows.get(row);
            List<XWPFTableCell> tableCells = tableRow.getTableCells();

            if (row == 0) {
                String chk = tableCells.get(0).getText();
                if (chk.contains("檔案紀錄/表格代號")) {
                    String tableName = tableCells.get(1).getText();
                    d2.tabName = tableName;
                }
            } else if (row == 1) {
                String chk = tableCells.get(0).getText();
                if (chk.contains("表格中文名稱")) {
                    String tableNameChs = tableCells.get(1).getText();
                    d2.tabNameChs = tableNameChs;
                }
            } else {
                if (StringUtils.isNotBlank(d2.tabName)) {
                    System.out.println("Table - " + d2.tabName + " / " + d2.tabNameChs);
                }

                if (tableCells.size() >= 3) {
                    if (tableCells.get(1).getText().contains("中文名稱") && tableCells.get(2).getText().contains("英文名稱")) {
                        startRow = row + 1;
                    }
                }

                if (startRow != -1 && row >= startRow && StringUtils.isNotBlank(d2.tabName)) {
                    if (tableCells.size() <= 7) {
                        break A;
                    }
                }

                if (startRow != -1 && row >= startRow && StringUtils.isNotBlank(d2.tabName)) {
                    for (int ii = 0; ii < tableCells.size(); ii++) {
                        System.out.println(ii + "---" + tableCells.get(ii).getText());
                    }
                    System.out.println("==================");

                    ColumnDef d = new ColumnDef();
                    // 中文名稱 英文名稱 型態 Key NN Default 說明
                    d.chinese = tableCells.get(1).getText();
                    d.english = tableCells.get(2).getText();
                    d.type = tableCells.get(3).getText();
                    d.key = tableCells.get(4).getText();
                    d.nn = tableCells.get(5).getText();
                    d.defaultV = tableCells.get(6).getText();
                    d.remark = tableCells.get(7).getText();
                    
                    if(StringUtils.isBlank(d.english)){
                        continue;
                    }

                    d2.colList.add(d);
                }
            }
        }
        
        if(StringUtils.isNotBlank(d2.tabName) && !d2.colList.isEmpty()){
            tabMap.put(d2.tabName, d2);
        }
    }

    static class TableDef {
        String tabName;
        String tabNameChs;
        List<ColumnDef> colList = new ArrayList<ColumnDef>();
        @Override
        public String toString() {
            return "TableDef [tabName=" + tabName + ", tabNameChs=" + tabNameChs + ", colList=" + colList + "]";
        }
    }

    static class ColumnDef {
        // 中文名稱 英文名稱 型態 Key NN Default 說明
        String chinese;
        String english;
        String type;
        String key;
        String nn;
        String defaultV;
        String remark;
        @Override
        public String toString() {
            return "ColumnDef [chinese=" + chinese + ", english=" + english + ", type=" + type + ", key=" + key + ", nn=" + nn + ", defaultV=" + defaultV + ", remark=" + remark + "]";
        }
    }

    private void showParagraph(XWPFParagraph para, int index) {
        System.out.println("Paragraph - " + para.getText());
        String text = StringUtils.defaultString(para.getText());
    }

    private void showTable(XWPFTable table) {
        List<XWPFTableRow> tableRows = table.getRows();
        System.out.println("Table - ");
        for (int row = 0; row < tableRows.size(); row++) {
            System.out.println("row index --- " + row);
            XWPFTableRow tableRow = tableRows.get(row);
            List<XWPFTableCell> tableCells = tableRow.getTableCells();
            for (int ii = 0; ii < tableCells.size(); ii++) {
                System.out.println(ii + "---" + tableCells.get(ii).getText());
            }
            System.out.println("==================");
        }
    }
}
