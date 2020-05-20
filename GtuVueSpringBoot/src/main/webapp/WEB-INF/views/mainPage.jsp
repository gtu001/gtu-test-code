<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ page  import="java.sql.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
	<%@include file="common/headCommon.jsp" %>
	<title>Home</title>
</head>
<body>
    <div class="wrapper">
        <!-- Sidebar Holder -->
        <jsp:include page="common/LeftNav.jsp"></jsp:include>

        <!-- Page Content Holder -->
        <div id="content">
      		<jsp:include page="common/TopNav.jsp"></jsp:include>
            <h2>歡迎使用OA後台管理系統</h2>
<!--             TODO -->
        </div>
    </div>

    </script>

</body>
</html>
