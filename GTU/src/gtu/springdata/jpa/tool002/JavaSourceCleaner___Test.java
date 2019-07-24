package gtu.springdata.jpa.tool002;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gtu.file.FileUtil;
import gtu.regex.tag.TagMatcher;

public class JavaSourceCleaner___Test {

    public static void main(String[] args) {
        JavaSourceCleaner___Test t = new JavaSourceCleaner___Test();
        // File baseDir = new File("D:/workstuff/workspace_taida/Taida_Model");
        File baseDir = new File("D:/workstuff/workspace_taida/isa95-model/src/main/java/com/delta/mes/model/isa95/resources/equipment");
        File javaFile = new File("D:/workstuff/workspace_taida/isa95-model/src/main/java/com/delta/mes/model/isa95/operations/capability/PersonnelCapability.java");

        List<File> fileLst = new ArrayList<File>();
        FileUtil.searchFileMatchs(baseDir, ".*\\.java", fileLst);
//        fileLst.add(javaFile);

        fileLst.stream().forEach(t::doCleanserFile__for__removeManyToMany);

        System.out.println("done...");
    }

    private void doCleanserFile__for__repository(File file) {
        String content1 = FileUtil.loadFromFile(file, "UTF8");

        StringBuffer sb = new StringBuffer();

        boolean findOk = false;
        Pattern ptn = Pattern.compile("\\/\\/\\s+relation\\s+↓+(?:(\r|\n|.)*?)\\/\\/\\s+relation\\s+↑+", Pattern.MULTILINE | Pattern.DOTALL);
        Matcher mth = ptn.matcher(content1);
        while (mth.find()) {
            mth.appendReplacement(sb, "");
            findOk = true;
        }
        mth.appendTail(sb);

        if (findOk) {
            System.out.println("fix : " + file);
        }

        FileUtil.saveToFile(file, sb.toString(), "UTF8");
    }

    private void doCleanserFile__for__entity(File file) {
        String content1 = FileUtil.loadFromFile(file, "UTF8");

        TagMatcher tagMatcher = new TagMatcher("// relation ↓", "// relation ↑", "\\/\\/\\s+relation\\s+↓+", "\\/\\/\\s+relation\\s+↑+", content1);

        boolean findOk = false;

        while (tagMatcher.findUnique()) {
            String tagContent = tagMatcher.group().groupWithTag();

            if (tagContent.contains("@Transient // switch(off)")) {
                tagContent = "";
                findOk = true;
            } else {
                break;
            }

            tagMatcher.appendReplacementForUnique(tagContent, findOk, true, false);
        }

        if (findOk) {
            System.out.println("fix : " + file);
            FileUtil.saveToFile(file, tagMatcher.getContent(), "UTF8");
        }
    }

    // @Transient
    // @DynamicDBRelation(//
    // setter = "setEquipmentActualProperties", //
    // repository = "EquipmentActualPropertyRepository", //
    // method = "findRelation4EquipmentActualProperty")
    // private String equipmentActualsId;

    private void doCleanserFile__for__DynamicAnnotation(File file) {
        String content1 = FileUtil.loadFromFile(file, "UTF8");

        String orignContent = content1.toString();

        TagMatcher tagMatcher = new TagMatcher("@Transient", ";", "\\@Transient(?:\r|\n|\\s|\t)*?\\@DynamicDBRelation", "\\w+\\;", content1);

        while (tagMatcher.findUnique()) {
            String tagContent = tagMatcher.group().groupWithTag();
            tagMatcher.appendReplacementForUnique("", false, true, false);
        }

        if (orignContent.equals(tagMatcher.getContent())) {
            return;
        }

        System.out.println(tagMatcher.getContent());
        System.out.println("fix : " + file);
        FileUtil.saveToFile(file, tagMatcher.getContent(), "UTF8");
    }
    
    private void doCleanserFile__for__removeManyToMany(File file) {
        String content1 = FileUtil.loadFromFile(file, "UTF8");

        TagMatcher tagMatcher = new TagMatcher("// relation ↓", "// relation ↑", "\\/\\/\\s+relation\\s+↓+", "\\/\\/\\s+relation\\s+↑+", content1);

        boolean findOk = false;

        while (tagMatcher.findUnique()) {
            String tagContent = tagMatcher.group().groupWithTag();

            if (tagContent.contains("@ManyToMany")) {
                tagContent = "";
                findOk = true;
            } else {
                break;
            }

            tagMatcher.appendReplacementForUnique(tagContent, findOk, true, false);
        }

        if (findOk) {
            System.out.println("fix : " + file);
            FileUtil.saveToFile(file, tagMatcher.getContent(), "UTF8");
        }
    }
    
}
