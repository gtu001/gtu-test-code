<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%
    List<Map<String, String>> lst = (List<Map<String, String>>) request.getAttribute("list");
%>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<link rel="stylesheet" href="css2/bootstrap.min.css">
<link rel="stylesheet" href="css2/font-awesome.min.css">
<link rel="stylesheet" href="css2/animate.css">
<link rel="stylesheet" href="css2/jquery-ui.min.css">
<link rel="stylesheet" href="css2/main.css">

<style>
.horizontal_center_clz {
	display: inline-block;
	margin: 0 auto;
	text-align: center;
}
</style>

<title>測試頁</title>
<script src="js2/jquery-ui.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		initPage();
	});
</script>
</head>
<body>
	<img alt="測試圖片" src="img/bigBoss.png" />

	<table border="1">
		<tr>
			<c:forEach var="item" items="<%=lst%>" varStatus="status">
				<td>${status.index}</td>
				<% Set<String> keys = ((Map<String,String>)pageContext.getAttribute("item")).keySet();%>
				<c:forEach  var="key" items="<%=keys %>" varStatus="status2">
					<td>${key}</td>
					<td>${item[key]}</td>
				</c:forEach>
			</c:forEach>
		</tr>
	</table>
</body>
</html>
