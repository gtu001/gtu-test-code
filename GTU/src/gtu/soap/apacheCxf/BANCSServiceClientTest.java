package gtu.soap.apacheCxf;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang.StringUtils;

import com.cathaybk.invf.rest.soap.service.SopaJaxbUtil;

import gtu.soap.apacheCxf.dto_real.CubxmlReq;
import gtu.soap.apacheCxf.dto_real.CubxmlResp;
import gtu.soap.apacheCxf.dto_real.CubxmlResp.Tranrs;

public class BANCSServiceClientTest {

    public static void main(String[] args) throws JAXBException {
        URL wsdlURL = BANCSService.WSDL_LOCATION;

        BANCSService ss = new BANCSService(wsdlURL, BANCSService.SERVICE);
        BANCSServiceSoap port = ss.getBANCSService();

        StringBuilder sb = new StringBuilder();
        sb.append("<CUBXML xmlns=\"http://www.cathaybk.com.tw/webservice/FNSCIF0000/\">");
        sb.append("    <MWHEADER>");
        sb.append("        <MSGID>FNSCIF0000</MSGID>");
        sb.append("        <SOURCECHANNEL>IVT-NT-CTF-01</SOURCECHANNEL>");
        sb.append("        <TXNSEQ>F11FA6C7E9BE</TXNSEQ>");
        sb.append("        <RETURNCODE></RETURNCODE>");
        sb.append("        <RETURNDESC></RETURNDESC>");
        sb.append("        <RETURNCODECHANNEL></RETURNCODECHANNEL>");
        sb.append("        <O360SEQ></O360SEQ>");
        sb.append("    </MWHEADER>");
        sb.append("    <TRANRQ>");
        sb.append("        <BranchId>0004</BranchId>");
        sb.append("        <TellerId>00000</TellerId>");
        sb.append("        <FnsFlagX></FnsFlagX>");
        sb.append("        <IdNo>G220061599              </IdNo>");
        sb.append("        <IdType>11</IdType>");
        sb.append("    </TRANRQ>");
        sb.append("</CUBXML>");

        {
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
            tranrq.setBranchid("0004");
            tranrq.setTellerid("00000");
            tranrq.setFnsflagx("");
            tranrq.setIdtype("11");
            tranrq.setIdno(StringUtils.rightPad("G220061599", 24));
            
            String requestXML2 = SopaJaxbUtil.transferObjToXMLString(req);
            
            String appendXmlns = "http://www.cathaybk.com.tw/webservice/FNSCIF0000/";
            
            Pattern ptn = Pattern.compile("\\<CUBXML\\>", Pattern.DOTALL | Pattern.MULTILINE);
            Matcher mth = ptn.matcher(requestXML2);
            StringBuffer sb1 = new StringBuffer();
            while(mth.find()) {
                mth.appendReplacement(sb1, "<CUBXML " + "xmlns=\""+appendXmlns+"\">");
            }
            mth.appendTail(sb1);
            
            System.out.println("1----" + requestXML2);
            System.out.println("2----" + sb1);
            String requestXML = sb.toString();
            System.out.println("Invoking checkID...");
            String resultXml = port.xServiceMethod(sb1.toString());
            System.out.println("checkID.result=" + resultXml);

            CubxmlResp returnBean = SopaJaxbUtil.transferSoapXMLStringToObj(resultXml,
                "http://www.cathaybk.com.tw/webservice/FNSCIF0000/", CubxmlResp.class);
            Tranrs tranrs = returnBean.getTranrs();
            String result = tranrs.getTxnrelind();
            System.out.println("result : " + result);

            // TxnRelInd
        }
    }
}
