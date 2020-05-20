package com.staffchannel.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="T_PIL_Case_Detail")
public class CaseDetail implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2568147271779429080L;

	@Id @GeneratedValue(strategy =GenerationType.AUTO)
	private Integer seq;

	@Column(name="cApplyURL")
	private String cApplyURL;

	@Column(name="psCompany")
	private String psCompany;

	@Column(name="psCompanyAns")
	private String psCompanyAns;

	@Column(name="psQuota")
	private String psQuota;

	@Column(name="psIntstRate")
	private String psIntstRate;

	@Column(name="psMonPay")
	private String psMonPay;

	@Column(name="psLastAmount")
	private String psLastAmount;

	@Column(name="psVidDate")
	private String psVidDate;

	@Column(name="psTotAnn")
	private String psTotAnn;

	@Column(name="psRiskLevel")
	private String psRiskLevel;

	@Column(name="psProduct")
	private String psProduct;

	@Column(name="psSystemIncome")
	private String psSystemIncome;

	@Column(name="psPPflag")
	private String psPPflag;

	@Column(name="cCommunicateTime")
	private String cCommunicateTime;

	@Column(name="IsCustomizedOffer")
	private String IsCustomizedOffer;

	@Column(name="cApplyProgram")
	private String cApplyProgram;

	@Column(name="ATLT")
	private String ATLT;

	@Column(name="TERM")
	private String TERM;

	@Column(name="psStartUpFee")
	private String psStartUpFee;

	@Column(name="OneStepStartUpFee")
	private String OneStepStartUpFee;

	@Column(name="TwoStepStartUpFee")
	private String TwoStepStartUpFee;

	@Column(name="ZP84")
	private String ZP84;

	@Column(name="cGrantsAccountBank")
	private String cGrantsAccountBank;

	@Column(name="cGrantsAccountBranch")
	private String cGrantsAccountBranch;

	@Column(name="cGrantsAccount")
	private String cGrantsAccount;

	@Column(name="cIsApplyEmailStatement")
	private String cIsApplyEmailStatement;

	@Column(name="cIsApplyShortMessageNotification")
	private String cIsApplyShortMessageNotification;

	@Column(name="cIsCompensatory")
	private String cIsCompensatory;

	@Column(name="Reviewed")
	private String Reviewed;

	@Column(name="ReviewDate")
	private String ReviewDate;

	@Column(name="Supplement")
	private String Supplement;

	@Column(name="NetBankAgreement")
	private String NetBankAgreement;

	@Column(name="NetPaper")
	private String NetPaper;

	@Column(name="NoticeWay")
	private String NoticeWay;

	@Column(name="Others")
	private String Others;

	@Column(name="cHUBCoSale")
	private String cHUBCoSale;

	@Column(name="cIdentifyWay")
	private String cIdentifyWay;

	@Column(name="cEMail")
	private String cEMail;

	@Column(name="cEngName")
	private String cEngName;

	@Column(name="ChangeName")
	private String ChangeName;

	@Column(name="cChNameEx")
	private String cChNameEx;

	@Column(name="cEngNameEx")
	private String cEngNameEx;

	@Column(name="cBirthday")
	private String cBirthday;

	@Column(name="cIdCardIssueCity")
	private String cIdCardIssueCity;

	@Column(name="LID1")
	private String LID1;

	@Column(name="LID2")
	private String LID2;

	@Column(name="LID3")
	private String LID3;

	@Column(name="cBirthPlaceCode")
	private String cBirthPlaceCode;

	@Column(name="cBirthCity")
	private String cBirthCity;

	@Column(name="cIsMultipleNationality")
	private String cIsMultipleNationality;

	@Column(name="cNationalCode2")
	private String cNationalCode2;

	@Column(name="cNationalCode3")
	private String cNationalCode3;

	@Column(name="EDU0")
	private String EDU0;

	@Column(name="mHouseOwnerType")
	private String mHouseOwnerType;

	@Column(name="cHouseholdAddr1")
	private String cHouseholdAddr1;

	@Column(name="cHouseholdAddr2")
	private String cHouseholdAddr2;

	@Column(name="cHouseholdAddr3")
	private String cHouseholdAddr3;

	@Column(name="cHouseholdAddr4")
	private String cHouseholdAddr4;

	@Column(name="mPrimPresentAddrType")
	private String mPrimPresentAddrType;

	@Column(name="PRIM_PRESENT_LIVING_DATE")
	private String PRIM_PRESENT_LIVING_DATE;

	@Column(name="cPresentAddr1")
	private String cPresentAddr1;

	@Column(name="cPresentAddr2")
	private String cPresentAddr2;

	@Column(name="cPresentAddr3")
	private String cPresentAddr3;

	@Column(name="cPresentAddr4")
	private String cPresentAddr4;

	@Column(name="cMailingAddrType")
	private String cMailingAddrType;

	@Column(name="cMailingAddr1")
	private String cMailingAddr1;

	@Column(name="cMailingAddr2")
	private String cMailingAddr2;

	@Column(name="cMailingAddr3")
	private String cMailingAddr3;

	@Column(name="cMailingAddr4")
	private String cMailingAddr4;

	@Column(name="TN20A")
	private String TN20A;

	@Column(name="TN20B")
	private String TN20B;

	@Column(name="IsReferee")
	private String IsReferee;

	@Column(name="cNameOfReferee")
	private String cNameOfReferee;

	@Column(name="cReferrerIdNumber")
	private String cReferrerIdNumber;

	@Column(name="PHA1A")
	private String PHA1A;

	@Column(name="PHA1B")
	private String PHA1B;

	@Column(name="ZH17")
	private String ZH17;

	@Column(name="ZA85")
	private String ZA85;

	@Column(name="cPrimAnnualSalary")
	private String cPrimAnnualSalary;

	@Column(name="TNI1")
	private String TNI1;

	@Column(name="cCompanyAddr1")
	private String cCompanyAddr1;

	@Column(name="cCompanyAddr2")
	private String cCompanyAddr2;

	@Column(name="cCompanyAddr3")
	private String cCompanyAddr3;

	@Column(name="cCompanyAddr4")
	private String cCompanyAddr4;

	@Column(name="TNO0A")
	private String TNO0A;

	@Column(name="TNO0B")
	private String TNO0B;

	@Column(name="TNO0C")
	private String TNO0C;

	@Column(name="TJB0")
	private String TJB0;

	@Column(name="cCompanyNameEx")
	private String cCompanyNameEx;

	@Column(name="cJobTitleEx")
	private String cJobTitleEx;

	@Column(name="RTJ0")
	private String RTJ0;

	@Column(name="cIsCompanyPrincipal")
	private String cIsCompanyPrincipal;

	@Column(name="cCompanyName")
	private String cCompanyName;

	@Column(name="cCompanyId")
	private String cCompanyId;

	@Column(name="cJobTitle")
	private String cJobTitle;

	@Column(name="cRelation")
	private String cRelation;

	@Column(name="cRelationName")
	private String cRelationName;

	@Column(name="cRelationId")
	private String cRelationId;

	@Column(name="cRelationForeignId")
	private String cRelationForeignId;

	@Column(name="cIsRelationCompanyPrincipal")
	private String cIsRelationCompanyPrincipal;

	@Column(name="cRelationCompanyName")
	private String cRelationCompanyName;

	@Column(name="cRelationCompanyId")
	private String cRelationCompanyId;

	@Column(name="cCompanyJobTitleOfRelatedPerson")
	private String cCompanyJobTitleOfRelatedPerson;

	@Column(name="CardStatusDesc")
	private String CardStatusDesc;

	@Column(name="CompanyStatusDesc")
	private String CompanyStatusDesc;

	@Column(name="otpSerialNo")
	private String otpSerialNo;

	@Column(name="otpSuccessDateTime")
	private String otpSuccessDateTime;

	@Column(name="cMarriage")
	private String cMarriage;

	@Column(name="cSex")
	private String cSex;

	@Column(name="mktCode")
	private String mktCode;

	@Column(name="userDevice")
	private String userDevice;

	@Column(name="mobileVPlusResult")
	private String mobileVPlusResult;

	@Column(name="mobileLastChangeDate")
	private String mobileLastChangeDate;

	@Column(name="OTPSendDXCDateTime")
	private Date OTPSendDXCDateTime;

	@Column(name="IsJoinReferee")
	private String IsJoinReferee;

	@Column(name="ocrChName")
	private String ocrChName;

	@Column(name="cCusMobileNum")
	private String cCusMobileNum;

	@Column(name="NEXT_COMM_TIME")
	private Date NEXT_COMM_TIME;

	@Column(name="isPreSelect")
	private String isPreSelect;

	@Column(name="ZL02_CUS")
	private String ZL02_CUS;

	@Column(name="AAPS_RESULT")
	private String AAPS_RESULT;

	@Column(name="AAPS_RESULT_DATE")
	private String AAPS_RESULT_DATE;

	@Column(name="PEPSanction")
	private String PEPSanction;

	@Column(name="cTaxQuantity")
	private String cTaxQuantity;

	@Column(name="cTaxCountry1")
	private String cTaxCountry1;

	@Column(name="cTaxID1")
	private String cTaxID1;

	@Column(name="cTaxReason1")
	private String cTaxReason1;

	@Column(name="cTaxReasonText1")
	private String cTaxReasonText1;

	@Column(name="cTaxCountry2")
	private String cTaxCountry2;

	@Column(name="cTaxID2")
	private String cTaxID2;

	@Column(name="cTaxReason2")
	private String cTaxReason2;

	@Column(name="cTaxReasonText2")
	private String cTaxReasonText2;

	@Column(name="ZCFG")
	private String ZCFG;

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getcApplyURL() {
		return cApplyURL;
	}

	public void setcApplyURL(String cApplyURL) {
		this.cApplyURL = cApplyURL;
	}

	public String getPsCompany() {
		return psCompany;
	}

	public void setPsCompany(String psCompany) {
		this.psCompany = psCompany;
	}

	public String getPsCompanyAns() {
		return psCompanyAns;
	}

	public void setPsCompanyAns(String psCompanyAns) {
		this.psCompanyAns = psCompanyAns;
	}

	public String getPsQuota() {
		return psQuota;
	}

	public void setPsQuota(String psQuota) {
		this.psQuota = psQuota;
	}

	public String getPsIntstRate() {
		return psIntstRate;
	}

	public void setPsIntstRate(String psIntstRate) {
		this.psIntstRate = psIntstRate;
	}

	public String getPsMonPay() {
		return psMonPay;
	}

	public void setPsMonPay(String psMonPay) {
		this.psMonPay = psMonPay;
	}

	public String getPsLastAmount() {
		return psLastAmount;
	}

	public void setPsLastAmount(String psLastAmount) {
		this.psLastAmount = psLastAmount;
	}

	public String getPsVidDate() {
		return psVidDate;
	}

	public void setPsVidDate(String psVidDate) {
		this.psVidDate = psVidDate;
	}

	public String getPsTotAnn() {
		return psTotAnn;
	}

	public void setPsTotAnn(String psTotAnn) {
		this.psTotAnn = psTotAnn;
	}

	public String getPsRiskLevel() {
		return psRiskLevel;
	}

	public void setPsRiskLevel(String psRiskLevel) {
		this.psRiskLevel = psRiskLevel;
	}

	public String getPsProduct() {
		return psProduct;
	}

	public void setPsProduct(String psProduct) {
		this.psProduct = psProduct;
	}

	public String getPsSystemIncome() {
		return psSystemIncome;
	}

	public void setPsSystemIncome(String psSystemIncome) {
		this.psSystemIncome = psSystemIncome;
	}

	public String getPsPPflag() {
		return psPPflag;
	}

	public void setPsPPflag(String psPPflag) {
		this.psPPflag = psPPflag;
	}

	public String getcCommunicateTime() {
		return cCommunicateTime;
	}

	public void setcCommunicateTime(String cCommunicateTime) {
		this.cCommunicateTime = cCommunicateTime;
	}

	public String getIsCustomizedOffer() {
		return IsCustomizedOffer;
	}

	public void setIsCustomizedOffer(String isCustomizedOffer) {
		IsCustomizedOffer = isCustomizedOffer;
	}

	public String getcApplyProgram() {
		return cApplyProgram;
	}

	public void setcApplyProgram(String cApplyProgram) {
		this.cApplyProgram = cApplyProgram;
	}

	public String getATLT() {
		return ATLT;
	}

	public void setATLT(String aTLT) {
		ATLT = aTLT;
	}

	public String getTERM() {
		return TERM;
	}

	public void setTERM(String tERM) {
		TERM = tERM;
	}

	public String getPsStartUpFee() {
		return psStartUpFee;
	}

	public void setPsStartUpFee(String psStartUpFee) {
		this.psStartUpFee = psStartUpFee;
	}

	public String getOneStepStartUpFee() {
		return OneStepStartUpFee;
	}

	public void setOneStepStartUpFee(String oneStepStartUpFee) {
		OneStepStartUpFee = oneStepStartUpFee;
	}

	public String getTwoStepStartUpFee() {
		return TwoStepStartUpFee;
	}

	public void setTwoStepStartUpFee(String twoStepStartUpFee) {
		TwoStepStartUpFee = twoStepStartUpFee;
	}

	public String getZP84() {
		return ZP84;
	}

	public void setZP84(String zP84) {
		ZP84 = zP84;
	}

	public String getcGrantsAccountBank() {
		return cGrantsAccountBank;
	}

	public void setcGrantsAccountBank(String cGrantsAccountBank) {
		this.cGrantsAccountBank = cGrantsAccountBank;
	}

	public String getcGrantsAccountBranch() {
		return cGrantsAccountBranch;
	}

	public void setcGrantsAccountBranch(String cGrantsAccountBranch) {
		this.cGrantsAccountBranch = cGrantsAccountBranch;
	}

	public String getcGrantsAccount() {
		return cGrantsAccount;
	}

	public void setcGrantsAccount(String cGrantsAccount) {
		this.cGrantsAccount = cGrantsAccount;
	}

	public String getcIsApplyEmailStatement() {
		return cIsApplyEmailStatement;
	}

	public void setcIsApplyEmailStatement(String cIsApplyEmailStatement) {
		this.cIsApplyEmailStatement = cIsApplyEmailStatement;
	}

	public String getcIsApplyShortMessageNotification() {
		return cIsApplyShortMessageNotification;
	}

	public void setcIsApplyShortMessageNotification(String cIsApplyShortMessageNotification) {
		this.cIsApplyShortMessageNotification = cIsApplyShortMessageNotification;
	}

	public String getcIsCompensatory() {
		return cIsCompensatory;
	}

	public void setcIsCompensatory(String cIsCompensatory) {
		this.cIsCompensatory = cIsCompensatory;
	}

	public String getReviewed() {
		return Reviewed;
	}

	public void setReviewed(String reviewed) {
		Reviewed = reviewed;
	}

	public String getReviewDate() {
		return ReviewDate;
	}

	public void setReviewDate(String reviewDate) {
		ReviewDate = reviewDate;
	}

	public String getSupplement() {
		return Supplement;
	}

	public void setSupplement(String supplement) {
		Supplement = supplement;
	}

	public String getNetBankAgreement() {
		return NetBankAgreement;
	}

	public void setNetBankAgreement(String netBankAgreement) {
		NetBankAgreement = netBankAgreement;
	}

	public String getNetPaper() {
		return NetPaper;
	}

	public void setNetPaper(String netPaper) {
		NetPaper = netPaper;
	}

	public String getNoticeWay() {
		return NoticeWay;
	}

	public void setNoticeWay(String noticeWay) {
		NoticeWay = noticeWay;
	}

	public String getOthers() {
		return Others;
	}

	public void setOthers(String others) {
		Others = others;
	}

	public String getcHUBCoSale() {
		return cHUBCoSale;
	}

	public void setcHUBCoSale(String cHUBCoSale) {
		this.cHUBCoSale = cHUBCoSale;
	}

	public String getcIdentifyWay() {
		return cIdentifyWay;
	}

	public void setcIdentifyWay(String cIdentifyWay) {
		this.cIdentifyWay = cIdentifyWay;
	}

	public String getcEMail() {
		return cEMail;
	}

	public void setcEMail(String cEMail) {
		this.cEMail = cEMail;
	}

	public String getcEngName() {
		return cEngName;
	}

	public void setcEngName(String cEngName) {
		this.cEngName = cEngName;
	}

	public String getChangeName() {
		return ChangeName;
	}

	public void setChangeName(String changeName) {
		ChangeName = changeName;
	}

	public String getcChNameEx() {
		return cChNameEx;
	}

	public void setcChNameEx(String cChNameEx) {
		this.cChNameEx = cChNameEx;
	}

	public String getcEngNameEx() {
		return cEngNameEx;
	}

	public void setcEngNameEx(String cEngNameEx) {
		this.cEngNameEx = cEngNameEx;
	}

	public String getcBirthday() {
		return cBirthday;
	}

	public void setcBirthday(String cBirthday) {
		this.cBirthday = cBirthday;
	}

	public String getcIdCardIssueCity() {
		return cIdCardIssueCity;
	}

	public void setcIdCardIssueCity(String cIdCardIssueCity) {
		this.cIdCardIssueCity = cIdCardIssueCity;
	}

	public String getLID1() {
		return LID1;
	}

	public void setLID1(String lID1) {
		LID1 = lID1;
	}

	public String getLID2() {
		return LID2;
	}

	public void setLID2(String lID2) {
		LID2 = lID2;
	}

	public String getLID3() {
		return LID3;
	}

	public void setLID3(String lID3) {
		LID3 = lID3;
	}

	public String getcBirthPlaceCode() {
		return cBirthPlaceCode;
	}

	public void setcBirthPlaceCode(String cBirthPlaceCode) {
		this.cBirthPlaceCode = cBirthPlaceCode;
	}

	public String getcBirthCity() {
		return cBirthCity;
	}

	public void setcBirthCity(String cBirthCity) {
		this.cBirthCity = cBirthCity;
	}

	public String getcIsMultipleNationality() {
		return cIsMultipleNationality;
	}

	public void setcIsMultipleNationality(String cIsMultipleNationality) {
		this.cIsMultipleNationality = cIsMultipleNationality;
	}

	public String getcNationalCode2() {
		return cNationalCode2;
	}

	public void setcNationalCode2(String cNationalCode2) {
		this.cNationalCode2 = cNationalCode2;
	}

	public String getcNationalCode3() {
		return cNationalCode3;
	}

	public void setcNationalCode3(String cNationalCode3) {
		this.cNationalCode3 = cNationalCode3;
	}

	public String getEDU0() {
		return EDU0;
	}

	public void setEDU0(String eDU0) {
		EDU0 = eDU0;
	}

	public String getmHouseOwnerType() {
		return mHouseOwnerType;
	}

	public void setmHouseOwnerType(String mHouseOwnerType) {
		this.mHouseOwnerType = mHouseOwnerType;
	}

	public String getcHouseholdAddr1() {
		return cHouseholdAddr1;
	}

	public void setcHouseholdAddr1(String cHouseholdAddr1) {
		this.cHouseholdAddr1 = cHouseholdAddr1;
	}

	public String getcHouseholdAddr2() {
		return cHouseholdAddr2;
	}

	public void setcHouseholdAddr2(String cHouseholdAddr2) {
		this.cHouseholdAddr2 = cHouseholdAddr2;
	}

	public String getcHouseholdAddr3() {
		return cHouseholdAddr3;
	}

	public void setcHouseholdAddr3(String cHouseholdAddr3) {
		this.cHouseholdAddr3 = cHouseholdAddr3;
	}

	public String getcHouseholdAddr4() {
		return cHouseholdAddr4;
	}

	public void setcHouseholdAddr4(String cHouseholdAddr4) {
		this.cHouseholdAddr4 = cHouseholdAddr4;
	}

	public String getmPrimPresentAddrType() {
		return mPrimPresentAddrType;
	}

	public void setmPrimPresentAddrType(String mPrimPresentAddrType) {
		this.mPrimPresentAddrType = mPrimPresentAddrType;
	}

	public String getPRIM_PRESENT_LIVING_DATE() {
		return PRIM_PRESENT_LIVING_DATE;
	}

	public void setPRIM_PRESENT_LIVING_DATE(String pRIM_PRESENT_LIVING_DATE) {
		PRIM_PRESENT_LIVING_DATE = pRIM_PRESENT_LIVING_DATE;
	}

	public String getcPresentAddr1() {
		return cPresentAddr1;
	}

	public void setcPresentAddr1(String cPresentAddr1) {
		this.cPresentAddr1 = cPresentAddr1;
	}

	public String getcPresentAddr2() {
		return cPresentAddr2;
	}

	public void setcPresentAddr2(String cPresentAddr2) {
		this.cPresentAddr2 = cPresentAddr2;
	}

	public String getcPresentAddr3() {
		return cPresentAddr3;
	}

	public void setcPresentAddr3(String cPresentAddr3) {
		this.cPresentAddr3 = cPresentAddr3;
	}

	public String getcPresentAddr4() {
		return cPresentAddr4;
	}

	public void setcPresentAddr4(String cPresentAddr4) {
		this.cPresentAddr4 = cPresentAddr4;
	}

	public String getcMailingAddrType() {
		return cMailingAddrType;
	}

	public void setcMailingAddrType(String cMailingAddrType) {
		this.cMailingAddrType = cMailingAddrType;
	}

	public String getcMailingAddr1() {
		return cMailingAddr1;
	}

	public void setcMailingAddr1(String cMailingAddr1) {
		this.cMailingAddr1 = cMailingAddr1;
	}

	public String getcMailingAddr2() {
		return cMailingAddr2;
	}

	public void setcMailingAddr2(String cMailingAddr2) {
		this.cMailingAddr2 = cMailingAddr2;
	}

	public String getcMailingAddr3() {
		return cMailingAddr3;
	}

	public void setcMailingAddr3(String cMailingAddr3) {
		this.cMailingAddr3 = cMailingAddr3;
	}

	public String getcMailingAddr4() {
		return cMailingAddr4;
	}

	public void setcMailingAddr4(String cMailingAddr4) {
		this.cMailingAddr4 = cMailingAddr4;
	}

	public String getTN20A() {
		return TN20A;
	}

	public void setTN20A(String tN20A) {
		TN20A = tN20A;
	}

	public String getTN20B() {
		return TN20B;
	}

	public void setTN20B(String tN20B) {
		TN20B = tN20B;
	}

	public String getIsReferee() {
		return IsReferee;
	}

	public void setIsReferee(String isReferee) {
		IsReferee = isReferee;
	}

	public String getcNameOfReferee() {
		return cNameOfReferee;
	}

	public void setcNameOfReferee(String cNameOfReferee) {
		this.cNameOfReferee = cNameOfReferee;
	}

	public String getcReferrerIdNumber() {
		return cReferrerIdNumber;
	}

	public void setcReferrerIdNumber(String cReferrerIdNumber) {
		this.cReferrerIdNumber = cReferrerIdNumber;
	}

	public String getPHA1A() {
		return PHA1A;
	}

	public void setPHA1A(String pHA1A) {
		PHA1A = pHA1A;
	}

	public String getPHA1B() {
		return PHA1B;
	}

	public void setPHA1B(String pHA1B) {
		PHA1B = pHA1B;
	}

	public String getZH17() {
		return ZH17;
	}

	public void setZH17(String zH17) {
		ZH17 = zH17;
	}

	public String getZA85() {
		return ZA85;
	}

	public void setZA85(String zA85) {
		ZA85 = zA85;
	}

	public String getcPrimAnnualSalary() {
		return cPrimAnnualSalary;
	}

	public void setcPrimAnnualSalary(String cPrimAnnualSalary) {
		this.cPrimAnnualSalary = cPrimAnnualSalary;
	}

	public String getTNI1() {
		return TNI1;
	}

	public void setTNI1(String tNI1) {
		TNI1 = tNI1;
	}

	public String getcCompanyAddr1() {
		return cCompanyAddr1;
	}

	public void setcCompanyAddr1(String cCompanyAddr1) {
		this.cCompanyAddr1 = cCompanyAddr1;
	}

	public String getcCompanyAddr2() {
		return cCompanyAddr2;
	}

	public void setcCompanyAddr2(String cCompanyAddr2) {
		this.cCompanyAddr2 = cCompanyAddr2;
	}

	public String getcCompanyAddr3() {
		return cCompanyAddr3;
	}

	public void setcCompanyAddr3(String cCompanyAddr3) {
		this.cCompanyAddr3 = cCompanyAddr3;
	}

	public String getcCompanyAddr4() {
		return cCompanyAddr4;
	}

	public void setcCompanyAddr4(String cCompanyAddr4) {
		this.cCompanyAddr4 = cCompanyAddr4;
	}

	public String getTNO0A() {
		return TNO0A;
	}

	public void setTNO0A(String tNO0A) {
		TNO0A = tNO0A;
	}

	public String getTNO0B() {
		return TNO0B;
	}

	public void setTNO0B(String tNO0B) {
		TNO0B = tNO0B;
	}

	public String getTNO0C() {
		return TNO0C;
	}

	public void setTNO0C(String tNO0C) {
		TNO0C = tNO0C;
	}

	public String getTJB0() {
		return TJB0;
	}

	public void setTJB0(String tJB0) {
		TJB0 = tJB0;
	}

	public String getcCompanyNameEx() {
		return cCompanyNameEx;
	}

	public void setcCompanyNameEx(String cCompanyNameEx) {
		this.cCompanyNameEx = cCompanyNameEx;
	}

	public String getcJobTitleEx() {
		return cJobTitleEx;
	}

	public void setcJobTitleEx(String cJobTitleEx) {
		this.cJobTitleEx = cJobTitleEx;
	}

	public String getRTJ0() {
		return RTJ0;
	}

	public void setRTJ0(String rTJ0) {
		RTJ0 = rTJ0;
	}

	public String getcIsCompanyPrincipal() {
		return cIsCompanyPrincipal;
	}

	public void setcIsCompanyPrincipal(String cIsCompanyPrincipal) {
		this.cIsCompanyPrincipal = cIsCompanyPrincipal;
	}

	public String getcCompanyName() {
		return cCompanyName;
	}

	public void setcCompanyName(String cCompanyName) {
		this.cCompanyName = cCompanyName;
	}

	public String getcCompanyId() {
		return cCompanyId;
	}

	public void setcCompanyId(String cCompanyId) {
		this.cCompanyId = cCompanyId;
	}

	public String getcJobTitle() {
		return cJobTitle;
	}

	public void setcJobTitle(String cJobTitle) {
		this.cJobTitle = cJobTitle;
	}

	public String getcRelation() {
		return cRelation;
	}

	public void setcRelation(String cRelation) {
		this.cRelation = cRelation;
	}

	public String getcRelationName() {
		return cRelationName;
	}

	public void setcRelationName(String cRelationName) {
		this.cRelationName = cRelationName;
	}

	public String getcRelationId() {
		return cRelationId;
	}

	public void setcRelationId(String cRelationId) {
		this.cRelationId = cRelationId;
	}

	public String getcRelationForeignId() {
		return cRelationForeignId;
	}

	public void setcRelationForeignId(String cRelationForeignId) {
		this.cRelationForeignId = cRelationForeignId;
	}

	public String getcIsRelationCompanyPrincipal() {
		return cIsRelationCompanyPrincipal;
	}

	public void setcIsRelationCompanyPrincipal(String cIsRelationCompanyPrincipal) {
		this.cIsRelationCompanyPrincipal = cIsRelationCompanyPrincipal;
	}

	public String getcRelationCompanyName() {
		return cRelationCompanyName;
	}

	public void setcRelationCompanyName(String cRelationCompanyName) {
		this.cRelationCompanyName = cRelationCompanyName;
	}

	public String getcRelationCompanyId() {
		return cRelationCompanyId;
	}

	public void setcRelationCompanyId(String cRelationCompanyId) {
		this.cRelationCompanyId = cRelationCompanyId;
	}

	public String getcCompanyJobTitleOfRelatedPerson() {
		return cCompanyJobTitleOfRelatedPerson;
	}

	public void setcCompanyJobTitleOfRelatedPerson(String cCompanyJobTitleOfRelatedPerson) {
		this.cCompanyJobTitleOfRelatedPerson = cCompanyJobTitleOfRelatedPerson;
	}

	public String getCardStatusDesc() {
		return CardStatusDesc;
	}

	public void setCardStatusDesc(String cardStatusDesc) {
		CardStatusDesc = cardStatusDesc;
	}

	public String getCompanyStatusDesc() {
		return CompanyStatusDesc;
	}

	public void setCompanyStatusDesc(String companyStatusDesc) {
		CompanyStatusDesc = companyStatusDesc;
	}

	public String getOtpSerialNo() {
		return otpSerialNo;
	}

	public void setOtpSerialNo(String otpSerialNo) {
		this.otpSerialNo = otpSerialNo;
	}

	public String getOtpSuccessDateTime() {
		return otpSuccessDateTime;
	}

	public void setOtpSuccessDateTime(String otpSuccessDateTime) {
		this.otpSuccessDateTime = otpSuccessDateTime;
	}

	public String getcMarriage() {
		return cMarriage;
	}

	public void setcMarriage(String cMarriage) {
		this.cMarriage = cMarriage;
	}

	public String getcSex() {
		return cSex;
	}

	public void setcSex(String cSex) {
		this.cSex = cSex;
	}

	public String getMktCode() {
		return mktCode;
	}

	public void setMktCode(String mktCode) {
		this.mktCode = mktCode;
	}

	public String getUserDevice() {
		return userDevice;
	}

	public void setUserDevice(String userDevice) {
		this.userDevice = userDevice;
	}

	public String getMobileVPlusResult() {
		return mobileVPlusResult;
	}

	public void setMobileVPlusResult(String mobileVPlusResult) {
		this.mobileVPlusResult = mobileVPlusResult;
	}

	public String getMobileLastChangeDate() {
		return mobileLastChangeDate;
	}

	public void setMobileLastChangeDate(String mobileLastChangeDate) {
		this.mobileLastChangeDate = mobileLastChangeDate;
	}

	public Date getOTPSendDXCDateTime() {
		return OTPSendDXCDateTime;
	}

	public void setOTPSendDXCDateTime(Date oTPSendDXCDateTime) {
		OTPSendDXCDateTime = oTPSendDXCDateTime;
	}

	public String getIsJoinReferee() {
		return IsJoinReferee;
	}

	public void setIsJoinReferee(String isJoinReferee) {
		IsJoinReferee = isJoinReferee;
	}

	public String getOcrChName() {
		return ocrChName;
	}

	public void setOcrChName(String ocrChName) {
		this.ocrChName = ocrChName;
	}

	public String getcCusMobileNum() {
		return cCusMobileNum;
	}

	public void setcCusMobileNum(String cCusMobileNum) {
		this.cCusMobileNum = cCusMobileNum;
	}

	public Date getNEXT_COMM_TIME() {
		return NEXT_COMM_TIME;
	}

	public void setNEXT_COMM_TIME(Date nEXT_COMM_TIME) {
		NEXT_COMM_TIME = nEXT_COMM_TIME;
	}

	public String getIsPreSelect() {
		return isPreSelect;
	}

	public void setIsPreSelect(String isPreSelect) {
		this.isPreSelect = isPreSelect;
	}

	public String getZL02_CUS() {
		return ZL02_CUS;
	}

	public void setZL02_CUS(String zL02_CUS) {
		ZL02_CUS = zL02_CUS;
	}

	public String getAAPS_RESULT() {
		return AAPS_RESULT;
	}

	public void setAAPS_RESULT(String aAPS_RESULT) {
		AAPS_RESULT = aAPS_RESULT;
	}

	public String getAAPS_RESULT_DATE() {
		return AAPS_RESULT_DATE;
	}

	public void setAAPS_RESULT_DATE(String aAPS_RESULT_DATE) {
		AAPS_RESULT_DATE = aAPS_RESULT_DATE;
	}

	public String getPEPSanction() {
		return PEPSanction;
	}

	public void setPEPSanction(String pEPSanction) {
		PEPSanction = pEPSanction;
	}

	public String getcTaxQuantity() {
		return cTaxQuantity;
	}

	public void setcTaxQuantity(String cTaxQuantity) {
		this.cTaxQuantity = cTaxQuantity;
	}

	public String getcTaxCountry1() {
		return cTaxCountry1;
	}

	public void setcTaxCountry1(String cTaxCountry1) {
		this.cTaxCountry1 = cTaxCountry1;
	}

	public String getcTaxID1() {
		return cTaxID1;
	}

	public void setcTaxID1(String cTaxID1) {
		this.cTaxID1 = cTaxID1;
	}

	public String getcTaxReason1() {
		return cTaxReason1;
	}

	public void setcTaxReason1(String cTaxReason1) {
		this.cTaxReason1 = cTaxReason1;
	}

	public String getcTaxReasonText1() {
		return cTaxReasonText1;
	}

	public void setcTaxReasonText1(String cTaxReasonText1) {
		this.cTaxReasonText1 = cTaxReasonText1;
	}

	public String getcTaxCountry2() {
		return cTaxCountry2;
	}

	public void setcTaxCountry2(String cTaxCountry2) {
		this.cTaxCountry2 = cTaxCountry2;
	}

	public String getcTaxID2() {
		return cTaxID2;
	}

	public void setcTaxID2(String cTaxID2) {
		this.cTaxID2 = cTaxID2;
	}

	public String getcTaxReason2() {
		return cTaxReason2;
	}

	public void setcTaxReason2(String cTaxReason2) {
		this.cTaxReason2 = cTaxReason2;
	}

	public String getcTaxReasonText2() {
		return cTaxReasonText2;
	}

	public void setcTaxReasonText2(String cTaxReasonText2) {
		this.cTaxReasonText2 = cTaxReasonText2;
	}

	public String getZCFG() {
		return ZCFG;
	}

	public void setZCFG(String zCFG) {
		ZCFG = zCFG;
	}

}
