/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.log.finder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.google.common.base.Joiner;

public class DebugMointerParameterParserDelimit {

    public static void main(String[] args) {
//        String parseStr = "aaaaaaa.aaaaaaaa(aaa.vvvv(dddd[3],\"vvvv \" , aaaa.bbbb[3].vvvv())  , \" bbbb ,  cccc  (, ddd ) \" , null , '  eeeew ', gggg.getMaxNumber( 1 , 100, \"\", 10L, '  )(  cc ' ,10f, 30d, false))[1][2].substring(0, 3)[3].toString()[4]";
//        String parseStr = "vvvvvvvvvvvv[0].toString().substring(3,5)[3].toXml()";
        String parseStr = "[2].table[3].toString()";
        DebugMointerParameterParserDelimit test = DebugMointerParameterParserDelimit.doParse(parseStr);
        System.out.println(ReflectionToStringBuilder.toString(test, ToStringStyle.MULTI_LINE_STYLE));
        for(String str : test.getFinalList()){
            System.out.println("==>"+str);
        }
        System.out.println("done...");
    }
    
    private DebugMointerParameterParserDelimit(){
    }
    
    public static DebugMointerParameterParserDelimit doParse(String parseStr){
        DebugMointerParameterParserDelimit test = new DebugMointerParameterParserDelimit();
        test.isSingleValue = !DebugMointerGetParseObject.quoteParameterPattern.matcher(parseStr).matches();
        parseStr = parseStr.replaceAll("^\\_\\#|\\#\\_$", "");
        test.parseStr = parseStr;
        test.execute();
        return test;
    }
    
    boolean isSingleValue = false;
    Map<String, String> strMap = new HashMap<String,String>();
    List<String> finalList;
    String parseStr;
    private void execute(){
        step1_simpleParseStr();
        List<String> list1 = this.step2_delimiteString();
        List<String> list2 = this.step3_delimiteStringCheckArray(list1);
        finalList = list2;
    }
    
    /**
     * 切割區塊,分出陣列
     */
    private List<String> step3_delimiteStringCheckArray(List<String> list1){
        Map<Float, String> sortMap = new TreeMap<Float, String>();
        for(int ii = 0 ; ii < list1.size() ; ii ++){
            String val = list1.get(ii);
            float tmpKeyStart = ii;
            float tmpKeyEnd = ii + 0.9999f;
            boolean isArrayStart = false;
            while(val.startsWith("[")){
                String newVal = val.substring(0, val.indexOf("]") +1);
                val = val.substring(newVal.length());
                tmpKeyStart += 0.0001f;
                sortMap.put(tmpKeyStart, newVal);
                isArrayStart = true;
            }
            boolean isArrayEnd = false;
            while(val.endsWith("]")){
                String newVal = val.substring(val.lastIndexOf("["));
                val = val.substring(0, val.length() - newVal.length());
                tmpKeyEnd -= 0.0001f;
                sortMap.put(tmpKeyEnd, newVal);
                isArrayEnd = true;
            }
            if(!isArrayStart || StringUtils.isNotBlank(val)){
                sortMap.put((float)ii, val);
                continue;
            }
            if(!isArrayEnd && StringUtils.isNotBlank(val)){
                tmpKeyEnd -= 0.0001f;
                sortMap.put((float)ii, val);
                continue;
            }
        }
        List<String> list2 = new ArrayList<String>();
        for(float key : sortMap.keySet()) {
            list2.add(sortMap.get(key));
        }
        return list2;
    }
    
    /**
     * 切割區塊,分出method
     */
    private List<String> step2_delimiteString(){
        List<String> list1 = new ArrayList<String>();
        String tmpParseStr = parseStr.toString();
        while(tmpParseStr.length() > 0){
            StringBuilder sb = new StringBuilder();
            char[] chs = tmpParseStr.toCharArray();
            for(int ii = 0 ; ii < chs.length ; ii ++){
                sb.append(chs[ii]);
                String tmpStr = sb.toString();
                int leftCount = StringUtils.countMatches(tmpStr, "(");
                int rightCount = StringUtils.countMatches(tmpStr, ")");
                if(chs[ii] == '.' && leftCount == rightCount && leftCount == 0 && sb.length()!=1){
                    sb.deleteCharAt(sb.length() - 1);
                    list1.add(sb.toString());
                    tmpParseStr = tmpParseStr.substring(sb.toString().length());
                    sb = new StringBuilder(".");
                    continue;
                }
                if(leftCount == rightCount && leftCount != 0){
                    list1.add(tmpStr);
                    tmpParseStr = tmpParseStr.substring(tmpStr.length());
                    sb = new StringBuilder();
                    continue;
                }
                if(ii == chs.length - 1){
                    list1.add(tmpStr);
                    tmpParseStr = "";
                    continue;
                }
            }
        }
        for(int ii = 0 ; ii < list1.size(); ii ++){
            list1.set(ii, list1.get(ii).replaceFirst("^\\.", ""));
        }
        return list1;
    }
    
    /**
     * 註記字串為 #str\\d+#
     */
    private void step1_simpleParseStr(){
        List<String> list1 = simpleParseStr(parseStr);
        for(int ii = 0 ; ii < list1.size() ; ii ++){
            String param = list1.get(ii);
//            System.out.println(ii + "---" + param);
            if((param.startsWith("'") && param.endsWith("'")) || //
                    (param.startsWith("\"") && param.endsWith("\""))){
                strMap.put("#str"+ii+"#", param);
                list1.set(ii, "#str"+ii+"#");
            }
        }
        parseStr = Joiner.on(",").join(list1);
    }
    
    private List<String> simpleParseStr(String parseStr) {
        List<String> list = new ArrayList<String>();
        String[] parameter = StringUtils.trim(parseStr).split(",", -1);
        StringBuilder binder = new StringBuilder();
        for (int ii = 0; ii < parameter.length; ii++) {
            String param = StringUtils.trim(parameter[ii]);
            if (binder.length() != 0) {
                String quote = StringUtils.trim(binder.toString()).substring(0, 1);
                if (param.endsWith(quote)) {
                    binder.append(parameter[ii]);
                    list.add(StringUtils.trim(binder.toString()));
                    binder = new StringBuilder();
                } else {
                    binder.append(parameter[ii]);
                }
            } else if (param.startsWith("\"") && !param.endsWith("\"")) {
                binder = new StringBuilder();
                binder.append(parameter[ii] + ",");
            } else if (param.startsWith("\'") && !param.endsWith("\'")) {
                binder = new StringBuilder();
                binder.append(parameter[ii] + ",");
            } else {
                list.add(param);
            }
        }
        return list;
    }

    public Map<String, String> getStrMap() {
        return strMap;
    }

    public List<String> getFinalList() {
        return finalList;
    }

    public String getParseStr() {
        return parseStr;
    }
}
