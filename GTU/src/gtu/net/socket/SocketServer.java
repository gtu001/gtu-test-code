package gtu.net.socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Vector;

public class SocketServer {
    public static Vector v = new Vector();
    private PrintStream serverLogOut;
    
    public static void main(String[] args) {
    	SocketServer server = new SocketServer();
//    	server.execute(8189, System.out);
    	server.execute(6666, System.out);
    }
    
    public void execute(int port, PrintStream serverLogOut){
    	int i = 1;
        try {
            ServerSocket s = new ServerSocket(port);
            for (;;) {
                Socket incoming = s.accept();
                serverLogOut.println("ID : " + i + ", Connection Started");
                new SocketServer_Thread(incoming, i, v, serverLogOut).start();
                i++;

                Thread.sleep(500);
            }
        } catch (Exception e) {
            serverLogOut.println("Exception in line 19 : " + e);
        }
    }
}

class SocketServer_Thread extends Thread {
    private Socket socket;
    private int counter;
    private Vector vector;
    private String name = "Unknown";
    private PrintStream serverLogOut;

    public SocketServer_Thread(Socket socket, int counter, Vector vector, PrintStream serverLogOut) {
        this.socket = socket;
        this.counter = counter;
        this.vector = vector;
        this.serverLogOut = serverLogOut;
    }

    public void connectionEnded() {
        //vector.remove(thisincoming);
        vector.remove(this);
        serverLogOut.println("ID : " + counter + ", Connection Ended");
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true /* autoFlush */);

            out.println("Enter BYE to exit.");
            out.println("Enter name:xxxx to set your name");
            //vector.add(incoming);
            vector.add(this);
            boolean done = false;
            while (!done) {
                String str = in.readLine();
                if (str == null) {
                    done = true;
                } else {
                    if (str.trim().equals("BYE")) {
                        done = true;
                    } else if (str.startsWith("name:")) {
                        name = str.substring(5);
                        out.println("Your name set to " + name);
                    } else {
                        PrintWriter tempOut;
                        serverLogOut.println("Current clients = " + vector.size());
                        String sendTo = "";
                        if (str.startsWith("to ") && str.indexOf(":") >= 0) {
                            sendTo = str.substring(3, str.indexOf(":"));
                            str = str.substring(str.indexOf(":") + 1);
                        }
                        for (Enumeration enu = vector.elements(); enu.hasMoreElements();) {
                            //Socket tempSocket = (Socket) e.nextElement();      
                            SocketServer_Thread tempThread = (SocketServer_Thread) enu.nextElement();
                            if (sendTo.equals("") || tempThread.name.equals(sendTo)) {
                                Socket tempSocket = tempThread.socket;
                                try {
                                    tempOut = new PrintWriter(tempSocket.getOutputStream(), true /* autoFlush */);
                                    tempOut.println(name + ":" + str);
                                } catch (Exception ex) {
                                    serverLogOut.println("Exception in line 78 : " + ex);
                                }
                            }
                        }
                    }
                }
            }
            socket.close();
        } catch (Exception e) {
            serverLogOut.println("Exception in line 87 : " + e);
        }
        connectionEnded();
    }
}