package gtu.net.urlConn.work;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import gtu.file.FileUtil;

public class StockExcelDownloadTest {

    public static void main(String[] args) throws Exception {
        StockExcelDownloadTest test = new StockExcelDownloadTest();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//        String yyyyMMdd = sdf.format(new Date());
        String yyyyMMdd = "20171003";
        String resultStr = test.getCsv(yyyyMMdd);
        System.out.println("result = [" + resultStr + "]");
        FileUtil.saveToFile(new File(FileUtil.DESKTOP_DIR, "stock_" + yyyyMMdd + ".csv"), resultStr, "big5");
        System.out.println("done...");
    }

    private String getCsv(String yyyyMMdd) throws Exception {
        StringBuffer sb = new StringBuffer();
        BufferedReader reader = null;
        try {
            URL url = new URL("http://www.twse.com.tw/exchangeReport/MI_INDEX?response=csv&date=" + yyyyMMdd + "&type=ALLBUT0999");
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(url.toURI());
            postRequest.addHeader("Authorization", "Client-ID " + "a2686b09bad8534");

            postRequest.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=Big5");

            List<NameValuePair> postParams = new ArrayList<NameValuePair>();
            postParams.add(new BasicNameValuePair("BUSINESSCODE", "BA"));
            postRequest.setEntity(new UrlEncodedFormEntity(postParams, "Big5"));

            HttpResponse response = httpClient.execute(postRequest);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Runtime Failed HTTP error code : " + response.getStatusLine().getStatusCode());
            }

            reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "big5"));
            String output = null;
            while ((output = reader.readLine()) != null) {
                System.out.println(output);
                sb.append(output + "\n");
            }
            httpClient.getConnectionManager().shutdown();
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            reader.close();
        }
    }
}
