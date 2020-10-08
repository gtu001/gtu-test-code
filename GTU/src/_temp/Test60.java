package _temp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.SerializationUtils;


public class Test60 {

    private static class TestBean implements Serializable {
        String xxxxx;
    }

    public static void main(String[] args) {
        TestBean a1 = new TestBean();
        a1.xxxxx = "aaaa";
        TestBean a2= (TestBean)SerializationUtils.clone(a1);
        a2.xxxxx = "bbbb";
        System.out.println(">>>> " + a1.xxxxx);
    }
}
