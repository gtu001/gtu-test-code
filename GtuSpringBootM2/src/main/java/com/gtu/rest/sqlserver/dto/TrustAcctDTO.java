package com.gtu.rest.sqlserver.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TrustAcctDTO {

    @JsonProperty(value="TrustAcct")
    private String trustAcct;

    @JsonProperty(value="AcctCloseDate")
    private String acctCloseDate;

    @JsonProperty(value="AcctInitDate")
    private String acctInitDate;

    @JsonProperty(value="AcctOwnerID")
    private String acctOwnerID;

    @JsonProperty(value="AdditionID")
    private String additionID;

    @JsonProperty(value="ApplicationID")
    private String applicationID;

    @JsonProperty(value="ApplicationIDNew")
    private String applicationIDNew;

    @JsonProperty(value="ApplicationIDP")
    private String applicationIDP;

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

    @JsonProperty(value="CertifNo")
    private Integer certifNo;

    @JsonProperty(value="CTFType")
    private Short CTFType;

    @JsonProperty(value="CustCorpID")
    private String custCorpID;

    @JsonProperty(value="CustomerID")
    private String customerID;

    @JsonProperty(value="DistributeAcct")
    private String distributeAcct;

    @JsonProperty(value="DistributeBankID")
    private String distributeBankID;

    @JsonProperty(value="DividendType")
    private Short dividendType;

    @JsonProperty(value="FeePurchase")
    private BigDecimal feePurchase;

    @JsonProperty(value="FeeRedeem")
    private BigDecimal feeRedeem;

    @JsonProperty(value="FeeSwitch")
    private BigDecimal feeSwitch;

    @JsonProperty(value="FeeType")
    private Short feeType;

    @JsonProperty(value="FourPartBranchID")
    private String fourPartBranchID;

    @JsonProperty(value="FourPartCanvasser")
    private String fourPartCanvasser;

    @JsonProperty(value="FourPartID")
    private String fourPartID;

    @JsonProperty(value="FourPartLicenceID")
    private String fourPartLicenceID;

    @JsonProperty(value="FourPartLicenceNo")
    private String fourPartLicenceNo;

    @JsonProperty(value="FundID")
    private String fundID;

    @JsonProperty(value="OrgBankID")
    private String orgBankID;

    @JsonProperty(value="PayType")
    private Short payType;

    @JsonProperty(value="ProjectID")
    private String projectID;

    @JsonProperty(value="PurchDistributeNo")
    private String purchDistributeNo;

    @JsonProperty(value="RedeemDistributeNo")
    private String redeemDistributeNo;

    @JsonProperty(value="SwitchDistributeNo")
    private String switchDistributeNo;

    @JsonProperty(value="TradeStatus")
    private Short tradeStatus;

    @JsonProperty(value="TradeType")
    private String tradeType;

    @JsonProperty(value="TrustAcctStatus")
    private Short trustAcctStatus;

    @JsonProperty(value="TrustCapital")
    private BigDecimal trustCapital;

    @JsonProperty(value="TrustType")
    private Short trustType;

    @JsonProperty(value="TxAltLock")
    private Short txAltLock;

    @JsonProperty(value="TxBranchID")
    private String txBranchID;

    @JsonProperty(value="TxCapital")
    private BigDecimal txCapital;

    @JsonProperty(value="TxLock")
    private Integer txLock;

    @JsonProperty(value="TxOSCapital")
    private BigDecimal txOSCapital;

    @JsonProperty(value="TxPUnits")
    private BigDecimal txPUnits;

    @JsonProperty(value="TxRUnits")
    private BigDecimal txRUnits;

    @JsonProperty(value="TxSUnits")
    private BigDecimal txSUnits;

    @JsonProperty(value="TxUnits")
    private BigDecimal txUnits;

    @JsonProperty(value="Units")
    private BigDecimal units;

    @JsonProperty(value="WithDrawAcct")
    private String withDrawAcct;

    @JsonProperty(value="WithDrawCardNo")
    private String withDrawCardNo;

    @JsonProperty(value="WithDrawCnt")
    private Integer withDrawCnt;

    @JsonProperty(value="WithDrawContinueStopCnt")
    private Short withDrawContinueStopCnt;

    @JsonProperty(value="WithDrawErrCnt")
    private Short withDrawErrCnt;

    @JsonProperty(value="WithDrawStatus")
    private Short withDrawStatus;

    @JsonProperty(value="WithDrawStopCnt")
    private Short withDrawStopCnt;

    public String getTrustAcct() {
        return trustAcct;
    }

    public void setTrustAcct(String trustAcct) {
        this.trustAcct = trustAcct;
    }

    public String getAcctCloseDate() {
        return acctCloseDate;
    }

    public void setAcctCloseDate(String acctCloseDate) {
        this.acctCloseDate = acctCloseDate;
    }

    public String getAcctInitDate() {
        return acctInitDate;
    }

    public void setAcctInitDate(String acctInitDate) {
        this.acctInitDate = acctInitDate;
    }

    public String getAcctOwnerID() {
        return acctOwnerID;
    }

    public void setAcctOwnerID(String acctOwnerID) {
        this.acctOwnerID = acctOwnerID;
    }

    public String getAdditionID() {
        return additionID;
    }

    public void setAdditionID(String additionID) {
        this.additionID = additionID;
    }

    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public String getApplicationIDNew() {
        return applicationIDNew;
    }

    public void setApplicationIDNew(String applicationIDNew) {
        this.applicationIDNew = applicationIDNew;
    }

    public String getApplicationIDP() {
        return applicationIDP;
    }

    public void setApplicationIDP(String applicationIDP) {
        this.applicationIDP = applicationIDP;
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

    public Integer getCertifNo() {
        return certifNo;
    }

    public void setCertifNo(Integer certifNo) {
        this.certifNo = certifNo;
    }

    public Short getCTFType() {
        return CTFType;
    }

    public void setCTFType(Short cTFType) {
        CTFType = cTFType;
    }

    public String getCustCorpID() {
        return custCorpID;
    }

    public void setCustCorpID(String custCorpID) {
        this.custCorpID = custCorpID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
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

    public BigDecimal getFeePurchase() {
        return feePurchase;
    }

    public void setFeePurchase(BigDecimal feePurchase) {
        this.feePurchase = feePurchase;
    }

    public BigDecimal getFeeRedeem() {
        return feeRedeem;
    }

    public void setFeeRedeem(BigDecimal feeRedeem) {
        this.feeRedeem = feeRedeem;
    }

    public BigDecimal getFeeSwitch() {
        return feeSwitch;
    }

    public void setFeeSwitch(BigDecimal feeSwitch) {
        this.feeSwitch = feeSwitch;
    }

    public Short getFeeType() {
        return feeType;
    }

    public void setFeeType(Short feeType) {
        this.feeType = feeType;
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

    public String getFourPartLicenceID() {
        return fourPartLicenceID;
    }

    public void setFourPartLicenceID(String fourPartLicenceID) {
        this.fourPartLicenceID = fourPartLicenceID;
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

    public String getOrgBankID() {
        return orgBankID;
    }

    public void setOrgBankID(String orgBankID) {
        this.orgBankID = orgBankID;
    }

    public Short getPayType() {
        return payType;
    }

    public void setPayType(Short payType) {
        this.payType = payType;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getPurchDistributeNo() {
        return purchDistributeNo;
    }

    public void setPurchDistributeNo(String purchDistributeNo) {
        this.purchDistributeNo = purchDistributeNo;
    }

    public String getRedeemDistributeNo() {
        return redeemDistributeNo;
    }

    public void setRedeemDistributeNo(String redeemDistributeNo) {
        this.redeemDistributeNo = redeemDistributeNo;
    }

    public String getSwitchDistributeNo() {
        return switchDistributeNo;
    }

    public void setSwitchDistributeNo(String switchDistributeNo) {
        this.switchDistributeNo = switchDistributeNo;
    }

    public Short getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(Short tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public Short getTrustAcctStatus() {
        return trustAcctStatus;
    }

    public void setTrustAcctStatus(Short trustAcctStatus) {
        this.trustAcctStatus = trustAcctStatus;
    }

    public BigDecimal getTrustCapital() {
        return trustCapital;
    }

    public void setTrustCapital(BigDecimal trustCapital) {
        this.trustCapital = trustCapital;
    }

    public Short getTrustType() {
        return trustType;
    }

    public void setTrustType(Short trustType) {
        this.trustType = trustType;
    }

    public Short getTxAltLock() {
        return txAltLock;
    }

    public void setTxAltLock(Short txAltLock) {
        this.txAltLock = txAltLock;
    }

    public String getTxBranchID() {
        return txBranchID;
    }

    public void setTxBranchID(String txBranchID) {
        this.txBranchID = txBranchID;
    }

    public BigDecimal getTxCapital() {
        return txCapital;
    }

    public void setTxCapital(BigDecimal txCapital) {
        this.txCapital = txCapital;
    }

    public Integer getTxLock() {
        return txLock;
    }

    public void setTxLock(Integer txLock) {
        this.txLock = txLock;
    }

    public BigDecimal getTxOSCapital() {
        return txOSCapital;
    }

    public void setTxOSCapital(BigDecimal txOSCapital) {
        this.txOSCapital = txOSCapital;
    }

    public BigDecimal getTxPUnits() {
        return txPUnits;
    }

    public void setTxPUnits(BigDecimal txPUnits) {
        this.txPUnits = txPUnits;
    }

    public BigDecimal getTxRUnits() {
        return txRUnits;
    }

    public void setTxRUnits(BigDecimal txRUnits) {
        this.txRUnits = txRUnits;
    }

    public BigDecimal getTxSUnits() {
        return txSUnits;
    }

    public void setTxSUnits(BigDecimal txSUnits) {
        this.txSUnits = txSUnits;
    }

    public BigDecimal getTxUnits() {
        return txUnits;
    }

    public void setTxUnits(BigDecimal txUnits) {
        this.txUnits = txUnits;
    }

    public BigDecimal getUnits() {
        return units;
    }

    public void setUnits(BigDecimal units) {
        this.units = units;
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

    public Integer getWithDrawCnt() {
        return withDrawCnt;
    }

    public void setWithDrawCnt(Integer withDrawCnt) {
        this.withDrawCnt = withDrawCnt;
    }

    public Short getWithDrawContinueStopCnt() {
        return withDrawContinueStopCnt;
    }

    public void setWithDrawContinueStopCnt(Short withDrawContinueStopCnt) {
        this.withDrawContinueStopCnt = withDrawContinueStopCnt;
    }

    public Short getWithDrawErrCnt() {
        return withDrawErrCnt;
    }

    public void setWithDrawErrCnt(Short withDrawErrCnt) {
        this.withDrawErrCnt = withDrawErrCnt;
    }

    public Short getWithDrawStatus() {
        return withDrawStatus;
    }

    public void setWithDrawStatus(Short withDrawStatus) {
        this.withDrawStatus = withDrawStatus;
    }

    public Short getWithDrawStopCnt() {
        return withDrawStopCnt;
    }

    public void setWithDrawStopCnt(Short withDrawStopCnt) {
        this.withDrawStopCnt = withDrawStopCnt;
    }
    
    
    
}
