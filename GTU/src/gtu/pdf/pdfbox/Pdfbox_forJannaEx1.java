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
import gtu.file.FileUtil;
import gtu.log.LogbackUtil;
import gtu.string.StringUtil_;

public class Pdfbox_forJannaEx1 {
    static {
        LogbackUtil.setRootLevel(Level.OFF);
    }

    public static void main(String[] args) throws InvalidPasswordException, IOException {
        Pdfbox_forJannaEx1 p = new Pdfbox_forJannaEx1();
        p.___saveTextToFile();
        System.out.println("done..");
    }
    
    private void ___saveTextToFile() {
        List<File> fileList = new ArrayList<File>();
        FileUtil.searchFileMatchs(new File("C:/Users/gtu00/OneDrive/Desktop/秀娟0501"), ".*\\.pdf", fileList);
        for(File f : fileList) {
            String jannaText = PdfboxUtil.loadText(f);
            FileUtil.saveToFile(new File(FileUtil.DESKTOP_DIR, f.getName() + ".txt"), jannaText, "utf8");
        }
    }
}