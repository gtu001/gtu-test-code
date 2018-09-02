package gtu.poi.xssf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import gtu.file.FileUtil;

@Service
public class SimpleExcelExportService {

    private static final Logger logger = Logger.getLogger(SimpleExcelExportService.class.getName());
    
    //Controller 端 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    @RequestMapping(value = "clicklog_excelExport", method = RequestMethod.POST, produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    @ResponseBody
    public void clicklog_excelExport(HttpServletRequest request, HttpServletResponse response) {
        logger.info("#. clicklog_excelExport start");
        response.reset();
        try {
            // 查詢
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            request.setAttribute("startDate", startDate);
            request.setAttribute("endDate", endDate);
            List<Object> lst = new ArrayList<Object>();

            String excelTmpFolder = FileUtil.TEMP_DIR;
            logger.info("excelTmpFolder:" + excelTmpFolder);
            String excelFileName = generateExcelFileName(excelTmpFolder);

            writeSimpleExcel(lst, _ExampleTestEnum.values(), excelFileName);
            FileInputStream ins = new FileInputStream(excelFileName);
            // 下面兩行要先寫(setContentType, setHeader)，否則檔案名稱可能會隨機變成網址名稱
            // response.setContentType("application/octet-stream");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-disposition", "attachment; filename=\"" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss") + "_CMS_log.xlsx\"");

            OutputStream out = response.getOutputStream();
            IOUtils.copy(ins, out);
            ins.close();

            response.setHeader("Set-Cookie", "fileDownload=true; path=/");

            response.getOutputStream().close();
            response.flushBuffer();

            File excelFile = new File(excelFileName);
            if (excelFile.exists())
                excelFile.delete();
        } catch (Exception ex) {
            logger.error("clicklog_excelExport Export error: " + ex.getMessage(), ex);
        } finally {
            logger.info("#. clicklog_excelExport end");
        }
    }
    //Controller 端 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    
    public String generateExcelFileName(String excelTmpFolder) {
        if (excelTmpFolder == null || "".equals(excelTmpFolder)) {
            logger.error("Invalid excel file name");
            return "";
        }
        return excelTmpFolder + //
                DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss") + //
                generateRandomString(5) + ".xlsx";
    }

    private String generateRandomString(int length) {
        if (length <= 0) {
            return "NONE";
        }
        SecureRandom random = new SecureRandom();
        String randomString = new BigInteger(length * 4, random).toString(32);
        return randomString;
    }

    public interface IExcelDefine {
        int getWidth();

        boolean isFieldMember();

        String getterName();

        String getTitle();
    }

    public enum _ExampleTestEnum implements IExcelDefine {
        XXXX("fieldName", "title", 100, true),//
        ;

        final String fieldName;
        final String title;
        final int width;
        final boolean isField;

        _ExampleTestEnum(String fieldName, String title, int width, boolean isField) {
            this.fieldName = fieldName;
            this.title = title;
            this.width = width;
            this.isField = isField;
        }

        @Override
        public int getWidth() {
            return width;
        }

        @Override
        public boolean isFieldMember() {
            return isField;
        }

        @Override
        public String getterName() {
            return fieldName;
        }

        @Override
        public String getTitle() {
            return title;
        }
    }

    public void writeSimpleExcel(List<?> data, Enum<? extends IExcelDefine>[] enumVal, String excelFileName) {
        FileOutputStream fileOut = null;

        Workbook wb = new XSSFWorkbook();
        try {
            fileOut = new FileOutputStream(excelFileName);
        } catch (FileNotFoundException e) {
            logger.error("File not found: " + e);
        }

        // CreationHelper createHelper = wb.getCreationHelper();
        Sheet mainSheet = wb.createSheet(WorkbookUtil.createSafeSheetName("Sheet1"));

        // 設定cell風格
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setWrapText(true);

        // 印製標頭
        Row rowHeader = mainSheet.createRow(0);
        for (Enum<? extends IExcelDefine> e : enumVal) {
            IExcelDefine e1 = (IExcelDefine) e;
            rowHeader.createCell(e.ordinal()).setCellValue(e1.getTitle());
        }

        // 印製內容
        for (int i = 0; i < data.size(); i++) {
            Row rowData = mainSheet.createRow(i + 1);

            Object v = data.get(i);

            for (Enum<? extends IExcelDefine> e : enumVal) {
                IExcelDefine e1 = (IExcelDefine) e;

                String valueStr = "";
                Object val = null;

                try {
                    if (e1.isFieldMember()) {
                        val = FieldUtils.readDeclaredField(v, e1.getterName(), true);
                    } else {
                        val = MethodUtils.invokeMethod(v, e1.getterName(), new Object[0]);
                    }
                    if (val != null) {
                        valueStr = String.valueOf(val);
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(String.format("cell process ERR : index[%d], ExcelDefine[%s], value[%s]", //
                            i, e, v), ex);
                }

                Cell zero = rowData.createCell(e.ordinal());
                zero.setCellValue(valueStr);
                zero.setCellStyle(cellStyle);
            }
        }

        // 設定寬度
        for (Enum<? extends IExcelDefine> e : enumVal) {
            IExcelDefine e1 = (IExcelDefine) e;
            if (e1.getWidth() > 0) {
                // custom寬度
                mainSheet.setColumnWidth(e.ordinal(), e1.getWidth());
            } else {
                // 自動寬度
                int width = 0;
                mainSheet.autoSizeColumn(e.ordinal());
                width = mainSheet.getColumnWidth(e.ordinal());
                mainSheet.setColumnWidth(e.ordinal(), width);
                logger.info("width[autoSize] : " + e.ordinal() + " width = " + width);
            }
        }

        try {
            wb.write(fileOut);
        } catch (IOException e) {
            logger.error("Excel export error: " + e);
        } finally {
            try {
                if (fileOut != null) {
                    fileOut.close();
                }
                if (wb != null) {
                    wb.close();
                }
            } catch (IOException e) {
                logger.error("Excel export error: " + e);
            }
        }
    }
}
