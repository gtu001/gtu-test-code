package gtu.spring.aop.work;

import java.io.IOException;
import java.sql.SQLException;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LazyInitializationException;
import org.springframework.jdbc.UncategorizedSQLException;

/**
 * 資料存取Advice。
 * 
 * @version $Id: RetryAccessAdvice.java,v 1.1, 2013-01-28 10:16:47Z, Evan Tung$
 */
public class RetryAccessAdvice implements MethodInterceptor {
    
    //<bean id="retryAccessAdvice" class="com.sti.estore.interceptor.RetryAccessAdvice" />

    /** 資料存取Advice記錄器。 */
    private static final Log logger = LogFactory.getLog(RetryAccessAdvice.class);
    private final static long RETRY_INTERVAL_MILLISECOND = 1000;
    /** 重試次數值。 */
    private final static int RETRY_FREQUENCY = 3;

    /**
     * 若為預期重試的例外發生，則允許重試，重試次數{@link #RETRY_KPI}。
     * 
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object resultObject = null;
        for (int i = 0; i < RETRY_FREQUENCY; i++) {
            try {
                resultObject = invocation.proceed();
                break;
            } catch (LazyInitializationException lazyInitializationException) {
                logger.warn(lazyInitializationException.getMessage(), lazyInitializationException);
                try {
                    Thread.sleep(RETRY_INTERVAL_MILLISECOND);
                } catch (Exception e) {
                    logger.warn(e.getMessage(), e);
                }
                /*
                 * }catch(GenericJDBCException genericJDBCException){
                 * logger.warn
                 * (genericJDBCException.getMessage(),genericJDBCException);
                 * try{
                 * Thread.sleep(RetryAccessConstants.RETRY_INTERVAL_MILLISECOND
                 * ); }catch(Exception e){ logger.warn(e.getMessage(),e); }
                 */
            } catch (UncategorizedSQLException uncategorizedSQLException) {
                logger.warn(uncategorizedSQLException.getMessage(), uncategorizedSQLException);
                SQLException sqlException = uncategorizedSQLException.getSQLException();
                if (sqlException != null && sqlException.getErrorCode() == 8103) {
                    try {
                        Thread.sleep(RETRY_INTERVAL_MILLISECOND);
                    } catch (Exception e) {
                        logger.warn(e.getMessage(), e);
                    }
                }
            } catch (IOException ioException) {
                logger.warn(ioException.getMessage(), ioException);
                try {
                    Thread.sleep(RETRY_INTERVAL_MILLISECOND);
                } catch (Exception e) {
                    logger.warn(e.getMessage(), e);
                }
            } catch (Throwable throwable) {
                throw throwable;
            }
        }
        return resultObject;
    }
}
