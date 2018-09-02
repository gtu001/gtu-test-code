/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gtu.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.iisigroup.ris.dbo.dbtool.action.ExportAction;
import com.iisigroup.ris.dbo.dbtool.action.ExportBatchAction;
import com.iisigroup.ris.dbo.dbtool.db.Column;
import com.iisigroup.ris.dbo.dbtool.db.Table;

/**
 * 
 * @author 1101526
 */
public class ExportXMLActionImpl implements ExportAction, ExportBatchAction {
    // public class ExportActionImpl {

    private static Map<String, String> TypeMap = new HashMap<String, String>();

    {
        if (TypeMap.size() == 0) {
            TypeMap.put("char", "string");
            TypeMap.put("varchar", "string");
            TypeMap.put("lvarchar", "string");
            TypeMap.put("int", "int");
            TypeMap.put("smallint", "int");
            TypeMap.put("int8", "int");
            TypeMap.put("date", "string");
            TypeMap.put("datetime", "string");
            TypeMap.put("blob", "string");

        }

    }

    @Override
    public void export(String domainName, String filePath, String packageStr, Table... tables) throws Exception {
        if (tables != null) {
            for (Table table : tables) {
                export(filePath, table, packageStr);
            }
        }
    }

    @Override
    public void export(String filePath, Table table, String packageStr) throws Exception {
        writexml(filePath, table, packageStr);
    }

    public void writexml(String filePath, Table table, String packageStr) throws Exception {

        String className = changeNameToClassName(table.getName());
        String packagePath = parsePackageToPath(packageStr);
        String filename = filePath + File.separator + packagePath + File.separator + className + ".hbm.xml";
        Document document = DocumentHelper.createDocument();
        document.addDocType("hibernate-mapping", "-//Hibernate/hibernate Mapping DTD 3.0//EN",
                "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd");
        Element rootE = document.addElement("hibernate-mapping");
        Element classE = rootE.addElement("class");
        classE.addAttribute("name", packageStr + "." + className);
        classE.addAttribute("table", table.getName());
        List<Column> pks = table.getPks();
        if (pks.size() > 1) {
            Element comIdE = classE.addElement("composite-id");
            Element keyE = null;
            Column keyCol = null;
            for (Iterator<Column> iterator = pks.iterator(); iterator.hasNext();) {
                keyCol = iterator.next();
                keyE = comIdE.addElement("key-property");
                keyE.addAttribute("name", changeColName(keyCol.getName()));
                keyE.addAttribute("type", TypeMap.get(keyCol.getType()));
                keyE.addAttribute("column", changeColName(keyCol.getName()));
                keyE.addAttribute("length", Integer.toString(keyCol.getLength()));
            }

        } else if (pks.size() == 1) {
            Column idCol = pks.get(0);
            Element idE = classE.addElement("id");
            idE.addAttribute("name", changeColName(idCol.getName()));
            idE.addAttribute("column", idCol.getName());
            idE.addAttribute("type", TypeMap.get(idCol.getType()));

        }
        List<Column> cols = table.getColumns();
        Element propertyE = null;
        Element colE = null;
        for (Iterator<Column> it = cols.iterator(); it.hasNext();) {
            Column column = it.next();
            propertyE = classE.addElement("property");
            propertyE.addAttribute("name", changeColName(column.getName()));
            propertyE.addAttribute("type", TypeMap.get(column.getType()));
            colE = propertyE.addElement("column");
            colE.addAttribute("name", changeColName(column.getName()));
            colE.addAttribute("length", Integer.toString(column.getLength()));

        }

        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");
        XMLWriter writer = null;
        try {
            String lPackagePath = StringUtils.join(new Object[] { filePath, File.separator, packagePath });
            File file = new File(lPackagePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            writer = new XMLWriter(new FileWriter(new File(filename)), format);
            writer.write(document);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }

    }

    private String changeColName(String colName) {
        String[] colNames = colName.split("_");
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String col : colNames) {
            if (i != 0) {
                sb.append(upperFirst(col));
            } else {
                sb.append(col.toLowerCase());
            }
            i++;
        }
        return sb.toString();
    }

    private String changeNameToClassName(String colName) {
        String[] colNames = colName.split("_");
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String col : colNames) {
            if (i != 0) {
                sb.append(upperFirst(col));
            } else {
                sb.append(col.toLowerCase());
            }
            i++;
        }

        return sb.toString().substring(0, 1).toUpperCase() + sb.toString();
    }

    private String upperFirst(String tmpStr) {
        String rtn = "";
        rtn = tmpStr.substring(0, 1).toUpperCase() + tmpStr.substring(1);
        return rtn;
    }

    private String parsePackageToPath(String packageStr) {
        String rtn = "";
        if (packageStr == null || packageStr.length() == 0) {
            return rtn;
        }
        String[] packages = packageStr.split("\\.", -1);

        StringBuilder sb = new StringBuilder();
        for (String tempP : packages) {
            sb.append(tempP + File.separator);
        }
        rtn = sb.toString();

        return rtn;
    }

    // public static void main(String[] args){
    // try {
    // Properties pro=new Properties();
    // pro.load(new FileInputStream(new
    // File(System.getProperty("user.dir")+File.separator+"db.properties")));
    // DBConnection.setDBProperties(pro);
    // DBParser dbp=new DBParser();
    // Table table=dbp.getTable("rldf004m");
    // ExportActionImpl et=new ExportActionImpl();
    // et.writexml(System.getProperty("user.dir"), table);
    // System.out.println(et.changeColName("col_first_name"));
    // } catch (Exception ex) {
    // Logger.getLogger(ExportActionImpl.class.getName()).log(Level.SEVERE,
    // null, ex);
    // }
    // }
    public static void main(String[] args) {
        // ExportXMLActionImpl test = new ExportXMLActionImpl();
        Document document = DocumentHelper.createDocument();
        document.addDocType("hibernate-mapping", "-//Hibernate/hibernate Mapping DTD 3.0//EN",
                "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd");
        Element rootE = document.addElement("hibernate-mapping");
        Element classE = rootE.addElement("class");
        classE.addAttribute("name", "com.iisigroup.com");
        classE.addAttribute("type", "string");
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");
        try {
            XMLWriter writer = new XMLWriter(new FileWriter(new File("test.xml")), format);
            writer.write(document);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(ExportXMLActionImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("done...");
    }

}
