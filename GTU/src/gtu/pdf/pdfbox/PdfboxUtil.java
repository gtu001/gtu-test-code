package gtu.pdf.pdfbox;

import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.text.PDFTextStripper;

import gtu.file.FileUtil;

public class PdfboxUtil {

    public static void main(String[] args) {
        File fromFile = new File("c:/Users/gtu00/OneDrive/Desktop/秀娟0501/PDF轉EXCEL/RCRP0S104.pdf");
        File toFile = new File(FileUtil.DESKTOP_DIR, fromFile.getName() + ".txt");
        String content = PdfboxUtil.loadText(fromFile);
        FileUtil.saveToFile(toFile, content, "utf8");
        System.out.println("done...");
    }

    public static String loadText(File file) {
        try {
            PDDocument doc = PDDocument.load(file);
            int keyLength = 256;
            AccessPermission ap = new AccessPermission();
            ap.setCanPrint(false);
            StandardProtectionPolicy spp = new StandardProtectionPolicy("12345", "", ap);
            spp.setEncryptionKeyLength(keyLength);
            spp.setPermissions(ap);
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(doc);
            doc.close();
            return text;
        } catch (Exception ex) {
            throw new RuntimeException("loadPdfToTxt ERR : " + ex.getMessage(), ex);
        }
    }
}
