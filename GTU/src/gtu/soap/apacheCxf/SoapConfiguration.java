package gtu.soap.apacheCxf;

import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.transport.HeadersAwareSenderWebServiceConnection;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpComponentsConnection;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

@Configuration
public class SoapConfiguration {

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        // this package must match the package in the <generatePackage> specified in
        // pom.xml
        marshaller.setContextPath("com.cathaybk.invf.rest.soap.dto");
        return marshaller;
    }

    public static class SoapConnector extends WebServiceGatewaySupport {
        public Object callWebService(String url, Object request) {
            HttpComponentsMessageSender sender = new HttpComponentsMessageSender();
            HttpClient httpClient = null;
            sender.setHttpClient(httpClient);
            getWebServiceTemplate().setMessageSender(sender);
            return getWebServiceTemplate().marshalSendAndReceive(url, request,
                new CustomMessageCallback("", "", "CIF6IDCNS/CheckID"));
        }
    }

    public static class CustomMessageCallback implements WebServiceMessageCallback {
        private String headerKey;
        private String headerValue;
        private String soapAction;

        public CustomMessageCallback(String headerKey, String headerValue, String soapAction) {
            this.headerKey = headerKey;
            this.headerValue = headerValue;
            this.soapAction = soapAction;
        }

        @Override
        public void doWithMessage(WebServiceMessage webServiceMessage) throws IOException, TransformerException {
            TransportContext context = TransportContextHolder.getTransportContext();
            {
                HttpComponentsConnection conn = (HttpComponentsConnection) context.getConnection();
                HttpPost post = conn.getHttpPost();
                // post.addHeader(headerKey, headerValue);
            }
            {
                HeadersAwareSenderWebServiceConnection conn = (HeadersAwareSenderWebServiceConnection) context
                    .getConnection();
                conn.addRequestHeader("SOAPAction", soapAction);
            }
        }
    }

    @Bean
    public SoapConnector soapConnector(Jaxb2Marshaller marshaller) {
        SoapConnector client = new SoapConnector();
        client.setMessageSender(new HttpComponentsMessageSender());
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }
}