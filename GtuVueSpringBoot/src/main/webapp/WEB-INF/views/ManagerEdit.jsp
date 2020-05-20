<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <%@include file="common/headCommon.jsp" %>

	<title>ManagerEdit</title>
</head>
<body>
    <div class="wrapper">  
        <!-- Sidebar Holder -->  
        <jsp:include page="common/LeftNav.jsp"></jsp:include>

        <!-- Page Content Holder -->
        <div id="content">
            <jsp:include page="common/TopNav.jsp"></jsp:include>
            <h2>管理列表</h2>
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th scope="col">Firstname</th>
                        <th scope="col">Lastname</th>
                        <th scope="col">Email</th>
                        <th scope="col">SSO ID</th>
                        <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
				        	<th width="100"></th>
				        </sec:authorize>
				         <sec:authorize access="hasRole('ADMIN')">
				        	<th width="100"></th>
				        </sec:authorize>
                    </tr>
                </thead>
                <tbody>
                  <c:forEach items="${users}" var="user">
					<tr>
						<td>${user.firstName}</td>
						<td>${user.lastName}</td>
						<td>${user.email}</td>
						<td>${user.ssoId}</td>
					    <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
							<td><a href="<c:url value='/edit-user-${user.ssoId}' />" class="btn btn-success custom-width">Edit</a></td>
				        </sec:authorize>
				        <sec:authorize access="hasRole('ADMIN')">
							<td><a href="<c:url value='/edit-user-menu-${user.ssoId}' />" class="btn btn-success ">Menu</a></td>
        				</sec:authorize>
				        <sec:authorize access="hasRole('ADMIN')">
							<td><a href="<c:url value='/delete-user-${user.ssoId}' />" class="btn btn-danger custom-width">delete</a></td>
        				</sec:authorize>
					</tr>
				</c:forEach>
                </tbody>
            </table>
            <sec:authorize access="hasRole('ADMIN')">
		 	<div class="well">
		 		<a class="create" href="<c:url value='/newuser' />">Add New User</a>
		 	</div>
	 		</sec:authorize>     
        </div>
    </div>

</body>
</html>
