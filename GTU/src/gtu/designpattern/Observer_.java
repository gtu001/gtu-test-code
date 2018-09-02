package gtu.designpattern;

import java.util.Observable;
import java.util.Observer;

/**
 * 使用Jdk版本
 */
public class Observer_ extends Observable {

    public static void main(String[] args) {
        Observer_ handler = new Observer_();

        MEvlForm bean = new MEvlForm();
        bean.status = "1";

        Observer o1 = new ApplyNode();
        Observer o2 = new ExamineNode();
        Observer o3 = new ReExamineNode();

        handler.addObserver(o1);
        handler.addObserver(o2);
        handler.addObserver(o3);

        handler.setChanged();
        handler.notifyObservers(bean);

        System.out.println(".....");
    }

    static class MEvlForm {
        String status;
    }

    static class ApplyNode implements Observer {
        private static final String STATUS_MATCH = "1";

        public void update(Observable o, Object arg) {
            if (arg instanceof MEvlForm) {
                MEvlForm form = (MEvlForm) arg;
                if (form.status.equals(STATUS_MATCH)) {
                    System.out.println("受考人申請作業 ok!");
                } else {
                    System.out.println("受考人申請作業 no ok!");
                }
            }
        }
    }

    static class ExamineNode implements Observer {
        private static final String STATUS_MATCH = "2";

        public void update(Observable o, Object arg) {
            if (arg instanceof MEvlForm) {
                MEvlForm form = (MEvlForm) arg;
                if (form.status.equals(STATUS_MATCH)) {
                    System.out.println("考評人作業 ok!");
                } else {
                    System.out.println("考評人作業 no ok!");
                }
            }
        }
    }

    static class ReExamineNode implements Observer {
        private static final String STATUS_MATCH = "3";

        public void update(Observable o, Object arg) {
            if (arg instanceof MEvlForm) {
                MEvlForm form = (MEvlForm) arg;
                if (form.status.equals(STATUS_MATCH)) {
                    System.out.println("複評人作業 ok!");
                } else {
                    System.out.println("複評人作業 no ok!");
                }
            }
        }
    }
}
