<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>Sign On</title>
<script src="/S2SH/js/jquery-1.11.3.js"></script>
</head>

<script type="text/javascript">
	$(document).ready(function() {
		$("#sendOutBtn").click(function() {
			$("#form").attr("action", "Login!input");
			$("#form").submit();
		});
		$("#accountCreateBtn").click(function() {
			$("#form").attr("action", "Login!goCreate");
			$("#form").submit();
		});
	});
</script>

<body>

	<s:if test="hasActionErrors()">
		<div class="errors">
			<s:actionerror />
		</div>
	</s:if>

	<s:if test="hasActionMessages()">
		<div class="welcome">
			<s:actionmessage />
		</div>
	</s:if>

	<s:form id="form" action="Login!input">
		<s:textfield key="user.userId" name="user.userId"
			value="%{user.userId}" />
		<s:password key="user.password" name="user.password"
			value="%{user.password}" />

		<input id="sendOutBtn" type="button" value="送出" />
		<input id="accountCreateBtn" type="button" value="建立新帳號" />
	</s:form>
</body>
</html>
