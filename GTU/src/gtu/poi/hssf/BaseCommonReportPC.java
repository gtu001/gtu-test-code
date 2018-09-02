package gtu.poi.hssf;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;

/**
 * SP_REPAIR_TICKET 單筆查詢結果報表
 * 
 * @author Troy
 * @version 2010/9/14
 */
public class BaseCommonReportPC  {
    
    private static final BaseCommonReportPC INST = new BaseCommonReportPC();
    public static BaseCommonReportPC getInstance(){
        return INST;
    }

//    public static void main(String[] args) throws Exception {
//        Object[][] mmmp = new Object[][] { 
//                new Object[] { "req.eform_REPORT_APP_ID_1", new String[] { "ANO3" } }, 
//                new Object[] { "req.eform_SP_REPAIR_TICKET_REPAIR_TICKET_ID_1", new String[] { "RT20100823002" } }, 
//        };
//        
//        BaseCommonReportPC pc = new BaseCommonReportPC();
//        Map inputMap = ArrayUtils.toMap(mmmp);
//        pc.reportProcess(null, inputMap, new ExcelReporter());
//        System.out.println("done...");
//    }

    public HSSFCell getCell(int rownum, int cellnum, HSSFSheet sheet) {
        HSSFRow row = sheet.getRow(rownum);
        if (row == null) {
            row = sheet.createRow(rownum);
        }
        HSSFCell cell = row.getCell((short) cellnum);
        if (cell == null) {
            cell = row.createCell((short) cellnum);
        }
        return cell;
    }

    /**
     * 合併儲存格
     */
    private void combineCell(HSSFSheet sheet, int startX, int startY, int endX, int endY) {
        Region region = new Region(startY, (short) startX, endY, (short) endX);
        sheet.addMergedRegion(region);
    }

    /**
     * 合併儲存格
     */
    private void combineCellRow(HSSFSheet sheet, int row, int startX, int endX) {
        this.combineCell(sheet, startX, row, endX, row);
    }

    public static class IIStyle {
        private final HSSFFont font;
        private final HSSFCellStyle cellStyle;
        public HSSFCellStyle getStyle() {
            return cellStyle;
        }
        public static IIStyle of(HSSFWorkbook workbook) {
            return new IIStyle(workbook);
        }
        private IIStyle(HSSFWorkbook workbook) {
            this.font = workbook.createFont();
            this.cellStyle = workbook.createCellStyle();
        }
        public IIStyle alignCenterSelection() {
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);
            return this;
        }
        public IIStyle alignCenter() {
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            return this;
        }
        public IIStyle alignRight() {
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            return this;
        }
        public IIStyle warpText() {
            cellStyle.setWrapText(true);
            return this;
        }
        public IIStyle verticalCenter() {
            cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            return this;
        }
        public IIStyle boderBlock() {
            HSSFColor color = new HSSFColor.BLACK();
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBottomBorderColor(color.getIndex());
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            cellStyle.setLeftBorderColor(color.getIndex());
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
            cellStyle.setRightBorderColor(color.getIndex());
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
            cellStyle.setTopBorderColor(color.getIndex());
            return this;
        }
        public IIStyle fontSize(int size) {
            font.setFontHeightInPoints((short) size);
            cellStyle.setFont(font);
            return this;
        }
        public IIStyle fontColor(HSSFColor color) {
            font.setColor(color.getIndex());
            cellStyle.setFont(font);
            return this;
        }
        public IIStyle fillForegroundColor(HSSFColor color) {
            cellStyle.setFillForegroundColor(color.getIndex());
            cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            return this;
        }
        public IIStyle fontBold() {
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            cellStyle.setFont(font);
            return this;
        }
        public IIStyle underline() {
            HSSFColor color = new HSSFColor.BLACK();
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBottomBorderColor(color.getIndex());
            return this;
        }
    }
    
    public void processExcelColumn(List<Cell> list, HSSFSheet sheet) {
        for (Cell excel : list) {
            this.getCell(excel.getRowStart(), excel.getColumnStart(), sheet).setCellValue(excel.getValue());
            if(excel.isCombine()) {
                this.combineCell(sheet, excel.getColumnStart(), excel.getRowStart(), excel.getColumnEnd(), excel.getRowEnd());
            }
            if (excel.getCellStyle() != null) {
                for (int rownum = excel.getRowStart(); rownum <= excel.getRowEnd(); rownum++) {
                    for (int cellnum = excel.getColumnStart(); cellnum <= excel.getColumnEnd(); cellnum++) {
                        this.getCell(rownum, cellnum, sheet).setCellStyle(excel.getCellStyle());
                    }
                }
            }
        }
    }

    public static class Cell {
        private final int rowStart;
        private final int rowEnd;
        private final int columnStart;
        private final int columnEnd;
        private final String value;
        private final HSSFCellStyle cellStyle;
        private final boolean combine;
        public static Cell create(String value, int rowStart, int rowEnd, int columnStart, int columnEnd, HSSFCellStyle cellStyle, boolean combine) {
            return new Cell(rowStart, rowEnd, columnStart, columnEnd, value, cellStyle, combine);
        }
        public static Cell colspan(int row, String value, int columnStart, int columnEnd, HSSFCellStyle cellStyle) {
            return new Cell(row, row, columnStart, columnEnd, value, cellStyle, true);
        }
        public static Cell colspan(int row, String value, int columnStart, int columnEnd) {
            return new Cell(row, row, columnStart, columnEnd, value, null, true);
        }
        public static Cell rowspan(int col, String value, int rowStart, int rowEnd, HSSFCellStyle cellStyle) {
            return new Cell(rowStart, rowEnd, col, col, value, cellStyle, true);
        }
        public static Cell rowspan(int col, String value, int rowStart, int rowEnd) {
            return new Cell(rowStart, rowEnd, col, col, value, null, true);
        }
        public Cell(int rowStart, int rowEnd, int columnStart, int columnEnd, String value, HSSFCellStyle cellStyle, boolean combine) {
            this.rowStart = rowStart;
            this.rowEnd = rowEnd;
            this.columnStart = columnStart;
            this.columnEnd = columnEnd;
            this.value = value;
            this.cellStyle = cellStyle;
            this.combine = combine;
        }
        public int getRowStart() {
            return rowStart;
        }
        public int getRowEnd() {
            return rowEnd;
        }
        public int getColumnStart() {
            return columnStart;
        }
        public int getColumnEnd() {
            return columnEnd;
        }
        public String getValue() {
            return value;
        }
        public HSSFCellStyle getCellStyle() {
            return cellStyle;
        }
        public boolean isCombine() {
            return combine;
        }
    }
}
