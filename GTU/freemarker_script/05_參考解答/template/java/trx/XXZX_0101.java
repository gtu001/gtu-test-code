<#import "/lib.ftl" as my>  

/**
 * 
 */
package com.cathay.xx.zx.trx;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.cathay.common.bo.ReturnMessage;
import com.cathay.common.exception.ErrorInputException;
import com.cathay.common.exception.ModuleException;
import com.cathay.common.im.util.MessageUtil;
import com.cathay.common.im.util.VOTool;
import com.cathay.common.service.authenticate.UserObject;
import com.cathay.common.trx.UCBean;
import com.cathay.common.util.DATE;
import com.cathay.common.util.IConstantMap;
import com.cathay.rz.n0.module.RZ_N0Z001;
import com.cathay.util.ReturnCode;
import com.cathay.util.Transaction;
import com.cathay.xx.vo.DTXXTP01;
import com.cathay.xx.zx.module.XX_ZX0100;
import com.igsapp.common.trx.ServiceException;
import com.igsapp.common.trx.TxException;
import com.igsapp.common.util.annotation.CallMethod;
import com.igsapp.common.util.annotation.TxBean;
import com.igsapp.wibc.dataobj.Context.RequestContext;
import com.igsapp.wibc.dataobj.Context.ResponseContext;

/**
 * <pre>
 * Date         Version Description Author
 * ${my.sysdate()}   1.0 Created     張純毓
 *程式功能    ${model_chinese}資料維護
 *程式名稱    ${edit_action_clz}.java
 *作業方式    ONLINE
 *概要說明    (1) 新增 (2) 修改 (3) 提交 (4) 審核 (5) 退回 (6) 刪除
 *按鈕授權    FUNC_ID = ZZZX0101
 *                              
 * <pre>
 * author
 * @since ${my.sysdate()}
 */
@SuppressWarnings("unchecked")
@TxBean
public class ${edit_action_clz} extends UCBean {

    /** 靜態的 log 物件 **/
    private static final Logger log = Logger.getLogger(${edit_action_clz}.class);

    /** 此 TxBean 程式碼共用的 ResponseContext */
    private ResponseContext resp;

    /** 此 TxBean 程式碼共用的 ReturnMessage */
    private ReturnMessage msg;

    /** 此 TxBean 程式碼共用的 UserObject */
    private UserObject user;
    
    /** 公司別 */
    private String DIV;

    /** 覆寫父類別的 start() 以強制於每次 Dispatcher 呼叫 method 時都執行程式自定的初始動作 **/
    public ResponseContext start(RequestContext req) throws TxException, ServiceException {
        super.start(req); //一定要 invoke super.start() 以執行權限檢核
        initApp(req); //呼叫自定的初始動作
        return null;
    }

    /**
     * 程式自定的初始動作，通常為取出 ResponseContext, UserObject, 
     * 及設定 ReturnMessage 及 response code.
     */
    private void initApp(RequestContext req) {
        // 建立此 TxBean 通用的物件
        resp = this.newResponseContext();
        msg = new ReturnMessage();
        user = this.getUserObject(req);
        // 先將 ReturnMessage 的 reference 加到 response context
        resp.addOutputData(IConstantMap.ErrMsg, msg);
        
        // 取得公司別
        DIV = user.getCOMP_ID();

        // 在 Cathay 通常只有一個 page 在前面 display，所以可以先設定
        resp.setResponseCode("success");
    }

    /**
     * 初始頁面
     * @param req
     * @return
     */
    @CallMethod(action = "prompt", url = "/XX/ZX/${edit_action_clz}/${edit_jsp}.jsp")
    public ResponseContext doPrompt(RequestContext req) {
        try {
			VOTool.setParamsFromLP_JSON(req);
            String str_reqMap = req.getParameter("LP_JSON");
            Map reqMap = StringUtils.isEmpty(str_reqMap) ? new HashMap() : VOTool.jsonToMap(str_reqMap);
            try {
                query(reqMap, new ${model_clz}(), true);
            } catch (Exception e) {
                log.error("初始查詢使用者資訊失敗", e);
                MessageHelper.setReturnMessage(msg, ReturnCode.OK, "初始查詢使用者資訊失敗");
            }
        } catch (Exception e) {
            log.error("初始失敗", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "初始失敗");
        }
        return resp;
    }

