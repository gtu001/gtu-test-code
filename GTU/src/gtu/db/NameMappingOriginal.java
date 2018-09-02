package gtu.db;

import gtu.console.SystemInUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class NameMappingOriginal {

    public static void main(String[] args) throws IOException {
        NameMappingOriginal test = new NameMappingOriginal();

        String sql = SystemInUtil.readContent();
        sql = test.formatFromOrginialSql(sql);

        // 特別為HP改寫, 一般變數或常數要列入參數
        SqlParam_HP hpParam = test.parseSqlToParam_BeforeForHP(sql);
        System.out.println(hpParam.mappingParamMap);

        SqlParam params = test.parseSqlToParam(hpParam.semicolonSql);

        String makeSql = test.toMakeSql(params.questionSql);

        // 特別為HP改寫, 一般變數或常數要列入參數
        test.forHpParametersReplace(hpParam, params);

        System.out.println("\n\n\n\n");

        System.out.println("private void xxxxxxxxxxxxxxxxxxxxxxxx() throws Exception {");

        System.out.println(makeSql);
        System.out.println("Object[] param = new Object[]{" + StringUtils.join(params.paramList, ",") + "};");

        System.out.println(test.getMethodName(sql));
        System.out.println();
        System.out.println();

        System.out.println("}");
        
        test.outputToParameterFoMethod(params.paramList.toString());
    }

    private void outputToParameterFoMethod(String content) throws IOException {
        Set<String> set = new LinkedHashSet<String>();
        Pattern ptn = Pattern.compile("[\\w\\(\\)\\.]+");
        BufferedReader reader = new BufferedReader(new StringReader(content));
        for (String line = null; (line = reader.readLine()) != null;) {
            Matcher mth = ptn.matcher(line);
            while (mth.find()) {
                set.add(mth.group());
            }
        }
        reader.close();

        for (String s : set) {
            System.out.println("String " + s + ", ");
        }
    }

    /**
     * 印出此sql 屬於要呼叫哪種method
     */
    private String getMethodName(String sql) {
        sql = StringUtils.defaultString(sql).toLowerCase();
        if (sql.trim().startsWith("select ")) {
            String rtn = " List<Map<String, Object>> queryList = this.query(sql, param);\n" + //
                    " for(Map<String,Object> map : queryList){\n" + //
                    "     String XXXXXXXX = (String)map.get(\"XXXXXXXXXXXXXXX\");\n" + //
                    "     String XXXXXXXX = (String)map.get(\"XXXXXXXXXXXXXXX\");\n" + //
                    "     String XXXXXXXX = (String)map.get(\"XXXXXXXXXXXXXXX\");\n" + //
                    " }\n" + //
                    " if(queryList.isEmpty()){\n" + //
                    " throw new RuntimeException(\"xxxxxxxxxxxx error\");\n" + //
                    "}\n" + //
                    "Map<String,Object> map = queryList.get(0);\n" + //
                    " Bean_XXXXXXXXXXXXX bean = new Bean_XXXXXXXXXXXXX();\n" + //
                    "     bean. XXXXXXXX = (String)map.get(\"XXXXXXXXXXXXXXX\");\n" + //
                    "     bean. XXXXXXXX = (String)map.get(\"XXXXXXXXXXXXXXX\");\n" + //
                    "     bean. XXXXXXXX = (String)map.get(\"XXXXXXXXXXXXXXX\");\n" + //
                    "";
            return rtn;
        } else {
            return " this.modify(sql, param);";
        }
    }

    /**
     * 將 sb.append(" Sql... "); 將開頭結尾截去
     */
    private String formatFromOrginialSql(String sql) {
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = new BufferedReader(new StringReader(sql));
            for (String line = null; (line = reader.readLine()) != null;) {
                line = StringUtils.trim(StringUtils.defaultString(line));
                line = line.replaceAll("^sb\\.append\\(\"|\"\\)\\;$", "");
                // line = StringUtils.trim(StringUtils.defaultString(line));
                line = " " + line + " ";
                sb.append(line + "\n");
            }
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 產生最終sql語句
     */
    private String toMakeSql(String sql) {
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = new BufferedReader(new StringReader(sql));

            sb.append("String sql = \n");
            for (String line = null; (line = reader.readLine()) != null;) {
                line = StringUtils.defaultString(line);
                sb.append("\"" + line + "\" + //\n");
            }
            sb.append("\"\"");
            sb.append(";");

            reader.close();
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * where MOD_SEQ = 'CONS_VALUE' and BBB = 100 'CONS_VALUE' 和 100 要列舉一併成為 ?
     * 變數
     */
    private SqlParam_HP parseSqlToParam_BeforeForHP(String sql) {
        String compareStr = "(\\>\\=|\\<\\=|\\=|\\<\\>|\\!\\=|\\>|\\<)";
        Pattern ptn1 = Pattern.compile("\\w+\\s*" + compareStr + "\\s*(\\'[^\\']*\\')");
        Pattern ptn2 = Pattern.compile("\\w+\\s*" + compareStr + "\\s*(\\d+)");

        Map<String, String> replaceMap = new HashMap<String, String>();
        Matcher mth = ptn1.matcher(sql);
        StringBuffer sb2 = new StringBuffer();

        int index = 0;

        // 替代掉 'CONS_VALUE'
        while (mth.find()) {
            System.out.println("#CONS_VALUE ------------- " + mth.group(2));
            String constVal = mth.group(2);
            String orignStr = mth.group();
            String replaceKey = ":" + SqlParam_HP.CONST_PREFIX + index;
            String newLine = orignStr.replaceAll(constVal, replaceKey);
            mth.appendReplacement(sb2, newLine);
            replaceMap.put(SqlParam_HP.CONST_PREFIX + index, constVal);
            index++;
        }
        mth.appendTail(sb2);

        // 替代掉 100
        StringBuffer sb3 = new StringBuffer();
        mth = ptn2.matcher(sb2.toString());
        while (mth.find()) {
            System.out.println("#100 ------------- " + mth.group(2));
            String constVal = mth.group(2);
            String orignStr = mth.group();
            String replaceKey = ":" + SqlParam_HP.CONST_PREFIX + index;
            String newLine = orignStr.replaceAll(constVal, replaceKey);
            mth.appendReplacement(sb3, newLine);
            replaceMap.put(SqlParam_HP.CONST_PREFIX + index, constVal);
            index++;
        }
        mth.appendTail(sb3);

        // --------------------------------------------------------------------START
        Pattern ptn3 = Pattern.compile("(crt_date|crt_time|mod_time|mod_pgm)\\s*\\=\\s*(sysdate|\\'[^\\']*\\')", Pattern.CASE_INSENSITIVE);
        StringBuffer sb4 = new StringBuffer();
        mth = ptn3.matcher(sb3.toString());
        while (mth.find()) {
            System.out.println("#special ----" + mth.group(1));
            String paramType = mth.group(1);
            String constVal = mth.group(2);
            String orignStr = mth.group();
            String replaceKey = ":" + SqlParam_HP.CONST_PREFIX + index;
            String newLine = orignStr.replaceAll(constVal, replaceKey);
            mth.appendReplacement(sb4, newLine);
            replaceMap.put(SqlParam_HP.CONST_PREFIX + index, paramType);
            index++;
        }
        mth.appendTail(sb4);
        // --------------------------------------------------------------------END

        SqlParam_HP out = new SqlParam_HP();
        out.orignialSql = sql;
        out.mappingParamMap = replaceMap;
        out.semicolonSql = sb4.toString();
        return out;
    }

    /**
     * 特別為HP改寫
     * 
     * where AAA = 'CONS_VALUE' and BBB = 100 'CONS_VALUE' 和 100 要列舉一併成為 ? 變數
     */
    private void forHpParametersReplace(SqlParam_HP hpParam, SqlParam params) {
        for (int ii = 0; ii < params.paramList.size(); ii++) {
            String paramStr = params.paramList.get(ii);
            if (paramStr.startsWith(SqlParam_HP.CONST_PREFIX) && hpParam.mappingParamMap.containsKey(paramStr)) {
                String replaceConstValue = hpParam.mappingParamMap.get(paramStr);
                replaceConstValue = forHpParametersTypeCheck(replaceConstValue);
                params.paramList.set(ii, replaceConstValue);
            }
        }
    }

    private String forHpParametersTypeCheck(String replaceConstValue) {
        replaceConstValue = StringUtils.defaultString(replaceConstValue);
        if (replaceConstValue.matches("\\d+")) {
            return replaceConstValue;// 數值
        } else {
            replaceConstValue = replaceConstValue.replaceAll("\\'", "");
            return String.format("\"%s\"", replaceConstValue);// 字串
        }
    }

    private SqlParam parseSqlToParam(String sql) {
        Pattern ptn = Pattern.compile("\\:(\\w+)");
        Matcher mth = ptn.matcher(sql);

        List<String> paramList = new ArrayList<String>();

        StringBuffer sb2 = new StringBuffer();

        while (mth.find()) {
            String key = mth.group(1);
            paramList.add(key);// debug用
            mth.appendReplacement(sb2, "?");
        }
        mth.appendTail(sb2);

        SqlParam out = new SqlParam();
        out.orginialSql = sql;
        out.questionSql = sb2.toString();
        out.paramList = paramList;
        return out;
    }

    private static class SqlParam_HP {
        private static final String CONST_PREFIX = "const_";
        String orignialSql;
        String semicolonSql;
        Map<String, String> mappingParamMap = new HashMap<String, String>();
    }

    private static class SqlParam {
        String orginialSql;
        String questionSql;
        List<String> paramList = new ArrayList<String>();
    }
}