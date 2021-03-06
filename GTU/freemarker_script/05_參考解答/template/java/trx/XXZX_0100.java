<#import "/lib.ftl" as my>  

package com.cathay.xx.zx.trx;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cathay.common.bo.ReturnMessage;
import com.cathay.common.exception.DataNotFoundException;
import com.cathay.common.exception.ErrorInputException;
import com.cathay.common.exception.ModuleException;
import com.cathay.common.exception.OverCountLimitException;
import com.cathay.common.im.util.MessageUtil;
import com.cathay.common.im.util.VOTool;
import com.cathay.common.message.MessageHelper;
import com.cathay.common.service.authenticate.UserObject;
import com.cathay.common.trx.UCBean;
import com.cathay.common.util.IConstantMap;
import com.cathay.util.ReturnCode;
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
 * 程式功能    ${model_chinese}作業
 * 程式名稱    ${main_action_clz}.java
 * 作業方式    ONLINE
 * 概要說明    (1) 查詢作業 ─  (2) 清除 ─ 清除畫面上資料 (3) 新增作業 ─超連結至 ${edit_action_clz}
 * 按鈕授權    無
 * 多筆查詢    (必須�悃鉹中@項)   無      
 *                              
 * <pre>
 * author
 * @since ${my.sysdate()}
 */
@SuppressWarnings("unchecked")
@TxBean
public class ${main_action_clz} extends UCBean {

    /** 靜態的 log 物件 **/
    private static final Logger log = Logger.getLogger(${main_action_clz}.class);

    /** 此 TxBean 程式碼共用的 ResponseContext */
    private ResponseContext resp;

    /** 此 TxBean 程式碼共用的 ReturnMessage */
    private ReturnMessage msg;

    /** 此 TxBean 程式碼共用的 UserObject */
    private UserObject user;

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

        // 在 Cathay 通常只有一個 page 在前面 display，所以可以先設定
        resp.setResponseCode("success");
    }

    /**
     * 初始頁面
     * @param req
     * @return
     */
    @CallMethod(action = "prompt", url = "/XX/ZX/${main_action_clz}/${main_jsp}.jsp")
    public ResponseContext doPrompt(RequestContext req) {
		
        return resp;
    }

    /**
     * 查詢
     * @param req
     * @return
     */
    @CallMethod(action = "query", type = CallMethod.TYPE_AJAX)
    public ResponseContext doQuery(RequestContext req) {
        try {
			VOTool.setParamsFromLP_JSON(req);
            Map reqMap = VOTool.jsonToMap(req.getParameter("reqMap"));
			
            //讀取資料檔
            List<Map> rtnList = new ${model_clz}().query(reqMap);
            resp.addOutputData("rtnList", rtnList);
			
            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "查詢成功");
        } catch (ErrorInputException eie) {
            log.error("查詢失敗", eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (DataNotFoundException dnfe) {
            log.error("查詢失敗", dnfe);
            MessageHelper.setReturnMessage(msg, ReturnCode.DATA_NOT_FOUND, "查無資料");
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error("查詢失敗", me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                if (me.getRootException() instanceof OverCountLimitException) {
                    MessageHelper.setReturnMessage(msg, req, ReturnCode.ERROR_MODULE, "查詢筆數超出系統限制，請縮小查詢範圍");
                } else {
                    MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "查詢失敗", me, req);
                }
            }
        } catch (Exception e) {
            log.error("查詢失敗", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "查詢失敗", e, req);
        }
        return resp;
    }

	
	
    /**
     * 新增
     * @param req
     * @return
     */
    public ResponseContext doInsert__TEMPLATE(RequestContext req) {
        try {

            Transaction.begin();
            try {

                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                throw e;
            }

            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "新增成功");

            try {

            } catch (DataNotFoundException e) {
                log.error("新增成功但查無資料", e);
                MessageHelper.setReturnMessage(msg, ReturnCode.OK, "新增成功但查無資料");
            } catch (Exception e) {
                log.error("新增成功但查詢失敗", e);
                MessageHelper.setReturnMessage(msg, ReturnCode.OK, "新增成功但查詢失敗");
            }

        } catch (ErrorInputException eie) {
            log.error("新增失敗", eie);
            //MessageUtil.setReturnMessage(msg, ReturnCode.ERROR_INPUT, "ZZXZ_0100_UI_001");
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error("新增失敗", me);
                //MessageUtil.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                //MessageUtil.setReturnMessage(msg, me, req, ReturnCode.ERROR_MODULE, "新增失敗");
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "新增失敗", me, req);
            }
        } catch (Exception e) {
            log.error("新增失敗", e);
            //MessageUtil.setReturnMessage(msg, e, req, ReturnCode.ERROR, "新增失敗");
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "新增失敗", e, req);
        }

        return resp;
    }
}
