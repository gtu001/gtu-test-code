<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>
<%@ page language='java' contentType='text/html; charset=BIG5'%>

<!--
�{���GXXZX0200.jsp
�\��G��EBAF�m��
�@�̡G
�����G
-->

<html>
<head>
<%@ include file='/html/CM/header.jsp'%>
<%@ include file='/html/CM/msgDisplayer.jsp'%>
<%@ taglib prefix='im' tagdir='/WEB-INF/tags/im'%>
<!--�פJ�~��Javascript �P css-->

<title></title>
<link href='${cssBase}/cm.css' rel='stylesheet' type='text/css'>

<script type='text/JavaScript' src='${htmlBase}/CM/js/ajax/prototype.js'></script>
<!-- ���n -->
<script type='text/JavaScript' src='${htmlBase}/CM/js/ajax/CSRUtil.js'></script>
<!-- ���n -->
<script type='text/JavaScript' src='${htmlBase}/CM/js/ui/TableUI.js'></script>
<!-- �ثe�t�α`�Ϊ����u�� -->
<script type='text/JavaScript' src='${htmlBase}/CM/js/ui/PageUI.js'></script>
<script type='text/javaScript' src='${htmlBase}/CM/js/ui/LocaleDisplay.js'></script>
<script type='text/javascript'>

<%-- ���͵e������ --%>
<%--�z�L prototype �� Event �����ť onload �ƥ�AĲ�o�ɶi�� initApp()--%>
Event.observe(window, 'load', new XXZX0200().initApp);

function XXZX0200(){

	<%-- TablE UI���� --%>
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
		<%--�d��--%>
		doQuery : function() {
			// �o�e Ajax �ШD
			ajaxRequest.post('query', {},
				function(resp){
					grid.load(resp.rtnList);
				}
			);
		},
		<%--�T�{--%>
		doConfirm : function(){
			
			var checkedRec = grid.getCheckedRecord(["DEP_NM","DEP_CODE"]);
			if(checkedRec.length == 0){
				alert("�п���@�����");
				return;
			}
			checkedRec = checkedRec[0];
			
			// ����popupwin�æ^�� DEP_CODE
			actions.closePopupWin(checkedRec);
		},
		<%--����--%>
		doCancel : function(){
			actions.closePopupWin();
		}
	};
	
		
	return {
	
		initApp : function() {
			
			PageUI.createPageWithAllBodySubElement(
				'XXZX0200',
				'�{���}�o�V�m',
				'���N������'
			);
			
			grid = new TableUI({
			 	table: $("grid"), 
			 	autoCheckBox:{
			 		type:'radio',
			 		text:'���'
				},
				column:[
					{header:"�����W��", key:'DEP_NM'},
					{header:"�����N��", key:'DEP_CODE'}
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
		<button id="btn_confirm" type="button" class="button">�T�w</button>
		<button id="btn_cancel" type="button" class="button">����</button>
	</div>
</body>
</html>