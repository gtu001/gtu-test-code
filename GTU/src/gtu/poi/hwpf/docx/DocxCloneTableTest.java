package gtu.poi.hwpf.docx;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;

// org.openxmlformats.schemas.wordprocessingml.x2006.main.impl.CTTcImpl

public class DocxCloneTableTest {

    File file = new File("C:/Users/Troy/Desktop/source.docx");
    File output = new File("C:/Users/Troy/Desktop/TEST.DOCx");

    //            WordExtractor wordExtractor = new WordExtractor(fis);
    //            HWPFDocument hwpfd = new HWPFDocument(fis);

    /**
     * @param args
     */
    public static void main(String[] args) {
        DocxCloneTableTest xx = new DocxCloneTableTest();
        xx.execute();
        System.out.println("done...");
    }

    public void execute() {
        try {
            FileInputStream fis = new FileInputStream(file);
            XWPFDocument document = new XWPFDocument(fis);
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            System.out.println("paragraphs size = " + paragraphs.size());
            for (int i = 0; i < paragraphs.size(); i++) {
                XWPFParagraph paragraph = paragraphs.get(i);
                System.out.println("getParagraphText == " + paragraph.getParagraphText());
            }
            List<XWPFTable> tables = document.getTables();
            System.out.println("tables size = " + paragraphs.size());
            for (int i = 0; i < tables.size(); i++) {
                XWPFTable table = tables.get(i);
                System.out.println("table.getStyleID() == " + table.getStyleID());
                List<XWPFTableRow> tableRows = table.getRows();
                System.out.println("tableRows.size() == " + tableRows.size());
                for (int row = 0; row < tableRows.size(); row++) {
                    XWPFTableRow tableRow = tableRows.get(row);
                    List<XWPFTableCell> tableCells = tableRow.getTableCells();
                    //                    System.out.println("tableCells.size() == " + tableCells.size());
                    for (int column = 0; column < tableCells.size(); column++) {
                        XWPFTableCell tableCell = tableCells.get(column);
                        //                        tableCell.setText("tab" + i + ",row" + row + ",column" + column);
                    }
                }
            }

            XWPFTable orign = tables.get(0);

            for (int ii = 0; ii < 70; ii++) {
                this.createCloneTable(orign, document);
                System.out.println("table clone size = " + document.getTables().size());
                this.changePage(document);
            }

            writeOut(document);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    void changePage(XWPFDocument document) {
        document.createParagraph().createRun().addBreak(BreakType.PAGE);
    }

    CTTbl createCloneTable(XWPFTable cloneFrom, XWPFDocument document) {
        CTBody ctbody = document.getDocument().getBody();
        ctbody.addNewTbl();
        int pos = ctbody.sizeOfTblArray() - 1;
        ctbody.setTblArray(pos, cloneFrom.getCTTbl());
        CTTbl cttbl = ctbody.getTblArray(pos);
        return cttbl;
    }

    private void writeOut(XWPFDocument document) throws FileNotFoundException {
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
