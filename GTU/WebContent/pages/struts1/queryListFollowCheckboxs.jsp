<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/head.jsp"%>

<script language="javascript">
	function save() {
		checkBoxFilter();
		jQuery("#cmd").val("editOnsale");
		jQuery("#forward").val("initPage");
		jQuery("#firstTime").val("");
		jQuery("#resultForm").submit();
	}
	function back(){
		jQuery("#cmd").val("init");
		jQuery("#forward").val("initPage");
		jQuery("#funName").val("");
		jQuery("#firstTime").val("YES");
		jQuery("#resultForm").submit();
	}
	jQuery(document).ready(function(){
		jQuery("input[id^='exposure_']").bind("click", function(){
			var chk = jQuery(this);
			var num = /[a-z]+_(\d+)/.exec(chk.attr("id"))[1];
			if(!chk.is(":checked")){
				jQuery("#top_"+num).removeAttr("checked");
			}
		});
		jQuery("input[id^='top_']").bind("click", function(){
			var chk = jQuery(this);
			var num = /[a-z]+_(\d+)/.exec(chk.attr("id"))[1];
			if(chk.is(":checked")){
				jQuery("#exposure_"+num).attr("checked", "checked");
			}
		});
		var debugMode = '<%=request.getParameter("debug")%>' != 'null';
		if(debugMode){
			jQuery("label[id^=titleLabel_]").hover(function(){
				var activityId = /titleLabel_(\d+)/.exec(jQuery(this).attr("id"))[1];
				jQuery("#hintDiv_" + activityId).show();
			},function(){
				var activityId = /titleLabel_(\d+)/.exec(jQuery(this).attr("id"))[1];
				jQuery("#hintDiv_" + activityId).hide();
			});
			jQuery("#label_coType").hover(function(){
				jQuery("#hint_coType").show();
			}, function(){
				jQuery("#hint_coType").hide();
			});
		}
	});
	
	function checkBoxFilter(){
		jQuery("#firstTime").val("");
		var exposure = '';
		var top = '';
		var all = '';
		jQuery("input[id^='exposure_']").each(function(){
			var chk = jQuery(this);
			var num = /[a-z]+_(\d+)/.exec(chk.attr("id"))[1];
			all += num + "^";
			if(chk.is(":checked")){
				exposure += num + "^";
			}
		});
		jQuery("input[id^='top_']").each(function(){
			var chk = jQuery(this);
			var num = /[a-z]+_(\d+)/.exec(chk.attr("id"))[1];
			if(chk.is(":checked")){
				top += num + "^";
			}
		});
		jQuery("#actionMode").val("exposure="+exposure+",top="+top+",all="+all);
	}
	
	//固定轉頁method ↓↓↓↓↓↓
	function goToPage(page) {
		checkBoxFilter();
		jQuery("#forward").val("exposure");
		document.getElementById("resultForm").pageNo.value = page;
		document.getElementById("resultForm").cmd.value="executeQuery";   
		document.getElementById("resultForm").submit();
	}
	function goToAssignedPage() {
		checkBoxFilter();
		jQuery("#forward").val("exposure");
		var pageNoValue = document.getElementById("page").value;
		if (isNaN(pageNoValue)) {
			pageNoValue = 1;
		}
		if (parseInt(pageNoValue, 10) > parseInt('${pageQuery.totalPageCount}', 10)) {
			pageNoValue = '${pageQuery.totalPageCount}';
		} else if (parseInt(pageNoValue, 10) < 1) {
			pageNoValue = 1;
		}
		document.getElementById("resultForm").pageNo.value = pageNoValue;
		document.getElementById("resultForm").cmd.value="executeQuery";   
		document.getElementById("resultForm").submit();
	}
	
	//固定轉頁method ↑↑↑↑↑↑
</script>
<SCRIPT LANGUAGE="JavaScript">
	document.write(getCalendarStyles());
</SCRIPT>
<SCRIPT LANGUAGE="JavaScript" ID="jscal1xx">
	var cal1xx = new CalendarPopup("testdiv1");
	cal1xx.showNavigationDropdowns();
