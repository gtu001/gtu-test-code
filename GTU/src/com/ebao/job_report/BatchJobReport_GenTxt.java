package com.ebao.job_report;

import gtu.db.DbConstant;
import gtu.db.tradevan.DBMain;
import gtu.file.FileUtil;
import gtu.poi.hssf.ExcelUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


public class BatchJobReport_GenTxt {

    public static void main(String[] args) throws SQLException, IOException {
        String runId = "43806";
        BatchJobReport_GenTxt test = new BatchJobReport_GenTxt();
        test.runIdFetchLogDesc("111", runId, DbConstant.getTestDataSource_R5UAT());

        System.out.println("done...");
    }
    
    private String jobId;
    private String runId;
    String filePath;
    Properties jobModuleProp;
    
    private File getPrefixPath(){
        if(StringUtils.isBlank(filePath)){
            return FileUtil.DESKTOP_DIR;
        }
        return new File(filePath);
    }
    
    public void runIdFetchLogDesc(String jobId, String runId, DataSource dataSource) throws IOException {
        this.jobId = jobId;
        this.runId = runId;
        
        initLoadProp();
        
        DBMain dbMain = getDBMain(dataSource);
        File file = new File(getPrefixPath(), "全球JOB_Report");
        file.mkdirs();
        
        String moduleId = StringUtils.defaultString(jobModuleProp.getProperty(jobId));
        
        File realFileRate = new File(file, moduleId + "_" + jobId + "_" + runId + "_費率.txt");
        File realFileNON = new File(file, moduleId + "_" + jobId + "_" + runId + ".txt");
        
        List<Map<String, Object>> list = dbMain.query("select distinct log_desc  from t_batch_log t where run_id = " + runId + " and log_level= 4 order by 1 ");
        for (Map<String, Object> m : list) {
            String logDesc = (String) m.get("LOG_DESC");
            putLog(logDesc);
        }
        
        Map<String,Set<String>> mapRate = new HashMap<String,Set<String>>();
        Map<String,Set<String>> map2 = new HashMap<String,Set<String>>();
        
        for(String log : LOG_MAP.keySet()){
            if(log.contains("率查無資料")){
                mapRate.put(log, LOG_MAP.get(log));
            }else{
                map2.put(log, LOG_MAP.get(log));
            }
        }
        
        if (!mapRate.isEmpty()) {
            writeFile(realFileRate, mapRate);
        }
        if (!map2.isEmpty()) {
            writeFile(realFileNON, map2);
        }
        
      
        //---------------------------------------------------------------------
        
        File realFileRate2 = new File(file, moduleId + "_" + jobId + "_" + runId + "_費率.xls");
        File realFileNON2 = new File(file, moduleId + "_" + jobId + "_" + runId + ".xls");
        
        if (!mapRate.isEmpty()) {
            writeExcelFile(realFileRate2, mapRate); 
        }
        if (!map2.isEmpty()) {
//            writeExcelFile(realFileNON2, map2); 
        }
    }
    

    private void writeFile(File realFile, Map<String,Set<String>> logMap) throws IOException {
        int ii = 0;
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(realFile)));
        for (String logDesc : logMap.keySet()) {
            Set<String> policyIdSet = LOG_MAP.get(logDesc);
            writer.write(String.format("#index:%d#-(總數:%d)-policyId:%s-------------------------------", ii, policyIdSet.size(), policyIdSet));
            writer.newLine();
            writer.write(logDesc);
            writer.newLine();
            writer.newLine();
            ii++;
        }
        writer.flush();
        writer.close();
        System.out.println(realFile.exists() + " -- " + realFile.getAbsolutePath());
    }
    
    private void writeExcelFile(File realFile, Map<String,Set<String>> logMap) throws IOException {
        try{
            int ii = 0;
            System.gc();
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("ERROR_LOG");
            for (String logDesc : logMap.keySet()) {
                Set<String> policyIdSet = LOG_MAP.get(logDesc);
                String prefix = String.format("#index:%d#-(sum:%d)-policyId:%s-------------------------------", ii, policyIdSet.size(), policyIdSet);
                
                HSSFRow row = sheet.createRow(ii);
                ExcelUtil.getInstance().setCellValue(row.createCell(0), prefix);
                ExcelUtil.getInstance().setCellValue(row.createCell(1), logDesc);
                ii++;
            }
            ExcelUtil.getInstance().writeExcel(realFile, workbook);
            System.out.println(realFile.exists() + " -- " + realFile.getAbsolutePath());
        }catch(Throwable ex){
            ex.printStackTrace();
        }
    }

    private Map<String, Set<String>> LOG_MAP = new HashMap<String, Set<String>>();

    private void putLog(String str) throws IOException {
        String newStr = replaceToStr(str, "\\[\\d+\\]", "##");
        // newStr = replaceToStr(newStr,
        // "to_date\\(\\'[\\d+]\\'\\,\\'yyyymmdd\\'\\)", "##");

        Set<String> set = new HashSet<String>();
        if (LOG_MAP.containsKey(newStr)) {
            set = LOG_MAP.get(newStr);
        } else {
            LOG_MAP.put(newStr, set);
        }
        Pattern ptn = Pattern.compile("\\[\\d+\\]", Pattern.DOTALL | Pattern.MULTILINE);
        Matcher mth = ptn.matcher(str);
        while (mth.find()) {
            set.add(mth.group());
        }
    }

    private static String replaceToStr(String text, String pattern, String toStr) {
        Pattern ptn = Pattern.compile(pattern);
        Matcher mth = ptn.matcher(text);
        StringBuffer sb2 = new StringBuffer();
        while (mth.find()) {
            mth.appendReplacement(sb2, toStr);
        }
        mth.appendTail(sb2);
        return sb2.toString();
    }
    
    private void initLoadProp() throws IOException{
        URL url = this.getClass().getResource("BatchJobReport_JobModule.properties");
        Properties prop = new Properties(); 
        prop.load(url.openStream());
        jobModuleProp = prop;
        for(Enumeration enu = prop.keys();enu.hasMoreElements();){
            String key = (String)enu.nextElement();
            String val = prop.getProperty(key);
//            System.out.println(key + "--" + val);
        }
    }

    private DBMain getDBMain(DataSource dataSource) {
        DBMain dbMain = new DBMain();
        dbMain.setDataSource(dataSource);
        return dbMain;
    }
}
