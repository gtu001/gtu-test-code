<#import "lib.ftl" as my>  

package com.cathay.xx.zx.module;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.cathay.common.exception.ErrorInputException;
import com.cathay.common.exception.ModuleException;
import com.cathay.common.im.util.VOTool;
import com.cathay.common.service.authenticate.UserObject;
import com.cathay.common.util.db.DBUtil;
import com.cathay.rz.n0.module.RZ_N0Z001;
import com.cathay.util.Transaction;
import com.cathay.xx.vo.DTXXTP01;
import com.igsapp.db.DataSet;

/**
 * <pre>
 * DATE	Version	Description	Author
 * ${my.sysdate()}	1.0	Create	張純毓
 * 
 * 一、	程式功能概要說明：
 * 模組名稱	${model_chinese}
 * 模組ID	${model_clz}
 * 概要說明	${model_desc}
 * 
 * </pre>
 * 
 * @author gtu001
 * 
 */
@SuppressWarnings("unchecked")
public class ${model_clz} {

    /** 靜態的 log 物件 **/
    private static final Logger log = Logger.getLogger(XX_ZX0100.class);
    
     
     /**
     * 查詢資料
     * 
     * @param map
     * @return
     * @throws ModuleException
     */
    public List<Map> query(Map reqMap, UserObject user) throws Exception {
        ErrorInputException eie = null;
        if (reqMap == null || reqMap.isEmpty()) {
            eie = this.getEieInstance(eie, "查詢資訊不得為空");//
        }
        if (user == null) {
            eie = this.getEieInstance(eie, "登入者資訊不得為空")); //
        }
        if (eie != null) {
            throw eie;
        }

        DataSet ds = Transaction.getDataSet();
        List<Map> rtnList = new ArrayList<Map>();
        
        <#list searchColumns as col>
        String ${col} = MapUtils.getString(reqMap, "${col}"); 
        </#list>

        <#list searchColumns as col>
        if (StringUtils.isNotEmpty(${col})) {
            <#if ! searchColumns_like?seq_contains(col)>
            ds.setField("${col}", ${col});
            <#else>
            ds.setField("${col}", new StirngBuilder().append("%").append(${col}).append("%").toString());
            </#if>
        }
        </#list>
        ds.setField("DIV_NO", user.getOpUnit());

        DBUtil.searchAndRetrieve(ds, SQL_queryNonCashChkList_001);
        while (ds.next()) {
            Map dataMap = VOTool.dataSetToMap(ds);
            rtnList.add(dataMap);
        }

