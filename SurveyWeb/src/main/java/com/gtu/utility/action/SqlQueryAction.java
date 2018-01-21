package com.gtu.utility.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
//import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.hibernate.SessionFactory;
import org.springframework.web.struts.DispatchActionSupport;

import com.gtu.util.ActionMessageUtils;
import com.gtu.util.ExceptionStackUtil;

public class SqlQueryAction extends DispatchActionSupport {

    private static final Logger logger = Logger.getLogger(SqlQueryAction.class);

    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    public ActionForward test(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaActionForm dynaForm = (DynaActionForm) form;

        logger.info("dynaForm = " + dynaForm);
        
        String textField = (String) dynaForm.get("textField");
        
        logger.info("textField = " + textField);
        
        textField = StringUtils.defaultString(textField);
        
        return mapping.findForward("success");
    }
}