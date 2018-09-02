/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.jsf;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Calendar;

import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

/**
 * PDF預覽列印
 */
@Component("pdfTransformController")
// @Controller("pdfTransformController")
@Scope("session")
public class PDFTransformController implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The logger. */
    private transient Logger logger = LoggerFactory.getLogger(getClass());

    /** PDF檔案位置 實體路徑. */
    private String pdfUrl;

    /** PDF檔名. */
    private String pdfFileName;

    /** 使用者ID. */
    private String userId;

    /** 目前檢視頁面頁碼. */
    private String currentPage;

    /** 是否預覽 true:預覽/false:直接列印. */
    private boolean reviewPage = true;

    /** 是否雙面列印 true:雙面/ false:單面. */
    private boolean twoSidePrint;

    /** 列印起始頁. */
    private String printStartPage;

    /** 列印結束頁. */
    private String printEndPage;

    /** 列印檔案位置 實體路徑. */
    private String printUrl;

    /** 轉換成String的PDF檔encode. */
    private String encodeStr = null;

    /** 跳頁頁碼. */
    private String gotopages;

    /** The gopages. */
    private String gopages;

    /** PDF總頁數. */
    private int totalPages;

    /** 前端頁面預覽jpg. */
    private StreamedContent dbImage = null;

    /** 前端頁面預覽連線. */
    private InputStream dbStream = null;

    /** 顯示下一頁按鍵. */
    private boolean showNextBt;

    /** 顯示上一頁按鍵. */
    private boolean showPreBt;

    /**
     * Constructor.
     */
    public PDFTransformController() {
        super();
    }

    /**
     * 開啟popup window檢視PDF檔第一頁.
     */
    public void initShowImage() {// dispatch
        logger.debug("########## 檢視PDF initShowImage 開始 #############");
        // logger.debug("####getExternalContext getRequestPathInfo#"
        // +
        // FacesContext.getCurrentInstance().getExternalContext().getRequestPathInfo());

        dbImage = null;
        dbStream = null;
        encodeStr = null;
        if (reviewPage) {
            dbImage = this._ganStreamContentImg(currentPage, dbImage);
        } else {
            try {
                this._openPDFtoEncode(pdfUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logger.debug("########## 檢視PDF initShowImage 結束 #############");
    }

    /**
     * 查下一頁.
     */
    public void queryForNextImage() {
        logger.debug("########## 查下一頁 queryForNextImage 開始 #############");
        dbImage = this._ganStreamContentImg(currentPage, dbImage);
        logger.debug("########## 查下一頁 queryForNextImage 結束 #############");
    }

    /**
     * 查上一頁.
     */
    public void queryForPreImage() {
        logger.debug("########## 查上一頁 queryForPreImage 開始 #############");
        dbImage = this._ganStreamContentImg(currentPage, dbImage);
        logger.debug("########## 查上一頁 queryForPreImage 結束 #############");
    }

    /**
     * 關閉預覽頁面.
     */
    public void closePdfViwer() {
        logger.debug("########## 關閉預覽頁面 closePdfViwer 開始 #############");
        // logger.debug("####getExternalContext getRequestPathInfo#"
        // +
        // FacesContext.getCurrentInstance().getExternalContext().getRequestPathInfo());

        try {
            pdfUrl = null;
            userId = null;
            pdfFileName = null;
            currentPage = "1";
            dbStream = null;
            dbImage = null;
            printStartPage = "";
            printEndPage = "";
            gotopages = "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug("########## 關閉預覽頁面closePdfViwer 結束#############");
        // return "MC3200.xhtml?faces-redirect=true";
    }

    /**
     * Clean direct.
     * 
     * @return the string
     */
    public String cleanDirect() {
        logger.debug("########## 重新導頁 #############");
        String url = FacesContext.getCurrentInstance().getExternalContext().getRequestPathInfo();
        url = url.substring(url.lastIndexOf("/"), url.length() + 1);
        logger.debug("### url ##" + url);
        return url + "?faces-redirect=true";
    }

    /**
     * 轉換pdf img.
     * 
     * @param currentPage
     *            當下要轉換成圖檔的頁面
     * @param dbImage
     *            前端頁面要顯示的圖檔
     * @return the streamed content
     */
    private StreamedContent _ganStreamContentImg(String currentPage, StreamedContent dbImage) {
        int page = Integer.parseInt(currentPage);
        Calendar cal = Calendar.getInstance();
        String cStr = String.valueOf(cal.getTimeInMillis());
        String keyWord = this.userId + cStr;
        if (page > 0) {

            // 轉換PDF檔為jpg檔
            String imgFileName = this._transPDF2Img(pdfUrl, keyWord, page);
            logger.debug("#### imgFileName ####" + imgFileName);
            if (null != imgFileName) {
                try {
                    dbStream = new FileInputStream(new File(imgFileName));
                    // view get the StreamedContent
                    dbImage = new DefaultStreamedContent(dbStream, "image/jpeg");

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        return dbImage;
    }

    /**
     * 轉換PDF檔為jpg.
     * 
     * @param pdfUrl
     *            the pdf url
     * @param keyword
     *            keyword
     * @param currentPage
     *            the current page
     * @return the string
     */
    private String _transPDF2Img(String pdfUrl, String keyword, int currentPage) {

        File file = new File(pdfUrl);
        RandomAccessFile raf;
        String imgFileName;
        try {
            raf = new RandomAccessFile(file, "r");
            FileChannel channel = raf.getChannel();
            ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            PDFFile pdffile = new PDFFile(buf);

            // PDF 總頁數
            int pages = pdffile.getNumPages();
            this.totalPages = pages;
            // 以總頁數和現在所在頁碼比較判斷是否顯示下一頁按鍵
            if (pages > currentPage) {
                this.showNextBt = true;
            } else {
                this.showNextBt = false;
            }

            // 上一頁按鍵
            if (currentPage > 1) {
                this.showPreBt = true;
            } else {
                this.showPreBt = false;
            }
            ;

            imgFileName = pdfUrl.substring(0, pdfUrl.indexOf(".pdf")).concat("-").concat(keyword).concat("-")
                    .concat(String.format("%03d", currentPage)).concat(".png");

            if (pages >= currentPage) {
                // get one page
                PDFPage page = pdffile.getPage(currentPage);
                // get the width and height for the doc at the default zoom

                int width = (int) (page.getBBox().getWidth());
                int height = (int) (page.getBBox().getHeight());

                Rectangle rect = new Rectangle(0, 0, width, height);
                int rotation = page.getRotation();
                Rectangle rect1 = rect;
                if (rotation == 90 || rotation == 270) {
                    rect1 = new Rectangle(0, 0, rect.height, rect.width);
                }

                // generate the image
                BufferedImage img = (BufferedImage) page.getImage(rect.width, rect.height, // width
                        // &
                        // height

                        rect1, // clip rect
                        null, // null for the ImageObserver
                        true, // fill background with white
                        true // block until drawing is done
                        );

                ImageIO.write(img, "png", new File(imgFileName));
                // 切割PDF檔-單頁
                this._pdfClip(pdfUrl, userId, currentPage, currentPage);
                // } else {
                // this._pdfClip(pdfUrl, userId, currentPage);
                // }

                return imgFileName;
            } else {
                // 頁數不對
                logger.debug("######## 頁數不對 ######");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 切割PDF檔.
     * 
     * @param pdfUrl
     *            檔案完整路徑
     * @param keyword
     *            辨識字
     * @param startPage
     *            開始頁數
     * @param endPage
     *            結束頁數
     */
    private void _pdfClip(String pdfUrl, String keyword, int startPage, int endPage) {
        logger.debug("####### 切割PDF檔 開始 ########");
        try {
            PdfReader reader = new PdfReader(pdfUrl);
            // 總頁數
            int count = reader.getNumberOfPages();
            // 起迄頁數不得小於0 及大於總頁數
            if (startPage > 0 && startPage <= count && endPage > 0 && endPage <= count) {
                // 取得檔名
                String pdfOutFile = pdfUrl.substring(0, pdfUrl.indexOf(".pdf")) + "-"
                        + String.format("%03d", startPage) + "-" + String.format("%03d", endPage) + ".pdf";
                this.printUrl = pdfOutFile;
                // 取得頁數
                int getPages = endPage - startPage + 1;
                Document document = new Document(reader.getPageSizeWithRotation(getPages));
                PdfCopy copy = new PdfCopy(document, new FileOutputStream(pdfOutFile));

                document.open();

                for (int j = startPage; j <= endPage; j++) {
                    document.newPage();
                    PdfImportedPage page = copy.getImportedPage(reader, j);
                    copy.addPage(page);
                }

                document.close();
                copy.close();
            } else {
                // 頁數錯誤
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug("####### 切割PDF檔 結束 ########");
    }

    /**
     * 由預覽列印畫面點選列印PDF檔.
     */
    public void printPdf() {
        logger.debug("######列印    開始######");
        try {
            logger.debug("###### 取得PDF檔 #####" + this.printUrl);
            logger.debug("###### 列印起始頁數 #####" + this.printStartPage);
            logger.debug("###### 列印結束頁數 #####" + this.printEndPage);
            // 有輸入起迄頁數, 重新切割檔案
            if (StringUtils.isNotBlank(this.printStartPage) && StringUtils.isNotBlank(this.printEndPage)) {
                _pdfClip(pdfUrl, userId, Integer.parseInt(printStartPage), Integer.parseInt(printEndPage));
            }
            _openPDFtoEncode(this.printUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.debug("######列印   結束######");
    }

    /**
     * 讀取PDF檔並encode成String.
     * 
     * @param url
     *            the url
     * @return String
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @author Erice
     */
    private void _openPDFtoEncode(String url) throws IOException {
        logger.debug("##### 讀取PDF檔並encode成String 開始 ######");
        File file = new File(url);
        // File file = new File("D:\pdfTester\RPT201010250002.pdf");
        DataInputStream istr = null;
        String bfStr = "";
        try {
            istr = new DataInputStream(new FileInputStream(file));
            long len = file.length();
            if (len > Integer.MAX_VALUE) {
                throw new IOException("File too long to decode: " + file.getName());
            }
            int contentLength = (int) len;
            byte[] bytes = new byte[contentLength];
            istr.read(bytes);

            bfStr = Base64.encodeBase64String(bytes);

        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (istr != null) {
                try {
                    istr.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        encodeStr = bfStr;
        logger.debug("##### 讀取PDF檔並encode成String 結束 ######");
    }

    /**
     * 跳頁.
     */
    public void gotoSearchPage() {
        logger.debug("###### 跳頁    開始######");
        logger.debug("###### gotopages ####" + this.gotopages);
        dbImage = this._ganStreamContentImg(this.gotopages, dbImage);
        logger.debug("###### 跳頁    結束######");
    }

    /**
     * Gets the pdf file name.
     * 
     * @return the pdf file name
     */
    public String getPdfFileName() {
        return pdfFileName;
    }

    /**
     * Sets the pdf file name.
     * 
     * @param pdfFileName
     *            the new pdf file name
     */
    public void setPdfFileName(String pdfFileName) {
        this.pdfFileName = pdfFileName;
    }

    /**
     * Gets the user id.
     * 
     * @return the user id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the user id.
     * 
     * @param userId
     *            the new user id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Gets the current page.
     * 
     * @return the current page
     */
    public String getCurrentPage() {
        return currentPage;
    }

    /**
     * Sets the current page.
     * 
     * @param currentPage
     *            the new current page
     */
    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * Gets the db image.
     * 
     * @return the db image
     */
    public StreamedContent getDbImage() {
        return this.dbImage;
    }

    /**
     * Sets the db image.
     * 
     * @param dbImage
     *            the new db image
     */
    public void setDbImage(StreamedContent dbImage) {
        this.dbImage = dbImage;
    }

    /**
     * Checks if is show next bt.
     * 
     * @return true, if is show next bt
     */
    public boolean isShowNextBt() {
        return showNextBt;
    }

    /**
     * Sets the show next bt.
     * 
     * @param showNextBt
     *            the new show next bt
     */
    public void setShowNextBt(boolean showNextBt) {
        this.showNextBt = showNextBt;
    }

    /**
     * Checks if is show pre bt.
     * 
     * @return true, if is show pre bt
     */
    public boolean isShowPreBt() {
        return showPreBt;
    }

    /**
     * Sets the show pre bt.
     * 
     * @param showPreBt
     *            the new show pre bt
     */
    public void setShowPreBt(boolean showPreBt) {
        this.showPreBt = showPreBt;
    }

    /**
     * Gets the total pages.
     * 
     * @return the total pages
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * Sets the total pages.
     * 
     * @param totalPages
     *            the new total pages
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * Gets the pdf url.
     * 
     * @return the pdf url
     */
    public String getPdfUrl() {
        return pdfUrl;
    }

    /**
     * Sets the pdf url.
     * 
     * @param pdfUrl
     *            the new pdf url
     */
    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    /**
     * Gets the gopages.
     * 
     * @return the gopages
     */
    public String getGopages() {
        return gopages;
    }

    /**
     * Sets the gopages.
     * 
     * @param gopages
     *            the new gopages
     */
    public void setGopages(String gopages) {
        this.gopages = gopages;
    }

    /**
     * Gets the gotopages.
     * 
     * @return the gotopages
     */
    public String getGotopages() {
        return gotopages;
    }

    /**
     * Sets the gotopages.
     * 
     * @param gotopages
     *            the new gotopages
     */
    public void setGotopages(String gotopages) {
        this.gotopages = gotopages;
    }

    /**
     * Gets the prints the url.
     * 
     * @return the prints the url
     */
    public String getPrintUrl() {
        return printUrl;
    }

    /**
     * Sets the prints the url.
     * 
     * @param printUrl
     *            the new prints the url
     */
    public void setPrintUrl(String printUrl) {
        this.printUrl = printUrl;
    }

    /**
     * Gets the encode str.
     * 
     * @return the encode str
     */
    public String getEncodeStr() {
        return encodeStr;
    }

    /**
     * Sets the encode str.
     * 
     * @param encodeStr
     *            the new encode str
     */
    public void setEncodeStr(String encodeStr) {
        this.encodeStr = encodeStr;
    }

    /**
     * Checks if is review page.
     * 
     * @return true, if is review page
     */
    public boolean isReviewPage() {
        return reviewPage;
    }

    /**
     * Sets the review page.
     * 
     * @param reviewPage
     *            the new review page
     */
    public void setReviewPage(boolean reviewPage) {
        this.reviewPage = reviewPage;
    }

    /**
     * Checks if is two side print.
     * 
     * @return true, if is two side print
     */
    public boolean isTwoSidePrint() {
        return twoSidePrint;
    }

    /**
     * Sets the two side print.
     * 
     * @param twoSidePrint
     *            the new two side print
     */
    public void setTwoSidePrint(boolean twoSidePrint) {
        this.twoSidePrint = twoSidePrint;
    }

    /**
     * Gets the prints the start page.
     * 
     * @return the prints the start page
     */
    public String getPrintStartPage() {
        return printStartPage;
    }

    /**
     * Sets the prints the start page.
     * 
     * @param printStartPage
     *            the new prints the start page
     */
    public void setPrintStartPage(String printStartPage) {
        this.printStartPage = printStartPage;
    }

    /**
     * Gets the prints the end page.
     * 
     * @return the prints the end page
     */
    public String getPrintEndPage() {
        return printEndPage;
    }

    /**
     * Sets the prints the end page.
     * 
     * @param printEndPage
     *            the new prints the end page
     */
    public void setPrintEndPage(String printEndPage) {
        this.printEndPage = printEndPage;
    }

}