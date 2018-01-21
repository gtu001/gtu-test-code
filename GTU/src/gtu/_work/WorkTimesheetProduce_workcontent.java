package gtu._work;

import gtu.number.RandomUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.HashMultiset;

/**
 * 工時表 timesheet 產生器
 * 
 * @author gtu001
 */
public class WorkTimesheetProduce_workcontent {

    public static void main(String[] args) throws ParseException {
        WorkTimesheetProduce_workcontent test = new WorkTimesheetProduce_workcontent();
        test.execute();
        System.out.println("done...");
    }
    
    public void execute(){
        try{
            HashMultiset<String> hashset = HashMultiset.<String>create();
            Pattern pattern = Pattern.compile("(\\d+\\/\\d+)(.*)");//  1/18修改進口報表
            Matcher matcher = null;
            File file = new File("C:/Users/gtu001/Desktop/關貿/週報/1111111.txt");
            LineNumberReader reader = new LineNumberReader(new InputStreamReader(new FileInputStream(file), "utf8"));
            for(String line = null; (line = reader.readLine())!= null ; ){
                matcher = pattern.matcher(line);
                if(matcher.matches()){
                    printResult(matcher.group(1), matcher.group(2));
                }else{
                    System.out.println("---" + line);
                }
            }
            reader.close();
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
    
    private void printResult(String date, String content){
        String t1 = RandomUtil.random("林靜怡", "陳英宏");
        String t2 = RandomUtil.random("SPIS", "BKIS");
        String t3 = "SPIS".equals(t2) ? "(SPIS_AP)Spis bp" : "(BKIS_AP)Bkis bp";
        String t4 = "口頭述說";
        String t5 = content;
        String t6 = "2015/" + date;
        String t7 = "2015/" + date;
        String t8 = "2015/" + date;
        String t9 = "";
        String t10 = "2015/" + date;
        String t11 = "troy";
        String t12 = "100%";
        String t13 = RandomUtil.random("3", "4", "6", "8", "12", "24");
        String t14 = t13;
        List<String> values = Arrays.asList(t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12,t13,t14);
        StringBuilder sb = new StringBuilder();
        for(int ii = 0 ; ii < values.size() ; ii ++){
            sb.append("%s\t");
        }
        System.out.println(String.format(sb.toString(), values.toArray()));
    }
}
