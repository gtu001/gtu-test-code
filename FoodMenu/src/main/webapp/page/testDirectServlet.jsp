<%@ page language="java" contentType="text/html;charset=Big5" %>	

<!-- Deposit.jsp -->

<%
//可以在此頁做檢查parameter的動作
%>
<jsp:forward page="/DispatcherServlet">
	<jsp:param name="api" value="DepositEx"/>
</jsp:forward>