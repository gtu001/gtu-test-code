package gtu.xml;

import java.io.File;
import java.util.List;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.dom4j.io.SAXReader;

public class Dom4jReadBulkFileTest {

    /**
     * @param args
     * @throws DocumentException 
     */
    public static void main(String[] args) throws DocumentException {
//      <object-stream>
//      <gtu.jpa.hibernate.PersonForNative>
//        <personid>F120174089</personid>
//        <birthYyymmdd>0530221</birthYyymmdd>
//        <siteId>65000170</siteId>
//        <count>0</count>
//      </gtu.jpa.hibernate.PersonForNative>
//      <gtu.jpa.hibernate.PersonForNative> 
//      ...很多 可能超過1G
        
      SAXReader reader = new SAXReader();
      reader.setDefaultHandler(new ElementHandler() {
          @Override
          public void onEnd(ElementPath arg0) {
              Element e = arg0.getCurrent(); //获得当前节点  
              if(e.getName().equals("gtu.jpa.hibernate.PersonForNative")) {
                 List<Element> elist = e.selectNodes("*");
                 //TODO 邏輯
                 e.detach(); //记得从内存中移去  
              }
          }
          @Override
          public void onStart(ElementPath arg0) {
          }
      });
      reader.read(new File("bulkBigFile.xml"));
    }

}
