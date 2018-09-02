<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<jsp:directive.page import="java.util.*"/>
<jsp:directive.page import="org.springframework.web.util.HtmlUtils"/>
<jsp:directive.page import="org.springframework.web.util.JavaScriptUtils"/>

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
<script
	src="${pageContext.request.contextPath}/js/jquery.fileDownload.js"></script>
	
<%
	String var01 = "<input type=\"text\" value=\"aaaa\" />";
	String var1 = HtmlUtils.htmlEscape(var01);
	String var2 = JavaScriptUtils.javaScriptEscape(var01);
	out.println("var1 = " + var1 + "<br/>");
	out.println("var2 = " + var2 + "<br/>");
	out.println("orign = " + var01 + "<br/>");
	
	String var02 = "alert('test');";
	String var3 = HtmlUtils.htmlEscape(var02);
	String var4 = JavaScriptUtils.javaScriptEscape(var02);
	out.println("var3 = " + var3 + "<br/>");
	out.println("var4 = " + var4 + "<br/>");
	out.println("orign = " + var02 + "<br/>");
%>
	
<script type="text/javascript">
	function test(){
		alert("<%=var4%>");
	}
	function testHtmlAndJavascript(){
		var html = "abcd測試12345<br/>&nbsp;\'\"";
		var javascript = "abcd測試12345<br/>&nbsp;\'\"";
		var jqxhr = $.get("/FoodMenu/HtmlJavascriptEscapeServlet", 
			{html:html, javascript:javascript},
			function(data){
				alert(JSON.stringify(data));
			}
		);
		jqxhr.fail(function() {
			for(var ii = 0 ; ii < arguments.length; ii ++){
				alert(JSON.stringify(arguments[ii]));
			}
		});
	}
	function testHtmlAndJavascriptRedirect(){
		var html = "abcd測試12345<br/>&nbsp;\'\"";
		var javascript = "abcd測試12345<br/>&nbsp;\'\"";
		window.location = "http://localhost:8080/FoodMenu/HtmlJavascriptEscapeServlet?html=" + html + "&javascript=" + javascript; 
	}
</script>
<title></title>
</head>
<body>
	<input type="button" value="test" onclick="test()" />
	<input type="button" value="testHtml&Javascript" onclick="testHtmlAndJavascript()" />
	<input type="button" value="testHtml&Javascript" onclick="testHtmlAndJavascriptRedirect()" />
</body>
</html>