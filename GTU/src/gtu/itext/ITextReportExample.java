package gtu.itext;

import gtu.file.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ITextReportExample {
    
    public static void main(String[] args){
        ITextReportExample test = new ITextReportExample();
        test.downloadPdfTest();
        System.out.println("done...");
    }
	
	public void downloadPdfTest() {
        try{
            FileOutputStream fos = new FileOutputStream(new File(FileUtil.DESKTOP_DIR, "test.pdf"));
            Document document = new Document(PageSize.A4.rotate(), 26, 28, 20, 15);
            PdfWriter.getInstance(document, fos);
            document.open();
            
            document.newPage();
            document.add(writeHeaderOne());
            document.add(writeHeaderTwo());
            for(int ii = 0 ; ii < 25; ii ++){
                document.add(writeRow());
            }
            
            document.newPage();
            document.add(writeHeaderOne());
            document.add(writeHeaderTwo());
            for(int ii = 0 ; ii < 25; ii ++){
                document.add(writeRow());
            }
            
            document.add(writeTotalCompany());
            
            document.close();
            fos.flush();
            fos.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	private PdfPTable writeHeaderOne() throws DocumentException, IOException{
	    String path = "C:/workspace/workspace_GM2/spis_web/src/main/webapp/PDF/kaiu.ttf";
        BaseFont baseCFont = BaseFont.createFont(path, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED); // for traditional chinese -->for 細明體
        BaseFont headerCFont = BaseFont.createFont(path, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        Font cFont = new Font(baseCFont, 10, Font.NORMAL);
        Font headerFont = new Font(headerCFont, 14, Font.BOLD);
        
        PdfPTable table = new PdfPTable(3);
        
        int tWidths[] = { 17, 66, 17 };
        table.setWidths(tWidths);
        table.getDefaultCell().setPadding(0);
        table.setWidthPercentage(100); // percentage
        table.getDefaultCell().setBorderWidth(0);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        // -------------reportNo--------------
        PdfPCell cell1 = new PdfPCell(new Phrase("報表編號: BLADR41", cFont));
        cell1.setBorder(0);
        table.addCell(cell1);

        // -------------tradevan name----------------
        PdfPCell cell2 = new PdfPCell(new Phrase("關貿網路股份有限公司", headerFont));
        cell2.setBorder(0);
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell2);

        // -------------makeDate----------------
        Phrase phrase = new Phrase("製表日期:    ", cFont);
        phrase.add(new Phrase("XXXXXXX", cFont));
        PdfPCell cell3 = new PdfPCell(phrase);
        cell3.setBorder(0);
        table.addCell(cell3);
        
        // -------------makePerson--------------
        PdfPCell cell4 = new PdfPCell(new Phrase("製表人員: SYSTEM", cFont));
        cell4.setBorder(0);
        table.addCell(cell4);

        // -------------report name----------------
        PdfPCell cell5 = new PdfPCell(new Phrase("防冒客戶逾五日未開機接收訊息之廠商月報表", headerFont));
        cell5.setBorder(0);
        cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell5);

        // -------------page----------------
        phrase = new Phrase("頁數:    ", cFont);
        phrase.add(new Phrase("  OF  ", cFont));
        phrase.add(new Phrase("xxxx", cFont));
        
        PdfPCell cell6 = new PdfPCell(phrase);
        cell6.setBorder(0);
        cell6.setFixedHeight(20f);
        table.addCell(cell6);

        // -------------space----------------
        PdfPCell cell7 = new PdfPCell(new Phrase(" ", cFont));
        cell7.setBorder(0);
        table.addCell(cell7);

        // -------------startDate-endDate----------------
        PdfPCell cell8 = new PdfPCell(new Phrase("期間:    " + "XXXXXXXX" + " 至 " + "XXXXXXX", cFont));
        cell8.setBorder(0);
        cell8.setMinimumHeight(35f);
        cell8.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell8);

        // -------------space----------------
        PdfPCell cell19 = new PdfPCell(new Phrase(" ", cFont));
        cell19.setBorder(0);
        table.addCell(cell19);
        return table;
	}
	
	public PdfPTable writeHeaderTwo() throws Exception {
        String path = "C:/workspace/workspace_GM2/spis_web/src/main/webapp/PDF/kaiu.ttf";
        BaseFont baseCFont = BaseFont.createFont(path, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED); // for traditional chinese -->for 細明體
        BaseFont headerCFont = BaseFont.createFont(path, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        Font cFont = new Font(baseCFont, 10, Font.NORMAL);
        Font headerFont = new Font(headerCFont, 14, Font.BOLD);
        
        PdfPTable table = new PdfPTable(8);
        try {
            int tWidths1[] = { 11, 20, 33, 10, 9, 4, 9, 4 };
            table.setWidths(tWidths1);
            table.getDefaultCell().setPadding(0);
            table.setWidthPercentage(100); // percentage
            table.getDefaultCell().setBorderWidth(0);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

            // -------------column name----------------
            System.out.println("統一編號");
            PdfPCell cell10 = new PdfPCell(new Phrase("   統一編號", cFont));
            cell10.setBorder(0);
            cell10.setVerticalAlignment(Element.ALIGN_TOP);
            table.addCell(cell10);

            System.out.println("公司名稱");
            PdfPCell cell11 = new PdfPCell(new Phrase("公司名稱", cFont));
            cell11.setBorder(0);
            cell11.setVerticalAlignment(Element.ALIGN_TOP);
            table.addCell(cell11);

            System.out.println("地址");
            PdfPCell cell12 = new PdfPCell(new Phrase("地址", cFont));
            cell12.setBorder(0);
            cell12.setVerticalAlignment(Element.ALIGN_TOP);
            table.addCell(cell12);

            System.out.println("電話");
            PdfPCell cell13 = new PdfPCell(new Phrase("電話", cFont));
            cell13.setBorder(0);
            cell13.setVerticalAlignment(Element.ALIGN_TOP);
            table.addCell(cell13);

            System.out.println("用戶代碼");
            PdfPCell cell14 = new PdfPCell(new Phrase("用戶代碼", cFont));
            cell14.setBorder(0);
            cell14.setVerticalAlignment(Element.ALIGN_TOP);
            table.addCell(cell14);

            System.out.println("次數");
            PdfPCell cell15 = new PdfPCell(new Phrase("次數", cFont));
            cell15.setBorder(0);
            cell15.setVerticalAlignment(Element.ALIGN_TOP);
            table.addCell(cell15);

            System.out.println("未開機日期");
            PdfPCell cell16 = new PdfPCell(new Phrase("未開機日期", cFont));
            cell16.setBorder(0);
            cell16.setVerticalAlignment(Element.ALIGN_TOP);
            table.addCell(cell16);

            System.out.println("space");
            // -------------space----------------
            PdfPCell cell18 = new PdfPCell(new Phrase(" ", cFont));
            cell18.setBorder(0);
            cell18.setVerticalAlignment(Element.ALIGN_TOP);
            table.addCell(cell18);

            System.out.println("斷行 1");
            // -------------斷行----------------
            PdfPCell cell9 = new PdfPCell(new Phrase(" ", cFont));
            cell9.setBorder(0);
            cell9.setColspan(8);
            cell9.setFixedHeight(8f);
            table.addCell(cell9);

            System.out.println("斷行 2");
            // -------------斷行----------------
            PdfPCell cell19 = new PdfPCell(new Phrase(" ", cFont));
            cell19.setBorder(0);
            cell19.setColspan(8);
            cell19.setBorder(Rectangle.TOP);
            cell19.setFixedHeight(8f);
            table.addCell(cell19);
        } catch (DocumentException e) {
            System.out.println(e.getMessage());
            System.out.println(e);
            throw e;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e);
            throw e;
        }
        // doc.add(table);
        return table;
    }
	
	public PdfPTable writeRow() throws Exception {
        String path = "C:/workspace/workspace_GM2/spis_web/src/main/webapp/PDF/kaiu.ttf";
        BaseFont baseCFont = BaseFont.createFont(path, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED); // for traditional chinese -->for 細明體
        BaseFont headerCFont = BaseFont.createFont(path, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        Font cFont = new Font(baseCFont, 10, Font.NORMAL);
        Font headerFont = new Font(headerCFont, 14, Font.BOLD);

        String codeId = "BAN";
        String data01 = "CUST_ID";
        String data02 = "NAME_C";
        String data03 = "ADDRESS_C";
        String data04 = "TEL_NO";
        PdfPTable table = new PdfPTable(8);
        int tWidths1[] = { 11, 20, 33, 10, 9, 4, 9, 4 };
        table.setWidths(tWidths1);
        table.getDefaultCell().setPadding(0);
        table.setWidthPercentage(100); // percentage
        table.getDefaultCell().setBorderWidth(0);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        // -------------ban--------------
        PdfPCell cell1 = new PdfPCell(new Phrase("    " + codeId, cFont));
        cell1.setBorder(0);
        cell1.setMinimumHeight(16f);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell1);

        // -------------nameC----------------
        PdfPCell cell2 = new PdfPCell(new Phrase(data02, cFont));
        cell2.setBorder(0);
        table.addCell(cell2);

        // -------------addressC----------------
        PdfPCell cell3 = new PdfPCell(new Phrase(data03, cFont));
        cell3.setBorder(0);
        table.addCell(cell3);

        // -------------tel----------------
        PdfPCell cell4 = new PdfPCell(new Phrase(data04, cFont));
        cell4.setBorder(0);
        table.addCell(cell4);

        // -------------custId----------------
        PdfPCell cell5 = new PdfPCell(new Phrase(data01, cFont));
        cell5.setBorder(0);
        table.addCell(cell5);

        // --------------times----------------
        String tmpTimes = "1";
        PdfPCell cell6 = new PdfPCell(new Phrase(tmpTimes, cFont));
        cell6.setBorder(0);
        cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell6);

        // --------------nunUseDate----------------
        PdfPCell cell7 = new PdfPCell(new Phrase("XXXX", cFont));
        cell7.setBorder(0);
        cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell7);

        // -------------space----------------
        PdfPCell cell18 = new PdfPCell(new Phrase(" ", cFont));
        cell18.setBorder(0);
        table.addCell(cell18);

        //doc.add(table);
        return table;
    }
	
	public PdfPTable writeTotalCompany() throws Exception {
        String path = "C:/workspace/workspace_GM2/spis_web/src/main/webapp/PDF/kaiu.ttf";
        BaseFont baseCFont = BaseFont.createFont(path, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED); // for traditional chinese -->for 細明體
        BaseFont headerCFont = BaseFont.createFont(path, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        Font cFont = new Font(baseCFont, 10, Font.NORMAL);
        Font headerFont = new Font(headerCFont, 14, Font.BOLD);

        PdfPTable table = new PdfPTable(2);
        table.setWidths(new int[] { 25, 75 });
        table.getDefaultCell().setPadding(0);
        table.setWidthPercentage(100); // percentage
        table.getDefaultCell().setBorderWidth(0);

        // -------------斷行----------------
        PdfPCell cell9 = new PdfPCell(new Phrase(" ", cFont));
        cell9.setBorder(0);
        cell9.setColspan(2);
        cell9.setBorder(Rectangle.BOTTOM);
        cell9.setFixedHeight(8f);
        table.addCell(cell9);

        // --------------totalCompany----------------
        PdfPCell cell1 = new PdfPCell(new Phrase(" ", cFont));
        cell1.setMinimumHeight(20f);
        cell1.setBorder(0);
        cell1.setVerticalAlignment(Element.ALIGN_BASELINE);
        table.addCell(cell1);

        PdfPCell cell2 = new PdfPCell(new Phrase("\n公司總計：     100", cFont));
        cell2.setMinimumHeight(20f);
        cell2.setBorder(0);
        cell2.setVerticalAlignment(Element.ALIGN_BASELINE);
        table.addCell(cell2);
        //doc.add(table);
        return table;
    }
}


