package gtu.string;

import org.apache.commons.lang.StringUtils;

public class StringNumberUtil {

    public static int parseInt(String value, int defaultVal) {
        try {
            return Integer.parseInt(StringUtils.trimToEmpty(value));
        } catch (Exception ex) {
            return defaultVal;
        }
    }

    public static float parseFloat(String value, float defaultVal) {
        try {
            return Float.parseFloat(StringUtils.trimToEmpty(value));
        } catch (Exception ex) {
            return defaultVal;
        }
    }

    public static double parseDouble(String value, double defaultVal) {
        try {
            return Double.parseDouble(StringUtils.trimToEmpty(value));
        } catch (Exception ex) {
            return defaultVal;
        }
    }

    public static long parseLong(String value, long defaultVal) {
        try {
            return Long.parseLong(StringUtils.trimToEmpty(value));
        } catch (Exception ex) {
            return defaultVal;
        }
    }

    public static short parseShort(String value, short defaultVal) {
        try {
            return Short.parseShort(StringUtils.trimToEmpty(value));
        } catch (Exception ex) {
            return defaultVal;
        }
    }

    public static boolean parseBoolean(String value, boolean defaultVal) {
        try {
            return Boolean.parseBoolean(StringUtils.trimToEmpty(value));
        } catch (Exception ex) {
            return defaultVal;
        }
    }

    public static byte parseByte(String value, byte defaultVal) {
        try {
            return Byte.parseByte(StringUtils.trimToEmpty(value));
        } catch (Exception ex) {
            return defaultVal;
        }
    }
}
