package gtu.quartz.ex3;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@Entity
@Table(name = "ACCT_BOND_TRUST_HOLDING_TEMP")
public class AcctBondTrustHoldingTemp {

    @EmbeddedId
    AcctBondTrustHoldingTempPk acctBondTrustHoldingTempPk;

    @Column(name = "CUST_ID")
    private String custId;

    @Column(name = "CONTRACT_NOTE_NBR")
    private String contractNoteNbr;

    @Column(name = "CUSTODIAN_INSTITUTE")
    private String custodianInstitute;

    @Column(name = "TMP_PRINCIPLE_PAYMENT")
    private BigDecimal tmpPrinciplePayment;

    @Column(name = "REDEEM_BRANCH_CODE")
    private BigDecimal redeemBranchCode;

    @Column(name = "REDEEM_AMT")
    private BigDecimal redeemAmt;

    @Column(name = "RESERVERS_UNIT")
    private BigDecimal reserversUnit;

    @Column(name = "FX_CURRENT_BAL")
    private BigDecimal fxCurrentBal;

    @Column(name = "NTD_CURRENT_BAL")
    private BigDecimal ntdCurrentBal;

    @Column(name = "FX_TXN_DEAL_AMT")
    private BigDecimal fxTxnDealAmt;

    @Column(name = "NTD_TXN_DEAL_AMT")
    private BigDecimal ntdTxnDealAmt;

    @Column(name = "TRUST_TYPE_CODE")
    private String trustTypeCode;

    @Column(name = "BON_TRUST_ACCT_NBR")
    private String bonTrustAcctNbr;

    @Column(name = "DISTRIBUTE_BANK_CODE")
    private String distributeBankCode;

    @Column(name = "DISTRIBUTE_ACCT_NBR")
    private String distributeAcctNbr;

    @Column(name = "BRANCH_CODE")
    private String branchCode;

    @Column(name = "PROJECT_ID")
    private String projectId;

    @Column(name = "BOND_MATURITY_IND")
    private String bondMaturityInd;

    @Column(name = "DBU_OBU_TXN_IND")
    private String dbuObuTxnInd;

    @Column(name = "UPPER_DOWN_ALERT_SET_IND")
    private String upperDownAlertSetInd;

    @Column(name = "SATIATION_POINT_PERCENT")
    private BigDecimal satiationPointPercent;

    @Column(name = "STOP_LOSS_POINT_PERCENT")
    private BigDecimal stopLossPointPercent;

    @Column(name = "RESERVE_FIELD")
    private String reserveField;

