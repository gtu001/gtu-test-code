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

    <%@include file="common/headCommon.jsp" %>

	<title>Menulist</title>
</head>

<body>
<div class="wrapper">  
        <!-- Sidebar Holder -->  
        <jsp:include page="common/LeftNav.jsp"></jsp:include>

        <!-- Page Content Holder -->
        <div id="content">
            <jsp:include page="common/TopNav.jsp"></jsp:include>
            <h2>清單列表</h2>
            <table class="table table-hover">
                <thead>
                    <tr>
                    	<th scope="col">ID</th>
                        <th scope="col">清單名稱</th>
                        <th scope="col">清單連結</th>
                        <th scope="col">清單敘述</th>
                        <th scope="col">清單父頁</th>
                        <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
				        	<th width="100"></th>
				        </sec:authorize>
				         <sec:authorize access="hasRole('ADMIN')">
				        	<th width="100"></th>
				        </sec:authorize>
                    </tr>
                </thead>
                <tbody>
                  <c:forEach items="${menuexisting}" var="menu">
                  	
					<tr>
						<td>${menu.id}</td>
						<td>${menu.menuName}</td>
						<td>${menu.menuUrl}</td>
						<td>${menu.menuDesc}</td>
						<td>${menu.fatherId}</td>
					    <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
							<td><a href="<c:url value='/edit-user-menu-${menu.id}' />" class="btn btn-success custom-width">編輯</a></td>
				        </sec:authorize>
				        <sec:authorize access="hasRole('ADMIN')">
							<td><a href="<c:url value='/delete-user-menu-${menu.id}' />" class="btn btn-danger custom-width">刪除</a></td>
        				</sec:authorize>
					</tr>
					
					<c:if test="${menu.subMenu.size()>0}">
					<c:forEach items="${menu.subMenu}" var="submenu">
					<tr>
						<td>${submenu.id}</td>
						<td>${submenu.menuName}</td>
						<td>${submenu.menuUrl}</td>
						<td>${submenu.menuDesc}</td>
						<td>${submenu.fatherId}</td>
					    <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
							<td><a href="<c:url value='/edit-user-menu-${submenu.id}' />" class="btn btn-success custom-width">編輯</a></td>
				        </sec:authorize>
				        <sec:authorize access="hasRole('ADMIN')">
							<td><a href="<c:url value='/delete-user-menu-${submenu.id}' />" class="btn btn-danger custom-width">刪除</a></td>
        				</sec:authorize>
					</tr>
					</c:forEach>
					</c:if>
				</c:forEach>
                </tbody>
            </table>
            <sec:authorize access="hasRole('ADMIN')">
		 	<div class="well">
		 		<a class="create" href="<c:url value='/createmenu' />">Add New Menu</a>
		 	</div>
	 	</sec:authorize>     
        </div>
    </div>
</body>
</html>