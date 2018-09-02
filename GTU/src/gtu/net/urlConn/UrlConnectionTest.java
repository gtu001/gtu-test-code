package gtu.net.urlConn;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Troy 2009/02/02
 * 
 */
public class UrlConnectionTest {

    public static void main(String[] args) {
        try {

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void copyURLpage(String urls, String destionation) {
        try {
            URL u = new URL(urls);
            URLConnection url = u.openConnection();
            BufferedInputStream bis = new BufferedInputStream(url.getInputStream());
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destionation));
            byte[] b = new byte[1024 * 8];
            int num;
            byte[] content;
            while ((num = bis.read(b, 0, b.length)) != -1) {
                bos.write(b);
            }
            bis.close();
            bos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private StringBuffer urlConnection(String urlStr, String info, int timeout) {
        StringBuffer sb = new StringBuffer();
        try {
            URL url = new URL(urlStr);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);

            connection.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "8859_1"); // 8859_1
            out.write(info);
            out.flush();
            out.close();

            InputStream is = connection.getInputStream();
            DataInputStream dis = new DataInputStream(is);

            byte[] buf = new byte[8 * 1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int ret = 0;
            byte[] content;
            while ((ret = dis.read(buf, 0, buf.length)) > -1) {
                content = new byte[ret];
                System.arraycopy(buf, 0, content, 0, ret);
                baos.write(content);
            }
            baos.flush();
            baos.close();
            sb.append(new String(baos.toByteArray(), "MS950")); // UTF-8
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb;
    }

    private static void showURL3() {
        try {
            URL u = new URL("http://www.gamer.com.tw");
            DataInputStream dis = new DataInputStream(u.openStream());
            byte buf[] = new byte[4096];
            int num;
            while ((num = dis.read()) != -1) {
                System.out.write(buf, 0, num);
            }
            System.out.println(new String(buf, "BIG5"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void showURL() {
        try {
            URL u = new URL("http://localhost:8080/LandBankFacelet/index.jsf");
            System.out.println("getAuthority()=" + u.getAuthority()); // 取得權限資訊
                                                                      // :
                                                                      // 使用者資訊,Host名稱,埠號
            System.out.println("getDefaultPort()=" + u.getDefaultPort());
            System.out.println("getFile()=" + u.getFile()); // 取得檔名 :
                                                            // /開始到#之間內容認定之檔名
                                                            // Ex:/boards/index.htm
            System.out.println("getHost()=" + u.getHost()); // 取得host->
                                                            // www.xxx.org or
                                                            // 127.0.0.1
            System.out.println("getPath()=" + u.getPath());
            System.out.println("getPort()=" + u.getPort());
            System.out.println("getProtocol()=" + u.getProtocol()); // 取得協定名稱
                                                                    // http
            System.out.println("getQuery()=" + u.getQuery()); // 取得 ?後面資訊
            System.out.println("getRef()=" + u.getRef()); // 取得錨標籤 #後面資訊
            System.out.println("getUserInfo()=" + u.getUserInfo()); // 使用者資訊
            System.out.println("toExternalForm()=" + u.toExternalForm());
            System.out.println("toString()=" + u.toString());
            System.out.println("toURI()=" + u.toURI());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void showURL2() {
        try {
            URL u = new URL("http://www.gamer.com.tw");
            Object obj = u.getContent();
            if (obj instanceof InputStream) {
                BufferedReader br = new BufferedReader(new InputStreamReader((InputStream) obj));
                StringBuffer sb = new StringBuffer();
                while (br.ready()) {
                    sb.append(br.readLine());
                }
                System.out.println(sb.toString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void showIntAddress() {
        try {
            InetAddress myadderss = InetAddress.getLocalHost();
            System.out.println(myadderss);
            System.out.println(myadderss.getCanonicalHostName());
            System.out.println(myadderss.getHostAddress());
            System.out.println(myadderss.getHostName());
            System.out.println(myadderss.getAddress());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
