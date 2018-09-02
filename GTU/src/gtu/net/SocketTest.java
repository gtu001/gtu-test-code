package gtu.net;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Troy 2009/02/02
 * 
 */
public class SocketTest {

    public void createServer() {
        try {
            byte buff[] = new byte[1024];
            Socket sock = new Socket("127.0.0.1", 2525);
            InputStream in = sock.getInputStream();
            int n = in.read(buff);
            System.out.println("Get Info from Server : ");
            System.out.write(buff, 0, n);
            sock.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void createClient() {
        try {
            ServerSocket svsock = new ServerSocket(2525);
            Socket sock = svsock.accept();
            PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
            out.println("hello!");
            sock.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
