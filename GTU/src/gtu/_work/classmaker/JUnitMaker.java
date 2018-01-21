package gtu._work.classmaker;

import gtu.class_.ClassPathUtil;
import gtu.collection.MapUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

/**
 * 產生一個 Junit 所有 class method的 test class
 * 
 * @author Troy 2012/1/21
 */
public class JUnitMaker {

    public static void main(String[] args) throws Exception {
        Map<String, String> apiMap = null;
        //        apiMap = ApiMatcher.newInstance().loadSysIn().execute().getApiMap();
        long begin = System.currentTimeMillis();
        JUnitMaker maker = JUnitMaker.newInstance(ServletContext.class, "")//
                .setFieldName("request")//
                .setPrefix("\\t")//
                // .setWriteToDir(null)//
                .apiMap(apiMap)//
                .setIgnore(true)//
                .allowPackage(false)//
                .allowPrivate(false)//
                .allowProtected(false)//
                .allowOther(false)//
                .execute();//
        System.out.println(maker.getGenerateFile().getCanonicalPath());
        long between = System.currentTimeMillis() - begin;
        System.out.println("during : " + between);
        System.out.println("done...");
    }

    private BufferedWriter writer;
    private final Class<?> clz;
    private final String packageUrl;
    private EnumMap<MethodType, Collection<Method>> methodMap;
    private boolean ignore;// @ignore 是否註解
    private static String FIELD_NAME = "test";// 預設field Name
    private static String PREFIX = "\\t";// log前置字
    private final static String SPACE = "    ";// Tab
    private Map<String, String> apiMap;// API
    private File writeTo;// 寫到目錄
    private File generateFile;// 寫到的檔案

