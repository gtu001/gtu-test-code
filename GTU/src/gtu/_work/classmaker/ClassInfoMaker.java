package gtu._work.classmaker;

import gtu.class_.ClassPathUtil;

import java.beans.PropertyDescriptor;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

/**
 * 產生一個 System.out 所有 class method的 test class
 * 
 * @author Troy
 */
public class ClassInfoMaker {

    public static void main(String[] args) throws Exception {
        ClassInfoMaker.newInstance(PropertyDescriptor.class)//
                .needTab(true)//
                .fieldName("prop")//
                .modeSysout()//
                .apiMap(null)//
                // .modeWriteFile()//
                // .modeStringBuffer()//
                .execute();//
        // .writeClassInfo().getWriter().toString();
        System.out.println("done...");
    }

    public void execute() throws Exception {
        if (this.writer == null) {
            throw new Exception("必須先選擇模式 mode*()");
        }
        if (fieldName == null || fieldName.length() == 0) {
            throw new Exception("fieldName 不可為 null");
        }

        write("package " + ClassInfoMaker.class.getPackage().getName() + ";");
        newLine();
        write("import " + clz.getName() + ";");
        newLine();
        write("import org.apache.log4j.Logger;");
        write("import gtu._work.classmaker.ToStringUtil;");
        newLine();
        write("public class " + clz.getSimpleName() + "Test {");
        newLine();
        write(SPACE + "private static Logger log = Logger.getLogger(getClass());");
        newLine();

        write(SPACE + "public static void main(String[] args) throws Exception{");
        write(SPACE + SPACE + String.format("%sTest %s = new %sTest();", clz.getSimpleName(), fieldName, clz.getSimpleName()));
        write(SPACE + SPACE + fieldName + ".writeClassInfo();");
        write(SPACE + "}");
        newLine();

        writeClassInfo();
        newLine();

        write("}");
        closeWriter();
    }

    public ClassInfoMaker writeClassInfo() throws IOException {
        write(SPACE + "public void writeClassInfo() throws Exception {");
        write(SPACE + SPACE + String.format("%s %s = new %s();", clz.getSimpleName(), fieldName, clz.getSimpleName()));
        methodFilter(getMethods(clz));
        for (Iterator<String> it = methodMap.keySet().iterator(); it.hasNext();) {
            String modifier = it.next();
            newLine();
            write(SPACE + SPACE + "//" + modifier);
            writeSystemOutInfo(methodMap.get(modifier));
        }
        write(SPACE + "}");
        return this;
    }

    private Method[] getMethods(Class<?> clz) {
        List<Method> list = new ArrayList<Method>();
        list.addAll(Arrays.asList(clz.getDeclaredMethods()));
        list.addAll(Arrays.asList(clz.getMethods()));
        return (Method[]) list.toArray(new Method[list.size()]);
    }

    private void writeSystemOutInfo(Collection<Method> methods) throws IOException {
        List<Method> hasParam = new ArrayList<Method>();
        List<Method> noParam = new ArrayList<Method>();

        for (Method method : methods) {
            if (method.getParameterTypes().length == 0) {
                noParam.add(method);
            } else {
                hasParam.add(method);
            }
        }

        for (Method method : noParam) {
            writeApi(method);
            writeMethod(method);
        }

        write(SPACE + SPACE + "//-------------------------------------------------------------------------");

        for (Method method : hasParam) {
            writeApi(method);
            writeMethod(method);
        }
    }

    private void writeApi(Method method) throws IOException {
        if (apiMap == null) {
            return;
        }
        if (!apiMap.containsKey(method.getName())) {
            return;
        }
        String content = apiMap.get(method.getName());
        write(SPACE + SPACE + "//" + content);
    }

    private void writeMethod(Method method) throws IOException {
        ReturnMethod rtnMethod = ReturnMethod.matcher(method);
        String format = rtnMethod.format;
        String callMethod = StaticMethod.matcher(method).apply(method, this);
        String parameter = HasParameterMethod.matcher(method).apply(method);
        String callMethod2 = ReturnMethod.matcher(method).apply(callMethod, parameter);
        // System.out.println("=========================================================================");
        // System.out.println(method.getName());
        // System.out.println("format = " + format);
        // System.out.println("callMethod = " + callMethod);
        // System.out.println("parameter = " + parameter);
        // System.out.println("callMethod2 = " + callMethod2);
        // System.out.println("=========================================================================");
        write(SPACE + SPACE + rtnMethod.applyFormat(format, PREFIX, callMethod, callMethod2));
    }

    enum HasParameterMethod {
        YES {
            public String apply(Method method) {
                return writeParameters(method);
            }
        }, //
        NO {
            public String apply(Method method) {
                return "";
            }
        }, //
        ;

        public static HasParameterMethod matcher(Method method) {
            if (method.getParameterTypes().length == 0) {
                return NO;
            } else {
                return YES;
            }
        }

        public abstract String apply(Method method);

        private static String writeParameters(Method method) {
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
    }

