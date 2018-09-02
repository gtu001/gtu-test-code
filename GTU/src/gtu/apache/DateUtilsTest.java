package gtu.apache;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.commons.lang.time.StopWatch;

/**
 * @author Troy 2009/02/02
 * 
 */
public class DateUtilsTest {

    public static void main(String[] args) {
        Date date = new Date();
        String isoDateTime = DateFormatUtils.ISO_DATETIME_FORMAT.format(date);
        String isoTime = DateFormatUtils.ISO_TIME_NO_T_FORMAT.format(date);
        FastDateFormat fdf = FastDateFormat.getInstance("yyyy-MM");
        String customDateTime = fdf.format(date); // Formats a Date object.

        System.out.println("Default format：" + date); // Default format：Fri Jan
                                                      // 23 17:40:50 CST 2009
        System.out.println("ISO_DATETIME_FORMAT：" + isoDateTime); // ISO_DATETIME_FORMAT：2009-01-23T17:40:50
        System.out.println("ISO_TIME_NO_T_FORMAT：" + isoTime); // ISO_TIME_NO_T_FORMAT：17:40:50
        System.out.println("Custom FastDateFormat：" + customDateTime); // Custom
                                                                       // FastDateFormat：2009-01

        System.out.println("====================================================");

        Date date1 = new Date();
        System.out.println("The time right now：" + date1); // The time right
                                                           // now：Fri Jan 23
                                                           // 17:40:51 CST 2009
        Date date2 = DateUtils.addHours(date1, 6);
        System.out.println("The day after 6 hourse：" + date2); // The day after
                                                               // 6 hourse：Fri
                                                               // Jan 23
                                                               // 23:40:51 CST
                                                               // 2009
        System.out.println("Is date1 and date2 Same Day？" + DateUtils.isSameDay(date1, date2)); // Is
                                                                                                // date1
                                                                                                // and
                                                                                                // date2
                                                                                                // Same
                                                                                                // Day？true
        System.out.println("Is date1 and date2 Same Instant？" + DateUtils.isSameInstant(date1, date2)); // Is
                                                                                                        // date1
                                                                                                        // and
                                                                                                        // date2
                                                                                                        // Same
                                                                                                        // Instant？false
        System.out.println("date1 after rounding：" + DateUtils.round(date1, Calendar.HOUR)); // date1
                                                                                             // after
                                                                                             // rounding：Fri
                                                                                             // Jan
                                                                                             // 23
                                                                                             // 18:00:00
                                                                                             // CST
                                                                                             // 2009
        System.out.println("date2 after truncation：" + DateUtils.truncate(date1, Calendar.HOUR)); // date2
                                                                                                  // after
                                                                                                  // truncation：Fri
                                                                                                  // Jan
                                                                                                  // 23
                                                                                                  // 17:00:00
                                                                                                  // CST
                                                                                                  // 2009
        String[] dates = { "2005.03.24 11:03:26", "2005-03-24 11:03", "2005/03/24" };
        try {
            for (int i = 0; i < dates.length; i++) {
                Date parsedDate = DateUtils.parseDate(dates[i], new String[] { "yyyy/MM/dd", "yyyy.MM.dd HH:mm:ss",
                        "yyyy-MM-dd HH:mm" });
                System.out.println("Parsed Date is >>" + parsedDate); // parse
                                                                      // them to
                                                                      // same
                                                                      // format
            }
            // Parsed Date is >>Thu Mar 24 11:03:26 CST 2005
            // Parsed Date is >>Thu Mar 24 11:03:00 CST 2005
            // Parsed Date is >>Thu Mar 24 00:00:00 CST 2005
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("====================================================");

        String a = DurationFormatUtils.formatPeriodISO(1000, 100000);
        System.out.println(" Time Period (ISO8601)：" + a); // Time Period
                                                           // (ISO8601)：P0Y0M0DT0H1M39.000S
        String b = DurationFormatUtils.formatPeriod(1000, 200000, "mm-ss");
        System.out.println(" Time Period (custom format)：" + b); // Time Period
                                                                 // (custom
                                                                 // format)：03-19
        String c = DurationFormatUtils.formatDurationISO(2000);
        System.out.println(" Time Duration (ISO8601)：" + c); // Time Duration
                                                             // (ISO8601)：P0Y0M0DT0H0M2.000S
        String d = DurationFormatUtils.formatDurationHMS(2267);
        System.out.println(" Time Duration (HMS)" + d); // Time Duration
                                                        // (HMS)0:00:02.267
        String e = DurationFormatUtils.formatDuration(162256, "H-mm-ss.SSS", true);
        System.out.println(" Time Duration (custom format)" + e); // Time
                                                                  // Duration
                                                                  // (custom
                                                                  // format)0-02-42.256
        System.out.println("====================================================");

        StopWatch sw = new StopWatch(); // for timings
        sw.start();
        try {
            Thread.sleep(99);
        } catch (InterruptedException ee) {
            // do nothing
        }
        sw.stop();
        System.out.println("operationA used " + sw.getTime() + " milliseconds."); // operationA
                                                                                  // used
                                                                                  // 170
                                                                                  // milliseconds.
        System.out.println("====================================================");
    }
}
