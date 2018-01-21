package gtu.designpattern;

public class Template {

    static abstract class HummerModel {
        abstract void start();//發動

        abstract void stop();//停止

        abstract void alarm();//喇叭

        abstract void engineBoom();//發動引擎

        public void run() {
            start();
            engineBoom();
            alarm();
            stop();
        }
    }

    static class HummerH1Model extends HummerModel {

        @Override
        void start() {
            System.out.println("發動");
        }

        @Override
        void stop() {
            System.out.println("停止");
        }

        @Override
        void alarm() {
            System.out.println("喇叭");
        }

        @Override
        void engineBoom() {
            System.out.println("引擎發動");
        }
    }

    public static void main(String[] args) {
        HummerH1Model hummer = new HummerH1Model();
        hummer.run();

        //        在一個操作中定義一個演算法框架，而將依些步驟延到子類別，使得子類別不需改變任何演算法結構即可新定義該演算法的否些特定步驟
        //        Define the skeleton of an algorithm in an operation. deferring some steps to subclasses. Template Method lets subclasses redefine certain steps of an algorithm without changing the algorithm structure.
    }
}
