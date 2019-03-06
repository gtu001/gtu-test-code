package gtu.xml.xmlmapper;

import gtu.string.StringUtilForDb;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

public class ReadXmlGenerateJavaBean {
    
    public static void main(String[] args) throws DocumentException, IOException, SecurityException, NoSuchFieldException{
        URL url = ReadXmlGenerateJavaBean.class.getResource("ReadXmlGenerateJavaBean.xml");
        File file = new File("C:/Users/gtu001/Desktop/ITE2UPS/ITE2UPS/C003814022297_多張INV.xml");
        SAXReader reader = new SAXReader();
//        reader.setEncoding("utf8");
//        Document doc = reader.read(url.openStream());
        
        Document doc = reader.read(file);
        Map<String,String> javaBeanMap = new LinkedHashMap<String,String>();
        showElementTree_toJavaBean(doc.getRootElement(), javaBeanMap);
        for(String key : javaBeanMap.keySet()){
            System.out.println(javaBeanMap.get(key));
        }
        
        System.out.println("done...");
    }
    
    /**
     * 純粹顯示結構 debug用
     */
    private static void showElementTree(Element element, int tab){
        StringBuilder prefix = new StringBuilder();
        for(int ii = 0; ii < tab; ii ++){
            prefix.append("\t");
        }
        List<Element> needTraceList = new ArrayList<Element>();
        List<Element> elist = element.elements();
        System.out.println(prefix + element.getName() + "[" + elist.size() + "]..." + element.getTextTrim());
        for(Element ie : elist){
            int size = ie.elements().size();
            System.out.println(prefix + "\t" + ie.getName() + "[" + size + "]..." + ie.getTextTrim());
            if(size > 0){
                needTraceList.add(ie);
            }
        }
        for(Element ie : needTraceList){
            System.out.println("==================================");
            showElementTree(ie, tab + 1);
        }
    }
    
    /**
     * 以xml產出javaBean
     */
    private static void showElementTree_toJavaBean(Element element, Map<String,String> javaBeans){
        List<Element> needTraceList = new ArrayList<Element>();
        List<Element> elist = element.elements();
        StringBuilder sb = new StringBuilder();
        sb.append(fetchAnnotation(element) + "\n");
        sb.append("private static class " + StringUtils.capitalize(StringUtilForDb.dbFieldToJava(element.getName())) + " { \n");
        Map<String, String> paramMap = new LinkedHashMap<String, String>();
        for(Element ie : elist){
            int size = ie.elements().size();
            String elementType = StringUtilForDb.dbFieldToJava(ie.getName());
            String annoStr = "\t" + fetchAnnotation(ie) + "\n";
            String val = null;
            if(size > 0){
                needTraceList.add(ie);
                val = annoStr + "\t" + StringUtils.capitalize(elementType) + " " + elementType + ";\n";
            }else{
                val = annoStr + "\t" + "String" + " " + elementType + ";\n";
            }
            if(!paramMap.containsKey(ie.getName())){
                paramMap.put(ie.getName(), val);
            }else if(paramMap.containsKey(ie.getName()) && !paramMap.get(ie.getName()).contains("List")){
                if(size > 0){
                    val = annoStr + "\tList<" + StringUtils.capitalize(elementType) + "> " + elementType + "s;\n";
                }else{
                    val = annoStr + "\tList<" + "String" + "> " + elementType + "s;\n";
                }
                paramMap.put(ie.getName(), val);
            }
        }
        for(String key : paramMap.keySet()){
            sb.append(paramMap.get(key));
        }
        sb.append("}\n");
        String parentName = element.getParent()!=null ? element.getParent().getName() + "." : "";
        javaBeans.put(parentName + element.getName(), sb.toString());
        for(Element ie : needTraceList){
            showElementTree_toJavaBean(ie, javaBeans);
        }
    }
    
    private static String fetchAnnotation(Element element){
        String parent = element.getParent() == null ? "" : element.getParent().getName();
        String tag = element.getName();
        String val = element.getTextTrim();
        return String.format("@TagInfo(parent = \"%s\", name = \"%s\", testVal = \"%s\")", parent, tag, val);
    }
    
    //==========================================================================================================
    
    @Target(value = { ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
            ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.PACKAGE })
    @Retention(RetentionPolicy.SOURCE)
    private @interface TagInfo {
        String parent();
        String name();
        String testVal() default "";
    }
}
