<#import "/lib.ftl" as my>  
<#import "ctbc_dao_maker.ftl" as ct>  

package com.sinosoft.lis.controller.kttest;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sinosoft.lis.db.LCPolDB;
import com.sinosoft.lis.db.${ct.getBlObj()['table']}DB;
import com.sinosoft.lis.i18n.I18nMessage;
import com.sinosoft.lis.pubfun.GlobalInput;
import com.sinosoft.lis.pubfun.PubFun;
import com.sinosoft.lis.pubfun.PubFun1;
import com.sinosoft.lis.pubfun.PubSubmit;
import com.sinosoft.lis.schema.LCPolSchema;
import com.sinosoft.lis.schema.${ct.getBlObj()['table']}Schema;
import com.sinosoft.lis.tb.ProposalApproveBLS;
import com.sinosoft.lis.vschema.${ct.getBlObj()['table']}Set;
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

@Service(name = "${ct.getBlObj()['blClass']}")
public class ${ct.getBlObj()['blClass']} implements BusinessService {
    private static final Logger logger = LoggerFactory.getLogger(${ct.getBlObj()['blClass']}.class);

    /** 传入数据的容器 */
    private VData mInputData = new VData();
    private VData mResult = new VData();
    /** 数据操作字符串 */
    private String mOperate;
    /** 错误处理类 */
    public CErrors mErrors = new CErrors();

    /** 业务处理相关变量 */
    /** 保单数据 */
    private ${ct.getBlObj()['table']}Schema m${ct.getBlObj()['table']}Schema = new ${ct.getBlObj()['table']}Schema();
    /** 全局数据 */
    private GlobalInput mGlobalInput = new GlobalInput();
    private TransferData mTransferData = new TransferData();

