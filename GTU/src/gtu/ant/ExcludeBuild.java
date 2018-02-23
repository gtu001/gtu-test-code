package gtu.ant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import gtu.console.SystemInUtil;

/**
 * 讀取config將符合錯誤格式的java掃出來 設定為exclude
 * 
 * @author Troy 2012/1/24
 */
public class ExcludeBuild {

    public static void main(String[] args) throws Exception {
        String logContent = SystemInUtil.readContent();
        new ExcludeBuild()//
                .setLog(logContent)//
                .execute();//
    }

    private String[] needAdd;
    private String logContent;

    private ExcludeBuild() throws Exception {
        try {
            List<String> list = IOUtils.readLines(getClass().getResourceAsStream("needadd.properties"));
            needAdd = new String[list.size()];
            needAdd = list.toArray(needAdd);
            // ListUtil.showListInfo(list);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("檔案不存在:needadd.properties");
        }
    }

    public ExcludeBuild setLog(String logContent) {
        this.logContent = logContent;
        return this;
    }

    public void execute() throws IOException {
        Validate.notEmpty(logContent);

        BufferedReader reader = new BufferedReader(new StringReader(logContent));
        String line = null;

        // String test =
        // "[javac] C:\\workspace\\GTU\\src\\gtu\\array\\ArrayTest.java:48: error: '.class' expected";

        Pattern pattern = Pattern.compile("(\\[javac]\\s.*\\\\)(\\w+\\.java)(:\\d+:\\serror)");
        Matcher matcher = null;

        Pattern pattern1 = Pattern.compile("(.*error:\\s)(.+)");
        Matcher matcher1 = null;

        Set<String> javaSet = new TreeSet<String>();
        Set<String> errorSet = new TreeSet<String>();

        while ((line = reader.readLine()) != null) {
            matcher = pattern.matcher(line);
            matcher1 = pattern1.matcher(line);
            if (matcher.find() && StringUtils.indexOfAny(line, needAdd) != -1) {
                javaSet.add(matcher.group(2));
            }
            if (matcher1.find() && !ArrayUtils.contains(needAdd, matcher1.group(2))) {
                errorSet.add(matcher1.group(2));
            }
        }

        System.out.println("!!有錯不compiler的檔案!!");
        for (String str : javaSet) {
            System.out.println(String.format("<exclude name=\"**/%s\"/>", str));
        }

        System.out.println("!!請加到needadd.properties!!");
        for (String str : errorSet) {
            System.out.println(str);
        }
    }
}
