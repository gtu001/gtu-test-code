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
<%@ taglib prefix='im' tagdir='/WEB-INF/tags/im'%>
<!--匯入外部Javascript 與 css-->

<title></title>
<link href='${r"${cssBase}"}/cm.css' rel='stylesheet' type='text/css'>

<script type='text/JavaScript' src='${r"${htmlBase}"}/CM/js/ajax/prototype.js'></script>
<!-- 必要 -->
<script type='text/JavaScript' src='${r"${htmlBase}"}/CM/js/ajax/CSRUtil.js'></script>
<!-- 必要 -->
<script type='text/JavaScript' src='${r"${htmlBase}"}/CM/js/ui/TableUI.js'></script>
<!-- 目前系統常用的表格工具 -->
<script type='text/JavaScript' src='${r"${htmlBase}"}/CM/js/ui/PageUI.js'></script>
<script type="text/JavaScript" src="${r"${htmlBase}"}/CM/js/calendar.js"></script>
<script type="text/javascript" src="${r"${htmlBase}"}/CM/js/ui/validation.js"></script>
<script type="text/javascript" src="${r"${htmlBase}"}/CM/js/ui/PageUI.js"></script>
<script type="text/JavaScript" src="${r"${htmlBase}"}/CM/js/calendar.js"></script>
<script type="text/javascript" src="${r"${htmlBase}"}/CM/js/ui/popupWin.js"></script>
<script type="text/javascript" src="${r"${htmlBase}"}/CM/js/ui/commView.js"></script>
<script type="text/JavaScript"
	src="${r"${htmlBase}"}/CM/js/ui/LocaleDisplay.js"></script>
<script type="text/JavaScript"
	src="${r"${htmlBase}"}/CM/js/ui/InputUtility.js"></script>
<script type="text/javascript" src="${r"${htmlBase}"}/CM/js/RPTUtil.js"></script>
<script type='text/javascript'>


<%-- 產生畫面物件 --%>
<%--透過 prototype 的 Event 物件監聽 onload 事件，觸發時進行 initApp()--%>
Event.observe(window, 'load', new ${edit_jsp}().initApp);

