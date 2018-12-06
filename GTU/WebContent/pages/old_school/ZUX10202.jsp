<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>
<%@ page language='java' contentType='text/html; charset=BIG5'%>

<!--
程式：XXZX0101.jsp
功能：基本EBAF練習
作者：
完成：
-->

<html>
<head>
<%@ include file='/html/CM/header.jsp'%>
<%@ include file='/html/CM/msgDisplayer.jsp'%>

<!--匯入外部Javascript 與 css-->

<title></title>
<link href='${cssBase}/cm.css' rel='stylesheet' type='text/css'>

<script type='text/JavaScript' src='${htmlBase}/CM/js/ajax/prototype.js'></script>
<!-- 必要 -->
<script type='text/JavaScript' src='${htmlBase}/CM/js/ajax/CSRUtil.js'></script>
<!-- 必要 -->
<script type='text/JavaScript' src='${htmlBase}/CM/js/ui/TableUI.js'></script>
<!-- 目前系統常用的表格工具 -->
<script type="text/JavaScript" src="${htmlBase}/CM/js/calendar.js"></script>
<script type="text/javascript" src="${htmlBase}/CM/js/ui/validation.js"></script>
<script type="text/javascript" src="${htmlBase}/CM/js/ui/PageUI.js"></script>
<script type="text/javascript" src="${htmlBase}/CM/js/ui/popupWin.js"></script>
<!-- <script type="text/javascript" src="${htmlBase}/CM/js/ui/commView.js"></script> -->
<script type="text/JavaScript"
	src="${htmlBase}/CM/js/ui/LocaleDisplay.js"></script>
<script type="text/JavaScript"
	src="${htmlBase}/CM/js/ui/InputUtility.js"></script>
<script type="text/javascript" src="${htmlBase}/CM/js/RPTUtil.js"></script>
<script language="JavaScript" src="<%=htmlBase%>/CM/js/showWindow.js"></script>
<script language="JavaScript" src="<%=htmlBase%>/CM/js/utility.js"></script>
<script language="JavaScript" src="<%=htmlBase%>/CM/js/ElementGroup.js"></script>
<script language="JavaScript" src="<%=htmlBase%>/CM/js/date.js"></script>
<script language="JavaScript" src="<%=htmlBase%>/CM/js/calendar.js"></script>

<style>
.textarea2 {
	display: inline;
	overflow-x: hidden;
	overflow-y: visible;
	height: 60px;
	width: 186px;
}

div.tbXY td {
	border: #C7E49C solid 0px;
}

.BoxCase1 {
	Z-INDEX: 100000;
	LEFT: -800px;
	VISIBILITY: hidden;
	WIDTH: 300px;
	POSITION: absolute;
	TOP: -600px;
	HEIGHT: 50px;
	overflow: visible;
	FILTER: Alpha(Opacity =     90);
	scrollbar-face-color: #B0D8FF;
	scrollbar-highlight-color: #B0D8FF;
	scrollbar-3dlight-color: #E6F2FF;
	scrollbar-darkshadow-color: #4DA2FF;
	scrollbar-shadow-color: #95C8FF;
	scrollbar-arrow-color: #fff;
	scrollbar-track-color: #DFEEFF;
}

.BoxCase1 {
	BORDER-RIGHT: 2px outset;
	PADDING-RIGHT: 6px;
	BORDER-TOP: 2px outset;
	PADDING-LEFT: 6px;
	BACKGROUND: #FFF;
	PADDING-BOTTOM: 6px;
	BORDER-LEFT: 2px outset;
	COLOR: #000000;
	PADDING-TOP: 6px;
	BORDER-BOTTOM: 2px outset;
	TEXT-ALIGN: left;
}

.BoxCase1 {
	font: 13px/ 15px verdana, arial, sans-serif;
}

.BoxCase1 p {
	margin: 0px;
	margin-top: 5px;
	line-height: 18px;
	color: '#0078F0';
	word-wrap: normal
}

.BoxCase1 p:first-letter {
	font-weight: 900;
	color: '#0078F0';
	font-size: 24px;
}

.BoxCase1 em {
	display: block;
	margin-top: 3px;
	color: #f60;
	font-style: normal;
	font-weight: bold;
}

.BoxCase1 em span {
	font-weight: bold;
	color: '#0078F0';
	word-wrap: normal
}

.BoxShad {
	LEFT: -800px;
	WIDTH: 100px;
	TOP: -600px;
	HEIGHT: 200px
}

.tabs_menu {
	padding: 3px;
	margin: 0px 2px;
	border: 1px solid #666;
	border-bottom-width: 0px;
	padding-bottom: 1px;
	border-collapse: collapse;
	background: #EEE;
	color: #666;
	border-radius: 7px 7px 0px 0px;
}

.onSelected {
	position: relative;
	z-index: 1 !important;
	top: 1px;
	padding-bottom: 1px;
	margin: 0px 3px 0px 3px;
	border: 1px solid #666;
	border-bottom-width: 0px;
	background: #FA1;
	color: #000;
	font-weight: bold;
	cursor: default;
	border-radius: 7px 7px 0px 0px;
}
</style>

<script type='text/javascript'>

<%-- 產生畫面物件 --%>
<%--透過 prototype 的 Event 物件監聽 onload 事件，觸發時進行 initApp()--%>
var mZUX10202 = new ZUX10202();
Event.observe(window, 'load', mZUX10202.initApp);

HTMLElement.prototype.after = function() {
	var argArr = Array.prototype.slice.call(arguments),
	docFrag = document.createDocumentFragment();
	argArr.forEach(function (argItem) {
		var isNode = argItem instanceof Node;
		docFrag.appendChild(isNode ? argItem : document.createTextNode(String(argItem)));
	});
	this.parentNode.insertBefore(docFrag, this.nextSibling);
}

HTMLElement.prototype.show = function(isShow) {
	if(isShow){
		this.style.visibility = "visible";
		this.style.display = "block";
	}else{
		this.style.visibility = "hidden";
     	this.style.display = "none";
	}
}

HTMLElement.prototype.emptyChildren = function(){
	var myNode = this;
	while (myNode.firstChild) {
	    myNode.removeChild(myNode.firstChild);
	}
}

