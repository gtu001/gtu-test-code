package _temp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gtu.collection.MapUtil;

public class Test37 {

    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\gtu00\\OneDrive\\Desktop\\worklog.txt");

        Map<String, List<String>> map = new TreeMap<String, List<String>>();

        Pattern pattern = Pattern.compile("(.*)\\s(\\d{4}\\-\\d{2}\\-\\d{2}\\s\\d{2}\\:\\d{2}\\:\\d{2})", Pattern.CASE_INSENSITIVE);
        Matcher matcher = null;
        LineNumberReader reader = new LineNumberReader(new InputStreamReader(new FileInputStream(file), "utf8"));
        for (String line = null; (line = reader.readLine()) != null;) {
            matcher = pattern.matcher(line);
            if (matcher.find()) {
//                System.out.println(matcher.group(1));
//                System.out.println(matcher.group(2));

                MapUtil.putAsCollection(matcher.group(2), matcher.group(1), map);
            }else {
                System.out.println("not found!!");
            }
        }
        reader.close();

        for (String k : map.keySet()) {
            System.out.println(k);
            List<String> lst = map.get(k);
            for (String v : lst) {
                System.out.println("\t" + v);
            }
        }

        System.out.println("done...");
    }

}
