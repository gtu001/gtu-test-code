<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">

<%@include file="common/headCommon.jsp"%>

<title>ManagerEditMenu</title>
</head>

<body>
	<div class="wrapper">
		<!-- Sidebar Holder -->
		<jsp:include page="common/LeftNav.jsp"></jsp:include>

		<!-- Page Content Holder -->
		<div id="content">
			<jsp:include page="common/TopNav.jsp"></jsp:include>
			<h2>Menu權限</h2>
			<form:form method="POST" modelAttribute="user" class="form-horizontal">
				<div class="form-row">
					<div class="form-group col-6 mt-5">
						<label for="exampleFormControlSelect2">Admin</label>
						<div class="overflow-auto" style="height: 130px">
							<c:forEach items="${menuexisting}" var="existing">
							<c:if test="${existing.subMenu.size()>0 }">
								<label class="form-check-label" for="defaultCheck1">${existing.menuName}</label>
								
								<ul>
									<c:forEach items="${existing.subMenu}" var="subexisting">
									<li>
										<input type="checkbox" value="" id="defaultCheck1">
										<label class="form-check-label" for="defaultCheck1">${subexisting.menuName}</label>
									</li>
									</c:forEach>
								</ul>
							</c:if>
							<c:if test="${existing.subMenu==null }">
							<div>
								<input type="checkbox" value="" id="defaultCheck1"> <label
									class="form-check-label" for="defaultCheck1">${existing.menuName}</label>
							</div>
							</c:if>
							</c:forEach>
						</div>
					</div>
					<div class="form-group col-6 mt-5">
						<label for="exampleFormControlSelect2">User</label>
						<div class="overflow-auto" style="height: 130px">
							<c:forEach items="${menuexisting}" var="existing">
							<c:if test="${existing.subMenu.size()>0 }">
								<label class="form-check-label" for="defaultCheck1">${existing.menuName}</label>
								
								<ul>
									<c:forEach items="${existing.subMenu}" var="subexisting">
									<li>
										<input type="checkbox" value="" id="defaultCheck1">
										<label class="form-check-label" for="defaultCheck1">${subexisting.menuName}</label>
									</li>
									</c:forEach>
								</ul>
							</c:if>
							<c:if test="${existing.subMenu==null }">
							<div>
								<input type="checkbox" value="" id="defaultCheck1"> <label
									class="form-check-label" for="defaultCheck1">${existing.menuName}</label>
							</div>
							</c:if>
							</c:forEach>
						</div>
					</div>
				</div>
				<div class="mt-5">
					<button type="button" class="btn btn-primary">確認</button>
				</div>
		</div>
		</form:form>
	</div>
	</div>
</body>

</html>