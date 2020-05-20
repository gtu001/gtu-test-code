<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>
<%@ page import="java.sql.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<%@include file="../common/headCommon.jsp"%>
<title>待審核案件</title>
<style>
.table .thead-light th {
	color: #ffffff;
	background-color: #386F99;
}

.table-borderless td, .table-borderless th {
	border: 0;
}

.panel-default {
	padding: 13px;
	border: solid 1px #DDDDDD;
	box-shadow: 0 0 1px 1px #DDDDDD;
}

.table thead tr th {
	vertical-align: middle;
}

.table tbody tr td {
	vertical-align: middle;
	width: 10%
}

img {
	max-width: 100%;
}

.input-group-text {
	border-radius: 5px;
	color: #ffffff;
	background-color: #386F99;
	padding-top: 5px;
	padding-right: 5px;
	padding-bottom: 5px;
	padding-left: 5px;
}

.form-inline>* {
	margin: 5px 3px;
}

.form-control {
	width: 70%;
}

.input-group {
	width: 20%;
}

.input-group-text {
	font-size: 11px;
}

.form-control {
	font-size: 11px;
}

.input-group-text {
	font-size: 11px;
}

.selectpicker {
	font-size: 11px;
}

.custom-select {
	border-top-left-radius: 0px;
	border-top-right-radius: 3px;
	border-bottom-right-radius: 3px;
	border-bottom-left-radius: 0px;
}

