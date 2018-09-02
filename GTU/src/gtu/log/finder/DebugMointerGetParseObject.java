package gtu.log.finder;

import gtu.log.Logger2File;
import gtu.log.finder.DebugMointerParameterParserMF.MethodOrFieldInvoke;
import gtu.log.finder.DebugMointerParameterParserMF.ParseToObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.map.SingletonMap;
import org.apache.commons.lang.Validate;

public class DebugMointerGetParseObject {
    
    private static final Pattern arrayPattern = Pattern.compile("\\[(\\d+)\\]");
    public static final Pattern quoteParameterPattern = Pattern.compile("\\_\\#.*\\#\\_");
    private Object[] mointerObjects;
    private Logger2File logger;
    
    public DebugMointerGetParseObject(Object[] mointerObjects, Logger2File logger) {
        super();
        this.mointerObjects = mointerObjects;
        this.logger = logger;
    }

    /**
     * 是否為路徑變數
     */
    public static boolean isQuoteParameter(String parseStr) {
        Matcher matcher = quoteParameterPattern.matcher(parseStr);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 取得parse物件
     */
    public Object getParseObject(String parseStr) {
        SingletonMap map = new SingletonMap();
        this.getParseObject(parseStr, map);
        return map.getValue();
    }
    
    private void getParseObject(String parseStr, SingletonMap parseValue) {
        if (parseStr == null) {
            Validate.isTrue(false, "未輸入任何資料");
        }
        Matcher matcher = quoteParameterPattern.matcher(parseStr);
        if (!matcher.matches()) {
            logger.debug("不符合_##_格式!!" + parseStr);
            Object value = DebugMointerParameterParserMF.parseToValue(parseStr, new ParseToObject() {
                @Override
                public Object apply(String parseStr) {
                    throw new IllegalArgumentException("無法解析變數:" + parseStr);
                }
            });
            parseValue.setValue(value);
            return;
        }
        logger.debug("group => " + matcher.group());
        List<MethodOrFieldInvoke> list = DebugMointerParameterParserMaster.newInstance(matcher.group()).execute();
        Object object = null;
        for (int ii = 0; ii < list.size(); ii++) {
            MethodOrFieldInvoke moi = list.get(ii);
            try {
                // 各層呼叫
                if (ii == 0) {
                    object = this.getArrayObject(ii, mointerObjects, moi);
                } else if (object != null && moi.getType() == MethodOrFieldInvoke.FIELD) {
                    Field field = getField(moi.fieldOrMethodName, object.getClass());
                    field.setAccessible(true);
                    object = field.get(object);
                } else if (object != null && moi.getType() == MethodOrFieldInvoke.ARRAY) {
                    object = this.getArrayObject(ii, object, moi);
                } else if (object != null && moi.getType() == MethodOrFieldInvoke.NOPARAM_METHOD) {
                    try {
                        Method mth = object.getClass().getDeclaredMethod(moi.fieldOrMethodName, new Class[0]);
                        mth.setAccessible(true);
                        object = mth.invoke(object, new Object[0]);
                    } catch (Exception ex) {
                        try {
                            Method mth = object.getClass().getMethod(moi.fieldOrMethodName, new Class[0]);
                            mth.setAccessible(true);
                            object = mth.invoke(object, new Object[0]);
                        } catch (Exception ex2) {
                            throw new IllegalArgumentException("錯誤呼叫method: ii=" + ii + ", moi =" + moi);
                        }
                    }
                } else if (object != null && moi.getType() == MethodOrFieldInvoke.COMPLEX_METHOD) {
                    // 修正真正的參數值
                    List<Object> newConditionList = new ArrayList<Object>();
                    if (!moi.parameterList.isEmpty()) {
                        for (int jj = 0; jj < moi.parameterList.size(); jj++) {
                            Object innerValue = moi.parameterList.get(jj);
                            if (innerValue != null && innerValue.getClass() == String.class && //
                                    String.valueOf(innerValue).startsWith("_#") && String.valueOf(innerValue).endsWith("#_")) {
                                newConditionList.add(this.getParseObject(String.valueOf(innerValue)));
                            } else {
                                newConditionList.add(innerValue);
                            }
                        }
                    }
                    moi.parameterList = newConditionList;

                    // 取得對應method
                    List<Method> conditionList = new ArrayList<Method>();
                    for (Method mtd : getAllMethods(object.getClass())) {
                        if (mtd.getName().equals(moi.fieldOrMethodName) && mtd.getParameterTypes().length == moi.parameterList.size()) {
                            mtd.setAccessible(true);
                            conditionList.add(mtd);
                        }
                    }
                    if (conditionList.isEmpty()) {
                        throw new RuntimeException("找不到對應method:" + parseStr);
                    } else if (conditionList.size() == 1) {
                        object = conditionList.get(0).invoke(object, moi.parameterList.toArray());
                    } else {
                        for (Method mth : conditionList) {
                            if (moi.isAmostMatch(mth.getParameterTypes())) {
                                try {
                                    object = mth.invoke(object, moi.parameterList.toArray());
                                    break;
                                } catch (Exception ex) {
                                    throw new RuntimeException("錯誤: parse :" + parseStr + ", ii=" + ii + ", moi =" + moi + //
                                            "\n method : " + mth + "\n" + //
                                            "\n parameter : \n" + DebugMointerTypeUtil.toStringExport(moi.parameterList.toArray()), //
                                            ex);
                                }
                            }
                        }
                    }
                }
                String clzName = object == null ? "NA" : object.getClass().getName();
                logger.debug("第" + ii + "層共" + list.size() + "層: " + clzName + " -> " + object);
                // 延伸呼叫
            } catch (Exception ex) {
                throw new RuntimeException("錯誤: parse :" + parseStr + ", ii=" + ii + ", moi =" + moi, ex);
            }
        }
        parseValue.setValue(object);
    }
    
    private Set<Method> getAllMethods(Class<?> clz) {
        Set<Method> set = new LinkedHashSet<Method>();
        for (Method m : clz.getDeclaredMethods()) {
            set.add(m);
        }
        for (Method m : clz.getMethods()) {
            set.add(m);
        }
        return set;
    }

    private Field getField(String fieldName, Class<?> clz) {
        try {
            return clz.getDeclaredField(fieldName);
        } catch (Exception ex) {
        }
        try {
            return clz.getField(fieldName);
        } catch (Exception ex) {
        }
        throw new RuntimeException("物件不存在Field:" + fieldName);
    }
    
    private Object getArrayObject(int ii, Object object, MethodOrFieldInvoke moi) {
        if (object == null) {
            return null;
        }
        Matcher matcher2 = arrayPattern.matcher(moi.fieldOrMethodName);
        if (!matcher2.matches()) {
            throw new IllegalArgumentException("不正確的名稱: ii = " + ii + " , moi = " + moi);
        }
        int pos = Integer.parseInt(matcher2.group(1));
        int len = Array.getLength(object);
        if (len > pos && pos >= 0) {
            return Array.get(object, Integer.parseInt(matcher2.group(1)));
        } else {
            throw new ArrayIndexOutOfBoundsException("取得位置超出:取" + pos + ",總長" + len + " -> ii :" + ii + ", moi =" + moi);
        }
    }
}
