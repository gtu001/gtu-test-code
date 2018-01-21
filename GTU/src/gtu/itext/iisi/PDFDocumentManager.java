package gtu.itext.iisi;

import gtu.itext.iisi.CHTFontFactory.RISFont;
import gtu.itext.iisi.marker.MarkInfo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Stack;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import tw.gov.moi.common.SystemConfig;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;

/**
 * @author Tsai, Chi-Feng
 * @version 1.0
 */
public class PDFDocumentManager {

    /** Logger Object. */
    @SuppressWarnings("unused")
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PDFDocumentManager.class);

    /** 存放可使用的 PDFDocBuilder */
    private Stack<PDFDocument> PDFPool = new Stack<PDFDocument>();

    private int usedCount = 0;

    synchronized public int freeBuilderCount() {
        return this.PDFPool.size();
    }

    synchronized public int usedBuilderCount() {
        return this.usedCount;
    }

    public PDFDocument getPDFDocument(File outfile,Rectangle pageSize) throws DocumentException, IOException {
        return getPDFDocument(outfile,pageSize, new LayoutInfo(), Collections.<MarkInfo> emptyList(), null);
    }

    public PDFDocument getPDFDocument(File outfile,Rectangle pageSize, LayoutInfo marginInfo) throws DocumentException, IOException {
        return getPDFDocument(outfile,pageSize, marginInfo, Collections.<MarkInfo> emptyList(), null);
    }

    public PDFDocument getPDFDocument(File outfile,Rectangle pageSize, LayoutInfo marginInfo, List<MarkInfo> markInfos) throws DocumentException, IOException {
        return getPDFDocument(outfile,pageSize, marginInfo, markInfos, null);
    }

    public PDFDocument getPDFDocument(File outfile,Rectangle pageSize, LayoutInfo marginInfo, List<MarkInfo> markInfos, Properties properties)
            throws DocumentException, IOException {

        PDFDocument pdfDoc = null;
        try {
            pdfDoc = this.PDFPool.pop();
        } catch (Exception e) {
        }
        if (pdfDoc == null) {
            pdfDoc = new PDFDocument(this.systemConfig);
        }
        pdfDoc.init(pageSize, marginInfo, markInfos, properties, new BufferedOutputStream(new FileOutputStream(outfile)));
        pdfDoc.setFontFactory(RISFont.SUNG.getFactory(this.systemConfig));
        this.usedCount++;
        return pdfDoc;
    }

    synchronized protected void close(PDFDocument db) {
        // 將資源歸還於 pool 之中.
        // usedDB.remove(db);
        this.usedCount--;
        //this.PDFPool.push(db);
    }

    private static PDFDocumentManager INSTANCE = null;

    private boolean initialized = false;

    @Autowired
    private SystemConfig systemConfig;

    public PDFDocumentManager() {
    }

    public static PDFDocumentManager getInstance(final SystemConfig systemConfig) {
        if (PDFDocumentManager.INSTANCE == null) {
            PDFDocumentManager.INSTANCE = new PDFDocumentManager();
            PDFDocumentManager.INSTANCE.systemConfig = systemConfig;
        }
        if (!PDFDocumentManager.INSTANCE.initialized) {
            PDFDocumentManager.INSTANCE.initialize();
        }
        return PDFDocumentManager.INSTANCE;
    }

    @PostConstruct
    void initialize() {
        synchronized (this) {
            if (this.initialized) {
                return;
            }
            this.initialized = true;
            for (int i = 0; i < 5; i++) {
                this.PDFPool.push(new PDFDocument(this.systemConfig));
            }
            INSTANCE = this;
        }
    }
}
