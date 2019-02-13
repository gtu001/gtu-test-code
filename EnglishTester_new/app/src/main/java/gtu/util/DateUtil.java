/*
 * @(#)DateUtil.java 1.00 07/10/16
 *
 * Copyright (c) 2007 Universal EC, Inc. All rights reserved.
 *
 * This class provides the commond utility of date.
 */
package gtu.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

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

        private String recentFix(long date) {
            long during = (System.currentTimeMillis() - date) / 1000;
            Map<String, Integer> unitMap = new LinkedHashMap<>();
            String[] units = new String[]{"秒", "分鐘", "小時"};
            BigDecimal chk = new BigDecimal(during);
            BigDecimal div = new BigDecimal(60);
            String tmpUnit = "";
            for (int ii = 0; ii < units.length; ii++) {

                //超過 12 小時顯示完整時間
                if (chk.doubleValue() >= 12 && units[ii].equalsIgnoreCase("小時")) {
                    return "";
                }

                if (chk.doubleValue() < 60) {
                    tmpUnit = units[ii];
                    break;
                }
                chk = chk.divide(div, 1, RoundingMode.HALF_UP);
            }
            return chk + tmpUnit;
        }

        public String getDateStr(long date, boolean isAppendSecond) {
            String timeFormat = "HH:mm";
            if (isAppendSecond) {
                timeFormat += ":ss";
            }
            if (today.getLeft() <= date && today.getRight() >= date) {
                String tmpStr = recentFix(date);
                if (StringUtils.isNotBlank(tmpStr)) {
                    return "今天 " + tmpStr + " 前";
                } else {
                    return "今天 " + DateFormatUtils.format(date, timeFormat);
                }
            } else if (yesterday.getLeft() <= date && yesterday.getRight() >= date) {
                return "昨天 " + DateFormatUtils.format(date, timeFormat);
            } else if (twoDayAgo.getLeft() <= date && twoDayAgo.getRight() >= date) {
                return "前天 " + DateFormatUtils.format(date, timeFormat);
            }
            return DateFormatUtils.format(date, "yyyy/MM/dd " + timeFormat);
        }
    }
}