    /**
     * 新增
     * @param req
     * @return
     */
    @CallMethod(action = "insert", type = CallMethod.TYPE_AJAX)
    public ResponseContext doInsert(RequestContext req) {
        try {
            Map reqMap = VOTool.jsonToMap(req.getParameter("reqMap"));
            ${model_clz} the${model_clz} = new ${model_clz}();
            Transaction.begin();
            try {
                the${model_clz}.insert(reqMap, user);
                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                throw e;
            }
            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "新增完成");
            reqMap.put("ACTION_TYPE", "U"); // 執行ACTION_TYPE = ‘U’之動作
            try {
                query(reqMap, the${model_clz}); // 執行初始查詢
            } catch (Exception e) {
                log.error("新增完成但重查失敗", e);
               MessageHelper.setReturnMessage(msg, ReturnCode.OK, "新增完成但重查失敗");
            }
        } catch (ErrorInputException eie) {
            log.error("新增失敗", eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (DataNotFoundException dnfe) {
            log.error("新增失敗", dnfe);
            MessageHelper.setReturnMessage(msg, ReturnCode.DATA_NOT_FOUND, "查無資料");
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error("新增失敗", me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "新增失敗", me, req);
            }
        } catch (Exception e) {
            log.error("新增失敗", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "新增失敗", e, req);
        }
        return resp;
    }

    /**
     * 修改
     * @param req
     * @return
     */
    @CallMethod(action = "update", type = CallMethod.TYPE_AJAX)
    public ResponseContext doUpdate(RequestContext req) {
        try {
            Map reqMap = VOTool.jsonToMap(req.getParameter("reqMap"));
            XX_ZX0100 the${model_clz} = new XX_ZX0100();
            Transaction.begin();
            try {
                the${model_clz}.update(reqMap, user);
                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                throw e;
            }
            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "修改完成");
            reqMap.put("ACTION_TYPE", "U");
            try {
                query(reqMap, the${model_clz}); // 執行初始查詢
            } catch (Exception e) {
                log.error("修改完成但重查失敗", e);
                MessageHelper.setReturnMessage(msg, ReturnCode.OK, "修改完成但重查失敗");
            }
        } catch (ErrorInputException eie) {
            log.error("修改失敗", eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (DataNotFoundException dnfe) {
            log.error("修改失敗", dnfe);
            MessageHelper.setReturnMessage(msg, ReturnCode.DATA_NOT_FOUND, "查無資料");
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error("修改失敗", me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "修改失敗", me, req);
            }
        } catch (Exception e) {
            log.error("修改失敗", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "修改失敗", e, req);
        }
        return resp;
    }

    /**
     * 刪除
     * @param req
     * @return
     */
    @CallMethod(action = "delete", type = CallMethod.TYPE_AJAX)
    public ResponseContext doDelete(RequestContext req) {
        try {
            Map reqMap = VOTool.jsonToMap(req.getParameter("reqMap"));
            String emp_id = MapUtils.getString(reqMap, "EMP_ID");
            Transaction.begin();
            try {
                new ${model_clz}().delete(emp_id);
                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                throw e;
            }
            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "完成刪除");
        } catch (ErrorInputException eie) {
            log.error("刪除失敗", eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (DataNotFoundException dnfe) {
            log.error("刪除失敗", dnfe);
            MessageHelper.setReturnMessage(msg, ReturnCode.DATA_NOT_FOUND, "查無資料");
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error("刪除失敗", me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "刪除失敗", me, req);
            }
        } catch (Exception e) {
            log.error("刪除失敗", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "刪除失敗", e, req);
        }
        return resp;
    }

    /**
     * 提交
     * @param req
     * @return
     */
    @SuppressWarnings("deprecation")
    @CallMethod(action = "submit", type = CallMethod.TYPE_AJAX)
    public ResponseContext doSubmit(RequestContext req) {
        try {
            Map reqMap = VOTool.jsonToMap(req.getParameter("reqMap"));
            String FLOW_NO = MapUtils.getString(reqMap, "FLOW_NO");
            DTXXTP01 DTXXTP01vo = VOTool.mapToVO(DTXXTP01.class, reqMap); // 將reqMap轉成vo

            ${model_clz} the${model_clz} = new ${model_clz}();
            Transaction.begin();
            try {
                new RZ_N0Z001().approveFlow(FLOW_NO, "提交", "", user.getEmpID(), user.getDivNo());
                the${model_clz}.confirm(DTXXTP01vo); // 更新審批流程至待審件 20
                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                throw e;
            }
            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "提交完成");
            reqMap.put("ACTION_TYPE", "U");
            try {
                query(reqMap, the${model_clz}); // 重新執行初始動作
            } catch (Exception e) {
                log.error("提交完成但重查失敗", e);
                MessageHelper.setReturnMessage(msg, ReturnCode.OK, "提交完成但重查失敗");
            }
      
        } catch (ErrorInputException eie) {
            log.error("提交失敗", eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (DataNotFoundException dnfe) {
            log.error("提交失敗", dnfe);
            MessageHelper.setReturnMessage(msg, ReturnCode.DATA_NOT_FOUND, "查無資料");
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error("提交失敗", me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "提交失敗", me, req);
            }
        } catch (Exception e) {
            log.error("提交失敗", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "提交失敗", e, req);
        }
        return resp;
    }

