package gtu.date;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang.time.DateFormatUtils;

/**
 * 日期時間的格式化工具類別。<br />
 * 提供了常用的日期時間的格式化方法，與Getech中常用的格式化樣式(pattern)。<br />
 * 這個類別是org.apache.commons.lang.time.DateFormatUtils類別的擴充，包含了所有
 * DateFormatUtils所提供的方法，另外加入了一些原類別未提供，但常用的方法。
 * 
 * DateFormatUtil提供的樣式分為5大類，每一類有可分為只顯示日期、顯示日期時間(至秒)與
 * 顯示日期時間(至毫秒)等3小類，列表下(其他沒有提供的在呼叫方法時傳入)：<br />
 * <table border = "1">
 * <tr>
 * <th>樣式類別</th>
 * <th>顯示單位</th>
 * <th>常數定義</th>
 * <th>樣式表示式</th>
 * <th>範例</th>
 * </tr>
 * <tr>
 * <td rowspan="3">一般(最常用)</td>
 * <td>日期</td>
 * <td>PATTERN_GENERAL_DATE</td>
 * <td>"yyyy/MM/dd"</td>
 * <td>"2005/12/25"</td>
 * </tr>
 * <tr>
 * <td>秒</td>
 * <td>PATTERN_GENERAL_DATE_TIME</td>
 * <td>"yyyy/MM/dd HH:mm:ss"</td>
 * <td>"2005/12/25 18:10:15"</td>
 * </tr>
 * <tr>
 * <td>毫秒</td>
 * <td>PATTERN_GENERAL_DATE_TIME_MILLI</td>
 * <td>"yyyy/MM/dd HH:mm:ss.SSS"</td>
 * <td>"2005/12/25 18:10:15.000"</td>
 * </tr>
 * <tr>
 * <td rowspan="3">連續(無多餘符號)</td>
 * <td>日期</td>
 * <td>PATTERN_CONTINUOUS_DATE</td>
 * <td>"yyyyMMdd"</td>
 * <td>"20051225"</td>
 * </tr>
 * <tr>
 * <td>秒</td>
 * <td>PATTERN_CONTINUOUS_DATE_TIME</td>
 * <td>"yyyyMMddHHmmss"</td>
 * <td>"20051225181015"</td>
 * </tr>
 * <tr>
 * <td>毫秒</td>
 * <td>PATTERN_CONTINUOUS_DATE_TIME_MILLI</td>
 * <td>"yyyyMMddHHmmssSSS"</td>
 * <td>"20051225181015000"</td>
 * </tr>
 * <tr>
 * <td rowspan="3">SQL(資料庫表示法)</td>
 * <td>日期</td>
 * <td>PATTERN_SQL_DATE</td>
 * <td>"yyyy-MM-dd"</td>
 * <td>"2005-12-25"</td>
 * </tr>
 * <tr>
 * <td>秒</td>
 * <td>PATTERN_SQL_DATE_TIME</td>
 * <td>"yyyy-MM-dd HH:mm:ss"</td>
 * <td>"2005-12-25 18:10:15"</td>
 * </tr>
 * <tr>
 * <td>毫秒</td>
 * <td>PATTERN_SQL_DATE_TIME_MILLI</td>
 * <td>"yyyy-MM-dd HH:mm:ss.SSS"</td>
 * <td>"2005-12-25 18:10:15.000"</td>
 * </tr>
 * <tr>
 * <td rowspan="3">ISO 8601定義</td>
 * <td>日期</td>
 * <td>PATTERN_ISO_DATE</td>
 * <td>"yyyy-MM-ddZZ"</td>
 * <td>"2005-12-25+08:00"</td>
 * </tr>
 * <tr>
 * <td>秒</td>
 * <td>PATTERN_ISO_DATE_TIME</td>
 * <td>"yyyy-MM-dd'T'HH:mm:ssZZ"</td>
 * <td>"2005-12-25T18:10:15+08:00"</td>
 * </tr>
 * <tr>
 * <td>毫秒</td>
 * <td>PATTERN_ISO_DATE_TIME_MILLI</td>
 * <td>"yyyy-MM-dd'T'HH:mm:ss.SSSZZ"</td>
 * <td>"2005-12-25T18:10:15.000+08:00"</td>
 * </tr>
 * </table>
 * 
 * @author Sam Ho 2009/02/02
 */
