package gtu.pdf.pdfbox;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.text.PDFTextStripper;

public class PdfboxUtil {

    public static void main(String[] args) {
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
