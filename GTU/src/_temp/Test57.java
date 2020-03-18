package _temp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import gtu.file.FileUtil;

public class Test57 {

    public static void main(String[] args) {

        List<String> lst = FileUtil.loadFromFile_asList(new File("C:\\Users\\wistronits\\Desktop\\xxxxxxxxxx.txt"), "utf8");

        Map<String, List<String>> map = new HashMap<String, List<String>>();

        for (String data : lst) {
            data = StringUtils.trimToEmpty(data);
            if (StringUtils.isBlank(data)) {
                continue;
            }
            if (data.indexOf(".") == -1) {
                continue;
            }
            String key = data.substring(data.lastIndexOf(".") + 1);
            addToMap(key, data, map);
        }

        List<String> lst2 = new ArrayList<String>();
        for (String key : map.keySet()) {
            for (String data : map.get(key)) {
                lst2.add(data);
            }
        }
        FileUtil.saveToFile(new File(FileUtil.DESKTOP_PATH, "xxxxxxx2.txt"), StringUtils.join(lst2, "\r\n"), "UTF8");
        System.out.println("done..");
    }

    private static void addToMap(String key, String data, Map<String, List<String>> map) {
        List<String> lst = new ArrayList<String>();
        if (map.containsKey(key)) {
            lst = map.get(key);
        }
        lst.add(data);
        map.put(key, lst);
    }
}
