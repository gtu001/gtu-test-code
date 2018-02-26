package gtu.log.finder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.mockito.cglib.proxy.Enhancer;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;

import gtu.log.finder.DebugMointerUI.Constant;

public class DebugMointerUI_forCglib {

    private class Handler implements MethodInterceptor {
        private final Object original;
        private final Object[] mappingField;

        public Handler(Object original, Object[] mappingField) {
            this.original = original;
            this.mappingField = mappingField;
        }

        public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            boolean usePlugIn = false;
            for (PluginMethod vo : usePluginLst) {
                if (StringUtils.equals(vo.methodName, method.getName())) {
                    if (method.getParameterTypes() == null && method.getParameterTypes() == null) {
                        usePlugIn = true;
                        break;
                    } else if ((method.getParameterTypes() != null && vo.parameterClasses != null) && //
                            (method.getParameterTypes().length == vo.parameterClasses.length) && //
                            isClassesEqual(method.getParameterTypes(), vo.parameterClasses)) {
                        usePlugIn = true;
                        break;
                    }
                }
            }

            Object rtnVal = null;
            if (usePlugIn == false) {
                rtnVal = method.invoke(original, args);
            } else {
                String classname = getMatchClasspath(o.getClass().getName()) + "_";
                Map<String, Object> map = new HashMap<String, Object>();
                map.put(Constant.INDICATE_CLASS_NAME, classname);
                map.put(Constant.INDICATE_METHOD_NAME, method.getName());
                if (method.getParameterTypes() != null && method.getParameterTypes().length != 0) {
                    map.put(Constant.INDICATE_PARAMETERS, args);
                }
                rtnVal = DebugMointerUI.startWithAndDispose(map, original, mappingField);
            }
            return rtnVal;
        }

        private String getMatchClasspath(String Classpath) {
            Pattern pattern = Pattern.compile("(.*)\\$\\$EnhancerByCGLIB\\$\\$\\w+");
            Matcher mth = pattern.matcher(Classpath);
            if (mth.find()) {
                return mth.group(1);
            }
            throw new RuntimeException("## classpath not match !! : " + Classpath);
        }
    }

    private boolean isClassesEqual(Class<?>[] clz1, Class<?>[] clz2) {
        int length = Math.max(clz1.length, clz2.length);
        for (int ii = 0; ii < length; ii++) {
            if (!StringUtils.equals(clz1[ii].getClass().getName(), clz2[ii].getClass().getName())) {
                return false;
            }
        }
        return true;
    }

    private List<PluginMethod> usePluginLst = new ArrayList<PluginMethod>();

    private class PluginMethod {
        String methodName;
        Class[] parameterClasses;
    }

    public void loadPluginClass(File classespath, String className) {
        try {
            log("<<< loadPluginClass start !!");
            List<PluginMethod> lst = new ArrayList<PluginMethod>();
            URLClassLoader loader = new URLClassLoader(new URL[] { classespath.toURL() }, Thread.currentThread().getContextClassLoader());
            Class<?> clz = Class.forName(className, true, loader);
            for (Method mth : clz.getDeclaredMethods()) {
                PluginMethod vo = new PluginMethod();
                vo.methodName = mth.getName();
                vo.parameterClasses = mth.getParameterTypes();
                lst.add(vo);
                this.logDetected(className, vo);
            }
            usePluginLst = lst;
            log("<<< loadPluginClass success !!");
        } catch (Exception e) {
            throw new RuntimeException("loadPluginClass ERR : " + e.getMessage(), e);
        }
    }

    private void logDetected(String className, PluginMethod vo) {
        if (vo.parameterClasses == null || vo.parameterClasses.length == 0) {
            log("\t" + className + "." + vo.methodName + "()" + " -- detected!");
        } else {
            List<String> clzLst = new ArrayList<String>();
            for (Class<?> c : vo.parameterClasses) {
                clzLst.add(c.getName());
            }
            log("\t" + className + "." + vo.methodName + "(" + StringUtils.join(clzLst, ", ") + ")" + " -- detected!");
        }
    }

    private void log(String message) {
        logDo.actionPerformed(new ActionEvent(message, 1, message));
    }

    private ActionListener logDo = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(e.getActionCommand());
        }
    };

    public <T> T createProxy(Class<?> orignClass, Object orignBean, List<?> mappingField) {
        MethodInterceptor handler = new Handler(orignBean, mappingField.toArray());
        T t = (T) Enhancer.create(orignClass, handler);
        return t;
    }

    static class XXXX {
        public String test(String aa, Long bb, int cc) {
            System.out.println("####### orign test");
            return "AAAAAA";
        }

        public String test(String aa, Long bb, int cc, String vvvv) {
            System.out.println("####### orign test");
            return "BBBBBB";
        }
    }

    public void setLogDo(ActionListener logDo) {
        this.logDo = logDo;
    }

    public static void main(String[] args) {
        DebugMointerUI_forCglib t = new DebugMointerUI_forCglib();
        t.loadPluginClass(new File("E:/workstuff/workspace/WebTest/webTest/target/classes"), "gtu.log.finder.DebugMointerUI_forCglib$XXXX_");
        XXXX orign = new XXXX();
        XXXX orignAndPlugin = t.createProxy(XXXX.class, orign, Arrays.asList(DebugMointerUI.getLogger()));
        orignAndPlugin.test(null, 0L, 0, null);
    }
}