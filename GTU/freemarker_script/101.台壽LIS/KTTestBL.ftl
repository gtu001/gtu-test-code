<#import "/lib.ftl" as my>  

<#function getBlObj>
    <#assign myHash = {"blClass", "BlTestTableBL", "table":"BlTestTable"}>
    <#if !blObj??>
        <#assign blObj = {}>
    </#if>
    <#list myHash?keys as key>
        <#if blObj[key]??>
            blObj[key] = myHash[key]
        </#if>
    </#list>
    <#return blObj>
</#function>


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


////////////////////////////////////////////////////


package com.sinosoft.lis.controller.kttest;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sinosoft.lis.acc.PubInsuAccFun;
import com.sinosoft.lis.db.LCGrpIvstPlanDB;
import com.sinosoft.lis.db.LCInsureAccDB;
import com.sinosoft.lis.db.LCInsureAccTraceDB;
import com.sinosoft.lis.db.LCPerInvestPlanDB;
import com.sinosoft.lis.db.LCPolDB;
import com.sinosoft.lis.db.LCPremToAccDB;
import com.sinosoft.lis.db.${getBlObj()['table']}DB;
import com.sinosoft.lis.i18n.I18nMessage;
import com.sinosoft.lis.pubfun.GlobalInput;
import com.sinosoft.lis.pubfun.MMap;
import com.sinosoft.lis.pubfun.PubFun;
import com.sinosoft.lis.pubfun.PubFun1;
import com.sinosoft.lis.pubfun.PubSubmit;
import com.sinosoft.lis.schema.LCInsureAccTraceSchema;
import com.sinosoft.lis.schema.LCPolSchema;
import com.sinosoft.lis.schema.LCPremToAccSchema;
import com.sinosoft.lis.schema.${getBlObj()['table']}Schema;
import com.sinosoft.lis.vdb.${getBlObj()['table']}DBSet;
import com.sinosoft.lis.vschema.LCInsureAccSet;
import com.sinosoft.lis.vschema.LCInsureAccTraceSet;
import com.sinosoft.lis.vschema.LCPremToAccSet;
import com.sinosoft.lis.vschema.${getBlObj()['table']}Set;
import com.sinosoft.service.BusinessService;
import com.sinosoft.service.stereotype.Service;
import com.sinosoft.utility.CError;
import com.sinosoft.utility.CErrors;
import com.sinosoft.utility.ExeSQL;
import com.sinosoft.utility.SQLwithBindVariables;
import com.sinosoft.utility.TransferData;
import com.sinosoft.utility.VData;

/**
 * <p>
 * Title:
 * </p>
 *
 * <p>
 * Description:利息账户转投资账户批处理
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2019
 * </p>
 *
 * <p>
 * Company:sinosoft
 * </p>
 *
 * @author:ck
 * @version 1.0
 * @Cleaned QianLy 2009-07-16
 */

@Service(name = "${getBlObj()['blClass']}")
public class ${getBlObj()['blClass']} implements BusinessService {
    private static final Logger logger = LoggerFactory.getLogger(${getBlObj()['blClass']}.class);

    private GlobalInput _GlobalInput = new GlobalInput();
    public CErrors mErrors = new CErrors();
    private TransferData _TransferData = new TransferData();

    private ${getBlObj()['table']}DBSet m${getBlObj()['table']}DBSet = new ${getBlObj()['table']}DBSet();
    private ${getBlObj()['table']}DB m${getBlObj()['table']}DB = new ${getBlObj()['table']}DB();
    private ${getBlObj()['table']}Set m${getBlObj()['table']}Set = new ${getBlObj()['table']}Set();

    public ${getBlObj()['blClass']}() {
    }

    public ${getBlObj()['blClass']}(GlobalInput tGlobalInput) {
        _GlobalInput = tGlobalInput;
    }

    private ${getBlObj()['table']}Schema getVO(TransferData tData) {
        <#list columnLst as col>
        <#if col?contains("序號")>
            <#continue>
        </#if>
        String ${col} = (String) tData.getValueByName("${col}");
        </#list>

        OLDMAKEDATE = PubFun.getCurrentDate();
        OLDMAKETIME = PubFun.getCurrentTime();

        ${getBlObj()['table']}Schema schema = new ${getBlObj()['table']}Schema();

        <#list columnLst as col>
        <#if col?contains("序號")>
            <#continue>
        </#if>
        <#assign javaParam = columnLst2[col?index] />
        schema.set${javaParam?cap_first}(${col});
        </#list>
        logger.debug("## schema : " + ReflectionToStringBuilder.toString(schema, ToStringStyle.MULTI_LINE_STYLE));
        return schema;
    }

    private void copySchemaToSchema(${getBlObj()['table']}Schema fromSchema, ${getBlObj()['table']}Schema toScheam) {
        <#list columnLst2 as col>
        <#if col?contains("序號")>
            <#continue>
        </#if>
        toScheam.set${col?cap_first}(fromSchema.get${col?cap_first}());
        </#list>
    }

