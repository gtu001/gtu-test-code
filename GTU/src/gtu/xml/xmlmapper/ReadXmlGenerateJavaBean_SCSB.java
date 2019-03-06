package gtu.xml.xmlmapper;

import java.io.File;
import java.io.StringReader;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ReadXmlGenerateJavaBean_SCSB {

    public static void main(String[] args) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===========================================================================

    public void parse(File file, Object bean) {
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(file);
            Element rootElemet = doc.getRootElement();
            readElement(rootElemet, bean);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public void parse(String xmlContent, Object bean) {
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(new StringReader(xmlContent));
            Element rootElemet = doc.getRootElement();
            readElement(rootElemet, bean);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Read
    // Block==================================================================================================
    private boolean isTagMatch(Element element, TagInfo tag1) {
        String parentName = element.getParent() != null ? element.getParent().getName() : "";
        String tagInfoParent = StringUtils.trimToEmpty(tag1.parent());
        boolean elementResult = tag1.name().equals(element.getName());
        boolean parentResult = StringUtils.equals(parentName, tagInfoParent);
        return elementResult && parentResult;
    }

    private String getTagValue(Element element) {
        String value = element.getTextTrim();
        List<Element> eList = element.elements();
        // if (eList.size() == 1 && eList.get(0).getName().equals("LINE")) {
        // value = eList.get(0).getTextTrim();
        // }
        return value;
    }

    private void readElement(Element element, Object bean)
            throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        List<Element> elist = element.elements();
        for (Element e : elist) {
            java.lang.reflect.Field fld = null;
            for (java.lang.reflect.Field fld3 : bean.getClass().getDeclaredFields()) {
                fld3.setAccessible(true);
                TagInfo tag2 = fld3.getAnnotation(TagInfo.class);
                if (isTagMatch(e, tag2)) {
                    fld = fld3;
                    break;
                }
            }
            if (fld != null) {
                fld.setAccessible(true);
                String value = getTagValue(e);
                Type type = null;
                try {
                    type = ((ParameterizedType) fld.getGenericType()).getActualTypeArguments()[0];
                } catch (Exception ex) {
                }
                if (fld.getType() == String.class) {
                    fld.set(bean, value);
                } else if (List.class.isAssignableFrom(fld.getType()) && type == String.class) {
                    List list = this.getListIfNeedCreateNew(fld, bean);
                    list.add(value);
                } else {
                    // 非字串欄位 需要root
                    if (List.class.isAssignableFrom(fld.getType())) {
                        List list = this.getListIfNeedCreateNew(fld, bean);
                        Object childObj = instantiateClass((Class<?>) type);
                        list.add(childObj);
                        readElement(e, childObj);
                    } else {
                        Object childObj = instantiateClass(fld.getType());
                        fld.set(bean, childObj);
                        readElement(e, childObj);
                    }
                }
            }
        }
        
        //處理有些Tag不存在導致NullPointerException
        processTagNotPresent(bean);
        processTagNotPresentForList(bean);
    }
    
    private void processTagNotPresent(Object bean) {
        for(Field f : bean.getClass().getDeclaredFields()) {
            if(f.getType() != String.class) {
                f.setAccessible(true);
                try {
                    Object val = FieldUtils.readDeclaredField(bean, f.getName(), true);
                    if(val == null) {
                        val = instantiateClass(f.getType());
                    }
                    FieldUtils.writeDeclaredField(bean, f.getName(), val, true);
                }catch(Exception ex) {
                    System.out.println("欄位處理錯誤 : " + f.getName() + ", ex : " + ex.getMessage());
                }
            }
        }
    }
    
    private void processTagNotPresentForList(Object bean) {
        for(Field f : bean.getClass().getDeclaredFields()) {
            if(f.getType().isAssignableFrom(List.class)) {
                f.setAccessible(true);
                try {
                    Object val = FieldUtils.readDeclaredField(bean, f.getName(), true);
                    if(val == null) {
                        val = new ArrayList();
                    }
                    FieldUtils.writeDeclaredField(bean, f.getName(), val, true);
                }catch(Exception ex) {
                    System.out.println("欄位處理錯誤 : " + f.getName() + ", ex : " + ex.getMessage());
                }
            }
        }
    }

    private Object instantiateClass(Class<?> clazz) {
        try {
            Constructor cons = clazz.getDeclaredConstructor(new Class[0]);
            cons.setAccessible(true);
            return cons.newInstance(new Object[0]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List getListIfNeedCreateNew(java.lang.reflect.Field fld, Object bean) throws IllegalArgumentException, IllegalAccessException {
        fld.setAccessible(true);
        List list = (List) fld.get(bean);
        if (list == null) {
            list = new ArrayList();
        }
        fld.set(bean, list);
        return list;
    }
    
    @Target(value = { ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE,
            ElementType.PACKAGE })
    @Retention(RetentionPolicy.RUNTIME)
    private @interface TagInfo {
        String parent();
        String name();
        String testVal();
        String desc();
    }
}
