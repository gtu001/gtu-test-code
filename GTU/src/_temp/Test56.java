package _temp;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

public class Test56 {

    public static void main(String[] args) {
        String msg = "^.jar^.class";
        String[] arry = StringUtils.trimToEmpty(msg).split("\\^", -1);
        System.out.println(Arrays.toString(arry));
    }
}
