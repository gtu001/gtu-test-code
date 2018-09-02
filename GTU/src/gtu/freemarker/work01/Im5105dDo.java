package gtu.freemarker.work01;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Im5105dDo implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private String field1 = "";// 測試用欄位

	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}
	
	private boolean isFirstPage = false;
	private boolean isLastPage = false;
	
	public boolean getIsFirstPage() {
		return isFirstPage;
	}

	public void setIsFirstPage(boolean isFirstPage) {
		this.isFirstPage = isFirstPage;
	}

	public boolean getIsLastPage() {
		return isLastPage;
	}

	public void setIsLastPage(boolean isLastPage) {
		this.isLastPage = isLastPage;
	}


	private String transactionId;// 【 1 】 識別碼 varchar2(20 char)
	private String msgType;// 【 2 】 訊息格式 varchar2(10 char)
	private String declNo;// 【 3 】 報單號碼 varchar2(14 char)
	private String itemNo;// 【 4 】 項次 number(4,0)
	private String permitNo1;// 【 5 】 輸出入許可文件號碼1 varchar2(14 char)
	private String permitNo2;// 【 6 】 輸出入許可文件號碼2 varchar2(14 char)
	private String permitNo3;// 【 7 】 輸出入許可文件號碼3 varchar2(14 char)
	private String permitNo4;// 【 8 】 輸出入許可文件號碼4 varchar2(14 char)
	private String permitNo5;// 【 9 】 輸出入許可文件號碼5 varchar2(14 char)
	private String permitItemNo1;// 【 10 】 輸出入許可文件項次1 number(4,0)
	private String permitItemNo2;// 【 11 】 輸出入許可文件項次2 number(4,0)
	private String permitItemNo3;// 【 12 】 輸出入許可文件項次3 number(4,0)
	private String permitItemNo4;// 【 13 】 輸出入許可文件項次4 number(4,0)
	private String permitItemNo5;// 【 14 】 輸出入許可文件項次5 number(4,0)
	private String sellerCommodityNo1;// 【 15 】 賣方料號1 varchar2(60 char)
	private String sellerCommodityNo2;// 【 16 】 賣方料號2 varchar2(60 char)
	private String buyerCommodityNo1;// 【 17 】 買方料號1 varchar2(60 char)
	private String buyerCommodityNo2;// 【 18 】 買方料號2 varchar2(60 char)
	private String goodsDesc;// 【 19 】 貨物名稱、商標(牌名)及規格 varchar2(512 char)
	private String packageMaterialCode;// 【 20 】 包裝材料代碼 varchar2(3 char)
	private String packageSpec;// 【 21 】 包裝規格說明 varchar2(200 char)
	private String goodsType;// 【 22 】 產品種類 varchar2(5 char)
	private String goodsDescC;// 【 23 】 貨物中文名稱(品名) varchar2(1024 char)
	private String trademark;// 【 24 】 商標(牌名) varchar2(70 char)
	private String goodsModel;// 【 25 】 型號 varchar2(80 char)
	private String goodsElementSpec;// 【 26 】 成分及規格 varchar2(512 char)
	private String elementThickness;// 【 27 】 厚度 varchar2(10 char)
	private String elementGrade;// 【 28 】 等級 varchar2(20 char)
	private String commodityBarcode;// 【 29 】 商品條碼 varchar2(14 char)
	private String packageType;// 【 30 】 包裝方式代碼 varchar2(3 char)
	private String undgNo1;// 【 31 】 危險貨物代碼1 varchar2(4 char)
	private String undgNo2;// 【 32 】 危險貨物代碼2 varchar2(4 char)
	private String carYear;// 【 33 】 型式年份 varchar2(4 char)
	private String carLeftSideMark;// 【 34 】 左駕駛 varchar2(1 char)
	private String carConditionCode;// 【 35 】 車況 varchar2(1 char)
	private String carType;// 【 36 】 車型 varchar2(20 char)
	private String carDoorNumber;// 【 37 】 車門數 varchar2(1 char)
	private String carDisplacement;// 【 38 】 排氣量 varchar2(6 char)
	private String carCylNumber;// 【 39 】 汽缸數 varchar2(2 char)
	private String carSeatNumber;// 【 40 】 座位數 varchar2(2 char)
	private String carTransmissionType;// 【 41 】 排檔 varchar2(1 char)
	private String carFuelType;// 【 42 】 燃油引擎種類 varchar2(2 char)
	private String carCatalystMark;// 【 43 】 觸媒轉換器 varchar2(1 char)
	private String origBondDeclNo;// 【 44 】 原進倉報單號碼 varchar2(14 char)
	private String origBondItemNo;// 【 45 】 原進倉報單項次 number(4,0)
	private String origExDeclNo;// 【 46 】 原出口報單號碼 varchar2(14 char)
	private String origExItemNo;// 【 47 】 原出口報單項次 number(4,0)
	private String govAssignNo1;// 【 48 】 主管機關指定代號1 varchar2(35 char)
	private String govAssignNo2;// 【 49 】 主管機關指定代號2 varchar2(35 char)
	private String govAssignNo3;// 【 50 】 主管機關指定代號3 varchar2(35 char)
	private String govAssignNo4;// 【 51 】 主管機關指定代號4 varchar2(35 char)
	private String govAssignNo5;// 【 52 】 主管機關指定代號5 varchar2(35 char)
	private String govAssignNo6;// 【 53 】 主管機關指定代號6 varchar2(35 char)
	private String govAssignNo7;// 【 54 】 主管機關指定代號7 varchar2(35 char)
	private String govAssignNo8;// 【 55 】 主管機關指定代號8 varchar2(35 char)
	private String govAssignNo9;// 【 56 】 主管機關指定代號9 varchar2(35 char)
	private String govAssignNo10;// 【 57 】 主管機關指定代號10 varchar2(35 char)
	private String cooNo;// 【 58 】 產地證明書號碼 varchar2(35 char)
	private String cooItemNo;// 【 59 】 產地證明書項次 varchar2(4 char)
	private String origCountryCode;// 【 60 】 生產國別代碼 varchar2(2 char)
	private String hitechImLicenceNo;// 【 61 】 戰略性高科技貨品國際進口證明號碼 varchar2(35 char)
	private String citesPermitNo;// 【 62 】 華盛頓公約進口許可證號碼 varchar2(35 char)
	private String cccCode1;// 【 63 】 貨品分類號列1 varchar2(11 char)
	private String cccCode2;// 【 64 】 貨品分類號列2 varchar2(11 char)
	private String cccSubCode;// 【 65 】 貨品分類號列序號 varchar2(2 char)
	private String tariffAdditionNoteCode;// 【 66 】 稅則增註 varchar2(5 char)
	private String ftaTariffMark;// 【 67 】 自由貿易協定優惠關稅註記 varchar2(2 char)
	private String declPriceTermCode;// 【 68 】 單價條件 varchar2(3 char)
	private String declUnitPrice;// 【 69 】 單價金額 number(25,6)
	private String unitCurrencyCode;// 【 70 】 單價幣別代碼 varchar2(3 char)
	private String netWeight;// 【 71 】 淨重 number(16,6)
	private String packageNumber;// 【 72 】 件數 number(8,0)
	private String packageUnit;// 【 73 】 件數單位 varchar2(3 char)
	private String qty;// 【 74 】 數量 number(20,6)
	private String qtyUnit;// 【 75 】 數量單位 varchar2(3 char)
	private String statQty;// 【 76 】 統計數量 number(20,6)
	private String statUnit;// 【 77 】 統計數量單位 varchar2(3 char)
	private String customsValueAmt;// 【 78 】 完稅價格 number(16,2)
	private String customsValueQty;// 【 79 】 完稅數量 number(20,6)
	private String customsValueRatio;// 【 80 】 折算率-完稅價格 number(12,4)
	private String imDutyPriceRate;// 【 81 】 進口稅率-從價 number(15,5)
	private String imDutyPriceRatio;// 【 82 】 折算率-從價 number(12,4)
	private String imDutyQtyRate;// 【 83 】 單位稅額-從量 number(15,5)
	private String imDutyQtyUnit;// 【 84 】 稅額單位-從量 varchar2(3 char)
	private String imDutyQtyRatio;// 【 85 】 折算率-從量 number(12,4)
	private String dutyType;// 【 86 】 納稅辦法代碼 varchar2(2 char)
	private String otherDutyChargeCode1;// 【 87 】 其他稅費代碼1 varchar2(3 char)
	private String otherDutyChargeCode2;// 【 88 】 其他稅費代碼2 varchar2(3 char)
	private String otherDutyChargeCode3;// 【 89 】 其他稅費代碼3 varchar2(3 char)
	private String otherDutyChargeCode4;// 【 90 】 其他稅費代碼4 varchar2(3 char)
	private String otherDutyChargeCode5;// 【 91 】 其他稅費代碼5 varchar2(3 char)
	private String otherDutyChargeCode6;// 【 92 】 其他稅費代碼6 varchar2(3 char)
	private String otherDutyChargeCode7;// 【 93 】 其他稅費代碼7 varchar2(3 char)
	private String otherDutyChargeCode8;// 【 94 】 其他稅費代碼8 varchar2(3 char)
	private String otherDutyChargeCode9;// 【 95 】 其他稅費代碼9 varchar2(3 char)
	private String otherDutyRate1;// 【 96 】 其他稅費率1 number(15,5)
	private String otherDutyRate2;// 【 97 】 其他稅費率2 number(15,5)
	private String otherDutyRate3;// 【 98 】 其他稅費率3 number(15,5)
	private String otherDutyRate4;// 【 99 】 其他稅費率4 number(15,5)
	private String otherDutyRate5;// 【 100 】 其他稅費率5 number(15,5)
	private String otherDutyRate6;// 【 101 】 其他稅費率6 number(15,5)
	private String otherDutyRate7;// 【 102 】 其他稅費率7 number(15,5)
	private String otherDutyRate8;// 【 103 】 其他稅費率8 number(15,5)
	private String otherDutyRate9;// 【 104 】 其他稅費率9 number(15,5)
	private String otherDutyRatio1;// 【 105 】 其他稅費折算率1 number(12,4)
	private String otherDutyRatio2;// 【 106 】 其他稅費折算率2 number(12,4)
	private String otherDutyRatio3;// 【 107 】 其他稅費折算率3 number(12,4)
	private String otherDutyRatio4;// 【 108 】 其他稅費折算率4 number(12,4)
	private String otherDutyRatio5;// 【 109 】 其他稅費折算率5 number(12,4)
	private String otherDutyRatio6;// 【 110 】 其他稅費折算率6 number(12,4)
	private String otherDutyRatio7;// 【 111 】 其他稅費折算率7 number(12,4)
	private String otherDutyRatio8;// 【 112 】 其他稅費折算率8 number(12,4)
	private String otherDutyRatio9;// 【 113 】 其他稅費折算率9 number(12,4)
	private String priceQtyMark1;// 【 114 】 從價或從量註記1 varchar2(1 char)
	private String priceQtyMark2;// 【 115 】 從價或從量註記2 varchar2(1 char)
	private String priceQtyMark3;// 【 116 】 從價或從量註記3 varchar2(1 char)
	private String priceQtyMark4;// 【 117 】 從價或從量註記4 varchar2(1 char)
	private String priceQtyMark5;// 【 118 】 從價或從量註記5 varchar2(1 char)
	private String priceQtyMark6;// 【 119 】 從價或從量註記6 varchar2(1 char)
	private String priceQtyMark7;// 【 120 】 從價或從量註記7 varchar2(1 char)
	private String priceQtyMark8;// 【 121 】 從價或從量註記8 varchar2(1 char)
	private String priceQtyMark9;// 【 122 】 從價或從量註記9 varchar2(1 char)
	private String examGoodsSpecCode;// 【 123 】 貨品規格代碼 varchar2(10 char)
	private String examGoodsContent;// 【 124 】 貨品規格內容 varchar2(512 char)
	private String origPermitNo;// 【 125 】 原輸出入許可文件號碼 varchar2(14 char)
	private String frgnMftrId;// 【 126 】 國外製造廠代碼 varchar2(15 char)
	private String frgnMftrName;// 【 127 】 國外製造廠名稱 varchar2(160 char)
	private String frgnMftrAddress;// 【 128 】 國外製造廠地址 varchar2(240 char)
	private String frgnMftrStateName;// 【 129 】 國外製造廠州別名稱 varchar2(20 char)
	private String frgnMftrStateCode;// 【 130 】 國外製造廠州別代碼 varchar2(5 char)
	private String lpcoWaiverCode;// 【 131 】 免經型式認可原因 varchar2(1 char)
	private String lpcoNo;// 【 132 】 型式認可證號 varchar2(14 char)
	private String lpcoBan;// 【 133 】 型式認可證號授權人統一編號 varchar2(14 char)
	private String lpcoIdType;// 【 134 】 身分識別代碼-型式認可 varchar2(3 char)
	private String mediLpcoNo;// 【 135 】 醫療器材許可證簽審文件編號 varchar2(14 char)
	private String mediLpcoBan;// 【 136 】 醫療器材許可證授權人統一編號 varchar2(14 char)
	private String mediLpcoIdType;// 【 137 】 身分識別代碼-醫療器材 varchar2(3 char)
	private String winePrevExamResultCode;// 【 138 】 以往進口查驗情形 varchar2(1 char)
	private String wineAlcohol;// 【 139 】 酒精成分 number(6,3)
	private String wineExpDate;// 【 140 】 有效日期 varchar2(8 char)
	private String wineBottleDate;// 【 141 】 裝瓶日期 varchar2(8 char)
	private String winePreserveDate;// 【 142 】 保存期限 varchar2(8 char)
	private String wineYear;// 【 143 】 年份 varchar2(4 char)
	private String wineAge;// 【 144 】 酒齡 number(4,0)
	private String wineRegion;// 【 145 】 地理標示 varchar2(100 char)
	private String sterilizationValue;// 【 146 】 產品殺菌值 number(2,1)
	private String finalPhValue;// 【 147 】 最終產品pH值 number(2,1)
	private String elementSpec;// 【 148 】 成分及規格 varchar2(512 char)
	private String subjectSpec;// 【 149 】 毛色及特徵(或植物學名) varchar2(80 char)
	private String subjectAgeYear;// 【 150 】 年齡-年 number(4,0)
	private String subjectAgeMonth;// 【 151 】 年齡-月 number(2,0)
	private String subjectMaleNumber;// 【 152 】 雄隻數量 number(6,0)
	private String subjectFemaleNumber;// 【 153 】 雌隻數量 number(6,0)
	private String subjectChipNo;// 【 154 】 晶片號碼 varchar2(18 char)
	private String subjectVaccInfo;// 【 155 】 預防注射種類/日期 varchar2(120 char)
	private String quarantineDesc;// 【 156 】 檢疫處理 varchar2(120 char)
	private String reserveField1;// 【 157 】 備用欄位1 varchar2(70 char)
	private String reserveField2;// 【 158 】 備用欄位2 varchar2(70 char)
	private String reserveField3;// 【 159 】 備用欄位3 varchar2(70 char)
	private String reserveField4;// 【 160 】 備用欄位4 varchar2(70 char)
	private String reserveField5;// 【 161 】 備用欄位5 varchar2(70 char)
	private String reserveField6;// 【 162 】 備用欄位6 varchar2(70 char)
	private String reserveField7;// 【 163 】 備用欄位7 varchar2(70 char)
	private String reserveField8;// 【 164 】 備用欄位8 varchar2(70 char)
	private String reserveField9;// 【 165 】 備用欄位9 varchar2(70 char)
	private String reserveField10;// 【 166 】 備用欄位10 varchar2(70 char)
	private String reserveFieldCode1;// 【 167 】 備註欄位代碼 1 varchar2(1 char)
	private String reserveFieldCode2;// 【 168 】 備註欄位代碼 2 varchar2(1 char)
	private String reserveFieldCode3;// 【 169 】 備註欄位代碼 3 varchar2(1 char)
	private String reserveFieldCode4;// 【 170 】 備註欄位代碼 4 varchar2(1 char)
	private String reserveFieldCode5;// 【 171 】 備註欄位代碼 5 varchar2(1 char)
	private String reserveFieldCode6;// 【 172 】 備註欄位代碼 6 varchar2(1 char)
	private String reserveFieldCode7;// 【 173 】 備註欄位代碼 7 varchar2(1 char)
	private String reserveFieldCode8;// 【 174 】 備註欄位代碼 8 varchar2(1 char)
	private String reserveFieldCode9;// 【 175 】 備註欄位代碼 9 varchar2(1 char)
	private String reserveFieldCode10;// 【 176 】 備註欄位代碼 10 varchar2(1 char)
	private String goodsSpec;// 【 177 】 規格 varchar2(350 char)
	private String declRemark;// 【 178 】 細項其他申報事項 varchar2(512 char)
	private String customsValueUnit;// 【 179 】 完稅單位 varchar2(3 char)
	private String bondGoodsQty;// 【 180 】 保稅貨物記帳數量 number(18,4)
	private String bondGoodsUnit;// 【 181 】 保稅貨物記帳單位 varchar2(3 char)
	
	//額外設定用欄位
	private int identityNo;//網頁識別用
	private String tradeTermCode;//單價條件
	private String PermitNoAndItem;//簽審文件號碼 - 項次處理
	
	List<String> items = new ArrayList<String>();

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getDeclNo() {
		return declNo;
	}

	public void setDeclNo(String declNo) {
		this.declNo = declNo;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getPermitNo1() {
		return permitNo1;
	}

	public void setPermitNo1(String permitNo1) {
		this.permitNo1 = permitNo1;
	}

	public String getPermitNo2() {
		return permitNo2;
	}

	public void setPermitNo2(String permitNo2) {
		this.permitNo2 = permitNo2;
	}

	public String getPermitNo3() {
		return permitNo3;
	}

	public void setPermitNo3(String permitNo3) {
		this.permitNo3 = permitNo3;
	}

	public String getPermitNo4() {
		return permitNo4;
	}

	public void setPermitNo4(String permitNo4) {
		this.permitNo4 = permitNo4;
	}

	public String getPermitNo5() {
		return permitNo5;
	}

	public void setPermitNo5(String permitNo5) {
		this.permitNo5 = permitNo5;
	}

	public String getPermitItemNo1() {
		return permitItemNo1;
	}

	public void setPermitItemNo1(String permitItemNo1) {
		this.permitItemNo1 = permitItemNo1;
	}

	public String getPermitItemNo2() {
		return permitItemNo2;
	}

	public void setPermitItemNo2(String permitItemNo2) {
		this.permitItemNo2 = permitItemNo2;
	}

	public String getPermitItemNo3() {
		return permitItemNo3;
	}

	public void setPermitItemNo3(String permitItemNo3) {
		this.permitItemNo3 = permitItemNo3;
	}

	public String getPermitItemNo4() {
		return permitItemNo4;
	}

	public void setPermitItemNo4(String permitItemNo4) {
		this.permitItemNo4 = permitItemNo4;
	}

	public String getPermitItemNo5() {
		return permitItemNo5;
	}

	public void setPermitItemNo5(String permitItemNo5) {
		this.permitItemNo5 = permitItemNo5;
	}

	public String getSellerCommodityNo1() {
		return sellerCommodityNo1;
	}

	public void setSellerCommodityNo1(String sellerCommodityNo1) {
		this.sellerCommodityNo1 = sellerCommodityNo1;
	}

	public String getSellerCommodityNo2() {
		return sellerCommodityNo2;
	}

	public void setSellerCommodityNo2(String sellerCommodityNo2) {
		this.sellerCommodityNo2 = sellerCommodityNo2;
	}

	public String getBuyerCommodityNo1() {
		return buyerCommodityNo1;
	}

	public void setBuyerCommodityNo1(String buyerCommodityNo1) {
		this.buyerCommodityNo1 = buyerCommodityNo1;
	}

	public String getBuyerCommodityNo2() {
		return buyerCommodityNo2;
	}

	public void setBuyerCommodityNo2(String buyerCommodityNo2) {
		this.buyerCommodityNo2 = buyerCommodityNo2;
	}

	public String getGoodsDesc() {
		return goodsDesc;
	}

	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
	}

	public String getPackageMaterialCode() {
		return packageMaterialCode;
	}

	public void setPackageMaterialCode(String packageMaterialCode) {
		this.packageMaterialCode = packageMaterialCode;
	}

	public String getPackageSpec() {
		return packageSpec;
	}

	public void setPackageSpec(String packageSpec) {
		this.packageSpec = packageSpec;
	}

	public String getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}

	public String getGoodsDescC() {
		return goodsDescC;
	}

	public void setGoodsDescC(String goodsDescC) {
		this.goodsDescC = goodsDescC;
	}

	public String getTrademark() {
		return trademark;
	}

	public void setTrademark(String trademark) {
		this.trademark = trademark;
	}

	public String getGoodsModel() {
		return goodsModel;
	}

	public void setGoodsModel(String goodsModel) {
		this.goodsModel = goodsModel;
	}

	public String getGoodsElementSpec() {
		return goodsElementSpec;
	}

	public void setGoodsElementSpec(String goodsElementSpec) {
		this.goodsElementSpec = goodsElementSpec;
	}

	public String getElementThickness() {
		return elementThickness;
	}

	public void setElementThickness(String elementThickness) {
		this.elementThickness = elementThickness;
	}

	public String getElementGrade() {
		return elementGrade;
	}

	public void setElementGrade(String elementGrade) {
		this.elementGrade = elementGrade;
	}

	public String getCommodityBarcode() {
		return commodityBarcode;
	}

	public void setCommodityBarcode(String commodityBarcode) {
		this.commodityBarcode = commodityBarcode;
	}

	public String getPackageType() {
		return packageType;
	}

	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}

	public String getUndgNo1() {
		return undgNo1;
	}

	public void setUndgNo1(String undgNo1) {
		this.undgNo1 = undgNo1;
	}

	public String getUndgNo2() {
		return undgNo2;
	}

	public void setUndgNo2(String undgNo2) {
		this.undgNo2 = undgNo2;
	}

	public String getCarYear() {
		return carYear;
	}

	public void setCarYear(String carYear) {
		this.carYear = carYear;
	}

	public String getCarLeftSideMark() {
		return carLeftSideMark;
	}

	public void setCarLeftSideMark(String carLeftSideMark) {
		this.carLeftSideMark = carLeftSideMark;
	}

	public String getCarConditionCode() {
		return carConditionCode;
	}

	public void setCarConditionCode(String carConditionCode) {
		this.carConditionCode = carConditionCode;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public String getCarDoorNumber() {
		return carDoorNumber;
	}

	public void setCarDoorNumber(String carDoorNumber) {
		this.carDoorNumber = carDoorNumber;
	}

	public String getCarDisplacement() {
		return carDisplacement;
	}

	public void setCarDisplacement(String carDisplacement) {
		this.carDisplacement = carDisplacement;
	}

	public String getCarCylNumber() {
		return carCylNumber;
	}

	public void setCarCylNumber(String carCylNumber) {
		this.carCylNumber = carCylNumber;
	}

	public String getCarSeatNumber() {
		return carSeatNumber;
	}

	public void setCarSeatNumber(String carSeatNumber) {
		this.carSeatNumber = carSeatNumber;
	}

	public String getCarTransmissionType() {
		return carTransmissionType;
	}

	public void setCarTransmissionType(String carTransmissionType) {
		this.carTransmissionType = carTransmissionType;
	}

	public String getCarFuelType() {
		return carFuelType;
	}

	public void setCarFuelType(String carFuelType) {
		this.carFuelType = carFuelType;
	}

	public String getCarCatalystMark() {
		return carCatalystMark;
	}

	public void setCarCatalystMark(String carCatalystMark) {
		this.carCatalystMark = carCatalystMark;
	}

	public String getOrigBondDeclNo() {
		return origBondDeclNo;
	}

	public void setOrigBondDeclNo(String origBondDeclNo) {
		this.origBondDeclNo = origBondDeclNo;
	}

	public String getOrigBondItemNo() {
		return origBondItemNo;
	}

	public void setOrigBondItemNo(String origBondItemNo) {
		this.origBondItemNo = origBondItemNo;
	}

	public String getOrigExDeclNo() {
		return origExDeclNo;
	}

	public void setOrigExDeclNo(String origExDeclNo) {
		this.origExDeclNo = origExDeclNo;
	}

	public String getOrigExItemNo() {
		return origExItemNo;
	}

	public void setOrigExItemNo(String origExItemNo) {
		this.origExItemNo = origExItemNo;
	}

	public String getGovAssignNo1() {
		return govAssignNo1;
	}

	public void setGovAssignNo1(String govAssignNo1) {
		this.govAssignNo1 = govAssignNo1;
	}

	public String getGovAssignNo2() {
		return govAssignNo2;
	}

	public void setGovAssignNo2(String govAssignNo2) {
		this.govAssignNo2 = govAssignNo2;
	}

	public String getGovAssignNo3() {
		return govAssignNo3;
	}

	public void setGovAssignNo3(String govAssignNo3) {
		this.govAssignNo3 = govAssignNo3;
	}

	public String getGovAssignNo4() {
		return govAssignNo4;
	}

	public void setGovAssignNo4(String govAssignNo4) {
		this.govAssignNo4 = govAssignNo4;
	}

	public String getGovAssignNo5() {
		return govAssignNo5;
	}

	public void setGovAssignNo5(String govAssignNo5) {
		this.govAssignNo5 = govAssignNo5;
	}

	public String getGovAssignNo6() {
		return govAssignNo6;
	}

	public void setGovAssignNo6(String govAssignNo6) {
		this.govAssignNo6 = govAssignNo6;
	}

	public String getGovAssignNo7() {
		return govAssignNo7;
	}

	public void setGovAssignNo7(String govAssignNo7) {
		this.govAssignNo7 = govAssignNo7;
	}

	public String getGovAssignNo8() {
		return govAssignNo8;
	}

	public void setGovAssignNo8(String govAssignNo8) {
		this.govAssignNo8 = govAssignNo8;
	}

	public String getGovAssignNo9() {
		return govAssignNo9;
	}

	public void setGovAssignNo9(String govAssignNo9) {
		this.govAssignNo9 = govAssignNo9;
	}

	public String getGovAssignNo10() {
		return govAssignNo10;
	}

	public void setGovAssignNo10(String govAssignNo10) {
		this.govAssignNo10 = govAssignNo10;
	}

	public String getCooNo() {
		return cooNo;
	}

	public void setCooNo(String cooNo) {
		this.cooNo = cooNo;
	}

	public String getCooItemNo() {
		return cooItemNo;
	}

	public void setCooItemNo(String cooItemNo) {
		this.cooItemNo = cooItemNo;
	}

	public String getOrigCountryCode() {
		return origCountryCode;
	}

	public void setOrigCountryCode(String origCountryCode) {
		this.origCountryCode = origCountryCode;
	}

	public String getHitechImLicenceNo() {
		return hitechImLicenceNo;
	}

	public void setHitechImLicenceNo(String hitechImLicenceNo) {
		this.hitechImLicenceNo = hitechImLicenceNo;
	}

	public String getCitesPermitNo() {
		return citesPermitNo;
	}

	public void setCitesPermitNo(String citesPermitNo) {
		this.citesPermitNo = citesPermitNo;
	}

	public String getCccCode1() {
		return cccCode1;
	}

	public void setCccCode1(String cccCode1) {
		this.cccCode1 = cccCode1;
	}

	public String getCccCode2() {
		return cccCode2;
	}

	public void setCccCode2(String cccCode2) {
		this.cccCode2 = cccCode2;
	}

	public String getCccSubCode() {
		return cccSubCode;
	}

	public void setCccSubCode(String cccSubCode) {
		this.cccSubCode = cccSubCode;
	}

	public String getTariffAdditionNoteCode() {
		return tariffAdditionNoteCode;
	}

	public void setTariffAdditionNoteCode(String tariffAdditionNoteCode) {
		this.tariffAdditionNoteCode = tariffAdditionNoteCode;
	}

	public String getFtaTariffMark() {
		return ftaTariffMark;
	}

	public void setFtaTariffMark(String ftaTariffMark) {
		this.ftaTariffMark = ftaTariffMark;
	}

	public String getDeclPriceTermCode() {
		return declPriceTermCode;
	}

	public void setDeclPriceTermCode(String declPriceTermCode) {
		this.declPriceTermCode = declPriceTermCode;
	}

	public String getDeclUnitPrice() {
		return declUnitPrice;
	}

	public void setDeclUnitPrice(String declUnitPrice) {
		this.declUnitPrice = declUnitPrice;
	}

	public String getUnitCurrencyCode() {
		return unitCurrencyCode;
	}

	public void setUnitCurrencyCode(String unitCurrencyCode) {
		this.unitCurrencyCode = unitCurrencyCode;
	}

	public String getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(String netWeight) {
		this.netWeight = netWeight;
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

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public String getQtyUnit() {
		return qtyUnit;
	}

	public void setQtyUnit(String qtyUnit) {
		this.qtyUnit = qtyUnit;
	}

	public String getStatQty() {
		return statQty;
	}

	public void setStatQty(String statQty) {
		this.statQty = statQty;
	}

	public String getStatUnit() {
		return statUnit;
	}

	public void setStatUnit(String statUnit) {
		this.statUnit = statUnit;
	}

	public String getCustomsValueAmt() {
		return customsValueAmt;
	}

	public void setCustomsValueAmt(String customsValueAmt) {
		this.customsValueAmt = customsValueAmt;
	}

	public String getCustomsValueQty() {
		return customsValueQty;
	}

	public void setCustomsValueQty(String customsValueQty) {
		this.customsValueQty = customsValueQty;
	}

	public String getCustomsValueRatio() {
		return customsValueRatio;
	}

	public void setCustomsValueRatio(String customsValueRatio) {
		this.customsValueRatio = customsValueRatio;
	}

	public String getImDutyPriceRate() {
		return imDutyPriceRate;
	}

	public void setImDutyPriceRate(String imDutyPriceRate) {
		this.imDutyPriceRate = imDutyPriceRate;
	}

	public String getImDutyPriceRatio() {
		return imDutyPriceRatio;
	}

	public void setImDutyPriceRatio(String imDutyPriceRatio) {
		this.imDutyPriceRatio = imDutyPriceRatio;
	}

	public String getImDutyQtyRate() {
		return imDutyQtyRate;
	}

	public void setImDutyQtyRate(String imDutyQtyRate) {
		this.imDutyQtyRate = imDutyQtyRate;
	}

	public String getImDutyQtyUnit() {
		return imDutyQtyUnit;
	}

	public void setImDutyQtyUnit(String imDutyQtyUnit) {
		this.imDutyQtyUnit = imDutyQtyUnit;
	}

	public String getImDutyQtyRatio() {
		return imDutyQtyRatio;
	}

	public void setImDutyQtyRatio(String imDutyQtyRatio) {
		this.imDutyQtyRatio = imDutyQtyRatio;
	}

	public String getDutyType() {
		return dutyType;
	}

	public void setDutyType(String dutyType) {
		this.dutyType = dutyType;
	}

	public String getOtherDutyChargeCode1() {
		return otherDutyChargeCode1;
	}

	public void setOtherDutyChargeCode1(String otherDutyChargeCode1) {
		this.otherDutyChargeCode1 = otherDutyChargeCode1;
	}

	public String getOtherDutyChargeCode2() {
		return otherDutyChargeCode2;
	}

	public void setOtherDutyChargeCode2(String otherDutyChargeCode2) {
		this.otherDutyChargeCode2 = otherDutyChargeCode2;
	}

	public String getOtherDutyChargeCode3() {
		return otherDutyChargeCode3;
	}

	public void setOtherDutyChargeCode3(String otherDutyChargeCode3) {
		this.otherDutyChargeCode3 = otherDutyChargeCode3;
	}

	public String getOtherDutyChargeCode4() {
		return otherDutyChargeCode4;
	}

	public void setOtherDutyChargeCode4(String otherDutyChargeCode4) {
		this.otherDutyChargeCode4 = otherDutyChargeCode4;
	}

	public String getOtherDutyChargeCode5() {
		return otherDutyChargeCode5;
	}

	public void setOtherDutyChargeCode5(String otherDutyChargeCode5) {
		this.otherDutyChargeCode5 = otherDutyChargeCode5;
	}

	public String getOtherDutyChargeCode6() {
		return otherDutyChargeCode6;
	}

	public void setOtherDutyChargeCode6(String otherDutyChargeCode6) {
		this.otherDutyChargeCode6 = otherDutyChargeCode6;
	}

	public String getOtherDutyChargeCode7() {
		return otherDutyChargeCode7;
	}

	public void setOtherDutyChargeCode7(String otherDutyChargeCode7) {
		this.otherDutyChargeCode7 = otherDutyChargeCode7;
	}

	public String getOtherDutyChargeCode8() {
		return otherDutyChargeCode8;
	}

	public void setOtherDutyChargeCode8(String otherDutyChargeCode8) {
		this.otherDutyChargeCode8 = otherDutyChargeCode8;
	}

	public String getOtherDutyChargeCode9() {
		return otherDutyChargeCode9;
	}

	public void setOtherDutyChargeCode9(String otherDutyChargeCode9) {
		this.otherDutyChargeCode9 = otherDutyChargeCode9;
	}

	public String getOtherDutyRate1() {
		return otherDutyRate1;
	}

	public void setOtherDutyRate1(String otherDutyRate1) {
		this.otherDutyRate1 = otherDutyRate1;
	}

	public String getOtherDutyRate2() {
		return otherDutyRate2;
	}

	public void setOtherDutyRate2(String otherDutyRate2) {
		this.otherDutyRate2 = otherDutyRate2;
	}

	public String getOtherDutyRate3() {
		return otherDutyRate3;
	}

	public void setOtherDutyRate3(String otherDutyRate3) {
		this.otherDutyRate3 = otherDutyRate3;
	}

	public String getOtherDutyRate4() {
		return otherDutyRate4;
	}

	public void setOtherDutyRate4(String otherDutyRate4) {
		this.otherDutyRate4 = otherDutyRate4;
	}

	public String getOtherDutyRate5() {
		return otherDutyRate5;
	}

	public void setOtherDutyRate5(String otherDutyRate5) {
		this.otherDutyRate5 = otherDutyRate5;
	}

	public String getOtherDutyRate6() {
		return otherDutyRate6;
	}

	public void setOtherDutyRate6(String otherDutyRate6) {
		this.otherDutyRate6 = otherDutyRate6;
	}

	public String getOtherDutyRate7() {
		return otherDutyRate7;
	}

	public void setOtherDutyRate7(String otherDutyRate7) {
		this.otherDutyRate7 = otherDutyRate7;
	}

	public String getOtherDutyRate8() {
		return otherDutyRate8;
	}

	public void setOtherDutyRate8(String otherDutyRate8) {
		this.otherDutyRate8 = otherDutyRate8;
	}

	public String getOtherDutyRate9() {
		return otherDutyRate9;
	}

	public void setOtherDutyRate9(String otherDutyRate9) {
		this.otherDutyRate9 = otherDutyRate9;
	}

	public String getOtherDutyRatio1() {
		return otherDutyRatio1;
	}

	public void setOtherDutyRatio1(String otherDutyRatio1) {
		this.otherDutyRatio1 = otherDutyRatio1;
	}

	public String getOtherDutyRatio2() {
		return otherDutyRatio2;
	}

	public void setOtherDutyRatio2(String otherDutyRatio2) {
		this.otherDutyRatio2 = otherDutyRatio2;
	}

	public String getOtherDutyRatio3() {
		return otherDutyRatio3;
	}

	public void setOtherDutyRatio3(String otherDutyRatio3) {
		this.otherDutyRatio3 = otherDutyRatio3;
	}

	public String getOtherDutyRatio4() {
		return otherDutyRatio4;
	}

	public void setOtherDutyRatio4(String otherDutyRatio4) {
		this.otherDutyRatio4 = otherDutyRatio4;
	}

	public String getOtherDutyRatio5() {
		return otherDutyRatio5;
	}

	public void setOtherDutyRatio5(String otherDutyRatio5) {
		this.otherDutyRatio5 = otherDutyRatio5;
	}

	public String getOtherDutyRatio6() {
		return otherDutyRatio6;
	}

	public void setOtherDutyRatio6(String otherDutyRatio6) {
		this.otherDutyRatio6 = otherDutyRatio6;
	}

	public String getOtherDutyRatio7() {
		return otherDutyRatio7;
	}

	public void setOtherDutyRatio7(String otherDutyRatio7) {
		this.otherDutyRatio7 = otherDutyRatio7;
	}

	public String getOtherDutyRatio8() {
		return otherDutyRatio8;
	}

	public void setOtherDutyRatio8(String otherDutyRatio8) {
		this.otherDutyRatio8 = otherDutyRatio8;
	}

	public String getOtherDutyRatio9() {
		return otherDutyRatio9;
	}

	public void setOtherDutyRatio9(String otherDutyRatio9) {
		this.otherDutyRatio9 = otherDutyRatio9;
	}

	public String getPriceQtyMark1() {
		return priceQtyMark1;
	}

	public void setPriceQtyMark1(String priceQtyMark1) {
		this.priceQtyMark1 = priceQtyMark1;
	}

	public String getPriceQtyMark2() {
		return priceQtyMark2;
	}

	public void setPriceQtyMark2(String priceQtyMark2) {
		this.priceQtyMark2 = priceQtyMark2;
	}

	public String getPriceQtyMark3() {
		return priceQtyMark3;
	}

	public void setPriceQtyMark3(String priceQtyMark3) {
		this.priceQtyMark3 = priceQtyMark3;
	}

	public String getPriceQtyMark4() {
		return priceQtyMark4;
	}

	public void setPriceQtyMark4(String priceQtyMark4) {
		this.priceQtyMark4 = priceQtyMark4;
	}

	public String getPriceQtyMark5() {
		return priceQtyMark5;
	}

	public void setPriceQtyMark5(String priceQtyMark5) {
		this.priceQtyMark5 = priceQtyMark5;
	}

	public String getPriceQtyMark6() {
		return priceQtyMark6;
	}

	public void setPriceQtyMark6(String priceQtyMark6) {
		this.priceQtyMark6 = priceQtyMark6;
	}

	public String getPriceQtyMark7() {
		return priceQtyMark7;
	}

	public void setPriceQtyMark7(String priceQtyMark7) {
		this.priceQtyMark7 = priceQtyMark7;
	}

	public String getPriceQtyMark8() {
		return priceQtyMark8;
	}

	public void setPriceQtyMark8(String priceQtyMark8) {
		this.priceQtyMark8 = priceQtyMark8;
	}

	public String getPriceQtyMark9() {
		return priceQtyMark9;
	}

	public void setPriceQtyMark9(String priceQtyMark9) {
		this.priceQtyMark9 = priceQtyMark9;
	}

	public String getExamGoodsSpecCode() {
		return examGoodsSpecCode;
	}

	public void setExamGoodsSpecCode(String examGoodsSpecCode) {
		this.examGoodsSpecCode = examGoodsSpecCode;
	}

	public String getExamGoodsContent() {
		return examGoodsContent;
	}

	public void setExamGoodsContent(String examGoodsContent) {
		this.examGoodsContent = examGoodsContent;
	}

	public String getOrigPermitNo() {
		return origPermitNo;
	}

	public void setOrigPermitNo(String origPermitNo) {
		this.origPermitNo = origPermitNo;
	}

	public String getFrgnMftrId() {
		return frgnMftrId;
	}

	public void setFrgnMftrId(String frgnMftrId) {
		this.frgnMftrId = frgnMftrId;
	}

	public String getFrgnMftrName() {
		return frgnMftrName;
	}

	public void setFrgnMftrName(String frgnMftrName) {
		this.frgnMftrName = frgnMftrName;
	}

	public String getFrgnMftrAddress() {
		return frgnMftrAddress;
	}

	public void setFrgnMftrAddress(String frgnMftrAddress) {
		this.frgnMftrAddress = frgnMftrAddress;
	}

	public String getFrgnMftrStateName() {
		return frgnMftrStateName;
	}

	public void setFrgnMftrStateName(String frgnMftrStateName) {
		this.frgnMftrStateName = frgnMftrStateName;
	}

	public String getFrgnMftrStateCode() {
		return frgnMftrStateCode;
	}

	public void setFrgnMftrStateCode(String frgnMftrStateCode) {
		this.frgnMftrStateCode = frgnMftrStateCode;
	}

	public String getLpcoWaiverCode() {
		return lpcoWaiverCode;
	}

	public void setLpcoWaiverCode(String lpcoWaiverCode) {
		this.lpcoWaiverCode = lpcoWaiverCode;
	}

	public String getLpcoNo() {
		return lpcoNo;
	}

	public void setLpcoNo(String lpcoNo) {
		this.lpcoNo = lpcoNo;
	}

	public String getLpcoBan() {
		return lpcoBan;
	}

	public void setLpcoBan(String lpcoBan) {
		this.lpcoBan = lpcoBan;
	}

	public String getLpcoIdType() {
		return lpcoIdType;
	}

	public void setLpcoIdType(String lpcoIdType) {
		this.lpcoIdType = lpcoIdType;
	}

	public String getMediLpcoNo() {
		return mediLpcoNo;
	}

	public void setMediLpcoNo(String mediLpcoNo) {
		this.mediLpcoNo = mediLpcoNo;
	}

	public String getMediLpcoBan() {
		return mediLpcoBan;
	}

	public void setMediLpcoBan(String mediLpcoBan) {
		this.mediLpcoBan = mediLpcoBan;
	}

	public String getMediLpcoIdType() {
		return mediLpcoIdType;
	}

	public void setMediLpcoIdType(String mediLpcoIdType) {
		this.mediLpcoIdType = mediLpcoIdType;
	}

	public String getWinePrevExamResultCode() {
		return winePrevExamResultCode;
	}

	public void setWinePrevExamResultCode(String winePrevExamResultCode) {
		this.winePrevExamResultCode = winePrevExamResultCode;
	}

	public String getWineAlcohol() {
		return wineAlcohol;
	}

	public void setWineAlcohol(String wineAlcohol) {
		this.wineAlcohol = wineAlcohol;
	}

	public String getWineExpDate() {
		return wineExpDate;
	}

	public void setWineExpDate(String wineExpDate) {
		this.wineExpDate = wineExpDate;
	}

	public String getWineBottleDate() {
		return wineBottleDate;
	}

	public void setWineBottleDate(String wineBottleDate) {
		this.wineBottleDate = wineBottleDate;
	}

	public String getWinePreserveDate() {
		return winePreserveDate;
	}

	public void setWinePreserveDate(String winePreserveDate) {
		this.winePreserveDate = winePreserveDate;
	}

	public String getWineYear() {
		return wineYear;
	}

	public void setWineYear(String wineYear) {
		this.wineYear = wineYear;
	}

	public String getWineAge() {
		return wineAge;
	}

	public void setWineAge(String wineAge) {
		this.wineAge = wineAge;
	}

	public String getWineRegion() {
		return wineRegion;
	}

	public void setWineRegion(String wineRegion) {
		this.wineRegion = wineRegion;
	}

	public String getSterilizationValue() {
		return sterilizationValue;
	}

	public void setSterilizationValue(String sterilizationValue) {
		this.sterilizationValue = sterilizationValue;
	}

	public String getFinalPhValue() {
		return finalPhValue;
	}

	public void setFinalPhValue(String finalPhValue) {
		this.finalPhValue = finalPhValue;
	}

	public String getElementSpec() {
		return elementSpec;
	}

	public void setElementSpec(String elementSpec) {
		this.elementSpec = elementSpec;
	}

	public String getSubjectSpec() {
		return subjectSpec;
	}

	public void setSubjectSpec(String subjectSpec) {
		this.subjectSpec = subjectSpec;
	}

	public String getSubjectAgeYear() {
		return subjectAgeYear;
	}

	public void setSubjectAgeYear(String subjectAgeYear) {
		this.subjectAgeYear = subjectAgeYear;
	}

	public String getSubjectAgeMonth() {
		return subjectAgeMonth;
	}

	public void setSubjectAgeMonth(String subjectAgeMonth) {
		this.subjectAgeMonth = subjectAgeMonth;
	}

	public String getSubjectMaleNumber() {
		return subjectMaleNumber;
	}

	public void setSubjectMaleNumber(String subjectMaleNumber) {
		this.subjectMaleNumber = subjectMaleNumber;
	}

	public String getSubjectFemaleNumber() {
		return subjectFemaleNumber;
	}

	public void setSubjectFemaleNumber(String subjectFemaleNumber) {
		this.subjectFemaleNumber = subjectFemaleNumber;
	}

	public String getSubjectChipNo() {
		return subjectChipNo;
	}

	public void setSubjectChipNo(String subjectChipNo) {
		this.subjectChipNo = subjectChipNo;
	}

	public String getSubjectVaccInfo() {
		return subjectVaccInfo;
	}

	public void setSubjectVaccInfo(String subjectVaccInfo) {
		this.subjectVaccInfo = subjectVaccInfo;
	}

	public String getQuarantineDesc() {
		return quarantineDesc;
	}

	public void setQuarantineDesc(String quarantineDesc) {
		this.quarantineDesc = quarantineDesc;
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

	public String getGoodsSpec() {
		return goodsSpec;
	}

	public void setGoodsSpec(String goodsSpec) {
		this.goodsSpec = goodsSpec;
	}

	public String getDeclRemark() {
		return declRemark;
	}

	public void setDeclRemark(String declRemark) {
		this.declRemark = declRemark;
	}

	public String getCustomsValueUnit() {
		return customsValueUnit;
	}

	public void setCustomsValueUnit(String customsValueUnit) {
		this.customsValueUnit = customsValueUnit;
	}

	public String getBondGoodsQty() {
		return bondGoodsQty;
	}

	public void setBondGoodsQty(String bondGoodsQty) {
		this.bondGoodsQty = bondGoodsQty;
	}

	public String getBondGoodsUnit() {
		return bondGoodsUnit;
	}

	public void setBondGoodsUnit(String bondGoodsUnit) {
		this.bondGoodsUnit = bondGoodsUnit;
	}

	public int getIdentityNo() {
		return identityNo;
	}

	public void setIdentityNo(int identityNo) {
		this.identityNo = identityNo;
	}

	public String getTradeTermCode() {
		return tradeTermCode;
	}

	public void setTradeTermCode(String tradeTermCode) {
		this.tradeTermCode = tradeTermCode;
	}

	public String getPermitNoAndItem() {
		return PermitNoAndItem;
	}

	public void setPermitNoAndItem(String permitNoAndItem) {
		PermitNoAndItem = permitNoAndItem;
	}

	public List<String> getItems() {
		return items;
	}

	public void setItems(List<String> items) {
		this.items = items;
	}
	
}
