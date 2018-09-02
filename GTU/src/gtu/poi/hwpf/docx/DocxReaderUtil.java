package gtu.poi.hwpf.docx;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

public class DocxReaderUtil {

    public static void main(String[] args) throws IOException {
        
    }
    
    /**
     * 顯示word docx內容
     */
    public static void showWordInfo(File file){
        try{
            FileInputStream fis = new FileInputStream(file);
            XWPFDocument document = new XWPFDocument(fis);
            for(int ii = 0 ; ii < document.getBodyElements().size() ; ii ++){
                IBodyElement ele = document.getBodyElements().get(ii);
                System.out.println("## index = " + ii);
                if(ele instanceof XWPFParagraph){
                    showParagraph((XWPFParagraph)ele);
                }else if(ele instanceof XWPFTable){
                    showTable((XWPFTable)ele);
                }
            }
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
    
    public static void showParagraph(XWPFParagraph para){
        System.out.println("Paragraph - " + para.getText());
    }
    
    public static void showTable(XWPFTable table){
        List<XWPFTableRow> tableRows = table.getRows();
        System.out.println("Table - ");
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
