package gtu.net.ping;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

public class GetMyIP {
    
    public static void main(String[] args){
        GetMyIP test = new GetMyIP();
        System.out.println("LAN : " + test.getVirtualIP());
        System.out.println("HostName : " + test.getHostName(test.getVirtualIP()));
        System.out.println("WAN : " + test.getRealIP());
        System.out.println("HostName : " + test.getHostName(test.getRealIP()));
        System.out.println("done...");
    }

    private String getRealIP() {
        try {
            URL pageUrl = new URL("http://checkip.dyndns.org");
            BufferedReader reader = new BufferedReader(new InputStreamReader(pageUrl.openStream()));

            StringBuffer pageBuffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                pageBuffer.append(line);
            }
            return getString(pageBuffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    private String getString(String str) {
        int num1 = str.lastIndexOf(":");
        int num2 = str.indexOf("</body>");
        str = str.substring(num1 + 1, num2);
        return str.trim();
    }

    private String getVirtualIP() {
        try {
            InetAddress add = InetAddress.getLocalHost();
            return add.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    private String getHostName(String str) {
        try {
            InetAddress add = InetAddress.getByName(str);
            return add.getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
