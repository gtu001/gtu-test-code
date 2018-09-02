package gtu.util;

import java.util.Calendar;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.Locale;

public class FormatterTest {

    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        // Send all output to the Appendable object sb
        Formatter formatter = new Formatter(sb, Locale.US);

        // Explicit argument indices may be used to re-order output.
        formatter.format("%4$2s %3$2s %2$2s %1$2s", "a", "b", "c", "d");
        sb.append("\n");
        // -> " d  c  b  a"

        // Optional locale as the first argument can be used to get
        // locale-specific formatting of numbers. The precision and width can be
        // given to round and align the value.
        formatter.format(Locale.FRANCE, "e = %+10.4f", Math.E);
        sb.append("\n");
        // -> "e =    +2,7183"

        // The '(' numeric flag may be used to format negative numbers with
        // parentheses rather than a minus sign. Group separators are
        // automatically inserted.
        formatter.format("Amount gained or lost since last statement: $ %(,.2f", -621758f);
        sb.append("\n");
        // -> "Amount gained or lost since last statement: $ (6,217.58)"

        // Writes a formatted string to System.out.
        formatter.format("Local time: %tT", Calendar.getInstance());
        sb.append("\n");
        // -> "Local time: 13:34:18"

        Calendar c = new GregorianCalendar(1995, 6, 23);
        formatter.format("Duke's Birthday: %1$tm %1$te,%1$tY", c);
        // -> s == "Duke's Birthday: May 23, 1995"
        
        System.out.println(sb);
    }
}
