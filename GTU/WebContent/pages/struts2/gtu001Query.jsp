<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- <%@ taglib prefix="sj" uri="/struts-jquery-tags" %> --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>

<head>

	<link rel="stylesheet" type="text/css" href="<s:url value="/css/kendo-ui/styles/kendo.default.min.css"/>" />
	<link rel="stylesheet" type="text/css" href="<s:url value="/css/kendo-ui/styles/kendo.common.min.css"/>" />
	<link rel="stylesheet" type="text/css" href="<s:url value="/css/kendo-ui/styles/Default/sprite.png"/>" />
	
	<link rel="stylesheet" href="//code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css">
	<script src="//code.jquery.com/jquery-1.10.2.js"></script>
	<script src="//code.jquery.com/ui/1.11.2/jquery-ui.js"></script>

	<%-- <script type="text/javascript" src="<s:url value="/js/kendo-ui/jquery.min.js"/>"></script>	--%>
	<script type="text/javascript" src="<s:url value="/js/kendo-ui/kendo.web.min.js"/>"></script>	
	
	
<style>
	 input.text { margin-bottom:12px; padding: .4em; }
	 fieldset { padding:0; border:0; margin-top:25px; }
	 #dialog label,#dialog input { display:block; }
	 .ui-dialog .ui-state-error { padding: .3em; }
	 .validateTips { border: 1px solid transparent; padding: 0.3em; }
</style>

