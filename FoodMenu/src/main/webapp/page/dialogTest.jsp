<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="${pageContext.request.contextPath}/js/jquery-2.1.1.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery-ui.js"></script>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery-ui.css" />

<STYLE type=text/css>
/* 關閉按鈕 隱藏 */
.no-close .ui-dialog-titlebar-close {
	display: none;
}
</STYLE>


<script type="text/javascript">
	$(document).ready(function() {
	});

	//顯示dialog
	function showDialog001() {
		$("#SHOWB_dialog").dialog({
			dialogClass : 'no-close',
			title : "測試",
			resizable : false,
			height : "auto",
			width : 800,
			modal : true, //鎖定頁面功能，僅能點選dialog box的元件. 預設關閉(false)。
			//autoOpen : false, //自動開啟
			closeOnEscape : false, //按Esc關閉
			draggable : false, //可拖曳
			buttons : {
				"確定" : function() {
					$(this).dialog("close");
				},
			}
		});

		//背景顏色
		$("#SHOWB_dialog").css("background-color", "#F2FAFA");
		//透明程度
		$("#SHOWB_dialog").css("opacity", "0");
	}
</script>
<title>Insert title here</title>
</head>
<body>

	<div id="SHOWA">
		<ul>
			<li><a href="javascript:showDialog001();">Dialog按鈕</a></li>
		</ul>
	</div>

	<br />
	<br />
	<br />
	<br />

	<div id="SHOWB_dialog">
		<div id="SHBOX">
			<span class="SWORD01_2_1"> <font color="red">以i方式，i訊息:
					<br />溫心提醒繳費限制，選擇不同繳費方式會影響可繳限額喔!!
			</font>
			</span>
			<table class="tableHasLine">
				<tr>
					<td><span class="SWORD01_2_2">繳費方式</span></td>
					<td><span class="SWORD01_2_2">全國繳費網</span></td>
					<td><span class="SWORD01_2_2">信用卡</span></td>
				</tr>
				<tr>
					<td><span class="SWORD01_2_2">躉繳(一次繳)</span></td>
					<td><span class="SWORD01_2_2">15,000～100,000</span></td>
					<td><span class="SWORD01_2_2">15,000～200,000</span></td>
				</tr>
				<tr>
					<td><span class="SWORD01_2_2">分期繳(年繳)</span></td>
					<td><span class="SWORD01_2_2">10,000～100,000</span></td>
					<td><span class="SWORD01_2_2">10,000～200,000</span></td>
				</tr>
			</table>
		</div>
	</div>

</body>
</html>