    private MMap mMap = new MMap();

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
        logger.debug("---commitData---");
        PubSubmit tPubSubmit = new PubSubmit();
        if (!tPubSubmit.submitData(mResult, mOperate)) {
            // @@错误处理
            this.mErrors.copyAllErrors(tPubSubmit.mErrors);
            CError tError = new CError();
            tError.moduleName = "${ct.getBlObj()['blClass']}";
            tError.functionName = "submitData";
            tError.errorMessage(new I18nMessage("数据提交失败!", "xIDx155879948680528XidX"));
            this.mErrors.addOneError(tError);
            return false;
        }
        return true;
    }

    /**
     * 将外部传入的数据分解到本类的属性中
     * 
     * @param: 无
     * @return: boolean
     */
    private boolean getInputData() {
        mGlobalInput = (GlobalInput) mInputData.getObjectByObjectName("GlobalInput", 0);
        mTransferData = (TransferData) mInputData.getObjectByObjectName("TransferData", 0);
        mLCRollBackLogSchema = (LCRollBackLogSchema) mInputData.getObjectByObjectName("LCRollBackLogSchema", 0);

        // 全局变量
        mGlobalInput.setSchema((GlobalInput) mInputData.getObjectByObjectName("GlobalInput", 0));
        // 保单
        ${ct.getBlObj()['table']}Schema t${ct.getBlObj()['table']}Schema = new ${ct.getBlObj()['table']}Schema();
        t${ct.getBlObj()['table']}Schema.setSchema((${ct.getBlObj()['table']}Schema) mInputData.getObjectByObjectName("${ct.getBlObj()['table']}Schema", 0));

        ${ct.getBlObj()['table']}DB t${ct.getBlObj()['table']}DB = new ${ct.getBlObj()['table']}DB();

        <#list pkColumnLst2 as col>
        t${ct.getBlObj()['table']}DB.set${col}(t${ct.getBlObj()['table']}Schema.get${col}());
        </#list>
        if (t${ct.getBlObj()['table']}DB.getInfo() == false) {
            // @@错误处理
            errorMessage("getInputData", "投保单查询失败!", "xIDx155879950081170XidX");
            return false;
        }
        m${ct.getBlObj()['table']}Schema.setSchema(t${ct.getBlObj()['table']}DB);
        return true;
    }
    
    /**
     * 校验传入的数据
     * 
     * @param: 无
     * @return: boolean
     */
    private boolean checkData() {
        if (!m${ct.getBlObj()['table']}Schema.getGrpContNo().trim().equals("0")) {
            // @@错误处理
            errorMessage("checkData", "此单不是投保单，不能进行复核操作!", "xIDx155879955816563XidX");
            return false;
        }
        if (!m${ct.getBlObj()['table']}Schema.getGrpPolNo().trim().equals("0")) {
            // @@错误处理
            errorMessage("checkData", "此投保单已经开始核保，不能进行复核操作!", "xIDx155879955818177XidX");
            return false;
        }
        /**
         * @todo 查询该次申请的投保单是否已经被其他用户申请
         */
        ${ct.getBlObj()['table']}DB t${ct.getBlObj()['table']}DB = new ${ct.getBlObj()['table']}DB();
        <#list pkColumnLst2 as col>
        t${ct.getBlObj()['table']}DB.set${col}(t${ct.getBlObj()['table']}Schema.get${col}());
        </#list>
        ${ct.getBlObj()['table']}Set t${ct.getBlObj()['table']}Set = t${ct.getBlObj()['table']}DB.query();
        if (t${ct.getBlObj()['table']}Set.get(1).getMakeDate() == null) {
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
        ${ct.getBlObj()['table']}Schema schema = m${ct.getBlObj()['table']}Schema;

        ${ct.getBlObj()['table']}Schema schema = getVO(mTransferData);
        boolean updateSuccess = false;

        if ("insert".equals(mOperate)) {
            schema.getDB().insert();
            updateSuccess = true;
        } else if ("update".equals(mOperate)) {
            ${ct.getBlObj()['table']}Schema schema2 = this.findByPk(schema);
            if (schema2 == null) {
                schema.getDB().insert();
                updateSuccess = true;
            } else {
                copySchemaToSchema(schema, schema2);
                schema2.getDB().update();
                updateSuccess = true;
            }
        } else if ("delete".equals(mOperate)) {
            ${ct.getBlObj()['table']}Schema schema2 = this.findByPk(schema);
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
        mInputData.add(m${ct.getBlObj()['table']}Schema);
    }

    @Override
    public VData getResult() {
        return mResult;
    }

    @Override
    public CErrors getErrors() {
        return mErrors;
    }


    private ${ct.getBlObj()['table']}Schema getVO(TransferData tData) {
        <#list columnLst as col>
        <#if col?contains("序號")>
            <#continue>
        </#if>
        String ${col} = (String) tData.getValueByName("${col}");
        </#list>

        OLDMAKEDATE = PubFun.getCurrentDate();
        OLDMAKETIME = PubFun.getCurrentTime();

        ${ct.getBlObj()['table']}Schema schema = new ${ct.getBlObj()['table']}Schema();

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

    private void copySchemaToSchema(${ct.getBlObj()['table']}Schema fromSchema, ${ct.getBlObj()['table']}Schema toScheam) {
        <#list columnLst2 as col>
        <#if col?contains("序號")>
            <#continue>
        </#if>
        toScheam.set${col?cap_first}(fromSchema.get${col?cap_first}());
        </#list>
    }

    private ${ct.getBlObj()['table']}Schema findByPk(${ct.getBlObj()['table']}Schema schema) {
        ${ct.getBlObj()['table']}DB t${ct.getBlObj()['table']}DB = new ${ct.getBlObj()['table']}DB();

        <#list pkColumnLst2 as col>
        t${ct.getBlObj()['table']}DB.set${col}(t${ct.getBlObj()['table']}Schema.get${col}());// 复核节点
        </#list>

        if (t${ct.getBlObj()['table']}DB.getInfo() != false) {
            return t${ct.getBlObj()['table']}DB.getSchema();
        }
        return null;
    }

    private ${ct.getBlObj()['table']}Schema findByPk2(${ct.getBlObj()['table']}Schema schema) {
        ${ct.getBlObj()['table']}DB t${ct.getBlObj()['table']}DB = new ${ct.getBlObj()['table']}DB();

        <#list pkColumnLst2 as col>
        t${ct.getBlObj()['table']}DB.set${col}(t${ct.getBlObj()['table']}Schema.get${col}());// 复核节点
        </#list>

        ${ct.getBlObj()['table']}Set t${ct.getBlObj()['table']}Set = t${ct.getBlObj()['table']}DB.query();
        logger.debug("# size = " + t${ct.getBlObj()['table']}Set.size());

        if (t${ct.getBlObj()['table']}Set.size() != 0) {
            return t${ct.getBlObj()['table']}Set.get(1);
        }
        return null;
    }

    private ${ct.getBlObj()['table']}Schema findByPk3(${ct.getBlObj()['table']}Schema schema) {
        ${ct.getBlObj()['table']}DB t${ct.getBlObj()['table']}DB = new ${ct.getBlObj()['table']}DB();
        String sql = "select * from ${ct.getBlObj()['table']} where ${ct.getPkWhereCondition2()}";
        SQLwithBindVariables sqlbv = new SQLwithBindVariables();
        sqlbv.sql(sql);
        <#list pkColumnLst2 as col>
        sqlbv.put("${col}", schema.get${col?cap_first}());
        </#list>

        ${ct.getBlObj()['table']}Set t${ct.getBlObj()['table']}Set = t${ct.getBlObj()['table']}DB.executeQuery(sqlbv);
        logger.debug("# size = " + t${ct.getBlObj()['table']}Set.size());
        if (t${ct.getBlObj()['table']}Set.size() != 0) {
            return t${ct.getBlObj()['table']}Set.get(1);
        }
        return null;
    }

    private ${ct.getBlObj()['table']}Schema findByPk4(${ct.getPkArgs()}) {
        ${ct.getBlObj()['table']}DB t${ct.getBlObj()['table']}DB = new ${ct.getBlObj()['table']}DB();
        ${ct.getBlObj()['table']}Set t${ct.getBlObj()['table']}Set = t${ct.getBlObj()['table']}DB.executeQuery("select * from ${ct.getBlObj()['table']} where ${ct.getPkWhereCondition()}");
        logger.debug("# size = " + t${ct.getBlObj()['table']}Set.size());
        if (t${ct.getBlObj()['table']}Set.size() != 0) {
            return t${ct.getBlObj()['table']}Set.get(1);
        }
        return null;
    }

    private void queryCustom001() {
        StringBuffer tSBSql = new StringBuffer();
        tSBSql.append("select polno, mainpolno from ${ct.getBlObj()['table']} where contno='?CONTNO?' and appflag='1'");
        SQLwithBindVariables tSBV = new SQLwithBindVariables();
        tSBV.sql(tSBSql.toString());
        tSBV.put("CONTNO", tContNo);
        ExeSQL tExeSQL = new ExeSQL();
        SSRS tResult = tExeSQL.execSQL(tSBV);
        for (int i = 0; i < tResult.getMaxRow(); i++) {
            String PolNo = tResult.GetText(i + 1, 1);
            String MainPolNo = tResult.GetText(i + 1, 2);
        }
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
}


////////////////////////////////////////////////////
