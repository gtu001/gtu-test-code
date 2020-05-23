<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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

<%@include file="../common/headCommon.jsp"%>
<title>修改角色資訊</title>
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
					<h2>修改角色資訊</h2>
				</c:when>
				<c:otherwise>
					<h2>新增角色資訊</h2>
				</c:otherwise>
			</c:choose>
			<form:form method="POST" modelAttribute="userProfile"
				class="form-horizontal">
				<form:input type="hidden" path="id" id="id" />
				<div class="form-row">
					<div class="form-group col-md-6">
						<label for="ssoId">角色名稱</label>
						<c:choose>
							<c:when test="${edit}">
								<form:input type="text" path="type" id="type"
									class="form-control input-sm" disabled="true" />
							</c:when>
							<c:otherwise>
								<form:input type="text" path="type" id="type"
									class="form-control input-sm" />
								<div class="has-error">
									<form:errors path="type" class="text-danger" />
								</div>
							</c:otherwise>
						</c:choose>
					</div>
				</div>

				<div class="form-group col-6 mt-5">
					<label for="exampleFormControlSelect2">清單管理</label>
					<div class="overflow-auto" style="height: 130px">
						<c:forEach items="${menuexisting}" var="existing" varStatus="loop">
							<c:set var="menu_name" value="menuList[${loop.index}]" />
							<input type="checkbox" path="${menu_name}.id" name="${menu_name}.id" value="${existing.id}" ${userHasMenusChecked[existing.id]}>
							<label class="form-check-label" for="${menu_name}.id">${existing.menuName}</label>
							<input type="hidden" value="${existing.menuName}" name="${menu_name}.menuName"  path="${menu_name}.menuName"></input>
							<input type="hidden" value="${existing.menuUrl}" name="${menu_name}.menuUrl"  path="${menu_name}.menuUrl"></input>
							<input type="hidden" value="${existing.menuDesc}" name="${menu_name}.menuDesc"  path="${menu_name}.menuDesc"></input>
							<input type="hidden" value="${existing.fatherId}" name="${menu_name}.fatherId" path="${menu_name}.fatherId"></input>
						
							<c:if test="${existing.subMenu.size()>0 }">
								<ul>
									<c:forEach items="${existing.subMenu}" var="subexisting" varStatus="loop">
										<c:set var="sub_menu_name" value="subMenu[${loop.index}]" />
										<li><input type="checkbox" path="${menu_name}.${sub_menu_name}.id" name="${menu_name}.${sub_menu_name}.id" value="${subexisting.id}" ${userHasMenusChecked[subexisting.id]}>
											<label class="form-check-label" for="${menu_name}.${sub_menu_name}.id">${subexisting.menuName}</label>
											<input type="hidden" value="${subexisting.menuName}" name="${menu_name}.${sub_menu_name}.menuName"  path="${menu_name}.${sub_menu_name}.menuName"></input>
											<input type="hidden" value="${subexisting.menuUrl}" name="${menu_name}.${sub_menu_name}.menuUrl"  path="${menu_name}.${sub_menu_name}.menuUrl"></input>
											<input type="hidden" value="${subexisting.menuDesc}" name="${menu_name}.${sub_menu_name}.menuDesc"  path="${menu_name}.${sub_menu_name}.menuDesc"></input>
											<input type="hidden" value="${subexisting.fatherId}" name="${menu_name}.${sub_menu_name}.fatherId" path="${menu_name}.${sub_menu_name}.fatherId"></input>
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

				<div class="mt-5">
					<c:choose>
						<c:when test="${edit}">
							<input type="submit" value="更新" class="btn btn-primary btn-sm" />
							<input type="button" value="取消" class="btn btn-primary btn-sm"
								onclick="location.href='<c:url value='/admin/rolesList' />'">
						</c:when>
						<c:otherwise>
							<input type="submit" value="新增" class="btn btn-primary btn-sm" />
							<input type="button" value="取消" class="btn btn-primary btn-sm"
								onclick="location.href='<c:url value='/admin/rolesList' />'">
						</c:otherwise>
					</c:choose>
				</div>
			</form:form>
		</div>
	</div>
</body>
</html>
