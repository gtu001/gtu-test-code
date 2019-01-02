package gtu.yaml.util;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.util.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;

import gtu.collection.MapUtil;
import gtu.file.FileUtil;
import gtu.json.JSONObject2CollectionUtil;
import gtu.reflect.ReflectUtil;

public class YamlMapUtil {

    private static class TTTT {
        String xxxx1 = "aaaa1";
        String xxxx2 = "aaaa2";
        List<GGGG> lst = new ArrayList<GGGG>();

        TTTT() {
            lst.add(new GGGG());
            lst.add(new GGGG());
        }

        public String getXxxx1() {
            return xxxx1;
        }

        public void setXxxx1(String xxxx1) {
            this.xxxx1 = xxxx1;
        }

        public String getXxxx2() {
            return xxxx2;
        }

        public void setXxxx2(String xxxx2) {
            this.xxxx2 = xxxx2;
        }

        public List<GGGG> getLst() {
            return lst;
        }

        public void setLst(List<GGGG> lst) {
            this.lst = lst;
        }

    }

    private static class GGGG {
        String yyyy = "bbbb";

        public String getYyyy() {
            return yyyy;
        }

        public void setYyyy(String yyyy) {
            this.yyyy = yyyy;
        }

    }

    public static void main(String[] args) {
        List<TTTT> lst = new ArrayList<TTTT>();
        lst.add(new TTTT());
        lst.add(new TTTT());
        lst.add(new TTTT());

        File yamlFile = new File(FileUtil.DESKTOP_DIR, "test001.yml");
        try {
            JSONArray ary = new JSONArray(lst);
            System.out.println(ary);
            List<Object> list = JSONObject2CollectionUtil.toList(ary);
            YamlUtil.saveToFile(yamlFile, list, false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("done...");
    }

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
