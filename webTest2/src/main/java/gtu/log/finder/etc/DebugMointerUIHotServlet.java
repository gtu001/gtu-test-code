package gtu.log.finder.etc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import gtu.exception.ExceptionStackUtil;
import gtu.file.FileUtil;
import gtu.javafx.traynotification.TrayNotificationHelper.RandomColorFill;
import gtu.log.finder.DebugMointerMappingField;
import gtu.log.finder.DebugMointerUI;
import gtu.log.finder.DebugMointerUI.Constant;
import gtu.log.finder.DebugMointerUI_forCglib;
import gtu.swing.util.JCommonUtil;

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

    private class ParamConf {
        String classpathStr;
        String orignClz;
        String invoke;
        String log;
        String exceptionLog;
        String del;
        String reverse;
        String customSpring;
        String springInvoke;
        String unzipClassesMode;
    }

    private void process(HttpServletRequest req, final HttpServletResponse response) {
        System.out.println("#. DebugMointerUIHotServlet start");
        final StringBuilder sb = new StringBuilder();
        try {
            // 如果上傳classpath.zip
            DebugMointerUIClassesUpload upload = new DebugMointerUIClassesUpload(req, sb);
            upload.uploadFile();

            ParamConf conf = new ParamConf();

            conf.classpathStr = upload.getParameter("classpath");
            conf.orignClz = upload.getParameter("orignClz");
            conf.invoke = upload.getParameter("invoke");
            conf.log = upload.getParameter("log");
            conf.exceptionLog = upload.getParameter("exceptionLog");
            conf.del = upload.getParameter("del");
            conf.reverse = upload.getParameter("reverse");
            conf.customSpring = upload.getParameter("customSpring");
            conf.springInvoke = upload.getParameter("springInvoke");
            conf.unzipClassesMode = upload.getParameter("unzipClassesMode");

            WebTestUtil webUtil = WebTestUtil.newInstnace().request(req).response(response);

            // DebugMointerUI.log
            if (webUtil.hasParameterKey("log")) {
                File logFile = new File(conf.log);
                if (logFile == null || !logFile.exists()) {
                    logFile = DebugMointerUI.getLogger().getLogFile();
                }
                if (logFile == null || !logFile.exists()) {
                    logH("檔案不存在 : " + logFile, sb);
                    return;
                }
                System.out.println("# download log file : " + logFile);
                FileInputStream fileInputStream = new FileInputStream(logFile);
                WebTestUtil.newInstnace().request(req).response(response).download(fileInputStream, logFile.getName());
                return;
            }

            // swing.log
            if (webUtil.hasParameterKey("exceptionLog")) {
                File logFile = new File(conf.exceptionLog);
                if (logFile == null || !logFile.exists()) {
                    logFile = JCommonUtil.handleException_getFile();
                }
                if (logFile == null || !logFile.exists()) {
                    logH("檔案不存在 : " + logFile, sb);
                    return;
                }
                System.out.println("# download exceptionLog file : " + logFile);
                FileInputStream fileInputStream = new FileInputStream(logFile);
                WebTestUtil.newInstnace().request(req).response(response).download(fileInputStream, logFile.getName());
                return;
            }

            // 刪除log
            if (webUtil.hasParameterKey("del")) {
                delFile(DebugMointerUI.getLogger().getLogFile(), false, sb);
                delFile(JCommonUtil.handleException_getFile(), true, sb);
            }

            File classpathFile = null;
            if (StringUtils.isNotBlank(conf.classpathStr)) {
                classpathFile = new File(conf.classpathStr);
                if (!classpathFile.exists()) {
                    logH("[classpath] 路徑不存在  : " + conf.classpathStr, sb);
                }
            } else {
                logH("[classpath]參數為空 : " + conf.classpathStr, sb);
            }

            if (classpathFile == null || !classpathFile.exists()) {
                classpathFile = getDefaultClasspathFile(sb);
                logH("[classpath] 使用預設路徑  : " + classpathFile, sb);
            }

            // 判斷是否上傳classpath.zip檔案
            if (upload.isUploadClassesZip()) {
                upload.extractClasspath(classpathFile.getAbsolutePath(), conf.unzipClassesMode);
            }

            if (StringUtils.isBlank(conf.orignClz)) {
                logH("[orignClz]參數為空 : " + conf.orignClz, sb);
            }

            DebugMointerUISpringWebHandler springHandler = new DebugMointerUISpringWebHandler(this, conf.customSpring);
            Object context = springHandler.getContext(sb);

            // 取得原始物件[可能已被proxy]
            Class<?> orignClass = null;
            Object orignBean = null;
            try {
                orignClass = Class.forName(conf.orignClz);
                orignBean = springHandler.getOrignBean(orignClass, context, sb);
                log("取得原始Bean物件[可能已被proxy] : " + orignBean);

                // 取得原始物件
                orignBean = orignPool.get(orignClass, orignBean);
                log("取得原始Bean物件 : " + orignBean);
            } catch (Exception e1) {
                logH("無法找到Orign Class Bean : " + conf.orignClz, sb);
            }

            if (orignBean == null) {
                logH("原始Class為空 ! : " + conf.orignClz, sb);
            }

            // 替換proxy
            if (orignBean != null) {
                try {
                    String classname = conf.orignClz + "_";
                    log("外掛Bean class : " + classname);

                    Object newProxy = null;

                    if (StringUtils.isBlank(conf.reverse)) {
                        DebugMointerUI_forCglib cglib = new DebugMointerUI_forCglib();
                        cglib.setLogDo(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                logH(e.getActionCommand(), sb);
                            }
                        });
                        cglib.loadPluginClass(classpathFile, classname);
                        newProxy = cglib.createProxy(orignClass, orignBean, Arrays.asList(context));
                    } else {
                        // 取得原始物件
                        newProxy = orignBean;
                        logH("[復原resverse]取得原始Bean物件 : " + orignBean, sb);
                    }

                    springHandler.replaceAllAutowired(orignClass, newProxy, context, sb);
                } catch (Throwable ex) {
                    exceptioinHandler(ex, sb);
                }
            }

            // 執行外掛
            if (StringUtils.isNotBlank(conf.invoke)) {
                try {
                    conf.invoke = URLDecoder.decode(conf.invoke, "utf8");
                    String classname = conf.orignClz + "_";
                    String method = conf.invoke;
                    if (conf.invoke.contains(".")) {
                        String[] vals = conf.invoke.split(",", -1);
                        classname = vals[0];
                        method = vals[1];
                    }

                    logH("<br/><h2>" + getRandomColorString("執行外掛") + "</h2>", sb);
                    logH(">>" + (classname + "." + method + "()!!") + " <<", sb);

                    if (orignBean == null) {
                        logH("原始Class為空 !" + conf.orignClz, sb);
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

                    StringBuilder tableSb = new StringBuilder();
                    tableSb.append("<table border='1'>\n");
                    tableSb.append("<tr><td width='10%'>回傳值</td><td width='90%'>內容</td></tr>\n");
                    tableSb.append("<tr><td>原始</td><td>" + DebugMointerMappingField.getObjSimpleStr(rtnVal) + "</td></tr>\n");
                    tableSb.append(
                            "<tr><td>formatter</td><td>" + DebugMointerUIHtmlUtil.replaceChangeLineToBr(ReflectionToStringBuilder.toString(rtnVal, ToStringStyle.MULTI_LINE_STYLE)) + "</td></tr>\n");
                    tableSb.append("</table>\n");
                    logH(tableSb.toString(), sb);
                } catch (Throwable ex) {
                    logH("<b>" + getRandomColorString("掛載失敗!!") + "</b>", sb);
                    exceptioinHandler(ex, sb);
                }
            }

            // 直接執行spring bean
            if (StringUtils.isNotBlank(conf.springInvoke)) {
                try {
                    logH("<br/><H3>" + getRandomColorString("直接執行SpringBean") + "</H3>", sb);
                    String[] vals = conf.springInvoke.split(",", -1);
                    if (vals.length != 2) {
                        logH("ERROR 參數錯誤, 必須為 : classpath,method", sb);
                    } else {
                        String classStr = vals[0];
                        String methodStr = vals[1];

                        logH("執行 : " + classStr + "." + methodStr + "()!", sb);
                        Class<?> clz = Class.forName(classStr);
                        Object springBean = springHandler.getOrignBean(clz, context, sb);

                        if (springBean == null) {
                            logH("ERROR 無法取得springBean!!", sb);
                        } else {
                            // 真正執行springInvoke
                            invokeSpringBeanMethod(springBean, methodStr, sb);
                        }
                    }
                } catch (Exception ex) {
                    logH("<b>" + getRandomColorString("執行失敗!!") + "</b>", sb);
                    exceptioinHandler(ex, sb);
                }
            }

            // 顯示log
            logH(getFormStr(req, conf), sb);
            logH(String.format("<a href=\"%s\"><i>檢視Log</i></a>", this.getLogLink(req)), sb);
            logH(String.format("<a href=\"%s\"><i>檢視Exception Log</i></a>", this.getSwingExceptionLogLink(req)), sb);
            logH(String.format("<a href=\"%s\"><i>刪除Log</i></a>", this.getDelLogLink(req)), sb);
        } catch (Throwable ex) {
            ex.printStackTrace();

            if (sb != null) {
                try {
                    exceptioinHandler(ex, sb);
                } catch (Exception ex2) {
                    ex2.printStackTrace();
                }
            }
        } finally {
            try {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/html; charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.println("<html><body bgcolor=\"#E6E6FA\">");
                out.println("<h1>" + getRandomColorString("掛載結果") + "</h1>");
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

    private void invokeSpringBeanMethod(Object springBean, String methodStr, StringBuilder sb) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        TreeMap<Integer, Method> methodMap = new TreeMap<Integer, Method>();
        for (Method m : springBean.getClass().getDeclaredMethods()) {
            if (StringUtils.equals(m.getName(), methodStr)) {
                if (m.getParameterTypes() == null || m.getParameterTypes().length == 0) {
                    methodMap.put(0, m);
                } else {
                    methodMap.put(m.getParameterTypes().length, m);
                }
            }
        }
        if (methodMap.isEmpty()) {
            logH("無法取得對應method : " + methodStr, sb);
        } else {
            Method mth = methodMap.get(methodMap.keySet().iterator().next());
            mth.setAccessible(true);

            boolean result = false;
            if (!Modifier.isStatic(mth.getModifiers())) {
                if (mth.getParameterTypes() == null || mth.getParameterTypes().length == 0) {
                    mth.invoke(springBean, new Object[0]);
                    result = true;
                } else {
                    Object[] params = new Object[mth.getParameterTypes().length];
                    mth.invoke(springBean, params);
                    result = true;
                }
            } else {
                if (mth.getParameterTypes() == null || mth.getParameterTypes().length == 0) {
                    mth.invoke(springBean.getClass(), new Object[0]);
                    result = true;
                } else {
                    Object[] params = new Object[mth.getParameterTypes().length];
                    mth.invoke(springBean.getClass(), params);
                    result = true;
                }
            }

            if (result) {
                logH("<b>" + getRandomColorString("執行成功!!") + "</b>", sb);
            } else {
                logH("<b>" + getRandomColorString("執行失敗!!") + "</b>", sb);
            }
        }
    }

    private void delFile(File file, boolean justEmptyContent, StringBuilder sb) {
        boolean result = false;
        String path = "FileNotFound";
        try {
            if (file != null && file.exists()) {
                if (!justEmptyContent) {
                    result = file.delete();
                } else {
                    FileUtil.saveToFile(file, "", "UTF8");
                    result = file.length() == 0;
                }
                path = file.getAbsolutePath();
            }
        } catch (Exception ex) {
        }
        logH("<br/><h4>刪除 : " + path + " : " + (result ? "成功" : "失敗") + "</h4>", sb);
    }

    private String getDelLogLink(HttpServletRequest request) {
        return getURL(request) + "?del";
    }

    private String getURL(HttpServletRequest request) {
        String uri = request.getScheme() + "://" + // "http" + "://
                request.getServerName() + // "myhost"
                ":" + // ":"
                request.getServerPort() + // "8080"
                request.getRequestURI() + // "/people"
                "";
        return uri;
    }

    private String getLogLink(HttpServletRequest request) {
        String path = DebugMointerUI.getLogger().getLogFile().getAbsolutePath();
        try {
            return getURL(request) + "?log=" + URLEncoder.encode(path, "utf8");
        } catch (Exception e) {
            return path;
        }
    }

    private String getSwingExceptionLogLink(HttpServletRequest request) {
        File file = JCommonUtil.handleException_getFile();
        String path = "";
        if (file != null && file.exists()) {
            path = file.getAbsolutePath();
        }
        try {
            return getURL(request) + "?exceptionLog=" + URLEncoder.encode(path, "utf8");
        } catch (Exception e) {
            return path;
        }
    }

    static void exceptioinHandler(Throwable ex, StringBuilder sb) {
        ex.printStackTrace();
        String message = ExceptionStackUtil.parseToStringBr(ex);
        StringBuilder tableSb = new StringBuilder();
        tableSb.append("<table border='1'>\n");
        tableSb.append("<tr><td width='100%'>" + message + "</td></tr>\n");
        tableSb.append("</table>\n");
        sb.append(tableSb.toString());
    }

    static String getRandomColorString(String stringVal) {
        if (StringUtils.isBlank(stringVal)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (char c : stringVal.toCharArray()) {
            sb.append(String.format("<font color='%s'>%s</font>", RandomColorFill.getInstance().get(), c));
        }
        return sb.toString();
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

    private File getDefaultClasspathFile(StringBuilder sb) {
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
            logH("getDefaultClasspathFile ERR : " + e.getMessage(), sb);
            return new File(System.getProperty("user.dir"), "classes");
        }
    }

    private String getFormStr(HttpServletRequest request, ParamConf conf) {
        StringBuilder sb = new StringBuilder();
        sb.append("<script type=\"text/javascript\">\n");
        sb.append("function showHide(id){                                  \n");
        sb.append("  var divForm = document.getElementById(id);   \n");
        sb.append("  if(divForm.style.display == \"none\"){              \n");
        sb.append("    divForm.style.display = \"block\";             \n");
        sb.append("  }else{                                              \n");
        sb.append("    divForm.style.display = \"none\";                \n");
        sb.append("  }                                                   \n");
        sb.append("}                                                     \n");
        sb.append(" function cleanAll(){                                        \n");
        sb.append("         cleanElement(\"customSpring\");                       \n");
        sb.append("         cleanElement(\"classpath\");                          \n");
        sb.append("         cleanElement(\"orignClz\");                           \n");
        sb.append("         cleanElement(\"invoke\");                             \n");
        sb.append("         cleanElement(\"springInvoke\");                       \n");
        sb.append(" }                                                           \n");
        sb.append(" function cleanElement(name){                                \n");
        sb.append("         document.getElementsByName(name)[0].value = '';     \n");
        sb.append(" }                                                           \n");
        sb.append("</script>\n");
        sb.append("<br/><a href=\"javascript:showHide('divForm');\"><i>表單</i></a><br/>\n");
        sb.append("<div id='divForm' style=\"background-color:" + RandomColorFill.getInstance().get() + "; padding:10px;margin-bottom:5px; display:none\">\n");
        sb.append(String.format("<form action=\"\" method=\"post\" enctype=\"multipart/form-data\">\n", this.getURL(request)));
        sb.append("<table border='1'>\n");
        sb.append(String.format("<tr><td>%s</td><td><input type=\"text\" name=\"%s\" style=\"width:500px\" value=\"%s\" /></td></tr>\n", "[customSpring]外掛位置", "customSpring", StringUtils.trimToEmpty(conf.customSpring)));
        sb.append(String.format("<tr><td>%s</td><td><input type=\"text\" name=\"%s\" style=\"width:500px\" value=\"%s\" /></td></tr>\n", "[classpath]外掛位置", "classpath", StringUtils.trimToEmpty(conf.classpathStr)));
        sb.append(String.format("<tr><td>%s</td><td><input type=\"text\" name=\"%s\" style=\"width:500px\" value=\"%s\" /></td></tr>\n", "[orignClz]替換類別", "orignClz", StringUtils.trimToEmpty(conf.orignClz)));
        sb.append(String.format("<tr><td>%s</td><td><input type=\"text\" name=\"%s\" style=\"width:500px\" value=\"%s\" /></td></tr>\n", "[invoke](Ex:類別,方法)", "invoke", StringUtils.trimToEmpty(conf.invoke)));
        sb.append(
                String.format("<tr><td>%s</td><td><input type=\"text\" name=\"%s\" style=\"width:500px\" value=\"%s\" /></td></tr>\n", "[springInvoke](Ex:類別,方法)", "springInvoke", StringUtils.trimToEmpty(conf.springInvoke)));
        sb.append(String.format("<tr><td>%1$s</td><td><input id=\"%2$s\" name=\"%2$s\" type=\"file\" style=\"width:500px\" /></td></tr>\n", "上傳classes.zip", "classesZip"));
        sb.append(String.format("<tr><td>%1$s</td><td>%2$s</td></tr>\n", "[unzipClassesMode]解壓縮方式", DebugMointerUIClassesUpload.UnzipMode.generateHtml("unzipClassesMode")));
        sb.append(String.format("<tr><td>%1$s</td><td><input id=\"%2$s\" name=\"%2$s\" type=\"checkbox\" value=\"Y\" /></td></tr>\n", "[reverse]復原", "reverse"));
        sb.append("<tr><td colspan='2' align=\"right\"><input type=\"button\" value=\"重設\" onclick=\"javascript:cleanAll();\" /><input type=\"submit\" value=\"送出\" /></td></tr>\n");
        sb.append("</table>\n");
        sb.append("</form>\n");
        sb.append("</div>\n");
        return sb.toString();
    }

    static void logH(String message, StringBuilder sb) {
        // System.out.println(message);
        sb.append(message + CHANGELINE);
    }
}
