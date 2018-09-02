package gtu.spring.aop.work;

import gtu.file.FileUtil;
import gtu.log.finder.DebugMointerUI;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.sf.cglib.proxy.MethodProxy;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class GTU_AopAspect {

    private static final Log log = LogFactory.getLog(GTU_AopAspect.class);
    private static final File CONFIG_FILE = new File(FileUtil.DESKTOP_DIR, "useDebugUI.properties");
    public static final String LOOP_KEY = "loop_again";
    private static long fileModifySize = -1; 
    private static Properties configProp = new Properties();
    
    //@Pointcut("execution(* com.sti..*Action.*(..))")
    @Pointcut("execution(* *(..)) && " +//
    		"(within(com.sti..*Impl) || within(com.sti..*Action)) && " +//
    		"!within(*..*CGLIB*)")
    public void actionPointcut() {
    }

    @Around("execution(* *(..)) && !within(*..*CGLIB*) && !within(org.springframework..*)")
    public Object joinPointAccess(ProceedingJoinPoint pjp) throws Throwable {
//        String key = pjp.getTarget().getClass().getSimpleName();
//        boolean findOk = validateProperties_checkHasOn(key);
//        log.debug("## AOP check --->" + findOk + "///" + pjp.getTarget().getClass().getName());
        
        Properties prop = getPropertiesIfChange();
        String methodName = getMethodName(pjp);
        if(methodName!=null && prop.containsKey(methodName)){
            return useDebugMointerUI(pjp);
        }else if(methodName == null){
            log.debug("####### methodName = null --> \n" + ReflectionToStringBuilder.toString(pjp, ToStringStyle.MULTI_LINE_STYLE));
        }
        Object returnValue = null;
        do{
            try{
                return pjp.proceed(pjp.getArgs());
            }catch(Throwable ex){
                log.error("[ERROR]joinPointAccess:"+ ex.getMessage(), ex);
                returnValue = useDebugMointerUI(pjp);
            }
        }while(prop.containsKey(LOOP_KEY));
        return returnValue;
    }
    
    private Object useDebugMointerUI(ProceedingJoinPoint pjp){
        String methodName = getMethodName(pjp);
        Map<String, String> classInfo = new HashMap<String, String>();
        if (StringUtils.isNotBlank(methodName)) {
            classInfo.put("executeMethodName", methodName);
        }
        for(StackTraceElement e : Thread.currentThread().getStackTrace()){
            log.debug("##\t" + e);
        }
        return DebugMointerUI.startWith(classInfo, pjp.getThis(), pjp.getArgs(), pjp, getPropertiesIfChange());
    }
    
    private String getMethodName(ProceedingJoinPoint pjp){
        for(Field f : pjp.getClass().getDeclaredFields()){
            f.setAccessible(true);
            try {
                Object findObj = f.get(pjp);
                if(findObj.getClass().getName().contains("CglibMethodInvocation")){
                    Field f3 = FieldUtils.getDeclaredField(findObj.getClass(), "methodProxy", true);
                    MethodProxy proxy = (MethodProxy)f3.get(findObj);
//                    log.debug("proxy1 = " + ReflectionToStringBuilder.toString(proxy));
//                    log.debug("proxy2 = " + ReflectionToStringBuilder.toString(proxy.getSignature()));
                    return proxy.getSignature().getName();
                }
                if(findObj!=null){
                    for(Method m : findObj.getClass().getDeclaredMethods()){
                        if(m.getParameterTypes().length == 0){
                            if(m.getName().equals("getName") && m.getReturnType() == String.class){
                                m.setAccessible(true);
                                return (String)m.invoke(findObj);
                            }
                            if(m.getName().equals("getMethod") && m.getReturnType() == Method.class){
                                m.setAccessible(true);
                                return ((Method)m.invoke(findObj)).getName();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.debug("error : " + e.getMessage());
            }
        }
        return null;
    }
    
    public static Properties getPropertiesIfChange(){
        try{
            boolean updateProp = false;
            if(CONFIG_FILE.exists()){
                if(CONFIG_FILE.length()!=fileModifySize){
                    fileModifySize = CONFIG_FILE.length();
                    updateProp = true;
                }
            }else{
                CONFIG_FILE.createNewFile();
            }
            Properties prop = null;
            if(updateProp){
                prop = new Properties();
                prop.load(new FileInputStream(CONFIG_FILE));
                configProp = prop;
            }else{
                prop = configProp;
            }
            return prop;
        }catch(Exception ex){
            log.error("properties 無法取得", ex);
        }
        return System.getProperties();
    }

    private void showInfo(ProceedingJoinPoint pjp){
        log.fatal("\t args : " + Arrays.toString(pjp.getArgs()));
        log.fatal("\t signature : " + ReflectionToStringBuilder.toString(pjp.getSignature(), ToStringStyle.MULTI_LINE_STYLE));
        log.fatal("\t target : " + pjp.getTarget().getClass());
        log.fatal("\t kind : " + pjp.getKind());
        log.fatal("\t source location : " + ReflectionToStringBuilder.toString(pjp.getSourceLocation(), ToStringStyle.MULTI_LINE_STYLE));
        log.fatal("\t static part : " + ReflectionToStringBuilder.toString(pjp.getStaticPart(), ToStringStyle.MULTI_LINE_STYLE));
//        log.fatal("\t this : " + pjp.getThis());//會導致無限地回
    }
}