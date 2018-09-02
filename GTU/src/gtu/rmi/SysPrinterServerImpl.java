package gtu.rmi;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.rmi.RemoteException;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaTray;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PageRanges;
import javax.print.attribute.standard.Sides;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tw.gov.moi.ae.print.SysPrinterException;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFRenderer;

/**
 * 系統列印實作
 * 
 * @author Sandy Chiu
 * 
 */
public class SysPrinterServerImpl extends java.rmi.server.UnicastRemoteObject {

    private static final long serialVersionUID = -3446181951873425673L;
    private static Logger logger = LoggerFactory.getLogger(SysPrinterServerImpl.class);

    public SysPrinterServerImpl() throws RemoteException {
    }

    public void printPdf(String printerName, String file, int copies, boolean isDuplex, String reportName, int startPage, int endPage) throws SysPrinterException, RemoteException {

        PrintService[] ps = PrintServiceLookup.lookupPrintServices(null, null);
        PrintService printService = null;
        File oldf = null;

        for (int i = 0; i < ps.length; i++) {
            if ((ps[i].getName().matches(".*" + printerName + ".*"))) {
                logger.debug("use printer:" + ps[i].getName());
                printService = ps[i];
            }
        }

        if (null != printService) {

            final PrinterJob pjob = PrinterJob.getPrinterJob();
            final PageFormat pformat = pjob.getPageFormat(null);

            try {

                oldf = new File(file);
                final byte[] content = getBytes(new FileInputStream(oldf.getAbsolutePath()));
                final ByteBuffer byteBuffer = ByteBuffer.wrap(content);

                PDFFile curFile = new PDFFile(byteBuffer);

                Book book = new Book();

                // 取得印表機預設paper物件
                Paper paper = pformat.getPaper();

                // implements Printable
                PDFPrintPage pages = new PDFPrintPage(curFile);

                int w = (int) curFile.getPage(0).getWidth();
                int h = (int) curFile.getPage(0).getHeight();
                logger.debug("pdf size w:" + (double) w / 72 * 2.54 + " h:" + (double) h / 72 * 2.54);

                // 在AIX機器上紙上的大小A4會取成Letter，自行指定紙張大小
                switch (w) {
                case 792: // a4橫印
                    w = 841;
                    h = 595;
                    break;

                case 612:// a4直印
                    w = 595;
                    h = 841;
                    break;

                default:
                    break;
                }

                logger.debug("page size w:" + (double) w / 72 * 2.54 + " h:" + (double) h / 72 * 2.54);

                // 可列印範圍 x左上角X坐標, y左上角y坐標, width:可列印寬, height:可列印高
                paper.setImageableArea(0, 0, w, h);

                paper.setSize(w, h);

                // LANDSCAPE=0 原點在右
                // PORTRAIT=1 原點在左
                // REVERSE_LANDSCAPE=2 MAC電腦設定
                // if (w > h) {
                // // 橫印
                // pformat.setOrientation(PageFormat.LANDSCAPE);
                // } else {
                // // 印
                // pformat.setOrientation(PageFormat.PORTRAIT);
                // }

                // logger.debug("Orientation(LANDSCAPE=0,PORTRAIT=1):{}",
                // pformat.getOrientation());

                pformat.setPaper(paper);

                logger.debug("## 取得印表機名稱為: " + printService.getName());
                logger.debug("## 印表機可印高度: " + pformat.getHeight() / 72 * 2.54 + " 公分");
                logger.debug("## 印表機可印寬度: " + pformat.getWidth() / 72 * 2.54 + " 公分");
                logger.debug("## 印表機可列印影像高: " + pformat.getImageableHeight() / 72 * 2.54 + " 公分");
                logger.debug("## 印表機可列印影像寬: " + pformat.getImageableWidth() / 72 * 2.54 + " 公分");
                logger.debug("## 印表機邊界(左): " + pformat.getImageableX() / 72 * 2.54 + " 公分");
                logger.debug("## 印表機邉界(右): " + pformat.getImageableY() / 72 * 2.54 + " 公分");
                logger.debug("## 紙張-高 : " + paper.getHeight() / 72 * 2.54 + " 公分");
                logger.debug("## 紙張-寬 : " + paper.getWidth() / 72 * 2.54 + " 公分");
                logger.debug("## 紙張-影像高 : " + paper.getImageableHeight() / 72 * 2.54 + " 公分");
                logger.debug("## 紙張-影像寬 : " + paper.getImageableWidth() / 72 * 2.54 + " 公分");
                logger.debug("## 紙張-邊界(左) :" + paper.getImageableX() / 72 * 2.54 + " 公分");
                logger.debug("## 紙張-邊界(右) :" + paper.getImageableY() / 72 * 2.54 + " 公分");

                pjob.setPrintService(printService);

                pjob.setJobName(reportName);

                book.append(pages, pformat, curFile.getNumPages());

                pjob.setPageable(book);

                PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
                if (isDuplex) {
                    aset.add(Sides.DUPLEX);
                } else {
                    aset.add(Sides.ONE_SIDED);
                }

                if (w > h) {
                    logger.debug("PageFormat:landscape");
                    aset.add(OrientationRequested.LANDSCAPE);
                } else {
                    logger.debug("PageFormat:protrait");
                    aset.add(OrientationRequested.PORTRAIT);
                }

                aset.add(MediaTray.MAIN);

                if (0 != startPage && 0 != endPage) {
                    PageRanges range = new PageRanges(startPage + "-" + endPage);
                    aset.add(range);
                }

                aset.add(new Copies(copies));

                pjob.print(aset);

            } catch (Exception e) {
                logger.error("Exception ", e);
            }

        } else {
            throw new SysPrinterException("AE-1201-E", "can't find printer:" + printerName);
        }

    }

