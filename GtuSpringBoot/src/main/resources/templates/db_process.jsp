
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
<!-- jqGrid ↓↓↓↓↓ -->
<link rel="stylesheet" type="text/css" media="screen"
	href="/css/jquery_jqGrid/ui.jqgrid.css" />
<script src="/js/grid.locale-tw.js" type="text/javascript"></script>
<script src="/js/jquery.jqGrid.min.js" type="text/javascript"></script>
<!-- jqGrid ↑↑↑↑↑ -->
<script type="text/javascript">
	var jqueryTool = new JqueryTool();

	$(document).ready(function() {
		initTabLst();
		tableOnSelect();
		queryBtnBind();
		saveOrUpdateBtnBind();
		deleteBtnBind();
		initShowSelected();
	});

	function tableOnSelect() {
		$("select[name=tableNames]").change(function() {
			var _select = $(this);
			var tableName = _select.val();
			var _bodyTable = $("#dbBody");

			if ($.trim(tableName).length == 0) {
				_bodyTable.empty();
				return;
			}

			$.get("/springdata-dbMain/table-get-columns/", {
				table : tableName
			}, //
			function(data) {//
				/*
					_bodyTable.empty();
					for (var ii = 0; ii < data.length; ii++) {
						var column = data[ii];
						_bodyTable.append($("<tr/>")
							.append($("<td/>").append(column+ ":"))
							.append($("<td/>").append($("<input />").attr("type", "text").attr("name", column)))
							);
					}
				 */
			}, "json")// xml,html,text,script,json,jsonp
			.fail(jqueryTool.ajaxFailFunc);
		});
	}

	function getMethodMapping(tableName, operateType) {
		var rtnObj = $.ajax({
			url : '/springdata-dbMain/table-get-methodMapping/',
			data : {
				table : tableName,
				operate : operateType
			},
			success : function(data) {
			},
			error : jqueryTool.ajaxFailFunc,
			async : false,
			dataType : 'json',
		}).responseJSON;
		return rtnObj;
	}

	function getForm(operateType) {
		var formData = $("#form").serializeObject();
		var table = $("select[name=tableNames]").val();
		var mapping = getMethodMapping(table, operateType);
		formData = $.extend(formData, mapping);
		return formData;
	}

	function postForm(operateType) {
		var formData = getForm(operateType);
		$.post("/springdata-dbMain/db_operate", formData, //
		function(data) {//
			alert(JSON.stringify(data));
		}, "json")//
		.fail(jqueryTool.ajaxFailFunc);
	}

	function saveOrUpdateBtnBind() {
		$("input[name=saveOrUpdate]").click(function() {
			postForm("saveOrUpdate");
		});
	}

	function queryColModel() {
		var formData = getForm("query");
		var rtnObj = $.ajax({
			url : '/springdata-dbMain/db_simple_query/colModel',
			data : formData,
			success : function(data) {
			},
			error : jqueryTool.ajaxFailFunc,
			async : false,
			dataType : 'json',
			method : "POST",
		}).responseJSON;
		return rtnObj;
	}

	var lastSel = -1;//最後一次選

	function queryStuff() {
		$("#jqGrid").jqGrid('GridUnload');//remove

		var postData = getForm("query");
		var colModel = queryColModel();

		$("#jqGrid").jqGrid({
			url : "/springdata-dbMain/db_simple_query/dataRows",
			datatype : "json",
			mtype : 'POST',
			postData : postData,
			colModel : colModel,
			viewrecords : true, // show the current page, data rang and total records on the toolbar
			width : 780,
			height : 200,
			rowNum : 30,
			rownumbers : true,
			loadonce : false, // this is just for the demo
			autowidth : true,
			sortname : 'id',
			sortorder : 'desc',
			pager : "#jqGridPager",
			rowList : [ 25, 50, 75, 100 ],
			gridview : true,
			caption : 'Select from existing server',
			loadtext : 'Loading, please wait',
			emptyRecords : "No Accounts Found",
			onSelectRow : function(id) {
				if (id && id !== lastSel) {
					jQuery('#grid_id').restoreRow(lastSel);
					lastSel = id;
				}
				//不work
				//jQuery('#jqGrid').editRow(id, true);
				//alert("onSelectRow : " + id);

				//$("#jqGrid").editGridRow(id, {});
			},
			/*
			ondblClickRow : function(id, row, column) {
				alert("dbl : " + id + " , " + JSON.stringify(row)
						+ " , " + column);
			},
			 */
			loadComplete : function(data, response) {
				if (data == null) {
					data = [ {} ];
				}
				//alert("loadComplete 查詢完成!\n" + data.length);
			},
			gridComplete : function(data, response) {
				if (data == null) {
					data = [ {} ];
				}
				//alert("gridComplete 查詢完成!\n" + data.length);
			},
		});

		$("#jqGrid").navGrid('#jqGridPager', // the buttons to appear on the toolbar of the grid
		{
			add : true,
			addtext : '新增',
			edit : true,
			edittext : '修改',
			del : true,
			deltext : '刪除',
			search : true,
			refresh : true
		},
		// options for the Edit Dialog
		{
			editCaption : "修改視窗",
			recreateForm : true,
			//checkOnUpdate : true,
			//checkOnSubmit : true,
			beforeSubmit : function(postdata, form, oper) {
				alert("postdata = " + JSON.stringify(postdata));
				alert("form = " + JSON.stringify(form));
				alert("oper = " + JSON.stringify(oper));

				if (confirm('確定要修改')) {
					// do something
					return [ true, '' ];
				} else {
					return [ false, '取消!!' ];
				}
			},
			closeAfterEdit : true,
			errorTextFormat : function(data) {
				return 'Error: ' + data.responseText
			}
		},
		// options for the Add Dialog
		{
			closeAfterAdd : true,
			recreateForm : true,
			errorTextFormat : function(data) {
				return 'Error: ' + data.responseText
			}
		},
		// options for the Delete Dailog
		{
			errorTextFormat : function(data) {
				return 'Error: ' + data.responseText
			}
		});
	}

	function queryBtnBind() {
		$("input[name=query]").click(function() {
			//postForm("query");
			queryStuff()
		});
	}

	function deleteBtnBind() {
		$("input[name=delete]").click(function() {
			postForm("delete");
		});
	}

	function initTabLst() {
		$.get("/springdata-dbMain/tables", {}, //
		function(data) {//
			var tabSel = $("select[name=tableNames]");
			tabSel.append($("<option />").attr("value", "").text("請選擇.."));
			for (var ii = 0; ii < data.length; ii++) {
				var val = data[ii];
				var txt = data[ii];
				tabSel.append($("<option />").attr("value", val).text(txt));
			}
		}, "json")// xml,html,text,script,json,jsonp
		.fail(jqueryTool.ajaxFailFunc);
	}

	function initShowSelected() {
		$('#showSelected')
				.on(
						'click',
						function() {
							var selArrRow = jQuery("#jqGrid").getGridParam(
									'selarrrow');//不work
							console.log("selArrRow size = " + selArrRow.length);
							var selectedAppIds = [];
							for (var i = 0; i < selArrRow.length; i++) {
								var celValue = $('#jqGrid').jqGrid('getCell',
										selArrRow[i], 'id');//id = column
								selectedAppIds.push(celValue);
							}
							alert(selectedAppIds);
							$('#jqGrid').trigger('reloadGrid');
						});
	}
</script>
<title></title>
</head>
<body>

	<span style="color: red" th:text="${message}" />
	<form id="form" action="/GtuSpringBoot/main">

		表名稱: <select name="tableNames"></select><br />
		<table id="dbBody"></table>
		<input type="button" name="query" value="query" /> <input
			type="button" name="saveOrUpdate" value="saveOrUpdate" /> <input
			type="button" name="delete" value="delete" /> <input type="reset"
			name="clear" value="clear" /><br /> <input type="button"
			id="showSelected" value="show selected" /><br />
	</form>

	<table id="jqGrid"></table>
	<div id="jqGridPager"></div>

</body>
</html>