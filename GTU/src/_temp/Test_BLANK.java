package _temp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.HashMultiset;




public class Test_BLANK {

    public static void main(String[] args) throws IOException {
        Test_BLANK test = new Test_BLANK();
        System.out.println("done...");
    }
    
    private void readFileAndWriteFile(){
        try{
            HashMultiset<String> hashset = HashMultiset.<String>create();
            Pattern pattern = Pattern.compile("Troy\\sChang");
            Matcher matcher = null;
            File file = new File("C:/Users/gtu001/Desktop/issue.txt");
            LineNumberReader reader = new LineNumberReader(new InputStreamReader(new FileInputStream(file), "big5"));
            for(String line = null; (line = reader.readLine())!= null ; ){
                matcher = pattern.matcher(line);
                while(matcher.find()){
//                    System.out.println(matcher.group());
//                    hashset.add(matcher.group(1));
                    System.out.println(line);
                }
            }
            reader.close();
            
            for(String key : hashset.elementSet()){
                System.out.println(key + "\t" + hashset.count(key));
            }
            
            File outputFile = new File("xxxxxx");
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "utf8"));
            writer.flush();
            writer.close();
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
    
    private void readFileTestWithPattern() throws IOException{
        File file = new File("C:\\Users\\gtu001\\Desktop\\vvvvv.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        StringBuilder sb = new StringBuilder();
        for(String line = null;(line = reader.readLine())!= null;){
            sb.append(line + '\n');
        }
        reader.close();
        
        Pattern ptn = Pattern.compile("Troy\\sChang", Pattern.MULTILINE);
        Matcher mth = ptn.matcher(sb.toString());
        while(mth.find()){
            System.out.println(mth.group());
        }
        System.out.println("done...");
    }
    
    private void readFileTest() throws IOException{
        File file = new File("C:\\Users\\gtu001\\Desktop\\vvvvv.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        for(String line = null;(line = reader.readLine())!= null;){
        }
        reader.close();
    }
}