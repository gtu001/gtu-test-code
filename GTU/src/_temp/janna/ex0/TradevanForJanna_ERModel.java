package _temp.janna.ex0;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import _temp.janna.ex0.TradevanForJanna_MssqlGenerator.TableDef;
import gtu.file.FileUtil;
import gtu.poi.hwpf.docx.DocxReaderUtil;
import gtu.poi.hwpf.docx.DocxTableUtil;
import gtu.swing.util.JCommonUtil;

public class TradevanForJanna_ERModel {

    HashMap<String, TableDef> allmap;

    public static void main(String[] args) throws IOException {
        TradevanForJanna_ERModel t = new TradevanForJanna_ERModel();
        t.initData();

        File docx = new File("C:/Users/gtu001/Desktop/Doc1.docx");

        FileInputStream fis = new FileInputStream(docx);
        XWPFDocument document = new XWPFDocument(fis);
        for (int ii = 0; ii < document.getBodyElements().size(); ii++) {
            IBodyElement ele = document.getBodyElements().get(ii);
            System.out.println("## index = " + ii);
            if (ele instanceof XWPFParagraph) {
            } else if (ele instanceof XWPFTable) {
            }
        }

        XWPFDocument document2 = new XWPFDocument();

        XWPFTable cloneFrom = document.getTables().get(0);

        for (String k : t.allmap.keySet()) {
            TableDef d = t.allmap.get(k);

            XWPFTable table2 = DocxTableUtil.createCloneTable(cloneFrom, document2);
            XWPFTableRow row = table2.getRow(0);
            row.getCell(0).setText(d.tabName + "" + d.tabNameChs);

            for (int ii = 0; ii < d.colList.size(); ii++) {
                XWPFTableRow row2 = t.getRow(table2, ii + 1);

                XWPFTableCell cell = t.getCell(row2, 0);
                String chs = d.colList.get(ii).chinese;
                chs = chs == null ? "" : chs;
                cell.setText(chs);

                XWPFTableCell cell2 = t.getCell(row2, 1);
                String eng = d.colList.get(ii).type;
                eng = eng == null ? "" : eng;
                cell2.setText(eng);
            }

            document2.createParagraph().createRun().addBreak();
        }

        DocxTableUtil.writeOut(new File("C:/Users/gtu001/Desktop/Doc2.docx"), document2);
    }

    private XWPFTableRow getRow(XWPFTable table2, int ii) {
        XWPFTableRow row2 = null;
        try {
            if (ii < table2.getNumberOfRows()) {
                row2 = table2.getRows().get(ii);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (row2 == null) {
            row2 = table2.createRow();
        }
        return row2;
    }

    private XWPFTableCell getCell(XWPFTableRow row2, int ii) {
        XWPFTableCell cell = null;
        try {
            cell = row2.getCell(ii);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (cell == null) {
            cell = row2.createCell();
        }
        return cell;
    }

    private void initData() {
        String dirPath = "D:/workstuff/workspace/gtu-test-code/GTU/src/_temp/janna/J122-SDD-001(資料庫及檔案規格)_V1.0_出租管理作業_20170528.docx";
        File file = JCommonUtil.filePathCheck(dirPath, "檔案路徑", false);
        List<File> fileList = new ArrayList<File>();
        if (file.isFile()) {
            if (file.getName().endsWith(".docx")) {
                fileList.add(file);
            }
        } else {
            FileUtil.searchFilefind(file, ".*\\.docx", fileList);
        }

        if (fileList.isEmpty()) {
            JCommonUtil._jOptionPane_showMessageDialog_error("查無檔案");
            return;
        }
        allmap = new HashMap<String, TableDef>();
        for (File f : fileList) {
            TradevanForJanna_MssqlGenerator gen = new TradevanForJanna_MssqlGenerator();
            gen.execute(f);
            allmap.putAll(gen.tabMap);
        }
    }
}
