<%@ page pageEncoding="UTF-8"%>

<%-- <%@ include file="/SurveyWeb/pages/common/page_top.jsp" %> --%>
<%@ include file="../common/page_top.jsp" %>

<html>
<head>
<script type="text/javascript">
	function getForm() {
		var form = $("form[name=dynaCustomerListForm]");
		return form;
	}
	$(document).ready(function(){
		$("input[name=doSubmit]").click(function(){
			getForm().attr("action", "${pageContext.request.contextPath}/SqlQuery.do?method=test");
			getForm().submit();
		});
	});
</script>
</head>
<body>
	<h1></h1>

	<jsp:include page="../common/messages.jsp" />

	<html:form action="/SqlQuery.do?method=test">
		<html:text property="textField"></html:text>

		<html:button property="doSubmit" value="送出"></html:button>
	</html:form>

	<jsp:include page="../common/page_buttom.jsp" />

</body>
</html>