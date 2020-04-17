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



package com.sinosoft.lis.controller.kttest;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sinosoft.lis.db.LCPolDB;
import com.sinosoft.lis.db.${getBlObj()['table']}DB;
import com.sinosoft.lis.i18n.I18nMessage;
import com.sinosoft.lis.pubfun.GlobalInput;
import com.sinosoft.lis.pubfun.PubFun;
import com.sinosoft.lis.pubfun.PubFun1;
import com.sinosoft.lis.pubfun.PubSubmit;
import com.sinosoft.lis.schema.LCPolSchema;
import com.sinosoft.lis.schema.${getBlObj()['table']}Schema;
import com.sinosoft.lis.tb.ProposalApproveBLS;
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
 * @author: Troy
 * @version 1.0
 * @Cleaned Troy ${my.ciDate1(.now, '-')}
 */

@Service(name = "${getBlObj()['blClass']}")
public class ${getBlObj()['blClass']} implements BusinessService {
    private static final Logger logger = LoggerFactory.getLogger(${getBlObj()['blClass']}.class);

    /** 传入数据的容器 */
    private VData mInputData = new VData();
    /** 数据操作字符串 */
    private String mOperate;
    /** 错误处理类 */
    public CErrors mErrors = new CErrors();

    /** 业务处理相关变量 */
    /** 保单数据 */
    private ${getBlObj()['table']}Schema m${getBlObj()['table']}Schema = new ${getBlObj()['table']}Schema();
    /** 全局数据 */
    private GlobalInput mGlobalInput = new GlobalInput();
    private TransferData mTransferData = new TransferData();

    // @Constructor
    public KTTestBL() {
    }

    public KTTestBL(GlobalInput tGlobalInput) {
        mGlobalInput = tGlobalInput;
    }

    /**
     * 数据提交的公共方法
     * 
     * @param: cInputData
     *             传入的数据 cOperate 数据操作字符串
     * @return:
     */
    public boolean submitData(VData data, String Operater) {
        // 将传入的数据拷贝到本类中
        mInputData = (VData) data.clone();
        this.mOperate = Operater;

        // 将外部传入的数据分解到本类的属性中，准备处理
        if (this.getInputData() == false) {
            return false;
        }
        logger.debug("---getInputData---");

        // 校验传入的数据
        if (this.checkData() == false) {
            return false;
        }
        logger.debug("---checkData---");

        // 根据业务逻辑对数据进行处理
        if (this.dealData() == false) {
            return false;
        }
        logger.debug("---dealData---");

        // 装配处理好的数据，准备给后台进行保存
        this.prepareOutputData();
        logger.debug("---prepareOutputData---");

        // 数据提交、保存
//        ProposalApproveBLS tProposalApproveBLS = new ProposalApproveBLS();
//        if (tProposalApproveBLS.submitData(mInputData, Operater) == false) {
//            // @@错误处理
//            this.mErrors.copyAllErrors(tProposalApproveBLS.mErrors);
//            return false;
//        }
        logger.debug("---commitData---");
        return true;
    }

    /**
     * 将外部传入的数据分解到本类的属性中
     * 
     * @param: 无
     * @return: boolean
     */
    private boolean getInputData() {
        // 全局变量
        mGlobalInput.setSchema((GlobalInput) mInputData.getObjectByObjectName("GlobalInput", 0));
        // 保单
        ${getBlObj()['table']}Schema t${getBlObj()['table']}Schema = new ${getBlObj()['table']}Schema();
        t${getBlObj()['table']}Schema.setSchema((${getBlObj()['table']}Schema) mInputData.getObjectByObjectName("${getBlObj()['table']}Schema", 0));

        ${getBlObj()['table']}DB t${getBlObj()['table']}DB = new ${getBlObj()['table']}DB();

        <#list pkColumnLst2 as col>
        t${getBlObj()['table']}DB.set${col}(t${getBlObj()['table']}Schema.get${col}());
        </#list>
        if (t${getBlObj()['table']}DB.getInfo() == false) {
            // @@错误处理
            errorMessage("getInputData", "投保单查询失败!", "xIDx155879950081170XidX");
            return false;
        }
        m${getBlObj()['table']}Schema.setSchema(t${getBlObj()['table']}DB);
        return true;
    }
    
