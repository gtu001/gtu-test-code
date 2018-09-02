package com.gtu.example.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
//import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.springframework.web.struts.DispatchActionSupport;

public class TestSomething2Action extends DispatchActionSupport {

    private static final Logger logger = Logger.getLogger(TestSomething2Action.class);

    public ActionForward testBtn1(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("# -- testBtn1");
        DynaActionForm mapForm = (DynaActionForm) form;
        logger.info("test ok");
        return mapping.findForward("test_index2");
    }
}