package _temp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import gtu.console.SystemInUtil;

public class Test39 {

    public static void main(String[] args) throws InterruptedException, IOException {
        String text = SystemInUtil.readContent();
        BufferedReader reader = new BufferedReader(new StringReader(text));
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (String line = null; (line = reader.readLine()) != null;) {
            if (StringUtils.isNotBlank(line)) {
                String[] arry = line.split("[\\t\\s]+", -1);
                String key = arry[0];
                String val = arry[1];
                map.put(val, key);
            }
        }

        for (String col : map.keySet()) {
            String comment = map.get(col);
            System.out.println("String " + col + " = null; //" + comment);
        }

        for (String col : map.keySet()) {
            String comment = map.get(col);
            System.out.println("map.put(\"" + col + "\", " + col + ");//" + comment);
        }
        
        System.out.println("done...");
    }

}
