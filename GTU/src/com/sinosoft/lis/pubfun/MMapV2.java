package com.sinosoft.lis.pubfun;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 可以顯示加入schema 或 set 的時機點
 * 
 * @author 701216
 */
public class MMapV2 extends MMap {
    private static final Logger logger = LoggerFactory.getLogger(MMapV2.class);

    private static SimpleDateFormat SDF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSSSS");

    private String getPrefix() {
        String tname = "[" + Thread.currentThread().getName() + "]";
        StackTraceElement[] sks = Thread.currentThread().getStackTrace();
        StackTraceElement currentElement = null;
        boolean findThisOk = false;
        for (int ii = 0; ii < sks.length; ii++) {
            if (StringUtils.equals(sks[ii].getFileName(), MMapV2.class.getSimpleName() + ".java")) {
                findThisOk = true;
            }
            if (findThisOk && //
                    !StringUtils.equals(sks[ii].getFileName(), MMapV2.class.getSimpleName() + ".java")) {
                currentElement = sks[ii];
                break;
            }
        }
        String timestamp = SDF.format(new Date());
        if (currentElement != null) {
            return tname + timestamp + "(" + currentElement.getClassName() + ":" + currentElement.getLineNumber() + ")";
        }
        return "(" + tname + timestamp + ")";
    }

    private Map<Object, Object[]> mapInfoMap = new LinkedHashMap<Object, Object[]>();

    public void showMMapInfo() {
        logger.info("//////////////////////////////////////////////////////////////////////");
        for (Object item : mapInfoMap.keySet()) {
            if (mapInfoMap.containsKey(item)) {
                Object[] _items = mapInfoMap.get(item);
                Object prefixLine = _items[1];
                Object schObj = item;
                Object operation = _items[0];
                if (com.sinosoft.persistence.Schema.class.isAssignableFrom(item.getClass())) {
                    com.sinosoft.persistence.Schema schema = (com.sinosoft.persistence.Schema) schObj;
                    logger.info("\t" + prefixLine + "\t" + operation + "\t[Schema]" + ReflectionToStringBuilder.toString(schema));
                } else if (com.sinosoft.persistence.Set.class.isAssignableFrom(item.getClass())) {
                    com.sinosoft.persistence.Set schSet = (com.sinosoft.persistence.Set) schObj;
                    logger.info("\t" + prefixLine + "\t" + operation + "\t[Set]" + schSet.getClass().getSimpleName() + "-------[set start]");
                    for (int ii = 1; ii <= schSet.size(); ii++) {
                        com.sinosoft.persistence.Schema schema = (com.sinosoft.persistence.Schema) schSet.get(ii);
                        logger.info("\t" + prefixLine + "\t" + operation + "\t" + ReflectionToStringBuilder.toString(schema));
                    }
                    logger.info("\t" + prefixLine + "\t" + operation + "\t[Set]" + schSet.getClass().getSimpleName() + "-------[set end]");
                } else if (com.sinosoft.utility.SQLwithBindVariables.class.isAssignableFrom(item.getClass())) {
                    com.sinosoft.utility.SQLwithBindVariables sqlBind = (com.sinosoft.utility.SQLwithBindVariables) schObj;
                    logger.info("\t" + prefixLine + "\t" + operation + "\t[SQLwithBindVariables]" + new SqlParamLogHandler(sqlBind).afterParseSql);
                } else {
                    logger.info("\t" + prefixLine + "\t" + operation + "\t" + item);
                }
            }
        }
        logger.info("//////////////////////////////////////////////////////////////////////");
    }

    private class SqlParamLogHandler {
        com.sinosoft.utility.SQLwithBindVariables sqlBind;
        String orignSql;
        String afterParseSql;

        SqlParamLogHandler(com.sinosoft.utility.SQLwithBindVariables sqlBind) {
            this.sqlBind = sqlBind;
            this.orignSql = sqlBind.originalSql();

            Map<String, String> paramMap = new HashMap<String, String>();
            try {
                HashMap<String, Object> values = (HashMap<String, Object>) FieldUtils.readDeclaredField(sqlBind, "values", true);
                for (String key : values.keySet()) {
                    String orignValue = "";
                    if (values.get(key) != null) {
                        orignValue = reverseParam(ReflectionToStringBuilder.toString(values.get(key), ToStringStyle.DEFAULT_STYLE));// NO_FIELD_NAMES_STYLE
                        paramMap.put(key, orignValue);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // UPDATE LPEdorItem set EdorState = 'N' WHERE EdorNo = '?EdorNo?'
            // AND ContNo = '?ContNo?' AND (ModifyDate > '?ModifyDate?' or
            // (ModifyDate = '?ModifyDate?' and ModifyTime > '?ModifyTime?'))
            // AND (MakeDate > '?MakeDate?' or (MakeDate = '?MakeDate?' and
            // MakeTime > '?MakeTime?')) AND not (EdorAcceptNo =
            // '?EdorAcceptNo?' and EdorNo = '?EdorNo?' and EdorType =
            // '?EdorType?' and ContNo = '?ContNo?' and InsuredNo =
            // '?InsuredNo?' and PolNo = '?PolNo?')

            this.afterParseSql = this.getParseSql(orignSql, paramMap);
        }

        private String getParseSql(String orignSql, Map<String, String> paramMap) {
            Pattern ptn = Pattern.compile("\\?\\w+?\\?");
            Matcher mth = ptn.matcher(orignSql);
            StringBuffer sb = new StringBuffer();
            while (mth.find()) {
                String para = mth.group();
                para = para.substring(1, para.length() - 1);
                String afterPara = "##找不到參數##";
                if (paramMap.containsKey(para)) {
                    afterPara = paramMap.get(para);
                }
                mth.appendReplacement(sb, afterPara);
            }
            mth.appendTail(sb);
            return sb.toString();
        }

        private String reverseParam(String reflectString) {
            // com.sinosoft.utility.SQLwithBindVariables$BindData@6149622f[expression=<null>,parsed=true,originValue=<null>,key=MakeTime,jdbcType=12,strValue=15:19:44,dateValue=<null>,intValue=0,dblValue=0.0,collection=false,strList=<null>,intList=<null>]
            Pattern ptn = Pattern.compile("strValue\\=(.*?)\\,");
            Matcher mth = ptn.matcher(reflectString);
            if (mth.find()) {
                return mth.group(1);
            }
            return "##取直失敗##";
        }
    }

    public void put(Object key, Object value) {
        super.put(key, value);
        if (key == null || value == null)
            return;
        mapInfoMap.put(key, new Object[] { value, getPrefix() });
    }

    public MMap toMMap() {
        MMap map = new MMap();
        for (Object key : super.keySet()) {
            map.put(key, super.get(key));
        }
        return map;
    }

    public void add(MMap map) {
        for (Object key : map.keySet()) {
            Object operation = map.get(key);
            put(key, operation);
        }
    }
}
