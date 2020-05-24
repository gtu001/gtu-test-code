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
<!-- ============================== -->
	<script defer src="<c:url value='/static/js/jquery.validate.js' />"></script>
	<script defer src="<c:url value='/static/js/localization/messages_zh_TW.js' />"></script>
<!-- ============================== -->
<style>
	span.required::before {
		content: "*";
		color:#FF0000;
	}
</style>

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
			
			<div id="message" class="alert alert-success lead"></div>

			<form:form id="form1" method="POST" modelAttribute="workdays" class="form-horizontal">
			<div class="form-row">
				<div class="form-group col-md-6">
					<label for="year">年</label>
					<input type="text" name="year" id="year" class="form-control" />
				</div>
				<div class="form-group col-md-6">
					<label for="month">月</label>
					<input type="text" name="month" id="month"
						class="form-control" />
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
	
	<script type="text/javascript">
		function doQuery() {
			messageHandler.hide();
			var year = $("#year").val();
			var month = $("#month").val();
			if(!/\d{4}/.test($.trim(year))) {
				messageHandler.text("請輸入年");
				return;
			}
			if(!/\d{1,2}/.test($.trim(month))) {
				messageHandler.text("請輸入月");
				return;
			}
			window.location = "mainPage-" + $("#year").val() + "-" + $("#month").val();
		}
		
		var messageHandler = new function() {
			this.show = function() {
				$("#message").show();
			};
			this.hide =  function() {
				$("#message").hide();
			};
			this.text =  function(text) {
				this.show(true);
				$("#message").text(text);
			};
			this.append = function(text) {
				this.show(true);
				$("#message").text($("#message").text() + " " + text);
			};
			this.hide();
		};
	
		function doSave() {
			$("#form1").submit();
		}
		
		$(document).ready(function(){
		});
	</script>
</body>
</html>