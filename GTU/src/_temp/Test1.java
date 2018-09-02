package _temp;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Test1 {

    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        cal.add(Calendar.HOUR, 11);
        System.out.println(sdf.format(cal.getTime()));
    }

}
