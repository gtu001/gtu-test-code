package gtu.jaxb.fakesoap;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class FakeSoapServiceTest {

    private Logger logger = LoggerFactory.getLogger(FakeSoapServiceTest.class);

    @Value("${apex.webapi.url}")
    private String apexUrl;

    public static void main(String[] args) {
        FakeSoapServiceTest b = new FakeSoapServiceTest();

        b.apexUrl = "http://88.8.111.242/MiddlewareWebApi/api/Execute/";

        FakeSoapDataBean cubxml = new FakeSoapDataBean();
        FakeSoapDataBean.TRANRQ tranrq = new FakeSoapDataBean.TRANRQ();
        tranrq.setTranDate("20190307");
        tranrq.setOpenTxCode("1");
        cubxml.setTranrq(tranrq);

        FakeSoapDataBean bean = b.restXmlRequest("APEXSPTIP0023", FakeSoapDataBean.class, cubxml);

        System.out.println(ReflectionToStringBuilder.toString(bean, ToStringStyle.MULTI_LINE_STYLE));
    }

    public <T> T restXmlRequest(String msgId, Class<T> clz, T cubxml) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        logger.debug("in cubxmlApexSTip0001, request xml = {}", cubxml);
        System.out.println("in cubxmlApexSTip0001, request xml = " + cubxml);

        StringWriter sw = new StringWriter();
        JAXBContext jaxbContext = null;
        T unmarshal = null;

        try {
            // Obj to XMLString
            jaxbContext = JAXBContext.newInstance(clz);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(cubxml, sw);
            System.out.println("in cubxmlApexSTip0001, request xml = " + sw);

            // XMLString to Obj
            String responseXml = removeXmlns(httpRequestPost(msgId, sw.toString()));
            StringReader stringReader = new StringReader(responseXml);
            Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();
            unmarshal = (T) unMarshaller.unmarshal(stringReader);
            logger.debug("in cubxmlApexSTip0001, response xml = {}", unmarshal);
            System.out.println("in cubxmlApexSTip0001, response xml = " + responseXml);
            System.out.println("in cubxmlApexSTip0001, response xml = " + unmarshal);
        } catch (JAXBException e) {
            logger.error(e.getMessage(), e);
        }

        return unmarshal;
    }

    private String httpRequestPost(String msgId, String xmlContent) {
        StringBuilder sb = new StringBuilder(100);
        try {
            URL url = new URL(apexUrl + msgId);
            Map<String, Object> params = new LinkedHashMap<String,Object>();
            params.put("MessageContent", xmlContent);
            logger.debug("xmlContent = {} ", xmlContent);
            StringBuilder postData = new StringBuilder();

            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (postData.length() != 0)
                    postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }

            byte[] postDataBytes = postData.toString().getBytes("big5");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);

            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "big5"));

            for (int c; (c = in.read()) >= 0;) {
                sb.append((char) c);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return sb.toString();
    }

    private String removeXmlns(String originXml) {
        int namespaceIndex = originXml.indexOf("<CUBXML xmlns=");
        if (namespaceIndex > -1) {
            int index = originXml.indexOf('>', namespaceIndex);
            return originXml.substring(0, namespaceIndex) + "<CUBXML>" + originXml.substring(index + 1);
        }
        return originXml;
    }
}
