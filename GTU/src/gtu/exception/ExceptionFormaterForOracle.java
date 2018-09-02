package gtu.exception;

import gtu.console.SystemInUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExceptionFormaterForOracle {

    public static void main(String[] args) throws IOException {
        String str = SystemInUtil.readContent();
//        BufferedReader bReader = new BufferedReader(new FileReader("D:/swing_error_20161215.log"));
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new StringReader(str));
        for(String line = null ; (line = reader.readLine())!=null ;){
            sb.append(line);
        }
        reader.close();
        
        Pattern ptn = Pattern.compile("ORA\\-\\d+\\:.*?\\,\\sline\\s\\d+");
        Matcher mth = ptn.matcher(sb.toString());
        while(mth.find()){
            System.out.println(mth.group());
        }
    }

}
