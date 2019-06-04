package gtu.soap.apacheCxf.dto_real;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "mwheader", "tranrq" })
@XmlRootElement(name = "CUBXML")
public class CubxmlReq {
    @XmlElement(name = "MWHEADER", nillable = true)
    Mwheader mwheader;
    @XmlElement(name = "TRANRQ", nillable = true)
    Tranrq tranrq;

    public Mwheader getMwheader() {
        return mwheader;
    }

    public void setMwheader(Mwheader mwheader) {
        this.mwheader = mwheader;
    }

    public Tranrq getTranrq() {
        return tranrq;
    }

    public void setTranrq(Tranrq tranrq) {
        this.tranrq = tranrq;
    }

    @XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "MWHEADER", propOrder = { "msgid", "sourcechannel", "txnseq", "returncode", "returndesc",
            "returncodechannel", "o360seq" })
    public static class Mwheader {
        @XmlElement(name = "MSGID", nillable = true)
        String msgid;
        @XmlElement(name = "SOURCECHANNEL", nillable = true)
        String sourcechannel;
        @XmlElement(name = "TXNSEQ", nillable = true)
        String txnseq;
        @XmlElement(name = "RETURNCODE", nillable = true)
        String returncode;
        @XmlElement(name = "RETURNDESC", nillable = true)
        String returndesc;
        @XmlElement(name = "RETURNCODECHANNEL", nillable = true)
        String returncodechannel;
        @XmlElement(name = "O360SEQ", nillable = true)
        String o360seq;

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

        public String getTxnseq() {
            return txnseq;
        }

        public void setTxnseq(String txnseq) {
            this.txnseq = txnseq;
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

        public String getO360seq() {
            return o360seq;
        }

        public void setO360seq(String o360seq) {
            this.o360seq = o360seq;
        }
    }

    @XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "TRANRQ", propOrder = { "branchid", "tellerid", "fnsflagx", "idno", "idtype" })
    public static class Tranrq {
        @XmlElement(name = "BranchId", nillable = true)
        String branchid;
        @XmlElement(name = "TellerId", nillable = true)
        String tellerid;
        @XmlElement(name = "FnsFlagX", nillable = true)
        String fnsflagx;
        @XmlElement(name = "IdNo", nillable = true)
        String idno;
        @XmlElement(name = "IdType", nillable = true)
        String idtype;

        public String getBranchid() {
            return branchid;
        }

        public void setBranchid(String branchid) {
            this.branchid = branchid;
        }

        public String getTellerid() {
            return tellerid;
        }

        public void setTellerid(String tellerid) {
            this.tellerid = tellerid;
        }

        public String getFnsflagx() {
            return fnsflagx;
        }

        public void setFnsflagx(String fnsflagx) {
            this.fnsflagx = fnsflagx;
        }

        public String getIdno() {
            return idno;
        }

        public void setIdno(String idno) {
            this.idno = idno;
        }

        public String getIdtype() {
            return idtype;
        }

        public void setIdtype(String idtype) {
            this.idtype = idtype;
        }
    }
}
