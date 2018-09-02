package com.gtu.sysutil.aop;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.appfuse.webapp.util.FacesUtils;
import org.aspectj.lang.ProceedingJoinPoint;

public class LoggingHandler {
    private Logger logger = Logger.getLogger(LoggingHandler.class);
    
    /**
     * Default error message
     */
    public static final String internalErrorMessage = "And internal server error has happened, please contact our support for more details.";

    /**
     * Handles any exceptions thrown from the managed beans
     * @throws Throwable 
     */
    public Object handle(ProceedingJoinPoint pjp) throws Throwable {
        try {
            return pjp.proceed();
        } catch (Throwable e) {
            String message = StringUtils.defaultString(e.getMessage());
            if (!message.isEmpty()) {
                message = " [" + message + "]";
            }
            FacesUtils.addErrorMessage(internalErrorMessage + message); 
            logger.error("##ERR## " + message, e);
            throw e;
        }
    }
}