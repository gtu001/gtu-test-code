package gtu.net.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class FileUpLoadProjServer {

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(8110);
            Socket socket = null;
            while (true) {
                try {
                    socket = server.accept();
                    //                    receiver(socket.getInputStream());
                    sender(new FileInputStream("C:/Users/gtu001/Desktop/親等關聯/ec回傳資料.txt"), socket.getOutputStream());
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        if (socket != null)
                            socket.close();
                    } catch (IOException e) {
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //發送
    static void sender(InputStream in, OutputStream out) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "BIG5"));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
        for (String line = null; (line = reader.readLine()) != null;) {
            writer.write(line);
            writer.newLine();
        }
        writer.flush();
        writer.close();
    }

    //接收 
    static void receiver(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        for (String line = null; (line = reader.readLine()) != null;) {
            writer.write(line);
            writer.newLine();
        }
        writer.flush();
        writer.close();
    }
}