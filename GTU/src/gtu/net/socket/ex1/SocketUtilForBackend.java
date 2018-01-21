package gtu.net.socket.ex1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class SocketUtilForBackend {

    public static void main(String[] args) {
        Map<String, Runnable> runMap = new LinkedHashMap<String, Runnable>();
        runMap.put("testok", new Runnable() {
            @Override
            public void run() {
                System.out.println("PRINT-----testOK");
            }
        });
        SocketUtilForBackend.runServer(6666, runMap);
    }
    
    public static void runClient(String host, int port, String sendCommand) {
        try {
            Socket s = new Socket(host, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            if (s.isConnected()) {
                out.println(sendCommand);
            }
            s.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void runServer(int port, Map<String, Runnable> runMap) {
        int i = 1;
        ServerSocket s = null;
        try {
            s = new ServerSocket(port);
            A: for (;;) {
                System.out.println("Server Port : " + port + ", waiting ...");
                Socket socket = s.accept();
                System.out.println("ID : " + i + ", Connection Started");

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                B: for (String line = null; (line = in.readLine()) != null;) {
                    for (String command : runMap.keySet()) {
                        if (StringUtils.equals(line, command)) {
                            System.out.println("exe command : " + command);
                            runMap.get(command).run();
                            socket.shutdownInput();
                            socket.shutdownOutput();
                            socket.close();
                            break B;
                        }
                    }
                    if (StringUtils.equals("shutdown", line)) {
                        System.out.println("shutdowning...");
                        break A;
                    }
                    Thread.sleep(100);
                }
                in.close();
                i++;
                
                Thread.sleep(100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                s.close();
                System.out.println("shutdown done...");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
