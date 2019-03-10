package gtu.yaml.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
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

        List<YamlMapUtil_Test_Bean> lst = new ArrayList<YamlMapUtil_Test_Bean>();
        YamlMapUtil_Test_Bean b1 = new YamlMapUtil_Test_Bean();
        b1.xxxx1 = "aaa   \n dddddd \n sadsfafsdf   |n  dfdf             \n\n";
        lst.add(b1);
        lst.add(new YamlMapUtil_Test_Bean());
        YamlMapUtil.getInstance().saveToFilePlain(yamlFile1, lst, false, null);
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

    public void saveToFilePlain(File file, Object targetObj, boolean append, final Map<String, Class<?>> classMap) {
        class PlainSetter {
            void go1(Object targetObj) {
                if (targetObj != null) {
                    if (Collection.class.isAssignableFrom(targetObj.getClass())) {
                        for (Object v : (Collection) targetObj) {
                            go1(v);
                        }
                    } else if (Map.class.isAssignableFrom(targetObj.getClass())) {
                        for (Object k : ((Map) targetObj).keySet()) {
                            go1(((Map) targetObj).get(k));
                        }
                    } else {
                        go2(targetObj);
                    }
                }
            }

            void go2(Object targetObj) {
                try {
                    BeanInfo beanInfo = Introspector.getBeanInfo(targetObj.getClass(), Introspector.USE_ALL_BEANINFO);
                    PropertyDescriptor[] desc = beanInfo.getPropertyDescriptors();
                    for (PropertyDescriptor d : desc) {
                        try {
                            Object v = d.getReadMethod().invoke(targetObj, new Object[0]);
                            if (v != null) {
                                if (v.getClass() == String.class) {
                                    v = YamlUtil.getPlainString((String) v);
                                    d.getWriteMethod().invoke(targetObj, new Object[] { v });
                                } else if (classMap != null && classMap.containsValue(v.getClass())) {
                                    go1(v);
                                }
                            }
                        } catch (Exception e) {
                            throw new RuntimeException("saveToFilePlain , field : " + d.getName() + " , ERR : " + e.getMessage(), e);
                        }
                    }
                } catch (Exception e1) {
                    throw new RuntimeException("saveToFilePlain, ERR : " + e1.getMessage(), e1);
                }
            }
        }
        new PlainSetter().go1(targetObj);
        saveToFile(file, targetObj, append);
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
