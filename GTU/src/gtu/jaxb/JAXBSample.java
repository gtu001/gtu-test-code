package gtu.jaxb;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.junit.Test;

public class JAXBSample {

    @XmlRootElement//其實有這個就可以了 , 其他anno似乎不用
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "person", propOrder = { "personId", "personName" })
    static class Person {
        @XmlElement(name = "PersonId", required = true)
        private String personId;
        @XmlElement(name = "PersonName", required = true)
        private String personName;
    }
    
    public static void main(String[] args) throws JAXBException{
        JAXBSample sample = new JAXBSample();
        sample.testObjectToXML();
        System.out.println("done...");
    }

    @Test
    public void testObjectToXML() throws JAXBException {
        Person person = new Person();
        person.personId = "gtu001";
        person.personName = "Troy";
        
        JAXBContext context = JAXBContext.newInstance(Person.class);
        Marshaller mars = context.createMarshaller();
        mars.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        StringWriter swriter = new StringWriter();
        mars.marshal(person, swriter);
        System.out.println(swriter.getBuffer());
        System.out.println("----------------------------------------------------------");
        
        StringReader sreader = new StringReader(swriter.getBuffer().toString());
        Unmarshaller unmars = context.createUnmarshaller();
        Person person2 = (Person) unmars.unmarshal(sreader);
        System.out.println(ReflectionToStringBuilder.toString(person2));
    }
}
