package com.gtu.rest.sqlserver.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TradeJournalDTO {
    
    @JsonProperty(value="RowID")
    private long rowID;

    @JsonProperty(value="ApplicationID")
    private String applicationID;

    @JsonProperty(value="BalanceUnits")
    private BigDecimal balanceUnits;

    @JsonProperty(value="Bonus")
    private BigDecimal bonus;

    @JsonProperty(value="BonusFlg")
    private Short bonusFlg;

    @JsonProperty(value="BranchID")
    private String branchID;

    @JsonProperty(value="Canvasser")
    private String canvasser;

    @JsonProperty(value="CanvasserBranchID")
    private String canvasserBranchID;

    @JsonProperty(value="CDSC")
    private BigDecimal cdsc;

    @JsonProperty(value="CertifIsuDate")
    private String certifIsuDate;

    @JsonProperty(value="CertiNo")
    private Integer certiNo;

    @JsonProperty(value="CloseType")
    private Short closeType;

    @JsonProperty(value="ConfirmationNo")
    private String confirmationNo;

    @JsonProperty(value="CountCapital")
    private BigDecimal countCapital;

    @JsonProperty(value="CountUnits")
    private BigDecimal countUnits;

    @JsonProperty(value="CTFType")
    private Short CTFType;

    @JsonProperty(value="CustomerID")
    private String customerID;

    @JsonProperty(value="DealType")
    private String dealType;

    @JsonProperty(value="DistributeNo")
    private String distributeNo;

    @JsonProperty(value="DividendType")
    private Short dividendType;

    @JsonProperty(value="DrawBatchNo")
    private String drawBatchNo;

    @JsonProperty(value="DrawBatchSeq")
    private Integer drawBatchSeq;

    @JsonProperty(value="ExRate")
    private BigDecimal exRate;

    @JsonProperty(value="FeeBranchOrg")
    private BigDecimal feeBranchOrg;

    @JsonProperty(value="FeeBranchsettle")
    private BigDecimal feeBranchsettle;

    @JsonProperty(value="FeeFourPartOrg")
    private BigDecimal feeFourPartOrg;

    @JsonProperty(value="FeeInvCorpOrg")
    private BigDecimal feeInvCorpOrg;

    @JsonProperty(value="FeeInvCorpsettle")
    private BigDecimal feeInvCorpsettle;

    @JsonProperty(value="FeeTrustOrg")
    private BigDecimal feeTrustOrg;

    @JsonProperty(value="FeeTrustsettle")
    private BigDecimal feeTrustsettle;

    @JsonProperty(value="FourPartBranchID")
    private String fourPartBranchID;

    @JsonProperty(value="FourPartCanvasser")
    private String fourPartCanvasser;

    @JsonProperty(value="FourPartID")
    private String fourPartID;

    @JsonProperty(value="FourPartLicenceNo")
    private String fourPartLicenceNo;

    @JsonProperty(value="FromTrustCapital")
    private BigDecimal fromTrustCapital;

    @JsonProperty(value="FromUnits")
    private BigDecimal fromUnits;

    @JsonProperty(value="FundID")
    private String fundID;

    @JsonProperty(value="LastDealDate")
    private String lastDealDate;

    @JsonProperty(value="MemoRemark")
    private String memoRemark;

    @JsonProperty(value="Nav")
    private BigDecimal nav;

    @JsonProperty(value="OrderDate")
    private String orderDate;

    @JsonProperty(value="OrderNo")
    private String orderNo;

    @JsonProperty(value="OrgBankID")
    private String orgBankID;

    @JsonProperty(value="PayDate")
    private String payDate;

    @JsonProperty(value="PayType")
    private Short payType;

    @JsonProperty(value="PostageFee")
    private BigDecimal postageFee;

    @JsonProperty(value="PreRemainTrustCapital")
    private BigDecimal preRemainTrustCapital;

    @JsonProperty(value="PreRemainUnits")
    private BigDecimal preRemainUnits;

    @JsonProperty(value="ReApplicationID")
    private String reApplicationID;

    @JsonProperty(value="RedeemCount")
    private BigDecimal redeemCount;

    @JsonProperty(value="RemainTrustCapital")
    private BigDecimal remainTrustCapital;

    @JsonProperty(value="RemainUnits")
    private BigDecimal remainUnits;

    @JsonProperty(value="RemittanceCharge")
    private BigDecimal remittanceCharge;

    @JsonProperty(value="ReOrderDate")
    private String reOrderDate;

    @JsonProperty(value="ReTJID")
    private BigDecimal reTJID;

    @JsonProperty(value="SettleAmt")
    private BigDecimal settleAmt;

    @JsonProperty(value="SettleNTAmt")
    private BigDecimal settleNTAmt;

    @JsonProperty(value="SpecDiscID")
    private Short specDiscID;

    @JsonProperty(value="StaffRMK")
    private String staffRMK;

    @JsonProperty(value="SwitchExRate")
    private BigDecimal switchExRate;

    @JsonProperty(value="SwitchOrgAcct")
    private String switchOrgAcct;

    @JsonProperty(value="SwitchOrgCertifNo")
    private Integer switchOrgCertifNo;

    @JsonProperty(value="SwitchRefence")
    private String switchRefence;

    @JsonProperty(value="Tax")
    private BigDecimal tax;

    @JsonProperty(value="TJID")
    private BigDecimal tjid;

    @JsonProperty(value="TradeStatus")
    private Short tradeStatus;

    @JsonProperty(value="TrustAcct")
    private String trustAcct;

    @JsonProperty(value="TrustCapital")
    private BigDecimal trustCapital;

    @JsonProperty(value="TrustFee")
    private BigDecimal trustFee;

    @JsonProperty(value="TrustPeriodEd")
    private String trustPeriodEd;

    @JsonProperty(value="TrustPeriodSt")
    private String trustPeriodSt;

    @JsonProperty(value="TrustType")
    private Short trustType;

    @JsonProperty(value="Units")
    private BigDecimal units;

    @JsonProperty(value="UWCCBRefNo")
    private String UWCCBRefNo;

    @JsonProperty(value="WithDrawAcct")
    private String withDrawAcct;

    @JsonProperty(value="WithDrawCardNo")
    private String withDrawCardNo;

    public long getRowID() {
        return rowID;
    }

    public void setRowID(long rowID) {
        this.rowID = rowID;
    }

    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public BigDecimal getBalanceUnits() {
        return balanceUnits;
    }

    public void setBalanceUnits(BigDecimal balanceUnits) {
        this.balanceUnits = balanceUnits;
    }

    public BigDecimal getBonus() {
        return bonus;
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }

    public Short getBonusFlg() {
        return bonusFlg;
    }

    public void setBonusFlg(Short bonusFlg) {
        this.bonusFlg = bonusFlg;
    }

    public String getBranchID() {
        return branchID;
    }

    public void setBranchID(String branchID) {
        this.branchID = branchID;
    }

    public String getCanvasser() {
        return canvasser;
    }

    public void setCanvasser(String canvasser) {
        this.canvasser = canvasser;
    }

    public String getCanvasserBranchID() {
        return canvasserBranchID;
    }

    public void setCanvasserBranchID(String canvasserBranchID) {
        this.canvasserBranchID = canvasserBranchID;
    }

    public BigDecimal getCdsc() {
        return cdsc;
    }

    public void setCdsc(BigDecimal cdsc) {
        this.cdsc = cdsc;
    }

    public String getCertifIsuDate() {
        return certifIsuDate;
    }

    public void setCertifIsuDate(String certifIsuDate) {
        this.certifIsuDate = certifIsuDate;
    }

    public Integer getCertiNo() {
        return certiNo;
    }

    public void setCertiNo(Integer certiNo) {
        this.certiNo = certiNo;
    }

    public Short getCloseType() {
        return closeType;
    }

    public void setCloseType(Short closeType) {
        this.closeType = closeType;
    }

    public String getConfirmationNo() {
        return confirmationNo;
    }

    public void setConfirmationNo(String confirmationNo) {
        this.confirmationNo = confirmationNo;
    }

    public BigDecimal getCountCapital() {
        return countCapital;
    }

    public void setCountCapital(BigDecimal countCapital) {
        this.countCapital = countCapital;
    }

    public BigDecimal getCountUnits() {
        return countUnits;
    }

    public void setCountUnits(BigDecimal countUnits) {
        this.countUnits = countUnits;
    }

    public Short getCTFType() {
        return CTFType;
    }

    public void setCTFType(Short cTFType) {
        CTFType = cTFType;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }

    public String getDistributeNo() {
        return distributeNo;
    }

    public void setDistributeNo(String distributeNo) {
        this.distributeNo = distributeNo;
    }

    public Short getDividendType() {
        return dividendType;
    }

    public void setDividendType(Short dividendType) {
        this.dividendType = dividendType;
    }

    public String getDrawBatchNo() {
        return drawBatchNo;
    }

    public void setDrawBatchNo(String drawBatchNo) {
        this.drawBatchNo = drawBatchNo;
    }

    public Integer getDrawBatchSeq() {
        return drawBatchSeq;
    }

    public void setDrawBatchSeq(Integer drawBatchSeq) {
        this.drawBatchSeq = drawBatchSeq;
    }

    public BigDecimal getExRate() {
        return exRate;
    }

    public void setExRate(BigDecimal exRate) {
        this.exRate = exRate;
    }

    public BigDecimal getFeeBranchOrg() {
        return feeBranchOrg;
    }

    public void setFeeBranchOrg(BigDecimal feeBranchOrg) {
        this.feeBranchOrg = feeBranchOrg;
    }

    public BigDecimal getFeeBranchsettle() {
        return feeBranchsettle;
    }

    public void setFeeBranchsettle(BigDecimal feeBranchsettle) {
        this.feeBranchsettle = feeBranchsettle;
    }

    public BigDecimal getFeeFourPartOrg() {
        return feeFourPartOrg;
    }

    public void setFeeFourPartOrg(BigDecimal feeFourPartOrg) {
        this.feeFourPartOrg = feeFourPartOrg;
    }

    public BigDecimal getFeeInvCorpOrg() {
        return feeInvCorpOrg;
    }

    public void setFeeInvCorpOrg(BigDecimal feeInvCorpOrg) {
        this.feeInvCorpOrg = feeInvCorpOrg;
    }

    public BigDecimal getFeeInvCorpsettle() {
        return feeInvCorpsettle;
    }

    public void setFeeInvCorpsettle(BigDecimal feeInvCorpsettle) {
        this.feeInvCorpsettle = feeInvCorpsettle;
    }

    public BigDecimal getFeeTrustOrg() {
        return feeTrustOrg;
    }

    public void setFeeTrustOrg(BigDecimal feeTrustOrg) {
        this.feeTrustOrg = feeTrustOrg;
    }

    public BigDecimal getFeeTrustsettle() {
        return feeTrustsettle;
    }

    public void setFeeTrustsettle(BigDecimal feeTrustsettle) {
        this.feeTrustsettle = feeTrustsettle;
    }

    public String getFourPartBranchID() {
        return fourPartBranchID;
    }

    public void setFourPartBranchID(String fourPartBranchID) {
        this.fourPartBranchID = fourPartBranchID;
    }

    public String getFourPartCanvasser() {
        return fourPartCanvasser;
    }

    public void setFourPartCanvasser(String fourPartCanvasser) {
        this.fourPartCanvasser = fourPartCanvasser;
    }

    public String getFourPartID() {
        return fourPartID;
    }

    public void setFourPartID(String fourPartID) {
        this.fourPartID = fourPartID;
    }

    public String getFourPartLicenceNo() {
        return fourPartLicenceNo;
    }

    public void setFourPartLicenceNo(String fourPartLicenceNo) {
        this.fourPartLicenceNo = fourPartLicenceNo;
    }

    public BigDecimal getFromTrustCapital() {
        return fromTrustCapital;
    }

    public void setFromTrustCapital(BigDecimal fromTrustCapital) {
        this.fromTrustCapital = fromTrustCapital;
    }

    public BigDecimal getFromUnits() {
        return fromUnits;
    }

    public void setFromUnits(BigDecimal fromUnits) {
        this.fromUnits = fromUnits;
    }

    public String getFundID() {
        return fundID;
    }

    public void setFundID(String fundID) {
        this.fundID = fundID;
    }

    public String getLastDealDate() {
        return lastDealDate;
    }

    public void setLastDealDate(String lastDealDate) {
        this.lastDealDate = lastDealDate;
    }

    public String getMemoRemark() {
        return memoRemark;
    }

    public void setMemoRemark(String memoRemark) {
        this.memoRemark = memoRemark;
    }

    public BigDecimal getNav() {
        return nav;
    }

    public void setNav(BigDecimal nav) {
        this.nav = nav;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrgBankID() {
        return orgBankID;
    }

    public void setOrgBankID(String orgBankID) {
        this.orgBankID = orgBankID;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public Short getPayType() {
        return payType;
    }

    public void setPayType(Short payType) {
        this.payType = payType;
    }

    public BigDecimal getPostageFee() {
        return postageFee;
    }

    public void setPostageFee(BigDecimal postageFee) {
        this.postageFee = postageFee;
    }

    public BigDecimal getPreRemainTrustCapital() {
        return preRemainTrustCapital;
    }

    public void setPreRemainTrustCapital(BigDecimal preRemainTrustCapital) {
        this.preRemainTrustCapital = preRemainTrustCapital;
    }

    public BigDecimal getPreRemainUnits() {
        return preRemainUnits;
    }

    public void setPreRemainUnits(BigDecimal preRemainUnits) {
        this.preRemainUnits = preRemainUnits;
    }

    public String getReApplicationID() {
        return reApplicationID;
    }

    public void setReApplicationID(String reApplicationID) {
        this.reApplicationID = reApplicationID;
    }

    public BigDecimal getRedeemCount() {
        return redeemCount;
    }

    public void setRedeemCount(BigDecimal redeemCount) {
        this.redeemCount = redeemCount;
    }

    public BigDecimal getRemainTrustCapital() {
        return remainTrustCapital;
    }

    public void setRemainTrustCapital(BigDecimal remainTrustCapital) {
        this.remainTrustCapital = remainTrustCapital;
    }

    public BigDecimal getRemainUnits() {
        return remainUnits;
    }

    public void setRemainUnits(BigDecimal remainUnits) {
        this.remainUnits = remainUnits;
    }

    public BigDecimal getRemittanceCharge() {
        return remittanceCharge;
    }

    public void setRemittanceCharge(BigDecimal remittanceCharge) {
        this.remittanceCharge = remittanceCharge;
    }

    public String getReOrderDate() {
        return reOrderDate;
    }

    public void setReOrderDate(String reOrderDate) {
        this.reOrderDate = reOrderDate;
    }

    public BigDecimal getReTJID() {
        return reTJID;
    }

    public void setReTJID(BigDecimal reTJID) {
        this.reTJID = reTJID;
    }

    public BigDecimal getSettleAmt() {
        return settleAmt;
    }

    public void setSettleAmt(BigDecimal settleAmt) {
        this.settleAmt = settleAmt;
    }

    public BigDecimal getSettleNTAmt() {
        return settleNTAmt;
    }

    public void setSettleNTAmt(BigDecimal settleNTAmt) {
        this.settleNTAmt = settleNTAmt;
    }

    public Short getSpecDiscID() {
        return specDiscID;
    }

    public void setSpecDiscID(Short specDiscID) {
        this.specDiscID = specDiscID;
    }

    public String getStaffRMK() {
        return staffRMK;
    }

    public void setStaffRMK(String staffRMK) {
        this.staffRMK = staffRMK;
    }

    public BigDecimal getSwitchExRate() {
        return switchExRate;
    }

    public void setSwitchExRate(BigDecimal switchExRate) {
        this.switchExRate = switchExRate;
    }

    public String getSwitchOrgAcct() {
        return switchOrgAcct;
    }

    public void setSwitchOrgAcct(String switchOrgAcct) {
        this.switchOrgAcct = switchOrgAcct;
    }

    public Integer getSwitchOrgCertifNo() {
        return switchOrgCertifNo;
    }

    public void setSwitchOrgCertifNo(Integer switchOrgCertifNo) {
        this.switchOrgCertifNo = switchOrgCertifNo;
    }

    public String getSwitchRefence() {
        return switchRefence;
    }

    public void setSwitchRefence(String switchRefence) {
        this.switchRefence = switchRefence;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTjid() {
        return tjid;
    }

    public void setTjid(BigDecimal tjid) {
        this.tjid = tjid;
    }

    public Short getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(Short tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getTrustAcct() {
        return trustAcct;
    }

    public void setTrustAcct(String trustAcct) {
        this.trustAcct = trustAcct;
    }

    public BigDecimal getTrustCapital() {
        return trustCapital;
    }

    public void setTrustCapital(BigDecimal trustCapital) {
        this.trustCapital = trustCapital;
    }

    public BigDecimal getTrustFee() {
        return trustFee;
    }

    public void setTrustFee(BigDecimal trustFee) {
        this.trustFee = trustFee;
    }

    public String getTrustPeriodEd() {
        return trustPeriodEd;
    }

    public void setTrustPeriodEd(String trustPeriodEd) {
        this.trustPeriodEd = trustPeriodEd;
    }

    public String getTrustPeriodSt() {
        return trustPeriodSt;
    }

    public void setTrustPeriodSt(String trustPeriodSt) {
        this.trustPeriodSt = trustPeriodSt;
    }

    public Short getTrustType() {
        return trustType;
    }

    public void setTrustType(Short trustType) {
        this.trustType = trustType;
    }

    public BigDecimal getUnits() {
        return units;
    }

    public void setUnits(BigDecimal units) {
        this.units = units;
    }

    public String getUWCCBRefNo() {
        return UWCCBRefNo;
    }

    public void setUWCCBRefNo(String uWCCBRefNo) {
        UWCCBRefNo = uWCCBRefNo;
    }

    public String getWithDrawAcct() {
        return withDrawAcct;
    }

    public void setWithDrawAcct(String withDrawAcct) {
        this.withDrawAcct = withDrawAcct;
    }

    public String getWithDrawCardNo() {
        return withDrawCardNo;
    }

    public void setWithDrawCardNo(String withDrawCardNo) {
        this.withDrawCardNo = withDrawCardNo;
    }
    
    
}