function ${edit_jsp}(){


	<%-- TablE UI物件 --%>
	var ajaxRequest = new CSRUtil.AjaxHandler.request('${r"${dispatcher}/${edit_action_clz}/"}');	
	var valid1;
	
	var validAction = {
		// 欄位檢核
		init: function(){
			valid1 = new Validation('form1', {focusOnError:true, immediate:false});
			<#list insertColumns_pk as col>
			valid1.define( "required" , { id : "${col}", errMsg:'${columnLabel[col]}不得為空'} );
			</#list>
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
		
		// 根據OP_STATUS讓指定按鈕失效
		btnDisable : function(reqMap){
			$w('BTN_confirm  BTN_insert BTN_update BTN_approve BTN_reject BTN_delete  ').each(  //BTN_back
				function(id) { 
					$(id).disable();
				}
			);
			
			var ACTION_TYPE = reqMap.ACTION_TYPE;
			
			if (ACTION_TYPE == "I"){ // 新增時
				<#list insertColumns as col>
				$('${col}').update(reqMap.${col} || '');
				</#list>
			
				$('BTN_insert').enable();
				
			} else { // 修改時, ACTION_TYPE=U
				<#list updateColumns as col>
				$('${col}').setValue(reqMap.${col} || '');
				</#list>
				
				if(reqMap.OP_STATUS == "10"){ // OP_STATUS 為10 審核、退回disable 其餘enable
					$w('BTN_confirm  BTN_insert BTN_update BTN_approve BTN_delete').each( 
						function(id) { 
							$(id).enable();
						}
					);
				}else if(reqMap.OP_STATUS == "20"){ // OP_STATUS 為20 提交disable 其餘enable
					$w('BTN_confirm  BTN_insert BTN_update BTN_reject BTN_delete').each( 
						function(id) { 
							$(id).enable();
						}
					);
				}else{
					$('BTN_update').enable();
				}
			}
		},
		
		getreqMap:function (){
                var sendRecord = {
                <#list insertColumns as col>
				<#if col?is_last>
					${col} : $('${col}') != null ? $('${col}').value : ""
				<#else>
					${col} : $('${col}') != null ? $('${col}').value : "",
				</#if>
                </#list>
                }
                return sendRecord;
        },	
	};
	
	var buttons = {
		<%--索引(單位代號)--%>
		doIndex_DEP : function(){
			popupWin.popup({									
				src : '${r"${dispatcher}"}/XXZX_0200/prompt' ,		
				scrolling : 'yes',	
				height : '520',
				cb : function(obj){ // 將 XXZX_0200所選取之 DEP_CODE代入畫面DIV_NO , DEP_NM顯示在DIV_NO旁
					$('DIV_NO').setValue(obj.DEP_CODE||'');// 單位代號
					$('DEP_NM').update(obj.DEP_NM||'');// 單位名稱
				}		
			});			
		},
	
		<%--新增--%>
		doInsert : function(){
			if(!valid1.validate()){
				return; 
			}
			// 發送 Ajax 請求
			ajaxRequest.post('insert', {reqMap : Object.toJSON(actions.getreqMap())},
				function(resp){
					actions.btnDisable(resp.rtnMap||{});
				}
			);
		},
		
		<%--修改--%>
		doUpdate : function(){
			if(!valid1.validate()){
				return; 
			}
			// 發送 Ajax 請求
			ajaxRequest.post('update', {reqMap : Object.toJSON(actions.getreqMap())},
				function(resp){
					actions.btnDisable(resp.rtnMap||{});
				}
			);
		},
		
		<%--提交--%>
		doSubmit : function(){
			// 發送 Ajax 請求
			ajaxRequest.post('submit', {reqMap : Object.toJSON(actions.getreqMap())},
				function(resp){
					actions.btnDisable(resp.rtnMap||{});
				}
			);
		},

		<%--審核--%>
		doApprove : function(){
			// 發送 Ajax 請求
			ajaxRequest.post('approve', {reqMap : Object.toJSON(actions.getreqMap())},
				function(resp){
					actions.btnDisable(resp.rtnMap||{});
				}
			);
		},
		
		<%--退回--%>
		doReject : function(){
			// 發送 Ajax 請求
			ajaxRequest.post('reject', {reqMap : Object.toJSON(actions.getreqMap())},
				function(resp){
					actions.btnDisable(resp.rtnMap||{});
				}
			);
		},
		
		<%--刪除--%>
		doDelete : function(){
			// 發送 Ajax 請求
			var isSuccess = false;
			ajaxRequest.setSynchronous(true); // 開啟同步
			ajaxRequest.post('delete', {reqMap : Object.toJSON(actions.getreqMap())},
				function(resp){
					isSuccess = true;
				}
			);
			ajaxRequest.setSynchronous(false); // 關閉同步
			
			if(isSuccess){ // 是否delete成功
				actions.closePopupWin(); // 回上一頁方法
			}
		},
		
		<%--回上一頁--%>
		doBack : function(){
			actions.closePopupWin(); // 回上一頁方法
		}
	};
	
		
	return {
		initApp : function() {
                        
			<#list insertColumns as col>
			<#if column_dateLst?? && column_timestampLst?? &&
			        (column_dateLst?seq_contains(col) || column_timestampLst?seq_contains(col))>
			<cathay:LocaleDisplay sysCode="RG" dateFields="${col}" var="localeDisplay" />
			</#if>
			</#list>
						
			PageUI.createPageWithAllBodySubElement(
				'${edit_jsp}',
				'程式開發訓練GTU001',
				'單位代號索引GTU001'
			);
			
			var rtnMap = <c:out value='${r"${rtnMap}"}' default='{}' escapeXml='false'/>;  // rtnMap物件
			actions.btnDisable(rtnMap);
			validAction.init();
			
			$('BTN_depNm_index').observe('click', buttons.doIndex_DEP); // 索引(單位代號)
			
			$('BTN_confirm').observe('click', buttons.doConfirm);//提交
			$('BTN_back').observe('click', buttons.doBack); // 回上一頁
			$('BTN_insert').observe('click', buttons.doInsert); // 新增
			$('BTN_update').observe('click', buttons.doUpdate); // 修改
			$('BTN_approve').observe('click', buttons.doApprove); // 審核
			$('BTN_reject').observe('click', buttons.doReject); // 退回
			$('BTN_delete').observe('click', buttons.doDelete); // 刪除
			
			PageUI.resize();
			displayMessage();
		}
	};	
}
	
</script>
</head>
<body>
<form name="form1" id="form1"
	style="PADDING-RIGHT: 0px; PADDING-LEFT: 0px; PADDING-BOTTOM: 0px; MARGIN: 0px; PADDING-TOP: 0px">
<table width="100%" border="0" cellpadding="0" cellspacing="1"
	class="tbBox2">

	<#list 0..formColumns?size-1 as ii>
	<#assign col1=formColumns[ii]!"" />
	<#assign col2=formColumns[ii+1]!"" />
	<tr>
		<td class="tbYellow" width="10%">${columnLabel[col1]!""}</td>
		<td class="tbYellow2" width="40%">
			<input id="${col1!""}" name="${col1!""}" type="text" class="textBox2" />
		</td>
		<td class="tbYellow" width="10%">${columnLabel[col2]!""}</td>
		<td class="tbYellow2" width="40%">
			<input id="${col2!""}" name="${col2!""}" type="text" class="textBox2" />
		</td>
	</tr>
	</#list>
	
	<tr>
		<td class="tbYellow2" colspan="4" align="center">
		<button id="BTN_insert" class="button">新增</button>
		<button id="BTN_update" class="button">修改</button>
		<button id="BTN_confirm" class="button">提交</button>
		<button id="BTN_approve" class="button">審核</button>
		<button id="BTN_reject" class="button">退回</button>
		<button id="BTN_delete" class="button">刪除</button>
		<button id="BTN_back" class="button">回上一頁</button>
		</td>
	</tr>

	<tr>
		<td class="tbYellow2" colspan="4" align="center"></td>
	</tr>
</table>
</form>
</body>
</html>