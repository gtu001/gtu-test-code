package gtu.poi.hwpf.doc;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class DocTableUtil {

    public static void main(String[] args) throws FileNotFoundException, IOException {
    }

    //    Range r = doc.getRange();
    //    Paragraph p = r.getParagraph(0);
    //    r.getParagraph(i).usesUnicode()
    //    Table t = r.getTable(p);

    public void loadWord(File file) throws FileNotFoundException, IOException {
        POIFSFileSystem poifsysm = new POIFSFileSystem(new FileInputStream(file));
        HWPFDocument doc = new HWPFDocument(poifsysm);
        
        Range range = doc.getRange();
        
        int count = -1;
        Table table = null;
        TableRow row = null;
        for (int ii = 0;; ii++) {
            try {
                Paragraph paragraph = range.getParagraph(ii);
                
                table = range.getTable(paragraph);
                System.out.println("table : " + table);
                for (int jj = 0; jj < table.numRows(); jj++) {
                    row = table.getRow(jj);
                    System.out.format("row:%d\t", jj);
                    for (int kk = 0; kk < row.numCells(); kk++) {
                        System.out.format("%d:%s\t", kk, getCellText(row.getCell(kk)));
                    }
                    System.out.println();
                }
            } catch (Exception ex) {
                if (ex.getMessage().equals("This paragraph doesn't belong to a table")) {
                    continue;
                }
                if (ex.getMessage().equals("This paragraph is not the first one in the table")) {
                    continue;
                }
                count = ii - 1;
                ex.printStackTrace();
                break;
            }
        }
        System.out.println("count = " + count);
    }

    public void writeDoc(HWPFDocument doc, File targetFile) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(doc.getDataStream());
        POIFSFileSystem fs = new POIFSFileSystem();
        DirectoryEntry directory = fs.getRoot();
        directory.createDocument("WordDocument", bais);
        FileOutputStream ostream = new FileOutputStream(targetFile);
        fs.writeFilesystem(ostream);
        bais.close();
        ostream.close();
    }

    public String debug_cellText___(TableCell cell) {
        CharacterRun crun = null;
        Paragraph para = null;
        Section sec = null;
        StringBuilder sb = new StringBuilder();
        sb.append("<<<");
        for (int ii = 0; ii < cell.numCharacterRuns(); ii++) {
            crun = cell.getCharacterRun(ii);
            sb.append(String.format("crun%d=[%s],", ii, crun.text()));
        }
        for (int ii = 0; ii < cell.numParagraphs(); ii++) {
            para = cell.getParagraph(ii);
            sb.append(String.format("para%d=[%s],", ii, para.text()));
        }
        for (int ii = 0; ii < cell.numSections(); ii++) {
            sec = cell.getSection(ii);
            sb.append(String.format("sec%d=[%s],", ii, sec.text()));
        }
        sb.append(">>>");
        return sb.toString();
    }

    public String getCellText(TableCell cell) {
        CharacterRun crun = null;
        StringBuilder sb = new StringBuilder();
        for (int ii = 0; ii < cell.numCharacterRuns(); ii++) {
            crun = cell.getCharacterRun(ii);
            sb.append(getMatch(crun.text()));
        }
        return sb.toString();
    }

    static Pattern ONLY_WORD = Pattern.compile("[\u4e00-\u9fa5|\uFE30-\uFFA0|\\w]+");

    public String getMatch(String text) {
        Matcher matcher = ONLY_WORD.matcher(text);
        StringBuilder sb = new StringBuilder();
        for (; matcher.find();) {
            sb.append(matcher.group());
        }
        return sb.toString();
    }
}
