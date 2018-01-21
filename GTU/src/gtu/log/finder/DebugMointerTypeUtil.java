package gtu.log.finder;

import gtu.log.PrintStreamAdapter;

import java.io.PrintStream;
import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.time.DateFormatUtils;

public class DebugMointerTypeUtil {

    public static void main(String[] args) {
        java.sql.Date stamp = new java.sql.Date(System.currentTimeMillis());
        System.out.println(dateToString(stamp));
    }

    /**
     * 是否為原生型別
     */
    public static boolean isPrimitive(Class<?> clz) {
        for (PrimitiveEnum e : PrimitiveEnum.values()) {
            if (e.clz == clz) {
                return true;
            }
        }
        return false;
    }

    /**
     * 轉換某型別的職
     */
    public static <T> T parseToType(String value, Class<T> clz) {
        if (value == null) {
            return null;
        }
        for (PrimitiveEnum e : PrimitiveEnum.values()) {
            if (e.clz == clz) {
                return (T) e.applyString(value);
            }
        }
        throw new RuntimeException("找不到對應原生型別" + clz + ",值:" + value);
    }

    /**
     * 將物件資料以字串明確顯示
     */
    public static String parseExplicitToString(Object object) {
        if (object == null) {
            return "<null>";
        }
        for (SimpleExplicitType e : SimpleExplicitType.values()) {
            if (e.clz == object.getClass() || e.clz.isAssignableFrom(object.getClass())) {
                return e.toStringZ(object);
            }
        }
        return ReflectionToStringBuilder.toString(object, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    /**
     * 原生型別陣列轉成物件型別陣列
     */
    public static Object[] filterArray(Object object) {
        if (object.getClass().isArray() && object.getClass().getComponentType().isPrimitive()) {
            Class<?> clz = PrimitiveToWrapperEnum.lookupWrapper(object.getClass().getComponentType());
            int length = Array.getLength(object);
            Object rtnArray = Array.newInstance(clz, length);
            for (int ii = 0; ii < length; ii++) {
                Array.set(rtnArray, ii, Array.get(object, ii));
            }
            return (Object[]) rtnArray;
        }
        return (Object[]) object;
    }

    /**
     * 將物件資料轉成明確字串簡報
     */
    public static String toStringExport(Object toObj) {
        try {
            if (toObj == null) {
                return "<null>";
            }
            if (toObj.getClass().isArray()) {
                return ToStringExport.ARRAY.applyString(toObj, 0);
            } else {
                for (DebugMointerTypeUtil.ToStringExport e2 : DebugMointerTypeUtil.ToStringExport.values()) {
                    for (Class<?> clz : e2.clz) {
                        if (clz.isAssignableFrom(toObj.getClass())) {
                            return e2.applyString(toObj, 0);
                        }
                    }
                }
            }
            return DebugMointerTypeUtil.ToStringExport.DEFAULT.applyString(toObj, 0);
        } catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("ERROR Object : " + toObj + " -- " + (toObj != null ? toObj.getClass().getName() : "NA") + "\n");
            ex.printStackTrace(new PrintStream(new PrintStreamAdapter("UTF8") {
                @Override
                public void println(String message) {
                    sb.append(message);
                }
            }, true));
            return sb.toString();
        }
    }

    enum PrimitiveToWrapperEnum {
        CHAR(char.class, Character.class), //
        BOOL(boolean.class, Boolean.class), //
        BYTE(byte.class, Byte.class), //
        SHORT(short.class, Short.class), //
        INT(int.class, Integer.class), //
        LONG(long.class, Long.class), //
        FLOAT(float.class, Float.class), //
        DOUBLE(double.class, Double.class), // '
        ;
        final Class<?> primitive, wrapper;

        PrimitiveToWrapperEnum(Class<?> clz1, Class<?> clz2) {
            this.primitive = clz1;
            this.wrapper = clz2;
        }

        static Class<?> lookupWrapper(Class<?> clz) {
            for (PrimitiveToWrapperEnum e : PrimitiveToWrapperEnum.values()) {
                if (clz == e.primitive) {
                    return e.wrapper;
                }
            }
            return null;
        }
    }

    enum PrimitiveEnum {
        STRING(String.class) {
            @Override
            Object applyString(String value) {
                if (value != null) {
                    if (value.matches("^\"|\"$")) {
                        value = value.replaceAll("^\"|\"$", "");
                    } else if (value.matches("^\'|\'$")) {
                        value = value.replaceAll("^\'|\'$", "");
                    }
                }
                return value;
            }
        }, //
        INT(int.class) {
            @Override
            Object applyString(String value) {
                return Integer.parseInt(value);
            }
        }, //
        INTEGER(Integer.class) {
            @Override
            Object applyString(String value) {
                return Integer.parseInt(value);
            }
        }, //
        LONG(long.class) {
            @Override
            Object applyString(String value) {
                return Long.parseLong(value);
            }
        }, //
        LONG_WRAPPER(Long.class) {
            @Override
            Object applyString(String value) {
                return Long.parseLong(value);
            }
        }, //
        CHAR(char.class) {
            @Override
            Object applyString(String value) {
                return value.charAt(0);
            }
        }, //
        CHARACTER(Character.class) {
            @Override
            Object applyString(String value) {
                return value.charAt(0);
            }
        }, //
        FLOAT(float.class) {
            @Override
            Object applyString(String value) {
                return Float.parseFloat(value);
            }
        }, //
        FLOAT_WRAPPER(Float.class) {
            @Override
            Object applyString(String value) {
                return Float.parseFloat(value);
            }
        }, //
        DOUBLE(double.class) {
            @Override
            Object applyString(String value) {
                return Double.parseDouble(value);
            }
        }, //
        DOUBLE_WRAPPER(Double.class) {
            @Override
            Object applyString(String value) {
                return Double.parseDouble(value);
            }
        }, //
        BYTE(byte.class) {
            @Override
            Object applyString(String value) {
                return Byte.parseByte(value);
            }
        }, //
        BYTE_WRAPPER(Byte.class) {
            @Override
            Object applyString(String value) {
                return Byte.parseByte(value);
            }
        }, //
        SHORT(short.class) {
            @Override
            Object applyString(String value) {
                return Short.parseShort(value);
            }
        }, //
        SHORT_WRAPPER(Short.class) {
            @Override
            Object applyString(String value) {
                return Short.parseShort(value);
            }
        }, //
        BOOLEAN(boolean.class) {
            @Override
            Object applyString(String value) {
                return Boolean.parseBoolean(value);
            }
        }, //
        BOOLEAN_WRAPPER(Boolean.class) {
            @Override
            Object applyString(String value) {
                return Boolean.parseBoolean(value);
            }
        }, //
        ;
        final Class<?> clz;

        PrimitiveEnum(Class<?> clz) {
            this.clz = clz;
        }

        abstract Object applyString(String value);
    }

    private enum SimpleExplicitType {
        Type1(String.class), //
        Type2(int.class), //
        Type3(Integer.class), //
        Type4(long.class), //
        Type5(Long.class), //
        Type6(char.class), //
        Type7(Character.class), //
        Type8(float.class), //
        Type9(Float.class), //
        TypeA(double.class), //
        TypeB(Double.class), //
        TypeC(byte.class), //
        TypeD(Byte.class), //
        TypeE(short.class), //
        TypeF(Short.class), //
        TypeG(boolean.class), //
        TypeH(Boolean.class), //
        TypeI(StringBuffer.class), //
        TypeJ(StringBuilder.class), //
        TypeK(Date.class) {
            @Override
            protected String toStringZ(Object object) {
                return DateFormatUtils.format((Date) object, "yyyy/MM/dd HH:mm:ss") + this.getClassName(object);
            }
        }, //
        TypeL(Calendar.class) {
            @Override
            protected String toStringZ(Object object) {
                return DateFormatUtils.format((Date) object, "yyyy/MM/dd HH:mm:ss") + this.getClassName(object);
            }
        }, //
        TypeM(Map.class) {
            @Override
            protected String toStringZ(Object object) {
                return object.toString() + this.getClassName(object);
            }
        }, //
        TypeN(Collection.class) {
            @Override
            protected String toStringZ(Object object) {
                return object.toString() + this.getClassName(object);
            }
        }, //
        ;
        final Class<?> clz;

        protected String getClassName(Object object) {
            String clzName = "NA";
            if (object != null) {
                clzName = object.getClass().getSimpleName();
            }
            return "(" + clzName + ")";
        }

        SimpleExplicitType(Class<?> clz) {
            this.clz = clz;
        }

        protected String toStringZ(Object object) {
            return String.valueOf(object) + "(" + object.getClass().getSimpleName() + ")";
        }
    }

    enum TempValueEnum {
        STRING(String.class) {
            @Override
            Object applyString(String value) {
                return value;
            }
        }, //
        INTEGER(Integer.class) {
            @Override
            Object applyString(String value) {
                return Integer.parseInt(value);
            }
        }, //
        LONG(Long.class) {
            @Override
            Object applyString(String value) {
                return Long.parseLong(value);
            }
        }, //
        CHARACTER(Character.class) {
            @Override
            Object applyString(String value) {
                return value.charAt(0);
            }
        }, //
        FLOAT(Float.class) {
            @Override
            Object applyString(String value) {
                return Float.parseFloat(value);
            }
        }, //
        DOUBLE(Double.class) {
            @Override
            Object applyString(String value) {
                return Double.parseDouble(value);
            }
        }, //
        BYTE(Byte.class) {
            @Override
            Object applyString(String value) {
                return Byte.parseByte(value);
            }
        }, //
        SHORT(Short.class) {
            @Override
            Object applyString(String value) {
                return Short.parseShort(value);
            }
        }, //
        BOOLEAN(Boolean.class) {
            @Override
            Object applyString(String value) {
                return Boolean.parseBoolean(value);
            }
        }, //
        STRINGBUFFER(StringBuffer.class) {
            @Override
            Object applyString(String value) {
                return null;
            }
        }, //
        STRINGBUILDER(StringBuilder.class) {
            @Override
            Object applyString(String value) {
                return new StringBuilder(value);
            }
        }, //
        DATE(Date.class) {
            @Override
            Object applyString(String value) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                    return sdf.parse(value);
                } catch (Exception ex) {
                    throw new IllegalArgumentException("日期轉型錯誤:yyyyMMddHHmmss = " + value + ", " + ex.getMessage(), ex);
                }
            }
        }, //
        CALENDAR(Calendar.class) {
            @Override
            Object applyString(String value) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                    Date date = sdf.parse(value);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    return cal;
                } catch (Exception ex) {
                    throw new IllegalArgumentException("日期轉型錯誤:yyyyMMddHHmmss = " + value + ", " + ex.getMessage(), ex);
                }
            }
        }, //
        DATE_TO_LONG(Long.class) {
            @Override
            Object applyString(String value) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                    Date date = sdf.parse(value);
                    return date.getTime();
                } catch (Exception ex) {
                    throw new IllegalArgumentException("日期轉型錯誤:yyyyMMddHHmmss = " + value + ", " + ex.getMessage(), ex);
                }
            }
        }, //
        MAP(Map.class) {
            @Override
            Object applyString(String value) {
                return new LinkedHashMap();
            }
        }, //
        SET(Set.class) {
            @Override
            Object applyString(String value) {
                return new LinkedHashSet();
            }
        }, //
        LIST(List.class) {
            @Override
            Object applyString(String value) {
                return new ArrayList();
            }
        }, //
        CLASS_PATH(Class.class) {
            @Override
            Object applyString(String value) {
                try {
                    return Class.forName(value).newInstance();
                } catch (Exception ex) {
                    throw new IllegalArgumentException("類別路徑錯誤 : " + value + ", " + ex.getMessage(), ex);
                }
            }
        }, //
        ;
        final Class<?> clz;

        TempValueEnum(Class<?> clz) {
            this.clz = clz;
        }

        abstract Object applyString(String value);
    }

    enum ToStringExport {
        PRIMITIVE(
                String.class, //
                Integer.class, Long.class, Character.class, Float.class, Double.class, Byte.class, Short.class,
                Boolean.class,//
                int.class, long.class, char.class, float.class, double.class, byte.class, short.class, boolean.class, //
                StringBuffer.class, StringBuilder.class) {
            @Override
            String applyString(Object value, int tabCount) {
                return getPrefix(value, tabCount) + "\n" + getTab(tabCount) + value;
            }
        }, //
        DATE(Date.class) {
            @Override
            String applyString(Object value, int tabCount) {
                Date date = (Date) value;
                return getPrefix(value, tabCount) + "\n"
                        + DateFormatUtils.format(date.getTime(), "yyyy/MM/dd HH:mm:ss.SSS");
            }
        }, //
        CALENDAR(Calendar.class) {
            @Override
            String applyString(Object value, int tabCount) {
                Calendar date = (Calendar) value;
                return getPrefix(value, tabCount) + "\n" + getTab(tabCount)
                        + DateFormatUtils.format(date.getTimeInMillis(), "yyyy/MM/dd HH:mm:ss.SSS");
            }
        }, //
        TIMPSTAMP(Timestamp.class) {
            @Override
            String applyString(Object value, int tabCount) {
                Timestamp date = (Timestamp) value;
                return getPrefix(value, tabCount) + "\n" + getTab(tabCount)
                        + DateFormatUtils.format(date.getTime(), "yyyy/MM/dd HH:mm:ss.SSS");
            }
        }, //
        DATE_SQL(java.sql.Date.class) {
            @Override
            String applyString(Object value, int tabCount) {//TODO
                java.sql.Date date = (java.sql.Date) value;
                return getPrefix(value, tabCount) + "\n" + getTab(tabCount)
                        + DateFormatUtils.format(date.getTime(), "yyyy/MM/dd HH:mm:ss.SSS");
            }
        }, //
        MAP(Map.class) {
            @Override
            String applyString(Object value, int tabCount) {
                Map map = (Map) value;
                int ii = 0;
                StringBuilder sb = new StringBuilder();
                sb.append(getPrefix(value, tabCount) + "\n");
                for (Object key : map.keySet()) {
                    sb.append(getTab(tabCount) + ii + " -> [key] = " + this.getReflectionStringOrNot(key, tabCount)
                            + "\n");
                    sb.append(getTab(tabCount) + "\t[value] = " + this.getReflectionStringOrNot(map.get(key), tabCount)
                            + "\n");
                    ii++;
                }
                sb.append(getTab(tabCount) + "一共 : " + map.size() + "筆");
                return sb.toString();
            }
        }, //
        LIST(Collection.class) {
            @Override
            String applyString(Object value, int tabCount) {
                Collection coll = (Collection) value;
                int ii = 0;
                StringBuilder sb = new StringBuilder();
                sb.append(getPrefix(value, tabCount) + "\n");
                for (Iterator it = coll.iterator(); it.hasNext();) {
                    sb.append(getTab(tabCount) + ii + " -> " + this.getReflectionStringOrNot(it.next(), tabCount)
                            + "\n");
                    ii++;
                }
                sb.append(getTab(tabCount) + "一共 : " + coll.size() + "筆");
                return sb.toString();
            }
        }, //
        ARRAY(void.class) {
            @Override
            String applyString(Object value, int tabCount) {
                StringBuilder sb = new StringBuilder();
                sb.append(getPrefix(value, tabCount) + "\n");
                for (int ii = 0; ii < Array.getLength(value); ii++) {
                    sb.append(getTab(tabCount) + ii + " -> "
                            + this.getReflectionStringOrNot(Array.get(value, ii), tabCount) + "\n");
                }
                sb.append(getTab(tabCount) + "一共 : " + Array.getLength(value) + "筆");
                return sb.toString();
            }
        }, //
        DEFAULT(void.class) {
            @Override
            String applyString(Object value, int tabCount) {
                return getPrefix(value, tabCount) + "\n" + getTab(tabCount)
                        + this.getReflectionStringOrNot(value, tabCount, true);
            }
        }, //
        ;
        final Class<?>[] clz;

        ToStringExport(Class<?>... clz) {
            this.clz = clz;
        }

        abstract String applyString(Object value, int tabCount);

        String getTab(int tabCount) {
            StringBuilder sb = new StringBuilder();
            for (int jj = 0; jj < tabCount; jj++) {
                sb.append("\t");
            }
            return sb.toString();
        }

        String getPrefix(Object value, int tabCount) {
            return getTab(tabCount) + "Class(" + value.getClass().getCanonicalName()
                    + //
                    ")\n" + getTab(tabCount) + "ToString(" + value + ")\n" + getTab(tabCount) + "HashCode("
                    + value.hashCode() + ")\n";
        }

        String getReflectionStringOrNot(Object value, int tabCount) {
            return getReflectionStringOrNot(value, tabCount, false);
        }

        String getReflectionStringOrNot(Object value, int tabCount, boolean multiStyle) {
            if (value == null) {
                return "<null>";
            }
            for (Class<?> c : ToStringExport.PRIMITIVE.clz) {
                if (c.isAssignableFrom(value.getClass())) {
                    return String.valueOf(value) + "(" + value.getClass().getSimpleName() + ")";
                }
            }
            // 地回處理-------------------------------------------------------------------------
            if (Map.class.isAssignableFrom(value.getClass())) {
                return ToStringExport.MAP.applyString(value, ++tabCount);
            }
            if (Collection.class.isAssignableFrom(value.getClass())) {
                return ToStringExport.LIST.applyString(value, ++tabCount);
            }
            if (value.getClass().isArray()) {
                return ToStringExport.ARRAY.applyString(value, ++tabCount);
            }
            // ------------------------------------------------------------------------------
            ToStringStyle style = ToStringStyle.SHORT_PREFIX_STYLE;
            if (multiStyle) {
                style = ToStringStyle.MULTI_LINE_STYLE;
            }
            return ReflectionToStringBuilder.toString(value, style);
        }
    }
    
    
    
    public static String dateToString(Object object){
        if(object == null){
            return "null";
        }
        if(object instanceof Date){
            Date date = (Date)object;
            return DateFormatUtils.format(date.getTime(), "yyyy/MM/dd HH:mm:ss.SSS");
        }else if(object instanceof java.sql.Date){
            java.sql.Date date = (java.sql.Date)object;
            return DateFormatUtils.format(date.getTime(), "yyyy/MM/dd HH:mm:ss.SSS");
        }else if(object instanceof Calendar){
            Calendar date = (Calendar)object;
            return DateFormatUtils.format(date.getTimeInMillis(), "yyyy/MM/dd HH:mm:ss.SSS");
        }else if(object instanceof Timestamp){
            Timestamp date = (Timestamp)object;
            return DateFormatUtils.format(date.getTime(), "yyyy/MM/dd HH:mm:ss.SSS");
        }
        throw new RuntimeException("未知日期型態 :" + object);
    }
}
