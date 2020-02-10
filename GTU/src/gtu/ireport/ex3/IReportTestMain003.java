package gtu.ireport.ex3;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import gtu.db.DbConstant;
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

public class IReportTestMain003 {

    private Logger logger = Logger.getLogger(getClass());

    public static void main(String[] args) throws ClassNotFoundException, JRException, IOException, SQLException {
        IReportTestMain003 test = new IReportTestMain003();
        test.exceute();
        System.out.println("done...");
    }

    public void exceute() throws JRException, IOException, ClassNotFoundException, SQLException {
        List<Map<String, Object>> colst = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        colst.add(map);

        // 為主報表所使用的外部參數
        Map parameters = new HashMap();
        String imagePathPrefix = "D:/workstuff/gtu-test-code/GTU/src/gtu/ireport/ex3/";
//        parameters.put("pay_detail_uuid", "4028e381702d881c01702d95bb6c0003");
        parameters.put("pay_detail_uuid", "4028e381702d881c01702e0cef2a0006");
        
        parameters.put("scissor", imagePathPrefix + "scissor.jpg");
        // 20150422 tzuching.kao 導入客戶-美吾華,新增LOGO及廣告頁圖檔
        parameters.put("maywufa_ad", imagePathPrefix + "MAYWUFA_AD.jpg");
        parameters.put("maywufa_logo", imagePathPrefix + "MAYWUFA_LOGO.jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Connection conn = null;
        try {
            conn = DbConstant.getTestDataSource_CTBC_jdts().getConnection();

            logger.info("載入主報表設計檔    E5203MasterTemplate.jasper");
            // 載入主報表模板
            // JasperReport jasperReport = (JasperReport)
            // JRLoader.loadObject(this.getClass().getResourceAsStream("BG_B1Z540.jasper"));
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new FileInputStream(imagePathPrefix + "後台公版.jasper"));
            JRDataSource dataSource = new JRMapCollectionDataSource((Collection) colst);

            // ByteArrayInputStream bgImg = new
            // ByteArrayInputStream(IOUtils.toByteArray(this.getClass().getResourceAsStream("BG_B1Z540_2.png")));
            // parameters.put("PATH",
            // "D:\\workstuff\\gtu-test-code\\GTU\\src\\gtu\\ireport\\ex2\\BG_B1Z540_2.png");

            logger.info("開始執行產出報表");
            // JasperPrint jasperPrint =
            // JasperFillManager.fillReport(jasperReport, parameters,
            // dataSource);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

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
        } catch (SQLException e) {
            throw e;
        }
    }
}
