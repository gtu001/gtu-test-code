<#import "ctbc_dao_maker.ftl" as ct>  


<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<%@include file="../common/jsp/UsrCheck.jsp" %>
<%
    GlobalInput tG11 = new GlobalInput();
    tG11 = (GlobalInput) session.getValue("GI");
    String Operator = tG11.Operator;
    String operatorComCode = tG11.ComCode;
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <SCRIPT src="../common/javascript/Common.js" ></SCRIPT>
    <SCRIPT src="../common/easyQueryVer3/EasyQueryVer3.js"></SCRIPT>
    <SCRIPT src="../common/cvar/CCodeOperate.js"></SCRIPT>
    <SCRIPT src="../common/javascript/MulLine.js"></SCRIPT>
    <SCRIPT src="../common/laydate/laydate.js"></SCRIPT>
    <SCRIPT src="../common/easyQueryVer3/EasyQueryCache.js"></SCRIPT>
    <LINK href="../common/css/Project.css" rel=stylesheet type=text/css>
    <LINK href="../common/css/Project3.css" rel=stylesheet type=text/css>
    <LINK href="../common/css/mulLine.css" rel=stylesheet type=text/css>
    <SCRIPT src="../common/javascript/jquery-1.7.2.js"></SCRIPT>
    
	<script src="../common/javascript/i18n/jquery.i18n.js"></script>
	<script src="../common/javascript/i18n/jquery.i18n.messagestore.js"></script>
	<script src="../common/javascript/i18n/i18n.js"></script>
	
    <SCRIPT src="KTTest.js"></SCRIPT>
    <%@include file="KTTestInit.jsp" %>
    <title>LGGroup KT </title>
</head>
<body>

<form method=post name=fm id="fm" >

    <div id="mainbox">
        <table class="common">

<!-- xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXx -->
			<#list pkColumnLst2 as col>
			<TR class=common>
                <TD class="title5">codetype</TD>
                <TD class="input5">
                    <Input class="common" name="${col}" id="${col}">
                </TD>
            </TR>
			</#list>
<!-- xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXx -->

            <TR class=common>
                <td colspan="4">
                    <a href="javascript:void(0)" class=button onclick="queryClick2();">查 询</a>
                </td>
            </tr>
            
        </table>
        <table>
            <tr>
                <td class="common"><IMG src="../common/images/butExpand.gif" style="cursor:hand;"
                                        OnClick="showPage(this,'datashow_box');"></td>
                <td class=titleImg>數據列表</td>
            </tr>
        </table>
        <table class=common>
            <tr class=common>
                <td style=" text-align: left">
                    <span id="span${ct.getGridName()}"></span>
                </td>
            </tr>
        </table>

<!-- xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXx -->
		<Div id="div${ct.getGridName()}_out" style="display: ''">
			<table>
				<tr>
					<td class=common><IMG src="../common/images/butExpand.gif"
						style="cursor: hand;" OnClick="showPage(this,div${ct.getGridName()});">
					</td>
					<td class=titleImg data-i18n="xIDx155818934613887XidX">數據列表</td>
				</tr>
			</table>
			<Div id="div${ct.getGridName()}" style="display: ''">
				<table class=common>
					<tr class=common>
						<td text-align: left colSpan=1><span id="span${ct.getGridName()}">
						</span></td>
					</tr>
				</table>
			</div>
		</Div>
<!-- xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXx -->


        <table class=common>
            <tr class=common>
                <td style=" text-align: left">
                    <INPUT VALUE="新增" TYPE=button onClick="showHandleBox('Add')" class="cssButton">
                    <INPUT VALUE="修改" TYPE=button onClick="showHandleBox('Update')" class="cssButton">
                    <INPUT VALUE="刪除" TYPE=button onClick="showHandleBox('Delete')" class="cssButton">
                </td>
            </tr>
        </table>

    </div>

<!-- =========================================================================================================================================================================== -->

	<div id="datahandlebox">
        <table>
            <tr>
                <td class="common"><IMG src="../common/images/butExpand.gif" style="cursor:hand;"
                                        OnClick="showPage(this,datachange_box);"></td>
                <td class=titleImg>數據更新</td>
            </tr>
        </table>
        <table class="common">
            <@ct.TR_generate_001 />           
        </table>
        <div id="divCmdButton">
            <INPUT VALUE="增加" TYPE=button id="insertButton" onClick="insertClick()" class="cssButton">
            <INPUT VALUE="更新" TYPE=button id="updateButton" onClick="updateClick()" class="cssButton">
            <INPUT VALUE="删除" TYPE=button id="deleteButton" onClick="deleteClick()" class="cssButton">
            <INPUT VALUE="返回" TYPE=button id="cancelButton" onClick="cancelClick()" class="cssButton">
        </div>
    </div>

 
</form>
<input type="hidden" id="handleFlag" value="">
<span id="spanCode" style="display: none; position:absolute; slategray"></span><br><br><br><br>
</body>
</html>
