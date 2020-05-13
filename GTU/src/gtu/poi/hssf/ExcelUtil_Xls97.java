package gtu.poi.hssf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

public class ExcelUtil_Xls97 {

    public static void main(String[] args) {
        for (int ii = 0; ii < 200; ii++) {
            System.out.println(ExcelUtil_Xls97.cellEnglishToPos(ii + 1));
        }
    }

    private ExcelUtil_Xls97() {
    }

    public Date getDate(double dateValue) {
        return HSSFDateUtil.getJavaDate(dateValue);
    }

    public String getDate(Cell cell, String pattern) {
        try {
            return DateFormatUtils.format(getDate(Double.parseDouble(ExcelUtil_Xls97.getInstance().readCell(cell))), pattern);
        } catch (Exception ex) {
            System.out.println("cell日期轉型錯誤:" + ex.getMessage());
            return "";
        }
    }

    public static ExcelUtil_Xls97 getInstance() {
        return INSTANCE;
    }

    /**
     * 用英文欄位取得相對row的index
     */
    public static int cellEnglishToPos(String column) {
        column = column.toUpperCase().trim();
        int len = column.length();
        char[] cc = column.toCharArray();
        int total = 0;
        for (int ii = 0; ii < cc.length; ii++) {
            total += ((int) cc[ii] - 64) * (int) Math.pow(26, len - ii - 1);
        }
        total--;
        return total;
    }

    /**
     * 用英文欄位取得相對row的index (從1開始)
     */
    public static String cellEnglishToPos(int columnIndex) {
        Map<Integer, Integer> m2 = new TreeMap<Integer, Integer>();
        int tmpColumn = columnIndex;
        while (true) {
            int exponent = 0;
            for (int i = 1; i < 1000; i++) {
                if (Math.pow(26, i) >= tmpColumn) {
                    exponent = i - 1;
                    break;
                }
            }
            tmpColumn = tmpColumn - (int) Math.pow(26, exponent);
            int value = 0;

            if (m2.containsKey(exponent)) {
                value = m2.get(exponent);
            }
            value += 1;
            m2.put(exponent, value);

            if (exponent <= 0) {
                m2.put(0, tmpColumn + 1);
                break;
            }
        }
        String rtnVal = "";
        List<Integer> keyLst = new ArrayList<Integer>(m2.keySet());
        Collections.sort(keyLst);
        for (int k : keyLst) {
            rtnVal = (char) (m2.get(k) + 64) + rtnVal;
        }
        return rtnVal;
    }

    public Row getRowChk(Sheet sheet, int rowPos) {
        Row row = sheet.getRow(rowPos);
        if (row == null) {
            row = sheet.createRow(rowPos);
        }
        return row;
    }

    public Cell getCellChk(Row row, int colPos) {
        Cell cell = row.getCell(colPos);
        if (cell == null) {
            cell = row.createCell(colPos);
        }
        return cell;
    }

