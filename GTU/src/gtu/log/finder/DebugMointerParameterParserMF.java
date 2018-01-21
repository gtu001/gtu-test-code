package gtu.log.finder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.google.common.base.Joiner;

public class DebugMointerParameterParserMF {

    public static void main(String[] args) {
        System.out.println("done...");
    }
    
    String parseStr;
    DebugMointerParameterParserDelimit delimit;
    public static DebugMointerParameterParserMF newInstance(String parseStr, DebugMointerParameterParserDelimit delimit){
        DebugMointerParameterParserMF test = new DebugMointerParameterParserMF();
        test.parseStr = parseStr;
        test.delimit = delimit;
        return test;
    }
    
    private DebugMointerParameterParserMF(){
    }

    public interface ParseToObject {
        Object apply(String parseStr);
    }

    private static final Pattern mainObjectPattern = Pattern.compile("[\\w\\.\\[\\]\\_]+");

    public MethodOrFieldInvoke execute() {
        Matcher matcher = mainObjectPattern.matcher(parseStr);
        if (!matcher.find()) {
            throw new RuntimeException("無法解析物件:" + parseStr);
        }
        MethodOrFieldInvoke moi = new MethodOrFieldInvoke();
        moi.fieldOrMethodName = matcher.group();
        String parameter = parseStr.substring(matcher.end());
        boolean isMethod = parameter.startsWith("(") && parameter.endsWith(")");
        parameter = parameter.replaceAll("^\\(|\\)$", "");
        parameter = fixParameter(parameter);
        if (isMethod) {
            if (StringUtils.isBlank(parameter)) {
                moi.isNoParameterMethod = true;
            } else {
                List<String> list = parseToParameterString(parameter);
                ParseToObject parseToObject = new ParseToObject() {
                    @Override
                    public Object apply(String parseStr) {
                        //單純只是字串
                        if(parseStr.matches("^\\#str\\d+\\#$")){
                            String strValue = delimit.getStrMap().get(parseStr);
                            if(strValue!=null && strValue.matches("^\'|\'$")){
                                strValue = strValue.replaceAll("^\'|\'$", "");
                            }else if(strValue!=null && strValue.matches("^\"|\"$")){
                                strValue = strValue.replaceAll("^\"|\"$", "");
                            }
                            return strValue;
                        }
                        return "_#" + parseStr + "#_";//TODO 此處遞迴
                    }
                };
                List<Object> parameterList = new ArrayList<Object>();
                for (String x1 : list) {
                    parameterList.add(parseToValue(x1, parseToObject));
                }
                moi.parameterList = parameterList;
            }
        }
        return moi;
    }

    private String fixParameter(String parameter) {
        int left = StringUtils.countMatches(parameter, "(");
        int right = StringUtils.countMatches(parameter, ")");
        if (left + 1 == right) {
            StringBuilder sb = new StringBuilder(parameter);
            sb.deleteCharAt(sb.lastIndexOf(")"));
            return sb.toString();
        } else if (left == right) {
            return parameter;
        }
        throw new RuntimeException("餐數括號不match:" + parameter + "[" + left + ", " + right + "]");
    }

    /**
     * 取得 method 參數
     */
    private List<String> parseToParameterString(String parseStr) {
//        List<String> list = simpleParseStr(parseStr);
        List<String> list = Arrays.asList(StringUtils.trim(parseStr).split(",", -1));
        int totalLeftParenthsis = 0;
        int totalRightParenthsis = 0;
        List<String> newList = new ArrayList<String>();
        List<String> subList = new ArrayList<String>();
        for (int ii = 0; ii < list.size(); ii++) {
            String param = list.get(ii);
            totalLeftParenthsis += StringUtils.countMatches(param, "(");
            totalRightParenthsis += StringUtils.countMatches(param, ")");
            if (totalLeftParenthsis != totalRightParenthsis) {
                subList.add(param);
            } else if (totalLeftParenthsis == totalRightParenthsis && totalLeftParenthsis != 0) {
                subList.add(param);
                newList.add(Joiner.on(",").join(subList));
                subList.clear();
            } else {
                newList.add(param);
            }
        }
        return newList;
    }

//    private List<String> simpleParseStr(String parseStr) {
//        List<String> list = new ArrayList<String>();
//        String[] parameter = StringUtils.trim(parseStr).split(",", -1);
//        StringBuilder binder = new StringBuilder();
//        for (int ii = 0; ii < parameter.length; ii++) {
//            String param = StringUtils.trim(parameter[ii]);
//            if (binder.length() != 0) {
//                String quote = StringUtils.trim(binder.toString()).substring(0, 1);
//                if (param.endsWith(quote)) {
//                    binder.append(parameter[ii]);
//                    list.add(StringUtils.trim(binder.toString()));
//                    binder = new StringBuilder();
//                } else {
//                    binder.append(parameter[ii]);
//                }
//            } else if (param.startsWith("\"") && !param.endsWith("\"")) {
//                binder = new StringBuilder();
//                binder.append(parameter[ii] + ",");
//            } else if (param.startsWith("\'") && !param.endsWith("\'")) {
//                binder = new StringBuilder();
//                binder.append(parameter[ii] + ",");
//            } else {
//                list.add(param);
//            }
//        }
//        return list;
//    }

