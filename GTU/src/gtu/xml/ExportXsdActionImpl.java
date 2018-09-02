package gtu.xml;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.iisigroup.ris.dbo.dbtool.action.ExportAction;
import com.iisigroup.ris.dbo.dbtool.action.ExportBatchAction;
import com.iisigroup.ris.dbo.dbtool.action.XsdConst;
import com.iisigroup.ris.dbo.dbtool.db.Column;
import com.iisigroup.ris.dbo.dbtool.db.DBParser;
import com.iisigroup.ris.dbo.dbtool.db.Table;
import com.iisigroup.ris.dbo.dbtool.util.GenerateUtils;

public class ExportXsdActionImpl implements ExportAction, ExportBatchAction, XsdConst {

    private Map<String, Element> elements = new HashMap<String, Element>();

    @Override
    public void export(String filePath, Table table, String packageStr) throws Exception {
        export(StringUtils.EMPTY, filePath, packageStr, table);
    }

    @Override
    public void export(String lDomainName, String filePath, String packageStr, Table... tables) throws Exception {
        String attributeName = StringUtils.EMPTY;
        String domainName = lDomainName;
        String packagePath = GenerateUtils.parsePackageToPath(packageStr);
        if (tables == null) {
            throw new Exception("tables not found");
        }
        if (StringUtils.isBlank(lDomainName) && tables.length > 1) {
            throw new Exception("domainName is required");
        }
        if (StringUtils.isBlank(lDomainName) && tables.length < 2) {
            domainName = GenerateUtils.changeColName(tables[0].getName());
        }
        String fullFileName = StringUtils.join(new Object[] { filePath, File.separator, File.separator, domainName,
                ".xsd" });

        Document document = DocumentHelper.createDocument();
        Element rootE = document.addElement("schema", "http://www.w3.org/2001/XMLSchema");
        rootE.addNamespace(String.format("%s", domainName),
                String.format("http://ris.iisigroup.com/%s/domain", domainName));
        rootE.addNamespace("ae", "http://ris.iisigroup.com/domain");
        rootE.addNamespace("jaxb", "http://java.sun.com/xml/ns/jaxb");
        rootE.addNamespace("xjc", "http://java.sun.com/xml/ns/jaxb/xjc");
        rootE.addNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        rootE.addAttribute("targetNamespace", String.format("http://ris.iisigroup.com/%s/domain", domainName));
        rootE.addAttribute("elementFormDefault", "qualified");
        rootE.addAttribute("jaxb:extensionBindingPrefixes", "xjc");
        rootE.addAttribute("jaxb:version", "1.0");

        Element importEl = rootE.addElement(XSD_IMPORT);
        importEl.addAttribute("namespace", "http://ris.iisigroup.com/domain");
        importEl.addAttribute("schemaLocation", "ris-ae.xsd");

        // loop
        for (Table table : tables) {
            attributeName = GenerateUtils.changeMainName(table.getName());
            Element complexType = rootE.addElement(XSD_COMPLEX_TYPE).addAttribute(NAME, attributeName);
            // Element extension =
            // complexType.addElement(XSD_COMPLEX_CONTEN).addElement(XSD_EXTENSION);
            Element sequence = complexType.addElement(XSD_SEQUENCE);
            processCols(rootE, table.getPks(), domainName, sequence);
            processCols(rootE, table.getColumns(), domainName, sequence);
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
            writer = new XMLWriter(new FileWriter(new File(fullFileName)), format);
            writer.write(document);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * process elements
     * 
     * @param columns
     * @param targetId
     */
    private void processCols(Element rootE, List<Column> columns, String lDomainName, Element targetId) {
        if (columns != null) {
            for (Column column : columns) {
                targetId.addElement(XsdConst.XSD_ELEMENT)
                        .addAttribute(XsdConst.NAME, GenerateUtils.changeColName(column.getName()))
                        .addAttribute(XsdConst.TYPE,
                                String.format("%s:%s", new Object[] { lDomainName, getType(column, rootE) }));
            }
        }
    }

    /**
     * generater simpleType
     * 
     * @param column
     * @param rootE
     * @return simpleTypeName
     */
    private String getType(Column column, Element rootE) {
        String newType = String.format("%s_%s", new Object[] { column.getType(), column.getLength() });
        if (!elements.containsKey(newType)) {
            String type = GenerateUtils.getType(column.getType());
            Element newSimpleType = rootE.addElement(XsdConst.XSD_SIMPLE_TYPE);
            Element restriction = newSimpleType.addAttribute(XsdConst.NAME, newType)
                    .addElement(XsdConst.XSD_RESTRICTION).addAttribute(XsdConst.BASE, type);
            if (StringUtils.equals(XSD_STRING, type)) {
                restriction.addElement(XsdConst.XSD_LENGTH).addAttribute(XsdConst.VALUE,
                        Integer.toString(column.getLength()));
            } else if (StringUtils.equals(XSD_INTEGER, type)) {
                restriction.addElement(XsdConst.XSD_PATTERN).addAttribute(XsdConst.VALUE,
                        String.format("\\d{0,%s}", column.getLength()));
            }
            elements.put(newType, newSimpleType);
        }
        return newType;
    }

    public static void main(String[] args) {
        try {
            DBParser dbp = new DBParser();
            Table table = dbp.getTable("ie_form_instance");
            new ExportXsdActionImpl().export(System.getProperty("user.dir"), table, "com.iisigroup.rl");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
