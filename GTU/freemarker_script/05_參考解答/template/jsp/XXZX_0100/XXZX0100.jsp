<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>
<%@ page language='java' contentType='text/html; charset=BIG5'%>

<!--
程式：XXZX0100.jsp
功能：基本EBAF練習
作者：
完成：
-->

<html>
<head>
<%@ include file='/html/CM/header.jsp'%>
<%@ include file='/html/CM/msgDisplayer.jsp'%>
<%@ taglib prefix='im' tagdir='/WEB-INF/tags/im'%>
<!--匯入外部Javascript 與 css-->

<title></title>
<link href='${cssBase}/cm.css' rel='stylesheet' type='text/css'>
<script type='text/JavaScript' src='${htmlBase}/CM/js/ajax/prototype.js'></script>
<!-- 必要 -->
<script type='text/JavaScript' src='${htmlBase}/CM/js/ajax/CSRUtil.js'></script>
<!-- 必要 -->
<script type='text/JavaScript' src='${htmlBase}/CM/js/ui/TableUI.js'></script>
<!-- 目前系統常用的表格工具 -->
<script type='text/JavaScript' src='${htmlBase}/CM/js/ui/PageUI.js'></script>
<script type='text/javaScript' src='${htmlBase}/CM/js/ui/LocaleDisplay.js'></script>
<script type="text/JavaScript" src="${htmlBase}/CM/js/RPTUtil.js"></script>
<script type='text/javascript' src='${htmlBase}/CM/js/ui/commView.js'></script>
<script type="text/JavaScript" src="${htmlBase}/CM/js/calendar.js"></script>
<script type="text/javascript" src="<%=htmlBase%>/CM/js/Validator.js"></script>
<script type="text/javascript" src="<%=htmlBase%>/CM/js/ui/core.js"></script>
<script type="text/javascript" src="<%=htmlBase%>/CM/js/jsonUtil.js"></script>
<script type="text/javascript" src="<%=htmlBase%>/CM/js/utility.js"></script>
<script type="text/javascript" src="<%=htmlBase%>/CM/js/ui/popupWin.js"></script>
<script type='text/javascript'>

<%-- 產生畫面物件 --%>
<%--透過 prototype 的 Event 物件監聽 onload 事件，觸發時進行 initApp()--%>
Event.observe(window, 'load', new LSM01000().initApp);

