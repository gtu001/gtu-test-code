package com.iisigroup.ris;

import gtu.collection.ListUtil;
import gtu.reflect.ToStringUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.web.context.WebApplicationContext;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.util.StatusPrinter;

public class InitServlet extends HttpServlet {

    private static Logger log = LoggerFactory.getLogger(InitServlet.class);

    private static final long serialVersionUID = 1L;

    private ServletWatcherUI servletWaterUI;

    private static final String SERVER_MODE_KEY = "showMeTheMoney";

    public void init() throws ServletException {
        log.debug("###########################################################");
        log.debug("# InitServlet = doPost start ...");
        log.debug("");
        log.debug("");
        log.debug("# getServletConfig().getInitParameterNames() ==============");
        for (Enumeration<?> enu = this.getServletConfig().getInitParameterNames(); enu.hasMoreElements();) {
            Object key = enu.nextElement();
            Object value = this.getServletConfig().getInitParameter((String) key);
            log.debug("\tkey:[{}],value:[{}]", new Object[] { key, value });
        }
        log.debug("");
        log.debug("# getServletContext().getInitParameterNames() ==============");
        for (Enumeration<?> enu = this.getServletContext().getInitParameterNames(); enu.hasMoreElements();) {
            Object key = enu.nextElement();
            Object value = this.getServletContext().getInitParameter((String) key);
            log.debug("\tkey:[{}],value:[{}]", new Object[] { key, value });
        }
        log.debug("");
        log.debug("# getServletContext().getAttributeNames() ==============");
        for (Enumeration<?> enu = this.getServletContext().getAttributeNames(); enu.hasMoreElements();) {
            Object key = enu.nextElement();
            Object value = this.getServletConfig().getInitParameter((String) key);
            log.debug("\tkey:[{}],value:[{}]", new Object[] { key, value });
        }
        log.debug("");
        log.debug("");
        String debug = this.getInitParameter("debug");
        log.debug("debug = [" + debug + "]");
        this.getServletContext().setAttribute(SERVER_MODE_KEY, false);
        if (StringUtils.isNotEmpty(debug)) {
            boolean bool = Boolean.parseBoolean(debug);
            this.getServletContext().setAttribute(SERVER_MODE_KEY, bool);
            log.debug("debug is't empty -----> set attribute '" + SERVER_MODE_KEY + "' = " + bool);
        }
        // resetSlf4jConfig(null);
        // sl4jShowInfo();
        log.debug("# InitServlet = doPost end ...");
        log.debug("###########################################################");
    }

    StringBuilder message = new StringBuilder();

    HttpServletRequest request;
    HttpServletResponse response;

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug(Thread.currentThread().toString());
        String action = request.getParameter("do");
        if (StringUtils.isEmpty(action)) {
            // log.debug(String.format("/init?do=???? [%s, %s]",
            // SERVER_MODE_KEY, "swing"));
            return;
        }

        if (action.equals(SERVER_MODE_KEY)) {
            String value = request.getParameter("val");
            if (StringUtils.isNotEmpty(value)) {
                this.setServerMode(Boolean.parseBoolean(value), request, response);
            } else {
                log.debug("?val=[true|false]");
            }
        }

