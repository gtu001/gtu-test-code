package _temp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.time.DateFormatUtils;

import gtu.file.FileUtil;




public class Test {
    

    public static void main(String[] a) throws Exception {
        
        System.out.println(List.class.isAssignableFrom(ArrayList.class));
        
        StringBuffer sb = new StringBuffer();
        File dirFile = new File("D:\\my_tool\\english");
        for(File f : dirFile.listFiles()){
            if(f.getName().startsWith("xxxx") && f.getName().endsWith(".properties")){
                String content = FileUtil.loadFromFile(f, "utf8");
                sb.append(content + "\n\n\n\n");
            }
        }
        String fileName = String.format("new_word_%s.properties", DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss"));
        File destFile = new File(dirFile, fileName);
        FileUtil.saveToFile(destFile, sb.toString(), "utf8");
    
        System.out.println(destFile);
        System.out.println("done...");
    }
}