<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<%@ page  import="java.sql.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<html>

<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <%@include file="../common/headCommon.jsp" %>
	<title>UserList</title>
</head>

<body>
<div class="wrapper">  
        <!-- Sidebar Holder -->  
        <jsp:include page="../common/LeftNav.jsp"></jsp:include>
        <div id="content">
       	 	<!-- Page Content Holder -->
            <jsp:include page="../common/TopNav.jsp"></jsp:include>
            <h2>管理列表</h2>
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th scope="col">帳號</th>
                        <th scope="col">姓名</th>
                        <th scope="col">電話</th>
                        <th scope="col">Email</th>
                        <th width="100">功能</th>
                    </tr>
                </thead>
                <tbody>
                  <c:forEach items="${users}" var="user">
					<tr>
						<td>${user.ssoId}</td>
						<td>${user.name}</td>
						<td>${user.phoneNumber}</td>
						<td>${user.email}</td>
					    <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
							<td><a href="<c:url value='/edit-user-${user.ssoId}' />" class="btn btn-success custom-width">編輯</a></td>
				        </sec:authorize>
				        <sec:authorize access="hasRole('ADMIN')">
							<td><a href="<c:url value='/admin/delete-user-${user.ssoId}' />" class="btn btn-danger custom-width">刪除</a></td>
        				</sec:authorize>
					</tr>
				</c:forEach>
                </tbody>
            </table>
            <sec:authorize access="hasRole('ADMIN')">
		 	<div class="well">
		 		<a href="<c:url value='/admin/newUser' />" class="btn btn-primary">新增使用者</a>
<%-- 		 		<a class="create" href="<c:url value='/newUser' />">新增使用者</a> --%>
		 	</div>
	 	</sec:authorize>     
        </div>
    </div>
</body>
</html>