package gtu.jaxb.fakesoap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CUBXML")
public class FakeSoapDataBean {
    
    // request 電文
//    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
//    <CUBXML>
//        <MWHEADER>
//            <MSGID>APEXSPTIP0023</MSGID>
//            <SOURCECHANNEL></SOURCECHANNEL>
//            <RETURNCODE/>
//            <RETURNDESC/>
//            <TXNSEQ></TXNSEQ>
//            <SERVERSEQ/>
//        </MWHEADER>
//        <TRANRQ>
//            <TranDate>20190306</TranDate>
//            <TranTime></TranTime>
//            <OpenTxCode>A101770888</OpenTxCode>
//        </TRANRQ>
//    </CUBXML>

    // response 電文
 //   <CUBXML xmlns="http://www.cathaybk.com.tw/webservice/APEXSPTIP0023/">
 //   <MWHEADER>
 //      <MSGID>APEXSPTIP0023</MSGID>
 //      <SOURCECHANNEL>IVT-NT-ETF-01</SOURCECHANNEL>
 //      <RETURNCODE>0010</RETURNCODE>
 //      <RETURNDESC>查無資料</RETURNDESC>
 //      <RETURNCODECHANNEL/>
 //      <TXNSEQ>0</TXNSEQ>
 //      <O360SEQ/>
 //      <SERVERSEQ/>
 //   </MWHEADER>
 //   <TRANRS>
 //      <ProcessDate>20190306</ProcessDate>
 //      <ProcessTime>174541</ProcessTime>
 //      <CustId></CustId>
 //      <CustName></CustName>
 //      <IdNote></IdNote>
 //      <IdType></IdType>
 //      <IsEmployee></IsEmployee>
 //      <TotalBuyRecentYear>000000000000000</TotalBuyRecentYear>
 //      <AuthNo></AuthNo>
 //      <AuthNote></AuthNote>
 //      <OpenTxCode></OpenTxCode>
 //      <TradeDate/>
 //      <PublishDate/>
 //      <PublishTime/>
 //      <CheckDate/>
 //      <CheckTime/>
 //      <CheckMemberCode></CheckMemberCode>
 //      <ConfirmDate/>
 //      <ConfirmTime/>
 //      <ConfirmMemberCode></ConfirmMemberCode>
 //      <ConfirmStatus></ConfirmStatus>
 //      <TranSource></TranSource>
 //      <BranchID></BranchID>
 //      <Status></Status>
 //      <TradeType></TradeType>
 //      <TrustType></TrustType>
 //      <BondsId></BondsId>
 //      <BondsName></BondsName>
 //      <BondsCcy></BondsCcy>
 //      <TradePrice>0000000000000</TradePrice>
 //      <TrustUnit>0000000000000</TrustUnit>
 //      <TrustAmt>0000000000000</TrustAmt>
 //      <FeeAmount>0000000000000</FeeAmount>
 //      <TransactionStatus></TransactionStatus>
 //      <ProjectCode></ProjectCode>
 //      <FromAccount></FromAccount>
 //      <ChargeStatus></ChargeStatus>
 //      <SellBranchID></SellBranchID>
 //      <DiscountLevel></DiscountLevel>
 //      <DiscountRate>00000</DiscountRate>
 //      <BuyFeeRate>00000000</BuyFeeRate>
 //      <DiscountFxRateNote></DiscountFxRateNote>
 //      <ServiceMemberBranchID></ServiceMemberBranchID>
 //      <ServiceMember></ServiceMember>
 //      <TellerMember></TellerMember>
 //      <IntroductionMemberBranchID></IntroductionMemberBranchID>
 //      <IntroductionMember></IntroductionMember>
 //      <IntroductionMemberUnit></IntroductionMemberUnit>
 //      <CPCStatus></CPCStatus>
 //      <CPCCancelReason></CPCCancelReason>
 //      <CPCEmpId></CPCEmpId>
 //      <CPCTime/>
 //   </TRANRS>
 //</CUBXML>

    public FakeSoapDataBean() {
        String msgId = "APEXSPTIP0023";
        this.mwheader = new MWHEADER();
        mwheader.setMsgid(msgId);
    }

    @XmlElement(name = "MWHEADER")// request standard
    protected MWHEADER mwheader;

    @XmlElement(name = "TRANRQ")// request data
    protected TRANRQ tranrq;

