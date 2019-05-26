package com.gtu.rest.sqlserver.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 * The persistent class for the TradeJournal database table.
 * 
 */
@Entity
@NamedQuery(name = "TradeJournal.findAll", query = "SELECT t FROM TradeJournal t")
public class TradeJournal implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "RowID")
    private long rowID;

    @Column(name = "ApplicationID")
    private String applicationID;

    @Column(name = "BalanceUnits")
    private BigDecimal balanceUnits;

    @Column(name = "Bonus")
    private BigDecimal bonus;

    @Column(name = "BonusFlg")
    private short bonusFlg;

    @Column(name = "BranchID")
    private String branchID;

    @Column(name = "Canvasser")
    private String canvasser;

    @Column(name = "CanvasserBranchID")
    private String canvasserBranchID;

    @Column(name = "CDSC")
    private BigDecimal cdsc;

    @Column(name = "CertifIsuDate")
    private String certifIsuDate;

    @Column(name = "CertiNo")
    private int certiNo;

    @Column(name = "CloseType")
    private short closeType;

    @Column(name = "ConfirmationNo")
    private String confirmationNo;

    @Column(name = "CountCapital")
    private BigDecimal countCapital;

    @Column(name = "CountUnits")
    private BigDecimal countUnits;

    private short CTFType;

    @Column(name = "CustomerID")
    private String customerID;

    @Column(name = "DealType")
    private String dealType;

    @Column(name = "DistributeNo")
    private String distributeNo;

    @Column(name = "DividendType")
    private short dividendType;

    @Column(name = "DrawBatchNo")
    private String drawBatchNo;

    @Column(name = "DrawBatchSeq")
    private int drawBatchSeq;

    @Column(name = "ExRate")
    private BigDecimal exRate;

    @Column(name = "FeeBranchOrg")
    private BigDecimal feeBranchOrg;

    @Column(name = "FeeBranchsettle")
    private BigDecimal feeBranchsettle;

    @Column(name = "FeeFourPartOrg")
    private BigDecimal feeFourPartOrg;

    @Column(name = "FeeInvCorpOrg")
    private BigDecimal feeInvCorpOrg;

    @Column(name = "FeeInvCorpsettle")
    private BigDecimal feeInvCorpsettle;

    @Column(name = "FeeTrustOrg")
    private BigDecimal feeTrustOrg;

    @Column(name = "FeeTrustsettle")
    private BigDecimal feeTrustsettle;

    @Column(name = "FourPartBranchID")
    private String fourPartBranchID;

    @Column(name = "FourPartCanvasser")
    private String fourPartCanvasser;

    @Column(name = "FourPartID")
    private String fourPartID;

    @Column(name = "FourPartLicenceNo")
    private String fourPartLicenceNo;

    @Column(name = "FromTrustCapital")
    private BigDecimal fromTrustCapital;

    @Column(name = "FromUnits")
    private BigDecimal fromUnits;

    @Column(name = "FundID")
    private String fundID;

    @Column(name = "LastDealDate")
    private String lastDealDate;

    @Column(name = "MemoRemark")
    private String memoRemark;

    @Column(name = "Nav")
    private BigDecimal nav;

    @Column(name = "OrderDate")
    private String orderDate;

    @Column(name = "OrderNo")
    private String orderNo;

    @Column(name = "OrgBankID")
    private String orgBankID;

    @Column(name = "PayDate")
    private String payDate;

    @Column(name = "PayType")
    private short payType;

    @Column(name = "PostageFee")
    private BigDecimal postageFee;

    @Column(name = "PreRemainTrustCapital")
    private BigDecimal preRemainTrustCapital;

    @Column(name = "PreRemainUnits")
    private BigDecimal preRemainUnits;

    @Column(name = "ReApplicationID")
    private String reApplicationID;

    @Column(name = "RedeemCount")
    private BigDecimal redeemCount;

    @Column(name = "RemainTrustCapital")
    private BigDecimal remainTrustCapital;

    @Column(name = "RemainUnits")
    private BigDecimal remainUnits;

    @Column(name = "RemittanceCharge")
    private BigDecimal remittanceCharge;

    @Column(name = "ReOrderDate")
    private String reOrderDate;

    @Column(name = "ReTJID")
    private BigDecimal reTJID;

    @Column(name = "SettleAmt")
    private BigDecimal settleAmt;

    @Column(name = "SettleNTAmt")
    private BigDecimal settleNTAmt;

    @Column(name = "SpecDiscID")
    private short specDiscID;

    @Column(name = "StaffRMK")
    private String staffRMK;

    @Column(name = "SwitchExRate")
    private BigDecimal switchExRate;

    @Column(name = "SwitchOrgAcct")
    private String switchOrgAcct;

    @Column(name = "SwitchOrgCertifNo")
    private int switchOrgCertifNo;

    @Column(name = "SwitchRefence")
    private String switchRefence;

    @Column(name = "Tax")
    private BigDecimal tax;

    @Column(name = "TJID")
    private BigDecimal tjid;

    @Column(name = "TradeStatus")
    private short tradeStatus;

    @Column(name = "TrustAcct")
    private String trustAcct;

    @Column(name = "TrustCapital")
    private BigDecimal trustCapital;

    @Column(name = "TrustFee")
    private BigDecimal trustFee;

    @Column(name = "TrustPeriodEd")
    private String trustPeriodEd;

    @Column(name = "TrustPeriodSt")
    private String trustPeriodSt;

    @Column(name = "TrustType")
    private short trustType;

    @Column(name = "Units")
    private BigDecimal units;

    private String UWCCBRefNo;

    @Column(name = "WithDrawAcct")
    private String withDrawAcct;

    @Column(name = "WithDrawCardNo")
    private String withDrawCardNo;

    public TradeJournal() {
    }

    public long getRowID() {
        return this.rowID;
    }

    public void setRowID(long rowID) {
        this.rowID = rowID;
    }

    public String getApplicationID() {
        return this.applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public BigDecimal getBalanceUnits() {
        return this.balanceUnits;
    }

    public void setBalanceUnits(BigDecimal balanceUnits) {
        this.balanceUnits = balanceUnits;
    }

    public BigDecimal getBonus() {
        return this.bonus;
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }

    public short getBonusFlg() {
        return this.bonusFlg;
    }

    public void setBonusFlg(short bonusFlg) {
        this.bonusFlg = bonusFlg;
    }

    public String getBranchID() {
        return this.branchID;
    }

    public void setBranchID(String branchID) {
        this.branchID = branchID;
    }

    public String getCanvasser() {
        return this.canvasser;
    }

    public void setCanvasser(String canvasser) {
        this.canvasser = canvasser;
    }

    public String getCanvasserBranchID() {
        return this.canvasserBranchID;
    }

    public void setCanvasserBranchID(String canvasserBranchID) {
        this.canvasserBranchID = canvasserBranchID;
    }

    public BigDecimal getCdsc() {
        return this.cdsc;
    }

    public void setCdsc(BigDecimal cdsc) {
        this.cdsc = cdsc;
    }

    public String getCertifIsuDate() {
        return this.certifIsuDate;
    }

    public void setCertifIsuDate(String certifIsuDate) {
        this.certifIsuDate = certifIsuDate;
    }

    public int getCertiNo() {
        return this.certiNo;
    }

    public void setCertiNo(int certiNo) {
        this.certiNo = certiNo;
    }

    public short getCloseType() {
        return this.closeType;
    }

    public void setCloseType(short closeType) {
        this.closeType = closeType;
    }

    public String getConfirmationNo() {
        return this.confirmationNo;
    }

    public void setConfirmationNo(String confirmationNo) {
        this.confirmationNo = confirmationNo;
    }

    public BigDecimal getCountCapital() {
        return this.countCapital;
    }

    public void setCountCapital(BigDecimal countCapital) {
        this.countCapital = countCapital;
    }

    public BigDecimal getCountUnits() {
        return this.countUnits;
    }

    public void setCountUnits(BigDecimal countUnits) {
        this.countUnits = countUnits;
    }

    public short getCTFType() {
        return this.CTFType;
    }

    public void setCTFType(short CTFType) {
        this.CTFType = CTFType;
    }

    public String getCustomerID() {
        return this.customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getDealType() {
        return this.dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }

    public String getDistributeNo() {
        return this.distributeNo;
    }

    public void setDistributeNo(String distributeNo) {
        this.distributeNo = distributeNo;
    }

    public short getDividendType() {
        return this.dividendType;
    }

    public void setDividendType(short dividendType) {
        this.dividendType = dividendType;
    }

    public String getDrawBatchNo() {
        return this.drawBatchNo;
    }

    public void setDrawBatchNo(String drawBatchNo) {
        this.drawBatchNo = drawBatchNo;
    }

    public int getDrawBatchSeq() {
        return this.drawBatchSeq;
    }

    public void setDrawBatchSeq(int drawBatchSeq) {
        this.drawBatchSeq = drawBatchSeq;
    }

    public BigDecimal getExRate() {
        return this.exRate;
    }

    public void setExRate(BigDecimal exRate) {
        this.exRate = exRate;
    }

    public BigDecimal getFeeBranchOrg() {
        return this.feeBranchOrg;
    }

    public void setFeeBranchOrg(BigDecimal feeBranchOrg) {
        this.feeBranchOrg = feeBranchOrg;
    }

    public BigDecimal getFeeBranchsettle() {
        return this.feeBranchsettle;
    }

    public void setFeeBranchsettle(BigDecimal feeBranchsettle) {
        this.feeBranchsettle = feeBranchsettle;
    }

    public BigDecimal getFeeFourPartOrg() {
        return this.feeFourPartOrg;
    }

    public void setFeeFourPartOrg(BigDecimal feeFourPartOrg) {
        this.feeFourPartOrg = feeFourPartOrg;
    }

    public BigDecimal getFeeInvCorpOrg() {
        return this.feeInvCorpOrg;
    }

    public void setFeeInvCorpOrg(BigDecimal feeInvCorpOrg) {
        this.feeInvCorpOrg = feeInvCorpOrg;
    }

    public BigDecimal getFeeInvCorpsettle() {
        return this.feeInvCorpsettle;
    }

    public void setFeeInvCorpsettle(BigDecimal feeInvCorpsettle) {
        this.feeInvCorpsettle = feeInvCorpsettle;
    }

    public BigDecimal getFeeTrustOrg() {
        return this.feeTrustOrg;
    }

    public void setFeeTrustOrg(BigDecimal feeTrustOrg) {
        this.feeTrustOrg = feeTrustOrg;
    }

    public BigDecimal getFeeTrustsettle() {
        return this.feeTrustsettle;
    }

    public void setFeeTrustsettle(BigDecimal feeTrustsettle) {
        this.feeTrustsettle = feeTrustsettle;
    }

    public String getFourPartBranchID() {
        return this.fourPartBranchID;
    }

    public void setFourPartBranchID(String fourPartBranchID) {
        this.fourPartBranchID = fourPartBranchID;
    }

    public String getFourPartCanvasser() {
        return this.fourPartCanvasser;
    }

    public void setFourPartCanvasser(String fourPartCanvasser) {
        this.fourPartCanvasser = fourPartCanvasser;
    }

    public String getFourPartID() {
        return this.fourPartID;
    }

    public void setFourPartID(String fourPartID) {
        this.fourPartID = fourPartID;
    }

    public String getFourPartLicenceNo() {
        return this.fourPartLicenceNo;
    }

    public void setFourPartLicenceNo(String fourPartLicenceNo) {
        this.fourPartLicenceNo = fourPartLicenceNo;
    }

    public BigDecimal getFromTrustCapital() {
        return this.fromTrustCapital;
    }

    public void setFromTrustCapital(BigDecimal fromTrustCapital) {
        this.fromTrustCapital = fromTrustCapital;
    }

    public BigDecimal getFromUnits() {
        return this.fromUnits;
    }

    public void setFromUnits(BigDecimal fromUnits) {
        this.fromUnits = fromUnits;
    }

    public String getFundID() {
        return this.fundID;
    }

    public void setFundID(String fundID) {
        this.fundID = fundID;
    }

    public String getLastDealDate() {
        return this.lastDealDate;
    }

    public void setLastDealDate(String lastDealDate) {
        this.lastDealDate = lastDealDate;
    }

    public String getMemoRemark() {
        return this.memoRemark;
    }

    public void setMemoRemark(String memoRemark) {
        this.memoRemark = memoRemark;
    }

    public BigDecimal getNav() {
        return this.nav;
    }

    public void setNav(BigDecimal nav) {
        this.nav = nav;
    }

    public String getOrderDate() {
        return this.orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderNo() {
        return this.orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrgBankID() {
        return this.orgBankID;
    }

    public void setOrgBankID(String orgBankID) {
        this.orgBankID = orgBankID;
    }

    public String getPayDate() {
        return this.payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public short getPayType() {
        return this.payType;
    }

    public void setPayType(short payType) {
        this.payType = payType;
    }

    public BigDecimal getPostageFee() {
        return this.postageFee;
    }

    public void setPostageFee(BigDecimal postageFee) {
        this.postageFee = postageFee;
    }

    public BigDecimal getPreRemainTrustCapital() {
        return this.preRemainTrustCapital;
    }

    public void setPreRemainTrustCapital(BigDecimal preRemainTrustCapital) {
        this.preRemainTrustCapital = preRemainTrustCapital;
    }

    public BigDecimal getPreRemainUnits() {
        return this.preRemainUnits;
    }

    public void setPreRemainUnits(BigDecimal preRemainUnits) {
        this.preRemainUnits = preRemainUnits;
    }

    public String getReApplicationID() {
        return this.reApplicationID;
    }

    public void setReApplicationID(String reApplicationID) {
        this.reApplicationID = reApplicationID;
    }

    public BigDecimal getRedeemCount() {
        return this.redeemCount;
    }

    public void setRedeemCount(BigDecimal redeemCount) {
        this.redeemCount = redeemCount;
    }

    public BigDecimal getRemainTrustCapital() {
        return this.remainTrustCapital;
    }

    public void setRemainTrustCapital(BigDecimal remainTrustCapital) {
        this.remainTrustCapital = remainTrustCapital;
    }

    public BigDecimal getRemainUnits() {
        return this.remainUnits;
    }

    public void setRemainUnits(BigDecimal remainUnits) {
        this.remainUnits = remainUnits;
    }

    public BigDecimal getRemittanceCharge() {
        return this.remittanceCharge;
    }

    public void setRemittanceCharge(BigDecimal remittanceCharge) {
        this.remittanceCharge = remittanceCharge;
    }

    public String getReOrderDate() {
        return this.reOrderDate;
    }

    public void setReOrderDate(String reOrderDate) {
        this.reOrderDate = reOrderDate;
    }

    public BigDecimal getReTJID() {
        return this.reTJID;
    }

    public void setReTJID(BigDecimal reTJID) {
        this.reTJID = reTJID;
    }

    public BigDecimal getSettleAmt() {
        return this.settleAmt;
    }

    public void setSettleAmt(BigDecimal settleAmt) {
        this.settleAmt = settleAmt;
    }

    public BigDecimal getSettleNTAmt() {
        return this.settleNTAmt;
    }

    public void setSettleNTAmt(BigDecimal settleNTAmt) {
        this.settleNTAmt = settleNTAmt;
    }

    public short getSpecDiscID() {
        return this.specDiscID;
    }

    public void setSpecDiscID(short specDiscID) {
        this.specDiscID = specDiscID;
    }

    public String getStaffRMK() {
        return this.staffRMK;
    }

    public void setStaffRMK(String staffRMK) {
        this.staffRMK = staffRMK;
    }

    public BigDecimal getSwitchExRate() {
        return this.switchExRate;
    }

    public void setSwitchExRate(BigDecimal switchExRate) {
        this.switchExRate = switchExRate;
    }

    public String getSwitchOrgAcct() {
        return this.switchOrgAcct;
    }

    public void setSwitchOrgAcct(String switchOrgAcct) {
        this.switchOrgAcct = switchOrgAcct;
    }

    public int getSwitchOrgCertifNo() {
        return this.switchOrgCertifNo;
    }

    public void setSwitchOrgCertifNo(int switchOrgCertifNo) {
        this.switchOrgCertifNo = switchOrgCertifNo;
    }

    public String getSwitchRefence() {
        return this.switchRefence;
    }

    public void setSwitchRefence(String switchRefence) {
        this.switchRefence = switchRefence;
    }

    public BigDecimal getTax() {
        return this.tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTjid() {
        return this.tjid;
    }

    public void setTjid(BigDecimal tjid) {
        this.tjid = tjid;
    }

    public short getTradeStatus() {
        return this.tradeStatus;
    }

    public void setTradeStatus(short tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getTrustAcct() {
        return this.trustAcct;
    }

    public void setTrustAcct(String trustAcct) {
        this.trustAcct = trustAcct;
    }

    public BigDecimal getTrustCapital() {
        return this.trustCapital;
    }

    public void setTrustCapital(BigDecimal trustCapital) {
        this.trustCapital = trustCapital;
    }

    public BigDecimal getTrustFee() {
        return this.trustFee;
    }

    public void setTrustFee(BigDecimal trustFee) {
        this.trustFee = trustFee;
    }

    public String getTrustPeriodEd() {
        return this.trustPeriodEd;
    }

    public void setTrustPeriodEd(String trustPeriodEd) {
        this.trustPeriodEd = trustPeriodEd;
    }

    public String getTrustPeriodSt() {
        return this.trustPeriodSt;
    }

    public void setTrustPeriodSt(String trustPeriodSt) {
        this.trustPeriodSt = trustPeriodSt;
    }

    public short getTrustType() {
        return this.trustType;
    }

    public void setTrustType(short trustType) {
        this.trustType = trustType;
    }

    public BigDecimal getUnits() {
        return this.units;
    }

    public void setUnits(BigDecimal units) {
        this.units = units;
    }

    public String getUWCCBRefNo() {
        return this.UWCCBRefNo;
    }

    public void setUWCCBRefNo(String UWCCBRefNo) {
        this.UWCCBRefNo = UWCCBRefNo;
    }

    public String getWithDrawAcct() {
        return this.withDrawAcct;
    }

    public void setWithDrawAcct(String withDrawAcct) {
        this.withDrawAcct = withDrawAcct;
    }

    public String getWithDrawCardNo() {
        return this.withDrawCardNo;
    }

    public void setWithDrawCardNo(String withDrawCardNo) {
        this.withDrawCardNo = withDrawCardNo;
    }

}