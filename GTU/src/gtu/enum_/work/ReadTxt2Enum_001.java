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

public class ReadTxt2Enum_001 {

    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        File file = new File("C:/Users/gtu001/Desktop/F.txt");
        autoGenerateEnum("TypeF", "F", file, "utf8");
        System.out.println("done...");
    }
    
    private static void autoGenerateEnum(String enumName, String startKey, File file, String encode) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encode));
        List<String> messageList = new ArrayList<String>();
        List<String> idList = new ArrayList<String>();
        Pattern ptn = Pattern.compile("^("+startKey+"[\\d\\_]+)(.*)");
        for(String line = null; (line = reader.readLine())!=null;){
            Matcher matcher = ptn.matcher(line);
            if(matcher.find()){
                messageList.add(line);
                idList.add(matcher.group(1));
            }else{
                int pos = messageList.size() - 1;
                String val = messageList.get(pos);
                messageList.set(pos, val +"^"+ line);
            }
        }
        reader.close();
        Map<String, String[]> valsMap = new LinkedHashMap<String,String[]>();
        int maxSize = -1;
        for(String message : messageList){
            Matcher matcher = ptn.matcher(message);
            matcher.find();
            String enumId = matcher.group(1);
            String[] splitStrs = matcher.group(2).split("\t");
            if(idList.get(0).equals(enumId)){
                idList.remove(0);
            }
            for(int ii = 0; ii < splitStrs.length; ii ++){
                splitStrs[ii] = splitStrs[ii].replaceAll("\"", "");
                splitStrs[ii] = splitStrs[ii].replace('\\', ' ');
                splitStrs[ii] = "\"" + StringUtils.trimToEmpty(splitStrs[ii]) + "\"";
            }
            valsMap.put(enumId, splitStrs);
            maxSize = Math.max(maxSize, splitStrs.length);
        }
        System.out.println("lost : " + idList);
        System.out.println("\n\n");
        
        System.out.format("private enum %s {%n", enumName);
        for(String enumId : valsMap.keySet()){
            String[] vals = valsMap.get(enumId);
            for(int ii = 0, len = (maxSize - vals.length); ii < len; ii ++){
                vals = ArrayUtils.add(vals, "\"\"");
            }
            String msg = Arrays.toString(vals).replaceAll("^\\[|\\]$", "");
            System.out.println(enumId + "(" + msg + "),//");
        }
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
