package gtu.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

//import javassist.Modifier;
//import org.apache.commons.lang.StringUtils;

public final class ToStringUtil {

    public static void main(String[] args) {
        ToStringUtil.Setting.DEBUG.apply();

        ArrayList<String> list = new ArrayList<String>();
        list.add("sadfasdfsafd");
        list.add("sadfasdfsafd");
        
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("aaaa", "aaaa");
        map.put("bbbb", "BBB");
        map.put("zzz1", 11);
        map.put("zzz2", 2L);
        map.put("zzz3", true);
        
        System.out.println(ToStringUtil.toString(list));
        System.out.println(ToStringUtil.toString(map));
        System.out.println("done...");
        System.out.println("-------------- main - end");
    }

    private ToStringUtil() {
    }

    private static final DontInvokeMethod DEFAULT_DONT_INVOKE_METHOD = DontInvokeMethod.defaultCreate();

    private static boolean EXCEPTION_PRINTSTACKTRACE;
    private static boolean SHOW_FIELD_MESSAGE;
    private static boolean SHOW_METHOD_MESSAGE;
    private static String CHANGE_LINE;
    private static String PREFIX_TAB;
    private static DontInvokeMethod DONT_INVOKE_METHOD;
    private static Setting SETTINIG = Setting.DEFAULT;
    private static TypeMatch CLASS_INFO_MODE;

    static {
        init();
    }

    public static void init() {
        SETTINIG.apply();
    }

