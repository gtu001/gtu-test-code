package com.example.englishtester.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DeleteErrorException;
import com.dropbox.core.v2.files.DownloadBuilder;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderErrorException;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.UploadBuilder;
import com.dropbox.core.v2.files.UploadErrorException;
import com.dropbox.core.v2.files.UploadUploader;
import com.dropbox.core.v2.files.WriteMode;
import com.dropbox.core.v2.users.FullAccount;

public class DropboxUtilV2 {

    public static void main(String[] args) throws ListFolderErrorException, DbxException, FileNotFoundException, IOException {
        DropboxUtilV2 t = new DropboxUtilV2();
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
            // return false;
        }
    }

    public static boolean exists(String path, DbxClientV2 client) {
        try {
            return true;
        } catch (Exception ex) {
            if (ex.getClass().getName().equals("com.dropbox.core.v2.files.GetMetadataErrorException") && //
                    ex.getMessage().contains("\"path\":\"not_found\"")) {
                return false;
            }
            throw new RuntimeException("exists ERR : " + ex.getMessage(), ex);
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
            // return false;
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
            // return false;
        }
    }

    public static Map<String, String> listFiles(String path, DbxClientV2 client) {
        Map<String, String> fileMap = new LinkedHashMap<String, String>();
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

    public static List<DropboxUtilV2_DropboxFile> listFilesV2(String path, DbxClientV2 client) {
        List<DropboxUtilV2_DropboxFile> fileMap = new ArrayList<DropboxUtilV2_DropboxFile>();
        try {
            ListFolderResult result = client.files().listFolder(path);
            List<Metadata> entries = result.getEntries();
            for (int ii = 0; ii < entries.size(); ii++) {
                Metadata ent = entries.get(ii);
                fileMap.add(new DropboxUtilV2_DropboxFile(ent));
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
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static class DropboxUtilV2_DropboxFile {
        private String name;
        private String fullPath;
        private boolean isFolder = false;
        private long size = -1;
        private long serverModify = -1;
        private long clientModify = -1;

        public DropboxUtilV2_DropboxFile(Metadata meta) {
            this.name = meta.getName();
            this.fullPath = meta.getPathDisplay();
            if (meta instanceof com.dropbox.core.v2.files.FolderMetadata) {
                this.isFolder = true;
            }
            if (meta instanceof com.dropbox.core.v2.files.FileMetadata) {
                com.dropbox.core.v2.files.FileMetadata m2 = (com.dropbox.core.v2.files.FileMetadata) meta;
                size = m2.getSize();
                serverModify = m2.getServerModified().getTime();
                clientModify = m2.getClientModified().getTime();
            }
        }

        public String getName() {
            return name;
        }

        public String getFullPath() {
            return fullPath;
        }

        public boolean isFolder() {
            return isFolder;
        }

        public long getSize() {
            return size;
        }

        public long getServerModify() {
            return serverModify;
        }

        public long getClientModify() {
            return clientModify;
        }
    }
}
