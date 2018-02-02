<%@ page language="java" contentType="text/html; charset=utf8"
	pageEncoding="utf8"%>
<%@ page import="com.fuco.mb.bill.dto.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.fuco.mb.conv.bank.writer.*"%>
<%@ page import="java.net.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/Skin2.css" />

<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.min.css" />
<!-- <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css" /> -->
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/bill-form.css" />
<style>
.ck-editor__editable {
    min-height: 400px;
}
.text_align_center {
	text-align : center;
}
</style>

<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/bootstrap.min.js"></script>

<script src="<%=request.getContextPath()%>/js/bill-form.js"></script>
<script	src="<%=request.getContextPath()%>/js/jquery.stickytableheaders.min.js"></script>
<!-- <script src="<%=request.getContextPath()%>/js/ckeditor.js"></script>-->
<script src="js/vue.min.js"></script>

<script type="text/javascript">
</script>
</head>
<body>
        <div class="container-fluid">
		<div class="row">
			<div id="app" class="col-md-12 mt-3">
				<h2 class="mb-3">
					<i class="fa fa-flag" aria-hidden="true"></i>{{getTitle()}}
				</h2>
				<div class="table-responsive">
					<table class="table table-striped">
						<thead>
							<tr>
								<th v-bind:class="listClz">#</th>
								<th v-bind:class="listClz">版本</th>
								<th v-bind:class="listClz">動作</th>
							</tr>
						</thead>
						<tbody>
							<tr v-for="(vo, index) of versionControlLst">
								<td v-bind:class="listClz">{{index + 1}}</td>
								<td v-bind:class="listClz">{{vo.version}}</td>
								<td v-bind:class="listClz">
									<button class="btn btn-sm btn-outline-primary" 
										v-on:click="previewMobileBtn(index, vo)"
										v-show="true">手機預覽</button>
									<button class="btn btn-sm btn-outline-primary" 
										v-on:click="previewWebBtn(index, vo)"
										v-show="true">網站預覽</button>
									<button class="btn btn-sm btn-outline-primary" 
										v-on:click="deleteBtn(index, vo)"
										v-show="true">刪除</button>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		</div>

</body>
<script type="text/javascript">
	$(document).ready(function() {
		var versionControlLst = JSON.parse("<%=request.getAttribute("versionControlLst")%>");//
		app.versionControlLst = versionControlLst;
		var type = "<%=request.getAttribute("type")%>";
		app.type = type;
	});
	
	var win = null;
	function showInPopupWindow(content, width, height){
		if(win != null && win != undefined){
			win.close();
		}
		win = window.open('versionControl_pupop','Popup_Window', //
			'toolbar=0,scrollbars=0,location=0,statusbar=0,menubar=0,resizable=0,width=500,height=300,left = 312,top = 234');
		win.focus();
		$(win.document).ready(function() {
			console.log("win - " + win.document);
			if(width == 0 && height == 0){
				win.showData4Window(content);
			}else{
				win.showData4Mobile(content, width, height);
			}
		});
	}
	
	function openWindowWithPostRequest(content, width, height) {
		var winName='MyWindow';
		var winURL='versionControl_pupop';
		var windowoption='resizable=yes,height=600,width=800,location=0,menubar=0,scrollbars=1';
		var params = { 'content' : content, "width" : width, "height" : height};         
		var form = document.createElement("form");
		form.setAttribute("method", "post");
		form.setAttribute("action", winURL);
		form.setAttribute("target",winName);  
		for (var i in params) {
			if (params.hasOwnProperty(i)) {
				var input = document.createElement('input');
				input.type = 'hidden';
				input.name = i;
				input.value = encodeURIComponent(params[i]);
				form.appendChild(input);
			}
		}              
		document.body.appendChild(form);                       
		var win = window.open('', winName,windowoption);
		win.focus();
		form.target = winName;
		form.submit();                 
		document.body.removeChild(form);           
	}
	
	var app = new Vue({
		el : '#app',
		data : {
			listClz : {
				"text_align_center" : true,
			},
			versionControlLst : {},
			version : "",
			type : "",
		},
		methods : {
			getTitle : function(){
				switch(this.type){
				case "1":
					return "上傳分期總約書管理";
				case "2":
					return "上傳申請注意事項文字管理";
				} 
				return "unknow";
			},
			previewMobileBtn : function(idx, vo){
				//showInPopupWindow(vo.context, 320, 568);
				openWindowWithPostRequest(vo.context, 320, 568);
			},
			previewWebBtn : function(idx, vo){
				//showInPopupWindow(vo.context, 0, 0);
				openWindowWithPostRequest(vo.context, 0, 0);
			},
			deleteBtn : function(idx, vo){
				$.ajax({
					type : 'POST',
					url : "versionControl_delete",
					data : {version : vo.version, type : app.type},
					dataType : "JSON",
					success : function(data) {
						console.log('版本刪除完成');
						if (data && data.result == true) {
							alert('版本刪除完成');
							document.location.href = "versionControl_query?type=" + app.type;
						} else if (data && $.trim(data.message) != '') {
							alert(data.message);
						}
					},
					error : function(jqXHR, textStatus, errorThrown) {
						console.log("版本刪除");
						alert('版本刪除失敗！！，請聯繫系統管理人員處理，謝謝');
						//alert(jqXHR + " , " + textStatus + " , " + errorThrown);
					}
				});
			},
		}
	});
</script>
</html>