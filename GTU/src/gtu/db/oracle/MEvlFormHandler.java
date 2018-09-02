package gtu.db.oracle;

import java.util.Observable;
import java.util.Observer;

public class MEvlFormHandler extends Observable {

    public MEvlFormHandler() {
    }

    public static void main(String[] args) {
        MEvlFormHandler handler = new MEvlFormHandler();

        MEvlForm bean = handler.new MEvlForm();

        Observer o1 = handler.new ApplyNode();
        Observer o2 = handler.new ExamineNode();
        Observer o3 = handler.new ReExamineNode();
        Observer o4 = handler.new SendExaminationPaperNode();
        Observer o5 = handler.new TheEndExamineNode();

        handler.addObserver(o1);
        handler.addObserver(o2);
        handler.addObserver(o3);
        handler.addObserver(o4);
        handler.addObserver(o5);

        System.out.println("-------------------------------");
        System.out.println("status = 1");
        handler.setChanged();
        bean.setStatus("1");
        handler.notifyObservers(bean);
        System.out.println("-------------------------------");
        System.out.println("status = 2");
        handler.setChanged();
        bean.setStatus("2");
        handler.notifyObservers(bean);
        System.out.println("-------------------------------");
        System.out.println("status = 3");
        handler.setChanged();
        bean.setStatus("3");
        handler.notifyObservers(bean);
        System.out.println("-------------------------------");
        System.out.println("status = 5");
        handler.setChanged();
        bean.setStatus("5");
        handler.notifyObservers(bean);
        System.out.println("-------------------------------");
        System.out.println("status = 6");
        handler.setChanged();
        bean.setStatus("6");
        handler.notifyObservers(bean);
        System.out.println("-------------------------------");

        System.out.println(".....");
    }

    class MEvlForm {
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    class TheEndExamineNode implements Observer {

        private static final String STATUS_MATCH = "6";

        public void update(Observable o, Object arg) {
            MEvlForm form = (MEvlForm) arg;
            if (form.getStatus().equals(STATUS_MATCH)) {
                System.out.println("考核完成 ok!");
            } else {
                System.out.println("考核完成 no ok!");
            }
        }
    }

    class SendExaminationPaperNode implements Observer {

        private static final String STATUS_MATCH = "5";

        public void update(Observable o, Object arg) {
            MEvlForm form = (MEvlForm) arg;
            if (form.getStatus().equals(STATUS_MATCH)) {
                System.out.println("評鑑表送出作業 ok!");
            } else {
                System.out.println("評鑑表送出作業 no ok!");
            }
        }
    }

    class ReExamineNode implements Observer {

        private static final String STATUS_MATCH = "3";

        public void update(Observable o, Object arg) {
            MEvlForm form = (MEvlForm) arg;
            if (form.getStatus().equals(STATUS_MATCH)) {
                System.out.println("覆評人作業 ok!");
            } else {
                System.out.println("覆評人作業 no ok!");
            }
        }
    }

    class ExamineNode implements Observer {

        private static final String STATUS_MATCH = "2";

        public void update(Observable o, Object arg) {
            MEvlForm form = (MEvlForm) arg;
            if (form.getStatus().equals(STATUS_MATCH)) {
                System.out.println("考評人作業 ok!");
            } else {
                System.out.println("考評人作業 no ok!");
            }
        }
    }

    class ApplyNode implements Observer {

        private static final String STATUS_MATCH = "1";

        public void update(Observable o, Object arg) {
            MEvlForm form = (MEvlForm) arg;
            if (form.getStatus().equals(STATUS_MATCH)) {
                System.out.println("受考人申請作業 ok!");
            } else {
                System.out.println("受考人申請作業 no ok!");
            }
        }
    }
}
