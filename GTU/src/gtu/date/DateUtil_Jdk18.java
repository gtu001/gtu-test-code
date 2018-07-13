package gtu.date;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class DateUtil_Jdk18 {

    private static final DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()//
            .appendValue(ChronoField.YEAR)//
            .appendValue(ChronoField.MONTH_OF_YEAR, 2)//
            .appendValue(ChronoField.DAY_OF_MONTH, 2)//
            .toFormatter();//

    public static String now() {
        return LocalDateTime.now().format(dateTimeFormatter);
    }

    public static String now(String pattern) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        String val = LocalDateTime.now().format(df);
        return val;
    }
}
