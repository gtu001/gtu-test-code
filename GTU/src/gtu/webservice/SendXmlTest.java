package gtu.webservice;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.FileUtils;

public class SendXmlTest {

    public static void main(String[] args) throws IOException {
//        http://v.youku.com/v_show/id_XMjY2OTIwNzMy.html
//            http://v.youku.com/v_show/id_XMjIyMTgwOTgw.html
//        System.out.println(//
//                sendXml("http://localhost:8080/tee-middleware/rws/receipt/import", //
//                new File("C:/Users/gtu001/Desktop/01_complete_content.xml")));
        
        //測試來源 http://webservice.webxml.com.cn/WebServices/MobileCodeWS.asmx  
        //測試xml
        /*
                <?xml version="1.0" encoding="utf-8"?>
                <ArrayOfString xmlns="http://WebXml.com.cn/">
                  <string>1111111111</string>
                  <string>2222222222</string>
                </ArrayOfString>
         */
        System.out.println(//
                sendXml("http://webservice.webxml.com.cn/WebServices/MobileCodeWS.asmx/getDatabaseInfo", //
                new File("C:/Users/gtu001/Desktop/xxxxxxxxxxxx.txt")));
    }
    
    public static boolean sendXml(String urlPath, File xmlFile) throws IOException{
        byte[] data = FileUtils.readFileToByteArray(xmlFile);
        String lengthStr = String.valueOf(data.length);
        URL url = new URL(urlPath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setReadTimeout(5000);
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
        conn.setRequestProperty("Content-Length", lengthStr);
        OutputStream outStream = conn.getOutputStream();
        outStream.write(data);
        outStream.flush();
        outStream.close();
        System.out.println(conn.getResponseCode());
        System.out.println(conn.getResponseMessage());
        if(conn.getResponseCode() == 200){
            //如果有回應
            InputStream inputStream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            for(String line = null; (line = reader.readLine())!=null;){
                System.out.println(line);
            }
            reader.close();
            return true;
        }
        return false;
    }
}
