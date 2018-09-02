package gtu.designpattern;

import gtu.designpattern.Factory.BlackHuman;
import gtu.designpattern.Factory.Human;
import gtu.designpattern.Factory.WhiteHuman;
import gtu.designpattern.Factory.YellowHuman;

public class AbstractFactory {

    interface HumanFactory {
        Human createYellowHuman();

        Human createBlackHuman();

        Human createWhiteHuman();
    }

    static class HumanFactoryImpl implements HumanFactory {
        @Override
        public Human createYellowHuman() {
            return new YellowHuman();
        }

        @Override
        public Human createBlackHuman() {
            return new BlackHuman();
        }

        @Override
        public Human createWhiteHuman() {
            return new WhiteHuman();
        }
    }

    //    為創建一組相關或互相依賴的物件提供一個介面，而且無須指定他們的具體類別
    //    Provide an interface for creating families of related or dependent objects without specifying their concrete classes.
}
