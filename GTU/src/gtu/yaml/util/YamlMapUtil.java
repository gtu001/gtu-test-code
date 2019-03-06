package gtu.yaml.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import gtu.file.FileUtil;
import gtu.json.JSONObject2CollectionUtil2;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class YamlMapUtil {

    public static class YamlMapUtil_Test_Bean {
        String xxxx1 = "aaaa1";

        public String getXxxx1() {
            return xxxx1;
        }

        public void setXxxx1(String xxxx1) {
            this.xxxx1 = xxxx1;
        }
    }

    public static void main(String[] args) {
        File yamlFile1 = new File(FileUtil.DESKTOP_DIR, "test001.yml");
        File yamlFile2 = new File(FileUtil.DESKTOP_DIR, "test002.yml");
        if (true) {
            List<YamlMapUtil_Test_Bean> lst = new ArrayList<YamlMapUtil_Test_Bean>();
            lst.add(new YamlMapUtil_Test_Bean());

            YamlMapUtil.getInstance().saveToFile(yamlFile1, lst, false);
            YamlMapUtil.getInstance().saveToFile(yamlFile2, new YamlMapUtil_Test_Bean(), false);
        }
        if (true) {
            List<YamlMapUtil_Test_Bean> vvvv1 = YamlMapUtil.getInstance().loadFromFile(yamlFile1, YamlMapUtil_Test_Bean.class, null);
            System.out.println(vvvv1);
            YamlMapUtil_Test_Bean vvvv2 = YamlMapUtil.getInstance().loadFromFile(yamlFile2, YamlMapUtil_Test_Bean.class, null);
            System.out.println(vvvv2);
        }
        System.out.println("done...");
    }

    private static final YamlMapUtil _INST = new YamlMapUtil();

    public static YamlMapUtil getInstance() {
        return _INST;
    }

    public void saveToFile(File file, Object targetObj, boolean append) {
        try {
            Object dumpObj = null;
            if (Collection.class.isAssignableFrom(targetObj.getClass())) {
                JSONArray arry = JSONArray.fromObject(targetObj);
                dumpObj = JSONObject2CollectionUtil2.toList(arry);
            } else {
                JSONObject jsonObj = JSONObject.fromObject(targetObj);
                dumpObj = JSONObject2CollectionUtil2.toMap(jsonObj);
            }

            YamlUtil.saveToFile(file, dumpObj, append);
        } catch (Exception e) {
            throw new RuntimeException("saveToFile ERR : " + e.getMessage(), e);
        }
    }

    public <T> T loadFromFile(InputStream inputStream, Class<?> clz, Map<String, Class<?>> classMap) {
        try {
            Object yamlObj = YamlUtil.loadInputStream(inputStream);
            if (yamlObj == null) {
                return null;
            }
            if (Collection.class.isAssignableFrom(yamlObj.getClass())) {
                JSONArray arry = JSONArray.fromObject(yamlObj);
                return (T) JSONArray.toList(arry, clz, classMap);
            } else {
                JSONObject jsonObj = JSONObject.fromObject(yamlObj);
                return (T) JSONObject.toBean(jsonObj, clz, classMap);
            }
        } catch (Exception e) {
            throw new RuntimeException("loadFromFile ERR : " + e.getMessage(), e);
        }
    }

    public <T> T loadFromFile(File file, Class<?> clz, Map<String, Class<?>> classMap) {
        try {
            return loadFromFile(new FileInputStream(file), clz, classMap);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("loadFromFile ERR : " + e.getMessage(), e);
        }
    }
}
