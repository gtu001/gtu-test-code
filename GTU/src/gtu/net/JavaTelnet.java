package gtu.net;

import java.io.*;
import java.net.*;

public class JavaTelnet {
    public static void main(String[] args) {
        String hostName;
        int port;
        InetAddress address;

        if (args.length == 2) {
            hostName = args[0];
            port = Integer.parseInt(args[1]);
        } else {
            System.out.println("Usage: java JavaTelnet address port");
//            return;
        }

        hostName = "ptt.cc";
        port = 23;
        
        try {
            address = InetAddress.getByName(hostName);
            try {
                Socket socket = new Socket(address, port);
                new SocketToOut(socket).start();
                new InToSocket(socket).start();
            } catch (IOException e) {
                System.err.println("Connection failed");
            }
        } catch (UnknownHostException e) {
            System.err.println("Unknown host");
        }
    }
}

class SocketToOut extends Thread {
    private Socket socket;
    private BufferedReader socketIn;

    public SocketToOut(Socket socket) throws IOException {
        this.socket = socket;
        socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream(), "big5"));
    }

    @Override
    public void run() {
        try {
            String line = null;
            while ((line = socketIn.readLine()) != null)
                System.out.println(line);
            socket.close();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
}

class InToSocket extends Thread {
    private Socket socket;
    private PrintStream socketOut;
    private BufferedReader in;

    public InToSocket(Socket socket) throws IOException {
        this.socket = socket;
        socketOut = new PrintStream(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {
        try {
            while (!socket.isClosed())
                socketOut.println(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}