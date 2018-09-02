/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.jpa.hibernate;

import gtu._work.dbVisualizer.DbvisXmlReader;
import gtu._work.dbVisualizer.DbvisXmlReader.DBInfo;
import gtu.file.FileUtil;
import gtu.log.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.PooledConnection;

import com.informix.jdbcx.IfxConnectionPoolDataSource;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.JDomDriver;

public class JdbcFastQuery {

    private static PrintStream out = System.out;

    static boolean debugMode = true;
    File baseDir;
    File bulkTxt;
    
    public static void main(String[] args) throws SQLException, IOException {
        JdbcFastQuery test = new JdbcFastQuery();
        Log.Setting.FULL.apply();
        test.execute();
        out.println("All done...");
    }

    public void execute() {
        mkdir();
        executeInJdbc();
        out.println("全部完成!!");
    }
    
    private void mkdir(){
        baseDir = new File(FileUtil.DESKTOP_PATH, "output_rcdf002e");
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
        bulkTxt = new File(baseDir, "bulk_rcdf002e.txt");
    }

    /**
     * Execute in jdbc.
     * 
     * @return the hash multiset
     */
    public void executeInJdbc() {
        out.println("-----------------------1");
        long startTime = System.currentTimeMillis();
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            XStream stream = new  XStream(new JDomDriver());
            out.println("-----------------------2");
            conn = getJdbcConnection().getConnection();
            JdbcDirtyFastQueryUtil.fixConntion(conn);
            stat = conn.createStatement();
            JdbcDirtyFastQueryUtil.fixStatemet(stat);
            rs = stat.executeQuery(SQL);
            
            int index = 0;
            FileOutputStream fos = new FileOutputStream(bulkTxt);
            ObjectOutputStream out1 = stream.createObjectOutputStream(fos);
            out.println("-----------------------3");
            while (rs.next()) {
                ++index;
                PersonForNative p = new PersonForNative();
                p.personid = rs.getString(1);
                p.birthYyymmdd = rs.getString(2);
                p.siteId = rs.getString(3);
                out1.writeObject(p);
                if(index % 1000 == 0){
                    out.println("目前寫入資料比數:" + index);
                }
            }
            out1.flush();
            out1.close();
            out.println("-----------------------4");
            this.closeJdbc(rs, stat, conn);
            out.println("-----------------------5");
            out.println("總比數 : " + index);
        } catch (Throwable ex) {
            Log.debug(ex);
            ex.printStackTrace(out);
            throw new RuntimeException(ex);
        } finally {
            out.println("-----------------------8");
            out.println("查詢總耗時:" + (System.currentTimeMillis() - startTime));
            this.closeJdbc(rs, stat, conn);
        }
    }


    
    private static final String SQL;
    static {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT                                                                            ");
        sb.append("     person_id,                                                                    ");
        sb.append("     birth_yyymmdd,                                                                ");
        sb.append("     (select site_id from rcdf001m c where e.person_id = c.person_id) as site_id   ");
        sb.append(" FROM                                                                              ");
        sb.append("     rcdf002e e                                                                    ");
        sb.append(" WHERE                                                                             ");
        sb.append("     tx_yyymmdd >= '0941221'                                                       ");
        sb.append(" AND apply_reason = '2'                                                            ");
        sb.append(" AND process_status = '9'                                                          ");
        SQL = sb.toString();
    }

    private PooledConnection getJdbcConnection() {
        IfxConnectionPoolDataSource cpds;
        try {
            DBInfo def = new DBInfo();
            def.alias = "sit測試";// 67000台南市戶所
            def.ip = "192.168.10.18";// 1 = 195.11.31.11
            def.port = "4526";// 2 = 4526
            def.schema = "chun0000";// 3 = tnu00310
            def.db = "aix2";// 4 = ibm
            def.userId = "srismapp";// srismdbc
            def.password = "ris31123";// JellNtfp23+JurdFlpPpxQ==

            DBInfo dbInfo = null;
            if (debugMode) {
                dbInfo = def;
            } else {
                DbvisXmlReader reader = new DbvisXmlReader();
                reader.execute();
                for (DBInfo d : reader.getDbInfoList()) {
                    if (d.alias.equals("00000000中央")) {
                        dbInfo = d;
                        break;
                    }
                }
            }

            String DB_HOST = dbInfo.ip;
            int DB_PORT = Integer.parseInt(dbInfo.port);
            String DB_USER = dbInfo.userId;
            String DB_PWD = dbInfo.password;
            String DB_SERVER_NAME = dbInfo.db;
            String DB_NAME = dbInfo.schema;
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
    
    public PrintStream getOut() {
        return out;
    }

    public void setOut(PrintStream out) {
        this.out = out;
    }
    
    static void closeJdbc(ResultSet rs, Statement stat, Connection conn) {
        try {
            rs.close();
        } catch (Exception e) {
        }
        try {
            stat.close();
        } catch (Exception e) {
        }
        try {
            conn.close();
        } catch (Exception e) {
        }
    }
    public static class JdbcDirtyFastQueryUtil {
        public static void fixConntion(Connection conn) throws SQLException {
            DatabaseMetaData dbMd = conn.getMetaData();
            if (dbMd.supportsTransactionIsolationLevel(Connection.TRANSACTION_READ_UNCOMMITTED)) {
                conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            }
            conn.setReadOnly(true);
            conn.setAutoCommit(false);
        }
        public static void fixStatemet(Statement stmt) throws SQLException{
            stmt.setFetchSize(100);
        }
    }
}