function SerialNoList() {
	this.initSelection = function(MOD_NO, SER_NO, mapX121List){
		this.mapX121List = mapX121List;
		var arry = document.getElementsByName("SER_NO_LIST");
		for(var ii = 0 ; ii < arry.length; ii ++){
			var serNoList = arry[ii];
			var modNo = serNoList.getAttribute("MOD_NO");
			var serNo = serNoList.getAttribute("SER_NO");
			
			if((MOD_NO == undefined && SER_NO == undefined) || 
				(modNo == MOD_NO && serNo == SER_NO)) {
				this.initDetail(serNoList, modNo, serNo);
			}
		}
	}
	
	this.initDetail = function(serNoList, modNo, serNo){
		var dtlArry = this.mapX121List[modNo + "-" + serNo];
		if(!dtlArry){
			return;
		}
		
		serNoList.emptyChildren();
		for(var ii = 0 ; ii < dtlArry.length; ii ++) {
			var span = document.createElement("span");
			var txt1 = document.createTextNode(" " + (ii + 1) + " ");
			span.setAttribute("jsonObj", Object.toJSON(dtlArry[ii]));
			span.setAttribute("class", "tabs_menu");
			span.appendChild(txt1);
			serNoList.appendChild(span);
			
			span.addEventListener("click", function(e) {
				var reqMap = JSON.parse(this.getAttribute("jsonObj"));
				mZUX10202.actions.doQuery0022(reqMap.FORM_NO, reqMap.MOD_NO, reqMap.SER_NO, reqMap.EXEC_NO, reqMap.SQL_NO, reqMap.CK_SQL, reqMap.Q_COLs);
			});
		}
	};
	return this;
}

function ActionForValueTr() {
	this.commonValidate = function(){
		var Q_COLs = mZUX10202.applyMapX112.getQColMap();
		if(!Q_COLs){
			return false;
		}
		if(!mZUX10202.applyMapX112.isQMapNoModify()){
			alert('查詢條件已異動,請重新查詢');
			return false;
		}
		var valueMap = mZUX10202.applyMapX112.getUColMap();
		if(!valueMap){
			return false;
		}
		return true;
	};
	
	this.getReqMap = function(){
		var reqMap = {
			FORM_NO : $('FORM_NO').value,
			MOD_NO : mZUX10202.applyMapX112.reqMap.MOD_NO,
			SER_NO : mZUX10202.applyMapX112.reqMap.SER_NO,
			EXEC_NO : mZUX10202.applyMapX112.reqMap.EXEC_NO,
		};
		return reqMap;
	}

	this.enter = function(){
		if(!this.commonValidate()){
			return;
		}
		mZUX10202.actions.doSaveValues_Input(this.getReqMap());
	};
	
	this.update = function(){
		if(!this.commonValidate()){
			return;
		}
		mZUX10202.actions.doSaveValues_Update(this.getReqMap());
	};
	
	this.delete1 = function(){
		if(!this.commonValidate()){
			return;
		}
		mZUX10202.actions.doSaveValues_Delete(this.getReqMap());
	};
}

function ApplyMapX112(reqMap, mapX112, x125Map) {
	this.reqMap = reqMap;
	this.mapX112 = mapX112;
	
	this.getX125Map = function(typeChar, x125Map){
		var rtnMap = {};
		for(var key in x125Map){
			if(key.indexOf(typeChar + "||") != -1){
				var keyNew = key.replace(typeChar + "||", "");
				rtnMap[keyNew] = x125Map[key];
			}
		}
		return rtnMap;
	};
	
	this.Qx125Map = this.getX125Map("Q", x125Map || {});
	this.Ux125Map = this.getX125Map("U", x125Map || {});
	
	this.COL_PROPERTIES = mapX112.COL_PROPERTIES;
	this.Q_COLs = mapX112.Q_COLs;
	this.U_COLs = mapX112.U_COLs;
	
	this.updateReqMap = function(reqMap1){
		for(var key in reqMap1){
			this.reqMap[key] = reqMap1[key];
		}
	};
	
	this.isQMapNoModify = function(){
		var currentMap = this.getQColMap();
		var orignMap = this.Qx125Map;
		for(var column in currentMap){
			if(currentMap[column] != orignMap[column]){
				return false;
			}
		}
		return true;
	};
	
	this.isUMapNoModify = function(){
		var currentMap = this.getUColMap();
		var orignMap = this.Ux125Map;
		for(var column in currentMap){
			if(currentMap[column] != orignMap[column]){
				return false;
			}
		}
		return true;
	};
	
	this.doQueryByQCOLDone = function(){
		this.Qx125Map = this.getQColMap();
	};
	
	this.tdAddInput = function(td, namePrefix, name){
		var input = document.createElement("input");
		input.setAttribute("name", namePrefix + "_" + name);
		var def = this.COL_PROPERTIES[name];
		input.setAttribute("nullable", def['NULLABLE']);
		input.setAttribute("maxlength", def['COL_LENGTH']);
		input.setAttribute("COL_TYPE", def['COL_TYPE']);
		td.appendChild(input);
		if(namePrefix == "Q"){
			input.value = this.Qx125Map[name] || "";
		}else if(namePrefix == "U"){
			input.value = this.Ux125Map[name] || "";
		}
	
		if("DATE" == def['COL_TYPE']) { //
			input.setAttribute('datatype','date');
			input.setAttribute('size',12);
			input.setAttribute('maxLength',10);
			
			var cal = document.createElement("img");
			cal.setAttribute("src","${imageBase}/CM/calendar.gif");
			cal.setAttribute("alt", "Date");
			cal.addEventListener("click", function(){
				getCalendarFor(input);
			});
			td.appendChild(cal);
		}
		return input;
	};
	
	this.getQColMap = function(){
		var map = {};
		for(var ii in this.Q_COLs){
			map[ii] = document.querySelector("input[name=" + "Q_" +  ii + "]").value;
		}
		return map;
	};
	
	this.getUColMap = function(){
		var map = {};
		for(var ii in this.U_COLs){
			map[ii] = document.querySelector("input[name=" + "U_" +  ii + "]").value;
		}
		return map;
	};
	
	this.createQueryBtn = function(){
		var btn = document.createElement("input");
		btn.setAttribute("id", "BTN_query");
		btn.setAttribute("type", "button");
		btn.setAttribute("value", "查詢");
		btn.addEventListener("click", function(e){
			mZUX10202.actions.doQueryByQCOL();
		});
		return btn;
	};
	
	this.createBtn = function(id, value, func){
		var btn = document.createElement("input");
		btn.setAttribute("id", id);
		btn.setAttribute("type", "button");
		btn.setAttribute("value", value);
		btn.addEventListener("click", function(e){
			func();
		});
		return btn;
	};
	
	this.setTd = function(td) {
		td.setAttribute("class", "tbBlue2");
		td.setAttribute("align", "center");
	};
	
	this.emptyTbody = function(){
		document.querySelector("tbody[id=Q_COLs_tbody]").emptyChildren();
		document.querySelector("tbody[id=U_COLs_tbody]").emptyChildren();
	};
	
	this.apply = function(){
		this.emptyTbody();
		this.applyQCols();
		this.applyUCols();
	};
	
	this.mapSize = function(myObj){
		return Object.keys(myObj).length;
	}
	
	this.applyQCols = function(){
		var Q_COLsTbody = document.querySelector("tbody[id=Q_COLs_tbody]");
		
		var trIndex = 0;
		var rowspanLength = this.mapSize(this.Q_COLs);
		for(var column in this.Q_COLs){
			var tr1 = document.createElement("tr");
			Q_COLsTbody.appendChild(tr1);
			
			var td1 = document.createElement("td");	
			this.setTd(td1);
			var td1Text = this.Q_COLs[column] + "(" + column + ")";
			var td1Text_td1 = document.createTextNode(td1Text);
			td1.appendChild(td1Text_td1);
			tr1.appendChild(td1);
			
			var td2 = document.createElement("td");
			this.setTd(td2);	
			this.tdAddInput(td2, "Q", column);
			tr1.appendChild(td2);
			
			if(trIndex == 0){
				var td3 = document.createElement("td");
				td3.setAttribute("rowspan", rowspanLength);
				this.setTd(td3);	
				td3.appendChild(this.createQueryBtn());
				tr1.appendChild(td3);
			}
			trIndex ++;
		}
	};
	
	this.isBtn_showControl = function() {
		if(!this.reqMap.EXEC_NO || this.reqMap.EXEC_NO == ''){
			document.querySelector("#BTN_doSaveValues_Input").show(true);
			document.querySelector("#BTN_doSaveValues_Update").show(false);
			document.querySelector("#BTN_doSaveValues_Delete").show(false);
		}else{
			document.querySelector("#BTN_doSaveValues_Input").show(false);
			document.querySelector("#BTN_doSaveValues_Update").show(true);
			document.querySelector("#BTN_doSaveValues_Delete").show(true);
		}
	};
	
	this.applyUCols = function(){
		var U_COLsTbody = document.querySelector("tbody[id=U_COLs_tbody]");
		
		var trIndex = 0;
		var rowspanLength = this.mapSize(this.U_COLs);
		for(var column in this.U_COLs){
			var tr1 = document.createElement("tr");
			U_COLsTbody.appendChild(tr1);
			
			var td1 = document.createElement("td");	
			this.setTd(td1);
			var td1Text = this.U_COLs[column] + "(" + column + ")";
			var td1Text_td1 = document.createTextNode(td1Text);
			td1.appendChild(td1Text_td1);
			tr1.appendChild(td1);
			
			var td2 = document.createElement("td");
			this.setTd(td2);	
			this.tdAddInput(td2, "U", column);
			tr1.appendChild(td2);
			
			if(trIndex == 0){
				var td3 = document.createElement("td");
				td3.setAttribute("rowspan", rowspanLength);
				this.setTd(td3);	
				
				td3.appendChild(this.createBtn("BTN_doSaveValues_Input", "輸入", function() { mZUX10202.actionForValueTr.enter(); }));
				td3.appendChild(this.createBtn("BTN_doSaveValues_Update", "儲存", function() { mZUX10202.actionForValueTr.update(); }));
				td3.appendChild(this.createBtn("BTN_doSaveValues_Delete", "刪除", function() { mZUX10202.actionForValueTr.delete1(); }));
				tr1.appendChild(td3);
				
				this.isBtn_showControl();
			}
			trIndex ++;
		}
	};
	
	return this;
}

