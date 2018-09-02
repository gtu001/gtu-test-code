package gtu.net.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class FileUpLoadProjClient extends Thread {

    public void run() {
        Socket socket = null;
        try {
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 8110);
            //發送
            //            ByteArrayInputStream bais = new ByteArrayInputStream("test client send!!".getBytes());
            //            FileUpLoadProjServer.sender(bais, socket.getOutputStream());

            //接收
            FileUpLoadProjServer.receiver(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        new FileUpLoadProjClient().start();
        System.out.println("done...");
    }
}
