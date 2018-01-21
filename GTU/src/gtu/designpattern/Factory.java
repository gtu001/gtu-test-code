package gtu.designpattern;

public class Factory {

    interface Human {
        public void getColor();
    }

    static class BlackHuman implements Human {
        @Override
        public void getColor() {
            System.out.println("黑色");
        }
    }

    static class YellowHuman implements Human {
        @Override
        public void getColor() {
            System.out.println("黃色");
        }
    }

    static class WhiteHuman implements Human {
        @Override
        public void getColor() {
            System.out.println("白色");
        }
    }

    static class HumanFactory {
        @SuppressWarnings("unchecked")
        public <T extends Human> T createHuman(Class<T> c) {
            Human human = null;
            try {
                human = (Human) Class.forName(c.getName()).newInstance();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return (T) human;
        }
    }

    //    定義一個用於創建物件的介面，讓子類別決定要實力畫哪個類別。工廠方法讓一個類別的實例畫工作遞延到旗子類別
    //Define an interface for creating an object, but let subclasses decide which class to instantiate. Factory Method lets a class defer instantiation to subclasses.
}
