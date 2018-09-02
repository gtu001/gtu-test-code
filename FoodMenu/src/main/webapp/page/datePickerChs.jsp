<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<jsp:directive.page import="java.util.*" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.2/jquery-ui.min.js"></script>

<%-- <script src="${pageContext.request.contextPath}/js/jquery-2.1.1.js"></script> --%>
<%-- <script src="${pageContext.request.contextPath}/js/jquery-ui.js"></script> --%>

<!-- <link
	href="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.2/themes/ui-lightness/jquery-ui.css"
	rel="stylesheet" type="text/css" /> -->
<link href="${pageContext.request.contextPath}/js/jquery-ui.css" rel="stylesheet" type="text/css" />

<script type="text/javascript">
	$(document).ready(function(){
		$.datepicker.regional['zh-TW']={
			dayNamesMin : [ "日", "一", "二", "三", "四", "五", "六" ],
			monthNames : [ "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月",
					"九月", "十月", "十一月", "十二月" ],
			monthNamesShort : [ "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月",
					"九月", "十月", "十一月", "十二月" ],
			dateFormat : "yy-mm-dd"
		};

		$.datepicker.setDefaults($.datepicker.regional["zh-TW"]);

		$("input[name=testDatePickerInput]").datepicker({
			dateFormat : 'yy-mm-dd',
			minDate : 0
		});
		
		var date = new Date(2017, 10, 1);
		$("input[name=testDatePickerInput]").datepicker("setDate", date);
	});
</script>
<title></title>
</head>
<body>
	<input type="text" name="testDatePickerInput" />
</body>
</html>