function ValTableControl() {
	this.tabMenuTriggerClick = function(){
		var objs = document.querySelector(".tabs_menu");
		if(Array.isArray(objs)){
			objs = objs[0];
		}
		//objs.classList.remove("onSelected");
		$(objs).simulate("click");
	};

	this.creatSQL_LINK = function(dataList) {
		if(!dataList){
			return;
		}
		
		document.querySelector('#SQL_LINK').emptyChildren();
		
		var sqlLink = document.querySelector("#SQL_LINK");
		var table1 = document.createElement("table");
		sqlLink.appendChild(table1);
		var tbody = document.createElement("tbody");
		table1.appendChild(tbody);
		
		var tr1 = document.createElement("tr");
		tbody.appendChild(tr1);
		
		//修正語法
		for(var i=0; i<dataList.length; i++){
			var td1 = document.createElement("td");
			td1.setAttribute("class", "tabs_menu");
			
			var data = dataList[i];
			
			var a1 = document.createElement("a");
			td1.appendChild(a1);
			a1.setAttribute("id", "SQLlink_" + i);
			a1.setAttribute("href", "#");
			a1.setAttribute("div_id", "SQLBOX" + i);
			a1.innerText = data.SQL_NO+'：'+data.DF_MEMO;
			
			a1.setAttribute("jsonObj", Object.toJSON(
								{MOD_NO : data.MOD_NO,
								SER_NO : data.SER_NO,
								SQL_NO : data.SQL_NO,
								EXEC_NO : data.EXEC_NO,
								}));
			a1.setAttribute("CK_SQL", data.CK_SQL);

			var div1 = document.createElement("div");
			a1.after(div1);
	        
	        a1.addEventListener("mouseover", function(e){
                var rect = td1.getBoundingClientRect();
                div1.style.position = "absolute";
                div1.style.left = (rect.left + rect.width);
	   	        div1.style.top = (rect.top + rect.height);
	                
	            div1.style.visibility = "visible";
	            div1.style.display = "block";
	        });
	        a1.addEventListener("mouseout", function(e){
	       		div1.style.visibility = "hidden";
	       		div1.style.display = "none";
	        });
	        
	        a1.addEventListener("click", function(e){
	        	td1.setAttribute("class", "tabs_menu onSelected");
				var data = JSON.parse(a1.getAttribute("jsonObj")||'{}');
				var CK_SQL = a1.getAttribute("CK_SQL") || "";
				mZUX10202.actions.doQuerySqlDetail(data, CK_SQL);
			});
			
			div1.setAttribute("class", "BoxCase1");
			div1.setAttribute("id", "SQLBOX"+i);
			div1.setAttribute("style", "width:auto;");
			div1.style.visibility = "hidden";
	       	div1.style.display = "none";
			
			var em1 = document.createElement("em");
			em1.setAttribute("style", "width:auto;");
			var txt1 = document.createTextNode("備註:");
			em1.appendChild(txt1);
			
			var span1 = document.createElement("span");
			var txt2 = document.createTextNode((data.SHOW_MEMO||''));
			span1.appendChild(txt2);
			
			em1.appendChild(span1);
			div1.appendChild(em1);
			
			tr1.appendChild(td1);
		}
	};
	
	this.controlSQLTable = function(rtnMap, TABLE_PROPERTIES_MAP){
		document.querySelector('#U_AREA').show(false);	
		document.querySelector('#D_AREA').show(false);		
		document.querySelector('#I_AREA').show(false);	
		document.querySelector('#Q_AREA').show(false);	
		
		var MOD_TP = rtnMap.MOD_TP;
		
		if('2' == MOD_TP){//修改
			document.querySelector('#Q_AREA').show(true);
			document.querySelector('#U_AREA').show(true);	
			this.createUPDSQLTable(rtnMap, TABLE_PROPERTIES_MAP);
		}else if('3' == MOD_TP){//刪除
			document.querySelector('#Q_AREA').show(true);
			document.querySelector('#D_AREA').show(true);
			this.createDELSQLTable(rtnMap, TABLE_PROPERTIES_MAP);
		}else if('4' == MOD_TP){//新增
			document.querySelector('#I_AREA').show(true);
			this.createINSSQLTable(rtnMap, TABLE_PROPERTIES_MAP);
		}
	};
	
	this.createTd = function(rowspan, width, innerHtml, innerText, clzType){
		var td1 = document.createElement("td");
		var classStr = "tbBlue2";
		switch(clzType) {
		case 2:
			classStr = "tbYellow2";
			break;
		}
		td1.setAttribute("class", classStr);
		td1.setAttribute("align", "center");
		if(rowspan){
			td1.setAttribute("rowspan", rowspan);
		}
		if(width){
			td1.setAttribute("width", width);
		}
		if(innerHtml){
			td1.innerHTML = innerHtml;
		}
		if(innerText){
			var txt1 = document.createTextNode(innerText);
			td1.appendChild(txt1);
		}
		return td1;
	};
	
	this.createTr = function(){
		var tr1 = document.createElement("tr");
		return tr1;
	};
	
	this.createRedSpan = function(text){
		var span = document.createElement("span");
		var txt = document.createTextNode(text);
		span.setAttribute("style", "font-weight:bold;color:red;");
		span.appendChild(txt);
		return span;
	};
	
	this.getColAndValue = function(Q_COL, Q_DATA, i) {
		var col = Q_COL[i];
		return {
			COL : col,
			preCOL : col, 
			value : Q_DATA[col],
		};
	};
	
	this.createUPDSQLTable = function(rtnMap,TABLE_PROPERTIES_MAP) {
		var QueryTable = document.querySelector('#Q_Table');
		var SQLTable = document.querySelector('#U_Table');
		QueryTable.emptyChildren();
		SQLTable.emptyChildren();
		
		//欄位
		var Q_COL = rtnMap.Q_COL?(rtnMap.Q_COL).split("||"):'';
		var U_COL = rtnMap.U_COL?(rtnMap.U_COL).split("||"):'';
		var S_COL = rtnMap.SHOW_COL?(rtnMap.SHOW_COL).split("||"):'';
		
		//輸入值
		var Q_DATA = rtnMap.Q_DATA_MAP||{};
		var U_DATA = rtnMap.U_DATA_MAP||{};
		var S_DATA = rtnMap.S_DATA_MAP||{};
		var B_DATA = rtnMap.DATA_BAK_MAP||{};
		
		var REMARKS_MAP = TABLE_PROPERTIES_MAP.REMARKS_MAP||{};//欄位名稱
		
		//查詢條件區域
		var Q_COL_size = Q_COL.length;
		for(var i=0 ; i < Q_COL.length ; i++){
			var tr1 = this.createTr();
			if(i==0){
				var innerHtml = (TABLE_PROPERTIES_MAP['TABLE_REMARKS']||rtnMap.TBL_NAME)+'</br>('+rtnMap.TBL_NAME+')';
				var td1 = this.createTd(Q_COL_size, null, innerHtml, null);
				tr1.appendChild(td1);
			}
			
			var tmpMap = this.getColAndValue(Q_COL, Q_DATA, i)||{};
			var COL = tmpMap.COL;
			var preCOL=tmpMap.preCOL;
			var value=tmpMap.value;
			
			tr1.appendChild(this.createTd(null, "35%", null, (REMARKS_MAP[COL]||COL)+'('+COL+')'));
			tr1.appendChild(this.createTd(null, null, null, value));
			
			if(i==0){
				tr1.appendChild(this.createTd(Q_COL_size, null, null, rtnMap.USER_CNT||'0'));
			}
			QueryTable.appendChild(tr1);
		}
		
		//顯示欄位區域
		var S_COL_size = S_COL.length;
		for(var i=0 ; i < S_COL.length ; i++){
			var tr1 = this.createTr();
		
			var tmpMap = this.getColAndValue(S_COL, S_DATA, i)||{};
			var COL = tmpMap.COL;
			var preCOL = tmpMap.preCOL;
			var value = tmpMap.value;
			
			tr1.appendChild(this.createTd(null, null, null, (REMARKS_MAP[COL]||COL)+'('+COL+')', 2));
			tr1.appendChild(this.createTd(null, null, null, value, 2));
			tr1.appendChild(this.createTd(null, null, null, value, 2));
			
			if(i==0){
				tr1.appendChild(this.createTd(S_COL_size, null, null, null, 2));
			}
			SQLTable.appendChild(tr1);
		}
		
		//更新欄位區域
		var U_COL_size = U_COL.length;
		for(var i=0 ; i < U_COL.length ; i++){
			var tr1 = this.createTr();
			
			var tmpMap = this.getColAndValue(U_COL,U_DATA,i)||{};
			var COL = tmpMap.COL;
			var preCOL=tmpMap.preCOL;
			var value=tmpMap.value;
			
			tr1.appendChild(this.createTd(null, null, null, (REMARKS_MAP[COL]||COL)+'('+COL+')'));
			tr1.appendChild(this.createTd(null, null, null, B_DATA[COL]||''));
			var td1 = this.createTd(null, null, null, null);
			td1.appendChild(this.createRedSpan(value));
			tr1.appendChild(td1);
					
			if(i==0){//執行結果
				var EXEC_STS = rtnMap.EXEC_STS||'';
				if("1"==EXEC_STS){
					tr1.appendChild(this.createTd(U_COL_size, null, '執行狀態：成功！<br> (執行成功的SQL不能調整)<br>異動筆數:'+rtnMap.EXEC_CNT, null));
				}else if("2"==EXEC_STS){
					tr1.appendChild(this.createTd(U_COL_size, null, null, '執行狀態：失敗！'));
				}else if("3"==EXEC_STS){
					tr1.appendChild(this.createTd(U_COL_size, null, null, '執行狀態：成功但已回復'));
				}else{
					tr1.appendChild(this.createTd(U_COL_size, null, null, '未執行'));
				}
			}
			SQLTable.appendChild(tr1);
		}	
	};
	
	this.createDELSQLTable = function(rtnMap,TABLE_PROPERTIES_MAP){
		var QueryTable = document.querySelector('#Q_Table');
		var SQLTable = document.querySelector('#D_Table');
		QueryTable.emptyChildren();
		SQLTable.emptyChildren();
		
		//欄位
		var Q_COL = rtnMap.Q_COL?(rtnMap.Q_COL).split("||"):'';
		var S_COL = rtnMap.SHOW_COL?(rtnMap.SHOW_COL).split("||"):'';
		
		//輸入值
		var Q_DATA = rtnMap.Q_DATA_MAP||{};
		var S_DATA = rtnMap.S_DATA_MAP||{};
		
		var REMARKS_MAP = TABLE_PROPERTIES_MAP.REMARKS_MAP||{};//欄位名稱
		
		//查詢條件區域
		var Q_COL_size = Q_COL.length;
		for(var i=0;i<Q_COL.length;i++){
			var tr1 = this.createTr();
			if(i==0){
				var innerHtml = (TABLE_PROPERTIES_MAP['TABLE_REMARKS']||rtnMap.TBL_NAME)+'</br>('+rtnMap.TBL_NAME+')';
				var td1 = this.createTd(Q_COL_size, null, innerHtml, null);
				tr1.appendChild(td1);
			}
			
			var tmpMap = this.getColAndValue(Q_COL, Q_DATA, i)||{};
			var COL = tmpMap.COL;
			var preCOL=tmpMap.preCOL;
			var value=tmpMap.value;
			
			tr1.appendChild(this.createTd(null, "35%", null, (REMARKS_MAP[COL]||COL)+'('+COL+')'));
			tr1.appendChild(this.createTd(null, null, null, value));
			
			if(i==0){
				tr1.appendChild(this.createTd(Q_COL_size, null, null, rtnMap.USER_CNT||'0'));
			}
			QueryTable.appendChild(tr1);
		}
		
		//顯示欄位區域
		var S_COL_size = S_COL.length;
		for(var i=0;i<S_COL.length;i++){
			var tr1 = this.createTr();
			
			var tmpMap = this.getColAndValue(S_COL,S_DATA,i)||{};
			var COL = tmpMap.COL;
			var preCOL=tmpMap.preCOL;
			var value=tmpMap.value;
			
			tr1.appendChild(this.createTd(null, null, null, (REMARKS_MAP[COL]||COL)+'('+COL+')'));
			tr1.appendChild(this.createTd(null, null, null, value||''));
					
			if(i==0){//執行結果
				var EXEC_STS = rtnMap.EXEC_STS||'';
				if("1"==EXEC_STS){
					tr1.appendChild(this.createTd(U_COL_size, null, '執行狀態：成功！<br> (執行成功的SQL不能調整)<br>異動筆數:'+rtnMap.EXEC_CNT, null));
				}else if("2"==EXEC_STS){
					tr1.appendChild(this.createTd(U_COL_size, null, null, '執行狀態：失敗！'));
				}else if("3"==EXEC_STS){
					tr1.appendChild(this.createTd(U_COL_size, null, null, '執行狀態：成功但已回復'));
				}else{
					tr1.appendChild(this.createTd(U_COL_size, null, null, '未執行'));
				}
			}
			SQLTable.appendChild(tr1);
		}
	};
	
	this.createINSSQLTable = function(rtnMap,TABLE_PROPERTIES_MAP){
		var SQLTable = document.querySelector('#I_Table');
		SQLTable.emptyChildren();
		
		//欄位
		var I_COL = rtnMap.I_COL?(rtnMap.I_COL).split("||"):'';
		
		//輸入值
		var I_DATA = rtnMap.I_DATA_MAP||{};
		
		var REMARKS_MAP = TABLE_PROPERTIES_MAP.REMARKS_MAP||{};//欄位名稱
		var COLTYPE_MAP = TABLE_PROPERTIES_MAP.COLTYPE_MAP||{};//欄位型態
		var LENGTH_MAP = TABLE_PROPERTIES_MAP.LENGTH_MAP||{};//欄位長度
		
		document.querySelector('#I_TableName').innerHTML = TABLE_PROPERTIES_MAP['TABLE_REMARKS']+'</br>('+rtnMap.TBL_NAME+')';
		
		//更新欄位區域
		var I_COL_size = I_COL.length;
		for(var i=0;i<I_COL.length;i++){
			var tr1 = this.createTr();
			
			var tmpMap = this.getColAndValue(I_COL,I_DATA,i)||{};
			var COL = tmpMap.COL;
			var preCOL=tmpMap.preCOL;
			var value=tmpMap.value;
			
			tr1.appendChild(this.createTd(null, null, null, (REMARKS_MAP[COL]||COL)+'('+COL+')'));
			var td11 = this.createTd(null, null, null, null);
			td11.appendChild(this.createRedSpan(value));
			tr1.appendChild(td11);
			tr1.appendChild(this.createTd(null, null, null, COLTYPE_MAP[COL]||''));
			tr1.appendChild(this.createTd(null, null, null, LENGTH_MAP[COL]||''));
					
			if(i==0){//執行結果
				var EXEC_STS = rtnMap.EXEC_STS||'';
				if("1"==EXEC_STS){
					tr1.appendChild(this.createTd(U_COL_size, null, '執行狀態：成功！<br> (執行成功的SQL不能調整)<br>異動筆數:'+rtnMap.EXEC_CNT, null));
				}else if("2"==EXEC_STS){
					tr1.appendChild(this.createTd(U_COL_size, null, null, '執行狀態：失敗！'));
				}else if("3"==EXEC_STS){
					tr1.appendChild(this.createTd(U_COL_size, null, null, '執行狀態：成功但已回復'));
				}else{
					tr1.appendChild(this.createTd(U_COL_size, null, null, '未執行'));
				}
			}
			SQLTable.appendChild(tr1);
		}	
	};
}

