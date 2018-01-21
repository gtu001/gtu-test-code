package gtu.designpattern;

public class Adapter {

    interface Duck {
        void quack();

        void fly();
    }

    static class MallardDuck implements Duck {
        @Override
        public void quack() {
            System.out.println("Quack!");
        }

        @Override
        public void fly() {
            System.out.println("I'm flying");
        }
    }

    interface Turkey {
        void gobble();

        void fly();
    }

    static class WildTurkey implements Turkey {
        @Override
        public void gobble() {
            System.out.println("Gobble gobble!");
        }

        @Override
        public void fly() {
            System.out.println("I'm flying a short distance");
        }
    }

    static class TurkeyAdapter implements Duck {
        Turkey turkey;

        @Override
        public void quack() {
            turkey.gobble();
        }

        @Override
        public void fly() {
            for (int ii = 0; ii < 5; ii++) {
                turkey.fly();
            }
        }
    }
}
