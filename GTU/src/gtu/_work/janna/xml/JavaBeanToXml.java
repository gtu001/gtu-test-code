package gtu._work.janna.xml;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class JavaBeanToXml {

    /**
     * @param args
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
        JavaBeanToXml simple = new JavaBeanToXml();

        DsName d = new DsName();
        Table t = new Table();
        Where w = new Where();
        Field_ f = new Field_();
        Operator o = new Operator();
        OrderBy ob = new OrderBy();
        Orderfield of = new Orderfield();

        d.name = "rcdf001m001";
        d.type = "select";
        d.tables.add(t);
        t.where = w;
        t.name = "rcdf001m";
        t.pretable = "table2";
        t.orderBy = ob;
        w.operators.add(o);
        ob.orderfields.add(of);
        of.name = "ordername";
        of.ordertype = "desc";
        o.between = "and";
        o.prerelation = "and";
        o.fields.add(f);
        f.name = "field";
        f.operation = "eq";
        f.prefield = "field2";

        Xml xml = new Xml();
        simple.createXml(d, xml);

        System.out.println(xml);
        System.out.println("done...");
    }

    void createXml(Object obj, Xml xml) throws IllegalArgumentException, IllegalAccessException {
        Class<?> clz = obj.getClass();
        Set<Class<?>> clzSet = new HashSet<Class<?>>();
        for (clzSet.add(clz); clz.getSuperclass() != Object.class; clzSet.add(clz.getSuperclass()), clz = clz
                .getSuperclass())
            ;
        for (Class<?> currentClz : clzSet) {
            // System.out.println("#### clz = " + obj);
            Attr at1 = currentClz.getAnnotation(Attr.class);
            if (at1 != null) {
                xml.tag = at1.val();
                // System.out.println("#### clz tag = " + at1.val());
            }
            String attribute = null;
            List<?> list = null;
            Field[] fields = currentClz.getDeclaredFields();
            // System.out.println("#### clz fields = " +
            // Arrays.toString(fields));
            for (Field field : fields) {
                Attr attr = field.getAnnotation(Attr.class);
                // System.out.println("### field = " + field.getName() + " - " +
                // attr);
                if (attr != null) {
                    attribute = attr.val();
                    Object fieldValue = fieldGet(obj, field);
                    if (fieldValue != null) {
                        if (fieldValue.getClass() == String.class) {
                            String fieldStr = (String) fieldValue;
                            if (StringUtils.isNotBlank(fieldStr)) {
                                xml.addAttr(attribute, fieldStr);
                            }
                        } else {
                            Xml xml1 = new Xml();
                            xml.addXml(xml1);
                            createXml(fieldValue, xml1);
                        }
                    }
                }
                if (field.getType().isAssignableFrom(List.class)) {
                    list = (List<?>) fieldGet(obj, field);
                    if (list != null) {
                        for (Object o : list) {
                            Xml xml1 = new Xml();
                            xml.addXml(xml1);
                            createXml(o, xml1);
                        }
                    }
                }
            }
        }
    }

    Object fieldGet(Object obj, Field field) throws IllegalArgumentException, IllegalAccessException {
        boolean access = field.isAccessible();
        field.setAccessible(true);
        Object rtn = field.get(obj);
        field.setAccessible(access);
        return rtn;
    }

    static class Attrib {
        String key;
        String value;

        Attrib(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String toString() {
            return String.format(" %s=\"%s\"", key, value);
        }
    }

    static class Xml {
        String tag;
        List<Attrib> attrs;
        List<Xml> xmls;

        Xml() {
            attrs = new ArrayList<Attrib>();
            xmls = new ArrayList<Xml>();
        }

        void addAttr(String key, String value) {
            attrs.add(new Attrib(key, value));
        }

        void addXml(Xml xml) {
            xmls.add(xml);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("<" + tag + (attrs.size() == 0 ? "" : " "));
            for (Attrib att : attrs) {
                sb.append(att);
            }
            sb.append((xmls.size() == 0 ? "/>" : ">") + "\r\n");
            for (Xml xml : xmls) {
                sb.append(xml);
            }
            sb.append((xmls.size() == 0 ? "" : String.format("</%s>\r\n", tag)));
            return sb.toString();
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface Attr {
        String val();
    }

    @Attr(val = "table")
    static class Table {
        @Attr(val = "name")
        String name;
        @Attr(val = "pretable")
        String pretable;
        @Attr(val = "where")
        Where where;
        @Attr(val = "orderby")
        OrderBy orderBy;

        @Override
        public String toString() {
            return "Table [name=" + name + ", pretable=" + pretable + ", where=" + where + ", orderBy=" + orderBy + "]";
        }
    }

    @Attr(val = "field")
    static class Field_ {
        @Attr(val = "name")
        String name;
        @Attr(val = "operation")
        String operation;
        @Attr(val = "prefield")
        String prefield;

        @Override
        public String toString() {
            return "Field_ [name=" + name + ", operation=" + operation + ", prefield=" + prefield + "]";
        }
    }

    @Attr(val = "ds")
    static class DsName {
        @Attr(val = "name")
        String name;
        @Attr(val = "type")
        String type;
        List<Table> tables;

        DsName() {
            tables = new ArrayList<Table>();
        }

        @Override
        public String toString() {
            return "DsName [name=" + name + ", type=" + type + ", tables=" + tables + "]";
        }
    }

    @Attr(val = "where")
    static class Where {
        List<Operator> operators;

        Where() {
            operators = new ArrayList<Operator>();
        }

        @Override
        public String toString() {
            return "Where [operators=" + operators + "]";
        }
    }

    @Attr(val = "operator")
    static class Operator {
        @Attr(val = "between")
        String between;
        @Attr(val = "prerelation")
        String prerelation;
        List<Field_> fields;

        Operator() {
            fields = new ArrayList<Field_>();
        }

        @Override
        public String toString() {
            return "Operator [between=" + between + ", prerelation=" + prerelation + ", fields=" + fields + "]";
        }
    }

    @Attr(val = "orderby")
    static class OrderBy {
        List<Orderfield> orderfields;

        OrderBy() {
            orderfields = new ArrayList<Orderfield>();
        }

        @Override
        public String toString() {
            return "OrderBy [orderfields=" + orderfields + "]";
        }
    }

    @Attr(val = "orderfield")
    static class Orderfield {
        @Attr(val = "name")
        String name;
        @Attr(val = "ordertype")
        String ordertype;

        @Override
        public String toString() {
            return "Orderfield [name=" + name + ", ordertype=" + ordertype + "]";
        }
    }
}
