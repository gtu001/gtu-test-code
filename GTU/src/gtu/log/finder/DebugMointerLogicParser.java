package gtu.log.finder;

import gtu.log.Logger2File;
import gtu.log.finder.DebugMointerParameterParserMF.ParseToObject;
import gtu.string.TokenReplacer;
import gtu.string.TokenReplacer.TokenReplacerAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.HashBiMap;

import static gtu.log.finder.DebugMointerUI.getLogger;

public class DebugMointerLogicParser {

    public static void main(String[] args) {
        boolean finalResult = DebugMointerLogicParser.newInstance(
                "_#[2].get(\"bbb\").substring(0,2).toLowerCase()#_ == \"bb\"", new ParseToObject() {
                    @Override
                    public Object apply(String arg0) {
                        return "XXXX";
                    }
                }).execute();
        getLogger().debug("最終結果:" + finalResult);
    }

    private static final Pattern quotePattern = Pattern.compile("\\(.*\\)");

    private DebugMointerLogicParser(String condition, ParseToObject parseToObject) {
        this.condition = condition;
        this.parseToObject = parseToObject;
    }

    public static DebugMointerLogicParser newInstance(String condition, ParseToObject parseToObject) {
        return new DebugMointerLogicParser(condition, parseToObject);
    }

    // --------------------------------------------
    private final ParseToObject parseToObject;// 來原端自訂取值的方式
    private String condition;// 完整if判斷式
    private HashBiMap<String, String> logicMap = HashBiMap.create();// 括號的區塊切割
    private Map<String, Boolean> proceedMap = new HashMap<String, Boolean>();// 處理#\d+#會放在此
    private List<String> serialProcessList = new ArrayList<String>();// 處理順序
    private static final String finalKey = "#99999#";// 最後結果的mapkey

    // --------------------------------------------
    public boolean execute() {
        getLogger().debug("#. DebugMointerLogicParser .s");
        condition = condition.replace("\n", "");
        condition = condition.replace("\r", "");

        getLogger().debug("原始:" + condition);
        condition = this.fixParseStr(condition);
        getLogger().debug("修正:" + condition);

        this.quoteParse(condition);
        this.fixLogicMap();
        this.serialFetchLogic();

        getLogger().debug("logicMap==" + logicMap);
        getLogger().debug("serialProcessList==" + serialProcessList);

        this.proceedParse();

        if (!this.proceedMap.containsKey(finalKey)) {
            throw new RuntimeException("處理失敗,無法取得最終結果:" + condition);
        }
        getLogger().debug("#. DebugMointerLogicParser .e");
        return this.proceedMap.get(finalKey);
    }

    private String fixParseStr(String condition) {
        return TokenReplacer.newInstance().pattern("\\_\\#", "\\#\\_")//
                .replace(condition, new TokenReplacerAction() {
                    @Override
                    public String apply(String finder) {
                        Object value = parseToObject.apply(finder);
                        if (value instanceof String) {
                            return "\"" + value + "\"";
                        } else if (value instanceof Long) {
                            return String.valueOf(value) + "L";
                        } else if (value instanceof Float) {
                            return String.valueOf(value) + "F";
                        } else if (value instanceof Double) {
                            return String.valueOf(value) + "D";
                        } else {
                            return String.valueOf(value);
                        }
                    }
                });
    }

    private void proceedParse() {
        for (String parseStr : serialProcessList) {
            getLogger().debug("section start : " + parseStr);
            Operator op = Operator.lookupFromParseStr(parseStr);
            if (op != null) {
                // 有 && 或 ||
                StringTokenizer tok = new StringTokenizer(parseStr, op.operator);
                Set<Boolean> resultSet = new HashSet<Boolean>();
                while (tok.hasMoreElements()) {
                    String parseInnerStr = (String) tok.nextElement();
                    parseInnerStr = StringUtils.trim(parseInnerStr);
                    boolean result = this.proceedParseInner(parseInnerStr);
                    getLogger().debug("inner : " + parseInnerStr + " -> " + result);
                    resultSet.add(result);
                }
                boolean proceedResult = op.apply(resultSet);
                getLogger().debug("section : " + proceedResult + " -> " + parseStr);
                String key = logicMap.inverse().get(parseStr);
                proceedMap.put(key, proceedResult);
                getLogger().debug("proceedMap==" + proceedMap);
            } else {
                // 沒有 && 或 ||
                parseStr = StringUtils.trim(parseStr);
                boolean result = this.proceedParseInner(parseStr);
                getLogger().debug("section : " + result + " -> " + parseStr);
                String key = logicMap.inverse().get(parseStr);
                proceedMap.put(key, result);
                getLogger().debug("proceedMap==" + proceedMap);
            }
        }
    }

