package gtu.xml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class W3CCreateXML {
    public static void main(String[] args) throws Exception {

        String[] data = { "123", "456", "789", "135" };// 資料
        String[] elmName = { "el", "el", "el", "el" };// 資料的Element Name

        String root = "root";
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element rootElement = document.createElement(root);
        document.appendChild(rootElement);
        for (int i = 1; i < data.length; i++) {// 建立xml
            Element em = document.createElement(elmName[i]);
            em.appendChild(document.createTextNode(data[i]));
            rootElement.appendChild(em);
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(System.out);
        transformer.transform(source, result);
    }
}