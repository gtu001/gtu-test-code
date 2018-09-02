package gtu.freemarker.work;

import gtu.file.FileUtil;
import gtu.poi.hssf.ExcelUtil;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class XlsReaderToFtl {

    static String CONTENT;
    static {
        StringBuilder sb = new StringBuilder();
        sb.append("<#import \"import.ftl\" as d>              \n");
        sb.append("<@compress single_line=true>               \n");
        sb.append("                                           \n");
        sb.append("                                           \n");
        sb.append("                                           \n");
        sb.append("%1$s                                       \n");
        sb.append("                                           \n");
        sb.append("                                           \n");
        sb.append("                                           \n");
        sb.append("%2$s                                       \n");
        sb.append("                                           \n");
        sb.append("                                           \n");
        sb.append("</@compress>");
        CONTENT = sb.toString();
    }

    public static void main(String[] args) throws Exception {
        HSSFWorkbook wb = ExcelUtil.getInstance().readExcel(new File("C:/Users/gtu001/Desktop/0171D姓名更正登記_20121113.xls"));
        HSSFSheet sheet = wb.getSheetAt(0);

        File targetDir = new File(FileUtil.DESKTOP_DIR, "test");
        targetDir.mkdirs();

        for (int ii = 0; ii <= sheet.getLastRowNum() + 10; ii++) {
            try {
                HSSFRow row = sheet.getRow(ii);
                HSSFCell cell1_ = row.getCell(1);
                HSSFCell cell2_ = row.getCell(2);
                HSSFCell cell3_ = row.getCell(3);
                String cell1 = cell1_ == null ? "" : ExcelUtil.getInstance().readHSSFCell(cell1_);//檔名
                String cell2 = cell2_ == null ? "" : ExcelUtil.getInstance().readHSSFCell(cell2_);//內容
                String cell3 = cell3_ == null ? "" : ExcelUtil.getInstance().readHSSFCell(cell3_);//註解
                System.out.println(cell1);
                if (StringUtils.isNotBlank(cell1)) {
                    System.out.println(cell1);
                    System.out.println(cell2);
                    System.out.println(cell3);
                    String content = String.format(CONTENT, cell2, cell3);
                    File saveFile = new File(targetDir, cell1 + ".ftl");
                    FileUtil.saveToFile(saveFile, content, "UTF8");
                }
                System.out.println("==========================================================");
                System.out.println();
            } catch (Exception ex) {
                System.err.println("error = " + ii + " , " + sheet.getLastRowNum());
                ex.printStackTrace();
                break;
            }
        }
        System.out.println("done...");
    }
}