    @Column(name = "MANAGEMENT_CHARAGE_FEE_FLAG")
    private String managementCharageFeeFlag;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "UPDATE_DTTM")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime updateDttm;

    public AcctBondTrustHoldingTemp() {

    }

    public AcctBondTrustHoldingTemp(String custId, String contractNoteNbr, String custodianInstitute,
            BigDecimal tmpPrinciplePayment, BigDecimal redeemBranchCode,
            BigDecimal redeemAmt, BigDecimal reserversUnit, BigDecimal fxCurrentBal, BigDecimal ntdCurrentBal,
            BigDecimal fxTxnDealAmt, BigDecimal ntdTxnDealAmt, String trustTypeCode, String bonTrustAcctNbr,
            String distributeBankCode, String distributeAcctNbr, String branchCode, String projectId,
            String bondMaturityInd, String dbuObuTxnInd, String upperDownAlertSetInd, BigDecimal satiationPointPercent,
            BigDecimal stopLossPointPercent, String reserveField, String managementCharageFeeFlag, String email,
            LocalDateTime updateDttm) {
        super();
        this.custId = custId;
        this.contractNoteNbr = contractNoteNbr;
        this.custodianInstitute = custodianInstitute;
        this.tmpPrinciplePayment = tmpPrinciplePayment;
        this.redeemBranchCode = redeemBranchCode;
        this.redeemAmt = redeemAmt;
        this.reserversUnit = reserversUnit;
        this.fxCurrentBal = fxCurrentBal;
        this.ntdCurrentBal = ntdCurrentBal;
        this.fxTxnDealAmt = fxTxnDealAmt;
        this.ntdTxnDealAmt = ntdTxnDealAmt;
        this.trustTypeCode = trustTypeCode;
        this.bonTrustAcctNbr = bonTrustAcctNbr;
        this.distributeBankCode = distributeBankCode;
        this.distributeAcctNbr = distributeAcctNbr;
        this.branchCode = branchCode;
        this.projectId = projectId;
        this.bondMaturityInd = bondMaturityInd;
        this.dbuObuTxnInd = dbuObuTxnInd;
        this.upperDownAlertSetInd = upperDownAlertSetInd;
        this.satiationPointPercent = satiationPointPercent;
        this.stopLossPointPercent = stopLossPointPercent;
        this.reserveField = reserveField;
        this.managementCharageFeeFlag = managementCharageFeeFlag;
        this.email = email;
        this.updateDttm = updateDttm;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getContractNoteNbr() {
        return contractNoteNbr;
    }

    public void setContractNoteNbr(String contractNoteNbr) {
        this.contractNoteNbr = contractNoteNbr;
    }

    public String getCustodianInstitute() {
        return custodianInstitute;
    }

    public void setCustodianInstitute(String custodianInstitute) {
        this.custodianInstitute = custodianInstitute;
    }

    public BigDecimal getTmpPrinciplePayment() {
        return tmpPrinciplePayment;
    }

    public void setTmpPrinciplePayment(BigDecimal tmpPrinciplePayment) {
        this.tmpPrinciplePayment = tmpPrinciplePayment;
    }

    public BigDecimal getRedeemBranchCode() {
        return redeemBranchCode;
    }

    public void setRedeemBranchCode(BigDecimal redeemBranchCode) {
        this.redeemBranchCode = redeemBranchCode;
    }

    public BigDecimal getRedeemAmt() {
        return redeemAmt;
    }

    public void setRedeemAmt(BigDecimal redeemAmt) {
        this.redeemAmt = redeemAmt;
    }

    public BigDecimal getReserversUnit() {
        return reserversUnit;
    }

    public void setReserversUnit(BigDecimal reserversUnit) {
        this.reserversUnit = reserversUnit;
    }

    public BigDecimal getFxCurrentBal() {
        return fxCurrentBal;
    }

    public void setFxCurrentBal(BigDecimal fxCurrentBal) {
        this.fxCurrentBal = fxCurrentBal;
    }

    public BigDecimal getNtdCurrentBal() {
        return ntdCurrentBal;
    }

    public void setNtdCurrentBal(BigDecimal ntdCurrentBal) {
        this.ntdCurrentBal = ntdCurrentBal;
    }

    public BigDecimal getFxTxnDealAmt() {
        return fxTxnDealAmt;
    }

    public void setFxTxnDealAmt(BigDecimal fxTxnDealAmt) {
        this.fxTxnDealAmt = fxTxnDealAmt;
    }

    public BigDecimal getNtdTxnDealAmt() {
        return ntdTxnDealAmt;
    }

    public void setNtdTxnDealAmt(BigDecimal ntdTxnDealAmt) {
        this.ntdTxnDealAmt = ntdTxnDealAmt;
    }

    public String getTrustTypeCode() {
        return trustTypeCode;
    }

    public void setTrustTypeCode(String trustTypeCode) {
        this.trustTypeCode = trustTypeCode;
    }

    public String getBonTrustAcctNbr() {
        return bonTrustAcctNbr;
    }

    public void setBonTrustAcctNbr(String bonTrustAcctNbr) {
        this.bonTrustAcctNbr = bonTrustAcctNbr;
    }

    public String getDistributeBankCode() {
        return distributeBankCode;
    }

    public void setDistributeBankCode(String distributeBankCode) {
        this.distributeBankCode = distributeBankCode;
    }

    public String getDistributeAcctNbr() {
        return distributeAcctNbr;
    }

    public void setDistributeAcctNbr(String distributeAcctNbr) {
        this.distributeAcctNbr = distributeAcctNbr;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getBondMaturityInd() {
        return bondMaturityInd;
    }

    public void setBondMaturityInd(String bondMaturityInd) {
        this.bondMaturityInd = bondMaturityInd;
    }

    public String getDbuObuTxnInd() {
        return dbuObuTxnInd;
    }

    public void setDbuObuTxnInd(String dbuObuTxnInd) {
        this.dbuObuTxnInd = dbuObuTxnInd;
    }

    public String getUpperDownAlertSetInd() {
        return upperDownAlertSetInd;
    }

    public void setUpperDownAlertSetInd(String upperDownAlertSetInd) {
        this.upperDownAlertSetInd = upperDownAlertSetInd;
    }

    public BigDecimal getSatiationPointPercent() {
        return satiationPointPercent;
    }

    public void setSatiationPointPercent(BigDecimal satiationPointPercent) {
        this.satiationPointPercent = satiationPointPercent;
    }

    public BigDecimal getStopLossPointPercent() {
        return stopLossPointPercent;
    }

    public void setStopLossPointPercent(BigDecimal stopLossPointPercent) {
        this.stopLossPointPercent = stopLossPointPercent;
    }

    public String getReserveField() {
        return reserveField;
    }

    public void setReserveField(String reserveField) {
        this.reserveField = reserveField;
    }

    public String getManagementCharageFeeFlag() {
        return managementCharageFeeFlag;
    }

    public void setManagementCharageFeeFlag(String managementCharageFeeFlag) {
        this.managementCharageFeeFlag = managementCharageFeeFlag;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getUpdateDttm() {
        return updateDttm;
    }

    public void setUpdateDttm(LocalDateTime updateDttm) {
        this.updateDttm = updateDttm;
    }
}
