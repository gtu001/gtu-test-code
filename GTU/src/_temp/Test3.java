package _temp;



public class Test3 {
    
    static ThreadLocal<String> test = new ThreadLocal<String>(){
        @Override
        protected String initialValue() {
            return "aaaaaaaaaaaaa";
        }
    };

    public static void main(String[] args) throws InterruptedException{
        System.out.println(Test3.test.get());
        Thread.sleep(1000);
        test.set("bbbbbbbbb");
        Thread.sleep(1000);
        System.out.println(Test3.test.get());
    }
}
