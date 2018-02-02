<%@ page language="java" contentType="text/html; charset=utf8"
	pageEncoding="utf8"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>

<script type="text/javascript">
	$(document).ready(function(){
		console.log("#. popup_win ready");
		
		var content = decodeURIComponent("<%=request.getAttribute("content")%>");
		$("#show_div").html(content);
		
		var width = "<%=request.getAttribute("width")%>";
		var height = "<%=request.getAttribute("height")%>";
		
		if(width != 0 && height != 0){
			$("#show_div").css("width", width + "px");
			$("#show_div").css("height", height + "px");
		}
	});

	function showData4Mobile(text, width, height){
		console.log("#. showData4Mobile");
		console.log("#. content -> " + text);
		console.log("#. findDiv : " + $("#show_div").length);
		$("#show_div").html(text);
		$("#show_div").css("width", width + "px");
		$("#show_div").css("height", height + "px");
	}
	
	function showData4Window(text){
		console.log("#. showData4Window");
		console.log("#. content -> " + text);
		console.log("#. findDiv : " + $("#show_div").length);
		$("#show_div").html(text);
	}
</script>
</head>
<body>

       	<div id="show_div" style="border-color:#aaaaee;border-width:3px;border-style:solid;padding:5px;background-color: powderblue;">
       	</div>

</body>
</html>