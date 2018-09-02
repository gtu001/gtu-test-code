package gtu.enum_.work;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

public class ReadTxt2Enum_002 {

    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        File file = new File("C:/Users/gtu001/Desktop/src1.txt");
        autoGenerateEnum("ITE2UPS", 1, file, "utf8");
        System.out.println("done...");
    }
    
    private static void autoGenerateEnum(String enumName, int indexOfEnumkey, File file, String encode) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encode));
        List<String[]> messageList = new ArrayList<String[]>();
        
        int maxSize = -1;
        for(String line = null; (line = reader.readLine())!=null;){
            String[] vals = line.split("\t", -1);
            messageList.add(vals);
            maxSize = Math.max(vals.length, maxSize);
        }
        reader.close();
        for(String[] vals : messageList){
            for(int ii = 0, len = (maxSize - vals.length); ii < len; ii ++){
                vals = ArrayUtils.add(vals, "");
            }
        }
        
        Map<String, String[]> valsMap = new LinkedHashMap<String,String[]>();
        for(String[] vals : messageList){
            String enumKey = vals[indexOfEnumkey];
            if(valsMap.containsKey(enumKey)){
                throw new RuntimeException("重複enumKey :" + enumKey);
            }
            for(int ii = 0; ii < vals.length; ii ++){
                vals[ii] = vals[ii].replaceAll("\"", "");
                vals[ii] = vals[ii].replace('\\', ' ');
                vals[ii] = "\"" + StringUtils.trimToEmpty(vals[ii]) + "\"";
            }
            valsMap.put(enumKey, vals);
        }
        
        System.out.println("\n\n");
        System.out.format("private enum %s {%n", enumName);
        System.out.println(";");
        List<String> parameterList = new ArrayList<String>();
        for(int ii = 0 ; ii < maxSize ; ii ++){
            System.out.println("final String desc" + ii + ";");
            parameterList.add("String desc" + ii);
        }
        String parameterMessage = parameterList.toString().replaceAll("^\\[|\\]$", "");
        System.out.println(enumName + "(" + parameterMessage + "){");
        for(int ii = 0 ; ii < maxSize ; ii ++){
            System.out.println("this.desc" + ii + " = desc" + ii + ";");
        }
        System.out.println("}");
        System.out.println("}");
    }
}
