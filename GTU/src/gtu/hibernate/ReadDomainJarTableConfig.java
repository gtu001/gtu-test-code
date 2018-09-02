package gtu.hibernate;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ReadDomainJarTableConfig {

    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        ReadDomainJarTableConfig test = new ReadDomainJarTableConfig();
        File file = new File("C:/Users/gtu001/.m2/repository/tw/gov/sris-db-domain/2.5.0-SRIS-SNAPSHOT/sris-db-domain-2.5.0-SRIS-SNAPSHOT.jar");
        test.execute(file, "Rldfz8930");
        System.out.println("pk=" + test.getPkColumns());
        System.out.println("col=" + test.getColumns());
        System.out.println("done...");
    }
    
    public void execute(File zipFile, String tableName){
        System.out.println("tableName =>" + tableName);
        StringBuffer xmlSb = readZipXml(zipFile, tableName);
        fetchColumn(xmlSb);
        writeXmlAndShow(tableName, xmlSb);
    }
    
    Set<String> pkColumns;
    Set<String> columns;
    boolean debug = false;
    
    private void writeXmlAndShow(String tableName, StringBuffer xmlSb){
        if(!debug){
            return;
        }
        try {
            File file = File.createTempFile(tableName, ".xml");
            FileUtils.writeStringToFile(file, xmlSb.toString(), "utf8");
            Runtime.getRuntime().exec(String.format("cmd /c call \"%s\"", file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private void fetchColumn(StringBuffer xmlSb){
        try {
            Document doc = new SAXReader().read(new StringReader(xmlSb.toString()));
            
            pkColumns = new HashSet<String>();
            columns = new HashSet<String>();
            
            List<Element> elemList1 = (List<Element>)doc.selectNodes("//hibernate-mapping/class/composite-id/key-property");
            for(Element e : elemList1){
                pkColumns.add(e.attributeValue("column"));
            }
            if(pkColumns.isEmpty()){
                String onlyPk = ((Element)doc.selectSingleNode("//hibernate-mapping/class/id")).attributeValue("column");
                pkColumns.add(onlyPk);
            }
            
            List<Element> elemList2 = (List<Element>)doc.selectNodes("//hibernate-mapping/class/property/column");
            for(Element e : elemList2){
                columns.add(e.attributeValue("name"));
            }
            columns.addAll(pkColumns);
        }catch(Exception ex){
            System.out.println("錯誤xml =>" + xmlSb);
            throw new RuntimeException(ex);
        }
    }

    private StringBuffer readZipXml(File zipFile, String tableName){
        try{
            ZipFile zip = new ZipFile(zipFile, "utf8");
            Enumeration<ZipEntry> en = zip.getEntries();

            Pattern pattern = Pattern.compile(tableName + "Type\\.hbm\\.xml", Pattern.CASE_INSENSITIVE);

            StringBuffer sb = new StringBuffer();
            while (en.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) en.nextElement();
                if (entry.isDirectory()) {
                    continue;
                }
                String name = entry.getName();
                if (name.endsWith("xml") && pattern.matcher(name).find()) {
                    System.out.println(name);
                    LineNumberReader reader = new LineNumberReader(new InputStreamReader(zip.getInputStream(entry), "utf8"));
                    for(String line = null; (line = reader.readLine())!=null;){
                        if(reader.getLineNumber() != 2){
                            sb.append(line + "\n");
                        }
                    }
                    reader.close();
                }
            }
            zip.close();
            return sb;
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public Set<String> getPkColumns() {
        return pkColumns;
    }

    public Set<String> getColumns() {
        return columns;
    }
}
