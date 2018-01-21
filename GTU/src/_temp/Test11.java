package _temp;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test11 {

    public static void main(String args[]) throws Exception {
        Test11 test = new Test11();
        try{
            Pattern pattern = Pattern.compile("TWD\\s([\\d\\.]+)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = null;
            File file = new File("C:/Users/gtu001/Desktop/xxxxxxx.txt");
            LineNumberReader reader = new LineNumberReader(new InputStreamReader(new FileInputStream(file), "utf8"));
            BigDecimal result = new BigDecimal(0);
            for(String line = null; (line = reader.readLine())!= null ; ){
                matcher = pattern.matcher(line);
                while(matcher.find()){
                    System.out.println(matcher.group());
                    result = result.add(new BigDecimal(matcher.group(1)));
                }
            }
            reader.close();
            
            System.out.println("====" + result);
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
        System.out.println("done...");
    }
}
