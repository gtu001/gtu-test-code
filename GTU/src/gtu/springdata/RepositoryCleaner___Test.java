package gtu.springdata;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gtu.file.FileUtil;
import gtu.regex.tag.TagMatcher;

public class RepositoryCleaner___Test {

    public static void main(String[] args) {
        RepositoryCleaner___Test t = new RepositoryCleaner___Test();
        // File baseDir = new File("D:/workstuff/workspace_taida/Taida_Model");
        File baseDir = new File("D:/workstuff/workspace_taida/isa95-model");

        List<File> fileLst = new ArrayList<File>();
        FileUtil.searchFileMatchs(baseDir, ".*\\.java", fileLst);

        fileLst.stream().forEach(t::doCleanserFile__for__entity);

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

        TagMatcher tagMatcher = new TagMatcher("// relation ↓", "// relation ↑", "\\/\\/\\s+relation\\s+↓+", "\\/\\/\\s+relation\\s+↑+", content1, true);

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
}
