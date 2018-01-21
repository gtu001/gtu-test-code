package gtu.thread.threadGroup;

/**
 * @author 2012/1/8
 */
public class ThreadGroupTest2 implements Runnable {
    public static void main(String[] args) {
        ThreadGroupTest2 p = new ThreadGroupTest2();
        p.init();
    }

    public void init() {
        try {
            // Create a new ThreadGroup and a child for that
            // new ThreadGroup.
            ThreadGroup parentGroup = new ThreadGroup("Parent ThreadGroup");
            ThreadGroup childGroup = new ThreadGroup(parentGroup, "Child ThreadGroup");

            // Create a second thread and start it.
            Thread thr2 = new Thread(parentGroup, this);
            System.out.println("Starting " + thr2.getName() + "...");
            thr2.start();

            // Create a third thread and start it.
            Thread thr3 = new Thread(childGroup, this);
            System.out.println("Starting " + thr3.getName() + "...");
            thr3.start();

            // Display the number of active threads in the
            // new ThreadGroup.
            System.out.println("Active threads in group \"" + parentGroup.getName() + "\" = "
                    + parentGroup.activeCount());

            // List the contents of the ThreadGroups.
            System.out.println("\nListing for ThreadGroup " + parentGroup.getName() + ":");
            parentGroup.list();

            System.out.println("\nListing for ThreadGroup " + childGroup.getName() + ":");
            childGroup.list();

            // Block until the other threads finish.
            thr2.join();
            thr3.join();
        } catch (InterruptedException ex) {
            System.out.println(ex.toString());
        }
    }

    // Implements Runnable.run()
    public void run() {
        for (int i = 0; i < 100000000; i++) {
            // Just increment a counter.
            counter++;
        }

        System.out.println(Thread.currentThread().getName() + " has finished executing.");
    }

    private int counter = 0;
}