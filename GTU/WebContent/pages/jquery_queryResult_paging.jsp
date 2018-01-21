<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/head.jsp"%>
<%@ page import="java.util.Enumeration"%>
<script type="text/javascript"
	src="<c:url value="/js/jquery-1.3.2.min.js" />"></script>

<script language="javascript">
	try{
		jQuery.noConflict();
		var size = 0;
		var currentPage = 1;
		var endPage = -1;
		var tableBody = null;
		jQuery(document).ready(function(){
			var includeDiv = jQuery("#INCLUDE_DIV");
			tableBody = jQuery("table[id=docList] tbody", includeDiv);
			var emptyTr = jQuery(".emptyData").clone();
			var copyTr = jQuery(".forCloneTr").clone();
			var url = "<c:url value="/admin/marketing/mobilePortalAD.do?cmd=initAjax" />";
			
			jQuery.ajax({  
				url: url,  
				type : "GET",
				dataType : "xml",
				success: function(data) { 
					var rows = jQuery("data > row", data);
					if(rows.length == 0){
						jQuery(".changePageTR").hide();
						return;
					}else{
						jQuery(".emptyData").remove();
					}
					jQuery(".forCloneTr").remove();
					
					rows.each(function(index){
						var newTr = copyTr.clone();
						
						var onShelf = new Boolean().valueOf(jQuery("onShelf", this).text());
						var adItemId = jQuery("adItemId", this).text();
						var title = jQuery("title", this).text();
						var displayOrder = jQuery("displayOrder", this).text();
						var startDate = jQuery("startDate", this).text().replace(/:\d+\.\d+$/, '').replace("-", "/").replace("-", "/");
						var endDate = jQuery("endDate", this).text().replace(/:\d+\.\d+$/, '').replace("-", "/").replace("-", "/");
						
						if(onShelf){
							newTr.find(".delTR").remove();
						}
						
						newTr.find(".delTR").click(function(){
							if(confirm("確認刪除:" + title + "?")){
								newTr.remove();
							}
							reflashTableCss();
							if(tableBody.find("tr").length == 0){
								tableBody.append(emptyTr);
							}
						});
						
						//newTr.find(".editTR")
						newTr.find(".displayOrder").text(displayOrder);
						newTr.find(".title a").text(title).attr("href", "javascript:void(0);");
						newTr.find(".endDate").text(startDate + " - " + endDate);
						newTr.find(".onShelf").text(onShelf ? "已上架" : "未上架");
						
						if(size > 9){
							newTr.hide();
						}
						
						newTr.attr("id", "rowData_" + index);
						
						tableBody.append(newTr);
						size ++;
					});
					
					reflashTableCss();
					includeDiv.show();
					reflushPageParams();
				},
				error: function(a, b, c){
					alert("小網上方區塊內容取得失敗!");
				}
			});
		});
		function reflashTableCss(){
			tableBody.find("tr").each(function(index){
				var tr1 = jQuery(this);
				if(index % 2 == 0){
					tr1.removeClass("oddBgColor");
				}else{
					tr1.addClass("oddBgColor");
				}
			});
		}
		function reflushPageParams(){
			endPage = parseInt(size/10);
			if(size/10 > 0){
				endPage ++;
			}
			jQuery(".startPage").text("1");
			jQuery(".endPage").text(endPage);
			jQuery(".totalCount").text(size);
		}
		function goToPage_first(){
			if(currentPage == 1){
				return;
			}
			goToAssignedPage_ajax(1);
		}
		function goToPage_prev(){
			if(currentPage <= 1){
				return;
			}
			goToAssignedPage_ajax(currentPage - 1);
		}
		function goToPage_next(){
			if(currentPage >= endPage){
				return;
			}
			goToAssignedPage_ajax(currentPage + 1);
		}
		function goToPage_last(){
			goToAssignedPage_ajax(endPage);
		}
		function goToAssignedPage_ajax(page){
			if(page == undefined){
				page = parseInt(jQuery(".jumpPageInput_ajax").val());
				if(isNaN(page)){
					alert("請輸入頁碼!");
					return;
				}
			}
			var start = (page - 1) * 10;
			var end = (page) * 10 - 1;
			start = start < 0 ? 0 : start;
			end = end > (size - 1) ? (size - 1) : end;
			//alert("page: " + page + " --> "+ start + " ... " + end);
			jQuery("tr[id^=rowData_]").each(function(index){
				var result = parseInt(/rowData\_(\d+)/.exec(jQuery(this).attr("id"))[1]);
				if(result < start || result > end){
					jQuery(this).hide();
				}else{
					jQuery(this).show();
				}
			});
			currentPage = page;
		}
	}catch(e){
		var msg = '';
		for(var ii in e){
			msg += ii + ".." + e[ii];
		}
		alert(msg);
	}
	//alert('\'end');
</script>

