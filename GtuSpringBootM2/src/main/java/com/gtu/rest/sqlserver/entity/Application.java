package com.gtu.rest.sqlserver.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 * The persistent class for the Application database table.
 * 
 */
@Entity
@NamedQuery(name = "Application.findAll", query = "SELECT a FROM Application a")
public class Application implements Serializable {
    private static final Long serialVersionUID = 1L;

    @Id
    @Column(name = "ApplicationID")
    private String applicationID;

    @Column(name = "AcctOwnerID")
    private String acctOwnerID;

    @Column(name = "ApplicationDate")
    private String applicationDate;

    @Column(name = "ApplicatStatus")
    private Short applicatStatus;

    @Column(name = "AUM")
    private BigDecimal aum;

    @Column(name = "AvgAsset")
    private BigDecimal avgAsset;

    @Column(name = "BonusFlg")
    private Short bonusFlg;

    @Column(name = "BranchID")
    private String branchID;

    @Column(name = "Canvasser")
    private String canvasser;

    @Column(name = "CanvasserBranchID")
    private String canvasserBranchID;

    @Column(name = "CanvasserType")
    private String canvasserType;

    @Column(name = "CertifNo")
    private Integer certifNo;

    @Column(name = "ChangeFlg")
    private Short changeFlg;

    @Column(name = "ClerkID")
    private String clerkID;

    @Column(name = "CloseType")
    private Short closeType;

    @Column(name = "ConfirmDate")
    private String confirmDate;

    @Column(name = "ConfirmTime")
    private String confirmTime;

    private Short CTFType;

    @Column(name = "CustomerID")
    private String customerID;

    @Column(name = "CustomerType")
    private String customerType;

    @Column(name = "CustRiskLevel")
    private String custRiskLevel;

    @Column(name = "DealType")
    private String dealType;

    @Column(name = "DistributeAcct")
    private String distributeAcct;

    @Column(name = "DistributeBankID")
    private String distributeBankID;

    @Column(name = "DividendType")
    private Short dividendType;

    @Column(name = "ExRateDiscRmk")
    private String exRateDiscRmk;

    @Column(name = "FeeBranchOrg")
    private BigDecimal feeBranchOrg;

    @Column(name = "FeeFourPartOrg")
    private BigDecimal feeFourPartOrg;

    @Column(name = "FeeInvCorpOrg")
    private BigDecimal feeInvCorpOrg;

    @Column(name = "FeeTrustOrg")
    private BigDecimal feeTrustOrg;

    @Column(name = "FourPartBranchID")
    private String fourPartBranchID;

    @Column(name = "FourPartCanvasser")
    private String fourPartCanvasser;

    @Column(name = "FourPartCanvasserType")
    private String fourPartCanvasserType;

    @Column(name = "FourPartID")
    private String fourPartID;

    @Column(name = "FourPartLicenceNo")
    private String fourPartLicenceNo;

    @Column(name = "FundID")
    private String fundID;

    @Column(name = "FundRiskLevel")
    private Short fundRiskLevel;

    @Column(name = "IdentityID")
    private Short identityID;

    @Column(name = "InvCorpSum")
    private BigDecimal invCorpSum;

    @Column(name = "InvestRate")
    private BigDecimal investRate;

    @Column(name = "IsInvestRateOver")
    private String isInvestRateOver;

    @Column(name = "IsSPRiskCust")
    private String isSPRiskCust;

    @Column(name = "MonDrawDate1")
    private Long monDrawDate1;

    @Column(name = "MonDrawDate2")
    private Long monDrawDate2;

    @Column(name = "MonDrawDate3")
    private Long monDrawDate3;

    @Column(name = "NegoConfirmID")
    private Short negoConfirmID;

    @Column(name = "NegoFeeRate")
    private BigDecimal negoFeeRate;

    @Column(name = "NegoRate")
    private BigDecimal negoRate;

    @Column(name = "OrderDate")
    private String orderDate;

    @Column(name = "OrderNo")
    private String orderNo;

    @Column(name = "OrderProcStatus")
    private Short orderProcStatus;

    @Column(name = "OrderTime")
    private String orderTime;

    @Column(name = "PayType")
    private Short payType;

    @Column(name = "PurchType")
    private String purchType;

    @Column(name = "RateBranchOrg")
    private double rateBranchOrg;

    @Column(name = "RateFourPartOrg")
    private double rateFourPartOrg;

    @Column(name = "RateInvCorpOrg")
    private double rateInvCorpOrg;

    @Column(name = "RateTrustOrg")
    private double rateTrustOrg;

