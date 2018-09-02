package com.gtu.exception;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;

import com.gtu.util.ExceptionStackUtil;

public class LocalExceptionHandler extends ExceptionHandler {

    private static final Logger logger = Logger.getLogger(LocalExceptionHandler.class);

    @Override
    public ActionForward execute(Exception ex, ExceptionConfig ae, ActionMapping mapping, ActionForm formInstance, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        logger.error(ex.getMessage(), ex);
        ExceptionStackUtil.parse(getClass(), ex, request);
        return super.execute(ex, ae, mapping, formInstance, request, response);
    }
}