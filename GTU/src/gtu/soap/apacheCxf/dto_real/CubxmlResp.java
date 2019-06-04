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
@XmlType(propOrder = { "mwheader", "tranrs" })
@XmlRootElement(name = "CUBXML")
public class CubxmlResp {
    @XmlElement(name = "MWHEADER", nillable = true)
    Mwheader mwheader;
    @XmlElement(name = "TRANRS", nillable = true)
    Tranrs tranrs;

    public Mwheader getMwheader() {
        return mwheader;
    }

    public void setMwheader(Mwheader mwheader) {
        this.mwheader = mwheader;
    }

    public Tranrs getTranrs() {
        return tranrs;
    }

    public void setTranrs(Tranrs tranrs) {
        this.tranrs = tranrs;
    }

    @XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "MWHEADER", propOrder = { "msgid", "sourcechannel", "returncode", "returndesc", "returncodechannel",
            "txnseq", "o360seq" })
    public static class Mwheader {
        @XmlElement(name = "MSGID", nillable = true)
        String msgid;
        @XmlElement(name = "SOURCECHANNEL", nillable = true)
        String sourcechannel;
        @XmlElement(name = "RETURNCODE", nillable = true)
        String returncode;
        @XmlElement(name = "RETURNDESC", nillable = true)
        String returndesc;
        @XmlElement(name = "RETURNCODECHANNEL", nillable = true)
        String returncodechannel;
        @XmlElement(name = "TXNSEQ", nillable = true)
        String txnseq;
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

        public String getO360seq() {
            return o360seq;
        }

        public void setO360seq(String o360seq) {
            this.o360seq = o360seq;
        }
    }

    @XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "TRANRS", propOrder = { "hostjournalno", "hostpostingdate", "hosttimestamp", "customernumber",
            "customertype", "titlecode", "firstname", "surname", "englishaddress1", "englishaddress2", "address1",
            "address2", "jcicindustrycode", "industrygroup", "industrysubgroup", "industryclass", "industryxx",
            "postcode", "countrycode", "phoneno", "faxnumber", "businessphone", "mobilephonenumber", "nationalitycode",
            "idno", "idissuedate", "idplace", "idtype", "domesticrisk", "crossborderrisk", "vipcode", "restrictstatus",
            "homebranchno", "bankrelind", "finrelind", "txnrelind", "posfinhold", "aomanager", "withholdtaxtype",
            "statementfreq", "statementcycle", "statementday", "createdate", "phoneresext", "phonebusext",
            "wtcpostflag", "custentrysrc", "registeredpostcode", "registeredaddress1", "registeredaddress2",
            "registeredphoneno1", "registeredphoneext1", "emailaddress", "aoidno", "pdslawagreedate",
            "pdslawagreebranch", "custentrysrcn", "maritalstatus", "expirydate", "birthday", "customername",
            "lastmaintaindt", "lastmaintainbr", "lastmaintainteller", "corpcategory", "foreignoriginalid", "cbcode",
            "statementpostmethod", "suspectstatus", "sensitivitycategory", "occupancy", "personalcode", "businesscode",
            "agtype1", "agcode1", "aglevel1", "agtype2", "agcode2", "aglevel2", "assetsize", "guardtype", "guardid",
            "guardtype2", "guardid2", "enterprisecode", "companytype", "idno2", "newlawagree", "effectdate" })
    public static class Tranrs {
        @XmlElement(name = "HostJournalNo", nillable = true)
        String hostjournalno;
        @XmlElement(name = "HostPostingDate", nillable = true)
        String hostpostingdate;
        @XmlElement(name = "HostTimestamp", nillable = true)
        String hosttimestamp;
        @XmlElement(name = "CustomerNumber", nillable = true)
        String customernumber;
        @XmlElement(name = "CustomerType", nillable = true)
        String customertype;
        @XmlElement(name = "TitleCode", nillable = true)
        String titlecode;
        @XmlElement(name = "FirstName", nillable = true)
        String firstname;
        @XmlElement(name = "SurName", nillable = true)
        String surname;
        @XmlElement(name = "EnglishAddress1", nillable = true)
        String englishaddress1;
        @XmlElement(name = "EnglishAddress2", nillable = true)
        String englishaddress2;
        @XmlElement(name = "Address1", nillable = true)
        String address1;
        @XmlElement(name = "Address2", nillable = true)
        String address2;
        @XmlElement(name = "JCICIndustryCode", nillable = true)
        String jcicindustrycode;
        @XmlElement(name = "IndustryGroup", nillable = true)
        String industrygroup;
        @XmlElement(name = "IndustrySubGroup", nillable = true)
        String industrysubgroup;
        @XmlElement(name = "IndustryClass", nillable = true)
        String industryclass;
        @XmlElement(name = "IndustryXX", nillable = true)
        String industryxx;
        @XmlElement(name = "PostCode", nillable = true)
        String postcode;
        @XmlElement(name = "CountryCode", nillable = true)
        String countrycode;
        @XmlElement(name = "PhoneNo", nillable = true)
        String phoneno;
        @XmlElement(name = "FaxNumber", nillable = true)
        String faxnumber;
        @XmlElement(name = "BusinessPhone", nillable = true)
        String businessphone;
        @XmlElement(name = "MobilePhoneNumber", nillable = true)
        String mobilephonenumber;
        @XmlElement(name = "NationalityCode", nillable = true)
        String nationalitycode;
        @XmlElement(name = "IdNo", nillable = true)
        String idno;
        @XmlElement(name = "IdIssueDate", nillable = true)
        String idissuedate;
        @XmlElement(name = "IdPlace", nillable = true)
        String idplace;
        @XmlElement(name = "IdType", nillable = true)
        String idtype;
        @XmlElement(name = "DomesticRisk", nillable = true)
        String domesticrisk;
        @XmlElement(name = "CrossBorderRisk", nillable = true)
        String crossborderrisk;
        @XmlElement(name = "VIPCode", nillable = true)
        String vipcode;
        @XmlElement(name = "RestrictStatus", nillable = true)
        String restrictstatus;
        @XmlElement(name = "HomeBranchNo", nillable = true)
        String homebranchno;
        @XmlElement(name = "BankRelInd", nillable = true)
        String bankrelind;
        @XmlElement(name = "FinRelInd", nillable = true)
        String finrelind;
        @XmlElement(name = "TxnRelInd", nillable = true)
        String txnrelind;
        @XmlElement(name = "PosFInHold", nillable = true)
        String posfinhold;
        @XmlElement(name = "AOManager", nillable = true)
        String aomanager;
        @XmlElement(name = "WithholdTaxType", nillable = true)
        String withholdtaxtype;
        @XmlElement(name = "StatementFreq", nillable = true)
        String statementfreq;
        @XmlElement(name = "StatementCycle", nillable = true)
        String statementcycle;
        @XmlElement(name = "StatementDay", nillable = true)
        String statementday;
        @XmlElement(name = "CreateDate", nillable = true)
        String createdate;
        @XmlElement(name = "PhoneResExt", nillable = true)
        String phoneresext;
        @XmlElement(name = "PhoneBusExt", nillable = true)
        String phonebusext;
        @XmlElement(name = "WtcPostFlag", nillable = true)
        String wtcpostflag;
        @XmlElement(name = "CustEntrySrc", nillable = true)
        String custentrysrc;
        @XmlElement(name = "RegisteredPostCode", nillable = true)
        String registeredpostcode;
        @XmlElement(name = "RegisteredAddress1", nillable = true)
        String registeredaddress1;
        @XmlElement(name = "RegisteredAddress2", nillable = true)
        String registeredaddress2;
        @XmlElement(name = "RegisteredPhoneNo1", nillable = true)
        String registeredphoneno1;
        @XmlElement(name = "RegisteredPhoneExt1", nillable = true)
        String registeredphoneext1;
        @XmlElement(name = "EmailAddress", nillable = true)
        String emailaddress;
        @XmlElement(name = "AOIdNo", nillable = true)
        String aoidno;
        @XmlElement(name = "PDSLawAgreeDate", nillable = true)
        String pdslawagreedate;
        @XmlElement(name = "PDSLawAgreeBranch", nillable = true)
        String pdslawagreebranch;
        @XmlElement(name = "CustEntrySrcN", nillable = true)
        String custentrysrcn;
        @XmlElement(name = "MaritalStatus", nillable = true)
        String maritalstatus;
        @XmlElement(name = "ExpiryDate", nillable = true)
        String expirydate;
        @XmlElement(name = "Birthday", nillable = true)
        String birthday;
        @XmlElement(name = "CustomerName", nillable = true)
        String customername;
        @XmlElement(name = "LastMaintainDt", nillable = true)
        String lastmaintaindt;
        @XmlElement(name = "LastMaintainBr", nillable = true)
        String lastmaintainbr;
        @XmlElement(name = "LastMaintainTeller", nillable = true)
        String lastmaintainteller;
        @XmlElement(name = "CorpCategory", nillable = true)
        String corpcategory;
        @XmlElement(name = "ForeignOriginalId", nillable = true)
        String foreignoriginalid;
        @XmlElement(name = "CBCode", nillable = true)
        String cbcode;
        @XmlElement(name = "StatementPostMethod", nillable = true)
        String statementpostmethod;
        @XmlElement(name = "SuspectStatus", nillable = true)
        String suspectstatus;
        @XmlElement(name = "SensitivityCategory", nillable = true)
        String sensitivitycategory;
        @XmlElement(name = "Occupancy", nillable = true)
        String occupancy;
        @XmlElement(name = "PersonalCode", nillable = true)
        String personalcode;
        @XmlElement(name = "BusinessCode", nillable = true)
        String businesscode;
        @XmlElement(name = "AGType1", nillable = true)
        String agtype1;
        @XmlElement(name = "AGCode1", nillable = true)
        String agcode1;
        @XmlElement(name = "AGLevel1", nillable = true)
        String aglevel1;
        @XmlElement(name = "AGType2", nillable = true)
        String agtype2;
        @XmlElement(name = "AGCode2", nillable = true)
        String agcode2;
        @XmlElement(name = "AGLevel2", nillable = true)
        String aglevel2;
        @XmlElement(name = "AssetSize", nillable = true)
        String assetsize;
        @XmlElement(name = "GuardType", nillable = true)
        String guardtype;
        @XmlElement(name = "GuardId", nillable = true)
        String guardid;
        @XmlElement(name = "GuardType2", nillable = true)
        String guardtype2;
        @XmlElement(name = "GuardId2", nillable = true)
        String guardid2;
        @XmlElement(name = "EnterpriseCode", nillable = true)
        String enterprisecode;
        @XmlElement(name = "CompanyType", nillable = true)
        String companytype;
        @XmlElement(name = "IdNo2", nillable = true)
        String idno2;
        @XmlElement(name = "NewLawAgree", nillable = true)
        String newlawagree;
        @XmlElement(name = "EffectDate", nillable = true)
        String effectdate;

        public String getHostjournalno() {
            return hostjournalno;
        }

        public void setHostjournalno(String hostjournalno) {
            this.hostjournalno = hostjournalno;
        }

        public String getHostpostingdate() {
            return hostpostingdate;
        }

        public void setHostpostingdate(String hostpostingdate) {
            this.hostpostingdate = hostpostingdate;
        }

        public String getHosttimestamp() {
            return hosttimestamp;
        }

        public void setHosttimestamp(String hosttimestamp) {
            this.hosttimestamp = hosttimestamp;
        }

        public String getCustomernumber() {
            return customernumber;
        }

        public void setCustomernumber(String customernumber) {
            this.customernumber = customernumber;
        }

        public String getCustomertype() {
            return customertype;
        }

        public void setCustomertype(String customertype) {
            this.customertype = customertype;
        }

        public String getTitlecode() {
            return titlecode;
        }

        public void setTitlecode(String titlecode) {
            this.titlecode = titlecode;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public String getEnglishaddress1() {
            return englishaddress1;
        }

        public void setEnglishaddress1(String englishaddress1) {
            this.englishaddress1 = englishaddress1;
        }

        public String getEnglishaddress2() {
            return englishaddress2;
        }

        public void setEnglishaddress2(String englishaddress2) {
            this.englishaddress2 = englishaddress2;
        }

        public String getAddress1() {
            return address1;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }

        public String getAddress2() {
            return address2;
        }

        public void setAddress2(String address2) {
            this.address2 = address2;
        }

        public String getJcicindustrycode() {
            return jcicindustrycode;
        }

        public void setJcicindustrycode(String jcicindustrycode) {
            this.jcicindustrycode = jcicindustrycode;
        }

        public String getIndustrygroup() {
            return industrygroup;
        }

        public void setIndustrygroup(String industrygroup) {
            this.industrygroup = industrygroup;
        }

        public String getIndustrysubgroup() {
            return industrysubgroup;
        }

        public void setIndustrysubgroup(String industrysubgroup) {
            this.industrysubgroup = industrysubgroup;
        }

        public String getIndustryclass() {
            return industryclass;
        }

        public void setIndustryclass(String industryclass) {
            this.industryclass = industryclass;
        }

        public String getIndustryxx() {
            return industryxx;
        }

        public void setIndustryxx(String industryxx) {
            this.industryxx = industryxx;
        }

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        public String getCountrycode() {
            return countrycode;
        }

        public void setCountrycode(String countrycode) {
            this.countrycode = countrycode;
        }

        public String getPhoneno() {
            return phoneno;
        }

        public void setPhoneno(String phoneno) {
            this.phoneno = phoneno;
        }

        public String getFaxnumber() {
            return faxnumber;
        }

        public void setFaxnumber(String faxnumber) {
            this.faxnumber = faxnumber;
        }

        public String getBusinessphone() {
            return businessphone;
        }

        public void setBusinessphone(String businessphone) {
            this.businessphone = businessphone;
        }

        public String getMobilephonenumber() {
            return mobilephonenumber;
        }

        public void setMobilephonenumber(String mobilephonenumber) {
            this.mobilephonenumber = mobilephonenumber;
        }

        public String getNationalitycode() {
            return nationalitycode;
        }

        public void setNationalitycode(String nationalitycode) {
            this.nationalitycode = nationalitycode;
        }

        public String getIdno() {
            return idno;
        }

        public void setIdno(String idno) {
            this.idno = idno;
        }

        public String getIdissuedate() {
            return idissuedate;
        }

        public void setIdissuedate(String idissuedate) {
            this.idissuedate = idissuedate;
        }

        public String getIdplace() {
            return idplace;
        }

        public void setIdplace(String idplace) {
            this.idplace = idplace;
        }

        public String getIdtype() {
            return idtype;
        }

        public void setIdtype(String idtype) {
            this.idtype = idtype;
        }

        public String getDomesticrisk() {
            return domesticrisk;
        }

        public void setDomesticrisk(String domesticrisk) {
            this.domesticrisk = domesticrisk;
        }

        public String getCrossborderrisk() {
            return crossborderrisk;
        }

        public void setCrossborderrisk(String crossborderrisk) {
            this.crossborderrisk = crossborderrisk;
        }

        public String getVipcode() {
            return vipcode;
        }

        public void setVipcode(String vipcode) {
            this.vipcode = vipcode;
        }

        public String getRestrictstatus() {
            return restrictstatus;
        }

        public void setRestrictstatus(String restrictstatus) {
            this.restrictstatus = restrictstatus;
        }

        public String getHomebranchno() {
            return homebranchno;
        }

        public void setHomebranchno(String homebranchno) {
            this.homebranchno = homebranchno;
        }

        public String getBankrelind() {
            return bankrelind;
        }

        public void setBankrelind(String bankrelind) {
            this.bankrelind = bankrelind;
        }

        public String getFinrelind() {
            return finrelind;
        }

        public void setFinrelind(String finrelind) {
            this.finrelind = finrelind;
        }

        public String getTxnrelind() {
            return txnrelind;
        }

        public void setTxnrelind(String txnrelind) {
            this.txnrelind = txnrelind;
        }

        public String getPosfinhold() {
            return posfinhold;
        }

        public void setPosfinhold(String posfinhold) {
            this.posfinhold = posfinhold;
        }

        public String getAomanager() {
            return aomanager;
        }

        public void setAomanager(String aomanager) {
            this.aomanager = aomanager;
        }

        public String getWithholdtaxtype() {
            return withholdtaxtype;
        }

        public void setWithholdtaxtype(String withholdtaxtype) {
            this.withholdtaxtype = withholdtaxtype;
        }

        public String getStatementfreq() {
            return statementfreq;
        }

        public void setStatementfreq(String statementfreq) {
            this.statementfreq = statementfreq;
        }

        public String getStatementcycle() {
            return statementcycle;
        }

        public void setStatementcycle(String statementcycle) {
            this.statementcycle = statementcycle;
        }

        public String getStatementday() {
            return statementday;
        }

        public void setStatementday(String statementday) {
            this.statementday = statementday;
        }

        public String getCreatedate() {
            return createdate;
        }

        public void setCreatedate(String createdate) {
            this.createdate = createdate;
        }

        public String getPhoneresext() {
            return phoneresext;
        }

        public void setPhoneresext(String phoneresext) {
            this.phoneresext = phoneresext;
        }

        public String getPhonebusext() {
            return phonebusext;
        }

        public void setPhonebusext(String phonebusext) {
            this.phonebusext = phonebusext;
        }

        public String getWtcpostflag() {
            return wtcpostflag;
        }

        public void setWtcpostflag(String wtcpostflag) {
            this.wtcpostflag = wtcpostflag;
        }

        public String getCustentrysrc() {
            return custentrysrc;
        }

        public void setCustentrysrc(String custentrysrc) {
            this.custentrysrc = custentrysrc;
        }

        public String getRegisteredpostcode() {
            return registeredpostcode;
        }

        public void setRegisteredpostcode(String registeredpostcode) {
            this.registeredpostcode = registeredpostcode;
        }

        public String getRegisteredaddress1() {
            return registeredaddress1;
        }

        public void setRegisteredaddress1(String registeredaddress1) {
            this.registeredaddress1 = registeredaddress1;
        }

        public String getRegisteredaddress2() {
            return registeredaddress2;
        }

        public void setRegisteredaddress2(String registeredaddress2) {
            this.registeredaddress2 = registeredaddress2;
        }

        public String getRegisteredphoneno1() {
            return registeredphoneno1;
        }

        public void setRegisteredphoneno1(String registeredphoneno1) {
            this.registeredphoneno1 = registeredphoneno1;
        }

        public String getRegisteredphoneext1() {
            return registeredphoneext1;
        }

        public void setRegisteredphoneext1(String registeredphoneext1) {
            this.registeredphoneext1 = registeredphoneext1;
        }

        public String getEmailaddress() {
            return emailaddress;
        }

        public void setEmailaddress(String emailaddress) {
            this.emailaddress = emailaddress;
        }

        public String getAoidno() {
            return aoidno;
        }

        public void setAoidno(String aoidno) {
            this.aoidno = aoidno;
        }

        public String getPdslawagreedate() {
            return pdslawagreedate;
        }

        public void setPdslawagreedate(String pdslawagreedate) {
            this.pdslawagreedate = pdslawagreedate;
        }

        public String getPdslawagreebranch() {
            return pdslawagreebranch;
        }

        public void setPdslawagreebranch(String pdslawagreebranch) {
            this.pdslawagreebranch = pdslawagreebranch;
        }

        public String getCustentrysrcn() {
            return custentrysrcn;
        }

        public void setCustentrysrcn(String custentrysrcn) {
            this.custentrysrcn = custentrysrcn;
        }

        public String getMaritalstatus() {
            return maritalstatus;
        }

        public void setMaritalstatus(String maritalstatus) {
            this.maritalstatus = maritalstatus;
        }

        public String getExpirydate() {
            return expirydate;
        }

        public void setExpirydate(String expirydate) {
            this.expirydate = expirydate;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getCustomername() {
            return customername;
        }

        public void setCustomername(String customername) {
            this.customername = customername;
        }

        public String getLastmaintaindt() {
            return lastmaintaindt;
        }

        public void setLastmaintaindt(String lastmaintaindt) {
            this.lastmaintaindt = lastmaintaindt;
        }

        public String getLastmaintainbr() {
            return lastmaintainbr;
        }

        public void setLastmaintainbr(String lastmaintainbr) {
            this.lastmaintainbr = lastmaintainbr;
        }

        public String getLastmaintainteller() {
            return lastmaintainteller;
        }

        public void setLastmaintainteller(String lastmaintainteller) {
            this.lastmaintainteller = lastmaintainteller;
        }

        public String getCorpcategory() {
            return corpcategory;
        }

        public void setCorpcategory(String corpcategory) {
            this.corpcategory = corpcategory;
        }

        public String getForeignoriginalid() {
            return foreignoriginalid;
        }

        public void setForeignoriginalid(String foreignoriginalid) {
            this.foreignoriginalid = foreignoriginalid;
        }

        public String getCbcode() {
            return cbcode;
        }

        public void setCbcode(String cbcode) {
            this.cbcode = cbcode;
        }

        public String getStatementpostmethod() {
            return statementpostmethod;
        }

        public void setStatementpostmethod(String statementpostmethod) {
            this.statementpostmethod = statementpostmethod;
        }

        public String getSuspectstatus() {
            return suspectstatus;
        }

        public void setSuspectstatus(String suspectstatus) {
            this.suspectstatus = suspectstatus;
        }

        public String getSensitivitycategory() {
            return sensitivitycategory;
        }

        public void setSensitivitycategory(String sensitivitycategory) {
            this.sensitivitycategory = sensitivitycategory;
        }

        public String getOccupancy() {
            return occupancy;
        }

        public void setOccupancy(String occupancy) {
            this.occupancy = occupancy;
        }

        public String getPersonalcode() {
            return personalcode;
        }

        public void setPersonalcode(String personalcode) {
            this.personalcode = personalcode;
        }

        public String getBusinesscode() {
            return businesscode;
        }

        public void setBusinesscode(String businesscode) {
            this.businesscode = businesscode;
        }

        public String getAgtype1() {
            return agtype1;
        }

        public void setAgtype1(String agtype1) {
            this.agtype1 = agtype1;
        }

        public String getAgcode1() {
            return agcode1;
        }

        public void setAgcode1(String agcode1) {
            this.agcode1 = agcode1;
        }

        public String getAglevel1() {
            return aglevel1;
        }

        public void setAglevel1(String aglevel1) {
            this.aglevel1 = aglevel1;
        }

        public String getAgtype2() {
            return agtype2;
        }

        public void setAgtype2(String agtype2) {
            this.agtype2 = agtype2;
        }

        public String getAgcode2() {
            return agcode2;
        }

        public void setAgcode2(String agcode2) {
            this.agcode2 = agcode2;
        }

        public String getAglevel2() {
            return aglevel2;
        }

        public void setAglevel2(String aglevel2) {
            this.aglevel2 = aglevel2;
        }

        public String getAssetsize() {
            return assetsize;
        }

        public void setAssetsize(String assetsize) {
            this.assetsize = assetsize;
        }

        public String getGuardtype() {
            return guardtype;
        }

        public void setGuardtype(String guardtype) {
            this.guardtype = guardtype;
        }

        public String getGuardid() {
            return guardid;
        }

        public void setGuardid(String guardid) {
            this.guardid = guardid;
        }

        public String getGuardtype2() {
            return guardtype2;
        }

        public void setGuardtype2(String guardtype2) {
            this.guardtype2 = guardtype2;
        }

        public String getGuardid2() {
            return guardid2;
        }

        public void setGuardid2(String guardid2) {
            this.guardid2 = guardid2;
        }

        public String getEnterprisecode() {
            return enterprisecode;
        }

        public void setEnterprisecode(String enterprisecode) {
            this.enterprisecode = enterprisecode;
        }

        public String getCompanytype() {
            return companytype;
        }

        public void setCompanytype(String companytype) {
            this.companytype = companytype;
        }

        public String getIdno2() {
            return idno2;
        }

        public void setIdno2(String idno2) {
            this.idno2 = idno2;
        }

        public String getNewlawagree() {
            return newlawagree;
        }

        public void setNewlawagree(String newlawagree) {
            this.newlawagree = newlawagree;
        }

        public String getEffectdate() {
            return effectdate;
        }

        public void setEffectdate(String effectdate) {
            this.effectdate = effectdate;
        }
    }
}
