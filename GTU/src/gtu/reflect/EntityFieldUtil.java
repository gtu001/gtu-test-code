package gtu.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.apache.log4j.Logger;

public class EntityFieldUtil {

    private static final Logger log = Logger.getLogger(EntityFieldUtil.class);

    /**
     * @param indicateClz
     *            指定entity的class (有時會會變成proxy需要自行指定)
     * @param entity
     *            entity的instance
     * @param fieldName
     *            field的name
     * @return
     */
    public static Object getFieldFromEntity(Class indicateClz, Object entity, String fieldName) {
        try {
            Field field = FieldUtils.getDeclaredField(indicateClz, fieldName, true);
            return field.get(entity);
        } catch (Exception ex) {
            String tmpFieldName = StringUtils.capitalize(fieldName);
            String[] invokeMethodNames = new String[] { "get" + tmpFieldName, "is" + tmpFieldName };
            for (String fname : invokeMethodNames) {
                try {
                    return MethodUtils.invokeMethod(entity, fname, new Object[0]);
                } catch (Exception ex1) {
                    log.info("找不到method : " + fname + " - " + ex1.getMessage());
                }
            }
            throw new RuntimeException("無法取得此欄位 : " + fieldName, ex);
        }
    }

    private static Object __primitiveConvert(Object value, Class targetClz) {
        if (value == null) {
            return null;
        }
        if (targetClz == String.class) {
            return String.valueOf(value);
        }
        return ConvertUtils.convert(String.valueOf(value), targetClz);
    }

    /**
     * @param indicateClz
     *            指定entity的class (有時會會變成proxy需要自行指定)
     * @param entity
     *            entity的instance
     * @param fieldName
     *            field的name
     * @param value
     *            要給的值
     */
    public static void setFieldToEntity(Class indicateClz, Object entity, String fieldName, Object value) {
        try {
            Field field = FieldUtils.getDeclaredField(indicateClz, fieldName, true);
            value = __primitiveConvert(value, field.getType());
            field.set(entity, value);
            return;
        } catch (Exception ex) {
            String methodName = "set" + StringUtils.capitalize(fieldName);
            for (Method mth : entity.getClass().getDeclaredMethods()) {
                if (mth.getName().equals(methodName)) {
                    value = __primitiveConvert(value, mth.getParameterTypes()[0]);
                    try {
                        mth.invoke(entity, value);
                        return;
                    } catch (Exception e) {
                        log.info("找不到method : " + fieldName + " - " + ex.getMessage());
                    }
                }
            }
            throw new RuntimeException("無法取得此欄位 : " + fieldName, ex);
        }
    }
}