function ZUX10202(){
	var debug;

	<%-- TablE UI物件 --%>
	var ajaxRequest = new CSRUtil.AjaxHandler.request('${dispatcher}/ZUX1_0202/');	
	var valid1;
	
	this.applyMapX112 = null;
	this.valTableControl = new ValTableControl();
	this.actionForValueTr = new ActionForValueTr();
	this.serialNoList = new SerialNoList();
	
	var validAction = {
		// 欄位檢核
		init: function(){
			valid1 = new Validation('form1', {focusOnError:true, immediate:false});
			valid1.define( "required" , { id : "RLT_TP", errMsg:'關聯表單種類為必輸欄位'} );
		},
		
		createRequired : function( type, map ){
			var valid = new Validation('form1', {focusOnError:true, immediate:false});
			Validation.addAllThese([
				['addErrorValid', '', function(v){ return false; } ]
			]);
			for(var key in map){
				switch(type){
					case "required" :
						valid.define( "required" , { id : key, errMsg: map[key] } );
						break;
					default :
						valid.define( "addErrorValid" , { id : key, errMsg: map[key]} );
						break;
				}
			}
			return valid;
		}
	};
	
	var actions = {
		closePopupWin : function(data){
			if(window.isPopupWin && window.popupWinBack){
				window.popupWinBack(data);
			}else if(window.parent && window.parent.popupWin){
				window.parent.popupWin.back(data);
			}else{
				CSRUtil.linkBack();
			}
		},
		
		btnDisable : function(mapX120){
			if(!mapX120){
				return;
			}
			var BTN_insert = document.getElementById("BTN_insert");
			if(!BTN_insert){
				return;
			}
			if('Y' == mapX120.showDBA){
				BTN_insert.style.display = "visible";
			}else{
				BTN_insert.style.display = "none";
			}
			if('Y' != mapX120.canDBA){
				BTN_insert.disabled = true;
			}else {
				BTN_insert.disabled = false;
			}
		},
		
		showReqMap : function(infoObj) {
			if(debug == undefined){
				debug = prompt('是否要開啟debug', false);
			}
			if(debug == false || debug == 'false') {
				return;
			}
			var showInfo = function(label, obj){
				var msg = "";
	            for(var ii in obj){
	            	msg += ii + " = " + obj[ii] + "\n";
	            }
	            alert("=====" + label + "=====\n" + msg);
			}
			if(Array.isArray(infoObj)){
				for(var ii = 0 ; ii < infoObj.length; ii ++){
					showInfo("arry[" + ii + "]", infoObj[ii]);
				}
			}else{
				showInfo("map", infoObj);
			}
		},
		
		applyReqMapToForm : function(reqMap){ 
			for(var id in reqMap){
            	var value = reqMap[id];
            	var element = document.getElementById(id);
            	var spanElement = document.getElementById(id + "_span");
            	if(!element){
            		element = document.getElementsByName(id);
            		if(element){
            			element = element[0];
            		}
            	}
            	if(element){
            		if(element.tagName == "span") {
            			element.innerHTML = value;
            		}else{
            			element.value = value;
            		}
            	}
            	if(spanElement){
            		spanElement.innerHTML = value;
            	}
            }
		},
		
		getreqMap : function (){
                var sendRecord = {
					"MOD_NO" : $("MOD_NO") ? $("MOD_NO").value : "",   //修正流水號
					"DIV_NO" : $("DIV_NO") ? $("DIV_NO").value : "", 
					"FORM_STS" : $("FORM_STS") ? $("FORM_STS").value : "",   //案件狀態
					"FORM_NO" : $("FORM_NO") ? $("FORM_NO").value : "",    //表單編號
					"RLT_FORMNO" : $("RLT_FORMNO") ? $("RLT_FORMNO").value : "",    //關聯表單號
					"RLT_TP" : $("RLT_TP") ? $("RLT_TP").value : "",      
					"CATG_NO" : $("CATG_NO") ? $("CATG_NO").value : "",   
					"IMPACT" : $("IMPACT") ? $("IMPACT").value : "", 
					"MEMO" : $("MEMO") ? $("MEMO").value : "",   
					"EXEC_TP" : $("EXEC_TP") ? $("EXEC_TP").value : "", 
					"EXEC_TIME" : $("EXEC_TIME") ? $("EXEC_TIME").value : "",  
					"TNS_EMPNO" : $("TNS_EMPNO") ? $("TNS_EMPNO").value : "",   
					"TNS_DIV_NO" : $("TNS_DIV_NO") ? $("TNS_DIV_NO").value : "",   
					"TNS_DT" : $("TNS_DT") ? $("TNS_DT").value : "",    
					"BUS_TP" : $("BUS_TP") ? $("BUS_TP").value : "", 
					"DB_CONN" : $("DB_CONN") ? $("DB_CONN").value : "", 
					"FORM_TP" : $("FORM_TP") ? $("FORM_TP").value : ""
                };
                actions.showReqMap(sendRecord);
                return sendRecord;
        },	
        
        doValidCheck : function(){
        	
        },
        
        doQuery0022 : function(FORM_NO, MOD_NO, SER_NO, EXEC_NO, SQL_NO, CK_SQL, Q_COLs){
			FORM_NO = document.querySelector("input[name=FORM_NO]").value;
			
			var reqMap = {
				FORM_NO : FORM_NO, 
				MOD_NO : MOD_NO, 
				SER_NO : SER_NO, 
				EXEC_NO : EXEC_NO, 
				SQL_NO : SQL_NO, 
				CK_SQL : CK_SQL,
			};
			
			$("EXEC_NO").value = EXEC_NO;
			$("MOD_NO").value = MOD_NO;
			$("FORM_NO").value = FORM_NO;
			//$("SER_NO").value = SER_NO;
			//$("SQL_NO").value = SQL_NO;
			
			ajaxRequest.post('doQuery0022', {reqMap : Object.toJSON(reqMap), Q_COLs : Object.toJSON(Q_COLs)},
				function(resp){
				
					actions.showReqMap(resp.mapX120);
					actions.applyReqMapToForm(resp.mapX120);
					
					mZUX10202.applyMapX112 = new ApplyMapX112(reqMap, resp.mapX112, resp.x125Map);
					mZUX10202.applyMapX112.apply();
					
					mZUX10202.valTableControl.creatSQL_LINK(resp.listX111);
					
					var rtnMap = resp.rtnMap||{};
					var tableMap = resp.tableMap||{};
					mZUX10202.valTableControl.controlSQLTable(rtnMap, tableMap);
				}
			);
		},
		
		doQueryByQCOL : function() {
			if(!mZUX10202.applyMapX112){
				alert('applyMapX112 undefined!!');
				return;
			}
			
			var reqMap = {
				FORM_NO : $("FORM_NO") ? $("FORM_NO").value : "",
				MOD_NO : mZUX10202.applyMapX112.reqMap.MOD_NO, 
				SER_NO : mZUX10202.applyMapX112.reqMap.SER_NO,
				EXEC_NO : mZUX10202.applyMapX112.reqMap.EXEC_NO,
			};
			
			ajaxRequest.post('doQueryByQCOL', {reqMap : Object.toJSON(reqMap), Q_COLs : Object.toJSON(mZUX10202.applyMapX112.getQColMap())},
				function(resp){
					actions.showReqMap(resp.listX111);
					actions.applyReqMapToForm(resp.listX111);
					
					actions.showReqMap(resp.mapX120);
					actions.applyReqMapToForm(resp.mapX120);
					
					mZUX10202.applyMapX112.doQueryByQCOLDone();
					
					mZUX10202.valTableControl.creatSQL_LINK(resp.listX111);
				}
			);
		},
		
		doQuerySqlDetail : function(data, CK_SQL){
			var reqMap = {
				FORM_NO : $("FORM_NO") ? $("FORM_NO").value : "",
				MOD_NO : data.MOD_NO, 
				SER_NO : data.SER_NO,
				EXEC_NO : data.EXEC_NO,
				SQL_NO : data.SQL_NO,
			};
			
			ajaxRequest.post('doQuerySqlDetail', {reqMap : Object.toJSON(reqMap), Q_COLs : Object.toJSON(mZUX10202.applyMapX112.getQColMap()), CK_SQL : CK_SQL},
				function(resp){
					var rtnMap = resp.rtnMap||{};
					var tableMap = resp.tableMap||{};
					
					mZUX10202.valTableControl.controlSQLTable(rtnMap, tableMap);
				}
			);
		},//
		
		doSaveValues_Input : function(reqMap1){
			var reqMap = {
				FORM_NO : reqMap1.FORM_NO, 
				MOD_NO : reqMap1.MOD_NO, 
				SER_NO : reqMap1.SER_NO, 
				EXEC_NO : reqMap1.EXEC_NO, 
			};
			
			ajaxRequest.post('doSaveValues_Input', 
				{	reqMap : Object.toJSON(reqMap), 
					Q_COLs : Object.toJSON(mZUX10202.applyMapX112.getQColMap()), 
					valueMap : Object.toJSON(mZUX10202.applyMapX112.getUColMap()),
				},
				function(resp){
					var rtnMap = {
						MOD_NO : resp.MOD_NO, 
						FORM_NO : resp.FORM_NO, 
						SER_NO : resp.SER_NO, 
						EXEC_NO : resp.EXEC_NO, 
					};
					
					actions.showReqMap(rtnMap);
					actions.applyReqMapToForm(rtnMap);
					
					mZUX10202.applyMapX112.updateReqMap(rtnMap);
					mZUX10202.applyMapX112.isBtn_showControl();
				
					mZUX10202.valTableControl.creatSQL_LINK(resp.listX111);
					
					var rtnMap = resp.rtnMap||{};
					var tableMap = resp.tableMap||{};
					mZUX10202.valTableControl.controlSQLTable(rtnMap, tableMap);
					
					var mapX121List = resp.mapX121List || [];
					mZUX10202.serialNoList.initSelection(rtnMap.MOD_NO, rtnMap.SER_NO, mapX121List);
				}
			);
		},//
		
		doSaveValues_Update : function(reqMap1){
			var reqMap = {
				FORM_NO : reqMap1.FORM_NO, 
				MOD_NO : reqMap1.MOD_NO, 
				SER_NO : reqMap1.SER_NO, 
				EXEC_NO : reqMap1.EXEC_NO, 
			};
			
			ajaxRequest.post('doSaveValues_Update', 
				{	reqMap : Object.toJSON(reqMap), 
					Q_COLs : Object.toJSON(mZUX10202.applyMapX112.getQColMap()), 
					valueMap : Object.toJSON(mZUX10202.applyMapX112.getUColMap()),
				},
				function(resp){
					var rtnMap = {
						MOD_NO : resp.MOD_NO, 
						FORM_NO : resp.FORM_NO, 
						SER_NO : resp.SER_NO, 
						EXEC_NO : resp.EXEC_NO, 
					};
					
					actions.showReqMap(rtnMap);
					actions.applyReqMapToForm(rtnMap);
					
					mZUX10202.applyMapX112.updateReqMap(rtnMap);
					mZUX10202.applyMapX112.isBtn_showControl();
				
					mZUX10202.valTableControl.creatSQL_LINK(resp.listX111);
					
					var rtnMap = resp.rtnMap||{};
					var tableMap = resp.tableMap||{};
					mZUX10202.valTableControl.controlSQLTable(rtnMap, tableMap);
					
					var mapX121List = resp.mapX121List || [];
					mZUX10202.serialNoList.initSelection(rtnMap.MOD_NO, rtnMap.SER_NO, mapX121List);
				}
			);
		},//
		
		doSaveValues_Delete : function(reqMap1){
			var reqMap = {
				FORM_NO : reqMap1.FORM_NO, 
				MOD_NO : reqMap1.MOD_NO, 
				SER_NO : reqMap1.SER_NO, 
				EXEC_NO : reqMap1.EXEC_NO, 
			};
			
			ajaxRequest.post('doSaveValues_Delete', 
				{	reqMap : Object.toJSON(reqMap), 
					Q_COLs : Object.toJSON(mZUX10202.applyMapX112.getQColMap()), 
					valueMap : Object.toJSON(mZUX10202.applyMapX112.getUColMap()),
				},
				function(resp){
					var rtnMap = {
						MOD_NO : resp.MOD_NO, 
						FORM_NO : resp.FORM_NO, 
						SER_NO : resp.SER_NO, 
						EXEC_NO : resp.EXEC_NO, 
					};
					
					actions.showReqMap(rtnMap);
					actions.applyReqMapToForm(rtnMap);
					
					mZUX10202.applyMapX112.updateReqMap(rtnMap);
					mZUX10202.applyMapX112.isBtn_showControl();
				
					mZUX10202.valTableControl.creatSQL_LINK(resp.listX111);
					
					var rtnMap = resp.rtnMap||{};
					var tableMap = resp.tableMap||{};
					mZUX10202.valTableControl.controlSQLTable(rtnMap, tableMap);
					
					var mapX121List = resp.mapX121List || [];
					mZUX10202.serialNoList.initSelection(rtnMap.MOD_NO, rtnMap.SER_NO, mapX121List);
				}
			);
		},//
	};
	this.actions = actions;
	
	var buttons = {
		BTN_XXXXXXXXXXX : function() {
			if(!valid1.validate()){
				return;
			}
			
			if( $("RLT_TP").value != '4' ){ 
				var v = validAction.createRequired({"RLT_FORMNO" : "關聯表單號不可空白"});
				if(!v.validate()){
					return;
				} 
			}
			
			ajaxRequest.post('doMemo', {reqMap : Object.toJSON($('form1').serialize(true))},
				function(resp){
					//            修正說明    MEMO    contentMap.CONTENT  輸入文字框，限制200 字
					var memo = document.getElementById("MEMO");
					var MEMO_span = document.getElementById("MEMO_span");
					memo.value = resp.mapX120.MEMO;
					MEMO_span.innerHTML = resp.mapX120.MEMO;
					
					$(memo).simulate('keyup');
					
					//            原因分類    CATG_NO contentMap.CATG_NO  若contentMap.CATG_NO有值
					var catg_no = document.getElementById("CATG_NO");
					catg_no.value = resp.mapX120.CATG_NO;
				}
			);
		} ,
	};
	this.buttons = buttons;
		
	this.initApp = function() {
					
		PageUI.createPageWithAllBodySubElement(
			'ZUX10202',
			'資料修正平台',
			'資料修正作業'
		);
		
		var rtnMap = <c:out value='${mapX120JSON}' default='{}' escapeXml='false'/>;  // rtnMap物件
		actions.btnDisable(rtnMap);
		actions.applyReqMapToForm(rtnMap);
		
		var mapX121List = <c:out value='${mapX121ListJSON}' default='{}' escapeXml='false'/>;  // rtnMap物件
		mZUX10202.serialNoList.initSelection(undefined, undefined, mapX121List);
		
		validAction.init();
		
		$("XXXXXXXXXX") &&  $("XXXXXXXXX").observe('click', buttons.BTN_XXXXXXXXXXX); 
		
		PageUI.resize();
		displayMessage();
	}
}

