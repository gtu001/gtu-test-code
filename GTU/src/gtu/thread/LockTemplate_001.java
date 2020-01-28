package gtu.thread;

import java.util.concurrent.locks.ReentrantLock;

public class LockTemplate_001 {

    public static void main(String[] args) {
        LockTemplate_001 t = new LockTemplate_001();
        t.tryLockMethod();
        System.out.println("done...");
    }

    private ReentrantLock demoLock = new ReentrantLock();

    private void tryLockMethod() {
        if (demoLock.tryLock()) {
            try {
                // TODO
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (demoLock.isLocked()) {
                    demoLock.unlock();
                }
            }
        }
    }
}