    public JUnitMaker execute() throws Exception {
        generateFile = new File(writeTo.getAbsolutePath() + "\\" + clz.getSimpleName() + "Test.java");
        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(generateFile), "UTF8"));

        write("package " + packageUrl + ";");
        newLine();

        write("import " + clz.getName() + ";");
        write("import org.junit.After;");
        write("import org.junit.AfterClass;");
        write("import org.junit.Before;");
        write("import org.junit.BeforeClass;");
        write("import org.junit.Test;");
        write("//import org.junit.Test;");
        write("import org.junit.Ignore;");
        write("import org.apache.log4j.Logger;");
        write("import gtu._work.classmaker.ToStringUtil;");
        newLine();

        write("@Ignore");
        write("public class " + clz.getSimpleName() + "Test {");
        newLine();

        write(SPACE + "private Logger log = Logger.getLogger(getClass());");
        write(SPACE + "private " + clz.getSimpleName() + " " + FIELD_NAME + " = null; ");
        newLine();

        // main
        writeMain();

        // junit @before @after
        newLine();
        write(SPACE + "//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        writeBeforeAfter();
        write(SPACE + "//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        newLine();

        // filter modifier
        modifierFilter(getMethods(clz));

        // junit @test
        for (Iterator<MethodType> it = methodMap.keySet().iterator(); it.hasNext();) {
            MethodType modifier = it.next();
            if (modifier.isDoPrint()) {
                newLine();
                newLine();
                write(SPACE + SPACE + "//" + modifier);

                // print method group
                writeSystemOutInfo(methodMap.get(modifier));
            }
        }

        write("}");
        closeWriter();

        return this;
    }

    private Method[] getMethods(Class<?> clz) {
        List<Method> list = new ArrayList<Method>();
        list.addAll(Arrays.asList(clz.getDeclaredMethods()));
        list.addAll(Arrays.asList(clz.getMethods()));
        return (Method[]) list.toArray(new Method[list.size()]);
    }

    protected void writeMain() throws Exception {
        newLine();
        write(SPACE + "public static void main(String[] args) throws Exception{");
        write(SPACE + SPACE
                + String.format("%sTest %s = new %sTest();", clz.getSimpleName(), "test", clz.getSimpleName()));
        write(SPACE + SPACE + "test.writeClassInfo();");
        write(SPACE + "}");
        newLine();

        System.out.println("api map = " + apiMap);

        write(SPACE + "@Ignore");
        write(ClassInfoMaker.newInstance(clz)//
                .apiMap(apiMap)//
                .needTab(true)//
                .fieldName(FIELD_NAME)//
                .modeStringBuffer()//
                .writeClassInfo()//
                .getWriter()//
                .toString());//
        newLine();
    }

    public JUnitMaker setPrefix(String prefix) {
        PREFIX = prefix;
        return this;
    }

    public JUnitMaker allowPrivate(boolean allowPrivate) {
        MethodType.PRIVATE.setDoPrint(allowPrivate);
        return this;
    }

    public JUnitMaker allowPackage(boolean allowPackage) {
        MethodType.PACKAGE.setDoPrint(allowPackage);
        return this;
    }

    public JUnitMaker allowProtected(boolean allowProtected) {
        MethodType.PROTECTED.setDoPrint(allowProtected);
        return this;
    }

    public JUnitMaker allowOther(boolean allowOther) {
        MethodType.OTHER.setDoPrint(allowOther);
        return this;
    }

    public JUnitMaker setIgnore(boolean ignore) {
        this.ignore = ignore;
        return this;
    }

    public JUnitMaker setFieldName(String fieldName) {
        FIELD_NAME = fieldName;
        return this;
    }

    public JUnitMaker apiMap(Map<String, String> apiMap) {
        this.apiMap = apiMap;
        return this;
    }

    public File getWriteTo() {
        return writeTo;
    }

    public File getGenerateFile() {
        return this.generateFile;
    }

    private void writeSystemOutInfo(Collection<Method> methods) throws Exception {
        Map<Map<String, Object>, Collection<Method>> categoryMap = MapUtil.categoryMapByGetter(methods, "getName");
        for (Iterator<Map<String, Object>> it = categoryMap.keySet().iterator(); it.hasNext();) {
            Collection<Method> list = categoryMap.get(it.next());
            writeTestMethod(list);
        }
    }

    private void writeTestMethod(Collection<Method> methods) throws IOException {
        int frist = 0;
        for (Method method : methods) {

            // write API
            writeApi(method);

            String methodName = method.getName().substring(0, 1).toUpperCase() + method.getName().substring(1);
            if (frist == 0) {
                String ignorePrefix = "";
                if (ignore) {
                    ignorePrefix = "//";
                }
                write(SPACE + String.format("%s@Ignore", ignorePrefix));
                // write(SPACE + "@Test"); //會出現莫名錯誤
                write(SPACE + "@org.junit.Test");
                write(SPACE + "public void test" + methodName + "() throws Exception {");
                write(SPACE + SPACE + "log.debug(\"# test" + methodName + " ...\");");
            }

            boolean isvoid = false;
            boolean isstatic = false;
            boolean hasParameter = false;

            if (method.getReturnType() == void.class) {
                isvoid = true;
            }
            if (Modifier.isStatic(method.getModifiers())) {
                isstatic = true;
            }
            if (method.getParameterTypes().length > 0) {
                hasParameter = true;
            }

            // write Method
            write(SPACE + SPACE + PrintFormat.matcher(isvoid, isstatic, hasParameter).apply(method, clz));

            frist++;
        }
        write(SPACE + "}");
    }

    private void writeApi(Method method) throws IOException {
        if (apiMap == null) {
            return;
        }
        if (!apiMap.containsKey(method.getName())) {
            return;
        }
        String content = apiMap.get(method.getName());
        write(SPACE + "/**");
        write(SPACE + "* " + content);
        write(SPACE + "*/");
    }

    private void writeBeforeAfter() throws IOException {
        write(SPACE + "@BeforeClass                                                       ");
        write(SPACE + "public static void setUpBeforeClass() throws Exception {           ");
        write(SPACE + "    //log.debug(\"# setUpBeforeClass ...\");                ");
        write(SPACE + "}                                                                  ");
        write(SPACE + "@AfterClass                                                        ");
        write(SPACE + "public static void tearDownAfterClass() throws Exception {         ");
        write(SPACE + "    //log.debug(\"# tearDownAfterClass ...\");              ");
        write(SPACE + "}                                                                  ");
        write(SPACE + "@Before                                                            ");
        write(SPACE + "public void setUp() throws Exception {                             ");
        write(SPACE + "    //log.debug(\"# setUp ...\");                           ");
        write(SPACE + SPACE + " " + FIELD_NAME + " = new " + clz.getSimpleName() + "();");
        write(SPACE + "}                                                                  ");
        write(SPACE + "@After                                                             ");
        write(SPACE + "public void tearDown() throws Exception {                          ");
        write(SPACE + "    //log.debug(\"# tearDown ...\");                        ");
        write(SPACE + SPACE + " " + FIELD_NAME + " = null;");
        write(SPACE + "}                                                                  ");
    }

    private static enum PrintFormat {
        VOID_STATIC_NO_PARAMETER(true, true, false) {
            @Override
            public String apply(Method method, Class<?> clz) {
                return String.format(NO_LOG_FORMAT, clz.getSimpleName() + "." + method.getName(), "");
            }
        }, //
        VOID_NO_PARAMETER(true, false, false) {
            @Override
            public String apply(Method method, Class<?> clz) {
                return String.format(NO_LOG_FORMAT, FIELD_NAME + "." + method.getName(), "");
            }
        }, //
        VOID_HAS_PARAMETER(true, false, true) {
            @Override
            public String apply(Method method, Class<?> clz) {
                return String.format(NO_LOG_FORMAT, FIELD_NAME + "." + method.getName(), appndParameters(method));
            }
        }, //
        VOID_STATIC_HAS_PARAMETER(true, true, true) {
            @Override
            public String apply(Method method, Class<?> clz) {
                return String.format(NO_LOG_FORMAT, clz.getSimpleName() + "." + method.getName(),
                        appndParameters(method));
            }
        }, //
        RETURN_STATIC_NO_PARAMETER(false, true, false) {
            @Override
            public String apply(Method method, Class<?> clz) {
                return String.format(LOG_FORMAT, PREFIX, method.getName(),
                        clz.getSimpleName() + "." + method.getName(), "");
            }
        }, //
        RETURN_NO_PARAMETER(false, false, false) {
            @Override
            public String apply(Method method, Class<?> clz) {
                return String.format(LOG_FORMAT, PREFIX, method.getName(), FIELD_NAME + "." + method.getName(), "");
            }
        }, //
        RETURN_STATIC_HAS_PARAMETER(false, true, true) {
            @Override
            public String apply(Method method, Class<?> clz) {
                return String.format(LOG_FORMAT, PREFIX, method.getName(),
                        clz.getSimpleName() + "." + method.getName(), appndParameters(method));
            }
        }, //
        RETURN_HAS_PARAMETER(false, false, true) {
            @Override
            public String apply(Method method, Class<?> clz) {
                return String.format(LOG_FORMAT, PREFIX, method.getName(), FIELD_NAME + "." + method.getName(),
                        appndParameters(method));
            }
        }, //
        ;

        public static PrintFormat matcher(boolean avoid, boolean astatic, boolean ahasParameter) {
            for (PrintFormat pf : PrintFormat.values()) {
                if (Arrays.equals(pf.config, new boolean[] { avoid, astatic, ahasParameter })) {
                    return pf;
                }
            }
            throw new RuntimeException("no match : isvoid/isstatic/hasParameter : " + avoid + "/" + astatic + "/"
                    + ahasParameter);
        }

        private boolean[] config;

        PrintFormat(boolean isVoid, boolean isStatic, boolean hasParameter) {
            config = new boolean[] { isVoid, isStatic, hasParameter };
        }

        private final static String LOG_FORMAT = "log.debug(\"%s%s = \" + %s(%s));";
        private final static String NO_LOG_FORMAT = "%s(%s);";

        private static String appndParameters(Method method) {
            StringBuilder sb = new StringBuilder();
            for (int ii = 0, total = method.getParameterTypes().length; ii < total; ii++) {
                Class<?> param = method.getParameterTypes()[ii];
                if (total - 1 == ii) {
                    sb.append(param.getName());
                } else {
                    sb.append(param.getName() + ", ");
                }
            }
            return sb.toString();
        }

        public abstract String apply(Method method, Class<?> clz);
    }

    private JUnitMaker(Class<?> clz, String packageUrl) throws UnsupportedEncodingException, FileNotFoundException {
        this.clz = clz;
        this.packageUrl = packageUrl;
        init();
    }

    private void init() throws UnsupportedEncodingException, FileNotFoundException {
        String basepath = null;
        if (packageUrl == null) {
            basepath = ClassPathUtil.getJavaFilePath(JUnitMaker.class);
        } else {
            basepath = System.getProperty("user.dir") + "\\src\\" + packageUrl.replaceAll(Pattern.quote("."), "/")
                    + "\\";
        }
        setWriteTo(new File(basepath));
    }

    public JUnitMaker setWriteTo(File file) throws UnsupportedEncodingException, FileNotFoundException {
        if (!file.isDirectory()) {
            throw new RuntimeException("必須設定為目錄 : " + file);
        }
        writeTo = file;
        return this;
    }

    /**
     * @param clz
     *            要產生junit的class
     * @param writeToClass
     *            要寫到目的的class(放在同一個package)
     * @return
     * @throws UnsupportedEncodingException
     * @throws FileNotFoundException
     */
    public static JUnitMaker newInstance(Class<?> clz, String packageUrl) throws UnsupportedEncodingException,
            FileNotFoundException {
        return new JUnitMaker(clz, packageUrl);
    }

    private void newLine() throws IOException {
        writer.newLine();
    }

    private void write(String message) throws IOException {
        writer.write(message);
        writer.newLine();
    }

    private void closeWriter() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void modifierFilter(Method[] methods) {
        methodMap = new EnumMap<MethodType, Collection<Method>>(MethodType.class);
        for (Method method : methods) {
            int modifier = method.getModifiers();
            MethodType key = null;
            if (Modifier.isPublic(modifier)) {
                key = MethodType.PUBLIC;
            } else if (Modifier.isProtected(modifier)) {
                key = MethodType.PROTECTED;
            } else if (Modifier.isPrivate(modifier)) {
                key = MethodType.PRIVATE;
            } else if (modifier == 0) {
                key = MethodType.PACKAGE;
            } else {
                key = MethodType.OTHER;
            }
            MapUtil.putAsCollection(key, method, methodMap);
        }
    }

    private enum MethodType {
        PUBLIC(true) {
        },
        PROTECTED(true) {
        },
        PACKAGE(true) {
        },
        PRIVATE(true) {
        },
        OTHER(true) {
        },
        ;

        private boolean doPrint;

        MethodType(boolean doPrint) {
            this.doPrint = doPrint;
        }

        public boolean isDoPrint() {
            return doPrint;
        }

        public void setDoPrint(boolean doPrint) {
            this.doPrint = doPrint;
        }
    }
}
