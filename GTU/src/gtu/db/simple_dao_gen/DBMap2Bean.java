package gtu.db.simple_dao_gen;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.lang.reflect.FieldUtils;

import gtu.string.StringUtilForDb;

public class DBMap2Bean {

    public static <T> Map<String, Object> toMap(T bean) {
        try {
            Map<String, Object> map = BeanUtilsBean.getInstance().describe(bean);
            Map<String, Object> rtnMap = new LinkedHashMap<String, Object>();
            for (String key : map.keySet()) {
                String colName = StringUtilForDb.javaToDbField(key).toUpperCase();
                rtnMap.put(colName, map.get(key));
            }
            return rtnMap;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static <T> T toBean(Map<String, Object> map, T bean) {
        String tmpJavaName = "";
        try {
            for (String key : map.keySet()) {
                String javaName = StringUtilForDb.dbFieldToJava(key);
                Object value = map.get(key);
                tmpJavaName = javaName;

                if (value != null) {
                    BeanUtilsBean.getInstance().setProperty(bean, javaName, value);
                } else {
                    FieldUtils.writeDeclaredField(bean, javaName, null, true);
                }
            }
            return bean;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage() + " -> " + tmpJavaName, e);
        }
    }

    public static <T> T toBean2(Map<String, Object> map, T bean) {
        try {
            PropertyDescriptor[] descriptors = BeanUtilsBean.getInstance().getPropertyUtils().getPropertyDescriptors(bean);
            Class clazz = bean.getClass();
            for (int i = 0; i < descriptors.length; i++) {
                String name = descriptors[i].getName();
                Method method = MethodUtils.getAccessibleMethod(clazz, descriptors[i].getWriteMethod());

                String javaName = name.replaceFirst("^(get|is)", "");
                String mapKey = StringUtilForDb.javaToDbField(javaName).toUpperCase();

                if ("CLASS".equals(mapKey)) {
                    continue;
                }

                if (!map.containsKey(mapKey)) {
                    throw new Exception("找不到  key : " + mapKey);
                }
                Object val = map.get(mapKey);
                System.out.println(mapKey + " - " + val);

                method.invoke(bean, new Object[] { val });
            }
            return bean;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static <T> List<T> toBeanList(List<Map<String, Object>> mapList, Class<T> targetClz) {
        List<T> rtnList = new ArrayList<T>();
        for (Map<String, Object> m : mapList) {
            T bean = null;
            try {
                bean = targetClz.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("toBeanList fail - " + e.getMessage(), e);
            }
            DBMap2Bean.toBean(m, bean);
            rtnList.add(bean);
        }
        return rtnList;
    }
}