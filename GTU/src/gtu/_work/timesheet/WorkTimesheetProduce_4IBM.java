package gtu._work.timesheet;

import java.awt.Point;
import java.io.File;
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

    public static void main(String[] args) throws ParseException {
        WorkTimesheetProduce_4IBM test = new WorkTimesheetProduce_4IBM();

        InputStream inputStream = test.getClass().getResourceAsStream("Timesheet-MES-July2018_Name.xls");

        HSSFWorkbook workbook = excelUtil.readExcel(inputStream);

        HSSFSheet sheet = workbook.getSheetAt(0);

        Map<String, Integer> workMap = new LinkedHashMap<String, Integer>();
        workMap.put("建立kubenate環境", 4);
        workMap.put("建立spring boot環境", 3);
        workMap.put("建立rabbit mq環境", 3);
        workMap.put("建立spring data環境", 4);
        workMap.put("建立postgre sql環境", 3);
        workMap.put("建立model", 20);

        FindStartRow startRow = test.new FindStartRow(sheet);

        for (int ii = 0; ii <= sheet.getLastRowNum(); ii++) {
            Row row = sheet.getRow(ii);
            test.new ProcessRow(row, ii, startRow, workMap);
        }

        excelUtil.writeExcel(new File(FileUtil.DESKTOP_DIR, "Timesheet-MES-July2018_張純毓Troy.xls"), workbook);

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
        private String[] fromTime = new String[] { "08:55", "08:56", "08:57", "08:58", "08:59", "09:00", "09:01", "09:02", "09:03" };
        private String[] $$toTime = new String[] { "18:00", "18:01", "18:02", "18:03", "18:04", "18:05", "18:06", "18:07", "18:08" };

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
