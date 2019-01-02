package gtu.yaml.util;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.util.StringUtils;

import gtu.collection.MapUtil;
import gtu.reflect.ReflectUtil;

public class YamlMapUtil {

    private static final String[] IGNORE_KEY_PATTERN = new String[] { "this\\$\\d+" };
    private static final YamlMapUtil _INST = new YamlMapUtil();

    public static YamlMapUtil getInstance() {
        return _INST;
    }

    public <T> void saveToFile(File file, List<T> beanLst, boolean append) {
        List<Map<String, Object>> mapLst = new ArrayList<Map<String, Object>>();
        for (T bean : beanLst) {
            Map<String, Object> m = new LinkedHashMap<String, Object>();
            MapUtil.beanToMap(bean, m);
            removeIgnoreKeys(m);
            mapLst.add(m);
        }
        YamlUtil.saveToFile(file, mapLst, append);
    }

    private void removeIgnoreKeys(Map<String, Object> map) {
        List<String> keys = new ArrayList<String>(map.keySet());
        A: for (String k : keys) {
            for (String p : IGNORE_KEY_PATTERN) {
                if (StringUtils.defaultString(k).matches(p)) {
                    map.remove(k);
                    continue A;
                }
            }
        }
    }

    public <T> List<T> loadFromFile(File file, Object parentInst, Class<T> clz) {
        List<T> beanLst = new ArrayList<T>();
        List<Map<String, Object>> lst = (List<Map<String, Object>>) YamlUtil.loadFromFile(file);
        for (Map<String, Object> m : lst) {
            T obj = (T) ReflectUtil.newInstanceDefault(clz, parentInst, false);
            MapUtil.mapToBean(obj, m);
            beanLst.add(obj);
        }
        return beanLst;
    }
}
