package gtu.poi.xssf;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelXlsxTest {
    
    public static void main(String[] args) throws IOException{
        ExcelXlsxTest test = new ExcelXlsxTest();
        test.readXlsxFile();
    }
    
    private void readXlsxFile() throws IOException {
        File xlsxFile = new File("C:/Users/gtu001/Desktop/活頁簿1.xlsx");
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(xlsxFile));
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(bis);
        System.out.println("共有多少sheet = " + xssfWorkbook.getNumberOfSheets());
        Sheet sheet = xssfWorkbook.getSheetAt(0);

        for (int j = 0; j < sheet.getPhysicalNumberOfRows(); j++) {
            Row row = sheet.getRow(j);
            System.out.print("row:("+j+")\t");
            for (int k = 0; k < row.getLastCellNum(); k++) {
                System.out.print(""+k+"=["+formatCellType(row.getCell(k))+"]\t");
            }
            System.out.println();
        }
        bis.close();
    }

    private String formatCellType(Cell cell) {
        String returnVal = StringUtils.EMPTY;
        if(cell == null){
            return "";
        }
        if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
            final NumberFormat formatter = new DecimalFormat("##");
            returnVal = formatter.format(Float.valueOf(cell.toString()));
        } else {
            returnVal = cell.toString();
        }
        return returnVal;
    }
}