    enum ReturnMethod {
        NO("%s;") {
            public String apply(String methodCaller, String writeParameter) {
                return methodCaller + "(" + writeParameter + ")";
            }

            public String applyFormat(String format, String prefix, String callMethod1, String callMethod2) {
                return String.format(format, callMethod2);
            }
        }, //
        YES("log.debug(\"%s%s = \" + %s);") {
            public String apply(String methodCaller, String writeParameter) {
                return "ToStringUtil.toString(" + methodCaller + "(" + writeParameter + "))";
            }

            public String applyFormat(String format, String prefix, String callMethod1, String callMethod2) {
                return String.format(format, prefix, callMethod1, callMethod2);
            }
        }, //
        ;

        private String format;

        ReturnMethod(String format) {
            this.format = format;
        }

        public static ReturnMethod matcher(Method method) {
            if (method.getReturnType() == void.class) {
                return ReturnMethod.NO;
            } else {
                return ReturnMethod.YES;
            }
        }

        public abstract String apply(String methodCaller, String writeParameter);

        public abstract String applyFormat(String format, String prefix, String callMethod1, String callMethod2);
    }

    enum StaticMethod {
        NORMAL {
            public String apply(Method method, ClassInfoMaker maker) {
                return maker.fieldName + "." + method.getName();
            }
        }, //
        STATIC {
            public String apply(Method method, ClassInfoMaker maker) {
                return maker.clz.getSimpleName() + "." + method.getName();
            }
        }, //
        ;

        public static StaticMethod matcher(Method method) {
            if (Modifier.isStatic(method.getModifiers())) {
                return StaticMethod.STATIC;
            } else {
                return StaticMethod.NORMAL;
            }
        }

        public abstract String apply(Method method, ClassInfoMaker maker);
    }

    private TWriter writer;
    private Class<?> clz;
    private Multimap<String, Method> methodMap;
    private String fieldName = "test";
    private static final String SPACE = "    ";
    private static String PREFIX = "\\t";
    private Map<String, String> apiMap;

    public static ClassInfoMaker newInstance(Class<?> clz) throws UnsupportedEncodingException, FileNotFoundException {
        return new ClassInfoMaker(clz);
    }

    private ClassInfoMaker(Class<?> clz) throws UnsupportedEncodingException, FileNotFoundException {
        this.clz = clz;
    }

    public ClassInfoMaker apiMap(Map<String, String> apiMap) {
        this.apiMap = apiMap;
        return this;
    }

    public ClassInfoMaker fieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public ClassInfoMaker needTab(boolean needTab) {
        if (needTab) {
            PREFIX = "\\t";
        } else {
            PREFIX = "";
        }
        return this;
    }

    public ClassInfoMaker modeSysout() throws Exception {
        writer = Mode.SYSTEMOUT.createWriter(clz);
        return this;
    }

    public ClassInfoMaker modeWriteFile() throws Exception {
        writer = Mode.BUFFEREDWRITER.createWriter(clz);
        return this;
    }

    public ClassInfoMaker modeStringBuffer() throws Exception {
        writer = Mode.STRINGBUFFER.createWriter(clz);
        return this;
    }

    enum Mode {
        STRINGBUFFER {
            public TWriter createWriter(Class<?> clz) throws Exception {
                return new MyStringBuffer();
            }
        },
        SYSTEMOUT {
            public TWriter createWriter(Class<?> clz) throws Exception {
                return new SystemOutWriter(System.out);
            }
        },
        BUFFEREDWRITER {
            public TWriter createWriter(Class<?> clz) throws Exception {
                String fileName = clz.getSimpleName() + "Test.java";
                File writeTo = new File(ClassPathUtil.getJavaFilePath(ClassInfoMaker.class) + fileName);
                return new MyBufferedWriter(new OutputStreamWriter(new FileOutputStream(writeTo), "UTF8"));
            }
        };

        public abstract TWriter createWriter(Class<?> clz) throws Exception;
    }

    public interface TWriter {
        public void newLine() throws IOException;

        public void write(String message) throws IOException;

        public void close() throws IOException;

        public String toString();
    }

    static class MyStringBuffer implements TWriter {
        private StringBuffer sb = new StringBuffer();

        public void newLine() throws IOException {
            sb.append("\r\n");
        }

        public void write(String message) throws IOException {
            sb.append(message);
        }

        public void close() throws IOException {
        }

        public String toString() {
            return sb.toString();
        }
    }

    static class SystemOutWriter extends PrintStream implements TWriter {
        public SystemOutWriter(OutputStream paramOutputStream) {
            super(paramOutputStream);
        }

        public void newLine() throws IOException {
            super.println();
        }

        public void write(String message) throws IOException {
            super.print(message);
        }
    }

    static class MyBufferedWriter extends BufferedWriter implements TWriter {
        public MyBufferedWriter(Writer paramWriter) {
            super(paramWriter);
        }
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

    private void methodFilter(Method[] methods) {
        ImmutableMultimap.Builder<String, Method> builder = ImmutableMultimap.builder();
        for (Method method : methods) {
            int modifier = method.getModifiers();
            String key = null;
            if (Modifier.isPublic(modifier)) {
                key = "PUBLIC";
            } else if (Modifier.isProtected(modifier)) {
                key = "PROTECTED";
            } else if (Modifier.isPrivate(modifier)) {
                key = "PRIVATE";
            } else if (modifier == 0) {
                key = "PACKAGE";
            } else {
                key = "OTHER";
            }
            builder.put(key, method);
        }
        methodMap = builder.build();
    }

    public TWriter getWriter() {
        return writer;
    }
}
