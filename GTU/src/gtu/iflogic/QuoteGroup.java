package gtu.iflogic;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.StringUtils;

public class QuoteGroup {
    String conditionStr;
    Map<String, String> logicMap;

    public void execute() {
        Validate.notBlank(conditionStr, "條件式不可為空");
        conditionStr = conditionStr.replace('\n', '\0');
        String conditionStrCopy = conditionStr.toString();

        Map<Integer, String> qouteMap = new TreeMap<Integer, String>();
        for (int pos = -1; (pos = conditionStrCopy.indexOf("(")) != -1;) {
            char[] charArray = conditionStrCopy.toCharArray();
            charArray[pos] = ' ';
            conditionStrCopy = new String(charArray);
            qouteMap.put(pos, "(");
        }
        for (int pos = -1; (pos = conditionStrCopy.indexOf(")")) != -1;) {
            char[] charArray = conditionStrCopy.toCharArray();
            charArray[pos] = ' ';
            conditionStrCopy = new String(charArray);
            qouteMap.put(pos, ")");
        }

        logicMap = new LinkedHashMap<String, String>();
        Set<Integer> posSet = new TreeSet<Integer>(qouteMap.keySet());
        do {
            int tempPos = -1;
            for (int pos : posSet) {
                String quote = qouteMap.get(pos);
                if ("(".equals(quote)) {
                    tempPos = pos;
                } else if (tempPos != -1) {
                    qouteMap.remove(tempPos);
                    qouteMap.remove(pos);
                    String key = "↑" + tempPos + "," + pos + "↑";
                    String replaceData = StringUtils.center(key, Math.abs(tempPos - pos), ' ');
                    String quoteStr = conditionStrCopy.substring(tempPos, pos);
                    StringBuilder sb = new StringBuilder(conditionStrCopy);
                    sb.delete(tempPos, pos);
                    sb.insert(tempPos, replaceData);
                    if (sb.length() != conditionStrCopy.length()) {
                        throw new RuntimeException("替換長度不一致 : " + sb.length() + " / " + conditionStrCopy.length());
                    }
                    conditionStrCopy = sb.toString();
                    // System.out.println(tempPos + "/" + pos + " == " +
                    // quoteStr);
                    tempPos = -1;
                    logicMap.put(key, quoteStr);
                }
            }
            posSet = new TreeSet<Integer>(qouteMap.keySet());
        } while (!posSet.isEmpty());
        logicMap.put("GROUPZERO", conditionStrCopy);
        for (String key : logicMap.keySet()) {
            System.out.println(key + " ===> " + logicMap.get(key));
        }
    }
}