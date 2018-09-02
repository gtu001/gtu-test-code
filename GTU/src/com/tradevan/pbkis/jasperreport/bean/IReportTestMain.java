package com.tradevan.pbkis.jasperreport.bean;

import gtu.file.FileUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.log4j.Logger;

public class IReportTestMain {
    
    private Logger logger = Logger.getLogger(getClass());

    public static void main(String[] args) throws ClassNotFoundException, JRException, IOException {
        IReportTestMain test = new IReportTestMain();
        test.exceute();
        System.out.println("done...");
    }
    
    public void exceute() throws JRException, IOException, ClassNotFoundException{
        List<Ex5203mDo> sourceBeanList = new ArrayList<Ex5203mDo>();
        ObjectInputStream ois = new ObjectInputStream(this.getClass().getResourceAsStream("ex5203_hard.txt"));
        sourceBeanList = (List<Ex5203mDo>) ois.readObject();
        ois.close();

        for (Ex5203mDo mdo : sourceBeanList) {
            mdo.setDetailDataSource(new JRBeanCollectionDataSource(mdo.getList()));
            mdo.setOtherDeclItemDataSource(new JRBeanCollectionDataSource(mdo.getOtherDeclItems()));
        }
        
        //為主報表所使用的外部參數
        Map parameters = new HashMap();
        
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        try {
            
            logger.info("載入主報表設計檔    E5203MasterTemplate.jasper");
            //載入主報表模板
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("E5203MasterTemplate.jasper"));
            //設定使用Jasperreport 的 DataSource
            JRDataSource dataSource = new JRBeanCollectionDataSource(sourceBeanList);
            
            logger.info("載入子報表設計檔 ");
            //載入子報表1模板
            JasperReport subJasperReport1 = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("E5203DetailTemplate.jasper"));
            
            //設定使用Jasperreport 的 DataSource
            parameters.put("Detail_Report_Parameter", subJasperReport1);
            
            logger.info("載入子報表2設計檔 ");
            //載入子報表3模板
            JasperReport subJasperReport2 = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("OtherDeclItemTemplate.jasper"));
                
            //設定使用Jasperreport 的 DataSource
            parameters.put("Other_Decl_Item_Report_Parameter", subJasperReport2);
            
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
