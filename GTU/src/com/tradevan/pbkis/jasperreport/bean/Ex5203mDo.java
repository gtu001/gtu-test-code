package com.tradevan.pbkis.jasperreport.bean;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;

public class Ex5203mDo implements Serializable{
    private static final long serialVersionUID = 1L;
    private String field1="";//測試用欄位
	
	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}
	
	transient JRDataSource detailDataSource;
	transient JRDataSource otherDeclItemDataSource;

	public JRDataSource getDetailDataSource() {
		return detailDataSource;
	}

	public void setDetailDataSource(JRDataSource detailDataSource) {
		this.detailDataSource = detailDataSource;
	}
	
	public JRDataSource getOtherDeclItemDataSource() {
		return otherDeclItemDataSource;
	}

	public void setOtherDeclItemDataSource(JRDataSource otherDeclItemDataSource) {
		this.otherDeclItemDataSource = otherDeclItemDataSource;
	}

	private String transactionId;//交易識別碼
	private String controlNo;//交換控制碼
	private String custName;//報關行名稱
	private String asType; //海空運別
	private String entryType; //輸入方式
	private String unbDate; //傳送日期
	private String unbTime; //傳送時間
	private String unbCtrlRefNo; //交換控制碼
	private String sendId; //送件人代碼
	private String sendQf; //送件人辨識碼
	private String recvId; //收件人代碼
	private String recvQf; //收件人辨識碼
	private String recvRefPasswd; //收件人參考碼
	private String unhMsgRefNo; //訊息參考號碼
	private String messageFunction; //異動別
	private String boxNo; //報關行箱號
	private String boxSubNo; //報關行箱號附碼
	private String brokerNo; //專責報關人員代號
	private String mawbNo; //主提單號碼
	private String hawbNo; //分提單號碼
	private String transportMode; //運輸方式
	private String storagePlaceCode; //貨櫃(物)存放處所
	private String dutyRefundReqMark;//申請沖退原料稅
	private String declType; //報單類別
	private String declNo; //報單號碼
	private String exporterBan;//貨物輸出人統編
	private String exporterNameE;//貨物輸出人名稱(英文)
	private String exporterNameC;//貨物輸出人名稱(中文)
	private String exporterAddressE;//貨物輸出人住址(英文)
	private String exporterAddressC;//貨物輸出人住址(中文)
	private String exporterBfNo;//貨物輸出人海關監管編號
	private String paymentMethod; //繳納方式
	private String eftBan; //線上扣繳開戶人統一編號
	private String eftAccountNo; //線上扣繳帳號
	private String buyerCountryCode; //買方國家代碼
	private String buyerBan; //買方統一編號或國外廠商英文名稱縮寫
	private String buyerBfNo; //買方海關監管編號
	private String buyerNameE; //買方名稱(英文);
	private String buyerNameC; //買方名稱(中文);
	private String buyerAddressE; //買方住址(英文)
	private String buyerAddressC; //買方住址(中文)
	private String declDate; //報關日期
	private String exportPort;//輸出口岸
	private String destinationCode;//目的地(卸存地)代碼
	private String tradeTerm; //貿易條件
	private String invoiceAmt; //發票金額
	private String currencyCode; //幣別
	private String exchangeRate; //外幣匯率
	private String freightFee; //運費
	private String insuranceFee; //保險費
	private String additionFee; //應加費用
	private String deductionFee; //應減費用
	private String fobValueTwd; //離岸價格(新台幣)
	private String fobValue; //起岸價格
	private String flightNo; //船機班次(呼號)
	private String vesselName; //航次
	private String packageNumber; //總件數
	private String packageUnit; //件數單位
	private String coPackageNote; //合成註記
	private String packageDesc; //包裝說明
	private String totalGrossWeight; //總毛重
	private String totalNetWeight; //總淨重
	private String licensingNote;
	private String dutyChargeCode1; //稅費代號1
	private String dutyAmt1; //稅費金額1(貨物稅)
	private String dutyChargeCode2; //稅費代號2
	private String dutyAmt2; //稅費金額2 (推貿費)
	private String dutyChargeCode3; //稅費代號3
	private String dutyAmt3; //稅費金額3 (商建費)
	private String dutyChargeCode4; //稅費代號4
	private String dutyAmt4; //稅費金額4 (營業稅)
	private String dutyChargeCode5; //稅費代號5
	private String dutyAmt5; //稅費金額5 (滯報費)
	private String dutyChargeCode6; //稅費代號6
	private String dutyAmt6; //稅費金額6, (菸酒稅)
	private String dutyChargeCode7; //稅費代號7
	private String dutyAmt7; //稅費金額7 (健康捐)
	private String dutyChargeCode8; //稅費代號8
	private String dutyAmt8; //稅費金額8 (額外關稅)
	private String dutyChargeCode9; //稅費代號9
	private String dutyAmt9; //稅費金額9, (業務費)
	private String totalDuty; //稅費合計
	private String marks; //標記
	private String remark1; //其他申報事項1
	private String remark2; //其他申報事項2
	private String declExamType; //申請查驗方式
	private String attachDocuNo1;
	private String attachDocuNo2;
	private String attachDocuNo3;
	private String origDeclNo1;//原進口報單號碼1
	private String origDeclNo2;//原進口報單號碼2
	private String origDeclNo3;//原進口報單號碼3
	private String origDeclNo4;//原進口報單號碼4
	private String origDeclNo5;//原進口報單號碼5
	private String origDeclNo6;//原進口報單號碼6
	private String origDeclNo7;//原進口報單號碼7
	private String origDeclNo8;//原進口報單號碼8
	private String origDeclNo9;//原進口報單號碼9
	private String relatedBfNo1;//製造之保稅工廠海關監管編號1
	private String relatedBfNo2;//製造之保稅工廠海關監管編號2
	private String relatedBfNo3;//製造之保稅工廠海關監管編號3
	private String relatedBfNo4;//製造之保稅工廠海關監管編號4
	private String relatedBfNo5;//製造之保稅工廠海關監管編號5
	private String relatedBfNo6;//製造之保稅工廠海關監管編號6
	private String relatedBfNo7;//製造之保稅工廠海關監管編號7
	private String relatedBfNo8;//製造之保稅工廠海關監管編號8
	private String relatedBfNo9;//製造之保稅工廠海關監管編號9
	private String relatedBfBan1;//製造之保稅工廠統編1
	private String relatedBfBan2;//製造之保稅工廠統編2
	private String relatedBfBan3;//製造之保稅工廠統編3
	private String relatedBfBan4;//製造之保稅工廠統編4
	private String relatedBfBan5;//製造之保稅工廠統編5
	private String relatedBfBan6;//製造之保稅工廠統編6
	private String relatedBfBan7;//製造之保稅工廠統編7
	private String relatedBfBan8;//製造之保稅工廠統編8
	private String relatedBfBan9;//製造之保稅工廠統編9
	private String dupDeclReqMark; //申請報單副本
	private String usedMaterialTable;
	
	/*
	Modify By Miller Huang 050607
 	出口報單relatedBfBan 表示為製造之保稅工廠統一編號
	RelatedBfNo  表示為製造之保稅工廠海關監管編號
	*/
	
	private String relatedBfBan; //製造之保稅工廠統一編號
	private String relatedBfNo; //製造之保稅工廠海關監管編號
	private String inWarehouseBan; //進倉保稅倉庫業者統一編號
	private String inWarehouseBfNo; //進倉保稅倉庫海關監管編號
	private String outWarehouseBan; //出倉保稅倉庫業者統一編號
	private String outWarehouseBfNo; //出倉保稅倉庫海關監管編號
	private String bondMonth; //保稅貨物按月彙報月份
	private String bondTradingRefNo; //交易對方參考編號
	private String note; //備註
	private String processDate; //處理日期
	private String processTime; //處理時間
	private String processStat; //處理狀態
	private String inspectType; //通關方式
	private String declRemark1; //其他申報事項1
	private String declRemark2; //其他申報事項2
	private String internalControlNo; //內控編碼

	//其他相關欄位
	private String custCname; //報關行中文名稱
	private String brokerName; //專責報關人員姓名
	private String storagePlaceName; //貨物存放處所中文名稱
	private String declTypeDesc; //報單類別說明
	
	/*private String exporterCname; //納稅義務人名稱(中文)
	private String exporterCaddress; //納稅義務人住址(中文)*/
	
	private String exportPortName; //取得起運口岸全名
	private String destinationName; //取得目的地國家全名
	private String buyerCountryName; //取得買方國家全名
	private String msg5107Date; //存證證明-5107 通關方式 (表單位置:通關方式)
	private String msg511xDate; //存證證明-5110/5111 Date (表單位置:核發稅單)
	private String msg5204Date; //存證證明-5116 Date (表單位置:放行)
	private String msgTp003Date; //存證證明-5116 Date (表單位置:放行)
	private String controlData; //存證證明-列印者身分及列印日期
	private String shippingMarks;//標記
	
	/** 新報表中新增加的欄位 **/
	private String vesselId;//船機註冊號碼
	private String fobCurrentCode;//離案幣別
	private String feePaymentMethod;//繳納方式(25)
	private String totalPackageNumber;//總件數
	
	/**新EX_5203M_N Table中新增加的欄位**/
	private String bfIdType1;//保稅工廠身分識別代碼1
	private String bfIdType2;//保稅工廠身分識別代碼2
	private String bfIdType3;//保稅工廠身分識別代碼3
	private String bfIdType4;//保稅工廠身分識別代碼4
	private String bfIdType5;//保稅工廠身分識別代碼5
	private String bfIdType6;//保稅工廠身分識別代碼6
	private String bfIdType7;//保稅工廠身分識別代碼7
	private String bfIdType8;//保稅工廠身分識別代碼8
	private String bfIdType9;//保稅工廠身分識別代碼9
	private String bfIdType10;//保稅工廠身分識別代碼10
	private String inBondWarehouseIdType;//進倉保稅倉庫業者身分識別代碼
	private String loadPortCode;//裝貨港代碼
	private String orgGroup;//貨主分類
	private String orgId;//貨主代碼
	private String outBondWarehouseIdType;//出倉保稅倉庫業身分識別代碼
	private String poaId;//營業稅記帳廠商編號
	private String recvDate;//接收時間
	private String reserveField1;//備用欄位1
	private String reserveField2;//備用欄位2
	private String reserveField3;//備用欄位3
	private String reserveField4;//備用欄位4
	private String reserveField5;//備用欄位5
	private String reserveField6;//備用欄位6
	private String reserveField7;//備用欄位7
	private String reserveField8;//備用欄位8
	private String reserveField9;//備用欄位9
	private String reserveField10;//備用欄位10
	private String retryCnt;//重新處理次數
	private String rsltCode;//處理結果代碼
	private String rsltMsg;//處理結果說明
	private String storagePlaceCode2;//卸存地點代碼2
	private String totFobValueTwd;//總離岸價格(新台幣)
	private String totInvoiceAmt;//發票總金額
	private String vanId;//網路別
	private String voyageFlightNo;//船舶航次(海)/航機班次(空)
	private String brokerAeoNo; //AEO編號

	/**
	 * 標記/貨櫃號碼(45)/其它申報事項(46)
	 */
	List<OtherDeclItemDo> otherDeclItems = new LinkedList<OtherDeclItemDo>();
	
	/**
	 * 出口貨物明細
	 */
	List<Ex5203dDo> list = new LinkedList<Ex5203dDo>();
	
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getControlNo() {
		return controlNo;
	}

	public void setControlNo(String controlNo) {
		this.controlNo = controlNo;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getAsType() {
		return asType;
	}

	public void setAsType(String asType) {
		this.asType = asType;
	}

	public String getEntryType() {
		return entryType;
	}

	public void setEntryType(String entryType) {
		this.entryType = entryType;
	}

	public String getUnbDate() {
		return unbDate;
	}

	public void setUnbDate(String unbDate) {
		this.unbDate = unbDate;
	}

	public String getUnbTime() {
		return unbTime;
	}

	public void setUnbTime(String unbTime) {
		this.unbTime = unbTime;
	}

	public String getUnbCtrlRefNo() {
		return unbCtrlRefNo;
	}

	public void setUnbCtrlRefNo(String unbCtrlRefNo) {
		this.unbCtrlRefNo = unbCtrlRefNo;
	}

	public String getSendId() {
		return sendId;
	}

	public void setSendId(String sendId) {
		this.sendId = sendId;
	}

	public String getSendQf() {
		return sendQf;
	}

	public void setSendQf(String sendQf) {
		this.sendQf = sendQf;
	}

	public String getRecvId() {
		return recvId;
	}

	public void setRecvId(String recvId) {
		this.recvId = recvId;
	}

	public String getRecvQf() {
		return recvQf;
	}

	public void setRecvQf(String recvQf) {
		this.recvQf = recvQf;
	}

	public String getRecvRefPasswd() {
		return recvRefPasswd;
	}

	public void setRecvRefPasswd(String recvRefPasswd) {
		this.recvRefPasswd = recvRefPasswd;
	}

	public String getUnhMsgRefNo() {
		return unhMsgRefNo;
	}

	public void setUnhMsgRefNo(String unhMsgRefNo) {
		this.unhMsgRefNo = unhMsgRefNo;
	}

	public String getMessageFunction() {
		return messageFunction;
	}

	public void setMessageFunction(String messageFunction) {
		this.messageFunction = messageFunction;
	}

	public String getBoxNo() {
		return boxNo;
	}

	public void setBoxNo(String boxNo) {
		this.boxNo = boxNo;
	}

	public String getBoxSubNo() {
		return boxSubNo;
	}

	public void setBoxSubNo(String boxSubNo) {
		this.boxSubNo = boxSubNo;
	}

	public String getBrokerNo() {
		return brokerNo;
	}

	public void setBrokerNo(String brokerNo) {
		this.brokerNo = brokerNo;
	}

	public String getMawbNo() {
		return mawbNo;
	}

	public void setMawbNo(String mawbNo) {
		this.mawbNo = mawbNo;
	}

	public String getHawbNo() {
		return hawbNo;
	}

	public void setHawbNo(String hawbNo) {
		this.hawbNo = hawbNo;
	}

	public String getTransportMode() {
		return transportMode;
	}

	public void setTransportMode(String transportMode) {
		this.transportMode = transportMode;
	}

	public String getStoragePlaceCode() {
		return storagePlaceCode;
	}

	public void setStoragePlaceCode(String storagePlaceCode) {
		this.storagePlaceCode = storagePlaceCode;
	}

	public String getDutyRefundReqMark() {
		return dutyRefundReqMark;
	}

	public void setDutyRefundReqMark(String dutyRefundReqMark) {
		this.dutyRefundReqMark = dutyRefundReqMark;
	}

	public String getDeclType() {
		return declType;
	}

	public void setDeclType(String declType) {
		this.declType = declType;
	}

	public String getDeclNo() {
		return declNo;
	}

	public void setDeclNo(String declNo) {
		this.declNo = declNo;
	}

	public String getExporterBan() {
		return exporterBan;
	}

	public void setExporterBan(String exporterBan) {
		this.exporterBan = exporterBan;
	}

	public String getExporterNameE() {
		return exporterNameE;
	}

	public void setExporterNameE(String exporterNameE) {
		this.exporterNameE = exporterNameE;
	}

	public String getExporterNameC() {
		return exporterNameC;
	}

	public void setExporterNameC(String exporterNameC) {
		this.exporterNameC = exporterNameC;
	}

	public String getExporterAddressE() {
		return exporterAddressE;
	}

	public void setExporterAddressE(String exporterAddressE) {
		this.exporterAddressE = exporterAddressE;
	}

	public String getExporterAddressC() {
		return exporterAddressC;
	}

	public void setExporterAddressC(String exporterAddressC) {
		this.exporterAddressC = exporterAddressC;
	}

	public String getExporterBfNo() {
		return exporterBfNo;
	}

	public void setExporterBfNo(String exporterBfNo) {
		this.exporterBfNo = exporterBfNo;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getEftBan() {
		return eftBan;
	}

	public void setEftBan(String eftBan) {
		this.eftBan = eftBan;
	}

	public String getEftAccountNo() {
		return eftAccountNo;
	}

	public void setEftAccountNo(String eftAccountNo) {
		this.eftAccountNo = eftAccountNo;
	}

	public String getBuyerCountryCode() {
		return buyerCountryCode;
	}

	public void setBuyerCountryCode(String buyerCountryCode) {
		this.buyerCountryCode = buyerCountryCode;
	}

	public String getBuyerBan() {
		return buyerBan;
	}

	public void setBuyerBan(String buyerBan) {
		this.buyerBan = buyerBan;
	}

	public String getBuyerBfNo() {
		return buyerBfNo;
	}

	public void setBuyerBfNo(String buyerBfNo) {
		this.buyerBfNo = buyerBfNo;
	}

	public String getBuyerNameE() {
		return buyerNameE;
	}

	public void setBuyerNameE(String buyerNameE) {
		this.buyerNameE = buyerNameE;
	}

	public String getBuyerNameC() {
		return buyerNameC;
	}

	public void setBuyerNameC(String buyerNameC) {
		this.buyerNameC = buyerNameC;
	}

	public String getBuyerAddressE() {
		return buyerAddressE;
	}

	public void setBuyerAddressE(String buyerAddressE) {
		this.buyerAddressE = buyerAddressE;
	}

	public String getBuyerAddressC() {
		return buyerAddressC;
	}

	public void setBuyerAddressC(String buyerAddressC) {
		this.buyerAddressC = buyerAddressC;
	}

	public String getDeclDate() {
		return declDate;
	}

	public void setDeclDate(String declDate) {
		this.declDate = declDate;
	}

	public String getExportPort() {
		return exportPort;
	}

	public void setExportPort(String exportPort) {
		this.exportPort = exportPort;
	}

	public String getDestinationCode() {
		return destinationCode;
	}

	public void setDestinationCode(String destinationCode) {
		this.destinationCode = destinationCode;
	}

	public String getTradeTerm() {
		return tradeTerm;
	}

	public void setTradeTerm(String tradeTerm) {
		this.tradeTerm = tradeTerm;
	}

	public String getInvoiceAmt() {
		return invoiceAmt;
	}

	public void setInvoiceAmt(String invoiceAmt) {
		this.invoiceAmt = invoiceAmt;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getFreightFee() {
		return freightFee;
	}

	public void setFreightFee(String freightFee) {
		this.freightFee = freightFee;
	}

	public String getInsuranceFee() {
		return insuranceFee;
	}

	public void setInsuranceFee(String insuranceFee) {
		this.insuranceFee = insuranceFee;
	}

	public String getAdditionFee() {
		return additionFee;
	}

	public void setAdditionFee(String additionFee) {
		this.additionFee = additionFee;
	}

	public String getDeductionFee() {
		return deductionFee;
	}

	public void setDeductionFee(String deductionFee) {
		this.deductionFee = deductionFee;
	}

	public String getFobValueTwd() {
		return fobValueTwd;
	}

	public void setFobValueTwd(String fobValueTwd) {
		this.fobValueTwd = fobValueTwd;
	}

	public String getFobValue() {
		return fobValue;
	}

	public void setFobValue(String fobValue) {
		this.fobValue = fobValue;
	}

	public String getFlightNo() {
		return flightNo;
	}

	public void setFlightNo(String flightNo) {
		this.flightNo = flightNo;
	}

	public String getVesselId() {
		return vesselId;
	}

	public void setVesselId(String vesselId) {
		this.vesselId = vesselId;
	}

	public String getVesselName() {
		return vesselName;
	}

	public void setVesselName(String vesselName) {
		this.vesselName = vesselName;
	}

	public String getPackageNumber() {
		return packageNumber;
	}

	public void setPackageNumber(String packageNumber) {
		this.packageNumber = packageNumber;
	}

	public String getPackageUnit() {
		return packageUnit;
	}

	public void setPackageUnit(String packageUnit) {
		this.packageUnit = packageUnit;
	}

	public String getCoPackageNote() {
		return coPackageNote;
	}

	public void setCoPackageNote(String coPackageNote) {
		this.coPackageNote = coPackageNote;
	}

	public String getPackageDesc() {
		return packageDesc;
	}

	public void setPackageDesc(String packageDesc) {
		this.packageDesc = packageDesc;
	}

	public String getTotalGrossWeight() {
		return totalGrossWeight;
	}

	public void setTotalGrossWeight(String totalGrossWeight) {
		this.totalGrossWeight = totalGrossWeight;
	}

	public String getTotalNetWeight() {
		return totalNetWeight;
	}

	public void setTotalNetWeight(String totalNetWeight) {
		this.totalNetWeight = totalNetWeight;
	}

	public String getLicensingNote() {
		return licensingNote;
	}

	public void setLicensingNote(String licensingNote) {
		this.licensingNote = licensingNote;
	}

	public String getDutyChargeCode1() {
		return dutyChargeCode1;
	}

	public void setDutyChargeCode1(String dutyChargeCode1) {
		this.dutyChargeCode1 = dutyChargeCode1;
	}

	public String getDutyAmt1() {
		return dutyAmt1;
	}

	public void setDutyAmt1(String dutyAmount1) {
		this.dutyAmt1 = dutyAmount1;
	}

	public String getDutyChargeCode2() {
		return dutyChargeCode2;
	}

	public void setDutyChargeCode2(String dutyChargeCode2) {
		this.dutyChargeCode2 = dutyChargeCode2;
	}

	public String getDutyAmt2() {
		return dutyAmt2;
	}

	public void setDutyAmt2(String dutyAmount2) {
		this.dutyAmt2 = dutyAmount2;
	}

	public String getDutyChargeCode3() {
		return dutyChargeCode3;
	}

	public void setDutyChargeCode3(String dutyChargeCode3) {
		this.dutyChargeCode3 = dutyChargeCode3;
	}

	public String getDutyAmt3() {
		return dutyAmt3;
	}

	public void setDutyAmt3(String dutyAmount3) {
		this.dutyAmt3 = dutyAmount3;
	}

	public String getDutyChargeCode4() {
		return dutyChargeCode4;
	}

	public void setDutyChargeCode4(String dutyChargeCode4) {
		this.dutyChargeCode4 = dutyChargeCode4;
	}

	public String getDutyAmt4() {
		return dutyAmt4;
	}

	public void setDutyAmt4(String dutyAmount4) {
		this.dutyAmt4 = dutyAmount4;
	}

	public String getDutyChargeCode5() {
		return dutyChargeCode5;
	}

	public void setDutyChargeCode5(String dutyChargeCode5) {
		this.dutyChargeCode5 = dutyChargeCode5;
	}

	public String getDutyAmt5() {
		return dutyAmt5;
	}

	public void setDutyAmt5(String dutyAmount5) {
		this.dutyAmt5 = dutyAmount5;
	}

	public String getDutyChargeCode6() {
		return dutyChargeCode6;
	}

	public void setDutyChargeCode6(String dutyChargeCode6) {
		this.dutyChargeCode6 = dutyChargeCode6;
	}

	public String getDutyAmt6() {
		return dutyAmt6;
	}

	public void setDutyAmt6(String dutyAmount6) {
		this.dutyAmt6 = dutyAmount6;
	}

	public String getDutyChargeCode7() {
		return dutyChargeCode7;
	}

	public void setDutyChargeCode7(String dutyChargeCode7) {
		this.dutyChargeCode7 = dutyChargeCode7;
	}

	public String getDutyAmt7() {
		return dutyAmt7;
	}

	public void setDutyAmt7(String dutyAmount7) {
		this.dutyAmt7 = dutyAmount7;
	}

	public String getDutyChargeCode8() {
		return dutyChargeCode8;
	}

	public void setDutyChargeCode8(String dutyChargeCode8) {
		this.dutyChargeCode8 = dutyChargeCode8;
	}

	public String getDutyAmt8() {
		return dutyAmt8;
	}

	public void setDutyAmt8(String dutyAmount8) {
		this.dutyAmt8 = dutyAmount8;
	}

	public String getDutyChargeCode9() {
		return dutyChargeCode9;
	}

	public void setDutyChargeCode9(String dutyChargeCode9) {
		this.dutyChargeCode9 = dutyChargeCode9;
	}

	public String getDutyAmt9() {
		return dutyAmt9;
	}

	public void setDutyAmt9(String dutyAmount9) {
		this.dutyAmt9 = dutyAmount9;
	}

	public String getTotalDuty() {
		return totalDuty;
	}

	public void setTotalDuty(String totalDuty) {
		this.totalDuty = totalDuty;
	}

	public String getMarks() {
		return marks;
	}

	public void setMarks(String marks) {
		this.marks = marks;
	}

	public String getRemark1() {
		return remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	public String getRemark2() {
		return remark2;
	}

	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

	public String getDeclExamType() {
		return declExamType;
	}

	public void setDeclExamType(String declExamType) {
		this.declExamType = declExamType;
	}

	public String getAttachDocuNo1() {
		return attachDocuNo1;
	}

	public void setAttachDocuNo1(String attachDocuNo1) {
		this.attachDocuNo1 = attachDocuNo1;
	}

	public String getAttachDocuNo2() {
		return attachDocuNo2;
	}

	public void setAttachDocuNo2(String attachDocuNo2) {
		this.attachDocuNo2 = attachDocuNo2;
	}

	public String getAttachDocuNo3() {
		return attachDocuNo3;
	}

	public void setAttachDocuNo3(String attachDocuNo3) {
		this.attachDocuNo3 = attachDocuNo3;
	}

	public String getOrigDeclNo1() {
		return origDeclNo1;
	}

	public void setOrigDeclNo1(String origDeclNo1) {
		this.origDeclNo1 = origDeclNo1;
	}

	public String getOrigDeclNo2() {
		return origDeclNo2;
	}

	public void setOrigDeclNo2(String origDeclNo2) {
		this.origDeclNo2 = origDeclNo2;
	}

	public String getOrigDeclNo3() {
		return origDeclNo3;
	}

	public void setOrigDeclNo3(String origDeclNo3) {
		this.origDeclNo3 = origDeclNo3;
	}

	public String getOrigDeclNo4() {
		return origDeclNo4;
	}

	public void setOrigDeclNo4(String origDeclNo4) {
		this.origDeclNo4 = origDeclNo4;
	}

	public String getOrigDeclNo5() {
		return origDeclNo5;
	}

	public void setOrigDeclNo5(String origDeclNo5) {
		this.origDeclNo5 = origDeclNo5;
	}

	public String getOrigDeclNo6() {
		return origDeclNo6;
	}

	public void setOrigDeclNo6(String origDeclNo6) {
		this.origDeclNo6 = origDeclNo6;
	}

	public String getOrigDeclNo7() {
		return origDeclNo7;
	}

	public void setOrigDeclNo7(String origDeclNo7) {
		this.origDeclNo7 = origDeclNo7;
	}

	public String getOrigDeclNo8() {
		return origDeclNo8;
	}

	public void setOrigDeclNo8(String origDeclNo8) {
		this.origDeclNo8 = origDeclNo8;
	}

	public String getOrigDeclNo9() {
		return origDeclNo9;
	}

	public void setOrigDeclNo9(String origDeclNo9) {
		this.origDeclNo9 = origDeclNo9;
	}

	public String getRelatedBfNo1() {
		return relatedBfNo1;
	}

	public void setRelatedBfNo1(String relatedBfNo1) {
		this.relatedBfNo1 = relatedBfNo1;
	}

	public String getRelatedBfNo2() {
		return relatedBfNo2;
	}

	public void setRelatedBfNo2(String relatedBfNo2) {
		this.relatedBfNo2 = relatedBfNo2;
	}

	public String getRelatedBfNo3() {
		return relatedBfNo3;
	}

	public void setRelatedBfNo3(String relatedBfNo3) {
		this.relatedBfNo3 = relatedBfNo3;
	}

	public String getRelatedBfNo4() {
		return relatedBfNo4;
	}

	public void setRelatedBfNo4(String relatedBfNo4) {
		this.relatedBfNo4 = relatedBfNo4;
	}

	public String getRelatedBfNo5() {
		return relatedBfNo5;
	}

	public void setRelatedBfNo5(String relatedBfNo5) {
		this.relatedBfNo5 = relatedBfNo5;
	}

	public String getRelatedBfNo6() {
		return relatedBfNo6;
	}

	public void setRelatedBfNo6(String relatedBfNo6) {
		this.relatedBfNo6 = relatedBfNo6;
	}

	public String getRelatedBfNo7() {
		return relatedBfNo7;
	}

	public void setRelatedBfNo7(String relatedBfNo7) {
		this.relatedBfNo7 = relatedBfNo7;
	}

	public String getRelatedBfNo8() {
		return relatedBfNo8;
	}

	public void setRelatedBfNo8(String relatedBfNo8) {
		this.relatedBfNo8 = relatedBfNo8;
	}

	public String getRelatedBfNo9() {
		return relatedBfNo9;
	}

	public void setRelatedBfNo9(String relatedBfNo9) {
		this.relatedBfNo9 = relatedBfNo9;
	}

	public String getRelatedBfBan1() {
		return relatedBfBan1;
	}

	public void setRelatedBfBan1(String relatedBfBan1) {
		this.relatedBfBan1 = relatedBfBan1;
	}

	public String getRelatedBfBan2() {
		return relatedBfBan2;
	}

	public void setRelatedBfBan2(String relatedBfBan2) {
		this.relatedBfBan2 = relatedBfBan2;
	}

	public String getRelatedBfBan3() {
		return relatedBfBan3;
	}

	public void setRelatedBfBan3(String relatedBfBan3) {
		this.relatedBfBan3 = relatedBfBan3;
	}

	public String getRelatedBfBan4() {
		return relatedBfBan4;
	}

	public void setRelatedBfBan4(String relatedBfBan4) {
		this.relatedBfBan4 = relatedBfBan4;
	}

	public String getRelatedBfBan5() {
		return relatedBfBan5;
	}

	public void setRelatedBfBan5(String relatedBfBan5) {
		this.relatedBfBan5 = relatedBfBan5;
	}

	public String getRelatedBfBan6() {
		return relatedBfBan6;
	}

	public void setRelatedBfBan6(String relatedBfBan6) {
		this.relatedBfBan6 = relatedBfBan6;
	}

	public String getRelatedBfBan7() {
		return relatedBfBan7;
	}

	public void setRelatedBfBan7(String relatedBfBan7) {
		this.relatedBfBan7 = relatedBfBan7;
	}

	public String getRelatedBfBan8() {
		return relatedBfBan8;
	}

	public void setRelatedBfBan8(String relatedBfBan8) {
		this.relatedBfBan8 = relatedBfBan8;
	}

	public String getRelatedBfBan9() {
		return relatedBfBan9;
	}

	public void setRelatedBfBan9(String relatedBfBan9) {
		this.relatedBfBan9 = relatedBfBan9;
	}

	public String getDupDeclReqMark() {
		return dupDeclReqMark;
	}

	public void setDupDeclReqMark(String dupDeclReqMark) {
		this.dupDeclReqMark = dupDeclReqMark;
	}

	public String getUsedMaterialTable() {
		return usedMaterialTable;
	}

	public void setUsedMaterialTable(String usedMaterialTable) {
		this.usedMaterialTable = usedMaterialTable;
	}

	public String getRelatedBfBan() {
		return relatedBfBan;
	}

	public void setRelatedBfBan(String relatedBfBan) {
		this.relatedBfBan = relatedBfBan;
	}

	public String getRelatedBfNo() {
		return relatedBfNo;
	}

	public void setRelatedBfNo(String relatedBfNo) {
		this.relatedBfNo = relatedBfNo;
	}

	public String getInWarehouseBan() {
		return inWarehouseBan;
	}

	public void setInWarehouseBan(String inWarehouseBan) {
		this.inWarehouseBan = inWarehouseBan;
	}

	public String getInWarehouseBfNo() {
		return inWarehouseBfNo;
	}

	public void setInWarehouseBfNo(String inWarehouseBfNo) {
		this.inWarehouseBfNo = inWarehouseBfNo;
	}

	public String getOutWarehouseBan() {
		return outWarehouseBan;
	}

	public void setOutWarehouseBan(String outWarehouseBan) {
		this.outWarehouseBan = outWarehouseBan;
	}

	public String getOutWarehouseBfNo() {
		return outWarehouseBfNo;
	}

	public void setOutWarehouseBfNo(String outWarehouseBfNo) {
		this.outWarehouseBfNo = outWarehouseBfNo;
	}

	public String getBondMonth() {
		return bondMonth;
	}

	public void setBondMonth(String bondMonth) {
		this.bondMonth = bondMonth;
	}

	public String getBondTradingRefNo() {
		return bondTradingRefNo;
	}

	public void setBondTradingRefNo(String bondTradingRefNo) {
		this.bondTradingRefNo = bondTradingRefNo;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getProcessDate() {
		return processDate;
	}

	public void setProcessDate(String processDate) {
		this.processDate = processDate;
	}

	public String getProcessTime() {
		return processTime;
	}

	public void setProcessTime(String processTime) {
		this.processTime = processTime;
	}

	public String getProcessStat() {
		return processStat;
	}

	public void setProcessStat(String processStat) {
		this.processStat = processStat;
	}

	public String getInspectType() {
		return inspectType;
	}

	public void setInspectType(String inspectType) {
		this.inspectType = inspectType;
	}

	public String getDeclRemark1() {
		return declRemark1;
	}

	public void setDeclRemark1(String declRemark1) {
		this.declRemark1 = declRemark1;
	}

	public String getDeclRemark2() {
		return declRemark2;
	}

	public void setDeclRemark2(String declRemark2) {
		this.declRemark2 = declRemark2;
	}

	public String getInternalControlNo() {
		return internalControlNo;
	}

	public void setInternalControlNo(String internalControlNo) {
		this.internalControlNo = internalControlNo;
	}

	public String getCustCname() {
		return custCname;
	}

	public void setCustCname(String custCname) {
		this.custCname = custCname;
	}

	public String getBrokerName() {
		return brokerName;
	}

	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}

	public String getStoragePlaceName() {
		return storagePlaceName;
	}

	public void setStoragePlaceName(String storagePlaceName) {
		this.storagePlaceName = storagePlaceName;
	}

	public String getDeclTypeDesc() {
		return declTypeDesc;
	}

	public void setDeclTypeDesc(String declTypeDesc) {
		this.declTypeDesc = declTypeDesc;
	}

	/*public String getExporterCname() {
		return exporterCname;
	}

	public void setExporterCname(String exporterCname) {
		this.exporterCname = exporterCname;
	}

	public String getExporterCaddress() {
		return exporterCaddress;
	}

	public void setExporterCaddress(String exporterCaddress) {
		this.exporterCaddress = exporterCaddress;
	}*/

	public String getExportPortName() {
		return exportPortName;
	}

	public void setExportPortName(String exportPortName) {
		this.exportPortName = exportPortName;
	}

	public String getDestinationName() {
		return destinationName;
	}

	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}

	public String getBuyerCountryName() {
		return buyerCountryName;
	}

	public void setBuyerCountryName(String buyerCountryName) {
		this.buyerCountryName = buyerCountryName;
	}

	public String getMsg5107Date() {
		return msg5107Date;
	}

	public void setMsg5107Date(String msg5107Date) {
		this.msg5107Date = msg5107Date;
	}

	public String getMsg511xDate() {
		return msg511xDate;
	}

	public void setMsg511xDate(String msg511xDate) {
		this.msg511xDate = msg511xDate;
	}

	public String getMsg5204Date() {
		return msg5204Date;
	}

	public void setMsg5204Date(String msg5204Date) {
		this.msg5204Date = msg5204Date;
	}

	public String getMsgTp003Date() {
		return msgTp003Date;
	}

	public void setMsgTp003Date(String msgTp003Date) {
		this.msgTp003Date = msgTp003Date;
	}

	public String getControlData() {
		return controlData;
	}

	public void setControlData(String controlData) {
		this.controlData = controlData;
	}

	public String getShippingMarks() {
		return shippingMarks;
	}

	public void setShippingMarks(String shippingMarks) {
		this.shippingMarks = shippingMarks;
	}

	public List<Ex5203dDo> getList() {
		return list;
	}

	public void setList(List<Ex5203dDo> list) {
		this.list = list;
	}

	public List<OtherDeclItemDo> getOtherDeclItems() {
		return otherDeclItems;
	}

	public void setOtherDeclItems(List<OtherDeclItemDo> otherDeclItems) {
		this.otherDeclItems = otherDeclItems;
	}
	
	//新EX_5203M_N 新增加的欄位屬性

	public String getLoadPortCode() {
		return loadPortCode;
	}

	public void setLoadPortCode(String loadPortCode) {
		this.loadPortCode = loadPortCode;
	}

	public String getFeePaymentMethod() {
		return feePaymentMethod;
	}

	public void setFeePaymentMethod(String feePaymentMethod) {
		this.feePaymentMethod = feePaymentMethod;
	}

	public String getTotalPackageNumber() {
		return totalPackageNumber;
	}

	public void setTotalPackageNumber(String totalPackageNumber) {
		this.totalPackageNumber = totalPackageNumber;
	}

	public String getFobCurrentCode() {
		return fobCurrentCode;
	}

	public void setFobCurrentCode(String fobCurrentCode) {
		this.fobCurrentCode = fobCurrentCode;
	}

	public String getBfIdType1() {
		return bfIdType1;
	}

	public void setBfIdType1(String bfIdType1) {
		this.bfIdType1 = bfIdType1;
	}

	public String getBfIdType2() {
		return bfIdType2;
	}

	public void setBfIdType2(String bfIdType2) {
		this.bfIdType2 = bfIdType2;
	}

	public String getBfIdType3() {
		return bfIdType3;
	}

	public void setBfIdType3(String bfIdType3) {
		this.bfIdType3 = bfIdType3;
	}

	public String getBfIdType4() {
		return bfIdType4;
	}

	public void setBfIdType4(String bfIdType4) {
		this.bfIdType4 = bfIdType4;
	}

	public String getBfIdType5() {
		return bfIdType5;
	}

	public void setBfIdType5(String bfIdType5) {
		this.bfIdType5 = bfIdType5;
	}

	public String getBfIdType6() {
		return bfIdType6;
	}

	public void setBfIdType6(String bfIdType6) {
		this.bfIdType6 = bfIdType6;
	}

	public String getBfIdType7() {
		return bfIdType7;
	}

	public void setBfIdType7(String bfIdType7) {
		this.bfIdType7 = bfIdType7;
	}

	public String getBfIdType8() {
		return bfIdType8;
	}

	public void setBfIdType8(String bfIdType8) {
		this.bfIdType8 = bfIdType8;
	}

	public String getBfIdType9() {
		return bfIdType9;
	}

	public void setBfIdType9(String bfIdType9) {
		this.bfIdType9 = bfIdType9;
	}

	public String getBfIdType10() {
		return bfIdType10;
	}

	public void setBfIdType10(String bfIdType10) {
		this.bfIdType10 = bfIdType10;
	}

	public String getInBondWarehouseIdType() {
		return inBondWarehouseIdType;
	}

	public void setInBondWarehouseIdType(String inBondWarehouseIdType) {
		this.inBondWarehouseIdType = inBondWarehouseIdType;
	}

	public String getOrgGroup() {
		return orgGroup;
	}

	public void setOrgGroup(String orgGroup) {
		this.orgGroup = orgGroup;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOutBondWarehouseIdType() {
		return outBondWarehouseIdType;
	}

	public void setOutBondWarehouseIdType(String outBondWarehouseIdType) {
		this.outBondWarehouseIdType = outBondWarehouseIdType;
	}

	public String getPoaId() {
		return poaId;
	}

	public void setPoaId(String poaId) {
		this.poaId = poaId;
	}

	public String getRecvDate() {
		return recvDate;
	}

	public void setRecvDate(String recvDate) {
		this.recvDate = recvDate;
	}

	public String getReserveField1() {
		return reserveField1;
	}

	public void setReserveField1(String reserveField1) {
		this.reserveField1 = reserveField1;
	}

	public String getReserveField2() {
		return reserveField2;
	}

	public void setReserveField2(String reserveField2) {
		this.reserveField2 = reserveField2;
	}

	public String getReserveField3() {
		return reserveField3;
	}

	public void setReserveField3(String reserveField3) {
		this.reserveField3 = reserveField3;
	}

	public String getReserveField4() {
		return reserveField4;
	}

	public void setReserveField4(String reserveField4) {
		this.reserveField4 = reserveField4;
	}

	public String getReserveField5() {
		return reserveField5;
	}

	public void setReserveField5(String reserveField5) {
		this.reserveField5 = reserveField5;
	}

	public String getReserveField6() {
		return reserveField6;
	}

	public void setReserveField6(String reserveField6) {
		this.reserveField6 = reserveField6;
	}

	public String getReserveField7() {
		return reserveField7;
	}

	public void setReserveField7(String reserveField7) {
		this.reserveField7 = reserveField7;
	}

	public String getReserveField8() {
		return reserveField8;
	}

	public void setReserveField8(String reserveField8) {
		this.reserveField8 = reserveField8;
	}

	public String getReserveField9() {
		return reserveField9;
	}

	public void setReserveField9(String reserveField9) {
		this.reserveField9 = reserveField9;
	}

	public String getReserveField10() {
		return reserveField10;
	}

	public void setReserveField10(String reserveField10) {
		this.reserveField10 = reserveField10;
	}

	public String getRetryCnt() {
		return retryCnt;
	}

	public void setRetryCnt(String retryCnt) {
		this.retryCnt = retryCnt;
	}

	public String getRsltCode() {
		return rsltCode;
	}

	public void setRsltCode(String rsltCode) {
		this.rsltCode = rsltCode;
	}

	public String getRsltMsg() {
		return rsltMsg;
	}

	public void setRsltMsg(String rsltMsg) {
		this.rsltMsg = rsltMsg;
	}

	public String getStoragePlaceCode2() {
		return storagePlaceCode2;
	}

	public void setStoragePlaceCode2(String storagePlaceCode2) {
		this.storagePlaceCode2 = storagePlaceCode2;
	}

	public String getTotFobValueTwd() {
		return totFobValueTwd;
	}

	public void setTotFobValueTwd(String totFobValueTwd) {
		this.totFobValueTwd = totFobValueTwd;
	}

	public String getTotInvoiceAmt() {
		return totInvoiceAmt;
	}

	public void setTotInvoiceAmt(String totInvoiceAmt) {
		this.totInvoiceAmt = totInvoiceAmt;
	}

	public String getVanId() {
		return vanId;
	}

	public void setVanId(String vanId) {
		this.vanId = vanId;
	}

	public String getVoyageFlightNo() {
		return voyageFlightNo;
	}

	public void setVoyageFlightNo(String voyageFlightNo) {
		this.voyageFlightNo = voyageFlightNo;
	}
	
	public String getBrokerAeoNo() {
		return brokerAeoNo;
	}

	public void setBrokerAeoNo(String brokerAeoNo) {
		this.brokerAeoNo = brokerAeoNo;
	}

}
