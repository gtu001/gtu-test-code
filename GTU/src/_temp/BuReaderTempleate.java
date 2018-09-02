package _temp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuReaderTempleate {

    public static void main(String[] args) {
        try{
            Pattern pattern = Pattern.compile("xxxxxxxxxxxx", Pattern.CASE_INSENSITIVE);
            Matcher matcher = null;
            File file = new File("xxxxxx");
            LineNumberReader reader = new LineNumberReader(new InputStreamReader(new FileInputStream(file), "utf8"));
            for(String line = null; (line = reader.readLine())!= null ; ){
                matcher = pattern.matcher(line);
                while(matcher.find()){
                    System.out.println(matcher.group());
                }
            }
            reader.close();
            
            File outputFile = new File("xxxxxx");
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "utf8"));
            writer.flush();
            writer.close();
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
