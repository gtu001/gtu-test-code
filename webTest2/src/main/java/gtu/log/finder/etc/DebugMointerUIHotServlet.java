package gtu.log.finder.etc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import gtu.class_.ClassUtil;
import gtu.exception.ExceptionStackUtil;
import gtu.log.finder.DebugMointerUI;
import gtu.log.finder.DebugMointerUI.Constant;
import gtu.log.finder.DebugMointerUI_forCglib;

public class DebugMointerUIHotServlet extends HttpServlet {

    private static final long serialVersionUID = 6413585252636622739L;
    private static final String CHANGELINE = "\n<br/>";
    private OrignPool orignPool = new OrignPool();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        this.process(req, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, final HttpServletResponse response) throws ServletException, IOException {
        this.process(req, response);
    }

    private void process(HttpServletRequest req, final HttpServletResponse response) {
        System.out.println("#. DebugMointerUIHotServlet start");
        final StringBuilder sb = new StringBuilder();
        try {
            // WebParamterWrapper request = new WebParamterWrapper(req, "utf8");

            String classpathStr = req.getParameter("classpath");
            String orignClz = req.getParameter("orignClz");
            String invoke = req.getParameter("invoke");

            File classpathFile = null;
            if (StringUtils.isNotBlank(classpathStr)) {
                classpathFile = new File(classpathStr);
                if (!classpathFile.exists()) {
                    log("[classpath] 路徑不存在  : " + classpathStr, sb);
                }
            } else {
                log("[classpath]參數為空 : " + classpathStr, sb);
            }

            if (classpathFile == null || !classpathFile.exists()) {
                classpathFile = getDefaultClasspathFile();
                log("[classpath] 使用預設路徑  : " + classpathFile, sb);
            }

            if (StringUtils.isBlank(orignClz)) {
                log("[orignClz]參數為空 : " + orignClz, sb);
            }

            ApplicationContext context = getContext(sb);

            // 取得原始物件[可能已被proxy]
            Class<?> orignClass = null;
            Object orignBean = null;
            try {
                orignClass = Class.forName(orignClz);
                orignBean = getOrignBean(orignClass, context, sb);
                log("取得原始Bean物件[可能已被proxy] : " + orignBean);

                // 取得原始物件
                orignBean = orignPool.get(orignClass, orignBean);
                log("取得原始Bean物件 : " + orignBean);
            } catch (Exception e1) {
                log("無法找到Orign Class Bean : " + orignClz, sb);
            }

            if (orignBean == null) {
                log("原始Class為空 ! : " + orignClz, sb);
            }

            if (orignBean != null) {
                String classname = orignClz + "_";
                log("外掛Bean class : " + classname);

                DebugMointerUI_forCglib cglib = new DebugMointerUI_forCglib();
                cglib.setLogDo(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        log(e.getActionCommand(), sb);
                    }
                });
                cglib.loadPluginClass(classpathFile, classname);
                Object newProxy = cglib.createProxy(orignClass, orignBean, Arrays.asList(context));

                this.replaceAllAutowired(orignClass, newProxy, context, sb);
            }

