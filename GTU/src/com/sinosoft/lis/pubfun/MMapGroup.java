package com.sinosoft.lis.pubfun;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//中科軟
public class MMapGroup {
    private static final Logger logger = LoggerFactory.getLogger(MMapGroup.class);

    private static SimpleDateFormat SDF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSSSS");

    // ==================================================================================================================
    // ==================================================================================================================

    public void put(Object key, Object value) {
        if (key == null || value == null)
            return;
        this._put(key, value);
        mapInfoMap.put(key, new Object[] { value, getPrefix() });
    }

    public void registerTablePk(String table, String[] pkColumns) {
        if (StringUtils.isNotBlank(table) && pkColumns != null) {
            registerPkMap.put(table, pkColumns);
        }
    }

    private Map<Object, Object> tempMap = new LinkedHashMap<Object, Object>();
    private Map<String, String[]> registerPkMap = new HashMap<String, String[]>();

    private com.sinosoft.persistence.Set getExistsSet(Object setObj) {
        for (Object obj : tempMap.keySet()) {
            if (obj.getClass().getName().equals(setObj.getClass().getName())) {
                return (com.sinosoft.persistence.Set) obj;
            }
        }
        return (com.sinosoft.persistence.Set) setObj;
    }

    private com.sinosoft.persistence.Set getExistsSet2(Object schema) {
        try {
            String classpath = "com.sinosoft.lis.vschema." + schema.getClass().getSimpleName().replaceAll("Schema$", "Set");
            for (Object obj : tempMap.keySet()) {
                if (obj.getClass().getName().equals(classpath)) {
                    return (com.sinosoft.persistence.Set) obj;
                }
            }
            com.sinosoft.persistence.Set setObj = (com.sinosoft.persistence.Set) Class.forName(classpath).newInstance();
            return setObj;
        } catch (Exception ex) {
            throw new RuntimeException("[getExistsSet2] ERR :" + ex.getMessage(), ex);
        }
    }

    private void distinctSet(com.sinosoft.persistence.Set set) {
        Set<Object> set1 = new LinkedHashSet<Object>();
        for (int ii = 1; ii <= set.size(); ii++) {
            Object obj = set.get(ii);
            set1.add(obj);
        }
        set.clear();
        for (Object obj : set1) {
            set.add(obj);
        }
    }

    private void distinctSetByPks(com.sinosoft.persistence.Set set) {
        String tableName = set.getClass().getSimpleName().replaceAll("Set$", "");
        if (!registerPkMap.containsKey(tableName)) {
            return;
        }
        String[] pkColumns = registerPkMap.get(tableName);
        Set<Object> set1 = new LinkedHashSet<Object>();
        for (int ii = 1; ii <= set.size(); ii++) {
            Object obj = set.get(ii);
            boolean isExists = false;
            String pk1 = getPkColumnString(pkColumns, obj);
            B: for (Object obj2 : set1) {
                String pk2 = getPkColumnString(pkColumns, obj2);
                if (StringUtils.equals(pk1, pk2)) {
                    isExists = true;
                    break;
                }
            }
            if (!isExists) {
                set1.add(obj);
            }
        }
        set.clear();
        for (Object obj : set1) {
            set.add(obj);
        }
    }

    private String getPkColumnString(String[] pkColumns, Object schema) {
        String tmpPk = "";
        try {
            StringBuilder sb = new StringBuilder();
            for (String pk : pkColumns) {
                tmpPk = pk;
                Object val = FieldUtils.readDeclaredField(schema, pk, true);
                String strVal = "";
                if (val != null) {
                    strVal = String.valueOf(val);
                }
                sb.append(strVal).append("|");
            }
            return sb.toString();
        } catch (Exception ex) {
            throw new RuntimeException("[getPkColumnString] PK : " + tmpPk + ", ERR : " + ex.getMessage(), ex);
        }
    }

    private void _put(Object key, Object value) {
        if (key.getClass().getSimpleName().endsWith("Schema")) {
            com.sinosoft.persistence.Set setObj = getExistsSet2(key);
            setObj.add(key);
            this.distinctSet(setObj);
            this.distinctSetByPks(setObj);
            tempMap.put(setObj, value);
        } else if (key.getClass().getSimpleName().endsWith("Set")) {
            com.sinosoft.persistence.Set setObj = getExistsSet(key);
            setObj.addAll((com.sinosoft.persistence.Set) key);
            this.distinctSet(setObj);
            this.distinctSetByPks(setObj);
            tempMap.put(setObj, value);
        } else {
            tempMap.put(key, value);
        }
    }

    public MMap toMMap() {
        MMap map = new MMap();
        for (Object obj : tempMap.keySet()) {
            Object operation = tempMap.get(obj);
            map.put(obj, operation);
        }
        return map;
    }

    // ==================================================================================================================
    // ==================================================================================================================

    private String getPrefix() {
        String tname = "[" + Thread.currentThread().getName() + "]";
        StackTraceElement[] sks = Thread.currentThread().getStackTrace();
        StackTraceElement currentElement = null;
        boolean findThisOk = false;
        for (int ii = 0; ii < sks.length; ii++) {
            if (StringUtils.equals(sks[ii].getFileName(), MMapGroup.class.getSimpleName() + ".java")) {
                findThisOk = true;
            }
            if (findThisOk && //
                    !StringUtils.equals(sks[ii].getFileName(), MMapGroup.class.getSimpleName() + ".java")) {
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
        logger.info("[put order --start]//////////////////////////////////////////////////////////////////////");
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
        logger.info("[put order --end]//////////////////////////////////////////////////////////////////////");
        logger.info("[distinct --start]//////////////////////////////////////////////////////////////////////");
        for (Object item : tempMap.keySet()) {
            Object schObj = item;
            Object operation = tempMap.get(item);
            String prefixLine = "";
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
        logger.info("[distinct --end]//////////////////////////////////////////////////////////////////////");
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
                String afterPara = "##æ銝å##";
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
            return "##å憭望##";
        }
    }
}
