package com.sinosoft.lis.pubfun;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sinosoft.utility.VData;

/**
 * 可以將 Schema 或 Set 同個表的合併成一個 Set 亦可註冊PK 若同時新增同樣PK兩次 只有第一筆會保留
 * 
 * @author 701216
 */
public class MMapShowInfo {
    private static final Logger logger = LoggerFactory.getLogger(MMapShowInfo.class);

    private static SimpleDateFormat SDF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSSSS");

    public static void showVDataInfo(VData vData) {
        logger.info("[showVDataInfo --start]//////////////////////////////////////////////////////////////////////");
        for (Iterator it = vData.iterator(); it.hasNext();) {
            Object item = it.next();
            String prefixLine = "";
            Object schObj = item;
            Object operation = "NA";
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
            } else if (item instanceof MMap) {
                logger.info("↓↓↓↓↓ Inner MMap start ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
                showMMapInfo((MMap) item);
                logger.info("↑↑↑↑↑ Inner MMap end ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
            } else {
                logger.info("\t" + prefixLine + "\t" + operation + "\t" + item);
            }
        }
        logger.info("[showVDataInfo --end]//////////////////////////////////////////////////////////////////////");
    }

    public static void showMMapInfo(MMap mMap) {
        logger.info("[showMMapInfo --start]//////////////////////////////////////////////////////////////////////");
        for (Object item : mMap.keySet()) {
            if (mMap.get(item) != null) {
                String prefixLine = "";
                Object schObj = item;
                Object operation = mMap.get(item);
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
        logger.info("[showMMapInfo --end]//////////////////////////////////////////////////////////////////////");
    }

    private static class SqlParamLogHandler {
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
}
