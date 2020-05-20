package com.staffchannel.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="T_PIL_Case_Header")
public class CaseHeader implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2825568420557853375L;

	@Id @GeneratedValue(strategy =GenerationType.AUTO)
	private Integer seq;

	@Column(name="cWebApplyDateTime")
	private Date cWebApplyDateTime;

	@Column(name="cWebSendDateTime")
	private Date cWebSendDateTime;

	@Column(name="cWebApplyStatus")
	private Integer cWebApplyStatus;

	@Column(name="cWebApplyNo")
	private String cWebApplyNo;

	@Column(name="cPrimAAPSNo")
	private String cPrimAAPSNo;

	@Column(name="ZL02")
	private String ZL02;

	@Column(name="cWebApplyStep")
	private String cWebApplyStep;

	@Column(name="DataChain")
	private Integer DataChain;

	@Column(name="R")
	private Integer R;

	@Column(name="CASE_NO")
	private String CASE_NO;

	@Column(name="cIdNumber")
	private String cIdNumber;

	@Column(name="cChName")
	private String cChName;

	@Column(name="cMobileNum")
	private String cMobileNum;

	@Column(name="cCustomerType")
	private String cCustomerType;

	@Column(name="IS_SEND_ORBIT")
	private String IS_SEND_ORBIT;

	@Column(name="IS_MKT_LIST")
	private String IS_MKT_LIST;

	@Column(name="APPLY_SALES_BEGIN_DATETIME")
	private Date APPLY_SALES_BEGIN_DATETIME;

	@Column(name="STEP1_DATETIME")
	private Date STEP1_DATETIME;

	@Column(name="STEP2_DATETIME")
	private Date STEP2_DATETIME;

	@Column(name="STEP3_DATETIME")
	private Date STEP3_DATETIME;

	@Column(name="SEND_ORBIT_DATETIME")
	private Date SEND_ORBIT_DATETIME;

	@Column(name="ORBIT_FEEDBACK_DATETIME")
	private Date ORBIT_FEEDBACK_DATETIME;
	
	@OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="seq", referencedColumnName="seq")
	private CaseDetail caseDetail = new CaseDetail();

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public Date getcWebApplyDateTime() {
		return cWebApplyDateTime;
	}

	public void setcWebApplyDateTime(Date cWebApplyDateTime) {
		this.cWebApplyDateTime = cWebApplyDateTime;
	}

	public Date getcWebSendDateTime() {
		return cWebSendDateTime;
	}

	public void setcWebSendDateTime(Date cWebSendDateTime) {
		this.cWebSendDateTime = cWebSendDateTime;
	}

	public Integer getcWebApplyStatus() {
		return cWebApplyStatus;
	}

	public void setcWebApplyStatus(Integer cWebApplyStatus) {
		this.cWebApplyStatus = cWebApplyStatus;
	}

	public String getcWebApplyNo() {
		return cWebApplyNo;
	}

	public void setcWebApplyNo(String cWebApplyNo) {
		this.cWebApplyNo = cWebApplyNo;
	}

	public String getcPrimAAPSNo() {
		return cPrimAAPSNo;
	}

	public void setcPrimAAPSNo(String cPrimAAPSNo) {
		this.cPrimAAPSNo = cPrimAAPSNo;
	}

	public String getZL02() {
		return ZL02;
	}

	public void setZL02(String zL02) {
		ZL02 = zL02;
	}

	public String getcWebApplyStep() {
		return cWebApplyStep;
	}

	public void setcWebApplyStep(String cWebApplyStep) {
		this.cWebApplyStep = cWebApplyStep;
	}

	public Integer getDataChain() {
		return DataChain;
	}

	public void setDataChain(Integer dataChain) {
		DataChain = dataChain;
	}

	public Integer getR() {
		return R;
	}

	public void setR(Integer r) {
		R = r;
	}

	public String getCASE_NO() {
		return CASE_NO;
	}

	public void setCASE_NO(String cASE_NO) {
		CASE_NO = cASE_NO;
	}

	public String getcIdNumber() {
		return cIdNumber;
	}

	public void setcIdNumber(String cIdNumber) {
		this.cIdNumber = cIdNumber;
	}

	public String getcChName() {
		return cChName;
	}

	public void setcChName(String cChName) {
		this.cChName = cChName;
	}

	public String getcMobileNum() {
		return cMobileNum;
	}

	public void setcMobileNum(String cMobileNum) {
		this.cMobileNum = cMobileNum;
	}

	public String getcCustomerType() {
		return cCustomerType;
	}

	public void setcCustomerType(String cCustomerType) {
		this.cCustomerType = cCustomerType;
	}

	public String getIS_SEND_ORBIT() {
		return IS_SEND_ORBIT;
	}

	public void setIS_SEND_ORBIT(String iS_SEND_ORBIT) {
		IS_SEND_ORBIT = iS_SEND_ORBIT;
	}

	public String getIS_MKT_LIST() {
		return IS_MKT_LIST;
	}

	public void setIS_MKT_LIST(String iS_MKT_LIST) {
		IS_MKT_LIST = iS_MKT_LIST;
	}

	public Date getAPPLY_SALES_BEGIN_DATETIME() {
		return APPLY_SALES_BEGIN_DATETIME;
	}

	public void setAPPLY_SALES_BEGIN_DATETIME(Date aPPLY_SALES_BEGIN_DATETIME) {
		APPLY_SALES_BEGIN_DATETIME = aPPLY_SALES_BEGIN_DATETIME;
	}

	public Date getSTEP1_DATETIME() {
		return STEP1_DATETIME;
	}

	public void setSTEP1_DATETIME(Date sTEP1_DATETIME) {
		STEP1_DATETIME = sTEP1_DATETIME;
	}

	public Date getSTEP2_DATETIME() {
		return STEP2_DATETIME;
	}

	public void setSTEP2_DATETIME(Date sTEP2_DATETIME) {
		STEP2_DATETIME = sTEP2_DATETIME;
	}

	public Date getSTEP3_DATETIME() {
		return STEP3_DATETIME;
	}

	public void setSTEP3_DATETIME(Date sTEP3_DATETIME) {
		STEP3_DATETIME = sTEP3_DATETIME;
	}

	public Date getSEND_ORBIT_DATETIME() {
		return SEND_ORBIT_DATETIME;
	}

	public void setSEND_ORBIT_DATETIME(Date sEND_ORBIT_DATETIME) {
		SEND_ORBIT_DATETIME = sEND_ORBIT_DATETIME;
	}

	public Date getORBIT_FEEDBACK_DATETIME() {
		return ORBIT_FEEDBACK_DATETIME;
	}

	public void setORBIT_FEEDBACK_DATETIME(Date oRBIT_FEEDBACK_DATETIME) {
		ORBIT_FEEDBACK_DATETIME = oRBIT_FEEDBACK_DATETIME;
	}

	public CaseDetail getCaseDetail() {
		return caseDetail;
	}

	public void setCaseDetail(CaseDetail caseDetail) {
		this.caseDetail = caseDetail;
	}

}
