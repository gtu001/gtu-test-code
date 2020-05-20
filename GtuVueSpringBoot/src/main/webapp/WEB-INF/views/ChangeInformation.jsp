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
    
	<%@include file="common/headCommon.jsp" %>
	<title>ChangeInformation</title>
</head>
<body>
    <div class="wrapper">
        <!-- Sidebar Holder -->
        <jsp:include page="common/LeftNav.jsp"></jsp:include>

        <!-- Page Content Holder -->
        <div id="content"> 
            <jsp:include page="common/TopNav.jsp"></jsp:include>
            <h2>修改個人資訊</h2>
            <form:form method="POST" modelAttribute="user" class="form-horizontal">
			<form:input type="hidden" path="id" id="id"/>
                <div class="form-row">
                    <div class="form-group col-md-6">
                        <label for="firstName">First Name</label>
                        <form:input type="text" path="firstName" id="firstName" class="form-control"/>
                        <div class="has-error">
							<form:errors path="firstName" class="help-inline"/>
						</div>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="lastName">Last Name</label>
                        <form:input type="text" path="lastName" id="lastName" class="form-control"/>
                        <div class="has-error">
							<form:errors path="lastName" class="help-inline"/>
						</div>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group col-md-6">
                        <label for="ssoId">SSO ID</label>
                       	<c:choose>
							<c:when test="${edit}">
								<form:input type="text" path="ssoId" id="ssoId" class="form-control input-sm" disabled="true"/>
							</c:when>
							<c:otherwise>
								<form:input type="text" path="ssoId" id="ssoId" class="form-control input-sm" />
								<div class="has-error">
									<form:errors path="ssoId" class="help-inline"/>
								</div>
							</c:otherwise>
						</c:choose>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="password">Password</label>
                        <form:input type="password" path="password" id="password" class="form-control"/>
                        <div class="has-error">
							<form:errors path="password" class="help-inline"/>
						</div>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group col-md-6">
                        <label for="email">Email</label>
                        <form:input type="text" path="email" id="email" class="form-control" />
                        <div class="has-error">
							<form:errors path="email" class="help-inline"/>
						</div>
                    </div>
                    <div class="form-group col-6">
                        <label for="userProfiles">Roles</label>
                        <form:select path="userProfiles" items="${roles}" multiple="true" itemValue="id" itemLabel="type" class="form-control"/>
                        <div class="has-error">
							<form:errors path="userProfiles" class="help-inline"/>
						</div>
                    </div>
                <div class="mt-5">
                    <c:choose>
						<c:when test="${edit}">
							<input type="submit" value="Update" class="btn btn-primary btn-sm"/> <a href="<c:url value='/list' />" class="btn btn-primary btn-sm">Cancel</a>
						</c:when>
						<c:otherwise>
							<input type="submit" value="Register" class="btn btn-primary btn-sm"/> <a href="<c:url value='/list' />">Cancel</a>
						</c:otherwise>
					</c:choose>
                </div>
            </form:form>
        </div>
    </div>
</body>
</html>
