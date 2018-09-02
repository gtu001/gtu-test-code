package gtu.poi.hssf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.lang.Validate;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * Excel元件
 * 
 * @author Troy
 */
public class ExcelWriter {

    public static class FontHandler {
        private HSSFFont font;

        /**
         * HSSFWorkbook.createFont()
         * 
         * @param font
         * @return
         */
        public static FontHandler newInstance(HSSFFont font) {
            return new FontHandler(font);
        }
        public static FontHandler newInstance(HSSFWorkbook workbook) {
            return new FontHandler(workbook.createFont());
        }

        private FontHandler(HSSFFont font) {
            this.font = font;
        }

        public FontHandler setBoldweight(short boldweight) {
            //HSSFFont.BOLDWEIGHT_BOLD 設粗體要放這個值 XXX
            font.setBoldweight(boldweight);
            return this;
        }

        public FontHandler setCharSet(byte charset) {
            font.setCharSet(charset);
            return this;
        }

        public FontHandler setColor(short color) {
            font.setColor(color);
            return this;
        }

        public FontHandler setFontName(String name) {
            font.setFontName(name);
            return this;
        }

        public FontHandler setItalic(boolean italic) {
            font.setItalic(italic);
            return this;
        }

        public FontHandler setStrikeout(boolean strikeout) {
            font.setStrikeout(strikeout);
            return this;
        }

        public FontHandler setTypeOffset(short offset) {
            font.setTypeOffset(offset);
            return this;
        }

        public FontHandler setUnderline(byte underline) {
            font.setUnderline(underline);
            return this;
        }

        public FontHandler setFontSize(short fontsize) {
            font.setFontHeightInPoints(fontsize);
            return this;
        }

        public HSSFFont getFont() {
            return font;
        }
    }

    public static class CellStyleHandler {
        /**
         * HSSFWorkbook.createCellStyle()
         * 
         * @param cellStyle
         * @return
         */
        public static CellStyleHandler newInstance(HSSFCellStyle cellStyle) {
            return new CellStyleHandler(cellStyle);
        }
        public static CellStyleHandler newInstance(HSSFWorkbook workbook) {
            return new CellStyleHandler(workbook.createCellStyle());
        }

        private final HSSFCellStyle cellStyle;
        private HSSFSheet sheet;

        private CellStyleHandler(HSSFCellStyle cellStyle) {
            this.cellStyle = cellStyle;
        }

        public HSSFCellStyle getCellStyle() {
            return cellStyle;
        }

        public CellStyleHandler setAlignment(short hssfCellStyle) {
            this.cellStyle.setAlignment(hssfCellStyle);
            return this;
        }

        /**
         * 設定框線顏色
         */
        public CellStyleHandler setBorder(HSSFColor color) {
            this.setBorder(color, cellStyle);
            return this;
        }

        private void setBorder(HSSFColor color, HSSFCellStyle cellStyle) {
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBottomBorderColor(color.getIndex());
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            cellStyle.setLeftBorderColor(color.getIndex());
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
            cellStyle.setRightBorderColor(color.getIndex());
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
            cellStyle.setTopBorderColor(color.getIndex());
        }

        /**
         * 設定報表資料外框顏色
         */
        public CellStyleHandler setBorderColor(HSSFSheet sheet, int headerLength, HSSFColor color) {
            this.setBorderColor(sheet, headerLength, color, cellStyle);
            return this;
        }

        private void setBorderColor(HSSFSheet sheet, int headerLength, HSSFColor color, HSSFCellStyle style) {
            this.setBorder(color, style);
            this.setCellStyle(sheet, 0, 0, sheet.getLastRowNum(), headerLength - 1, style);
        }

        /**
         * 設定point style
         */
        public CellStyleHandler applyStyle(int rowNum, int cellNum) {
            this.setCellStyle(sheet, rowNum, cellNum, cellStyle);
            return this;
        }

