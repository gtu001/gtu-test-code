package gtu.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class ReflectUtil {

    private ReflectUtil() {
    }

    public static <T> T newInstance(Class<T> clz) {
        try {
            Constructor<T> constructor = clz.getDeclaredConstructor(new Class[0]);
            constructor.setAccessible(true);
            T t = constructor.newInstance(new Object[0]);
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getField(String field, Object bean) {
        try {
            Field fed = bean.getClass().getDeclaredField(field);
            boolean access = fed.isAccessible();
            fed.setAccessible(true);
            Object rtn = fed.get(bean);
            fed.setAccessible(access);
            return rtn;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void setField(String field, Object value, Object bean) {
        try {
            Field fed = bean.getClass().getDeclaredField(field);
            boolean access = fed.isAccessible();
            fed.setAccessible(true);
            fed.set(bean, value);
            fed.setAccessible(access);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Object methodDeclaredInvoke(Object bean, String methodName, Class<?>[] types, Object[] params) {
        try {
            Method method = bean.getClass().getDeclaredMethod(methodName, types);
            boolean access = method.isAccessible();
            method.setAccessible(true);
            Object rtn = method.invoke(bean, params);
            method.setAccessible(access);
            return rtn;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Object methodInvoke(Object bean, String methodName, Class<?>[] types, Object[] params) {
        try {
            Method method = bean.getClass().getMethod(methodName, types);
            boolean access = method.isAccessible();
            method.setAccessible(true);
            Object rtn = method.invoke(bean, params);
            method.setAccessible(access);
            return rtn;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static <T> T createByConstructor(Class<T> clz, Class<?>[] constructorParamsClass, Object[] constructorParams) {
        try {
            Constructor<T> constructor = clz.getDeclaredConstructor(constructorParamsClass);
            boolean access = constructor.isAccessible();
            constructor.setAccessible(true);
            T rtn = constructor.newInstance(constructorParams);
            constructor.setAccessible(access);
            return rtn;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 取得Field的泛型 TODO
     */
    public static Class<?> getFieldGenericType(Field field) {
        // Type type = null;
        // try {
        // type = ((ParameterizedType)
        // fld.getGenericType()).getActualTypeArguments()[0];
        // } catch (Exception ex) {
        // }
        ParameterizedType type = (ParameterizedType) field.getGenericType();
        System.out.println(ReflectionToStringBuilder.toString(type, ToStringStyle.MULTI_LINE_STYLE));
        return (Class<?>) type.getRawType();
    }

    public static Class<?> getFieldGenericType_2(Field field) {
        ParameterizedType type = (ParameterizedType) field.getGenericType();
        Class<?> genericType = (Class<?>) type.getActualTypeArguments()[0];
        return genericType;
    }

    public static class Anno {
        private Class<?> beanClz;

        private Anno(Class<?> beanClz) {
            this.beanClz = beanClz;
        }

        public static Anno newInstance(Class<?> beanClz) {
            return new Anno(beanClz);
        }

        public <T extends Annotation> T getMethod(String methodName, Class<?>[] paramTypes, Class<T> anno) throws SecurityException, NoSuchMethodException {
            Method method = beanClz.getDeclaredMethod(methodName, paramTypes);
            return method.getAnnotation(anno);
        }

        public <T extends Annotation> T getField(String fieldName, Class<T> anno) throws SecurityException, NoSuchFieldException {
            Field field = beanClz.getDeclaredField(fieldName);
            return field.getAnnotation(anno);
        }
    }

    public static AccessibleObject searchFieldOrMethod(String name, Class<?> clz) {
        for (Method method : clz.getMethods()) {
            if (method.getName().indexOf(name) != -1) {
                return method;
            }
        }
        for (Method method : clz.getDeclaredMethods()) {
            if (method.getName().indexOf(name) != -1) {
                return method;
            }
        }
        for (Field field : clz.getFields()) {
            if (field.getName().indexOf(name) != -1) {
                return field;
            }
        }
        for (Field field : clz.getDeclaredFields()) {
            if (field.getName().indexOf(name) != -1) {
                return field;
            }
        }
        if (clz.getSuperclass() != null) {
            return searchFieldOrMethod(name, clz.getSuperclass());
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        Test xx = new ReflectUtil().new Test(null);
        System.out.println(ReflectUtil.methodDeclaredInvoke(xx, "testMethod", new Class[0], new Object[0]));
        System.out.println("done...");
    }

    private class Test {
        private String xxx;

        private Test(String xxx) {
            this.xxx = xxx;
        }

        private void testMethod() {
            System.out.println("testMethod...");
        }
    }
}
