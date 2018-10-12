<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>
<%@ page language='java' contentType='text/html; charset=BIG5'%>

<!--
程式：XXZX0200.jsp
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
<script type='text/javascript'>

<%-- 產生畫面物件 --%>
<%--透過 prototype 的 Event 物件監聽 onload 事件，觸發時進行 initApp()--%>
Event.observe(window, 'load', new XXZX0200().initApp);

function XXZX0200(){

	<%-- TablE UI物件 --%>
	var grid;		
	var ajaxRequest = new CSRUtil.AjaxHandler.request('${dispatcher}/XXZX_0200/');	
	
	var actions = {
		closePopupWin : function(data){
			if(window.isPopupWin && window.popupWinBack){
				window.popupWinBack(data);
			}else if(window.parent && window.parent.popupWin){
				window.parent.popupWin.back(data);
			}else{
				CSRUtil.linkBack();
			}
		}
	};
	
	var buttons = {
		<%--查詢--%>
		doQuery : function() {
			// 發送 Ajax 請求
			ajaxRequest.post('query', {},
				function(resp){
					grid.load(resp.rtnList);
				}
			);
		},
		<%--確認--%>
		doConfirm : function(){
			
			var checkedRec = grid.getCheckedRecord(["DEP_NM","DEP_CODE"]);
			if(checkedRec.length == 0){
				alert("請選取一筆資料");
				return;
			}
			checkedRec = checkedRec[0];
			
			// 關閉popupwin並回傳 DEP_CODE
			actions.closePopupWin(checkedRec);
		},
		<%--取消--%>
		doCancel : function(){
			actions.closePopupWin();
		}
	};
	
		
	return {
	
		initApp : function() {
			
			PageUI.createPageWithAllBodySubElement(
				'XXZX0200',
				'程式開發訓練',
				'單位代號索引'
			);
			
			grid = new TableUI({
			 	table: $("grid"), 
			 	autoCheckBox:{
			 		type:'radio',
			 		text:'選取'
				},
				column:[
					{header:"部門名稱", key:'DEP_NM'},
					{header:"部門代號", key:'DEP_CODE'}
				]
			});
			
			buttons.doQuery();
			$('btn_confirm').observe('click',buttons.doConfirm);
			$('btn_cancel').observe('click',buttons.doCancel);
			
			PageUI.resize();
			displayMessage();
		}
	};	
}
	
</script>
</head>
<body>
	<table id="grid" class="grid"></table>
	<div align="center">
		<button id="btn_confirm" type="button" class="button">確定</button>
		<button id="btn_cancel" type="button" class="button">取消</button>
	</div>
</body>
</html>