    @Column(name = "RawFeeRate")
    private BigDecimal rawFeeRate;

    @Column(name = "ReExamineID")
    private String reExamineID;

    @Column(name = "SpecDiscID")
    private Integer specDiscID;

    @Column(name = "StaffFlg")
    private String staffFlg;

    @Column(name = "SwitchFeeStatus")
    private Integer switchFeeStatus;

    @Column(name = "ToFundDividendType")
    private Short toFundDividendType;

    @Column(name = "ToFundID")
    private String toFundID;

    @Column(name = "ToFundIDCloseType")
    private Short toFundIDCloseType;

    @Column(name = "TradeType")
    private String tradeType;

    @Column(name = "TrustAcct")
    private String trustAcct;

    @Column(name = "TrustCapital")
    private BigDecimal trustCapital;

    @Column(name = "TrustDepositAcctID")
    private Short trustDepositAcctID;

    @Column(name = "TrustPeriodEd")
    private String trustPeriodEd;

    @Column(name = "TrustPeriodSt")
    private String trustPeriodSt;

    @Column(name = "TrustType")
    private Short trustType;

    @Column(name = "TxBranchID")
    private String txBranchID;

    @Column(name = "Units")
    private BigDecimal units;

    @Column(name = "ValidApplicFlg")
    private String validApplicFlg;

    @Column(name = "VoicePID")
    private Integer voicePID;

    @Column(name = "WithDrawAcct")
    private String withDrawAcct;

    @Column(name = "WithDrawCardNo")
    private String withDrawCardNo;

    @Column(name = "WithDrawErrCnt")
    private Short withDrawErrCnt;

    @Column(name = "WithDrawFlg1")
    private Short withDrawFlg1;

    @Column(name = "WithDrawFlg2")
    private Short withDrawFlg2;

    @Column(name = "WithDrawFlg3")
    private Short withDrawFlg3;

    @Column(name = "WithDrawPeriodEdDt")
    private String withDrawPeriodEdDt;

    @Column(name = "WithDrawPeriodStDt")
    private String withDrawPeriodStDt;

    public Application() {
    }

