package gtu.swing;

import javax.swing.ImageIcon;
import javax.swing.SwingWorker;

public class SwingWorkerTest {
    
    SwingWorker worker = new SwingWorker<String, Integer>() {
        @Override
        public String doInBackground() {
            return "rtn string";
        }

        @Override
        public void done() {
            try {
                String rtnVal = get();
                System.out.println("rtnVal = " + rtnVal);
            } catch (InterruptedException ignore) {
            } catch (java.util.concurrent.ExecutionException e) {
                String why = null;
                Throwable cause = e.getCause();
                if (cause != null) {
                    why = cause.getMessage();
                } else {
                    why = e.getMessage();
                }
                System.err.println("Error retrieving file: " + why);
            }
        }
    };

    public static void main(String[] args) {
        SwingWorkerTest t = new SwingWorkerTest();
        t.worker.run();
        System.out.println("done...");
    }

}