    private byte[] getBytes(final InputStream inputStream) throws IOException, PrinterException {
        final byte[] buffer = new byte[2048];
        int readCount = -1;
        final ByteArrayOutputStream bis = new ByteArrayOutputStream();
        final BufferedInputStream biss = new BufferedInputStream(inputStream, buffer.length);
        while ((readCount = biss.read(buffer)) > -1) {
            bis.write(buffer, 0, readCount);
        }
        bis.flush();
        return bis.toByteArray();
    }

    class PDFPrintPage implements Printable {

        /** The file. */
        private PDFFile file;

        /**
         * Instantiates a new pDF print page.
         * 
         * @param file
         *            the file
         */
        PDFPrintPage(PDFFile file) {
            this.file = file;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.awt.print.Printable#print(java.awt.Graphics,
         * java.awt.print.PageFormat, int)
         */
        public int print(Graphics g, PageFormat format, int index) throws PrinterException {
            logger.debug("");
            logger.debug("***inner class PDFPrintPage start*****************************************");
            int pagenum = index + 1;

            if ((pagenum >= 1) && (pagenum <= file.getNumPages())) {
                // 坐標空間
                Graphics2D g2 = (Graphics2D) g;

                // pdf 文件
                PDFPage page = file.getPage(pagenum);

                logger.debug("***PDFPage*****************************************");
                logger.debug("##PDF 頁面: 文件高度###" + page.getHeight() / 72 * 2.54 + " 公分");
                logger.debug("##PDF 頁面: 文件寬度###" + page.getWidth() / 72 * 2.54 + " 公分");
                logger.debug("##PDF 頁面: 長寬比例###" + page.getAspectRatio() / 72 * 2.54 + " 公分");
                logger.debug("##PDF 頁面: 旋轉方向###" + page.getRotation() / 72 * 2.54 + " 公分");
                logger.debug("##PDF 頁面: 頁面邊框物件-高###" + page.getBBox().getHeight() / 72 * 2.54 + " 公分");
                logger.debug("##PDF 頁面: 頁面邊框物件-寬###" + page.getBBox().getWidth() / 72 * 2.54 + " 公分");
                logger.debug("********************************************");

                // 列印影像物件
                Rectangle imageArea = new Rectangle((int) page.getWidth(), (int) page.getHeight());

                g2.translate(0, 0);

                PDFRenderer pgs = new PDFRenderer(page, g2, imageArea, null, null);
                try {
                    page.waitForFinish();
                    pgs.run();
                } catch (InterruptedException ie) {
                    // nothing to do
                }
                return PAGE_EXISTS;
            } else {
                return NO_SUCH_PAGE;
            }
        }
    }
}