<div id="INCLUDE_DIV">
	<form id="resultForm2" name="resultForm2" method="post"
		action="<c:url value="/admin/marketing/mobilePortalAD.do" />">

		<input type="hidden" name="cmd" id="cmd" value="" /> <input
			type="hidden" name="pageNo" id="pageNo" value="${pageNo}" /> <input
			type="hidden" name="adTabId" id="adTabId" value="${adTabId}" /> <input
			type="hidden" name="adItemId" id="adItemId" value="" /> <input
			type="hidden" name="onShelf" id="onShelf" value="${onShelf}" /> <input
			type="hidden" name="actionMode" id="actionMode" value="" />
		<div style="text-align: right;">
			<a href="<c:url value='/img/new/portal_top_block.jpg'/>"
				target="_blank"> <span class="ps_inline"> 前台圖文位置</span>
			</a>
		</div>

		<div class="dataBody" style="width: 98%; margin: 0 auto;">
			<table>
				<tr>
					<td style="width: 25%;"><label class="fieldCaption">上架狀態：</label>
						<select id="onShelfSelect" name="onShelfSelect">
							<option value="ALL"
								<c:if test="${onShelf eq 'ALL' }">selected</c:if>>全部</option>
							<option value="Y" <c:if test="${onShelf eq 'Y' }">selected</c:if>>已上架</option>
							<option value="N" <c:if test="${onShelf eq 'N' }">selected</c:if>>已下架</option>
					</select></td>
					<td><label class="fieldCaption">上架期間：</label> <input
						id="startDate" name="startDate" type="text" size="10"
						value="${startDate}" /> <img
						src="<c:url value="/img/icon/calendar.gif" />"
						name="startDateIcon" id="startDateIcon" style="Cursor: Hand"
						onClick="cal1xx.select(document.getElementById('startDate'),'startDateIcon','yyyy/MM/dd'); return false;" />
						～ <input id="endDate" name="endDate" type="text" size="10"
						value="${endDate}" /> <img
						src="<c:url value="/img/icon/calendar.gif" />" name="endDateIcon"
						id="endDateIcon" style="Cursor: Hand"
						onClick="cal1xx.select(document.getElementById('endDate'),'endDateIcon','yyyy/MM/dd'); return false;" />
					</td>
				</tr>
				<tr>
					<td colspan="2" style="width: 25%;"><input type="button"
						id="productName" name="productName" class="actionButton"
						value="查詢" onclick="queryAdItem();" /></td>
				</tr>
			</table>
			<table border="0">
				<!-- xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx by gtu001 ↓↓↓↓↓↓ -->
					<tr class="changePageTR">
						<td>
							<div class="dataPage pageicon">
								<ul>
									<li><a href="javascript:goToPage_first();"><img
											src="<c:url value="/img/icon/first_1.gif" />" alt="" /></a></li>
									<li><a
										href="javascript:goToPage_prev();"><img
											src="<c:url value="/img/icon/prev_1.gif" />" alt="" /></a></li>
									<li><a
										href="javascript:goToPage_next();"><img
											src="<c:url value="/img/icon/next_1.gif" />" alt="" /></a></li>
									<li><a
										href="javascript:goToPage_last();"><img
											src="<c:url value="/img/icon/last_1.gif" />" alt="" /></a></li>
								</ul>
							</div>
							<div class="dataAmount popwin">
								<bean:message key="root.count" />
								<label class="startPage"></label>
								&nbsp;-&nbsp;
								<label class="endPage"></label>
								<bean:message key="root.item" />
								<bean:message key="root.comma" />
								<bean:message key="root.total" />
								&nbsp;
								<label class="totalCount"></label>
								&nbsp;
								<bean:message key="root.item" />
								<bean:message key="root.information" />
								&nbsp;&nbsp;&nbsp;
								<bean:message key="root.jump" />
								<input name="page" id="page" class="jumpPageInput_ajax" type="text"
									size="1" value=""
									onKeyDown="if(event.keyCode==13){goToAssignedPage_ajax()}" />
								<bean:message key="root.page" />
								<input name="GO" type="button" value="GO"
									class="greenStyleButton" onclick="goToAssignedPage_ajax();" />
							</div>
						</td>
					</tr>
				<!-- xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx by gtu001 ↑↑↑↑↑↑ -->
			</table>
			<table id="docList" style="width: 100%; text-align: left;">
				<thead>
					<tr style="">
						<th style="width: 6%; text-align: left;"></th>
						<th style="width: 4%; text-align: left;"></th>
						<th style="width: 10%; text-align: left;">顯示順序</th>
						<th style="width: 18%; text-align: left;">圖檔標題</th>
						<th style="text-align: left;">上架期間</th>
						<th style="width: 10%; text-align: left;">上架狀態</th>
					</tr>
				</thead>
				<tbody>
						<!-- xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx by gtu001 ↓↓↓↓↓↓ -->
						<tr class="emptyData">
							<td colspan="7" align="center"><bean:message
									key="query.emptyResult" /></td>
						</tr>
						<tr class="oddBgColor forCloneTr">
							<td>
								<input name="button32" type="button"
											class="lightGreyStyleButton delTR" value="刪除" />
							</td>
							<td><a class="editTR" href="javascript:;"><img
									src="<c:url value="/img/icon/editing.gif"/>" alt="" width="23"
									height="16" /></a></td>
							<td class="displayOrder"></td>
							<td class="title"><a/></td>
							<td class="endDate"></td>
							<td class="onShelf"></td>
						</tr>
						<!-- xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx by gtu001 ↑↑↑↑↑↑ -->
				</tbody>
			</table>
			<div style="text-align: left; font-size: 12px;">
				<div id="button_align_center">
					<input name="button32" type="button" class="crudOrangeButton"
						value="新圖檔上傳" onclick="showUploadImg('NEW');" /> <input
						name="button33" type="button" class="actionButton" value="排序圖檔"
						onclick="popupSort();" />
				</div>
			</div>
		</div>
	</form>
</div>
