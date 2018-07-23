package gtu.apache;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

public class BeanUtilGtu {

    public static Object beanToMap(Object bean) {
        try {
            return BeanUtils.describe(bean);
        } catch (Exception e) {
            throw new RuntimeException("beanToMap ERR : " + e.getMessage(), e);
        }
    }

    public static void mapToBean(Object bean, Map<String, ? extends Object> map) {
        try {
            BeanUtils.populate(bean, map);
        } catch (Exception e) {
            throw new RuntimeException("mapToBean ERR : " + e.getMessage(), e);
        }
    }

    public static Object getPropertyByField(Object object, String name) {
        try {
            Object value = null;
            Class<?> clz = object.getClass();
            Field field = clz.getDeclaredField(name);
            boolean access = field.isAccessible();
            field.setAccessible(true);
            value = field.get(object);
            field.setAccessible(access);
            return value;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void setPropertyByField(Object object, String name, Object value) {
        try {
            Class<?> clz = object.getClass();
            Field field = clz.getDeclaredField(name);
            boolean access = field.isAccessible();
            field.setAccessible(true);
            field.set(object, value);
            field.setAccessible(access);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
