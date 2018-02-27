package gtu.log.finder.etc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import gtu.exception.ExceptionStackUtil;
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

    private void process(HttpServletRequest req, final HttpServletResponse response) {
        System.out.println("#. DebugMointerUIHotServlet start");
        final StringBuilder sb = new StringBuilder();

        try {
            String classpathStr = req.getParameter("classpath");
            String orignClz = req.getParameter("orignClz");
            String invoke = req.getParameter("invoke");
            String log = req.getParameter("log");
            String exceptionLog = req.getParameter("exceptionLog");
            String del = req.getParameter("del");

            WebTestUtil webUtil = WebTestUtil.newInstnace().request(req).response(response);

            // DebugMointerUI.log
            if (webUtil.hasParameterKey("log")) {
                File logFile = new File(log);
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
                File logFile = new File(exceptionLog);
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
                delFile(DebugMointerUI.getLogger().getLogFile(), sb);
                delFile(JCommonUtil.handleException_getFile(), sb);
            }

            File classpathFile = null;
            if (StringUtils.isNotBlank(classpathStr)) {
                classpathFile = new File(classpathStr);
                if (!classpathFile.exists()) {
                    logH("[classpath] 路徑不存在  : " + classpathStr, sb);
                }
            } else {
                logH("[classpath]參數為空 : " + classpathStr, sb);
            }

            if (classpathFile == null || !classpathFile.exists()) {
                classpathFile = getDefaultClasspathFile();
                logH("[classpath] 使用預設路徑  : " + classpathFile, sb);
            }

            if (StringUtils.isBlank(orignClz)) {
                logH("[orignClz]參數為空 : " + orignClz, sb);
            }

            DebugSpringWebHandler springHandler = new DebugSpringWebHandler(this);
            Object context = springHandler.getContext(sb);

            // 取得原始物件[可能已被proxy]
            Class<?> orignClass = null;
            Object orignBean = null;
            try {
                orignClass = Class.forName(orignClz);
                orignBean = springHandler.getOrignBean(orignClass, context, sb);
                log("取得原始Bean物件[可能已被proxy] : " + orignBean);

                // 取得原始物件
                orignBean = orignPool.get(orignClass, orignBean);
                log("取得原始Bean物件 : " + orignBean);
            } catch (Exception e1) {
                logH("無法找到Orign Class Bean : " + orignClz, sb);
            }

            if (orignBean == null) {
                logH("原始Class為空 ! : " + orignClz, sb);
            }

            // 替換proxy
            if (orignBean != null) {
                String classname = orignClz + "_";
                log("外掛Bean class : " + classname);

                DebugMointerUI_forCglib cglib = new DebugMointerUI_forCglib();
                cglib.setLogDo(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        logH(e.getActionCommand(), sb);
                    }
                });
                cglib.loadPluginClass(classpathFile, classname);
                Object newProxy = cglib.createProxy(orignClass, orignBean, Arrays.asList(context));

                springHandler.replaceAllAutowired(orignClass, newProxy, context, sb);
            }

            // 執行外掛
            if (StringUtils.isNotBlank(invoke)) {
                invoke = URLDecoder.decode(invoke, "utf8");
                String classname = orignClz + "_";
                String method = invoke;
                if (invoke.contains(".")) {
                    String[] vals = invoke.split(",", -1);
                    classname = vals[0];
                    method = vals[1];
                }

                logH("<br/><h2>" + getRandomColorString("執行外掛") + "</h2>", sb);
                logH(">>" + getRandomColorString(classname + "." + method + "()!!") + " <<", sb);

                if (orignBean == null) {
                    logH("原始Class為空 !" + orignClz, sb);
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
                    StringBuilder tableSb = new StringBuilder();
                    tableSb.append("<table border='1'>\n");
                    tableSb.append("<tr><td width='10%'>回傳值</td><td width='90%'>內容</td></tr>\n");
                    tableSb.append("<tr><td>原始</td><td>" + DebugMointerMappingField.getObjSimpleStr(rtnVal) + "</td></tr>\n");
                    tableSb.append("<tr><td>formatter</td><td>" + HtmlUtil.replaceChangeLineToBr(ReflectionToStringBuilder.toString(rtnVal, ToStringStyle.MULTI_LINE_STYLE)) + "</td></tr>\n");
                    tableSb.append("</table>\n");
                    logH(tableSb.toString(), sb);
                } catch (Throwable ex) {
                    exceptioinHandler(ex, sb);
                }
            }

            // 顯示log
            logH(getFormStr(req), sb);
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

    private void delFile(File file, StringBuilder sb) {
        boolean result = false;
        String path = "FileNotFound";
        try {
            if (file != null && file.exists()) {
                result = file.delete();
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
            if(valueStr == null) {
                return new File(System.getProperty("user.dir"), "classes");
            }
            File classespath = new File(valueStr.split(",")[0]);
            return classespath;
        } catch (Exception e) {
            throw new RuntimeException("getDefaultClasspathFile ERR : " + e.getMessage(), e);
        }
    }

    private String getFormStr(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("<script type=\"text/javascript\">\n");
        sb.append("function showHide(){                                  \n");
        sb.append("  var divForm = document.getElementById('divForm');   \n");
        sb.append("  if(divForm.style.display == \"none\"){              \n");
        sb.append("    divForm.style.display = \"block\";             \n");
        sb.append("  }else{                                              \n");
        sb.append("    divForm.style.display = \"none\";                \n");
        sb.append("  }                                                   \n");
        sb.append("}                                                     \n");
        sb.append("</script>\n");
        sb.append("<br/><a href=\"javascript:showHide();\"><i>表單</i></a><br/>\n");
        sb.append("<div id='divForm' style=\"background-color:" + RandomColorFill.getInstance().get() + "; padding:10px;margin-bottom:5px; display:none\">\n");
        sb.append(String.format("<form action=\"\">\n", this.getURL(request)));
        sb.append("<table border='1'>\n");
        sb.append(String.format("<tr><td>%s</td><td><input type=\"text\" name=\"%s\" style=\"width:500px\" /></td></tr>\n", "[classpath]外掛位置", "classpath"));
        sb.append(String.format("<tr><td>%s</td><td><input type=\"text\" name=\"%s\" style=\"width:500px\" /></td></tr>\n", "[orignClz]替換類別", "orignClz"));
        sb.append(String.format("<tr><td>%s</td><td><input type=\"text\" name=\"%s\" style=\"width:500px\" /></td></tr>\n", "[invoke](Ex:類別,方法)", "invoke"));
        sb.append("<tr><td colspan='2'><input type=\"submit\" value=\"送出\" /></td></tr>\n");
        sb.append("</table>\n");
        sb.append("</div>\n");
        return sb.toString();
    }

    static void logH(String message, StringBuilder sb) {
        System.out.println(message);
        sb.append(message + CHANGELINE);
    }
}
