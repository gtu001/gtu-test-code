
<!-- % @ page language="java" contentType="text/html; charset=UTF8"
	pageEncoding="UTF8" % -->
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
<meta charset="UTF-8">
<meta http-equiv="Content-Type" content="text/html; charset=UTF8">
<link href="/css/jquery_ui/jquery-ui.css" rel="stylesheet">
<script src="/js/jquery-3.3.1.js"></script>
<script src="/js/jquery-ui.js"></script>
<script src="/js/gtu_util.js"></script>
<script src="/js/vue.js"></script>
<script type="text/javascript">
	var jqueryTool = new JqueryTool();
	
	$(document).ready(function() {
		initTabLst();
		tableOnSelect();
		queryBtnBind();
		saveOrUpdateBtnBind();
		deleteBtnBind();
	});

	function tableOnSelect() {
		$("select[name=tableNames]").change(function() {
			var _select = $(this);
			var tableName = _select.val();
			var _bodyTable = $("#dbBody");
			$.get("/springdata-dbMain/table-get-columns/", {
				table : tableName
			}, //
			function(data) {//
				_bodyTable.empty();
				for (var ii = 0; ii < data.length; ii++) {
					var column = data[ii];
					_bodyTable.append(
						$("<tr/>").append(
							$("<td/>").append(column + ":")
						).append(
							$("<td/>").append($("<input />").attr("type", "text").attr("name", column))
						)
					);
				}
			}, "json")// xml,html,text,script,json,jsonp
			.fail(jqueryTool.ajaxFailFunc);
		});
	}
	
	function getMethodMapping(tableName, operateType){
		var rtnObj = $.ajax({
	        url: '/springdata-dbMain/table-get-methodMapping/',
	        data : {
				table : tableName , 
				operate : operateType
			},
	        success: function (data) {
	        },
	        error: jqueryTool.ajaxFailFunc,
	        async: false,
	        dataType:'json',
	    }).responseJSON;
	    return rtnObj;
	}
	
	function postForm(operateType){
		var formData = $("#form").serializeObject();
		var table = $("select[name=tableNames]").val();
		var mapping = getMethodMapping(table, operateType);
		formData = $.extend(formData, mapping);
		
		$.post("/springdata-dbMain/db_operate", formData, //
		function(data) {//
			alert(JSON.stringify(data));
		}, "json")//
		.fail(jqueryTool.ajaxFailFunc);
	}
	
	function saveOrUpdateBtnBind(){
		$("input[name=saveOrUpdate]").click(function(){
			postForm("saveOrUpdate");
		});
	}
	
	function queryStuff(){
		$.post("/springdata-dbMain/db_simple_query", {}, //
		function(data) {//
			alert(JSON.stringify(data));
			alert($("#jqGrid").length);
			$("#jqGrid").jqGrid(data);
		}, "json")//
		.fail(jqueryTool.ajaxFailFunc);
	}
	
	function queryBtnBind(){
		$("input[name=query]").click(function(){
			//postForm("query");
			queryStuff()
		});
	}
	
	function deleteBtnBind(){
		$("input[name=delete]").click(function(){
			postForm("delete");
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
	<form id="form" action="/GtuSpringBoot/main">
	
		表名稱: <select name="tableNames"></select><br />
		<table id="dbBody"></table>
		<input type="button" name="query" value="query" />
		<input type="button" name="saveOrUpdate" value="saveOrUpdate" />
		<input type="button" name="delete" value="delete" />
		<input type="reset" name="clear" value="clear" /><br />
	</form>
	
	<table id="jqGrid"></table>
   	<div id="jqGridPager"></div>

</body>
</html>