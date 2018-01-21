<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<%@ include file="../common/page_top.jsp"%>

<html>
<head>
<script type="text/javascript">
	$(document).ready(function() {
		$("#testBtn").bind("click", function() {
			$("input[name=method]").val("testBtn1");
			$("form").submit();
		});
	});
</script>
</head>
<body>
	<h1>測試</h1>

	<%@ include file="../common/messages2.jsp"%>

	<html:form action="/TestSomething2" method="POST">
		<html:hidden property="method" />
		<table border="1">
			<tr>
				<td><input id="testBtn" type="button"
					value="test1" /></td>
				<td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			<tr>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
		</table>
	</html:form>
</body>
</html>


