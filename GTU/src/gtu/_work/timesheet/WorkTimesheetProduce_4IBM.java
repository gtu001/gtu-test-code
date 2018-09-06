package gtu._work.timesheet;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import gtu.file.FileUtil;
import gtu.poi.hssf.ExcelUtil;

/**
 * 工時表 timesheet 產生器
 * 
 * @author gtu001
 */
public class WorkTimesheetProduce_4IBM {

    static ExcelUtil excelUtil = ExcelUtil.getInstance();

    public static void main(String[] args) throws ParseException, FileNotFoundException {
        WorkTimesheetProduce_4IBM test = new WorkTimesheetProduce_4IBM();

        // InputStream inputStream =
        // test.getClass().getResourceAsStream("Timesheet-MES-July2018_Name.xls");
        InputStream inputStream = new FileInputStream(new File(FileUtil.DESKTOP_DIR, "Timesheet-MES-August 2018_Name.xls"));

        HSSFWorkbook workbook = excelUtil.readExcel(inputStream);

        HSSFSheet sheet = workbook.getSheetAt(0);

        Map<String, Integer> workMap = new LinkedHashMap<String, Integer>();
        workMap.put("修改centeral page修改centeral page ", 1);
        workMap.put("修改畫面重整修改畫面重整 ", 1);
        workMap.put("show detail tableshow detail table ", 1);
        workMap.put("修改 json loading修改 json loading ", 1);
        workMap.put("修改為 stomp 版本修改為 stomp 版本 ", 1);
        workMap.put("修改至中, 與template 來源修改至中, 與template 來源 ", 1);
        workMap.put("jq bootgrid & stomp for rabbit mqjq bootgrid & stomp for rabbit mq ", 1);
        workMap.put("刪除重複entity刪除重複entity ", 1);
        workMap.put("修改關聯修改關聯 ", 1);
        workMap.put("刪除測試code刪除測試code ", 1);
        workMap.put("修正型別修正型別 ", 1);
        workMap.put("補上 relation補上 relation ", 1);
        workMap.put("補上 relation step3 補上 relation step3 ", 1);
        workMap.put("補上 relation step2補上 relation step2 ", 1);
        workMap.put("補上 relation step1補上 relation step1 ", 1);
        workMap.put("補缺 補缺 ", 1);
        workMap.put("equipment 多對多equipment 多對多 ", 1);
        workMap.put("測試用 測試用 ", 1);
        workMap.put("測試用測試用 ", 1);
        workMap.put("拿掉 dynamic relation拿掉 dynamic relation ", 1);
        workMap.put("改多對多改多對多 ", 1);
        workMap.put("加入 entity & relation for equipment加入 entity & relation for equipment ", 1);
        workMap.put("拿掉 dynamic拿掉 dynamic ", 1);
        workMap.put("repository cleaner repository cleaner ", 1);
        workMap.put("錯誤修正 operation relation錯誤修正 operation relation ", 1);
        workMap.put("加入 operation 關聯加入 operation 關聯 ", 1);
        workMap.put("加入 repository 加入 repository ", 1);
        workMap.put("補上 operations entity 補上 operations entity ", 1);
        workMap.put("優先加入 oper seg 優先加入 oper seg ", 1);
        workMap.put("修正關聯修正關聯 ", 1);
        workMap.put("修改 repository pk type修改 repository pk type ", 1);
        workMap.put("將PK 修改為 string將PK 修改為 string ", 1);
        workMap.put("修改關聯 final修改關聯 final ", 1);
        workMap.put("修改jsp 修改jsp ", 1);
        workMap.put("加入關聯加入關聯 ", 1);
        workMap.put("修改繼承修改繼承 ", 1);
        workMap.put("補上缺的entity補上缺的entity ", 1);

        FindStartRow startRow = test.new FindStartRow(sheet);

        for (int ii = 0; ii <= sheet.getLastRowNum(); ii++) {
            Row row = sheet.getRow(ii);
            test.new ProcessRow(row, ii, startRow, workMap);
        }
        excelUtil.writeExcel(new File(FileUtil.DESKTOP_DIR, "Timesheet-MES-August 2018_張純毓Troy.xls"), workbook);

        System.out.println("done...");
    }

    private String getWorkContent(Map<String, Integer> workMap) {
        if (workMap.isEmpty()) {
            throw new RuntimeException("Empty!! : " + workMap);
        }
        String key = workMap.keySet().iterator().next();
        int val = workMap.get(key);
        val--;
        if (val == 0) {
            workMap.remove(key);
        } else {
            workMap.put(key, val);
        }
        return key;
    }

    private class ProcessRow {
        private String[] fromTime = new String[] { "8:55", "8:56", "8:57", "8:58", "8:59", "9:00", "9:01", "9:02", "9:03" };
        private String[] $$toTime = new String[] { "6:00", "6:01", "6:02", "6:03", "6:04", "6:05", "6:06", "6:07", "6:08" };

        ProcessRow(Row row, int rowIndex, FindStartRow firstRow, Map<String, Integer> workMap) {
            Cell cell = row.getCell((int) firstRow.weekend.getX());
            String cellVal = excelUtil.readCell(cell);
            if ("Weekend".equals(cellVal)) {
                return;
            }
            if (rowIndex <= firstRow.fromHMm.getY() || rowIndex >= firstRow.endIndex) {
                return;
            }

            // process
            setCellValueByPoint(getRandomVal(fromTime), firstRow.fromHMm, row);
            setCellValueByPoint(getRandomVal($$toTime), firstRow.toHMm, row);
            setCellValueByPoint(getWorkContent(workMap), firstRow.weekend, row);
        }

        private String getRandomVal(String[] arry) {
            return arry[new Random().nextInt(arry.length)];
        }

        private void setCellValueByPoint(String strVal, Point point, Row row) {
            int cellIndex = (int) point.getX();
            Cell cell = excelUtil.getCellChk(row, cellIndex);
            if (StringUtils.isBlank(excelUtil.readCell(cell))) {
                excelUtil.setCellValue(cell, strVal);
                System.out.println(String.format("cell : %d - %s", cellIndex, strVal));
            }
        }
    }

    private class FindStartRow {

        Point dayDate = new Point();
        Point fromHMm = new Point();
        Point toHMm = new Point();
        Point weekend = new Point();
        int endIndex = -1;

        FindStartRow(HSSFSheet sheet) {
            for (int ii = 0; ii <= sheet.getLastRowNum(); ii++) {
                Row row = sheet.getRow(ii);
                if (row == null) {
                    continue;
                }
                for (int jj = 0; jj < row.getLastCellNum(); jj++) {
                    String value = ExcelUtil.getInstance().readCell(row.getCell(jj));
                    value = StringUtils.trimToEmpty(value);

                    int x = jj;
                    int y = ii;

                    if (value.equals("Day, Date")) {
                        dayDate.setLocation(x, y);
                    } else if (value.equals("from (h:mm)")) {
                        fromHMm.setLocation(x, y);
                    } else if (value.equals("to (h:mm)")) {
                        toHMm.setLocation(x, y);
                    } else if (value.equals("Weekend")) {
                        weekend.setLocation(x, -1);
                    }

                    if (endIndex == -1) {
                        if (dayDate.getX() > 0 && dayDate.getY() > 0) {
                            if (dayDate.getX() == x) {
                                if (StringUtils.isBlank(value)) {
                                    endIndex = y;
                                }
                            }
                        }
                    }
                }
            }
            weekend.y = fromHMm.y;
        }
    }
}
