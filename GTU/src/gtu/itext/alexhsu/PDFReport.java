package gtu.itext.alexhsu;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.GrayColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 作為產生PDF報表的abstract class 定義產生報表所需的流程，subclass 只需撰寫報表內容所需的格式與資料
 * 
 * @author Alex
 * @version 1.0.0
 * 
 */
public abstract class PDFReport {
    public static final int REPORTSIZE_A4 = 0;
    public static final int REPORTSIZE_A4_LANDSCAPE = 1;
    public static final int CELL_NO_BORDER = PdfPCell.NO_BORDER;
    public static final int CELL_BOX = PdfPCell.BOX;
    public static final int CELL_ALIGN_RIGHT = PdfPCell.ALIGN_RIGHT;
    public static final int CELL_ALIGN_LEFT = PdfPCell.ALIGN_LEFT;
    public static final int CELL_ALIGN_CENTER = PdfPCell.ALIGN_CENTER;
    //紀錄報表大小與格式
    private int reportSize = 0;
    //報表所使用的字型
    private Font font = null;
    //頁碼所在位置
    private float position = 0;
    //字型資料
    private BaseFont basefont;
    //字型檔所在位置
    private String fontPath = "";
    //報表所在位置
    private String reportFileName = "";
    //規費-每頁單價
    //private final int PageMoney = 40;
    //100/5/24：改為每張30元
    private final int PageMoney = 30;

    private String officeName;

    private String timeSum;

    private String printDate;

    public PDFReport() {
        init();
    }

