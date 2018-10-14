package com.example.gtu001.qrcodemaker.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class PingUtil extends Activity {

    private static String ping(String str) {
        String resault = "";
        Process p;
        try { //ping -c 3 -w 100 中 ，-c 是指ping的次数 3是指ping 3次 ，-w 100 以秒为单位指定超时间隔，是指超时时间为100秒
            p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + str);
            int status = p.waitFor();
            InputStream input = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = in.readLine()) != null) {
                buffer.append(line);
            }
            System.out.println("Return ============" + buffer.toString());
            if (status == 0) {
                resault = "success";
            } else {
                resault = "faild";
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return resault;
    }

    public static class NetPing extends AsyncTask<String, String, String> {

        String host;

        public NetPing(String host) {
            this.host = host;
        }

        @Override
        protected String doInBackground(String... params) {
            String s = "";
            s = ping(this.host);
            Log.i("ping", s);
            return s;
        }
    }
}
