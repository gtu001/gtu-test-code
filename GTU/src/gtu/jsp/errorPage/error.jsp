<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" isErrorPage="true"%>
<%
/* 沒用到
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="bean"	uri="http://jakarta.apache.org/struts/tags-bean-el"%>
<%@taglib prefix="html"	uri="http://jakarta.apache.org/struts/tags-html-el"%>
<%@taglib prefix="logic" uri="http://struts.apache.org/tags-logic-el"%>
*/
  %>
  
<script type="text/javascript">
//<![CDATA[

//]]>
</script>

<html>
	<head>
		<title>網頁發生錯誤</title>
	</head>
	<body>
		<center>
			<table border="1" width="80%">
				<tr>
					<td>
						<H3>
							例外處理頁面
						</H3>
					</td>
				</tr>
				<tr>
					<td>
						請將錯誤訊息回報管理員
					</td>
				</tr>
				<tr>
					<td>
						錯誤碼：
						<%=request.getAttribute("javax.servlet.error.status_code")%>
						<br>
						訊息：
						<%=request.getAttribute("javax.servlet.error.message")%>
						<br>
						例外：
						<%=request.getAttribute("javax.servlet.error.exception_type")%>
						<br>
					</td>
				</tr>
				
				<tr>
					<td>
						<h6>
						<%
							if (exception != null) {
								out.println(exception.getCause().toString()+"<br/>");
								StackTraceElement[] selemt = exception.getStackTrace();
								for(int ii=0;ii<selemt.length;ii++){
									out.println(selemt[ii].toString());
								}
							}
							
							StackTraceElement[] elements = Thread.currentThread().getStackTrace();
							for(int ii = 0 ; ii < elements.length ; ii ++){
							    out.println(elements[ii]);
							}
						%>
						</h6>
					</td>
				</tr>
			</table>
		</center>
	</body>
</html>
