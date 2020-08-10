<#import "/lib.ftl" as my>  
<#import "ctbc_dao_maker.ftl" as ct>  

<%@page import="com.sinosoft.lis.pubfun.GlobalInput"%>
<%@page import="com.sinosoft.lis.pubfun.PubFun"%>
<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<%
    //添加页面控件的初始化。
    GlobalInput tG1 = new GlobalInput();
    tG1 = (GlobalInput) session.getValue("GI");
%>
<script language="JavaScript">
	var turnPage = new turnPageClass();
	var dataMap = null;
	var showInfo;

	$(function() {
        try {
            initInpBox();
            init${ct.getGridName()}();
        } catch (re) {
            alert("InitForm函数中发生异常:初始化界面错误!");
        }
    });

    function initInpBox() {
    }

	function init${ct.getGridName()}() {
        var iArray = new Array();
        try {
            <#list columnLst as col>
            iArray[${col?index}] = new Array();
            iArray[${col?index}][0] = "${col}";//列名
            iArray[${col?index}][1] = "100px";//列宽(隱藏為0px)
            iArray[${col?index}][2] = 10;//列最大值
            iArray[${col?index}][3] = 0;//是否允许输入,1表示允许，0表示不允许,3=隱藏
            
            </#list>                

            ${ct.getGridName()} = new MulLineEnter("fm", "${ct.getGridName()}");
            //这些属性必须在loadMulLine前
            ${ct.getGridName()}.mulLineCount = 5;
            ${ct.getGridName()}.displayTitle = 1;
            ${ct.getGridName()}.canChk = 0;
            ${ct.getGridName()}.canSel = 1;
            ${ct.getGridName()}.locked = 1;                //是否锁定：1为锁定 0为不锁定
            ${ct.getGridName()}.hiddenPlus = 1;            //是否隐藏"+"添加一行标志：1为隐藏；0为不隐藏
            ${ct.getGridName()}.hiddenSubtraction = 1; //是否隐藏"-"添加一行标志：1为隐藏；0为不隐藏
            ${ct.getGridName()}.recordNo = 0;          //设置序号起始基数为10，如果要分页显示数据有用
            ${ct.getGridName()}.selBoxEventFuncName = "showSelection";
            ${ct.getGridName()}.loadMulLine(iArray);
        } catch (ex) {
            i18nAlert("{0}函数中发生异常:初始化界面错误!", "xIDx155819100858247XidX", "AgentTrussInit.jsp-->init${ct.getGridName()}");
            alert(ex);
        }
    }

	function showHandleBox(flag) {
		$("#mainbox").hide();
		$("#datahandlebox").hide();
		$("#insertButton").hide();
		$("#updateButton").hide();
		$("#deleteButton").hide();

		if (flag == "Add") {
			clearData();
			$("#datahandlebox").show();
			$("#insertButton").show();
		} else if (flag == "Update") {
			copyDataMapToForm();
			//if(!RowsEvent())
			//{
			//	return false;
			//}
			$("#datahandlebox").show();
			$("#updateButton").show();
		} else if (flag == "Delete") {
			copyDataMapToForm();
			$("#datahandlebox").show();
			$("#deleteButton").show();
		}
	}

	function queryClick(){
        //init${ct.getGridName()}();

        <#list columnLst as col>
        var ${col} = $("#${col}").val();
        </#list>

        var sqlid1 = "${ct.getFunObj()['sqlMethod']}";
        var mySql1 = new SqlClass();
        mySql1.setResourceName("${ct.getFunObj()['pkg']}.${ct.getFunObj()['sqlClass']}");//指定使用的properties文件名
        mySql1.setModule("${ct.getFunObj()['module']}");
        mySql1.setSqlId(sqlid1);//指定使用的Sql的id
        <#list columnLst as col>
        mySql1.addSubPara(${col});//指定传入的参数
        </#list>
        strSQL = mySql1.getString();
        turnPage.queryModal(mySql1.getString(), ${ct.getGridName()});

        if(${ct.getGridName()}.mulLineCount <= 0){
            i18nAlert("没有符合条件的数据！", "xIDx155819132999813XidX");
            return false;
        }
    }

    function querySimple() {
    	simpleGridQuery("${ct.getFunObj()['pkg']}.${ct.getFunObj()['sqlClass']}", "${ct.getFunObj()['sqlMethod']}", ['86110020190410001782'], "${ct.getFunObj()['module']}", ${ct.getGridName()});
    }

	function showSelection() {
        var arrResult = new Array();
        var tSel = ${ct.getGridName()}.getSelNo();
        if (tSel == 0 || tSel == null) {
            i18nAlert("请先选择一条记录!", "xIDx155819131507920XidX");
            return;
        } else {
            //设置需要返回的数组
            var strSQL = "";
            var codetype = ${ct.getGridName()}.getRowColData(tSel - 1, 1);
            var code = ${ct.getGridName()}.getRowColData(tSel - 1, 2);

            debugger
            <#list 1..50 as x>
            var para${x} = ${ct.getGridName()}.getRowColData(tSel - 1, ${x});
            </#list>

            var tResourceName = "${ct.getFunObj()['pkg']}.${ct.getFunObj()['sqlClass']}";
            var sqlid4 = "${ct.getFunObj()['sqlMethod']}";
            strSQL = wrapSql(tResourceName, sqlid4, [ codetype, code ], "${ct.getFunObj()['module']}");

            turnPage.strQueryResult = easyQueryVer3(strSQL, 1, 0, 1);
            if (!turnPage.strQueryResult) { //判断是否查询成功
                i18nAlert("查询失败！", "xIDx155819130418153XidX");
                return false;
            }
            //查询成功则拆分字符串，返回二维数组
            arrResult = decodeEasyQueryResult(turnPage.strQueryResult);
            alert(
                <@ct.showAlert001 /> 
                 );//

            <@ct.makeQueryResultToMap />
        }
    }

    function showSelectionArry() {
    	for(var i=0;i<${ct.getGridName()}.mulLineCount;i++){
			if (${ct.getGridName()}.getChkNo(i)){
				alert("第"+parseInt(i+1)+"行被選中");
			}
		}
		if(${ct.getGridName()}.getSelNo() != "0"){
			alert("選中第"+${ct.getGridName()}.getSelNo()+"行");
		}
    }

	// 提交后操作,服务器数据返回后执行的操作
	function afterSubmit(FlagStr, Content) {
		showInfo.close();
		if (FlagStr == "Succ") {
			showMessagePage("處理成功", Content);
			parent.fraInterface.window.location = "./${ct.getFunObj()['qryJsp']}";
		} else {
			showMessagePage("處理失敗", Content);
		}
	}

	function doSubmitAndAfterSubmit() {
		for(var i=0;i<${ct.getGridName()}.mulLineCount;i++){
			if (${ct.getGridName()}.getChkNo(i)){
	           var para0 = ${ct.getGridName()}.getRowColData(i, 0);
	           //alert(para9);
			}
		}
		if(showInfo != null) {
			showInfo.close();
		}
		showInfo = showNoticeWindow('Warn');
		fm.action = "../API/${ct.getFunObj()['module']}/${ct.getFunObj()['pkg']}/${ct.getFunObj()['controllerClass']}/UPDATE";
		ajaxSubmit(document.getElementById("fm"), function( FlagStr, content ) {
			showInfo = showMessagePage("儲存成功", "更新完成!");
			window.location.reload();
		});
	}

	function cancelClick() {
		$("#mainbox").show();
		$("#datahandlebox").hide();
	}

	function insertClick() {
		if (!beforeSubmit()) {
			return;
		}
		showInfo = showMessagePage("新增數據", "正在保存数据，请您稍候并且不要修改屏幕上的值或链接其他页面");
		fm.action = "../API/${ct.getFunObj()['module']}/${ct.getFunObj()['pkg']}/${ct.getFunObj()['controllerClass']}/ADD";
		ajaxSubmit(document.getElementById("fm"));
	}

	function deleteClick() {
		if (!beforeSubmit()) {
			return;
		}
		showInfo = showMessagePage("新增數據", "正在保存数据，请您稍候并且不要修改屏幕上的值或链接其他页面");
		fm.action = "../API/${ct.getFunObj()['module']}/${ct.getFunObj()['pkg']}/${ct.getFunObj()['controllerClass']}/DELETE";
		ajaxSubmit(document.getElementById("fm"));
	}

	function updateClick() {
		if (!beforeSubmit()) {
			return;
		}
		showInfo = showMessagePage("新增數據", "正在保存数据，请您稍候并且不要修改屏幕上的值或链接其他页面");
		fm.action = "../API/${ct.getFunObj()['module']}/${ct.getFunObj()['pkg']}/${ct.getFunObj()['controllerClass']}/UPDATE";
		ajaxSubmit(document.getElementById("fm"));
	}

	function trimToEmpty(strVal) {
		if (strVal == undefined) {
			return '';
		}
		return String(strVal).trim();
	}

	function copyDataMapToForm() {
		if (dataMap == null) {
			return false;
		}
		var errLst = new Array();
		for ( var k in dataMap) {
			try {
				document.all(k).value = dataMap[k];
			} catch (e) {
				errLst.push(k);
			}
		}
		if (errLst.length != 0) {
			alert("欄位錯誤 : " + errLst);
		}
	}

	// 提交前的校验、计算
	function beforeSubmit() {
		return true;
	}

	function RowsEvent() {
		var checkFlag = ${ct.getGridName()}.getSelNo() - 1;
		if (checkFlag < 0) {
			alert("請選擇要處理的數據");
			return false;
		}

		<#assign idx = 1>
		<#list columnLst as col>
		<#if col?contains('序號')>
			<#continue>
		</#if>
		var ${col} = ${ct.getGridName()}.getRowColData(checkFlag, ${idx});
		<#assign idx = idx + 1>
		</#list>

		<#list columnLst as col>
		<#if col?contains('序號')>
			<#continue>
		</#if>
		$("#${col}").val(${col});
		</#list>
		return true;
	}

	function clearData() {
        <#list columnLst as col>
        document.all('${col}').value = '';
        </#list>
    }

	// 信息同步提示窗
	var showMessagePage = function(title, content, messagePicture, width,
			height) {

		if (messagePicture == "" || messagePicture == null) {
			messagePicture = "C";
		}
		if (width == "" || width == null) {
			width = "550";
		}
		if (height == "" || height == null) {
			height = "250";
		}

		var urlStr = "../common/jsp/MessagePage.jsp?picture=" + messagePicture
				+ "&content=" + encodeURIComponent(content);
		var name = title;
		var iWidth = width;
		var iHeight = height;
		var iTop = (window.screen.availHeight - iHeight) / 2;
		var iLeft = (window.screen.availWidth - iWidth) / 2;
		var tmpShowInfo = window
				.open(
						urlStr,
						name,
						"status=no,toolbar=no,menubar=no,location=no,resizable=no,scrollbars=0,titlebar=no,height="
								+ iHeight
								+ ",width="
								+ iWidth
								+ ",innerHeight="
								+ iHeight
								+ ",innerWidth="
								+ iWidth + ",left=" + iLeft + ",top=" + iTop,
						false);
		tmpShowInfo.focus();
		return tmpShowInfo;
	}




	/*
	* sqlresourcename = "bq.PEdorTypePTInputSql"
	* sqlId = "PEdorTypePTInputSql3"
	* paramArry = [tContNo,tContNo]
	* pkg = "ind_pa"
	*/
	function simpleGridQuery(sqlresourcename, sqlId, paramArry, pkg, DataGrid) {
	    var strSQL = wrapSql(sqlresourcename, sqlId, paramArry, pkg);
	    var arrSelected = new Array();
	    turnPage.strQueryResult  = easyQueryVer3(strSQL, 1, 0, 1);
	
	    //判断是否查询成功
	    if (!turnPage.strQueryResult) {
	        //i18nAlert("没有相应的投保人或被保人信息！", "xIDx15581913594741XidX");
	        return false;
	    }
	    //清空数据容器，两个不同查询共用一个turnPage对象时必须使用，最好加上，容错
	    turnPage.arrDataCacheSet = clearArrayElements(turnPage.arrDataCacheSet);
	    //查询成功则拆分字符串，返回二维数组
	    turnPage.arrDataCacheSet = decodeEasyQueryResult(turnPage.strQueryResult);
	    //设置初始化过的MULTILINE对象，VarGrid为在初始化页中定义的全局变量
	    turnPage.pageDisplayGrid = DataGrid;
	    //保存SQL语句
	    turnPage.strQuerySql = strSQL;
	    //设置查询起始位置
	    turnPage.pageIndex = 0;
	    //在查询结果数组中取出符合页面显示大小设置的数组
	    arrDataSet = turnPage.getData(turnPage.arrDataCacheSet, turnPage.pageIndex, MAXSCREENLINES);
	    console.table(arrDataSet);
	    //调用MULTILINE对象显示查询结果
	    displayMultiline(arrDataSet, turnPage.pageDisplayGrid);
	}


	//setDropdownValue("#EdorType", "#EdorTypeName", "edortype", "PT");
    function setDropdownValue(field, cField, strCodeName, value) {
        value = String(value);
        $(field).val(value);
        var Field = $(field).get(0);
        var strCondition = '3';
        var strConditionField = 'risktype3';
        var arrShowCodeObj = Object();
        var refresh = false;
        var showWidth = 0;
        var changeEven = undefined;
        var tCode = searchCode(strCodeName, strCondition, strConditionField);
        if(!tCode) {
            requestServer(strCodeName, strCondition, strConditionField, showWidth, changeEven);
            document.all("spanCode").style.display ='none';
        }
        var tCode = searchCode(strCodeName, strCondition, strConditionField);
        if(!tCode) {
            console.log(strCodeName + " 取得下拉失敗! : " + value);
            return;
        }
        //console.table(tCode);
        for(var ii = 0 ; ii < tCode.length; ii ++) {
            var arry = tCode[ii];
            if(arry[0] == value) {
                $(cField).val(arry[1]);
                return true;
            }
        }
        $(cField).val("");
        return false;
    }

  	    
    function RowGridHelper(gridId) {
    	var config = {
    		gridId : gridId
    	}
    	var getMaxRowNo = function() {
    		var maxRow = 0;
    		var testInput = $("[name^=" + config.gridId + "]");
	    	var reg = new RegExp(config.gridId + ".*?r(\\d+)");
	    	for(var ii = 0 ; ii < testInput.length; ii ++ ){
	    		var mth = reg.exec($(testInput).eq(ii).attr("id"));
	    		if(mth != null) {
	    			maxRow = Math.max(maxRow, parseFloat(mth[1], 10));
	    		}
	    	}
	    	return maxRow;
    	};
    	var getMappingCellInput = function(rowNo, cellNo) {
    		return $("#" + config.gridId + cellNo + "r" + rowNo);
    	};
    	var getMappingCellInputFromObj = function(refElement, cellNo) {
    		var mth = /.*?r(\d+)/.exec($(refElement).attr("id"));
    		var rowNo = parseFloat(mth[1], 10);
    		return getMappingCellInput(rowNo, cellNo);
    	};
    	var getCheckbox = function(rowNo) {
    		return $("#" + config.gridId + "Chk" + rowNo);
    	};
    	var getSelectedRowNoArry = function() {
    		var arry = new Array();
    		for(var rowNo = 0 ; rowNo <= getMaxRowNo() ; rowNo ++) {
    			if($("#" + config.gridId + "Sel" + rowNo).is(":checked")){
    				arry.push(rowNo);
    			}else if($("#" + config.gridId + "Chk" + rowNo).is(":checked")){
    				arry.push(rowNo);
    			}
    		}
    		return arry;
    	};
    	return {
    		getMaxRowNo : getMaxRowNo,
    		getMappingCellInput : getMappingCellInput,
    		getMappingCellInputFromObj : getMappingCellInputFromObj,
    		getCheckbox : getCheckbox,
    		getSelectedRowNoArry : getSelectedRowNoArry
    	}
    }

    function processAjax() {
	    var postData=new PostData();
	    postData.setModuleName("ind_pa");
	    postData.setTheRestUrl("/bq/PEdorTypeST/");
	    postData.setAction("INIT");
	    postData.put("EdorAcceptNo",document.getElementById("EdorAcceptNo").value);
	    postData.put("ContNo",document.getElementById("ContNo").value);
	    postData.submit(postData, function(flagStr,content,result) {
		    document.all('EdorValiDate').value = result.DefaultEdorValiDate;
		    document.all('PayToDate').value = result.PayToDate;
		});
	}

</script>
