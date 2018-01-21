package gtu.poi.hssf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;

public class ExcelUtil {

    public static void main(String[] args) {
        System.out.println(ExcelUtil.cellEnglishToPos(3047));
    }

    private ExcelUtil() {
    }

    public Date getDate(double dateValue) {
        return HSSFDateUtil.getJavaDate(dateValue);
    }

    public String getDate(HSSFCell cell, String pattern) {
        try {
            return DateFormatUtils.format(getDate(Double.parseDouble(ExcelUtil.getInstance().readHSSFCell(cell))), pattern);
        } catch (Exception ex) {
            System.out.println("cell日期轉型錯誤:" + ex.getMessage());
            return "";
        }
    }

    public static ExcelUtil getInstance() {
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
     * 用英文欄位取得相對row的index
     */
    public static String cellEnglishToPos(int column) {
        Transformer c1 = new Transformer() {
            @Override
            public Object transform(Object input) {
                int v = Integer.parseInt(String.valueOf(input));
                for (int ii = 0;; ii++) {
                    if (Math.pow(26, ii) > v) {
                        return ii - 1;
                    }
                }
            }
        };
        Map<Integer, Integer> m2 = new TreeMap<Integer,Integer>();
        while(true) {
            int exponent = (Integer)c1.transform(column);
            column -= Math.pow(26, exponent);
            
            int value = 0;
            if(m2.containsKey(exponent)) {
                value = m2.get(exponent);
            }
            value ++;
            m2.put(exponent, value);
            
            if(exponent == 0) {
                m2.put(exponent, column + 1);
                break;
            }
        }
        StringBuilder sb = new StringBuilder();
        for(int k : m2.keySet()) {
            char c = (char)(m2.get(k) + 64);
            sb.insert(0, c);
        }
        return sb.toString();
    }

    public HSSFRow getRowChk(HSSFSheet sheet, int rowPos) {
        HSSFRow row = sheet.getRow(rowPos);
        if (row == null) {
            row = sheet.createRow(rowPos);
        }
        return row;
    }

    public HSSFCell getCellChk(HSSFRow row, int colPos) {
        HSSFCell cell = row.getCell(colPos);
        if (cell == null) {
            cell = row.createCell(colPos);
        }
        return cell;
    }

    /**
     * 讀取檔案成HSSFWorkbook物件
     */
    public HSSFWorkbook readExcel(File file) throws Exception {
        int size = (int) (file.length() - file.length() % 512);
        byte[] buffer = new byte[size];
        InputStream inputFile = new FileInputStream(file);
        inputFile.read(buffer, 0, size);
        inputFile.close();
        InputStream byteIS = new ByteArrayInputStream(buffer);
        byteIS.close();
        return new HSSFWorkbook(byteIS);
    }

    /**
     * 讀取檔案成HSSFWorkbook物件
     */
    public HSSFWorkbook readExcel2(File file) throws Exception {
        InputStream inputFile = new FileInputStream(file);

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
    }

    /**
     * 寫檔
     */
    public void writeExcel(File file, HSSFWorkbook workbook) throws FileNotFoundException, IOException {
        if (workbook.getNumberOfSheets() == 0) {
            workbook.createSheet();
        }
        workbook.write(new FileOutputStream(file));
    }

    /**
     * 寫檔
     */
    public void writeExcel(String filename, HSSFWorkbook workbook) throws FileNotFoundException, IOException {
        if (workbook.getNumberOfSheets() == 0) {
            workbook.createSheet();
        }
        workbook.write(new FileOutputStream(filename));
    }

    /**
     * 工作表sheet
     * 
     * @param workbook
     * @param columnslist
     * @return
     */
    public List<List<Map<String, String>>> readHSSFWorkbook(HSSFWorkbook workbook, List<String[]> columnslist) {
        List<List<Map<String, String>>> rtnlist = new ArrayList<List<Map<String, String>>>();
        List<Map<String, String>> append = null;
        for (int ii = 0; ii < workbook.getNumberOfSheets(); ii++) {
            append = null;
            try {
                append = this.readHSSFSheet(workbook.getSheetAt(ii), columnslist.get(ii));
            } catch (Exception ex) {
                append = this.readHSSFSheet(workbook.getSheetAt(ii), null);
            }
            rtnlist.add(append);
        }
        return rtnlist;
    }

    /**
     * @param sheet
     *            HSSFSheet --> List
     * @param columns
     * @return
     */
    public List<Map<String, String>> readHSSFSheet(HSSFSheet sheet, String[] columns) {
        List<Map<String, String>> rtnlist = new ArrayList<Map<String, String>>();
        for (int ii = 0; ii <= sheet.getLastRowNum(); ii++) {
            rtnlist.add(this.readHSSFRow(sheet.getRow(ii), columns));
        }
        return rtnlist;
    }

    /**
     * @param row
     *            HSSFRow --> Map
     * @param columns
     * @return
     */
    public Map<String, String> readHSSFRow(HSSFRow row, String[] columns) {
        Map<String, String> rowMap = new LinkedHashMap<String, String>();
        for (int ii = 0; ii < row.getLastCellNum(); ii++) {
            HSSFCell cell = row.getCell(ii);
            if (columns != null) {
                for (String col : columns) {
                    rowMap.put(col, this.readHSSFCell(cell));
                }
            } else {
                rowMap.put(String.valueOf(ii), this.readHSSFCell(cell));
            }
        }
        return rowMap;
    }

    public String readHSSFCell(HSSFRow row, String cellStr) {
        int pos = cellEnglishToPos(cellStr);
        if (row == null) {
            return "";
        }
        HSSFCell cell = row.getCell(pos);
        return readHSSFCell(cell);
    }

    /**
     * @param cell
     *            HSSFCell-->String
     * @return
     */
    public String readHSSFCell(HSSFCell cell) {
        if (cell == null) {
            System.out.println("cell 為 null");
            return "";
        }
        final DecimalFormat df = new DecimalFormat("####################0.##########");
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        switch (cell.getCellType()) {
        case HSSFCell.CELL_TYPE_BLANK:
            return "";
        case HSSFCell.CELL_TYPE_BOOLEAN:
            return Boolean.valueOf(cell.getBooleanCellValue()).toString().trim();
        case HSSFCell.CELL_TYPE_NUMERIC:
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                return sdf.format(cell.getDateCellValue());
            }
            return df.format(cell.getNumericCellValue());
        case HSSFCell.CELL_TYPE_STRING:
            return cell.getRichStringCellValue().getString().trim();
        case HSSFCell.CELL_TYPE_FORMULA:
            return cell.getCellFormula();
        case HSSFCell.CELL_TYPE_ERROR:
            return Byte.toString(cell.getErrorCellValue());
        default:
            return "##POI## Unknown cell type";
        }
    }

