package gtu.itext.html;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;

import org.dom4j.DocumentException;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import gtu.file.FileUtil;

public class Html2Pdf {

    public static StringBuilder readHtml(String inputFile) throws IOException {
        StringBuilder sb = new StringBuilder();
        FileReader fr = new FileReader(inputFile);
        BufferedReader br = new BufferedReader(fr);

        while (br.ready()) {
            sb.append(br.readLine());
            // System.out.println(br.readLine());
        }
        fr.close();
        return sb;
    }

    public static StringBuilder readHtml2(String inputFile) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "Big5")); // 指定讀取文件的編碼格式，以免出現中文亂碼
        String str = null;
        while ((str = reader.readLine()) != null) {
            sb.append(str);
            // System.out.println(str);
        }
        return sb;
    }

    public boolean convertHtmlToPdf(String inputFile, String outputFile) throws Exception {
        OutputStream os = new FileOutputStream(outputFile);
        ITextRenderer renderer = new ITextRenderer();
        String url = new File(inputFile).toURI().toURL().toString();
        renderer.setDocument(url);
        // 解決中文支援問題
        ITextFontResolver fontResolver = renderer.getFontResolver();
        
//        fontResolver.addFont("C:\\Windows\\Fonts\\mingliu.ttc", //
//                BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        
        // 解決圖片的相對路徑問題
        // renderer.getSharedContext().setBaseURL("file:/D:/test");
        renderer.layout();
        renderer.createPDF(os);
        os.flush();
        os.close();
        return true;
    }

    private void test__simpleOutputPDF() throws DocumentException, IOException {
        String inputFile = FileUtil.DESKTOP_PATH + "test.html";
        String outputFile = FileUtil.DESKTOP_PATH + "test.pdf";

        Document document = new Document(PageSize.LETTER);
        PdfWriter.getInstance(document, new FileOutputStream(outputFile));
        document.open();

        BaseFont baseFont = BaseFont.createFont("MHei-Medium", "UniCNS-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font font = new Font(baseFont);
        document.add(new Paragraph("解決中文問題了！", font));

        HTMLWorker htmlWorker = new HTMLWorker(document);

        htmlWorker.parse(new StringReader(Html2Pdf.readHtml2(inputFile).toString()));
        // htmlWorker.parse(new StringReader("<h1>pdf test</h1>"));
        document.close();
    }

    /**
     * @param args
     * @throws DocumentException
     * @throws IOException
     */
    public static void main(String[] args) throws DocumentException, IOException {

        // ITextRenderer renderer = new ITextRenderer();

        String inputFile = FileUtil.DESKTOP_PATH + "test.html";
        String outputFile = FileUtil.DESKTOP_PATH + "test.pdf";

        // String url = new File(inputFile).toURI().toURL().toString();

        Html2Pdf html2Pdf = new Html2Pdf();
        try {
            html2Pdf.convertHtmlToPdf(inputFile, outputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}