<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page pageEncoding="UTF-8"%>

<%-- 
<div style="color: red">
	<html:errors />
</div>  
--%>

<logic:messagesPresent message="true">
	<html:messages id="aMsg" message="true">
		<logic:present name="aMsg">
			<!-- Messages -->
			<div class="messages">
				警告 : <bean:write name="aMsg" filter="false" />
			</div>
		</logic:present>
	</html:messages>
</logic:messagesPresent>

<logic:messagesPresent message="false">
	<html:messages id="aMsg" message="false">
		<logic:present name="aMsg">
			<!-- Warnings-->
			<div class="warnings">
				錯誤 : <bean:write name="aMsg" filter="false" />
			</div>
		</logic:present>
	</html:messages>
</logic:messagesPresent>