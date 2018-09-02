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
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/bill-form.css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/jquery-ui.min.css">
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
<script src="<%=request.getContextPath()%>/js/jquery-ui.min.js"></script>
<script src="js/vue.min.js"></script>

<script type="text/javascript">
</script>
</head>
<body>
	<h1>分期申請紀錄</h1>
	<div class="container-fluid">
		<div class="row">
			<div id="app" class="col-md-12 mt-3">
				<h2 class="mb-3">
					<i class="fa fa-flag" aria-hidden="true"></i>
				</h2>
				<div class="table-responsive">

					<table border="1" width="100%">
						<tr>
							<td>
								開始日期
							</td>
							<td>
								<date-picker @update-date="updateStartDate"
									id="startDate" v-bind:value="startDate"></date-picker>
							</td>
							<td>
								結束日期
							</td>
							<td>
								<date-picker @update-date="updateEndDate" 
									id="endDate" v-bind:value="endDate"></date-picker>
							</td>
							<td>
								<input type="button" v-on:click="queryBtn" value="查詢" />
							</td>
						</tr>
					</table>

					<table border="1" class="table table-striped">
						<thead>
							<tr>
								<th v-bind:class="listClz">#</th>
								<th v-bind:class="listClz">分期帳單年月</th>
								<th v-bind:class="listClz">分期期數</th>
								<th v-bind:class="listClz">申請金額</th>
								<!-- <th v-bind:class="listClz">驗證碼</th>-->
								<th v-bind:class="listClz">申請時間</th>
								<th v-bind:class="listClz">總約定書版本</th>
								<!-- <th v-bind:class="listClz">注意事項版本</th>-->
							</tr>
						</thead>
						<tbody>
							<tr v-for="(vo, index) of fuconarwFucoapLst">
								<td v-bind:class="listClz">{{index + 1}}</td>
								<td v-bind:class="listClz">{{formatDateStr(vo.ibkApplDate)}}</td>
								<td v-bind:class="listClz">{{vo.ibkTransTimes}}</td>
								<td v-bind:class="listClz">{{vo.ibkTransAmt}}</td>
								<!-- <td v-bind:class="listClz"></td> -->
								<td v-bind:class="listClz">{{formatDateStr(vo.ibkApplDate)}}&nbsp;{{vo.ibkApplTime}}</td>
								<td v-bind:class="listClz">{{vo.fucoApplNo}}</td>
								<!-- <td v-bind:class="listClz">{{vo.fucoNoteNo}}</td>-->
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
		var fuconarwFucoapLst = JSON.parse("<%=request.getAttribute("fuconarwFucoapLst")%>");//
		app.fuconarwFucoapLst = fuconarwFucoapLst;
		
		app.startDate = getReqDate("<%=request.getAttribute("startDate")%>");
		app.endDate = getReqDate("<%=request.getAttribute("endDate")%>");
	});

	Vue.component('date-picker', {
	  template: '<input type="text" class="form-control"/>',
	  props: [],
	  mounted: function() {
	    var self = this;
	    var ops = {
	        dayNames: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"],
	        dayNamesMin: ["日", "一", "二", "三", "四", "五", "六"],
	        monthNames: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
	        monthNamesShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
	        prevText: "上月",
	        nextText: "次月",
	        weekHeader: "週",
	        showMonthAfterYear: true,
	        dateFormat: "yy-mm-dd",
	        onSelect: function(date) {
	          self.$emit('update-date', date);
	        },
	    };
	    $(this.$el).datepicker(ops);
	    $(this.$el).blur(function(){
	    	self.$emit('update-date', $(this).val());
	    })
	  },
	  beforeDestroy: function() {
	    $(this.$el).datepicker('hide').datepicker('destroy');
	  }
	});

	function getReqDate(dateStr){
		if($.trim(dateStr)!='' && dateStr != 'null'){
			return dateStr;
		}
		return getCurrentDate();
	}
	
	function getCurrentDate(){
		var d = new Date();
		var yyyy = ("0" + String(d.getFullYear())).slice(-4);
		var MM = ("0" + String(d.getMonth() + 1)).slice(-2);
		var dd = ("0" + d.getDate()).slice(-2);
		return yyyy + "-" + MM + "-" + dd;
	}
	
	var app = new Vue({
		el : '#app',
		data : {
			listClz : {
				"text_align_center" : true,
			},
			fuconarwFucoapLst : {},
			startDate : null,
			endDate : null,
		},
		methods : {
			formatDateStr : function(dateStr){
				return dateStr.substring(0,4) + "/" +
				dateStr.substring(4,6) + "/" + 
				dateStr.substring(6); 
			},
			updateStartDate : function(date) {
				this.startDate = date;
			},
			updateEndDate : function(date) {
				this.endDate = date;
			},
			queryBtn : function(e) {
				$(e.target).blur();
				var form = document.createElement("form");
				form.setAttribute("method", "post");
				form.setAttribute("action", "fuconarwFucoap_query");
				var params = {startDate : this.startDate, endDate : this.endDate};
				for ( var i in params) {
					if (params.hasOwnProperty(i)) {
						var input = document.createElement('input');
						input.type = 'hidden';
						input.name = i;
						if (params[i] == null) {
							input.value = "";
						} else {
							input.value = encodeURIComponent(params[i]);
						}
						form.appendChild(input);
					}
				}
				document.body.appendChild(form);
				form.submit();
			}
		}
	});
</script>
</html>