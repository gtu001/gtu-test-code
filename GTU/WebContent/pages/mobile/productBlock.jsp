<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.sti.estore.model.MobilePortalActCategory"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%-- meta --%>
<jsp:include page="/mobilestore/jsp/frontend/shopping/commonMeta.jsp" />
<%-- css --%>
<jsp:include page="/mobilestore/jsp/frontend/shopping/commonCss.jsp" />
<%-- js --%>
<jsp:include page="/mobilestore/jsp/frontend/shopping/commonJs.jsp" />
		

<script type="text/javascript" src="<c:url value="/mobilestore/js/jquery.masonry.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/mobilestore/owl-carousel/owl.carousel.js"/>"></script>

<meta http-equiv="Content-Type"       content="text/html; charset=utf-8" />
<meta http-equiv="Content-Style-Type" content="text/css"/>
<meta http-equiv="Cache-Control"      content="no-store" />
<meta http-equiv="Pragma"             content="no-cache" />
<meta http-equiv="Expires"            content="0" />	

<script type="text/javascript">
	// index.jsp 的 javascript ↓↓↓↓↓
	//廣告banner
	$(document).ready(function() {
	});
	
	//商品排版
	$(window).load(function () {
		var footerCopy = $(".copyRight").clone();
		$("#MIEELD_CONTENT").load('<c:url value="/mFrontendIndex.do?cmd=showMainPortal" />',//initMiddleInclude
			{},
			function(responseText){
				$("#owl-demo").owlCarousel({
					navigation : false,
					slideSpeed : 300,
					paginationSpeed : 300,
					singleItem : true,
					rewindSpeed : 0,
					autoPlay : 10000 //十秒轉頁
				});
				
				if($(".copyRight").length == 0){
					$("div[data-role='page']").append(footerCopy);
				}
			}
		);
		
		$(".indexPrd_li h3").click(function () {
	        $('.masonry_box').imagesLoaded(function () {
	            $('.masonry_box').masonry({
	                itemSelector: '.index_imgBox',
	                animate: true
	            });
	        });
	    });
	});
</script>

<script type="text/javascript">
	// middle.jsp 的 javascript ↓↓↓↓↓
	//煥業
	function showActivityDetail(coType, actId){
		var pageNoHidden = $("#"+coType+"_"+actId+"_pageNoHidden");
		if(pageNoHidden.parents(".indexPrd_li").find("#noData").length != 0){
			return;//已沒資料不顯示下一頁
		}
		var pageNo = parseInt(pageNoHidden.val());
		if(isNaN(pageNo)){
			pageNo = 0;
		}
		pageNo ++;
		//alert("coType = " + coType + ", actId = " + actId + ", pageNO = " + pageNo);
		var urlStr = '<c:url value="/mFrontendIndex.do?cmd=showActivityDetail"/>';
		urlStr += "&coType=" + coType + "&pageNo=" + pageNo + "&ACT=" + actId;
		$("div.b_box.b_box01.masonry_box[id*="+coType+"_"+actId+"]").load(
			urlStr,
			{},
			function(responseText){
				pageNoHidden.val(pageNo);
				pageNoHidden.parents(".indexPrd_li").find(".masonry_box").resize();
				if(pageNoHidden.parents(".indexPrd_li").find("#noData").length!=0){
					pageNoHidden.val("");
				}
			}
		);
	}
	
	function lookMoreLink(eventId, coType, productId){
		var param = "";
		param += "event_id=" + eventId;
		
		switch(coType){
		case 'LH':
			param += "&da_type=H";
			param += "&co_type=LH";
		break;
		case 'LC':
			param += "&co_type=LC";
		break;
		case 'DA_HANDSET':
			param += "&da_type=H";
			param += "&co_type=DA";
		break;
		case 'DA_ACCESSORY':
			param += "&da_type=A";
			param += "&co_type=DA";
		break;
		}
		param += "&mobile_web=Y";
		if(typeof(productId) != "undefined"){
			param += "&prod_id=" + productId;
		}
		var urlStr = '<c:url value="/event?"/>' + param;
		window.location = urlStr;
	}
	
	$(document).ready(function(){
		//初始化其他區塊
		$(".indexPrd_li h3").bind("click.init", function(){
			var currentId = $(this).parents(".indexPrd_li").find("input[id$=_pageNoHidden]").attr("id");
			//重設各區塊狀態
			var choiceContent = null;
			var coType = null;
			var actId = null;
			//重設頁數
			$("input[id$=_pageNoHidden]").each(function(){
				if($(this).attr("id") == currentId){
					var array = /(LC|LH|DA_HANDSET|DA_ACCESSORY)\_(\d+)/g.exec($(this).attr("id"));
					if(array!=null && array.hasOwnProperty("1") && array.hasOwnProperty("2")){
						choiceContent = $(this).parents(".indexPrd_li").find("div.b_box.b_box01.masonry_box");
						coType = array["1"];
						actId = array["2"];
					}
				}else{
					$(this).val("");
				}
			});
			if(choiceContent != null && coType != null && actId != null){
				if(!choiceContent.parent().hasClass("ui-collapsible-content-collapsed")){
					showActivityDetail(coType, actId);
				}else{
					choiceContent.children().remove();//非開啟狀態移調子項目
					$("input[id$=_pageNoHidden]").val("");
				}
			}
		});
		
		$("div[data-role=collapsible-set]").each(function(){
			$(this).find("h3").eq(0).trigger("click");
		});
	});
