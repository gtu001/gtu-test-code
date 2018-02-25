package gtu.log.finder.etc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import gtu.class_.ClassUtil;
import gtu.exception.ExceptionStackUtil;
import gtu.log.finder.DebugMointerUI;
import gtu.log.finder.DebugMointerUI_forCglib;

public class DebugMointerUIHotServlet extends HttpServlet {

    private static final long serialVersionUID = 6413585252636622739L;
    private static final String CHANGELINE = "\n<br/>";
    private OrignPool orignPool = new OrignPool();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        super.doGet(req, response);
        this.process(req, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, final HttpServletResponse response) throws ServletException, IOException {
        super.doPost(req, response);
        this.process(req, response);
    }

    private void process(HttpServletRequest req, final HttpServletResponse response) {
        System.out.println("#. DebugMointerUIHotServlet start");
        PrintWriter writer = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=UTF-8");

            writer = response.getWriter();

            DebugMointerUI_forCglib cglib = new DebugMointerUI_forCglib();

            // WebParamterWrapper request = new WebParamterWrapper(req, "utf8");

            String file = req.getParameter("file");
            String orignClz = req.getParameter("orignClz");

            File classpathFile = null;
            if (StringUtils.isNotBlank(file)) {
                classpathFile = new File(file);
                if (!classpathFile.exists()) {
                    log("路徑不存在  : " + file, writer);
                }
            } else {
                log("參數為空[file] : " + file, writer);
            }

            if (classpathFile == null || !classpathFile.exists()) {
                classpathFile = getDefaultClasspathFile();
                log("使用預設路徑  : " + classpathFile, writer);
            }

            if (StringUtils.isBlank(orignClz)) {
                log("參數為空[orignClz] : " + orignClz, writer);
            }

            Class<?> orignClass = null;
            try {
                orignClass = Class.forName(orignClz);
            } catch (Exception e1) {
                log("無法找到Class : " + orignClz, writer);
            }

            if (orignClass != null) {
                ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
                context = getContextCustom();

                // 取得原始物件[可能已被proxy]
                Object orignBean = context.getBean(orignClass);
                log("取得原始Bean物件[可能已被proxy] : " + orignBean);

                // 取得原始物件
                orignBean = orignPool.get(orignClass, orignBean);
                log("取得原始Bean物件 : " + orignBean);

                if (orignBean == null) {
                    log("原始Class為空 !" + orignClz, writer);
                }

                String classname = orignClz + "_";
                log("外掛Bean class : " + classname);
                cglib.loadPluginClass(classpathFile, classname);
                Object newProxy = cglib.createProxy(orignClass, orignBean, Arrays.asList(context));

                this.replaceAllAutowired(orignClass, newProxy, context, writer);
            }
        } catch (Exception ex) {
            ex.printStackTrace();

            if (writer != null) {
                try {
                    log(ExceptionStackUtil.parseToString(ex), writer);
                } catch (Exception ex2) {
                    ex2.printStackTrace();
                }
            }
        } finally {
            try {
                writer.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                writer.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.out.println("#. DebugMointerUIHotServlet end");
        }
    }

    private class OrignPool {
        private Map<Class<?>, Object> orignPool = new HashMap<Class<?>, Object>();

        private Object get(Class<?> clz, Object orignBean) {
            if (orignPool.containsKey(clz)) {
                return orignPool.get(clz);
            } else {
                orignPool.put(clz, orignBean);
                return orignBean;
            }
        }
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

    private void log(String message, PrintWriter writer) {
        System.out.println(message);
        writer.write(message + CHANGELINE);
    }

    private <T> void replaceAllAutowired(Class<?> orignClass, Object newProxy, ApplicationContext context, PrintWriter writer) {
        log("Bean size : " + context.getBeanDefinitionCount());
        for (String name : context.getBeanDefinitionNames()) {
            Object bean = context.getBean(name);
            if (bean.getClass().getDeclaredFields() != null) {
                for (Field f : bean.getClass().getDeclaredFields()) {
                    if (ClassUtil.isAssignFrom(f.getType(), orignClass)) {
                        try {
                            Object orignBean = FieldUtils.readDeclaredField(bean, f.getName(), true);
                            if(StringUtils.equals(orignBean.getClass().getName(), orignClass.getName())) {
                                FieldUtils.writeDeclaredField(bean, f.getName(), newProxy, true);
                                log("更換 " + bean.getClass().getName() + "." + f.getName() + " - 成功! : " + newProxy, writer);
                            }
                        } catch (IllegalAccessException ex) {
                            log("[ERROR]更換 " + bean.getClass().getName() + "." + f.getName() + " - 失敗!", writer);
                            log(ExceptionStackUtil.parseToString(ex), writer);
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
