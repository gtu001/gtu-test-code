package com.gtu.rest.sqlserver.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApplicationDTO {

    @JsonProperty(value =  "ApplicationID")
    private String applicationID;

    @JsonProperty(value =  "AcctOwnerID")
    private String acctOwnerID;

    @JsonProperty(value =  "ApplicationDate")
    private String applicationDate;

    @JsonProperty(value =  "ApplicatStatus")
    private Short applicatStatus;

    @JsonProperty(value =  "AUM")
    private BigDecimal aum;

    @JsonProperty(value =  "AvgAsset")
    private BigDecimal avgAsset;

    @JsonProperty(value =  "BonusFlg")
    private Short bonusFlg;

    @JsonProperty(value =  "BranchID")
    private String branchID;

    @JsonProperty(value =  "Canvasser")
    private String canvasser;

    @JsonProperty(value =  "CanvasserBranchID")
    private String canvasserBranchID;

    @JsonProperty(value =  "CanvasserType")
    private String canvasserType;

    @JsonProperty(value =  "CertifNo")
    private Integer certifNo;

    @JsonProperty(value =  "ChangeFlg")
    private Short changeFlg;

    @JsonProperty(value =  "ClerkID")
    private String clerkID;

    @JsonProperty(value =  "CloseType")
    private Short closeType;

    @JsonProperty(value =  "ConfirmDate")
    private String confirmDate;

    @JsonProperty(value =  "ConfirmTime")
    private String confirmTime;

    private Short CTFType;

    @JsonProperty(value =  "CustomerID")
    private String customerID;

    @JsonProperty(value =  "CustomerType")
    private String customerType;

    @JsonProperty(value =  "CustRiskLevel")
    private String custRiskLevel;

    @JsonProperty(value =  "DealType")
    private String dealType;

    @JsonProperty(value =  "DistributeAcct")
    private String distributeAcct;

    @JsonProperty(value =  "DistributeBankID")
    private String distributeBankID;

    @JsonProperty(value =  "DividendType")
    private Short dividendType;

    @JsonProperty(value =  "ExRateDiscRmk")
    private String exRateDiscRmk;

    @JsonProperty(value =  "FeeBranchOrg")
    private BigDecimal feeBranchOrg;

    @JsonProperty(value =  "FeeFourPartOrg")
    private BigDecimal feeFourPartOrg;

    @JsonProperty(value =  "FeeInvCorpOrg")
    private BigDecimal feeInvCorpOrg;

    @JsonProperty(value =  "FeeTrustOrg")
    private BigDecimal feeTrustOrg;

    @JsonProperty(value =  "FourPartBranchID")
    private String fourPartBranchID;

    @JsonProperty(value =  "FourPartCanvasser")
    private String fourPartCanvasser;

    @JsonProperty(value =  "FourPartCanvasserType")
    private String fourPartCanvasserType;

    @JsonProperty(value =  "FourPartID")
    private String fourPartID;

    @JsonProperty(value =  "FourPartLicenceNo")
    private String fourPartLicenceNo;

    @JsonProperty(value =  "FundID")
    private String fundID;

    @JsonProperty(value =  "FundRiskLevel")
    private Short fundRiskLevel;

    @JsonProperty(value =  "IdentityID")
    private Short identityID;

    @JsonProperty(value =  "InvCorpSum")
    private BigDecimal invCorpSum;

    @JsonProperty(value =  "InvestRate")
    private BigDecimal investRate;

    @JsonProperty(value =  "IsInvestRateOver")
    private String isInvestRateOver;

    @JsonProperty(value =  "IsSPRiskCust")
    private String isSPRiskCust;

    @JsonProperty(value =  "MonDrawDate1")
    private long monDrawDate1;

    @JsonProperty(value =  "MonDrawDate2")
    private long monDrawDate2;

    @JsonProperty(value =  "MonDrawDate3")
    private long monDrawDate3;

    @JsonProperty(value =  "NegoConfirmID")
    private Short negoConfirmID;

    @JsonProperty(value =  "NegoFeeRate")
    private BigDecimal negoFeeRate;

    @JsonProperty(value =  "NegoRate")
    private BigDecimal negoRate;

    @JsonProperty(value =  "OrderDate")
    private String orderDate;

    @JsonProperty(value =  "OrderNo")
    private String orderNo;

    @JsonProperty(value =  "OrderProcStatus")
    private Short orderProcStatus;

    @JsonProperty(value =  "OrderTime")
    private String orderTime;

    @JsonProperty(value =  "PayType")
    private Short payType;

    @JsonProperty(value =  "PurchType")
    private String purchType;

    @JsonProperty(value =  "RateBranchOrg")
    private Double rateBranchOrg;

    @JsonProperty(value =  "RateFourPartOrg")
    private Double rateFourPartOrg;

    @JsonProperty(value =  "RateInvCorpOrg")
    private Double rateInvCorpOrg;

    @JsonProperty(value =  "RateTrustOrg")
    private Double rateTrustOrg;

    @JsonProperty(value =  "RawFeeRate")
    private BigDecimal rawFeeRate;

    @JsonProperty(value =  "ReExamineID")
    private String reExamineID;

    @JsonProperty(value =  "SpecDiscID")
    private Integer specDiscID;

    @JsonProperty(value =  "StaffFlg")
    private String staffFlg;

    @JsonProperty(value =  "SwitchFeeStatus")
    private Integer switchFeeStatus;

    @JsonProperty(value =  "ToFundDividendType")
    private Short toFundDividendType;

    @JsonProperty(value =  "ToFundID")
    private String toFundID;

    @JsonProperty(value =  "ToFundIDCloseType")
    private Short toFundIDCloseType;

    @JsonProperty(value =  "TradeType")
    private String tradeType;

    @JsonProperty(value =  "TrustAcct")
    private String trustAcct;

    @JsonProperty(value =  "TrustCapital")
    private BigDecimal trustCapital;

    @JsonProperty(value =  "TrustDepositAcctID")
    private Short trustDepositAcctID;

    @JsonProperty(value =  "TrustPeriodEd")
    private String trustPeriodEd;

    @JsonProperty(value =  "TrustPeriodSt")
    private String trustPeriodSt;

    @JsonProperty(value =  "TrustType")
    private Short trustType;

    @JsonProperty(value =  "TxBranchID")
    private String txBranchID;

    @JsonProperty(value =  "Units")
    private BigDecimal units;

    @JsonProperty(value =  "ValidApplicFlg")
    private String validApplicFlg;

    @JsonProperty(value =  "VoicePID")
    private Integer voicePID;

    @JsonProperty(value =  "WithDrawAcct")
    private String withDrawAcct;

    @JsonProperty(value =  "WithDrawCardNo")
    private String withDrawCardNo;

    @JsonProperty(value =  "WithDrawErrCnt")
    private Short withDrawErrCnt;

    @JsonProperty(value =  "WithDrawFlg1")
    private Short withDrawFlg1;

    @JsonProperty(value =  "WithDrawFlg2")
    private Short withDrawFlg2;

    @JsonProperty(value =  "WithDrawFlg3")
    private Short withDrawFlg3;

    @JsonProperty(value =  "WithDrawPeriodEdDt")
    private String withDrawPeriodEdDt;

    @JsonProperty(value =  "WithDrawPeriodStDt")
    private String withDrawPeriodStDt;

    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public String getAcctOwnerID() {
        return acctOwnerID;
    }

    public void setAcctOwnerID(String acctOwnerID) {
        this.acctOwnerID = acctOwnerID;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
    }

    public Short getApplicatStatus() {
        return applicatStatus;
    }

    public void setApplicatStatus(Short applicatStatus) {
        this.applicatStatus = applicatStatus;
    }

    public BigDecimal getAum() {
        return aum;
    }

    public void setAum(BigDecimal aum) {
        this.aum = aum;
    }

    public BigDecimal getAvgAsset() {
        return avgAsset;
    }

    public void setAvgAsset(BigDecimal avgAsset) {
        this.avgAsset = avgAsset;
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

    public String getCanvasserType() {
        return canvasserType;
    }

    public void setCanvasserType(String canvasserType) {
        this.canvasserType = canvasserType;
    }

    public Integer getCertifNo() {
        return certifNo;
    }

    public void setCertifNo(Integer certifNo) {
        this.certifNo = certifNo;
    }

    public Short getChangeFlg() {
        return changeFlg;
    }

    public void setChangeFlg(Short changeFlg) {
        this.changeFlg = changeFlg;
    }

    public String getClerkID() {
        return clerkID;
    }

    public void setClerkID(String clerkID) {
        this.clerkID = clerkID;
    }

    public Short getCloseType() {
        return closeType;
    }

    public void setCloseType(Short closeType) {
        this.closeType = closeType;
    }

    public String getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(String confirmDate) {
        this.confirmDate = confirmDate;
    }

    public String getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
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

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCustRiskLevel() {
        return custRiskLevel;
    }

    public void setCustRiskLevel(String custRiskLevel) {
        this.custRiskLevel = custRiskLevel;
    }

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }

    public String getDistributeAcct() {
        return distributeAcct;
    }

    public void setDistributeAcct(String distributeAcct) {
        this.distributeAcct = distributeAcct;
    }

    public String getDistributeBankID() {
        return distributeBankID;
    }

    public void setDistributeBankID(String distributeBankID) {
        this.distributeBankID = distributeBankID;
    }

    public Short getDividendType() {
        return dividendType;
    }

    public void setDividendType(Short dividendType) {
        this.dividendType = dividendType;
    }

    public String getExRateDiscRmk() {
        return exRateDiscRmk;
    }

    public void setExRateDiscRmk(String exRateDiscRmk) {
        this.exRateDiscRmk = exRateDiscRmk;
    }

    public BigDecimal getFeeBranchOrg() {
        return feeBranchOrg;
    }

    public void setFeeBranchOrg(BigDecimal feeBranchOrg) {
        this.feeBranchOrg = feeBranchOrg;
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

    public BigDecimal getFeeTrustOrg() {
        return feeTrustOrg;
    }

    public void setFeeTrustOrg(BigDecimal feeTrustOrg) {
        this.feeTrustOrg = feeTrustOrg;
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

    public String getFourPartCanvasserType() {
        return fourPartCanvasserType;
    }

    public void setFourPartCanvasserType(String fourPartCanvasserType) {
        this.fourPartCanvasserType = fourPartCanvasserType;
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

    public String getFundID() {
        return fundID;
    }

    public void setFundID(String fundID) {
        this.fundID = fundID;
    }

    public Short getFundRiskLevel() {
        return fundRiskLevel;
    }

    public void setFundRiskLevel(Short fundRiskLevel) {
        this.fundRiskLevel = fundRiskLevel;
    }

    public Short getIdentityID() {
        return identityID;
    }

    public void setIdentityID(Short identityID) {
        this.identityID = identityID;
    }

    public BigDecimal getInvCorpSum() {
        return invCorpSum;
    }

    public void setInvCorpSum(BigDecimal invCorpSum) {
        this.invCorpSum = invCorpSum;
    }

    public BigDecimal getInvestRate() {
        return investRate;
    }

    public void setInvestRate(BigDecimal investRate) {
        this.investRate = investRate;
    }

    public String getIsInvestRateOver() {
        return isInvestRateOver;
    }

    public void setIsInvestRateOver(String isInvestRateOver) {
        this.isInvestRateOver = isInvestRateOver;
    }

    public String getIsSPRiskCust() {
        return isSPRiskCust;
    }

    public void setIsSPRiskCust(String isSPRiskCust) {
        this.isSPRiskCust = isSPRiskCust;
    }

    public long getMonDrawDate1() {
        return monDrawDate1;
    }

    public void setMonDrawDate1(long monDrawDate1) {
        this.monDrawDate1 = monDrawDate1;
    }

    public long getMonDrawDate2() {
        return monDrawDate2;
    }

    public void setMonDrawDate2(long monDrawDate2) {
        this.monDrawDate2 = monDrawDate2;
    }

    public long getMonDrawDate3() {
        return monDrawDate3;
    }

    public void setMonDrawDate3(long monDrawDate3) {
        this.monDrawDate3 = monDrawDate3;
    }

    public Short getNegoConfirmID() {
        return negoConfirmID;
    }

    public void setNegoConfirmID(Short negoConfirmID) {
        this.negoConfirmID = negoConfirmID;
    }

    public BigDecimal getNegoFeeRate() {
        return negoFeeRate;
    }

    public void setNegoFeeRate(BigDecimal negoFeeRate) {
        this.negoFeeRate = negoFeeRate;
    }

    public BigDecimal getNegoRate() {
        return negoRate;
    }

    public void setNegoRate(BigDecimal negoRate) {
        this.negoRate = negoRate;
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

    public Short getOrderProcStatus() {
        return orderProcStatus;
    }

    public void setOrderProcStatus(Short orderProcStatus) {
        this.orderProcStatus = orderProcStatus;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public Short getPayType() {
        return payType;
    }

    public void setPayType(Short payType) {
        this.payType = payType;
    }

    public String getPurchType() {
        return purchType;
    }

    public void setPurchType(String purchType) {
        this.purchType = purchType;
    }

    public Double getRateBranchOrg() {
        return rateBranchOrg;
    }

    public void setRateBranchOrg(Double rateBranchOrg) {
        this.rateBranchOrg = rateBranchOrg;
    }

    public Double getRateFourPartOrg() {
        return rateFourPartOrg;
    }

    public void setRateFourPartOrg(Double rateFourPartOrg) {
        this.rateFourPartOrg = rateFourPartOrg;
    }

    public Double getRateInvCorpOrg() {
        return rateInvCorpOrg;
    }

    public void setRateInvCorpOrg(Double rateInvCorpOrg) {
        this.rateInvCorpOrg = rateInvCorpOrg;
    }

    public Double getRateTrustOrg() {
        return rateTrustOrg;
    }

    public void setRateTrustOrg(Double rateTrustOrg) {
        this.rateTrustOrg = rateTrustOrg;
    }

    public BigDecimal getRawFeeRate() {
        return rawFeeRate;
    }

    public void setRawFeeRate(BigDecimal rawFeeRate) {
        this.rawFeeRate = rawFeeRate;
    }

    public String getReExamineID() {
        return reExamineID;
    }

    public void setReExamineID(String reExamineID) {
        this.reExamineID = reExamineID;
    }

    public Integer getSpecDiscID() {
        return specDiscID;
    }

    public void setSpecDiscID(Integer specDiscID) {
        this.specDiscID = specDiscID;
    }

    public String getStaffFlg() {
        return staffFlg;
    }

    public void setStaffFlg(String staffFlg) {
        this.staffFlg = staffFlg;
    }

    public Integer getSwitchFeeStatus() {
        return switchFeeStatus;
    }

    public void setSwitchFeeStatus(Integer switchFeeStatus) {
        this.switchFeeStatus = switchFeeStatus;
    }

    public Short getToFundDividendType() {
        return toFundDividendType;
    }

    public void setToFundDividendType(Short toFundDividendType) {
        this.toFundDividendType = toFundDividendType;
    }

    public String getToFundID() {
        return toFundID;
    }

    public void setToFundID(String toFundID) {
        this.toFundID = toFundID;
    }

    public Short getToFundIDCloseType() {
        return toFundIDCloseType;
    }

    public void setToFundIDCloseType(Short toFundIDCloseType) {
        this.toFundIDCloseType = toFundIDCloseType;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
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

    public Short getTrustDepositAcctID() {
        return trustDepositAcctID;
    }

    public void setTrustDepositAcctID(Short trustDepositAcctID) {
        this.trustDepositAcctID = trustDepositAcctID;
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

    public String getTxBranchID() {
        return txBranchID;
    }

    public void setTxBranchID(String txBranchID) {
        this.txBranchID = txBranchID;
    }

    public BigDecimal getUnits() {
        return units;
    }

    public void setUnits(BigDecimal units) {
        this.units = units;
    }

    public String getValidApplicFlg() {
        return validApplicFlg;
    }

    public void setValidApplicFlg(String validApplicFlg) {
        this.validApplicFlg = validApplicFlg;
    }

    public Integer getVoicePID() {
        return voicePID;
    }

    public void setVoicePID(Integer voicePID) {
        this.voicePID = voicePID;
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

    public Short getWithDrawErrCnt() {
        return withDrawErrCnt;
    }

    public void setWithDrawErrCnt(Short withDrawErrCnt) {
        this.withDrawErrCnt = withDrawErrCnt;
    }

    public Short getWithDrawFlg1() {
        return withDrawFlg1;
    }

    public void setWithDrawFlg1(Short withDrawFlg1) {
        this.withDrawFlg1 = withDrawFlg1;
    }

    public Short getWithDrawFlg2() {
        return withDrawFlg2;
    }

    public void setWithDrawFlg2(Short withDrawFlg2) {
        this.withDrawFlg2 = withDrawFlg2;
    }

    public Short getWithDrawFlg3() {
        return withDrawFlg3;
    }

    public void setWithDrawFlg3(Short withDrawFlg3) {
        this.withDrawFlg3 = withDrawFlg3;
    }

    public String getWithDrawPeriodEdDt() {
        return withDrawPeriodEdDt;
    }

    public void setWithDrawPeriodEdDt(String withDrawPeriodEdDt) {
        this.withDrawPeriodEdDt = withDrawPeriodEdDt;
    }

    public String getWithDrawPeriodStDt() {
        return withDrawPeriodStDt;
    }

    public void setWithDrawPeriodStDt(String withDrawPeriodStDt) {
        this.withDrawPeriodStDt = withDrawPeriodStDt;
    }
}
