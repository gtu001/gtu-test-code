package gtu.freemarker.work;

import gtu.file.FileUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IfReplacer {

    static File file = new File("C:/Users/gtu001/Desktop/test");

    static String encode = "UTF8";

    public static void main(String[] args) throws IOException {
        IfReplacer rrr = new IfReplacer();
        String content = null;
        for (File f : file.listFiles()) {
            System.out.println("## current file : " + f.getName());
            content = FileUtil.loadFromFile(f, encode);
            rrr.scanner(content, f);
        }
        System.out.println("done...");
    }

    Pattern ifPattern = Pattern.compile("<#if\\s(.*)>");
    //    Pattern conditionPattern = Pattern.compile("\\$\\{\\(([\\w\\.]+)\\)\\!\"?d?\\.?undefined\"?\\}");
    Pattern conditionPattern = Pattern.compile("\\$\\{(dto\\.[\\w\\.]+)\\}");

    //    Pattern conditionPattern = Pattern.compile("(dto\\.[\\w\\.]+)");

    void scanner(String content, File targetFile) throws IOException {
        Matcher matcher = null;
        Matcher matcher1 = null;
        StringWriter strWriter = new StringWriter();
        BufferedReader reader = new BufferedReader(new StringReader(content));
        BufferedWriter writer = new BufferedWriter(strWriter);
        Replace replace = null;
        Set<Replace> repset = new HashSet<Replace>();
        String temp = null;

        boolean needModify = false;
        for (String line = null; (line = reader.readLine()) != null;) {
            matcher = ifPattern.matcher(line);
            if (matcher.find()) {
                writer.write("<#if ");
                matcher1 = conditionPattern.matcher(matcher.group(1));
                while (matcher1.find()) {
                    System.out.println("find => " + matcher1.group(1));
                    replace = new Replace();
                    replace.orign = matcher1.group(0);
                    replace.dto = matcher1.group(1);
                    replace.param = replace.dto.substring(replace.dto.lastIndexOf(".") + 1);
                    replace.assign = String.format("<#assign %s = (%s)!d.undefined>", replace.param, replace.dto);
                    repset.add(replace);
                    needModify = true;
                }
                temp = matcher.group(1);
                for (Replace rep : repset) {
                    temp = temp.replaceAll(Pattern.quote(rep.orign), rep.param);
                }
                writer.write(temp);
                writer.write(">");
                writer.newLine();
            } else {
                writer.write(line);
                writer.newLine();
            }
        }

        writer.newLine();
        writer.newLine();
        for (Replace rep : repset) {
            writer.write(rep.assign);
            writer.newLine();
            // System.out.println(rep);
        }
        reader.close();
        writer.flush();
        writer.close();

        if (needModify) {
            // System.out.println(strWriter.getBuffer());
            FileUtil.saveToFile(targetFile, strWriter.getBuffer().toString(), encode);
            System.out.println("save file : " + targetFile.getName());
        }
    }

    static class Replace {
        String assign;
        String dto;
        String param;
        String orign;

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((assign == null) ? 0 : assign.hashCode());
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
            Replace other = (Replace) obj;
            if (assign == null) {
                if (other.assign != null)
                    return false;
            } else if (!assign.equals(other.assign))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "Replace [assign=" + assign + ", dto=" + dto + ", param=" + param + ", orign=" + orign + "]";
        }
    }
}
