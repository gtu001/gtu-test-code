package _temp;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

public class Test56 {

    public static void main(String[] args) {
        String msg = "^.jar^.class";
        String[] arry = StringUtils.trimToEmpty(msg).split("\\^", -1);
        // System.out.println(Arrays.toString(arry));
        Pattern ptn = Pattern.compile("[\\w\\-\\:\\/]+\\s\\d{2}\\:\\d{2}\\:\\d{2}|[\\w\\-\\:\\/]+|\\w+");
        Matcher mth = ptn.matcher("2018-33-22");
        while (mth.find()) {
            System.out.println(mth.group());
        }
        System.out.println("done...");
    }
}