    //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    public static String invokeField(Object obj) {
        return invokeField(obj, ".*", false);
    }

    public static String invokeMethod(Object obj) {
        return invokeMethod(obj, ".*", false);
    }

    public static String invokeField(Object obj, String regex) {
        return invokeField(obj, regex, false);
    }

    public static String invokeMethod(Object obj, String regex) {
        return invokeMethod(obj, regex, false);
    }

    public static String invokeField(Object obj, String regex, boolean ignoreNull) {
        if (obj == null) {
            return "{object == null}";
        }
        class F implements Comparable<F> {
            String type;
            Field field;

            F(String type, Field field) {
                this.type = type;
                this.field = field;
            }

            @Override
            public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((field == null) ? 0 : field.hashCode());
                return result;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj)
                    return true;
                if (obj == null)
                    return false;
                if (getClass() != obj.getClass())
                    return false;
                F other = (F) obj;
                if (field == null) {
                    if (other.field != null)
                        return false;
                } else if (!field.equals(other.field))
                    return false;
                return true;
            }

            public int compareTo(F arg0) {
                return this.field.getName().compareToIgnoreCase(arg0.field.getName());
            }
        }
        StringBuilder sb = new StringBuilder();
        Class<?> clz = obj.getClass();
        appendTitle(obj, clz, sb);
        Set<F> fields = new HashSet<F>();
        for (Field field : clz.getFields()) {
            fields.add(new F("P", field));
        }
        for (Field field : clz.getDeclaredFields()) {
            fields.add(new F("D", field));
        }
        List<F> list = new ArrayList<F>(fields);
        Collections.sort(list);
        for (F f : list) {
            if (!f.field.getName().matches(regex)) {
                continue;
            }
            Object val = null;
            try {
                if (!f.field.isAccessible()) {
                    f.field.setAccessible(true);
                    val = f.field.get(obj);
                    f.field.setAccessible(false);
                } else {
                    val = f.field.get(obj);
                }
            } catch (Exception e) {
                exceptionHandler(e);
            }
            if (!(val == null && ignoreNull)) {
                sb.append(String.format("%s\t(%s)[%s] ==> %s%s", (f.type.equals("P") ? "-" : ""), getModifier(f.field), f.field.getName(), ToStringUtil.toString(val), CHANGE_LINE));
            }
        }
        appendEnd(sb);
        return sb.toString();
    }

    public static String invokeMethod(Object obj, String regex, boolean ignoreNull) {
        if (obj == null) {
            return "{object == null}";
        }
        class M implements Comparable<M> {
            String type;
            Method method;

            M(String type, Method method) {
                this.type = type;
                this.method = method;
            }

            @Override
            public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((method == null) ? 0 : method.hashCode());
                return result;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj)
                    return true;
                if (obj == null)
                    return false;
                if (getClass() != obj.getClass())
                    return false;
                M other = (M) obj;
                if (method == null) {
                    if (other.method != null)
                        return false;
                } else if (!method.equals(other.method))
                    return false;
                return true;
            }

            public int compareTo(M arg0) {
                return this.method.getName().compareToIgnoreCase(arg0.method.getName());
            }
        }
        StringBuilder sb = new StringBuilder();
        Class<?> clz = obj.getClass();
        appendTitle(obj, clz, sb);
        Set<M> methods = new HashSet<M>();
        for (Method method : clz.getMethods()) {
            if (method.getReturnType() != void.class && method.getParameterTypes().length == 0) {
                methods.add(new M("P", method));
            }
        }
        for (Method method : clz.getDeclaredMethods()) {
            if (method.getReturnType() != void.class && method.getParameterTypes().length == 0) {
                methods.add(new M("D", method));
            }
        }
        List<M> list = new ArrayList<M>(methods);
        Collections.sort(list);
        for (M m : list) {
            if (!m.method.getName().matches(regex)) {
                continue;
            }
            Object val = null;
            try {
                if (!m.method.isAccessible()) {
                    m.method.setAccessible(true);
                    val = m.method.invoke(obj, new Object[0]);
                    m.method.setAccessible(false);
                } else {
                    val = m.method.invoke(obj, new Object[0]);
                }
            } catch (Exception e) {
                exceptionHandler(e);
            }
            if (!(val == null && ignoreNull)) {
                sb.append(String.format("%s\t(%s)[%s] ==> %s%s", (m.type.equals("P") ? "-" : ""), getModifier(m.method), m.method.getName(), ToStringUtil.toString(val), CHANGE_LINE));
            }
        }
        appendEnd(sb);
        return sb.toString();
    }

    public static String toString(Object obj) {
        Class<?> clz = null;
        if (obj != null) {
            clz = obj.getClass();
        }
        return toString(obj, clz);
    }

    public static String toString(Class<?> clz) {
        return toString(clz, clz);
    }

    public static String toString(Object obj, Class<?> clz) {
        String rtn = null;
        try {
            rtn = TypeMatch.fromValue(obj, clz).toString(obj, clz);
        } catch (Exception ex) {
            exceptionHandler(ex);
        }
        init();
        return rtn;
    }

    public static String toString(Object obj, TypeMatch typeMatch) {
        String rtn = null;
        try {
            if (obj == null) {
                return toString(obj);
            }
            rtn = typeMatch.apply(obj, obj.getClass());
        } catch (Exception ex) {
            exceptionHandler(ex);
        }
        init();
        return rtn;
    }

    static abstract class CompareTwoObject {
        abstract void fieldDiff(Field f, Object fromVal, Object toVal);

        abstract void methodDiff(Method m, Object fromVal, Object toVal);

        int fieldDiffCount;

        int fieldDiffCount() {
            return fieldDiffCount;
        }

        int methodDiffCount;

        int methodDiffCount() {
            return fieldDiffCount;
        }

        Class<?> clz = null;

        Class<?> getClz() {
            return clz;
        }

        StringBuilder sb = null;

        StringBuilder getBuffer() {
            return sb;
        }
    }

    public static String compareDiff(Object from, Object to) {
        return compareDiff(from, to, new CompareTwoObject() {
            void fieldDiff(Field f, Object fromVal, Object toVal) {
                sbappendTabLine(String.format("(%s)[%s] ==> from:[%s],to:[%s]", getModifier(f), f.getName(), fromVal, toVal), sb);
            }

            void methodDiff(Method m, Object fromVal, Object toVal) {
                sbappendTabLine(String.format("(%s)[%s] ==> from:[%s],to:[%s]", getModifier(m), m.getName(), fromVal, toVal), sb);
            }
        });
    }

    public static String compareDiffSummary(Object from, Object to) {
        final List<String> fL = new ArrayList<String>();
        final List<String> mL = new ArrayList<String>();
        CompareTwoObject compare = new CompareTwoObject() {
            void fieldDiff(Field f, Object fromVal, Object toVal) {
                fL.add(f.getName());
            }

            void methodDiff(Method m, Object fromVal, Object toVal) {
                mL.add(m.getName());
            }
        };
        compareDiff(from, to, compare);
        return String.format("[COMPARE summary][%s](field:%d)%s,(method:%d)%s", compare.getClz(), fL.size(), fL, mL.size(), mL);
    }

    public static Map<String, Object> compareDiffMap(Object from, Object to) {
        final Map<String, Object> rtn = new HashMap<String, Object>();
        rtn.put("field", new HashSet<Field>());
        rtn.put("method", new HashSet<Method>());
        compareDiff(from, to, new CompareTwoObject() {
            @SuppressWarnings("unchecked")
            void fieldDiff(Field f, Object fromVal, Object toVal) {
                rtn.put("from_" + f.getName(), fromVal);
                rtn.put("to_" + f.getName(), toVal);
                ((Set<Field>) rtn.get("field")).add(f);
            }

            @SuppressWarnings("unchecked")
            void methodDiff(Method m, Object fromVal, Object toVal) {
                rtn.put("from_" + m.getName(), fromVal);
                rtn.put("to_" + m.getName(), toVal);
                ((Set<Method>) rtn.get("method")).add(m);
            }
        });
        return rtn;
    }

    private static String compareDiff(Object from, Object to, CompareTwoObject compareTwoObject) {
        if (from == null || to == null) {
            return String.format("[value can't be null!][from:%s,to:%s]", from, to);
        }
        Class<?> clz = null;
        if ((clz = from.getClass()) != to.getClass()) {
            return String.format("[class can't be different!][from:%s,to:%s]", from.getClass(), to.getClass());
        }
        compareTwoObject.clz = clz;
        StringBuilder sb = new StringBuilder();
        compareTwoObject.sb = sb;
        sb.append(String.format("[COMPARE list different][class:%s]%s", clz, CHANGE_LINE));
        Set<Field> fieldSet = new HashSet<Field>();
        for (Field f : clz.getDeclaredFields()) {
            fieldSet.add(f);
        }
        for (Field f : clz.getFields()) {
            fieldSet.add(f);
        }
        Object arg1 = null;
        Object arg2 = null;
        boolean compareResult = false;
        sb.append(String.format("(field)%s", CHANGE_LINE));
        int fieldDiefCount = 0;
        for (Field f : fieldSet) {
            boolean ac = f.isAccessible();
            f.setAccessible(true);
            try {
                arg1 = f.get(from);
                arg2 = f.get(to);
                compareResult = arg1 != null ? arg1.equals(arg2) : (arg2 == null ? true : false);
                if (!compareResult) {
                    compareTwoObject.fieldDiff(f, arg1, arg2);
                    fieldDiefCount++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            f.setAccessible(ac);
        }
        Set<Method> methodSet = new HashSet<Method>();
        for (Method m : clz.getDeclaredMethods()) {
            if (m.getParameterTypes().length == 0 && m.getReturnType() != void.class) {
                methodSet.add(m);
            }
        }
        for (Method m : clz.getMethods()) {
            if (m.getParameterTypes().length == 0 && m.getReturnType() != void.class) {
                methodSet.add(m);
            }
        }
        sb.append(String.format("(method)%s", CHANGE_LINE));
        int methodDiffCount = 0;
        for (Method m : methodSet) {
            boolean ac = m.isAccessible();
            m.setAccessible(true);
            try {
                arg1 = m.invoke(from, new Object[0]);
                arg2 = m.invoke(to, new Object[0]);
                compareResult = arg1 != null ? arg1.equals(arg2) : (arg2 == null ? true : false);
                if (!compareResult) {
                    compareTwoObject.methodDiff(m, arg1, arg2);
                    methodDiffCount++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            m.setAccessible(ac);
        }
        appendEnd(sb);
        compareTwoObject.fieldDiffCount = fieldDiefCount;
        compareTwoObject.methodDiffCount = methodDiffCount;
        return sb.toString();
    }

    //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

    private static String getMethodMsg(Method method, Object obj) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_TAB + "[" + method.getName() + " = ");
        try {
            boolean access = method.isAccessible();
            method.setAccessible(true);
            Object val = method.invoke(obj, new Object[0]);
            if (val != null) {
                sb.append("([" + val.getClass().getSimpleName() + "]" + getModifier(method) + ")" + val);
            } else {
                sb.append(val);
            }
            method.setAccessible(access);
        } catch (Exception e) {
            exceptionHandler(method.getName(), e, sb);
        }
        sb.append("]" + CHANGE_LINE);
        return sb.toString();
    }

    private static String getFieldMsg(Field field, Object obj) {
        StringBuilder sb = new StringBuilder();
        boolean access = field.isAccessible();
        field.setAccessible(true);
        sb.append(PREFIX_TAB + "[" + field.getName() + " = ");
        try {
            Object val = field.get(obj);
            if (val != null) {
                sb.append("([" + val.getClass().getSimpleName() + "]" + getModifier(field) + ")" + val + ", hash:" + val.hashCode());
            } else {
                sb.append(val);
            }
        } catch (Exception e) {
            exceptionHandler(field.getName(), e, sb);
        }
        field.setAccessible(access);
        sb.append("]" + CHANGE_LINE);
        return sb.toString();
    }

    private static void exceptionHandler(Exception ex) {
        exceptionHandler(null, ex, null);
    }

    private static void exceptionHandler(String message, Exception ex) {
        exceptionHandler(message, ex, null);
    }

    private static void exceptionHandler(String message, Exception ex, StringBuilder sb) {
        if (EXCEPTION_PRINTSTACKTRACE) {
            ex.printStackTrace();
        }
        if (sb != null) {
            sbappendTabLine("[error][" + message + "][message : " + ex.toString() + "]", sb);
        }
    }

    private static String getModifier(Member member) {
        return getModifier(member, -1);
    }

    private static String getModifier(Member member, int modifier) {
        StringBuilder sb = new StringBuilder();
        final String prefix = "-";
        final String rearfix = "";
        int mod = modifier;
        if (member != null) {
            mod = member.getModifiers();
        }

        if (Modifier.isPublic(mod))
            sb.append(prefix + "<+>" + rearfix);
        if (Modifier.isPrivate(mod))
            sb.append(prefix + "<->" + rearfix);
        if (Modifier.isProtected(mod))
            sb.append(prefix + "<#>" + rearfix);
        if (Modifier.isPackage(mod))
            sb.append(prefix + "<~>" + rearfix);
        if (Modifier.isStatic(mod))
            sb.append(prefix + "S" + rearfix);
        if (Modifier.isFinal(mod))
            sb.append(prefix + "F" + rearfix);
        if (Modifier.isSynchronized(mod))
            sb.append(prefix + "Syn" + rearfix);
        if (Modifier.isVolatile(mod))
            sb.append(prefix + "Vol" + rearfix);
        if (Modifier.isTransient(mod))
            sb.append(prefix + "Tra" + rearfix);
        if (Modifier.isNative(mod))
            sb.append(prefix + "Nat" + rearfix);
        if (Modifier.isInterface(mod))
            sb.append(prefix + "Inf" + rearfix);
        if (Modifier.isAnnotation(mod))
            sb.append(prefix + "Ann" + rearfix);
        if (Modifier.isEnum(mod))
            sb.append(prefix + "Enu" + rearfix);
        if (Modifier.isAbstract(mod))
            sb.append(prefix + "Abs" + rearfix);
        if (Modifier.isStrict(mod))
            sb.append(prefix + "Stc" + rearfix);
        return sb.toString();
    }

    static class DontInvokeMethod {
        Set<Method> methods;

        @SuppressWarnings("unchecked")
        private static DontInvokeMethod defaultCreate() {
            try {
                DontInvokeMethod ali = new DontInvokeMethod();
                ali.methods = new HashSet<Method>();
                ali.add(Object.class, //
                        Object.class.getMethod("getClass", new Class[0]), Object.class.getMethod("toString", new Class[0]));
                ali.methods = Collections.unmodifiableSet(ali.methods);
                return ali;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return DontInvokeMethod.newInstance();
        }

        private static DontInvokeMethod newInstance() {
            DontInvokeMethod ali = new DontInvokeMethod();
            ali.methods = new HashSet<Method>();
            return ali;
        }

        private DontInvokeMethod add(Method method) {
            methods.add(method);
            return this;
        }

        private DontInvokeMethod add(Class<?> clz, Method... exception) {
            List<Method> list = Arrays.asList(exception);
            for (Method method : clz.getMethods()) {
                if (!list.contains(method)) {
                    methods.add(method);
                }
            }
            return this;
        }

        private DontInvokeMethod add(Class<?> clz, String... exception) {
            List<String> list = Arrays.asList(exception);
            for (Method method : clz.getMethods()) {
                if (!list.contains(method.getName())) {
                    methods.add(method);
                }
            }
            return this;
        }

        private boolean contains(Method method) {
            String name = method.getName();
            Class<?>[] paraType = method.getParameterTypes();
            Class<?> rtnType = method.getReturnType();
            for (Method m : methods) {
                boolean sameName = name.equals(m.getName());
                boolean sameParaType = Arrays.equals(paraType, m.getParameterTypes());
                boolean sameRtnType = rtnType == m.getReturnType();
                if (sameName && sameParaType && sameRtnType) {
                    return true;
                }
            }
            return false;
        }

        public String toString() {
            //            return ListUtil.getList(Iterables.transform(new Iterable<Method>() {
            //                public Iterator<Method> iterator() {
            //                    return methods.iterator();
            //                }
            //            }, new Function<Method, String>() {
            //                public String apply(Method paramF) {
            //                    return paramF.getName();
            //                }
            //            }).iterator()).toString();
            return this.toString();
        }
    }

    static void DEBUG(Object messageg) {
        if (SETTINIG == Setting.DEBUG) {
            System.err.println(messageg);
        }
    }

    public static enum TypeMatch {
        NULL(new Class[] { Void.TYPE }) {
            public String apply(Object obj, Class<?> clz) {
                return "{object = null}";
            }
        },
        PRIMITIVE_ARRAY(new Class[] { Double[].class, double[].class, Float[].class, float[].class, String[].class, Long[].class, long[].class, Character[].class, char[].class, Byte[].class,
                byte[].class, Integer[].class, int[].class, Boolean[].class, boolean[].class }) {
            public String apply(Object obj, Class<?> clz) {
                return "[" + clz + " = " + Arrays.toString((Object[]) obj) + "]";
            }
        }, //
        OBJECT_ARRAY(new Class[] { Object[].class }) {
            public String apply(Object obj, Class<?> clz) {
                StringBuilder sb = new StringBuilder();
                appendTitle(obj, clz, sb);
                int index = 0;
                Object[] objectArray = (Object[]) obj;
                if (objectArray.length == 0) {
                    sbappendTabLine(IS_EMPTY, sb);
                }
                for (int ii = 0; ii < objectArray.length; ii++) {
                    Object val = objectArray[ii];
                    Class<?> valClz = val != null ? val.getClass() : null;
                    sbappendTabLine("no." + index + " : (" + valClz.getName() + ")[" + val + "]", sb);
                    index++;
                }
                appendEnd(sb);
                return sb.toString();
            }
        }, //
        PRIMITIVE(new Class[] { String.class, Double.class, Float.class, Long.class, Character.class, Byte.class, Integer.class, Boolean.class }) {
            public String apply(Object obj, Class<?> clz) {
                return "[" + clz.getSimpleName() + " = " + obj + "]";
            }
        }, //
        CLASS_DECLARED_MODE(new Class[] { Class.class }) {
            public String apply(Object obj, Class<?> clz) {
                StringBuilder sb = new StringBuilder();
                sb.append("{");
                sb.append("(" + getModifier(null, clz.getModifiers()) + ")" + clz.getName() + CHANGE_LINE);
                for (Class<?> in : clz.getInterfaces()) {
                    sb.append("    (interface)" + in.getName() + CHANGE_LINE);
                }
                for (Class<?> c = clz.getSuperclass(); c != Object.class; c = c.getSuperclass()) {
                    sb.append("  (super)" + c.getName() + CHANGE_LINE);
                    for (Class<?> in : c.getInterfaces()) {
                        sb.append("    (interface)" + in.getName() + CHANGE_LINE);
                    }
                }
                sb.append("  (field)" + CHANGE_LINE);
                for (Field f : clz.getDeclaredFields()) {
                    int modifier = f.getModifiers();
                    Object value = null;
                    if (Modifier.isStatic(modifier) && Modifier.isFinal(modifier)) {
                        try {
                            value = f.get(clz);
                            sbappendTabLine("[" + getModifier(f) + "] " + f.getType().getSimpleName() + " " + f.getName() + " = " + value, sb);
                        } catch (Exception e) {
                        }
                    } else {
                        sbappendTabLine("[" + getModifier(f) + "] " + f.getType().getSimpleName() + " " + f.getName(), sb);
                    }
                }
                sb.append("  (method)" + CHANGE_LINE);
                for (Method m : clz.getDeclaredMethods()) {
                    StringBuilder cb = new StringBuilder();
                    for (Class<?> c : m.getParameterTypes()) {
                        cb.append(c.getSimpleName() + ",");
                    }
                    if (cb.length() != 0) {
                        cb.deleteCharAt(cb.length() - 1);
                    }
                    sbappendTabLine("[" + getModifier(m) + "] " + m.getReturnType().getSimpleName() + " " + m.getName() + " (" + cb + ")", sb);
                }
                appendEnd(sb);
                return sb.toString();
            }
        }, //
        CLASS_FULL_MODE(new Class[] { Class.class }) {
            public String apply(Object obj, Class<?> clz) {
                StringBuilder sb = new StringBuilder();
                sb.append("{");
                sb.append("(" + getModifier(null, clz.getModifiers()) + ")" + clz.getName() + CHANGE_LINE);
                for (Class<?> in : clz.getInterfaces()) {
                    sb.append("    (interface)" + in.getName() + CHANGE_LINE);
                }
                for (Class<?> c = clz.getSuperclass(); c != Object.class; c = c.getSuperclass()) {
                    sb.append("  (super)" + c.getName() + CHANGE_LINE);
                    for (Class<?> in : c.getInterfaces()) {
                        sb.append("    (interface)" + in.getName() + CHANGE_LINE);
                    }
                }
                sb.append("  (field)" + CHANGE_LINE);
                Set<Field> declaredFieldSet = new HashSet<Field>();
                for (Field f : clz.getDeclaredFields()) {
                    declaredFieldSet.add(f);
                    int modifier = f.getModifiers();
                    Object value = null;
                    if (Modifier.isStatic(modifier) && Modifier.isFinal(modifier)) {
                        try {
                            value = f.get(clz);
                            sbappendTabLine("[" + getModifier(f) + "] " + f.getType().getSimpleName() + " " + f.getName() + " = " + value, sb);
                        } catch (Exception e) {
                        }
                    } else {
                        sbappendTabLine("[" + getModifier(f) + "] " + f.getType().getSimpleName() + " " + f.getName(), sb);
                    }
                }
                for (Field f : clz.getFields()) {
                    if (declaredFieldSet.contains(f)) {
                        continue;
                    }
                    int modifier = f.getModifiers();
                    Object value = null;
                    if (Modifier.isStatic(modifier) && Modifier.isFinal(modifier)) {
                        try {
                            value = f.get(clz);
                            sbappendTabLine(StringUtils_rightPad("- [" + getModifier(f) + "] " + f.getType().getSimpleName() + " " + f.getName() + " = " + value, 70) //
                                    + declaredClass(f.getDeclaringClass()), sb);
                        } catch (Exception e) {
                        }
                    } else {
                        sbappendTabLine(StringUtils_rightPad("- [" + getModifier(f) + "] " + f.getType().getSimpleName() + " " + f.getName(), 70) //
                                + declaredClass(f.getDeclaringClass()), sb);
                    }
                }
                sb.append("  (method)" + CHANGE_LINE);
                Set<Method> declaredMethodSet = new HashSet<Method>();
                for (Method m : clz.getDeclaredMethods()) {
                    declaredMethodSet.add(m);
                    StringBuilder cb = new StringBuilder();
                    for (Class<?> c : m.getParameterTypes()) {
                        cb.append(c.getSimpleName() + ",");
                    }
                    if (cb.length() != 0) {
                        cb.deleteCharAt(cb.length() - 1);
                    }
                    sbappendTabLine("[" + getModifier(m) + "] " + m.getReturnType().getSimpleName() + " " + m.getName() + " (" + cb + ")", sb);
                }
                for (Method m : clz.getMethods()) {
                    if (declaredMethodSet.contains(m)) {
                        continue;
                    }
                    StringBuilder cb = new StringBuilder();
                    for (Class<?> c : m.getParameterTypes()) {
                        cb.append(c.getSimpleName() + ",");
                    }
                    if (cb.length() != 0) {
                        cb.deleteCharAt(cb.length() - 1);
                    }
                    sbappendTabLine(StringUtils_rightPad("- [" + getModifier(m) + "] " + m.getReturnType().getSimpleName() + " " + m.getName() + " (" + cb + ")", 70)//
                            + declaredClass(m.getDeclaringClass()), sb);
                }
                appendEnd(sb);
                return sb.toString();
            }
        }, //
        ITERATOR(new Class[] { Iterator.class }) {
            public String apply(Object obj, Class<?> clz) {
                StringBuilder sb = new StringBuilder();
                appendTitle(obj, clz, sb);
                int index = 0;
                for (Iterator<?> it = (Iterator<?>) obj; it.hasNext();) {
                    Object val = it.next();
                    Class<?> valClz = val != null ? val.getClass() : null;
                    sbappendTabLine("no." + index + " : (" + valClz.getName() + ")[" + val + "]", sb);
                    index++;
                }
                if (index == 0) {
                    sbappendTabLine(IS_EMPTY, sb);
                }
                appendEnd(sb);
                return sb.toString();
            }
        }, //
        ENUMERATION(new Class[] { Enumeration.class }) {
            public String apply(Object obj, Class<?> clz) {
                StringBuilder sb = new StringBuilder();
                appendTitle(obj, clz, sb);
                int index = 0;
                for (Enumeration<?> enu = (Enumeration<?>) obj; enu.hasMoreElements();) {
                    Object val = enu.nextElement();
                    Class<?> valClz = val != null ? val.getClass() : null;
                    sbappendTabLine("no." + index + " : (" + valClz.getName() + ")[" + val + "]", sb);
                    index++;
                }
                if (index == 0) {
                    sbappendTabLine(IS_EMPTY, sb);
                }
                appendEnd(sb);
                return sb.toString();
            }
        }, //
        COLLECTION(new Class[] { Collection.class }) {
            public String apply(Object obj, Class<?> clz) {
                StringBuilder sb = new StringBuilder();
                appendTitle(obj, clz, sb);
                int index = 0;
                for (Iterator<?> it = (Iterator<?>) ((Collection<?>) obj).iterator(); it.hasNext();) {
                    Object val = it.next();
                    Class<?> valClz = val != null ? val.getClass() : null;
                    sbappendTabLine("no." + index + " : (" + valClz.getName() + ")[" + val + "]", sb);
                    index++;
                }
                if (index == 0) {
                    sbappendTabLine(IS_EMPTY, sb);
                }
                appendEnd(sb);
                return sb.toString();
            }
        }, //
        MAP(new Class[] { Map.class }) {
            public String apply(Object obj, Class<?> clz) {
                StringBuilder sb = new StringBuilder();
                appendTitle(obj, clz, sb);
                int index = 0;
                if (((Map<?, ?>) obj).isEmpty()) {
                    sbappendTabLine(IS_EMPTY, sb);
                }
                for (Entry<?, ?> entry : ((Map<?, ?>) obj).entrySet()) {
                    Object key = entry.getKey();
                    Object value = entry.getValue();
                    Class<?> keyClz = key != null ? key.getClass() : null;
                    Class<?> valueClz = value != null ? value.getClass() : null;
                    sbappendTabLine("no." + index + " key(" + keyClz.getName() + ")[" + key + "]\tvalue(" + valueClz.getName() + ")[" + value + "]", sb);
                    index++;
                }
                appendEnd(sb);
                return sb.toString();
            }
        }, //
        OBJECT(new Class[] { Object.class }) {
            public String apply(Object obj, Class<?> clz) {
                StringBuilder sb = new StringBuilder();
                appendTitle(obj, clz, sb);

                if (SHOW_FIELD_MESSAGE) {
                    sb.append(" , #FIELD --> " + CHANGE_LINE);
                    for (Field field : clz.getDeclaredFields()) {
                        sb.append(getFieldMsg(field, obj));
                    }
                }

                if (SHOW_METHOD_MESSAGE) {
                    sb.append(" , #METHOD --> " + CHANGE_LINE);
                    for (Method method : clz.getMethods()) {
//                    for (Method method : fetchAllMethod(clz)) {//private方法不應該被呼叫
                        if (method.getReturnType() == void.class) {
                            continue;
                        }
                        if (method.getParameterTypes().length > 0) {
                            continue;
                        }

                        if (!DONT_INVOKE_METHOD.contains(method)) {

                            boolean notDeclaredMethod = false;
                            try {
                                clz.getDeclaredMethod(method.getName(), new Class[0]);
                            } catch (SecurityException e) {
                            } catch (NoSuchMethodException e) {
                                notDeclaredMethod = true;
                            }

                            sb.append((notDeclaredMethod ? "- " : ""));
                            sb.append(getMethodMsg(method, obj));
                        }
                    }
                }
                appendEnd(sb);
                return sb.toString();
            }
        }//
        ;
        private static final String IS_EMPTY = "is EMPTY!!";
        //

        final private Class<?>[] classes;
        
        private static Set<Method> fetchAllMethod(Class<?> clz){
            Set<Method> set = new TreeSet<Method>(new Comparator<Method>() {
                @Override
                public int compare(Method o1, Method o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            if(clz.getDeclaredMethods() != null){
                for(Method m : clz.getDeclaredMethods()){
                    set.add(m);
                }
            }
            if(clz.getMethods() != null){
                for(Method m : clz.getMethods()){
                    set.add(m);
                }
            }
            return set;
        }

        public static TypeMatch fromValue(Object object, Class<?> clz) {
            if (object == null && clz == null) {
                DEBUG("TypeMatch: object == null && clz == null");
                return TypeMatch.NULL;
            }
            if (object == clz && clz instanceof Class) {
                DEBUG("TypeMatch: object == clz && clz instanceof Class");
                return CLASS_INFO_MODE;
            }
            if (clz.isPrimitive()) {
                DEBUG("TypeMatch: clz.isPrimitive()");
                return TypeMatch.PRIMITIVE;
            }
            for (TypeMatch am : TypeMatch.values()) {
                for (Class<?> c : am.classes) {
                    if (c == clz) {
                        DEBUG("TypeMatch: for() c == clz");
                        return am;
                    }
                }
            }
            for (TypeMatch am : TypeMatch.values()) {
                DEBUG("TypeMatch: for() c.isAssignableFrom(clz) => " + am);
                for (Class<?> c : am.classes) {
                    if (c.isAssignableFrom(clz)) {
                        DEBUG("TypeMatch: for() c.isAssignableFrom(clz) = " + c);
                        return am;
                    }
                }
            }
            return TypeMatch.OBJECT;
        }

        TypeMatch(Class<?>[] classes) {
            this.classes = classes;
        }

        String declaredClass(Class<?> clz) {
            return String.format("[declared:%s]", clz.getSimpleName());
        }

        public String toString(Object obj, Class<?> clz) {
            final String prefix = "[" + SETTINIG.toString() + ":" + this.name() + "]";
            final String str = apply(obj, clz);
            final String rearfix = "";
            return String.format("%s%s%s", prefix, str, rearfix);
        }

        public abstract String apply(Object obj, Class<?> clz);
    }

    private static void sbappendTabLine(String message, StringBuilder sb) {
        sb.append(PREFIX_TAB + message + CHANGE_LINE);
    }

    static void appendTitle(Object obj, Class<?> clz, StringBuilder sb) {
        sb.append("{(" + clz.getName() + ") = " + obj + CHANGE_LINE);
    }

    static void appendEnd(StringBuilder sb) {
        sb.append("}");
    }

    public static void showInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append("\tSHOW_FIELD_MESSAGE = " + SHOW_FIELD_MESSAGE + "\r\n");
        sb.append("\tSHOW_METHOD_MESSAGE = " + SHOW_METHOD_MESSAGE + "\r\n");
        sb.append("\tCHANGE_LINE = " + CHANGE_LINE + "\r\n");
        sb.append("\tPREFIX_TAB = " + PREFIX_TAB + "\r\n");
        sb.append("\tDONT_INVOKE_METHOD = " + DONT_INVOKE_METHOD + "\r\n");
        sb.append("\tDEFAULT_DONT_INVOKE_METHOD = " + DEFAULT_DONT_INVOKE_METHOD);
        sb.append("]");
        System.out.println(sb);
    }

    public enum Setting {
        DEFAULT {
            public void apply() {
                EXCEPTION_PRINTSTACKTRACE = false;
                SHOW_FIELD_MESSAGE = false;
                SHOW_METHOD_MESSAGE = true;
                CHANGE_LINE = "\r\n";
                PREFIX_TAB = "\t";
                DONT_INVOKE_METHOD = DEFAULT_DONT_INVOKE_METHOD;
                CLASS_INFO_MODE = TypeMatch.CLASS_FULL_MODE;
                SETTINIG = this;
            }
        },
        SIMPLE {
            public void apply() {
                EXCEPTION_PRINTSTACKTRACE = false;
                SHOW_FIELD_MESSAGE = false;
                SHOW_METHOD_MESSAGE = false;
                CHANGE_LINE = "";
                PREFIX_TAB = "";
                DONT_INVOKE_METHOD = DEFAULT_DONT_INVOKE_METHOD;
                CLASS_INFO_MODE = TypeMatch.CLASS_DECLARED_MODE;
                SETTINIG = this;
            }
        },
        FULL {
            public void apply() {
                EXCEPTION_PRINTSTACKTRACE = true;
                SHOW_FIELD_MESSAGE = true;
                SHOW_METHOD_MESSAGE = true;
                CHANGE_LINE = "\r\n";
                PREFIX_TAB = "\t";
                DONT_INVOKE_METHOD = DontInvokeMethod.newInstance();
                CLASS_INFO_MODE = TypeMatch.CLASS_FULL_MODE;
                SETTINIG = this;
            }
        },
        DEBUG {
            public void apply() {
                EXCEPTION_PRINTSTACKTRACE = true;
                SHOW_FIELD_MESSAGE = true;
                SHOW_METHOD_MESSAGE = true;
                CHANGE_LINE = "\r\n";
                PREFIX_TAB = "\t";
                DONT_INVOKE_METHOD = DontInvokeMethod.newInstance();
                CLASS_INFO_MODE = TypeMatch.CLASS_FULL_MODE;
                SETTINIG = this;
            }
        },
        ;

        public abstract void apply();
    }
    
    /**
     * javassist.Modifier
     */
    public static class Modifier {
        public static final int PUBLIC = 1;
        public static final int PRIVATE = 2;
        public static final int PROTECTED = 4;
        public static final int STATIC = 8;
        public static final int FINAL = 16;
        public static final int SYNCHRONIZED = 32;
        public static final int VOLATILE = 64;
        public static final int TRANSIENT = 128;
        public static final int NATIVE = 256;
        public static final int INTERFACE = 512;
        public static final int ABSTRACT = 1024;
        public static final int STRICT = 2048;
        public static final int ANNOTATION = 8192;
        public static final int ENUM = 16384;
        public Modifier() {
        }
        public static boolean isPublic(int mod) {
            return (mod & 0x1) != 0;
        }
        public static boolean isPrivate(int mod) {
            return (mod & 0x2) != 0;
        }
        public static boolean isProtected(int mod) {
            return (mod & 0x4) != 0;
        }
        public static boolean isPackage(int mod) {
            return (mod & 0x7) == 0;
        }
        public static boolean isStatic(int mod) {
            return (mod & 0x8) != 0;
        }
        public static boolean isFinal(int mod) {
            return (mod & 0x10) != 0;
        }
        public static boolean isSynchronized(int mod) {
            return (mod & 0x20) != 0;
        }
        public static boolean isVolatile(int mod) {
            return (mod & 0x40) != 0;
        }
        public static boolean isTransient(int mod) {
            return (mod & 0x80) != 0;
        }
        public static boolean isNative(int mod) {
            return (mod & 0x100) != 0;
        }
        public static boolean isInterface(int mod) {
            return (mod & 0x200) != 0;
        }
        public static boolean isAnnotation(int mod) {
            return (mod & 0x2000) != 0;
        }
        public static boolean isEnum(int mod) {
            return (mod & 0x4000) != 0;
        }
        public static boolean isAbstract(int mod) {
            return (mod & 0x400) != 0;
        }
        public static boolean isStrict(int mod) {
            return (mod & 0x800) != 0;
        }
        public static int setPublic(int mod) {
            return mod & 0xFFFFFFF9 | 0x1;
        }
        public static int setProtected(int mod) {
            return mod & 0xFFFFFFFC | 0x4;
        }
        public static int setPrivate(int mod) {
            return mod & 0xFFFFFFFA | 0x2;
        }
        public static int setPackage(int mod) {
            return mod & 0xFFFFFFF8;
        }
        public static int clear(int mod, int clearBit) {
            return mod & (clearBit ^ 0xFFFFFFFF);
        }
        public static String toString(int mod) {
            return java.lang.reflect.Modifier.toString(mod);
        }
    }
    
    private static String StringUtils_rightPad(String str, int len){
        if(str == null){
            str = "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        for(int ii = 0, end = len - str.length() ; ii < end ; ii ++){
            sb.append(" ");
        }
        return sb.toString();
    }
}