public class DateFormatUtil {

    /**
     * "yyyy/MM/dd"的格式化樣式，如"2005/12/25"。
     */
    public static final String PATTERN_GENERAL_DATE = "yyyy/MM/dd";

    /**
     * "yyyy/MM/dd HH:mm:ss"的格式化樣式，如"2005/12/25 18:10:15"。
     */
    public static final String PATTERN_GENERAL_DATE_TIME = "yyyy/MM/dd HH:mm:ss";

    /**
     * "yyyy/MM/dd HH:mm:ss.SSS"的格式化樣式，如"2005/12/25 18:10:15.000"。
     */
    public static final String PATTERN_GENERAL_DATE_TIME_MILLI = "yyyy/MM/dd HH:mm:ss.SSS";

    /**
     * "yyyyMMdd"的格式化樣式，如"20051225"。
     */
    public static final String PATTERN_CONTINUOUS_DATE = "yyyyMMdd";

    /**
     * "yyyyMMddHHmmss"的格式化樣式，如"20051225181015"。
     */
    public static final String PATTERN_CONTINUOUS_DATE_TIME = "yyyyMMddHHmmss";

    /**
     * "yyyyMMddHHmmssSSS"的格式化樣式，如"20051225181015000"。
     */
    public static final String PATTERN_CONTINUOUS_DATE_TIME_MILLI = "yyyyMMddHHmmssSSS";

    /**
     * "yyyy-MM-dd"的格式化樣式，如"2005-12-25"。
     */
    public static final String PATTERN_SQL_DATE = "yyyy-MM-dd";

    /**
     * "yyyy-MM-dd HH:mm:ss"的格式化樣式，如"2005-12-25 18:10:15"。
     */
    public static final String PATTERN_SQL_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    /**
     * "yyyy-MM-dd HH:mm:ss.SSS"的格式化樣式，如"2005-12-25 18:10:15.000"。
     */
    public static final String PATTERN_SQL_DATE_TIME_MILLI = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * "yyyy-MM-ddZZ"的格式化樣式，如"2005-12-25+08:00"。
     */
    public static final String PATTERN_ISO_DATE = DateFormatUtils.ISO_DATE_TIME_ZONE_FORMAT.getPattern();

    /**
     * "yyyy-MM-dd'T'HH:mm:ssZZ"的格式化樣式，如"2005-12-25T18:10:15+08:00"。
     */
    public static final String PATTERN_ISO_DATE_TIME = DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.getPattern();

    /**
     * "yyyy-MM-dd'T'HH:mm:ss.SSSZZ"的格式化樣式， 如"2005-12-25T18:10:15.000+08:00"。
     */
    public static final String PATTERN_ISO_DATE_TIME_MILLI = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";

    /**
     * 私有建構子，目的在防止別人new此類別物件。
     */
    private DateFormatUtil() {
        super();
    }

    /**
     * 根據指定的樣式，將java.util.Date格式化成字串。<br />
     * 與org.apache.commons.lang.time.DateFormatUtils的同名方法的不同之處， 在當date ==
     * null的時候，會回傳空白字串""。
     * 
     * @param date
     *            欲被格式化的日期。
     * @param pattern
     *            指定的格式化樣式。
     * @return 格式化後的日期字串。
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return "";
        } else {
            return DateFormatUtils.format(date, pattern);
        }
    }

    /**
     * 根據指定的樣式與地區，將java.util.Date格式化成字串。<br />
     * 與org.apache.commons.lang.time.DateFormatUtils的同名方法的不同之處， 在當date ==
     * null的時候，會回傳空白字串""。
     * 
     * @param date
     *            欲被格式化的日期。
     * @param pattern
     *            指定的格式化樣式。
     * @param locale
     *            指定的地區，可以為null。
     * @return 格式化後的日期字串。
     */
    public static String format(Date date, String pattern, Locale locale) {
        if (date == null) {
            return "";
        } else {
            return DateFormatUtils.format(date, pattern, locale);
        }
    }

