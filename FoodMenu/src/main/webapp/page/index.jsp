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
<%-- <script src="${pageContext.request.contextPath}/js/jquery-2.1.1.js"></script> --%>

<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script
	src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.2/jquery-ui.min.js"></script>
<link
	href="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.2/themes/ui-lightness/jquery-ui.css"
	rel="stylesheet" type="text/css" />

<script
	src="${pageContext.request.contextPath}/js/jquery.fileDownload.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#ajaxTestBtn").bind("click", function() {
			window.location = "/FoodMenu/page/ajaxTest.jsp";
		});
		$("#downloadTestBtn").bind("click", function() {
			window.location = "/FoodMenu/page/downloadTest.jsp";
		});
		$("#redirectMenuQuery").bind("click", function() {
			window.location = "/FoodMenu/page/orderIndex.jsp";
		});
		$("#ajaxUploadTestBtn").bind("click", function() {
			window.location = "/FoodMenu/page/ajaxUploadFile.jsp";
		});
		$("#placeHolderBtn").bind("click", function() {
			window.location = "/FoodMenu/page/placeholder.jsp";
		});
		$("#alertifyTestBtn").bind("click", function() {
			window.location = "/FoodMenu/page/alertifyTest.jsp";
		});
		$("#blockUITestBtn").bind("click", function() {
			window.location = "/FoodMenu/page/blockUITest.jsp";
		});
		$("#dialogTestBtn").bind("click", function() {
			window.location = "/FoodMenu/page/dialogTest.jsp";
		});
		$("#htmlJavascriptEscapeBtn").bind("click", function() {
			window.location = "/FoodMenu/page/htmlJavascriptEscape.jsp";
		});
		$("#datePickerChsBtn").bind("click", function() {
			window.location = "/FoodMenu/page/datePickerChs.jsp";
		});
	});
</script>
<title>Insert title here</title>
</head>
<body>
	<table border="1" width="100%">
		<tr>
			<td><input type="button" id="redirectMenuQuery" value="菜單" /></td>
			<td><input type="button" id="ajaxTestBtn" value="測試ajax" /></td>
			<td><input type="button" id="downloadTestBtn" value="測試下載" /></td>
			<td><input type="button" id="ajaxUploadTestBtn" value="ajax上傳" /></td>
		</tr>
		<tr>
			<td><input type="button" id="placeHolderBtn" value="placeHolder測試" /></td>
			<td><input type="button" id="alertifyTestBtn" value="alertify測試" /></td>
			<td><input type="button" id="blockUITestBtn" value="blockUI測試" /></td>
			<td><input type="button" id="dialogTestBtn" value="dialog測試" /></td>
		</tr>
		<tr>
			<td><input type="button" id="htmlJavascriptEscapeBtn" value="htmlJavascriptEscape測試" /></td>
			<td><input type="button" id="datePickerChsBtn" value="中文日曆" /></td>
			<td><input type="button" id="" value="" /></td>
			<td><input type="button" id="" value="" /></td>
		</tr>
	</table>
</body>
</html>