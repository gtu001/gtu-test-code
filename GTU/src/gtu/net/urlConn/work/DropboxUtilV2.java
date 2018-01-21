package gtu.net.urlConn.work;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DownloadBuilder;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderErrorException;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.UploadBuilder;
import com.dropbox.core.v2.files.UploadUploader;
import com.dropbox.core.v2.files.WriteMode;
import com.dropbox.core.v2.users.FullAccount;

public class DropboxUtilV2 {

    public static void main(String[] args) throws ListFolderErrorException, DbxException, FileNotFoundException, IOException {
        DropboxUtilV2 t = new DropboxUtilV2();

        DbxClientV2 client = t.getClient("3y3nIS0p6KoAAAAAAAAsnT59ymBJxJMRFUjx2pIdE0UfaY4S5PSQJYgs5UsXOTbI");

//        t.listFiles("/english_prop", client);
//
//        t.download("", new FileOutputStream(new File("C:\\Users\\gtu001\\Desktop\\x.txt")), client);/// english_prop/new_word_20170928175618.properties
//
//        t.upload("/x/x.txt", new FileInputStream(new File("C:\\Users\\gtu001\\Desktop\\x.txt")), client);
//
//        t.delete("/x/x.txt", client);
        
        isAccessTokenOk("3y3nIS0p6KoAAAAAAAAsnT59ymBJxJMRFUjx2pIdE0UfaY4S5PSQJYgs5UsXOTbI");

        System.out.println("done...");
    }

    public static boolean delete(String path, DbxClientV2 client) {
        try {
            Metadata meta = client.files().delete(path);
            System.out.println("del : " + meta.toStringMultiline());
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
//            return false;
        }
    }

    public static boolean upload(String path, InputStream in, DbxClientV2 client) {
        try {
            UploadBuilder uploadBuilder = client.files().uploadBuilder(path);
            uploadBuilder.withMode(WriteMode.ADD);
            uploadBuilder.uploadAndFinish(in);
            UploadUploader builder = uploadBuilder.start();
            System.out.println("upload done : " + path);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
//            return false;
        }
    }

    public static boolean download(String path, FileOutputStream out, DbxClientV2 client) {
        try {
            DownloadBuilder downloadBuilder = client.files().downloadBuilder(path);
            downloadBuilder.download(out);
            DbxDownloader<FileMetadata> start = downloadBuilder.start();
            FileMetadata result = start.getResult();
            System.out.println("download rev : " + result.getRev());
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
//            return false;
        }
    }

    public static Map<String,String> listFiles(String path, DbxClientV2 client) {
        Map<String,String> fileMap = new LinkedHashMap<String,String>();
        try {
            ListFolderResult result = client.files().listFolder(path);
            List<Metadata> entries = result.getEntries();
            for (int ii = 0; ii < entries.size(); ii++) {
                Metadata ent = entries.get(ii);
                System.out.println(ent.getPathDisplay());
                fileMap.put(ent.getName(), ent.getPathDisplay());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return fileMap;
    }

    public static DbxClientV2 getClient(String accessToken) {
        DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
        DbxClientV2 client = new DbxClientV2(config, accessToken);
        return client;
    }

    public static boolean isAccessTokenOk(String accessToken) {
        try {
            DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
            DbxClientV2 client = new DbxClientV2(config, accessToken);
            FullAccount account = client.users().getCurrentAccount();
            System.out.println("name : " + account.getName());
            System.out.println("country : " + account.getCountry());
            System.out.println("accountId : " + account.getAccountId());
            System.out.println("email : " + account.getEmail());
            return true;
        }catch(Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
