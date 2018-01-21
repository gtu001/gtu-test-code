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
<script src="${pageContext.request.contextPath}/js/alertify.js"></script>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/alertify.core.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/alertify.default.css" />

<script type="text/javascript">
	(function(global, undefined) {
		"use strict";

		var document = global.document, Gy;

		Gy = function() {

			var _gyInner = {
				labels : {
					ok : "OK___VVV",
					cancel : "Cancel___VVV"
				},
				init : function(){
					alert("___init()");
				},
				dialog : function() {
					this.init();
					alert(JSON.stringify(this.labels));
				},
			};

			return {
				alert : function() {
					_gyInner.dialog();
					//return this;
				},
			};
		};

		if (typeof global.gy === "undefined") {
			global.gy = new Gy();
		}

	}(this));

	function testAlert(){
		alertify.alert("test alert!!!");
	}
</script>
<title>Insert title here</title>
</head>
<body>
	<input type="button" value="test" onclick="javascript:testAlert();" />
</body>
</html>