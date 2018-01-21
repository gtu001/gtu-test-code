package gtu.pdf.pdfbox;

import gtu.file.FileUtil;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.text.PDFTextStripper;

public class PdfboxTest {

    public static void main(String[] args) throws InvalidPasswordException, IOException {
        PDDocument doc = PDDocument.load(new File("D:/Users/700910/Desktop/6.網路投保系統(ezbao)_ezbao.pdf"));

        // Define the length of the encryption key.
        // Possible values are 40, 128 or 256.
        int keyLength = 256;

        AccessPermission ap = new AccessPermission();

        // disable printing, everything else is allowed
        ap.setCanPrint(false);

        // Owner password (to open the file with all permissions) is "12345"
        // User password (to open the file but with restricted permissions, is
        // empty here)
        
        StandardProtectionPolicy spp = new StandardProtectionPolicy("12345", "", ap);
        spp.setEncryptionKeyLength(keyLength);
        spp.setPermissions(ap);
//        doc.protect(spp);//解除加密
//        doc.save("filename-encrypted.pdf");
        
        PDFTextStripper stripper = new PDFTextStripper();
        stripper.setStartPage(1406);
        stripper.setEndPage(1657);
        String text = stripper.getText(doc);
        System.out.println(text);
        
        FileUtil.saveToFile("D:/Users/700910/Desktop/test.txt", text.getBytes("utf8"));
        
        doc.close();
    }

}