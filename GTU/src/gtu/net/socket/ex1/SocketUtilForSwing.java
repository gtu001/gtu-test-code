package gtu.net.socket.ex1;

import java.util.HashMap;
import java.util.Map;

import gtu.net.ping.PingTest;

public class SocketUtilForSwing {

    public static void startSwingUI(final String host, final int port, Class<?> uiClass, final Runnable runnable) {
        final String command = "run_" + uiClass.getSimpleName();
        if (!PingTest.ping(host, port)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Map<String, Runnable> runMap = new HashMap<String, Runnable>();
                    runMap.put(command, runnable);
                    SocketUtilForBackend.runServer(port, runMap);
                }
            }).start();
        }
        do {
            System.out.println("start client ...");
            SocketUtilForBackend.runClient(host, port, command);
        } while (!PingTest.ping(host, port));
    }
}
