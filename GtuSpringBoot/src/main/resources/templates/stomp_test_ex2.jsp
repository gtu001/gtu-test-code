
<!-- % @ page language="java" contentType="text/html; charset=UTF8"
	pageEncoding="UTF8" % -->
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
<meta charset="UTF-8">
<meta http-equiv="Content-Type" content="text/html; charset=UTF8">
<link href="/GtuSpringBoot/css/jquery_ui/jquery-ui.css" rel="stylesheet">
<script src="/GtuSpringBoot/js/jquery-3.3.1.js"></script>
<script src="/GtuSpringBoot/js/jquery-ui.js"></script>
<script src="/GtuSpringBoot/js/gtu_util.js"></script>
<script src="/GtuSpringBoot/js/vue.js"></script>

<!-- jqGrid ↓↓↓↓↓↓ -->
<link rel="stylesheet" type="text/css" media="screen"
	href="/GtuSpringBoot/css/jquery_jqGrid/ui.jqgrid.css" />
<script src="/GtuSpringBoot/js/grid.locale-tw.js" type="text/javascript"></script>
<script src="/GtuSpringBoot/js/jquery.jqGrid.min.js"
	type="text/javascript"></script>
<!-- jqGrid ↑↑↑↑↑↑ -->

<!-- bootstrap ↓↓↓↓↓↓ -->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
	integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO"
	crossorigin="anonymous" />
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"
	integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49"
	crossorigin="anonymous"></script>
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"
	integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy"
	crossorigin="anonymous"></script>
<!-- bootstrap ↑↑↑↑↑↑ -->

<!-- stomp ↓↓↓↓↓↓ -->
<script src="/GtuSpringBoot/js/stomp.js"></script>
<!-- stomp ↑↑↑↑↑↑ -->

<!-- bootgrid ↓↓↓↓↓ -->
<script src="/GtuSpringBoot/js/jquery.bootgrid.min.js"
	type="text/javascript"></script>
<link href="/GtuSpringBoot/css/jquery_bootgrid/jquery.bootgrid.css"
	rel="stylesheet" />
<!-- <link href="/GtuSpringBoot/css/jquery_bootgrid/jquery.bootgrid.min.css" rel="stylesheet" /> -->
<!-- bootgrid ↑↑↑↑↑ -->



<script type="text/javascript">
	const WEB_PREFIX = "/GtuSpringBoot";
	var jqueryTool = new JqueryTool();
	var client = createStomp();

	var destintion = '/topic/dashboard-materialized-view2';

	var tabRow = [ {
		"item_id" : "物料耗用",
		"item_qty" : "數量"
	}, {
		"item_id" : "治工具使用",
		"item_qty" : "數量"
	}, {
		"item_id" : "工單狀態",
		"item_qty" : "數量"
	} ];

	function createStomp() {
		var ws = new WebSocket('ws://192.168.99.100:15674/ws');//5672
		var client = Stomp.over(ws);
		var on_connect = function() {
			client.subscribe(destintion, function(d) {
				processContentToJqBootgrid(d.body);
			});
		};
		var on_error = function() {
			console.log('error');
			alert("error");
		};
		client.connect('guest', 'guest', on_connect, on_error, '/');

		// default receive callback to get message from temporary queues
		client.onreceive = function(m) {
			console.log(m.body);
			alert(m.body)
		}
		return client;
	}

	function init_sendTxtBtn() {
		$('#sendTextBtn').click(function() {
			var text = $('#sendText').val();
			if (text) {
				client.send(destintion, {
					//'reply-to' : topicName,
					'content-type' : "application/json; charset=utf-8",
					"content-length" : text.length,
				}, text);
				$('#sendText').val("");
			}
			return false;
		});
	}

	function init_Table() {
		init_getJqBootgridColumnConfig("#jq_table_1", 0);
		init_getJqBootgridColumnConfig("#jq_table_2", 1);
		init_getJqBootgridColumnConfig("#jq_table_3", 2);
		init_jqBootgrid("#jq_table_1");
		init_jqBootgrid("#jq_table_2");
		init_jqBootgrid("#jq_table_3");
	}

	function init_getJqBootgridColumnConfig(jqTable, rowIdx) {
		var __append_map = function(id, text, arry) {
			var map = {};
			map['id'] = id;
			map['text'] = text;
			arry.push(map);
		};

		var arry = new Array();
		__append_map('item_id', tabRow[rowIdx]['item_id'], arry);
		__append_map('item_qty', tabRow[rowIdx]['item_qty'], arry);

		$(jqTable).find("thead tr").empty();
		/*
		$(jqTable).find("thead tr").append(
					"<th data-column-id='"+ "workRequestId" +"'>"
							+ "workRequestId" + "</th>");
		 */
		for (var ii = 0; ii < arry.length; ii++) {
			$(jqTable).find("thead tr").append(
					"<th data-column-id='"+ arry[ii]['id'] +"'>"
							+ arry[ii]['text'] + "</th>");
		}
	}

	function init_jqBootgrid(jqTable) {
		$(jqTable).bootgrid(
				{
					ajax : false,
					post : function() {
						/* To accumulate custom parameter with the request object */
						return {
							id : "b0df282a-0d67-40e5-8558-c9e93b7befed"
						};
					},
					navigation : 0,
					//url : WEB_PREFIX + "/jq_bootgrid_test/get_model",
					formatters : {
						"link" : function(column, row) {
							return "<a href=\"#\">" + column.id + ": " + row.id
									+ "</a>";
						}
					}
				});
	}

	function processContentToJqBootgrid(body) {
		var tabIds = [ "#jq_table_1", "#jq_table_2", "#jq_table_3" ];

		var json = JSON.parse(body);

		for (var ii = 0; ii < json.length; ii++) {
			var jsonArry = json[ii]['table'];
			processDtlTable(tabIds[ii], jsonArry);
		}
	}

	function processDtlTable(tableId, jsonArry) {
		$(tableId).bootgrid("clear");
		$(tableId).bootgrid("append", jsonArry);
	}

	$(document).ready(function() {
		init_Table();
		init_sendTxtBtn();
	});
</script>
<title></title>
</head>
<body>

	<textarea id="sendText"></textarea>
	<input type="button" id="sendTextBtn" value="send" />

	<span id="span_productName"></span>
	<span id="span_quantity"></span>

	<table border="0">
		<tr>
			<td>
				<table id="jq_table_1"
					class="table table-condensed table-hover table-striped">
					<thead>
						<tr>
						</tr>
					</thead>
				</table>
			</td>
			<td>
				<table id="jq_table_2"
					class="table table-condensed table-hover table-striped">
					<thead>
						<tr>
						</tr>
					</thead>
				</table>
			</td>
			<td>
				<table id="jq_table_3"
					class="table table-condensed table-hover table-striped">
					<thead>
						<tr>
						</tr>
					</thead>
				</table>
			</td>
		</tr>
	</table>

</body>
</html>