    @XmlElement(name = "TRANRS")// response data
    protected TRANRS tranrs;

    @XmlTransient
    public MWHEADER getMwheader() {
        return mwheader;
    }

    public void setMwheader(MWHEADER mwheader) {
        this.mwheader = mwheader;
    }

    public TRANRQ getTranrq() {
        return (TRANRQ) tranrq;
    }

    public void setTranrq(TRANRQ tranrq) {
        this.tranrq = tranrq;
    }

    public TRANRS getTranrs() {
        return (TRANRS) tranrs;
    }

    public void setTranrs(TRANRS tranrs) {
        this.tranrs = tranrs;
    }
    
//  <TranDate>20190306</TranDate>
//  <TranTime></TranTime>
//  <OpenTxCode>A101770888</OpenTxCode>

    /**
     * @author E123474
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "tranDate", "tranTime", "openTxCode"})
    public static class TRANRQ {

        @XmlElement(name = "TranDate")
        protected String tranDate;

        @XmlElement(name = "TranTime")
        protected String tranTime;

        @XmlElement(name = "OpenTxCode")
        protected String openTxCode;

        public String getTranDate() {
            return tranDate;
        }

        public void setTranDate(String tranDate) {
            this.tranDate = tranDate;
        }

        public String getTranTime() {
            return tranTime;
        }

        public void setTranTime(String tranTime) {
            this.tranTime = tranTime;
        }

        public String getOpenTxCode() {
            return openTxCode;
        }

        public void setOpenTxCode(String openTxCode) {
            this.openTxCode = openTxCode;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class TRANRS {

        @XmlElement(name = "ProcessDate")
        protected int processDate;

        @XmlElement(name = "ProcessTime")
        protected int processTime;

        @XmlElement(name = "TrustAmount")
        protected String trustAmount;

        public int getProcessDate() {
            return processDate;
        }

        public void setProcessDate(int processDate) {
            this.processDate = processDate;
        }

        public int getProcessTime() {
            return processTime;
        }

        public void setProcessTime(int processTime) {
            this.processTime = processTime;
        }

        public String getTrustAmount() {
            return trustAmount;
        }

        public void setTrustAmount(String trustAmount) {
            this.trustAmount = trustAmount;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "msgid", "sourcechannel", "returncode", "returndesc", "returncodechannel",
            "txnseq", "o360SEQ", "serverSeq" })
    public static class MWHEADER {

        @XmlElement(name = "MSGID", nillable = true)
        protected String msgid;

        @XmlElement(name = "SOURCECHANNEL", nillable = true)
        protected String sourcechannel;

        @XmlElement(name = "RETURNCODE", nillable = true)
        protected String returncode;

        @XmlElement(name = "RETURNDESC", nillable = true)
        protected String returndesc;

        @XmlElement(name = "RETURNCODECHANNEL", nillable = true)
        protected String returncodechannel;

        @XmlElement(name = "TXNSEQ", nillable = true)
        protected String txnseq;

        @XmlElement(name = "O360SEQ", nillable = true)
        protected String o360SEQ;

        @XmlElement(name = "SERVERSEQ", nillable = true)
        protected String serverSeq;

        public String getMsgid() {
            return msgid;
        }

        public void setMsgid(String msgid) {
            this.msgid = msgid;
        }

        public String getSourcechannel() {
            return sourcechannel;
        }

        public void setSourcechannel(String sourcechannel) {
            this.sourcechannel = sourcechannel;
        }

        public String getReturncode() {
            return returncode;
        }

        public void setReturncode(String returncode) {
            this.returncode = returncode;
        }

        public String getReturndesc() {
            return returndesc;
        }

        public void setReturndesc(String returndesc) {
            this.returndesc = returndesc;
        }

        public String getReturncodechannel() {
            return returncodechannel;
        }

        public void setReturncodechannel(String returncodechannel) {
            this.returncodechannel = returncodechannel;
        }

        public String getTxnseq() {
            return txnseq;
        }

        public void setTxnseq(String txnseq) {
            this.txnseq = txnseq;
        }

        public String getO360SEQ() {
            return o360SEQ;
        }

        public void setO360SEQ(String o360SEQ) {
            this.o360SEQ = o360SEQ;
        }

        public String getServerSeq() {
            return serverSeq;
        }

        public void setServerSeq(String serverSeq) {
            this.serverSeq = serverSeq;
        }
    }
}
