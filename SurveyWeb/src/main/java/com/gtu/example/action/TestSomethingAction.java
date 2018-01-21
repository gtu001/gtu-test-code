package com.gtu.example.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
//import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.springframework.web.struts.DispatchActionSupport;

public class TestSomethingAction extends DispatchActionSupport {

    private static final Logger logger = Logger.getLogger(TestSomethingAction.class);

    public ActionForward testShowMessage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("# -- testShowMessage");
        DynaActionForm dynaForm = (DynaActionForm) form;
        
        ActionMessages messages = new ActionMessages();
        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("test001"));
        saveMessages(request, messages);
        
        return mapping.findForward("test_index");
    }
    
    public ActionForward testErrorMessage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("# -- testErrorMessage");
        DynaActionForm dynaForm = (DynaActionForm) form;
        
        ActionMessages messages = new ActionMessages();
        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("exception.message", new Object[] {"測試錯誤訊息"}));
        saveErrors(request, messages);
        
        return mapping.findForward("test_index");
    }
}