    /**
     * 根據指定的樣式與時區，將java.util.Date格式化成字串。<br />
     * 與org.apache.commons.lang.time.DateFormatUtils的同名方法的不同之處， 在當date ==
     * null的時候，會回傳空白字串""。
     * 
     * @param date
     *            欲被格式化的日期。
     * @param pattern
     *            指定的格式化樣式。
     * @param timeZone
     *            指定的時區，可以為null。
     * @return 格式化後的日期字串。
     */
    public static String format(Date date, String pattern, TimeZone timeZone) {
        if (date == null) {
            return "";
        } else {
            return DateFormatUtils.format(date, pattern, timeZone);
        }
    }

    /**
     * 根據指定的樣式、地區與時區，將java.util.Date格式化成字串。<br />
     * 與org.apache.commons.lang.time.DateFormatUtils的同名方法的不同之處， 在當date ==
     * null的時候，會回傳空白字串""。
     * 
     * @param date
     *            欲被格式化的日期。
     * @param pattern
     *            指定的格式化樣式。
     * @param timeZone
     *            指定的時區，可以為null。
     * @param locale
     *            指定的地區，可以為null。
     * @return 格式化後的日期字串。
     */
    public static String format(Date date, String pattern, TimeZone timeZone, Locale locale) {
        if (date == null) {
            return "";
        } else {
            return DateFormatUtils.format(date, pattern, timeZone, locale);
        }
    }

    /**
     * 根據指定的樣式，將時間毫秒值格式化成字串。
     * 
     * @param millis
     *            the date to format expressed in milliseconds.
     * @param pattern
     *            指定的格式化樣式。
     * @return 格式化後的日期字串。
     */
    public static String format(long millis, String pattern) {
        return DateFormatUtils.format(millis, pattern);
    }

    /**
     * 根據指定的樣式與地區，將時間毫秒值格式化成字串。
     * 
     * @param millis
     *            the date to format expressed in milliseconds.
     * @param pattern
     *            指定的格式化樣式。
     * @param locale
     *            指定的地區，可以為null。
     * @return 格式化後的日期字串。
     */
    public static String format(long millis, String pattern, Locale locale) {
        return DateFormatUtils.format(millis, pattern, locale);
    }

    /**
     * 根據指定的樣式與時區，將時間毫秒值格式化成字串。
     * 
     * @param millis
     *            the time expressed in milliseconds
     * @param pattern
     *            指定的格式化樣式。
     * @param timeZone
     *            指定的時區，可以為null。
     * @return the formatted date
     */
    public static String format(long millis, String pattern, TimeZone timeZone) {
        return DateFormatUtils.format(millis, pattern, timeZone);
    }

    /**
     * F根據指定的樣式、時區與地區，將時間毫秒值格式化成字串。
     * 
     * @param millis
     *            the date to format expressed in milliseconds.
     * @param pattern
     *            指定的格式化樣式。
     * @param timeZone
     *            指定的時區，可以為null。
     * @param locale
     *            指定的地區，可以為null。
     * @return 格式化後的日期字串。
     */
    public static String format(long millis, String pattern, TimeZone timeZone, Locale locale) {
        return DateFormatUtils.format(millis, pattern, timeZone, locale);
    }

    /**
     * 根據指定的樣式，將java.util.Date先轉換成國際標準時區，再格式化成字串。<br />
     * 國際標準時區( [UTC] Coordinated Universal Time)就是 (格林威治時間 [GMT] Greenwich Mean
     * Time)< br />
     * 與org.apache.commons.lang.time.DateFormatUtils的同名方法的不同之處， 在當date ==
     * null的時候，會回傳空白字串""。
     * 
     * @param date
     *            欲被格式化的日期。
     * @param pattern
     *            指定的格式化樣式。
     * @return 格式化後的日期字串。
     */
    public static String formatUTC(Date date, String pattern) {
        if (date == null) {
            return "";
        } else {
            return DateFormatUtils.formatUTC(date, pattern);
        }
    }

