<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ include file="/common/head.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>網路門市::後臺管理系統</title>
<script language="JavaScript" type="text/javascript" src="<c:url value="/js/lib/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<c:url value="/js/prototype.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<c:url value="/js/estore-util.js"/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/styles/base_layout.css"/>" />
<link rel="stylesheet" type="text/css" href="<c:url value="/styles/form.css"/>" />
<link rel="stylesheet" type="text/css" href="<c:url value="/styles/button.css"/>" />
<script language="JavaScript">
	var myDebug = false;

	function moveUp() {
		moveOneStep(-1);
	}

	function moveDown() {
		moveOneStep(1);
	}

	function moveTop() {
		if (!isAtTop() && !isSelected())
			return;
		var options = document.getElementById("itemSelect").options;
		var tempString = options[options.selectedIndex].text;
		var tempValue = options[options.selectedIndex].value;

		for (i = options.selectedIndex; i > 0; i--) {
			options[i].text = options[i - 1].text;
			options[i].value = options[i - 1].value;
		}

		options[0].text = tempString;
		options[0].value = tempValue;
		options[0].selected = true;
	}

	function moveBottom() {
		if (!isAtBottom() && !isSelected())
			return;
		var options = document.getElementById("itemSelect").options;
		var tempString = options[options.selectedIndex].text;
		var tempValue = options[options.selectedIndex].value;

		for (i = options.selectedIndex; i < options.length - 1; i++) {
			options[i].text = options[i + 1].text;
			options[i].value = options[i + 1].value;
		}

		options[options.length - 1].text = tempString;
		options[options.length - 1].value = tempValue;
		options[options.length - 1].selected = true;
	}

	function isAtTop() {
		var options = document.getElementById("itemSelect").options;
		if (options.selectedIndex == 0)
			return true;
		return false;
	}

	function isAtBottom() {
		var options = document.getElementById("itemSelect").options;
		if (options.selectedIndex == options.length - 1)
			return true;
		return false;
	}

	function isSelected() {
		var options = document.getElementById("itemSelect").options;
		if (options.selectedIndex >= 0)
			return true;
		return false;
	}

	function moveOneStep(direction) {
		var ddl = document.getElementById("itemSelect");
		var selectedIndex = ddl.options.selectedIndex;
		var optionLength = ddl.options.length;
		if (myDebug == true) {
			alert("direction: " + direction);
			alert("selected index: " + selectedIndex);
			alert("options length: " + optionLength);
		}
		if (direction == -1 && selectedIndex <= 0)
			return;
		if (direction == 1 && selectedIndex >= optionLength - 1)
			return;
		changePosition(selectedIndex, selectedIndex + direction);
	}

	function changePosition(index1, index2) {
		if (myDebug == true) {
			alert("index1: " + index1);
			alert("index2: " + index2);
		}

		var options = document.getElementById("itemSelect").options;
		var tempString = options[index1].text;
		var tempValue = options[index1].value;
		if (myDebug == true) {
			alert("temp string: " + tempString);
			alert("temp value: " + tempValue);
		}

		options[index1].text = options[index2].text;
		options[index1].value = options[index2].value;

		options[index2].text = tempString;
		options[index2].value = tempValue;
		options[index2].selected = true;
	}

	/*
	 *儲存排序
	 */
	function saveSorting() {
		var sortArray = new Array();
		var sortOptions = document.getElementById("itemSelect").options;
		for (i = 0; i < sortOptions.length; i++) {
			sortArray.push(sortOptions[i].value);
		}
		window.opener.saveSorting(sortArray);
		window.close();
	}
	
	//---------------------------------------------------來源端的javascript 
	function sort() {
		var param = '<c:url value="/admin/marketing/mobilePortalManagement.do?cmd=sort"/>';
		popupWindow(param, 800, 900);
	}
	function popupWindow(url, width, height, overflow) {
		if (overflow == '' || !/^(scroll|resize|both)$/.test(overflow)) {
			overflow = 'both';
		}
		var win = window.open(url, '', 'width=' + width + ',height=' + height
				+ ',scrollbars='
				+ (/^(scroll|both)$/.test(overflow) ? 'yes' : 'no')
				+ ',resizable='
				+ (/^(rezise|both)$/.test(overflow) ? 'yes' : 'no')
				+ ',status=yes,toolbar=no,menubar=no,location=no,resizable=no');
		return win;
	}
	function saveSorting(array){
		//alert(array);
		jQuery("#cmd").val("editSort");
		jQuery("#forward").val("initPage");
		jQuery("#funName").val(array);
		jQuery("#actionMode").val(array);
		jQuery("#resultForm").submit();
	}
	//---------------------------------------------------來源端的javascript 
</script>
</head>
<body>
	<div class="formContainer">
		<div class="formHeader">
			<div class="title">首頁區塊標題(區塊名稱)</div>
		</div>
		<div>
			<hr class="hrline" />
		</div>
		<br>
		<form id="resultForm" name="resultForm" method="post"
			action="<c:url value="/admin/marketing/ranking.do" />">
			<input type="hidden" name="cmd" id="cmd" value="" />
			<input type="hidden" name="tabOrder" id="tabOrder"
				value="${tabOrder}" />
			<input type="hidden" name="handsetCategoryId" id="handsetCategoryId"
				value="${handsetCategoryId}" />
			<div class="formBody" align="center">
				<table>
					<tr>
						<td colspan="2"><label class="fieldCaption">圖檔標題</label></td>
					</tr>
					<tr>
						<td rowspan="4" valign="middle" align="center"
							style="padding: 0px; width: 236px;">
							<select id="itemSelect" name="itemSelect" size="20"
								style="width: 100%; font-size: small;">
								<c:forEach var="item" items="${funNameList}">
									<option value="${item.id}">
										<c:out value="${coTypeMap[item.coType]}" />
									</option>
								</c:forEach>
						</select></td>
						<td class="border-leftside" style="width: 20px"
							style="Cursor: Hand" onclick="moveTop();"><img
							src="<c:url value='/img/icon/up_end.gif'/>" alt="移到最前面" /></td>
					</tr>
					<tr>
						<td class="border-leftside" style="Cursor: Hand"
							onclick="moveUp();"><img
							src="<c:url value='/img/icon/up.gif'/>" alt="向上" /></td>
					</tr>
					<tr>
						<td class="border-leftside" style="Cursor: Hand"
							onclick="moveDown();"><img
							src="<c:url value='/img/icon/down.gif'/>" alt="向下" /></td>
					</tr>
					<tr>
						<td class="border-leftside" style="Cursor: Hand"
							onclick="moveBottom();"><img
							src="<c:url value='/img/icon/down_end.gif'/>" alt="移到最後面" /></td>
					</tr>
					<tr>
						<td colspan="2" valign="middle" align="center"><input
							name="button" type="button" class="actionButton"
							onclick="saveSorting();" value="儲存" />&nbsp; <input
							name="button2" type="button" class="actionButton"
							onclick="javascript:window.close();" value="取消" /></td>
					</tr>
				</table>
			</div>
		</form>
	</div>
</body>
<logic:messagesPresent message="true">
	<script language="javascript">
		alert("<html:messages id='messages' message='true'><bean:write name='messages'/>\n</html:messages>");
	</script>
</logic:messagesPresent>
</html>