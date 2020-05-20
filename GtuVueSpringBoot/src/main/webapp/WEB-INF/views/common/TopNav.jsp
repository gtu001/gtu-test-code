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
    <!-- Page Content Holder -->
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <div class="container-fluid">
            <button type="button" id="sidebarCollapse" class="navbar-btn">
                <span></span>
                <span></span>
                <span></span>
            </button>
            <ul class="navbar-nav ml-auto" id="user">
                <li class ="nav-item dropdown no-arrow">
                    <a  href="#" id="userDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" class="nav-link dropdown-toggle mr-5">親愛的 ${loggedinuser}，您好!</a>
                    <div class="dropdown-menu dropdown-menu-right" aria-labelledby="userDropdown">
                        <a class="dropdown-item" href="<c:url value='/edit-user-${loggedinuser}' />">修改個人資訊</a>
                        <a class="dropdown-item" href="<c:url value="/logout" />" >登出</a>
                    </div>
                </li>
            </ul>
        </div>
    </nav>
</body>
</html>