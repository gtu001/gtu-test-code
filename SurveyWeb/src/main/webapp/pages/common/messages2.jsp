<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page import="org.apache.struts.util.PropertyMessageResources"%>
<%@ page pageEncoding="UTF-8"%>


<script type="text/javascript">
	function initMessages() {
		if($("#showMessageSpan").length == 0){
			return;
		}
		var msg = $("#showMessageSpan").text();
		msg = $.trim(msg).replace(' ', '').replace("\n", '');
		msg = $.trim(msg);
		if (msg != '') {
			alert(msg);
		}
	}
	
	function initErrorMessages() {
		if($("#showErrorMessage").length == 0){
			return;
		}
		var msg = $("#showErrorMessage").text();
		msg = $.trim(msg).replace(' ', '').replace("\n", '');
		msg = $.trim(msg);
		if (msg != '') {
			alert(msg);
		}
	}

	$(document).ready(function() {
		initMessages();
		initErrorMessages();
	});
</script>

<%
    PropertyMessageResources p = (PropertyMessageResources) request.getAttribute("org.apache.struts.action.MESSAGE");
    try {
        java.lang.reflect.Field v = p.getClass().getDeclaredField("messages");
        v.setAccessible(true);
        //out.println(v.get(p));
    } catch (Exception ex) {
        out.println(ex.getMessage());
    }
%>

<logic:messagesPresent message="true">
	<span id="showMessageSpan" style="display: none">
		<html:messages
			id="message" message="true">
			<%
			    out.println(pageContext.getAttribute("message", PageContext.PAGE_SCOPE));
			%>
		</html:messages>
	</span>
</logic:messagesPresent>

<span id="showErrorMessage" style="display: none">
	<html:errors />
</span>