        private void setCellStyle(HSSFSheet sheet, int rowNum, int cellNum, HSSFCellStyle style) {
            Validate.notNull(sheet);
            Validate.notNull(style);
            HSSFRow row = sheet.getRow(rowNum);
            HSSFCell cell = row.getCell(cellNum);
            if (null == cell) {
                cell = row.createCell(cellNum);
            }
            cell.setCellStyle(style);
        }

        private void setCellStyle(HSSFSheet sheet, int startRowIndex, int endRowIndex, int startCellIndex,
                int endCellIndex, HSSFCellStyle style) {
            Validate.notNull(sheet);
            Validate.notNull(style);
            for (int rowNum = startRowIndex; rowNum <= endRowIndex; rowNum++) {
                for (int cellNum = startCellIndex; cellNum <= endCellIndex; cellNum++) {
                    try {
                        this.setCellStyle(sheet, rowNum, cellNum, style);
                    } catch (Exception ex) {
                        System.err.println("row:" + rowNum + " cell:" + cellNum);
                    }
                }
            }
        }

        /**
         * 設定matrix style
         */
        public CellStyleHandler applyStyle(int top, int down, int left, int right) {
            this.setCellStyle(sheet, top, down, left, right, cellStyle);
            return this;
        }

        /**
         * 設定前景顏色
         */
        public CellStyleHandler setForegroundColor(HSSFColor color) {
            this.setForegroundColor(color, cellStyle);
            return this;
        }

        private void setForegroundColor(HSSFColor color, HSSFCellStyle cellStyle) {
            cellStyle.setFillForegroundColor(color.getIndex());
            cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        }

        /**
         * 設定報表資料行顏色
         */
        public CellStyleHandler setLineColor(HSSFSheet sheet, int rowNum, int headerLength, HSSFColor color) {
            this.setLineColor(sheet, rowNum, headerLength, color, cellStyle);
            return this;
        }

        private void setLineColor(HSSFSheet sheet, int rowNum, int headerLength, HSSFColor color, HSSFCellStyle style) {
            Validate.notNull(sheet);
            Validate.notNull(style);
            this.setForegroundColor(color, style);
            this.setCellStyle(sheet, rowNum, 0, rowNum, headerLength - 1, style);
        }

        public CellStyleHandler setSheet(HSSFSheet sheet) {
            this.sheet = sheet;
            Validate.notNull(sheet);
            return this;
        }

        public CellStyleHandler setVerticalAlignment(short hssfCellStyle) {
            this.cellStyle.setVerticalAlignment(hssfCellStyle);
            return this;
        }

        public CellStyleHandler setFont(HSSFFont font) {
            this.cellStyle.setFont(font);
            return this;
        }

        public CellStyleHandler setWrapText(boolean value) {
            this.cellStyle.setWrapText(value);
            return this;
        }
    }

    private static ExcelWriter ONLY_INSTANCE = null;

    public static ExcelWriter getInstance() {
        if (ONLY_INSTANCE == null) {
            ONLY_INSTANCE = new ExcelWriter();
        }
        return ONLY_INSTANCE;
    }

    private ExcelWriter() {
    }

    public short getDataFormat(String df, HSSFWorkbook workbook) {
        return workbook.createDataFormat().getFormat(df);
    }

    public void setDataFormat(HSSFSheet sheet, short id) {
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            HSSFRow row = sheet.getRow(i);
            int f = row.getFirstCellNum();
            HSSFCellStyle s = row.getCell(f).getCellStyle();
            s.setDataFormat(id);
            row.getCell(f).setCellStyle(s);
        }
    }

    /**
     * 寫檔
     */
    public void writeExcel(File file, HSSFWorkbook workbook) throws FileNotFoundException, IOException {
        workbook.write(new FileOutputStream(file));
    }

    /**
     * 寫檔
     */
    public void writeExcel(String filename, HSSFWorkbook workbook) throws FileNotFoundException, IOException {
        workbook.write(new FileOutputStream(filename));
    }
}