    /**
     * 根據指定的樣式與地區，將java.util.Date先轉換成國際標準時區， 再格式化成字串。<br />
     * 國際標準時區( [UTC] Coordinated Universal Time)就是 (格林威治時間 [GMT] Greenwich Mean
     * Time)< br />
     * 與org.apache.commons.lang.time.DateFormatUtils的同名方法的不同之處， 在當date ==
     * null的時候，會回傳空白字串""。
     * 
     * @param date
     *            欲被格式化的日期。
     * @param pattern
     *            指定的格式化樣式。
     * @param locale
     *            指定的地區，可以為null。
     * @return 格式化後的日期字串。
     */
    public static String formatUTC(Date date, String pattern, Locale locale) {
        if (date == null) {
            return "";
        } else {
            return DateFormatUtils.formatUTC(date, pattern, locale);
        }
    }

    /**
     * 根據指定的樣式，將時間毫秒值格式化成字串。<br />
     * 國際標準時區( [UTC] Coordinated Universal Time)就是 (格林威治時間 [GMT] Greenwich Mean
     * Time)< br />
     * 
     * @param millis
     *            the date to format expressed in milliseconds.
     * @param pattern
     *            指定的格式化樣式。
     * @return 格式化後的日期字串。
     */
    public static String formatUTC(long millis, String pattern) {
        return DateFormatUtils.formatUTC(millis, pattern);
    }

    /**
     * 根據指定的樣式與地區，將時間毫秒值格式化成字串。<br />
     * 國際標準時區( [UTC] Coordinated Universal Time)就是 (格林威治時間 [GMT] Greenwich Mean
     * Time)< br />
     * 
     * @param millis
     *            the date to format expressed in milliseconds.
     * @param pattern
     *            指定的格式化樣式。
     * @param locale
     *            指定的地區，可以為null。
     * @return 格式化後的日期字串。
     */
    public static String formatUTC(long millis, String pattern, Locale locale) {
        return DateFormatUtils.formatUTC(millis, pattern, locale);
    }

    /**
     * 根據指定的格式化樣式，將日期表示字串剖析成java.util.Date物件。<br />
     * 若指定的格式化樣式無法成功剖析此字串，會拋出ParseException。
     * 
     * @param str
     *            欲被剖析的日期表示字串，不得為null。
     * @param parsePattern
     *            指定的格式化樣式，不得為null。
     * @return 剖析成功後的日期物件。
     * @throws IllegalArgumentException
     *             若str或parsePattern為null時。
     * @throws ParseException
     *             若剖析失敗時。
     */
    public static Date parseDate(String str, String parsePattern) throws ParseException {
        String[] parsePatterns = null;

        if (str == null || parsePattern == null) {
            throw new IllegalArgumentException("Date and Pattern must not be null");
        }

        parsePatterns = new String[] { parsePattern };

        return parseDate(str, parsePatterns);
    }

    /**
     * 根據指定的(多個)格式化樣式，將日期表示字串剖析成java.util.Date物件。<br />
     * 若指定的所有的格式化樣式皆無法成功剖析此字串，會拋出ParseException。
     * 
     * @param str
     *            欲被剖析的日期表示字串，不得為null。
     * @param parsePatterns
     *            指定的格式化樣式陣列，不得為null。
     * @return 剖析成功後的日期物件。
     * @throws IllegalArgumentException
     *             若str或parsePatterns為null時。
     * @throws ParseException
     *             若剖析失敗時。
     */
    public static Date parseDate(String str, String[] parsePatterns) throws ParseException {
        if (str == null || parsePatterns == null) {
            throw new IllegalArgumentException("Date and Patterns must not be null");
        }

        SimpleDateFormat parser = null;
        ParsePosition pos = new ParsePosition(0);
        for (int i = 0; i < parsePatterns.length; i++) {
            if (i == 0) {
                parser = new SimpleDateFormat(parsePatterns[0]);
            } else {
                parser.applyPattern(parsePatterns[i]);
            }
            pos.setIndex(0);
            parser.parse(str);
            Date date = parser.parse(str, pos);
            if (date != null && pos.getIndex() == str.length()) {
                return date;
            }
        }
        throw new ParseException("Unable to parse the date: " + str, -1);
    }
}
