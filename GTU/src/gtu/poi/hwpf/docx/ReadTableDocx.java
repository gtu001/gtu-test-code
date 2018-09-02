package gtu.poi.hwpf.docx;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

public class ReadTableDocx {

    public static void main(String[] args) throws IOException {
        File file = new File("C:/Users/gtu001/Desktop/5-4-2.docx");
        FileInputStream fis = new FileInputStream(file);
        XWPFDocument document = new XWPFDocument(fis);
        XWPFTable table = document.getTables().get(0);
        List<XWPFTableRow> tableRows = table.getRows();
        for (int row = 0; row < tableRows.size(); row++) {
            System.out.println("row index --- " + row);
            XWPFTableRow tableRow = tableRows.get(row);
            List<XWPFTableCell> tableCells = tableRow.getTableCells();
            for(int ii = 0 ; ii < tableCells.size() ; ii ++){
                System.out.println(ii + "---" + tableCells.get(ii).getText());
            }
            System.out.println("==================");
        }
    }
}
