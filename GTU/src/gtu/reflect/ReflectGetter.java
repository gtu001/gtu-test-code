package gtu.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javassist.Modifier;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectGetter {

    private Logger log = LoggerFactory.getLogger(ReflectGetter.class);

    private ReflectGetter() {
    }

    Object object;

    public static ReflectGetter newInstance() {
        return new ReflectGetter();
    }

    public ReflectGetter apply(Object value) {
        Validate.notNull(value);
        object = value;
        return this;
    }

    public Object get() {
        return object;
    }

    public ReflectGetter field(String fieldName) {
        Class<?> clz = object.getClass();
        Object obj = null;
        obj = filterField(fieldName, "getDeclaredFields", clz.getDeclaredFields());
        if (obj == null) {
            obj = filterField(fieldName, "getFields", clz.getFields());
        }
        if (obj == null) {
            throw new RuntimeException("field : " + fieldName + ", not found!!");
        }
        object = obj;
        return this;
    }

    public ReflectGetter method(String methodName) {
        Class<?> clz = object.getClass();
        Object obj = null;
        obj = filterMethod(methodName, "getDeclaredMethods", clz.getDeclaredMethods());
        if (obj == null) {
            obj = filterMethod(methodName, "getMethods", clz.getMethods());
        }
        if (obj == null) {
            throw new RuntimeException("method : " + methodName + ", not found!!");
        }
        object = obj;
        return this;
    }

    Object filterMethod(String methodName, String message, Method[] methods) {
        Object rtn = null;
        for (Method m : methods) {
            if (m.getName().indexOf(methodName) != -1 && m.getReturnType() != void.class
                    && m.getParameterTypes().length == 0) {
                log.debug("found " + message + " : (" + getModifier(m.getModifiers()) + ")" + m.getName());
                boolean bool = m.isAccessible();
                m.setAccessible(true);
                try {
                    rtn = m.invoke(m, new Object[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                m.setAccessible(bool);
                log.debug("value = " + rtn);
                break;
            }
        }
        return rtn;
    }

    Object filterField(String fieldName, String message, Field[] fields) {
        Object rtn = null;
        for (Field m : fields) {
            if (m.getName().indexOf(fieldName) != -1) {
                log.debug("found " + message + " : (" + getModifier(m.getModifiers()) + ")" + m.getName());
                boolean bool = m.isAccessible();
                m.setAccessible(true);
                try {
                    rtn = m.get(object);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                m.setAccessible(bool);
                log.debug("value = " + rtn);
                break;
            }
        }
        return rtn;
    }

    String getModifier(int modifier) {
        StringBuilder sb = new StringBuilder();
        final String prefix = "-";
        final String rearfix = "";
        if (Modifier.isPublic(modifier))
            sb.append(prefix + "Public" + rearfix);
        if (Modifier.isPrivate(modifier))
            sb.append(prefix + "Private" + rearfix);
        if (Modifier.isProtected(modifier))
            sb.append(prefix + "Protected" + rearfix);
        if (Modifier.isPackage(modifier))
            sb.append(prefix + "Package" + rearfix);
        if (Modifier.isStatic(modifier))
            sb.append(prefix + "Static" + rearfix);
        if (Modifier.isFinal(modifier))
            sb.append(prefix + "Final" + rearfix);
        if (Modifier.isSynchronized(modifier))
            sb.append(prefix + "Synchronized" + rearfix);
        if (Modifier.isVolatile(modifier))
            sb.append(prefix + "Volatile" + rearfix);
        if (Modifier.isTransient(modifier))
            sb.append(prefix + "Transient" + rearfix);
        if (Modifier.isNative(modifier))
            sb.append(prefix + "Native" + rearfix);
        if (Modifier.isInterface(modifier))
            sb.append(prefix + "Interface" + rearfix);
        if (Modifier.isAnnotation(modifier))
            sb.append(prefix + "Annotation" + rearfix);
        if (Modifier.isEnum(modifier))
            sb.append(prefix + "Enum" + rearfix);
        if (Modifier.isAbstract(modifier))
            sb.append(prefix + "Abstract" + rearfix);
        if (Modifier.isStrict(modifier))
            sb.append(prefix + "Strict" + rearfix);
        return sb.toString();
    }
}
