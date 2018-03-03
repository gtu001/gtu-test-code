package gtu.log.finder.etc;

import java.lang.reflect.Field;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.commons.lang.reflect.MethodUtils;

import gtu.class_.ClassUtil;

public class DebugMointerUISpringWebHandler {

    private boolean useSpringMode = false;
    private ServletConfig servlet;

    private Class<?> ApplicationContext;
    private Class<?> WebApplicationContextUtils;
    private Class<?> customFetchSpringClass;

    public DebugMointerUISpringWebHandler(ServletConfig servlet, String customFetchSpringClass) {
        try {
            this.servlet = servlet;
            ApplicationContext = Class.forName("org.springframework.context.ApplicationContext");
            WebApplicationContextUtils = Class.forName("org.springframework.web.context.support.WebApplicationContextUtils");
            useSpringMode = true;
        } catch (Exception ex) {
        }
        
        if(StringUtils.isNotBlank(customFetchSpringClass)) {
            try {
                this.customFetchSpringClass = Class.forName(customFetchSpringClass);
            }catch(Exception ex) {
            }
        }
    }

    public Object getContext(StringBuilder sb) {
        if (!useSpringMode) {
            return null;
        }

        //預設取得springContext
        Object context = null;
        try {
            context = MethodUtils.invokeStaticMethod(WebApplicationContextUtils, "getWebApplicationContext", servlet.getServletContext());
        } catch (Exception ex) {
            DebugMointerUIHotServlet.logH("getContext [context] ERR : " + ex.getMessage(), sb);
        }
        
        //使用自訂取得springContext
        Object contextCustom = null;
        try {
            Object customBean = customFetchSpringClass.newInstance();
            contextCustom = MethodUtils.invokeMethod(customBean, "call", new Object[0]);
            if (contextCustom != null) {
                DebugMointerUIHotServlet.logH("取得spring context [Custom] : " + contextCustom, sb);
                return contextCustom;
            }
        } catch (Exception ex) {
            DebugMointerUIHotServlet.logH("getContext [Custom] ERR : " + ex.getMessage(), sb);
        }
        
        DebugMointerUIHotServlet.logH("取得spring context : " + context, sb);
        return context;
    }

    public Object getOrignBean(Class<?> orignClass, Object context, StringBuilder sb) {
        if (!useSpringMode) {
            return null;
        }

        try {
            return MethodUtils.invokeMethod(context, "getBean", orignClass);
        } catch (Exception ex) {
            DebugMointerUIHotServlet.exceptioinHandler(ex, sb);
        }
        return null;
    }

    private Object getBean(String beanName, Object context) {
        try {
            return MethodUtils.invokeMethod(context, "getBean", beanName);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private Integer getBeanDefinitionCount(Object context) {
        try {
            return (Integer) MethodUtils.invokeMethod(context, "getBeanDefinitionCount", new Object[0]);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private String[] getBeanDefinitionNames(Object context) {
        try {
            return (String[]) MethodUtils.invokeMethod(context, "getBeanDefinitionNames", new Object[0]);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new String[0];
        }
    }

    public <T> void replaceAllAutowired(Class<?> orignClass, Object newProxy, Object context, StringBuilder sb) {
        if (!useSpringMode) {
            return;
        }

        DebugMointerUIHotServlet.logH("Bean size : " + getBeanDefinitionCount(context), sb);
        Pattern cglibPattern = Pattern.compile("(.*)\\$\\$EnhancerByCGLIB\\$\\$\\w+");
        for (String name : getBeanDefinitionNames(context)) {
            Object bean = getBean(name, context);
            if (bean.getClass().getDeclaredFields() != null) {
                for (Field f : bean.getClass().getDeclaredFields()) {
                    if (ClassUtil.isAssignFrom(f.getType(), orignClass)) {
                        try {
                            Object orignBean = FieldUtils.readDeclaredField(bean, f.getName(), true);

                            String orignBeanName = "";
                            if (orignBean != null) {
                                orignBeanName = StringUtils.trimToEmpty(orignBean.getClass().getName());
                                Matcher mth = cglibPattern.matcher(orignBean.getClass().getName());
                                if (mth.find()) {
                                    orignBeanName = StringUtils.trimToEmpty(mth.group(1));
                                }
                            }

                            if (orignBean == null || StringUtils.equals(orignBeanName, orignClass.getName())) {
                                FieldUtils.writeDeclaredField(bean, f.getName(), newProxy, true);
                                DebugMointerUIHotServlet.logH("更換 " + (bean.getClass().getName() + "." + f.getName()) + " - " + //
                                        DebugMointerUIHotServlet.getRandomColorString("成功!") + " : " + newProxy, sb);
                            }
                        } catch (IllegalAccessException ex) {
                            DebugMointerUIHotServlet.logH("[ERROR]更換 " + bean.getClass().getName() + "." + f.getName() + " - " + //
                                    DebugMointerUIHotServlet.getRandomColorString("失敗!") + " : " + newProxy, sb);
                            DebugMointerUIHotServlet.exceptioinHandler(ex, sb);
                        }
                    }
                }
            }
        }
    }
}
