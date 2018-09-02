package gtu.sms;

import java.net.*;
import java.io.*;

/**
 * 廠商 BulkSMS：
 * 
 * // 官方網站：http://www.bulksms.com/
 * 
 * // API 可參考 ： http://developer.bulksms.com/eapi/
 */
public class SendUnicodeSms {

    static public String stringToHex(String s) {
        char[] chars = s.toCharArray();
        String next;
        StringBuffer output = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            next = Integer.toHexString((int) chars[i]);
            // Unfortunately, toHexString doesn't pad with zeroes, so we have
            // to.
            for (int j = 0; j < (4 - next.length()); j++) {
                output.append("0");
            }
            output.append(next);
        }
        return output.toString();
    }

    static public void main(String[] args) {
        try {
            String data = "";
            data += "username=" + URLEncoder.encode("gtu001", "ISO-8859-1");
            data += "&password=" + URLEncoder.encode("123474736", "ISO-8859-1");
            data += "&message=" + stringToHex("This is a test: ☺ \nArabic: شصض\nChinese: 本网");
            data += "&dca=16bit";
            data += "&want_report=1";
            data += "&msisdn=886954006788";

            URL url = new URL("http://bulksms.2way.co.za/eapi/submission/send_sms/2/2.0");

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                // Print the response output...
                System.out.println(line);
            }
            wr.close();
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
