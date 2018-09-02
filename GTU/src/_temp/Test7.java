package _temp;

import gtu.console.SystemInUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.Map;

public class Test7 {

    public static void main(String[] args) throws IOException {
        String str = SystemInUtil.readContent();
//        BufferedReader bReader = new BufferedReader(new FileReader("D:/swing_error_20161215.log"));
        
        CountMap m = new CountMap();
        
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new StringReader(str));
        for(String line = null ; (line = reader.readLine())!=null ;){
//            sb.append(line);
            m.add(line, 1);
        }
        reader.close();
        
        for(String key : m.map.keySet()){
            System.out.println(key + " - " + m.map.get(key));
        }
        
//        Pattern ptn = Pattern.compile("ORA\\-\\d+\\:.*?\\,\\sline\\s\\d+");
//        Matcher mth = ptn.matcher(sb.toString());
//        while(mth.find()){
//            System.out.println(mth.group());
//        }
    }

    private static class CountMap {
        Map<String,Integer> map = new LinkedHashMap<String,Integer>();
        public void add(String key, int val){
            int value = 0;
            if(map.containsKey(key)){
                value = map.get(key);
            }
            value += val;
            map.put(key, value);
        }
    }
}
