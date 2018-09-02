package gtu.designpattern;

import java.util.Random;

public class Mediator {

    public static void main(String[] args) {
        MediatorImpl mediator = new MediatorImpl();
        System.out.println("-----------採購人員採購電腦-----------");
        Purchase purchase = new Purchase(mediator);
        purchase.buyIBMComputer(100);
        System.out.println("-----------銷售人員銷售電腦-----------");
        Sale sale = new Sale(mediator);
        sale.sellIBMComputer(1);
        System.out.println("-----------庫房管理人員清倉處理-----------");
        Stock stock = new Stock(mediator);
        stock.clearStock();

        //用一個中介物件封裝一系列的物件交互作用，中介者使各物件不需要直接的互動，從而使其偶和鬆散，而且可以獨立的改變他們之間的交互作用
        //Define an object that encapsulates how a set of objects interact. Mediator promotes loose coupling by keeping objects from referring to each other explicitly. and it lets you vary their interaction independently.
    }

    static class MediatorImpl {
        private Purchase purchase;//採購
        private Sale sale;//銷售
        private Stock stock;//庫存

        //中介者最重要的方法
        public void execute(String str, Object... objects) {
            if ("purchase.buy".equals(str)) {
                this.buyComputer((Integer) objects[0]);
            }
            if ("sale.sell".equals(str)) {
                this.sellComputer((Integer) objects[0]);
            }
            if ("sale.offsell".equals(str)) {
                this.offSell();
            }
            if ("stock.clear".equals(str)) {
                this.clearStock();
            }
        }

        private void buyComputer(int number) {
            int saleStatus = sale.getSaleStatus();
            if (saleStatus > 80) {//銷售良好
                System.out.println("採購IBM電腦:" + number);
                stock.increase(number);
            } else {//銷售情況不好
                int buyNumber = number / 2;
                System.out.println("採購IBM電腦:" + buyNumber);
            }
        }

        private void sellComputer(int number) {
            if (stock.getStockNumber() < number) {
                purchase.buyIBMComputer(number);
            }
            stock.decrease(number);
        }

        private void offSell() {
            System.out.println("折價銷售IBM電腦:" + stock.getStockNumber());
        }

        //清倉處理
        private void clearStock() {
            sale.offSale();
            purchase.refuseBuyIBM();//要求不採購
        }
    }

    static abstract class AbstractColleague {
        MediatorImpl mediator;

        public AbstractColleague(MediatorImpl mediator) {
            this.mediator = mediator;
        }
    }

    static class Purchase extends AbstractColleague {
        public Purchase(MediatorImpl mediator) {
            super(mediator);
        }

        public void buyIBMComputer(int number) {
            super.mediator.execute("purchase.buy", number);
        }

        public void refuseBuyIBM() {
            System.out.println("不再採購IBM電腦");
        }
    }

    static class Stock extends AbstractColleague {
        public Stock(MediatorImpl mediator) {
            super(mediator);
        }

        private static int CUMPUTER_NUMBER = 100;

        public void increase(int number) {
            CUMPUTER_NUMBER = CUMPUTER_NUMBER + number;
            System.out.println("庫存數量為:" + CUMPUTER_NUMBER);
        }

        public void decrease(int number) {
            CUMPUTER_NUMBER = CUMPUTER_NUMBER - number;
            System.out.println("庫存數量為:" + CUMPUTER_NUMBER);
        }

        public int getStockNumber() {
            return CUMPUTER_NUMBER;
        }

        public void clearStock() {
            System.out.println("清理存貨數量為:" + CUMPUTER_NUMBER);
            super.mediator.execute("stock.clear");
        }
    }

    static class Sale extends AbstractColleague {
        public Sale(MediatorImpl mediator) {
            super(mediator);
        }

        public void sellIBMComputer(int number) {
            super.mediator.execute("sale.sell", number);
            System.out.println("銷售IBM電腦:" + number);
        }

        public int getSaleStatus() {
            Random rand = new Random(System.currentTimeMillis());
            int saleStatus = rand.nextInt(100);
            System.out.println("IBM電腦銷售情況為:" + saleStatus);
            return saleStatus;
        }

        public void offSale() {
            super.mediator.execute("sale.offsell");
        }
    }
}
