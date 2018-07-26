
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
		//queryBtnBind();
		//saveOrUpdateBtnBind();
		//deleteBtnBind();
		//initShowSelected();
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
				queryStuff();
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

	function getTableMapping(tableName) {
		var rtnObj = $.ajax({
			url : '/springdata-dbMain/table-mapping/',
			data : {
				table : tableName,
			},
			success : function(data) {
			},
			error : jqueryTool.ajaxFailFunc,
			async : false,
			dataType : 'json',
		}).responseJSON;
		return rtnObj;
	}

	function getForm() {
		var formData = $("#form").serializeObject();
		var table = $("select[name=tableNames]").val();
		var mapping = getTableMapping(table);
		formData = $.extend(formData, mapping);
		return formData;
	}

	function postForm() {
		var formData = getForm();
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
			subGrid: true,
		    subGridRowExpanded: function (subgridDivId, rowId) {
		        var subgridTableId = subgridDivId + "_div";
		        $("#" + subgridDivId).html("<div id="+subgridTableId+">");
		        //alert(subgridTableId + " -- " + $("#" + subgridTableId).length + " -- " + rowId);
		        
		        var formData = getForm();
		        formData = $.extend(formData, {rowId : rowId});
				$.post("/springdata-dbMain/query_relation", formData, //
				function(data) {//
					var dataStr = JSON.stringify(data);
					console.log(dataStr);
					
					var getLocalMapLst = function(data){
						try{
							var rtnArry = new Array();
							var arry = data['rowReader']['rows'];
							for(var i in arry){
								rtnArry.push(arry[i]['loaclMap']);
							}
							return rtnArry;
						}catch(e){
							return [];
						}
					}
					
					var subgridArry = data['subgrid'];
					for(var ii = 0 ; ii < subgridArry.length ; ii ++){
						var d = subgridArry[ii];
						
						var caption = d['fieldName'];
						var colNames = d['colModel'];
						var colModel = d['colModel'];
						var data = getLocalMapLst(d);
						
						//建立關聯表
						subgridGenerate(subgridTableId, ii, caption, colNames, colModel, data);
					}
					
				}, "json")//
				.fail(jqueryTool.ajaxFailFunc);
		    },
		    subGridRowColapsed: function (subgridDivId, rowId) {
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
			searchtext :　"查詢",
			refresh : true,
			refreshtext : "重整",
		},
		// options for the Edit Dialog
		{
			editCaption : "修改視窗",
			recreateForm : true,
			//checkOnUpdate : true,
			//checkOnSubmit : true,
			url : "/springdata-dbMain/db_save",
			beforeSubmit : function(postdata, form, oper) {
				postdata = $.extend(postdata, getForm());
			
				//alert("postdata = " + JSON.stringify(postdata));
				//alert("form = " + JSON.stringify(form));
				//alert("oper = " + JSON.stringify(oper));

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
			url : "/springdata-dbMain/db_save",
			beforeSubmit : function(postdata, form, oper) {
				postdata = $.extend(postdata, getForm());
			
				//alert("postdata = " + JSON.stringify(postdata));
				//alert("form = " + JSON.stringify(form));
				//alert("oper = " + JSON.stringify(oper));

				if (confirm('確定要新增')) {
					// do something
					return [ true, '' ];
				} else {
					return [ false, '取消!!' ];
				}
			},
			errorTextFormat : function(data) {
				return 'Error: ' + data.responseText
			}
		},
		// options for the Delete Dailog
		{
			url : "/springdata-dbMain/db_delete",
			onclickSubmit : function(data, d1, d2){
				var postData = data['delData'];
				postData = $.extend(postData, getForm());
			},
			
			beforeSubmit : function(postdata, form, oper) {
				postdata = $.extend(postdata, getForm());
				
				//alert("postdata = " + JSON.stringify(postdata));
				//alert("form = " + JSON.stringify(form));
				//alert("oper = " + JSON.stringify(oper));
				
				if (confirm('確定要刪除')) {
					// do something
					return [ true, '' ];
				} else {
					return [ false, '取消!!' ];
				}
			},
			errorTextFormat : function(data) {
				return 'Error: ' + data.responseText
			}
		});
	}
	
	function subgridGenerate(appendDivId, index, caption, colNames, colModel, data){
		var subgridTableId = appendDivId + "_tab_" + index;
		$("#" + appendDivId).append($("<table id="+ subgridTableId +" />"))
	
		$("#" + subgridTableId).jqGrid('GridUnload');//remove
		$("#" + subgridTableId).jqGrid({
            datatype: 'local',
            caption : caption,
            data: data,
            //colNames: colNames,
            colModel: colModel,
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
	</form>

	<table id="jqGrid"></table>
	<div id="jqGridPager"></div>

</body>
</html>