package gtu.poi.hwpf.docx;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;

public class DocxTableUtil {
    
    /**
     * 建立一個有編號與縮排的內容
     * @param document
     * @param num 主題編號(1=大主提,2=次主題,3=再次主題...etc)
     * @param text 文字
     */
    public static void createParagraph(XWPFDocument document, Long num, Integer indentation, String text) {
        try{
            List<String> textList = new ArrayList<String>();
            BufferedReader reader = new BufferedReader(new StringReader(text));
            for (String line = null; (line = reader.readLine()) != null;) {
                textList.add(line);
            }
            reader.close();
            createParagraph(document, num, indentation, textList.toArray(new String[0]));
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
    
    /**
     * 建立一個有編號與縮排的內容
     * @param document
     * @param num 主題編號(1=大主提,2=次主題,3=再次主題...etc)
     * @param spaceAfter 縮排格數
     * @param text 文字
     */
    public static void createParagraph(XWPFDocument document, Long num, Integer indentation, String... text) {
        XWPFParagraph createParagraph = document.createParagraph();
        if(indentation != null){
            createParagraph.setIndentationLeft(200 * indentation);
        }
        if (num != null) {
            createParagraph.setNumID(BigInteger.valueOf(num));
        }
        XWPFRun createRun = createParagraph.createRun();
        for(int ii = 0 ; ii < text.length ; ii ++){
            String t = text[ii];
            createRun.setText(t);
            if(ii != text.length - 1){
                createRun.addBreak();
            }
        }
    }

    /**
     * 設定預設表格排版
     */
    public static void setTableRowDefault(XWPFTableRow row) {
        for (int ii = 0;; ii++) {
            XWPFTableCell cell = row.getCell(ii);
            if (cell == null) {
                break;
            }
            List<XWPFParagraph> plist = cell.getParagraphs();
            for (XWPFParagraph p : plist) {
                setTableRowDefault(p);
            }
        }
    }

    /**
     * 設定預設排版
     */
    public static void setTableRowDefault(XWPFParagraph para) {
        para.setIndentationFirstLine(0);// 設定尺規為0
        para.setIndentationHanging(0);
        para.setIndentationLeft(0);
        para.setIndentationRight(0);
        para.setAlignment(ParagraphAlignment.LEFT);
        if (para.getRuns().size() > 0) {
            for (int ii = 0; ii < para.getRuns().size(); ii++) {
                para.removeRun(ii);
                ii--;
            }
        }
    }

    public static void removeCellContent(XWPFTableCell cell) {
        for (; cell.getParagraphs().size() > 0;) {
            cell.removeParagraph(0);
        }
    }

    /**
     * 換頁
     */
    public static void changePage(XWPFDocument document) {
        document.createParagraph().createRun().addBreak(BreakType.PAGE);
    }

    public static XWPFTable createCloneTable(XWPFTable cloneFrom, XWPFDocument document) {
        CTBody ctbody = document.getDocument().getBody();
        ctbody.addNewTbl();
        int pos = ctbody.sizeOfTblArray() - 1;
        ctbody.setTblArray(pos, cloneFrom.getCTTbl());
        CTTbl cttbl = ctbody.getTblArray(pos);
        XWPFTable table2 = new XWPFTable(cttbl, document);
        return table2;
    }

    @Deprecated
    public static XWPFTable createCloneTable2(XWPFTable cloneFrom, XWPFDocument document) {
        CTTbl ctTbl = CTTbl.Factory.newInstance(); // Create a new CTTbl for the new table
        ctTbl.set(cloneFrom.getCTTbl()); // Copy the template table's CTTbl
        XWPFTable table2 = new XWPFTable(ctTbl, document); // Create a new table using the CTTbl upon 
        return table2;
    }
    
    @Deprecated
    public static XWPFTable createCloneTable3(XWPFTable cloneFrom, XWPFDocument document){
        CTTbl tbl = document.getDocument().getBody().insertNewTbl(0);
        tbl.set(cloneFrom.getCTTbl());
        XWPFTable table2 = new XWPFTable(tbl, document);
        return table2;
    }

    public static void debugShowTable(XWPFTable table) {
        List<XWPFTableRow> tableRows = table.getRows();
        for (int row = 0; row < tableRows.size(); row++) {
            System.out.print("row " + row);
            XWPFTableRow tableRow = tableRows.get(row);
            List<XWPFTableCell> tableCells = tableRow.getTableCells();
            for (int ii = 0; ii < tableCells.size(); ii++) {
                System.out.print("\t" + ii + ":" + tableCells.get(ii).getText());
            }
            System.out.println();
        }
    }

    public static void cloneCell(XWPFTableCell clone, XWPFTableCell source) {
        CTTcPr cttcPr = clone.getCTTc().isSetTcPr() ? clone.getCTTc().getTcPr() : clone.getCTTc().addNewTcPr();
        cttcPr.set(source.getCTTc().getTcPr());

        int indexParag = 0, indexTable = 0;
        for (IBodyElement iBodyElement : source.getBodyElements()) {
            BodyElementType beType = iBodyElement.getElementType();
            if (beType == BodyElementType.PARAGRAPH) {
                XWPFParagraph sourceParag = (XWPFParagraph) iBodyElement;
                XWPFParagraph cloneParag = indexParag < clone.getParagraphs().size() ? clone.getParagraphs().get(indexParag) : clone.addParagraph();
                // cloneParagraph(cloneParag, sourceParag);
                indexParag++;
            } else if (beType == BodyElementType.TABLE) {
                XWPFTable sourceTable = (XWPFTable) iBodyElement;
                XWPFTable cloneTable = indexTable < clone.getTables().size() ? clone.getTables().get(indexTable) : clone.insertNewTbl(sourceTable.getCTTbl().newCursor());
                if (cloneTable == null)
                    cloneTable = new XWPFTable(clone.getCTTc().addNewTbl(), clone);

                // cloneTable(cloneTable, sourceTable);
                indexTable++;
            }
        }
    }

    public static void writeOut(File output, XWPFDocument document) throws FileNotFoundException {
        ByteArrayOutputStream ostream = new ByteArrayOutputStream();
        FileOutputStream out = new FileOutputStream(output);
        try {
            document.write(ostream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 输出字节流
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
