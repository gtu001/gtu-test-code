package gtu.json;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javassist.Modifier;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ToJsonObject {

    public static void main(String[] args) throws Exception {
        Test t = new Test();
        JSONObject obj = new JSONObject("{'aaa':'aaa','test2':{'bbb':'bbb2'},'list':[{'bbb':'bbb3'}],'string_list':[3,4]}");
        System.out.println(obj);
        setBeanFromJsonObject(obj, t);
        System.out.println(t);
    }

    private static class Test {
        String aaa;
        Test2 test2;
        List<Test2> list;
        List<String> string_list;

        @Override
        public String toString() {
            return "Test [aaa=" + aaa + ", test2=" + test2 + ", list=" + list + ", string_list=" + string_list + "]";
        }
    }

    private static class Test2 {
        String bbb;

        @Override
        public String toString() {
            return "Test2 [bbb=" + bbb + "]";
        }
    }

    public static <T> void setBeanFromJsonObject(JSONObject obj, T bean) {
        try {
            for (Field f : bean.getClass().getDeclaredFields()) {
                if (Modifier.isStatic(f.getModifiers()) || Modifier.isFinal(f.getModifiers())) {
                    System.out.println(f.getName() + " is ignore!");
                    continue;
                }
                if (!obj.has(f.getName())) {
                    System.out.println(f.getName() + " is ignore!");
                    continue;
                }

                Object val = null;
                if (f.getType() == boolean.class || f.getType() == Boolean.class) {
                    val = obj.getBoolean(f.getName());
                } else if (f.getType() == double.class || f.getType() == Double.class) {
                    val = obj.getDouble(f.getName());
                } else if (f.getType() == long.class || f.getType() == Long.class) {
                    val = obj.getLong(f.getName());
                } else if (f.getType() == int.class || f.getType() == Integer.class) {
                    val = obj.getInt(f.getName());
                } else if (f.getType() == String.class) {
                    val = obj.getString(f.getName());
                } else {
                    val = obj.get(f.getName());
                    setBeanFromJsonObject_inner(f, bean, val);// 未實測
                    continue;
                }
                f.setAccessible(true);
                f.set(bean, val);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static <T, K> void setBeanFromJsonObject_inner(Field f, T bean, Object val) {
        try {
            // 若是JsonArray
            if (List.class.isAssignableFrom(f.getType()) && val instanceof JSONArray) {
                ParameterizedType ptype = (ParameterizedType) f.getGenericType();
                Class<K> clz = (Class<K>) ptype.getActualTypeArguments()[0];
                List<K> newList = new ArrayList<K>();

                JSONArray jsonArray = (JSONArray) val;
                for (int ii = 0; ii < jsonArray.length(); ii++) {
                    Object detailObj = jsonArray.get(ii);

                    if (detailObj instanceof JSONObject) {
                        JSONObject detailObj2 = (JSONObject) detailObj;
                        K newBean = gtu.reflect.ReflectUtil.newInstance(clz);
                        newList.add(newBean);
                        setBeanFromJsonObject(detailObj2, newBean);
                    } else {
                        K detailObj2 = (K) detailObj;
                        newList.add(detailObj2);
                    }
                }

                f.setAccessible(true);
                f.set(bean, newList);
                // 若是JsonObject
            } else if (val instanceof JSONObject) {
                Class<?> clz = f.getType();
                Object detailBean = gtu.reflect.ReflectUtil.newInstance(clz);
                setBeanFromJsonObject((JSONObject) val, detailBean);

                f.setAccessible(true);
                f.set(bean, detailBean);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static JSONArray listToJSONArray(List<Map<String, Object>> list) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        if (list.size() == 0) {
            return jsonArray;
        }
        for (int i = 0, total = list.size(); i < total; i++) {
            Map<String, Object> record = list.get(i);
            JSONObject jsonObject = new JSONObject();
            for (Iterator<String> it = record.keySet().iterator(); it.hasNext();) {
                String key = it.next();
                jsonObject.put(key, record.get(key));
            }
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }
}