    public static Object parseToValue(String parseStr, ParseToObject parseToObject) {
        if (parseStr.equalsIgnoreCase("null")) {
            return null;
        } else if (parseStr.equals("''") || parseStr.equals("\"\"")) {
            return "";
        } else if (parseStr.matches("^\\'.*\\'$")) {// 字串
            return parseStr.replaceAll("^\\'|\\'$", "");
        } else if (parseStr.matches("^\".*\"$")) {// 字串
            return parseStr.replaceAll("^\"|\"$", "");
        } else if (parseStr.matches("^\\-?\\d+[lL]{1}$")) {// long
            return Long.parseLong(parseStr.replaceAll("[lL]", ""));
        } else if (parseStr.matches("^\\-?\\d+[dD]{1}$")) {// double
            return Double.parseDouble(parseStr.replaceAll("[dD]", ""));
        } else if (parseStr.matches("^\\-?\\d+[fF]{1}$")) {// float
            return Float.parseFloat(parseStr.replaceAll("[fF]", ""));
        } else if (parseStr.matches("^\\-?\\d+$")) {// int
            return Integer.parseInt(parseStr);
        } else if (StringUtils.equalsIgnoreCase(parseStr, "true") || //
                StringUtils.equalsIgnoreCase(parseStr, "false")) {// boolean
            return Boolean.parseBoolean(parseStr);
        } else {
            return parseToObject.apply(parseStr);
        }
    }

    static class MethodOrFieldInvoke {
        String fieldOrMethodName;
        List<Object> parameterList = new ArrayList<Object>();
        boolean isNoParameterMethod;
        boolean isSimpleValue;
        Object simpleValue;
        
        static final int FIELD = 0;
        static final int NOPARAM_METHOD = 1;
        static final int COMPLEX_METHOD = 2;
        static final int ARRAY = 3;
        static final int VALUE = 4;
        
        public int getType(){
            if(isSimpleValue){
                return VALUE;
            }else if(!parameterList.isEmpty()){
                return COMPLEX_METHOD;
            }else if(isNoParameterMethod == true && parameterList.isEmpty()){
                return NOPARAM_METHOD;
            }else if(fieldOrMethodName.indexOf("(") == -1 && fieldOrMethodName.indexOf("[") == -1){
                return FIELD;
            }else if(fieldOrMethodName.indexOf("[") != -1 && fieldOrMethodName.indexOf("]") != -1){
                return ARRAY;
            }
            throw new RuntimeException("無法判斷類型:" + ReflectionToStringBuilder.toString(this));
        }

        private List<Class<?>> getMatchClassList() {
            List<Class<?>> list = new ArrayList<Class<?>>();
            for (int ii = 0; ii < parameterList.size(); ii++) {
                Object value = parameterList.get(ii);
                if (value == null) {
                    list.add(void.class);
                } else {
                    list.add(value.getClass());
                }
            }
            return list;
        }

        public boolean isAmostMatch(Class<?>[] clz) {
            List<Class<?>> clzList = getMatchClassList();
            for (int ii = 0; ii < clz.length; ii++) {
                Class<?> clax = clz[ii];
                if (clzList.get(ii) == void.class) {
                    continue;
                } else if (clzList.get(ii) != clax) {
                    return false;
                }
            }
            return true;
        }
        
        private String showType(){
            switch (this.getType()) {
            case MethodOrFieldInvoke.FIELD:
                return "FIELD";
            case MethodOrFieldInvoke.NOPARAM_METHOD:
                return "NOPARAM_METHOD";
            case MethodOrFieldInvoke.COMPLEX_METHOD:
                return "COMPLEX_METHOD";
            case MethodOrFieldInvoke.ARRAY:
                return "ARRAY";
            case MethodOrFieldInvoke.VALUE:
                return "VALUE";
            }
            return "NA";
        }

        @Override
        public String toString() {
            return "MethodOrFieldInvoke [fieldOrMethodName=" + fieldOrMethodName + ", parameterList=" + parameterList + ", isNoParameterMethod=" + isNoParameterMethod + ", isSimpleValue="
                    + isSimpleValue + ", simpleValue=" + simpleValue  + ", showType=" + showType() +  "]";
        }
    }
}
