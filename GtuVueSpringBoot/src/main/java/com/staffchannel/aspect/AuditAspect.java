/**
 * 
 */
package com.staffchannel.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.staffchannel.UserDetails.CurrentUser;

/**
 * @author Walalala
 *
 */
@Aspect
public class AuditAspect {
 
    private static Logger logger = LoggerFactory.getLogger(AuditAspect.class);

    @AfterReturning(pointcut = "execution(* com.staffchannel.security.SecurityAuthenticationSuccessHandler.onAuthenticationSuccess(..))", returning = "result")
    public void after(JoinPoint joinPoint, Object result) throws Throwable {
    	logger.info(">>> user: " + getPrincipal());
    }

    private String getPrincipal(){
		String userName = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			userName = ((UserDetails)principal).getUsername();
		} else {
			userName = principal.toString();
		}
		return userName;
	}
}
