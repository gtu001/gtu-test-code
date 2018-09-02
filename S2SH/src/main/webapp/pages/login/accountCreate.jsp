<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>Account create</title>
    <script src="/S2SH/js/jquery-1.11.3.js"></script>
</head>

<script type="text/javascript">
$(document).ready(function(){
	$("#sendOut").click(function(){
		$("#form").attr("action", "Login!doCreate");
		$("#form").submit();
	});
});
</script>

<body>
<s:form id="form" action="Login!input">

    <s:textfield key="user.userId" name="user.userId" />
    <s:textfield key="user.userName" name="user.userName" />
    <s:textfield key="user.email" name="user.email" />
    <s:password key="user.password" name="user.password" />
    
    <input id="sendOut" type="button" value="建立" />
    <input id="resetBtn" type="reset" value="取消" />
</s:form>
</body>
</html>
