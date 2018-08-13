
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


<script type="text/javascript">
	const WEB_PREFIX = "/GtuSpringBoot";
	var jqueryTool = new JqueryTool();
	var client = createStomp();

	function createStomp() {
		var ws = new WebSocket('ws://192.168.99.100:15674/ws');//5672
		var client = Stomp.over(ws);
		var on_connect = function() {
			client.subscribe("/queue/test", function(d) {
				alert(d.body);
	        });
		};
		var on_error = function() {
			console.log('error');
			alert("error");
		};
		client.connect('guest', 'guest', on_connect, on_error, '/');

		// default receive callback to get message from temporary queues
		client.onreceive = function(m) {
			$('#stompDiv').append($("<code>").text(m.body));
		}
		return client;
	}

	function init_sendTxtBtn() {
		$('#sendTextBtn').click(function() {
			var text = $('#sendText').val();
			if (text) {
				client.send('/queue/test', {
					'reply-to' : '/temp-queue/foo'
				}, text);
				$('#sendText').val("");
			}
			return false;
		});
	}

	$(document).ready(function() {
		init_sendTxtBtn();
	});
</script>
<title></title>
</head>
<body>

	<h1>
		<a href="index.html">RabbitMQ Web STOMP Examples</a> > Temporary Queue
	</h1>

	<p>
		When you type text in the form's input, the application will send a
		message to the
		<code>/queue/test</code>
		destination with the
		<code>reply-to</code>
		header set to
		<code>/temp-queue/foo</code>
		.
	</p>
	<p>
		The STOMP client sets a default
		<code>onreceive</code>
		callback to receive messages from this temporary queue and display the
		message's text.
	</p>
	<p>
		Finally, the client subscribes to the
		<code>/queue/test</code>
		destination. When it receives message from this destination, it
		reverses the message's text and reply by sending the reversed text to
		the destination defined by the message's
		<code>reply-to</code>
		header.
	</p>

	<input type="text" id="sendText" />
	<input type="button" id="sendTextBtn" value="send" />

	<div id="stompDiv"></div>

</body>
</html>