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
 * ${my.sysdate()}   1.0 Created     ±i¯Â·¶
 * µ{¦¡¥\¯à    ${model_chinese}§@·~
 * µ{¦¡¦WºÙ    ${main_action_clz}.java
 * §@·~¤è¦¡    ONLINE
 * ·§­n»¡©ú    (1) ¬d¸ß§@·~ ¢w  (2) ²M°£ ¢w ²M°£µe­±¤W¸ê®Æ (3) ·s¼W§@·~ ¢w¶W³sµ²¦Ü ${edit_action_clz}
 * «ö¶s±ÂÅv    µL
 * ¦hµ§¬d¸ß    (¥²¶·„Ñ¨ä¤¤¤@¶µ)   µL      
 *                              
 * <pre>
 * author
 * @since ${my.sysdate()}
 */
@SuppressWarnings("unchecked")
@TxBean
public class ${main_action_clz} extends UCBean {

    /** ÀRºAªº log ª«¥ó **/
    private static final Logger log = Logger.getLogger(${main_action_clz}.class);

    /** ¦¹ TxBean µ{¦¡½X¦@¥Îªº ResponseContext */
    private ResponseContext resp;

    /** ¦¹ TxBean µ{¦¡½X¦@¥Îªº ReturnMessage */
    private ReturnMessage msg;

    /** ¦¹ TxBean µ{¦¡½X¦@¥Îªº UserObject */
    private UserObject user;

    /** ÂÐ¼g¤÷Ãþ§Oªº start() ¥H±j¨î©ó¨C¦¸ Dispatcher ©I¥s method ®É³£°õ¦æµ{¦¡¦Û©wªºªì©l°Ê§@ **/
    public ResponseContext start(RequestContext req) throws TxException, ServiceException {
        super.start(req); //¤@©w­n invoke super.start() ¥H°õ¦æÅv­­ÀË®Ö
        initApp(req); //©I¥s¦Û©wªºªì©l°Ê§@
        return null;
    }

    /**
     * µ{¦¡¦Û©wªºªì©l°Ê§@¡A³q±`¬°¨ú¥X ResponseContext, UserObject, 
     * ¤Î³]©w ReturnMessage ¤Î response code.
     */
    private void initApp(RequestContext req) {
        // «Ø¥ß¦¹ TxBean ³q¥Îªºª«¥ó
        resp = this.newResponseContext();
        msg = new ReturnMessage();
        user = this.getUserObject(req);
        // ¥ý±N ReturnMessage ªº reference ¥[¨ì response context
        resp.addOutputData(IConstantMap.ErrMsg, msg);

        // ¦b Cathay ³q±`¥u¦³¤@­Ó page ¦b«e­± display¡A©Ò¥H¥i¥H¥ý³]©w
        resp.setResponseCode("success");
    }

    /**
     * ªì©l­¶­±
     * @param req
     * @return
     */
    @CallMethod(action = "prompt", url = "/XX/ZX/${main_action_clz}/${main_jsp}.jsp")
    public ResponseContext doPrompt(RequestContext req) {
		
        return resp;
    }

    /**
     * ¬d¸ß
     * @param req
     * @return
     */
    @CallMethod(action = "query", type = CallMethod.TYPE_AJAX)
    public ResponseContext doQuery(RequestContext req) {
        try {
			VOTool.setParamsFromLP_JSON(req);
            Map reqMap = VOTool.jsonToMap(req.getParameter("reqMap"));
			
            //Åª¨ú¸ê®ÆÀÉ
            List<Map> rtnList = new ${model_clz}().query(reqMap);
            resp.addOutputData("rtnList", rtnList);
			
            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "¬d¸ß¦¨¥\");
        } catch (ErrorInputException eie) {
            log.error("¬d¸ß¥¢±Ñ", eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (DataNotFoundException dnfe) {
            log.error("¬d¸ß¥¢±Ñ", dnfe);
            MessageHelper.setReturnMessage(msg, ReturnCode.DATA_NOT_FOUND, "¬dµL¸ê®Æ");
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error("¬d¸ß¥¢±Ñ", me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                if (me.getRootException() instanceof OverCountLimitException) {
                    MessageHelper.setReturnMessage(msg, req, ReturnCode.ERROR_MODULE, "¬d¸ßµ§¼Æ¶W¥X¨t²Î­­¨î¡A½ÐÁY¤p¬d¸ß½d³ò");
                } else {
                    MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "¬d¸ß¥¢±Ñ", me, req);
                }
            }
        } catch (Exception e) {
            log.error("¬d¸ß¥¢±Ñ", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "¬d¸ß¥¢±Ñ", e, req);
        }
        return resp;
    }

	
	
    /**
     * ·s¼W
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

            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "·s¼W¦¨¥\");

            try {

            } catch (DataNotFoundException e) {
                log.error("·s¼W¦¨¥\¦ý¬dµL¸ê®Æ", e);
                MessageHelper.setReturnMessage(msg, ReturnCode.OK, "·s¼W¦¨¥\¦ý¬dµL¸ê®Æ");
            } catch (Exception e) {
                log.error("·s¼W¦¨¥\¦ý¬d¸ß¥¢±Ñ", e);
                MessageHelper.setReturnMessage(msg, ReturnCode.OK, "·s¼W¦¨¥\¦ý¬d¸ß¥¢±Ñ");
            }

        } catch (ErrorInputException eie) {
            log.error("·s¼W¥¢±Ñ", eie);
            //MessageUtil.setReturnMessage(msg, ReturnCode.ERROR_INPUT, "ZZXZ_0100_UI_001");
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error("·s¼W¥¢±Ñ", me);
                //MessageUtil.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                //MessageUtil.setReturnMessage(msg, me, req, ReturnCode.ERROR_MODULE, "·s¼W¥¢±Ñ");
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "·s¼W¥¢±Ñ", me, req);
            }
        } catch (Exception e) {
            log.error("·s¼W¥¢±Ñ", e);
            //MessageUtil.setReturnMessage(msg, e, req, ReturnCode.ERROR, "·s¼W¥¢±Ñ");
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "·s¼W¥¢±Ñ", e, req);
        }

        return resp;
    }
}
