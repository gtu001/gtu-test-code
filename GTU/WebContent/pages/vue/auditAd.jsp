<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<link rel="stylesheet" href="css2/bootstrap.min.css">
<link rel="stylesheet" href="css2/font-awesome.min.css">
<link rel="stylesheet" href="css2/jquery-ui.min.css">
<link rel="stylesheet" href="css2/jquery-ui.min.css">
<script src="js2/vue.min.js"></script>
<script type="text/javascript">
</script>
<title>上海商銀</title>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<div id="app" class="col-md-12 mt-3">
				<h2 class="mb-3">
					<i class="fa fa-flag" aria-hidden="true"></i> 廣告列表
				</h2>
				<div class="table-responsive">
					<table class="table table-striped">
						<thead>
							<tr>
								<th>#</th>
								<th id="th_adprjname">廣告名稱</th>
								<th id="th_blockname">廣告區塊</th>
								<th id="th_stimeFix">開始日期</th>
								<th id="th_etimeFix">結束日期</th>
								<th id="th_ctimeFix">建立日期</th>
								<th id="th_creater">創建者</th>
								<th id="th_approverFix">審核者</th>
								<th id="th_statusFix">審核狀態</th>
								<th>動作</th>
							</tr>
						</thead>
						<tbody>
							<tr v-for="(campaign, index) of campaigns">
								<td>{{index + 1}}</td>
								<td>{{campaign.adprjname}}</td>
								<td>{{campaign.blockname}}</td>
								<td>{{campaign.stimeFix}}</td>
								<td>{{campaign.etimeFix}}</td>
								<td>{{campaign.ctimeFix}}</td>
								<td>{{campaign.creater}}</td>
								<td>{{campaign.approverFix}}</td>
								<td>{{campaign.statusFix}}</td>
								<td>
								<!-- "0: 待審核
										1: 審核中
										3: 未通過
										4: 已通過"
								 -->
									<button class="btn btn-sm btn-outline-primary" 
										v-on:click="sendAudit(index, campaign)"
										v-show="(campaign.status=='1'||campaign.status=='0') && 
												(campaign.status!='3') &&
												(campaign.status2!='4') && 
												(auditLevel == '1' || auditLevel == '2')">審核[1]</button>
									<button class="btn btn-sm btn-outline-primary" 
										v-on:click="sendAudit(index, campaign)"
										v-show="(campaign.status=='4'&&campaign.status2!='4') && 
												(auditLevel == '2')">審核[2]</button>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<!-- Modal -->
	<div class="modal fade" id="exampleModalLong" tabindex="-1"
		role="dialog" aria-labelledby="exampleModalLongTitle"
		aria-hidden="true">
		<div class="modal-dialog modal-lg" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="exampleModalLongTitle">Modal title</h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">...</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal">Close</button>
					<button type="button" class="btn btn-primary">Save changes</button>
				</div>
			</div>
		</div>
	</div>

</body>
<script src="js2/popper.min.js"></script>
<script src="js2/jquery-3.2.1.min.js"></script>
<script src="js2/jquery-ui.min.js"></script>
<script src="js2/bootstrap.min.js"></script>
<script src="js2/Business.js"></script>
<script src="js2/main.js"></script>
<script src="js2/sorting.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		var campaignsArry = JSON.parse("<%=request.getAttribute("campaignsLst")%>");
		app.campaigns = campaignsArry;
		var auditLevel = "<%=request.getAttribute("auditLevel")%>";
		app.auditLevel = auditLevel;
		
		conf = new SortConf("conf", resetTable, app.campaigns);
		resetTable();
	});
		
	function resetTable(){
		$("#th_adprjname").html(conf.getTitleLink('adprjname', '廣告名稱'));
		$("#th_blockname").html(conf.getTitleLink('blockname', '廣告區塊'));
		$("#th_stimeFix").html(conf.getTitleLink('stimeFix', '開始日期'));
		$("#th_etimeFix").html(conf.getTitleLink('etimeFix', '結束日期'));
		$("#th_ctimeFix").html(conf.getTitleLink('ctimeFix', '建立日期'));
		$("#th_creater").html(conf.getTitleLink('creater', '創建者'));
		$("#th_approverFix").html(conf.getTitleLink('approverFix', '審核者'));
		$("#th_statusFix").html(conf.getTitleLink('statusFix', '審核狀態'));
		app.campaigns = conf.arry;
	}
		
	function sendAuditHandler(idx, campaign) {
	   	//alert("" + idx + "..." + JSON.stringify(campaign));
	   	var url = "auditAdNext" + '?idx=' + encodeURIComponent(idx) + 
	   							"&adprjid=" + encodeURIComponent(campaign['adprjid']) + 
	   							"&blockcode=" + encodeURIComponent(campaign['blockcode']);
	   	document.location.href = url;
	}
</script>
</html>
