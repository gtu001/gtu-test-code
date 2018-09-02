package gtu.net;

import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

public class ProxyNet {

    
    public static void main(String[] args){
        ProxyNet pc = new ProxyNet();
        pc.showUrlDefautProxy("http://www.yahoo.com");
    }
    
    private void showUrlDefautProxy(String url) {
        System.setProperty("java.net.useSystemProxies", "true");
        List l = null;
        try {
            l = ProxySelector.getDefault().select(new URI(url));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (l != null) {
            for (Iterator iter = l.iterator(); iter.hasNext();) {
                java.net.Proxy proxy = (java.net.Proxy) iter.next();
                System.out.println("proxy hostname : " + proxy.type());
                InetSocketAddress addr = (InetSocketAddress) proxy.address();
                if (addr == null) {
                    System.out.println("No Proxy");
                } else {
                    System.out.println("proxy hostname : " + addr.getHostName());
                    System.out.println("proxy port : " + addr.getPort());
                }
            }
        }
    }
}
