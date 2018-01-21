package gtu.db.jdbc.util;

import gtu.console.SystemInUtil;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlParseQuestionToParam {

    public static void main(String[] args) throws IOException {
        String text = SystemInUtil.readContent();
        // 第一行為sql
        // 第二行以後為參數
        // line1 ... sql: UPDATE mkt_vd_fund SET ...
        // line2-n ... param[0]:2

        Pattern ptn = Pattern.compile(".*\\sparam\\[(\\d+)\\]\\:(.*)");

        String sql = null;
        Map<Integer, String> param = new HashMap<Integer, String>();
        LineNumberReader reader = new LineNumberReader(new StringReader(text));
        for (String line = null; (line = reader.readLine()) != null;) {
            if (reader.getLineNumber() == 1) {
                sql = line.substring(line.indexOf("sql:") + 4);
            } else {
                Matcher mth = ptn.matcher(line);
                if (mth.find()) {
                    int key = Integer.parseInt(mth.group(1));
                    String val = mth.group(2);
                    param.put(key, val);
                }
            }
        }

        StringBuffer sb = new StringBuffer();
        int index = 0;
        ptn = Pattern.compile("\\?");
        Matcher mth = ptn.matcher(sql);
        while(mth.find()){
            if(!param.containsKey(index)){
                throw new RuntimeException("無此參數 index : " + index + ", " + param);
            }
            mth.appendReplacement(sb, getDBVal(param.get(index)));
            index ++;
        }
        mth.appendTail(sb);
        
        System.out.println("Sql process ==> \n" + sb);
    }

    private static String getDBVal(String val){
        if(val == null || val.equalsIgnoreCase("null")){
            return "null";
        }
        return "'" + val.trim() + "'";
    }
}
