package gtu._work.ui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Row;

import gtu.collection.MapUtil;
import gtu.file.FileUtil;
import gtu.poi.hssf.ExcelUtil_Xls97;
import gtu.poi.hssf.ExcelWriter;
import gtu.poi.hssf.ExcelWriter.CellStyleHandler;

public class FastDBQueryUI_RecordWatcherDirectXls {

    String fileMiddleName;

    List<Integer> pkIndexLst = new ArrayList<Integer>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    NewNameHandler mNewNameHandler;
    StringBuffer errMsg = new StringBuffer();
    Map<String, String> columnsAndChinese;

    public FastDBQueryUI_RecordWatcherDirectXls(String fileMiddleName, List<Integer> pkIndexLst, Map<String, String> columnsAndChinese) {
        this.fileMiddleName = fileMiddleName;
        this.pkIndexLst = pkIndexLst;
        this.columnsAndChinese = columnsAndChinese;
    }

    private File compareOldAndNew(List<Object[]> oldLst, List<Object[]> newLst, List<String> titleLst, long queryEndTime) {
        Map<String, Object[]> oArry = new LinkedHashMap<String, Object[]>();
        Map<String, Object[]> nArry = new LinkedHashMap<String, Object[]>();

        if (pkIndexLst.isEmpty()) {
            throw new RuntimeException("請先設定欄位比對PK!");
        }

        this.appendAllRecords(oldLst, oArry);
        this.appendAllRecords(newLst, nArry);

        List<Object[]> delArry = new ArrayList<Object[]>();
        List<Object[]> newArry = new ArrayList<Object[]>();
        List<Pair<Object[], Object[]>> updArry = new ArrayList<Pair<Object[], Object[]>>();

        for (String key : oArry.keySet()) {
            if (!nArry.containsKey(key)) {
                delArry.add(oArry.get(key));
            } else {
                Object[] oldx = oArry.get(key);
                Object[] newx = nArry.get(key);
                if (!isArrayEquals(oldx, newx)) {
                    updArry.add(Pair.of(oldx, newx));
                }
            }
        }

        for (String key : nArry.keySet()) {
            if (!oArry.containsKey(key)) {
                newArry.add(nArry.get(key));
            }
        }

        File xlsFile = null;
        if (!delArry.isEmpty() || !newArry.isEmpty() || !updArry.isEmpty()) {
            if (mNewNameHandler == null) {
                mNewNameHandler = new NewNameHandler(fileMiddleName, queryEndTime);
            }
            xlsFile = this.writeExcel(titleLst, delArry, newArry, updArry);
            System.out.println("寫入比對檔 : " + xlsFile);
        } else {
            errMsg.append("前後資料沒有任何異動!");
        }
        return xlsFile;
    }

    private File writeExcel(List<String> titleLst, List<Object[]> delArry, List<Object[]> newArry, List<Pair<Object[], Object[]>> updArry) {
        File xlsFile = new File(FileUtil.DESKTOP_DIR, mNewNameHandler.getName(false));

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sn = null;
        HSSFSheet sd = null;
        HSSFSheet su = null;

        boolean isExist = false;
        if (xlsFile.exists()) {
            isExist = true;
            wb = ExcelUtil_Xls97.getInstance().readExcel(xlsFile);
            sn = wb.getSheet("new");
            sd = wb.getSheet("del");
            su = wb.getSheet("update");
        } else {
            sn = wb.createSheet("new");
            sd = wb.createSheet("del");
            su = wb.createSheet("update");
        }

        CellStyleHandler pkCs = ExcelWriter.CellStyleHandler.newInstance(wb.createCellStyle())//
                .setForegroundColor(new HSSFColor.LAVENDER());
        CellStyleHandler nonPkCs = ExcelWriter.CellStyleHandler.newInstance(wb.createCellStyle())//
                .setForegroundColor(new HSSFColor.AQUA());

        CellStyleHandler changeCs = ExcelWriter.CellStyleHandler.newInstance(wb.createCellStyle())//
                .setForegroundColor(new HSSFColor.YELLOW());

        CellStyleHandler splitCs = ExcelWriter.CellStyleHandler.newInstance(wb.createCellStyle())//
                .setForegroundColor(new HSSFColor.BLUE_GREY());

        Object[] titleLst2 = titleLst.toArray();
        if (!isExist) {
            addRow(0, sn, titleLst2);
            addRow(0, sd, titleLst2);
            addRow(0, su, titleLst2);
            applyStyleByIndex(0, sn, pkIndexLst, pkCs, nonPkCs);
            applyStyleByIndex(0, sd, pkIndexLst, pkCs, nonPkCs);
            applyStyleByIndex(0, su, pkIndexLst, pkCs, nonPkCs);

            if (isColumnsAndChineseReady()) {
                addRow_NChinese(1, sn, titleLst2, columnsAndChinese);
                addRow_NChinese(1, sd, titleLst2, columnsAndChinese);
                addRow_NChinese(1, su, titleLst2, columnsAndChinese);
                applyStyleByIndex(1, sn, pkIndexLst, pkCs, nonPkCs);
                applyStyleByIndex(1, sd, pkIndexLst, pkCs, nonPkCs);
                applyStyleByIndex(1, su, pkIndexLst, pkCs, nonPkCs);
            }
        } else {
            addRowSplit(sn.getLastRowNum() + 1, sn, titleLst2, splitCs);
            addRowSplit(sd.getLastRowNum() + 1, sd, titleLst2, splitCs);
            addRowSplit(su.getLastRowNum() + 1, su, titleLst2, splitCs);
        }

        for (Object[] d : delArry) {
            addRow(sd.getLastRowNum() + 1, sd, d);
        }

        for (Object[] d : newArry) {
            addRow(sn.getLastRowNum() + 1, sn, d);
        }

        int uIdx = su.getLastRowNum() + 1;
        for (Pair<Object[], Object[]> d : updArry) {
            addRowDiff(uIdx, su, d.getLeft(), d.getRight(), changeCs);
            uIdx += 2;
        }

        ExcelUtil_Xls97.getInstance().autoCellSize(sn);
        ExcelUtil_Xls97.getInstance().autoCellSize(sd);
        ExcelUtil_Xls97.getInstance().autoCellSize(su);

        try {
            ExcelUtil_Xls97.getInstance().writeExcel(xlsFile, wb);
        } catch (Exception ex) {
            xlsFile = new File(FileUtil.DESKTOP_DIR, mNewNameHandler.getName(true));
            ExcelUtil_Xls97.getInstance().writeExcel(xlsFile, wb);
        }
        return xlsFile;
    }

