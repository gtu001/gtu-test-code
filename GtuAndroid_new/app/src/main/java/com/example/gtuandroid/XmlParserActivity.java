package com.example.gtuandroid;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class XmlParserActivity extends Activity {

    private static final String TAG = XmlParserActivity.class.getSimpleName();

    TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);

        LinearLayout contentView = createContentView();

        textView = new TextView(this);
        contentView.addView(textView);

        // SAX
        Button button1 = new Button(this);
        button1.setText("SAX");
        contentView.addView(button1);
        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                parseBySAX(getXmlFile());
            }
        });

        // DOM
        Button button2 = new Button(this);
        button2.setText("DOM");
        contentView.addView(button2);
        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                parseByDOM(getXmlFile());
            }
        });

        // XmlPullParser
        Button button3 = new Button(this);
        button3.setText("XmlPullParser");
        contentView.addView(button3);
        button3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                XmlEmployeeHandler handler = new XmlEmployeeHandler();
                List<Employee> parseList = handler.parse(getXmlFile());
                StringBuilder sb = new StringBuilder();
                for(Employee emp : parseList){
                    sb.append(emp + "\n");
                }
                textView.setText(sb.toString());
            }
        });
    }

    private InputStream getXmlFile() {
        InputStream fileIs = null;
        try {
            fileIs = getAssets().open("file.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileIs;
    }

    private void parseByDOM(InputStream fileIs) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document parse = builder.parse(fileIs);
            Element element = parse.getDocumentElement();
            element.normalize();

            StringBuilder sb = new StringBuilder();
            NodeList nList = element.getElementsByTagName("employee");
            for (int ii = 0; ii < nList.getLength(); ii++) {
                Node node = nList.item(ii);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element2 = (Element) node;
                    sb.append("name : " + getValue("name", element2) + "\n");
                    sb.append("salary : " + getValue("salary", element2) + "\n");
                }
            }
            textView.setText(sb.toString());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getValue(String tag, Element element) {
        NodeList childNodes = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = childNodes.item(0);
        return node.getNodeValue();
    }

    private void parseBySAX(final InputStream fileIs) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = factory.newSAXParser();
            final StringBuilder sb = new StringBuilder();
            DefaultHandler handler = new DefaultHandler() {
                boolean name = false;
                boolean age = false;

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    if (qName.equalsIgnoreCase("name")) {
                        name = true;
                    }
                    if (qName.equalsIgnoreCase("age")) {
                        age = true;
                    }
                }

                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                }

                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    if (name) {
                        String tagName = new String(ch, start, length);
                        sb.append("name :" + tagName + "\n");
                        name = false;
                    }
                    if (age) {
                        String tagAge = new String(ch, start, length);
                        sb.append("age :" + tagAge + "\n");
                        age = false;
                    }
                }
            };
            saxParser.parse(fileIs, handler);
            textView.setText(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseResponseXml1(InputStream inStream) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inStream, "UTF-8");
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
            case XmlPullParser.START_DOCUMENT:
                break;
            case XmlPullParser.START_TAG:
                String name = parser.getName();
                if ("getMobileCodeInfoParseIt".equals(name)) {
                    // TODO
                }
                break;
            case XmlPullParser.END_TAG:
                break;
            }
            eventType = parser.next();
        }
    }

    private class XmlEmployeeHandler {
        List<Employee> list = new ArrayList<Employee>();
        String text;
        Employee employee;

        public List<Employee> parse(InputStream is) {
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(is, null);

                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String tagname = parser.getName();
                    switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("employee")) {
                            employee = new Employee();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("employee")) {
                            list.add(employee);
                        } else if (tagname.equalsIgnoreCase("id")) {
                            employee.id = text;
                        } else if (tagname.equalsIgnoreCase("name")) {
                            employee.name = text;
                        } else if (tagname.equalsIgnoreCase("salary")) {
                            employee.salary = text;
                        }
                        break;
                    default:
                        break;
                    }
                    eventType = parser.next();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return list;
        }
    }

    private class Employee {
        String id;
        String name;
        String salary;

        public String toString() {
            return "id : " + id + ", name : " + name + ", salary : " + salary;
        }
    }

    private LinearLayout createContentView() {
        LinearLayout layout = new LinearLayout(this);
        ScrollView scroll = new ScrollView(this);
        setContentView(scroll);
        layout.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(layout, //
                new LayoutParams(LayoutParams.FILL_PARENT, // 設定與螢幕同寬
                        LayoutParams.WRAP_CONTENT));// 高度隨內容而定
        return layout;
    }
}
