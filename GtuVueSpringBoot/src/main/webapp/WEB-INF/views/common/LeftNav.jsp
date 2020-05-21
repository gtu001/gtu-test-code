<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>

<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
</head>

<body>
	<!-- Sidebar Holder -->
	<nav id="sidebar">
		<div class="sidebar-header">
			<img src="<%=request.getContextPath()%>/static/jpg/logo_tw.png" width="100%">
			<ul class="list-unstyled components">
				<li>
	            	<a href="<c:url value='/main' />">首頁</a>
	            </li>
			<c:forEach items="${menuMap}" var="menu">
				<li class="active">
					<c:if test="${menu.subMenu.size()>0}">
                    	<a href="#sub_${menu.id}" data-toggle="collapse" aria-expanded="false" class="dropdown-toggle">${menu.menuName}</a>
                    	<ul class="collapse list-unstyled" id="sub_${menu.id}">
                    	<c:forEach items="${menu.subMenu}" var="submenu">
	                        <li>
	                            <a href="<c:url value='/${submenu.menuUrl}' />">${submenu.menuName}</a>
	                        </li>
                        </c:forEach>
                    	</ul>  
					</c:if>
                   	<c:if test="${menu.subMenu==null}">
                    	<a href="<c:url value='/${menu.menuUrl}' />" aria-expanded="false" class="dropdown-toggle">${menu.menuName}</a>
                    </c:if>
                </li>
             </c:forEach>
			</ul>
		</div>	
	</nav>
</body>

</html>