</script>
<!-- xxxxxxxxxxxxxxxxxxxxxxxxxxxxx by gtu001 ↓↓↓↓↓↓ 流動標題 -->
<div class="shadow_T"></div>
<div class="shadow_B"></div>
<div id="owl-demo" class="owl-carousel banner_box">
	<c:forEach var="item" items="${adItems}">
		<div class="item">
			<a href="<c:url value="${item.contentUrl}"/>" data-ajax="false" title="${item.title}"> 
				<img src="<c:url value="${item.imagePath}"/>">
				<!-- ${item.imagePath} TODO 待確認-->
			</a>
		</div>
	</c:forEach>	
</div>
<!-- xxxxxxxxxxxxxxxxxxxxxxxxxxxxx by gtu001 ↑↑↑↑↑↑ 流動標題 -->
<!-- xxxxxxxxxxxxxxxxxxxxxxxxxxxxx by gtu001 ↓↓↓↓↓↓ 中間區塊 -->
<c:forEach var="activityCategory_0" items="${All_activityCategory}" varStatus="status">
	<c:choose>
		<c:when test="${status.index == LC_INDEX}">
			<!-- LC -->
			<div class="title_T T${status.index + 1}">
				<h1 class="t_T${status.index + 1}">${activityCategory_0.title}</h1>
				<div class="shadow_T"></div>
			</div>
			<ul data-role="listview">
				<c:forEach var="item" items="${activityCategory_0.mobilePortalActivities}" varStatus="status2">
					<c:choose>
						<c:when test="${status2.index == 0}">
							<div class="con2_banner">
					            <a href="javascript:lookMoreLink('${item.activity.id}', '${activityCategory_0.coType}');">
					                <!-- <img src='<c:url value="${item.activity.imagePath}" />' /> -->
					                <img src="<c:url value="/mobilestore/images/push_icon03.png"/>" />
					            </a>
					        </div>
						</c:when>
						<c:otherwise>
							<c:if test="${ROWS_LIMIT == -1 || status2.index < ROWS_LIMIT}">
								<li>
					                <a href="javascript:lookMoreLink('${item.activity.id}', '${activityCategory_0.coType}');">
					                    <span class="arrLeft">
					                    	${item.activity.title}
					                    	<!-- activity[Id:${item.activity.id}] MobilePortalActivity[Id:${item.id}] -->	
					                    </span>
					                </a>
					            </li>
				            </c:if>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			 </ul>
		</c:when>
		<c:otherwise>
			<!-- 非LC = ${activityCategory_0.coType} -->
			<div class="title_T T${status.index + 1}">
				<h1 class="t_T${status.index + 1}">${activityCategory_0.title}</h1>
				<div class="shadow_T"></div>
			</div>
			<div data-role="collapsible-set">
				<c:forEach var="item" items="${activityCategory_0.mobilePortalActivities}" varStatus="status2">
					<c:if test="${ROWS_LIMIT == -1 || status2.index < ROWS_LIMIT}">
						<div data-role="collapsible" data-inset="false" data-iconpos="right" class="indexPrd_li">
							<h3 id="activity_title_${status2.index}">
								<span class="arrLeft">
									${item.activity.title}
									<!-- activity[Id:${item.activity.id}] MobilePortalActivity[Id:${item.id}] -->
								</span>
							</h3>
							<div class="b_box b_box01 masonry_box" id="div_content_${activityCategory_0.coType}_${item.activity.id}">
								<!--#### 產品細節在此  productBlock_details.jsp ####-->
							</div>
							<div class="moreBox">
								<input type="hidden" id="${activityCategory_0.coType}_${item.activity.id}_pageNoHidden" value="" />
								<a href="javascript:showActivityDetail('${activityCategory_0.coType}', '${item.activity.id}')" class="moreAf">看更多</a>
								<!-- <a href="javascript:lookMoreLink('${item.activity.id}', '${activityCategory_0.coType}')" class="moreAf">看更多</a>-->
							</div>
						</div>
					</c:if>
				</c:forEach>
			</div>
		</c:otherwise>
	</c:choose>
</c:forEach>
<!-- xxxxxxxxxxxxxxxxxxxxxxxxxxxxx by gtu001 ↑↑↑↑↑↑ 中間區塊 -->
<!-- xxxxxxxxxxxxxxxxxxxxxxxxxxxxx by gtu001 ↓↓↓↓↓↓ 底部banner -->
<div class="firstApp">搶先預約</div>
<!-- portalImage1, portalImage2, shareImagePath -->
<c:forEach var="item" items="${footerBanner}">
	<div class="firstAppBanner">
	    <a title="${item.title}" href="<c:url value="/mPreorderActivity/preorder.do?activityId=${item.id}"/>" target="_self">
			<img src="<c:url value="${item.portalImage1}"/>" />
		</a>
	</div>
</c:forEach>
<!-- xxxxxxxxxxxxxxxxxxxxxxxxxxxxx by gtu001 ↑↑↑↑↑↑ 底部banner -->