<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>
<%@ page import="java.sql.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<%@include file="../common/headCommon.jsp"%>
<title>Insert title here</title>
<style>
.panel-default {
	padding: 13px;
	border: solid 1px #DDDDDD;
	box-shadow: 0 0 1px 1px #DDDDDD;
}

.row {
	padding: 5px;
}

.form-group {
	padding-right: 5px;
}

.form-control {
	background-color: #FAFAFA;
	font-size: 13px;
	border: 0px;
}

.input-group-text {
	background-color: #FAFAFA;
	font-size: 13px;
	border: 0px;
	color: #386F99;
}

.input-text-black {
	color: #000000;
}

.span-style {
	background-color: #386F99;
	color: #FFFFFF;
	font-size: 11px;
	padding-right: 5 !important;
}

hr {
	display: block;
	height: 1px;
	border: 0;
	border-top: 1px solid #386F99;
	margin: 1em 0;
	padding: 0;
	padding-left: 15px;
}

.widthset {
	width: 50%;
}

.paddingleft {
	padding-left: 15px;
}

.border-right {
	color: #386F99;
}

.splitline {
	border-right: 1px solid #386F99;
}

ul li a:hover {
	background-color: #386F99 !important;
	color: #FFFFFF;
}

ul li a.selected {
	background-color: #386F99 !important;
	color: #FFFFFF;
}

.fontsize {
	font-size: 13px;
	color: black;
}

.colcolor {
	background-color: #E4E2E9;
	border-radius: 3px;
}

span {
  display: inline-block;
}

