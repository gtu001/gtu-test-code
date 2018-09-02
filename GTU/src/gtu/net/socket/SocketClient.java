package gtu.net.socket;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;

public class SocketClient {
    public static BufferedReader in;
    public static PrintWriter out;
    public static TextField jtf;

    public static void main(String[] args) {
        JFrame f = new JFrame("Socket Client");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container c = f.getContentPane();
        c.setLayout(new BorderLayout());
        f.setLocation(300, 300);
        f.setSize(500, 300);
        TextArea jta = new TextArea(5, 30);
        jta.setFocusable(false);
        //jta.setEnabled(false);
        jta.setFont(new Font("Arial", 0, 20));
        jtf = new TextField("");
        jtf.setFont(new Font("Arial", 0, 20));
        c.add("Center", jta);
        c.add("South", jtf);
        jtf.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    out.println(jtf.getText());
                    jtf.setText("");
                }
            }
        });
        f.setVisible(true);
        try {
//            Socket s = new Socket("127.0.0.1", 8189);
            Socket s = new Socket("127.0.0.1", 6666);
//            Socket s = new Socket("192.168.0.1", 6666);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(), true);
            
            if(s.isConnected()){
                boolean more = true;
                while (more) {
                    String inStr = in.readLine();
                    if (inStr == null) {
                        more = false;
                    } else {
                        jta.append("" + inStr + "\n");
                    }
                }
            }
            
            s.close();
        } catch (IOException e) {
            jta.append("Exception in line 52 : " + e);
        } 
    }
}