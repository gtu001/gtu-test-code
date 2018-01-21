<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script src="${pageContext.request.contextPath}/js/jquery-2.1.1.js"></script><!-- jquery-2.1.1.js -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery-ui.css" />

<!-- jqGrid -->
<script src="${pageContext.request.contextPath}/js/jquery.jqGrid.src.js"></script>
<script src="${pageContext.request.contextPath}/js/grid.locale-tw.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/ui.jqgrid.css" />


<script type="text/javascript">
	$(document).ready(function(){
		$("#jqGrid").jqGrid({
			url: '/FoodMenu/JqGridServlet',
			datatype: "json",
			 colModel: [
			 	{
			 		label:'Sn',
			 		width:10,
			 		formatter: formatSn
			 	},
				{ label: 'Category Name', name: 'CategoryName', width: 75 },
				{ label: 'Product Name', name: 'ProductName', width: 90 },
				{ label: 'Country', name: 'Country', width: 100 },
				{ label: 'Price', name: 'Price', width: 80, sorttype: 'integer' },
				// sorttype is used only if the data is loaded locally or loadonce is set to true
				{ label: 'Quantity', name: 'Quantity', width: 80, sorttype: 'number' },
                {
                	label: 'Function',
                    name: 'Quantity',
                    width: 50,
                    formatter: formatFunc
                }       
			],
			viewrecords: true, // show the current page, data rang and total records on the toolbar
			width: 780,
			height: 200,
			rowNum: 30,
			loadonce: true, // this is just for the demo
			pager: "#jqGridPager"
		});
	});
	function formatSn(cellValue, options, rowObject){
		return options['rowId'];
	}
	function formatFunc(cellValue, options, rowObject){
		var template = "<input type='button' value='修改' onclick='alert(\""+ rowObject['Country'] +"\")' />";
		return template;
	}
</script>
<title>Insert title here</title>
</head>
<body>
	<form action="/FoodMenu/OrderServlet" method="post">
		點菜查詢
		<table border="1">
			<tr>
				<td>類型</td>
				<td><select name="foodType">
						<option value="">全部</option>
						<option value="1">沙拉</option>
						<option value="2">湯</option>
						<option value="3">主菜</option>
				</select></td>
			</tr>
			<tr>
				<td>價格</td>
				<td><input name="priceStart" type="text" />~<input
					name="priceEnd" type="text" /></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" value="查詢" /></td>
			</tr>
		</table>
		
		<table id="jqGrid"></table>
    	<div id="jqGridPager"></div>
    	
	</form>
</body>
</html>