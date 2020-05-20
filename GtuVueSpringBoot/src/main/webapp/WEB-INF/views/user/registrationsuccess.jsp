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
	<%@include file="../common/headCommon.jsp" %>
	
	<title>Registration Confirmation Page</title>
</head>
<body>
	<div class="wrapper">  
		<!-- Sidebar Holder -->  
        <jsp:include page="../common/LeftNav.jsp"></jsp:include>
        <div id="content">
	        <!-- Page Content Holder -->
			<jsp:include page="../common/TopNav.jsp"></jsp:include> 
			<div class="alert alert-success lead">
		    	${success}
			</div>
		
			<span class="well floatRight">
				返回 <a href="<c:url value='/admin/usersList' />">使用者清單</a>
			</span>
		</div>
	</div>
</body>

</html>