</SCRIPT>
<!--內容區域 -->
<div class="dataGridContainer">
	<div class="header">
		<div class="title">小網區塊活動露出設定</div>
	</div>
	<hr class="hrline" />

	<!--Datagrid資料列表記錄 -->
	<form id="resultForm" name="resultForm" method="post"
		action="<c:url value="/admin/marketing/mobilePortalManagement.do" />">
		<input type="hidden" name="forward" id="forward" value="" />
		<input type="hidden" name="funName" id="funName" value="${dyForm['funName']}" />
		<input type="hidden" name="cmd" id="cmd" value="" />
		<input type="hidden" name="pageNo" id="pageNo" value="${pageNo}" />
		<input type="hidden" name="actCategoryId" id="actCategoryId" value="${actCategoryId}" />
		<input type="hidden" name="firstTime" id="firstTime" value="" />
		<textarea id="actionMode" name="actionMode" style="display:none"></textarea>

		<div class="dataBody" style="width: 98%; margin: 0 auto;">
			<table id="docList" style="width: 100%; text-align: left;">
				<tr class="">
					<td width="20%"><label class="fieldCaption">區塊名稱：</label></td>
					<td colspan="10">
						<label id="label_coType">${coTypeMap[dyForm['funName']]}</label>
						<span id="hint_coType" style="display:none">
							<font color="red">${dyForm['funName']}</font>
						</span>
					</td>
				</tr>
				<tr class="">
					<td><label class="fieldCaption">小網首頁區塊標題：</label></td>
					<td colspan="10">${title}</td>
				</tr>
				<tr class="">
					<td colspan="10" width="100%">
						<div class="dataPage pageicon">
							<ul>
								<li><a href="javascript:goToPage('1');"><img src="<c:url value="/img/icon/first_1.gif" />" alt="" /></a></li>
								<li><a href="javascript:goToPage('${pageQuery.previousPageNo}');"><img src="<c:url value="/img/icon/prev_1.gif" />" alt="${pageQuery.previousPageNo}" /></a></li>
								<li><a href="javascript:goToPage('${pageQuery.nextPageNo}');"><img src="<c:url value="/img/icon/next_1.gif" />" alt="${pageQuery.nextPageNo}" /></a></li>
								<li><a href="javascript:goToPage('${pageQuery.totalPageCount}');"><img src="<c:url value="/img/icon/last_1.gif" />" alt="${pageQuery.totalPageCount}" /></a></li>
							</ul>
						</div>
						<div class="dataAmount popwin">
							<bean:message key="root.count" />
							<c:out value="${pageQuery.currentIndex + 1}" />&nbsp;-&nbsp;
							<c:choose>
								<c:when	test="${(pageQuery.currentPageNo * pageQuery.pageSize) > pageQuery.totalCount}">
									<c:out value="${pageQuery.totalCount}" />
								</c:when>
								<c:otherwise>
									<c:out value="${pageQuery.currentPageNo * pageQuery.pageSize}" />
								</c:otherwise>
							</c:choose>
							<bean:message key="root.item" />
							<bean:message key="root.comma" />
							<bean:message key="root.total" />&nbsp;
								<c:out value="${pageQuery.totalCount}" />&nbsp;
							<bean:message key="root.item" />
							<bean:message key="root.information" />&nbsp;&nbsp;&nbsp;
							<bean:message key="root.jump" />
							<input name="page" id="page" class="jumpPageInput" type="text" size="1" value="" onKeyDown="if(event.keyCode==13){goToAssignedPage()}" />
							<bean:message key="root.page" />
							<input name="GO" type="button" value="GO" class="greenStyleButton" onclick="goToAssignedPage();" />
						</div>
					</td>
				</tr>
			</table>
			<table>
				<thead>
					<tr style="">
	                    <th style="width:50%;text-align:left;">活動標題</th>
	                    <th style="width:30%;text-align:left;">活動期間</th>
	                    <th style="width:10%;text-align:left;">露出</th>
	                    <th style="width:10%;text-align:left;">置頂</th>
					</tr>
				</thead>
				<tbody>
					<c:if test="${empty pageQuery.result}">
						<tr>
							<td colspan="7" align="center"><bean:message key="query.emptyResult" /></td>
						</tr>
					</c:if>
					<c:forEach var="item" items="${pageQuery.result}" varStatus="status">
					<tr class="${status.index % 2 == 0 ? '' : 'oddBgColor'}">
						<td>
							<label id="titleLabel_${item.activityId}">${item.title}</label>
							<span id="hintDiv_${item.activityId}" style="display:none">
								<font color="red">${item.activityId}</font>
							</span>
						</td>
						<td>
							${item.startDate} - ${item.endDate}
						</td>
						<td>
							<input id="exposure_${item.activityId}" type="checkbox" ${item.exposure?'checked':''} value="${item.title}" />
						</td>
						<td>
							<input id="top_${item.activityId}" type="checkbox" ${item.top?'checked':''} value="${item.title}" />
						</td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
			<div style="text-align: left; font-size: 12px;">
				<div id="button_align_center">
					<input name="button32" type="button" class="crudOrangeButton"
						value="儲存" onclick="save();" />
					<input name="button32" type="button" class="crudOrangeButton" 
						value="取消" onclick="back();" />
				</div>
			</div>
		</div>
	</form>
</div>
<!--Datagrid資料列表記錄結束 -->
<DIV ID="testdiv1"
	STYLE="position: absolute; visibility: hidden; background-color: white; layer-background-color: white;"></DIV>
<logic:messagesPresent message="true">
	<script language="javascript">
		alert("<html:messages id='messages' message='true'><bean:write name='messages'/>\n</html:messages>");
	</script>
</logic:messagesPresent>