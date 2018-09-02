package gtu.xml.xstream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Date;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class MainTest {

    public void testParseStringToXml() throws IOException {
        XStream xstream1 = new XStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        xstream1.toXML(new TestObj(), writer);
        String xml2 = outputStream.toString("UTF-8");
        System.out.println(xml2);
    }

    public void testXml2Object() {
        XStream xstream = new XStream(new DomDriver());
        xstream.alias("person", Person.class);
        xstream.alias("phonenumber", PhoneNumber.class);
        xstream.registerConverter(new DateConverter());
        String xml = xstream.toXML(getJoe());
        System.out.println(xml);
        System.out.println("--------------------");
        Person newJoe = (Person) xstream.fromXML(xml);
        System.out.println(newJoe.toString());
        System.out.println("--------------------");
    }

    public void testAnnotationToXml() {
        XStream xstream = new XStream(new DomDriver());
        xstream = new XStream(new DomDriver());
        xstream.processAnnotations(Person.class);
        xstream.processAnnotations(PhoneNumber.class);
        xstream.toXML(getJoe(), System.out);
        System.out.println("--------------------");
    }

    @URLToImageTest
    public void testObject2Xml() throws IOException, ClassNotFoundException {
        XStream xstream = new XStream(new DomDriver());
        StringWriter swriter = new StringWriter();
        ObjectOutputStream out = xstream.createObjectOutputStream(swriter);
        out.writeObject(getJoe());
        out.flush();
        out.close();
        System.out.println(swriter);
        ByteArrayInputStream bais = new ByteArrayInputStream(swriter.getBuffer().toString().getBytes());
        ObjectInputStream input = xstream.createObjectInputStream(bais);
        Person person = (Person) input.readObject();
        System.out.println(person);
    }

    public Person getJoe() {
        Person joe = new Person("Joe", "Walnes");
        joe.setPhone(new PhoneNumber(123, "1234-456"));
        joe.setFax(new PhoneNumber(123, "9999-999"));
        joe.setBirthDate(new Date());
        joe.setRemark("remark");
        Person f1 = new Person("Steven", "Jobs");
        joe.setPhone(new PhoneNumber(144, "1234-555"));
        joe.setFamily(Arrays.asList(f1));
        return joe;
    }

    static class TestObj {
        String testStr;
        int testInt;
    }
}
