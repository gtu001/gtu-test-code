package com.gtu.rest.sqlserver.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 * The persistent class for the TrustAcct database table.
 * 
 */
@Entity
@Table(name = "TrustAcct", catalog = "CTFLHistory", schema = "dbo")
public class TrustAcct implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "TrustAcct")
    private String trustAcct;

    @Column(name = "AcctCloseDate")
    private String acctCloseDate;

    @Column(name = "AcctInitDate")
    private String acctInitDate;

    @Column(name = "AcctOwnerID")
    private String acctOwnerID;

    @Column(name = "AdditionID")
    private String additionID;

    @Column(name = "ApplicationID")
    private String applicationID;

    @Column(name = "ApplicationIDNew")
    private String applicationIDNew;

    @Column(name = "ApplicationIDP")
    private String applicationIDP;

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

    @Column(name = "CertifNo")
    private int certifNo;

    private short CTFType;

    @Column(name = "CustCorpID")
    private String custCorpID;

    @Column(name = "CustomerID")
    private String customerID;

    @Column(name = "DistributeAcct")
    private String distributeAcct;

    @Column(name = "DistributeBankID")
    private String distributeBankID;

    @Column(name = "DividendType")
    private short dividendType;

    @Column(name = "FeePurchase")
    private BigDecimal feePurchase;

    @Column(name = "FeeRedeem")
    private BigDecimal feeRedeem;

    @Column(name = "FeeSwitch")
    private BigDecimal feeSwitch;

    @Column(name = "FeeType")
    private short feeType;

    @Column(name = "FourPartBranchID")
    private String fourPartBranchID;

    @Column(name = "FourPartCanvasser")
    private String fourPartCanvasser;

    @Column(name = "FourPartID")
    private String fourPartID;

    @Column(name = "FourPartLicenceID")
    private String fourPartLicenceID;

    @Column(name = "FourPartLicenceNo")
    private String fourPartLicenceNo;

    @Column(name = "FundID")
    private String fundID;

    @Column(name = "OrgBankID")
    private String orgBankID;

    @Column(name = "PayType")
    private short payType;

    @Column(name = "ProjectID")
    private String projectID;

    @Column(name = "PurchDistributeNo")
    private String purchDistributeNo;

    @Column(name = "RedeemDistributeNo")
    private String redeemDistributeNo;

    @Column(name = "SwitchDistributeNo")
    private String switchDistributeNo;

    @Column(name = "TradeStatus")
    private short tradeStatus;

    @Column(name = "TradeType")
    private String tradeType;

    @Column(name = "TrustAcctStatus")
    private short trustAcctStatus;

    @Column(name = "TrustCapital")
    private BigDecimal trustCapital;

    @Column(name = "TrustType")
    private short trustType;

    @Column(name = "TxAltLock")
    private short txAltLock;

    @Column(name = "TxBranchID")
    private String txBranchID;

    @Column(name = "TxCapital")
    private BigDecimal txCapital;

    @Column(name = "TxLock")
    private int txLock;

    @Column(name = "TxOSCapital")
    private BigDecimal txOSCapital;

    @Column(name = "TxPUnits")
    private BigDecimal txPUnits;

    @Column(name = "TxRUnits")
    private BigDecimal txRUnits;

    @Column(name = "TxSUnits")
    private BigDecimal txSUnits;

    @Column(name = "TxUnits")
    private BigDecimal txUnits;

    @Column(name = "Units")
    private BigDecimal units;

    @Column(name = "WithDrawAcct")
    private String withDrawAcct;

    @Column(name = "WithDrawCardNo")
    private String withDrawCardNo;

    @Column(name = "WithDrawCnt")
    private int withDrawCnt;

    @Column(name = "WithDrawContinueStopCnt")
    private short withDrawContinueStopCnt;

    @Column(name = "WithDrawErrCnt")
    private short withDrawErrCnt;

    @Column(name = "WithDrawStatus")
    private short withDrawStatus;

    @Column(name = "WithDrawStopCnt")
    private short withDrawStopCnt;

    public TrustAcct() {
    }

    public String getTrustAcct() {
        return this.trustAcct;
    }

    public void setTrustAcct(String trustAcct) {
        this.trustAcct = trustAcct;
    }

    public String getAcctCloseDate() {
        return this.acctCloseDate;
    }

    public void setAcctCloseDate(String acctCloseDate) {
        this.acctCloseDate = acctCloseDate;
    }

    public String getAcctInitDate() {
        return this.acctInitDate;
    }

    public void setAcctInitDate(String acctInitDate) {
        this.acctInitDate = acctInitDate;
    }

    public String getAcctOwnerID() {
        return this.acctOwnerID;
    }

    public void setAcctOwnerID(String acctOwnerID) {
        this.acctOwnerID = acctOwnerID;
    }

    public String getAdditionID() {
        return this.additionID;
    }

    public void setAdditionID(String additionID) {
        this.additionID = additionID;
    }

    public String getApplicationID() {
        return this.applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public String getApplicationIDNew() {
        return this.applicationIDNew;
    }

    public void setApplicationIDNew(String applicationIDNew) {
        this.applicationIDNew = applicationIDNew;
    }

    public String getApplicationIDP() {
        return this.applicationIDP;
    }

    public void setApplicationIDP(String applicationIDP) {
        this.applicationIDP = applicationIDP;
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

    public int getCertifNo() {
        return this.certifNo;
    }

    public void setCertifNo(int certifNo) {
        this.certifNo = certifNo;
    }

    public short getCTFType() {
        return this.CTFType;
    }

    public void setCTFType(short CTFType) {
        this.CTFType = CTFType;
    }

    public String getCustCorpID() {
        return this.custCorpID;
    }

    public void setCustCorpID(String custCorpID) {
        this.custCorpID = custCorpID;
    }

    public String getCustomerID() {
        return this.customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
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

    public short getDividendType() {
        return this.dividendType;
    }

    public void setDividendType(short dividendType) {
        this.dividendType = dividendType;
    }

    public BigDecimal getFeePurchase() {
        return this.feePurchase;
    }

    public void setFeePurchase(BigDecimal feePurchase) {
        this.feePurchase = feePurchase;
    }

    public BigDecimal getFeeRedeem() {
        return this.feeRedeem;
    }

    public void setFeeRedeem(BigDecimal feeRedeem) {
        this.feeRedeem = feeRedeem;
    }

    public BigDecimal getFeeSwitch() {
        return this.feeSwitch;
    }

    public void setFeeSwitch(BigDecimal feeSwitch) {
        this.feeSwitch = feeSwitch;
    }

    public short getFeeType() {
        return this.feeType;
    }

    public void setFeeType(short feeType) {
        this.feeType = feeType;
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

    public String getFourPartLicenceID() {
        return this.fourPartLicenceID;
    }

    public void setFourPartLicenceID(String fourPartLicenceID) {
        this.fourPartLicenceID = fourPartLicenceID;
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

    public String getOrgBankID() {
        return this.orgBankID;
    }

    public void setOrgBankID(String orgBankID) {
        this.orgBankID = orgBankID;
    }

    public short getPayType() {
        return this.payType;
    }

    public void setPayType(short payType) {
        this.payType = payType;
    }

    public String getProjectID() {
        return this.projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getPurchDistributeNo() {
        return this.purchDistributeNo;
    }

    public void setPurchDistributeNo(String purchDistributeNo) {
        this.purchDistributeNo = purchDistributeNo;
    }

    public String getRedeemDistributeNo() {
        return this.redeemDistributeNo;
    }

    public void setRedeemDistributeNo(String redeemDistributeNo) {
        this.redeemDistributeNo = redeemDistributeNo;
    }

    public String getSwitchDistributeNo() {
        return this.switchDistributeNo;
    }

    public void setSwitchDistributeNo(String switchDistributeNo) {
        this.switchDistributeNo = switchDistributeNo;
    }

    public short getTradeStatus() {
        return this.tradeStatus;
    }

    public void setTradeStatus(short tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getTradeType() {
        return this.tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public short getTrustAcctStatus() {
        return this.trustAcctStatus;
    }

    public void setTrustAcctStatus(short trustAcctStatus) {
        this.trustAcctStatus = trustAcctStatus;
    }

    public BigDecimal getTrustCapital() {
        return this.trustCapital;
    }

    public void setTrustCapital(BigDecimal trustCapital) {
        this.trustCapital = trustCapital;
    }

    public short getTrustType() {
        return this.trustType;
    }

    public void setTrustType(short trustType) {
        this.trustType = trustType;
    }

    public short getTxAltLock() {
        return this.txAltLock;
    }

    public void setTxAltLock(short txAltLock) {
        this.txAltLock = txAltLock;
    }

    public String getTxBranchID() {
        return this.txBranchID;
    }

    public void setTxBranchID(String txBranchID) {
        this.txBranchID = txBranchID;
    }

    public BigDecimal getTxCapital() {
        return this.txCapital;
    }

    public void setTxCapital(BigDecimal txCapital) {
        this.txCapital = txCapital;
    }

    public int getTxLock() {
        return this.txLock;
    }

    public void setTxLock(int txLock) {
        this.txLock = txLock;
    }

    public BigDecimal getTxOSCapital() {
        return this.txOSCapital;
    }

    public void setTxOSCapital(BigDecimal txOSCapital) {
        this.txOSCapital = txOSCapital;
    }

    public BigDecimal getTxPUnits() {
        return this.txPUnits;
    }

    public void setTxPUnits(BigDecimal txPUnits) {
        this.txPUnits = txPUnits;
    }

    public BigDecimal getTxRUnits() {
        return this.txRUnits;
    }

    public void setTxRUnits(BigDecimal txRUnits) {
        this.txRUnits = txRUnits;
    }

    public BigDecimal getTxSUnits() {
        return this.txSUnits;
    }

    public void setTxSUnits(BigDecimal txSUnits) {
        this.txSUnits = txSUnits;
    }

    public BigDecimal getTxUnits() {
        return this.txUnits;
    }

    public void setTxUnits(BigDecimal txUnits) {
        this.txUnits = txUnits;
    }

    public BigDecimal getUnits() {
        return this.units;
    }

    public void setUnits(BigDecimal units) {
        this.units = units;
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

    public int getWithDrawCnt() {
        return this.withDrawCnt;
    }

    public void setWithDrawCnt(int withDrawCnt) {
        this.withDrawCnt = withDrawCnt;
    }

    public short getWithDrawContinueStopCnt() {
        return this.withDrawContinueStopCnt;
    }

    public void setWithDrawContinueStopCnt(short withDrawContinueStopCnt) {
        this.withDrawContinueStopCnt = withDrawContinueStopCnt;
    }

    public short getWithDrawErrCnt() {
        return this.withDrawErrCnt;
    }

    public void setWithDrawErrCnt(short withDrawErrCnt) {
        this.withDrawErrCnt = withDrawErrCnt;
    }

    public short getWithDrawStatus() {
        return this.withDrawStatus;
    }

    public void setWithDrawStatus(short withDrawStatus) {
        this.withDrawStatus = withDrawStatus;
    }

    public short getWithDrawStopCnt() {
        return this.withDrawStopCnt;
    }

    public void setWithDrawStopCnt(short withDrawStopCnt) {
        this.withDrawStopCnt = withDrawStopCnt;
    }

}