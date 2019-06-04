
package gtu.soap.apacheCxf.dto;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the tw.com.cathaybk.webservice package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: tw.com.cathaybk.webservice
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link XServiceMethod }
     * 
     */
    public XServiceMethod createXServiceMethod() {
        return new XServiceMethod();
    }

    /**
     * Create an instance of {@link XServiceMethodResponse }
     * 
     */
    public XServiceMethodResponse createXServiceMethodResponse() {
        return new XServiceMethodResponse();
    }

    /**
     * Create an instance of {@link ECHO }
     * 
     */
    public ECHO createECHO() {
        return new ECHO();
    }

    /**
     * Create an instance of {@link ECHOResponse }
     * 
     */
    public ECHOResponse createECHOResponse() {
        return new ECHOResponse();
    }

}
