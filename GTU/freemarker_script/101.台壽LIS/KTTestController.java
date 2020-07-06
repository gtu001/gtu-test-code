<#import "/lib.ftl" as my>  
<#import "ctbc_dao_maker.ftl" as ct>  


package com.sinosoft.lis.controller.${ct.getFunObj()['pkg']};

import com.sinosoft.lis.controller.*;
import com.sinosoft.lis.i18n.I18nMessage;
import com.sinosoft.lis.${ct.getFunObj()['pkg']}.${ct.getFunObj()['uiClass']};
import com.sinosoft.lis.pubfun.GlobalInput;
import com.sinosoft.lis.pubfun.PubFun;
import com.sinosoft.lis.schema.${ct.getBlObj()['table']}Schema;
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
@RequestMapping(value = Service.IND_NB)
public class ${ct.getFunObj()['controllerClass']} extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(${ct.getFunObj()['controllerClass']}.class);

    private ${ct.getBlObj()['table']}Schema getSchema(RequestInfo request) {
        <#list columnLst as col>
        String ${col} = request.getParameter("${col}");
        </#list>

        <#list columnLst as col>
        logger.info("${col}:" + ${col});
        </#list>

        OLDMAKEDATE = PubFun.getCurrentDate();
        OLDMAKETIME = PubFun.getCurrentTime();

        ${ct.getBlObj()['table']}Schema schema = new ${ct.getBlObj()['table']}Schema();
        <#list columnLst as col>
        <#assign javaParam = columnLst2[col?index] />
        schema.set${javaParam?cap_first}(${col});
        </#list>
        return schema;
    }

    private ResponseInfo common_process(@RequestBody RequestInfo request, HttpSession session, String operation) {
        ResponseInfo tResponseInfo = new ResponseInfo();
        String FlagStr = "";
        try {
            logger.info("參數清單 parameters ↓↓↓↓↓↓ ========================================================================");
            for(Enumeration enu = request.getParameterNames(); enu.hasMoreElements(); ) {
                String key = (String)enu.nextElement();
                String[] values = request.getParameterValues(key);
                logger.info("\t" + key + "\tvalue : " + Arrays.asList(values));
            }
            logger.info("參數清單 parameters ↑↑↑↑↑↑ ========================================================================");


            // 封装返回值
            ResponseInfo response = new ResponseInfo();
            String Content = "";

            GlobalInput tG = new GlobalInput();
            tG = (GlobalInput) session.getValue("GI");

            VData tVData = new VData();
            // 工作流操作型别，根据此值检索活动ID，取出服务类执行具体业务逻辑
            // wFlag = "0000001404";

            tVData.add(this.getSchema(request));
            tVData.add(tG);

            // BusinessDelegate tBusinessDelegate =
            // BusinessDelegate.getBusinessDelegate();
            // if (!tBusinessDelegate.submitData(tVData, operation, "KTTestBL"))
            // tBusinessDelegate.getCErrors().getError(0).errorMessage()
            // {

            ${ct.getFunObj()['uiClass']} t${ct.getFunObj()['uiClass']} = new ${ct.getFunObj()['uiClass']}();

            t${ct.getFunObj()['uiClass']}.submitData(tVData, operation);

            tError = ${ct.getFunObj()['uiClass']}.mErrors;
            //tError = tBusinessDelegate.getCErrors();
            if (!tError.needDealError())
                Content =new I18nMessage("保存成功。","tai156646161419992shou").getMessage();
                FlagStr = "Succ";
                responseInfo.succ();
                responseInfo.setContent(Content);
            } else {
                Content = new I18nMessage("保存失败，原因是:","tai156646160833412shou").getMessage() + tError.getFirstError();
                FlagStr = "Fail";
                responseInfo.fail();
                responseInfo.setContent(Content);
            }
            VData result = t${ct.getFunObj()['uiClass']}.getResult();
            logger.debug("UnionConfirmSave", "-------------------end workflow---------------------");
            if (response != null) {
                return response;
            }
        } catch (Exception e) {
            e.printStackTrace();
            tResponseInfo.fail();
            tResponseInfo.setContent("程序發生異常：" + e.getMessage());
        }
        return tResponseInfo;
    }

    @Override
    @ApiOperation("KTTEST->保存")
    @PostMapping(value = "/${ct.getFunObj()['pkg']}/${ct.getFunObj()['controllerClass']}/" + Action.ADD_ACTION)
    public ResponseInfo add(@RequestBody RequestInfo request, HttpSession session) {
        return common_process(request, session, "insert");
    }

    @Override
    @ApiOperation("KTTEST->保存")
    @PostMapping(value = "/${ct.getFunObj()['pkg']}/${ct.getFunObj()['controllerClass']}/" + Action.UPDATE_ACTION)
    public ResponseInfo update(@RequestBody RequestInfo request, HttpSession session) {
        return common_process(request, session, "update");
    }

    @Override
    @ApiOperation("KTTEST->保存")
    @PostMapping(value = "/${ct.getFunObj()['pkg']}/${ct.getFunObj()['controllerClass']}/" + Action.DELETE_ACTION)
    public ResponseInfo delete(@RequestBody RequestInfo request, HttpSession session) {
        return common_process(request, session, "delete");
    }

    @ResponseBody
    @ApiOperation("KTTEST-->查询")
    @PostMapping(value = "/${ct.getFunObj()['pkg']}/${ct.getFunObj()['sqlClass']}/Query/${ct.getFunObj()['sqlMethod']}")
    public String query(@RequestBody RequestInfo requestInfo, HttpSession session, HttpServletRequest request) {
        return super.query(requestInfo, session, request);
    }

    @ResponseBody
    @ApiOperation("KTTEST-->查询2")
    @PostMapping(value = "/${ct.getFunObj()['pkg']}/${ct.getFunObj()['sqlClass']}/Query/${ct.getFunObj()['sqlMethod']}")
    public String query2(@RequestBody RequestInfo requestInfo, HttpSession session, HttpServletRequest request) {
        return super.query(requestInfo, session, request);
    }
}
