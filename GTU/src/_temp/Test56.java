package _temp;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gtu.file.FileUtil;

public class Test56 {

    public static void main(String[] args) {
        File file = new File("D:\\work_tool\\20200114_sister_workspace\\cashweb\\src\\conf\\com\\wistron\\cashweb\\spring\\applicationContext-ibatis.xml");
        String content = FileUtil.loadFromFile(file, "UTF8");

        Pattern ptn = Pattern.compile("[\\x80-\\xFF]", Pattern.DOTALL | Pattern.MULTILINE);
        Matcher mth = ptn.matcher(content);

        int findCount= 0;
        StringBuffer sb = new StringBuffer();
        while (mth.find()) {
            mth.appendReplacement(sb, "^^^" + mth.group() + "^^^");
            findCount ++;
        }
        mth.appendTail(sb);
        System.out.println("FindCount = " + findCount);
        System.out.println(sb);
    }
}