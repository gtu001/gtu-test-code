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
 * ${my.sysdate()}   1.0 Created     �i�·�
 * �{���\��    ${model_chinese}�@�~
 * �{���W��    ${main_action_clz}.java
 * �@�~�覡    ONLINE
 * ���n����    (1) �d�ߧ@�~ �w  (2) �M�� �w �M���e���W��� (3) �s�W�@�~ �w�W�s���� ${edit_action_clz}
 * ���s���v    �L
 * �h���d��    (�����Ѩ䤤�@��)   �L      
 *                              
 * <pre>
 * author
 * @since ${my.sysdate()}
 */
@SuppressWarnings("unchecked")
@TxBean
public class ${main_action_clz} extends UCBean {

    /** �R�A�� log ���� **/
    private static final Logger log = Logger.getLogger(${main_action_clz}.class);

    /** �� TxBean �{���X�@�Ϊ� ResponseContext */
    private ResponseContext resp;

    /** �� TxBean �{���X�@�Ϊ� ReturnMessage */
    private ReturnMessage msg;

    /** �� TxBean �{���X�@�Ϊ� UserObject */
    private UserObject user;

    /** �мg�����O�� start() �H�j���C�� Dispatcher �I�s method �ɳ�����{���۩w����l�ʧ@ **/
    public ResponseContext start(RequestContext req) throws TxException, ServiceException {
        super.start(req); //�@�w�n invoke super.start() �H�����v���ˮ�
        initApp(req); //�I�s�۩w����l�ʧ@
        return null;
    }

    /**
     * �{���۩w����l�ʧ@�A�q�`�����X ResponseContext, UserObject, 
     * �γ]�w ReturnMessage �� response code.
     */
    private void initApp(RequestContext req) {
        // �إߦ� TxBean �q�Ϊ�����
        resp = this.newResponseContext();
        msg = new ReturnMessage();
        user = this.getUserObject(req);
        // ���N ReturnMessage �� reference �[�� response context
        resp.addOutputData(IConstantMap.ErrMsg, msg);

        // �b Cathay �q�`�u���@�� page �b�e�� display�A�ҥH�i�H���]�w
        resp.setResponseCode("success");
    }

    /**
     * ��l����
     * @param req
     * @return
     */
    @CallMethod(action = "prompt", url = "/XX/ZX/${main_action_clz}/${main_jsp}.jsp")
    public ResponseContext doPrompt(RequestContext req) {
		
        return resp;
    }

    /**
     * �d��
     * @param req
     * @return
     */
    @CallMethod(action = "query", type = CallMethod.TYPE_AJAX)
    public ResponseContext doQuery(RequestContext req) {
        try {
			VOTool.setParamsFromLP_JSON(req);
            Map reqMap = VOTool.jsonToMap(req.getParameter("reqMap"));
			
            //Ū�������
            List<Map> rtnList = new ${model_clz}().query(reqMap);
            resp.addOutputData("rtnList", rtnList);
			
            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "�d�ߦ��\");
        } catch (ErrorInputException eie) {
            log.error("�d�ߥ���", eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (DataNotFoundException dnfe) {
            log.error("�d�ߥ���", dnfe);
            MessageHelper.setReturnMessage(msg, ReturnCode.DATA_NOT_FOUND, "�d�L���");
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error("�d�ߥ���", me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                if (me.getRootException() instanceof OverCountLimitException) {
                    MessageHelper.setReturnMessage(msg, req, ReturnCode.ERROR_MODULE, "�d�ߵ��ƶW�X�t�έ���A���Y�p�d�߽d��");
                } else {
                    MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "�d�ߥ���", me, req);
                }
            }
        } catch (Exception e) {
            log.error("�d�ߥ���", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "�d�ߥ���", e, req);
        }
        return resp;
    }

	
	
    /**
     * �s�W
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

            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "�s�W���\");

            try {

            } catch (DataNotFoundException e) {
                log.error("�s�W���\���d�L���", e);
                MessageHelper.setReturnMessage(msg, ReturnCode.OK, "�s�W���\���d�L���");
            } catch (Exception e) {
                log.error("�s�W���\���d�ߥ���", e);
                MessageHelper.setReturnMessage(msg, ReturnCode.OK, "�s�W���\���d�ߥ���");
            }

        } catch (ErrorInputException eie) {
            log.error("�s�W����", eie);
            //MessageUtil.setReturnMessage(msg, ReturnCode.ERROR_INPUT, "ZZXZ_0100_UI_001");
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error("�s�W����", me);
                //MessageUtil.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                //MessageUtil.setReturnMessage(msg, me, req, ReturnCode.ERROR_MODULE, "�s�W����");
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "�s�W����", me, req);
            }
        } catch (Exception e) {
            log.error("�s�W����", e);
            //MessageUtil.setReturnMessage(msg, e, req, ReturnCode.ERROR, "�s�W����");
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "�s�W����", e, req);
        }

        return resp;
    }
}
