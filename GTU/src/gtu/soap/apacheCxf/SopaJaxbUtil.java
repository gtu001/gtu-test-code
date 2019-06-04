package com.cathaybk.invf.rest.soap.service;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.StringUtils;

public class SopaJaxbUtil {

    public static <T> String transferObjToXMLString(T obj) throws JAXBException {
        String xmlString;
        JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "big5");

        StringWriter sw = new StringWriter();
        marshaller.marshal(obj, sw);
        xmlString = sw.toString();

        return xmlString;
    }

    public static <T> T transferSoapXMLStringToObj(String xmlString, String replaceStr, Class<T> clazz) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader reader = new StringReader(StringUtils.replace(xmlString, replaceStr, ""));
        T obj = (T) unmarshaller.unmarshal(reader);
        return obj;
    }
}
