package gtu.ireport;

import java.io.Serializable;
import java.math.BigDecimal;

public class Ex5203dDo  implements Serializable{
	private String field2="";//測試用欄位
	
	public String getField2() {
		return field2;
	}

	public void setField2(String field2) {
		this.field2 = field2;
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

	private String transactionId;// 交易識別碼
	private String declNo; // 報單號碼
	private String asType; // 海空運別
	private String itemNo; // 項次
	private String itemDesc; // 項次類別說明
	private String buyerCommodityNo; // 買方料號
	private String sellerCommodityNo; // 賣方料號
	private String tradeMark; // 商標
	private String goodsDesc; // 貨物名稱
	private String goodsDesc1; // 貨物名稱
	private String goodsDesc2; // 貨物名稱
	private String goodsDesc3; // 貨物名稱
	private String goodsDesc4; // 貨物名稱
	private String goodsDesc5; // 貨物名稱
	private String goodsDesc6; // 貨物名稱
	private String goodsDesc7; // 貨物名稱
	private String goodsDesc8; // 貨物名稱
	private String goodsDesc9; // 貨物名稱
	private String goodsDesc10; // 貨物名稱
	private String goodsDesc11; // 貨物名稱
	private String goodsDesc12; // 貨物名稱
	private String goodsDesc13; // 貨物名稱
	private String goodsBrand; // 牌名
	private String goodsModel; // 型號
	private String goodsSpec; // 規格
	private String carYear; // 車輛年份
	private String carType; // 車輛車型
	private String carDoorNumber; // 車輛車門
	private String carDisplacement; // 車輛排氣量
	private String carCylNumber; // 車輛汽缸數
	private String carSeatNumber; // 車輛座位數
	private String carTransmission; // 車輛排擋
	private String carUnleaded; // 車輛使用供鉛汽油
	private String carCatalyst; // 車輛觸媒轉化器
	private String carLeftSide; // 車輛左駕駛
	private String origDeclNo; // 保稅倉庫原進倉報單號碼
	private String origItemNo; // 保稅倉庫原進倉報單項次
	private String govAssignNo1; // 主管機關指定代號1
	private String govAssignNo2; // 主管機關指定代號2
	private String govAssignNo3; // 主管機關指定代號3
	private String govAssignNo4; // 主管機關指定代號4
	private String govAssignNo5; // 主管機關指定代號5
	private String govAssignNo6; // 主管機關指定代號6
	private String govAssignNo7; // 主管機關指定代號7
	private String govAssignNo8; // 主管機關指定代號8
	private String govAssignNo9; // 主管機關指定代號9
	private String govAssignNo10; // 主管機關指定代號10
	private String origCountryCode; // 生產國別
	private String origCountry; // 生產國別說明
	private String permitNo1; // 簽審機關輸入許可文件號碼1
	private String permitItemNo1; // 簽審機關輸入許可文件項次1
	private String permitNo2; // 簽審機關輸入許可文件號碼2
	private String permitItemNo2; // 簽審機關輸入許可文件項次2
	private String permitNo3; // 簽審機關輸入許可文件號碼3
	private String permitItemNo3; // 簽審機關輸入許可文件項次3
	private String permitNo4; // 簽審機關輸入許可文件號碼4
	private String permitItemNo4; // 簽審機關輸入許可文件項次4
	private String permitNo5; // 簽審機關輸入許可文件號碼5
	private String permitItemNo5; // 簽審機關輸入許可文件項次5
	private String cccCode; // 商品分類號列
	private String cccSubCode; // 商品分類號列附碼
	private String additionNote; // 稅則增註
	private String declPriceTerms; // 單價條件
	private String fobValueTwd; // 台幣離岸價格(Fob Value)
	private String currencyCode; // 單價幣別
	private String netWeight; // 淨重
	private String qty; // 數量
	private String qtyUnit; // 數量單位
	private String bondGoodsQty; // 記帳數量
	private String bondGoodsUnit; // 記帳單位
	private String statQty; // 統計數量
	private String statUnit; // 統計單位
	private String statMode; // 統計方式
	private String customValueAmt; // 完稅價格
	private String customValueQty; // 完稅數量
	private String cvtRatio; // 折算率
	private String imDutyPriceRate; // 從價進口稅率
	private String imDutyPriceCvtRatio; // 折算率(內銷比率)
	private String imDutyQtyRate; // 從量進口單位稅額
	private String dutyUnitQty; // 從量進口稅額單位
	private String imDutyQtyCvtRatio; // 從量進口折算率
	private String dutyType; // 納稅辦法
	private String otherTaxCode1; // 其他稅費代碼1,(貨物稅)
	private String otherTaxRate1; // 稅費率1
	private String otherTaxCvtRatio1; // 折算率1
	private String otherTaxCode2; // 其他稅費代碼2, (菸酒稅)
	private String otherTaxRate2; // 稅費率2
	private String otherTaxCvtRatio2; // 折算率2
	private String otherTaxCode3; // 其他稅費代碼3 (健康捐)
	private String otherTaxRate3; // 稅費率3
	private String otherTaxCvtRatio3; // 折算率3
	private String otherTaxCode4; // 其他稅費代碼4, (額外關稅)
	private String otherTaxRate4; // 稅費率4
	private String otherTaxCvtRatio4; // 折算率4
	private String otherTaxCode5; // 其他稅費代碼5
	private String otherTaxRate5; // 稅費率5
	private String otherTaxCvtRatio5; // 折算率5
	private String otherTaxCode6; // 其他稅費代碼6
	private String otherTaxRate6; // 稅費率6
	private String otherTaxCvtRatio6; // 折算率6
	private String otherTaxCode7; // 其他稅費代碼7
	private String otherTaxRate7; // 稅費率7
	private String otherTaxCvtRatio7; // 折算率7
	private String otherTaxCode8; // 其他稅費代碼8
	private String otherTaxRate8; // 稅費率8
	private String otherTaxCvtRatio8; // 折算率8
	private String otherTaxCode9; // 其他稅費代碼9
	private String otherTaxRate9; // 稅費率9
	private String otherTaxCvtRatio9; // 折算率9
	
	/** 新報表中新增加的欄位 **/
	private String fobCurrentCode;//單價(36) - 幣別
	private String declUnitPrice;//單價(36) - 金額
	private String tradeTermCode;//條件
	private String bondGoodsMark; //保稅貨物註記:"YB" "NB"
	private String govAssignNo; //保稅貨物註記/主管機關指定代號
	private String origBondDeclNo; //保稅倉庫原進倉報單號碼
	private String origBondItemNo; //保稅倉庫原進倉報單項次
	
	private String unitPrice;//有錯顧家 by gtu001
	
	public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getTradeTermCode() {
		return tradeTermCode;
	}

	public void setTradeTermCode(String tradeTermCode) {
		this.tradeTermCode = tradeTermCode;
	}

	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getDeclNo() {
		return declNo;
	}
	public void setDeclNo(String declNo) {
		this.declNo = declNo;
	}
	public String getAsType() {
		return asType;
	}
	public void setAsType(String asType) {
		this.asType = asType;
	}
	public String getItemNo() {
		return itemNo;
	}
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	public String getBuyerCommodityNo() {
		return buyerCommodityNo;
	}
	public void setBuyerCommodityNo(String buyerCommodityNo) {
		this.buyerCommodityNo = buyerCommodityNo;
	}
	public String getSellerCommodityNo() {
		return sellerCommodityNo;
	}
	public void setSellerCommodityNo(String sellerCommodityNo) {
		this.sellerCommodityNo = sellerCommodityNo;
	}
	public String getTradeMark() {
		return tradeMark;
	}
	public void setTradeMark(String tradeMark) {
		this.tradeMark = tradeMark;
	}
	public String getGoodsDesc() {
		return goodsDesc;
	}
	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
	}
	public String getGoodsDesc1() {
		return goodsDesc1;
	}
	public void setGoodsDesc1(String goodsDesc1) {
		this.goodsDesc1 = goodsDesc1;
	}
	public String getGoodsDesc2() {
		return goodsDesc2;
	}
	public void setGoodsDesc2(String goodsDesc2) {
		this.goodsDesc2 = goodsDesc2;
	}
	public String getGoodsDesc3() {
		return goodsDesc3;
	}
	public void setGoodsDesc3(String goodsDesc3) {
		this.goodsDesc3 = goodsDesc3;
	}
	public String getGoodsDesc4() {
		return goodsDesc4;
	}
	public void setGoodsDesc4(String goodsDesc4) {
		this.goodsDesc4 = goodsDesc4;
	}
	public String getGoodsDesc5() {
		return goodsDesc5;
	}
	public void setGoodsDesc5(String goodsDesc5) {
		this.goodsDesc5 = goodsDesc5;
	}
	public String getGoodsDesc6() {
		return goodsDesc6;
	}
	public void setGoodsDesc6(String goodsDesc6) {
		this.goodsDesc6 = goodsDesc6;
	}
	public String getGoodsDesc7() {
		return goodsDesc7;
	}
	public void setGoodsDesc7(String goodsDesc7) {
		this.goodsDesc7 = goodsDesc7;
	}
	public String getGoodsDesc8() {
		return goodsDesc8;
	}
	public void setGoodsDesc8(String goodsDesc8) {
		this.goodsDesc8 = goodsDesc8;
	}
	public String getGoodsDesc9() {
		return goodsDesc9;
	}
	public void setGoodsDesc9(String goodsDesc9) {
		this.goodsDesc9 = goodsDesc9;
	}
	public String getGoodsDesc10() {
		return goodsDesc10;
	}
	public void setGoodsDesc10(String goodsDesc10) {
		this.goodsDesc10 = goodsDesc10;
	}
	public String getGoodsDesc11() {
		return goodsDesc11;
	}
	public void setGoodsDesc11(String goodsDesc11) {
		this.goodsDesc11 = goodsDesc11;
	}
	public String getGoodsDesc12() {
		return goodsDesc12;
	}
	public void setGoodsDesc12(String goodsDesc12) {
		this.goodsDesc12 = goodsDesc12;
	}
	public String getGoodsDesc13() {
		return goodsDesc13;
	}
	public void setGoodsDesc13(String goodsDesc13) {
		this.goodsDesc13 = goodsDesc13;
	}
	public String getGoodsBrand() {
		return goodsBrand;
	}
	public void setGoodsBrand(String goodsBrand) {
		this.goodsBrand = goodsBrand;
	}
	public String getGoodsModel() {
		return goodsModel;
	}
	public void setGoodsModel(String goodsModel) {
		this.goodsModel = goodsModel;
	}
	public String getGoodsSpec() {
		return goodsSpec;
	}
	public void setGoodsSpec(String goodsSpec) {
		this.goodsSpec = goodsSpec;
	}
	public String getCarYear() {
		return carYear;
	}
	public void setCarYear(String carYear) {
		this.carYear = carYear;
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
	public String getCarTransmission() {
		return carTransmission;
	}
	public void setCarTransmission(String carTransmission) {
		this.carTransmission = carTransmission;
	}
	public String getCarUnleaded() {
		return carUnleaded;
	}
	public void setCarUnleaded(String carUnleaded) {
		this.carUnleaded = carUnleaded;
	}
	public String getCarCatalyst() {
		return carCatalyst;
	}
	public void setCarCatalyst(String carCatalyst) {
		this.carCatalyst = carCatalyst;
	}
	public String getCarLeftSide() {
		return carLeftSide;
	}
	public void setCarLeftSide(String carLeftSide) {
		this.carLeftSide = carLeftSide;
	}
	public String getOrigDeclNo() {
		return origDeclNo;
	}
	public void setOrigDeclNo(String origDeclNo) {
		this.origDeclNo = origDeclNo;
	}
	public String getOrigItemNo() {
		return origItemNo;
	}
	public void setOrigItemNo(String origItemNo) {
		this.origItemNo = origItemNo;
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
	public String getOrigCountryCode() {
		return origCountryCode;
	}
	public void setOrigCountryCode(String origCountryCode) {
		this.origCountryCode = origCountryCode;
	}
	public String getOrigCountry() {
		return origCountry;
	}
	public void setOrigCountry(String origCountry) {
		this.origCountry = origCountry;
	}
	public String getPermitNo1() {
		return permitNo1;
	}
	public void setPermitNo1(String permitNo1) {
		this.permitNo1 = permitNo1;
	}
	public String getPermitItemNo1() {
		return permitItemNo1;
	}
	public void setPermitItemNo1(String permitItemNo1) {
		this.permitItemNo1 = permitItemNo1;
	}
	public String getPermitNo2() {
		return permitNo2;
	}
	public void setPermitNo2(String permitNo2) {
		this.permitNo2 = permitNo2;
	}
	public String getPermitItemNo2() {
		return permitItemNo2;
	}
	public void setPermitItemNo2(String permitItemNo2) {
		this.permitItemNo2 = permitItemNo2;
	}
	public String getPermitNo3() {
		return permitNo3;
	}
	public void setPermitNo3(String permitNo3) {
		this.permitNo3 = permitNo3;
	}
	public String getPermitItemNo3() {
		return permitItemNo3;
	}
	public void setPermitItemNo3(String permitItemNo3) {
		this.permitItemNo3 = permitItemNo3;
	}
	public String getPermitNo4() {
		return permitNo4;
	}
	public void setPermitNo4(String permitNo4) {
		this.permitNo4 = permitNo4;
	}
	public String getPermitItemNo4() {
		return permitItemNo4;
	}
	public void setPermitItemNo4(String permitItemNo4) {
		this.permitItemNo4 = permitItemNo4;
	}
	public String getPermitNo5() {
		return permitNo5;
	}
	public void setPermitNo5(String permitNo5) {
		this.permitNo5 = permitNo5;
	}
	public String getPermitItemNo5() {
		return permitItemNo5;
	}
	public void setPermitItemNo5(String permitItemNo5) {
		this.permitItemNo5 = permitItemNo5;
	}
	public String getCccCode() {
		return cccCode;
	}
	public void setCccCode(String cccCode) {
		this.cccCode = cccCode;
	}
	public String getCccSubCode() {
		return cccSubCode;
	}
	public void setCccSubCode(String cccSubCode) {
		this.cccSubCode = cccSubCode;
	}
	public String getAdditionNote() {
		return additionNote;
	}
	public void setAdditionNote(String additionNote) {
		this.additionNote = additionNote;
	}
	public String getDeclPriceTerms() {
		return declPriceTerms;
	}
	public void setDeclPriceTerms(String declPriceTerms) {
		this.declPriceTerms = declPriceTerms;
	}
	public String getFobValueTwd() {
		return fobValueTwd;
	}
	public void setFobValueTwd(String fobValueTwd) {
		this.fobValueTwd = fobValueTwd;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getNetWeight() {
		return netWeight;
	}
	public void setNetWeight(String netWeight) {
		this.netWeight = netWeight;
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
	public String getStatMode() {
		return statMode;
	}
	public void setStatMode(String statMode) {
		this.statMode = statMode;
	}
	public String getCustomValueAmt() {
		return customValueAmt;
	}
	public void setCustomValueAmt(String customValueAmt) {
		this.customValueAmt = customValueAmt;
	}
	public String getCustomValueQty() {
		return customValueQty;
	}
	public void setCustomValueQty(String customValueQty) {
		this.customValueQty = customValueQty;
	}
	public String getCvtRatio() {
		return cvtRatio;
	}
	public void setCvtRatio(String cvtRatio) {
		this.cvtRatio = cvtRatio;
	}
	public String getImDutyPriceRate() {
		return imDutyPriceRate;
	}
	public void setImDutyPriceRate(String imDutyPriceRate) {
		this.imDutyPriceRate = imDutyPriceRate;
	}
	public String getImDutyPriceCvtRatio() {
		return imDutyPriceCvtRatio;
	}
	public void setImDutyPriceCvtRatio(String imDutyPriceCvtRatio) {
		this.imDutyPriceCvtRatio = imDutyPriceCvtRatio;
	}
	public String getImDutyQtyRate() {
		return imDutyQtyRate;
	}
	public void setImDutyQtyRate(String imDutyQtyRate) {
		this.imDutyQtyRate = imDutyQtyRate;
	}
	public String getDutyUnitQty() {
		return dutyUnitQty;
	}
	public void setDutyUnitQty(String dutyUnitQty) {
		this.dutyUnitQty = dutyUnitQty;
	}
	public String getImDutyQtyCvtRatio() {
		return imDutyQtyCvtRatio;
	}
	public void setImDutyQtyCvtRatio(String imDutyQtyCvtRatio) {
		this.imDutyQtyCvtRatio = imDutyQtyCvtRatio;
	}
	public String getDutyType() {
		return dutyType;
	}
	public void setDutyType(String dutyType) {
		this.dutyType = dutyType;
	}
	public String getOtherTaxCode1() {
		return otherTaxCode1;
	}
	public void setOtherTaxCode1(String otherTaxCode1) {
		this.otherTaxCode1 = otherTaxCode1;
	}
	public String getOtherTaxRate1() {
		return otherTaxRate1;
	}
	public void setOtherTaxRate1(String otherTaxRate1) {
		this.otherTaxRate1 = otherTaxRate1;
	}
	public String getOtherTaxCvtRatio1() {
		return otherTaxCvtRatio1;
	}
	public void setOtherTaxCvtRatio1(String otherTaxCvtRatio1) {
		this.otherTaxCvtRatio1 = otherTaxCvtRatio1;
	}
	public String getOtherTaxCode2() {
		return otherTaxCode2;
	}
	public void setOtherTaxCode2(String otherTaxCode2) {
		this.otherTaxCode2 = otherTaxCode2;
	}
	public String getOtherTaxRate2() {
		return otherTaxRate2;
	}
	public void setOtherTaxRate2(String otherTaxRate2) {
		this.otherTaxRate2 = otherTaxRate2;
	}
	public String getOtherTaxCvtRatio2() {
		return otherTaxCvtRatio2;
	}
	public void setOtherTaxCvtRatio2(String otherTaxCvtRatio2) {
		this.otherTaxCvtRatio2 = otherTaxCvtRatio2;
	}
	public String getOtherTaxCode3() {
		return otherTaxCode3;
	}
	public void setOtherTaxCode3(String otherTaxCode3) {
		this.otherTaxCode3 = otherTaxCode3;
	}
	public String getOtherTaxRate3() {
		return otherTaxRate3;
	}
	public void setOtherTaxRate3(String otherTaxRate3) {
		this.otherTaxRate3 = otherTaxRate3;
	}
	public String getOtherTaxCvtRatio3() {
		return otherTaxCvtRatio3;
	}
	public void setOtherTaxCvtRatio3(String otherTaxCvtRatio3) {
		this.otherTaxCvtRatio3 = otherTaxCvtRatio3;
	}
	public String getOtherTaxCode4() {
		return otherTaxCode4;
	}
	public void setOtherTaxCode4(String otherTaxCode4) {
		this.otherTaxCode4 = otherTaxCode4;
	}
	public String getOtherTaxRate4() {
		return otherTaxRate4;
	}
	public void setOtherTaxRate4(String otherTaxRate4) {
		this.otherTaxRate4 = otherTaxRate4;
	}
	public String getOtherTaxCvtRatio4() {
		return otherTaxCvtRatio4;
	}
	public void setOtherTaxCvtRatio4(String otherTaxCvtRatio4) {
		this.otherTaxCvtRatio4 = otherTaxCvtRatio4;
	}
	public String getOtherTaxCode5() {
		return otherTaxCode5;
	}
	public void setOtherTaxCode5(String otherTaxCode5) {
		this.otherTaxCode5 = otherTaxCode5;
	}
	public String getOtherTaxRate5() {
		return otherTaxRate5;
	}
	public void setOtherTaxRate5(String otherTaxRate5) {
		this.otherTaxRate5 = otherTaxRate5;
	}
	public String getOtherTaxCvtRatio5() {
		return otherTaxCvtRatio5;
	}
	public void setOtherTaxCvtRatio5(String otherTaxCvtRatio5) {
		this.otherTaxCvtRatio5 = otherTaxCvtRatio5;
	}
	public String getOtherTaxCode6() {
		return otherTaxCode6;
	}
	public void setOtherTaxCode6(String otherTaxCode6) {
		this.otherTaxCode6 = otherTaxCode6;
	}
	public String getOtherTaxRate6() {
		return otherTaxRate6;
	}
	public void setOtherTaxRate6(String otherTaxRate6) {
		this.otherTaxRate6 = otherTaxRate6;
	}
	public String getOtherTaxCvtRatio6() {
		return otherTaxCvtRatio6;
	}
	public void setOtherTaxCvtRatio6(String otherTaxCvtRatio6) {
		this.otherTaxCvtRatio6 = otherTaxCvtRatio6;
	}
	public String getOtherTaxCode7() {
		return otherTaxCode7;
	}
	public void setOtherTaxCode7(String otherTaxCode7) {
		this.otherTaxCode7 = otherTaxCode7;
	}
	public String getOtherTaxRate7() {
		return otherTaxRate7;
	}
	public void setOtherTaxRate7(String otherTaxRate7) {
		this.otherTaxRate7 = otherTaxRate7;
	}
	public String getOtherTaxCvtRatio7() {
		return otherTaxCvtRatio7;
	}
	public void setOtherTaxCvtRatio7(String otherTaxCvtRatio7) {
		this.otherTaxCvtRatio7 = otherTaxCvtRatio7;
	}
	public String getOtherTaxCode8() {
		return otherTaxCode8;
	}
	public void setOtherTaxCode8(String otherTaxCode8) {
		this.otherTaxCode8 = otherTaxCode8;
	}
	public String getOtherTaxRate8() {
		return otherTaxRate8;
	}
	public void setOtherTaxRate8(String otherTaxRate8) {
		this.otherTaxRate8 = otherTaxRate8;
	}
	public String getOtherTaxCvtRatio8() {
		return otherTaxCvtRatio8;
	}
	public void setOtherTaxCvtRatio8(String otherTaxCvtRatio8) {
		this.otherTaxCvtRatio8 = otherTaxCvtRatio8;
	}
	public String getOtherTaxCode9() {
		return otherTaxCode9;
	}
	public void setOtherTaxCode9(String otherTaxCode9) {
		this.otherTaxCode9 = otherTaxCode9;
	}
	public String getOtherTaxRate9() {
		return otherTaxRate9;
	}
	public void setOtherTaxRate9(String otherTaxRate9) {
		this.otherTaxRate9 = otherTaxRate9;
	}
	public String getOtherTaxCvtRatio9() {
		return otherTaxCvtRatio9;
	}
	public void setOtherTaxCvtRatio9(String otherTaxCvtRatio9) {
		this.otherTaxCvtRatio9 = otherTaxCvtRatio9;
	}

	public String getFobCurrentCode() {
		return fobCurrentCode;
	}

	public void setFobCurrentCode(String fobCurrentCode) {
		this.fobCurrentCode = fobCurrentCode;
	}
	
	public String getDeclUnitPrice() {
		return declUnitPrice;
	}
	public void setDeclUnitPrice(String declUnitPrice) {
		this.declUnitPrice = declUnitPrice;
	}
	
	public String getBondGoodsMark() {
		return bondGoodsMark;
	}

	public void setBondGoodsMark(String bondGoodsMark) {
		this.bondGoodsMark = bondGoodsMark;
	}
	
	public String getGovAssignNo() {
		return govAssignNo;
	}

	public void setGovAssignNo(String govAssignNo) {
		this.govAssignNo = govAssignNo;
	}
	public String getOrigBondDeclNo() {
		return origBondDeclNo;
	}
	public void setOrigBondDeclNo(String origDeclNo) {
		this.origBondDeclNo = origBondDeclNo;
	}
	public String getOrigBondItemNo() {
		return origBondItemNo;
	}
	public void setOrigBondItemNo(String origItemNo) {
		this.origBondItemNo = origBondItemNo;
	}
	
}