    /**
     * 審核
     * @param req
     * @return
     */
    @SuppressWarnings("deprecation")
    @CallMethod(action = "approve", type = CallMethod.TYPE_AJAX)
    public ResponseContext doApprove(RequestContext req) {
        try {
            Map reqMap = VOTool.jsonToMap(req.getParameter("reqMap"));
            String FLOW_NO = MapUtils.getString(reqMap, "FLOW_NO");
            DTXXTP01 DTXXTP01vo = VOTool.mapToVO(DTXXTP01.class, reqMap); // 將reqMap轉成vo

            ${model_clz} the${model_clz} = new ${model_clz}();
            Transaction.begin();
            try {
                new RZ_N0Z001().approveFlow(FLOW_NO, "審核", "", user.getEmpID(), user.getDivNo());
                the${model_clz}.approve(DTXXTP01vo); // 更新審批流程至待審件 30
                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                throw e;
            }
            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "審核完成");
            reqMap.put("ACTION_TYPE", "U");
            try {
                query(reqMap, the${model_clz}); // 重新執行初始動作
            } catch (Exception e) {
                log.error("審核完成但重查失敗", e);
                MessageHelper.setReturnMessage(msg, ReturnCode.OK, "審核完成但重查失敗");
            }

        } catch (ErrorInputException eie) {
            log.error("審核失敗", eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (DataNotFoundException dnfe) {
            log.error("審核失敗", dnfe);
            MessageHelper.setReturnMessage(msg, ReturnCode.DATA_NOT_FOUND, "查無資料");
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error("審核失敗", me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "審核失敗", me, req);
            }
        } catch (Exception e) {
            log.error("審核失敗", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "審核失敗", e, req);
        }
        return resp;
    }

    /**
     * 退回
     * @param req
     * @return
     */
    @SuppressWarnings("deprecation")
    @CallMethod(action = "reject", type = CallMethod.TYPE_AJAX)
    public ResponseContext doReject(RequestContext req) {
        try {
            Map reqMap = VOTool.jsonToMap(req.getParameter("reqMap"));
            String FLOW_NO = MapUtils.getString(reqMap, "FLOW_NO");
            DTXXTP01 DTXXTP01vo = VOTool.mapToVO(DTXXTP01.class, reqMap); // 將reqMap轉成vo

            ${model_clz} the${model_clz} = new ${model_clz}();
            Transaction.begin();
            try {
                new RZ_N0Z001().rejectFlow(FLOW_NO, "退回", "", user.getEmpID(), user.getDivNo());
                the${model_clz}.reject(DTXXTP01vo); // 更新審批流程至待審件 10
                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                throw e;
            }
            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "退回完成");
            reqMap.put("ACTION_TYPE", "U");
            try {
                query(reqMap, the${model_clz}); // 重新執行初始動作
            } catch (Exception e) {
                log.error("退回完成但重查失敗", e);
                MessageHelper.setReturnMessage(msg, ReturnCode.OK, "退回完成但重查失敗");
            }
        
        } catch (ErrorInputException eie) {
            log.error("退回失敗", eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (DataNotFoundException dnfe) {
            log.error("退回失敗", dnfe);
            MessageHelper.setReturnMessage(msg, ReturnCode.DATA_NOT_FOUND, "查無資料");
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error("退回失敗", me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "退回失敗", me, req);
            }
        } catch (Exception e) {
            log.error("退回失敗", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "退回失敗", e, req);
        }
        return resp;
    }
    
    
    private Map getFileUploadReqMap(RequestContext req) {
        Map reqMap = new HashMap();
        for (Enumeration enu = req.getParameterNames(); enu.hasMoreElements();) {
            String key = (String) enu.nextElement();
            String value = req.getParameter(key);
            reqMap.put(key, value);
        }
        return reqMap;
    }

