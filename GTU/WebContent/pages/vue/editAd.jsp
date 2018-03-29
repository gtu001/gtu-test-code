<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.fuco.cmsad.dao.entity.Blockmap"%>

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

<style>
.horizontal_center_clz {
	display : inline-block;
	margin: 0 auto;
	text-align: center;
}
</style>

<title>上海商業儲蓄銀行</title>
<script type="text/javascript">
	function initPage(){
		var businessArry = JSON.parse("<%=request.getAttribute("blockLst")%>");
		var adcasedataVo = JSON.parse("<%=request.getAttribute("adcasedataVo")%>");
		var adphotodataVo = JSON.parse("<%=request.getAttribute("adphotodataVo")%>");
		Vue.set(app, 'businesses', businessArry);
		app.applyForEditPage(adcasedataVo, adphotodataVo);
	}

	var data = new FormData(); //data為一表單物件
	function setValue(Value) { //將值存入data表單
		data = Value;
	}
	function getValue() { //得data表單
		return data;
	}
	var StartEnd = [ '', '' ]; //宣告一個日期陣列，為日期判斷用
	function changeStart(Start) {
		//將開始日期記起來。
		StartEnd[0] = Start;
	}
	function changeEnd(End) {
		//將結束日期記起來。
		StartEnd[1] = End;
	}
	function checkStartEnd(startDate, endDate) { //檢核開始日期和結束日期
		startDate = startDate.substring(0, 4) + startDate.substring(5, 7)
				+ startDate.substring(8, 10);
		endDate = endDate.substring(0, 4) + endDate.substring(5, 7)
				+ endDate.substring(8, 10);
		if (endDate < startDate)
			return false;
		else
			return true;
	}
	function checkadBlockRadio() { //檢核選擇區塊是否有選取
		var adBlockRadio = $("input[name='adBlockCheck']:checked").val(); //jQuery的寫法，取選擇區塊勾選的值

		if (adBlockRadio == undefined) // 注意檢查完全沒有選取的寫法	   
			return false;
		else
			return true;
	}
	function doCreateAd(step_check) { //上傳圖片檢核                                                
		if (step_check == 'AdtoCheck') {
			var File1 = document.getElementById("File1").value;
			var File2 = ''; //document.getElementById("File2").value;
			var img1Alt = document.getElementById("img1Alt").value;
			var img1Url = document.getElementById("img1Url").value;
			var img2Alt = ''; //document.getElementById("img2Alt").value;
			var img2Url = ''; //document.getElementById("img2Url").value;
			if (File1 != '') {
				/*
				if (img1Alt == '' || img1Url == '') {
					alert('圖片1的廣告描述或鏈結網址未寫入');
					return false;
				}
				 */
				if (img1Alt == '') {
					alert('圖片1的廣告描述未寫入');
					return false;
				}
			}

			File2 = '';
			if (File2 != '') {
				if (img2Alt == '' || img2Url == '') {
					alert('圖片2的廣告描述或鏈結網址未寫入');
					return false;
				}
			}

		} else if (step_check == 'AdtoCheck2') {
			var customerCheck = ($("input[name='Choosecustomer']:checked").length);
			if (customerCheck == 0) {
				alert('顯示客群未勾選');
				return false;
			}
		}
		return true;
	}

	function checkAdprjidIsOk(adprjid) { //確認廣告id是否可用
		var data = $.ajax({
			type : 'POST',
			url : "checkAdprjidIsOk",
			data : {
				"adprjid" : adprjid
			},
			async : false,
			dataType : "JSON",
			error : function(jqXHR, textStatus, errorThrown) {
				console.log("廣告ID確認失敗 : " + jqXHR + ", " + textStatus + ", "
						+ errorThrown);
			}
		}).responseJSON;
		if (data) {
			if (data.result == true) {
				return true;
			} else {
				alert("廣告圖檔名稱重複，請重新修改後上傳，謝謝");
				return false;
			}
		}
		return false;
	}
	
	//取得現行廣告
	function getNowAdmobPicsHandler(blockcode, ugid){
		var data = {
			'blockcode' : blockcode, 
			'ugid' : ugid, 
		};
		$.ajax({
			type : 'POST',
			url : "getNowAdmobPics",
			data : data,
			dataType : "JSON",
			success : function(data) {
				var nowAdArry = new Array();
				for(var ii = 0 ; ii < data['data'].length ; ii ++){
					nowAdArry.push(data['data'][ii]['src']);
				}
				app.nowAdImages = nowAdArry;
			},
			error : function(jqXHR, textStatus, errorThrown) {
				console.log("取得現行廣告失敗");
				alert('取得現行廣告失敗！！，請聯繫廣告系統管理人員處理，謝謝');
			}
		});
	}

	function submitInfo() { //上送前將所有的資料收集後送出
		var info = getValue();

		if ($.trim(info.imgAlt) == '') {
			alert("廣告描述不可空白");
			return;
		}
		/*
		if($.trim(info.imgUrl) == ''){
			alert("鏈結網址不可空白");
			return;
		}
		 */
		if ($.trim(info.image) == '' || $.trim(info.imageName) == '') {
			alert("圖片未選擇");
			return;
		}

		var newadGroup = '';
		for (i = 0; i < info.adGroup.length; i++) {
			newadGroup = newadGroup + info.adGroup[i];
			if (i + 1 != info.adGroup.length)
				newadGroup = newadGroup + ',';
		}

		var data = {
			adBlock : info.adBlock,
			adName : info.adName,
			startDate : info.startDate,
			endDate : info.endDate,
			imgAlt : info.imgAlt,
			imgUrl : info.imgUrl,
			image : info.image,
			imageName : info.imageName,
			img2Alt : info.img2Alt,
			img2Url : "", //info.img2Url,
			image2 : "", //info.image2,
			image2Name : "", //info.image2Name,
			adGroup : newadGroup,
			isDefault : info.isDefault,
		};

		$.ajax({
			type : 'POST',
			url : "newAd",
			data : data,
			dataType : "JSON",
			success : function(data) {
				console.log('廣告回傳成功');
				if (data.status == "0000") {
					alert(data.msg);
					document.location.href = "adEdit";
				} else if (data.status == "0001") {
					alert(data.msg);
				} else if (data.status == "0002") {
					alert(data.msg);
				}
			},
			error : function(jqXHR, textStatus, errorThrown) {
				console.log("廣告回傳失敗");
				alert('廣告新增失敗！！，請聯繫廣告系統管理人員處理，謝謝');
			}
		});
	}
