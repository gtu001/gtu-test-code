package gtu.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetTool {
    InetAddress myIPaddress = null;
    InetAddress myServer = null;

    public static void main(String args[]) throws UnknownHostException {
        NetTool mytool;
        mytool = new NetTool();

        System.out.println("Your host IP is: " + mytool.getMyIP());
        System.out.println("The Server IP is :" + mytool.getServerIP());
    }

    // 取得LOCALHOST的IP地址
    public InetAddress getMyIP() {
        try {
            myIPaddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
        }
        return (myIPaddress);
    }

    // 取得 www.abc.com 的IP地址
    public InetAddress getServerIP() {
        try {
            myServer = InetAddress.getByName("www.abc.com");
        } catch (UnknownHostException e) {
        }
        return (myServer);
    }
}
