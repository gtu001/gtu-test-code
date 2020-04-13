<#import "/lib.ftl" as my>  

<#function getColParamArgsLst>
        <#local rtn = "">
        <#local lst = []>
        <#list columnLst as col>
        		<#assign varData = "@{${col?index}}" />
                <#local lst = lst + [ varData ]>
        </#list>
        <#local rtn = my.listJoin(lst, "  ")>
        <#return rtn>
</#function>

<#function getColParamArgsLst2>
        <#local rtn = "">
        <#local lst = []>
        <#list columnLst as col>
        		<#assign varData = "@Param(\"para${col?index}\") String para${col?index} " />
                <#local lst = lst + [ varData ]>
        </#list>
        <#local rtn = my.listJoin(lst, " , ")>
        <#return rtn>
</#function>

<#function getGridName>
        <#local rtn = "">
        <#if grid?? && grid?has_content>
            <#local rtn = grid>
        <#else>
            <#local rtn = "XX_GRIDNAME_XX">
        </#if>
        <#return rtn>
</#function>

<#function getFunObj>
    <#assign myHash = { "pkg": "app", "sqlClass": "DSScanContSql", "sqlMethod" : "DSScanContSql0", "module" : "ind_bpo", "controllerClass" : "DSScanContController" }>
    <#if !funObj??>
        <#assign funObj = {}>
    </#if>
    <#list myHash?keys as key>
        <#if funObj[key]??>
            funObj[key] = myHash[key]
        </#if>
    </#list>
    <#return funObj>
</#function>


////////////////////////////////////////////////////


    function queryClick(){
        init${getGridName()}();

        <#list columnLst as col>
        var ${col} = $("#${col}").val();
        </#list>

        var sqlid1 = "${getFunObj()['sqlMethod']}";
        var mySql1 = new SqlClass();
        mySql1.setResourceName("${getFunObj()['pkg']}.${getFunObj()['sqlClass']}");//指定使用的properties文件名
        mySql1.setModule("${getFunObj()['module']}");
        mySql1.setSqlId(sqlid1);//指定使用的Sql的id
        <#list columnLst as col>
        mySql1.addSubPara(${col});//指定传入的参数
        </#list>
        strSQL = mySql1.getString();
        turnPage.queryModal(mySql1.getString(), ${getGridName()});

        if(${getGridName()}.mulLineCount <= 0){
            i18nAlert("没有符合条件的数据！", "xIDx155819132999813XidX");
            return false;
        }
    }


////////////////////////////////////////////////////

        <Table>
            <TR>
                <TD class=common>
                   <IMG  src= "../common/images/butExpand.gif" style= "cursor:hand;" OnClick= "showPage(this,div${getGridName()});">
                </TD>
                <TD class= titleImg data-i18n="xIDx155818931036374XidX">价格信息</TD>
            </TR>
        </Table>
        <Div  id= "div${getGridName()}" style= "display: ''">
            <table  class= common>
                    <tr  class= common>
                            <td text-align: left colSpan=1>
                                <span id="span${getGridName()}" >
                                </span>
                            </td>
                    </tr>
            </table>
        </div>



///////////////////////////////////////////////////////////////////////


<%@page import="com.sinosoft.lis.pubfun.GlobalInput"%>
<%@page import="com.sinosoft.lis.pubfun.PubFun"%>
<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>


<%
    //添加页面控件的初始化。
    GlobalInput tG1 = new GlobalInput();
    tG1 = (GlobalInput) session.getValue("GI");
