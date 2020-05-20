<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    
	<%@include file="../common/headCommon.jsp" %>
	<title>修改資訊</title>
</head>
<body>
    <div class="wrapper">
        <!-- Sidebar Holder -->
        <jsp:include page="../common/LeftNav.jsp"></jsp:include>
        <div id="content">
	        <!-- Page Content Holder -->
			<jsp:include page="../common/TopNav.jsp"></jsp:include> 
            <c:choose>
				<c:when test="${edit}">
					<h2>修改個人資訊</h2>
				</c:when>
				<c:otherwise>
					<h2>註冊個人資訊</h2>
				</c:otherwise>
			</c:choose>
            <form:form method="POST" modelAttribute="user" class="form-horizontal">
			<form:input type="hidden" path="id" id="id"/>
				<div class="form-row">
                    <div class="form-group col-md-6">
                        <label for="ssoId">帳號</label>
                       	<c:choose>
							<c:when test="${edit}">
								<form:input type="text" path="ssoId" id="ssoId" class="form-control input-sm" disabled="true"/>
							</c:when>
							<c:otherwise>
								<form:input type="text" path="ssoId" id="ssoId" class="form-control input-sm" />
								<div class="has-error">
									<form:errors path="ssoId" class="text-danger"/>
								</div>
							</c:otherwise>
						</c:choose>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="password">密碼</label>
                        <form:input type="password" path="password" id="password" class="form-control"/>
                        <div class="has-error">
							<form:errors path="password" class="text-danger"/>
						</div>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group col-md-6">
                        <label for="name">姓名</label>
                        <form:input type="text" path="name" id="name" class="form-control"/>
                        <div class="has-error">
							<form:errors path="name" class="text-danger"/>
						</div>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="phoneNumber">電話</label>
                        <form:input type="text" path="phoneNumber" id="phoneNumber" class="form-control"/>
                        <div class="has-error">
							<form:errors path="phoneNumber" class="text-danger"/>
						</div>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group col-md-6">
                        <label for="email">Email</label>
                        <form:input type="text" path="email" id="email" class="form-control" />
                        <div class="has-error">
							<form:errors path="email" class="text-danger"/>
						</div>
                    </div>
                    <div class="form-group col-6">
                        <label for="userProfiles">權限</label>
                        <form:select path="userProfiles" items="${roles}" multiple="true" itemValue="id" itemLabel="type" class="form-control"/>
                        <div class="has-error">
							<form:errors path="userProfiles" class="text-danger"/>
						</div>
                    </div>
                </div>
                <div class="mt-5">
                    <c:choose>
						<c:when test="${edit}">
							<input type="submit" value="更新" class="btn btn-primary btn-sm"/>
							<input type="button" value="取消" class="btn btn-primary btn-sm" onclick="location.href='<c:url value='/main' />'">
						</c:when>
						<c:otherwise>
							<input type="submit" value="註冊" class="btn btn-primary btn-sm"/>
							<input type="button" value="取消" class="btn btn-primary btn-sm" onclick="location.href='<c:url value='/admin/usersList' />'">
						</c:otherwise>
					</c:choose>
                </div>
            </form:form>
        </div>
    </div>
</body>
</html>
