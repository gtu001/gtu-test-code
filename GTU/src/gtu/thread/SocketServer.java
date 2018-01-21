package gtu.thread;

public class SocketServer implements java.lang.Runnable {
    private int port;
    private java.net.ServerSocket ss;

    public SocketServer(int port) throws java.io.IOException {
        this.port = port;
        // 建立一個ServerSocket
        this.ss = new java.net.ServerSocket(port);
    }

    public void run() {
        java.net.Socket sk = null;
        while (true)// 永遠執行
        {
            // 等待連入
            System.out.println("waiting...");
            try {
                // 取得連線Socket
                sk = this.ss.accept();
                System.out.println("accept");
                // 取得Client連線Address
                System.out.println(sk.getLocalAddress());
                sk.close();
                System.out.println("close");
            } catch (java.io.IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }

        }
    }

    public static void main(String args[]) throws java.io.IOException {
        // runable要new一個Thread,再把runnable置入
        java.lang.Thread thread = new java.lang.Thread(new SocketServer(81));
        thread.start();
    }
}

//可利利用Browser來測試結果，
//
//在網址列打入http://localhost:81     http://ServerIP:port
//
//可以看到Server程式回應如下：
//
//    waiting…
//    0.0.0.0/0.0.0.0
//    waiting…
//
//取得連線Socket後馬上又accept等待了。