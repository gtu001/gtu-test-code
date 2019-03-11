package gtu.xml;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.dom4j.Attribute;
import org.dom4j.Element;

import gtu.freemarker.FreeMarkerSimpleUtil;


public class XmlInfoUtil {
    
    public static void main(String[] args) {
        System.out.println("done...");
    }
    
    public static void showXmlInfo(Element element, StringBuilder sb) {
        sb.append("Tag:"+element.getName()+"["+element.getTextTrim()+"]");
        sb.append(",Attr:[");
        for (Object attr_ : element.attributes()) {
            Attribute attr = (Attribute) attr_;
            sb.append(attr.getName() + "=" + attr.getValue() + ", ");
        }
        sb.append("]");
        sb.append(",Children:[");
        for (Object node : element.selectNodes("*")) {
            Element node2 = (Element) node;
            showXmlInfo(node2, sb);
        }
        sb.append("]\n");
    }
    
    static final String XML_CLASS_FTL;
    static {
        StringBuilder sb = new StringBuilder();
        sb.append(" static class ${tag.name} {                   \n");
        sb.append("         <#list tag.attr as var>              \n");
        sb.append("                 String ${var};               \n");
        sb.append("         </#list>                             \n");
        sb.append("                                              \n");
        sb.append("         String text;                         \n");
        sb.append("                                              \n");
        sb.append("         <#list tag.children as var>          \n");
        sb.append("                 Tag ${var.name};                  \n");
        sb.append("         </#list>                             \n");
        sb.append(" }                                            \n");
        XML_CLASS_FTL = sb.toString();
    }
    
    public static String createTagClass(Element element){
        Tag tag = getTagInfo(element, new Tag());
        StringBuilder sb = new StringBuilder();
        createTagClass(tag, sb);
        return sb.toString();
    }
    
    static void createTagClass(Tag tag, StringBuilder sb){
        Map<String,Object> root = new HashMap<String,Object>();
        root.put("tag", tag);
        try {
            sb.append(FreeMarkerSimpleUtil.replace(XML_CLASS_FTL, root)).append("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!tag.children.isEmpty()){
            for(Tag ch : tag.children){
                createTagClass(ch, sb);
            }
        }
    }
    
    public static Tag getTagInfo(Element element){
        return getTagInfo(element, new Tag());
    }
    
    static Tag getTagInfo(Element element, Tag tag){
        tag.name = element.getName();
        for (Object attr_ : element.attributes()) {
            Attribute attr = (Attribute) attr_;
            tag.attr.add(attr.getName());
        }
        for (Object node : element.selectNodes("*")) {
            Element node2 = (Element) node;
            tag.children.add(getTagInfo(node2, new Tag()));
        }
        return tag;
    }
    
    public static class Tag {
        String name;
        Set<String> attr = new HashSet<String>();
        Set<Tag> children = new HashSet<Tag>();
        
        public String getName() {
            return name;
        }

        public Set<String> getAttr() {
            return attr;
        }

        public Set<Tag> getChildren() {
            return children;
        }

        @Override
        public String toString() {
            return "\nTag [name=" + name + ", attr=" + attr + ", children=" + children + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((attr == null) ? 0 : attr.hashCode());
            result = prime * result + ((children == null) ? 0 : children.hashCode());
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            return result;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Tag other = (Tag) obj;
            if (attr == null) {
                if (other.attr != null)
                    return false;
            } else if (!attr.equals(other.attr))
                return false;
            if (children == null) {
                if (other.children != null)
                    return false;
            } else if (!children.equals(other.children))
                return false;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            return true;
        }
    }
}