</script>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<div id="app" class="col-md-12 mt-3">
				<div class="container-fluid">
					<%--navigation--%>
					<div class="row border border-secondary mb-3">
						<div class="stepper-steps col-md-9 col-12">
							<div class="stepper-step">
								<div class="stepper-stepContent"
									v-bind:class="{'stepper-step-isActive': step==1}">
									<h5>
										<span class="stepper-stepMarker">1</span> 選擇區塊
									</h5>
								</div>
							</div>
							<div class="stepper-step">
								<div class="stepper-stepContent"
									v-bind:class="{'stepper-step-isActive': step==2}">
									<h5>
										<span class="stepper-stepMarker">2</span> 上傳圖片
									</h5>
								</div>
							</div>
							<div class="stepper-step" v-if="adBlock=='首頁輪播'&&step!==1">
								<div class="stepper-stepContent"
									v-bind:class="{'stepper-step-isActive': step==3}">
									<h5>
										<span class="stepper-stepMarker">3</span> 顯示客群
									</h5>
								</div>
							</div>
						</div>
						<div class="stepper-content col-md-3 col-12">
							<div class="stepper-actions pt-3 pr-1">
								<div v-if="adBlock!=='首頁輪播'">
									<button class="btn btn-success pull-right ml-1" v-if="step==2"
										@click="submitPage" onclick="submitInfo()">送審</button>
									<button class="btn btn-primary pull-right ml-1" v-if="step!==2"
										@click="nextPage">下一步</button>
								</div>
								<div v-else>
									<button class="btn btn-success pull-right ml-1" v-if="step==3"
										@click="submitPage" onclick="submitInfo()">送審</button>
									<button class="btn btn-primary pull-right ml-1" v-if="step!==3"
										@click="nextPage">下一步</button>
								</div>
								<button class="btn btn-primary pull-right ml-1" v-if="step!==1"
									@click="prevPage">上一步</button>
								<button class="btn btn-light pull-right" v-if="step==1">上一頁</button>
							</div>
						</div>
					</div>
					<%--choose block--%>
					<template v-if="step==1">
					<div class="row animated fadeInRight">
						<div class="col-md-6">
							<blockquote class="blockquote">
								<h4 class="p-0">選擇區塊</h4>
							</blockquote>
							<div class="mb-2 card bg-light">
								<div class="card-body">
									<div class="custom-controls-stacked">
										<label class="custom-control custom-radio"
											v-for="business of businesses"> 
											<input :value="business.blockcode" type="radio"
												class="custom-control-input" v-model="adBlock"
												name="adBlockCheck" v-on:click="blockRadioChoice(business)"> 
											<span
											class="custom-control-indicator"></span> <span
											class="custom-control-description">
												<h4>{{ business.blockname }}</h4>
										</span>
										</label>
									</div>
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<blockquote class="blockquote">
								<h4 class="p-0">廣告資訊</h4>
							</blockquote>
							<div class="card bg-light">
								<div class="card-body">
									<form>
										<div class="form-group row">
											<label for="adName"
												class="col-sm-3 col-form-label text-right">廣告名稱</label>
											<div class="col-sm-9">
												<input type="text" class="form-control" id="adName"
													v-model="adName">
											</div>
										</div>
										<div class="form-group row">
											<label for="startDate"
												class="col-sm-3 col-form-label text-right">開始日期</label>
											<div class="col-sm-9">
												<date-picker @update-date="updateStartDate"
													:placeholder="startDate" id="startDate"></date-picker>
											</div>
										</div>
										<div class="form-group row">
											<label for="endDate"
												class="col-sm-3 col-form-label text-right">結束日期</label>
											<div class="col-sm-9">
												<date-picker @update-date="updateEndDate"
													v-bind:min-date="true" :placeholder="endDate" id="endDate"></date-picker>
											</div>
										</div>
									</form>
								</div>
								<!-- blockphoto -->
								<div class="horizontal_center_clz custom-controls-stacked">
									<img v-bind:src="blockphoto" style="max-width: 300px" />
								</div>
							</div>
						</div>
					</div>
					</template>
					<%--upload image--%>
					<template v-if="step==2">
					<div class="row">
						<div class="col-md-6 animated fadeInRight">
							<blockquote class="blockquote">
								<h4 class="p-0">上傳圖片</h4>
							</blockquote>
						</div>
						<div class="col-md-6 animated fadeInRight">
							<blockquote class="blockquote">
								<h4 class="p-0">預覽</h4>
							</blockquote>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6 animated fadeInRight">
							<div class="card mb-4 bg-light">
								<h4 class="m-2">圖片</h4>
								<div class="card-body">
									<form>
										<div class="form-group row">
											<label for="img1Alt" class="col-sm-4 col-form-label">廣告描述</label>
											<div class="col-sm-8">
												<input type="text" class="form-control" id="img1Alt"
													placeholder="廣告描述" v-model="imgAlt">
											</div>
										</div>
										<div class="form-group row">
											<label for="img1Url" class="col-sm-4 col-form-label">鏈結網址</label>
											<div class="col-sm-8">
												<input type="text" class="form-control" id="img1Url"
													placeholder="鏈結網址" v-model="imgUrl">
											</div>
										</div>
										<div class="form-group row">
											<label for="exampleFormControlFile1" class="col-sm-4 col-form-label">請選擇上傳圖片</label>
											<div class="col-sm-8">
												<input type="file" @change="onFileChange" id="File1">
											</div>
										</div>
										<div class="form-group row">
											<label class="col-sm-8 col-form-label">
											<small>(建議圖片尺寸：寬300像素，高600像素)<br>(建議圖片大小不超過200k)<br>(圖片請使用以下副檔名：jpg、jpeg、png)<br>(圖片的檔名與副檔名大小寫請與CSV檔一致)
											</small>
											</label>
										</div>
										<div class="form-group row">
											<label for="isDefault" class="col-sm-4 col-form-label">是否為預設廣告<br>
												<small>(請確認default廣告日期)</small>
											</label>
											<div class="col-sm-8" style="margin-top: 20px;">
												<input vertical-align="middle" type="checkbox" v-model:value="isDefault" />
											</div>
										</div>
									</form>
								</div>
							</div>
						</div>
						<div class="col-md-6 mb-4 animated fadeInRight">
							<div class="card bg-light h-50">
								<div class="card-body p-1">
									<div v-if="image">
										<img :src="image" style="max-height: 236px" />
										<p class="mb-0" v-if="imageSize<=200">{{imageName}},
											檔案大小：{{imageSize}}KB</p>
										<p class="text-danger mb-0" v-else>{{imageName}},
											超過建議檔案大小({{imageSize}}KB)</p>
									</div>
								</div>
							</div>
							
							<blockquote class="blockquote">
								<h4 class="p-0">現行廣告</h4>
							</blockquote>
							<div class="card bg-light h-50">
								<div class="card-body p-1">
									<div v-if="nowAdImages">
										<div v-for="nowAdImage of nowAdImages" class="horizontal_center_clz" style="display:table;">
												<img :src="nowAdImage" style="max-height: 236px" />
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>

					<%--
					<div class="row">
						<div class="col-md-6 animated fadeInRight">
							<div class="card bg-light">
								<h4 class="m-2">圖片2</h4>
								<div class="card-body">
									<form>
										<div class="form-group row">
											<label for="img2Alt" class="col-sm-4 col-form-label">廣告描述</label>
											<div class="col-sm-8">
												<input type="text" class="form-control" id="img2Alt"
													placeholder="廣告描述" v-model="img2Alt">
											</div>
										</div>
										<div class="form-group row">
											<label for="img2Url" class="col-sm-4 col-form-label">鏈結網址</label>
											<div class="col-sm-8">
												<input type="text" class="form-control" id="img2Url"
													placeholder="鏈結網址" v-model="img2Url">
											</div>
										</div>
										<div class="form-group row">
											<label for="exampleFormControlFile1"
												class="col-sm-4 col-form-label">請選擇上傳圖片<br> <small>(建議圖片大小不超過200k)</small></label>
											<div class="col-sm-8">
												<input type="file" @change="onFileChange2" id="File2">
											</div>
										</div>
									</form>
								</div>
							</div>
						</div>
						<div class="col-md-6 animated fadeInRight">
							<div class="card bg-light h-100">
								<div class="card-body p-1">
									<div v-if="image2">
										<img :src="image2" style="max-height: 236px" />
										<p class="mb-0" v-if="image2Size<=200">{{image2Name}},
											檔案大小：{{image2Size}}KB</p>
										<p class="text-danger mb-0" v-else>{{image2Name}},
											超過建議檔案大小({{image2Size}}KB)</p>
									</div>
								</div>
							</div>
						</div>
					</div>
					--%> 
					</template>

					<%--choose group--%>
					<template v-if="step==3">
					<div class="row animated fadeInRight">
						<div class="col-md-6">
							<blockquote class="blockquote">
								<h4 class="p-0">顯示客群</h4>
							</blockquote>
							<div class="card ml-2 bg-light border-0">
								<div class="card-body">
									<div class="custom-controls-stacked">
										<label class="custom-control custom-checkbox"
											v-for="group of groups"> <input type="checkbox"
											class="custom-control-input" :value="group.title"
											v-model="adGroup" name="Choosecustomer"> <span
											class="custom-control-indicator"></span> <span
											class="custom-control-description"><h4>{{
													group.title }}</h4></span>
										</label>
									</div>
								</div>
							</div>
						</div>
					</div>
					</template>
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
<script src="js2/Business.js"></script>
<script src="js2/main.js"></script>
<script src="js2/simpleDateCheck.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		initPage();
	});
</script>
</html>
