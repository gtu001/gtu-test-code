package gtu.designpattern;

public class Decorator {

    public static void main(String[] args) {
        Beverage beverage = new Espresso();
        System.out.println(beverage.getDescription() + " $" + beverage.cost());

        Beverage beverage2 = new HouseBlend();
        System.out.println(beverage2.getDescription() + " $" + beverage2.cost());

        Beverage beverage3 = new Mocha(beverage);
        System.out.println(beverage3.getDescription() + " $" + beverage3.cost());

        Beverage beverage4 = new Mocha(beverage2);
        System.out.println(beverage4.getDescription() + " $" + beverage4.cost());

        Beverage beverage5 = new Soy(beverage3);
        System.out.println(beverage5.getDescription() + " $" + beverage5.cost());
    }

    static abstract class Beverage {
        String description = "Unknown Beverage";

        public String getDescription() {
            return description;
        }

        public double cost() {
            return 0d;
        }
    }

    static abstract class CondimentDecorator extends Beverage {
        public abstract String getDescription();
    }

    static class Espresso extends Beverage {
        Espresso() {
            description = "Espresso";
        }

        public double cost() {
            return 1.99;
        }
    }

    static class HouseBlend extends Beverage {
        HouseBlend() {
            description = "House Blend Coffee";
        }

        public double cost() {
            return .89;
        }
    }

    static class Mocha extends CondimentDecorator {
        Beverage beverage;

        Mocha(Beverage beverage) {
            this.beverage = beverage;
        }

        @Override
        public String getDescription() {
            return beverage.getDescription() + ", Mocha";
        }

        public double cost() {
            return .2 + beverage.cost();
        }
    }

    static class Soy extends CondimentDecorator {
        Beverage beverage;

        Soy(Beverage beverage) {
            this.beverage = beverage;
        }

        @Override
        public String getDescription() {
            return beverage.getDescription() + ", Soy";
        }

        public double cost() {
            return .4 + beverage.cost();
        }
    }
}
