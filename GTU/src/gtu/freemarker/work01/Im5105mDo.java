package gtu.freemarker.work01;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Im5105mDo implements Serializable {
	
    private static final long serialVersionUID = 1L;

    private int totalPage;
	
	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	
	private String field1 = "";// 測試用欄位

	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	// 標記/貨櫃號碼(45)/其它申報事項(46)
	List<OtherDeclItemDo> otherDeclItems = new LinkedList<OtherDeclItemDo>();

	// 進口貨物明細
	List<Im5105dDo> list = new LinkedList<Im5105dDo>();


	private String transactionId;// 【 1 】 識別碼 varchar2(20 char)
	private String controlNo;// 【 2 】 交換控制碼 varchar2(14 char)
	private String ioType;// 【 3 】 IN, OUTBOUND 類別 varchar2(1 char)
	private String entryType;// 【 4 】 輸入方式 varchar2(1 char)
	private String msgType;// 【 5 】 訊息格式 varchar2(10 char)
	private String sendDate;// 【 6 】 傳送日期 varchar2(8 char)
	private String sendTime;// 【 7 】 傳送時間 varchar2(6 char)
	private String sendId;// 【 8 】 送件人代碼 varchar2(35 char)
	private String sendType;// 【 9 】 送件人類別 varchar2(4 char)
	private String recvId;// 【 10 】 收件人代碼 varchar2(35 char)
	private String recvType;// 【 11 】 收件人類別 varchar2(4 char)
	private String recvRefPwd;// 【 12 】 收件人參考碼報關磁片密碼 varchar2(14 char)
	private String ietType;// 【 13 】 進出口轉運別 varchar2(3 char)
	private String declCustomsCode;// 【 14 】 收單關別 varchar2(2 char)
	private String imCustomsCode;// 【 15 】 進口關別 varchar2(2 char)
	private String msgFuncCode;// 【 16 】 訊息功能代碼 varchar2(2 char)
	private String comboDeclMark;// 【 17 】 單證合一註記 varchar2(1 char)
	private String totInvoiceAmt;// 【 18 】 發票總金額 number(16,2)
	private String licenseNote;// 【 19 】 簽證情形 varchar2(1 char)
	private String totPackageNumber;// 【 20 】 總件數 number(8,0)
	private String totGrossWeight;// 【 21 】 總毛重 number(20,6)
	private String declNo;// 【 22 】 報單號碼 varchar2(14 char)
	private String declDate;// 【 23 】 報關日期 varchar2(8 char)
	private String declType;// 【 24 】 報單類別 varchar2(2 char)
	private String examType;// 【 25 】 申請審驗方式 varchar2(1 char)
	private String dupDeclReqQty;// 【 26 】 申請報單副本份數 number(2,0)
	private String brokerBoxNo;// 【 27 】 報關業者箱號 varchar2(4 char)
	private String brokerSubBoxNo;// 【 28 】 報關業者箱號附碼 varchar2(1 char)
	private String brokerType;// 【 29 】 報關業號碼別代碼 varchar2(3 char)
	private String manifNo;// 【 30 】 艙單號碼 varchar2(4 char)
	private String imTransportMode;// 【 31 】 進口運輸方式代碼 varchar2(4 char)
	private String vesselRegNo;// 【 32 】 海關通關號碼 varchar2(8 char)
	private String vesselId;// 【 33 】 船（機）代碼 varchar2(25 char)
	private String voyageFlightNo;// 【 34 】 船舶航次（海）/航機班次（空） varchar2(12 char)
	private String importDate;// 【 35 】 進口日期 varchar2(8 char)
	private String asType;// 【 36 】 海空運別 varchar2(1 char)
	private String carrierId;// 【 37 】 運輸業者/代理行代碼 varchar2(14 char)
	private String carrierType;// 【 38 】 運輸業者/代理行類別代碼 varchar2(2 char)
	private String splitMark;// 【 39 】 分批註記 varchar2(1 char)
	private String storagePlaceCode;// 【 40 】 卸存地點代碼 varchar2(8 char)
	private String currencyCode;// 【 41 】 幣別代碼 varchar2(3 char)
	private String exchangeRate;// 【 42 】 外幣匯率 number(14,5)
	private String noDutyWaiverMark;// 【 43 】 排除低價免稅註記 varchar2(1 char)
	private String dutyPaymentMethod;// 【 44 】 海關稅費繳納方式代碼 varchar2(1 char)
	private String dutyChargeCode1;// 【 45 】 稅費代號 1 varchar2(3 char)
	private String dutyChargeCode2;// 【 46 】 稅費代號 2 varchar2(3 char)
	private String dutyChargeCode3;// 【 47 】 稅費代號 3 varchar2(3 char)
	private String dutyChargeCode4;// 【 48 】 稅費代號 4 varchar2(3 char)
	private String dutyChargeCode5;// 【 49 】 稅費代號 5 varchar2(3 char)
	private String dutyChargeCode6;// 【 50 】 稅費代號 6 varchar2(3 char)
	private String dutyChargeCode7;// 【 51 】 稅費代號 7 varchar2(3 char)
	private String dutyChargeCode8;// 【 52 】 稅費代號 8 varchar2(3 char)
	private String dutyChargeCode9;// 【 53 】 稅費代號 9 varchar2(3 char)
	private String dutyAmt1;// 【 54 】 稅費金額1(貨物稅) number(12,0)
	private String dutyAmt2;// 【 55 】 稅費金額2(推貿費) number(12,0)
	private String dutyAmt3;// 【 56 】 稅費金額3(商建費) number(12,0)
	private String dutyAmt4;// 【 57 】 稅費金額4(營業稅) number(12,0)
	private String dutyAmt5;// 【 58 】 稅費金額5(滯報費) number(12,0)
	private String dutyAmt6;// 【 59 】 稅費金額6(菸酒稅) number(12,0)
	private String dutyAmt7;// 【 60 】 稅費金額7(健康捐) number(12,0)
	private String dutyAmt8;// 【 61 】 稅費金額8(額外關稅) number(12,0)
	private String dutyAmt9;// 【 62 】 稅費金額9(業務費) number(12,0)
	private String totDutyAmt;// 【 63 】 稅費合計 number(12,0)
	private String dutyMemoPrtReqMark;// 【 64 】 申請稅單列印 varchar2(1 char)
	private String consigneeId;// 【 65 】 收貨人代碼 varchar2(14 char)
	private String consigneeIdType;// 【 66 】 收貨人身分識別代碼 varchar2(3 char)
	private String consigneeNameE;// 【 67 】 收貨人英文名稱 varchar2(160 char)
	private String consigneeNameC;// 【 68 】 收貨人中文名稱 varchar2(140 char)
	private String consigneeAddressE;// 【 69 】 收貨人英文地址 varchar2(240 char)
	private String consigneeAddressC;// 【 70 】 收貨人中文地址 varchar2(210 char)
	private String reserveFieldCode1;// 【 71 】 備用欄位代碼 1 varchar2(1 char)
	private String reserveFieldCode2;// 【 72 】 備用欄位代碼 2 varchar2(1 char)
	private String reserveFieldCode3;// 【 73 】 備用欄位代碼 3 varchar2(1 char)
	private String reserveFieldCode4;// 【 74 】 備用欄位代碼 4 varchar2(1 char)
	private String reserveFieldCode5;// 【 75 】 備用欄位代碼 5 varchar2(1 char)
	private String reserveFieldCode6;// 【 76 】 備用欄位代碼 6 varchar2(1 char)
	private String reserveFieldCode7;// 【 77 】 備用欄位代碼 7 varchar2(1 char)
	private String reserveFieldCode8;// 【 78 】 備用欄位代碼 8 varchar2(1 char)
	private String reserveFieldCode9;// 【 79 】 備用欄位代碼 9 varchar2(1 char)
	private String reserveFieldCode10;// 【 80 】 備用欄位代碼 10 varchar2(1 char)
	private String reserveField1;// 【 81 】 備用欄位 1 varchar2(70 char)
	private String reserveField2;// 【 82 】 備用欄位 2 varchar2(70 char)
	private String reserveField3;// 【 83 】 備用欄位 3 varchar2(70 char)
	private String reserveField4;// 【 84 】 備用欄位 4 varchar2(70 char)
	private String reserveField5;// 【 85 】 備用欄位 5 varchar2(70 char)
	private String reserveField6;// 【 86 】 備用欄位 6 varchar2(70 char)
	private String reserveField7;// 【 87 】 備用欄位 7 varchar2(70 char)
	private String reserveField8;// 【 88 】 備用欄位 8 varchar2(70 char)
	private String reserveField9;// 【 89 】 備用欄位 9 varchar2(70 char)
	private String reserveField10;// 【 90 】 備用欄位 10 varchar2(70 char)
	private String consignorIdType;// 【 91 】 發貨人身分識別代碼 varchar2(3 char)
	private String consignorNameE;// 【 92 】 發貨人英文名稱 varchar2(160 char)
	private String consignorNameC;// 【 93 】 發貨人中文名稱 varchar2(140 char)
	private String consignorAddressE;// 【 94 】 發貨人英文地址 varchar2(240 char)
	private String consignorAddressC;// 【 95 】 發貨人中文地址 varchar2(210 char)
	private String consignorId;// 【 96 】 發貨人代碼 varchar2(14 char)
	private String relatedPartyCode;// 【 97 】 特殊關係 varchar2(3 char)
	private String freightFee;// 【 98 】 運費 number(16,2)
	private String insuranceFee;// 【 99 】 保險費 number(16,2)
	private String additionFee;// 【 100 】 應加費用 number(16,2)
	private String deductionFee;// 【 101 】 應減費用 number(16,2)
	private String businessTaxBasis;// 【 102 】 營業稅稅基 number(12,0)
	private String destinationDesc;// 【 103 】 運送抵達地 varchar2(210 char)
	private String frgnExportDate;// 【 104 】 國外出口日期 varchar2(8 char)
	private String totFobValue;// 【 105 】 總離岸價格 number(14,0)
	private String totCifValueTwd;// 【 106 】 總起岸價格(新台幣) number(14,0)
	private String notifyId;// 【 107 】 受通知人代碼 varchar2(14 char)
	private String notifyIdType;// 【 108 】 受通知人-身分識別代碼 varchar2(3 char)
	private String notifyNameE;// 【 109 】 受通知人英文名稱 varchar2(160 char)
	private String notifyNameC;// 【 110 】 受通知人中文名稱 varchar2(140 char)
	private String notifyAddressE;// 【 111 】 受通知人英文地址 varchar2(240 char)
	private String notifyAddressC;// 【 112 】 受通知人中文地址 varchar2(210 char)
	private String sellerId;// 【 113 】 出口人(或賣方)統一編號或國外廠商英文名稱縮寫 varchar2(14 char)
	private String sellerIdType;// 【 114 】 出口人(或賣方)身分識別代碼 varchar2(3 char)
	private String sellerNameE;// 【 115 】 出口人(或賣方)英文名稱 varchar2(160 char)
	private String sellerNameC;// 【 116 】 出口人(或賣方)中文名稱 varchar2(140 char)
	private String sellerBfNo;// 【 117 】 出口人(或賣方)海關監管編號 varchar2(8 char)
	private String sellerCountryCode;// 【 118 】 出口人(或賣方)國家代碼 varchar2(2 char)
	private String sellerAddressE;// 【 119 】 出口人(或賣方)英文地址 varchar2(240 char)
	private String sellerAddressC;// 【 120 】 出口人(或賣方)中文地址 varchar2(210 char)
	private String declRemark1;// 【 121 】 其他申報事項 1 varchar2(512 char)
	private String declRemark2;// 【 122 】 其他申報事項 2 varchar2(512 char)
	private String dutyPayerId;// 【 123 】 進口人(納稅義務人)統一編號 varchar2(14 char)
	private String poaId;// 【 124 】 營業稅記帳廠商編號 varchar2(8 char)
	private String dutyPayerIdType;// 【 125 】 進口人(納稅義務人)身分識別代碼 varchar2(3 char)
	private String dutyPayerNameE;// 【 126 】 進口人(納稅義務人)英文名稱 varchar2(160 char)
	private String dutyPayerNameC;// 【 127 】 進口人(納稅義務人)中文名稱 varchar2(140 char)
	private String dutyPayerBfNo;// 【 128 】 進口人(納稅義務人)海關監管編號 varchar2(8 char)
	private String dutyPayerAddressE;// 【 129 】 進口人(納稅義務人)英文地址 varchar2(240 char)
	private String dutyPayerAddressC;// 【 130 】 進口人(納稅義務人)中文地址 varchar2(210 char)
	private String dutyPayerTel;// 【 131 】 進口人(納稅義務人)電話 varchar2(20 char)
	private String dutyPayerEmail;// 【 132 】 進口人(納稅義務人)電子郵件 varchar2(60 char)
	private String loadPortCode;// 【 133 】 裝貨港代碼 varchar2(5 char)
	private String packageUnit;// 【 134 】 件數單位 varchar2(3 char)
	private String coPackageMark;// 【 135 】 合成註記 varchar2(1 char)
	private String packageTypeDesc;// 【 136 】 包裝說明 varchar2(70 char)
	private String shippingMarks;// 【 137 】 標記 varchar2(1024 char)
	private String eftPermitNo;// 【 138 】 先放後稅核准之案號 varchar2(12 char)
	private String brokerRegNo;// 【 139 】 專責報關人員代號 varchar2(5 char)
	private String tradeTermCode;// 【 140 】 交易條件代碼 varchar2(3 char)
	private String mawbNo;// 【 141 】 主提單號碼 varchar2(35 char)
	private String hawbNo;// 【 142 】 分提單號碼 varchar2(35 char)
	private String caDocType;// 【 143 】 運輸文件類別代碼 varchar2(3 char)
	private String bondAddDutyCode;// 【 144 】 保稅貨物內銷補稅原因代碼 varchar2(2 char)
	private String bondMonth;// 【 145 】 月份 number(2,0)
	private String bondTraderRefNo;// 【 146 】 交易對方參考編號 varchar2(14 char)
	private String inBondWarehouseBan;// 【 147 】 進倉保稅倉庫業者統一編號 varchar2(14 char)
	private String inBondWarehouseIdType;// 【 148 】 進倉保稅倉庫-身分識別代碼 varchar2(3 char)
	private String inBondWarehouseNo;// 【 149 】 進倉保稅倉庫代碼 varchar2(12 char)
	private String outBondWarehouseBan;// 【 150 】 出倉保稅倉庫業者統一編號 varchar2(14 char)
	private String outBondWarehouseIdType;// 【 151 】 出倉保稅倉庫業者身分識別代碼 varchar2(3 char)
	private String outBondWarehouseNo;// 【 152 】 出倉保稅倉庫代碼 varchar2(12 char)
	private String exRelatedBfBan1;// 【 153 】 原出口保稅廠商統一編號1 varchar2(14 char)
	private String exRelatedBfBan2;// 【 154 】 原出口保稅廠商統一編號2 varchar2(14 char)
	private String exRelatedBfBan3;// 【 155 】 原出口保稅廠商統一編號3 varchar2(14 char)
	private String exRelatedBfBan4;// 【 156 】 原出口保稅廠商統一編號4 varchar2(14 char)
	private String exRelatedBfBan5;// 【 157 】 原出口保稅廠商統一編號5 varchar2(14 char)
	private String exRelatedBfBan6;// 【 158 】 原出口保稅廠商統一編號6 varchar2(14 char)
	private String exRelatedBfBan7;// 【 159 】 原出口保稅廠商統一編號7 varchar2(14 char)
	private String exRelatedBfBan8;// 【 160 】 原出口保稅廠商統一編號8 varchar2(14 char)
	private String exRelatedBfBan9;// 【 161 】 原出口保稅廠商統一編號9 varchar2(14 char)
	private String exRelatedIdType;// 【 162 】 原出口保稅廠商身分識別代碼 varchar2(3 char)
	private String exRelatedBfNo1;// 【 163 】 原出口保稅廠商海關監管編號 1 varchar2(8 char)
	private String exRelatedBfNo2;// 【 164 】 原出口保稅廠商海關監管編號 2 varchar2(8 char)
	private String exRelatedBfNo3;// 【 165 】 原出口保稅廠商海關監管編號 3 varchar2(8 char)
	private String exRelatedBfNo4;// 【 166 】 原出口保稅廠商海關監管編號 4 varchar2(8 char)
	private String exRelatedBfNo5;// 【 167 】 原出口保稅廠商海關監管編號 5 varchar2(8 char)
	private String exRelatedBfNo6;// 【 168 】 原出口保稅廠商海關監管編號 6 varchar2(8 char)
	private String exRelatedBfNo7;// 【 169 】 原出口保稅廠商海關監管編號 7 varchar2(8 char)
	private String exRelatedBfNo8;// 【 170 】 原出口保稅廠商海關監管編號 8 varchar2(8 char)
	private String exRelatedBfNo9;// 【 171 】 原出口保稅廠商海關監管編號 9 varchar2(8 char)
	private String ucrNo;// 【 172 】 貨物唯一追蹤號碼 varchar2(35 char)
	private String hitechMark;// 【 173 】 是否為高科技產品 varchar2(3 char)
	private String hitechImLicenceNo;// 【 174 】 高科技產品進口證明證號 varchar2(35 char)
	private String msgRefNo;// 【 175 】 訊息參考號碼 varchar2(20 char)
	private String dupDeclReqMark;// 【 176 】 申請報單副本 varchar2(3 char)
	private String feePaymentMethod;// 【 177 】 繳納方式 varchar2(2 char)
	private String eftBan;// 【 178 】 線上扣繳開戶人統編 varchar2(14 char)
	private String eftBankAccount;// 【 179 】 線上扣繳帳號 varchar2(17 char)
	private String eftBanType;// 【 180 】 線上扣繳開戶人類別 varchar2(3 char)
	private String bankId;// 【 181 】 付款行代號 varchar2(3 char)
	private String bankBranchId;// 【 182 】 付款行分行代號 varchar2(4 char)
	private String transportMode;// 【 183 】 運輸方式 varchar2(4 char)
	private String vesselName;// 【 184 】 船機名稱 varchar2(35 char)
	private String carrierName;// 【 185 】 運輸業者/代理行名稱 varchar2(160 char)
	private String shipmentPortCode;// 【 186 】 起運口岸 varchar2(5 char)
	private String origDeclNo1;// 【 187 】 原出口報單號碼1 varchar2(14 char)
	private String origDeclNo2;// 【 188 】 原出口報單號碼2 varchar2(14 char)
	private String origDeclNo3;// 【 189 】 原出口報單號碼3 varchar2(14 char)
	private String origDeclNo4;// 【 190 】 原出口報單號碼4 varchar2(14 char)
	private String origDeclNo5;// 【 191 】 原出口報單號碼5 varchar2(14 char)
	private String origDeclNo6;// 【 192 】 原出口報單號碼6 varchar2(14 char)
	private String origDeclNo7;// 【 193 】 原出口報單號碼7 varchar2(14 char)
	private String origDeclNo8;// 【 194 】 原出口報單號碼8 varchar2(14 char)
	private String origDeclNo9;// 【 195 】 原出口報單號碼9 varchar2(14 char)
	private String cifValue;// 【 196 】 起岸價格 number(17,2)
	private String fobCurrencyCode;// 【 197 】 離岸價格幣別 varchar2(3 char)
	private String imDutyAmt;// 【 198 】 進口稅 number(12,0)
	private String totNetWeight;// 【 199 】 總淨重 number(20,6)
	private String processDate;// 【 200 】 處理日期 varchar2(8 char)
	private String processTime;// 【 201 】 處理時間 varchar2(6 char)
	private String declResultCode;// 【 202 】 通關方式 varchar2(3 char)
	
	//額外新增欄位
	private String declTypeDesc;// 【 24 】 報單類別說明 varchar2(2 char)

	public List<OtherDeclItemDo> getOtherDeclItems() {
		return otherDeclItems;
	}

	public void setOtherDeclItems(List<OtherDeclItemDo> otherDeclItems) {
		this.otherDeclItems = otherDeclItems;
	}

	public List<Im5105dDo> getList() {
		return list;
	}

	public void setList(List<Im5105dDo> list) {
		this.list = list;
	}

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

	public String getIoType() {
		return ioType;
	}

	public void setIoType(String ioType) {
		this.ioType = ioType;
	}

	public String getEntryType() {
		return entryType;
	}

	public void setEntryType(String entryType) {
		this.entryType = entryType;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getSendId() {
		return sendId;
	}

	public void setSendId(String sendId) {
		this.sendId = sendId;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	public String getRecvId() {
		return recvId;
	}

	public void setRecvId(String recvId) {
		this.recvId = recvId;
	}

	public String getRecvType() {
		return recvType;
	}

	public void setRecvType(String recvType) {
		this.recvType = recvType;
	}

	public String getRecvRefPwd() {
		return recvRefPwd;
	}

	public void setRecvRefPwd(String recvRefPwd) {
		this.recvRefPwd = recvRefPwd;
	}

	public String getIetType() {
		return ietType;
	}

	public void setIetType(String ietType) {
		this.ietType = ietType;
	}

	public String getDeclCustomsCode() {
		return declCustomsCode;
	}

	public void setDeclCustomsCode(String declCustomsCode) {
		this.declCustomsCode = declCustomsCode;
	}

	public String getImCustomsCode() {
		return imCustomsCode;
	}

	public void setImCustomsCode(String imCustomsCode) {
		this.imCustomsCode = imCustomsCode;
	}

	public String getMsgFuncCode() {
		return msgFuncCode;
	}

	public void setMsgFuncCode(String msgFuncCode) {
		this.msgFuncCode = msgFuncCode;
	}

	public String getComboDeclMark() {
		return comboDeclMark;
	}

	public void setComboDeclMark(String comboDeclMark) {
		this.comboDeclMark = comboDeclMark;
	}

	public String getTotInvoiceAmt() {
		return totInvoiceAmt;
	}

	public void setTotInvoiceAmt(String totInvoiceAmt) {
		this.totInvoiceAmt = totInvoiceAmt;
	}

	public String getLicenseNote() {
		return licenseNote;
	}

	public void setLicenseNote(String licenseNote) {
		this.licenseNote = licenseNote;
	}

	public String getTotPackageNumber() {
		return totPackageNumber;
	}

	public void setTotPackageNumber(String totPackageNumber) {
		this.totPackageNumber = totPackageNumber;
	}

	public String getTotGrossWeight() {
		return totGrossWeight;
	}

	public void setTotGrossWeight(String totGrossWeight) {
		this.totGrossWeight = totGrossWeight;
	}

	public String getDeclNo() {
		return declNo;
	}

	public void setDeclNo(String declNo) {
		this.declNo = declNo;
	}

	public String getDeclDate() {
		return declDate;
	}

	public void setDeclDate(String declDate) {
		this.declDate = declDate;
	}

	public String getDeclType() {
		return declType;
	}

	public void setDeclType(String declType) {
		this.declType = declType;
	}

	public String getExamType() {
		return examType;
	}

	public void setExamType(String examType) {
		this.examType = examType;
	}

	public String getDupDeclReqQty() {
		return dupDeclReqQty;
	}

	public void setDupDeclReqQty(String dupDeclReqQty) {
		this.dupDeclReqQty = dupDeclReqQty;
	}

	public String getBrokerBoxNo() {
		return brokerBoxNo;
	}

	public void setBrokerBoxNo(String brokerBoxNo) {
		this.brokerBoxNo = brokerBoxNo;
	}

	public String getBrokerSubBoxNo() {
		return brokerSubBoxNo;
	}

	public void setBrokerSubBoxNo(String brokerSubBoxNo) {
		this.brokerSubBoxNo = brokerSubBoxNo;
	}

	public String getBrokerType() {
		return brokerType;
	}

	public void setBrokerType(String brokerType) {
		this.brokerType = brokerType;
	}

	public String getManifNo() {
		return manifNo;
	}

	public void setManifNo(String manifNo) {
		this.manifNo = manifNo;
	}

	public String getImTransportMode() {
		return imTransportMode;
	}

	public void setImTransportMode(String imTransportMode) {
		this.imTransportMode = imTransportMode;
	}

	public String getVesselRegNo() {
		return vesselRegNo;
	}

	public void setVesselRegNo(String vesselRegNo) {
		this.vesselRegNo = vesselRegNo;
	}

	public String getVesselId() {
		return vesselId;
	}

	public void setVesselId(String vesselId) {
		this.vesselId = vesselId;
	}

	public String getVoyageFlightNo() {
		return voyageFlightNo;
	}

	public void setVoyageFlightNo(String voyageFlightNo) {
		this.voyageFlightNo = voyageFlightNo;
	}

	public String getImportDate() {
		return importDate;
	}

	public void setImportDate(String importDate) {
		this.importDate = importDate;
	}

	public String getAsType() {
		return asType;
	}

	public void setAsType(String asType) {
		this.asType = asType;
	}

	public String getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(String carrierId) {
		this.carrierId = carrierId;
	}

	public String getCarrierType() {
		return carrierType;
	}

	public void setCarrierType(String carrierType) {
		this.carrierType = carrierType;
	}

	public String getSplitMark() {
		return splitMark;
	}

	public void setSplitMark(String splitMark) {
		this.splitMark = splitMark;
	}

	public String getStoragePlaceCode() {
		return storagePlaceCode;
	}

	public void setStoragePlaceCode(String storagePlaceCode) {
		this.storagePlaceCode = storagePlaceCode;
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

	public String getNoDutyWaiverMark() {
		return noDutyWaiverMark;
	}

	public void setNoDutyWaiverMark(String noDutyWaiverMark) {
		this.noDutyWaiverMark = noDutyWaiverMark;
	}

	public String getDutyPaymentMethod() {
		return dutyPaymentMethod;
	}

	public void setDutyPaymentMethod(String dutyPaymentMethod) {
		this.dutyPaymentMethod = dutyPaymentMethod;
	}

	public String getDutyChargeCode1() {
		return dutyChargeCode1;
	}

	public void setDutyChargeCode1(String dutyChargeCode1) {
		this.dutyChargeCode1 = dutyChargeCode1;
	}

	public String getDutyChargeCode2() {
		return dutyChargeCode2;
	}

	public void setDutyChargeCode2(String dutyChargeCode2) {
		this.dutyChargeCode2 = dutyChargeCode2;
	}

	public String getDutyChargeCode3() {
		return dutyChargeCode3;
	}

	public void setDutyChargeCode3(String dutyChargeCode3) {
		this.dutyChargeCode3 = dutyChargeCode3;
	}

	public String getDutyChargeCode4() {
		return dutyChargeCode4;
	}

	public void setDutyChargeCode4(String dutyChargeCode4) {
		this.dutyChargeCode4 = dutyChargeCode4;
	}

	public String getDutyChargeCode5() {
		return dutyChargeCode5;
	}

	public void setDutyChargeCode5(String dutyChargeCode5) {
		this.dutyChargeCode5 = dutyChargeCode5;
	}

	public String getDutyChargeCode6() {
		return dutyChargeCode6;
	}

	public void setDutyChargeCode6(String dutyChargeCode6) {
		this.dutyChargeCode6 = dutyChargeCode6;
	}

	public String getDutyChargeCode7() {
		return dutyChargeCode7;
	}

	public void setDutyChargeCode7(String dutyChargeCode7) {
		this.dutyChargeCode7 = dutyChargeCode7;
	}

	public String getDutyChargeCode8() {
		return dutyChargeCode8;
	}

	public void setDutyChargeCode8(String dutyChargeCode8) {
		this.dutyChargeCode8 = dutyChargeCode8;
	}

	public String getDutyChargeCode9() {
		return dutyChargeCode9;
	}

	public void setDutyChargeCode9(String dutyChargeCode9) {
		this.dutyChargeCode9 = dutyChargeCode9;
	}

	public String getDutyAmt1() {
		return dutyAmt1;
	}

	public void setDutyAmt1(String dutyAmt1) {
		this.dutyAmt1 = dutyAmt1;
	}

	public String getDutyAmt2() {
		return dutyAmt2;
	}

	public void setDutyAmt2(String dutyAmt2) {
		this.dutyAmt2 = dutyAmt2;
	}

	public String getDutyAmt3() {
		return dutyAmt3;
	}

	public void setDutyAmt3(String dutyAmt3) {
		this.dutyAmt3 = dutyAmt3;
	}

	public String getDutyAmt4() {
		return dutyAmt4;
	}

	public void setDutyAmt4(String dutyAmt4) {
		this.dutyAmt4 = dutyAmt4;
	}

	public String getDutyAmt5() {
		return dutyAmt5;
	}

	public void setDutyAmt5(String dutyAmt5) {
		this.dutyAmt5 = dutyAmt5;
	}

	public String getDutyAmt6() {
		return dutyAmt6;
	}

	public void setDutyAmt6(String dutyAmt6) {
		this.dutyAmt6 = dutyAmt6;
	}

	public String getDutyAmt7() {
		return dutyAmt7;
	}

	public void setDutyAmt7(String dutyAmt7) {
		this.dutyAmt7 = dutyAmt7;
	}

	public String getDutyAmt8() {
		return dutyAmt8;
	}

	public void setDutyAmt8(String dutyAmt8) {
		this.dutyAmt8 = dutyAmt8;
	}

	public String getDutyAmt9() {
		return dutyAmt9;
	}

	public void setDutyAmt9(String dutyAmt9) {
		this.dutyAmt9 = dutyAmt9;
	}

	public String getTotDutyAmt() {
		return totDutyAmt;
	}

	public void setTotDutyAmt(String totDutyAmt) {
		this.totDutyAmt = totDutyAmt;
	}

	public String getDutyMemoPrtReqMark() {
		return dutyMemoPrtReqMark;
	}

	public void setDutyMemoPrtReqMark(String dutyMemoPrtReqMark) {
		this.dutyMemoPrtReqMark = dutyMemoPrtReqMark;
	}

	public String getConsigneeId() {
		return consigneeId;
	}

	public void setConsigneeId(String consigneeId) {
		this.consigneeId = consigneeId;
	}

	public String getConsigneeIdType() {
		return consigneeIdType;
	}

	public void setConsigneeIdType(String consigneeIdType) {
		this.consigneeIdType = consigneeIdType;
	}

	public String getConsigneeNameE() {
		return consigneeNameE;
	}

	public void setConsigneeNameE(String consigneeNameE) {
		this.consigneeNameE = consigneeNameE;
	}

	public String getConsigneeNameC() {
		return consigneeNameC;
	}

	public void setConsigneeNameC(String consigneeNameC) {
		this.consigneeNameC = consigneeNameC;
	}

	public String getConsigneeAddressE() {
		return consigneeAddressE;
	}

	public void setConsigneeAddressE(String consigneeAddressE) {
		this.consigneeAddressE = consigneeAddressE;
	}

	public String getConsigneeAddressC() {
		return consigneeAddressC;
	}

	public void setConsigneeAddressC(String consigneeAddressC) {
		this.consigneeAddressC = consigneeAddressC;
	}

	public String getReserveFieldCode1() {
		return reserveFieldCode1;
	}

	public void setReserveFieldCode1(String reserveFieldCode1) {
		this.reserveFieldCode1 = reserveFieldCode1;
	}

	public String getReserveFieldCode2() {
		return reserveFieldCode2;
	}

	public void setReserveFieldCode2(String reserveFieldCode2) {
		this.reserveFieldCode2 = reserveFieldCode2;
	}

	public String getReserveFieldCode3() {
		return reserveFieldCode3;
	}

	public void setReserveFieldCode3(String reserveFieldCode3) {
		this.reserveFieldCode3 = reserveFieldCode3;
	}

	public String getReserveFieldCode4() {
		return reserveFieldCode4;
	}

	public void setReserveFieldCode4(String reserveFieldCode4) {
		this.reserveFieldCode4 = reserveFieldCode4;
	}

	public String getReserveFieldCode5() {
		return reserveFieldCode5;
	}

	public void setReserveFieldCode5(String reserveFieldCode5) {
		this.reserveFieldCode5 = reserveFieldCode5;
	}

	public String getReserveFieldCode6() {
		return reserveFieldCode6;
	}

	public void setReserveFieldCode6(String reserveFieldCode6) {
		this.reserveFieldCode6 = reserveFieldCode6;
	}

	public String getReserveFieldCode7() {
		return reserveFieldCode7;
	}

	public void setReserveFieldCode7(String reserveFieldCode7) {
		this.reserveFieldCode7 = reserveFieldCode7;
	}

	public String getReserveFieldCode8() {
		return reserveFieldCode8;
	}

	public void setReserveFieldCode8(String reserveFieldCode8) {
		this.reserveFieldCode8 = reserveFieldCode8;
	}

	public String getReserveFieldCode9() {
		return reserveFieldCode9;
	}

	public void setReserveFieldCode9(String reserveFieldCode9) {
		this.reserveFieldCode9 = reserveFieldCode9;
	}

	public String getReserveFieldCode10() {
		return reserveFieldCode10;
	}

	public void setReserveFieldCode10(String reserveFieldCode10) {
		this.reserveFieldCode10 = reserveFieldCode10;
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

	public String getConsignorIdType() {
		return consignorIdType;
	}

	public void setConsignorIdType(String consignorIdType) {
		this.consignorIdType = consignorIdType;
	}

	public String getConsignorNameE() {
		return consignorNameE;
	}

	public void setConsignorNameE(String consignorNameE) {
		this.consignorNameE = consignorNameE;
	}

	public String getConsignorNameC() {
		return consignorNameC;
	}

	public void setConsignorNameC(String consignorNameC) {
		this.consignorNameC = consignorNameC;
	}

	public String getConsignorAddressE() {
		return consignorAddressE;
	}

	public void setConsignorAddressE(String consignorAddressE) {
		this.consignorAddressE = consignorAddressE;
	}

	public String getConsignorAddressC() {
		return consignorAddressC;
	}

	public void setConsignorAddressC(String consignorAddressC) {
		this.consignorAddressC = consignorAddressC;
	}

	public String getConsignorId() {
		return consignorId;
	}

	public void setConsignorId(String consignorId) {
		this.consignorId = consignorId;
	}

	public String getRelatedPartyCode() {
		return relatedPartyCode;
	}

	public void setRelatedPartyCode(String relatedPartyCode) {
		this.relatedPartyCode = relatedPartyCode;
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

	public String getBusinessTaxBasis() {
		return businessTaxBasis;
	}

	public void setBusinessTaxBasis(String businessTaxBasis) {
		this.businessTaxBasis = businessTaxBasis;
	}

	public String getDestinationDesc() {
		return destinationDesc;
	}

	public void setDestinationDesc(String destinationDesc) {
		this.destinationDesc = destinationDesc;
	}

	public String getFrgnExportDate() {
		return frgnExportDate;
	}

	public void setFrgnExportDate(String frgnExportDate) {
		this.frgnExportDate = frgnExportDate;
	}

	public String getTotFobValue() {
		return totFobValue;
	}

	public void setTotFobValue(String totFobValue) {
		this.totFobValue = totFobValue;
	}

	public String getTotCifValueTwd() {
		return totCifValueTwd;
	}

	public void setTotCifValueTwd(String totCifValueTwd) {
		this.totCifValueTwd = totCifValueTwd;
	}

	public String getNotifyId() {
		return notifyId;
	}

	public void setNotifyId(String notifyId) {
		this.notifyId = notifyId;
	}

	public String getNotifyIdType() {
		return notifyIdType;
	}

	public void setNotifyIdType(String notifyIdType) {
		this.notifyIdType = notifyIdType;
	}

	public String getNotifyNameE() {
		return notifyNameE;
	}

	public void setNotifyNameE(String notifyNameE) {
		this.notifyNameE = notifyNameE;
	}

	public String getNotifyNameC() {
		return notifyNameC;
	}

	public void setNotifyNameC(String notifyNameC) {
		this.notifyNameC = notifyNameC;
	}

	public String getNotifyAddressE() {
		return notifyAddressE;
	}

	public void setNotifyAddressE(String notifyAddressE) {
		this.notifyAddressE = notifyAddressE;
	}

	public String getNotifyAddressC() {
		return notifyAddressC;
	}

	public void setNotifyAddressC(String notifyAddressC) {
		this.notifyAddressC = notifyAddressC;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getSellerIdType() {
		return sellerIdType;
	}

	public void setSellerIdType(String sellerIdType) {
		this.sellerIdType = sellerIdType;
	}

	public String getSellerNameE() {
		return sellerNameE;
	}

	public void setSellerNameE(String sellerNameE) {
		this.sellerNameE = sellerNameE;
	}

	public String getSellerNameC() {
		return sellerNameC;
	}

	public void setSellerNameC(String sellerNameC) {
		this.sellerNameC = sellerNameC;
	}

	public String getSellerBfNo() {
		return sellerBfNo;
	}

	public void setSellerBfNo(String sellerBfNo) {
		this.sellerBfNo = sellerBfNo;
	}

	public String getSellerCountryCode() {
		return sellerCountryCode;
	}

	public void setSellerCountryCode(String sellerCountryCode) {
		this.sellerCountryCode = sellerCountryCode;
	}

	public String getSellerAddressE() {
		return sellerAddressE;
	}

	public void setSellerAddressE(String sellerAddressE) {
		this.sellerAddressE = sellerAddressE;
	}

	public String getSellerAddressC() {
		return sellerAddressC;
	}

	public void setSellerAddressC(String sellerAddressC) {
		this.sellerAddressC = sellerAddressC;
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

	public String getDutyPayerId() {
		return dutyPayerId;
	}

	public void setDutyPayerId(String dutyPayerId) {
		this.dutyPayerId = dutyPayerId;
	}

	public String getPoaId() {
		return poaId;
	}

	public void setPoaId(String poaId) {
		this.poaId = poaId;
	}

	public String getDutyPayerIdType() {
		return dutyPayerIdType;
	}

	public void setDutyPayerIdType(String dutyPayerIdType) {
		this.dutyPayerIdType = dutyPayerIdType;
	}

	public String getDutyPayerNameE() {
		return dutyPayerNameE;
	}

	public void setDutyPayerNameE(String dutyPayerNameE) {
		this.dutyPayerNameE = dutyPayerNameE;
	}

	public String getDutyPayerNameC() {
		return dutyPayerNameC;
	}

	public void setDutyPayerNameC(String dutyPayerNameC) {
		this.dutyPayerNameC = dutyPayerNameC;
	}

	public String getDutyPayerBfNo() {
		return dutyPayerBfNo;
	}

	public void setDutyPayerBfNo(String dutyPayerBfNo) {
		this.dutyPayerBfNo = dutyPayerBfNo;
	}

	public String getDutyPayerAddressE() {
		return dutyPayerAddressE;
	}

	public void setDutyPayerAddressE(String dutyPayerAddressE) {
		this.dutyPayerAddressE = dutyPayerAddressE;
	}

	public String getDutyPayerAddressC() {
		return dutyPayerAddressC;
	}

	public void setDutyPayerAddressC(String dutyPayerAddressC) {
		this.dutyPayerAddressC = dutyPayerAddressC;
	}

	public String getDutyPayerTel() {
		return dutyPayerTel;
	}

	public void setDutyPayerTel(String dutyPayerTel) {
		this.dutyPayerTel = dutyPayerTel;
	}

	public String getDutyPayerEmail() {
		return dutyPayerEmail;
	}

	public void setDutyPayerEmail(String dutyPayerEmail) {
		this.dutyPayerEmail = dutyPayerEmail;
	}

	public String getLoadPortCode() {
		return loadPortCode;
	}

	public void setLoadPortCode(String loadPortCode) {
		this.loadPortCode = loadPortCode;
	}

	public String getPackageUnit() {
		return packageUnit;
	}

	public void setPackageUnit(String packageUnit) {
		this.packageUnit = packageUnit;
	}

	public String getCoPackageMark() {
		return coPackageMark;
	}

	public void setCoPackageMark(String coPackageMark) {
		this.coPackageMark = coPackageMark;
	}

	public String getPackageTypeDesc() {
		return packageTypeDesc;
	}

	public void setPackageTypeDesc(String packageTypeDesc) {
		this.packageTypeDesc = packageTypeDesc;
	}

	public String getShippingMarks() {
		return shippingMarks;
	}

	public void setShippingMarks(String shippingMarks) {
		this.shippingMarks = shippingMarks;
	}

	public String getEftPermitNo() {
		return eftPermitNo;
	}

	public void setEftPermitNo(String eftPermitNo) {
		this.eftPermitNo = eftPermitNo;
	}

	public String getBrokerRegNo() {
		return brokerRegNo;
	}

	public void setBrokerRegNo(String brokerRegNo) {
		this.brokerRegNo = brokerRegNo;
	}

	public String getTradeTermCode() {
		return tradeTermCode;
	}

	public void setTradeTermCode(String tradeTermCode) {
		this.tradeTermCode = tradeTermCode;
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

	public String getCaDocType() {
		return caDocType;
	}

	public void setCaDocType(String caDocType) {
		this.caDocType = caDocType;
	}

	public String getBondAddDutyCode() {
		return bondAddDutyCode;
	}

	public void setBondAddDutyCode(String bondAddDutyCode) {
		this.bondAddDutyCode = bondAddDutyCode;
	}

	public String getBondMonth() {
		return bondMonth;
	}

	public void setBondMonth(String bondMonth) {
		this.bondMonth = bondMonth;
	}

	public String getBondTraderRefNo() {
		return bondTraderRefNo;
	}

	public void setBondTraderRefNo(String bondTraderRefNo) {
		this.bondTraderRefNo = bondTraderRefNo;
	}

	public String getInBondWarehouseBan() {
		return inBondWarehouseBan;
	}

	public void setInBondWarehouseBan(String inBondWarehouseBan) {
		this.inBondWarehouseBan = inBondWarehouseBan;
	}

	public String getInBondWarehouseIdType() {
		return inBondWarehouseIdType;
	}

	public void setInBondWarehouseIdType(String inBondWarehouseIdType) {
		this.inBondWarehouseIdType = inBondWarehouseIdType;
	}

	public String getInBondWarehouseNo() {
		return inBondWarehouseNo;
	}

	public void setInBondWarehouseNo(String inBondWarehouseNo) {
		this.inBondWarehouseNo = inBondWarehouseNo;
	}

	public String getOutBondWarehouseBan() {
		return outBondWarehouseBan;
	}

	public void setOutBondWarehouseBan(String outBondWarehouseBan) {
		this.outBondWarehouseBan = outBondWarehouseBan;
	}

	public String getOutBondWarehouseIdType() {
		return outBondWarehouseIdType;
	}

	public void setOutBondWarehouseIdType(String outBondWarehouseIdType) {
		this.outBondWarehouseIdType = outBondWarehouseIdType;
	}

	public String getOutBondWarehouseNo() {
		return outBondWarehouseNo;
	}

	public void setOutBondWarehouseNo(String outBondWarehouseNo) {
		this.outBondWarehouseNo = outBondWarehouseNo;
	}

	public String getExRelatedBfBan1() {
		return exRelatedBfBan1;
	}

	public void setExRelatedBfBan1(String exRelatedBfBan1) {
		this.exRelatedBfBan1 = exRelatedBfBan1;
	}

	public String getExRelatedBfBan2() {
		return exRelatedBfBan2;
	}

	public void setExRelatedBfBan2(String exRelatedBfBan2) {
		this.exRelatedBfBan2 = exRelatedBfBan2;
	}

	public String getExRelatedBfBan3() {
		return exRelatedBfBan3;
	}

	public void setExRelatedBfBan3(String exRelatedBfBan3) {
		this.exRelatedBfBan3 = exRelatedBfBan3;
	}

	public String getExRelatedBfBan4() {
		return exRelatedBfBan4;
	}

	public void setExRelatedBfBan4(String exRelatedBfBan4) {
		this.exRelatedBfBan4 = exRelatedBfBan4;
	}

	public String getExRelatedBfBan5() {
		return exRelatedBfBan5;
	}

	public void setExRelatedBfBan5(String exRelatedBfBan5) {
		this.exRelatedBfBan5 = exRelatedBfBan5;
	}

	public String getExRelatedBfBan6() {
		return exRelatedBfBan6;
	}

	public void setExRelatedBfBan6(String exRelatedBfBan6) {
		this.exRelatedBfBan6 = exRelatedBfBan6;
	}

	public String getExRelatedBfBan7() {
		return exRelatedBfBan7;
	}

	public void setExRelatedBfBan7(String exRelatedBfBan7) {
		this.exRelatedBfBan7 = exRelatedBfBan7;
	}

	public String getExRelatedBfBan8() {
		return exRelatedBfBan8;
	}

	public void setExRelatedBfBan8(String exRelatedBfBan8) {
		this.exRelatedBfBan8 = exRelatedBfBan8;
	}

	public String getExRelatedBfBan9() {
		return exRelatedBfBan9;
	}

	public void setExRelatedBfBan9(String exRelatedBfBan9) {
		this.exRelatedBfBan9 = exRelatedBfBan9;
	}

	public String getExRelatedIdType() {
		return exRelatedIdType;
	}

	public void setExRelatedIdType(String exRelatedIdType) {
		this.exRelatedIdType = exRelatedIdType;
	}

	public String getExRelatedBfNo1() {
		return exRelatedBfNo1;
	}

	public void setExRelatedBfNo1(String exRelatedBfNo1) {
		this.exRelatedBfNo1 = exRelatedBfNo1;
	}

	public String getExRelatedBfNo2() {
		return exRelatedBfNo2;
	}

	public void setExRelatedBfNo2(String exRelatedBfNo2) {
		this.exRelatedBfNo2 = exRelatedBfNo2;
	}

	public String getExRelatedBfNo3() {
		return exRelatedBfNo3;
	}

	public void setExRelatedBfNo3(String exRelatedBfNo3) {
		this.exRelatedBfNo3 = exRelatedBfNo3;
	}

	public String getExRelatedBfNo4() {
		return exRelatedBfNo4;
	}

	public void setExRelatedBfNo4(String exRelatedBfNo4) {
		this.exRelatedBfNo4 = exRelatedBfNo4;
	}

	public String getExRelatedBfNo5() {
		return exRelatedBfNo5;
	}

	public void setExRelatedBfNo5(String exRelatedBfNo5) {
		this.exRelatedBfNo5 = exRelatedBfNo5;
	}

	public String getExRelatedBfNo6() {
		return exRelatedBfNo6;
	}

	public void setExRelatedBfNo6(String exRelatedBfNo6) {
		this.exRelatedBfNo6 = exRelatedBfNo6;
	}

	public String getExRelatedBfNo7() {
		return exRelatedBfNo7;
	}

	public void setExRelatedBfNo7(String exRelatedBfNo7) {
		this.exRelatedBfNo7 = exRelatedBfNo7;
	}

	public String getExRelatedBfNo8() {
		return exRelatedBfNo8;
	}

	public void setExRelatedBfNo8(String exRelatedBfNo8) {
		this.exRelatedBfNo8 = exRelatedBfNo8;
	}

	public String getExRelatedBfNo9() {
		return exRelatedBfNo9;
	}

	public void setExRelatedBfNo9(String exRelatedBfNo9) {
		this.exRelatedBfNo9 = exRelatedBfNo9;
	}

	public String getUcrNo() {
		return ucrNo;
	}

	public void setUcrNo(String ucrNo) {
		this.ucrNo = ucrNo;
	}

	public String getHitechMark() {
		return hitechMark;
	}

	public void setHitechMark(String hitechMark) {
		this.hitechMark = hitechMark;
	}

	public String getHitechImLicenceNo() {
		return hitechImLicenceNo;
	}

	public void setHitechImLicenceNo(String hitechImLicenceNo) {
		this.hitechImLicenceNo = hitechImLicenceNo;
	}

	public String getMsgRefNo() {
		return msgRefNo;
	}

	public void setMsgRefNo(String msgRefNo) {
		this.msgRefNo = msgRefNo;
	}

	public String getDupDeclReqMark() {
		return dupDeclReqMark;
	}

	public void setDupDeclReqMark(String dupDeclReqMark) {
		this.dupDeclReqMark = dupDeclReqMark;
	}

	public String getFeePaymentMethod() {
		return feePaymentMethod;
	}

	public void setFeePaymentMethod(String feePaymentMethod) {
		this.feePaymentMethod = feePaymentMethod;
	}

	public String getEftBan() {
		return eftBan;
	}

	public void setEftBan(String eftBan) {
		this.eftBan = eftBan;
	}

	public String getEftBankAccount() {
		return eftBankAccount;
	}

	public void setEftBankAccount(String eftBankAccount) {
		this.eftBankAccount = eftBankAccount;
	}

	public String getEftBanType() {
		return eftBanType;
	}

	public void setEftBanType(String eftBanType) {
		this.eftBanType = eftBanType;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getBankBranchId() {
		return bankBranchId;
	}

	public void setBankBranchId(String bankBranchId) {
		this.bankBranchId = bankBranchId;
	}

	public String getTransportMode() {
		return transportMode;
	}

	public void setTransportMode(String transportMode) {
		this.transportMode = transportMode;
	}

	public String getVesselName() {
		return vesselName;
	}

	public void setVesselName(String vesselName) {
		this.vesselName = vesselName;
	}

	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	public String getShipmentPortCode() {
		return shipmentPortCode;
	}

	public void setShipmentPortCode(String shipmentPortCode) {
		this.shipmentPortCode = shipmentPortCode;
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

	public String getCifValue() {
		return cifValue;
	}

	public void setCifValue(String cifValue) {
		this.cifValue = cifValue;
	}

	public String getFobCurrencyCode() {
		return fobCurrencyCode;
	}

	public void setFobCurrencyCode(String fobCurrencyCode) {
		this.fobCurrencyCode = fobCurrencyCode;
	}

	public String getImDutyAmt() {
		return imDutyAmt;
	}

	public void setImDutyAmt(String imDutyAmt) {
		this.imDutyAmt = imDutyAmt;
	}

	public String getTotNetWeight() {
		return totNetWeight;
	}

	public void setTotNetWeight(String totNetWeight) {
		this.totNetWeight = totNetWeight;
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

	public String getDeclResultCode() {
		return declResultCode;
	}

	public void setDeclResultCode(String declResultCode) {
		this.declResultCode = declResultCode;
	}

	public String getDeclTypeDesc() {
		return declTypeDesc;
	}

	public void setDeclTypeDesc(String declTypeDesc) {
		this.declTypeDesc = declTypeDesc;
	}
	
	

}
