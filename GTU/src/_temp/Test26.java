package _temp;

import gtu.file.FileReaderWriterUtil;
import gtu.file.FileReaderWriterUtil.ReaderZ;
import gtu.file.FileReaderWriterUtil.ReaderZLine;
import gtu.poi.hssf.ExcelUtil;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class Test26 {

    public static void main(String[] args) throws Exception {
        File dFile = new File("C:/Users/gtu001/Desktop/檢查/xxx/新文字文件.txt");

        ReaderZ reader = FileReaderWriterUtil.ReaderZ.newInstance(dFile);
        reader.init();
        final Set<String> allSet = new HashSet<String>();
        reader.read(new ReaderZLine() {
            public boolean readLine(String line) {
                if (StringUtils.isNotBlank(line)) {
                    allSet.add(line.trim());
                }
                return true;
            }
        });

        
        
        File file = new File("C:/Users/gtu001/Desktop/檢查/xxx/SDP-SDK-SIT-testcase_sunny.xls");
        HSSFWorkbook book = ExcelUtil.getInstance().readExcel(file);

        HSSFSheet sheet = book.getSheetAt(2);

        Set<String> set = new HashSet<String>();
        HSSFRow row = null;
        HSSFCell cell = null;
        String value = null;
        for (int ii = 0; ii <= sheet.getLastRowNum(); ii++) {
            row = sheet.getRow(ii);
            if(row != null){
                cell = row.getCell(6);
                if (cell != null) {
                    value = ExcelUtil.getInstance().readHSSFCell(cell);
                } else {
                    value = null;
                }
                if(StringUtils.isNotBlank(value)){
                    set.add(value.trim());
                }
            }
        }
        
        
        Set<String> findNotOkSet = new HashSet<String>();
        for(String s : allSet){
            boolean findOk = false;
            for(String t : set){
                if(s.equalsIgnoreCase(t)){
                    findOk = true;
                    break;
                }
            }
            if(!findOk){
                findNotOkSet.add(s);
            }
        }
        
        for(String s : findNotOkSet){
            System.out.println("notFound = " + s);
        }
    }

}
