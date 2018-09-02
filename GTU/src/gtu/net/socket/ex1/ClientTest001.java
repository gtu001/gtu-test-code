package gtu.net.socket.ex1;

public class ClientTest001 {

    public static void main(String[] args) {
        SocketUtilForBackend.runClient("127.0.0.1", 6666, "shutdown");
    }

}