<sj:head locale="zh_TW" />
<title><s:text name='xxxxxxxx'>XXXXXXXXXXXXXXA</s:text></title>
<script type="text/javascript">
	var dialogConfig = {
		resizable: false,
		height:440,
		width:400,
		modal: true,
		title: "",
		buttons: {
			"儲存": function() {
				if(!checkData()){
					return;
				}
				save();
				$( this ).dialog( "close" );
			},
			"取消": function() {
				$( this ).dialog( "close" );
			}
		},
		beforeClose: function( event, ui ) {
		}
    }

	$(document).ready(function(){
		$("#btnQuery3, #insertBtn, #btnClear").button();

		var datepickerConfig = {
			showOn: "both",
			buttonImageOnly: true,
			dateFormat: "yymmdd",
			buttonImage: "/APBKIS/images/calendar.gif",
			beforeShow: function(){
			},
			onClose: function(){
			}
		}
		
		$("#declDateStart_").datepicker(datepickerConfig);
		$("#declDateEnd_").datepicker(datepickerConfig);
		
		$("#queryForm").submit(function(event){
			event.preventDefault();
			
			var ds = new kendo.data.DataSource({
	            transport: {
	                read: {
	           			url: "queryJSON_queryMain",
	           			dataType: "json",
	           			type: "POST",
	           		},
                    update: {
                        url: "queryJSON_updateMain",
                        dataType: "json",
                        type: "POST",
                    },
                    destroy: {
                        url: "queryJSON_destroyMain",
                        dataType: "json",
                        type: "POST",
                    },
                    create: {
                        url: "queryJSON_createMain",
                        dataType: "json",
                        type: "POST",
                    },
	           		parameterMap: function (data, type) {
	         			if(type == "read"){
	         				var param = $("#queryForm").serialize();
	         				param += "&page=" + data.page;
	         				param += "&pageSize=" + data.pageSize;
	         				param += "&skip=" + data.skip;
	         				param += "&take=" + data.take;
	         				if(data.sort){
		         				param += "&sort.field=" + data.sort[0]["field"];
		         				param += "&sort.dir=" + data.sort[0]["dir"];
	         				}
	         				return param;
	         			}
	         			if (type !== "read" && data.models) {
                            return { models: kendo.stringify(data.models) };
                        }
	         		}
				},
				batch: true,
	            schema: {
	            	data:"mapLists",
	            	total: function(data) {
	                    //return data.mapLists == null ? 0 : data.mapLists.length;
	                    var dd = data;
	                    return data.totalCount;
	                },
	         	    model: {
	         	    	id : "PK",
	         	    	fields: {
	         	    		PK: { type: "string", },
	         	    		COL1: { type: "string", },
	         	    		COL2: { type: "string", },
	         	    		COL3: { type: "string", },
	         	    		COL4: { type: "string", },
	         	    		COL5: { type: "string", },
	         	    		COL6: { type: "string", },
	         	    	}
	         	    }
	            },
				pageSize: 5,
				serverPaging: true,
                serverFiltering: true,
                serverSorting: true
			});
	
            $("#grid").kendoGrid({
               dataSource: ds,
               columns: [
					{ 
						headerAttributes: {
							style: "text-align: center;"
						},
						attributes: {
							style: "text-align: center;"
						},
						title: "選取",
						template: function(obj) {
							var msg = "<input type=\"button\" value=\"編輯\" onclick=\"doEdit('" + obj.PK + "')\" />";
							msg += "<input type=\"button\" value=\"刪除\" onclick=\"doDelete('" + obj.PK + "')\" />";
							return msg;
						}
					},
                    {
                        field: "PK", title: "PK",
                        headerAttributes: {
							style: "text-align: center;"
						},
						attributes: {
							style: "text-align: center;"
						},
                    },
                    {
                        field: "COL1", title: "COL1",
                        headerAttributes: {
							style: "text-align: center;"
						},
						attributes: {
							style: "text-align: center;"
						},
                    },
                    {
                        field: "COL2", title: "COL2",
                        headerAttributes: {
							style: "text-align: center;"
						},
						attributes: {
							style: "text-align: center;"
						},
                    },
                    { command: ["edit", "destroy"], title: "功能", width: "250px" }
               ],
            	columnMenu: true,
            	reorderable: true,
            	resizable: true,
                sortable: true,
                scrollable: true,
                editable: "inline",
				pageable: {
					input: true,
	                numeric: false,
	                messages: {
	                    display: "總筆數：{2}",
	                    empty: "查無資料",
	                    page: "頁數",
	                    of: " / {0}頁", 
	                    first: "第一頁",
	                    previous: "上一頁",
	                    next: "下一頁",
	                    last: "最後頁",
	                    itemsPerPage: "每頁顯示"
	                },
	                pageSizes: [5, 10, 20, 30]
				}
            });
            ds.fetch(function() {
    			if(ds.total() != null){
    				$("#grid").show();
    			}
    			if(ds._data.length == 0){
    				alert("查無資料!");
    			}
    		});
            $("#grid tr:even").css("background-color", "#FFFACD");
		});
		
		$("#insertBtn").click(function(){
			cleanDialogInput();
			$.extend( dialogConfig, {"title":"新增"} );
			$("#dialog").dialog(dialogConfig);
			$("#dialog_type").val("insert");
			changeReadonly("dialog_pk", false);
		});
	});
	
	function doEdit(pk){
		cleanDialogInput();
		$.extend( dialogConfig, {"title":"修改"} );
		$("#dialog").dialog(dialogConfig);
		$("#dialog_type").val("update");
		changeReadonly("dialog_pk", true);
		$.ajax({
			url:'/APBKIS/gtu001_struts2_test!querySingle',
			data:'PK=' + pk,
			type:'POST',
			dataType:'json',
			async: true,
			success: function(objResult){
				//alert(JSON.stringify(objResult));
				$("#dialog_pk").val(pk);
				$("#dialog_col1").val(objResult.col1);
				$("#dialog_col2").val(objResult.col2);
				$("#dialog_col3").val(objResult.col3);
				$("#dialog_col4").val(objResult.col4);
				$("#dialog_col5").val(objResult.col5);
				$("#dialog_col6").val(objResult.col6);
            },
            error:function(xhr, ajaxOptions, thrownError){
                alert(xhr.status);
                alert(thrownError);
            }
		});
	}
	
	function doDelete(pk){
		if(confirm("是否要刪除:" + pk)){
			$("#dialog_type").val("delete");
			$("#dialog_pk").val(pk);
			save();
		}
	}
	
	function save(){
		$.ajax({
			url:'/APBKIS/gtu001_struts2_test!ajaxSave',
			data:getDialogSerialize(),
			type:'POST',
			dataType:'json',
			async: true,
			success: function(objResult){
				if($("#dialog").dialog("instance") != undefined && //
						$("#dialog").dialog("isOpen")){
					$("#dialog").dialog("close");
				}
				alert(objResult);
				$("#queryForm").submit();
            },
            error:function(xhr, ajaxOptions, thrownError){
                alert(xhr.status);
                alert(thrownError);
            }
		});
	}
	
	function getDialogSerialize(){
		var msg = '';
		$("#dialog *").each(function(){
			if($(this).attr("name") != undefined){
				msg += "&" + $(this).attr("name") + "=" + $(this).val();
			}
		});
		return msg;
	}
	
	function cleanDialogInput(){
		$("#dialog *").each(function(){
			if($(this).attr("name") != undefined){
				$(this).val("");
				$(this).removeClass("ui-state-error");
			}
		});
		$(".validateTips").text("");
	}
	
	function changeReadonly(id, readonly){
		if(readonly){
			$("#" + id).css( "background-color", "#EEEEEE" );
			$("#" + id).attr("readOnly", "readOnly");	
		}else{
			$("#" + id).css( "background-color", "#FFFFFF" );
			$("#" + id).removeAttr("readOnly");
		}
	}
	
	function checkLength( o, label, min, max ) {
		if ( o.val().length > max || o.val().length < min ) {
			o.addClass( "ui-state-error" );
			updateTips( label + " 長度必須介於 " + min + " 與 " + max + " 之間" );
			return false;
		} else {
			return true;
		}
	}
	
	function updateTips( t ) {
		var tips = $( ".validateTips" );
		tips.text( t ).addClass( "ui-state-highlight" );
		setTimeout(function() {
			tips.removeClass( "ui-state-highlight", 1500 );
		}, 500 );
	}
	
	function checkData(){
		var pk = $("#dialog_pk");
		var col1 = $("#dialog_col1");
		var col2 = $("#dialog_col2");
		var col3 = $("#dialog_col3");
		var col4 = $("#dialog_col4");
		var col5 = $("#dialog_col5");
		var col6 = $("#dialog_col6");
		
		var allFields = $( [] ).add( pk ).add( col1 ).add( col2 ).add( col3 ).add( col4 ).add( col5 ).add( col6 );
		
		var valid = true;
		allFields.removeClass( "ui-state-error" );
		valid = valid && checkLength( pk, "PK", 3, 16 );
		valid = valid && checkLength( col1, "COL1", 3, 16 );
		valid = valid && checkLength( col2, "COL2", 3, 16 );
		valid = valid && checkLength( col3, "COL3", 3, 16 );
		valid = valid && checkLength( col4, "COL4", 3, 16 );
		valid = valid && checkLength( col5, "COL5", 3, 16 );
		valid = valid && checkLength( col6, "COL6", 3, 16 );
		
		return valid;
	}