    private void init() {
        try {
            // basefont = BaseFont.createFont("e:/test/ma1b5xp-pcnew.ttf",
            // BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            basefont = BaseFont.createFont("MHei-Medium", "UniCNS-UCS2-H", BaseFont.EMBEDDED);
            font = new Font(basefont, 12);
        } catch (DocumentException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 建立報表主體內容
     * 
     * @param writer
     * @param docu
     */
    public abstract void doBody(PdfWriter writer, Document docu);

    /**
     * 產生報表，制定報表產生流程
     */
    public final void doReport() {
        Document docu = null;
        ByteArrayOutputStream baos = null;

        if (reportSize == 0) {
            docu = new Document(PageSize.A4, 30, 30, 30, 30);
        } else if (reportSize == 1) {
            docu = new Document(PageSize.A4.rotate(), 30, 30, 30, 30);
        }

        try {

            if (fontPath.length() > 0) {

                basefont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                font = new Font(basefont, 10);
            }

            PdfWriter writer;

            //			baos=new ByteArrayOutputStream();

            writer = PdfWriter.getInstance(docu, new FileOutputStream(reportFileName));
            //			writer=PdfWriter.getInstance(docu, baos);

            //			writer = PdfWriter.getInstance(docu, System.out);

            //隱藏Tool bar menu bar 
            writer.setViewerPreferences(PdfWriter.HideMenubar | PdfWriter.HideToolbar);
            //只允許列印
            writer.setEncryption(null, null, PdfWriter.ALLOW_PRINTING, PdfWriter.STANDARD_ENCRYPTION_128);

            PageXofY pageNo = new PageXofY();
            //			System.out.println("position="+position);
            pageNo.setFont(basefont);
            writer.setPageEvent(pageNo);

            //騎縫章
            Seal seal = new Seal();
            //將騎縫章註冊到PdfWriter裡
            writer.setPageEvent(seal);

            //設定浮水印
            WaterMark waterMark = new WaterMark();
            //設定浮水印的字型			
            Font ft = new Font(basefont, 48, Font.BOLD, new GrayColor(0.75f));
            waterMark.setFont(ft);
            Font pft = new Font(basefont, 12, Font.BOLD, new GrayColor(0.75f));
            waterMark.setPointFont(pft);
            System.out.println("officeName = " + officeName);
            waterMark.setSiteName(officeName);
            writer.setPageEvent(waterMark);

            docu.open();
            doBody(writer, docu);
            int n = writer.getPageNumber();
            //傳入騎縫章的最後一頁
            seal.setAllPage(n);
            docu.add(createPayTable(n));
            /*
             * 因原程式在填入資料，會照取出值的順序來排。 程式會用總頁數去計算規費，但是規費是印在最下方時，
             * 會造成因欄位數很多，而造成規費會跳至下一頁， 但是計算時並沒有計到規費這一頁，會導至於規費會錯誤， 及騎縫章錯誤。
             * 原錯誤例子：所有的資料只有三頁，因第3頁欄位很滿， 計算規費時，只有三張，將規費計算好時，要印出，
             * 因第3頁放不下，規費的資料就會跳到下一頁，也就是第四頁。 但是規費只有計算3頁的費用。
             * 
             * 所以在此先將塞一小行空白，讓頁數先跳到下一頁， 這樣規費在計算及騎縫章就不會錯誤。
             */
            //			docu.add(createSpaceTable());
            ////			//傳入騎縫章的最後一頁
            //			int i = writer.getPageNumber();
            //			seal.setAllPage(i);
            //			docu.add(createPayTable(i));

        } catch (DocumentException ex) {
            Logger.getLogger(PDFReport.class.getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(PDFReport.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("# docu.close() ...");
        docu.close();
    }

    // 計算規費
    public PdfPTable createPayTable(int pageNum) {
        float[] colsSize = { 1f };

        PdfPTable table = new PdfPTable(colsSize);
        table.setTotalWidth(100);
        //		int pageNum = docu.getPageNumber();
        int total = PageMoney * pageNum;
        PdfPCell cell = new PdfPCell();
        addCell(table, "規費：新臺幣" + total + "元整", cell, PDFReport.CELL_ALIGN_RIGHT, PDFReport.CELL_NO_BORDER, 1);
        return table;
    }

    public PdfPTable createSpaceTable() {
        float[] colsSize = { 1f };

        PdfPTable table = new PdfPTable(colsSize);
        table.setTotalWidth(0);
        PdfPCell cell = new PdfPCell();
        addCell(table, "", cell, PDFReport.CELL_ALIGN_RIGHT, PDFReport.CELL_NO_BORDER, 1);
        return table;
    }

    /**
     * 設定報表的大小，目前只有A4直印與A4橫印
     * 
     * @param reportSize
     */
    public void setReportSize(int reportSize) {

        this.reportSize = reportSize;
        if (reportSize != 0 && reportSize != 1) {
            this.reportSize = 0;
        }

    }

    protected void addCell(PdfPTable table, String celldata, PdfPCell cell, int align, int bordertype, int colSpans) {
        if (cell == null) {
            cell = new PdfPCell();
        }
        cell.setBorder(bordertype);
        cell.setColspan(colSpans);
        cell.setHorizontalAlignment(align);
        cell.setPaddingBottom(5);
        Phrase phrase = new Phrase(celldata, font);
        cell.setPhrase(phrase);
        table.addCell(cell);

    }

    protected void addCell(PdfPTable table, String celldata, PdfPCell cell, int align, int bordertype, int colSpans, int fontSize) {
        if (cell == null) {
            cell = new PdfPCell();
        }
        Font newFont = new Font(basefont, fontSize);
        cell.setBorder(bordertype);
        cell.setColspan(colSpans);
        cell.setPaddingBottom(5);
        cell.setHorizontalAlignment(align);
        Phrase phrase = new Phrase(celldata, newFont);
        cell.setPhrase(phrase);
        table.addCell(cell);

    }

    protected void addCell(PdfPTable table, Phrase cellPhrase, PdfPCell cell, int align, int bordertype, int colSpans) {
        if (cell == null) {
            cell = new PdfPCell();
        }
        cell.setPaddingBottom(5);
        cell.setBorder(bordertype);
        cell.setColspan(colSpans);
        cell.setHorizontalAlignment(align);
        cell.setPhrase(cellPhrase);
        table.addCell(cell);

    }

    /**
     * 設定頁碼所在位置，右上方向下多少point
     * 
     * @param position
     */
    public void setPosition(float position) {
        this.position = position;
    }

    /**
     * 設定字型檔位置，沒設定使用預設字型
     * 
     * @param fontPath
     */
    public void setFontPath(String fontPath) {
        this.fontPath = fontPath;

    }

    /**
     * 設定報表檔檔名與存放位置
     * 
     * @param reportFileName
     */
    public void setReportFileName(String reportFileName) {
        this.reportFileName = reportFileName;
    }

    public Font getFont() {
        return this.font;
    }

    /**
     * @return the officeName
     */
    public String getOfficeName() {
        return officeName;
    }

    /**
     * @param officeName
     *            the officeName to set
     */
    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    /**
     * @return the timeSum
     */
    public String getTimeSum() {
        return timeSum;
    }

    /**
     * @param timeSum
     *            the timeSum to set
     */
    public void setTimeSum(String timeSum) {
        this.timeSum = timeSum;
    }

    /**
     * @return the printDate
     */
    public String getPrintDate() {
        return printDate;
    }

    /**
     * @param printDate
     *            the printDate to set
     */
    public void setPrintDate(String printDate) {
        this.printDate = printDate;
    }
}
