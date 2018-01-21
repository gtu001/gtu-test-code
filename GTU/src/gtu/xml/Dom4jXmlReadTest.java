package gtu.xml;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class Dom4jXmlReadTest {

    public static void main(String[] args) throws Exception {
        File file = new File("src/gtu/xml/simpleEform.xml");
        SAXReader reader = new SAXReader();
        reader.setEncoding("utf8");
        Document doc = reader.read(file);

        // 取得完整xml內容
        // System.out.println("root element = " + doc.getRootElement().asXML());

        // 取得檔案名稱
        System.out.println("doc name = " + doc.getName());

        // 取得路徑
        System.out.println("doc path = " + doc.getPath());
        System.out.println("unique paht = " + doc.getUniquePath());

        System.out.println("parent = " + doc.getParent());

        // 取得包夾的字串內容
        // System.out.println("string value = " + doc.getStringValue());

        System.out.println("text = " + doc.getText());

        // 是否有內容
        System.out.println("has content = " + doc.hasContent());

        System.out.println("node count = " + doc.nodeCount());

        // "/"後可放 '.', '..', '@', '*', <QName>
        List<Node> nodeList = doc.selectNodes("eform/*");
        for (Node node : nodeList) {
            System.out.println("# node name = " + node.getName());
            Element element = (Element) node;
            System.out.println("\tnode attribute count = " + element.attributeCount());

            for (Iterator it = element.attributeIterator(); it.hasNext();) {
                Attribute attribute = (Attribute) it.next();
                System.out.println("\tattribute = " + attribute);
            }

            for (Iterator it = element.elementIterator(); it.hasNext();) {
                Element elem = (Element) it.next();
                System.out.println("\telement name = " + elem.getName());
            }
        }

        // 直接取得某個位置的屬性
        List<Attribute> list = doc.selectNodes("//eform/activity/form/@id");
        System.out.println("form id = " + list);

        // 直接取得第幾個element
        Element element = (Element) doc.selectSingleNode("//eform/activity/form[2]/block/column[3]");
        System.out.println("element1 = " + element.asXML());

        // 直接取得某屬性為password的column
        Element element2 = (Element) doc.selectSingleNode("//eform/block/column[@id='password']");
        System.out.println("element2 = " + element2.asXML());

        // 直接取得包夾在內的文字element(但是不能換行)
        Element element3 = (Element) doc.selectSingleNode("//eform/block/column[form='請輸入帳號密碼']");
        System.out.println("element3 = " + element3.asXML());

        System.out.println("done...");
    }
}
