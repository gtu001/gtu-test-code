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
 * ${my.sysdate()}   1.0 Created     �i�·�
 *�{���\��    ${model_chinese}��ƺ��@
 *�{���W��    ${edit_action_clz}.java
 *�@�~�覡    ONLINE
 *���n����    (1) �s�W (2) �ק� (3) ���� (4) �f�� (5) �h�^ (6) �R��
 *���s���v    FUNC_ID = ZZZX0101
 *                              
 * <pre>
 * author
 * @since ${my.sysdate()}
 */
@SuppressWarnings("unchecked")
@TxBean
public class ${edit_action_clz} extends UCBean {

    /** �R�A�� log ���� **/
    private static final Logger log = Logger.getLogger(${edit_action_clz}.class);

    /** �� TxBean �{���X�@�Ϊ� ResponseContext */
    private ResponseContext resp;

    /** �� TxBean �{���X�@�Ϊ� ReturnMessage */
    private ReturnMessage msg;

    /** �� TxBean �{���X�@�Ϊ� UserObject */
    private UserObject user;
    
    /** ���q�O */
    private String DIV;

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
        
        // ���o���q�O
        DIV = user.getCOMP_ID();

        // �b Cathay �q�`�u���@�� page �b�e�� display�A�ҥH�i�H���]�w
        resp.setResponseCode("success");
    }

    /**
     * ��l����
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
                log.error("��l�d�ߨϥΪ̸�T����", e);
                MessageHelper.setReturnMessage(msg, ReturnCode.OK, "��l�d�ߨϥΪ̸�T����");
            }
        } catch (Exception e) {
            log.error("��l����", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "��l����");
        }
        return resp;
    }

    /**
     * �s�W
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
            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "�s�W����");
            reqMap.put("ACTION_TYPE", "U"); // ����ACTION_TYPE = ��U�����ʧ@
            try {
                query(reqMap, the${model_clz}); // �����l�d��
            } catch (Exception e) {
                log.error("�s�W���������d����", e);
               MessageHelper.setReturnMessage(msg, ReturnCode.OK, "�s�W���������d����");
            }
        } catch (ErrorInputException eie) {
            log.error("�s�W����", eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (DataNotFoundException dnfe) {
            log.error("�s�W����", dnfe);
            MessageHelper.setReturnMessage(msg, ReturnCode.DATA_NOT_FOUND, "�d�L���");
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error("�s�W����", me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "�s�W����", me, req);
            }
        } catch (Exception e) {
            log.error("�s�W����", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "�s�W����", e, req);
        }
        return resp;
    }

    /**
     * �ק�
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
            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "�ק粒��");
            reqMap.put("ACTION_TYPE", "U");
            try {
                query(reqMap, the${model_clz}); // �����l�d��
            } catch (Exception e) {
                log.error("�ק粒�������d����", e);
                MessageHelper.setReturnMessage(msg, ReturnCode.OK, "�ק粒�������d����");
            }
        } catch (ErrorInputException eie) {
            log.error("�ק異��", eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (DataNotFoundException dnfe) {
            log.error("�ק異��", dnfe);
            MessageHelper.setReturnMessage(msg, ReturnCode.DATA_NOT_FOUND, "�d�L���");
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error("�ק異��", me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "�ק異��", me, req);
            }
        } catch (Exception e) {
            log.error("�ק異��", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "�ק異��", e, req);
        }
        return resp;
    }

    /**
     * �R��
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
            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "�����R��");
        } catch (ErrorInputException eie) {
            log.error("�R������", eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (DataNotFoundException dnfe) {
            log.error("�R������", dnfe);
            MessageHelper.setReturnMessage(msg, ReturnCode.DATA_NOT_FOUND, "�d�L���");
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error("�R������", me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "�R������", me, req);
            }
        } catch (Exception e) {
            log.error("�R������", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "�R������", e, req);
        }
        return resp;
    }

    /**
     * ����
     * @param req
     * @return
     */
    @SuppressWarnings("deprecation")
    @CallMethod(action = "submit", type = CallMethod.TYPE_AJAX)
    public ResponseContext doSubmit(RequestContext req) {
        try {
            Map reqMap = VOTool.jsonToMap(req.getParameter("reqMap"));
            String FLOW_NO = MapUtils.getString(reqMap, "FLOW_NO");
            DTXXTP01 DTXXTP01vo = VOTool.mapToVO(DTXXTP01.class, reqMap); // �NreqMap�নvo

            ${model_clz} the${model_clz} = new ${model_clz}();
            Transaction.begin();
            try {
                new RZ_N0Z001().approveFlow(FLOW_NO, "����", "", user.getEmpID(), user.getDivNo());
                the${model_clz}.confirm(DTXXTP01vo); // ��s�f��y�{�ܫݼf�� 20
                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                throw e;
            }
            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "���槹��");
            reqMap.put("ACTION_TYPE", "U");
            try {
                query(reqMap, the${model_clz}); // ���s�����l�ʧ@
            } catch (Exception e) {
                log.error("���槹�������d����", e);
                MessageHelper.setReturnMessage(msg, ReturnCode.OK, "���槹�������d����");
            }
      
        } catch (ErrorInputException eie) {
            log.error("���楢��", eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (DataNotFoundException dnfe) {
            log.error("���楢��", dnfe);
            MessageHelper.setReturnMessage(msg, ReturnCode.DATA_NOT_FOUND, "�d�L���");
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error("���楢��", me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "���楢��", me, req);
            }
        } catch (Exception e) {
            log.error("���楢��", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "���楢��", e, req);
        }
        return resp;
    }

    /**
     * �f��
     * @param req
     * @return
     */
    @SuppressWarnings("deprecation")
    @CallMethod(action = "approve", type = CallMethod.TYPE_AJAX)
    public ResponseContext doApprove(RequestContext req) {
        try {
            Map reqMap = VOTool.jsonToMap(req.getParameter("reqMap"));
            String FLOW_NO = MapUtils.getString(reqMap, "FLOW_NO");
            DTXXTP01 DTXXTP01vo = VOTool.mapToVO(DTXXTP01.class, reqMap); // �NreqMap�নvo

            ${model_clz} the${model_clz} = new ${model_clz}();
            Transaction.begin();
            try {
                new RZ_N0Z001().approveFlow(FLOW_NO, "�f��", "", user.getEmpID(), user.getDivNo());
                the${model_clz}.approve(DTXXTP01vo); // ��s�f��y�{�ܫݼf�� 30
                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                throw e;
            }
            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "�f�֧���");
            reqMap.put("ACTION_TYPE", "U");
            try {
                query(reqMap, the${model_clz}); // ���s�����l�ʧ@
            } catch (Exception e) {
                log.error("�f�֧��������d����", e);
                MessageHelper.setReturnMessage(msg, ReturnCode.OK, "�f�֧��������d����");
            }

        } catch (ErrorInputException eie) {
            log.error("�f�֥���", eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (DataNotFoundException dnfe) {
            log.error("�f�֥���", dnfe);
            MessageHelper.setReturnMessage(msg, ReturnCode.DATA_NOT_FOUND, "�d�L���");
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error("�f�֥���", me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "�f�֥���", me, req);
            }
        } catch (Exception e) {
            log.error("�f�֥���", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "�f�֥���", e, req);
        }
        return resp;
    }

    /**
     * �h�^
     * @param req
     * @return
     */
    @SuppressWarnings("deprecation")
    @CallMethod(action = "reject", type = CallMethod.TYPE_AJAX)
    public ResponseContext doReject(RequestContext req) {
        try {
            Map reqMap = VOTool.jsonToMap(req.getParameter("reqMap"));
            String FLOW_NO = MapUtils.getString(reqMap, "FLOW_NO");
            DTXXTP01 DTXXTP01vo = VOTool.mapToVO(DTXXTP01.class, reqMap); // �NreqMap�নvo

            ${model_clz} the${model_clz} = new ${model_clz}();
            Transaction.begin();
            try {
                new RZ_N0Z001().rejectFlow(FLOW_NO, "�h�^", "", user.getEmpID(), user.getDivNo());
                the${model_clz}.reject(DTXXTP01vo); // ��s�f��y�{�ܫݼf�� 10
                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                throw e;
            }
            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "�h�^����");
            reqMap.put("ACTION_TYPE", "U");
            try {
                query(reqMap, the${model_clz}); // ���s�����l�ʧ@
            } catch (Exception e) {
                log.error("�h�^���������d����", e);
                MessageHelper.setReturnMessage(msg, ReturnCode.OK, "�h�^���������d����");
            }
        
        } catch (ErrorInputException eie) {
            log.error("�h�^����", eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (DataNotFoundException dnfe) {
            log.error("�h�^����", dnfe);
            MessageHelper.setReturnMessage(msg, ReturnCode.DATA_NOT_FOUND, "�d�L���");
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error("�h�^����", me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "�h�^����", me, req);
            }
        } catch (Exception e) {
            log.error("�h�^����", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "�h�^����", e, req);
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
     * ����W��
     * @param req
     * @return
     */
    @CallMethod(action = "upload", name = CallMethod.TYPE_NA)
    public ResponseContext doUpload(RequestContext req) {
        try {
            // ���o�W�Ǹ��|���ɮ׸��
            FileItem inputFile = FileStoreUtil.parseUploadStream(req); //�פJ�ɮ�

            Map reqMap = getFileUploadReqMap(req);
            resp.addOutputData("updateMap", VOTool.toJSON(reqMap));

            ${model_clz} the${model_clz} = new ${model_clz}();
            Transaction.begin();
            try {
                new RZ_N0Z001().upload(FLOW_NO, "�W��", "", user.getEmpID(), user.getDivNo());
                the${model_clz}.upload(DTXXTP01vo); // ��s�f��y�{�ܫݼf�� 10
                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                throw e;
            }
        } catch (ErrorInputException eie) {
            log.error("�W�ǥ���", eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (DataNotFoundException dnfe) {
            log.error("�W�ǥ���", dnfe);
            MessageHelper.setReturnMessage(msg, ReturnCode.DATA_NOT_FOUND, "�d�L���");
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error("�W�ǥ���", me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "�W�ǥ���", me, req);
            }
        } catch (Exception e) {
            log.error("�W�ǥ���", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "�W�ǥ���", e, req);
        } finally {
            try {
                Map map = new HashMap();
                map.put(IConstantMap.ErrMsg, msg);
                EncodingHelper.send2iframe(req, map, msg);
            } catch (Exception e) {
                log.error("�W�ǥ���", e);
            }
        }
        return resp;
    }

    /**
     * �ھ�ActionType��������{���X
     * @param map
     * @throws Exception 
     */
    private void query(Map map, ${model_clz} the${model_clz}) throws Exception {
        query(map, the${model_clz}, false);
    }

    private void query(Map map, ${model_clz} the${model_clz}, boolean isPrompt) throws Exception {
        String ACTION_TYPE = MapUtils.getString(map, "ACTION_TYPE");
        if(StringUtils.isBlank(ACTION_TYPE)){
            map.put("OP_STATUS_NM", "��J��");
            map.put("ACTION_TYPE", "I");
            ACTION_TYPE= "I";
        }
        if ("I".equals(ACTION_TYPE)) {
            String UPDT_DATE = DATE.currentTime().toString();
            map.put("UPDT_DATE", UPDT_DATE.substring(0, UPDT_DATE.length() - 4));
            map.put("UPDT_NM", user.getEmpName());
            resp.addOutputData("rtnMap", VOTool.toJSON(map)); // �ഫ��json���e�ݪ� jstl c:out �ϥ�
        } else {
            Map rtnMap = theXX_ZX0100.query(map).get(0);
            if (isPrompt) {
                String UPDT_DATE = MapUtils.getString(rtnMap, "UPDT_DATE", ""); // ���XUPDT_DATE,�Y�L�ȫh�ɪŦr��
                rtnMap.remove("UPDT_DATE");
                rtnMap.put("UPDT_DATE", UPDT_DATE.substring(0, UPDT_DATE.length() - 4));
                resp.addOutputData("rtnMap", VOTool.toJSON(rtnMap)); // �ഫ��json���e�ݪ� jstl c:out �ϥ�
            } else {
                String UPDT_DATE = MapUtils.getString(rtnMap, "UPDT_DATE", ""); // ���XUPDT_DATE,�Y�L�ȫh�ɪŦr��
                rtnMap.remove("UPDT_DATE");
                rtnMap.put("UPDT_DATE", UPDT_DATE.substring(0, UPDT_DATE.length() - 4));
                resp.addOutputData("rtnMap", rtnMap); // �����ǻ����󵹫e�ݪ�Ajax�ϥ�
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
            resp.addOutputData("FLOW_NO", rtnMap.get("FLOW_NO")); // ���o�f��y����
            resp.addOutputData("OP_STATUS", rtnMap.get("OP_STATUS")); // ���o���A
            resp.addOutputData("OP_STATUS_NM", rtnMap.get("OP_STATUS_NM")); // ���o���A����
            */
            
        }

    }

}
