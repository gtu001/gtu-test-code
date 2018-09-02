package gtu.net.urlConn.demo;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DemoTestHttpURLConnection {
    public static void main(String[] args) throws Exception {

        // 传给对方参数，也可以是对象（此处的对象必须是可序列化的对象）
        String sMessage = "客 户端传入到服务区点数据了！"
                + DemoTestHttpURLConnection.class.getName();

        // 服务器地址
        URL url = new URL(
                "http://127.0.0.1:8088/ServletServiceDemo/demoService");

        // 打开地址
        URLConnection con = url.openConnection();

        // 指示应用程序要从 URL 连接读取数据
        con.setDoInput(true);

        // 指示应用程序要将数据写入 URL 连接
        con.setDoOutput(true);

        // 设置一般请求属性 (不设置也没什么关系 ：)
        con.setRequestProperty("Content-type", "application/octest-stream");

        // 从Url连接中获取输出流 (即：将该流传给服务器)
        OutputStream out = con.getOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream(out);

        // 将之前设置的好的对象传入给服务器
        objStream.writeObject(sMessage);
        objStream.flush();
        objStream.close();
        out.close();

        // 从Url连接中获取输入流信息 (即：服务器的输出流信息)
        InputStream in = con.getInputStream();
        ObjectInputStream back = new ObjectInputStream(in);

        // 得到服务器对象信息！
        System.out.println((String) back.readObject());

        back.close();
        in.close();
    }
}
