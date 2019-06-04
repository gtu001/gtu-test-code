package gtu.soap.apacheCxf;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import gtu.soap.apacheCxf.dto_real.CubxmlReq;
import gtu.soap.apacheCxf.dto_real.CubxmlResp;

@Service
public class BANCSServiceClient  {

    @Value("${ws.cif.url}")
    private String urlTr;//

    private String replaceStr = "http://www.cathaybk.com.tw/webservice/FNSCIF0000/";

    private static final Logger logger = LoggerFactory.getLogger(BANCSServiceClient.class);

    public CubxmlResp sendSoap(String branchId, String tellerid, String idType, String custId) {
        CubxmlReq req = new CubxmlReq();
        CubxmlReq.Mwheader header = new CubxmlReq.Mwheader();
        header.setMsgid("FNSCIF0000");
        header.setSourcechannel("IVT-NT-CTF-01");
        header.setTxnseq("F11FA6C7E9BE");
        header.setReturncode("");
        header.setReturncodechannel("");
        header.setReturndesc("");
        header.setO360seq("");
        req.setMwheader(header);
        CubxmlReq.Tranrq tranrq = new CubxmlReq.Tranrq();
        req.setTranrq(tranrq);
        tranrq.setBranchid(branchId);
        tranrq.setTellerid(tellerid);
        tranrq.setFnsflagx("");
        tranrq.setIdtype(idType);
        tranrq.setIdno(StringUtils.rightPad(custId, 24));

        try {
            CubxmlResp returnBean = sendSoap(req);
            return returnBean;
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    private CubxmlResp sendSoap(CubxmlReq req) throws JAXBException {

        String sendXml = SopaJaxbUtil.transferObjToXMLString(req);
        sendXml = this.replaceRequestXml(sendXml, replaceStr);

        logger.info("Send xml = {}", sendXml);

        URL wsdlURL = BANCSServiceClient.class.getResource("BANCSService.xml");

        BANCSService ss = new BANCSService(wsdlURL, BANCSService.SERVICE);
        BANCSServiceSoap port = ss.getBANCSService();

        // CheckIDResponse response = (CheckIDResponse)
        // mSoapConnector.callWebService(urlTr, req1);
        // String returnXml = response.getCheckIDResult();

        String returnXml = port.xServiceMethod(sendXml);
        logger.info("Return xml = " + returnXml);

        return (CubxmlResp) SopaJaxbUtil.transferSoapXMLStringToObj(returnXml,
            replaceStr, CubxmlResp.class);
    }

    private String replaceRequestXml(String requestXML2, String appendXmlns) {
        Pattern ptn = Pattern.compile("\\<CUBXML\\>", Pattern.DOTALL | Pattern.MULTILINE);
        Matcher mth = ptn.matcher(requestXML2);
        StringBuffer sb1 = new StringBuffer();
        while (mth.find()) {
            mth.appendReplacement(sb1, "<CUBXML " + "xmlns=\"" + appendXmlns + "\">");
        }
        mth.appendTail(sb1);
        return sb1.toString();
    }
}
