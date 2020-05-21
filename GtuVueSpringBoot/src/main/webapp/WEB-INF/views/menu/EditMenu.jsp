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

    <%@include file="../common/headCommon.jsp" %>

    <title>EditMenu</title>
</head>

<body>
    <div class="wrapper">
        <!-- Sidebar Holder -->
        <jsp:include page="../common/LeftNav.jsp"></jsp:include>

        <!-- Page Content Holder -->
        <div id="content">
            <jsp:include page="../common/TopNav.jsp"></jsp:include>
            <c:choose>
				<c:when test="${edit}">
					<h2>修改清單資訊</h2>
				</c:when>
				<c:otherwise>
					<h2>新增清單資訊</h2>
				</c:otherwise>
			</c:choose>
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
							<input type="submit" value="更新" class="btn btn-primary btn-sm"/>
							<input type="button" value="取消" class="btn btn-primary btn-sm" onclick="location.href='<c:url value='/menulist' />'">
						</c:when>
						<c:otherwise>
							<input type="submit" value="註冊" class="btn btn-primary btn-sm"/>
							<input type="button" value="取消" class="btn btn-primary btn-sm" onclick="location.href='<c:url value='/menulist' />'">
						</c:otherwise>
					</c:choose>
                </div>
            </form:form>
        </div>
    </div>
</body>

</html>