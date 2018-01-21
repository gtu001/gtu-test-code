package gtu.db;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameMapping {
    private final String sql;
    private final Map<String, Object> param;
    private String questionSql;// 問號sql
    private List<String> questionList = new ArrayList<String>();// 變數使用順序
    private List<Object> valueList = new ArrayList<Object>();// 值順序
    private Set<String> columnList = new LinkedHashSet<String>();// 用到的參數欄位

    public NameMapping(String sql, Map<String, Object> param) {
        this.sql = sql;
        this.param = param;
    }

    public void execute() {
        Pattern ptn = Pattern.compile("\\:(\\w+)");
        Matcher mth = ptn.matcher(sql);

        StringBuffer sb2 = new StringBuffer();

        while (mth.find()) {
            String key = mth.group(1);
            columnList.add(key);// debug用
            if (param.containsKey(key)) {
                questionList.add(key);
                valueList.add(param.get(key));
                // System.out.println("->" + key);
                mth.appendReplacement(sb2, "?");
            } else {
                System.out.println("找不到符合的key : " + key);
                mth.appendReplacement(sb2, ":" + key);
            }
        }
        mth.appendTail(sb2);
        questionSql = sb2.toString();

        // generate();//debug用
        // debug();//debug用
    }

    private void generate() {
        for (String c : columnList) {
            System.out.println("String " + c + ", ");
        }
        for (String c : columnList) {
            System.out.format("param.put(\"%1$s\", %1$s);\n", c);
        }
    }

    private void debug() {
        System.out.println("Mapping SQL : " + questionSql);
        System.out.println("serial param size = " + questionList.size());
        for (int ii = 0; ii < questionList.size(); ii++) {
            String key = questionList.get(ii);
            System.out.println((ii + 1) + "[" + key + "]:" + param.get(key));
        }
    }

    public String getQuestionSql() {
        return questionSql;
    }

    public List<Object> getValueList() {
        return valueList;
    }
}