package gtu.springdata;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import gtu.file.FileUtil;
import gtu.regex.util.RegexMatchGroupReplacer;

public class EntityFieldNameRevamper__Test {

    public static void main(String[] args) {
        EntityFieldNameRevamper__Test t = new EntityFieldNameRevamper__Test();

        t.execute(new File("D:/workstuff/workspace_taida/"));

        System.out.println("done...");
    }

    public void execute(File baseDir) {
        List<File> fileLst = new ArrayList<>();
        FileUtil.searchFilefind(baseDir, ".*\\.java$", fileLst);

        // fileLst.add(new
        // File("D:/workstuff/workspace_taida/isa95-model/src/main/java/com/delta/mes/model/isa95/operations/capability/EquipmentCapability.java"));

        fileLst.stream().forEach(f -> {
            System.out.println("file : " + f);
            String content = FileUtil.loadFromFile(f, "UTF8");

            if (content.contains("@EntityScan") || content.indexOf("@Entity") == -1) {
                return;
            } else {
                System.out.println("findok -- " + content.indexOf("@Entity"));
            }

            Map<Integer, String> map = new HashMap<Integer, String>();
            map.put(1, "");
            map.put(2, "String");

            // Map<Integer, String> rtnMap = new HashMap<Integer, String>();
            List debugLst = new ArrayList<>();

            content = RegexMatchGroupReplacer.fixByPattern(content, "\\@Id[\\s\\t\r\n]*?(@GeneratedValue)[\\s\\t\r\n]*?\\@Column\\(.*?\\)[\\s\\t\r\n]*?private\\s+(Long)\\s+(\\w+)?\\;", map, debugLst,
                    null);

            String fieldName = ((Map<Integer, String>) debugLst.get(0)).get(3);

            // ------------------------------------------------------------
            Map<Integer, String> map1 = new HashMap<Integer, String>();
            map1.put(1, "String");

            content = RegexMatchGroupReplacer.fixByPattern(content, "public\\s+(\\w+)\\s+get" + fieldName + "\\(", map1, null, Pattern.CASE_INSENSITIVE);

            // ------------------------------------------------------------
            Map<Integer, String> map2 = new HashMap<Integer, String>();
            map2.put(1, "String");

            content = RegexMatchGroupReplacer.fixByPattern(content, "public\\s+void\\s+set" + fieldName + "\\(\\s*(\\w+)\\s+", map2, null, Pattern.CASE_INSENSITIVE);

            System.out.println(content);

            FileUtil.saveToFile(f, content, "UTF8");
        });
    }
}
