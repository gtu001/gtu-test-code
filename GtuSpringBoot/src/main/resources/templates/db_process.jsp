
<!-- % @ page language="java" contentType="text/html; charset=UTF8"
	pageEncoding="UTF8" % -->
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
<meta charset="UTF-8">
<meta http-equiv="Content-Type" content="text/html; charset=UTF8">
<script src="/js/jquery-3.3.1.js"></script>
<script src="/js/gtu_util.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		initTabLst();

		tableOnSelect();
	});

	function tableOnSelect() {
		$("select[name=tableNames]").change(function() {
			var _select = $(this);
			var tableName = _select.val();
			var _body = $("#dbBody");

/*
			alert("#dbBody = " + $("#dbBody").length);
			alert("div[id=dbBody] = " + $("div[id=dbBody]").length);
			alert("div = " + $("div").length);
			alert("br = " + $("br").length);
			alert("input = " + $("input").length);
			alert("html = " + $("html").length);
			*/
			
			alert("dbBody -- " + document.getElementById("dbBody").length);

			$.get("/springdata-dbMain/table-get-columns/", {
				table : tableName
			}, //
			function(data) {//
				_body.empty();
				for (var ii = 0; ii < data.length; ii++) {
					var column = data[ii];
					_body.append(column + ":");
					//_body.append($("<input />").attr("type", "text").attr("name", column));
					_body.append("<br/>");
				}
			}, "json")// xml,html,text,script,json,jsonp
			.fail(jqueryTool.ajaxFailFunc);
		});
	}

	function initTabLst() {
		$.get("/springdata-dbMain/tables", {}, //
		function(data) {//
			var tabSel = $("select[name=tableNames]");
			for (var ii = 0; ii < data.length; ii++) {
				var val = data[ii];
				var txt = data[ii];
				tabSel.append($("<option />").attr("value", val).text(txt));
			}
		}, "json")// xml,html,text,script,json,jsonp
		.fail(jqueryTool.ajaxFailFunc);
	}
</script>
<title></title>
</head>
<body>

	<span style="color: red" th:text="${message}" />
	<form action="/GtuSpringBoot/main">
		表名稱: <select name="tableNames" /> <br />

		<div id="dbBody" />

		<input type="button" value="GO" /><br /> <input type="button"
			id="b1" value="b1" /><br /> <input type="button" id="b2" value="b2" /><br />
	</form>

</body>
</html>