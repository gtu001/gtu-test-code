package gtu.net.urlConn.work;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONObject;

public class Imgur_UploadTest001 {

    private static final String UPLOAD_URL = "https://api.imgur.com/3/image";
    private static final String clientId = "a2686b09bad8534";
    private static final String clientSecret = "60c5cc0358429cea1807367381cefd2ce6389209";
    
    public static void main(String[] args) throws FileNotFoundException{
        Imgur_UploadTest001 t = new Imgur_UploadTest001();
        t.doInBackground(new File("D:/oVL0DN.gif"));
    }

    protected String doInBackground(File file) throws FileNotFoundException {
        InputStream imageIn = new FileInputStream(file);

        HttpURLConnection conn = null;
        InputStream responseIn = null;

        try {
            conn = (HttpURLConnection) new URL(UPLOAD_URL).openConnection();
            conn.setDoOutput(true);

            // conn.setRequestProperty("Authorization", "Bearer " +
            // accessToken);
            conn.setRequestProperty("Authorization", "Client-ID " + clientId);

            OutputStream out = conn.getOutputStream();
            copy(imageIn, out);
            out.flush();
            out.close();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                responseIn = conn.getInputStream();
                return onInput(responseIn);
            } else {
                System.out.println("responseCode=" + conn.getResponseCode());
                responseIn = conn.getErrorStream();
                StringBuilder sb = new StringBuilder();
                Scanner scanner = new Scanner(responseIn);
                while (scanner.hasNext()) {
                    sb.append(scanner.next());
                }
                System.out.println("error response: " + sb.toString());
                return null;
            }
        } catch (Exception ex) {
            System.err.println("Error during POST" + ex.getMessage());
            ex.printStackTrace();
            return null;
        } finally {
            try {
                responseIn.close();
            } catch (Exception ignore) {
            }
            try {
                conn.disconnect();
            } catch (Exception ignore) {
            }
            try {
                imageIn.close();
            } catch (Exception ignore) {
            }
        }
    }

    private static int copy(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[8192];
        int count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    protected String onInput(InputStream in) throws Exception {
        StringBuilder sb = new StringBuilder();
        Scanner scanner = new Scanner(in);
        while (scanner.hasNext()) {
            sb.append(scanner.next());
        }
        
        System.out.println(sb);

        JSONObject root = new JSONObject(sb.toString());
        String id = root.getJSONObject("data").getString("id");
        String deletehash = root.getJSONObject("data").getString("deletehash");
        System.out.println("new imgur url: http://imgur.com/" + id + " (delete hash: " + deletehash + ")");
        return id;
    }

}
