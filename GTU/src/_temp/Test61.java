package _temp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.SerializationUtils;

public class Test61 {

    public static void main(String[] args) {
        String aaaa = "aaa\\dddd\\eeee\\rrrr";
        String[] arry = aaaa.split("\\\\", 2);
        System.out.println(Arrays.toString(arry));
    }
}