    /**
     * 讀取檔案成Workbook物件
     */
    public HSSFWorkbook readExcel(File file) {
        InputStream inputFile = null;
        try {
            int size = (int) (file.length() - file.length() % 512);
            byte[] buffer = new byte[size];
            inputFile = new FileInputStream(file);
            inputFile.read(buffer, 0, size);
            inputFile.close();
            InputStream byteIS = new ByteArrayInputStream(buffer);
            byteIS.close();
            return new HSSFWorkbook(byteIS);
        } catch (Exception ex) {
            throw new RuntimeException("readExcel ERR : " + ex.getMessage(), ex);
        } finally {
            try {
                inputFile.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 讀取檔案成Workbook物件
     */
    public HSSFWorkbook readExcel2(File file) {
        InputStream inputFile = null;
        try {
            inputFile = new FileInputStream(file);

            // read entire stream into byte array:
            ByteArrayOutputStream byteOS = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int count;
            while ((count = inputFile.read(buffer)) != -1)
                byteOS.write(buffer, 0, count);
            byteOS.close();
            byte[] allBytes = byteOS.toByteArray();

            // create workbook from array:
            InputStream byteIS = new ByteArrayInputStream(allBytes);
            HSSFWorkbook workBook = new HSSFWorkbook(byteIS);
            return workBook;
        } catch (Exception ex) {
            throw new RuntimeException("readExcel2 ERR : " + ex.getMessage(), ex);
        } finally {
            try {
                inputFile.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 讀取檔案成Workbook物件
     */
    public HSSFWorkbook readExcel(InputStream inputStream) {
        try {
            return new HSSFWorkbook(inputStream);
        } catch (Exception ex) {
            throw new RuntimeException("readExcel ERR : " + ex.getMessage(), ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 讀取檔案成Workbook物件
     */
    public HSSFWorkbook readExcel2(InputStream inputStream) {
        try {
            // read entire stream into byte array:
            ByteArrayOutputStream byteOS = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int count;
            while ((count = inputStream.read(buffer)) != -1)
                byteOS.write(buffer, 0, count);
            byteOS.close();
            byte[] allBytes = byteOS.toByteArray();

            // create workbook from array:
            InputStream byteIS = new ByteArrayInputStream(allBytes);
            HSSFWorkbook workBook = new HSSFWorkbook(byteIS);
            return workBook;
        } catch (Exception ex) {
            throw new RuntimeException("readExcel2 ERR : " + ex.getMessage(), ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 寫檔
     */
    public void writeExcel(File file, Workbook workbook) {
        FileOutputStream out = null;
        try {
            if (workbook.getNumberOfSheets() == 0) {
                workbook.createSheet();
            }
            out = new FileOutputStream(file);
            workbook.write(out);
        } catch (Exception ex) {
            throw new RuntimeException("writeExcel ERR : " + ex.getMessage(), ex);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 寫檔
     */
    public void writeExcel(String filename, Workbook workbook) {
        FileOutputStream out = null;
        try {
            if (workbook.getNumberOfSheets() == 0) {
                workbook.createSheet();
            }
            out = new FileOutputStream(filename);
            workbook.write(out);
        } catch (Exception ex) {
            throw new RuntimeException("writeExcel ERR : " + ex.getMessage(), ex);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 工作表sheet
     * 
     * @param workbook
     * @param columnslist
     * @return
     */
    public List<List<Map<String, String>>> readWorkbook(Workbook workbook, List<String[]> columnslist) {
        List<List<Map<String, String>>> rtnlist = new ArrayList<List<Map<String, String>>>();
        List<Map<String, String>> append = null;
        for (int ii = 0; ii < workbook.getNumberOfSheets(); ii++) {
            append = null;
            try {
                append = this.readSheet(workbook.getSheetAt(ii), columnslist.get(ii));
            } catch (Exception ex) {
                append = this.readSheet(workbook.getSheetAt(ii), null);
            }
            rtnlist.add(append);
        }
        return rtnlist;
    }

    /**
     * @param sheet
     *            Sheet --> List
     * @param columns
     * @return
     */
    public List<Map<String, String>> readSheet(Sheet sheet, String[] columns) {
        List<Map<String, String>> rtnlist = new ArrayList<Map<String, String>>();
        for (int ii = 0; ii <= sheet.getLastRowNum(); ii++) {
            rtnlist.add(this.readRow(sheet.getRow(ii), columns));
        }
        return rtnlist;
    }

    /**
     * @param row
     *            Row --> Map
     * @param columns
     * @return
     */
    public Map<String, String> readRow(Row row, String[] columns) {
        Map<String, String> rowMap = new LinkedHashMap<String, String>();
        for (int ii = 0; ii < row.getLastCellNum(); ii++) {
            Cell cell = row.getCell(ii);
            if (columns != null) {
                for (String col : columns) {
                    rowMap.put(col, this.readCell(cell));
                }
            } else {
                rowMap.put(String.valueOf(ii), this.readCell(cell));
            }
        }
        return rowMap;
    }

    public String readCell(Row row, int cellPos) {
        if (row == null) {
            return "";
        }
        Cell cell = row.getCell(cellPos);
        return readCell(cell);
    }

    public String readCell(Row row, String cellStr) {
        int pos = cellEnglishToPos(cellStr);
        if (row == null) {
            return "";
        }
        Cell cell = row.getCell(pos);
        return readCell(cell);
    }

    /**
     * @param cell
     *            Cell-->String
     * @return
     */
    public String readCell(Cell cell) {
        if (cell == null) {
            System.out.println("cell 為 null");
            return "";
        }
        final DecimalFormat df = new DecimalFormat("####################0.##########");
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        switch (cell.getCellType()) {
        case Cell.CELL_TYPE_BLANK:
            return "";
        case Cell.CELL_TYPE_BOOLEAN:
            return Boolean.valueOf(cell.getBooleanCellValue()).toString().trim();
        case Cell.CELL_TYPE_NUMERIC:
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                return sdf.format(cell.getDateCellValue());
            }
            return df.format(cell.getNumericCellValue());
        case Cell.CELL_TYPE_STRING:
            return cell.getRichStringCellValue().getString().trim();
        case Cell.CELL_TYPE_FORMULA:
            return cell.getCellFormula();
        case Cell.CELL_TYPE_ERROR:
            return Byte.toString(cell.getErrorCellValue());
        default:
            return "##POI## Unknown cell type";
        }
    }

    /**
     * 取得儲存格內容(多判斷日期欄位)
     */
    public Object readCell2(Cell cell) {
        if (cell == null) {
            System.out.println("cell 為 null");
            return "";
        }
        switch (cell.getCellType()) {
        case Cell.CELL_TYPE_BLANK:
            return "";
        case Cell.CELL_TYPE_BOOLEAN:
            return Boolean.valueOf(cell.getBooleanCellValue()).toString();
        case Cell.CELL_TYPE_NUMERIC:
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue();
            }
            return cell.getNumericCellValue();
        case Cell.CELL_TYPE_STRING:
            return cell.getRichStringCellValue().getString();
        case Cell.CELL_TYPE_FORMULA:
            return cell.getCellFormula();
        case Cell.CELL_TYPE_ERROR:
            return Byte.toString(cell.getErrorCellValue());
        default:
            return "##POI## Unknown cell type";
        }
    }

    /**
     * 設日期直靠這個
     */
    public static class DateValueSetter {
        private CellStyle cellStyle;

        public static DateValueSetter of(String formatPattern, Workbook wb) {
            return new DateValueSetter(formatPattern, wb);
        }

        private DateValueSetter(String formatPattern, Workbook wb) {
            cellStyle = wb.createCellStyle();
            CreationHelper createHelper = wb.getCreationHelper();
            cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(formatPattern));
        }

        public void set(Cell cell, Date date) {
            cell.setCellValue(date);
            cell.setCellStyle(cellStyle);
        }
    }

    /**
     * 設定儲存格內容
     */
    public void setCellValue(Cell cell, Object value) {
        if (value instanceof Number) {
            Number n = (Number) value;
            cell.setCellValue(n.doubleValue());
        } else if (value instanceof String) {
            cell.setCellValue(new HSSFRichTextString(getLimitString32767((String) value)));
        } else if (value instanceof IFormula) {
            cell.setCellFormula(((IFormula) value).getFormula());
        } else if (value == null) {
            cell.setCellValue(new HSSFRichTextString(""));
        } else {
            cell.setCellValue(new HSSFRichTextString(getLimitString32767(value.toString())));
        }
    }

    private String getLimitString32767(String val) {
        try {
            byte[] bt = val.getBytes("utf8");
            byte[] newBt = new byte[32767];
            int bound = Math.min(newBt.length, bt.length);
            System.arraycopy(bt, 0, newBt, 0, bound);
            String val2 = new String(newBt, 0, bound, "utf8");
            return val2;
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 設定column寬度
     */
    public void setSheetWidth(Sheet sheet, short[] widths) {
        for (int i = 0; i < widths.length; i++) {
            sheet.setColumnWidth(i, widths[i]);
        }
    }

    /**
     * 定義IFormula
     */
    public interface IFormula {
        public String getFormula();
    }

    // #############################################################################################

    public void setContent(Sheet sheet, List<?> contentList, String[] columns) {
        Row Row = null;
        for (int ii = 0; ii < contentList.size(); ii++) {
            Object innerObj = contentList.get(ii);
            Row = sheet.createRow(sheet.getLastRowNum() + 1);
            if (innerObj instanceof Map) {
                this.setRowByMap(Row, (Map) innerObj, columns);
            } else if (innerObj instanceof List) {
                this.setRowByList(Row, (List) innerObj);
            }
        }
    }

    public void setRowByMap(Row Row, Map innerMap, String[] columns) {
        Cell cell = null;
        for (int ii = 0; ii < columns.length; ii++) {
            Integer cellNum = (int) Row.getLastCellNum();
            if (cellNum < 0) {
                cellNum = 0;
            }
            cell = Row.createCell(cellNum);
            Object value = innerMap.get(columns[ii]);
            this.setCellValue(cell, value);
        }
    }

    public void setRowByList(Row Row, List innerlist) {
        Cell cell = null;
        for (int ii = 0; ii < innerlist.size(); ii++) {
            Integer cellNum = (int) Row.getLastCellNum();
            if (cellNum < 0) {
                cellNum = 0;
            }
            cell = Row.createCell(cellNum);
            Object value = innerlist.get(ii);
            this.setCellValue(cell, value);
        }
    }

    // #############################################################################################

    /**
     * 設定matrix style
     */
    public void setCellStyle(Sheet sheet, int colStart, int rowStart, int colEnd, int rowEnd, CellStyle style) {
        for (int nowX = colStart; nowX <= colEnd; nowX++) {
            for (int nowY = rowStart; nowY <= rowEnd; nowY++) {
                try {
                    Row row = sheet.getRow(nowX);
                    Cell cell = row.getCell(nowY);
                    if (null == cell) {
                        cell = row.createCell(nowY);
                    }
                    cell.setCellStyle(style);
                } catch (Exception ex) {
                    System.err.println("Error : " + ex.getMessage() + " x:" + nowX + " y:" + nowY);
                    ex.printStackTrace();
                }
            }
        }
    }

    public short getDataFormat(String df, Workbook workbook) {
        return workbook.createDataFormat().getFormat(df);
    }

    public void setDataFormat(Sheet sheet, short id) {
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            int f = row.getFirstCellNum();
            CellStyle s = row.getCell(f).getCellStyle();
            s.setDataFormat(id);
            row.getCell(f).setCellStyle(s);
        }
    }

    public void debugShowSheetData(Sheet sheet) {
        Row row = null;
        Cell cell = null;
        String value = null;
        for (int ii = 0; ii <= sheet.getLastRowNum(); ii++) {
            row = sheet.getRow(ii);
            System.out.format("row:%d", ii);
            if (row == null) {
                System.out.println();
                continue;
            }
            for (int jj = 0; jj < row.getLastCellNum(); jj++) {
                cell = row.getCell(jj);
                if (cell != null) {
                    value = ExcelUtil_Xls97.getInstance().readCell(cell);
                } else {
                    value = null;
                }
                System.out.format("\t%d[%s]", jj, value);
            }
            System.out.println();
        }
    }

    public void debugShowSheetData__________BLANK_4_TEMPLATE(Sheet sheet) {
        for (int ii = 0; ii <= sheet.getLastRowNum(); ii++) {
            Row row = sheet.getRow(ii);
            if (row == null) {
                continue;
            }
            for (int jj = 0; jj < row.getLastCellNum(); jj++) {
                String value = ExcelUtil_Xls97.getInstance().readCell(row.getCell(jj));
            }
        }
    }

    public static void copyRow(Sheet worksheet, int sourceRowNum, int destinationRowNum) {
        // Get the source & new row
        Row newRow = worksheet.getRow(destinationRowNum);
        Row sourceRow = worksheet.getRow(sourceRowNum);

        // If the row exist in destination, push down all rows by 1 else create
        // a new row
        if (newRow != null) {
            worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
        } else {
            newRow = worksheet.createRow(destinationRowNum);
        }

        newRow.setHeight(sourceRow.getHeight());
        newRow.setHeightInPoints(sourceRow.getHeightInPoints());

        // Loop through source columns to add to new row
        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
            // Grab a copy of the old/new cell
            Cell oldCell = sourceRow.getCell(i);
            Cell newCell = newRow.createCell(i);

            // If the old cell is null jump to next cell
            if (oldCell == null) {
                newCell = null;
                continue;
            }

            // Use old cell style
            newCell.setCellStyle(oldCell.getCellStyle());

            // If there is a cell comment, copy
            if (newCell.getCellComment() != null) {
                newCell.setCellComment(oldCell.getCellComment());
            }

            // If there is a cell hyperlink, copy
            if (oldCell.getHyperlink() != null) {
                newCell.setHyperlink(oldCell.getHyperlink());
            }

            // Set the cell data type
            newCell.setCellType(oldCell.getCellType());

            // Set the cell data value
            switch (oldCell.getCellType()) {
            case Cell.CELL_TYPE_BLANK:
                newCell.setCellValue(oldCell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                newCell.setCellValue(oldCell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_ERROR:
                newCell.setCellErrorValue(oldCell.getErrorCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA:
                newCell.setCellFormula(oldCell.getCellFormula());
                break;
            case Cell.CELL_TYPE_NUMERIC:
                newCell.setCellValue(oldCell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_STRING:
                newCell.setCellValue(oldCell.getRichStringCellValue());
                break;
            }
        }

        // If there are are any merged regions in the source row, copy to new
        // row
        for (int i = 0; i < worksheet.getNumMergedRegions(); i++) {
            CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(i);
            if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
                CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(), (newRow.getRowNum() + (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow())),
                        cellRangeAddress.getFirstColumn(), cellRangeAddress.getLastColumn());
                worksheet.addMergedRegion(newCellRangeAddress);
            }
        }
    }

    /**
     * sheet.addMergedRegion(CellRangeAddress.valueOf("B2:D5"));
     */
    public void mergeCell(Sheet sheet, String rangeStr) {
        sheet.addMergedRegion(CellRangeAddress.valueOf(rangeStr));
    }

    public void autoCellSize(Sheet sheet) {
        for (int jj = 0; jj < sheet.getRow(0).getLastCellNum(); jj++) {
            sheet.autoSizeColumn(jj);
        }
    }

    private static final ExcelUtil_Xls97 INSTANCE = new ExcelUtil_Xls97();
}
