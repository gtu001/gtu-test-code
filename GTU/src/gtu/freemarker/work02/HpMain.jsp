<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="com.ibt.dcs.controller.BaseController"%>
<fmt:setBundle basename="msg" var="msg" />
<!DOCTYPE html>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${jspTitle}</title>
</head>

<body>
	<script type="text/javascript">
		ctrl.insertUrl = '/${taskId}/json/insert.do';
		ctrl.updateUrl = '/${taskId}/json/update.do';
		ctrl.deleteUrl = '/${taskId}/json/delete.do';
	
		ctrl.query = function() {
			var queryParams = $('#tb-fm').serializeArray();
			$('#dg').datagrid('load', {
				<#list list as x>
				${x["COLUMN_NAME"]?lower_case} : queryParams[${x?index}].value,
				</#list>
			});
		}

		$('#dg').datagrid({
			url : 'ptrm0100/json/query.do',
			queryParams : {
				<#list list as x>
				${x["COLUMN_NAME"]?lower_case} : '',
				</#list>
			},
			onLoadSuccess : function(data) {
				if (data.total) {
					$('#dg').datagrid('selectRow', 0);
				}
			},
			onLoadError : function() {
				alert('error');
			},
			onDblClickRow : function() {
				ctrl.view();
			}
		});

		function myformatter(date) {
			var y = date.getFullYear();
			var m = date.getMonth() + 1;
			var d = date.getDate();
			var resultStr = y + '/' + (m < 10 ? ('0' + m) : m) + '/'
					+ (d < 10 ? ('0' + d) : d);
			return resultStr;
		}
		function myparser(s) {
			if (!s) {
				return new Date();
			}
			if (typeof (s) == 'number') {
				return new Date(s);
			}
			var ss = (s.split('/'));
			var y = parseInt(ss[0], 10);
			var m = parseInt(ss[1], 10);
			var d = parseInt(ss[2], 10);
			if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
				return new Date(y, m - 1, d);
			} else {
				return new Date();
			}
		}
		function onSelect(date) {
			//alert(date);
		}
	</script>
	<!-- toolbox start -->
	<div id="tb" style="padding: 5px 10px;">
		<form id="tb-fm" style="margin: 5px 0">
			<#list list as x>
				<label>${x["columnChs"]!}：</label>
				<input name="${x["COLUMN_NAME"]?lower_case}" class="easyui-textbox"
					data-options="width:100" />
			</#list>
			<a href="#" class="easyui-linkbutton"
				data-options="plain:true,iconCls:'icon-search'" onclick="ctrl.query()">查詢</a>
			<a href="#" class="easyui-linkbutton"
				data-options="plain:true,iconCls:'icon-add'" onclick="ctrl.add()"> <fmt:message
					key="btn.add" bundle="${r"${"}msg${r"}"}" />
			</a>
		</form>
	</div>
	<!-- toolbox end -->
	<!-- datagrid sart -->

	<table id="dg" class="easyui-datagrid" title="${jspTitle}"
		data-options="toolbar:'#tb',rownumbers:true,singleSelect:true"
		style="width: 100%; height: 100%">
		<thead>
			<tr>
				<#list list as x>
				<th data-options="field:'${x["COLUMN_NAME"]?lower_case}',halign:'center',width:100">${x["columnChs"]!}</th>
				</#list>
			</tr>
		</thead>
	</table>
	<!-- datagrid end -->
	<!-- dialog start -->
	<div id="dlg-tb">
		<a href="#" class="easyui-linkbutton" iconCls="icon-save" plain="true"
			onclick="ctrl.save();"> <fmt:message key="btn.save" bundle="${r"${"}msg${r"}"}" /> <!-- 儲存 -->
		</a> <a href="#" class="easyui-linkbutton" iconCls="icon-clear"
			plain="true" onclick="ctrl.clear();"> <fmt:message key="btn.clear"
				bundle="${r"${"}msg${r"}"}" /> <!-- 清除 -->
		</a> <a href="#" id="dlg-tb-btn-remove" class="easyui-linkbutton"
			iconCls="icon-remove" plain="true" onclick="ctrl.remove();"> <fmt:message
				key="btn.remove" bundle="${r"${"}msg${r"}"}" /> <!-- 刪除 -->
		</a> <a href="#" class="easyui-linkbutton" iconCls="icon-cancel"
			plain="true" onclick="$('#dlg').dialog('close');"> <fmt:message
				key="btn.close" bundle="${r"${"}msg${r"}"}" /> <!-- 關閉 -->
		</a>
	</div>

	<!-- 新增dialog -->
	<div id="dlg" class="easyui-dialog"
		data-options="toolbar:'#dlg-tb',closed:true,width:690">
		<form id="dlg-fm" method="post" novalidate="">
			<table style="padding: 5px 15px">
				<#list list as x>
				<tr>
					<td><label><span class="col-required">*</span>${x["columnChs"]!}：</label></td>
					<td><input name="${x["COLUMN_NAME"]?lower_case}" class="easyui-textbox"
						data-options="width:100" required /></td>
				</tr>
				</#list>
				<tr>
					<td><label>產生日期︰</label></td>
					<td><input name="mod_time" class="easyui-datebox"
						data-options="formatter:myformatter,parser:myparser,onSelect:onSelect" />
					</td>
				</tr>
				<tr>
					<td><label>產生人員︰</label></td>
					<td><input name="wf_desc" class="easyui-textbox"
						data-options="width:160" /></td>
				</tr>
			</table>
		</form>
	</div>
	<!-- dialog end -->
</body>

</html>
