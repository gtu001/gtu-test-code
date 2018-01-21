package gtu.spring.aop.work;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.ThrowsAdvice;

public class LogAdvice implements ThrowsAdvice {
  //<bean id="logAdvice" class="com.sti.estore.util.LogAdvice" />
    
    private static final Log logger = LogFactory.getLog(LogAdvice.class);

    public void afterThrowing(Method method, Object[] args, Object target, Throwable exception) {
        logger.error("System Exception Log:" + exception.getMessage());
    }
}
