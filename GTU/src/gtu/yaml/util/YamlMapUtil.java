package gtu.yaml.util;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import gtu.collection.MapUtil;
import gtu.file.FileUtil;
import gtu.reflect.ReflectUtil;

public class YamlMapUtil {

    private static final YamlMapUtil _INST = new YamlMapUtil();

    public static YamlMapUtil getInstance() {
        return _INST;
    }

    public <T> void saveToFile(File file, List<T> beanLst, boolean append) {
        List<Map<String, Object>> mapLst = new ArrayList<Map<String, Object>>();
        for (T bean : beanLst) {
            Map<String, Object> m = new LinkedHashMap<String, Object>();
            MapUtil.beanToMap(bean, m);
            mapLst.add(m);
        }
        YamlUtil.saveToFile(file, mapLst, append);
    }

    public <T> List<T> loadFromFile(File file, Object parentInst, Class<T> clz) {
        List<T> beanLst = new ArrayList<T>();
        List<Map<String, Object>> lst = (List<Map<String, Object>>) YamlUtil.loadFromFile(file);
        for (Map<String, Object> m : lst) {
            T obj = (T) ReflectUtil.newInstanceDefault(clz, parentInst, false);
            mapToBean4Yaml(obj, m);
            beanLst.add(obj);
        }
        return beanLst;
    }

    private <T> T mapToBean4Yaml(T bean, Map<String, Object> map) {
        try {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String field = entry.getKey();
                if (field.startsWith("this$")) {
                    continue;
                }
                Field fid = bean.getClass().getDeclaredField(field);
                boolean access = fid.isAccessible();
                fid.setAccessible(true);
                fid.set(bean, entry.getValue());
                fid.setAccessible(access);
            }
            return bean;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