.icon-play {
	background-image: url(/GtuVueSpringBoot/static/jpg/reviewIcon2.png);
	background-size: cover;
	display: inline-block;
	height: 15px;
	width: 15px;
}
</style>
</head>
<body>
	<div class="wrapper">
		<!-- Sidebar Holder -->
		<jsp:include page="../common/LeftNav.jsp"></jsp:include>
		<div id="content">
			<!-- Page Content Holder -->
			<jsp:include page="../common/TopNav.jsp"></jsp:include>
			<div style="margin: 10px">
				<form class="form-inline">
					<div class="form-group">
						<div class="input-group">
							<div class="input-group-prepend">
								<span class="input-group-text" id="basic-addon3">起始日期</span>
							</div>
							<div class='input-group date' id='datetimepicker1'>
								<input type='text' class="form-control custom-select" size="14" />
								<span class="input-group-addon"> <span
									class="glyphicon glyphicon-calendar"></span>
								</span>
							</div>
							<script type="text/javascript">
								$(function() {
									$('#datetimepicker1').datetimepicker();
								});
							</script>
						</div>
					</div>
					到
					<div class="form-group">
						<div class="input-group">
							<div class="input-group-prepend">
								<span class="input-group-text" id="basic-addon3">截止日期</span>
							</div>
							<div class='input-group date' id='datetimepicker2'>
								<input type='text' class="form-control custom-select" size="14" />
								<span class="input-group-addon"> <span
									class="glyphicon glyphicon-calendar"></span>
								</span>
							</div>
							<script type="text/javascript">
								$(function() {
									$('#datetimepicker2').datetimepicker();
								});
							</script>
						</div>
					</div>
					<div class="form-group">
						<div class="input-group">
							<div class="input-group-prepend">
								<span class="input-group-text" id="basic-addon3">狀態</span>
							</div>
							<select class="selectpicker custom-select"
								aria-describedby="basic-addon3">
								<option value="1.13.8" selected>--- 全選 ---</option>
								<option value="1.13.8">1.13.8</option>
								<option value="1.8.1">1.8.1</option>
								<option value="1.8.0">1.8.0</option>
								<option value="1.7.7">1.7.7</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<div class="input-group">
							<div class="input-group-prepend">
								<span class="input-group-text" id="basic-addon3">營業員</span>
							</div>
							<select class="selectpicker custom-select"
								aria-describedby="basic-addon3">
								<option value="1.13.8" selected>--- 全選 ---</option>
								<option value="1.13.8">1.13.8</option>
								<option value="1.8.1">1.8.1</option>
								<option value="1.8.0">1.8.0</option>
								<option value="1.7.7">1.7.7</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<div class="input-group">
							<div class="input-group-prepend">
								<span class="input-group-text" id="basic-addon3">分公司</span>
							</div>
							<select class="selectpicker custom-select"
								aria-describedby="basic-addon3">
								<option value="1 s.13.8" selected>--- 全選 ---</option>
								<option value="1.13.8">1.13.8</option>
								<option value="1.8.1">1.8.1</option>
								<option value="1.8.0">1.8.0</option>
								<option value="1.7.7">1.7.7</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<div class="input-group">
							<button type="button" class="btn">搜尋</button>
						</div>
					</div>
				</form>
				<div class="panel panel-default"
					style="-moz-border-radius: 5px; -webkit-border-radius: 5px; -ie-border-radius: 5px; -opera-border-radius: 5px; -chrome-border-radius: 5px; border-radius: 5px;">
					<div class="panel-heading"></div>
					<div class="panel-body">
						<div class="table-responsive">
							<table
								class="table table-bordered table-striped table-borderless">
								<thead class="thead-light">
									<tr>
										<th>客戶名稱</th>
										<th>營業員</th>
										<th>證件資料</th>
										<th>身份認證</th>
										<th>憑證申請</th>
										<th>文件審核</th>
										<th>交割銀行</th>
										<th>備註</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td>1</td>
										<td>張三</td>
										<td><button class="btn btn-default" onclick="gotoDataPage()">
												<img
													src="<%=request.getContextPath()%>/static/jpg/reviewIcon3.png" />
											</button></td>
										<td><button class="btn btn-default">
												<img
													src="<%=request.getContextPath()%>/static/jpg/reviewIcon3.png" />
											</button></td>
										<td><button class="btn btn-default">
												<img
													src="<%=request.getContextPath()%>/static/jpg/reviewIcon3.png" />
											</button></td>
										<td><button class="btn btn-default">
												<img
													src="<%=request.getContextPath()%>/static/jpg/reviewIcon3.png" />
											</button></td>
										<td><button class="btn btn-default">
												<img
													src="<%=request.getContextPath()%>/static/jpg/reviewIcon3.png" />
											</button></td>
										<td><input type="text" class="form-control"></td>
									</tr>
									<tr>
										<td>2</td>
										<td>李四</td>
										<td><button class="btn btn-default">
												<img
													src="<%=request.getContextPath()%>/static/jpg/reviewIcon3.png" />
											</button></td>
										<td><button class="btn btn-default">
												<img
													src="<%=request.getContextPath()%>/static/jpg/reviewIcon3.png" />
											</button></td>
										<td><button class="btn btn-default">
												<img
													src="<%=request.getContextPath()%>/static/jpg/reviewIcon3.png" />
											</button></td>
										<td><button class="btn btn-default">
												<img
													src="<%=request.getContextPath()%>/static/jpg/reviewIcon3.png" />
											</button></td>
										<td><button class="btn btn-default">
												<img
													src="<%=request.getContextPath()%>/static/jpg/reviewIcon3.png" />
											</button></td>
										<td><input type="text" class="form-control"></td>
									</tr>
									<tr>
										<td>3</td>
										<td>王五</td>
										<td><button class="btn btn-default">
												<img
													src="<%=request.getContextPath()%>/static/jpg/reviewIcon3.png" />
											</button></td>
										<td><button class="btn btn-default">
												<img
													src="<%=request.getContextPath()%>/static/jpg/reviewIcon3.png" />
											</button></td>
										<td><button class="btn btn-default">
												<img
													src="<%=request.getContextPath()%>/static/jpg/reviewIcon3.png" />
											</button></td>
										<td><button class="btn btn-default">
												<img
													src="<%=request.getContextPath()%>/static/jpg/reviewIcon3.png" />
											</button></td>
										<td><button class="btn btn-default">
												<img
													src="<%=request.getContextPath()%>/static/jpg/reviewIcon3.png" />
											</button></td>
										<td><input type="text" class="form-control"></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					<div class="panel-footer">
						<div class="row">
							<div class="col-md-2"></div>
							<div class="col-md-2">
								<img
									src="<%=request.getContextPath()%>/static/jpg/reviewPic1.png">
							</div>
							<div class="col-md-2">
								<img
									src="<%=request.getContextPath()%>/static/jpg/reviewPic2.png">
							</div>
							<div class="col-md-2">
								<img
									src="<%=request.getContextPath()%>/static/jpg/reviewPic3.png">
							</div>
							<div class="col-md-2">
								<img
									src="<%=request.getContextPath()%>/static/jpg/reviewPic4.png">
							</div>
							<div class="col-md-2"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	function gotoDataPage() {
		location.href = "http://127.0.0.1:8080/GtuVueSpringBoot/gotoDataReview";
	};
</script>
</html>