    private boolean proceedParseInner(OpExpress ope) {
        Object left = DebugMointerParameterParserMF.parseToValue(ope.leftStr, parseToObject);
        getLogger().debug("left => " + left);
        Object right = DebugMointerParameterParserMF.parseToValue(ope.rightStr, parseToObject);
        getLogger().debug("right => " + right);
        NumberEnum num = NumberEnum.lookup(left) != null ? NumberEnum.lookup(left) : NumberEnum.lookup(right);
        if (num != null) {
            int compareVal = num.compare(left, right);
            switch (ope.operator) {
            case GreaterEq:
                return compareVal >= 0;
            case LessEq:
                return compareVal <= 0;
            case Greater:
                return compareVal > 0;
            case Less:
                return compareVal < 0;
            case Equal:
                return compareVal == 0;
            case Not:
                return compareVal != 0;
            }
        }
        UnsupportedOperationException ifError = new UnsupportedOperationException("無法支援此處理:" + ope);
        switch (ope.operator) {
        case GreaterEq:
        case LessEq:
        case Greater:
        case Less:
            throw ifError;
        case Equal:
            return ObjectUtils.equals(left, right);
        case Not:
            return !ObjectUtils.equals(left, right);
        }
        throw ifError;
    }

    private boolean proceedParseInner(String parseInnerStr) {
        OperatorInner op = OperatorInner.lookupFromParseStr(parseInnerStr);
        if (op == null) {
            if (parseInnerStr.matches("\\#\\d+\\#") && proceedMap.containsKey(parseInnerStr)) {
                return proceedMap.get(parseInnerStr);
            }
            throw new RuntimeException("無法判斷的運算:[" + parseInnerStr + "]");
        }
        StringTokenizer tok = new StringTokenizer(parseInnerStr, op.operator);
        OpExpress ope = new OpExpress();
        ope.leftStr = StringUtils.trim((String) tok.nextElement());
        ope.rightStr = StringUtils.trim((String) tok.nextElement());
        ope.operator = op;
        return this.proceedParseInner(ope);
    }

    private void serialFetchLogic() {
        for (int ii = logicMap.size() - 1 - 1; ii >= 0; ii--) {
            String value = logicMap.get("#" + ii + "#");
            serialProcessList.add(value);
        }
        serialProcessList.add(logicMap.get(finalKey));
    }