%>
<script language="JavaScript">
    $(function() {
        try {
            initInpBox();
            init${getGridName()}();
        } catch (re) {
            alert("InitForm函数中发生异常:初始化界面错误!");
        }
    });

    function initInpBox() {
    }

    function init${getGridName()}() {
        var iArray = new Array();
        try {
            <#list columnLst as col>
            iArray[${col?index}] = new Array();
            iArray[${col?index}][0] = "${col}";//列名
            iArray[${col?index}][1] = "100px";//列宽
            iArray[${col?index}][2] = 10;//列最大值
            iArray[${col?index}][3] = 0;//是否允许输入,1表示允许，0表示不允许
            
            </#list>                

            ${getGridName()} = new MulLineEnter("fm", "${getGridName()}");
            //这些属性必须在loadMulLine前
            ${getGridName()}.mulLineCount = 5;
            ${getGridName()}.displayTitle = 1;
            ${getGridName()}.canChk = 0;
            ${getGridName()}.canSel = 1;
            ${getGridName()}.locked = 1;                //是否锁定：1为锁定 0为不锁定
            ${getGridName()}.hiddenPlus = 1;            //是否隐藏"+"添加一行标志：1为隐藏；0为不隐藏
            ${getGridName()}.hiddenSubtraction = 1; //是否隐藏"-"添加一行标志：1为隐藏；0为不隐藏
            ${getGridName()}.recordNo = 0;          //设置序号起始基数为10，如果要分页显示数据有用
            ${getGridName()}.selBoxEventFuncName = "showSelection";
            ${getGridName()}.loadMulLine(iArray);
        } catch (ex) {
            i18nAlert("{0}函数中发生异常:初始化界面错误!", "xIDx155819100858247XidX", "AgentTrussInit.jsp-->initRollBackGrid");
            alert(ex);
        }
    }


    function showSelection() {
        var arrResult = new Array();
        var tSel = ${getGridName()}.getSelNo();
        if (tSel == 0 || tSel == null) {
            i18nAlert("请先选择一条记录!", "xIDx155819131507920XidX");
            return;
        } else {
            //设置需要返回的数组
            var strSQL = "";
            var codetype = ${getGridName()}.getRowColData(tSel - 1, 1);
            var code = ${getGridName()}.getRowColData(tSel - 1, 2);

            var tResourceName = "${getFunObj()['pkg']}.${getFunObj()['sqlClass']}";
            var sqlid4 = "${getFunObj()['sqlMethod']}";
            strSQL = wrapSql(tResourceName, sqlid4, [ codetype, code ], "${getFunObj()['module']}");

            turnPage.strQueryResult = easyQueryVer3(strSQL, 1, 0, 1);
            if (!turnPage.strQueryResult) { //判断是否查询成功
                i18nAlert("查询失败！", "xIDx155819130418153XidX");
                return false;
            }
            //查询成功则拆分字符串，返回二维数组
            arrResult = decodeEasyQueryResult(turnPage.strQueryResult);
            alert("CODETYPE : " + arrResult[0][0] + "\n"//
                 + "CODE : " + arrResult[0][1] + "\n"//
                 + "CODENAME : " + arrResult[0][2] + "\n"//
                 + "CODEALIAS : " + arrResult[0][3] + "");//
        }
    }

</script>


////////////////////////////////////////////////////


package com.sinosoft.lis.sql.easyquery.${getFunObj()['pkg']};

import com.sinosoft.persistence.Clause;
import com.sinosoft.persistence.EasyQuery;
import com.sinosoft.persistence.Param;
import com.sinosoft.persistence.SQL;
import com.sinosoft.utility.Numeric;
import com.sinosoft.utility.SSRS;

@EasyQuery
public interface ${getFunObj()['sqlClass']} {

    @SQL(value = "Select * from XXXXXXXXX where 1=1   ${getColParamArgsLst()}  ", clauses = {
    <#list columnLst as col>
            <#assign paramX = "#{" + "para${col?index}" + "}" />
            @Clause(key = ${col?index}, clause = "and ${col} = '${paramX}'", expression = "${paramX} != empty"),
    </#list>)
    SSRS ${getFunObj()['sqlMethod']}(${getColParamArgsLst2()});

}



////////////////////////////////////////////////////


package com.sinosoft.lis.controller.${getFunObj()['pkg']};

import com.sinosoft.lis.i18n.I18nMessage;
import com.sinosoft.lis.controller.*;
import com.sinosoft.lis.pubfun.GlobalInput;
import com.sinosoft.lis.schema.LOAccUnitPriceSchema;
import com.sinosoft.service.BusinessDelegate;
import com.sinosoft.utility.CErrors;
import com.sinosoft.utility.TransferData;
import com.sinosoft.utility.VData;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = Service.${getFunObj()['module']})
public class ${getFunObj()['controllerClass']} extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(${getFunObj()['controllerClass']}.class);

    @ResponseBody
    @ApiOperation("保全流程-->查询")
    @PostMapping(value ="/${getFunObj()['pkg']}/${getFunObj()['sqlClass']}/Query/${getFunObj()['sqlMethod']}")
    public String querywithInputSql11(@RequestBody RequestInfo requestInfo, HttpSession session, HttpServletRequest request) {
        return super.query(requestInfo, session, request);
    }
}

////////////////////////////////////////////////////
////////////////////////////////////////////////////
////////////////////////////////////////////////////
////////////////////////////////////////////////////
////////////////////////////////////////////////////
////////////////////////////////////////////////////
////////////////////////////////////////////////////