    public String getApplicationID() {
        return this.applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public String getAcctOwnerID() {
        return this.acctOwnerID;
    }

    public void setAcctOwnerID(String acctOwnerID) {
        this.acctOwnerID = acctOwnerID;
    }

    public String getApplicationDate() {
        return this.applicationDate;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
    }

    public Short getApplicatStatus() {
        return this.applicatStatus;
    }

    public void setApplicatStatus(Short applicatStatus) {
        this.applicatStatus = applicatStatus;
    }

    public BigDecimal getAum() {
        return this.aum;
    }

    public void setAum(BigDecimal aum) {
        this.aum = aum;
    }

    public BigDecimal getAvgAsset() {
        return this.avgAsset;
    }

    public void setAvgAsset(BigDecimal avgAsset) {
        this.avgAsset = avgAsset;
    }

    public Short getBonusFlg() {
        return this.bonusFlg;
    }

    public void setBonusFlg(Short bonusFlg) {
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

    public String getCanvasserType() {
        return this.canvasserType;
    }

    public void setCanvasserType(String canvasserType) {
        this.canvasserType = canvasserType;
    }

    public Integer getCertifNo() {
        return this.certifNo;
    }

    public void setCertifNo(Integer certifNo) {
        this.certifNo = certifNo;
    }

    public Short getChangeFlg() {
        return this.changeFlg;
    }

    public void setChangeFlg(Short changeFlg) {
        this.changeFlg = changeFlg;
    }

    public String getClerkID() {
        return this.clerkID;
    }

    public void setClerkID(String clerkID) {
        this.clerkID = clerkID;
    }

    public Short getCloseType() {
        return this.closeType;
    }

    public void setCloseType(Short closeType) {
        this.closeType = closeType;
    }

    public String getConfirmDate() {
        return this.confirmDate;
    }

    public void setConfirmDate(String confirmDate) {
        this.confirmDate = confirmDate;
    }

    public String getConfirmTime() {
        return this.confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    public Short getCTFType() {
        return this.CTFType;
    }

    public void setCTFType(Short CTFType) {
        this.CTFType = CTFType;
    }

    public String getCustomerID() {
        return this.customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getCustomerType() {
        return this.customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCustRiskLevel() {
        return this.custRiskLevel;
    }

    public void setCustRiskLevel(String custRiskLevel) {
        this.custRiskLevel = custRiskLevel;
    }

    public String getDealType() {
        return this.dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }

    public String getDistributeAcct() {
        return this.distributeAcct;
    }

    public void setDistributeAcct(String distributeAcct) {
        this.distributeAcct = distributeAcct;
    }

    public String getDistributeBankID() {
        return this.distributeBankID;
    }

    public void setDistributeBankID(String distributeBankID) {
        this.distributeBankID = distributeBankID;
    }

    public Short getDividendType() {
        return this.dividendType;
    }

    public void setDividendType(Short dividendType) {
        this.dividendType = dividendType;
    }

    public String getExRateDiscRmk() {
        return this.exRateDiscRmk;
    }

    public void setExRateDiscRmk(String exRateDiscRmk) {
        this.exRateDiscRmk = exRateDiscRmk;
    }

    public BigDecimal getFeeBranchOrg() {
        return this.feeBranchOrg;
    }

    public void setFeeBranchOrg(BigDecimal feeBranchOrg) {
        this.feeBranchOrg = feeBranchOrg;
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

    public BigDecimal getFeeTrustOrg() {
        return this.feeTrustOrg;
    }

    public void setFeeTrustOrg(BigDecimal feeTrustOrg) {
        this.feeTrustOrg = feeTrustOrg;
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

    public String getFourPartCanvasserType() {
        return this.fourPartCanvasserType;
    }

    public void setFourPartCanvasserType(String fourPartCanvasserType) {
        this.fourPartCanvasserType = fourPartCanvasserType;
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

    public String getFundID() {
        return this.fundID;
    }

    public void setFundID(String fundID) {
        this.fundID = fundID;
    }

    public Short getFundRiskLevel() {
        return this.fundRiskLevel;
    }

    public void setFundRiskLevel(Short fundRiskLevel) {
        this.fundRiskLevel = fundRiskLevel;
    }

    public Short getIdentityID() {
        return this.identityID;
    }

    public void setIdentityID(Short identityID) {
        this.identityID = identityID;
    }

    public BigDecimal getInvCorpSum() {
        return this.invCorpSum;
    }

    public void setInvCorpSum(BigDecimal invCorpSum) {
        this.invCorpSum = invCorpSum;
    }

    public BigDecimal getInvestRate() {
        return this.investRate;
    }

    public void setInvestRate(BigDecimal investRate) {
        this.investRate = investRate;
    }

    public String getIsInvestRateOver() {
        return this.isInvestRateOver;
    }

    public void setIsInvestRateOver(String isInvestRateOver) {
        this.isInvestRateOver = isInvestRateOver;
    }

    public String getIsSPRiskCust() {
        return this.isSPRiskCust;
    }

    public void setIsSPRiskCust(String isSPRiskCust) {
        this.isSPRiskCust = isSPRiskCust;
    }

    public Long getMonDrawDate1() {
        return this.monDrawDate1;
    }

    public void setMonDrawDate1(Long monDrawDate1) {
        this.monDrawDate1 = monDrawDate1;
    }

    public Long getMonDrawDate2() {
        return this.monDrawDate2;
    }

    public void setMonDrawDate2(Long monDrawDate2) {
        this.monDrawDate2 = monDrawDate2;
    }

    public Long getMonDrawDate3() {
        return this.monDrawDate3;
    }

    public void setMonDrawDate3(Long monDrawDate3) {
        this.monDrawDate3 = monDrawDate3;
    }

    public Short getNegoConfirmID() {
        return this.negoConfirmID;
    }

    public void setNegoConfirmID(Short negoConfirmID) {
        this.negoConfirmID = negoConfirmID;
    }

    public BigDecimal getNegoFeeRate() {
        return this.negoFeeRate;
    }

    public void setNegoFeeRate(BigDecimal negoFeeRate) {
        this.negoFeeRate = negoFeeRate;
    }

    public BigDecimal getNegoRate() {
        return this.negoRate;
    }

    public void setNegoRate(BigDecimal negoRate) {
        this.negoRate = negoRate;
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

    public Short getOrderProcStatus() {
        return this.orderProcStatus;
    }

    public void setOrderProcStatus(Short orderProcStatus) {
        this.orderProcStatus = orderProcStatus;
    }

    public String getOrderTime() {
        return this.orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public Short getPayType() {
        return this.payType;
    }

    public void setPayType(Short payType) {
        this.payType = payType;
    }

    public String getPurchType() {
        return this.purchType;
    }

    public void setPurchType(String purchType) {
        this.purchType = purchType;
    }

    public double getRateBranchOrg() {
        return this.rateBranchOrg;
    }

    public void setRateBranchOrg(double rateBranchOrg) {
        this.rateBranchOrg = rateBranchOrg;
    }

    public double getRateFourPartOrg() {
        return this.rateFourPartOrg;
    }

    public void setRateFourPartOrg(double rateFourPartOrg) {
        this.rateFourPartOrg = rateFourPartOrg;
    }

    public double getRateInvCorpOrg() {
        return this.rateInvCorpOrg;
    }

    public void setRateInvCorpOrg(double rateInvCorpOrg) {
        this.rateInvCorpOrg = rateInvCorpOrg;
    }

    public double getRateTrustOrg() {
        return this.rateTrustOrg;
    }

    public void setRateTrustOrg(double rateTrustOrg) {
        this.rateTrustOrg = rateTrustOrg;
    }

    public BigDecimal getRawFeeRate() {
        return this.rawFeeRate;
    }

    public void setRawFeeRate(BigDecimal rawFeeRate) {
        this.rawFeeRate = rawFeeRate;
    }

    public String getReExamineID() {
        return this.reExamineID;
    }

    public void setReExamineID(String reExamineID) {
        this.reExamineID = reExamineID;
    }

    public Integer getSpecDiscID() {
        return this.specDiscID;
    }

    public void setSpecDiscID(Integer specDiscID) {
        this.specDiscID = specDiscID;
    }

    public String getStaffFlg() {
        return this.staffFlg;
    }

    public void setStaffFlg(String staffFlg) {
        this.staffFlg = staffFlg;
    }

    public Integer getSwitchFeeStatus() {
        return this.switchFeeStatus;
    }

    public void setSwitchFeeStatus(Integer switchFeeStatus) {
        this.switchFeeStatus = switchFeeStatus;
    }

    public Short getToFundDividendType() {
        return this.toFundDividendType;
    }

    public void setToFundDividendType(Short toFundDividendType) {
        this.toFundDividendType = toFundDividendType;
    }

    public String getToFundID() {
        return this.toFundID;
    }

    public void setToFundID(String toFundID) {
        this.toFundID = toFundID;
    }

    public Short getToFundIDCloseType() {
        return this.toFundIDCloseType;
    }

    public void setToFundIDCloseType(Short toFundIDCloseType) {
        this.toFundIDCloseType = toFundIDCloseType;
    }

    public String getTradeType() {
        return this.tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
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

    public Short getTrustDepositAcctID() {
        return this.trustDepositAcctID;
    }

    public void setTrustDepositAcctID(Short trustDepositAcctID) {
        this.trustDepositAcctID = trustDepositAcctID;
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

    public Short getTrustType() {
        return this.trustType;
    }

    public void setTrustType(Short trustType) {
        this.trustType = trustType;
    }

    public String getTxBranchID() {
        return this.txBranchID;
    }

    public void setTxBranchID(String txBranchID) {
        this.txBranchID = txBranchID;
    }

    public BigDecimal getUnits() {
        return this.units;
    }

    public void setUnits(BigDecimal units) {
        this.units = units;
    }

    public String getValidApplicFlg() {
        return this.validApplicFlg;
    }

    public void setValidApplicFlg(String validApplicFlg) {
        this.validApplicFlg = validApplicFlg;
    }

    public Integer getVoicePID() {
        return this.voicePID;
    }

    public void setVoicePID(Integer voicePID) {
        this.voicePID = voicePID;
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

    public Short getWithDrawErrCnt() {
        return this.withDrawErrCnt;
    }

    public void setWithDrawErrCnt(Short withDrawErrCnt) {
        this.withDrawErrCnt = withDrawErrCnt;
    }

    public Short getWithDrawFlg1() {
        return this.withDrawFlg1;
    }

    public void setWithDrawFlg1(Short withDrawFlg1) {
        this.withDrawFlg1 = withDrawFlg1;
    }

    public Short getWithDrawFlg2() {
        return this.withDrawFlg2;
    }

    public void setWithDrawFlg2(Short withDrawFlg2) {
        this.withDrawFlg2 = withDrawFlg2;
    }

    public Short getWithDrawFlg3() {
        return this.withDrawFlg3;
    }

    public void setWithDrawFlg3(Short withDrawFlg3) {
        this.withDrawFlg3 = withDrawFlg3;
    }

    public String getWithDrawPeriodEdDt() {
        return this.withDrawPeriodEdDt;
    }

    public void setWithDrawPeriodEdDt(String withDrawPeriodEdDt) {
        this.withDrawPeriodEdDt = withDrawPeriodEdDt;
    }

    public String getWithDrawPeriodStDt() {
        return this.withDrawPeriodStDt;
    }

    public void setWithDrawPeriodStDt(String withDrawPeriodStDt) {
        this.withDrawPeriodStDt = withDrawPeriodStDt;
    }

}