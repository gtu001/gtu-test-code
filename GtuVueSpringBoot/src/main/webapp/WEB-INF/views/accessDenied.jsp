<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	<%@include file="common/headCommon.jsp" %>
	
	<title>Registration Confirmation Page</title>
</head>
<body>
	<div class="wrapper">  
		<!-- Sidebar Holder -->  
        <jsp:include page="common/LeftNav.jsp"></jsp:include>
		<div id="content">
	        <!-- Page Content Holder -->
			<jsp:include page="common/TopNav.jsp"></jsp:include> 
			<span>使用者 <strong>${loggedinuser}</strong>, 您好! 您沒有權限訪問此頁面, 請重新<span class="floatRight"><a href="<c:url value="/logout" />">登入</a></span>其他帳號或返回首頁, 謝謝</span>
		</div>
	</div>
</body>
</html>