<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">

<%@include file="common/headCommon.jsp"%>

<title>CreateMenu</title>
</head>

<body>
	<div class="wrapper">
		<!-- Sidebar Holder -->
		<jsp:include page="common/LeftNav.jsp"></jsp:include>

		<!-- Page Content Holder -->
		<div id="content">
			<jsp:include page="common/TopNav.jsp"></jsp:include>
			<h2>新增Menu</h2>
			<div class="form-row mt-5">
				<div class="form-group col-auto">
					<label for="inputEmail4">MenuName</label> 
					<input type="Text" class="form-control" id="inputEmail4">
				</div>
				<div class="form-group col-auto ">
					<label for="inputEmail4">MenuUrl</label> 
					<input type="Text" class="form-control" id="inputEmail4">
				</div>
			</div>
			<div class="form-row">
				<div class="form-group col-auto">
					<label for="inputEmail4">MenuDesc
					</label> <input type="Text" class="form-control" id="inputEmail4">
				</div>
				<div class="form-group col-auto">
					<label for="inputEmail4">FathID</label> 
					<input type="Text" class="form-control" id="inputEmail4">
				</div>
			</div>
			<button type="submit" class="btn btn-primary">新增</button>
		</div>
	</div>

</body>

</html>