function LSM01000(){
//LSM0_0900 >> 單獨打勾((僅顯示檢核異常件,匯出,選取

	<%-- TablE UI物件 --%>
	var valid1;
	var grid;
	var ajaxRequest = new CSRUtil.AjaxHandler.request('${dispatcher}/XXZX_0100/');	

	<%--按鈕授權--%>
	function buttonAuth() {
	
    }
	

	<%--畫面檢核--%>
	function initValidation(){
		
	}
	<%--日期檢核--%>
	function defineValid(isQuery){		   
		
	}
	
	
	<%-- 存取畫面--%>
	function getreqMap(){

	};



	<%--功能按鈕--%>
	var buttons = {

		<%-- 查詢 --%>
		doQuery : function(){
			
		},		
		<%--發送國內股票市場別更動訊息通知--%>	
		 doSendMail : function(){		   	    
            
		},

		<%--匯入--%>					
		doImport : function() {
			
		},
		<%-- 代號索引 --%>
		doIndex:function(){
			
		},
		
		<%-- 清除 --%>
		doClear : function(){

		}
		

	};	
	function changeSNO_TYPE(event){
		var radio = event.element();
		$('SNO').disabled= (radio.value!=1) ;
	};
	function changeMKT_CODE_TYPE(event){
			var radio = event.element();
	
		// 市場別 MKT_CODE_TYPE 1.預設為全部市場別
		//               2.選取單一市場別時，enable「市場別」下拉選單
		//               3.選取投資組合多選時，open「市場別」多選視窗
			if(radio.value==0) {
		
			// 2.4.1 投資組合選全部時，PFL_ID =「投資組合」下拉選單的所有選項(逗點隔開)	
			var selectValue = '';	
			
			for(i=0;i<$('MKT_CODE').options.length;i++) {
			    if($('MKT_CODE').options[i].value!='') {
					selectValue +=  $('MKT_CODE').options[i].value + ",";	
				}
			}
			if(selectValue.length > 0){
				selectValue = selectValue.substring(0, selectValue.length - 1);
			}	
		    $('theMKT_CODE_List').value=selectValue;
		    $('theMKT_CODE_List_TEXT').value=selectValue;		
		    
		    
		} else if(radio.value==1) {
		} else if(radio.value==2) {	
			
			//開啟資料多選視窗
			popupWin.popup({
				src: '<%=dispatcher%>/LSM0_1000/promptPFL',
				width:600,       	// 寬 default 800px
				height:210 ,		// 高 default 500px
				left:30,			// 左邊邊距 default 30px
				top:30,			// 上邊邊距 default 50px
				scroll:'yes',
				cb:function(obj){
					    var selectValue = '';	
						for(var i=0; i<obj.length; i++){
							var m = obj[i];
							//alert( m['DEFAULT_NAME'] );
							//alert( m['DEFAULT_VALUE']);	
							selectValue = selectValue + m['DEFAULT_VALUE'] + ",";	
						}
						if(selectValue.length > 0){
							selectValue = selectValue.substring(0, selectValue.length - 1);
						}						
 					    $('theMKT_CODE_List').value=selectValue;
 					    $('theMKT_CODE_List_TEXT').value=selectValue;
				}
			}); 
		};
	
		$('MKT_CODE').disabled= (radio.value!=1) ;
	    $('theMKT_CODE_List_TEXT').disabled = (radio.value!=0 && radio.value!=2) ;
	};
	
	return {			
		initApp : function() {			
			PageUI.createPageWithAllBodySubElement("LSM01000","檢核作業","股票收盤價檢核");
			<cathay:LocaleDisplay sysCode="LS" dateFields = "SYS_DT_STR SYS_DT_END"  var="localeDisplay"/>
			displayMessage();
			grid = new TableUI({
				table :$('grid'),
				autoCheckBox:{isSelectAll:true},
				validateRecord: function(rec, sn){ return rec.PRICE_CHECK_RESULT == 'N'; },
            	split:['ACC_CLOSE_PRICE'],
				pageSize :10,
            	whenDataNull:"",
				column:[	
					{header: "庫存日期" , key:'BAL_DATE', sortable:true, sortRule:'date', attrs:{rowSpan:2}}  
					,{header: "版本" , key:'BAL_TYPE_NAME', attrs:{rowSpan:2}}
					,{header: "版本代碼" , key:'BAL_TYPE', attrs:{rowSpan:2}}
					,{header: "股票代號(外碼)" , key:'STK_NO', sortable:true, attrs:{rowSpan:2}}
					,{header: "股票代號(內碼)" , key:'SNO', attrs:{rowSpan:2}}
					,{header: "股票名稱" , key:'STK_SNAME', sortable:true, attrs:{rowSpan:2}}
					,{header: "市場別" , key:'MKT_CODE', sortable:true, attrs:{rowSpan:2}}
					,{header: "市場別代碼" , key:'MKT_CODE_NAME', attrs:{rowSpan:2}}
					,{header: "檢核結果" , key:'PRICE_CHECK_RESULT', sortable:true, attrs:{rowSpan:2}}
					,{header: "公司庫存" , attrs:{colSpan:2} , empty:'rowSpan_down'}
					,{header: "NAV庫存" , attrs:{colSpan:2} , empty:'rowSpan_down'}
					,{header: "寶碩庫存" , attrs:{colSpan:2} , empty:'rowSpan_down'}　
					,{header: "Cmoney" , attrs:{colSpan:2} , empty:'rowSpan_down'}
					,{header: "內部評價" , attrs:{colSpan:2} , empty:'rowSpan_down'}
					,{header: "備註" , key:'REMARK', attrs:{rowSpan:2}}
					,{header: "異動人員ID" , key:'LST_PROC_ID', attrs:{rowSpan:2}}
					,{header: "異動人員姓名" , key:'LST_PROC_NAME', attrs:{rowSpan:2}}
					,{header: "異動時間" , key:'LST_PROC_DATE', attrs:{rowSpan:2}}
					,{header: "系統別" , key:'SYS_NO', attrs:{rowSpan:2}} 
					
					,{header: "收盤價" , key:'ACC_CLOSE_PRICE', sortable:true }
					,{header: "股價日期" , key:'ACC_MP_DATE', sortRule:'date', sortable:true }
					,{header: "收盤價" , key:'PFL_CLOSE_PRICE', sortable:true }
					,{header: "股價日期" , key:'PFL_MP_DATE', sortRule:'date', sortable:true }
					,{header: "收盤價" , key:'SRC3_CLOSE_PRICE', sortable:true }
					,{header: "股價日期" , key:'SRC3_MP_DATE', sortRule:'date', sortable:true }
					,{header: "收盤價" , key:'CMONEY_CLOSE_PRICE', sortable:true }
					,{header: "股價日期" , key:'CMONEY_MP_DATE', sortRule:'date', sortable:true }
					,{header: "收盤價" , key:'SRC7_CLOSE_PRICE', sortable:true }
					,{header: "股價日期" , key:'SRC7_MP_DATE', sortRule:'date', sortable:true }
				]}
			);
			initValidation();
			buttonAuth();    <%-- 按鈕權限 --%>
			

			$('btn_query').observe('click', buttons.doQuery);<%-- 查詢 --%>
			$$('input[name="SNO_TYPE"]').each(function(rad){
				rad.observe('click', changeSNO_TYPE);
			});	
			$$('input[name="MKT_CODE_TYPE"]').each(function(rad){
				rad.observe('click', changeMKT_CODE_TYPE);
			});
			
			
						
			<%--上一頁回來重查--%>
			if(CSRUtil.isBackLink('form1')){
				buttons.doQuery();		
			}
									
			PageUI.resize();
			displayMessage();	
		}
	};	
}
	
