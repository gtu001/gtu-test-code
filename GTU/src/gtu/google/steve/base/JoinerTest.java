package gtu.google.steve.base;

import com.google.common.base.Joiner;

public class JoinerTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        JoinerTest test = new JoinerTest();
        System.out.println(test.toString());
    }

    public String toString() {
        // on 以"XX"符號隔開
        final StringBuilder builder = new StringBuilder().append(getClass().getSimpleName()).append("{");
        final StringBuilder joiner = Joiner.on(", ").appendTo(builder, "text:" + "ttt", "value:" + "vvvv").append("}");
        return joiner.toString();
    }

}
