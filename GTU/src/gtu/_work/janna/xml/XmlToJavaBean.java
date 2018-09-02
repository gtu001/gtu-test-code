package gtu._work.janna.xml;

import gtu._work.janna.xml.JavaBeanToXml.Attr;
import gtu._work.janna.xml.JavaBeanToXml.Attrib;
import gtu._work.janna.xml.JavaBeanToXml.Xml;
import gtu.log.Log;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class XmlToJavaBean {

    // TODO
    // TODO
    // TODO
    // TODO 未完成
    // TODO
    // TODO
    // TODO
    public static void main(String[] args) throws DocumentException {
        System.out.println(String.format("^\\s*<\\s?%1$s(\\s?|>).*<\\s?/\\s?%1$s\\s?>$", "XXXXX"));

        XmlToJavaBean test = new XmlToJavaBean();
        Xml xml = null;
        Attrib attr = null;

        String xmlContent = "<aaaa key=\"value\">vvvv</aaaa>";
        byte[] b = xmlContent.getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(b);

        SAXReader sax = new SAXReader();
        sax.read(bais);
        // System.out.println(ToStringUtil.toString(sax));

        Log.debug("test...1");
        Log.debug("test...2");
        Log.debug("test...3");
    }

    private Set<Class<?>> clzs = new HashSet<Class<?>>();

    public void registerJavaBean(Class<?> clz) {
        Attr attr = clz.getAnnotation(Attr.class);
        if (attr == null) {
            throw new UnsupportedOperationException("此class無定義Attr，不支援此操作");
        }
        clzs.add(clz);
        return;
    }

    public Object createJavaBean(String xmlstr) {
        this.content = this.replaceNextLine(xmlstr);
        Pattern pattern = null;
        Matcher matcher = null;
        Class<?> currentTag = null;
        for (Class<?> clz : clzs) {
            String tag = clz.getAnnotation(Attr.class).val();

        }
        return null;
    }

    void findTag(String tag) {
        Pattern pattern = null;
        Matcher matcher = null;
        pattern = Pattern.compile(String.format("^(?=\\s*)<(?=\\s?)%1$s(\\s|>|\\s\\w.*).*<\\s?/\\s?%1$s\\s?>(?=\\s*)$",
                tag));
        matcher = pattern.matcher(content);
        if (matcher.find()) {

        }
    }

    StringBuilder content;

    private StringBuilder replaceNextLine(String content) {
        StringBuilder sb = new StringBuilder();
        for (char c : content.toCharArray()) {
            if (!(c == '\r' || c == '\n' || c == '\t')) {
                sb.append(c);
            }
        }
        return sb;
    }

    private void read() throws MalformedURLException, DocumentException {
        File file = new File("src/gtu/xml/simpleEform.xml");
        Document doc = new SAXReader().read(file);

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
