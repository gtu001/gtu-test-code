package gtu._work;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class WorkTimesheetProduce_WeekReport {

    public static void main(String[] args) throws IOException {
        WorkTimesheetProduce_WeekReport test = new WorkTimesheetProduce_WeekReport();
        test.execute();
        System.out.println("done...");
    }
    
    public void execute(){
        try{
            File file = new File("C:/Users/gtu001/Desktop/關貿/週報/週報暫存.txt");
            LineNumberReader reader = new LineNumberReader(new InputStreamReader(new FileInputStream(file), "utf8"));
            
            String tmpDate = "";
            Set<String> set = new HashSet<String>();
            
            List<String> list = new ArrayList<String>();
            
            String month = "3";//月份 TODO
            
            List<String> stlist = new ArrayList<String>();
            
            int indexNum = 1;
            for (String line = null; (line = reader.readLine()) != null;) {
                String line2 = StringUtils.trim(StringUtils.defaultString(line));
                line2 = line2.replaceAll("Modified \\(content only\\)", "");
                line2 = line2.trim();
                int pos = line2.lastIndexOf("/");
                
                if(StringUtils.defaultString(line).startsWith(month + "/")){
                    tmpDate = StringUtils.defaultString(line).trim();
                    
                    if(!stlist.isEmpty()){
                        String sys = stlist.toString().replaceAll("^\\[|\\]$", "");
                        System.out.println(sys);
                        stlist = new ArrayList<String>();
                    }
                    
//                    System.out.println("日期" + tmpDate);
                    System.out.println(tmpDate);
                }
                
                if(pos != -1){
                    String fileName = line2.substring(pos + 1);
                    if(fileName.indexOf(".")!=-1){
//                        System.out.println(fileName);
                        stlist.add(fileName);
                    }
                    
                    if(!set.contains(fileName) && fileName.indexOf(".")!=-1){
                        set.add(fileName);
                        list.add(String.format("%s\t%s\t%s\t%s\t%s\t%s\t", indexNum, fileName, "修改", tmpDate, tmpDate, "100%"));
                        indexNum ++;
                    }
                }else{
//                    System.out.println(line);
                    stlist.add(line);
                }
            }
            
            if(!stlist.isEmpty()){
                String sys = stlist.toString().replaceAll("^\\[|\\]$", "");
                System.out.println(sys);
                stlist = new ArrayList<String>();
            }
            
            reader.close();
            System.out.println("==================================");
            System.out.println("==================================");
            System.out.println("==================================");
            System.out.println("==================================");
            for(String s : list){
                System.out.println(s);
            }
            System.out.println("==================================");
            System.out.println("==================================");
            System.out.println("==================================");
            System.out.println("==================================");
            for(String s : set){
                String xxx = s.substring(0, s.lastIndexOf("."));
                System.out.println("[修改]" + xxx);
            }
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
}