<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">

<link rel="stylesheet" href="css2/bootstrap.min.css">
<link rel="stylesheet" href="css2/font-awesome.min.css">
<link rel="stylesheet" href="css2/animate.css">
<link rel="stylesheet" href="css2/jquery-ui.min.css">
<link rel="stylesheet" href="css2/main.css">

<script src="js/vue.min.js"></script>
<title>上海商銀</title>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<div id="app" class="col-md-12 mt-3">
				<div class="container-fluid">
					<blockquote class="blockquote pr-0">
						<h4 class="p-0">
							廣告內容
							<button class="btn btn-primary pull-right" v-on:click="approveBtn">核准</button>
							<button class="btn btn-danger pull-right mr-2" v-on:click="disapproveBtn">未通過</button>
							<button class="btn btn-light pull-right mr-2" v-on:click="prevPageBtn">上一頁</button>
						</h4>
					</blockquote>
					<div class="row mb-2">
						<div class="col-md-6">
							<div class="card bg-light mb-4">
								<div class="card-body">
									<form>
										<div class="form-group row">
											<label for="inputEmail3" class="col-sm-3 col-form-label">廣告名稱</label>
											<div class="col-sm-9">
												<input type="text" class="form-control" id="inputEmail3"
													placeholder="" v-bind:value="adListVo.adprjname" readonly="true">
											</div>
										</div>
										<div class="form-group row">
											<label for="inputEmail3" class="col-sm-3 col-form-label">選擇區塊</label>
											<div class="col-sm-9">
												<input type="text" class="form-control" id="inputEmail3"
													placeholder="" v-bind:value="adListVo.blockname" readonly="true">
											</div>
										</div>
										<div class="form-group row">
											<label for="inputPassword3" class="col-sm-3 col-form-label">開始日期</label>
											<div class="col-sm-9">
												<input type="text" class="form-control"
													id="inputPassword3" placeholder="" v-bind:value="adListVo.stimeFix" readonly="true">
											</div>
										</div>
										<div class="form-group row">
											<label for="inputPassword3" class="col-sm-3 col-form-label">結束日期</label>
											<div class="col-sm-9">
												<input type="text" class="form-control"
													id="inputPassword3" placeholder="" v-bind:value="adListVo.etimeFix" readonly="true">
											</div>
										</div>
										<div class="form-group row" v-for="(photo, index) of photoList">
											<label for="inputPassword3" class="col-sm-3 col-form-label">圖片{{index + 1}}--連結</label>
											<div class="col-sm-9">
												<input type="text" class="form-control"
													id="inputPassword3" placeholder="" v-bind:value="photo.liinkto" readonly="true">
											</div>
										</div>
										<div class="form-group row">
											<label for="inputPassword3" class="col-sm-3 col-form-label">選擇客群</label>
											<div class="col-sm-9">
												<!--input type="password" class="form-control" id="inputPassword3" placeholder="VIP A, VIP C"-->
												<div style="padding: .5rem 0rem;" v-for="(group, index) of adgroupLst">
													<span class="badge badge-secondary">{{group.ugid}}</span>
												</div>
											</div>
										</div>
									</form>
								</div>
							</div>
							<div class="card bg-light">
								<div class="card-body">
									<form>
										<div class="form-group">
											<label for="exampleFormControlTextarea1">意見</label>
											<textarea class="form-control" v-model="auditComment"
												id="exampleFormControlTextarea1" rows="3"></textarea>
										</div>
									</form>
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="card bg-light mb-4">
								<div class="card-body">
									<h4>結果預覽</h4>
									<div class="form-group row" v-for="photo of photoList">
										<div class="col-sm-9">
											<img src="image" v-bind:src="photo.photo"
												style="max-height: 236px" class="img-fluid p-2" />
											<p class="mb-0" v-if="true">檔案大小：{{toPicSize(photo.photo)}}
												KB</p>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
<script src="js2/vue.min.js"></script>
<script src="js2/popper.min.js"></script>
<script src="js2/jquery-3.2.1.min.js"></script>
<script src="js2/jquery-ui.min.js"></script>
<script src="js2/bootstrap.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		var adListVo = JSON.parse("<%=request.getAttribute("adListVo")%>");//
		app.adListVo = adListVo;
		
		var photoList = JSON.parse("<%=request.getAttribute("photoList")%>");//
		app.photoList = photoList;
		
		var adgroupLst = JSON.parse("<%=request.getAttribute("adgroupLst")%>");//
		app.adgroupLst = adgroupLst;
	});

	function dataURLtoBlob(dataurl) {
		var arr = dataurl.split(',');
		var mime = arr[0].match(/:(.*?);/)[1];
		var bstr = atob(arr[1]);
		var n = bstr.length;
		var u8arr = new Uint8Array(n);
		while (n--) {
			u8arr[n] = bstr.charCodeAt(n);
		}
		return new Blob([ u8arr ], {
			type : mime
		});
	}

	var app = new Vue({
		el : '#app',
		data : {
			adListVo : {},
			auditSelect : null,
			auditComment : null,
			photoList : [],
			adgroupLst : [],
		},
		methods : {
			approveBtn : function(e){
				this.auditSelect = "approve";
				this.confirmBtn(e);
			},
			disapproveBtn : function(e){
				this.auditSelect = "disapprove";
				this.confirmBtn(e);
			},
			prevPageBtn : function(e) {
				history.back();
				$(e.target).blur();
			},
			confirmBtn : function(e) {
				$(e.target).blur();

				if (this.auditSelect == "disapprove" && $.trim(this.auditComment) == '') {
					alert("請輸入審核意見");
					return;
				}

				//var data = this.$data;
				var data = {};
				$.extend(data, this.adListVo);
				data['auditSelect'] = this.auditSelect;
				data['auditComment'] = this.auditComment;

				$.ajax({
					type : 'POST',
					url : "auditAdNextSubmit",
					data : data,
					dataType : "JSON",
					success : function(data) {
						console.log('審核完成');
						if (data.status == "0000") {
							alert(data.msg);
							document.location.href = "adManager";
						} else if (data.status == "0001") {
							alert(data.msg);
						} else if (data.status == "0002") {
							alert(data.msg);
						}
					},
					error : function(jqXHR, textStatus, errorThrown) {
						console.log("審核更新失敗");
						alert('審核更新失敗！！，請聯繫廣告系統管理人員處理，謝謝');
					}
				});
			},
			toPicSize : function(dataurl) {
				var d = dataURLtoBlob(dataurl);
				return Math.round(d.size / 1024);
			},
		}
	});
</script>
</html>
