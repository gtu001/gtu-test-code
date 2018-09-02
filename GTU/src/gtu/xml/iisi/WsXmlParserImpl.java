/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.xml.iisi;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Web Service XML轉換實作.
 * 
 * @param <X> 欲轉換的資料型態，可以是任意型態，但其所屬的package必需要有ObjectFactory或jaxb.index
 * @author Sandy Chiu
 */
public class WsXmlParserImpl {
    
    @XmlRootElement
    static class TestA {
        List<TestB> list = new ArrayList<TestB>();
        @Override
        public String toString() {
            return "TestA [list=" + list + "]";
        }
    }
    static class TestB{
        String aaa;
        String bbb;
        @Override
        public String toString() {
            return "TestB [aaa=" + aaa + ", bbb=" + bbb + "]";
        }
    }
    
    public static void main(String[] args) throws JAXBException, UnsupportedEncodingException{
        TestB b1 = new TestB();
        TestB b2 = new TestB();
        b1.aaa = "AAA1";
        b1.bbb = "BBB1";
        b2.aaa = "AAA2";
        b2.bbb = "BBB2";
        TestA a = new TestA();
        a.list.add(b1);
        a.list.add(b2);
        WsXmlParserImpl t = new WsXmlParserImpl(); 
        String message = t.objToXML(a);
        System.out.println("aaa==="+message);
        TestA aa = t.parseToObj(TestA.class, message);
        System.out.println("aaa==="+aa);
        System.out.println(aa);
    }

    private static Logger logger = LoggerFactory.getLogger(WsXmlParserImpl.class);

    @SuppressWarnings("unchecked")
    public <T> T parseToObj(Class<T> clazz, String str) throws JAXBException, UnsupportedEncodingException {
        JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
        Unmarshaller um = jaxbContext.createUnmarshaller();
        return (T) um.unmarshal(new ByteArrayInputStream(str.getBytes("UTF-8")));
    }

    public String objToXML(Object obj) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
        StringWriter writer = new StringWriter();
        final Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(obj, writer);
        return writer.toString();
    }
}
