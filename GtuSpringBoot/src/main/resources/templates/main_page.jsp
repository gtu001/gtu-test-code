
<!-- % @ page language="java" contentType="text/html; charset=UTF8"
	pageEncoding="UTF8" % -->
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="Content-Type" content="text/html; charset=UTF8">
<title></title>
</head>
<body>

	<span style="color:red" th:text="${message}" />
	<form action="/GtuSpringBoot/main" method="get">
		請輸入功能 : 
		<select name="path">
			<option value="">請選擇</option>
			<option value="/springdata-dbMain/dbMain">DB</option>
		</select>
		<br />
		 <input type="submit" value="GO" /><br />
	</form>

</body>
</html>