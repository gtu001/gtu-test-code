package gtu._work;

import gtu.file.FileUtil;
import gtu.poi.hssf.ExcelUtil;

import java.io.File;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.PooledConnection;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.informix.jdbcx.IfxConnectionPoolDataSource;

public class PersonIdFinder {

    private PrintStream out = System.out;
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        PersonIdFinder test = new PersonIdFinder();
        test.execute();
        System.out.println("done...");
    }
    
    public void execute(){
        Connection conn = null;
        try {
            conn = getProductionConnection().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select t.tabname from systables t join syscolumns c on t.tabid = c.tabid where c.colname = 'person_id'");
            List<String> list = new ArrayList<String>();
            while(rs.next()){
                list.add(rs.getString(1));
            }
            rs.close();
            Collections.sort(list);
            
            ExcelUtil util = ExcelUtil.getInstance();
            HSSFWorkbook wk = new HSSFWorkbook();
            for(String tableName : list){
                String sql = "select * from "+tableName+" where person_id = 'F226556372'";
                try{
                    rs = stmt.executeQuery(sql);
                    ResultSetMetaData rsmd = rs.getMetaData();
                    List<String> rtnArray = new ArrayList<String>();
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        rtnArray.add(rsmd.getColumnName(i));
                    }
                    
                    List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
                    while(rs.next()){
                        Map<String, String> valMap = new HashMap<String,String>();
                        for(String col : rtnArray){
                            String value = rs.getString(col);
                            valMap.put(col, value);
                        }
                        dataList.add(valMap);
                    }
                    rs.close();
                    
                    if(!dataList.isEmpty()){
                        HSSFSheet sheet = wk.createSheet(tableName);
                        out.println("建立資料:" + tableName + ", 比數:" + dataList.size());
                        int ii = 0;
                        for(Map<String, String> map : dataList){
                            for(String col : map.keySet()){
                                String value = map.get(col);
                                HSSFRow row = sheet.createRow(ii++);
                                util.setCellValue(row.createCell(0), col);
                                util.setCellValue(row.createCell(1), value);
                            }
                        }
                        ii++;
                    }
                }catch(Exception ex){
                    out.println("錯誤 : " + ex.getMessage() + " , sql = " + sql);
                    ex.printStackTrace();
                }
            }
            
            util.writeExcel(new File(FileUtil.DESKTOP_PATH, "機密.xls"), wk);
            out.println("done...");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try{
                conn.close();
            }catch(Exception ex){
            }
        }
    }
    
//    DBInfo [alias=65000新北市戶所, ip=195.1.102.11, port=4526, schema=teu00020, db=ibm, userId=srismapp, password=Sth!aix1, fullUrl=jdbc:informix-sqli://195.1.102.11:4526/teu00020:informixserver=ibm;DB_LOCALE=zh_tw.utf8;CLIENT_LOCALE=zh_tw.utf8;GL_USEGLU=1]
    private static PooledConnection getProductionConnection() {
        IfxConnectionPoolDataSource cpds;
        try {
            String DB_HOST = "195.1.102.11";
            int DB_PORT = 4526;
            String DB_USER = "srismapp";
            String DB_PWD = "Sth!aix1";
            String DB_SERVER_NAME = "ibm";
            String DB_NAME = "teu00020";//RL XXX
//            String DB_NAME = "chun0000";//RC XXX
            String DB_LOCALE = "zh_tw.utf8";
            String DB_CLIENT_LOCALE = "zh_tw.utf8";

            cpds = new IfxConnectionPoolDataSource();
            cpds.setDescription("Pick-A-Seat Connection pool");
            cpds.setIfxCPMMaxConnections(-1);
            cpds.setIfxCPMMaxPoolSize(10);
            cpds.setIfxCPMMinPoolSize(5);

            cpds.setIfxIFXHOST(DB_HOST);
            cpds.setPortNumber(DB_PORT);
            cpds.setUser(DB_USER);
            cpds.setPassword(DB_PWD);
            cpds.setServerName(DB_SERVER_NAME);
            cpds.setDatabaseName(DB_NAME);
            cpds.setIfxCLIENT_LOCALE(DB_CLIENT_LOCALE);
            cpds.setIfxDB_LOCALE(DB_LOCALE);

            return cpds.getPooledConnection();
        } catch (Exception sqle) {
            throw new RuntimeException(sqle);
        }
    }
    
    private static PooledConnection getDebugConnection() {
        IfxConnectionPoolDataSource cpds;
        try {
            String DB_HOST = "192.168.10.18";
            int DB_PORT = 4526;
            String DB_USER = "srismapp";
            String DB_PWD = "ris31123";
            String DB_SERVER_NAME = "aix2";
            String DB_NAME = "teun0020";//RL XXX
//            String DB_NAME = "chun0000";//RC XXX
            String DB_LOCALE = "zh_tw.utf8";
            String DB_CLIENT_LOCALE = "zh_tw.utf8";

            cpds = new IfxConnectionPoolDataSource();
            cpds.setDescription("Pick-A-Seat Connection pool");
            cpds.setIfxCPMMaxConnections(-1);
            cpds.setIfxCPMMaxPoolSize(10);
            cpds.setIfxCPMMinPoolSize(5);

            cpds.setIfxIFXHOST(DB_HOST);
            cpds.setPortNumber(DB_PORT);
            cpds.setUser(DB_USER);
            cpds.setPassword(DB_PWD);
            cpds.setServerName(DB_SERVER_NAME);
            cpds.setDatabaseName(DB_NAME);
            cpds.setIfxCLIENT_LOCALE(DB_CLIENT_LOCALE);
            cpds.setIfxDB_LOCALE(DB_LOCALE);

            return cpds.getPooledConnection();
        } catch (Exception sqle) {
            throw new RuntimeException(sqle);
        }
    }

    public void setOut(PrintStream out) {
        this.out = out;
    }
}
