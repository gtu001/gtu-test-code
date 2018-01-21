package gtu.xml.xstream;

import org.junit.Test;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;

public class XStreamJSONSample {

    @Test
    public void testJsonByJettisonMappedXmlDriver() {
        System.out.println("------------------------------------------");
        System.out.println("testJsonByJettisonMappedXmlDriver");
        Person person = new MainTest().getJoe();
        XStream xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.alias("person", Person.class);
        xstream.alias("phoneNumber", PhoneNumber.class);
        xstream.toXML(person, System.out);
        System.out.println("------------------------------------------");
    }

    @Test
    public void testJsonByJsonHierarchicalStreamDriver() {
        System.out.println("------------------------------------------");
        System.out.println("testJsonByJsonHierarchicalStreamDriver");
        Person person = new MainTest().getJoe();
        XStream xstream = new XStream(new JsonHierarchicalStreamDriver());
        xstream.alias("person", Person.class);
        xstream.alias("phoneNumber", PhoneNumber.class);
        xstream.toXML(person, System.out);
        System.out.println("------------------------------------------");
    }
}
