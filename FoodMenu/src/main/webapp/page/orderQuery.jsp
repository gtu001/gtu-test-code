<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="${pageContext.request.contextPath}/js/jquery-2.1.1.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		//alert('test');
	});
</script>
<title>Insert title here</title>
</head>
<body>
	<form action="/FoodMenu/OrderServlet" method="post">
	點菜查詢結果
	<table border="1">
		<tr>
			<td>類型</td>
			<td><c:out value="xxxxxxx" /></td>
		</tr>
	</table>
	</form>
</body>
</html>