    private void applyStyleByIndex(int rowIdx, HSSFSheet sn, List<Integer> indexLst, CellStyleHandler pkCs, CellStyleHandler nonPkCs) {
        pkCs.setSheet(sn);
        nonPkCs.setSheet(sn);
        Row row = sn.getRow(rowIdx);
        int max = row.getLastCellNum();
        if (max >= 255) {
            max = 255;
        }
        for (int ii = 0; ii <= max; ii++) {
            if (indexLst.contains(ii)) {
                pkCs.applyStyle(rowIdx, ii);
            } else {
                nonPkCs.applyStyle(rowIdx, ii);
            }
        }
    }

    private void addRow(int rowIdx, HSSFSheet sn, Object[] data) {
        HSSFRow r1 = sn.createRow(rowIdx);
        for (int ii = 0; ii < data.length; ii++) {
            String d = String.valueOf(data[ii]);
            r1.createCell(ii).setCellValue(d);
        }
    }

    private void addRow_NChinese(int rowIdx, HSSFSheet sn, Object[] data, Map<String, String> columnsAndChinese) {
        HSSFRow r1 = sn.createRow(rowIdx);
        for (int ii = 0; ii < data.length; ii++) {
            String d = String.valueOf(data[ii]);
            String chinese = MapUtil.getIgnorecase(d, columnsAndChinese);
            r1.createCell(ii).setCellValue(chinese);
        }
    }

    private void addRowSplit(int rowIdx, HSSFSheet sn, Object[] data, CellStyleHandler split) {
        String[] arry = new String[data.length];
        Arrays.fill(arry, "");
        addRow(rowIdx, sn, arry);
        split.setSheet(sn);
        split.applyStyle(rowIdx, rowIdx, 0, data.length - 1);
    }

    private void addRowDiff(int startRowIdx, HSSFSheet sn, Object[] oldx, Object[] newx, CellStyleHandler cs) {
        List<Integer> diffIndexLst = new ArrayList<Integer>();
        for (int ii = 0; ii < oldx.length; ii++) {
            String ox = String.valueOf(oldx[ii]);
            String nx = String.valueOf(newx[ii]);
            if (!StringUtils.equals(ox, nx)) {
                diffIndexLst.add(ii);
            }
        }

        addRow(startRowIdx, sn, oldx);
        addRow(startRowIdx + 1, sn, newx);

        for (int cellIdx : diffIndexLst) {
            cs.setSheet(sn);
            cs.applyStyle(startRowIdx, cellIdx);
            cs.applyStyle(startRowIdx + 1, cellIdx);
        }
    }

    private boolean isArrayEquals(Object[] oldx, Object[] newx) {
        String o1 = StringUtils.join(oldx);
        String n1 = StringUtils.join(newx);
        return StringUtils.equals(o1, n1);
    }

    private void appendAllRecords(List<Object[]> oldLst, Map<String, Object[]> oArry) {
        for (int ii = 0; ii < oldLst.size(); ii++) {
            Object[] arry = oldLst.get(ii);
            List<String> pkLst = new ArrayList<String>();
            for (int idx : pkIndexLst) {
                pkLst.add(String.valueOf(arry[idx]));
            }
            String key = StringUtils.join(pkLst, "^");
            oArry.put(key, arry);
        }
    }

