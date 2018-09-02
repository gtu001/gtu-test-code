<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%-- <script src="${pageContext.request.contextPath}/js/jquery-2.1.1.js"></script> --%>

<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script
	src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.2/jquery-ui.min.js"></script>
<link
	href="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.2/themes/ui-lightness/jquery-ui.css"
	rel="stylesheet" type="text/css" />

<style>
.red-loader {
	position: fixed;
	width: 100%;
	height: 100%;
	top: 0;
	z-index: 10000;
	background: rgba(255, 255, 255, 0.8);
	display: none;
}

.red-loader img {
	position: absolute;
	width: 50px;
	height: 50px;
	left: 50%;
	top: 50%;
	margin: -25px 0 0 -25px;
}
</style>

<script type="text/javascript">
	function openLoader() {
		$('#red-loader').css('display', 'block');
	};
	
	function closeLoader() {
		$('#red-loader').fadeOut("slow", function() {
	
		});
	};
	
	function test(){
		openLoader();
		setTimeout(function(){
			closeLoader();
		}, 5000);
	}
</script>
<title></title>
</head>
<body>
	<input type="button" value="test" onclick="test()" />

	<div class="red-loader" id="red-loader">
		<img src="/FoodMenu/images2/red-loader.gif">
	</div>
</body>
</html>