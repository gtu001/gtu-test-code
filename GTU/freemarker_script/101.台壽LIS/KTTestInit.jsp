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
            iArray[${col?index}][1] = "100px";//列宽
            iArray[${col?index}][2] = 10;//列最大值
            iArray[${col?index}][3] = 0;//是否允许输入,1表示允许，0表示不允许
            
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
        init${ct.getGridName()}();

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
</script>
