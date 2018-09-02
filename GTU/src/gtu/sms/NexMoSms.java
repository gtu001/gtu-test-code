package gtu.sms;

import java.net.*;
import java.io.*;

/**
 * 廠商 nexmo：
 * 
 * // 官方網站：https://www.nexmo.com/
 */
public class NexMoSms {

    public static void main(String[] args) {
        try {
            // Construct data
            String data = "";

            data += "api_key=94b66efc";
            data += "&api_secret=4cedbd5c4ec3be4b";
            data += "&from=NEXMO";
            data += "&to=886954006788";
            data += "&type=unicode";
            data += "&text=" + URLEncoder.encode("銘言：沒有蘿莉，沒有未來。", "utf-8");

            System.out.println(data);

            // Send data
            // Please see the FAQ regarding HTTPS (port 443) and HTTP (port
            // 80/5567)
            URL url = new URL("https://rest.nexmo.com/sms/json");

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String json = rd.readLine();
            System.out.println(json);

            wr.close();
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
