package gtu.net.urlConn;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import sun.net.www.protocol.http.Handler;

/**
 * 檢查門號是否為遠傳有效門號
 * 
 * @author Steve Tien
 * @version 1.0, Oct 9, 2009
 */
public class COCheckMobileIsValidPC {

    public static void main(String[] args) throws Exception {
        COCheckMobileIsValidPC test = new COCheckMobileIsValidPC();
        test.checkFetMobile("http://www.google.com.tw");
    }

    private String checkFetMobile(String urlStr) throws Exception {

        StringBuilder sigRequestBuilder = new StringBuilder();
        StringBuilder xmlStringBuilder = new StringBuilder();

        OutputStreamWriter outputStreamWriter = null;
        BufferedReader xmlReader = null;

        HttpURLConnection urlConnection;

        try {

            sigRequestBuilder.append("#hl=");
            sigRequestBuilder.append(URLEncoder.encode("zh-TW", "UTF-8"));
            sigRequestBuilder.append("&");
            sigRequestBuilder.append("q=");
            sigRequestBuilder.append(URLEncoder.encode("test", "UTF-8"));

            URL url = new URL(urlStr);

            String urlProtocol = url.getProtocol();
            String urlHost = url.getHost();
            int urlPort = url.getPort();
            String urlFile = url.getFile();
            System.out.println(String.format("protocal = %s, host = %s, port = %s, file = %s", urlProtocol, urlHost,
                    urlPort, urlFile));

            Handler urlHandler = new sun.net.www.protocol.http.Handler();
            url = new URL(urlProtocol, urlHost, urlPort, urlFile, urlHandler);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setConnectTimeout(15000);
            urlConnection.setDoOutput(true);

            logHttpUrlConnection(urlConnection);

            System.out.println("file.encoding = " + System.getProperty("file.encoding"));

            System.out.println("寫入 = " + sigRequestBuilder.toString());
            outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8");
            System.out.println("outputStreamEncoding = " + outputStreamWriter.getEncoding());
            outputStreamWriter.write(sigRequestBuilder.toString());
            outputStreamWriter.flush();
            outputStreamWriter.close();

            int httpStatusCode = urlConnection.getResponseCode();
            System.out.println("httpStatusCode = " + httpStatusCode);

            InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream(), "BIG5");
            System.out.println("inputStreamEncoding = " + inputStreamReader.getEncoding());
            xmlReader = new BufferedReader(inputStreamReader);

            String xmlLine;
            while ((xmlLine = xmlReader.readLine()) != null) {
                xmlStringBuilder.append(xmlLine);
            }
            xmlReader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (null != outputStreamWriter)
                outputStreamWriter.close();
            if (null != xmlReader)
                xmlReader.close();
        }
        return xmlStringBuilder.toString();
    }

    private void logHttpUrlConnection(HttpURLConnection urlConnection) {
        System.out.println("# logHttpUrlConnection...");
        System.out.println("HttpURLConnection = " + urlConnection);
        boolean defaultAllowUserInteraction = HttpURLConnection.getDefaultAllowUserInteraction();
        System.out.println("defaultAllowUserInteraction = " + defaultAllowUserInteraction);
        boolean followRedirects = HttpURLConnection.getFollowRedirects();
        System.out.println("followRedirects = " + followRedirects);
        boolean allowUserInteraction = urlConnection.getAllowUserInteraction();
        System.out.println("allowUserInteraction = " + allowUserInteraction);
        boolean instanceFollowRedirects = urlConnection.getInstanceFollowRedirects();
        System.out.println("instanceFollowRedirects = " + instanceFollowRedirects);
        int connectTimeout = urlConnection.getConnectTimeout();
        System.out.println("connectTimeout = " + connectTimeout);
        int readTimeout = urlConnection.getReadTimeout();
        System.out.println("readTimeout = " + readTimeout);
        boolean defaultUseCaches = urlConnection.getDefaultUseCaches();
        System.out.println("defaultUseCaches = " + defaultUseCaches);
        boolean doInput = urlConnection.getDoInput();
        System.out.println("doInput = " + doInput);
        boolean doOutput = urlConnection.getDoOutput();
        System.out.println("doOutput = " + doOutput);
        System.out.println("# logHttpUrlConnection...");
    }
}