</script>
</head>
<body>	
	
	<s:form id="queryForm" name="queryForm" method="post" action="gtu001_struts2_test!queryMain">
	
	<table width="100%" border="0" cellpadding="0" cellspacing="0" >
		<tr> 
			<td class="PageFunction" ></td>
			<td class="PageFunctionBg">
				<!-- 1.功能名稱 begin -->
				<table width="100%">
		 			<tr>
		 				<td class="PageFunctionIcon"></td>
		  				<td class="PageFunctionTx"><s:text name='xxxxxxxx'>XXXXXXXXXXXXB</s:text></td>
					</tr>
				</table>
				<!-- 1.功能名稱 end -->
			</td>
			<td class="PageFunctionFt"></td>
		</tr>
	</table>
	
	<font color="red">
		<s:include value="/pages/msg.jsp"></s:include>
	</font>
	<div class="dataBody" >
		<!-- 2.查詢區塊 begin -->
		<table width="100%">	
			<tr class="TableTr">
				<td class="TableTdLabel"  width="10%" id="tdEdiType01"><s:text name="xxxxxxxx">XXXXX0</s:text></td>
				<td class="TableTdInput" id="tdEdiType02">
					<s:textfield id="declDateStart_" name="declDateStart_" cssClass="text" />
					~
					<s:textfield id="declDateEnd_" name="declDateEnd_" cssClass="text" />
				</td>
			</tr>	
			<tr class="TableTr" >
				<td class=TableTdLabel><s:text name="xxxxxxxx">PK</s:text></td>
				<td class=TableTdInput>
					<s:textfield id="pkStart" name="pkStart" maxlength="14" size="16" onblur="this.value=this.value.toUpperCase();" cssClass="text" />－
					<s:textfield id="pkEnd" name="pkEnd" maxlength="14" size="16" onblur="this.value=this.value.toUpperCase();" cssClass="text" />
				</td>
			</tr>
			<tr class="actionRow" align="center">
				 <td colspan="2">
				 	<s:submit id="btnQuery3" key="button.query" />
				 	<!-- <input id="btnQuery2" type="button" value="test" />-->
				 	<input id="insertBtn" type="button" value="新增" />
				 	<input id="btnClear" type="reset" class="actionButton" value="<s:text name="button.clear">清除</s:text>" />
	            </td>
			</tr>
		</table>
		<!-- 2.查詢區塊 end -->		
		</div>
		
		<div id="grid"></div>
		
		<div id="dialog" style="display:none" title="">
			<p class="validateTips"></p>
			<fieldset>
				<label><s:text name="xxxxxxxx">PK</s:text></label>
				<s:textfield id="dialog_pk" name="dialog_pk" cssClass="text" />
				<label><s:text name="xxxxxxxx">COL1</s:text></label>
				<s:textfield id="dialog_col1" name="dialog_col1" cssClass="text" />
				<label><s:text name="xxxxxxxx">COL2</s:text></label>
				<s:textfield id="dialog_col2" name="dialog_col2" cssClass="text" />
				<label><s:text name="xxxxxxxx">COL3</s:text></label>
				<s:textfield id="dialog_col3" name="dialog_col3" cssClass="text" />
				<label><s:text name="xxxxxxxx">COL4</s:text></label>
				<s:textfield id="dialog_col4" name="dialog_col4" cssClass="text" />
				<label><s:text name="xxxxxxxx">COL5</s:text></label>
				<s:textfield id="dialog_col5" name="dialog_col5" cssClass="text" />
				<label><s:text name="xxxxxxxx">COL6</s:text></label>
				<s:textfield id="dialog_col6" name="dialog_col6" cssClass="text" />
			</fieldset>
			<s:hidden id="dialog_type" name="dialog_type" />
		</div>
	</s:form>
</body>
</html>
