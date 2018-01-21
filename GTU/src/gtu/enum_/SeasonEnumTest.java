package gtu.enum_;

public class SeasonEnumTest {

    public static void main(String[] args) {
        System.out.println(Season.SPRING.value);
        Season.SPRING.test();
        Season.SUMMER.test();
        Season.AUTUM.test();
        Season.WINTER.test();
    }

    private interface What {
        void test();
    }

    private enum Season implements What {
        SPRING, SUMMER {
            public void test() {// 覆蓋
                System.out.println("我恨夏天");
            };

            public void test1() {// 雖定義此method但外面看不到
                System.out.println("我愛夏天");
            };
        },
        AUTUM, WINTER;

        private String value;
        static {
            SPRING.value = "春";
            SUMMER.value = "夏";
            AUTUM.value = "秋";
            WINTER.value = "冬";
        }

        public void test() {
            System.out.println("不特別有感覺的季節");
        }
    }
}
