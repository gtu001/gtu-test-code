package gtu.pdf.pdfbox;

import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.text.PDFTextStripper;

import ch.qos.logback.classic.Level;
import gtu.binary.StringUtil4FullChar;
import gtu.log.LogbackUtil;
import gtu.string.StringUtil_;

public class Pdfbox_forJannaEx1 {
    static {
        LogbackUtil.setRootLevel(Level.OFF);
    }

    public static void main(String[] args) throws InvalidPasswordException, IOException {
        File file = new File("E:/workstuff/workspace/gtu-test-code/PythonGtu/gtu/pdf/ex1/RCRP0S102.pdf");

        Pdfbox_forJannaEx1 p = new Pdfbox_forJannaEx1();
        String jannaText = p.loadPdfToTxt(file);
        List<PdfOnePage> lst2 = p.parseJannaTextToBean(jannaText);

        for (PdfOnePage v : lst2) {
            System.out.println(v.lst);
        }
    }

    private class PdfOnePage {
        List<String> lst = new ArrayList<String>();
    }

    private boolean isEndOfTable(String line) {
        Pattern endPtn = Pattern.compile("[a-zA-Z]+");
        Matcher mth = endPtn.matcher(line);
        if (mth.find()) {
            return true;
        }
        return StringUtil_.hasChineseWord(line);
    }

    private List<PdfOnePage> parseJannaTextToBean(String jannaText) throws IOException {
        List<PdfOnePage> lst = new ArrayList<PdfOnePage>();
        LineNumberReader reader = new LineNumberReader(new StringReader(jannaText));

        PdfOnePage tmpPage = new PdfOnePage();

        int startPos = -1;
        for (String line = null; (line = reader.readLine()) != null;) {
            line = StringUtils.trimToEmpty(line);
            if (startPos == -1 && line.endsWith("加率‰")) {
                startPos = reader.getLineNumber() + 1;
                System.out.println("find start :" + startPos + " \t" + line);
                continue;
            }
            if (startPos != -1) {
                if (isEndOfTable(line)) {
                    startPos = -1;
                    lst.add(tmpPage);
                    tmpPage = new PdfOnePage();
                    System.out.println("find end :" + startPos + " \t" + line);
                } else {
                    tmpPage.lst.add(line);
                    System.out.println("\t\t append : " + line);
                }
            }
        }

        reader.close();
        return lst;
    }

    private String loadPdfToTxt(File file) throws IOException {
        PDDocument doc = PDDocument.load(file);
        int keyLength = 256;
        AccessPermission ap = new AccessPermission();
        ap.setCanPrint(false);
        StandardProtectionPolicy spp = new StandardProtectionPolicy("12345", "", ap);
        spp.setEncryptionKeyLength(keyLength);
        spp.setPermissions(ap);
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(doc);
        // FileUtil.saveToFile(new File(FileUtil.DESKTOP_PATH, "tttt.txt"),
        // text, "UTF-8");
        doc.close();
        return text;
    }
}