        return rtnList;
    }
    
    
    
    /**
     * 新增資料
     * 
     * @param reqMap
     * @param userObj
     * @throws ModuleException
     */
    public void insert(Map reqMap, UserObject user) throws ModuleException {
        //針對傳入參數進行檢核
        
        ErrorInputException eie = null;
        if (reqMap == null || reqMap.isEmpty()) {
            eie = this.getEieInstance(eie, "查詢資訊不得為空");//
        }
        if (user == null) {
            eie = this.getEieInstance(eie, "登入者資訊不得為空")); //
        }
        if (eie != null) {
            throw eie;
        }

        <#list insertColumns as col>
        String ${col} = MapUtils.getString(reqMap, "${col}"); 
        </#list>
        Timestamp UPDT_DATE = new Timestamp(System.currentTimeMillis());
        String OP_STATUS = "10";
        
        DataSet ds = Transaction.getDataSet();
        
        // 若檢核無誤
        <#list insertColumns as col>
        <#if insertColumns_pk?seq_contains(col)>
        if (StringUtils.isEmpty(${col})) {
            throw new ErrorInputException("${columnLabel[col]}不可為空");
        }else{
            ds.setField("${col}", ${col});
        }
        <#else>
        if (StringUtils.isNotEmpty(${col})) {
            ds.setField("${col}", ${col});
        }
        </#if>
        </#list>
        
        ds.setField("OP_STATUS", OP_STATUS);
        ds.setField("UPDT_ID", user.getEmpID());
        ds.setField("UPDT_DATE", UPDT_DATE);

        // 新增資料

        ds.beginTransaction();
        DBUtil.executeUpdate(ds, SQL_insert_001);
    }
    
    
    
    /**
     * 更新資料 
     * 
     * @param reqMap
     * @param userObj
     * @throws ModuleException
     */
    public void update(Map reqMap, UserObject user) throws ModuleException {
        //針對傳入參數進行檢核

        <#list updateColumns as col>
        String ${col} = MapUtils.getString(reqMap, "${col}"); 
        </#list>
        Timestamp UPDT_DATE = new Timestamp(System.currentTimeMillis());

        DataSet ds = Transaction.getDataSet();
        
        // 若檢核無誤
        <#list updateColumns as col>
        <#if updateColumns_pk?seq_contains(col)>
        if (StringUtils.isEmpty(${col})) {
            throw new ErrorInputException("${columnLabel[col]}不可為空");
        }else{
            ds.setField("${col}", ${col});
        }
        <#else>
        if (StringUtils.isNotEmpty(${col})) {
            ds.setField("${col}", ${col});
        }
        </#if>
        </#list>
        
        ds.setField("UPDT_ID", user.getEmpID());
        ds.setField("UPDT_DATE", UPDT_DATE);
        
        // 將資料更新

        ds.beginTransaction();
        DBUtil.executeUpdate(ds, SQL_update_001);
    }
    
    
    <#assign delParams = my.fixArry(deleteColumns_pk, 'String ', '', false)/>
    <#assign delParamStr = my.listJoin(delParams, ',')/>
    
    /**
     * 刪除員工基本資料
     * 
     * @param reqMap
     * @param userObj
     * @throws ModuleException
     */
    public void delete(${delParamStr}) throws ModuleException {
        //針對傳入參數進行檢核
        //依傳入EMP_ID刪除 人員基本資料檔 DBXX.DTXXTP01 資料
        DataSet ds = Transaction.getDataSet();
        ds.beginTransaction();
        
        <#list deleteColumns_pk as col>
        if (StringUtils.isEmpty(${col})) {
            throw new ErrorInputException("${columnLabel[col]}不可為空");
        }else{
            ds.setField("${col}", ${col});
        }
        </#list>
        
        DBUtil.executeUpdate(ds, SQL_delete_001);
    }

    /**
     * 提交
     * @param vo
     * @throws ModuleException
     */
    public void confirm(${model_vo_clz} vo) throws ModuleException {
        if (vo == null) {
            throw new ErrorInputException("${model_chinese}資料不可為空");
        }
        <#list approveColumns_pk as col>
        if (StringUtils.isEmpty(vo.get${col}())) {
            throw new ErrorInputException("${columnLabel[col]}不可為空");
        }
        </#list>
        
        //更新狀態待審件 20
        vo.setOP_STATUS(20);
        VOTool.update(vo);
    }

    /**
     * 審核
     * @param vo
     * @throws ModuleException
     */
    public void approve(${model_vo_clz} vo) throws ModuleException {
        if (vo == null) {
            throw new ErrorInputException("${model_chinese}資料不可為空");
        }
        <#list approveColumns_pk as col>
        if (StringUtils.isEmpty(vo.get${col}())) {
            throw new ErrorInputException("${columnLabel[col]}不可為空");
        }
        </#list>
        
        //更新狀態 至輸入件30
        vo.setOP_STATUS(30);
        VOTool.update(vo);
    }

    /**
     * 退回
     * @param vo
     * @throws ModuleException
     */
    public void reject(${model_vo_clz} vo) throws ModuleException {
        if (vo == null) {
            throw new ErrorInputException("${model_chinese}資料不可為空");
        }
        <#list approveColumns_pk as col>
        if (StringUtils.isEmpty(vo.get${col}())) {
            throw new ErrorInputException("${columnLabel[col]}不可為空");
        }
        </#list>
        
        //更新狀態 至輸入件10
        vo.setOP_STATUS(10);
        VOTool.update(vo);
    }
    

    /**
     *  輸入檢核
     * @param eie
     * @param errMsg
     * @return
     */
    private ErrorInputException getEieInstance(ErrorInputException eie, String errMsg) {
        if (eie == null) {
            eie = new ErrorInputException();
        }
        eie.appendMessage(errMsg);
        return eie;
    }
    
}
