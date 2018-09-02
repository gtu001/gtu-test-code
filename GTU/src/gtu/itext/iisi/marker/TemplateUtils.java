package gtu.itext.iisi.marker;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;
import net.sf.json.util.JSONUtils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class TemplateUtils {

    public static String[] parseLine(String line, Map<String, String> params, String markId) {
        if (line == null || line.startsWith("#")) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        String[] split = StringUtils.split(line, ",");
        Pattern ptn = Pattern.compile("\\$\\{(\\w+)\\}");
        boolean globalImage = true;
        for (int i = 0; i < split.length; i++) {
            String item = split[i].trim();
            StringBuilder tmpS = new StringBuilder();
            int nowStart = 0;
            Matcher matcher = ptn.matcher(item);
            while (matcher.find()) {
                if (i == 1 && "1".equals(split[0])) {
                    globalImage = false;
                }
                int start = matcher.start();
                int end = matcher.end();
                tmpS.append(item.substring(nowStart, start));
                String key = matcher.group(1);
                tmpS.append(StringUtils.defaultString(params.get(key)));
                nowStart = end;
            }
            tmpS.append(StringUtils.substring(item, nowStart));
            split[i] = tmpS.toString();
        }
        if (!globalImage && split.length > 2) {
            split[1] = markId + File.separator + split[1];
        }
        return split;
    }

    public static void main(String[] args) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("AA", "aaa");
        params.put("BB", "bbb");

        {
            String[] parseLine = parseLine("1, 2, b, ${AA}x, ${AA}-${BB}-${CC}", params, "SS001");
            System.err.println(Arrays.toString(parseLine));
        }
        {
            String[] parseLine = parseLine("1, ${AA}, b, ${AA}x, ${AA}-${BB}-${CC}", params, "SS001");
            System.err.println(Arrays.toString(parseLine));
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("AAA", "1");
        map.put("BBB", "2");
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("AAA", "3");
        map2.put("BBB", "4");

        @SuppressWarnings("unchecked")
        List<Map<String, String>> list = Arrays.asList(map, map2);
        String valueToString = JSONUtils.valueToString(list);
        System.err.println(valueToString);
        String json = "[{BBB=2, AAA=1} \n, {BBB=4, AAA=3}]";
        final JSON jjj = JSONSerializer.toJSON(json);
        if (jjj.isArray()) {
            JSONArray json2 = (JSONArray) jjj;
            System.err.println(json2.get(0));
        }

    }
}
