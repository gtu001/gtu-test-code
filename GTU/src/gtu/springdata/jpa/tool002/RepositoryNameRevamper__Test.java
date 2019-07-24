package gtu.springdata.jpa.tool002;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gtu.file.FileUtil;
import gtu.regex.util.RegexMatchGroupReplacer;

public class RepositoryNameRevamper__Test {

    public static void main(String[] args) {
        RepositoryNameRevamper__Test t = new RepositoryNameRevamper__Test();

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

            // if (content.contains("@EntityScan") || content.indexOf("@Entity")
            // == -1) {
            // return;
            // } else {
            // System.out.println("findok -- " + content.indexOf("@Entity"));
            // }

            Map<Integer, String> map = new HashMap<Integer, String>();
            map.put(1, "String");

            List debugLst = new ArrayList<>();

            // extends CrudRepository<PhysicalAssetProperty, String>
            content = RegexMatchGroupReplacer.fixByPattern(content, "extends\\s+CrudRepository\\<.*?\\,\\s+(\\w+)\\>", map, debugLst, null);

            if (debugLst.isEmpty()) {
                return;
            }

            System.out.println("<<<<<" + debugLst);

            System.out.println(content);

            FileUtil.saveToFile(f, content, "UTF8");
        });
    }
}