    /**
     * 附件上傳
     * @param req
     * @return
     */
    @CallMethod(action = "upload", name = CallMethod.TYPE_NA)
    public ResponseContext doUpload(RequestContext req) {
        try {
            // 取得上傳路徑的檔案資料
            FileItem inputFile = FileStoreUtil.parseUploadStream(req); //匯入檔案

            Map reqMap = getFileUploadReqMap(req);
            resp.addOutputData("updateMap", VOTool.toJSON(reqMap));

            ${model_clz} the${model_clz} = new ${model_clz}();
            Transaction.begin();
            try {
                new RZ_N0Z001().upload(FLOW_NO, "上傳", "", user.getEmpID(), user.getDivNo());
                the${model_clz}.upload(DTXXTP01vo); // 更新審批流程至待審件 10
                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                throw e;
            }
        } catch (ErrorInputException eie) {
            log.error("上傳失敗", eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (DataNotFoundException dnfe) {
            log.error("上傳失敗", dnfe);
            MessageHelper.setReturnMessage(msg, ReturnCode.DATA_NOT_FOUND, "查無資料");
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error("上傳失敗", me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "上傳失敗", me, req);
            }
        } catch (Exception e) {
            log.error("上傳失敗", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "上傳失敗", e, req);
        } finally {
            try {
                Map map = new HashMap();
                map.put(IConstantMap.ErrMsg, msg);
                EncodingHelper.send2iframe(req, map, msg);
            } catch (Exception e) {
                log.error("上傳失敗", e);
            }
        }
        return resp;
    }

    /**
     * 根據ActionType執行相關程式碼
     * @param map
     * @throws Exception 
     */
    private void query(Map map, ${model_clz} the${model_clz}) throws Exception {
        query(map, the${model_clz}, false);
    }

    private void query(Map map, ${model_clz} the${model_clz}, boolean isPrompt) throws Exception {
        String ACTION_TYPE = MapUtils.getString(map, "ACTION_TYPE");
        if(StringUtils.isBlank(ACTION_TYPE)){
            map.put("OP_STATUS_NM", "輸入中");
            map.put("ACTION_TYPE", "I");
            ACTION_TYPE= "I";
        }
        if ("I".equals(ACTION_TYPE)) {
            String UPDT_DATE = DATE.currentTime().toString();
            map.put("UPDT_DATE", UPDT_DATE.substring(0, UPDT_DATE.length() - 4));
            map.put("UPDT_NM", user.getEmpName());
            resp.addOutputData("rtnMap", VOTool.toJSON(map)); // 轉換成json給前端的 jstl c:out 使用
        } else {
            Map rtnMap = theXX_ZX0100.query(map).get(0);
            if (isPrompt) {
                String UPDT_DATE = MapUtils.getString(rtnMap, "UPDT_DATE", ""); // 取出UPDT_DATE,若無值則補空字串
                rtnMap.remove("UPDT_DATE");
                rtnMap.put("UPDT_DATE", UPDT_DATE.substring(0, UPDT_DATE.length() - 4));
                resp.addOutputData("rtnMap", VOTool.toJSON(rtnMap)); // 轉換成json給前端的 jstl c:out 使用
            } else {
                String UPDT_DATE = MapUtils.getString(rtnMap, "UPDT_DATE", ""); // 取出UPDT_DATE,若無值則補空字串
                rtnMap.remove("UPDT_DATE");
                rtnMap.put("UPDT_DATE", UPDT_DATE.substring(0, UPDT_DATE.length() - 4));
                resp.addOutputData("rtnMap", rtnMap); // 直接傳遞物件給前端的Ajax使用
            }

            /*
            
            resp.addOutputData("ACTION_TYPE", ACTION_TYPE);
            resp.addOutputData("EMP_ID", rtnMap.get("EMP_ID"));
            resp.addOutputData("EMP_NAME", rtnMap.get("EMP_NAME"));
            resp.addOutputData("DIV_NO", rtnMap.get("DIV_NO"));
            resp.addOutputData("BIRTHDAY", rtnMap.get("BIRTHDAY"));
            resp.addOutputData("POSITION", rtnMap.get("POSITION"));
            resp.addOutputData("UPDT_NM", rtnMap.get("UPDT_NM"));
            resp.addOutputData("DEP_NM", rtnMap.get("DIV_NO_NM"));
            String UPDT_DATE = MapUtils.getString(rtnMap, "UPDT_DATE", "");
            resp.addOutputData("UPDT_DATE", UPDT_DATE.substring(0, UPDT_DATE.length() - 4));
            resp.addOutputData("FLOW_NO", rtnMap.get("FLOW_NO")); // 取得審批流水號
            resp.addOutputData("OP_STATUS", rtnMap.get("OP_STATUS")); // 取得狀態
            resp.addOutputData("OP_STATUS_NM", rtnMap.get("OP_STATUS_NM")); // 取得狀態中文
            */
            
        }

    }

}
