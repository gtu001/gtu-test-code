package com.ebao.job_report;

import gtu.db.DbConstant;
import gtu.db.tradevan.DBMain;
import gtu.file.FileUtil;
import gtu.poi.hssf.ExcelUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class BatchJobReport_GenXlsReport {

    public static void main(String[] args) throws SQLException, IOException {
        BatchJobReport_GenXlsReport test = new BatchJobReport_GenXlsReport();

        List<String> list = new ArrayList<String>();
        list.add("44483");
        list.add("44484");
        list.add("44267");
        list.add("44263");
        list.add("44257");
        list.add("44266");
        list.add("44317");
        list.add("44402");
        list.add("44303");
        list.add("39593");
        list.add("44307");
        list.add("44308");
        list.add("44286");
        list.add("44223");
        list.add("44224");
        list.add("44210");
        list.add("44212");
        list.add("44213");
        list.add("44217");
        list.add("44218");
        list.add("44221");
        list.add("44222");
        list.add("44215");
        list.add("44216");
        list.add("44219");
        list.add("44220");
        list.add("44441");
        list.add("44442");
        list.add("44443");
        list.add("44444");
        list.add("44395");
        list.add("44396");
        list.add("44391");
        list.add("44450");
        list.add("44445");
        list.add("44449");
        list.add("44447");
        list.add("44448");
        list.add("44446");
        list.add("44503");
        list.add("44504");
        list.add("44355");
        list.add("44285");
        list.add("44310");
        list.add("44514");
        list.add("39594");
        list.add("44430");
        list.add("44388");
        list.add("44353");
        list.add("44262");
        list.add("44270");
        list.add("44496");
        list.add("44209");
        list.add("44392");
        list.add("44331");
        list.add("44235");
        list.add("44493");
        list.add("44492");
        list.add("44356");
        list.add("44272");
        list.add("44472");
        list.add("44293");
        list.add("44309");
        list.add("44245");
        list.add("44225");
        list.add("44311");
        list.add("44312");
        list.add("44313");
        list.add("44289");
        list.add("44269");
        list.add("44314");
        list.add("44315");
        list.add("44264");
        list.add("44242");
        list.add("44265");
        list.add("44318");
        list.add("44288");
        list.add("44316");
        list.add("44403");
        list.add("44302");
        list.add("44408");
        list.add("44409");
        list.add("39590");
        list.add("39591");
        list.add("39592");
        list.add("44306");
        list.add("44275");
        list.add("44276");
        list.add("44282");
        list.add("44283");
        list.add("44323");
        list.add("44241");
        list.add("44277");
        list.add("44278");
        list.add("44350");
        list.add("44254");
        list.add("44255");
        list.add("44294");
        list.add("44428");
        list.add("44301");
        list.add("44296");
        list.add("44297");
        list.add("44346");
        list.add("44279");
        list.add("44427");
        list.add("44232");
        list.add("44226");
        list.add("44233");
        list.add("44227");
        list.add("39595");
        list.add("44274");
        list.add("44342");
        list.add("44429");
        list.add("44292");
        list.add("44349");
        list.add("44252");
        list.add("44253");
        list.add("44400");
        list.add("44247");
        list.add("44246");
        list.add("44414");
        list.add("44413");
        list.add("44239");
        list.add("44240");
        list.add("44249");
        list.add("44248");
        list.add("44354");
        list.add("44343");
        list.add("44300");
        list.add("44287");
        list.add("44298");
        list.add("44299");
        list.add("44457");
        list.add("44458");
        list.add("44459");
        list.add("44460");
        list.add("44461");
        list.add("44465");
        list.add("44324");
        list.add("44271");
        list.add("39596");
        list.add("39597");
        list.add("39598");
        list.add("39599");
        list.add("39600");
        list.add("39601");
        list.add("44268");
        list.add("39602");
        list.add("39604");
        list.add("44273");
        list.add("44494");
        list.add("44389");
        list.add("44401");
        list.add("44387");
        list.add("44290");
        list.add("39605");
        list.add("44325");
        list.add("44339");
        list.add("44351");
        list.add("44341");
        list.add("44419");
        list.add("44512");
        list.add("44211");
        list.add("44214");
        list.add("44258");
        list.add("44259");
        list.add("44260");
        list.add("44261");
        list.add("44243");
        list.add("44513");
        list.add("44244");
        list.add("44506");
        list.add("44238");
        list.add("44291");
        list.add("44322");
        list.add("44487");
        list.add("44488");
        list.add("44335");
        list.add("44338");
        list.add("44333");
        list.add("44332");
        list.add("44334");
        list.add("44336");
        list.add("44397");
        list.add("44398");
        list.add("44399");
        list.add("44393");
        list.add("44438");
        list.add("44515");
        list.add("44418");
        list.add("44407");
        list.add("44344");
        list.add("44345");
        list.add("44425");
        list.add("44463");
        list.add("44519");
        list.add("44411");
        list.add("44405");
        list.add("44412");
        list.add("44451");
        list.add("44440");
        list.add("44439");
        list.add("44406");
        list.add("44404");
        list.add("44329");
        list.add("44426");
        list.add("44410");
        list.add("44423");
        list.add("44421");
        list.add("44424");
        list.add("44422");
        list.add("44507");
        list.add("44509");
        list.add("44508");
        list.add("44505");
        list.add("44462");
        list.add("39589");
        list.add("44491");
        list.add("44348");
        list.add("44453");
        list.add("44518");
        list.add("44489");
        list.add("44517");
        list.add("44490");
        list.add("44455");
        list.add("44456");
        list.add("44454");
        list.add("44284");
        list.add("44473");
        list.add("44474");
        list.add("44475");
        list.add("44476");
        list.add("44477");
        list.add("44478");
        list.add("44480");
        list.add("44481");
        list.add("44485");
        list.add("44479");
        list.add("44482");
        list.add("44486");
        list.add("44256");
        list.add("44295");
        list.add("44236");
        list.add("44237");
        list.add("44280");
        list.add("44281");
        list.add("44452");
        list.add("39603");
        list.add("44495");
        list.add("44390");
        list.add("44340");
        list.add("44347");
        list.add("44337");
        list.add("44394");
        list.add("44420");
        list.add("44352");
        list.add("44234");
        list.add("44386");

        test.execute(list, DbConstant.getTestDataSource_R5DEV());

        System.out.println("done...");
    }

    DBMain dbMain;
    Properties transCodeProp;
    String filePath;

    private File getPrefixPath() {
        if (StringUtils.isBlank(filePath)) {
            return FileUtil.DESKTOP_DIR;
        }
        return new File(filePath);
    }

    public void execute(List<String> runidList, DataSource dataSource) throws IOException {
        dbMain = getDBMain(dataSource);

        initLoadProp();

        File file = new File(getPrefixPath(), "全球JOB_Report");
        file.mkdirs();

        File realFile = new File(file, "總表.xls");

        processBatchJobRun(runidList, realFile);
    }

    private void processBatchJobRun(List<String> runidList, File realFile) throws FileNotFoundException, IOException {
        String sql = "select t.*, r.job_name from t_batch_job_run t, t_batch_job r where t.real_job_id = r.job_id and t.run_id in ( " + StringUtils.join(runidList, ",") + " ) ";
        List<Map<String, Object>> list = dbMain.query(sql);
        if (list.isEmpty()) {
            throw new RuntimeException("SQL錯誤 : " + sql);
        }

        HSSFWorkbook wk = new HSSFWorkbook();
        HSSFSheet sheet = wk.createSheet();

        HSSFRow row1 = sheet.createRow(0);

        ExcelUtil.getInstance().setCellValue(row1.createCell(0), "RUN_ID");
        ExcelUtil.getInstance().setCellValue(row1.createCell(1), "JOB_NAME");
        ExcelUtil.getInstance().setCellValue(row1.createCell(2), "JOB_ID");
        ExcelUtil.getInstance().setCellValue(row1.createCell(3), "分片方式");
        ExcelUtil.getInstance().setCellValue(row1.createCell(4), "run_TOTAL");
        ExcelUtil.getInstance().setCellValue(row1.createCell(5), "run_SUCCESS");
        ExcelUtil.getInstance().setCellValue(row1.createCell(6), "run_FAIL");
        ExcelUtil.getInstance().setCellValue(row1.createCell(7), "run_OTHER");
        ExcelUtil.getInstance().setCellValue(row1.createCell(8), "TOTAL");
        ExcelUtil.getInstance().setCellValue(row1.createCell(9), "SUCCESS");
        ExcelUtil.getInstance().setCellValue(row1.createCell(10), "FAIL");
        ExcelUtil.getInstance().setCellValue(row1.createCell(11), "OTHER");
        ExcelUtil.getInstance().setCellValue(row1.createCell(12), "ERROR_LOG");
        ExcelUtil.getInstance().setCellValue(row1.createCell(13), "WARN_LOG");
        ExcelUtil.getInstance().setCellValue(row1.createCell(14), "INFO_LOG");
        ExcelUtil.getInstance().setCellValue(row1.createCell(15), "DEBUG_LOG");

        int ii = 1;
        for (Map<String, Object> rec : list) {
            Integer run_id = getBig(rec.get("RUN_ID"));
            String job_id = String.valueOf(getBig(rec.get("REAL_JOB_ID")));
            String jobName = String.valueOf(rec.get("JOB_NAME"));

            Integer total_record = getBig(rec.get("TOTAL_RECORD"));
            Integer success_record = getBig(rec.get("SUCCESS_RECORD"));
            Integer failed_record = getBig(rec.get("FAILED_RECORD"));

            RecordCount normalVer = new RecordCount(total_record, success_record, failed_record, null);// #1

            RecordCount transCodeVer = null;// #2
            String transCode = transCodeProp.getProperty(job_id);
            if (StringUtils.isNotBlank(transCode)) {
                transCodeVer = getDetailRecordInfo(transCode);
            }

            RecordCount javaVer = getJavaJobRecordInfo(String.valueOf(run_id));

            RecordCount financeVer = getFinanaceRecordInfo(job_id);

            Object[] rtnVal = getRecordType(transCodeVer, javaVer, financeVer);

            String recordType = (String) rtnVal[0];
            RecordCount record = (RecordCount) rtnVal[1];
            
            if(record == null){
                record = new RecordCount(null,null,null,null);
            }
            
            System.out.println(Arrays.toString(rtnVal));

            HSSFRow row2 = sheet.createRow(ii);
            ExcelUtil.getInstance().setCellValue(row2.createCell(0), run_id);
            ExcelUtil.getInstance().setCellValue(row2.createCell(1), jobName);
            ExcelUtil.getInstance().setCellValue(row2.createCell(2), job_id);
            ExcelUtil.getInstance().setCellValue(row2.createCell(3), recordType);
            ExcelUtil.getInstance().setCellValue(row2.createCell(4), normalVer.total);
            ExcelUtil.getInstance().setCellValue(row2.createCell(5), normalVer.success);
            ExcelUtil.getInstance().setCellValue(row2.createCell(6), normalVer.fail);
            ExcelUtil.getInstance().setCellValue(row2.createCell(7), normalVer.other);
            ExcelUtil.getInstance().setCellValue(row2.createCell(8), record.total);
            ExcelUtil.getInstance().setCellValue(row2.createCell(9), record.success);
            ExcelUtil.getInstance().setCellValue(row2.createCell(10), record.fail);
            ExcelUtil.getInstance().setCellValue(row2.createCell(11), record.other);
            ExcelUtil.getInstance().setCellValue(row2.createCell(12), getLogCount(String.valueOf(run_id), 4));
            ExcelUtil.getInstance().setCellValue(row2.createCell(13), getLogCount(String.valueOf(run_id), 3));
            ExcelUtil.getInstance().setCellValue(row2.createCell(14), getLogCount(String.valueOf(run_id), 2));
            ExcelUtil.getInstance().setCellValue(row2.createCell(15), getLogCount(String.valueOf(run_id), 1));

            ii++;
        }

        ExcelUtil.getInstance().writeExcel(realFile, wk);
    }

    private Object[] getRecordType(RecordCount transCodeVer, RecordCount javaVer, RecordCount financeVer) {
        RecordCount rtn = null;
        String type = "";
        if (StringUtils.equals(javaVer.indicateType, "java") ||  !isRecordCountEmpty(javaVer)) {
            rtn = javaVer;
            type = "Java";
        } else if (StringUtils.equals(javaVer.indicateType, "Plsql") || !isRecordCountEmpty(transCodeVer)) {
            rtn = transCodeVer;
            type = "Plsql";
        } else if (StringUtils.equals(javaVer.indicateType, "GL Posting") || !isRecordCountEmpty(financeVer)) {
            rtn = financeVer;
            type = "GL Posting";
        } else {
            type = "不分片";
        }
        return new Object[] { type, rtn };
    }

    private boolean isRecordCountEmpty(RecordCount d) {
        if (d == null || d.isAllEmpty()) {
            return true;
        }
        return false;
    }

    private static class RecordCount {
        Integer total;
        Integer success;
        Integer fail;
        Integer other;
        
        String indicateType;

        public RecordCount(Integer total, Integer success, Integer fail, Integer other) {
            super();
            this.total = total;
            this.success = success;
            this.fail = fail;
            this.other = other;
        }

        public RecordCount() {
        }

        private boolean isNullOrZero(Integer val) {
            if (val == null || val == 0) {
                return true;
            }
            return false;
        }

        private boolean isAllEmpty() {
            if (isNullOrZero(total) && isNullOrZero(success) && isNullOrZero(fail) && isNullOrZero(other)) {
                return true;
            }
            return false;
        }

        @Override
        public String toString() {
            return "RecordCount [total=" + total + ", success=" + success + ", fail=" + fail + ", other=" + other + "]";
        }
    }

    private RecordCount getDetailRecordInfo(String transCode) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT case                                                                 ");
        sb.append("          when st.status_name is null then                                   ");
        sb.append("           'NON_RUN'                                                         ");
        sb.append("          else                                                               ");
        sb.append("           st.status_name                                                    ");
        sb.append("        end as STATUS_CHS,                                                   ");
        sb.append("        count(1) as CNT                                                      ");
        sb.append("   FROM t_batch_multi_streaming t                                            ");
        sb.append("   left outer join t_batch_job_status st on t.status = st.status_id          ");
        sb.append("  where upper(t.trans_code) = '" + transCode + "'                                          ");
        sb.append("  group by st.status_name                                                    ");
        List<Map<String, Object>> query = dbMain.query(sb.toString());
        RecordCount d = new RecordCount(0, 0, 0, 0);
        for (Map<String, Object> m : query) {
            String title = (String) m.get("STATUS_CHS");
            int count = getBig(m.get("CNT"));
            if ("EXECUTE FAILED".equalsIgnoreCase(title)) {
                d.fail = d.fail + count;
            } else if ("EXECUTE SUCCESS".equalsIgnoreCase(title)) {
                d.success = d.success + count;
            } else {
                d.other = d.other + count;
            }
        }
        d.total = d.other + d.success + d.fail;
        if(!query.isEmpty()){
            d.indicateType = "Plsql";
        }
        return d;
    }

    private RecordCount getJavaJobRecordInfo(String runId) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select status, count(status) as cunt      ");
        sb.append("   from t_batch_job_record t               ");
        sb.append("  where t.run_id = '" + runId + "'                      ");
        sb.append("  group by status                          ");
        List<Map<String, Object>> query = dbMain.query(sb.toString());
        RecordCount d = new RecordCount(0, 0, 0, 0);
        for (Map<String, Object> m : query) {
            BigDecimal status = (BigDecimal) m.get("STATUS");
            BigDecimal cunt = (BigDecimal) m.get("CUNT");
            switch (status.intValue()) {
            case 105:
                d.fail += cunt.intValue();
                break;
            case 107:
                d.success += cunt.intValue();
                break;
            default:
                d.other += cunt.intValue();
                break;
            }
        }
        d.total = d.other + d.success + d.fail;
        if(!query.isEmpty()){
            d.indicateType = "java";
        }
        return d;
    }

    private RecordCount getFinanaceRecordInfo(String jobId) {
        String table = "";
        switch (Integer.parseInt(jobId)) {
        case 2656:
            table = "t_prem_arap_gl";
            break;
        case 2675:
            table = "t_cash_gl";
            break;
        case 2714:
            table = "t_capital_distribute_gl";
            break;
        case 1332924:
            table = "t_fin_separate_adjust_gl";
            break;
        case 2694:
            table = "t_fund_cash_gl";
            break;
        default:
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" select count(*) as cunt, 'success' as status    ");
        sb.append("   from " + table + " a                         ");
        sb.append("  where a.posted = 'Y'                           ");
        sb.append(" union                                           ");
        sb.append(" select count(*) as cunt, 'fail' as status       ");
        sb.append("   from " + table + " a                         ");
        sb.append("  where a.posted = 'N'                           ");
        List<Map<String, Object>> query = dbMain.query(sb.toString());
        RecordCount d = new RecordCount(0, 0, 0, 0);
        for (Map<String, Object> m : query) {
            String status = (String) m.get("STATUS");
            BigDecimal cunt = (BigDecimal) m.get("CUNT");
            if("success".equalsIgnoreCase(status)){
                d.success += cunt.intValue();
            }else if("fail".equalsIgnoreCase(status)){
                d.fail += cunt.intValue();
            }
        }
        d.total = d.other + d.success + d.fail;
        d.indicateType = "GL Posting";
        return d;
    }
    
    private Integer getLogCount(String runId, int level){
        StringBuilder sb = new StringBuilder();
        sb.append(" select count(run_id) as cunt   ");
        sb.append("   from t_batch_log t           ");
        sb.append("  where log_level = "+level+"           ");
        sb.append("    and run_id = "+runId+"          ");
        sb.append("  group by run_id               ");
        List<Map<String, Object>> query = dbMain.query(sb.toString());
        if(query.isEmpty()){
            return null;
        }
        return getBig(query.get(0).get("CUNT"));
    }

    private Integer getBig(Object val) {
        if (val == null) {
            return null;
        }
        if (val instanceof BigDecimal) {
            return ((BigDecimal) val).intValue();
        }
        return null;
    }

    private void initLoadProp() throws IOException {
        URL url = this.getClass().getResource("BatchJobReport_GenXlsReport_Transcode.properties");
        Properties prop = new Properties();
        prop.load(url.openStream());
        transCodeProp = prop;
        for (Enumeration enu = prop.keys(); enu.hasMoreElements();) {
            String key = (String) enu.nextElement();
            String val = prop.getProperty(key);
            // System.out.println(key + "--" + val);
        }
    }

    private DBMain getDBMain(DataSource dataSource) {
        DBMain dbMain = new DBMain();
        dbMain.setDataSource(dataSource);
        return dbMain;
    }
}
