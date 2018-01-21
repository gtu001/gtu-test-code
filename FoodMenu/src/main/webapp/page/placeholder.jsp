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

<link href="css/select2.css" rel="stylesheet" />
<STYLE type=text/css>
.placeholder {
	color: #aaa;
}
</STYLE>

<script src="${pageContext.request.contextPath}/js/jquery-2.1.1.js"></script>
<script
	src="${pageContext.request.contextPath}/js/jquery.placeholder.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		//https://mathiasbynens.be/demo/placeholder
		$("#test001").placeholder();
	});
</script>
<title>Insert title here</title>
</head>
<body>

	<input type="text" id="test001" placeholder="請輸入內容" size="10" />
</body>
</html>