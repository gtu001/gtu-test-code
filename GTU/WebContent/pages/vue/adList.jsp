<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.sf.json.JSONArray"%>
<%@ page import="net.sf.json.JSONObject"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<%
	JSONArray jrrAd = null;
	JSONObject joAd = null;
	jrrAd = JSONArray.fromObject(request.getSession().getAttribute("adlist"));
%>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta http-equiv="X-UA-Compatible" content="ie=edge">
	<link rel="stylesheet" href="css2/bootstrap.min.css">
	<link rel="stylesheet" href="css2/font-awesome.min.css">
	<script src="js2/vue.min.js"></script>
	<title>上海商銀</title>

<script>
	function doCreateAd() {
		document.location.href = "createAd";
	}
</script>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<div id="app" class="col-md-12 mt-3">
				<section class="mb-3">
					<button type="button" class="btn btn-primary"
						onclick="doCreateAd()">
						<i class="fa fa-plus" aria-hidden="true"> </i> 新增廣告
					</button>
					<span class="ml-3"><small>創建新廣告，或由下表中選擇。</small></span>
				</section>
				<h2 class="mb-3">
					<i class="fa fa-flag" aria-hidden="true"></i> 廣告列表
				</h2>
				
				<div class="table-responsive">
					<table class="table table-striped">
						<thead>
							<tr>
								<th>#</th>
								<th>廣告名稱</th>
								<th>廣告區塊</th>
								<th>開始日期</th>
								<th>結束日期</th>
								<th>創建者</th>
								<th>審核者</th>
								<th>審核狀態</th>
							</tr>
						</thead>
						<tbody>

							<c:forEach var="item" varStatus="idx" items="${adLst}">
							
								<tr>
									<td>${idx.index + 1}<hidden name="${idx.index}" value="${item.adprjid}" /></td>
									<td>${item.adprjname}</td>
									<td>${item.blockname}</td>
									<td>${item.stimeFix}</td>
									<td>${item.etimeFix}</td>
									<td>${item.creater}</td>
									<td>${item.approver}</td>
									<td>${item.statusFix}</td>
								</tr>
							
							</c:forEach>

						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</body>
<script src="js2/popper.min.js"></script>
<script src="js2/jquery-3.2.1.min.js"></script>
<script src="js2/popper.min.js"></script>
<script src="js2/Business.js"></script>
<script src="js2/main.js"></script>
</html>
