package gtu.xml;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.DOMReader;

public class XmlUtil {
    private static Logger logger = Logger.getLogger(XmlUtil.class);
    
    /**
     * converts a W3C DOM document into a dom4j document
     */
    public static Document buildDocment(org.w3c.dom.Document domDocument) {
//        DOMReader xmlReader = new DOMReader(factory);//可傳入 factory
        DOMReader xmlReader = new DOMReader();
        return xmlReader.read(domDocument);
    }
    
    /**
     * 從字串轉回Document物件
     */
    public static Document parseXmlString(String text) throws DocumentException {
        Document document = DocumentHelper.parseText(text);
        return document;
    }

    public static String getNodeValueByPath(Element root, String sPath, String sNameSpace) {
        String sRtn = "";
        try {
            HashMap xmlMap = new HashMap();
            xmlMap.put("ns", sNameSpace);

            XPath x = root.createXPath("//ns:" + sPath);
            x.setNamespaceURIs(xmlMap);

            Element element = (Element) x.selectSingleNode(root);

            sRtn = element.getText();
        } catch (NullPointerException ne) {
            sRtn = "";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sRtn;
    }

    public static List<Element> getNodeListByPath(Element root, String sPath, String sNameSpace) {
        List<Element> rtnList = new LinkedList<Element>();
        try {
            HashMap xmlMap = new HashMap();
            xmlMap.put("ns", sNameSpace);

            XPath x = root.createXPath("//ns:" + sPath);
            x.setNamespaceURIs(xmlMap);
            rtnList = x.selectNodes(root);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rtnList;
    }

    public static List<Element> getNodeListByArgs(Element root, String sPath, String sNameSpace, Object[] order) {
        List<Element> rtnList = new LinkedList<Element>();
        try {
            HashMap xmlMap = new HashMap();
            xmlMap.put("ns", sNameSpace);
            // logger.debug("######### 0223 SIZE:" + order.length);
            for (int ii = 0; ii < order.length; ii++) {
                // ns:UserInfo[ns:Id//text()='081536']

                // logger.debug("(" + ii + ")xPath:" + "//ns:" + sPath +
                // "[ns:Id//text()='" + order[ii].toString()+ "']");
                XPath x = root.createXPath("//ns:" + sPath + "[ns:Id//text()='" + order[ii].toString() + "']");
                x.setNamespaceURIs(xmlMap);

                Element element = (Element) x.selectSingleNode(root);
                if (element == null) {
                    logger.debug("Element Null:" + order[ii].toString());
                } else {
                    // logger.debug("#################:" + element.asXML());
                    rtnList.add(element);
                }
            }
            // rtnList = x.selectNodes(root);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rtnList;
    }
}