    private ${getBlObj()['table']}Schema findByPk(${getBlObj()['table']}Schema schema) {
        String polno = schema.getPolNo();
        String serialNo = schema.getSerialNo();
        ${getBlObj()['table']}Set t${getBlObj()['table']}Set = m${getBlObj()['table']}DB.executeQuery("select * from ${getBlObj()['table']} where ${getPkWhereCondition()}");
        logger.debug("# size = " + t${getBlObj()['table']}Set.size());
        if (t${getBlObj()['table']}Set.size() != 0) {
            return t${getBlObj()['table']}Set.get(1);
        }
        return null;
    }

    /**
     ** 投連計價批處理調用 FundPriceCalAuto
     */
    public boolean submitData(VData data, String Operater) {
        _GlobalInput = (GlobalInput) data.getObjectByObjectName("GlobalInput", 0);
        _TransferData = (TransferData) data.getObjectByObjectName("TransferData", 0);

        ${getBlObj()['table']}Schema schema = getVO(_TransferData);
        boolean updateSuccess = false;

        if ("insert".equals(Operater)) {
            schema.getDB().insert();
            updateSuccess = true;
        } else if ("update".equals(Operater)) {
            ${getBlObj()['table']}Schema schema2 = this.findByPk(schema);
            if (schema2 == null) {
                schema.getDB().insert();
                updateSuccess = true;
            } else {
                copySchemaToSchema(schema, schema2);
                schema2.getDB().update();
                updateSuccess = true;
            }
        } else if ("delete".equals(Operater)) {
            ${getBlObj()['table']}Schema schema2 = this.findByPk(schema);
            if (schema2 != null) {
                schema2.getDB().delete();
                updateSuccess = true;
            }
        }
        return updateSuccess;
    }

    /////////////////////////////////////////////////////////////////////////
    
    private boolean sbmtData(VData data) {
        PubSubmit pubSubmit = new PubSubmit();
        if (!pubSubmit.submitData(data, "")) {
            logger.info("数据库操作失败");
            return false;
        }
        return true;
    }

    public CErrors getCErrors() {
        return mErrors;
    }

    public VData getResult() {
        return null;
    }

    public CErrors getErrors() {
        logger.debug("//////////////" + mErrors.getFirstError());
        return mErrors;
    }

    public LCPolSchema queryLCPol(String PolNo) {
        logger.debug("帐户内查询保单表");
        LCPolDB tLCPolDB = new LCPolDB();
        tLCPolDB.setPolNo(PolNo);
        if (tLCPolDB.getInfo() == false) {
            // @@错误处理
            logger.debug("queryLCPol保单表查询失败");
            this.mErrors.copyAllErrors(tLCPolDB.mErrors);

            CError tError = new CError();
            tError.moduleName = "DealAccount";
            tError.functionName = "queryLCPol";
            tError.errorMessage(new I18nMessage("保单表查询失败!", "xIDx155879948776124XidX"));
            this.mErrors.addOneError(tError);

            return null;
        }
        return tLCPolDB.getSchema();
    }

    private void commonUsage() {
        String CurrentDate = PubFun.getCurrentDate();
        String CurrentTime = PubFun.getCurrentTime();
        PubFun.calDate(CurrentDate, 2, "D", null);
        String tLimit = PubFun.getNoLimit("222");
        String serNo = PubFun1.CreateMaxNo("SERIALNO", tLimit);
    }

    private double getDoubleValue(String tSql) {
        ExeSQL tExeSQL = new ExeSQL();
        double dd = Double.parseDouble(tExeSQL.getOneValue(tSql));
        return dd;
    }

    public ${getBlObj()['table']}Set queryDoc(String docCode, String bussType, String subType) {
        String sql = "select * from ES_DOC_MAIN ";
        sql += " where  DocCode='?DocCode?' ";
        sql += " and BussType='?BussType?' ";
        sql += " and SubType='?SubType?' ";
        sql += " union ";
        sql += "select * from ES_DOC_MAIN ";
        sql += " where  exists (select 1 from es_doc_relation c where c.bussno = ES_DOC_MAIN.Doccode  and c.busstype = ES_DOC_MAIN.Busstype"
                + " and c.subtype = ES_DOC_MAIN.Subtype and c.DocCode='?DocCode?'  and c.doccode is not null ";
        sql += " and c.BussType='?BussType?' ";
        sql += " and c.SubType='?SubType?' )";

        SQLwithBindVariables sqlbv = new SQLwithBindVariables();
        sqlbv.sql(sql);
        sqlbv.put("DocCode", docCode);
        sqlbv.put("BussType", bussType);
        sqlbv.put("SubType", subType);

        return m${getBlObj()['table']}DB.executeQuery(sqlbv);
    }
}