            if (StringUtils.isNotBlank(invoke)) {
                String classname = orignClz + "_";
                String method = invoke;
                if (invoke.contains(".")) {
                    String[] vals = invoke.split(".");
                    classname = vals[0];
                    method = vals[1];
                }

                log("<br/><h1>執行外掛</h1>");
                log(">> " + classname + "." + method + "()!! <<", sb);

                if (orignBean == null) {
                    log("原始Class為空 !" + orignClz, sb);
                }

                Object[] refArry = new Object[] { //
                        context, //
                        req, //
                        response,//
                };//
                
                Map<String, Object> map = new HashMap<String, Object>();
                map.put(Constant.INDICATE_CLASS_PATH, classpathFile.getAbsolutePath());
                map.put(Constant.INDICATE_CLASS_NAME, classname);
                map.put(Constant.INDICATE_METHOD_NAME, method);
                Object rtnVal = DebugMointerUI.startWithAndDispose(map, orignBean, refArry);

                try {
                    log("執行結果 ↓↓↓↓↓↓↓↓↓↓↓↓ ");
                    log(HtmlUtil.replaceChangeLineToBr(ReflectionToStringBuilder.toString(rtnVal, ToStringStyle.MULTI_LINE_STYLE)), sb);
                    log("執行結果 ↑↑↑↑↑↑↑↑↑↑↑↑ ");
                } catch (Exception ex) {
                    log(ExceptionStackUtil.parseToStringBr(ex), sb);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();

            if (sb != null) {
                try {
                    log(ExceptionStackUtil.parseToStringBr(ex), sb);
                } catch (Exception ex2) {
                    ex2.printStackTrace();
                }
            }
        } finally {
            try {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/html; charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.println("<html><body>");
                out.println("<h1>掛載結果</h1>");
                out.println(sb.toString());
                out.println("</body></html>");
                out.println("<html><body>");
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("#. DebugMointerUIHotServlet end");
        }
    }

    private class OrignPool {
        private Map<Class<?>, Object> orignPool = new HashMap<Class<?>, Object>();

        private Object get(Class<?> clz, Object orignBean) {
            if (clz == null) {
                return null;
            }
            if (orignPool.containsKey(clz)) {
                return orignPool.get(clz);
            } else {
                orignPool.put(clz, orignBean);
                return orignBean;
            }
        }
    }

    private ApplicationContext getContext(StringBuilder sb) {
        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        context = getContextCustom();
        log("取得spring context : " + context);
        return context;
    }

    private Object getOrignBean(Class<?> orignClass, ApplicationContext context, StringBuilder sb) {
        try {
            return context.getBean(orignClass);
        } catch (Exception ex) {
            log(ExceptionStackUtil.parseToStringBr(ex), sb);
        }
        return null;
    }

    private File getDefaultClasspathFile() {
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream(DebugMointerUI.configFile));
            Pattern ptn = Pattern.compile("ExecuteConfig\\_(\\d+)");
            String propKey = "ExecuteConfig_";
            int val = 100;
            for (Enumeration enu = prop.keys(); enu.hasMoreElements();) {
                String key = (String) enu.nextElement();
                Matcher mth = ptn.matcher(key);
                if (mth.find()) {
                    val = Math.min(val, Integer.parseInt(mth.group(1)));
                }
            }
            String valueStr = prop.getProperty(propKey + val);
            File classespath = new File(valueStr.split(",")[0]);
            return classespath;
        } catch (Exception e) {
            throw new RuntimeException("getDefaultClasspathFile ERR : " + e.getMessage(), e);
        }
    }

    private void log(String message, StringBuilder sb) {
        System.out.println(message);
        sb.append(message + CHANGELINE);
    }

    private <T> void replaceAllAutowired(Class<?> orignClass, Object newProxy, ApplicationContext context, StringBuilder sb) {
        log("Bean size : " + context.getBeanDefinitionCount());
        for (String name : context.getBeanDefinitionNames()) {
            Object bean = context.getBean(name);
            if (bean.getClass().getDeclaredFields() != null) {
                for (Field f : bean.getClass().getDeclaredFields()) {
                    if (ClassUtil.isAssignFrom(f.getType(), orignClass)) {
                        try {
                            Object orignBean = FieldUtils.readDeclaredField(bean, f.getName(), true);
                            if (orignBean == null || StringUtils.equals(orignBean.getClass().getName(), orignClass.getName())) {
                                FieldUtils.writeDeclaredField(bean, f.getName(), newProxy, true);
                                log("更換 " + bean.getClass().getName() + "." + f.getName() + " - 成功! : " + newProxy, sb);
                            }
                        } catch (IllegalAccessException ex) {
                            log("[ERROR]更換 " + bean.getClass().getName() + "." + f.getName() + " - 失敗!", sb);
                            log(ExceptionStackUtil.parseToStringBr(ex), sb);
                        }
                    }
                }
            }
        }
    }

    private ApplicationContext getContextCustom() {
        try {
            Class<?> clz = Class.forName("com.fuco.mb.bill.controller.StaticContextAccessor");
            Object val1 = FieldUtils.readDeclaredStaticField(clz, "instance", true);
            ApplicationContext app = (ApplicationContext) FieldUtils.readDeclaredField(val1, "applicationContext", true);
            return app;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
