package gtu.log.finder.etc;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.commons.lang.reflect.MethodUtils;

import gtu.class_.ClassUtil;

public class DebugSpringWebHandler {

    private boolean useSpringMode = false;
    private ServletConfig servlet;

    private Class<?> ApplicationContext;
    private Class<?> WebApplicationContextUtils;

    public DebugSpringWebHandler(ServletConfig servlet) {
        try {
            this.servlet = servlet;
            ApplicationContext = Class.forName("org.springframework.context.ApplicationContext");
            WebApplicationContextUtils = Class.forName("org.springframework.web.context.support.WebApplicationContextUtils");
            useSpringMode = true;
        } catch (Exception ex) {
        }
    }

    public Object getContext(StringBuilder sb) {
        if (!useSpringMode) {
            return null;
        }

        Object context = null;
        try {
            context = MethodUtils.invokeStaticMethod(WebApplicationContextUtils, "getWebApplicationContext", servlet.getServletContext());
        } catch (Exception ex) {
            DebugMointerUIHotServlet.exceptioinHandler(ex, sb);
        }
        Object context2 = null;
        try {
            Class<?> clz = Class.forName("com.fuco.mb.bill.controller.StaticContextAccessor");
            Object val1 = FieldUtils.readDeclaredStaticField(clz, "instance", true);
            context2 = FieldUtils.readDeclaredField(val1, "applicationContext", true);
        } catch (Exception ex) {
            DebugMointerUIHotServlet.exceptioinHandler(ex, sb);
        }
        if (context2 != null) {
            DebugMointerUIHotServlet.logH("取得spring context2 : " + context2, sb);
            return context2;
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
                                DebugMointerUIHotServlet.logH("更換 " + DebugMointerUIHotServlet.getRandomColorString(bean.getClass().getName() + "." + f.getName()) + " - 成功! : " + newProxy, sb);
                            }
                        } catch (IllegalAccessException ex) {
                            DebugMointerUIHotServlet.logH("[ERROR]更換 " + bean.getClass().getName() + "." + f.getName() + " - 失敗!", sb);
                            DebugMointerUIHotServlet.exceptioinHandler(ex, sb);
                        }
                    }
                }
            }
        }
    }
}
