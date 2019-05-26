package gtu.properties;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.FieldUtils;

import gtu.reflect.ReflectUtil;

public class PropertiesMultiUtil {

    public static void main(String[] args) {
        PropertiesUtilBean config = new PropertiesUtilBean(PropertiesMultiUtil.class);

        TestConfigMultiBean b1 = new TestConfigMultiBean();
        b1.key1 = "key1Data";
        b1.key2 = "key2Data";
        b1.val1 = "val1Data";
        b1.val2 = "val2Data";

        String key = PropertiesMultiUtil.getKey(b1);
        String value = PropertiesMultiUtil.getValue(b1);

        config.getConfigProp().setProperty(key, value);
        config.store();
        config.browse();

        for (Enumeration<Object> enu = config.getConfigProp().keys(); enu.hasMoreElements();) {
            String key1 = (String) enu.nextElement();
            String value1 = (String) config.getConfigProp().getProperty(key1);

            TestConfigMultiBean b2 = PropertiesMultiUtil.of(key1, value1, TestConfigMultiBean.class);
            System.out.println(b2);
        }
    }

    private static class TestConfigMultiBean {
        private static String[] KEYS_DEF = new String[] { "key1", "key2" };
        private static String[] VALUES_DEF = new String[] { "val1", "val2" };
        String key1;
        String key2;
        String val1;
        String val2;

        @Override
        public String toString() {
            return "TestConfigMultiBean [key1=" + key1 + ", key2=" + key2 + ", val1=" + val1 + ", val2=" + val2 + "]";
        }
    }

    private static String getArry(int idx, String[] arry, String defaultVal) {
        if (idx <= arry.length - 1) {
            return StringUtils.defaultString(arry[idx]);
        }
        return defaultVal;
    }

    private static final String KEYS = "KEYS_DEF";
    private static final String VALUES = "VALUES_DEF";
    private static final String SPLIT_STR = "#^#";

    public static <T> T of(String key, String value, Class<?> clz) {
        try {
            String[] keysColumns = (String[]) FieldUtils.readDeclaredStaticField(clz, KEYS, true);
            String[] valuesColumns = (String[]) FieldUtils.readDeclaredStaticField(clz, VALUES, true);

            T inst = (T) ReflectUtil.newInstanceDefault(clz, null, false);

            String[] keys = StringUtils.defaultString(key).split(Pattern.quote(SPLIT_STR));
            String[] values = StringUtils.defaultString(value).split(Pattern.quote(SPLIT_STR));

            for (int ii = 0; ii < keysColumns.length; ii++) {
                String fieldName = keysColumns[ii];
                String val = getArry(ii, keys, "");
                FieldUtils.writeDeclaredField(inst, fieldName, val, true);
            }

            for (int ii = 0; ii < valuesColumns.length; ii++) {
                String fieldName = valuesColumns[ii];
                String val = getArry(ii, values, "");
                FieldUtils.writeDeclaredField(inst, fieldName, val, true);
            }
            return inst;
        } catch (Exception e) {
            throw new RuntimeException("PropertiesMultiBean init Err : " + e.getMessage(), e);
        }
    }

    public static <T> String getKey(T inst) {
        try {
            String[] columns = (String[]) FieldUtils.readDeclaredStaticField(inst.getClass(), KEYS, true);
            return getArryData(inst, columns);
        } catch (Exception e) {
            throw new RuntimeException("PropertiesMultiBean getKey Err : " + e.getMessage(), e);
        }
    }

    public static <T> String getValue(T inst) {
        try {
            String[] columns = (String[]) FieldUtils.readDeclaredStaticField(inst.getClass(), VALUES, true);
            return getArryData(inst, columns);
        } catch (Exception e) {
            throw new RuntimeException("PropertiesMultiBean getValue Err : " + e.getMessage(), e);
        }
    }

    private static <T> String getArryData(T inst, String[] fieldNames) throws IllegalAccessException {
        List<String> lst = new ArrayList<String>();
        for (int ii = 0; ii < fieldNames.length; ii++) {
            String fieldName = fieldNames[ii];
            String value = (String) FieldUtils.readDeclaredField(inst, fieldName, true);
            lst.add(value);
        }
        return StringUtils.join(lst, SPLIT_STR);
    }
}