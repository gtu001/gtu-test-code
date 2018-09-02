/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.itext.iisi;

import gtu.itext.iisi.marker.MarkInfo;
import gtu.itext.iisi.marker.Marker;
import gtu.itext.iisi.marker.PDFPageInfo;

import java.awt.Color;
import java.awt.geom.Point2D.Float;
import java.io.IOException;
import java.io.OutputStream;
import java.util.EnumSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import tw.gov.moi.common.SystemConfig;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 提供簡易的API, 用以產生PDF文件. 在現在的版本中, 將以隨機產生的owner密碼, 限制檔案只能列印.
 */
public class PDFDocument {

    //####################################################################
    //## [Method] sub-block : 分組計數器.
    //####################################################################
    /**
     * The Class GroupCounter.
     */
    private static class GroupCounter {

        private int offset = 0;

        /** The count. */
        private int count = 0;

        /**
         * Recount.
         */
        public void recount() {
            this.count = 0;
        }
    }

    /** Logger Object. */
    @SuppressWarnings("unused")
    final private static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PDFDocument.class);

    /**
     * 用於在加入 elements, 設定本頁是否已開始.
     */
    private class MyDocument extends Document {

        /** The topPoint. */
        final private float topPoint;

        /**
         * Instantiates a new myDocument.
         * 
         * @param pageSize the pageSize
         * @param marginLeft the marginLeft
         * @param marginRight the marginRight
         * @param marginTop the marginTop
         * @param marginBottom the marginBottom
         */
        private MyDocument(Rectangle pageSize, LayoutInfo layout) {
            super(pageSize, layout.marginLeft//
                    , layout.marginRight//
                    , layout.marginTop + layout.headerExtra//
                    , layout.marginBottom + layout.footerExtra//
            );
            this.topPoint = pageSize.getHeight() - layout.marginTop;
        }

        /**
         * @see com.lowagie.text.Document#add(com.lowagie.text.Element)
         */
        @Override
        public boolean add(Element element) throws DocumentException {

            final int originalPage = PDFDocument.this.pdfWriter.getPageNumber();

            //boolean originBeginFlag = PDFDocBuilder.this.newPageBegin;
            //System.out.println("H:" + PDFDocBuilder.this.document.bottomMargin());
            //{
            //    final float verticalPosition = getPdfWriter().getVerticalPosition(true);
            //    System.out.println("\n\n");
            //    //System.out.println("before add : " + PDFDocBuilder.this.newPageBegin);
            //    System.out.println("before add : " + verticalPosition);
            //    System.out.println("before add : " + originalPage);
            //    System.out.println("before add : " + PDFDocBuilder.this.pdfWriter.getCurrentPageNumber());
            //    System.out.println("add : " + element);
            //}

            final boolean result = super.add(element);
            if (result) {
                final float verticalPosition = getPdfWriter().getVerticalPosition(true);
                PDFDocument.this.pageChanged = (originalPage != PDFDocument.this.pdfWriter.getPageNumber());
                PDFDocument.this.newPageBegin = (verticalPosition == this.topPoint); // 已填滿一頁，接下來的輸出會在新頁的最開端.;
                //                if (PDFDocument.this.pageChanged) {
                //                    //System.out.println("new page after : " + element);
                //                    LOGGER.debug("verticalPosition  :{}", verticalPosition);
                //                    LOGGER.debug("pageChanged  :{}", PDFDocument.this.pageChanged);
                //                    LOGGER.debug("newPageBegin :{}", PDFDocument.this.newPageBegin);
                //                }
            } else {
                PDFDocument.this.pageChanged = false;
            }

            //                        {
            //                            final float verticalPosition = getPdfWriter().getVerticalPosition(true);
            //                            System.out.println("after add : " + verticalPosition);
            //                            System.out.println("after add : " + PDFDocBuilder.this.pdfWriter.getPageNumber());
            //                            System.out.println("after add : " + PDFDocBuilder.this.pdfWriter.getCurrentPageNumber());
            //                        }

            return result;
        }
    }

    //================================================
    //== [static variables] Block Start
    //====

    //private static final String CONTENT_TYPE = "application/pdf";

    //####################################################################
    //## [static variables] sub-block : PDF 文件保全設定key值
    //####################################################################

    /** The Constant KEY_Encryption_Disable. */
    public static final String KEY_Encryption_Enable = "KEY_Encryption_Enable";

    /** The Constant KEY_Encryption_Permissions. */
    public static final String KEY_Encryption_Permissions = "KEY_Encryption_Permissions";

    /** The Constant KEY_Encryption_UserPassword. */
    public static final String KEY_Encryption_UserPassword = "KEY_Encryption_UserPassword";

    /** The Constant KEY_Encryption_OwnerPassword. */
    public static final String KEY_Encryption_OwnerPassword = "KEY_Encryption_OwnerPassword";

    //====
    //== [static variables] Block Stop
    //================================================
    //== [instance variables] Block Start
    //====
    /**
     * iText 的文件物件. 本來設計目標是希望可以重複使用一個 Document 實體. 但是在iText中, 似乎並沒有reset一個document資料的method. 所以在目前的設計中, 每次重設(call
     * init())PDFDocBuilder 時, 仍然需要重新new一次Document 及其相關物件
     */
    private Document document = null;

    /** 將doc以pdf格式輸出的 writer. */
    private PdfWriter pdfWriter = null;

    private CounterOutputStream baout;

    final private SystemConfig systemConfig;

    //####################################################################
    //## [instance variables] sub-block : 產生頁首頁尾、浮水印 相關
    //####################################################################

    /** The watermarkImage. */
    private List<Marker> markers;

    /** The pageNumber. */
    private int pageNumber = 0;

    /** 頁數計算. */
    private GroupCounter counter = new GroupCounter();

    private boolean toBeClose = false;

    private boolean showMarginBorder = false;

    //####################################################################
    //## [instance variables] sub-block : 輸出換頁資訊
    //####################################################################

    /**
     * 輸出過程發生換頁，且新的一頁尚未輸出任何資料.
     */
    private boolean newPageBegin = true;

    /**
     * 輸出過程發生換頁.
     */
    private boolean pageChanged = false;

    //####################################################################
    //## [instance variables] sub-block : 文件內部使用字型 相關
    //## 中文文件所使用的字型集合
    //####################################################################

    /** The fontFactory. */
    private CHTFontFactory fontFactory;

    /** 定義文件所使用字型基準. */
    private FontInfo fontInfo;

    private LayoutInfo layoutInfo;

    //====
    //== [instance variables] Block Stop
    //================================================
    //== [static Constructor] Block Start
    //====
    //====
    //== [static Constructor] Block Stop
    //================================================
    //== [Constructors] Block Start (含init method)
    //====

    /** 限定只有Builder Manager 可建立此類物件. */
    protected PDFDocument(SystemConfig systemConfig) {
        this.systemConfig = systemConfig;
    }

    //====
    //== [Constructors] Block Stop
    //================================================
    //== [Overrided Method] Block Start (toString/equals+hashCode)
    //====
    //====
    //== [Overrided Method] Block Stop
    //================================================
    //== [Accessor] Block Start
    //====

    /**
     * Gets the {@link pdfWriter}.
     * 
     * @return 傳回 pdfWriter。
     */
    public final PdfWriter getPdfWriter() {
        return this.pdfWriter;
    }

    /**
     * Sets the {@link fontFactory}.
     * 
     * @param fontFactory the new {@link fontFactory}
     */
    public void setFontFactory(CHTFontFactory fontFactory) {
        this.fontFactory = fontFactory;
        this.fontInfo = this.fontFactory.createFontInfo(12);
    }

    /**
     * 取得內含的 iText Document 物件.
     * 
     * @return the {@link document}
     */
    public Document getDocument() {
        return this.document;
    }

    /**
     * Gets the {@link pageNumber}.
     * 
     * @return the {@link pageNumber}
     */
    public int getPageNumber() {
        return this.pageNumber;
    }

    /**
     * Checks if is newPageBegin.
     * 
     * @return true, if is newPageBegin
     */
    public boolean isNewPageBegin() {
        return this.newPageBegin;
    }

    /**
     * Checks if is pageChanged.
     * 
     * @return true, if is pageChanged
     */
    public boolean isPageChanged() {
        return this.pageChanged;
    }

    /**
     * @return the fontInfo
     */
    public FontInfo getFontInfo() {
        return this.fontInfo;
    }

    /**
     * @param showMarginBorder the showMarginBorder to set
     */
    public void setShowMarginBorder(boolean showMarginBorder) {
        this.showMarginBorder = showMarginBorder;
    }

    //====
    //== [Accessor] Block Stop
    //================================================
    //== [Static Method] Block Start
    //====
    //====
    //== [Static Method] Block Stop
    //================================================
    //== [Method] Block Start
    //====
    //####################################################################
    //## [Method] sub-block : 初始化相關
    //####################################################################

    /**
     * 初始化, 設定此份文件的版面資訊 (頁面大小, 留空, 使用的浮水印).
     * 
     * @param pageSize the pageSize
     * @param marginLeft the marginLeft
     * @param marginRight the marginRight
     * @param marginTop the marginTop
     * @param marginBottom the marginBottom
     * @param markInfos 如果指定的浮水印別名不存在, 並不會導致Exception產生, 只是輸出的pdf中不會使用浮水印而已.
     * @param properties the properties
     * @throws DocumentException the document exception
     */
    protected void init(Rectangle pageSize//
                        , LayoutInfo layoutInfo //
                        , List<MarkInfo> markInfos//
                        , Properties properties, OutputStream out // 
    ) throws DocumentException {

        // this.pageCountTemplates.clear();
        this.markers = MarkInfo.toMarkers(this.systemConfig, markInfos);
        this.document = new MyDocument(pageSize, layoutInfo);
        this.layoutInfo = layoutInfo;
        this.toBeClose = false;
        this.baout = new CounterOutputStream(out);
        try {
            this.pdfWriter = PdfWriter.getInstance(this.document, this.baout);
        } catch (DocumentException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        this.setEncryption(properties);
        this.pdfWriter.setPageEvent(new PageEventHelper());
        this.document.open();

        // 重設字型樣式.
        this.fontFactory = CHTFontFactory.Default;
    }

    /**
     * 設定文件的安全機制.
     * 
     * @param properties Properties KEY_Encryption_Disable = Boolean.TRUE ： 停用安全機制. 否則啟用.
     * 
     * @throws DocumentException 設定安全性失敗.
     */
    private void setEncryption(Properties properties) throws DocumentException {

        boolean useEncryption = false;
        int encryption_Permissions = PdfWriter.ALLOW_PRINTING; //PdfWriter.ALLOW_SCREENREADERS;
        String encryption_OwnerPassword = null;
        String encryption_UserPassword = null;

        if (properties != null) {
            // 只有此值設為 TRUE 時, 才啟用 Encryption 功能
            useEncryption = Boolean.TRUE.equals(properties.get(KEY_Encryption_Enable));
            if (useEncryption) {
                if (properties.contains(KEY_Encryption_Permissions)) {
                    try {
                        encryption_Permissions = ((Integer) properties.get(KEY_Encryption_Permissions)).intValue();
                    } catch (Exception e) {
                    }
                }
                encryption_UserPassword = (String) properties.get(KEY_Encryption_UserPassword);
                encryption_OwnerPassword = (String) properties.get(KEY_Encryption_OwnerPassword);
            }
        }
        if (useEncryption) {
            this.pdfWriter.setEncryption(//
                    encryption_UserPassword == null ? null : encryption_UserPassword.getBytes(),//
                    encryption_OwnerPassword == null ? null : encryption_OwnerPassword.getBytes(),//
                    encryption_Permissions,//
                    PdfWriter.ENCRYPTION_AES_128); // 
        }
    }

    //####################################################################
    //## [Method] sub-block : 關閉PDF文件，並寫入串流操作
    //####################################################################

    /**
     * CloseMe.
     * 
     * @throws IOException
     */
    private void closeMe() throws IOException {
        try {
            PDFDocumentManager.getInstance(null).close(this);
        } catch (Exception e) {
        }
        this.baout.close();

    }

    /**
     * 結束產生一個pdf文檔, 並以指定的串流輸出.
     * 
     * @param out the out
     * @return the int
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public PDFInfo close() throws IOException {

        this.toBeClose = true;
        this.document.close();
        int pageSize = this.counter.count - this.counter.offset;
        int length = this.baout.getSize();
        try {
            closeMe();
        } finally {
            this.baout = null;
            this.document = null;
            this.pdfWriter = null;
            this.markers = null;
            this.layoutInfo = null;
            this.fontInfo = null;
        }
        return new PDFInfo(length, pageSize);
    }

    /**
     * CloseAndGC.
     * 
     * @param out the out
     * @return the int
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public PDFInfo closeAndGC(OutputStream out) throws IOException {
        try {
            return close();
        } catch (IOException ex) {
            throw ex;
        } finally {
            Runtime.getRuntime().gc();
        }
    }

    //####################################################################
    //## [Method] sub-block : 常用文件內容輸出處理.
    //####################################################################

    /**
     * 直接要求文件換頁. Signals that an new page has to be started.
     * 
     * @return <CODE>true</CODE> if the page was added, <CODE>false</CODE> if not.
     */
    public boolean newPage() {
        return this.document.newPage();
    }

    /**
     * 加入 Elements.
     * 
     * @param element the element
     * @return true, if successful
     * @throws DocumentException the document exception
     */
    public boolean add(Element element) throws DocumentException {
        return this.document.add(element);
    }

    /**
     * 輸出單行文字.
     * 
     * @param str the str
     * @throws DocumentException the document exception
     */
    public void writeText(String str) throws DocumentException {
        writeText(str, this.fontInfo.size);
    }

    /**
     * 輸出單行文字.
     * 
     * @param str the str
     * @throws DocumentException the document exception
     */
    public void writeText(final String str, int size) throws DocumentException {
        Paragraph p = new Paragraph(StringUtils.defaultString(str), this.fontFactory.getNormalChinese(size));
        //p.setSpacingAfter(2);
        this.document.add(p);
    }

    /**
     * 輸出文字，並指定對齊方式.
     */
    public void writeText(final String str, int size, int align) throws DocumentException {
        Paragraph p = new Paragraph(StringUtils.defaultString(str), this.fontFactory.getNormalChinese(size));
        p.setAlignment(align);
        //p.setSpacingAfter(2);
        this.document.add(p);
    }

    //####################################################################
    //## [Method] sub-block : Table
    //####################################################################

    public float estimateTableWidthPercentage(Coordinate coordinate, float width) {
        float totalWidth = this.document.right() - this.document.left();
        float tableWidth = coordinate.trans(width);
        final float p = 100 * tableWidth / totalWidth;
        return Math.min(Math.max(p, 0f), 100f);
    }

    /**
     * 建立表格, 僅定義欄數.
     */
    public TableiText createTable(float widthPercentage, int numColumns) {
        TableiText tableiText = new TableiText(this, numColumns);
        tableiText.getTable().setWidthPercentage(widthPercentage);
        return tableiText;
    }

    /**
     * 建立表格
     */
    public TableiText createTable(float widthPercentage, float[] widths) throws DocumentException {
        TableiText tableiText = new TableiText(this, widths);
        tableiText.getTable().setWidthPercentage(widthPercentage);
        return tableiText;
    }

    /**
     * 建立表格
     */
    public TableiText createTable(final Coordinate coordinate, float[] widths) throws DocumentException {
        TableiText tableiText = new TableiText(this, widths);
        float width = 0;
        for (float f : widths) {
            width += f;
        }
        tableiText.getTable().setTotalWidth(coordinate.trans(width));
        return tableiText;
    }

    //####################################################################
    //## [Method] sub-block : 字型定義.
    //####################################################################

    public FontInfo createFontInfo(final int size) {
        return this.fontFactory.createFontInfo(size, EnumSet.noneOf(FontStyle.class), Color.BLACK);
    }

    public FontInfo createFontInfo(final int size, final Set<FontStyle> style, final Color color) {
        return this.fontFactory.createFontInfo(size, style, color);
    }

    public FontInfo createFontInfo(final int size, final Set<FontStyle> style, final Color color, final Color bcolor) {
        return this.fontFactory.createFontInfo(size, style, color, bcolor);
    }

    public FontInfo createFontInfo(final int size, final FontStyle style, final Color color) {
        return this.fontFactory.createFontInfo(size, style, color);
    }

    public FontInfo createFontInfo(final int size, final FontStyle style, final Color color, final Color bcolor) {
        return this.fontFactory.createFontInfo(size, style, color, bcolor);
    }

    public ParagraphBuilder paragraphBuilder() throws DocumentException {
        return new ParagraphBuilder(this);
    }

    //====
    //== [Method] Block Stop
    //================================================
    //== [Main Method] Block Start
    //====
    // public static void main(String[] args) {
    //      
    // }
    //====
    //== [Main Method] Block Stop
    //================================================

    //####################################################################
    //## [Method] sub-block : 常用文件內容輸出處理.
    //####################################################################

    /**
     * 頁面事件處理.
     */
    public class PageEventHelper extends PdfPageEventHelper {

        /**
         * 初始總頁數區塊.
         * 
         * @param writer the writer
         * @param document the document
         * @see com.lowagie.text.pdf.PdfPageEventHelper#onOpenDocument(com.lowagie.text.pdf.PdfWriter,
         *      com.lowagie.text.Document)
         */
        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            PDFDocument.this.counter.recount();
        }

        /**
         * 單頁開始 浮水印處理.
         * 
         * @param writer the writer
         * @param document the document
         */
        @Override
        public void onStartPage(PdfWriter writer, Document document) {
            PDFDocument.this.pageNumber = writer.getPageNumber();
            PDFDocument.this.newPageBegin = true;
        }

        /**
         * @see com.lowagie.text.pdf.PdfPageEventHelper#onEndPage(com.lowagie.text.pdf.PdfWriter,
         *      com.lowagie.text.Document)
         */
        @Override
        public void onEndPage(PdfWriter writer, Document document) {

            final PdfContentByte dc = writer.getDirectContent();
            final PdfContentByte dcu = writer.getDirectContentUnder();
            PDFDocument.this.counter.count++;

            //==========================================
            // 輸出 浮水印
            //==========================================

            if (PDFDocument.this.markers != null) {
                final Rectangle pageSize = PDFDocument.this.pdfWriter.getPageSize();
                final PDFPageInfo pageInfo = new PDFPageInfo();
                pageInfo.setPageSize(pageSize);
                pageInfo.setCurrentPageNumber(PDFDocument.this.pageNumber);
                if (PDFDocument.this.toBeClose) {
                    pageInfo.setTotalPageNumber(PDFDocument.this.pageNumber);
                } else {
                    pageInfo.setTotalPageNumber(PDFDocument.this.pageNumber + 1);
                }
                for (Marker marker : PDFDocument.this.markers) {
                    try {
                        marker.drawMarker(DocumentUtils.Default, dcu, pageInfo);
                    } catch (IOException e) {
                    } catch (BadElementException e) {
                    } catch (DocumentException e) {
                    }
                }
            }

            //==========================================
            // 輸出  header  /  頁碼
            //==========================================

            if (PDFDocument.this.layoutInfo.hasHeader()) {
                dc.beginText();
                PDFDocument.this.layoutInfo.drawHeaders(dc, document//
                        , PDFDocument.this.fontFactory.getBaseFont()//
                        , PDFDocument.this.counter.count);
                dc.endText();
            }

            //==========================================
            // 輸出頁框
            //==========================================
            //@formatter:off
            if (PDFDocument.this.showMarginBorder) {
                LOGGER.debug("for pageInfo : top : {}/{}", document.top(), document.topMargin());
                LOGGER.debug("for pageInfo : left : {}/{}", document.left(), document.leftMargin());
                LOGGER.debug("for pageInfo : right : {}/{}", document.right(), document.rightMargin());
                LOGGER.debug("for pageInfo : buttom : {}/{}/{}", document.bottom(), document.bottomMargin());
                final Rectangle pageSize = PDFDocument.this.pdfWriter.getPageSize();
                DocumentUtils.Default.drawLines(dcu, new Float(0, document.top()), new Float(pageSize.getWidth(), document.top()));
                DocumentUtils.Default.drawLines(dcu, new Float(0, document.bottomMargin()),new Float(pageSize.getWidth(), document.bottomMargin()));
                DocumentUtils.Default.drawLines(dcu, new Float(document.right(), 0), new Float(document.right(), pageSize.getHeight()));
                DocumentUtils.Default.drawLines(dcu, new Float(document.leftMargin(), 0),new Float(document.leftMargin(), pageSize.getHeight()));
            }
            //@formatter:on

        }

        /**
         * @see com.lowagie.text.pdf.PdfPageEventHelper#onCloseDocument(com.lowagie.text.pdf.PdfWriter,
         *      com.lowagie.text.Document)
         */
        @Override
        public void onCloseDocument(PdfWriter writer, Document document) {
            if (PDFDocument.this.layoutInfo.hasHeader()) {
                PDFDocument.this.layoutInfo.drawHeaderTemplates(writer, PDFDocument.this.fontFactory.getBaseFont(), PDFDocument.this.counter.count);

            }
        }
    }

}
