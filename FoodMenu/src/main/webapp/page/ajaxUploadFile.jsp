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
<script src="${pageContext.request.contextPath}/js/jquery-2.1.1.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.form.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		// $("#go_ajs").click(ajs_upload);   
		// 直接 change 就上傳   
		$("#pic").change(ajs_upload);
		$("#go_ajs").click(ajs_upload);
	
		function ajs_upload() {
			$("#ajaxForm").ajaxSubmit({
				beforeSubmit : function() {
				},
				success : function(resp, st, xhr, $form) {
					//alert(resp.result);//也可直接回傳json物件
					var jsonObj = JSON.parse(resp);
					alert(jsonObj.result);
				},
				error : function(xhr, status, error) {
					alert(status + " : " + error);
		        },
			});
		}
	});
	
	function showObj(obj){
		var msg = '';
		for(var ii in obj){
			msg += ii + ".." + obj[ii] + "\n";
		}
		alert(msg);
	}
</script>
<title>Insert title here</title>
</head>
<body>
	<form action="/FoodMenu/AjaxUploadServlet" method="post" id="ajaxForm" enctype="multipart/form-data">
		<input type="file" id="pic" name="pic"><br>
		<button id="go_ajs" type="button">上傳</button>
	</form>
	圖片：
	<br>
	<img src="" id="uploadImg" alt="" />

</body>
</html>