</script>
</head>
<body>

<form name="form1" id="form1"
	style="PADDING-RIGHT: 0px; PADDING-LEFT: 0px; PADDING-BOTTOM: 0px; MARGIN: 0px; PADDING-TOP: 0px">

<input type="text" name="FORM_NO" value="${mapX120.FORM_NO}"
	title="FORM_NO" /> <input type="text" name="MOD_NO"
	value="${mapX120.MOD_NO}" title="MOD_NO" /> <input type="text"
	name="EXEC_NO" value="" title="EXEC_NO" /> <input type="text"
	name="SER_NO" value="" title="SER_NO" /> 
<%--↓↓↓↓↓↓↓↓ 資料修正作業  ↓↓↓↓↓↓↓↓--%>
<TABLE width=100% border=0 cellpadding="0" cellspacing="1"
	class="tbBox2" align="center" id="SER_NO_LIST_Main">
	<thead>
		<c:forEach var="mapX111" items="${listX111}" varStatus="idx">
			<tr>
				<td width="20%" class="tbBlue" style="white-space: nowrap"
					align="left">${mapX111.SER_NO}.${mapX111.DF_MEMO} <input
					type="hidden" value="${mapX111.MOD_NO}"
					id="mapX111_MOD_NO_${idx.index}" alt="MOD_NO" /> <input
					type="hidden" value="${mapX111.SER_NO}"
					id="mapX111_SER_NO_${idx.index}" alt="SER_NO" /> <input
					type="hidden" value="${mapX111.EXEC_NO}"
					id="mapX111_EXEC_NO_${idx.index}" alt="EXEC_NO" /> <input
					type="hidden" value="${mapX111.SQL_NO}"
					id="mapX111_SQL_NO_${idx.index}" alt="SQL_NO" /> <input
					type="hidden" value="${mapX111.Q_COLs}"
					id="mapX111_Q_COLs_${idx.index}" alt="Q_COLs" /> <a
					href="javascript:mZUX10202.actions.doQuery0022('${mapX111.FORM_NO}', 
						'${mapX111.MOD_NO}', '${mapX111.SER_NO}', '${mapX111.EXEC_NO}', '${mapX111.SQL_NO}', '${mapX111.CK_SQL}', '${mapX111.Q_COLs}')">
				&nbsp;&nbsp;<font color="red"> ＋ </font>&nbsp;&nbsp; </a> <span
					name="SER_NO_LIST" MOD_NO="${mapX111.MOD_NO}"
					SER_NO="${mapX111.SER_NO}"></span></td>
			</tr>
		</c:forEach>
	</thead>
