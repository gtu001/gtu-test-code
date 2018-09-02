package _temp;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Test10 {

    public static void main(String[] args) {
        SimpleDateFormat sdf= new SimpleDateFormat("%a, %d %b %Y %H:%M:%S %z");
//        "Sat, 21 Aug 2010 22:31:20 +0000"
        System.out.println(sdf.format(new Date()));
        
//        api.dropboxapi.com
//        content.dropboxapi.com
//        notify.dropboxapi.com
    }
}
