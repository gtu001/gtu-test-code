package gtu.xml;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XmlFormatter {

    public static String format(String inputXML) {
        try {
            Source xmlInput = new StreamSource(new StringReader(inputXML));
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            // initialize StreamResult with File object to save to file
            StreamResult result = new StreamResult(new StringWriter());
            // DOMSource source = new DOMSource(doc);
            transformer.transform(xmlInput, result);
            String xmlString = result.getWriter().toString();
            return xmlString;
        } catch (Throwable e) {
            throw new RuntimeException("format ERR : " + e.getMessage(), e);
        }
    }
}
