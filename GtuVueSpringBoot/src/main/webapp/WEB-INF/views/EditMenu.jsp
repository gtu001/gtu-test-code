<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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

    <title>EditMenu</title>
</head>

<body>
    <div class="wrapper">
        <!-- Sidebar Holder -->
        <jsp:include page="common/LeftNav.jsp"></jsp:include>

        <!-- Page Content Holder -->
        <div id="content">
            <jsp:include page="common/TopNav.jsp"></jsp:include>
            <h2>修改員工資訊</h2>
            <form:form method="POST" modelAttribute="menu" class="form-horizontal">
			<form:input type="hidden" path="id" id="id"/>
                <div class="form-row">
                    <div class="form-group col-md-6">
                        <label for="menuName">清單名稱</label>
                        <form:input type="text" path="menuName" id="menuName" class="form-control"/>
                        <div class="has-error">
							<form:errors path="menuName" class="help-inline"/>
						</div>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="menuUrl">清單連結</label>
                        <form:input type="text" path="menuUrl" id="menuUrl" class="form-control"/>
                        <div class="has-error">
							<form:errors path="menuUrl" class="help-inline"/>
						</div>
                    </div>
                </div>
                <div class="form-row">
                	<div class="form-group col-md-6">
                        <label for="menuDesc">清單敘述</label>
                        <form:input type="text" path="menuDesc" id="menuDesc" class="form-control"/>
                        <div class="has-error">
							<form:errors path="menuDesc" class="help-inline"/>
						</div>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="fatherId">清單父頁</label>
                        <form:input type="text" path="fatherId" id="fatherId" class="form-control"/>
                        <div class="has-error">
							<form:errors path="fatherId" class="help-inline"/>
						</div>
                    </div>
                </div>
                <div class="mt-5">
                    <c:choose>
						<c:when test="${edit}">
							<input type="submit" value="Update" class="btn btn-primary btn-sm"/> <a href="<c:url value='/Menulist' />" class="btn btn-primary btn-sm">Cancel</a>
						</c:when>
						<c:otherwise>
							<input type="submit" value="Register" class="btn btn-primary btn-sm"/> <a href="<c:url value='/Menulist' />">Cancel</a>
						</c:otherwise>
					</c:choose>
                </div>
            </form:form>
        </div>
    </div>
</body>

</html>