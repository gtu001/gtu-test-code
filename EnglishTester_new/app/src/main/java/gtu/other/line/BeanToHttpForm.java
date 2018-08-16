package gtu.other.line;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.util.ClassUtils;

public class BeanToHttpForm {
    private Object bean;
    private StringBuffer sb = new StringBuffer();

    public static BeanToHttpForm newInstance(Object bean) {
        return new BeanToHttpForm(bean);
    }

    private BeanToHttpForm(Object bean) {
        this.bean = bean;
        this.load();
    }

    private void appendParameter(String name, String value) throws UnsupportedEncodingException {
        String prefix = (sb.length() != 0) ? "&" : "";
        sb.append(prefix + name + "=" + URLEncoder.encode(value, "UTF8"));
    }

    private void load() {
        Class targetClz = bean.getClass();
        for (; targetClz != null && targetClz != Object.class; targetClz = targetClz.getSuperclass()) {
            for (Field f : targetClz.getDeclaredFields()) {
                if (ClassUtils.isPrimitiveOrWrapper(f.getType()) || f.getType() == String.class) {
                    try {
                        Object val = FieldUtils.getDeclaredField(targetClz, f.getName(), true).get(bean);
                        val = val == null ? "" : val;
                        String strVal = String.valueOf(val);

                        appendParameter(f.getName(), strVal);
                    } catch (Exception e) {
                        System.out.println("Warning " + f.getName() + " --> " + e.getMessage());
                    }
                }
            }
        }
    }

    public String getParameterString() {
        return sb.toString();
    }
}