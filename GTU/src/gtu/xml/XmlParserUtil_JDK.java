package gtu.xml;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class XmlParserUtil_JDK {

    public static void main(String[] args) {
        XmlParserUtil_JDK test = new XmlParserUtil_JDK();
        StringBuffer sb=new StringBuffer();
        sb.append("<XMLString>");
        sb.append("<OP1>NEW_SUB_ACTIVATION</OP1>");
        sb.append("<OP2>KF2</OP2>");
        sb.append("<MSISDN>0926296918</MSISDN>");
        sb.append("<newMSISDN></newMSISDN>");
        sb.append("<STATUS>B</STATUS>");
        sb.append("<VAS1></VAS1>");
        sb.append("<VAS2></VAS2>");
        sb.append("<ROCID>A123456789</ROCID>");
        sb.append("<timestamp>20060505160000</timestamp>");
        sb.append("<CNTRID>7778</CNTRID>");
        sb.append("<BE></BE>");
        sb.append("<OPTIONAL></OPTIONAL>");
        sb.append("<DealerCode></DealerCode>");
        sb.append("<RATEPLAN1></RATEPLAN1>");
        sb.append("<RATEPLAN2></RATEPLAN2>");
        sb.append("</XMLString>");
        System.out.println(sb);
        
        Map<String, String> parsetoHashtable = test.parsetoHashtable(sb.toString());
        System.out.println(parsetoHashtable);
    }

    public Map<String,String> parsetoHashtable(String xmlStr) {
        final Map<String,String> hXmlStr = new HashMap<String,String>();
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = spf.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setContentHandler(new DefaultHandler() {
                private String tag;

                public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
                    tag = qName;
                }

                public void characters(char[] ch, int start, int length) throws SAXException {
                    String str = new String(ch, start, length);
                    if (tag != null) {
                        if (hXmlStr.get(tag) != null) {
                            hXmlStr.put(tag, hXmlStr.get(tag) + str);
                            System.out.println("1---" + tag + " -- " + hXmlStr.get(tag) + " ,,, " + str);
                        } else {
                            hXmlStr.put(tag, str);
                            System.out.println("2---" + tag + " -- " + str);
                        }
                    }
                }

                public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
                    tag = null;
                }
            });
            if (xmlStr == null || xmlStr.equals("")) {
                hXmlStr.put("Exception", "InvalidXML");
            } else {
                xmlReader.parse(new InputSource(new StringReader(xmlStr)));
            }
        } catch (Exception e) {
            hXmlStr.put("Exception", "InvalidXML:[" + e + "]");
            e.printStackTrace();
        }
        return hXmlStr;
    }

}
