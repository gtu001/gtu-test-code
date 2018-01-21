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
	function initPage(){
		var campaignsArry = JSON.parse("<%=request.getAttribute("campaignsLst")%>");
		app.campaigns = campaignsArry;
	}
	
	function sendAuditHandler(idx, campaign) {
    	//alert("" + idx + "..." + JSON.stringify(campaign));
    	var url = "auditAdNext" + '?idx=' + encodeURIComponent(idx) + 
    							"&adprjid=" + encodeURIComponent(campaign['adprjid']) + 
    							"&blockcode=" + encodeURIComponent(campaign['blockcode']);
    	document.location.href = url;
    }
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
								<th>廣告名稱</th>
								<th>廣告區塊</th>
								<th>開始日期</th>
								<th>結束日期</th>
								<th>創建者</th>
								<th>審核者</th>
								<th>審核狀態</th>
								<th>動作</th>
							</tr>
						</thead>
						<tbody>
							<tr v-for="(campaign, index) of campaigns">
								<%--
								<td>{{ campaign.id }}}</td>
								<td>{{ campaign.adName }}}</td>
								<td>{{ campaign.adBlock }}}</td>
								<td>{{ campaign.startDate }}}</td>
								<td>{{ campaign.endDate }}}</td>
								<td>{{ campaign.adCreator }}}</td>
								<td>{{ campaign.adAuditor }}}</td>
								<td>{{ campaign.adStatus }}}</td>
								<td>
									<button class="btn btn-sm btn-outline-primary"
										v-show="campaign.adStatus=='審核中'||campaign.adStatus=='待審核'">審核</button>
								</td>
								 --%>
								<td>{{index + 1}}</td>
								<td>{{campaign.adprjname}}</td>
								<td>{{campaign.blockname}}</td>
								<td>{{campaign.stimeFix}}</td>
								<td>{{campaign.etimeFix}}</td>
								<td>{{campaign.creater}}</td>
								<td>{{campaign.approver}}</td>
								<td>{{campaign.statusFix}}</td>
								<td>
									<button class="btn btn-sm btn-outline-primary" 
										v-on:click="sendAudit(index, campaign)"
										v-show="campaign.status=='1'||campaign.status=='0'">審核</button>
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
<script type="text/javascript">
$(document).ready(function() {
	initPage();
});
</script>
</html>
