package gtu.poi.hwpf.doc;

import gtu.file.FileUtil;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

public class DocTableTest__waitForDel {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        DocTableTest__waitForDel test = new  DocTableTest__waitForDel();
        test.loadWord();
        
        File file = new File(FileUtil.DESKTOP_DIR, "笨娟要的東西.sql");
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf8"));
        final String format = "COMMENT ON TABLE %s IS '%s';";
        for(TableZ t : test.tableList){
//            System.out.println(t);
            if(t.name == null){
                continue;
            }
            writer.write(t.name);
            writer.newLine();
            for(String key : t.fieldMap.keySet()){
                writer.write(String.format(format, key, t.fieldMap.get(key)));
                writer.newLine();
            }
            writer.newLine();
            writer.newLine();
            writer.newLine();
        }
        writer.flush();
        writer.close();
        System.out.println("done...");
    }

    //    Range r = doc.getRange();
    //    Paragraph p = r.getParagraph(0);
    //    r.getParagraph(i).usesUnicode()
    //    Table t = r.getTable(p);
    
    private static class TableZ {
        String name;
        Map<String,String> fieldMap = new LinkedHashMap<String,String>();
        @Override
        public String toString() {
            return "TableZ [name=" + name + ", fieldMap=" + fieldMap + "]";
        }
    }
    
    List<TableZ> tableList = new ArrayList<TableZ>();

    void loadWord() throws FileNotFoundException, IOException {
        File file = new File("C:/Users/gtu001/Desktop/DB轉檔/CAC-DBS.doc");
        POIFSFileSystem poifsysm = new POIFSFileSystem(new FileInputStream(file));
        HWPFDocument doc = new HWPFDocument(poifsysm);
        
        Pattern tableNamePtn = Pattern.compile("[\\w]+");

        Range range = doc.getRange();
        int count = -1;
        Table table = null;
        TableRow row = null;
        for (int ii = 0;; ii++) {
            try {
                table = range.getTable(range.getParagraph(ii));
                System.out.println("table : " + table);
                
                TableZ tab = new TableZ();
                Z : for (int jj = 0; jj < table.numRows(); jj++) {
                    row = table.getRow(jj);
                    System.out.format("row:%d\t", jj);
                    for (int kk = 0; kk < row.numCells(); kk++) {
                        System.out.format("%d:%s\t", kk, getCellText(row.getCell(kk)));
                        
                        //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx 
                        if(jj == 0){
                            String val = getCellText(row.getCell(0));
                            Matcher mth = tableNamePtn.matcher(val);
                            if(!mth.find()){
                                continue Z;
                            }
                            String tableName = mth.group();
                            tab.name = tableName;
                        }else if(jj == 1){
                            // do nothing
                        }else {
                            String key = getCellText(row.getCell(0));
                            String val = getCellText(row.getCell(1));
                            tab.fieldMap.put(key, val);
                        }
                        //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx 
                    }
                    System.out.println();
                }
                
                tableList.add(tab);
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

    void writeDoc(HWPFDocument doc, File targetFile) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(doc.getDataStream());
        POIFSFileSystem fs = new POIFSFileSystem();
        DirectoryEntry directory = fs.getRoot();
        directory.createDocument("WordDocument", bais);
        FileOutputStream ostream = new FileOutputStream(targetFile);
        fs.writeFilesystem(ostream);
        bais.close();
        ostream.close();
    }

    String debug_cellText___(TableCell cell) {
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

    String getCellText(TableCell cell) {
        CharacterRun crun = null;
        StringBuilder sb = new StringBuilder();
        for (int ii = 0; ii < cell.numCharacterRuns(); ii++) {
            crun = cell.getCharacterRun(ii);
            sb.append(getMatch(crun.text()));
        }
        return sb.toString();
    }

    static Pattern ONLY_WORD = Pattern.compile("[\u4e00-\u9fa5|\uFE30-\uFFA0|\\w]+");

    String getMatch(String text) {
        Matcher matcher = ONLY_WORD.matcher(text);
        StringBuilder sb = new StringBuilder();
        for (; matcher.find();) {
            sb.append(matcher.group());
        }
        return sb.toString();
    }
}