    private void fixLogicMap() {
        Set<String> keySet = new HashSet<String>(logicMap.keySet());
        for (String key : keySet) {
            String needReplaceStr = logicMap.get(key);
            Matcher matcher = quotePattern.matcher(needReplaceStr);
            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                String matcherGroup = matcher.group().replaceAll("^\\(|\\)$", "");
                for (String key2 : keySet) {
                    String rep = logicMap.get(key2);
                    if (StringUtils.equals(rep, matcherGroup)) {
                        matcher.appendReplacement(sb, Matcher.quoteReplacement(key2));
                    }
                }
            }
            matcher.appendTail(sb);
            logicMap.put(key, sb.toString());
        }
    }

    private void quoteParse(String conditionStr) {
        getLogger().debug("===>" + conditionStr);
        Matcher matcher = quotePattern.matcher(conditionStr);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String key = "#" + logicMap.size() + "#";
            matcher.appendReplacement(sb, Matcher.quoteReplacement(key));
            String rewParseStr = matcher.group().replaceAll("^\\(|\\)$", "");
            logicMap.put(key, rewParseStr);
            quoteParse(rewParseStr);
        }
        matcher.appendTail(sb);
        if (!logicMap.containsValue(sb.toString())) {
            logicMap.put(finalKey, sb.toString());
        }
    }

    private static class OpExpress {
        String leftStr;
        String rightStr;
        OperatorInner operator;

        @Override
        public String toString() {
            return "OpExpress [leftStr=" + leftStr + ", rightStr=" + rightStr + ", operator=" + operator + "]";
        }
    }

    private enum NumberEnum {
        LONG(Long.class, long.class) {
            @Override
            int compare(Object arg0, Object arg1) {
                return ObjectUtils.compare(Long.parseLong(arg0.toString()), Long.parseLong(arg1.toString()));
            }
        }, //
        SHORT(Short.class, short.class) {
            @Override
            int compare(Object arg0, Object arg1) {
                return ObjectUtils.compare(Short.parseShort(arg0.toString()), Short.parseShort(arg1.toString()));
            }
        }, //
        INT(Integer.class, int.class) {
            @Override
            int compare(Object arg0, Object arg1) {
                return ObjectUtils.compare(Integer.parseInt(arg0.toString()), Integer.parseInt(arg1.toString()));
            }
        }, //
        DOUBLE(Double.class, double.class) {
            @Override
            int compare(Object arg0, Object arg1) {
                return ObjectUtils.compare(Double.parseDouble(arg0.toString()), Double.parseDouble(arg1.toString()));
            }
        }, //
        FLOAT(Float.class, float.class) {
            @Override
            int compare(Object arg0, Object arg1) {
                return ObjectUtils.compare(Float.parseFloat(arg0.toString()), Float.parseFloat(arg1.toString()));
            }
        }, //
        BYTE(Byte.class, byte.class) {
            @Override
            int compare(Object arg0, Object arg1) {
                return ObjectUtils.compare(Byte.parseByte(arg0.toString()), Byte.parseByte(arg1.toString()));
            }
        }, //
        CHAR(Character.class, char.class) {
            @Override
            int compare(Object arg0, Object arg1) {
                return ObjectUtils.compare(arg0.toString().charAt(0), arg1.toString().charAt(0));
            }
        }, //
        ;
        final Class<?>[] clz;

        NumberEnum(Class<?> clz1, Class<?> clz2) {
            clz = new Class<?>[] { clz1, clz2 };
        }

        abstract int compare(Object object1, Object object2);

        private static NumberEnum lookup(Object object) {
            if (object == null) {
                return null;
            }
            for (NumberEnum e : NumberEnum.values()) {
                for (Class<?> clz : e.clz) {
                    if (object.getClass() == clz) {
                        return e;
                    }
                }
            }
            return null;
        }
    }

    private enum OperatorInner {
        GreaterEq(">="), //
        LessEq("<="), //
        Greater(">"), //
        Less("<"), //
        Equal("=="), //
        Not("!="), //
        ;
        final String operator;

        OperatorInner(String operator) {
            this.operator = operator;
        }

        private static OperatorInner lookupFromParseStr(String parseStr) {
            for (OperatorInner o : OperatorInner.values()) {
                if (parseStr.indexOf(o.operator) != -1) {
                    return o;
                }
            }
            return null;
        }
    }

    private enum Operator {
        AND("&&") {
            @Override
            boolean apply(Set<Boolean> arg0) {
                if (arg0.contains(false)) {
                    return false;
                }
                return true;
            }
        }, //
        OR("||") {
            @Override
            boolean apply(Set<Boolean> arg0) {
                if (arg0.contains(true)) {
                    return true;
                }
                return false;
            }
        }, //
        ;
        final String operator;

        Operator(String operator) {
            this.operator = operator;
        }

        abstract boolean apply(Set<Boolean> resultSet);

        private static Operator lookupFromParseStr(String parseStr) {
            for (Operator o : Operator.values()) {
                if (parseStr.indexOf(o.operator) != -1) {
                    return o;
                }
            }
            return null;
        }
    }
}
