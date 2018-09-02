package gtu.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfiguration;


/**
 * 用於設定PC 參數 , Annotation 用 Transfer 可將 Configuration 物件轉成 所對應 Java Bean
 * 
 * @author Troy
 */
public class TTTPcConfigurationTransfer {

    public static TTTPcConfigurationTransfer ins() {
        return new TTTPcConfigurationTransfer();
    }

    private TTTPcConfigurationTransfer() {
    }

    private Param register = Param.ins();
   

    /**
     * 測試
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // 參考 Test 的 Annotation 設定
        Object object = new Test();
        final DefaultConfiguration conf = new DefaultConfiguration("x", "y");

        // 假設 Configuration 有設定以下值
        conf.setAttribute("value", "test001");
        conf.setAttribute("int", "");
        conf.setAttribute("bool", "");
        conf.setAttribute("date", "2010/5/5");

        final TTTPcConfigurationTransfer pcConfigurationTransfer = TTTPcConfigurationTransfer.ins();
        
        pcConfigurationTransfer.getRegister()
        .register(InnerTest.class, new CreateNewInstance() {
            public Object create() {
                return new InnerTest();
            }
        }).register(Test.class, new CreateNewInstance(){
            public Object create() {
                return new Test();
            }
        }).register(InnerTTT.class, new CreateNewInstance(){
            public Object create() {
                return new InnerTTT();
            }
        }).addRole(Transfer.class, new Process() {
            public Object process(Annotation annotation, Class<?> parseToType, Type genericType, Param register, Object fromParseObject) throws Exception {
                Transfer transfer = (Transfer) annotation;
                TTTPcConfigurationTransfer.ins().getType(parseToType, genericType);
                Object parseVal = TTTPcConfigurationTransfer.ins().getTransferValue(transfer, parseToType, (Configuration)fromParseObject);
                return parseVal;
            }
        }).addRole(TransferChild.class, new Process() {
            public Object process(Annotation annotation, Class<?> parseToType, Type genericType, Param register, Object fromParseObject) throws Exception {
                TransferChild transfer = (TransferChild) annotation;
                TTTPcConfigurationTransfer.ins().getType(parseToType, genericType);
                Object createInstance = register.getClassNewInstance(parseToType).create();
                String name = transfer.name();
                System.out.println("name -- " + name);
                Configuration conf2 = ((Configuration)fromParseObject).getChild(transfer.name(), true);
                if (transfer.process().compareTo(TransferChild.Process.FIELD) == 0) {
                    pcConfigurationTransfer.transBeanField(createInstance, conf2);
                } else if (transfer.process().compareTo(TransferChild.Process.METHOD) == 0) {
                    pcConfigurationTransfer.transBeanMethod(createInstance, conf2);
                }
                return createInstance;
            }
        });
        
        pcConfigurationTransfer.transBeanField(object, conf);

        System.out.println(object);
    }

    public interface CreateNewInstance {
        public abstract Object create();
    }
    

    public static class Param {
        private Map<Class<?>, CreateNewInstance> objectNewInstance;
        private Map<Class<?>, Process> annotationRoleMap;

        public static Param ins() {
            return new Param();
        }

        public Param register(Class<?> clz, CreateNewInstance newInstance) {
            if (objectNewInstance == null) {
                objectNewInstance = new HashMap<Class<?>, CreateNewInstance>();
            }
            objectNewInstance.put(clz, newInstance);
            return this;
        }
        
        public Param addRole(Class<?> clz, Process process) {
            if (annotationRoleMap == null) {
                annotationRoleMap = new HashMap<Class<?>, Process>();
            }
            annotationRoleMap.put(clz, process);
            return this;
        }

        public CreateNewInstance getClassNewInstance(Class<?> clz) throws Exception {
            if (objectNewInstance == null || objectNewInstance.isEmpty()) {
                throw new Exception("未登錄任何Class的Create Method");
            }
            System.out.println("1........==" + clz.getGenericSuperclass());
            System.out.println("2........==" + clz.getComponentType());
            System.out.println("3........==" + clz);
            if (!objectNewInstance.containsKey(clz)) {
                throw new Exception("未登錄此Class : " + clz.getName() + " 的Create Method");
            }
            return objectNewInstance.get(clz);
        }

        public Map<Class<?>, CreateNewInstance> getObjectNewInstance() {
            return objectNewInstance;
        }
        public Map<Class<?>, Process> getAnnotationRoleMap() {
            return annotationRoleMap;
        }
    }

    private Object getTransferValue(Transfer transfer, Class<?> parseToType, Configuration conf) throws Exception {
        String name = transfer.name();
        boolean require = transfer.require();
        String errMessage = transfer.errorMessage();
        String defaultValue = transfer.defaultValue();
        String confAttr = conf.getAttribute(name, "");
        if (require && confAttr.length() == 0) {
            if (errMessage.length() == 0) {
                errMessage = String.format("%s 參數為必須!", name);
            }
            throw new Exception(errMessage);
        }
        String val = confAttr.length() == 0 ? defaultValue : confAttr;
        Object parseVal = null;
        try {
            parseVal = this.parseType(val, parseToType);
        } catch (NumberFormatException ex) {
            if (ex.getMessage().equals("For input string: \"\"") && defaultValue.length() == 0) {
                parseVal = this.parseType("0", parseToType);
            }
        }
        return parseVal;
    }
    
    public void getType(Class<?> clz, Type type) {
        System.out.println("######");
        System.out.println(type);
        System.out.println(type.getClass());
        if(type instanceof Class<?>) {
            Class<?> rtn = (Class<?>)type;
            System.out.println("aaaa -- " + rtn);
        }
        if(type instanceof ParameterizedType) {
            ParameterizedType impl = (ParameterizedType)type;
            System.out.println("aaaa -- " + Arrays.toString(impl.getActualTypeArguments()));
        }
        System.out.println("######");
    }

    public interface Process {
        public abstract Object process(Annotation annotation, Class<?> parseToType, Type genericType, Param register, Object fromParseObject) throws Exception;
    }

    private void transBeanField(Object object, Object fromParseObject) throws Exception {
        Class<?> clz = object.getClass();
        for (Field field : clz.getDeclaredFields()) {
            for (Annotation anno : field.getDeclaredAnnotations()) {
                Map<Class<?>, Process> annotationMap = register.getAnnotationRoleMap();
                System.out.println(".1..." + anno.annotationType());
                System.out.println(".2..." + annotationMap);
                if (annotationMap.containsKey(anno.annotationType())) {
                    System.out.println(anno);
                    Class<?> fieldType = field.getType();
                    Type genericType = field.getGenericType();
                    System.out.println("@@@@@" + fieldType + "......." + genericType);
                    Object value = annotationMap.get(anno.annotationType()).process(anno, fieldType, genericType, register, fromParseObject);
                    boolean bool = field.isAccessible();
                    field.setAccessible(true);
                    field.set(object, value);
                    field.setAccessible(bool);
                }else {
                    System.out.println("....." + anno.annotationType());
                }
            }
        }
    }

    private void transBeanMethod(Object object, Object fromParseObject) throws Exception {
        Class<?> clz = object.getClass();
        for (Field field : clz.getDeclaredFields()) {
            for (Annotation anno : field.getDeclaredAnnotations()) {
                Map<Class<?>, Process> annotationMap = register.getAnnotationRoleMap();
                if (annotationMap.containsKey(anno.annotationType())) {
                    System.out.println(anno);
                    String fieldName = field.getName();
                    Class<?> fieldType = field.getType();
                    Type genericType = field.getGenericType();
                    String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Object value = annotationMap.get(anno.annotationType()).process(anno, fieldType, genericType, register, fromParseObject);
                    clz.getMethod(methodName, fieldType).invoke(object, value);
                }
            }
        }
    }

    private Object parseType(String value, Class<?> paramType) throws ParseException {
        if (paramType == String.class) {
            return value;
        }
        if (paramType == boolean.class || paramType == Boolean.class) {
            return Boolean.parseBoolean(value);
        }
        if (paramType == short.class || paramType == Short.class) {
            return Short.parseShort(value);
        }
        if (paramType == byte.class || paramType == Byte.class) {
            return Byte.parseByte(value);
        }
        if (paramType == int.class || paramType == Integer.class) {
            return Integer.parseInt(value);
        }
        if (paramType == long.class || paramType == Long.class) {
            return Long.parseLong(value);
        }
        if (paramType == float.class || paramType == Float.class) {
            return Float.parseFloat(value);
        }
        if (paramType == double.class || paramType == Double.class) {
            return Double.parseDouble(value);
        }
        if (paramType == char.class) {
            return value.charAt(0);
        }
//        if (paramType == Date.class) {
//            return PcHelper.ins().parseDate(value);
//        }
//        if (paramType == Map.class) {
//            return PcHelper.ins().parseMap(value);
//        }
//        if (paramType == List.class) {
//            return PcHelper.ins().parseList(value);
//        }
        return value;
    }

    @Target( { java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Transfer {
        public abstract boolean require() default false;

        public abstract String name();

        public abstract String errorMessage() default "";

        public abstract String defaultValue() default "";
    }

    @Target( { java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TransferChild {
        public abstract boolean require() default false;

        public abstract String name();

        public abstract String errorMessage() default "";

        public abstract Process process() default Process.FIELD;

        enum Process {
            FIELD, METHOD
        }
    }
    
    private static class InnerTTT {
        @Transfer(name = "aa", require = false, defaultValue = "okok")
        private String ok;
    }

    private static class InnerTest {
        @Transfer(name = "aa", require = false, defaultValue = "---")
        private String aa;
        @Transfer(name = "bb", require = false, defaultValue = "---")
        private String bb;
        
        @TransferChild(name = "wewewe")
        private InnerTTT tttt;

        @Override
        public String toString() {
            return "InnerTest [aa=" + aa + ", bb=" + bb + ", tttt=" + tttt + "]";
        }
    }

    protected static class Test {
        @Transfer(name = "xx", require = false, defaultValue = "---")
        private String value;
        @Transfer(name = "bool", require = false, defaultValue = "T")
        private boolean bool;
        @Transfer(name = "int", require = false, defaultValue = "1")
        private int intVal;
        @Transfer(name = "xxxxx", require = false, defaultValue = "CURRENT_DATE_TIME")
        private Date date;
        @TransferChild(name = "inner")
        private InnerTest innerTest;
        @TransferChild(name = "innerList")
        private List<InnerTest> innerTestList;

        @Override
        public String toString() {
            return "Test [bool=" + bool + ", date=" + date + ", innerTest=" + innerTest + ", innerTestList=" + innerTestList + ", intVal=" + intVal + ", value=" + value + "]";
        }
    }

    public Param getRegister() {
        return register;
    }
    public void setRegister(Param register) {
        this.register = register;
    }
}
