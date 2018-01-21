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
<script src="${pageContext.request.contextPath}/js/jquery.blockUI.js"></script>

<script type="text/javascript">
	function showLoadMsg(arg1) {
		$.blockUI({
			message : (arg1) ? arg1 : '處理中...',
			//timeout:0,
			css : {
				border : 'none',
				padding : '15px',
				backgroundColor : '#000',
				'-webkit-border-radius' : '10px',
				'-moz-border-radius' : '10px',
				'-o-border-radius' : '10px',
				'border-radius' : '10px',
				opacity : .5,
				color : '#fff'
			}
		});
		setTimeout(function(){
			$.unblockUI();
		}, 3000);
	}
	function testBlockUI() {
		showLoadMsg();
	}
</script>
<title>Insert title here</title>
</head>
<body>
	<input type="button" value="test" onclick="javascript:testBlockUI();" />
</body>
</html>