</table>

<TABLE width=100% border=0 cellpadding="0" cellspacing="1"
	class="tbBox2" align="center" id="XXXXXXXXX">
	<thead>
		<tr>
			<td width="20%" class="tbBlue" style="white-space: nowrap"
				align="left">修正序號:<span id="EXEC_NO_span"></span></td>
			<td width="80%" class="tbBlue" style="white-space: nowrap"
				align="left">修正說明:</td>
		</tr>

		<tr>
			<td width="100%" class="tbBlue" style="white-space: nowrap"
				align="left" colspan="10">
			<TABLE width=100% border=0 cellpadding="0" cellspacing="1"
				class="tbBox2" align="center">
				<tr>
					<td width="80%" class="tbBlue" style="white-space: nowrap"
						align="center" colspan="2">查詢欄位</td>
					<td width="20%" class="tbBlue" style="white-space: nowrap"
						align="left">操作</td>
				</tr>
				<tbody id="Q_COLs_tbody"></tbody>
			</TABLE>
			</td>
		</tr>

		<tr>
			<td width="100%" class="tbBlue" style="white-space: nowrap"
				align="left" colspan="10">
			<TABLE width=100% border=0 cellpadding="0" cellspacing="1"
				class="tbBox2" align="center">
				<tr>
					<td width="80%" class="tbBlue" style="white-space: nowrap"
						align="center" colspan="2">更新欄位</td>
					<td width="20%" class="tbBlue" style="white-space: nowrap"
						align="left">操作</td>
				</tr>
				<tbody id="U_COLs_tbody"></tbody>
			</TABLE>
			</td>
		</tr>

		<tr>
			<td width="20%" class="tbBlue" style="white-space: nowrap"
				align="left">修正語法</td>
			<td width="80%" class="tbBlue" style="white-space: nowrap"
				align="left">點選可預覽修正前後資料及執行結果</td>
		</tr>
		<tr>
			<td class="tbBlue" colspan="3"><span id="SQL_LINK"></span></td>
		</tr>

		<tr>
			<td width="100%" class="tbBlue" style="white-space: nowrap"
				align="left" colspan="10">
			<TABLE width=100% border=0 cellpadding="0" cellspacing="1"
				class="tbBox2" align="center" id="Q_AREA" style="display: none;">
				<tr>
					<td width="20%" class="tbBlue" style="white-space: nowrap"
						align="center">表名</td>
					<td width="70%" class="tbBlue" style="white-space: nowrap"
						align="center" colspan="2">查詢條件</td>
					<td width="10%" class="tbBlue" style="white-space: nowrap"
						align="center">筆數</td>
				</tr>
				<tbody id="Q_Table"></tbody>
			</TABLE>
			</td>
		</tr>

		<tr>
			<td width="100%" class="tbBlue" style="white-space: nowrap"
				align="left" colspan="10">
			<TABLE width=100% border=0 cellpadding="0" cellspacing="1"
				class="tbBox2" align="center" id="U_AREA" style="display: none;">
				<tr>
					<td width="25%" class="tbBlue" style="white-space: nowrap"
						align="center">修正的項目</td>
					<td width="25%" class="tbBlue" style="white-space: nowrap"
						align="center">修正前</td>
					<td width="25%" class="tbBlue" style="white-space: nowrap"
						align="center">修正後</td>
					<td width="25%" class="tbBlue" style="white-space: nowrap"
						align="center">執行狀況</td>
				</tr>
				<tbody id="U_Table"></tbody>
			</TABLE>
			</td>
		</tr>

		<tr>
			<td width="100%" class="tbBlue" style="white-space: nowrap"
				align="left" colspan="10">
			<TABLE width=100% border=0 cellpadding="0" cellspacing="1"
				class="tbBox2" align="center" id="D_AREA" style="display: none;">
				<tr>
					<td width="20%" class="tbBlue" style="white-space: nowrap"
						align="center">修正的項目</td>
					<td width="70%" class="tbBlue" style="white-space: nowrap"
						align="center">修正前</td>
					<td width="10%" class="tbBlue" style="white-space: nowrap"
						align="center">執行狀況</td>
				</tr>
				<tbody id="D_Table"></tbody>
			</TABLE>
			</td>
		</tr>

		<tr>
			<td width="100%" class="tbBlue" style="white-space: nowrap"
				align="left" colspan="10">
			<TABLE width=100% border=0 cellpadding="0" cellspacing="1"
				class="tbBox2" align="center" id="I_AREA" style="display: none;">
				<tr>
					<td width="100%" class="tbBlue" style="white-space: nowrap"
						align="center" colspan="10">XXX設定檔</td>
				</tr>
				<tr>
					<td width="100%" class="tbBlue" style="white-space: nowrap"
						align="center" colspan="10">XXXXTABLE</td>
				</tr>
				<tr>
					<td width="25%" class="tbBlue" style="white-space: nowrap"
						align="center">字段名稱</td>
					<td width="25%" class="tbBlue" style="white-space: nowrap"
						align="center">填寫需要新增的資料</td>
					<td width="25%" class="tbBlue" style="white-space: nowrap"
						align="center">資料型態</td>
					<td width="25%" class="tbBlue" style="white-space: nowrap"
						align="center">長度</td>
					<td width="25%" class="tbBlue" style="white-space: nowrap"
						align="center">執行狀況</td>
				</tr>
				<tbody id="I_Table"></tbody>
			</TABLE>
			</td>
		</tr>
	</thead>
</table>

<%--↑↑↑↑↑↑↑↑ 資料修正作業  ↑↑↑↑↑↑↑↑--%></form>
</body>
</html>