</script>
</head>
<body>
<form name='form1' id='form1' style='PADDING-RIGHT: 0px; PADDING-LEFT: 0px; PADDING-BOTTOM: 0px; MARGIN: 0px; PADDING-TOP: 0px'>
<table width='100%' border='0' cellpadding='0' cellspacing='1' class='tbBox2' id="table1">
	<tr>
		<td class='tbYellow' width="20%">庫存日期</td>
		<td class='tbYellow2'>
			<input id="SYS_DT_STR" name="SYS_DT_STR" type="text" class="textBox2" value="${SYS_DATE}" /> ~ 
			<input id="SYS_DT_END" name="SYS_DT_END" type="text" class="textBox2" value="${SYS_DATE}" />　　　　　　
			<label><input type="checkbox" name="IS_CHECK" id="IS_CHECK" value="1" checked /> 僅顯示檢核異常件</label>	
		</td>
		<td class="tbYellow2" align="center" rowspan='4' width="20%">
				<button class="button" id="BTN_QUERY" >查詢</button> &nbsp;
				<button class="button" id="BTN_CLEAR" >清除</button>
		</td>
	</tr>
	<tr>
		<td class="tbYellow">庫存版本</td>
		<td class="tbYellow2" >
			<select id="BAL_TYPE" name="BAL_TYPE">
				<option value="0" >日結</option>
				<c:forEach var="map" items="${BAL_TYPE_List}" >
					<option value = "${map.CODE}" >${map.CODE}</option>
				</c:forEach>
			</select>		
		</td>
	</tr>
	<tr>
		<td class="tbYellow">
			<input type="radio" name="SNO_TYPE" id="SNO_TYPE_0" value="0" checked />全部股票
			<input type="radio" name="SNO_TYPE" id="SNO_TYPE_1" value="1" />單一股票
		</td>
		<td class='tbYellow2''>
			<select id="SNO" style="width:160px" class="textBox2" disabled>
				<option value="">全部</option>
				<c:forEach var="map" items="${theSTK_List}">
					<option value = "${map.SNO}">${map.STK_NO} ${map.STK_SNAME}</option>
				</c:forEach>
			</select>
		</td>
	</tr>
	<tr>
		<td width="10%" class="tbYellow">
		    <input type="radio" name="MKT_CODE_TYPE" id="MKT_CODE_TYPE_0"  value="0" checked>全部市場別
		    <input type="radio" name="MKT_CODE_TYPE" id="MKT_CODE_TYPE_1"  value="1">單一市場別
		    <input type="radio" name="MKT_CODE_TYPE" id="MKT_CODE_TYPE_2"  value="2">市場別多選
		</td>	
		<td class="tbYellow2">		
			<select id="MKT_CODE" name="MKT_CODE" class="textBox2" style="width:160px" disabled>
				<option value="">全部</option>
				<c:forEach  var="map" items="${MKT_CODE_List}">
					<option value="<c:out value="${map.CODE}" />" ><c:out value="${map.CODE}" /></option>
				</c:forEach>
			</select> &nbsp;			    
			<input type="text" name="theMKT_CODE_List_TEXT" id="theMKT_CODE_List_TEXT" value="" readOnly class="textBox2" size="50" disabled>
			<input type="text" id="theMKT_CODE_List" />
		</td>	
	</tr>
	<tr>
		<td align="center" class="tbYellow2" colspan='3'>
			<button id="BTN_REMARK" class="button" >備註維護</button> &nbsp;	
			<button id="BTN_EXPORT" class="button" >匯出</button>
		</td>
	</tr>
</table>
</form>
<table id='grid'></table>
</body>
</html>