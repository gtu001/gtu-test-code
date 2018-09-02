/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.jpa.hibernate;

import gtu.file.FileUtil;
import gtu.log.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.dom4j.io.SAXReader;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.google.common.base.Joiner;
import com.google.common.collect.HashMultiset;

public class LoadToDBAndWriteFile {

    private static PrintStream out = System.out;

    File dataBase;
    File baseDir;
    File bulkTxt;
    BufferedWriter overWriter;
    BufferedWriter oneWriter;
    BufferedWriter siteWriter;
    EmbeddedDataSource dataSource;
    
    public static void main(String[] args) throws SQLException, IOException {
        LoadToDBAndWriteFile test = new LoadToDBAndWriteFile();
        Log.Setting.FULL.apply();
        test.bulkTxt = new File("G:/錄音筆功能/output_rcdf002e/bulk_rcdf002e.txt");
        test.execute();
        test.writeFile();
        out.println("All done...");
    }

    public void execute() {
        init();
        createTable();
        saveToDb();
        writeFile();
        out.println("全部完成!!");
    }
    
    private void init(){
        baseDir = new File(FileUtil.DESKTOP_PATH, "output_rcdf002e");
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
        if(bulkTxt == null){
            bulkTxt = new File(baseDir, "bulk_rcdf002e.txt");
        }
        if(!bulkTxt.exists()){
            throw new RuntimeException("檔案不存在:" + bulkTxt);
        }
        dataBase = new File(baseDir, "dataBase");
        try{
            File outputOverFile = new File(baseDir, "超過1次請領.txt");
            overWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputOverFile),
                    "utf8"));
            File outputOneFile = new File(baseDir, "只1次請領.txt");
            oneWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputOneFile),
                    "utf8"));
            File outputSiteFile = new File(baseDir, "作業點統計.txt");
            siteWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputSiteFile),
                    "utf8"));
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
    
    private void saveToDb() {
        long startTime = System.currentTimeMillis();
        try {
            out.println("-----------------------1");
            out.println("寫入資料庫!!");
            out.println("-----------------------2");
            
            final JdbcTemplate template = new JdbcTemplate(dataSource);
            
//          <object-stream>
//            <gtu.jpa.hibernate.PersonForNative>
//              <personid>F120174089</personid>
//              <birthYyymmdd>0530221</birthYyymmdd>
//              <siteId>65000170</siteId>
//              <count>0</count>
//            </gtu.jpa.hibernate.PersonForNative>
//            <gtu.jpa.hibernate.PersonForNative> ...很多
            
//            /*
            SAXReader reader = new SAXReader();
            reader.setDefaultHandler(new ElementHandler() {
                long insertTime = 0;
                long updateTime = 0;
                long total = 0;
                @Override
                public void onEnd(ElementPath arg0) {
                    Element e = arg0.getCurrent(); //获得当前节点  
                    if(e.getName().equals("gtu.jpa.hibernate.PersonForNative")) {
                       List<Element> elist = e.selectNodes("*");
                       PersonForNative p = new PersonForNative();
                       for(Element e2 : elist){
                           if(e2.getName().equals("personid")){
                               p.personid = e2.getText();
                           }else if(e2.getName().equals("birthYyymmdd")){
                               p.birthYyymmdd = e2.getText();
                           }else if(e2.getName().equals("siteId")){
                               p.siteId = e2.getText();
                           }
//                           out.println(p);
                       }
                       try{
                           Integer countVal = template.queryForObject("select count from person where person_id = '"+p.personid+"' ", Integer.class);
                           countVal ++;
                           updateTime += template.update("update person set count = " + countVal + " where person_id = '"+p.personid+"' ");
//                           out.println("update ->" + update);
                       }catch(EmptyResultDataAccessException ex){
                           insertTime += template.update("insert into person (person_id, birth_yyymmdd, site_id, count) values (?,?,?,?)", //
                                   new Object[]{p.personid, p.birthYyymmdd, p.siteId, 1}, //
                                   new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER});
//                           out.println("update ->" + insert);
                       }
                       total ++;
                       if (total % 1000 == 0) {
                           out.println("資料異動次數-> 新增:" + insertTime + ",修改:"+updateTime);
                       }
                       e.detach(); //记得从内存中移去   TODO
                    }
                }
                @Override
                public void onStart(ElementPath arg0) {
                }
            });
            reader.read(bulkTxt);
//            */
            
        } catch (Throwable e) {
            out.println(e.getLocalizedMessage());
            Log.debug(e);
            throw new RuntimeException("失敗", e);
        } finally {
            out.println("-----------------------8");
            out.println("寫入資料庫耗時:" + (System.currentTimeMillis() - startTime));
            out.println("-----------------------8");
        }
    }
    
    private void writeFile() {
        long startTime = System.currentTimeMillis();
        try {
            out.println("#. writeFile .s");
            out.println("-----------------------1");
            out.println("開始寫檔!!");
            out.println("-----------------------2");
            final HashMultiset<String> siteSet = HashMultiset.<String> create();
            final JdbcTemplate template = new JdbcTemplate(dataSource);
            template.query("select * from person where count = 1", new RowCallbackHandler() {
                long ii = 0;

                @Override
                public void processRow(ResultSet paramResultSet) throws SQLException {
                    PersonForNative p = new PersonForNative();
                    p.personid = paramResultSet.getString(1);
                    p.birthYyymmdd = paramResultSet.getString(2);
                    p.siteId = paramResultSet.getString(3);
                    p.count = paramResultSet.getInt(4);
                    // out.println("QUERY1 ==> " + p);
                    siteSet.add(p.siteId);
                    writeFile(p.writeOne(), oneWriter);
                    ii++;
                    if (ii % 1000 == 0) {
                        out.println("請領一次目前寫入人數:" + ii);
                    }
                }
            });
            this.closeWriter(oneWriter);
            template.query("select * from person where count > 1", new RowCallbackHandler() {
                long ii = 0;

                @Override
                public void processRow(ResultSet paramResultSet) throws SQLException {
                    PersonForNative p = new PersonForNative();
                    p.personid = paramResultSet.getString(1);
                    p.birthYyymmdd = paramResultSet.getString(2);
                    p.siteId = paramResultSet.getString(3);
                    p.count = paramResultSet.getInt(4);
                    // out.println("QUERYX ==> " + p);
                    writeFile(p.writeOver(), overWriter);
                    ii++;
                    if (ii % 1000 == 0) {
                        out.println("請領多次目前寫入人數:" + ii);
                    }
                }
            });
            this.closeWriter(overWriter);
            for (String key : siteSet.elementSet()) {
                Object[] vals = new Object[] { this.getEmptySpace(8, StringUtils.leftPad(key, 8, '0')),//
                        this.getEmptySpace(6, StringUtils.leftPad(String.valueOf(siteSet.count(key)), 6, '0')),//
                };
                writeFile(Joiner.on(",").join(vals), siteWriter);
            }
            this.closeWriter(siteWriter);
        } catch (Throwable e) {
            out.println(e.getLocalizedMessage());
            // throw new RuntimeException("失敗", e);
        } finally {
            out.println("-----------------------8");
            out.println("寫黨總耗時:" + (System.currentTimeMillis() - startTime));
        }
    }
    
    private void createTable() {
        Connection conn = null;
        Statement stmt = null;
        try {
            dataSource = new EmbeddedDataSource();
            dataSource.setCreateDatabase("create");
            dataSource.setDatabaseName(dataBase.getAbsolutePath());
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
            boolean result = stmt.execute("CREATE TABLE person(person_id varchar(10), birth_yyymmdd varchar(7), site_id varchar(8), count int, PRIMARY KEY (person_id))");
            out.println("建立資料庫 : " + result);
        } catch (java.sql.SQLException e){
            if(e.getMessage().endsWith("dataBase already exists.")){
                e.printStackTrace();
                return;
            }
        } catch (Throwable e) {
            out.println(e.getLocalizedMessage());
            throw new RuntimeException("失敗", e);
        } finally {
            JdbcFastQuery.closeJdbc(null, stmt, conn);
        }
    }
    
    private void writeFile(String content, BufferedWriter writer){
        try {
            writer.write(content);
            writer.newLine();
        } catch (Exception e) {
            throw new RuntimeException(content, e);
        }
    }

    private String getEmptySpace(int len, String value){
        if(value!=null && value.length() == len){
            return value;
        }else if(value == null){
            StringBuilder sb = new StringBuilder();
            for(int ii = 0 ; ii < len ; ii ++){
                sb.append(" ");
            }
            return sb.toString();
        }
        throw new RuntimeException("長度有問題:"+value + ",要求長度:" + len);
    }
    
    private void closeWriter(BufferedWriter writer){
        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PrintStream getOut() {
        return out;
    }

    public void setOut(PrintStream out) {
        LoadToDBAndWriteFile.out = out;
    }
}