package gtu.springdata;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Entity;

import org.reflections.Reflections;
import org.reflections.Store;
import org.springframework.util.StringUtils;

import com.google.common.collect.Multimap;

public class GetterSetterAppender {

    public static void main(String[] args) {
        GetterSetterAppender t = new GetterSetterAppender();
        File baseDir = new File("D:/workstuff/workspace_taida");

        t.execute("com.delta.mes.model.isa95", baseDir);

        System.out.println("done...");
    }

    public void execute(String basePackage, File baseDir) {
        javaLst = new ArrayList<>();
        searchFilefind(baseDir, ".*\\.java$", javaLst);

        Reflections ref = new Reflections(basePackage);

        Set<String> sets = getSubClassPaths(ref);
        Set<Class> clsSet = sets.stream().map(p -> {
            try {
                return Class.forName(p);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).filter(c -> c.isAnnotationPresent(Entity.class)).collect(Collectors.toCollection(LinkedHashSet::new));

        clsSet.stream().forEach(c -> {
            checkGetterSetter(c);
        });
    }

    List<File> javaLst;

    private File findClassFile(Class clz) {
        return javaLst.stream().filter(f -> f.getName().replaceAll("\\.java$", "").equals(clz.getSimpleName())).findAny().orElse(null);
    }

    private static final String GETTER_STR;
    private static final String SETTER_STR;
    private JavaFileEndAppender javaFileEndAppender = new JavaFileEndAppender();
    static {
        StringBuffer sb = new StringBuffer();
        sb.append("    public {1} {0}() '{'            \n");
        sb.append("        return {2};                 \n");
        sb.append("    }                               \n");
        GETTER_STR = sb.toString();
        sb.delete(0, sb.length());

        sb.append("    public void {0}({1} {2}) '{'      \n");
        sb.append("        this.{2} = {2};             \n");
        sb.append("    }                               \n");
        SETTER_STR = sb.toString();
    }

    private String getMethodString(String fieldName, Class fieldType, Class genericType, boolean isGetter) {
        String parameter = fieldName;
        String methodName = null;
        String typeName = fieldType.getSimpleName();
        if (genericType != null) {
            typeName = typeName + String.format("<%s>", genericType.getSimpleName());
        }
        if (isGetter) {
            if (fieldType == boolean.class || fieldType == Boolean.class) {
                methodName = "is" + StringUtils.capitalize(fieldName);
            } else {
                methodName = "get" + StringUtils.capitalize(fieldName);
            }
            return MessageFormat.format(GETTER_STR, new Object[] { methodName, typeName, parameter });
        } else {
            methodName = "set" + StringUtils.capitalize(fieldName);
            return MessageFormat.format(SETTER_STR, new Object[] { methodName, typeName, parameter });
        }
    }

    private class JavaFileEndAppender {
        JavaFileEndAppender() {
        }

        private List<String> readLines(File file) {
            BufferedReader reader = null;
            try {
                List<String> lst = new ArrayList<>();
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
                for (String line = null; (line = reader.readLine()) != null;) {
                    lst.add(line);
                }
                return lst;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            } finally {
                try {
                    reader.close();
                } catch (Exception e) {
                }
            }
        }

        private int findJavaEndPos(List<String> lst) {
            for (int ii = lst.size() - 1; ii > 0; ii--) {
                String val = lst.get(ii);
                if (val == null || val.trim().length() == 0) {
                    continue;
                }
                if (val.trim().equals("}")) {
                    return ii - 1;
                }
            }
            return -1;
        }

        private void apply(File file, String blockContent) {
            List<String> lst = (readLines(file));

            int maxPos = findJavaEndPos(lst);

            System.out.println(file + " -- " + maxPos);

            List<String> a1 = lst.subList(0, maxPos + 1);
            List<String> a2 = lst.subList(maxPos + 1, lst.size());

            List<String> all = new ArrayList<String>();
            all.addAll(a1);
            all.add("\r\n");
            all.add(blockContent);
            all.add("\r\n");
            all.addAll(a2);

            StringBuffer sb = new StringBuffer();
            all.stream().forEach(str -> {
                sb.append(str + "\r\n");
            });

            saveToFile(file, sb.toString(), "UTF8");
        }
    }

    public static void saveToFile(File file, String content, String encode) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), encode));
            writer.write(content);
            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void searchFilefind(File file, String pattern, List<File> fileList) {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory() && file.listFiles() != null) {
            for (File f : file.listFiles()) {
                searchFilefind(f, pattern, fileList);
            }
        } else {
            if (Pattern.compile(pattern).matcher(file.getName()).find()) {
                if (!fileList.contains(file)) {
                    fileList.add(file);
                }
            }
        }
    }

    private Class getGenericType(Field field) {
        ParameterizedType type = (ParameterizedType) field.getGenericType();
        Class<?> genericType = (Class<?>) type.getActualTypeArguments()[0];
        return genericType;
    }

    private void appendLostGetterSetterMethod(Field field, Class clz, boolean isGetter) {
        File javaFile = findClassFile(clz);

        Class genericType = null;
        if (Collection.class.isAssignableFrom(field.getType())) {
            genericType = getGenericType(field);
        }

        String blockContent = getMethodString(field.getName(), field.getType(), genericType, isGetter);

        javaFileEndAppender.apply(javaFile, blockContent);
    }

    private void findGetterSetter(Field field, Class clz) {
        String label = clz.getSimpleName();
        String fieldName = field.getName();
        Class fieldType = field.getType();

        boolean findGetter = Stream.of(clz.getDeclaredMethods()).filter(m -> m.getName().equals("get" + StringUtils.capitalize(fieldName))).findAny().isPresent();
        boolean findGetterIs = Stream.of(clz.getDeclaredMethods()).filter(m -> m.getName().equals("is" + StringUtils.capitalize(fieldName))).findAny().isPresent();
        boolean findSetter = Stream.of(clz.getDeclaredMethods()).filter(m -> m.getName().equals("set" + StringUtils.capitalize(fieldName))).findAny().isPresent();

        if (fieldType == boolean.class || fieldType == Boolean.class) {
            if (findGetterIs == false) {
                System.out.println(label + " - is" + StringUtils.capitalize(fieldName) + " \t not found!!");
                this.appendLostGetterSetterMethod(field, clz, true);
            }
        } else {
            if (findGetter == false) {
                System.out.println(label + " - get" + StringUtils.capitalize(fieldName) + " \t not found!!");
                this.appendLostGetterSetterMethod(field, clz, true);
            }
        }
        if (findSetter == false) {
            System.out.println(label + " - set" + StringUtils.capitalize(fieldName) + " \t not found!!");
            this.appendLostGetterSetterMethod(field, clz, false);
        }
    }

    private void checkGetterSetter(Class clz) {
        for (Field f : clz.getDeclaredFields()) {
            findGetterSetter(f, clz);
        }
    }

    public static Set<String> getSubClassPaths(Reflections reflections) {
        Set<String> lst = new LinkedHashSet<>();
        Set<Class<? extends Object>> respositories = reflections.getSubTypesOf(java.lang.Object.class);
        for (Object v : respositories) {
            // System.out.println("--" + v);
        }
        Store store = reflections.getStore();
        // System.out.println(FieldUtils.readDeclaredField(store, "storeMap",
        // true));
        Set<String> set = store.keySet();
        for (String v : set) {
            // System.out.println("--" + v);
            Multimap<String, String> map = store.get(v);
            // System.out.println("size = " + map.size());
            for (String k1 : map.keys()) {
                Collection<String> values = map.get(k1);
                // System.out.println(values);
                lst.addAll(values);
            }
        }
        return lst;
    }

}
