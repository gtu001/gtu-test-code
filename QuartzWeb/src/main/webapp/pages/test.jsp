<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="${pageContext.request.contextPath}/js/jquery-3.2.1.js"></script>
<link rel="stylesheet" href="" type="text/css">
<script type="text/javascript">
	function selectRadio(action) {
		$("#form").attr("action",
				"${pageContext.request.contextPath}/" + action + ".do");
		alert($("#form").attr("action"));
		$("#form").submit();
	}
</script>
</head>
<body>
	<form id="form" action="">
		<input type="radio" name="choiceRadio"
			onclick="javascript:selectRadio('QuartzOrignServlet');" />QuartzOrignServlet<br />
		<input type="radio" name="choiceRadio"
			onclick="javascript:selectRadio('SpringQuartzServlet');" />SpringQuartzServlet<br />
	</form>
</body>
</html>

