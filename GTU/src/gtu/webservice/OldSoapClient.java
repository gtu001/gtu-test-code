package gtu.webservice;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * @author Troy 2009/02/02
 * 
 */
public class OldSoapClient {
    private static Logger logger = Logger.getLogger(OldSoapClient.class);

    public static void main(String[] args) {

        try {
            byte[] payload = OldSoapClient.genIdVerifyXml("081536", "LB081536").getBytes("UTF-8");
            // byte[] payload =
            // XmlGenerator.genIdInquiryXml("081536").getBytes("UTF-8");
            // byte[] payload =
            // XmlGenerator.genIdInquiryExXml("081536").getBytes("UTF-8");

            URL url = new URL("http://adws.landbankt.com.tw/WebLdap/UserService.asmx");

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            con.setRequestProperty("Content-Length", Integer.toString(payload.length));

            con.setRequestProperty("SOAPAction", "http://microsoft.com/taiwan/mcs/IdVerify");
            // con.setRequestProperty("SOAPAction","http://microsoft.com/taiwan/mcs/IdInquiry");
            // con.setRequestProperty("SOAPAction","http://microsoft.com/taiwan/mcs/IdInquiryEx");

            String payloadStr = new String(payload, "UTF-8");

            OutputStream os = con.getOutputStream();

            logger.debug("Send:" + payloadStr);

            os.write(payload);
            os.flush();
            os.close();

            InputStream is = con.getInputStream();
            DataInputStream dis = new DataInputStream(is);

            byte[] buf = new byte[8 * 1024];
            StringBuffer sb = new StringBuffer();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int ret = 0;
            byte[] content;
            while ((ret = dis.read(buf, 0, buf.length)) > -1) {
                content = new byte[ret];
                System.arraycopy(buf, 0, content, 0, ret);
                baos.write(content);
            }
            baos.flush();
            baos.close();
            sb.append(new String(baos.toByteArray(), "UTF-8"));
            logger.debug("Received: " + new String(baos.toByteArray(), "UTF-8"));
            logger.debug("------------" + (baos.toByteArray().length));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static String genIdInquiryXml(String sId) {
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
        sb.append("<soap:Body>");
        sb.append("<IdInquiry xmlns=\"http://microsoft.com/taiwan/mcs\">");
        sb.append("<id>" + sId + "</id>");
        sb.append("</IdInquiry>");
        sb.append("</soap:Body>");
        sb.append("</soap:Envelope>");
        logger.debug(sb.toString());
        return sb.toString();
    }

    public static String genIdInquiryExXml(String sId) {

        // POST /WebLdap/UserService.asmx HTTP/1.1
        // Host: adws.landbankt.com.tw
        // Content-Type: text/xml; charset=utf-8
        // Content-Length: length
        // SOAPAction: "http://microsoft.com/taiwan/mcs/IdInquiryEx"
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
        sb.append("<soap:Body>");
        sb.append("<IdInquiryEx xmlns=\"http://microsoft.com/taiwan/mcs\">");
        sb.append("<id>" + sId + "</id>");
        sb.append("</IdInquiryEx>");
        sb.append("</soap:Body>");
        sb.append("</soap:Envelope>");
        logger.debug(sb.toString());
        return sb.toString();
    }

    public static String genIdVerifyXml(String sId, String sPassword) {
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
        sb.append("<soap:Body>");
        sb.append("<IdVerify xmlns=\"http://microsoft.com/taiwan/mcs\">");
        sb.append("<id>" + sId + "</id>");
        sb.append("<password>" + sPassword + "</password>");
        sb.append("</IdVerify>");
        sb.append("</soap:Body>");
        sb.append("</soap:Envelope>");
        logger.debug(sb.toString());
        return sb.toString();
    }

    public static String genAggregateInquiryXml(String sDepartment, String sGroupName) {
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
        sb.append("<soap:Body>");
        sb.append("<AggregateInquiry xmlns=\"http://microsoft.com/taiwan/mcs\">");
        sb.append("<department>" + sDepartment + "</department>");
        sb.append("<groupName>" + sGroupName + "</groupName>");
        sb.append("</AggregateInquiry>");
        sb.append("</soap:Body>");
        sb.append("</soap:Envelope>");
        logger.debug(sb.toString());
        return sb.toString();
    }

    public static String genAddMemberXml(String sId, String sGroupName) {
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
        sb.append("<soap:Body>");
        sb.append("<AddMember xmlns=\"http://microsoft.com/taiwan/mcs\">");
        sb.append("<id>" + sId + "</id>");
        sb.append("<groupName>" + sGroupName + "</groupName>");
        sb.append("</AddMember>");
        sb.append("</soap:Body>");
        sb.append("</soap:Envelope>");
        logger.debug(sb.toString());
        return sb.toString();
    }

    public static String genRemoveMemberXml(String sId, String sGroupName) {
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
        sb.append("<soap:Body>");
        sb.append("<RemoveMember xmlns=\"http://microsoft.com/taiwan/mcs\">");
        sb.append("<id>" + sId + "</id>");
        sb.append("<groupName>" + sGroupName + "</groupName>");
        sb.append("</RemoveMember>");
        sb.append("</soap:Body>");
        sb.append("</soap:Envelope>");
        logger.debug(sb.toString());
        return sb.toString();
    }

    public static String parseIdInquiryResult(String sXml) {
        StringBuffer sb = new StringBuffer();

        // <?xml version="1.0" encoding="utf-8"?>
        // <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
        // xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        // xmlns:xsd="http://www.w3.org/2001/XMLSchema"
        // xmlns:wsa="http://schemas.xmlsoap.org/ws/2004/08/addressing">
        // <soap:Header>
        // <wsa:Action>http://microsoft.com/taiwan/mcs/IdInquiryResponse</wsa:Action>
        // <wsa:MessageID>urn:uuid:bf11db78-c134-43f5-9127-026cf97554f5</wsa:MessageID>
        // <wsa:RelatesTo>urn:uuid:0878fc8e-a752-4339-96ad-830dfd0bd416</wsa:RelatesTo>
        // <wsa:To>http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous</wsa:To>
        // </soap:Header>
        // <soap:Body>
        // <IdInquiryResponse xmlns="http://microsoft.com/taiwan/mcs">
        // <IdInquiryResult>
        // <Status>
        // <StatusCode>0</StatusCode>
        // <Severity>INFO</Severity>
        // </Status>
        // <Users>
        // <UserInfo>
        // <Id>081536</Id>
        // <DistinguishedName>CN=081536,CN=Users,DC=corpnet,DC=landbankt,DC=com,DC=tw</DistinguishedName>
        // <DisplayName>蕭鏡聲</DisplayName>
        // <Title>4</Title>
        // <Department>00H</Department>
        // <MemberOf>
        // <cn>ECARD</cn>
        // <cn>eBilling</cn>
        // <cn>Loan</cn>
        // <cn>Deposit</cn>
        // <cn>manager</cn>
        // <cn>lbuser</cn>
        // <cn>intra_salary</cn>
        // </MemberOf>
        // </UserInfo>
        // </Users>
        // </IdInquiryResult>
        // </IdInquiryResponse>
        // </soap:Body>
        // </soap:Envelope>
        return sb.toString();
    }

    public static String parseIdInquiryExResult(String sXml) {
        StringBuffer sb = new StringBuffer();
        // <?xml version="1.0" encoding="utf-8"?>
        // <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
        // xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        // xmlns:xsd="http://www.w3.org/2001/XMLSchema"
        // xmlns:wsa="http://schemas.xmlsoap.org/ws/2004/08/addressing">
        // <soap:Header>
        // <wsa:Action>http://microsoft.com/taiwan/mcs/IdInquiryExResponse</wsa:Action>
        // <wsa:MessageID>urn:uuid:b1a0ffa7-679c-4b73-9d94-5863e78e5163</wsa:MessageID>
        // <wsa:RelatesTo>urn:uuid:4773695d-c189-42d5-b912-c2577b3080ff</wsa:RelatesTo>
        // <wsa:To>http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous</wsa:To>
        // </soap:Header>
        // <soap:Body>
        // <IdInquiryExResponse xmlns="http://microsoft.com/taiwan/mcs">
        // <IdInquiryExResult>
        // <Status>
        // <StatusCode>403</StatusCode>
        // <Severity>ERROR</Severity><Desciption>Access
        // Denied!!</Desciption></Status><Users><UserInfoEx><Id>081536</Id></UserInfoEx></Users></IdInquiryExResult></IdInquiryExResponse></soap:Body></soap:Envelope>
        return sb.toString();
    }

    public static String parseIdVerifyResult(String sXml) {
        // <?xml version="1.0" encoding="utf-8"?>
        // <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
        // xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        // xmlns:xsd="http://www.w3.org/2001/XMLSchema"
        // xmlns:wsa="http://schemas.xmlsoap.org/ws/2004/08/addressing">
        // <soap:Header>
        // <wsa:Action>http://microsoft.com/taiwan/mcs/IdVerifyResponse</wsa:Action>
        // <wsa:MessageID>urn:uuid:e8c4fe9d-aa49-4750-bfbf-e379b2fa07c1</wsa:MessageID>
        // <wsa:RelatesTo>urn:uuid:19d1cdb0-2194-4830-9060-78da75b323c8</wsa:RelatesTo>
        // <wsa:To>http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous</wsa:To>
        // </soap:Header>
        // <soap:Body>
        // <IdVerifyResponse xmlns="http://microsoft.com/taiwan/mcs">
        // <IdVerifyResult>
        // <Status>
        // <StatusCode>0</StatusCode>
        // <Severity>INFO</Severity>
        // <Desciption>Logon Succeed.</Desciption>
        // </Status>
        // <Users>
        // <UserInfo>
        // <Id>081536</Id>
        // <DistinguishedName>CN=081536,CN=Users,DC=corpnet,DC=landbankt,DC=com,DC=tw</DistinguishedName>
        // <DisplayName>蕭鏡聲</DisplayName>
        // <Title>4</Title>
        // <Department>00H</Department>
        // <MemberOf>
        // <cn>ECARD</cn>
        // <cn>eBilling</cn>
        // <cn>Loan</cn>
        // <cn>Deposit</cn>
        // <cn>manager</cn>
        // <cn>lbuser</cn>
        // <cn>intra_salary</cn>
        // </MemberOf>
        // </UserInfo>
        // </Users>
        // </IdVerifyResult>
        // </IdVerifyResponse>
        // </soap:Body>
        // </soap:Envelope>
        String sRtn = "";
        try {
            logger.debug("parseIdVerifyResult(XML):" + sXml);
            DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
            Document doc = dfactory.newDocumentBuilder().parse(
                    new InputSource(new ByteArrayInputStream(sXml.getBytes())));

            NodeList nNodeList = XPathAPI.selectNodeList(doc, "IdVerifyResult/Status/StatusCode");
            // for (int i=0; i<nNodeList.getLength(); i++) {

            Node resultNode = nNodeList.item(0);
            logger.debug(resultNode.toString());

            sRtn = resultNode.getChildNodes().item(0).getNodeValue();
            // logger.debug("Node Name  = " + resultNode.getNodeName());
            // logger.debug("Node Value = " +
            // resultNode.getChildNodes().item(0).getNodeValue());
            // }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // return sRtn.equals("0") ? true : false;
        return sRtn;

        // <?xml version="1.0" encoding="utf-8"?>
        // <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        // xmlns:xsd="http://www.w3.org/2001/XMLSchema"
        // xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
        // <soap:Body>
        // <IdVerifyResponse xmlns="http://microsoft.com/taiwan/mcs">
        // <IdVerifyResult>
        // <Status>
        // <StatusCode>int</StatusCode>
        // <Severity>string</Severity>
        // <Desciption>string</Desciption>
        // </Status>
        // <Users>
        // <UserInfo>
        // <Id>string</Id>
        // <DistinguishedName>string</DistinguishedName>
        // <DisplayName>string</DisplayName>
        // <Title>string</Title>
        // <Department>string</Department>
        // <MemberOf xsi:nil="true" />
        // </UserInfo>
        // <UserInfo>
        // <Id>string</Id>
        // <DistinguishedName>string</DistinguishedName>
        // <DisplayName>string</DisplayName>
        // <Title>string</Title>
        // <Department>string</Department>
        // <MemberOf xsi:nil="true" />
        // </UserInfo>
        // </Users>
        // </IdVerifyResult>
        // </IdVerifyResponse>
        // </soap:Body>
        // </soap:Envelope>

    }
}
