package gtu.pdf.pdfbox;

import java.io.File;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.text.PDFTextStripper;

public class PdfboxUtil {

    public static void main(String[] args) {
        // File fromFile = new
        // File("c:/Users/gtu00/OneDrive/Desktop/秀娟0501/PDF轉EXCEL/RCRP0S104.pdf");
        // File toFile = new File(FileUtil.DESKTOP_DIR, fromFile.getName() +
        // ".txt");
        // String content = PdfboxUtil.loadText(fromFile);
        // FileUtil.saveToFile(toFile, content, "utf8");

        // File fromDir = new
        // File("C:/Users/gtu00/OneDrive/Desktop/秀娟0501/new_pdf");
        // for (File f : fromDir.listFiles()) {
        // File toFile = new File(FileUtil.DESKTOP_DIR, f.getName() + ".txt");
        // String content = PdfboxUtil.loadText(f);
        // FileUtil.saveToFile(toFile, content, "utf8");
        // }

        File pdfFile = new File("D:/gtu001_dropbox/Dropbox/guava/電子書/(Unwind Trilogy 1) Neal Shusterman - Unwind  -Simon & Schuster Books For Young Readers (2007).pdf");
        String text = PdfboxUtil.loadText(pdfFile, Pair.of(1, 5));
        System.out.println(text);

        System.out.println("done...");
    }

    public static String loadText(File file) {
        return loadText(file, null);
    }

    public static String loadText(File file, Pair<Integer, Integer> range) {
        try {
            PDDocument doc = PDDocument.load(file);
            int keyLength = 256;
            AccessPermission ap = new AccessPermission();
            ap.setCanPrint(false);
            StandardProtectionPolicy spp = new StandardProtectionPolicy("12345", "", ap);
            spp.setEncryptionKeyLength(keyLength);
            spp.setPermissions(ap);
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setParagraphStart("^---para start---^");
            stripper.setParagraphEnd("^---para end---^");
            stripper.setArticleStart("^---article start---^");
            stripper.setArticleEnd("^---article end---^");
            stripper.setIndentThreshold(4);
            if (range != null) {
                stripper.setStartPage(range.getLeft());
                stripper.setEndPage(range.getRight());
            }
            String text = stripper.getText(doc);
            doc.close();
            return text;
        } catch (Exception ex) {
            throw new RuntimeException("loadPdfToTxt ERR : " + ex.getMessage(), ex);
        }
    }
}
