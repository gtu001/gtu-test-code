package com.gtu.sysutil.exc;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.ViewHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import org.apache.log4j.Logger;
import org.appfuse.webapp.util.FacesUtils;

public class CustomExceptionHandler extends ExceptionHandlerWrapper {

    private static final Logger logger = Logger.getLogger(CustomExceptionHandler.class);
    private final ExceptionHandler wrapped;

    public CustomExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return this.wrapped;
    }

    public void handle() throws FacesException {
        final Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator();

        StringWriter exceptionWriter = new StringWriter();
        
        while (i.hasNext()) {
            ExceptionQueuedEvent event = i.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
            // get the exception from context
            Throwable t = context.getException();
            final FacesContext fc = FacesContext.getCurrentInstance();
            final ExternalContext externalContext = fc.getExternalContext();
            final Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();
            final ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) fc.getApplication().getNavigationHandler();
            // here you do what ever you want with exception
            try {
                // log error ?
                logger.error("Severe Exception Occured", t);
                // log.log(Level.SEVERE, "Critical Exception!", t);
                // redirect error page
                requestMap.put("exceptionMessage", t.getMessage());
                nav.performNavigation("/pages/common/error.xhtml");
                fc.renderResponse();
                // remove the comment below if you want to report the error in a
                // jsf error message
                // JsfUtil.addErrorMessage(t.getMessage());
                
                putExceptionMessage(exceptionWriter, t);
                
                redirectErrorPage();
            } finally {
                // remove it from queue
                i.remove();
            }
        }
        
        FacesUtils.getRequest().setAttribute("exceptionMessage", exceptionWriter.toString());
        
        // parent hanle
        getWrapped().handle();
    }
    
    private void putExceptionMessage(StringWriter sw, Throwable ge){
        PrintWriter pw = new PrintWriter(sw);
        ge.printStackTrace(pw);
        Throwable parentThrowEx = ge;
        while ((parentThrowEx = parentThrowEx.getCause()) != null) {
            parentThrowEx.printStackTrace(pw);
        }
    }

    private void redirectErrorPage(){
        FacesContext fc = FacesContext.getCurrentInstance();
        String viewId = "/pages/common/error.xhtml";
        ViewHandler viewHandler = fc.getApplication().getViewHandler();
        fc.setViewRoot(viewHandler.createView(fc, viewId));
        fc.getPartialViewContext().setRenderAll(true);
        fc.renderResponse();
    }
}