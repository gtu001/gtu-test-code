<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ page import="java.util.Enumeration"%>
<html>
<head>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/default.css" />
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/styles.css" />
	
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/application.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery-1.7.1.js"></script>

	<script type="text/javascript">
	//<![CDATA[
		$(document).ready(function(){
			//alert('jquery init ok!!');
			$("html body #searchBar").bind("keypress", function(){
				var searchVal = $(this).val();
				if(searchVal == null || searchVal == ''){
					$("html .result").find("tr").each(function(index){
						$(this).show();
					});
				}else{
					$("html .result").find("tr").each(function(index){
						if($(this).attr("name") != undefined){
							return;
						}
						if($(this).text().toLowerCase().indexOf(searchVal.toLowerCase()) == -1){
							$(this).hide();
						}else{
							$(this).show();
						}
					});
				}
			});
			$("html body #searchBar").trigger("keypress");
		});
	//]]>
	</script>
</head>
<body>
	
	<br/>
	
	<c:out value="search bar : " /><input type="text" id="searchBar" size="20" class="fieldValueD" />
	
	<br/>

	<table class="result" border="1">
		<tr name="unmodified">
			<td colspan="2"><font color="RED">REQUEST METHOD</font></td>
		</tr>
		<%
		   out.println("<tr><td>request.getMethod</td><td>" + request.getMethod() + "</td></tr>");
		   out.println("<tr><td>request.getAuthType</td><td>" + request.getAuthType() + "</td></tr>");
		   out.println("<tr><td>request.getCookies</td><td>" + request.getCookies() + "</td></tr>");
		   out.println("<tr><td>request.getHeaderNames</td><td>" + request.getHeaderNames() + "</td></tr>");
		   out.println("<tr><td>request.getPathInfo</td><td>" + request.getPathInfo() + "</td></tr>");
		   out.println("<tr><td>request.getPathTranslated</td><td>" + request.getPathTranslated() + "</td></tr>");
		   out.println("<tr><td>request.getContextPath</td><td>" + request.getContextPath() + "</td></tr>");
		   out.println("<tr><td>request.getQueryString</td><td>" + request.getQueryString() + "</td></tr>");
		   out.println("<tr><td>request.getRemoteUser</td><td>" + request.getRemoteUser() + "</td></tr>");
		   out.println("<tr><td>request.getUserPrincipal</td><td>" + request.getUserPrincipal() + "</td></tr>");
		   out.println("<tr><td>request.getRequestedSessionId</td><td>" + request.getRequestedSessionId() + "</td></tr>");
		   out.println("<tr><td>request.getRequestURI</td><td>" + request.getRequestURI() + "</td></tr>");
		   out.println("<tr><td>request.getRequestURL</td><td>" + request.getRequestURL() + "</td></tr>");
		   out.println("<tr><td>request.getServletPath</td><td>" + request.getServletPath() + "</td></tr>");
		   out.println("<tr><td>request.getSession</td><td>" + request.getSession() + "</td></tr>");
		   out.println("<tr><td>request.isRequestedSessionIdValid</td><td>" + request.isRequestedSessionIdValid() + "</td></tr>");
		   out.println("<tr><td>request.isRequestedSessionIdFromCookie</td><td>" + request.isRequestedSessionIdFromCookie() + "</td></tr>");
		   out.println("<tr><td>request.isRequestedSessionIdFromURL</td><td>" + request.isRequestedSessionIdFromURL() + "</td></tr>");
		   out.println("<tr><td>request.isRequestedSessionIdFromUrl</td><td>" + request.isRequestedSessionIdFromUrl() + "</td></tr>");
		   out.println("<tr><td>request.getScheme</td><td>" + request.getScheme() + "</td></tr>");
		   out.println("<tr><td>request.getProtocol</td><td>" + request.getProtocol() + "</td></tr>");
		   out.println("<tr><td>request.getInputStream</td><td>" + request.getInputStream() + "</td></tr>");
		   out.println("<tr><td>request.getContentLength</td><td>" + request.getContentLength() + "</td></tr>");
		   out.println("<tr><td>request.getContentType</td><td>" + request.getContentType() + "</td></tr>");
		   out.println("<tr><td>request.getAttributeNames</td><td>" + request.getAttributeNames() + "</td></tr>");
		   out.println("<tr><td>request.getCharacterEncoding</td><td>" + request.getCharacterEncoding() + "</td></tr>");
		   out.println("<tr><td>request.getParameterNames</td><td>" + request.getParameterNames() + "</td></tr>");
		   out.println("<tr><td>request.getParameterMap</td><td>" + request.getParameterMap() + "</td></tr>");
		   out.println("<tr><td>request.getServerName</td><td>" + request.getServerName() + "</td></tr>");
		   out.println("<tr><td>request.getServerPort</td><td>" + request.getServerPort() + "</td></tr>");
		   //out.println("<tr><td>request.getReader</td><td>" + request.getReader() + "</td></tr>");//會錯!!
		   out.println("<tr><td>request.getRemoteAddr</td><td>" + request.getRemoteAddr() + "</td></tr>");
		   out.println("<tr><td>request.getRemoteHost</td><td>" + request.getRemoteHost() + "</td></tr>");
		   out.println("<tr><td>request.getLocale</td><td>" + request.getLocale() + "</td></tr>");
		   out.println("<tr><td>request.getLocales</td><td>" + request.getLocales() + "</td></tr>");
		   out.println("<tr><td>request.isSecure</td><td>" + request.isSecure() + "</td></tr>");
		   out.println("<tr><td>request.getRemotePort</td><td>" + request.getRemotePort() + "</td></tr>");
		   out.println("<tr><td>request.getLocalName</td><td>" + request.getLocalName() + "</td></tr>");
		   out.println("<tr><td>request.getLocalAddr</td><td>" + request.getLocalAddr() + "</td></tr>");
		   out.println("<tr><td>request.getLocalPort</td><td>" + request.getLocalPort() + "</td></tr>");
		%>
		<tr name="unmodified">
			<td colspan="2"><font color="RED">REQUEST ATTRIBUTE</font></td>
		</tr>
		<tr name="unmodified">
			<td>key</td>
			<td>value</td>
		</tr>
		<%
			for(Enumeration enu = request.getAttributeNames(); enu.hasMoreElements() ;){
			    String key = (String)enu.nextElement();
			    Object value = request.getAttribute(key);
			    out.println("<tr><td>" + key + "</td><td>" + value + "</td></tr>");
			}
		%>
		<tr name="unmodified">
			<td colspan="2"><font color="RED">SESSION METHOD</font></td>
		</tr>
		<%
	        out.println("<tr><td>session.getId</td><td>" + session.getId() + "</td></tr>");
	        out.println("<tr><td>session.getCreationTime</td><td>" + session.getCreationTime() + "</td></tr>");
	        out.println("<tr><td>session.getLastAccessedTime</td><td>" + session.getLastAccessedTime() + "</td></tr>");
	        out.println("<tr><td>session.getServletContext</td><td>" + session.getServletContext() + "</td></tr>");
	        out.println("<tr><td>session.getMaxInactiveInterval</td><td>" + session.getMaxInactiveInterval() + "</td></tr>");
	        out.println("<tr><td>session.getSessionContext</td><td>" + session.getSessionContext() + "</td></tr>");
	        out.println("<tr><td>session.getAttributeNames</td><td>" + session.getAttributeNames() + "</td></tr>");
	        out.println("<tr><td>session.getValueNames</td><td>" + session.getValueNames() + "</td></tr>");
	        out.println("<tr><td>session.isNew</td><td>" + session.isNew() + "</td></tr>");
		%>
		<tr name="unmodified">
			<td colspan="2"><font color="RED">SESSION ATTRIBUTE</font></td>
		</tr>
		<tr name="unmodified">
			<td>key</td>
			<td>value</td>
		</tr>
		<%
			for(Enumeration enu = session.getAttributeNames(); enu.hasMoreElements() ;){
			    String key = (String)enu.nextElement();
			    Object value = session.getAttribute(key);
			    out.println("<tr><td>" + key + "</td><td>" + value + "</td></tr>");
			}
		%>
		<tr name="unmodified">
			<td colspan="2"><font color="RED">SERVLET_CONTEXT METHOD</font></td>
		</tr>
		<%
			javax.servlet.ServletContext context = session.getServletContext();
	        out.println("<tr><td>context.getContextPath</td><td>" + context.getContextPath() + "</td></tr>");
	        out.println("<tr><td>context.getMajorVersion</td><td>" + context.getMajorVersion() + "</td></tr>");
	        out.println("<tr><td>context.getMinorVersion</td><td>" + context.getMinorVersion() + "</td></tr>");
	        out.println("<tr><td>context.getServlets</td><td>" + context.getServlets() + "</td></tr>");
	        out.println("<tr><td>context.getServletNames</td><td>" + context.getServletNames() + "</td></tr>");
	        out.println("<tr><td>context.getServerInfo</td><td>" + context.getServerInfo() + "</td></tr>");
	        out.println("<tr><td>context.getInitParameterNames</td><td>" + context.getInitParameterNames() + "</td></tr>");
	        out.println("<tr><td>context.getAttributeNames</td><td>" + context.getAttributeNames() + "</td></tr>");
	        out.println("<tr><td>context.getServletContextName</td><td>" + context.getServletContextName() + "</td></tr>");
		%>
		<tr name="unmodified">
			<td colspan="2"><font color="RED">SERVLET_CONTEXT ATTRIBUTE</font></td>
		</tr>
		<tr name="unmodified">
			<td>key</td>
			<td>value</td>
		</tr>
		<%
			for(Enumeration enu = session.getServletContext().getAttributeNames(); enu.hasMoreElements() ;){
			    String key = (String)enu.nextElement();
			    Object value = session.getServletContext().getAttribute(key);
			    out.println("<tr><td>" + key + "</td><td>" + value + "</td></tr>");
			}
		%>
	</table>
</body>
</html>
<%
    final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger("index.jsp");
%>
<%!
    private void saveLog(String message) {
        try {
            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy_MM_dd");
            String fileName = "log_" + df.format(new java.util.Date()) + ".txt";
			String filePathName = System.getProperty("user.home") + "\\Desktop\\" + fileName;            
            java.io.FileOutputStream out = new java.io.FileOutputStream(filePathName, true);
            message += "\n";
            out.write(message.getBytes("BIG5"));
            out.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
%>
