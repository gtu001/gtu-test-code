
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

<!-- bootstrap ↓↓↓↓↓↓ -->
<script src="/GtuSpringBoot/js/stomp.js"></script>
<!-- bootstrap ↑↑↑↑↑↑ -->

<!-- bootgrid ↓↓↓↓↓ -->
<script src="/GtuSpringBoot/js/jquery.bootgrid.min.js"
	type="text/javascript"></script>
<link rel="/GtuSpringBoot/css/jquery_bootgrid/jquery.bootgrid.min.css" />
<!-- bootgrid ↑↑↑↑↑ -->



<script type="text/javascript">
	const WEB_PREFIX = "/GtuSpringBoot";
	var jqueryTool = new JqueryTool();
	var client = createStomp();
	var columnConfig = null;

	var destintion = '/topic/test';
	var topicName = "/dashboard/materialized-view";

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

	function init_getJqBootgridColumnConfig() {
		columnConfig = $.ajax({
			url : WEB_PREFIX + '/jq_bootgrid_test/dashboard/template',
			data : {},
			success : function(data) {
			},
			error : jqueryTool.ajaxFailFunc,
			async : false,
			dataType : 'json',
		}).responseJSON;

		$("#grid-data thead tr").empty();
		$("#grid-data thead tr").append(
					"<th data-column-id='"+ "workRequestId" +"'>"
							+ "workRequestId" + "</th>");
		for (var ii = 0; ii < columnConfig.length; ii++) {
			$("#grid-data thead tr").append(
					"<th data-column-id='"+ columnConfig[ii]['id'] +"'>"
							+ columnConfig[ii]['text'] + "</th>");
		}
	}

	function init_jqBootgrid() {
		$("#grid-data").bootgrid(
				{
					ajax : false,
					post : function() {
						/* To accumulate custom parameter with the request object */
						return {
							id : "b0df282a-0d67-40e5-8558-c9e93b7befed"
						};
					},
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
		var json = JSON.parse(body);

		var productId = json["product-id"];
		var productName = json["product-name"];
		var planQuantity = json["plan-quantity"];
		var actulaQuantity = json["actula-quantity"];
		var inProgressWorkRequests = json["in-progress-work-requests"];

		$("span[id=span_productName]").text(productName);
		$("span[id=span_quantity]").text(planQuantity + "/" + actulaQuantity);

		$("#grid-data").bootgrid("clear");

		var thArry = new Array();
		$("th[data-column-id]").each(function(d) {
			thArry.push($(this).attr("data-column-id"));
		});

		var arry = new Array();
		for (var ii = 0; ii < inProgressWorkRequests.length; ii++) {
			var workRequestId = inProgressWorkRequests[ii]["work-request-id"];
			var workUnit = inProgressWorkRequests[ii]["work-unit"];

			var map = {};
			map['workRequestId'] = workRequestId;

			for (var jj = 0; jj < thArry.length; jj++) {
				if (thArry[jj] == workUnit) {
					map[thArry[jj]] = 'M';
				} else if(!thArry[jj] in map) {
					map[thArry[jj]] = '';
				}
			}
			
			console.log(map);
			
			arry.push(map);
		}
		$("#grid-data").bootgrid("append", arry);
	}

	$(document).ready(function() {
		init_getJqBootgridColumnConfig();
		init_jqBootgrid();
		init_sendTxtBtn();
	});
</script>
<title></title>
</head>
<body>

	<h1>Test Model</h1>

	<textarea id="sendText"></textarea>
	<input type="button" id="sendTextBtn" value="send" />

	<span id="span_productName"></span>
	<span id="span_quantity"></span>
	<table id="grid-data"
		class="table table-condensed table-hover table-striped">
		<thead>
			<tr>
				<th data-column-id="ssss1">ssss1</th>
				<!--  <th data-column-id="vvvv2" data-formatter="link"
					data-sortable="false">vvvv2</th>-->
				<th data-column-id="vvvv2" data-formatter="link"
					data-sortable="false">vvvv2</th>
				<th data-column-id="ooo" data-order="desc">ooo</th>
				<th data-column-id="gggg" data-type="numeric">Link</th>
			</tr>
		</thead>
	</table>
</body>
</html>