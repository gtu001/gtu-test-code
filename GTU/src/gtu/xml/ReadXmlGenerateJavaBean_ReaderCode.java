package gtu.xml;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.BeanUtils;

public class ReadXmlGenerateJavaBean_ReaderCode {
    

    public static void main(String[] args) {
        try {
            ReadXmlGenerateJavaBean_ReaderCode test = new ReadXmlGenerateJavaBean_ReaderCode();
            test.run("C:/Users/gtu001/Desktop/outbound.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===========================================================================

    public void run(String filePath) {
        try {
            File file = new File(filePath);
            SAXReader reader = new SAXReader();

            Document doc = reader.read(file);
            Element rootElemet = doc.getRootElement();

            SbpisInfo info = new SbpisInfo();
            readElement(rootElemet, info);

            System.out.println("done...");
        } catch (Exception e) {
            e.printStackTrace();
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

    public void readElement(Element element, Object bean) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException,
            InvocationTargetException {
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
                        Object childObj = BeanUtils.instantiateClass((Class<?>) type);
                        list.add(childObj);
                        readElement(e, childObj);
                    } else {
                        Object childObj = BeanUtils.instantiateClass(fld.getType());
                        fld.set(bean, childObj);
                        readElement(e, childObj);
                    }
                }
            }
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

    // Mapping
    // Bean=======================================================================================

    @TagInfo(parent = "", name = "SBPIS-Info", testVal = "", desc = "")
    private static class SbpisInfo {
        @TagInfo(parent = "SBPIS-Info", name = "transactionId", testVal = "00106901", desc = "")
        String transactionid;
        @TagInfo(parent = "SBPIS-Info", name = "customerId", testVal = "12345678", desc = "")
        String customerid;
        @TagInfo(parent = "SBPIS-Info", name = "processId", testVal = "07", desc = "")
        String processid;
        @TagInfo(parent = "SBPIS-Info", name = "ioType", testVal = "O", desc = "")
        String iotype;
    }
}
