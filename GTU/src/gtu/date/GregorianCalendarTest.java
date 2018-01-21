package gtu.date;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.time.DateFormatUtils;

public class GregorianCalendarTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
    }

    private void test001() {
        // Calendar.YEAR——年份
        // Calendar.MONTH——月份
        // Calendar.DATE——日期
        // Calendar.DAY_OF_MONTH——日期，和上面的字段意义完全相同
        // Calendar.HOUR——12小时制的小时
        // Calendar.HOUR_OF_DAY——24小时制的小时
        // Calendar.MINUTE——分钟
        // Calendar.SECOND——秒
        // Calendar.DAY_OF_WEEK——星期几

        Calendar calendar = new GregorianCalendar();
        System.out.println("YEAR = " + calendar.get(Calendar.YEAR));
        System.out.println("MONTH = " + calendar.get(Calendar.MONTH));
        System.out.println("DAY_OF_MONTH = " + calendar.get(Calendar.DAY_OF_MONTH));
        System.out.println("HOUR_OF_DAY = " + calendar.get(Calendar.HOUR_OF_DAY));
        System.out.println("MINUTE = " + calendar.get(Calendar.MINUTE));
        System.out.println("SECOND = " + calendar.get(Calendar.SECOND));
    }

    private void testDateAdd() {
        Calendar calendar = new GregorianCalendar();
        Date today = calendar.getTime();
        System.out.println("today = " + DateFormatUtils.format(today, "yyyy/MM/dd"));
        calendar.add(Calendar.DATE, 14);
        Date todateAfter = calendar.getTime();
        System.out.println("todateAfter = " + DateFormatUtils.format(todateAfter, "yyyy/MM/dd"));
    }

    private String getLastDay(String year, String month) {
        Calendar cDate = new GregorianCalendar();
        cDate.set(Integer.parseInt(year), Integer.parseInt(month) - 1, 01); // 设置年月日，日随便设一个就可以
        int day = cDate.getActualMaximum(Calendar.DAY_OF_MONTH);
        String s = new Integer(day).toString();
        return s;
    }
}
