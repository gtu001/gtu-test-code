package _temp;

import java.io.LineNumberReader;
import java.io.StringReader;

import com.google.common.collect.HashMultiset;

import gtu.console.SystemInUtil;

public class Test28 {

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        try {
            HashMultiset<String> hashset = HashMultiset.<String> create();
            
            String text = SystemInUtil.readContent();
            
            LineNumberReader reader = new LineNumberReader(new StringReader(text));
            for (String line = null; (line = reader.readLine()) != null;) {
                if(!line.matches("^\\d+$")){
                    System.out.println(line);
                }
            }
            reader.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
