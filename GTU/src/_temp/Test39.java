package _temp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Test39 {

    public static void main(String[] args) throws Exception {
        String dateStr = "2018-02-07 05:15:37.124";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date d = sdf.parse(dateStr);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        System.out.println(sdf.format(cal.getTime()));
        System.out.println("done...");
    }
}
