package gtu.itext.alexhsu;

import java.io.File;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class TestReport extends PDFReport {

    static String getPath() {
        System.out.println(TestReport.class.getResource("").getPath());
        return null;
    }

    public static void main(String[] args) {
        TestReport pdfReport = new TestReport();
        String reportName = "c://reportFileName.pdf";

        pdfReport.setPrintDate("101/02/20  16:43:32");
        pdfReport.setReportFileName(reportName);
        pdfReport.setFontPath("c:/windows/fonts/KAIU.TTF");
        pdfReport.setReportSize(PDFReport.REPORTSIZE_A4_LANDSCAPE);
        pdfReport.setPosition(10);
        pdfReport.setOfficeName("內政部");
        pdfReport.doReport();
        try {
            System.out.println("##### == " + getPath());

            File file = new File(reportName);
            System.out.println("reportName === " + file.getAbsolutePath());

            Process pr = Runtime.getRuntime().exec("cmd /c  " + reportName);

            pr.waitFor();
            System.out.println("reportend");
            // 刪除列印檔案

            file.delete();

        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        System.out.println("done...");
    }

    @Override
    public void doBody(PdfWriter writer, Document docu) {
        {
            float[] colsSize = { 1f, 1f, 1f };
            PdfPTable table = new PdfPTable(colsSize);
            //      table.setTotalWidth(90);
            table.setWidthPercentage(100);
            table.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell cell = new PdfPCell();
            addCell(table, "警語", cell, PDFReport.CELL_ALIGN_LEFT, PDFReport.CELL_NO_BORDER, 3);
            addCell(table, "核發機關：", cell, PDFReport.CELL_ALIGN_LEFT, PDFReport.CELL_NO_BORDER, 1);
            addCell(table, "核發案號：", cell, PDFReport.CELL_ALIGN_LEFT, PDFReport.CELL_NO_BORDER, 1);
            addCell(table, "申請範圍：親等", cell, PDFReport.CELL_ALIGN_RIGHT, PDFReport.CELL_NO_BORDER, 1);
            addCell(table, "申請事由：", cell, PDFReport.CELL_ALIGN_LEFT, PDFReport.CELL_NO_BORDER, 3);
            addCell(table, "法令依據：", cell, PDFReport.CELL_ALIGN_LEFT, PDFReport.CELL_NO_BORDER, 3);
            addCell(table, "資料最後異動日期：", cell, PDFReport.CELL_ALIGN_LEFT, PDFReport.CELL_NO_BORDER, 1);
            addCell(table, "列印日期：", cell, PDFReport.CELL_ALIGN_LEFT, PDFReport.CELL_NO_BORDER, 1);
            addCell(table, " ", cell, PDFReport.CELL_ALIGN_LEFT, PDFReport.CELL_NO_BORDER, 1);
            table.setSpacingAfter(10);
            try {
                docu.add(table);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }

        for (int okok = 0; okok < 5; okok++) {
            float[] titleWidth = { 1f };
            PdfPTable table = new PdfPTable(titleWidth);
            table.setWidthPercentage(100);
            PdfPCell titalCell = new PdfPCell();
            titalCell.setMinimumHeight(20);
            addCell(table, "親等關聯資料", titalCell, PDFReport.CELL_ALIGN_CENTER, PDFReport.CELL_NO_BORDER, 1, 16);
            titalCell.setMinimumHeight(5);
            try {
                docu.add(table);
            } catch (DocumentException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            table = new PdfPTable(new float[] { 20f, 20f, 20f, 20f, 20f });
            table.setWidthPercentage(100f);//設定整體寬度
            PdfPCell cell = new PdfPCell();
            for (int ii = 0; ii < 77; ii++) {
                addCell(table, String.valueOf(ii), cell, PDFReport.CELL_ALIGN_LEFT, PDFReport.CELL_BOX, 1);
                if (ii > 5 && (ii % 5 == 0)) {
                    addCell(table, String.valueOf(ii), cell, PDFReport.CELL_ALIGN_LEFT, PDFReport.CELL_BOX, 5);
                }
            }
            table.setSpacingAfter(15);

            try {
                docu.add(table);
            } catch (DocumentException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

}
