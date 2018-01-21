/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.jpa.hibernate;

import gtu.log.Log;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.HashMultiset;

public class JPACreateManagerFactory_RealJpa {

    private static PrintStream out = System.out;
    private static EntityManagerFactory emf;

    static boolean debugMode = true;

    public static void main(String[] args) throws SQLException, IOException {
        JPACreateManagerFactory_RealJpa test = new JPACreateManagerFactory_RealJpa();
        Log.Setting.FULL.apply();
        test.execute();
        out.println("All done...");
    }

    public void execute() {
        try {
            executeInJpa();
            out.println("全部完成!!");
        } catch (Exception ex) {
            Log.debug(ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     * Execute in jpa.
     * 
     * @return the hash multiset
     */
    public HashMultiset<PersonForNative> executeInJpa() {
        long startTime = System.currentTimeMillis();
        EntityManager entityManager = null;
        try {
            initJpaConfig();
            // -----------------------------------------------------------------------------
            entityManager = emf.createEntityManager();

            String sql1 = "select person_id, birth_yyymmdd, site_id from rcdf002e WHERE tx_yyymmdd >= '0941221' AND apply_reason = '2' AND process_status = '9'";
            Query query = entityManager.createNativeQuery(sql1, PersonForNative.class);
            int cache = 1000;
            query.setMaxResults(cache);

            HashMultiset<PersonForNative> personSet = HashMultiset.<PersonForNative> create();

            boolean done = false;
            int index = 0;
            for (int page = 0; !done; page++) {
                query.setFirstResult(page * cache);

                long queryStartTime = System.currentTimeMillis();
                List<PersonForNative> list = query.getResultList();
                long queryDuringTime = System.currentTimeMillis() - queryStartTime;
                out.println("查詢時間:" + queryDuringTime);
                if (list.size() < cache) {
                    done = true;
                }

                for (int ii = 0; ii < list.size(); ii++) {
                    PersonForNative person = list.get(ii);
                    personSet.add(person);
                    out.println((++index) + "..." + person);
                }
            }

            int jj = 0;
            String sql2 = "select person_id, birth_yyymmdd, site_id from rcdf001m WHERE person_id = :person_id";
            for (PersonForNative person : personSet.elementSet()) {
                Query query2 = entityManager.createNativeQuery(sql2, PersonForRealData.class);
                query2.setParameter("person_id", person.personid);
                List<PersonForRealData> list = query2.getResultList();
                if (list.size() > 0) {
                    PersonForRealData p2 = list.get(0);
                    person.birthYyymmdd = p2.birthYyymmdd;
                    person.siteId = p2.siteId;
                    out.println((++jj) + "=> fix =>" + person);
                }
            }

            out.println("sql done...");
            return personSet;
        } catch (Throwable ex) {
            Log.debug(ex);
            ex.printStackTrace(out);
            throw new RuntimeException(ex);
        } finally {
            out.println("查詢總耗時:" + (System.currentTimeMillis() - startTime));
            try {
                entityManager.close();
            } catch (Exception ex) {
            }
        }
    }

    public PrintStream getOut() {
        return out;
    }

    public void setOut(PrintStream out) {
        this.out = out;
    }

    private void initJpaConfig() throws IOException {
        if (debugMode) {
            File destDir = new File("D:/_桌面/workspace/GTU/build/classes/META-INF/");
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            File srcFile = new File("D:/_桌面/workspace/GTU/src/gtu/jpa/hibernate/persistence.xml");
            File destFile = new File("D:/_桌面/workspace/GTU/build/classes/META-INF/persistence.xml");
            if (srcFile.length() != destFile.length() || srcFile.lastModified() != destFile.lastModified()) {
                out.println("更新persistence.xml!!");
                FileUtils.copyFile(srcFile, destFile);
            }
        }
        Properties prop = new Properties();
        if (!debugMode) {
            // 外面環境
            prop.setProperty(
                    "hibernate.connection.url",
                    "jdbc:informix-sqli://192.1.0.11:4526/chu00000:informixserver=ibm;DB_LOCALE=zh_tw.utf8;CLIENT_LOCALE=zh_tw.utf8;GL_USEGLU=1");
            prop.setProperty("hibernate.connection.username", "srismapp");
            prop.setProperty("hibernate.connection.password", "Sth!aix1");
        }
        emf = Persistence.createEntityManagerFactory("test", prop);
    }
}