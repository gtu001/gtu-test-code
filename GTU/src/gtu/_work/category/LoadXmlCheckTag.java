/*
 * Copyright (c) 2010-2020 IISI. All rights reserved.
 * 
 * This software is the confidential and proprietary information of IISI.
 */
package gtu._work.category;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * 掃目錄底下所有jsp檔有用到哪些tag
 * 
 * @author Troy
 */
public class LoadXmlCheckTag {

    /** 讀取目錄 */
    private String dirPath = "C:/workspace_spy_iisi/ris3db_simple/src/gtu/db/iisi/hibernate/hbm";

    /** 匯出目錄 */
    private static final String EXPORT_FILE_PATH = System.getProperty("user.home") + "\\Desktop\\Edit1.txt";

    /** 副檔名 */
    private String subFileName = ".xml";

    private static final String[] IGNORE_TAGS = new String[] {};
    //    private static final String[] IGNORE_TAGS = new String[] { "p:inputText", "h:outputText" };

    private Map<Tag, Set<File>> tagMap;
    private boolean writeLogFile = true;

    private LoadXmlCheckTag() {
    }

    public Map<Tag, Set<File>> getTagMap() {
        return tagMap;
    }

    public static LoadXmlCheckTag newInstance() {
        return new LoadXmlCheckTag();
    }

    public LoadXmlCheckTag dirPath(String dirPath) {
        this.dirPath = dirPath;
        return this;
    }

    public LoadXmlCheckTag subFileName(String subFileName) {
        this.subFileName = subFileName;
        return this;
    }

    public LoadXmlCheckTag writeLogFile(boolean writeLogFile) {
        this.writeLogFile = writeLogFile;
        return this;
    }

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        new LoadXmlCheckTag().execute();
        System.out.println("done...");
    }

    public void execute() throws IOException {
        tagMap = new HashMap<Tag, Set<File>>();

        loadJspFiles(new File(dirPath));

        Set<Tag> keys = tagMap.keySet();

        List<Tag> keyList = Arrays.<Tag> asList((Tag[]) keys.toArray(new Tag[keys.size()]));
        Collections.sort(keyList);

        if (writeLogFile) {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(EXPORT_FILE_PATH), "BIG5"));
            for (Object key : keyList) {
                writer.write("[" + key + "]");
                writer.newLine();

                for (File file : tagMap.get(key)) {
                    writer.write("\t\t" + file.getName());
                    writer.newLine();
                }
            }
            writer.close();
        }
    }

    private void loadJspFiles(File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                loadJspFiles(f);
            }
        } else {
            try {
                if (file.getName().endsWith(subFileName)) {
                    readJspTags(file);
                } else {
                    System.out.println("跳過檔案 : " + file.getName());
                }
            } catch (FileNotFoundException e) {
                System.err.println("找無檔案 : " + file.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("讀檔錯誤 : " + file.getAbsolutePath());
            }
        }
    }

    final Pattern startTagPattern = Pattern.compile("^<(\\w+)\\s.*$");
    final Pattern endTagPattern = Pattern.compile(">");
    final Pattern attributePattern = Pattern.compile("\\s?(\\w+)=\"[\\w#${}\\.]+\"");

    private void readJspTags(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        for (String line = null; (line = reader.readLine()) != null;) {
            sb.append(line);
        }
        reader.close();

        String content = sb.toString().replace('\n', ' ').replace('\r', ' ');

        String tempContent = content.toString();
        Set<String> tagContentList = new HashSet<String>();
        for (int startPos = -1; (startPos = tempContent.indexOf("<")) != -1;) {
            if (tempContent.substring(startPos).startsWith("<!--")) {
                tempContent = tempContent.substring(tempContent.indexOf("-->") + 3);
                continue;
            }
            if (tempContent.substring(startPos).startsWith("<script")) {
                tempContent = tempContent.substring(tempContent.indexOf("script>") + 7);
                continue;
            }

            int endPos = -1;
            Matcher endMatcher = endTagPattern.matcher(tempContent);
            while (endMatcher.find()) {
                if (endMatcher.start() > startPos) {
                    endPos = endMatcher.start();
                    break;
                }
            }

            try {
                String currentTag = tempContent.substring(startPos, endPos + 1);
                //                System.out.println("^^^^^^" + currentTag + "$$$$$$");
                tagContentList.add(currentTag);
            } catch (RuntimeException ex) {
                System.err.format("error content ====== [%s]\n", tempContent);
                System.err.format("error startPos ====== [%s]\n", startPos);
                System.err.format("error endPos ====== [%d]\n", endPos);
                System.err.format("error file ====== [%s]\n", file);
                throw ex;
            }

            tempContent = tempContent.substring(endPos + 1);
        }

        Matcher startTagMatcher = null;
        Matcher attributeMatcher = null;

        List<Tag> fileTagList = new ArrayList<Tag>();

        for (String tagContent : tagContentList) {
            startTagMatcher = startTagPattern.matcher(tagContent);
            Tag t = new Tag();

            t.tagContent = tagContent;
            t.file = file;

            if (startTagMatcher.find()) {
                t.name = startTagMatcher.group(1);
            } else {
                continue;
            }

            if (StringUtils.indexOfAny(t.name, IGNORE_TAGS) != -1) {
                continue;
            }

            attributeMatcher = attributePattern.matcher(tagContent);
            while (attributeMatcher.find()) {
                t.attrs.add(attributeMatcher.group(1));
            }

            fileTagList.add(t);
        }

        for (Tag t : fileTagList) {
            this.mapPut(t, file, tagMap);
        }
    }

    private void mapPut(Tag key, File value, Map<Tag, Set<File>> map) {
        Set<File> set = new HashSet<File>();
        if (map.containsKey(key)) {
            set = map.get(key);
        }
        set.add(value);
        map.put(key, set);
    }

    static class TagStartEnd implements Comparable<TagStartEnd> {
        boolean tagStart;
        int pos;

        public TagStartEnd(boolean tagStart, int pos) {
            super();
            this.tagStart = tagStart;
            this.pos = pos;
        }

        @Override
        public int compareTo(TagStartEnd arg0) {
            if (this.pos < arg0.pos) {
                return -1;
            } else if (this.pos > arg0.pos) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public static class Tag implements Comparable<Tag> {
        File file;
        String tagContent;
        String name;
        Set<String> attrs = new HashSet<String>();

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((attrs == null) ? 0 : attrs.hashCode());
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
            if (attrs == null) {
                if (other.attrs != null)
                    return false;
            } else if (!attrs.equals(other.attrs))
                return false;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            return true;
        }

        //        @Override
        //        public String toString() {
        //            return "Tag [name=" + name + ", attrs=" + attrs + ", tagContent=" + tagContent + "]";
        //        }

        @Override
        public String toString() {
            return "[" + name + ", attrs=" + attrs + "]";
        }

        @Override
        public int compareTo(Tag arg0) {
            return this.name.compareTo(arg0.name);
        }
    }
}
