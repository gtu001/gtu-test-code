package gtu.log.finder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import gtu.file.FileUtil;
import gtu.freemarker.FreeMarkerSimpleUtil;


public class DebugMointerTypeUtilForHTML {
    
    static class CCCC {
        String xxxx;
    }
    
    static class AAA {
        String cvv;
        String eeee;
        CCCC ccc = new CCCC();
    }
    
    static class Test11 {
        AAA aaaaa = new AAA();
        String aa = "AA";
        String bb = "dd";
        int cc = 1;
        public String xxxx(String vvvv, int vdddsd){
            return "";
        }
        public void vvvsdsdfdf(String vvvv, int vdddsd){
        }
    }
    
    public DebugMointerTypeUtilForHTML(){
        config = new Config();
    }
    
    private final static String HTML_STR;
    static {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = DebugMointerTypeUtilForHTML.class.getResource("DebugMointerUI_base.html");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            for(String line = null; (line = reader.readLine())!=null;){
                sb.append(line + "\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        HTML_STR = sb.toString();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        DebugMointerTypeUtilForHTML test = new DebugMointerTypeUtilForHTML();
        config.debug = true;
        Map<Object,Object> map = new HashMap<Object,Object>();
        Map<Object,Object> map2 = new HashMap<Object,Object>();
        map2.put("aaa", "bbbb");
        map2.put(new Test11(), new Test11());
        List<Object> sss22s = new ArrayList<Object>();
        sss22s.add("bbbb");
        sss22s.add(new Test11());
        map.put(new Test11(), new Test11());
        map.put("2222", 111);
        List<Object> ssss = new ArrayList<Object>();
        ssss.add("aaaaa");
        ssss.add("bbbbb");
        ssss.add(new Test11());
        ssss.add(map);
        ssss.add(sss22s);
        ssss.add(map2);
        test.executeAll(ssss, null, null, null, -1, true);
        System.out.println("done...");
    }
    
    private static Config config = new Config();
    private static class Config {
        File baseDir = FileUtil.DESKTOP_DIR;
        boolean sortMembers;
        int layerLimit = -1;
        String rootSpecialTitle;
        List<String> ignoreContains = new ArrayList<String>();
        boolean debug;
        PrintStream outForDebug;
    }

    private static void debug(Object message){
        if(message == null){
            return;
        }
        if(config.outForDebug == null){
            try {
                File file = new File(FileUtil.DESKTOP_DIR, DebugMointerTypeUtilForHTML.class.getSimpleName() + "_debug.txt");
                file = FileUtil.getNewFile(file);
                config.outForDebug = new PrintStream(new FileOutputStream(file), true);
                System.out.println("log file : " + file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        PrintStream out = config.outForDebug;
        if(Throwable.class.isAssignableFrom(message.getClass())){
            ((Throwable)message).printStackTrace(out);
        }else{
            String dateStr = "[" + String.format("%tY/%<tm/%<td %<tH:%<tM:%<tS", System.currentTimeMillis()) + "]";
            out.println(dateStr + String.valueOf(message));
            System.out.println(message);
        }
    }
    
    private static void writeHtml(HtmlObjectGenerate rootObject){
        try{
            if(!rootObject.currentDir.exists()){
                rootObject.currentDir.mkdirs();
            }
            if(rootObject.currentFile.exists()){
                debug("檔案重複:" + rootObject.currentFile);
                return;
            }
            Map<String,Object> root = new HashMap<String,Object>();
            root.put("root", rootObject);
            File outputFile = rootObject.currentFile;
            if(!config.debug){
                FreeMarkerSimpleUtil.replace(HTML_STR, root, new FileOutputStream(outputFile));
            }else{
                FreeMarkerSimpleUtil.replace(//
                        new File("C:/workspace/workspace/GTU/src/gtu/log/finder/DebugMointerUI_base.html"), //
//                        new File("D:/workspace/Gtu/src/gtu/log/finder/DebugMointerUI_base.html"), //
                        root, new FileWriter(outputFile));//測試用
            }
            debug("寫黨完成 >> " + outputFile.exists() + " >> " + outputFile.getName() + " >> " + outputFile + " >> " + rootObject);
        }catch(Exception ex){
            debug("writeHtml = " + ReflectionToStringBuilder.toString(rootObject));
            debug(ex);
        }
    }
    
    private List<String> getCommonIgnoreClassPatternList() {
        return Arrays.asList(//
                "java.util.regex.Pattern",//
                "java.lang.reflect",//
                "java.util.HashMap$EntrySet",//
                "java.util.HashMap$Entry",//
                "org.apache.commons.logging.impl.Log4JLogger",//
                "org.apache.log4j.Logger",//
                "java.lang.ref"//
        );
    }
    
    /**
     * @param object 來源物件
     * @param title 標題
     * @param baseDir 基層目錄(預設桌面)
     * @param ignoreContains 要忽略的物件
     * @param layerLimit 限制階層
     * @param sort 是否排序
     */
    public void executeAll(final Object object, final String title, final File baseDir, //
            final List<String> ignoreContains, final int layerLimit, //
            final boolean sort){
        
        if (baseDir != null) {
            config.baseDir = baseDir;
        }
        if (StringUtils.isNotBlank(title)) {
            config.rootSpecialTitle = title;
        }
        if (CollectionUtils.isNotEmpty(ignoreContains)) {
            config.ignoreContains = ignoreContains;
        }
        config.ignoreContains.addAll(getCommonIgnoreClassPatternList());
        config.layerLimit = layerLimit;
        config.sortMembers = sort;
        if(object == null){
            return;
        }
        HtmlObjectGenerate rootObject = new HtmlObjectGenerate(object, null, null, null);
    }
    
    public static class HtmlObjectGenerate implements Serializable{
        private static final long serialVersionUID = 7728632637112641929L;
        boolean valueIsNull;
        Class<?> clz;
        Object value;
        Throwable errorOccor;
        File currentDir;
        File currentFile;
        String fieldName;
        HtmlObjectGenerate parent;
        List<HtmlObjectGenerate> fieldList = Collections.emptyList();
        String fileNameShort;
        Map<HtmlObjectGenerate,HtmlObjectGenerate> relateMap = Collections.emptyMap();
        boolean needCreateFile;
        Class<?> indicateClass;//特別指定使用反射的class
        String specialTitle = "";
        MethodZ[] methodList;
        String infoStr;
        int layerCount;
        String extraLink;
        
        int fieldListNeedSize = -1;
        int relateMapNeedSize = -1;
        
        Thread checkDataCompleteThread;
        Thread innerLoopThread;
        
        public void setIndicateClass(Class<?> indicateClass){
            if(valueIsNull || indicateClass == null){
                return;
            }
            if(indicateClass.isAssignableFrom(clz)){
                this.indicateClass = indicateClass;
                debug("類別無法指定為此類別 :" + indicateClass.getName());
            }else{
                this.indicateClass = null;
                debug("設定成功!:" + indicateClass.getName());
            }
        }
        
        HtmlObjectGenerate(Object value, Object field, Throwable errorOccor, HtmlObjectGenerate parent){
            this.value = value;
            if(field instanceof Field){
                Field field2 = ((Field) field);
                fieldName = field2.getName();
                clz = field2.getType();
            }else {
                fieldName = String.valueOf(field);
            }
            if (value != null) {
                clz = value.getClass();
            }else{
                valueIsNull = true;
            }
            this.errorOccor = errorOccor;
            this.parent = parent;
            if(parent != null){
                this.currentDir = new File(parent.currentDir, String.valueOf(parent.hashCode()));
                this.fileNameShort = fetchFileName(value, fieldName);
            }else{
                this.fileNameShort = "index";
                if(StringUtils.isNotBlank(config.rootSpecialTitle)){
                    specialTitle = config.rootSpecialTitle;
                }
            }
            
            layerCount = initLayerCount();
            methodList = initMethodList(value);
            needCreateFile = initNeedCreateFile(value);
            infoStr = initInfo(value);
            
            //根物件
            if(this.currentDir == null){
                String fileName = getOrignToString(value);
                this.currentDir = new File(config.baseDir, fileName);
            }
            this.currentFile = new File(currentDir, fileNameShort + ".html");
            
            Validate.notNull(currentDir, "ERROR:" + this.toString());
            Validate.notNull(currentFile, "ERROR:" + this.toString());
            
            //設定連結
            extraLink = initExtraLink();
            
            //寫子節點
            startInnerLoopThread(parent == null);//TODO
        }
        
        private void startInnerLoopThread(boolean needRun){
            if(needRun && isHasExtraLink()){
                startInnerLoopThread(value, this);
            }
        }
        
        private boolean initNeedCreateFile(Object value){
            if(valueIsNull){
                return false;
            }
            if(value instanceof Class){
                return false;
            }
            if(DebugMointerTypeUtil.isPrimitive(clz)){
                return false;
            }
            if(value instanceof Date||value instanceof Calendar||//
                    value instanceof Timestamp||value instanceof java.sql.Date){
                return false;
            }
            if(config.layerLimit != -1 && layerCount > config.layerLimit){
                specialTitle = "[超過階層限制 : " + config.layerLimit + "]";
                return false;
            }
            if(errorOccor != null){
                specialTitle = "[發稱錯誤 : " + errorOccor.getMessage() + "]";
                return false;
            }
            if(isIgnoreClass()){
                specialTitle = "[忽略類別]";
                return false;
            }
            return true;
        }
        
        private int initLayerCount(){
            if(this.parent == null){
                return 0;
            }
            return this.parent.layerCount + 1;
        }
        
        private boolean isIgnoreClass(){
            if(CollectionUtils.isEmpty(config.ignoreContains)){
                return false;
            }
            Class<?> tmpClass = indicateClass != null ? indicateClass : clz;
            String tmpClassName = tmpClass.getName();
            for(String str : config.ignoreContains){
                if(StringUtils.isNotBlank(str) && tmpClassName.contains(str)){
                    return true;
                }
                try{
                    if(Pattern.compile(str).matcher(tmpClassName).find()){
                        return true;
                    }
                }catch(Exception ex){
                }
            }
            return false;
        }
        
        private void startInnerLoopThread(final Object value, final HtmlObjectGenerate htmlObj) {
            Map<HtmlObjectGenerate, HtmlObjectGenerate> relateMap = new LinkedHashMap<HtmlObjectGenerate, HtmlObjectGenerate>();
            if (Map.class.isAssignableFrom(htmlObj.clz)) {
                Map<?, ?> map = (Map<?, ?>) value;
                relateMapNeedSize = map.size();
                Map<String, Object> sortKeyMap = null;
                if (config.sortMembers) {
                    sortKeyMap = new TreeMap<String, Object>();
                } else {
                    sortKeyMap = new HashMap<String, Object>();
                }
                for (Object key : map.keySet()) {
                    sortKeyMap.put(String.valueOf(key), key);
                }
                for (String key : sortKeyMap.keySet()) {
                    Object realKey = sortKeyMap.get(key);
                    HtmlObjectGenerate $key = new HtmlObjectGenerate(realKey, null, null, htmlObj);
                    HtmlObjectGenerate $value = new HtmlObjectGenerate(map.get(realKey), null, null, htmlObj);
                    relateMap.put($key, $value);
                }
            } else if (Collection.class.isAssignableFrom(htmlObj.clz)) {
                Collection<?> coll = (Collection<?>) value;
                relateMapNeedSize = coll.size();
                int index = 0;
                for (Object val : coll) {
                    HtmlObjectGenerate $key = new HtmlObjectGenerate(index, null, null, htmlObj);
                    HtmlObjectGenerate $value = new HtmlObjectGenerate(val, null, null, htmlObj);
                    relateMap.put($key, $value);
                    index++;
                }
            } else if (htmlObj.clz.isArray()) {
                relateMapNeedSize = Array.getLength(value);
                for (int ii = 0, len = Array.getLength(value); ii < len; ii++) {
                    HtmlObjectGenerate $key = new HtmlObjectGenerate(ii, null, null, htmlObj);
                    HtmlObjectGenerate $value = new HtmlObjectGenerate(Array.get(value, ii), null, null, htmlObj);
                    relateMap.put($key, $value);
                }
            }
            
            if (!relateMap.isEmpty()) {
                htmlObj.relateMap = relateMap;
            }else{
                List<HtmlObjectGenerate> fieldList = new ArrayList<HtmlObjectGenerate>();
                Class<?> clz = indicateClass != null ? indicateClass : htmlObj.clz;
                Field[] fields = clz.getDeclaredFields();
                fieldListNeedSize = fields.length;
                for (Field f : fields) {
                    try {
                        f.setAccessible(true);
                        Object innerValue = f.get(value);
                        HtmlObjectGenerate html = new HtmlObjectGenerate(innerValue, f, null, htmlObj);
                        fieldList.add(html);
                    } catch (Exception ex) {
                        debug(ex);
                        fieldList.add(new HtmlObjectGenerate("error", f, ex, htmlObj));
                    }
                }
                htmlObj.fieldList = fieldList;
            }
            
            //寫黨
            writeHtml(htmlObj);
            
            for(HtmlObjectGenerate k : htmlObj.relateMap.keySet()){
                HtmlObjectGenerate v = htmlObj.relateMap.get(k);
                k.startInnerLoopThread(true);
                v.startInnerLoopThread(true);
            }
            for(HtmlObjectGenerate f : htmlObj.fieldList){
                f.startInnerLoopThread(true);
            }
        }

        private String fetchFileName(Object object, String indicateName){
            if(StringUtils.isNotBlank(indicateName) && !StringUtils.equalsIgnoreCase("null", indicateName)){
                indicateName = indicateName + "_";
            }else{
                indicateName = "";
            }
            return indicateName + getOrignToString(object);
        }
        
        public boolean isHasFieldList(){
            return CollectionUtils.isNotEmpty(fieldList);
        }
        
        private String initInfo(Object value){
            if(StringUtils.isBlank(specialTitle) && parent == null){
                specialTitle = "[ROOT]";
            }else{
//              specialTitle = "["+layerCount+"]";//TODO debug用
            }
            if(value == null){
                return specialTitle + "null";
            }
            if(errorOccor!=null){
                return specialTitle + "ERROR:" + errorOccor.getMessage();
            }
            Class<?> tmpClass = indicateClass != null ? indicateClass : clz;
            if(Map.class.isAssignableFrom(tmpClass) || Collection.class.isAssignableFrom(tmpClass)){
                int sizeZ = -1;
                for(Method method : value.getClass().getMethods()){
                    if(method.getName().equals("size") && method.getReturnType() == int.class && method.getParameterTypes().length == 0){
                        try{
                            method.setAccessible(true);
                            sizeZ = (Integer)method.invoke(value, new Object[0]);
                            break;
                        }catch(Exception ex){
                            debug(ex);
                        }
                    }
                }
                return specialTitle + getClassName() + "@" + Integer.toHexString(value.hashCode()) + " - size : " + sizeZ;
            }else if(tmpClass.isArray()){
                return specialTitle + getClassName() + "@" + Integer.toHexString(value.hashCode()) + " - size : " + Array.getLength(value);
            }else if(DebugMointerTypeUtil.isPrimitive(tmpClass)){
                return specialTitle + getClassName() + "@" + value;
            }else if(value instanceof Date||value instanceof Calendar||value instanceof Timestamp||value instanceof java.sql.Date){
                return specialTitle + getClassName() + "@" + DebugMointerTypeUtil.dateToString(value);
            }else{
                try{
                    return specialTitle + getReflectDetail(ReflectionToStringBuilder.toString(value));    
                }catch(Exception ex){
//                    debug("getInfo = " + ReflectionToStringBuilder.toString(this));
//                    debug(ex);
                    List<String> excludeList = new ArrayList<String>();
                    for(Field f : tmpClass.getDeclaredFields()){
                        if(Collection.class.isAssignableFrom(f.getType()) || Map.class.isAssignableFrom(f.getType())){
                            excludeList.add(f.getName());
                        }
                    }
                    try{
                        return specialTitle + getReflectDetail(ReflectionToStringBuilder.toStringExclude(value, excludeList));
                    }catch(Exception ex22){
//                        debug("getInfo = " + ReflectionToStringBuilder.toString(this));
//                        debug(ex22);
                        return specialTitle + getClassName() + "@" + Integer.toHexString(value.hashCode());
                    }
                }
            }
        }
        
        private static String getReflectDetail(String value){
            if(value == null || value.length() < 100){
                return value;
            }
            return StringUtils.substring(value, 0 , 100) + "...etc";
        }
        
        public String getInfo(){
            return infoStr;
        }
        
        private MethodZ[] initMethodList(Object value){
            if(value == null || Map.class.isAssignableFrom(clz) || Collection.class.isAssignableFrom(clz) || clz.isArray()){
                return new MethodZ[0];
            }
            Class<?> tmpClz = indicateClass!=null ? indicateClass : clz;
            Map<String,MethodZ> sortKeyMap = null;
            if(config.sortMembers){
                sortKeyMap = new TreeMap<String,MethodZ>();
            }else{
                sortKeyMap = new HashMap<String,MethodZ>();
            }
            for(Method method : tmpClz.getDeclaredMethods()){
                sortKeyMap.put(method.getName(), new MethodZ(method));
            }
            return sortKeyMap.values().toArray(new MethodZ[0]);
        }
        
        public MethodZ[] getMethodList(){
            return methodList;
        }
        
        public String getClassName(Object object){
            if(object instanceof MethodZ){
                List<String> list = new ArrayList<String>();
                MethodZ method = (MethodZ)object;
                for(Class<?> c : method.getParameterTypes()){
                    list.add(DebugMointerTypeUtilForHTML.getClassName(c));
                }
                return list.toString();
            }else if(object instanceof Class){
                Class<?> clx = (Class<?>)object;
                return DebugMointerTypeUtilForHTML.getClassName(clx);
            }else{
                return "[ERROR]不支援的物件: " + object;
            }
        }
        
        public boolean isHasMethodList(){
            return getMethodList().length > 0;
        }
        
        public String getRelateMapTh(int index){
            String[] th = new String[2];
            if(Map.class.isAssignableFrom(clz)){
                th = new String[]{"key", "value"};
            }else if(Collection.class.isAssignableFrom(clz)){
                th = new String[]{"index", "value"};
            }else if(clz.isArray()){
                th = new String[]{"index", "value"};
            }else{
                th = new String[]{"NA", "NA"};
            }
            return th[index];
        }
        
        public String getModifier(MethodZ method){
            return DebugMointerUI.JObject.getModifier(method.getModifiers());
        }
        
        public String getClassName(){
            Class<?> tmpClz = null;
            if(indicateClass != null){
                tmpClz = indicateClass;
            }else if(clz != null){
                tmpClz = clz;
            }else{
                return "NA";
            }
            return DebugMointerTypeUtilForHTML.getClassName(tmpClz);
        }

        public String getFieldName() {
            return fieldName;
        }
        
        public boolean isHasExtraLink(){
            return needCreateFile;
        }
        
        private String initExtraLink(){
            String extraLink = "#";
            if(needCreateFile && parent!=null){
                try {
                    String currentLink = currentFile.getCanonicalPath();
                    String rootPath = currentDir.getParentFile().getCanonicalPath();
                    extraLink = currentLink.substring(rootPath.length());
                    extraLink = extraLink.replace('\\', '/');
                    if (extraLink.startsWith("/")) {
                        extraLink = extraLink.substring(1);
                    }
                } catch (Exception ex) {
                    debug("START ######################");
                    debug("getExtraLink = " + ReflectionToStringBuilder.toString(this));
                    debug("canonicalPath = " + FileUtil.getCanonicalPath(currentFile));
                    debug("rootPath = " + FileUtil.getCanonicalPath(parent.currentDir));
                    debug("extraLink = " + extraLink);
                    debug(ex);
                    debug("END ######################");
                }
            }
            return extraLink;
        }
        
        public String getExtraLink(){
            return extraLink;
        }

        @Override
        public String toString() {
            return "HtmlObjectGenerate [specialTitle=" + specialTitle + ", layerCount=" + layerCount
                    + ", needCreateFile=" + needCreateFile + ", fieldName=" + fieldName + ", fileNameShort="
                    + fileNameShort + ", clz=" + clz + ", currentFile=" + currentFile + ", fieldListNeedSize=" + fieldListNeedSize + ", relateMapNeedSize="
                    + relateMapNeedSize + "]";
        }

        public List<HtmlObjectGenerate> getFieldList() {
            return fieldList;
        }

        public boolean isHasRelateMap(){
            return relateMap!=null && !relateMap.isEmpty();
        }

        public Map<HtmlObjectGenerate, HtmlObjectGenerate> getRelateMap() {
            return relateMap;
        }
        
        public String fetchRelateMapValueInfo(HtmlObjectGenerate key){
            if(relateMap == null || !relateMap.containsKey(key)){
                return "null";
            }
            return relateMap.get(key).getInfo();
        }
        
        public HtmlObjectGenerate fetchRelateMapValue(HtmlObjectGenerate key){
            if(relateMap == null || !relateMap.containsKey(key)){
                return new HtmlObjectGenerate(null, null, null, null);
            }
            return relateMap.get(key);
        }

        //for debug ↓↓↓↓↓↓
        public String getCurrentDir() {
            return String.valueOf(currentDir);
        }

        public String getCurrentFile() {
            return String.valueOf(currentFile);
        }
        
        public boolean isCurrentFileExist(){
            return currentFile.exists();
        }
    }
    
    private static String getClassName(Class<?> indicateClass){
        String suffix = "";
        Class<?> tmpClass = indicateClass;
        if(tmpClass.isArray()){
            suffix = "[]";
            tmpClass = tmpClass.getComponentType();
        }
        return (DebugMointerTypeUtil.isPrimitive(tmpClass) ? tmpClass.getSimpleName() : tmpClass.getName()) + suffix;
    }
    
    private static String getOrignToString(Object object){
        if(object == null){
            return "<null>";
        }
        return object.getClass().getName() + "@" + Integer.toHexString(object.hashCode());
    }
    
    public static class MethodZ implements Serializable{
        private static final long serialVersionUID = 2633624326589676552L;
        private int modifiers;
        private String name;
//        private Annotation[] declaredAnnotations;
//        private Class declaringClass;
        private Class<?>[] parameterTypes;
        private Class<?> returnType;
//        private TypeVariable[] typeParameters;
//        private boolean synthetic;
//        private String toGenericString;
//        private Object defaultValue;
//        private Class[] exceptionTypes;
//        private Type[] genericExceptionTypes;
//        private Type[] genericParameterTypes;
//        private Type genericReturnType;
//        private Annotation[][] parameterAnnotations;
//        private boolean bridge;
//        private boolean varArgs;
//        private Annotation[] annotations;
//        private boolean accessible;
        
        MethodZ(Method entity){
            modifiers = entity.getModifiers();
            name = entity.getName();
//            declaredAnnotations = entity.getDeclaredAnnotations();
//            declaringClass = entity.getDeclaringClass();
            parameterTypes = entity.getParameterTypes();
            returnType = entity.getReturnType();
//            typeParameters = entity.getTypeParameters();
//            synthetic = entity.isSynthetic();
//            toGenericString = entity.toGenericString();
//            defaultValue = entity.getDefaultValue();
//            exceptionTypes = entity.getExceptionTypes();
//            genericExceptionTypes = entity.getGenericExceptionTypes();
//            genericParameterTypes = entity.getGenericParameterTypes();
//            genericReturnType = entity.getGenericReturnType();
//            parameterAnnotations = entity.getParameterAnnotations();
//            bridge = entity.isBridge();
//            varArgs = entity.isVarArgs();
//            annotations = entity.getAnnotations();
//            accessible = entity.isAccessible();
        }
        
        public int getModifiers(){  return this.modifiers;}
        public String getName(){  return this.name;}
//        public Annotation[] getDeclaredAnnotations(){  return this.declaredAnnotations;}
//        public Class getDeclaringClass(){  return this.declaringClass;}
        public Class<?>[] getParameterTypes(){  return this.parameterTypes;}
        public Class<?> getReturnType(){  return this.returnType;}
//        public TypeVariable[] getTypeParameters(){  return this.typeParameters;}
//        public boolean isSynthetic(){  return this.synthetic;}
//        public String toGenericString(){  return this.toGenericString;}
//        public Object getDefaultValue(){  return this.defaultValue;}
//        public Class[] getExceptionTypes(){  return this.exceptionTypes;}
//        public Type[] getGenericExceptionTypes(){  return this.genericExceptionTypes;}
//        public Type[] getGenericParameterTypes(){  return this.genericParameterTypes;}
//        public Type getGenericReturnType(){  return this.genericReturnType;}
//        public Annotation[][] getParameterAnnotations(){  return this.parameterAnnotations;}
//        public boolean isBridge(){  return this.bridge;}
//        public boolean isVarArgs(){  return this.varArgs;}
//        public Annotation[] getAnnotations(){  return this.annotations;}
//        public boolean isAccessible(){  return this.accessible;}
    }
}
