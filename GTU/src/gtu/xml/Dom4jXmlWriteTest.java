package gtu.xml;

import gtu.file.FileUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.HTMLWriter;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class Dom4jXmlWriteTest {

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
            XMLWriter writer = new XMLWriter(new FileWriter(new File(FileUtil.DESKTOP_DIR, "test1.xml")), format);
            writer.write(document);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Dom4jXmlWriteTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            HTMLWriter writer = new HTMLWriter(new FileWriter(new File(FileUtil.DESKTOP_DIR, "test2.xml")), format);
            writer.write(document);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Dom4jXmlWriteTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("done...");
    }
}
