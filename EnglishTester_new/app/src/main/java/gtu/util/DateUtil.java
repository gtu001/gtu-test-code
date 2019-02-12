/*
 * @(#)DateUtil.java 1.00 07/10/16
 *
 * Copyright (c) 2007 Universal EC, Inc. All rights reserved.
 *
 * This class provides the commond utility of date.
 */
package gtu.util;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Calendar;

//import org.junit.Test;

/**
 * This class provides the commond utility of date.
 *
 * @author EFT Dept. of UEC.
 * <p>
 * 2009/02/02
 * @version 1.00 16 Oct 2007
 */
public class DateUtil {

    public static class DateFriendlyInfoHelper {
        private Pair<Long, Long> today;
        private Pair<Long, Long> yesterday;
        private Pair<Long, Long> twoDayAgo;

        public DateFriendlyInfoHelper() {
            today = getDayPair(0);
            yesterday = getDayPair(-1);
            twoDayAgo = getDayPair(-2);
        }

        private Pair<Long, Long> getDayPair(int addDay) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, addDay);

            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            long start = cal.getTimeInMillis();

            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 99999);

            long end = cal.getTimeInMillis();
            return Pair.of(start, end);
        }

        public String getDateStr(long date, boolean isAppendSecond) {
            String timeFormat = "HH:mm";
            if (isAppendSecond) {
                timeFormat += ":ss";
            }
            if (today.getLeft() <= date && today.getRight() >= date) {
                return "今天 " + DateFormatUtils.format(date, timeFormat);
            } else if (yesterday.getLeft() <= date && yesterday.getRight() >= date) {
                return "昨天 " + DateFormatUtils.format(date, timeFormat);
            } else if (twoDayAgo.getLeft() <= date && twoDayAgo.getRight() >= date) {
                return "前天 " + DateFormatUtils.format(date, timeFormat);
            }
            return DateFormatUtils.format(date, "yyyy/MM/dd " + timeFormat);
        }
    }


}
