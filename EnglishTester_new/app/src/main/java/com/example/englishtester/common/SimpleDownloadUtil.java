package com.example.englishtester.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SimpleDownloadUtil {

    public static long downloadFile(String urlStr, File outputFile) {

        long startTime = System.currentTimeMillis();
        URL url = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        FileOutputStream os = null;
        byte[] buff = new byte[1024 * 4];
        long size = 0;
        int r = 0;

        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            if (conn == null)
                return -1;

            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setDoInput(true);

            is = conn.getInputStream();
            os = new FileOutputStream(outputFile);
            while ((r = is.read(buff)) > 0) {
                os.write(buff, 0, r);
                size += r;
            }
            os.flush();
            os.close();

            return size;
        } catch (Exception e) {
            throw new RuntimeException("downloadFile ERR : " + e.getMessage(), e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e) {
                }
            }

            System.out.println("downloadFile Cost : " + (System.currentTimeMillis() - startTime));
        }
    }
}