        if (action.equals("swing")) {
            if (servletWaterUI == null) {
                servletWaterUI = new ServletWatcherUI();
                servletWaterUI.setInitServlet(this);
            }
            this.request = request;
            this.response = response;

            servletWaterUI.setVisible(true);
            while (servletWaterUI.isVisible()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void showRequestSession(HttpServletRequest request, HttpServletResponse response) {
        debug("START -------------------------------------------------------------");
        debug("# Request #");
        showGetAttribute(request.getAttributeNames(), request);
        debug("# Session #");
        showGetAttribute(request.getSession().getAttributeNames(), request.getSession());
        debug("# ServletContext #");
        showGetAttribute(request.getSession().getServletContext().getAttributeNames(), request.getSession()
                .getServletContext());
        try {
            debug("# ServletConfig #");
            ServletConfig servletConfig = this.getServletConfig();
            if (servletConfig != null) {
                debug(ToStringUtil.toString(servletConfig));
            } else {
                debug("\tservletConfig = null");
            }
        } catch (Exception ex) {
            debug("\tservletConfig = error !!, " + ex);
            ex.printStackTrace();
        }
        debug("# Info #");
        debug(ToStringUtil.toString(request));
        debug(ToStringUtil.toString(request.getSession()));
        debug(ToStringUtil.toString(request.getSession().getServletContext()));
        debug("END -------------------------------------------------------------");
    }

    public static void setSlf4jRootLevel(Level level) {
        log.debug("START -------------------------------------------------------------");
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        log.debug("set root = " + level);
        context.getLogger("ROOT").setLevel(level);
        log.debug("END -------------------------------------------------------------");
    }

    public static void setSlf4jLevel(String loggerName, Level level) {
        log.debug("START -------------------------------------------------------------");
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        log.debug("set logger = " + loggerName);
        if (StringUtils.isBlank(loggerName)) {
            log.debug("loggerName is empty , do nothing!!");
        } else {
            log.debug("######################");
            ListUtil.showListInfo(context.getLoggerList());
            log.debug("######################");
            ch.qos.logback.classic.Logger lll = null;
            if (lll == null) {
                try {
                    lll = context.getLogger(loggerName);
                    log.debug("getLogger by String = " + lll);
                    lll.setLevel(level);
                } catch (Exception ex) {
                    log.debug("error", ex);
                }
            }
            if (lll == null) {
                try {
                    lll = context.getLogger(Class.forName(loggerName));
                    log.debug("getLogger by Class = " + lll);
                    lll.setLevel(level);
                } catch (Exception ex) {
                    log.debug("error", ex);
                }
            }
        }
        log.debug("END -------------------------------------------------------------");
    }

    void resetSlf4jConfig(File file) {
        log.debug("START -------------------------------------------------------------");
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        try {
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);
            context.reset();
            if (file != null) {
                log.debug("set logback = " + file.getAbsolutePath());
                configurator.doConfigure(file);
            } else {
                configurator.doConfigure(this.getClass().getResource("logback.xml"));
            }
        } catch (Exception ex) {
            log.error("resetSlf4jConfig", ex);
        }
        log.debug("END -------------------------------------------------------------");
    }

    void sl4jShowInfo() {
        log.debug("START -------------------------------------------------------------");
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusPrinter.printInCaseOfErrorsOrWarnings(context);
        StatusPrinter.print(context);
        log.debug("END -------------------------------------------------------------");
    }

    void showSpringInfo(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        ServletContext context = session.getServletContext();

        WebApplicationContext webContext = (WebApplicationContext) context
                .getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

        if (webContext == null) {
            System.err.println(" WebApplicationContext = null");
            return;
        }

        debug("START -------------------------------------------------------------");
        debug("# WebApplicationContext START -------------------------------------------------------------");
        debug("\tgetServletContext = " + webContext.getServletContext());
        debug("\tgetServletContext = " + webContext.getServletContext());
        debug("\tgetParent = " + webContext.getParent());
        debug("\tgetId = " + webContext.getId());
        debug("\tgetDisplayName = " + webContext.getDisplayName());
        debug("\tgetStartupDate = " + webContext.getStartupDate());
        debug("\tgetAutowireCapableBeanFactory = " + webContext.getAutowireCapableBeanFactory());
        debug("\tgetBeanDefinitionCount = " + webContext.getBeanDefinitionCount());
        debug("\tgetParentBeanFactory = " + webContext.getParentBeanFactory());
        debug("\tgetClassLoader = " + webContext.getClassLoader());
        debug("#BEAN - getBeanDefinitionNames - start -------------------------------------------------------------");
        for (String name : Arrays.asList(webContext.getBeanDefinitionNames())) {
            try {
                debug("\t" + name + "\t" + webContext.getBean(name).getClass().getSimpleName());
            } catch (Exception ex) {
                debug("\t" + name + " is error!!");
            }
        }
        debug("#BEAN - getBeanDefinitionNames - end -------------------------------------------------------------");

        DefaultListableBeanFactory autowire = (DefaultListableBeanFactory) webContext.getAutowireCapableBeanFactory();
        debug("#START - getAutowireCapableBeanFactory -------------------------------------------------------------");
        debug("\tgetAutowireCandidateResolver = " + autowire.getAutowireCandidateResolver());
        debug("\tgetBeanDefinitionCount = " + autowire.getBeanDefinitionCount());
        debug("\tgetBeanDefinitionNames = " + autowire.getBeanDefinitionNames());
        debug("\tisConfigurationFrozen = " + autowire.isConfigurationFrozen());
        debug("\tgetAutowireCandidateResolver = " + autowire.getAutowireCandidateResolver());
        debug("\tgetBeanDefinitionCount = " + autowire.getBeanDefinitionCount());
        debug("\tgetBeanDefinitionNames = " + autowire.getBeanDefinitionNames());
        debug("\tisConfigurationFrozen = " + autowire.isConfigurationFrozen());
        debug("\tgetAccessControlContext = " + autowire.getAccessControlContext());
        debug("\tgetParentBeanFactory = " + autowire.getParentBeanFactory());
        debug("\tgetTypeConverter = " + autowire.getTypeConverter());
        debug("\tgetBeanClassLoader = " + autowire.getBeanClassLoader());
        debug("\tgetTempClassLoader = " + autowire.getTempClassLoader());
        debug("\tisCacheBeanMetadata = " + autowire.isCacheBeanMetadata());
        debug("\tgetBeanExpressionResolver = " + autowire.getBeanExpressionResolver());
        debug("\tgetConversionService = " + autowire.getConversionService());
        debug("\tgetBeanPostProcessorCount = " + autowire.getBeanPostProcessorCount());
        debug("\tgetRegisteredScopeNames = " + Arrays.toString(autowire.getRegisteredScopeNames()));
        debug("\tgetBeanPostProcessors = " + autowire.getBeanPostProcessors());
        debug("\tgetPropertyEditorRegistrars = " + autowire.getPropertyEditorRegistrars());
        debug("\tgetCustomEditors = " + autowire.getCustomEditors());
        debug("\tgetSingletonCount = " + autowire.getSingletonCount());
        debug("\tgetSingletonNames = " + Arrays.toString(autowire.getSingletonNames()));
        debug("#BEAN - start -------------------------------------------------------------");
        Set<String> sameBeanName = new HashSet<String>();
        for (String name : Arrays.asList(autowire.getBeanDefinitionNames())) {
            try {
                if (webContext.containsBean(name) && webContext.getBean(name) == autowire.getBean(name)) {
                    sameBeanName.add(name);
                    continue;
                }
                debug("\t" + name + "\t" + autowire.getBean(name).getClass().getSimpleName());
            } catch (Exception ex) {
                debug("\t" + name + " is error!!");
            }
        }
        debug("SAME BEAN = " + sameBeanName);
        debug("#BEAN - end -------------------------------------------------------------");
        debug("START = getBeanPostProcessors -------------------------------------------------------------");
        showCollection(autowire.getBeanPostProcessors());
        debug("START = getPropertyEditorRegistrars -------------------------------------------------------------");
        showCollection(autowire.getPropertyEditorRegistrars());
        debug("START = getCustomEditors -------------------------------------------------------------");
        showMap(autowire.getCustomEditors());
        debug("END -------------------------------------------------------------");
    }

    void setServerMode(boolean bool, HttpServletRequest request, HttpServletResponse response) {
        log.debug("# setServerMode ...");
        try {
            request.getSession().getServletContext().setAttribute(SERVER_MODE_KEY, bool);
            log.debug("set attribute '" + SERVER_MODE_KEY + "' = " + bool);
        } catch (Exception ex) {
            log.error("", ex);
        }
    }

    private void showMap(Map<?, ?> map) {
        for (Iterator<?> it = map.keySet().iterator(); it.hasNext();) {
            Object key = it.next();
            Object value = map.get(key);
            debug("\tkey:[" + key + "]\tvalue:[" + value + "]");
        }
    }

    private void showGetAttribute(Enumeration<?> enu, Object src) {
        try {
            Method method = src.getClass().getMethod("getAttribute", new Class[] { String.class });
            for (; enu.hasMoreElements();) {
                Object name = enu.nextElement();
                Object val = method.invoke(src, name);
                debug("\tkey:[" + name + "]\tvalue:[" + val + "]");
            }
        } catch (Exception ex) {
            log.error(src.getClass().toString(), ex);
        }
    }

    private void showCollection(Collection<?> coll) {
        for (Object obj : coll) {
            debug("\t" + obj);
        }
    }

    private void debug(String message) {
        this.message.append(message + "\r\n");
        log.trace(message);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void destroy() {
    }
}