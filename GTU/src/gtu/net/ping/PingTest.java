package gtu.net.ping;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

//import org.junit.Test;

public class PingTest {

    public static void main(String[] args) throws UnknownHostException, IOException {
        // System.out.println(InetAddress.getAllByName("gtu_vm"));

        String ip = "10.10.2.101";
        int port = 1521;
        
        System.out.println("connect ip : " + ip + " " + port);
        
        Socket server = new Socket();
        InetSocketAddress address = new InetSocketAddress(ip, port);
        server.connect(address, 30000);
        server.close();
        System.out.println(" connect ok !!");
        
        System.out.println("done...");
    }

    public void testPing2() throws UnknownHostException, IOException {
        System.out.println("# testPing2 ...");
        Socket socket = new Socket(InetAddress.getByName("127.0.0.1"), 80);
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        PrintStream ps = new PrintStream(socket.getOutputStream());
        ps.println("Hello");
        String str = dis.readLine();
        if (str.equals("Hello"))
            System.out.println("Alive!");
        else
            System.out.println("Dead or echo port not responding");
        socket.close();
    }

    public void testPing1() throws UnknownHostException, IOException {
        System.out.println("# testPing1 ...");
        InetAddress address = InetAddress.getByName("127.0.0.1");
        System.out.println("Name: " + address.getHostName());
        System.out.println("Addr: " + address.getHostAddress());
        System.out.println("Reach: " + address.isReachable(3000));
    }

    public void testPing3() {
        System.out.println("# testPing3 ...");
        Socket server = null;
        try {
            server = new Socket();
            InetSocketAddress address = new InetSocketAddress("127.0.0.1", 80);
            server.connect(address, 5000);
        } catch (UnknownHostException e) {
            System.out.println("telnet失败");
        } catch (IOException e) {
            System.out.println("telnet失败");
        } finally {
            if (server != null)
                try {
                    server.close();
                } catch (IOException e) {
                }
        }
    }

    public static boolean ping(String host, int port) {
        System.out.println("# ping ..." + host + " " + port);
        Socket server = null;
        try {
            server = new Socket();
            InetSocketAddress address = new InetSocketAddress(host, port);
            server.connect(address, 5000);
            return true;
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            if (server != null)
                try {
                    server.close();
                } catch (IOException e) {
                }
        }
    }
}
