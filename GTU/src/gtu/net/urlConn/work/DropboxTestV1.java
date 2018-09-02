package gtu.net.urlConn.work;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import com.dropbox.core.DbxWriteMode;


/**
 * @deprecated
 * version 1.7 or 1.8.2 
 * 
 *      <!-- dropbox -->
        <!-- <dependency>
            <groupId>com.dropbox.core</groupId>
            <artifactId>dropbox-core-sdk</artifactId>
            <version>1.7</version>
            <exclusions>
                <exclusion>
                    <groupId>com.fasterxml.jackson.core</groupId>
                    <artifactId>jackson-core</artifactId>
                </exclusion>
            </exclusions>
        </dependency> -->
 */
public class DropboxTestV1 {

    public static void main(String[] args) throws Exception {
        DropboxTestV1 t = new DropboxTestV1();
//        t.authorize();
//        t.upload(t.getClient());
//        t.listFile(t.getClient());
//        t.download(t.getClient());
        System.out.println(t.getAccessToken());
        System.out.println("done...");
    }
    
    public void download(DbxClient client) throws DbxException, IOException{
        FileOutputStream outputStream = new FileOutputStream("92358.jpg");
        try {
            DbxEntry.File downloadedFile = client.getFile("/92358.jpg", null,
                outputStream);
            System.out.println("Metadata: " + downloadedFile.toString());
        } finally {
            outputStream.close();
        }
    }
    
    public DbxClient getClient(){
        String accessToken = "3y3nIS0p6KoAAAAAAAAsnT59ymBJxJMRFUjx2pIdE0UfaY4S5PSQJYgs5UsXOTbI";
        DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0", Locale.getDefault().toString());
        DbxClient client = new DbxClient(config, accessToken);
        return client;
    }
    
    public void listFile(DbxClient client) throws DbxException{
        DbxEntry.WithChildren listing = client.getMetadataWithChildren("/");
        System.out.println("Files in the root path:");
        for (DbxEntry child : listing.children) {
            System.out.println("    " + child.name + ": " + child.toString());
        }
    }
    
    public void upload(DbxClient client) throws DbxException, IOException{
        File inputFile = new File("D:\\92358.jpg");
        FileInputStream inputStream = new FileInputStream(inputFile);
        try {
            DbxEntry.File uploadedFile = client.uploadFile("/92358.jpg",
                DbxWriteMode.add(), inputFile.length(), inputStream);
            System.out.println("Uploaded: " + uploadedFile.toString());
        } finally {
            inputStream.close();
        }
    }

    public String getAccessToken() throws IOException, DbxException {
        final String APP_KEY = "bgj27yau9ctu4je";
        final String APP_SECRET = "bbd65xo3euuj2mg";

        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

        DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0", Locale.getDefault().toString());
        DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);

        String authorizeUrl = webAuth.start();
        System.out.println("1. Go to: " + authorizeUrl);
        System.out.println("2. Click \"Allow\" (you might have to log in first)");
        System.out.println("3. Copy the authorization code.");
        
        String code = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
        
        DbxAuthFinish authFinish = webAuth.finish(code);
        String accessToken = authFinish.accessToken;
        
        System.out.println("accessToken : "+accessToken);
        
        //<input type="text" class="auth-box" value="3y3nIS0p6KoAAAAAAAAvbEe4Jb9H5dk1AyFVR0Z1Y58" id="auth-code-input" />
        
        DbxClient client = new DbxClient(config, accessToken);
//        System.out.println("Linked account: " + client.getAccountInfo().displayName);
        return accessToken;
    }

}
