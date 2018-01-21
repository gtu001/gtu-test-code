<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<h1>頁面導向錯誤</h1>
<br />
exception key : <html:errors />
<br />
<textarea id="exceptionStackArea" rows="50"
	style="width: 100%; display: visible">
  <%
      if (request.getAttribute("exceptionStack") != null) {
          out.println(request.getAttribute("exceptionStack"));
      }
  %>
</textarea>