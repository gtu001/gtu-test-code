/*
 * @(#)DateUtil.java 1.00 07/10/16
 * 
 * Copyright (c) 2007 Universal EC, Inc. All rights reserved.
 * 
 * This class provides the commond utility of date.
 */
package gtu.date;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;

//import org.junit.Test;

/**
 * This class provides the commond utility of date.
 * 
 * @version 1.00 16 Oct 2007
 * @author EFT Dept. of UEC.
 * 
 *         2009/02/02
 */
public class DateUtil {

    public static void main(String[] args) {
        System.out.println(DateUtil.getTaiwanDate());
    }

    // @Test
    public void testDateSpecialChar() {
        final SimpleDateFormat format = new SimpleDateFormat("'TX'yyyyMMddHHmmssSSS");
        System.out.println(format.format(new Date()));
    }

    // @Test
    public void testGetDisplayName() {
        Calendar now = Calendar.getInstance();
        Locale locale = Locale.getDefault();

        Map<String, Integer> names1 = now.getDisplayNames(Calendar.ERA, Calendar.LONG, locale);
        Map<String, Integer> names2 = now.getDisplayNames(Calendar.MONTH, Calendar.LONG, locale);
        Map<String, Integer> names3 = now.getDisplayNames(Calendar.DAY_OF_WEEK, Calendar.LONG, locale);
        Map<String, Integer> names4 = now.getDisplayNames(Calendar.AM_PM, Calendar.LONG, locale);

        System.out.println(names1);
        System.out.println(names2);
        System.out.println(names3);
        System.out.println(names4);
    }

