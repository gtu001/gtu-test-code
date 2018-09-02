package gtu._work.classmaker;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javassist.Modifier;

public class ImplCreater {

    /**
     * @param args
     */
    public static void main(String[] args) {
        ImplCreater.newInstance(ByteArrayOutputStream.class).execute();
    }

    private ImplCreater(Class<?> clz) {
        this.clz = clz;
    }

    public ImplCreater execute() {
        List<AccessType> accessTypeList = new ArrayList<AccessType>();
        //clz.getDeclaredMethods()
        for (Method method : clz.getMethods()) {
            try {
                accessTypeList.add(new AccessType(method));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        StringBuilder sb = new StringBuilder();
        for (AccessType access : accessTypeList) {
            sb.append(access);
        }
        System.out.println(sb);
        return this;
    }

    static class AccessType {

        String accesssType;
        String staticType;
        String syncType;
        Parameter returnType;
        String methodName;
        Set<String> throwSet;
        List<Parameter> paramList;

        Method method;

        static class Parameter {
            String typeName;
            String parameter;

            public String toString() {
                return typeName + " " + parameter;
            }
        }

        private static final String PREFIX = "\t";
        private static final String INNER_PREFIX = "\t\t";
        private static final String CHGLINE = "\r\n";
        private static final String SYSOUT = "System.out.println";

        //        private static final String SYSOUT = "log.debug";

        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (throwSet.isEmpty()) {
                sb.append(String.format("%s%s %s %s %s %s(%s) {%s", PREFIX, accesssType, staticType, syncType,
                        returnType.typeName, methodName, parameterGroupToString(), CHGLINE));
            } else {
                sb.append(String.format("%s%s %s %s %s %s(%s) throws %s {%s", PREFIX, accesssType, staticType,
                        syncType, returnType.typeName, methodName, parameterGroupToString(), throwSetToString(),
                        CHGLINE));
            }
            if (returnType.typeName.equals("void")) {
                sb.append(String.format("%ssuper.%s(%s);%s", INNER_PREFIX, methodName, parameterToString(), CHGLINE));
            } else {
                sb.append(String.format("%s%s = super.%s(%s);%s", INNER_PREFIX, returnType, methodName,
                        parameterToString(), CHGLINE));
            }
            //TODO;
            sb.append(defaultToString());
            //TODO;
            if (!returnType.typeName.equals("void")) {
                sb.append(String.format("%sreturn %s;%s", INNER_PREFIX, returnType.parameter, CHGLINE));
            }
            sb.append(String.format("%s}%s", PREFIX, CHGLINE));
            return sb.toString();
        }

        protected String throwSetToString() {
            int size = throwSet.size() - 1;
            int index = 0;
            StringBuilder sb = new StringBuilder();
            for (Iterator<String> it = throwSet.iterator(); it.hasNext();) {
                if (size == index) {
                    sb.append(it.next());
                } else {
                    sb.append(it.next() + ", ");
                }
                index++;
            }
            return sb.toString();
        }

        protected String defaultToString() {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%s%s(\"%s\");%s", INNER_PREFIX, SYSOUT, "# " + method.getName() + " ...", CHGLINE));
            for (Parameter p : paramList) {
                sb.append(String.format("%s%s(\"%s = \" + %s);%s", INNER_PREFIX, SYSOUT, p.parameter, p.parameter,
                        CHGLINE));
            }
            return sb.toString();
        }

        private String parameterToString() {
            StringBuilder sb = new StringBuilder();
            for (int ii = 0; ii < paramList.size(); ii++) {
                sb.append(paramList.get(ii).parameter);
                if (ii != paramList.size() - 1) {
                    sb.append(", ");
                }
            }
            return sb.toString();
        }

        private String parameterGroupToString() {
            StringBuilder sb = new StringBuilder();
            for (int ii = 0; ii < paramList.size(); ii++) {
                sb.append(paramList.get(ii));
                if (ii != paramList.size() - 1) {
                    sb.append(", ");
                }
            }
            return sb.toString();
        }

        AccessType(Method method) {
            int modifier = method.getModifiers();
            if (Modifier.isPrivate(modifier)) {
                throw new UnsupportedOperationException("method = private");
            }
            if (Modifier.isFinal(modifier)) {
                throw new UnsupportedOperationException("method = final");
            }

            this.method = method;

            accesssType = "";
            staticType = "";
            syncType = "";
            methodName = "";

            methodName = method.getName();

            if (Modifier.isProtected(modifier)) {
                accesssType = "protected";
            }
            if (Modifier.isPackage(modifier)) {
                accesssType = "";
            }
            if (Modifier.isPublic(modifier)) {
                accesssType = "public";
            }
            if (Modifier.isStatic(modifier)) {
                staticType = "static";
            }
            if (Modifier.isSynchronized(modifier)) {
                syncType = "synchronized";
            }

            returnType = createParameterName(method.getReturnType());

            Set<String> throwSet = new HashSet<String>();
            for (Class<?> ex : method.getExceptionTypes()) {
                throwSet.add(ex.getSimpleName());
            }
            this.throwSet = throwSet;

            List<Parameter> paramList = new ArrayList<Parameter>();
            for (Class<?> p : method.getParameterTypes()) {
                paramList.add(createParameterName(p));
            }
            this.paramList = paramList;
        }

        private Parameter createParameterName(Class<?> p) {
            Parameter parameter = new AccessType.Parameter();
            parameter.typeName = p.getSimpleName();
            String parm = p.getSimpleName().substring(0, 1).toLowerCase() + p.getSimpleName().substring(1);
            if (p.isArray()) {
                parm = parm.replaceAll(Pattern.quote("[]"), "");
            }
            if (p == Class.class || p == Class[].class) {
                parm = "clz";
            }
            if (p == int.class || p == int[].class) {
                parm = "int_";
            }
            if (p == boolean.class || p == boolean[].class) {
                parm = "bool";
            }
            if (p == byte.class || p == byte[].class) {
                parm = "bit";
            }
            parameter.parameter = parm;
            return parameter;
        }
    }

    private final Class<?> clz;

    public static ImplCreater newInstance(Class<?> clz) {
        return new ImplCreater(clz);
    }

}
