package gtu.ireport.ex2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import gtu.file.FileUtil;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;

public class IReportTestMain002 {

    private Logger logger = Logger.getLogger(getClass());

    public static void main(String[] args) throws ClassNotFoundException, JRException, IOException {
        IReportTestMain002 test = new IReportTestMain002();
        test.exceute();
        System.out.println("done...");
    }

    public void exceute() throws JRException, IOException, ClassNotFoundException {
        List<Map<String, Object>> colst = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("POLICY_NO", "POLICY_NO");
        map.put("COMP_NAME", "COMP_NAME");
        map.put("INSR_NAME", "INSR_NAME");
        map.put("BIRTH_DATE", "BIRTH_DATE");
        map.put("INSR_ID", "INSR_ID");
        map.put("DIV_NM", "DIV_NM");
        map.put("WRK_TITLE", "WRK_TITLE");
        map.put("EMP_NO", "EMP_NO");
        map.put("MailingAddress", "MailingAddress");
        map.put("INSR_NAME_0", "INSR_NAME_0");
        map.put("INSR_NAME_1", "INSR_NAME_1");
        map.put("INSR_NAME_20", "INSR_NAME_20");
        map.put("INSR_NAME_21", "INSR_NAME_21");
        map.put("BIRTH_DATE_0", "BIRTH_DATE_0");
        map.put("BIRTH_DATE_1", "BIRTH_DATE_1");
        map.put("BIRTH_DATE_20", "BIRTH_DATE_20");
        map.put("BIRTH_DATE_21", "BIRTH_DATE_21");
        map.put("INSR_ID_0", "INSR_ID_0");
        map.put("INSR_ID_1", "INSR_ID_1");
        map.put("INSR_ID_20", "INSR_ID_20");
        map.put("INSR_ID_21", "INSR_ID_21");
        map.put("CLASS_0", "CLASS_0");
        map.put("CLASS_1", "CLASS_1");
        map.put("CLASS_20", "CLASS_20");
        map.put("CLASS_21", "CLASS_21");
        map.put("BARCODE_APLY_NO", "BARCODE_APLY_NO");
        map.put("APLY_NO", "APLY_NO");
        colst.add(map);

        // 為主報表所使用的外部參數
        Map parameters = new HashMap();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            logger.info("載入主報表設計檔    E5203MasterTemplate.jasper");
            // 載入主報表模板
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("BG_B1Z540.jasper"));
            JRDataSource dataSource = new JRMapCollectionDataSource((Collection) colst);

            // ByteArrayInputStream bgImg = new
            // ByteArrayInputStream(IOUtils.toByteArray(this.getClass().getResourceAsStream("BG_B1Z540_2.png")));
            parameters.put("PATH", "D:\\workstuff\\gtu-test-code\\GTU\\src\\gtu\\ireport\\ex2\\BG_B1Z540_2.png");

            logger.info("開始執行產出報表");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            
            JRPdfExporter exporter = new JRPdfExporter();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
            exporter.exportReport();

            byte[] bytes = out.toByteArray();

            FileOutputStream fis = new FileOutputStream(new File(FileUtil.DESKTOP_PATH, "test.pdf"));
            fis.write(bytes);
            fis.flush();
            fis.close();

        } catch (JRException e) {
            throw e;
        } catch (FileNotFoundException e) {
            throw e;
        }
    }
}
