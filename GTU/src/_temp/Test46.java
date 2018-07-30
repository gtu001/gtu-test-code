package _temp;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.time.DateFormatUtils;

public class Test46 {

    public static void main(String[] args) throws InterruptedException, IOException {
        Test46 t = new Test46();

        long xxxx = -511850183;
        
        String str = DateFormatUtils.format(xxxx, "yyyy/MM/dd HH:mm:ss");
        System.out.println(str);
        System.out.println("done...");
    }
}
