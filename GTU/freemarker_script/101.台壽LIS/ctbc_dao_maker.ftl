<#import "/lib.ftl" as my>  

<#function getFunObj>
    <#assign myHash = { "pkg": "app", 
                        "sqlClass": "DSScanContSql", 
                        "sqlMethod" : "DSScanContSql0", 
                        "module" : "ind_bpo", 
                        "controllerClass" : "DSScanContController", 
                        "uiClass" : "DSSestUI" 
                        }>
    <#if !funObj??>
        <#assign funObj = {}>
    </#if>
    <#list myHash?keys as key>
        <#if ! funObj[key]??>
            <#assign funObj = funObj + {key: myHash[key]} />
        </#if>
    </#list>
    <#return funObj>
</#function>

<#function getBlObj>
    <#assign myHash = {"blClass", "BlTestTableBL", 
                       "table":"BlTestTable"
                       }>
    <#if !blObj??>
        <#assign blObj = {}>
    </#if>
    <#list myHash?keys as key>
        <#if ! blObj[key]??>
            <#assign blObj = blObj + {key: myHash[key]} />
        </#if>
    </#list>
    <#return blObj>
</#function>


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

<#function getColParamArgsLst3>
        <#local rtn = "">
        <#local lst = []>
        <#list columnLst as col>
                <#local lst = lst + [ col ]>
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


<#macro showAlert001>
    <#local hasSerial = "N">
    <#list columnLst as col>
        <#if col?is_first && col?contains("序號")>
            <#local hasSerial = "Y">
        </#if>
        <#local currentIndex = col?index>
        <#if hasSerial == "Y">
            <#local currentIndex = currentIndex - 1>
        </#if>
        <#if col?is_first && hasSerial =="Y">
            "${col} : " + "na" + "\n" + //
        <#elseif col?is_last>
            "${col} : " + arrResult[0][${currentIndex}] + "\n"//
        <#else>
            "${col} : " + arrResult[0][${currentIndex}] + "\n" + //
        </#if>
    </#list>
</#macro>

<#macro makeQueryResultToMap>
    dataMap = {};
    <#local hasSerial = "N">
    <#list columnLst as col>
        <#if col?is_first && col?contains("序號")>
            <#local hasSerial = "Y">
        </#if>
        <#local currentIndex = col?index>
        <#if hasSerial == "Y">
            <#local currentIndex = currentIndex - 1>
        </#if>
        <#if col?is_first && hasSerial =="Y">
        <#else>
            dataMap['${col}'] = trimToEmpty(arrResult[0][${currentIndex}]);
        </#if>
    </#list>
</#macro>

<#macro TR_generate_001>
    <#local needSkip = "N">
    <#list columnLst as col>
        <#if col?contains("序號")>
            <#continue>
        </#if>
        <#if needSkip == "Y">
            <#local needSkip = "N">
            <#continue>
        </#if>
        <TR class=common>
            <TD class="title5">${col}</TD>
            <TD class="input5">
                <Input class="common" name="${col}" id="${col}">
            </TD>
            <#if col?index < columnLst?size-1>
                <#local colNext = columnLst[col?index + 1]>
                <#local needSkip = "Y">
            <#else>
                <#local colNext = "">
                <#local needSkip = "N">
            </#if>
            <TD class="title5">${colNext}</TD>
            <TD class="input5">
                <Input class="common" name="${colNext}" id="${colNext}">
            </TD>
        </tr>
    </#list>
</#macro>

<#if ! columnLst2??>
    <#assign columnLst2 = columnLst />
</#if>

<#if ! pkColumnLst2??>
    <#assign pkColumnLst2 = ['AAAA', 'BBBB'] />
</#if>

<#function getPkWhereCondition>
    <#local rtn = "">
    <#local lst = []>
    <#list pkColumnLst2 as col>
        <#local varData = " " + col + " = '\" + " + col + " + \"' "  />
        <#local lst = lst + [ varData ]>
    </#list>
    <#local rtn = my.listJoin(lst, " and ")>
    <#return rtn>
