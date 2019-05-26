package gtu.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.reflect.FieldUtils;

public class JaxbBeanToMapUtil {

    public static void main(String[] args) {
        Map<String, Object> rtnMap = new LinkedHashMap<String, Object>();

        Records r = new Records();
        FakeBeanFiller.fillBean(r, 10);

        simpleJaxbToMap(r, rtnMap);

        System.out.println(rtnMap);
    }

    @XmlType(name = "Records")
    @XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Records {
        @XmlElement(name = "TestData")
        String testData;
        @XmlElement(name = "Record")
        List<Record> records;
        
        @XmlElement(name = "MapRecord")
        Map<String,Record> recordsMap;
    }

    @XmlType(name = "Record")
    @XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Record {
        @XmlElement(name = "TradeDate")
        String tradedate;
        @XmlElement(name = "PublishDate")
        String publishdate;
        @XmlElement(name = "PublishTime")
        String publishtime;
    }

    public static Map<String, Object> simpleJaxbToMap(Object bean) {
        Map<String, Object> rtnMap = new LinkedHashMap<String, Object>();
        simpleJaxbToMap(bean, rtnMap);
        return rtnMap;
    }

    private static void simpleJaxbToMap(Object bean, Map<String, Object> rtnMap) {
        String fieldName = "";
        try {
            for (Field f : bean.getClass().getDeclaredFields()) {
                XmlElement xmlElement = f.getDeclaredAnnotation(XmlElement.class);
                fieldName = xmlElement.name();
                Object val = FieldUtils.readDeclaredField(bean, f.getName(), true);
                if (Collection.class.isAssignableFrom(f.getType())) {
                    Collection coll = (Collection) val;
                    List lst = new ArrayList();
                    transToMapList(bean, f.getName(), lst);
                    rtnMap.put(fieldName, lst);
                } else if (Map.class.isAssignableFrom(f.getType())) {
                    Map map = (Map) val;
                    Map mapZ = new LinkedHashMap();
                    for (Object key : map.keySet()) {
                        Object val2 = map.get(key);
                        simpleJaxbToMap(val2, mapZ);
                    }
                    rtnMap.put(fieldName, mapZ);
                } else {
                    rtnMap.put(fieldName, (String) val);
                }
            }
        } catch (UnsupportedOperationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException("simpleJaxbToMap 欄位 :" + fieldName + ", 轉換失敗!!, ERR : " + ex.getMessage(), ex);
        }
    }

    private static void transToMapList(Object bean, String fieldName, List lst) {
        try {
            Field field = bean.getClass().getDeclaredField(fieldName);
            // Class clz = getFieldGenericType_4Collection(field);
            Collection coll = (Collection) FieldUtils.readDeclaredField(bean, fieldName, true);
            for (Object v : coll) {
                Map<String, Object> map = new LinkedHashMap<String, Object>();
                simpleJaxbToMap(v, map);
                lst.add(map);
            }
            FieldUtils.writeDeclaredField(bean, fieldName, lst, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Class<?> getFieldGenericType(Field field) {
        // Type type = null;
        // try {
        // type = ((ParameterizedType)
        // fld.getGenericType()).getActualTypeArguments()[0];
        // } catch (Exception ex) {
        // }
        ParameterizedType type = (ParameterizedType) field.getGenericType();
        System.out.println(ReflectionToStringBuilder.toString(type, ToStringStyle.MULTI_LINE_STYLE));
        return (Class<?>) type.getRawType();
    }

    public static Class<?> getFieldGenericType_4Collection(Field field) {
        ParameterizedType type = (ParameterizedType) field.getGenericType();
        Class<?> genericType = (Class<?>) type.getActualTypeArguments()[0];
        return genericType;
    }
}