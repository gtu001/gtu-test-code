package gtu.itext.iisi.data;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 * @author Chi-Feng
 */
public class CellBeanProperty implements CellDataSource {

    /** Thread-safe 版本的 SimpleDateFormat */
    private static class MyFormat extends ThreadLocal<SimpleDateFormat> {
        final private String format;

        private MyFormat(String format) {
            super();
            this.format = format;
        }

        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(this.format);
        }
    }

    private static final MyFormat formatterYYMMDD = new MyFormat("yyyy/MM/dd");

    private static final MyFormat formatterYYMMDDTime = new MyFormat("yyyy/MM/dd HH:mm:ss");

    /**
     * 定義自動轉型作業.
     * 
     * 若轉型失敗，則丟出 Exception.
     * 
     * @author 920111 在 2010/4/26 建立
     */
    static public class CastTo<F, T> extends CellBeanProperty {
        public CastTo(String propertyName) {
            super(propertyName);
        }

        @SuppressWarnings("unchecked")
        protected T trans(Object dataObj, F v) {
            return (T) v;
        }

        @Override
        final public T eval(Object dataObj) {
            @SuppressWarnings("unchecked")
            // 使用者應保證呼叫對象的 指定 property 的型別為 指定的 F
            F v = (F) super.eval(dataObj);
            return trans(dataObj, v);
        }
    }

    /**
     * 西元年月日，格式轉 Date Object
     * 
     * @return 非時間格式，傳回NULL
     */
    static public CastTo<Object, Date> asDate(String propertyName) {
        CastTo<Object, Date> p = new CastTo<Object, Date>(propertyName) {
            @Override
            protected Date trans(Object dataObj, Object value) {
                if (value instanceof Date) {
                    return (Date) value;
                } else {
                    String str = ObjectUtils.toString(value);
                    try {
                        return CellBeanProperty.formatterYYMMDD.get().parse(str);
                    } catch (ParseException e) {
                        // 無法解析時，以 null 處理.
                        return null;
                    }
                }
            }
        };
        return p;
    }

    /**
     * 西元年月日時，格式轉 Date Object
     * 
     * @return 非時間格式，傳回NULL
     */
    static public CastTo<Object, Date> asDateTime(String propertyName) {
        CastTo<Object, Date> p = new CastTo<Object, Date>(propertyName) {
            @Override
            protected Date trans(Object dataObj, Object value) {
                if (value instanceof Date) {
                    return (Date) value;
                } else {
                    String str = ObjectUtils.toString(value);
                    try {
                        return CellBeanProperty.formatterYYMMDDTime.get().parse(str);
                    } catch (ParseException e) {
                        // 無法解析時，以 null 處理.
                        return null;
                    }
                }
            }
        };
        return p;
    }

    static public CastTo<Object, Integer> asInteger(String propertyName) {
        CastTo<Object, Integer> p = new CastTo<Object, Integer>(propertyName) {
            @Override
            protected Integer trans(Object dataObj, Object value) {
                if (value instanceof Number) {
                    return ((Number) value).intValue();
                }
                if (value instanceof String) {
                    return toInteger((String) value);
                }
                return null;
            }
        };
        return p;
    }

    /**
     * 字串轉換為 Integer.
     * 
     * @return 非數值格式，傳回NULL
     */
    final private static Integer toInteger(String str) {
        if (!NumberUtils.isNumber(str)) {
            return null;
        }
        return NumberUtils.createInteger(str);
    }

    private String propertyName;

    public CellBeanProperty(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return this.propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public void reset() {
    };

    @Override
    public Object eval(Object dataObj) {
        try {
            return PropertyUtils.getProperty(dataObj, this.propertyName);
        } catch (IllegalAccessException e) {
            return null;
        } catch (InvocationTargetException e) {
            return null;
        } catch (NoSuchMethodException e) {
            return null;
        } catch (NestedNullException e) {
            return null;
        }
    }

}
