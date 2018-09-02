package gtu.itext;

import gtu.file.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;

public class ITextSimpleTest {

    public static void main(String[] args) throws DocumentException, IOException {
        //1. 標楷體 C:\WINDOWS\Fonts\KAIU.TTF
        BaseFont bfChinese2 = BaseFont.createFont("C:\\windows\\fonts\\KAIU.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

        //2. 細明體/新細明體 C:\WINDOWS\Fonts\MINGLIU.TTC  (到時要使用其index(如0, 1)來指定哪一個)
        BaseFont bfChinese3 = BaseFont.createFont("C:\\windows\\fonts\\MINGLIU.TTC,1", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

        //        BaseFont bfChinese = BaseFont.createFont("MHei-Medium", "UniCNS-UCS2-H", BaseFont.NOT_EMBEDDED);

        //        字型的宣告法：
        Font fontChinese = new Font(bfChinese2, 12, Font.NORMAL);

        Document document = new Document();

        document.addTitle("Hello World example");
        document.addAuthor("Bruno Lowagie");
        document.addSubject("This example explains how to add metadata.");
        document.addKeywords("iText, Hello World, step 3, metadata");
        document.addCreator("My program using iText");

        File file = new File(FileUtil.DESKTOP_PATH, "Chap0101.pdf");
        System.out.println(file.getAbsolutePath());
        PdfWriter.getInstance(document, new FileOutputStream(file));

        // step 3: we open the document
        document.open();

        document.add(new Chunk("\u6e96\u53d7\u4fdd\u4eba", fontChinese));
        document.add(new Paragraph("大家好！", fontChinese));
        document.add(new Paragraph("Hello World!"));

        //                1. 換頁：
        document.newPage();
        //                2. 表格：
        //         設置 Table
        Table aTable = new Table(3);
        int width[] = { 25, 25, 50 };
        aTable.setWidths(width);
        aTable.setWidth(80); // 占頁面寬度 80%
//        aTable.setDefaultHorizontalAlignment(Element.ALIGN_LEFT);
//        aTable.setDefaultVerticalAlignment(Element.ALIGN_MIDDLE);
        aTable.setAutoFillEmptyCells(true); //自動填滿
        aTable.setPadding(1);
        aTable.setSpacing(1);
//        aTable.setDefaultCellBorder(0);
        aTable.setBorder(0);
        Cell cell = new Cell(new Phrase("這是一個測試的 3*3 Table 資料", fontChinese));
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setRowspan(3);
        aTable.addCell(cell);
        aTable.addCell(new Cell("#1"));
        aTable.addCell(new Cell("#2"));
        aTable.addCell(new Cell("#3"));
        aTable.addCell(new Cell("#4"));
        aTable.addCell(new Cell("#5"));
        aTable.addCell(new Cell("#6"));
        document.add(aTable);
        //        3. 圖片：
        // 可以是絕對路徑，也可以是URL
//        Image img = Image.getInstance("D:/_桌面/workspace/GTU/nicePic.JPG");
        // Image image = Image.getInstance(new URL(http://xxx.com/logo.jpg));
//        img.setAbsolutePosition(0, 0);
//        document.add(img);

        document.close();
        System.out.println("done...");
    }
}