</#function>

<#function getPkWhereCondition2>
    <#local rtn = "">
    <#local lst = []>
    <#list pkColumnLst2 as col>
        <#local varData = " " + col + " = '?" + col + "?'"  />
        <#local lst = lst + [ varData ]>
    </#list>
    <#local rtn = my.listJoin(lst, " and ")>
    <#return rtn>
</#function>

<#function getPkArgs>
    <#local rtn = "">
    <#local lst = []>
    <#list pkColumnLst2 as col>
        <#local varData = "String " + col + ""  />
        <#local lst = lst + [ varData ]>
    </#list>
    <#local rtn = my.listJoin(lst, ", ")>
    <#return rtn>
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
            alert(
                <@showAlert001 /> 
                 );//

            <@makeQueryResultToMap />
        }
    }

    function clearForm() {
        <#list columnLst as col>
        document.all('${col}').value = '';
        </#list>
    }

    function trimToEmpty(strVal) {
        if (strVal == undefined) {
            return '';
        }
        return String(strVal).trim();
    }

    function copyDataMapToForm() {
        if(dataMap == null) {
            return false;
        }
        var errLst = new Array();
        for(var k in dataMap) {
            try{
                document.all(k).value = dataMap[k];
            }catch(e) {
                errLst.push(k);
            }
        }
        if(errLst.length != 0) {
            alert("欄位錯誤 : " + errLst);
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

    @SQL(value = "Select ${getColParamArgsLst3()} from XXXXXXXXX where 1=1   ${getColParamArgsLst()}  ", clauses = {
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

            <#list columnLst as col>
            String ${col} = request.getParameter("${col}");
            </#list>
            <#list columnLst as col>
            logger.info("${col}:" + ${col});
            </#list>
            
            <#list columnLst as col>
            mTransferData.setNameAndValue("${col}", ${col});
            </#list>


////////////////////////////////////////////////////


    <div id="datahandlebox">
        <table>
            <tr>
                <td class="common"><IMG src="../common/images/butExpand.gif" style="cursor:hand;"
                                        OnClick="showPage(this,datachange_box);"></td>
                <td class=titleImg>數據更新</td>
            </tr>
        </table>
        <table class="common">
            <@TR_generate_001 />           
        </table>
        <div id="divCmdButton">
            <INPUT VALUE="增加" TYPE=button id="insertButton" onClick="insertClick()" class="cssButton">
            <INPUT VALUE="更新" TYPE=button id="updateButton" onClick="updateClick()" class="cssButton">
            <INPUT VALUE="删除" TYPE=button id="deleteButton" onClick="deleteClick()" class="cssButton">
            <INPUT VALUE="返回" TYPE=button id="cancelButton" onClick="cancelClick()" class="cssButton">
        </div>
    </div>


////////////////////////////////////////////////////

    private LCRollBackLogSchema getVO(TransferData tData) {
        <#list columnLst as col>
            String ${col} = (String) tData.getValueByName("${col}");
        </#list>

        OLDMAKEDATE = PubFun.getCurrentDate();
        OLDMAKETIME = PubFun.getCurrentTime();
        MAKEDATE = PubFun.getCurrentDate();
        MAKETIME = PubFun.getCurrentTime();

        LCRollBackLogSchema schema = new LCRollBackLogSchema();
        <#list columnLst as col>
            schema.set${col}(${col});
        </#list>

        logger.debug("## schema : " + ReflectionToStringBuilder.toString(schema, ToStringStyle.MULTI_LINE_STYLE));
        return schema;
    }

////////////////////////////////////////////////////
////////////////////////////////////////////////////
////////////////////////////////////////////////////
////////////////////////////////////////////////////
////////////////////////////////////////////////////
////////////////////////////////////////////////////
////////////////////////////////////////////////////
////////////////////////////////////////////////////
////////////////////////////////////////////////////
////////////////////////////////////////////////////