    public Triple<List<String>, List<Class<?>>, List<Object[]>> convertXlsToQueryResult(File xlsFile) {
        ExcelUtil_Xls97 xlsUtil = ExcelUtil_Xls97.getInstance();
        HSSFWorkbook wb = xlsUtil.readExcel(xlsFile);
        HSSFSheet sheet = wb.getSheetAt(0);
        if (wb.getNumberOfSheets() == 2) {
            sheet = wb.getSheetAt(1);
        }

        List<Object[]> rowLst = new ArrayList<Object[]>();
        List<String> columnLst = new ArrayList<String>();
        for (int jj = 0; jj < sheet.getRow(0).getLastCellNum(); jj++) {
            String value = ExcelUtil_Xls97.getInstance().readCell(sheet.getRow(0).getCell(jj));
            if (StringUtils.isNotBlank(value)) {
                columnLst.add(value);
            }
        }
        int columnCount = columnLst.size();
        TreeMap<Integer, Class<?>> typeClzMap = new TreeMap<Integer, Class<?>>();
        for (int ii = 1; ii <= sheet.getLastRowNum(); ii++) {
            Row row = sheet.getRow(ii);
            if (row == null) {
                continue;
            }
            List<Object> dataRow = new ArrayList<Object>();
            for (int jj = 0; jj < row.getLastCellNum(); jj++) {
                Object value = ExcelUtil_Xls97.getInstance().readCell2(row.getCell(jj));
                dataRow.add(value);
                if (value != null && !typeClzMap.containsKey(jj)) {
                    typeClzMap.put(jj, value.getClass());
                }
            }
            if (columnCount != dataRow.size()) {
                // throw new RuntimeException("Warning : " + xlsFile.getName() +
                // ", Row " + ii + " 現實欄位數為 " + dataRow.size() + " , 要求為 " +
                // columnCount);
                System.out.println("Warning : " + xlsFile.getName() + ", Row " + ii + " 現實欄位數為 " + dataRow.size() + " , 要求為 " + columnCount);
                continue;
            }
            rowLst.add(dataRow.toArray());
        }
        if (typeClzMap.size() != columnCount) {
            throw new RuntimeException("classType 現實欄位數為 " + typeClzMap.size() + " , 要求為 " + columnCount);
        }
        List<Class<?>> typeLst = new ArrayList<Class<?>>(typeClzMap.values());
        return Triple.of(columnLst, typeLst, rowLst);
    }

    public Pair<File, String> run(File orignXls, File newXls) {
        try {
            Triple<List<String>, List<Class<?>>, List<Object[]>> orignQueryResult = convertXlsToQueryResult(orignXls);
            Triple<List<String>, List<Class<?>>, List<Object[]>> compareResult = convertXlsToQueryResult(newXls);
            File diffFile = null;

            if (orignQueryResult != null && compareResult != null) {
                List<String> titleLst = orignQueryResult.getLeft();
                if (titleLst.size() != compareResult.getLeft().size()) {
                    throw new RuntimeException("欄位數不同(old/new) : " + titleLst.size() + "/" + compareResult.getLeft().size());
                } else {
                    // 真實比對 XXX
                    // ==============================================================
                    diffFile = compareOldAndNew(orignQueryResult.getRight(), compareResult.getRight(), titleLst, System.currentTimeMillis());
                    // 真實比對 XXX
                    // ==============================================================
                }
            }
            return Pair.of(diffFile, errMsg.toString());
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }

    private class NewNameHandler {
        String middleName;
        long queryEndTime;

        private NewNameHandler(String fileMiddleName, long queryEndTime) {
            middleName = StringUtils.trimToEmpty(fileMiddleName);
            this.queryEndTime = queryEndTime;
        }

        private String getName(boolean addOne) {
            if (addOne) {
                this.queryEndTime++;
            }
            String xlsNameTemp = "FastDBQueryUI_DiffMark_" + middleName + "_" + sdf.format(queryEndTime) + ".xls";
            return xlsNameTemp;
        }
    }

    public void setPkIndexLst(List<Integer> pkIndexLst) {
        this.pkIndexLst = pkIndexLst;
    }

    private boolean isColumnsAndChineseReady() {
        if (columnsAndChinese == null || columnsAndChinese.isEmpty()) {
            return false;
        }
        for (String key : columnsAndChinese.keySet()) {
            if (StringUtils.isNotBlank(columnsAndChinese.get(key))) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        String fileMiddleName = "test";
        List<Integer> pkIndexLst = new ArrayList<Integer>();
        pkIndexLst.add(0);
        pkIndexLst.add(1);
        pkIndexLst.add(2);
        File beforeFile = new File("C:/Users/wistronits/Desktop/執行前後/執行前後/1A.LCInsureAcc.xls");
        File afterFile = new File("C:/Users/wistronits/Desktop/執行前後/執行前後/1B.LCInsureAcc.xls");
        FastDBQueryUI_RecordWatcherDirectXls mFastDBQueryUI_RecordWatcherDirectXls = new FastDBQueryUI_RecordWatcherDirectXls(fileMiddleName, pkIndexLst, null);
        mFastDBQueryUI_RecordWatcherDirectXls.run(beforeFile, afterFile);
        System.out.println("done..");
    }
}
