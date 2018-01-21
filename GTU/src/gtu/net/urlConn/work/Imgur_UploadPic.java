package gtu.net.urlConn.work;

import gtu.json.ToJsonObject;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
//import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.poi.util.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class Imgur_UploadPic {

    // {"data":{"id":"XCPYpG2","title":"test","description":"test","datetime":1448872990,"type":"image\/gif","animated":true,"width":460,"height":345,"size":888899,"views":0,"bandwidth":0
    // ,"vote":null,"favorite":false,"nsfw":null,"section":null,"account_url":null,"account_id":0,
    // "comment_preview":null,"deletehash":"IX3D9D4qAd9LFYg","name":"test","gifv":"http:\/\/i.imgur.com\/XCPYpG2.gifv","webm":"http:\/\/i.imgur.com\/XCPYpG2.webm","mp4":"http:\/\/i.imgur.com\/XCPYpG2.mp4","link":"http:\/\/i.imgur.com\/XCPYpG2.gif","looping":true},"success":true,"status":200}
    // {"data":{"id":"XCPYpG2","title":"test","description":"test","datetime":1448872990,"type":"image\/gif","animated":true,"width":460,"height":345,"size":888899,"views":2,"bandwidth":1777798,"vote":null,"favorite":false,"nsfw":null,"section":null,"account_url":null,"account_id":null,"comment_preview":null,
    // "gifv":"http:\/\/i.imgur.com\/XCPYpG2.gifv","webm":"http:\/\/i.imgur.com\/XCPYpG2.webm","mp4":"http:\/\/i.imgur.com\/XCPYpG2.mp4","link":"http:\/\/i.imgur.com\/XCPYpG2.gif","looping":true},"success":true,"status":200}

    public static void main(String[] args) throws Exception {
        Imgur_UploadPic util = Imgur_UploadPic.getInstance();
        // String client_id = "5386aab829e87c1";
        // String client_secret = "40a5318beed25f9a70dbea0af86aac5c2b66a272";
        String client_id = "a2686b09bad8534";
        String client_secret = "60c5cc0358429cea1807367381cefd2ce6389209";
        File imageFile = new File("D:/gtu001_dropbox/Dropbox/guava/畫圖/強圖/7ccfe786bfd2d3c4efdee919abe6e293.jpg");
        System.out.println(util.getPictureInformation(client_id, "XCPYpG2"));
        System.out.println(util.updateImageInformation(client_id, "IX3D9D4qAd9LFYg"));
        // System.out.println(util.deletePicture(client_id, "IX3D9D4qAd9LFYg"));
         System.out.println(util.uploadPicture1(client_id, imageFile));
        System.out.println(util.uploadPicture2(client_id, imageFile));
    }

    private Imgur_UploadPic() {
    }

    private static final Imgur_UploadPic _INST = new Imgur_UploadPic();

    public static Imgur_UploadPic getInstance() {
        return _INST;
    }

    /**
     * 取得圖片資訊, 但無法取得deletehash
     */
    public ImgurData getPictureInformation(String clientID, String id) throws Exception {
        URL url;
        url = new URL("https://api.imgur.com/3/image/" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Client-ID " + clientID);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.connect();

        StringBuilder stb = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            stb.append(line).append("\n");
        }
        rd.close();

        ImgurData data = parseToBean(stb.toString());
        // return stb.toString();
        return data;
    }

    private static class ImgurData {
        String title;
        String description;
        String datetime;
        String type;
        boolean animated;
        int width;
        int height;
        long size;
        int views;
        String deletehash;
        String name;
        String link;
        boolean looping;

        @Override
        public String toString() {
            return "ImgurData [title=" + title + ", description=" + description + ", datetime=" + datetime + ", type=" + type + ", animated=" + animated + ", width=" + width + ", height=" + height
                    + ", size=" + size + ", views=" + views + ", deletehash=" + deletehash + ", name=" + name + ", link=" + link + ", looping=" + looping + "]";
        }
    }

    private ImgurData parseToBean(String strVal) throws JSONException {
        JSONObject obj = new JSONObject(strVal);
        if (obj.getInt("status") == 200 && obj.getBoolean("success")) {
            JSONObject data = (JSONObject) obj.get("data");
            ImgurData data2 = new ImgurData();
            ToJsonObject.setBeanFromJsonObject(data, data2);
            return data2;
        }
        return null;
    }

    /**
     * 更新圖片資訊
     */
    public String updateImageInformation(String clientID, String deletehash) throws Exception {
        URL url;
        url = new URL("https://api.imgur.com/3/image/" + deletehash);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Client-ID " + clientID);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.connect();

        String data = URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode("okok1", "UTF-8") + "&" + URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode("okok2", "UTF-8");
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.flush();
        wr.close();

        StringBuilder stb = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            stb.append(line).append("\n");
        }
        rd.close();
        return stb.toString();
    }

    /**
     * 刪除圖片
     */
    public String deletePicture(String clientID, String deletehash) throws Exception {
        URL url;
        url = new URL("https://api.imgur.com/3/image/" + deletehash);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("Authorization", "Client-ID " + clientID);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.connect();

        StringBuilder stb = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            stb.append(line).append("\n");
        }
        rd.close();
        return stb.toString();
    }

    public String uploadPicture1(String clientID, File imageFile) throws Exception {
        URL url;
        url = new URL("https://api.imgur.com/3/image");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        String dataImage = getPicturePostInfo(imageFile);

        String data = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(dataImage, "UTF-8");

        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Client-ID " + clientID);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        conn.connect();
        StringBuilder stb = new StringBuilder();
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.flush();

        // Get the response
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            stb.append(line).append("\n");
        }
        wr.close();
        rd.close();

        return stb.toString();
    }

    public String uploadPicture2(String clientID, File imageFile) throws Exception {
        // create needed strings
        String address = "https://api.imgur.com/3/image";

        // Create HTTPClient and post
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(address);

        // create base64 image
        File file = imageFile;

        try {
            // read image
            String dataImage = getPicturePostInfo(imageFile);

            // add header
            post.addHeader("Authorization", "Client-ID " + clientID);
            // add image
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("image", dataImage));

            nameValuePairs.add(new BasicNameValuePair("title", "test"));
            nameValuePairs.add(new BasicNameValuePair("description", "test"));
            nameValuePairs.add(new BasicNameValuePair("name", "test"));
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // execute
            HttpResponse response = client.execute(post);

            // read response
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder sb = new StringBuilder();
            for (String line = null; (line = rd.readLine()) != null;) {
                sb.append(line + "\n");
            }

            return sb.toString();
        } catch (Exception e) {
            return "error: " + e.toString();
        }
    }

    private String getPicturePostInfo(File imageFile) throws IOException {
        String dataImage = "";
        if (imageFile.getName().toLowerCase().endsWith("gif")) {
            InputStream tmpIS = new FileInputStream(imageFile);
            byte[] tmp = IOUtils.toByteArray(tmpIS);
            dataImage = new Base64().encodeAsString(tmp);
        } else {
            BufferedImage image = ImageIO.read(imageFile);
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            ImageIO.write(image, "BNG", byteArray);
            byte[] byteImage = byteArray.toByteArray();
            dataImage = new Base64().encodeAsString(byteImage);
        }
        return dataImage;
    }
}
