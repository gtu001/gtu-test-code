package gtu.springdata;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.reflections.Reflections;
import org.reflections.Store;

import com.google.common.collect.Multimap;

public class EntityBeautifierMainTest {

    public static void main(String[] args) {
        EntityBeautifierMainTest t = new EntityBeautifierMainTest();

        File dir = new File("D:/workstuff/workspace_taida/isa95-model/src/main/java");

        List<File> lst = new ArrayList<>();
        t.searchFilefind(dir, ".*\\.java", lst);

        Reflections ref = new Reflections("com.delta.mes.model.isa95");

        Set<Class<?>> allEntities = t.getSubClassPaths(ref).stream().map(path -> {
            try {
                return Class.forName(path);
            } catch (Exception ex) {
                throw new RuntimeException("getSubClassPaths ERR : " + ex.getMessage() + " --> " + path);
            }
        }).collect(Collectors.toCollection(LinkedHashSet::new));

        lst.stream().forEach(f -> {
            String name = StringUtils.substring(f.getName(), 0, -5);
            Class clz = allEntities.stream().filter(c -> StringUtils.equals(c.getSimpleName(), name)).findAny().orElse(null);

            if (clz == null) {
                System.out.println("----------------" + name);
            } else {
                t.entityAppendAnnotation(f);
            }
        });
    }

    private void entityAppendAnnotation(File javaFile) {
        String content = loadFromFile(javaFile, "UTF8");
        try {
            LineNumberReader reader = new LineNumberReader(new StringReader(content));
            String line = null;

            LinkedList<String> insertLst = new LinkedList<String>();
            while ((line = reader.readLine()) != null) {
                String className = isClassLine(line);
                String propertyName = isPropertyLine(line);
                if (className != null) {
                    String dbName = javaToDbField(className);
                    dbName = toTableAnnotation(dbName);
                    insertLst.add(dbName);
                } else if (propertyName != null) {
                    String ppName = javaToDbField(propertyName);
                    ppName = toPropertyAnnotation(ppName);
                    insertLst.add(ppName);
                }
                insertLst.add(line);
            }
            reader.close();

            String rtnContent = StringUtils.join(insertLst, "\r\n");
            // System.out.println(rtnContent);

            saveToFile(javaFile, rtnContent, "UTF8");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String toTableAnnotation(String tableName) {
        return String.format("@Table(name = \"%s\")", tableName);
    }

    public String toPropertyAnnotation(String propertyName) {
        return String.format("@Column(name = \"%s\")", propertyName);
    }

    public String javaToDbField(String columnName) {
        char[] coln = columnName.trim().toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int ii = 0; ii < coln.length; ii++) {
            if (ii != 0 && Character.isUpperCase(coln[ii])) {
                sb.append("_" + Character.toLowerCase(coln[ii]));
            } else {
                sb.append(Character.toLowerCase(coln[ii]));
            }
        }
        return sb.toString();
    }

    private String isClassLine(String line) {
        Pattern ptn = Pattern.compile("public\\sclass\\s(\\w+)");
        Matcher mth = ptn.matcher(line);
        if (mth.find()) {
            return mth.group(1);
        }
        return null;
    }

    private String isPropertyLine(String line) {
        Pattern ptn = Pattern.compile("(?:private|protected)\\s(?:String|Long)\\s(\\w+?)\\;");
        Matcher mth = ptn.matcher(line);
        if (mth.find()) {
            return mth.group(1);
        }
        return null;
    }

    public String loadFromFile(File file, String encode) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encode));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\r\n");
            }
            reader.close();
            return sb.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void saveToFile(File file, String content, String encode) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), encode));
            writer.write(content);
            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void searchFilefind(File file, String pattern, List<File> fileList) {
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

    public Set<String> getSubClassPaths(Reflections reflections) {
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