    /**
     * 取得儲存格內容(多判斷日期欄位)
     */
    public Object readHSSFCell2(HSSFCell cell) {
        if (cell == null) {
            System.out.println("cell 為 null");
            return "";
        }
        switch (cell.getCellType()) {
        case HSSFCell.CELL_TYPE_BLANK:
            return "";
        case HSSFCell.CELL_TYPE_BOOLEAN:
            return Boolean.valueOf(cell.getBooleanCellValue()).toString();
        case HSSFCell.CELL_TYPE_NUMERIC:
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue();
            }
            return cell.getNumericCellValue();
        case HSSFCell.CELL_TYPE_STRING:
            return cell.getRichStringCellValue().getString();
        case HSSFCell.CELL_TYPE_FORMULA:
            return cell.getCellFormula();
        case HSSFCell.CELL_TYPE_ERROR:
            return Byte.toString(cell.getErrorCellValue());
        default:
            return "##POI## Unknown cell type";
        }
    }

    /**
     * 設定儲存格內容
     */
    public void setCellValue(HSSFCell cell, Object value) {
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
    public void setSheetWidth(HSSFSheet sheet, short[] widths) {
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

    public void setContent(HSSFSheet sheet, List<?> contentList, String[] columns) {
        HSSFRow hssfRow = null;
        for (int ii = 0; ii < contentList.size(); ii++) {
            Object innerObj = contentList.get(ii);
            hssfRow = sheet.createRow(sheet.getLastRowNum() + 1);
            if (innerObj instanceof Map) {
                this.setRowByMap(hssfRow, (Map) innerObj, columns);
            } else if (innerObj instanceof List) {
                this.setRowByList(hssfRow, (List) innerObj);
            }
        }
    }

    public void setRowByMap(HSSFRow hssfRow, Map innerMap, String[] columns) {
        HSSFCell cell = null;
        for (int ii = 0; ii < columns.length; ii++) {
            Integer cellNum = (int) hssfRow.getLastCellNum();
            if (cellNum < 0) {
                cellNum = 0;
            }
            cell = hssfRow.createCell(cellNum);
            Object value = innerMap.get(columns[ii]);
            this.setCellValue(cell, value);
        }
    }

    public void setRowByList(HSSFRow hssfRow, List innerlist) {
        HSSFCell cell = null;
        for (int ii = 0; ii < innerlist.size(); ii++) {
            Integer cellNum = (int) hssfRow.getLastCellNum();
            if (cellNum < 0) {
                cellNum = 0;
            }
            cell = hssfRow.createCell(cellNum);
            Object value = innerlist.get(ii);
            this.setCellValue(cell, value);
        }
    }

    // #############################################################################################

    /**
     * 設定matrix style
     */
    public void setCellStyle(HSSFSheet sheet, int colStart, int rowStart, int colEnd, int rowEnd, HSSFCellStyle style) {
        for (int nowX = colStart; nowX <= colEnd; nowX++) {
            for (int nowY = rowStart; nowY <= rowEnd; nowY++) {
                try {
                    HSSFRow row = sheet.getRow(nowX);
                    HSSFCell cell = row.getCell(nowY);
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

    public void debugShowSheetData(HSSFSheet sheet) {
        HSSFRow row = null;
        HSSFCell cell = null;
        String value = null;
        for (int ii = 0; ii <= sheet.getLastRowNum(); ii++) {
            row = sheet.getRow(ii);
            System.out.format("row:%d", ii);
            for (int jj = 0; jj < row.getLastCellNum(); jj++) {
                cell = row.getCell(jj);
                if (cell != null) {
                    value = ExcelUtil.getInstance().readHSSFCell(cell);
                } else {
                    value = null;
                }
                System.out.format("\t%d[%s]", jj, value);
            }
            System.out.println();
        }
    }

    public static void copyRow(HSSFSheet worksheet, int sourceRowNum, int destinationRowNum) {
        // Get the source & new row
        HSSFRow newRow = worksheet.getRow(destinationRowNum);
        HSSFRow sourceRow = worksheet.getRow(sourceRowNum);

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
            HSSFCell oldCell = sourceRow.getCell(i);
            HSSFCell newCell = newRow.createCell(i);

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

    private static final ExcelUtil INSTANCE = new ExcelUtil();
}