.mytext {
  width: 70px;
}
</style>
</head>
<body>
	<div class="wrapper">
		<!-- Sidebar Holder -->
		<jsp:include page="../common/LeftNav.jsp"></jsp:include>
		<div id="content">
			<!-- Page Content Holder -->
			<jsp:include page="../common/TopNav.jsp"></jsp:include>
			<div style="margin: 10px">
				<div class="panel panel-default"
					style="-moz-border-radius: 5px; -webkit-border-radius: 5px; -ie-border-radius: 5px; -opera-border-radius: 5px; -chrome-border-radius: 5px; border-radius: 5px;">
					<div class="panel-heading">
						<form class="form-inline">
							<div class="form-group">
								<div class="input-group">
									<div class="input-group-prepend">
										<span class="input-group-text" id="basic-addon3"> <img
											id="logo"
											src="<%=request.getContextPath()%>/static/jpg/21.svg">&nbsp
											客戶姓名:
										</span>
									</div>
									<div class='input-group' id='datetimepicker1'>
										<input type='text' class="form-control" size="14" />
									</div>
								</div>
							</div>
							<div class="form-group">
								<div class="input-group">
									<div class="input-group-prepend">
										<span class="input-group-text" id="basic-addon3"> <img
											id="logo"
											src="<%=request.getContextPath()%>/static/jpg/20.svg">&nbsp
											電話:
										</span>
									</div>
									<div class='input-group' id='datetimepicker1'>
										<input type='text' class="form-control" size="14" />
									</div>
								</div>
							</div>
							<div class="form-group">
								<div class="input-group">
									<div class="input-group-prepend">
										<span class="input-group-text" id="basic-addon3"> <img
											id="logo"
											src="<%=request.getContextPath()%>/static/jpg/19.svg">&nbsp
											開戶分公司:
										</span>
									</div>
									<div class='input-group' id='datetimepicker1'>
										<input type='text' class="form-control" size="14" />
									</div>
								</div>
							</div>
							<div class="form-group">
								<div class="input-group">
									<div class="input-group-prepend">
										<span class="input-group-text" id="basic-addon3"> <img
											id="logo"
											src="<%=request.getContextPath()%>/static/jpg/18.svg">&nbsp
											營業員:
										</span>
									</div>
									<div class='input-group' id='datetimepicker1'>
										<input type='text' class="form-control" size="14" />
									</div>
								</div>
							</div>
						</form>
						<hr width="100%">
					</div>
					<div class="panel-body">
						<form class="row">
							<div class="col-6 splitline">
								<div class="row w-100 paddingleft">
									<div class="col-auto mr-auto input-group-text">
										<img id="logo"
											src="<%=request.getContextPath()%>/static/jpg/22.svg">&nbsp
										參照頁
									</div>
									<div class="col-auto input-group-text">編輯</div>
								</div>
								<hr width="95%">
								<div class="row">
									<div class="col-12">
										<ul id="reference" class="nav nav-tabs justify-content-center">
											<li><a class="input-group-text" href="#document"
												data-toggle="tab">證件</a></li>
											<li class="active"><a class="input-group-text"
												href="#data" data-toggle="tab">資料</a></li>
											<li><a class="input-group-text" href="#video"
												data-toggle="tab">視訊</a></li>
											<li><a class="input-group-text" href="#bank"
												data-toggle="tab">銀行</a></li>
										</ul>
									</div>
									<div class="col-12">
										<div id="referenceTabContent" class="tab-content">
											<div class="tab-pane fade" id="document">
												<p>菜鸟教程是一个提供最新的web技术站点，本站免费提供了建站相关的技术文档，帮助广大web技术爱好者快速入门并建立自己的网站。菜鸟先飞早入行——学的不仅是技术，更是梦想。</p>
											</div>
											<div class="tab-pane fade in active" id="data">
												<div class="row">
													<div class="col-md-1"></div>
													<div class="col-md-2 input-group-text colcolor">姓名</div>
													<div class="col-md-3 input-group-text fontsize">陳哈哈</div>
													<div class="col-md-2 input-group-text colcolor">英文</div>
													<div class="col-md-3 input-group-text fontsize"></div>
													<div class="col-md-1"></div>
												</div>
												<div class="row">
													<div class="col-md-1"></div>
													<div class="col-md-2 input-group-text colcolor">身分證號</div>
													<div class="col-md-3 input-group-text fontsize">W1234567890</div>
													<div class="col-md-2 input-group-text colcolor">性別</div>
													<div class="col-md-3 input-group-text fontsize">男</div>
													<div class="col-md-1"></div>
												</div>
												<div class="row">
													<div class="col-md-1"></div>
													<div class="col-md-2 input-group-text colcolor">學歷</div>
													<div class="col-md-3 input-group-text fontsize">天才</div>
													<div class="col-md-2 input-group-text colcolor">生日</div>
													<div class="col-md-3 input-group-text fontsize">19850120</div>
													<div class="col-md-1"></div>
												</div>
												<div class="row">
													<div class="col-md-1"></div>
													<div class="col-md-2 input-group-text colcolor">戶籍地址</div>
													<div class="col-md-8 input-group-text fontsize">台北市</div>
													<div class="col-md-1"></div>
												</div>
												<div class="row">
													<div class="col-md-1"></div>
													<div class="col-md-2 input-group-text colcolor">通訊</div>
													<div class="col-md-8 input-group-text fontsize">N/A</div>
													<div class="col-md-1"></div>
												</div>
												<div class="row">
													<div class="col-md-1"></div>
													<div class="col-md-2 input-group-text colcolor">手機號碼</div>
													<div class="col-md-3 input-group-text fontsize">0912345678</div>
													<div class="col-md-2 input-group-text colcolor">電話</div>
													<div class="col-md-3 input-group-text fontsize"></div>
													<div class="col-md-1"></div>
												</div>
												<div class="row">
													<div class="col-md-1"></div>
													<div class="col-md-2 input-group-text colcolor">戶籍電話</div>
													<div class="col-md-3 input-group-text fontsize"></div>
													<div class="col-md-2 input-group-text colcolor">Fax</div>
													<div class="col-md-3 input-group-text fontsize"></div>
													<div class="col-md-1"></div>
												</div>
												<div class="row">
													<div class="col-md-1"></div>
													<div class="col-md-2 input-group-text colcolor">email</div>
													<div class="col-md-8 input-group-text fontsize">chen.abc@gj.com</div>
													<div class="col-md-1"></div>
												</div>
												<div class="row">
													<div class="col-md-1"></div>
													<div class="col-md-2 input-group-text colcolor">公司</div>
													<div class="col-md-3 input-group-text fontsize">台灣銀行</div>
													<div class="col-md-2 input-group-text colcolor">公司電話</div>
													<div class="col-md-3 input-group-text fontsize"></div>
													<div class="col-md-1"></div>
												</div>
												<div class="row">
													<div class="col-md-1"></div>
													<div class="col-md-2 input-group-text colcolor">職業</div>
													<div class="col-md-3 input-group-text fontsize">營業員</div>
													<div class="col-md-2 input-group-text colcolor">職業</div>
													<div class="col-md-3 input-group-text fontsize"></div>
													<div class="col-md-1"></div>
												</div>
												<div class="row">
													<div class="col-md-1"></div>
													<div class="col-md-2 input-group-text colcolor">公司地址</div>
													<div class="col-md-8 input-group-text fontsize"></div>
													<div class="col-md-1"></div>
												</div>
												<div class="row">
													<div class="col-md-1"></div>
													<div class="col-md-2 input-group-text colcolor">國籍</div>
													<div class="col-md-3 input-group-text fontsize"></div>
													<div class="col-md-2 input-group-text colcolor">第二國籍</div>
													<div class="col-md-3 input-group-text fontsize"></div>
													<div class="col-md-1"></div>
												</div>
											</div>
											<div class="tab-pane fade" id="video">
												<p>jMeter 是一款开源的测试软件。它是 100% 纯 Java 应用程序，用于负载和性能测试。</p>
											</div>
											<div class="tab-pane fade" id="bank">
												<div class="row">
													<div class="col-md-1"></div>
													<div class="col-md-3 input-group-text colcolor">銀行名稱</div>
													<div class="col-md-7 input-group-text fontsize">BANK OF TAIWAN LIANCHENG BRANCH</div>
													<div class="col-md-1"></div>
												</div>
												<div class="row">
													<div class="col-md-1"></div>
													<div class="col-md-3 input-group-text colcolor">SWIFT CODE</div>
													<div class="col-md-7 input-group-text fontsize">BKTWTWP048</div>
													<div class="col-md-1"></div>
												</div>
												<div class="row">
													<div class="col-md-1"></div>
													<div class="col-md-3 input-group-text colcolor">帳號</div>
													<div class="col-md-7 input-group-text fontsize">1234567890123456</div>
													<div class="col-md-1"></div>
												</div>
												<div class="row">
													<div class="col-md-1"></div>
													<div class="col-md-3 input-group-text colcolor">戶名</div>
													<div class="col-md-7 input-group-text fontsize">陳先生</div>
													<div class="col-md-1"></div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="col-6">
								<div class="row w-100 paddingleft">
									<div class="col-auto mr-auto input-group-text">
										<img id="logo"
											src="<%=request.getContextPath()%>/static/jpg/23.svg">&nbsp
										審查頁
									</div>
								</div>
								<hr width="95%">
								<div class="row">
									<div class="col-12">
										<ul id="review" class="nav nav-tabs justify-content-center">
											<li class="active"><a class="input-group-text"
												href="#idcardTab" data-toggle="tab">證件</a></li>
											<li class="active"><a class="input-group-text"
												href="#videoTab" data-toggle="tab">視訊</a></li>
											<li><a class="input-group-text" href="#bankTab"
												data-toggle="tab">銀行</a></li>
											<li><a class="input-group-text" href="#signatureTab"
												data-toggle="tab">簽名式樣</a></li>
											<li><a class="input-group-text" href="#recordTab"
												data-toggle="tab">紀錄</a></li>
										</ul>
									</div>
									<div class="col-12">
										<div id="reviewTabContent" class="tab-content">
											<div class="tab-pane active" id="idcardTab">
												<div class="tabbable" style="margin: 3px;">
													<ul class="nav nav-pills justify-content-center"
														id="divstyletab">
														<li class="active"><a class="input-group-text"
															href="#tab1" data-toggle="tab">身分證(正)</a></li>
														<li><a class="input-group-text" href="#tab2"
															data-toggle="tab">身分證(背)</a></li>
														<li><a class="input-group-text" href="#tab3"
															data-toggle="tab">第二證件</a></li>
													</ul>
													<div class="tab-content">
														<!-- 第一个tab -->
														<div class="tab-pane fade active in" id="tab1">
															<img id="logo" class="img-fluid"
																src="<%=request.getContextPath()%>/static/jpg/idcardFront.png">
															<div class="row">
															<div class="col-2">
																<span class="input-group-text" id="basic-addon3">	審核狀態: </span>
															</div>
															<div class="col-2">
																<span class="input-group-text input-text-black">未審核</span>
															</div>
															<div class="col-2">
															</div>
															<div class="col-2 mr-0">
															<span class="input-group-text span-style mytext">需補件</span>
															</div>
															<div class="col-2 mr-0">
															<span class="input-group-text span-style mytext">複審通過</span>
															</div>
															<div class="col-2 mr-0">
															<span class="input-group-text span-style mytext">退回初審</span>
															</div>															</div>
														</div>
														<!-- 第二个tab -->
														<div class="tab-pane fade" id="tab2">b</div>
														<!-- 第三个tab -->
														<div class="tab-pane fade" id="tab3">c</div>
														<!-- 中间图表结束 -->
													</div>
												</div>
											</div>
											<div class="tab-pane" id="videoTab">
												<div class="tabbable" style="margin: 3px;">
													<ul class="nav nav-pills justify-content-center"
														id="divstyletab">
														<li class="active"><a class="input-group-text"
															href="#tab4" data-toggle="tab">test</a></li>
														<li><a class="input-group-text" href="#tab5"
															data-toggle="tab">test1</a></li>
														<li><a class="input-group-text" href="#tab6"
															data-toggle="tab">test2</a></li>
													</ul>
													<div class="tab-content">
														<!-- 第一个tab -->
														<div class="tab-pane fade active in" id="tab4">a</div>
														<!-- 第二个tab -->
														<div class="tab-pane fade" id="tab5">b</div>
														<!-- 第三个tab -->
														<div class="tab-pane fade" id="tab6">c</div>
														<!-- 中间图表结束 -->
													</div>
												</div>
											</div>
											<div class="tab-pane" id="bankTab">
												<div class="fontsize">銀行帳戶</div>
												<img id="logo" class="img-fluid"
																src="<%=request.getContextPath()%>/static/jpg/idcardFront.png">
												<div class="row">
													<div class="col-md-1"></div>
													<div class="col-md-3 input-group-text colcolor">銀行名稱</div>
													<div class="col-md-7 input-group-text fontsize">台灣銀行</div>
													<div class="col-md-1"></div>
												</div>
												<div class="row">
													<div class="col-md-1"></div>
													<div class="col-md-3 input-group-text colcolor">銀行帳戶</div>
													<div class="col-md-7 input-group-text fontsize">677</div>
													<div class="col-md-1"></div>
												</div>
											</div>
											<div class="tab-pane" id="signatureTab">
												<img id="logo" class="img-fluid"
																src="<%=request.getContextPath()%>/static/jpg/signature.png">
											</div>
											<div class="tab-pane" id="recordTab">
												<div class="tabbable" style="margin: 3px;">
													<ul class="nav nav-pills justify-content-center"
														id="divstyletab">
														<li class="active"><a class="input-group-text"
															href="#tab13" data-toggle="tab">123</a></li>
														<li><a class="input-group-text" href="#tab14"
															data-toggle="tab">321</a></li>
														<li><a class="input-group-text" href="#tab15"
															data-toggle="tab">666</a></li>
													</ul>
													<div class="tab-content">
														<!-- 第一个tab -->
														<div class="tab-pane fade active in" id="tab13">a</div>
														<!-- 第二个tab -->
														<div class="tab-pane fade" id="tab14">b</div>
														<!-- 第三个tab -->
														<div class="tab-pane fade" id="tab15">c</div>
														<!-- 中间图表结束 -->
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</form>
					</div>
					<div class="panel-footer"></div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>