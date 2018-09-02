package gtu.net.ping;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.Enumeration;

public class NetworkInterfaceTest {
    
    public static void main(String[] args) {
        try {
            System.out.println(java.net.InetAddress.getLocalHost().getHostAddress());

            Enumeration ifE = NetworkInterface.getNetworkInterfaces();
            while (ifE.hasMoreElements()) {
                Enumeration addrE = ((NetworkInterface) ifE.nextElement()).getInetAddresses();
                while (addrE.hasMoreElements()) {
                    System.out.println("addrs:" + ((InetAddress) addrE.nextElement()).getHostAddress());
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        
        String s1 = "";  // unknown
        try {
          Socket socket = new Socket("yahoo.com",80);
          String s = socket.getLocalAddress().getHostAddress();
          if(!s.equals("255.255.255.255")){
              s1 = s;
          }
          socket.close();
          System.out.println("-->" + s1);
        } catch(SecurityException _ex) { // FORBIDDEN
            _ex.printStackTrace();
        } catch(Exception _ex) { // ERROR
            _ex.printStackTrace();
        }
    }
}
