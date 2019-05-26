//
// 此檔案是由 JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 所產生 
// 請參閱 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 一旦重新編譯來源綱要, 對此檔案所做的任何修改都將會遺失. 
// 產生時間: 2018.07.16 於 05:17:22 PM CST 
//


package com.gtu.rest.client.wms;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.cathaybk.invf.rest.client.wms package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {
    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.cathaybk.invf.rest.client.wms
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ServiceMethodRequest }
     * 
     */
    public ServiceMethodRequest createServiceMethodRequest() {
        return new ServiceMethodRequest();
    }

    /**
     * Create an instance of {@link ServiceMethodResponse }
     * 
     */
    public ServiceMethodResponse createServiceMethodResponse() {
        return new ServiceMethodResponse();
    }
}
