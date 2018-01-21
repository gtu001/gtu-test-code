package _temp;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Test20_ReadXml {
    static class ReadObj {
        String Host ;
        String Port ;
        String User ;
        String Pass ;
        String Name ;
        @Override
        public String toString() {
            return "ReadObj (\"" + Host + "\", " + Port + ", \"" + User + "\", \"" + Pass + "\", \"" + Name
                    + "\"),//";
        }
    }

    public static void main(String[] args) throws DocumentException{
        File file = new File("G:\\FileZilla.xml");
        Document doc = new SAXReader().read(file);

        List<ReadObj> list = new ArrayList<ReadObj>();
        List<Element> nodeList = (List<Element>)doc.selectNodes("//FileZilla3/Servers/Folder/Server");
        for (Element element : nodeList) {
            ReadObj r = new ReadObj();
            for (Iterator it = element.elementIterator(); it.hasNext();) {
                Element elem = (Element) it.next();
                Field field = null;
                try {
                    field = ReadObj.class.getDeclaredField(elem.getName());
                    field.setAccessible(true);
                    field.set(r, elem.getText());
                } catch (Exception e) {
//                    System.out.println(e.getMessage() + " = " + (field != null ? field.getName() : ""));
                }
            }
            list.add(r);
        }
        
        for(ReadObj r : list){
            System.out.println(r);
        }
        System.out.println("done...");
    }
}
