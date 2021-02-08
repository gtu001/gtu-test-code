package gtu.google.bookmark;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import gtu.file.FileUtil;

public class GoogleBookmarkService {

    public static void main(String[] args) {
        GoogleBookmarkService t = new GoogleBookmarkService();
        boolean loginResult = t.googleLogin();
        System.out.println("登入結果 : " + loginResult);
    }

    // https://my.oschina.net/elinac/blog/698050
    // google bookmark api
    // https://www.google.com/bookmarks/?output=xml&num=10000000
    // https://blog.csdn.net/zidangtou515/article/details/83700570

    CloseableHttpClient mClient = HttpClients.createDefault();
    String mSig;

    public String getHtmlContent(InputStream inputStream) {
        StringBuffer sb = new StringBuffer();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            for (String line = null; (line = reader.readLine()) != null;) {
                sb.append(line);
            }
            FileUtil.saveToFile(new File(FileUtil.DESKTOP_PATH, "____tmp.html"), sb.toString(), "UTF8");
        } catch (Exception ex) {
            throw new RuntimeException("getSignature ERR : " + ex.getMessage(), ex);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                }
            }
        }
        return sb.toString();
    }

    /**
     * 登录google账户
     */
    private boolean googleLogin() {
        // HttpPost post = new
        // HttpPost("https://www.google.com/accounts/ServiceLoginAuth");
        HttpPost post = new HttpPost("https://accounts.google.com/ServiceLoginAuth");

        post.addHeader("Cookie", "GALX=zidangtou");

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("GALX", "zidangtou"));
        nvps.add(new BasicNameValuePair("Email", "gtu001@gmail.com"));
        nvps.add(new BasicNameValuePair("Passwd", "luv90cxc048c"));

        try {
            post.setEntity(new UrlEncodedFormEntity(nvps));
            HttpResponse response = mClient.execute(post);

            System.out.println(response.getStatusLine());
            System.out.println(response.getStatusLine().getStatusCode());
            System.out.println(getHtmlContent(response.getEntity().getContent()));

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK && validateLogin()) {
                return true;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean validateLogin() {
        return true;
    }

    public String getSignature(InputStream inputStream) {
        String html = getHtmlContent(inputStream);
        Pattern ptn = Pattern.compile("<smh:signature>(\\w+?)</smh:signature>", Pattern.CASE_INSENSITIVE);
        Matcher mth = ptn.matcher(html);
        if (mth.find()) {
            return mth.group(1);
        }
        throw new RuntimeException("找不到Signature : " + html);
    }

    /**
     * 获取服务器书签数据，获取sig参数
     */
    private void getBookmarks() {
        HttpGet get = new HttpGet("https://www.google.com/bookmarks/lookup?output=rss");
        try {
            HttpResponse response = mClient.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // Log.d("getBookmarks",
                // EntityUtils.toString(response.getEntity()));
                mSig = getSignature(response.getEntity().getContent());
                System.out.println("getSignature mSig = " + mSig);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加单个书签
     */
    private void addBookmark() {
        HttpPost post = new HttpPost("https://www.google.com/bookmarks/mark");
        ArrayList<BasicNameValuePair> bnvps = new ArrayList<BasicNameValuePair>();
        bnvps.add(new BasicNameValuePair("bkmk", "www.ceshi.com"));
        bnvps.add(new BasicNameValuePair("title", "title_ceshi"));
        bnvps.add(new BasicNameValuePair("annotation", "anno_ceshi"));
        bnvps.add(new BasicNameValuePair("labels", "label_ceshi"));
        try {
            post.setEntity(new UrlEncodedFormEntity(bnvps));
            mClient.execute(post);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量添加多个书签
     */
    private void batchAddBookmarks() {
        HttpPost post = new HttpPost("https://www.google.com/bookmarks/mark?op=upload_selection&sig=" + mSig);
        ArrayList<BasicNameValuePair> bnvps = new ArrayList<BasicNameValuePair>();

        bnvps.add(new BasicNameValuePair("bm_1", "X"));
        bnvps.add(new BasicNameValuePair("url_1", "www.ceshi1.com"));
        bnvps.add(new BasicNameValuePair("ttl_1", "ceshi1"));
        bnvps.add(new BasicNameValuePair("lbl_1", "111"));

        bnvps.add(new BasicNameValuePair("bm_2", "X"));
        bnvps.add(new BasicNameValuePair("url_2", "www.ceshi2.com"));
        bnvps.add(new BasicNameValuePair("ttl_2", "ceshi2"));
        bnvps.add(new BasicNameValuePair("lbl_2", "222"));
        try {
            post.setEntity(new UrlEncodedFormEntity(bnvps));
            mClient.execute(post);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除书签
     */
    private void deleteBookmark() {
        HttpGet get = new HttpGet("https://www.google.com/bookmarks/mark?dlq=书签id&sig=" + mSig);
        try {
            mClient.execute(get);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
