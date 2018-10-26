<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>
<%@ page language='java' contentType='text/html; charset=BIG5'%>

<!--
�{���GXXZX0101.jsp
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
<link href='${r"${cssBase}"}/cm.css' rel='stylesheet' type='text/css'>

<script type='text/JavaScript' src='${r"${htmlBase}"}/CM/js/ajax/prototype.js'></script>
<!-- ���n -->
<script type='text/JavaScript' src='${r"${htmlBase}"}/CM/js/ajax/CSRUtil.js'></script>
<!-- ���n -->
<script type='text/JavaScript' src='${r"${htmlBase}"}/CM/js/ui/TableUI.js'></script>
<!-- �ثe�t�α`�Ϊ����u�� -->
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


<%-- ���͵e������ --%>
<%--�z�L prototype �� Event �����ť onload �ƥ�AĲ�o�ɶi�� initApp()--%>
Event.observe(window, 'load', new ${edit_jsp}().initApp);

function ${edit_jsp}(){


	<%-- TablE UI���� --%>
	var ajaxRequest = new CSRUtil.AjaxHandler.request('${r"${dispatcher}/${edit_action_clz}/"}');	
	var valid1;
	
	var validAction = {
		// ����ˮ�
		init: function(){
			valid1 = new Validation('form1', {focusOnError:true, immediate:false});
			<#list insertColumns_pk as col>
			valid1.define( "required" , { id : "${col}", errMsg:'${columnLabel[col]}���o����'} );
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
		
		// �ھ�OP_STATUS�����w���s����
		btnDisable : function(reqMap){
			$w('BTN_confirm  BTN_insert BTN_update BTN_approve BTN_reject BTN_delete  ').each(  //BTN_back
				function(id) { 
					$(id).disable();
				}
			);
			
			var ACTION_TYPE = reqMap.ACTION_TYPE;
			
			if (ACTION_TYPE == "I"){ // �s�W��
				<#list insertColumns as col>
				$('${col}').update(reqMap.${col} || '');
				</#list>
			
				$('BTN_insert').enable();
				
			} else { // �ק��, ACTION_TYPE=U
				<#list updateColumns as col>
				$('${col}').setValue(reqMap.${col} || '');
				</#list>
				
				if(reqMap.OP_STATUS == "10"){ // OP_STATUS ��10 �f�֡B�h�^disable ��lenable
					$w('BTN_confirm  BTN_insert BTN_update BTN_approve BTN_delete').each( 
						function(id) { 
							$(id).enable();
						}
					);
				}else if(reqMap.OP_STATUS == "20"){ // OP_STATUS ��20 ����disable ��lenable
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
		<%--����(���N��)--%>
		doIndex_DEP : function(){
			popupWin.popup({									
				src : '${r"${dispatcher}"}/XXZX_0200/prompt' ,		
				scrolling : 'yes',	
				height : '520',
				cb : function(obj){ // �N XXZX_0200�ҿ���� DEP_CODE�N�J�e��DIV_NO , DEP_NM��ܦbDIV_NO��
					$('DIV_NO').setValue(obj.DEP_CODE||'');// ���N��
					$('DEP_NM').update(obj.DEP_NM||'');// ���W��
				}		
			});			
		},
	
		<%--�s�W--%>
		doInsert : function(){
			if(!valid1.validate()){
				return; 
			}
			// �o�e Ajax �ШD
			ajaxRequest.post('insert', {reqMap : Object.toJSON(actions.getreqMap())},
				function(resp){
					actions.btnDisable(resp.rtnMap||{});
				}
			);
		},
		
		<%--�ק�--%>
		doUpdate : function(){
			if(!valid1.validate()){
				return; 
			}
			// �o�e Ajax �ШD
			ajaxRequest.post('update', {reqMap : Object.toJSON(actions.getreqMap())},
				function(resp){
					actions.btnDisable(resp.rtnMap||{});
				}
			);
		},
		
		<%--����--%>
		doSubmit : function(){
			// �o�e Ajax �ШD
			ajaxRequest.post('submit', {reqMap : Object.toJSON(actions.getreqMap())},
				function(resp){
					actions.btnDisable(resp.rtnMap||{});
				}
			);
		},

		<%--�f��--%>
		doApprove : function(){
			// �o�e Ajax �ШD
			ajaxRequest.post('approve', {reqMap : Object.toJSON(actions.getreqMap())},
				function(resp){
					actions.btnDisable(resp.rtnMap||{});
				}
			);
		},
		
		<%--�h�^--%>
		doReject : function(){
			// �o�e Ajax �ШD
			ajaxRequest.post('reject', {reqMap : Object.toJSON(actions.getreqMap())},
				function(resp){
					actions.btnDisable(resp.rtnMap||{});
				}
			);
		},
		
		<%--�R��--%>
		doDelete : function(){
			// �o�e Ajax �ШD
			var isSuccess = false;
			ajaxRequest.setSynchronous(true); // �}�ҦP�B
			ajaxRequest.post('delete', {reqMap : Object.toJSON(actions.getreqMap())},
				function(resp){
					isSuccess = true;
				}
			);
			ajaxRequest.setSynchronous(false); // �����P�B
			
			if(isSuccess){ // �O�_delete���\
				actions.closePopupWin(); // �^�W�@����k
			}
		},
		
		<%--�^�W�@��--%>
		doBack : function(){
			actions.closePopupWin(); // �^�W�@����k
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
				'�{���}�o�V�mGTU001',
				'���N������GTU001'
			);
			
			var rtnMap = <c:out value='${r"${rtnMap}"}' default='{}' escapeXml='false'/>;  // rtnMap����
			actions.btnDisable(rtnMap);
			validAction.init();
			
			$('BTN_depNm_index').observe('click', buttons.doIndex_DEP); // ����(���N��)
			
			$('BTN_confirm').observe('click', buttons.doConfirm);//����
			$('BTN_back').observe('click', buttons.doBack); // �^�W�@��
			$('BTN_insert').observe('click', buttons.doInsert); // �s�W
			$('BTN_update').observe('click', buttons.doUpdate); // �ק�
			$('BTN_approve').observe('click', buttons.doApprove); // �f��
			$('BTN_reject').observe('click', buttons.doReject); // �h�^
			$('BTN_delete').observe('click', buttons.doDelete); // �R��
			
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
		<button id="BTN_insert" class="button">�s�W</button>
		<button id="BTN_update" class="button">�ק�</button>
		<button id="BTN_confirm" class="button">����</button>
		<button id="BTN_approve" class="button">�f��</button>
		<button id="BTN_reject" class="button">�h�^</button>
		<button id="BTN_delete" class="button">�R��</button>
		<button id="BTN_back" class="button">�^�W�@��</button>
		</td>
	</tr>

	<tr>
		<td class="tbYellow2" colspan="4" align="center"></td>
	</tr>
</table>
</form>
</body>
</html>