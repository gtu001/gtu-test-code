package _temp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gtu.file.FileUtil;

public class Test35 {

    public static void main(String[] args) throws Exception {
//        System.out.println(System.getProperty("java.library.path"));
//        
        List<String> lst = new ArrayList<String>();
        
        Pattern pattern = Pattern.compile("轉個人帳單.*ID\\=(\\w+)");
        Matcher matcher = null;
        File file = new File("E:/workstuff/workstuff/workspace_scsb/SCSB/SCSB/Logs/DC_2018-01-29 16-42-01.log");
        LineNumberReader reader = new LineNumberReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
        for(String line = null; (line = reader.readLine())!= null ; ){
            matcher = pattern.matcher(line);
            while(matcher.find()){
                System.out.println(matcher.group());
                lst.add(matcher.group(1));
            }
        }
        reader.close();
        
        File outputFile = new File(FileUtil.DESKTOP_PATH, "samplingList.txt");
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "utf8"));
        for(String v : lst) {
            writer.write(v);
            writer.newLine();
        }
        writer.flush();
        writer.close();
        System.out.println("done...");
    }
}
