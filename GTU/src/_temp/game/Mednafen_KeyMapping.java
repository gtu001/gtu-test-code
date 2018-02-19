package _temp.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mednafen_KeyMapping {

    public static void main(String[] args) throws InterruptedException {
        try {
            Pattern pattern = Pattern.compile("SDLK_(\\w+)\\s*\\=\\s*(\\d+)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = null;
            Map<Integer, String> keyMap = new LinkedHashMap<Integer, String>();
            InputStream key1InputStream = Mednafen_KeyMapping.class.getResourceAsStream("key1.txt");
            LineNumberReader reader = new LineNumberReader(new InputStreamReader(key1InputStream, "utf8"));
            for (String line = null; (line = reader.readLine()) != null;) {
                matcher = pattern.matcher(line);
                while (matcher.find()) {
                    Integer key = Integer.parseInt(matcher.group(2));
                    String val = matcher.group(1);
                    keyMap.put(key, val);
                }
            }
            reader.close();

            Pattern pattern2 = Pattern.compile("(\\w+)\\.input\\.port1\\.gamepad\\.(\\w+)\\skeyboard\\s(\\d+)", Pattern.CASE_INSENSITIVE);
            InputStream key2InputStream = new FileInputStream(new File("C:\\Users\\gtu00\\OneDrive\\Desktop\\mednafen-0.9.48-win64\\mednafen-09x.cfg"));
            LineNumberReader reader2 = new LineNumberReader(new InputStreamReader(key2InputStream, "utf8"));
            for (String line = null; (line = reader2.readLine()) != null;) {
                matcher = pattern2.matcher(line);
                while (matcher.find()) {
                    String type = matcher.group(1);
                    String key = matcher.group(2);
                    Integer code = Integer.parseInt(matcher.group(3));
                    String mappingKey = "NOT FOUND";
                    if (keyMap.containsKey(code)) {
                        mappingKey = keyMap.get(code);
                    }
                    System.out.println(String.format("%s.%s = %s", type, key, mappingKey));
                }
            }
            reader2.close();

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        System.out.println("done...");
    }

}
