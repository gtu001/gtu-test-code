package gtu._work.etc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class DbHbmChecker {

    static final String DS_XML_DIR = "C:/L-CONFIG/SRIS/RIS/config/db/DSxml";
    static final String HIBERNATE_CFG = "C:/L-CONFIG/SRIS/RIS/config/db/MapConfig/65000030.cfg.xml";
    static final String DB_DOMAIN_JAR = "C:/Users/gtu001/.m2/repository/tw/gov/sris-db-domain/1.2.0-SNAPSHOT/sris-db-domain-1.2.0-SNAPSHOT.jar";

    
    public static void main(String[] args) throws DocumentException, IOException {
        DbHbmChecker chker = new DbHbmChecker();
        chker.printLostHbmInJarsType();//找出hibernate設定裡面有 但是 domain Jar檔有缺的db table
//        chker.printLostHbmType();//檢查hibernate cfg 是否有 ds xml 缺的 table
//          chker.printLostHbmTypeInDsXml();//印出hibernate 沒有 卻存在於 ds xml的 dsxml檔案
        System.out.println("done...");
    }

    /**
     * 找出hibernate設定裡面有 但是 domain Jar檔有缺的db table
     */
    void printLostHbmInJarsType() throws DocumentException, IOException {
        List<String> hbms = getMapConfigHbms();
        List<String> jars = getJarDbType();
        Set<String> findNoOk = new HashSet<String>();
        for (String hbm : hbms) {
            boolean findOk = false;
            for (String jar : jars) {
                if (hbm.equalsIgnoreCase(jar)) {
                    findOk = true;
                    break;
                }
            }
            if (!findOk) {
                findNoOk.add(hbm);
            }
        }
        List<String> flist = new ArrayList<String>(findNoOk);
        Collections.sort(flist);
        System.out.println("jar 有 但cfg 沒有 ");
        for(String miss : flist){
            System.out.println(miss);
        }
    }

    /**
     * 印出hibernate 沒有 卻存在於 ds xml的 dsxml檔案
     */
    void printLostHbmTypeInDsXml() throws DocumentException {
        List<String> hbms = getMapConfigHbms();
        Map<String, List<File>> dstab = getUseDsTable();
        for (String ddd : dstab.keySet()) {
            ddd = ddd.toLowerCase();
            boolean findOk = false;
            for (String hhh : hbms) {
                hhh = hhh.toLowerCase();
                if (hhh.indexOf(ddd) != -1) {
                    findOk = true;
                    break;
                }
            }
            if (!findOk) {
                System.out.println("=======>" + ddd);
                for (File dsXml : dstab.get(ddd)) {
                    System.out.println("\t" + dsXml);
                    //FIXME 將有問題的dsXml移除↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
                    //dsXml.delete();
                    //FIXME 將有問題的dsXml移除↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
                }
            }
        }
    }

    /**
     * 檢查hibernate cfg 是否有 ds xml 缺的 table
     */
    void printLostHbmType() throws DocumentException {
        List<String> hbms = getMapConfigHbms();
        Map<String, List<File>> dstab = getUseDsTable();
        for (String ddd : dstab.keySet()) {
            ddd = ddd.toLowerCase();
            boolean findOk = false;
            for (String hhh : hbms) {
                hhh = hhh.toLowerCase();
                if (hhh.indexOf(ddd) != -1) {
                    findOk = true;
                    break;
                }
            }
            if (!findOk) {
                String format = "<mapping resource=\"tw/gov/moi/domain/%sType.hbm.xml\"/>";
                System.out.println(String.format(format, ddd.toUpperCase().substring(0, 1) + ddd.substring(1)));
            }
        }
    }

    //取得domain Jar檔裡有的 db table
    private List<String> getJarDbType() throws IOException {
        Set<String> set = new HashSet<String>();
        ZipFile zip = new ZipFile(DB_DOMAIN_JAR);
        Pattern ptn = Pattern.compile("tw/gov/moi/domain/(\\w+\\.hbm\\.xml)");
        Matcher matcher = null;
        for (Enumeration<? extends ZipEntry> enu = zip.entries(); enu.hasMoreElements();) {
            ZipEntry zipEntry = enu.nextElement();
            matcher = ptn.matcher(zipEntry.getName());
            if (matcher.find()) {
                set.add(matcher.group(1));
            }
        }
        return new ArrayList<String>(set);
    }

    //取得ds xml有用到的db table
    private Map<String, List<File>> getUseDsTable() throws DocumentException {
        File ff = new File(DS_XML_DIR);
        Map<String, List<File>> map = new HashMap<String, List<File>>();
        for (File f : ff.listFiles()) {
            Document doc = new SAXReader().read(f);
            Element root = (Element) doc.selectSingleNode("*");
            List<Object> list = root.selectNodes("*");
            for (Object o : list) {
                if (o instanceof org.dom4j.Namespace) {
                    continue;
                }
                Element e = (Element) o;
                List<Element> eee = e.selectNodes("*");
                for (Element e1 : eee) {
                    Attribute attr = e1.attribute("name");
                    if (attr != null) {
                        String table = attr.getValue();
                        this.putFileMap(table, f, map);
                    }
                }
            }
        }
        return map;
    }

    //取得hibernate.cfg有的db table
    private List<String> getMapConfigHbms() throws DocumentException {
        File f = new File(HIBERNATE_CFG);
        Document doc = new SAXReader().read(f);
        Element root = (Element) doc.selectSingleNode("*");
        List<Object> list = root.selectNodes("//hibernate-configuration/session-factory/mapping");
        Set<String> set = new HashSet<String>();
        for (Object o : list) {
            Element e = (Element) o;
            Attribute attr = e.attribute("resource");
            String val = attr.getValue();
            val = val.substring(val.lastIndexOf("/") + 1);
            set.add(val);
        }
        return new ArrayList<String>(set);
    }

    private void putFileMap(String table, File file, Map<String, List<File>> map) {
        List<File> list = new ArrayList<File>();
        if (map.containsKey(table)) {
            list = map.get(table);
        } else {
            map.put(table, list);
        }
        if (!list.contains(file)) {
            list.add(file);
        }
    }
}
