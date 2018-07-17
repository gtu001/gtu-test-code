
<!-- % @ page language="java" contentType="text/html; charset=UTF8"
	pageEncoding="UTF8" % -->
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="Content-Type" content="text/html; charset=UTF8">
<script src="/js/jquery-3.3.1.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$.get("/springdata-dbMain/tables", {}, function(data) {
			alert(typeof (data));
			alert(JSON.stringify(data));
		}).fail(function(a1, a2, a3) {
			alert(JSON.stringify(a1)));
			alert(JSON.stringify(a2)));
			alert(JSON.stringify(a3)));
		});

		$("path").bind("blur", function() {

		});
	});
</script>
<title></title>
</head>
<body>

	<span style="color: red" th:text="${message}" />
	<form action="/GtuSpringBoot/main">
		表名稱: <input type="text" name="path" /><br />

		<div id="dbBody" />

		<input type="button" value="GO" /><br />
	</form>

</body>
</html>