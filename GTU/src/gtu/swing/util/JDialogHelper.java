package gtu.swing.util;

import javax.swing.JDialog;

public class JDialogHelper {

    public static void applyAutoClose(final JDialog dlg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                }
                while (true) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        break;
                    }
                    if (JCommonUtil.isOnTop(dlg)) {
                        break;
                    }
                }
                while (true) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        break;
                    }
                    if (!JCommonUtil.isOnTop(dlg)) {
                        dlg.dispose();
                        break;
                    }
                }
            }
        }).start();
    }
}
