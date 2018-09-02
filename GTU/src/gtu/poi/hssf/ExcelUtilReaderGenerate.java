package gtu.poi.hssf;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import gtu.file.FileUtil;

public class ExcelUtilReaderGenerate {

    public static void main(String[] args) throws Exception {
        File file = new File(FileUtil.DESKTOP_DIR, "活頁簿1.xlsx");
        Workbook wb = ExcelUtil.getInstance().readExcel_xlsx(file);
        System.out.println(ExcelUtilReaderGenerate.newInstance().sheet(wb.getSheetAt(0)).build());;
    }

    private static final String METHOD_BODY;
    private static final String CELL_BODY;

    private Sheet sheet;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("        File file = new File(FileUtil.DESKTOP_DIR, \"\");                                 \n");
        sb.append("        System.out.println(file.exists());                                                                         \n");
        sb.append("        Workbook wb = ExcelUtil.getInstance().readExcel_xlsx(file);                                                \n");
        sb.append("        Sheet sheet = wb.getSheetAt(0);                                                                            \n");
        sb.append("        ExcelUtil.getInstance().debugShowSheetData(sheet);                                                         \n");
        sb.append("                                                                                                                   \n");
        sb.append("        for (int ii = 0; ii <= sheet.getLastRowNum(); ii++) '{'                                                      \n");
        sb.append("            Row row = sheet.getRow(ii);                                                                            \n");
        sb.append("            if(row == null) '{'                                                                                      \n");
        sb.append("                System.out.println(\"row idx : \" + ii + \" is null\");                                                \n");
        sb.append("                continue;                                                                                          \n");
        sb.append("            }                                                                                                      \n");
        sb.append("                                                                                                                   \n");
        sb.append("            {0} \n");
        sb.append("                                                                                                                   \n");
        sb.append("//            for (int jj = 0; jj < row.getLastCellNum(); jj++) '{'                                                  \n");
        sb.append("//            }                                                                                                    \n");
        sb.append("        }                                                                                                          \n");
        METHOD_BODY = sb.toString();

        sb = new StringBuilder();
        sb.append("            String cellVal{0} = ExcelUtil.getInstance().readCell(ExcelUtil.getInstance().getCellChk(row, {0}));        \n");
        CELL_BODY = sb.toString();
    }

    private ExcelUtilReaderGenerate() {
    }

    public static ExcelUtilReaderGenerate newInstance() {
        return new ExcelUtilReaderGenerate();
    }

    public ExcelUtilReaderGenerate sheet(Sheet sheet) {
        this.sheet = sheet;
        return this;
    }

    public String build() {
        try {
            int maxCellLen = 0;
            for (int ii = 0; ii <= sheet.getLastRowNum(); ii++) {
                Row row = sheet.getRow(ii);
                if (row != null) {
                    maxCellLen = Math.max(row.getLastCellNum(), maxCellLen);
                }
            }

            List<String> lst1 = new ArrayList<String>();
            List<String> lst2 = new ArrayList<String>();
            for (int ii = 0; ii < maxCellLen; ii++) {
                lst1.add(MessageFormat.format(CELL_BODY, new Object[] {ii}));
                lst2.add("cellVal" + ii);
            }
            
            String content = StringUtils.join(lst1, "") + //
                    String.format("System.out.println(%s);\n", StringUtils.join(lst2, "+ \"\\t\" +"));
            return MessageFormat.format(METHOD_BODY, content);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