    /**
     * parse 系統預設format
     */
    public static Date parseDateString(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException("parse failed : " + dateString, e);
        }
    }

    public static String getTaiwanDate() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String yyyyMMdd = format.format(date);
        String yyy = StringUtils.leftPad(String.valueOf(Integer.parseInt(yyyyMMdd.substring(0, 4)) - 1911), 3, '0');
        return yyy + yyyyMMdd.substring(4);
    }

    /**
     * 取得工作日區間 : 去除(六日)
     * 
     * @param amount
     * @param beginDate
     * @return
     */
    public static Date getBetweenWorkDate(int amount, Date beginDate) {
        int addDate = 0;
        for (int ii = 1; ii <= amount; ii++) {
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(beginDate);
            cal2.add(Calendar.DATE, ii);
            int week = cal2.get(Calendar.DAY_OF_WEEK);
            if (week != 1 && week != 7) {
                addDate++;
            }
        }
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(beginDate);
        cal2.add(Calendar.DATE, addDate);
        return cal2.getTime();
    }

    /**
     * 範例 : 取得執行過程所耗時間
     */
    public void countProcessTime() {
        long before = new Date().getTime();
        // 處理過程
        long after = new Date().getTime();
        System.out.println(after - before); // 執行毫秒數
    }

    /**
     * 取得當下日期時間 yyyyMMddHHmmss
     * 
     * @return
     */
    public static String getCurrentDateTime() {
        Date d = new Date();
        SimpleDateFormat df = new SimpleDateFormat();
        df.applyPattern("yyyy/MM/dd HH:mm:ss");
        return df.format(d);
    }

    /**
     * 取得當下日期時間 yyyyMMddHHmmss
     * 
     * @return
     */
    public static String getCurrentDateTime(boolean WithDelimeter) {
        if (WithDelimeter)
            return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    /**
     * 取得當下日期 yyyyMMdd
     * 
     * @return
     */
    public static String getCurrentDate(boolean WithDelimeter) {
        if (WithDelimeter)
            return new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        return new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    /**
     * 取得當下日期(民國) yyyMMdd
     * 
     * @return
     */
    public static String getTwCurrentDate(boolean WithDelimeter) {
        String def = "yyyyMMdd";
        if (WithDelimeter)
            def = "yyyy/MM/dd";
        String str = new SimpleDateFormat(def).format(new Date());
        return String.valueOf(Integer.parseInt(str.substring(0, 4)) - 1911) + str.substring(4);
    }

    /**
     * 取得該年月的天數
     * 
     * @return
     */
    public static int getMonthHaveDays(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        return cal.getActualMaximum(Calendar.DATE);
    }

    /**
     * 取得yyyyMMdd為星期幾
     */
    public static String getDayOfTheWeek(String yyyyMMdd) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date date = sdf.parse(yyyyMMdd);
            return getDayOfTheWeek(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 取得星期幾
     */
    public static String getDayOfTheWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return getDayOfTheWeek(cal);
    }

    /**
     * 取得星期幾
     */
    public static String getDayOfTheWeek(Calendar cal) {
        int val = cal.get(Calendar.DAY_OF_WEEK);
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(1, "日");
        map.put(2, "一");
        map.put(3, "二");
        map.put(4, "三");
        map.put(5, "四");
        map.put(6, "五");
        map.put(7, "六");
        return map.get(val);
    }

    /**
     * @param deli
     *            : "/" => 2007/03/18
     * @return Ex: true = 20070318 ; false = 0960318
     */
    public static String getToday(boolean ceOrTw, String deli) {
        if (deli == null)
            deli = "";
        Calendar cal = Calendar.getInstance();
        int WTYear = (cal.get(Calendar.YEAR));
        String WTYearS = new String();
        if (ceOrTw) {
            WTYearS = fillZero(String.valueOf(WTYear), 4);
        } else {
            WTYearS = fillZero(String.valueOf(WTYear - 1911), 3);
        }
        int Month = cal.get(Calendar.MONTH) + 1;
        String MonthS = fillZero(String.valueOf(Month), 2);
        int DAY = cal.get(Calendar.DAY_OF_MONTH);
        String DAYS = fillZero(String.valueOf(DAY), 2);
        String WTDate = WTYearS + deli + MonthS + deli + DAYS;
        return WTDate;
    }

    /**
     * @param tmpTime
     *            2008042112300
     * @return "2008-04-21 12:30:00.0"
     */
    public static String getDBTime(String yyyyMMddHHmmss) {
        String d = "";
        try {
            SimpleDateFormat temp = new SimpleDateFormat("yyyyMMddHHmmss");
            d = new java.sql.Timestamp(temp.parse(yyyyMMddHHmmss).getTime()).toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return d;
    }

    /**
     * 將傳入的日期格式從sSourceFormat轉成sDestinationFormat ex:
     * parseDate("20070425102520","yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss"){
     * return "2007/04/25 10:25:20" 用於queryDb()
     */
    public static String parseDate(String sDate, String sSourceFormat, String sDestinationFormat) {
        String sRtn = "";
        try {
            sRtn = new java.text.SimpleDateFormat(sDestinationFormat).format(new java.text.SimpleDateFormat(sSourceFormat).parse(sDate));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sRtn;
    }

    /**
     * @param 將日期字串yyyyMMddHHmmss
     *            --> long型態
     */
    public static long getMillins(String yyyyMMddHHmmss) {
        long d = 0;
        try {
            java.util.Date date = new java.util.Date();
            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMddhhmmss");
            date = formatter.parse(yyyyMMddHHmmss);
            java.util.Calendar c = java.util.Calendar.getInstance();
            c.setTime(date);
            d = c.getTimeInMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * @param 將日期long型態
     *            --> 字串yyyyMMddHHmmss
     */
    public static String getMillins(long yyyyMMddHHmmss) {
        String d = new String();
        try {
            java.util.Date date = new java.util.Date();
            date.setTime(yyyyMMddHHmmss);
            d = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * 增減日期時間 yyyyMMddHHmmss
     */
    public static String dateOperation(String yyyyMMddHHmmss, int day, int hour, int min, int sec) {
        String rtn = new String();
        long date = day * 24 * 60 * 60 * 1000 + hour * 60 * 60 * 1000 + min * 60 * 1000 + sec * 1000;
        rtn = getMillins(getMillins(yyyyMMddHHmmss) + date);
        return rtn;
    }

    /**
     * 計算兩個日期時間間隔
     * 
     * @return 天,時,分,秒,豪秒
     */
    public static String compareDate(String yyyyMMddHHmmss1, String yyyyMMddHHmmss2) {
        long d1 = getMillins(yyyyMMddHHmmss1);
        long d2 = getMillins(yyyyMMddHHmmss2);
        long tmp = (d1 > d2) ? (d1 - d2) : (d2 - d1);
        int day = 0, hour = 0, min = 0, sec = 0;
        for (; tmp >= 24 * 60 * 60 * 1000; tmp -= 24 * 60 * 60 * 1000, day++)
            ;
        for (; tmp >= 60 * 60 * 1000; tmp -= 60 * 60 * 1000, hour++)
            ;
        for (; tmp >= 60 * 1000; tmp -= 60 * 1000, min++)
            ;
        for (; tmp >= 60; tmp -= 60, sec++)
            ;
        return String.valueOf(day) + "," + String.valueOf(hour) + "," + String.valueOf(min) + "," + String.valueOf(sec) + "," + String.valueOf(tmp);
    }

    /**
     * 計算兩個日期時間間隔
     * 
     * @return 天,時,分,秒,豪秒
     */
    public static String compareDate(Date date1, Date date2) {
        if (date1 == null || date2 == null)
            return null;
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date1);
        long d1 = c.getTimeInMillis();
        c.setTime(date2);
        long d2 = c.getTimeInMillis();
        long tmp = (d1 > d2) ? (d1 - d2) : (d2 - d1);
        int day = 0, hour = 0, min = 0, sec = 0;
        for (; tmp >= 24 * 60 * 60 * 1000; tmp -= 24 * 60 * 60 * 1000, day++)
            ;
        for (; tmp >= 60 * 60 * 1000; tmp -= 60 * 60 * 1000, hour++)
            ;
        for (; tmp >= 60 * 1000; tmp -= 60 * 1000, min++)
            ;
        for (; tmp >= 60; tmp -= 60, sec++)
            ;
        return String.valueOf(day) + "," + String.valueOf(hour) + "," + String.valueOf(min) + "," + String.valueOf(sec) + "," + String.valueOf(tmp);
    }

    /**
     * 取得兩日期相差天數
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static int compareDate_getDiffDays(Date date1, Date date2) {
        long d3 = Math.abs(date1.getTime() - date2.getTime());
        int result = (int) (d3 / (24 * 60 * 60 * 1000));
        return result;
    }

    private static String fillZero(String str, int len) {
        if (str == null)
            return "";
        len = len - str.length();
        if (len < 0)
            return str;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            sb.append('0');
        }
        return sb.toString() + str;
    }

    /**
     * @param 將日期字串yyyyMMddHHmmss
     *            --> Date型態
     */
    public static Date getFormatToDate(String yyyyMMddHHmmss) {
        java.util.Date date = new java.util.Date();
        try {
            yyyyMMddHHmmss = yyyyMMddHHmmss.replaceAll("/", "").replaceAll(":", "");
            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMddhhmmss");
            date = formatter.parse(yyyyMMddHHmmss);
        } catch (Exception e) {
            return null;
        }
        return date;
    }

    /**
     * @param 將日期字串yyyyMMdd
     *            --> Date型態
     */
    public static Date getFormatToDateSimple(String yyyyMMdd) {
        java.util.Date date = new java.util.Date();
        try {
            yyyyMMdd = yyyyMMdd.replaceAll("/", "").replaceAll(":", "");
            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMdd");
            date = formatter.parse(yyyyMMdd);
        } catch (Exception e) {
            return null;
        }
        return date;
    }

    /**
     * 增減日期時間 yyyyMMddHHmmss
     */
    public static Date dateOperation(Date yyyyMMddHHmmss, int day, int hour, int min, int sec) {
        String rtn = new String();
        long date = day * 24 * 60 * 60 * 1000 + hour * 60 * 60 * 1000 + min * 60 * 1000 + sec * 1000;
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(yyyyMMddHHmmss);
        long d = c.getTimeInMillis();
        d = d + date;
        c.setTimeInMillis(d);
        return c.getTime();
    }

    /**
     * 計算所耗時間
     * 
     * @param wasteTotal
     * @return
     */
    public static String wasteTotalTime(long wasteTotal) {
        wasteTotal = wasteTotal / 1000;
        long sec_ = wasteTotal % 60;
        wasteTotal = wasteTotal / 60;
        long min = wasteTotal % 60;
        wasteTotal = wasteTotal / 60;
        long hours = wasteTotal % 24;
        wasteTotal = wasteTotal / 24;
        return wasteTotal + "天" + hours + "時" + min + "分" + sec_ + "秒";
    }

    /**
     * 計算年齡
     * 
     * @param birthday
     *            出生日期
     * @param calculateDay
     *            計算日期
     * @return 歲數
     */
    public static final int calculationAge(Date birthday, Date calculateDay) {
        @SuppressWarnings("unused")
        int day = 1, month = 0, year = 1, ageYears, ageMonths, ageDays;
        Calendar calculate = Calendar.getInstance();
        calculate.setTime(calculateDay);
        Calendar birth = Calendar.getInstance();
        birth.setTime(birthday);

        try {
            year = birth.get(Calendar.YEAR);
            if (year > calculate.get(Calendar.YEAR)) {
                System.out.println("Invalid year");
            }
            month = birth.get(Calendar.MONTH);
            if (month < 0 || month > 11) {
                System.out.println("month between 1 to 12.");
                return 0;
            } else {
                // month--;
                if (year == calculate.get(Calendar.YEAR)) {
                    if (month > calculate.get(Calendar.MONTH)) {
                        System.out.println("Invalid month");
                        return 0;
                    }
                }
            }
            day = birth.get(Calendar.DATE);
            if (month == 0 || month == 2 || month == 4 || month == 6 || month == 7 || month == 9 || month == 11) {
                if (day > 31 || day < 1) {
                    System.out.println("day between 1 to 31");
                    return 0;
                }
            } else if (month == 3 || month == 5 || month == 8 || month == 10) {
                if (day > 30 || day < 1) {
                    System.out.println("day between 1 to 30");
                    return 0;
                }
            } else {
                if (new GregorianCalendar().isLeapYear(year)) {
                    if (day < 1 || day > 29) {
                        System.out.println("day between 1 to 29");
                        return 0;
                    }
                } else if (day < 1 || day > 28) {
                    System.out.println("day between 1 to 28.");
                    return 0;
                }
            }
            if (year == calculate.get(Calendar.YEAR)) {
                if (month == calculate.get(Calendar.MONTH)) {
                    if (day > calculate.get(Calendar.DAY_OF_MONTH)) {
                        System.out.println("Invalid date");
                        return 0;
                    }
                }
            }
        } catch (NumberFormatException ne) {
            System.out.println(ne.getMessage() + " is not a legal entry");
            return 0;
        }

        Calendar bd = new GregorianCalendar(year, month, day);
        ageYears = calculate.get(Calendar.YEAR) - bd.get(Calendar.YEAR);
        if (calculate.before(new GregorianCalendar(calculate.get(Calendar.YEAR), month, day))) {
            ageYears--;
            ageMonths = (12 - (bd.get(Calendar.MONTH) + 1)) + (bd.get(Calendar.MONTH));
            if (day > calculate.get(Calendar.DAY_OF_MONTH)) {
                ageDays = day - calculate.get(Calendar.DAY_OF_MONTH);
            } else if (day < calculate.get(Calendar.DAY_OF_MONTH)) {
                ageDays = calculate.get(Calendar.DAY_OF_MONTH) - day;
            } else {
                ageDays = 0;
            }
        } else if (calculate.after(new GregorianCalendar(calculate.get(Calendar.YEAR), month, day))) {
            ageMonths = (calculate.get(Calendar.MONTH) - (bd.get(Calendar.MONTH)));
            if (day > calculate.get(Calendar.DAY_OF_MONTH))
                ageDays = day - calculate.get(Calendar.DAY_OF_MONTH) - day;
            else if (day < calculate.get(Calendar.DAY_OF_MONTH)) {
                ageDays = calculate.get(Calendar.DAY_OF_MONTH) - day;
            } else
                ageDays = 0;
        } else {
            ageYears = calculate.get(Calendar.YEAR) - bd.get(Calendar.YEAR);
            ageMonths = 0;
            ageDays = 0;
        }

        return ageYears;
    }

    /**
     * 取得Timestamp
     * 
     * @param Str
     * @return
     */
    public static Timestamp getTimestamp(String Str) {
        Timestamp time = null;
        try {
            time = java.sql.Timestamp.valueOf(Str);
        } catch (Exception e) {
            System.out.println("getTimestamp error:" + e);
        }
        return time;
    }

    private void testTimezone() {
        Calendar calendar = new GregorianCalendar();
        TimeZone timeZone = calendar.getTimeZone();
        System.out.println(timeZone);
        // Asia/Taipei
        TimeZone timeZone2 = TimeZone.getTimeZone("Asia/Taipei");
        System.out.println(timeZone2);
        // -Duser.timezone=Asia/Taipei
        // -Duser.timezone=GMT+8
        //Jboss 的 standalone.conf 加入 JAVA_OPTS="$JAVA_OPTS -Duser.timezone=GMT+8"
    }

    /**
     * 取得某日為當年的第幾天
     * 
     * @param date
     * @return
     */
    public static int getDaysOfTheYear(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("DDD");
        return Integer.parseInt(sdf.format(date));
    }

    // 太陽日
    public Date getSunSolarDay(String yyyy, int dayOfMonth) throws ParseException {
        int year = Integer.parseInt(yyyy);
        int monthAndDay = dayOfMonth - 1;
        Calendar calendar = new GregorianCalendar(year, 00, 01);
        calendar.add(Calendar.DAY_OF_MONTH, monthAndDay);
        return calendar.getTime();
    }

    public static XMLGregorianCalendar getXmlNow() throws DatatypeConfigurationException {
        XMLGregorianCalendar cal = DatatypeFactory.newInstance().newXMLGregorianCalendar();
        Calendar date = Calendar.getInstance();
        cal.setYear(date.get(Calendar.YEAR));
        cal.setMonth(date.get(Calendar.MONTH) + 1);
        cal.setDay(date.get(Calendar.DATE));
        cal.setTime(date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE), date.get(Calendar.SECOND));
        return cal;
    }
}
