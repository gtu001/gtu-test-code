<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>
<%@ page import="java.sql.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.staffchannel.service.WorkdayServiceImpl" %>

<html>

<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<%@include file="../common/headCommon.jsp"%>
<title>UserList</title>
</head>

<body>
	<div class="wrapper">
		<!-- Sidebar Holder -->
		<jsp:include page="../common/LeftNav.jsp"></jsp:include>
		<div id="content">
			<!-- Page Content Holder -->
			<jsp:include page="../common/TopNav.jsp"></jsp:include>
			<h2>工作日管理</h2>

			<form:form id="form1" method="POST" modelAttribute="workdays" class="form-horizontal">
			<div class="form-row">
				<div class="form-group col-md-6">
					<label for="year">年</label>
					<form:input type="text" path="year" id="year" class="form-control" />
					<div class="has-error">
						<form:errors path="year" class="text-danger" />
					</div>
				</div>
				<div class="form-group col-md-6">
					<label for="month">月</label>
					<form:input type="text" path="month" id="month"
						class="form-control" />
					<div class="has-error">
						<form:errors path="month" class="text-danger" />
					</div>
				</div>
			</div>
			
			<div class="form-row">
				<div class="form-group col-md-6">
					<input type="button" value="查詢" class="btn btn-primary btn-sm" onclick="javascript:doQuery();">
				</div>
				<div class="form-group col-md-6">
				</div>
			</div>

			<table class="table table-hover">
				<thead>
					<tr>
						<th scope="col">ID</th>
						<th scope="col">日期</th>
						<th width="200">假日類型</th>
						<th width="200">註記</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${workdays}" var="workday" varStatus="loop">
						<tr>
							<td>${workday.seq}</td>
							<td>
								<fmt:formatDate value="${workday.datetime}" pattern="yyyy-MM-dd" />
							</td>
							<td><input type="text"
								name="workdays[${loop.index}].holidayType"
								value="${workday.holidayType}"></input></td>
							<td><input type="text" name="workdays[${loop.index}].remark"
								value="${workday.remark}"></input></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			
			<input type="button" value="儲存" class="btn btn-primary btn-sm" onclick="javascript:doSave();">
			</form:form>
		</div>
	</div>
	
	<script type="javascript">
		function doQuery() {
			window.href = "/workday/mainPage-" + $("#year").val() + "-" + $("#month").val();
		}
	
		function doSave() {
			$("#form1").submit();
		}
	</script>
</body>
</html>