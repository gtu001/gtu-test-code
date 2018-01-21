package gtu.google.steve.base;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;

public class FunctionsTest {

    /**
     * 不變 直接回傳所apply的值
     */
    @Test
    public void testIdentity() {
        System.out.println("# testIdentity ...");
        System.out.println(Functions.<String> identity().apply("test"));
        System.out.println(Functions.<Integer> identity().apply(20));
    }

    /**
     * 傳回apply值得toString()
     */
    @Test
    public void testToStringFunction() {
        System.out.println("# tesTtoStringFunction ...");
        System.out.println(Functions.toStringFunction().apply("test"));
        System.out.println(Functions.toStringFunction().apply(20));
    }

    /**
     * 不管apply值為合，結果都為constant的值
     */
    @Test
    public void testConstant() {
        System.out.println("# testConstant ...");
        System.out.println(Functions.<String> constant("一定回傳這個").apply("test"));
    }

    /**
     * apply值是否符合
     */
    @Test
    public void testForPredicate() {
        System.out.println("# testForPredicate ...");
        System.out.println(Functions.forPredicate(new Predicate<String>() {
            public boolean apply(String paramT) {
                return StringUtils.isNumeric(paramT);
            }
        }).apply("111"));
    }

    /**
     * 放入Function<B, C> , Function<A, BB> BB必須繼承B
     * 
     * 最後apply會 A -> C
     */
    @Test
    public void testCompose() {
        System.out.println("# testCompose ...");
        Function<A, C> fun = Functions.compose(new Function<B, C>() {
            public C apply(B paramF) {
                return new C(paramF.str + "\tB->C");
            }
        }, new Function<A, BB>() {
            public BB apply(A paramF) {
                return new BB(paramF.str + "\tA->BB");
            }
        });
        C c = fun.apply(new A("AAA"));
        System.out.println("compose = " + c);
    }

    /**
     * 以map作為轉換的參考表 key -> value
     */
    @Test
    public void testForMap() {
        ImmutableMap.Builder<String, Integer> map = ImmutableMap.builder();
        map.put("aaaaa", 1);
        map.put("bbbbb", 2);
        ImmutableMap<String, Integer> immutableMap = map.build();
        System.out.println("aaaaa = " + Functions.forMap(immutableMap).apply("aaaaa"));
        System.out.println("bbbbb = " + Functions.forMap(immutableMap).apply("bbbbb"));
    }

    private static class A {
        private String str;

        private A(String str) {
            this.str = str;
        }
    }

    private static class B {
        private String str;

        private B(String str) {
            this.str = str;
        }
    }

    private static class BB extends B {
        private BB(String str) {
            super(str);
        }
    }

    private static class C {
        private String str;

        private C(String str) {
            this.str = str;
        }

        @Override
        public String toString() {
            return "C [str=" + str + ", toString()=" + super.toString() + "]";
        }
    }
}
