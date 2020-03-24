package _temp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

import gtu.file.FileUtil;

public class Test57 {

    public static void main(String[] args) {

        List<String> lst = FileUtil.loadFromFile_asList(new File("C:/Users/wistronits/Desktop/中信資料/大概是這些檔2.txt"), "utf8");

        // distinct //
        {
            Set<String> lst22 = new LinkedHashSet<String>();
            for (String strVal : lst) {
                if (strVal.contains("cashweb")) {
                    strVal = strVal.substring(strVal.indexOf("cashweb") - 1);
                }
                if (strVal.contains("cashportal")) {
                    strVal = strVal.substring(strVal.indexOf("cashportal") - 1);
                }
                lst22.add(strVal);
            }
            lst = new ArrayList<String>(lst22);
        }
        // distinct //

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
        lst2.add(String.valueOf(map.keySet()));

        // [zip, xlsx, DML, rar, 7z, jsp, js, wsdd, DDL, bak, sql, docx, txt,
        // java, xml, doc, properties]
        String[] keys = new String[] { "DML", "jsp", "js", "wsdd", "DDL", "sql", "java", "xml", "properties" };

        for (String key : map.keySet()) {
            if (ArrayUtils.contains(keys, key)) {
                for (String data : map.get(key)) {
                    lst2.add(data);
                }
            }
            lst2.add("");
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
