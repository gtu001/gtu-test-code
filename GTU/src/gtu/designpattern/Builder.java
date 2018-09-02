package gtu.designpattern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Builder {

    static abstract class CarModel {
        List<String> sequences = new ArrayList<String>();

        abstract void start();

        abstract void stop();

        abstract void alarm();

        abstract void engineBoom();

        public void setSequences(List<String> sequences) {
            this.sequences = sequences;
        }

        final public void run() {
            for (int ii = 0; ii < sequences.size(); ii++) {
                String actionName = sequences.get(ii);
                if ("start".equals(actionName)) {
                    start();
                }
                if ("stop".equals(actionName)) {
                    start();
                }
                if ("alarm".equals(actionName)) {
                    start();
                }
                if ("engineBoom".equals(actionName)) {
                    start();
                }
            }
        }
    }

    static class BenzModel extends CarModel {

        @Override
        void start() {
            System.out.println("benz start");
        }

        @Override
        void stop() {
            System.out.println("benz stop");
        }

        @Override
        void alarm() {
            System.out.println("benz alarm");
        }

        @Override
        void engineBoom() {
            System.out.println("benz engineBoom");
        }
    }

    static class BMWModel extends CarModel {

        @Override
        void start() {
            System.out.println("BMW start");
        }

        @Override
        void stop() {
            System.out.println("BMW stop");
        }

        @Override
        void alarm() {
            System.out.println("BMW alarm");
        }

        @Override
        void engineBoom() {
            System.out.println("BMW engineBoom");
        }
    }

    public void main(String[] args) {
        BenzModel benzModel = new BenzModel();
        benzModel.setSequences(Arrays.asList("start", "stop", "alarm", "engineBoom"));
        benzModel.run();
        BMWModel bmwModel = new BMWModel();
        bmwModel.setSequences(Arrays.asList("start", "alarm", "stop"));
        bmwModel.run();

        //        將一個複雜物件的建構與他的表示分離，使得同樣的建構過程可以見出不同的表示
        //        Separate the construction of a complex object from its representation so that the same construction process can create different representations.
    }
}
