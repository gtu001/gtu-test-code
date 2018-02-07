package _temp;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import gtu.file.FileUtil;
import gtu.poi.hssf.ExcelUtil;

public class Test39 {

    public static void main(String[] args) throws Exception {
        Test39 t = new Test39();
        
        File excelFile = new File("C:/Users/gtu00/OneDrive/Desktop/張純毓__上海專案_上班+加班時數統計表_20180207.xlsx");
        
        ExcelUtil util = ExcelUtil.getInstance();
        
        Workbook wb = util.readExcel_xlsx(excelFile);
        
        Sheet sheet = wb.getSheetAt(0);
        
        Map<String, List<String>> map = new TreeMap<String, List<String>>();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd");
        
        for (int ii = 0; ii <= sheet.getLastRowNum(); ii++) {
            Row row = sheet.getRow(ii);
            
            if(row == null) {
                continue;
            }
            
            String dateStr = ExcelUtil.getInstance().readCell(ExcelUtil.getInstance().getCellChk(row, 0));
            String workingStuff = ExcelUtil.getInstance().readCell(ExcelUtil.getInstance().getCellChk(row, 1));
            
            try {
                Date d = sdf.parse(dateStr);
                dateStr = sdf2.format(d);
            }catch(Exception ex) {
                System.err.println(ex.getMessage());
            }
            
            List<String> lst = new ArrayList<String>();
            if(map.containsKey(dateStr)) {
                lst = map.get(dateStr);
            }
            map.put(dateStr, lst);
            lst.add(workingStuff);
        }
        
        XSSFWorkbook wb2 = new XSSFWorkbook();
        Sheet sheet2 = wb2.createSheet();
        
        int rowIdx = 0 ;
        for(String dateStr : map.keySet()) {
            Row row = sheet2.createRow(rowIdx);
            
            String workingStuff = StringUtils.join(map.get(dateStr), " , ");
            
            row.createCell(0).setCellValue(dateStr);
            row.createCell(1).setCellValue("9:00-18:00");
            row.createCell(2).setCellValue("張純毓");
            row.createCell(3).setCellValue(workingStuff);
            
            rowIdx ++;
        }

        File writeFile = new File(FileUtil.DESKTOP_PATH, "test2.xlsx");
        ExcelUtil.getInstance().writeExcel(writeFile, wb2);
        
        System.out.println("done...");
    }
}
