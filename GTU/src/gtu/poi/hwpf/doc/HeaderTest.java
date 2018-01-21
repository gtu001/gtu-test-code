package gtu.poi.hwpf.doc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.HeaderStories;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class HeaderTest {

    public static void main(String[] args) throws Exception {
        File dir = new File("D:/9-2-PDS-有問題");
        HeaderTest ttt = new HeaderTest();
        for (File file : dir.listFiles()) {
            ttt.convertTOUnit2(file);
        }
        System.out.println("done...");
    }

    private void convertTOUnit2(final File file) {
        InputStream fis = null;

        try {
            fis = new FileInputStream(file);
            POIFSFileSystem fs = new POIFSFileSystem(fis);
            HWPFDocument doc = new HWPFDocument(fs);
            Range range = doc.getRange();

            int parNum = range.numParagraphs();
            Paragraph par;
            Table table;

            for (int i = 0; i < parNum; i++) {
                try {
                    par = range.getParagraph(i);
                    table = range.getTable(par);
                    for (int j = 0; j < table.numRows(); j++) {
                        for (int k = 0; k < table.getRow(j).numCells(); k++) {
                            String cellText = table.getRow(j).getCell(k).text();
                            //                            System.out.println(cellText);
                        }
                    }
                } catch (IllegalArgumentException ex) {
                    continue;
                }
            }

            HeaderStories hs = new HeaderStories(doc);

            System.out.println("numCharacterRuns ==" + hs.getRange().numCharacterRuns());
            System.out.println("numParagraphs ==" + hs.getRange().numParagraphs());
            System.out.println("numSections ==" + hs.getRange().numSections());
            for (int i = 0; i < hs.getRange().numParagraphs(); i++) {
                String data = hs.getRange().getParagraph(i).text();
                System.out.println(data);
            }
            OutputStream out = new FileOutputStream(file);
            //            doc.write(out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
