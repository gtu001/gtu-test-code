<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="BIG5"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>
</head>
<body>
	<form action="test2" method="post">
		<input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />
		<button type="submit" class="btn btn-primary">完成2</button>
	</form>
</body>
</html>