    /**
     * 校验传入的数据
     * 
     * @param: 无
     * @return: boolean
     */
    private boolean checkData() {
        if (!m${getBlObj()['table']}Schema.getGrpContNo().trim().equals("0")) {
            // @@错误处理
            errorMessage("checkData", "此单不是投保单，不能进行复核操作!", "xIDx155879955816563XidX");
            return false;
        }
        if (!m${getBlObj()['table']}Schema.getGrpPolNo().trim().equals("0")) {
            // @@错误处理
            errorMessage("checkData", "此投保单已经开始核保，不能进行复核操作!", "xIDx155879955818177XidX");
            return false;
        }
        /**
         * @todo 查询该次申请的投保单是否已经被其他用户申请
         */
        ${getBlObj()['table']}DB t${getBlObj()['table']}DB = new ${getBlObj()['table']}DB();
        <#list pkColumnLst2 as col>
        t${getBlObj()['table']}DB.set${col}(t${getBlObj()['table']}Schema.get${col}());
        </#list>
        ${getBlObj()['table']}Set t${getBlObj()['table']}Set = t${getBlObj()['table']}DB.query();
        if (t${getBlObj()['table']}Set.get(1).getMakeDate() == null) {
            // @@错误处理
            errorMessage("checkData", "此投保单已经被操作员选取，不能重复进行操作!", "xIDx155879964781357XidX");
            return false;
        }
        return true;
    }

    private void errorMessage(String functionName, String chMessage, String messageCode) {
        CError tError = new CError();
        tError.moduleName = getClass().getSimpleName();
        tError.functionName = functionName;
        tError.errorMessage(new I18nMessage(chMessage, messageCode));
        this.mErrors.addOneError(tError);
    }
    
    private boolean dealData() {
        mGlobalInput = (GlobalInput) mInputData.getObjectByObjectName("GlobalInput", 0);
        mTransferData = (TransferData) mInputData.getObjectByObjectName("TransferData", 0);

        ${getBlObj()['table']}Schema schema = getVO(mTransferData);
        boolean updateSuccess = false;

        if ("insert".equals(mOperate)) {
            schema.getDB().insert();
            updateSuccess = true;
        } else if ("update".equals(mOperate)) {
            ${getBlObj()['table']}Schema schema2 = this.findByPk(schema);
            if (schema2 == null) {
                schema.getDB().insert();
                updateSuccess = true;
            } else {
                copySchemaToSchema(schema, schema2);
                schema2.getDB().update();
                updateSuccess = true;
            }
        } else if ("delete".equals(mOperate)) {
            ${getBlObj()['table']}Schema schema2 = this.findByPk(schema);
            if (schema2 != null) {
                schema2.getDB().delete();
                updateSuccess = true;
            }
        }
        return updateSuccess;
    }
    
    /**
     * 根据业务逻辑对数据进行处理
     * 
     * @param: 无
     * @return: void
     */
    private void prepareOutputData() {
        mInputData.clear();
        mInputData.add(m${getBlObj()['table']}Schema);
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


    private ${getBlObj()['table']}Schema findByPk2(${getBlObj()['table']}Schema schema) {
        ${getBlObj()['table']}DB t${getBlObj()['table']}DB = new ${getBlObj()['table']}DB();
        
        <#list pkColumnLst2 as col>
        t${getBlObj()['table']}DB.set${col}(t${getBlObj()['table']}Schema.get${col}());// 复核节点
        </#list>
        ${getBlObj()['table']}Set t${getBlObj()['table']}Set = t${getBlObj()['table']}DB.query();
        
        logger.debug("# size = " + t${getBlObj()['table']}Set.size());
        if (t${getBlObj()['table']}DB.getInfo() == false) {
            // @@错误处理
            errorMessage("getInputData", "投保单查询失败!", "xIDx155879950081170XidX");
        }else if (t${getBlObj()['table']}Set.size() != 0) {
            return t${getBlObj()['table']}Set.get(1);
        }
        return null;
    }

    private ${getBlObj()['table']}Schema findByPk(${getBlObj()['table']}Schema schema) {
        ${getBlObj()['table']}DB t${getBlObj()['table']}DB = new ${getBlObj()['table']}DB();
        <#list pkColumnLst2 as col>
        String ${col} = schema.get${col?cap_first}();
        </#list>
        ${getBlObj()['table']}Set t${getBlObj()['table']}Set = t${getBlObj()['table']}DB.executeQuery("select * from ${getBlObj()['table']} where ${getPkWhereCondition()}");
        logger.debug("# size = " + t${getBlObj()['table']}Set.size());
        if (t${getBlObj()['table']}Set.size() != 0) {
            return t${getBlObj()['table']}Set.get(1);
        }
        return null;
    }

    private ${getBlObj()['table']}Schema findByPk(${getPkArgs()}) {
        ${getBlObj()['table']}DB t${getBlObj()['table']}DB = new ${getBlObj()['table']}DB();
        ${getBlObj()['table']}Set t${getBlObj()['table']}Set = t${getBlObj()['table']}DB.executeQuery("select * from ${getBlObj()['table']} where ${getPkWhereCondition()}");
        logger.debug("# size = " + t${getBlObj()['table']}Set.size());
        if (t${getBlObj()['table']}Set.size() != 0) {
            return t${getBlObj()['table']}Set.get(1);
        }
        return null;
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


////////////////////////////////////////////////////
