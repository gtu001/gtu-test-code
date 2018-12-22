package com.example.gtu001.qrcodemaker.common;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.webkit.MimeTypeMap;

import org.apache.commons.lang3.StringUtils;

public class DownloadHelper {
    private static final String TAG = DownloadHelper.class.getSimpleName();

    private static final DownloadHelper _INST = new DownloadHelper();

    public static DownloadHelper getInstance(){
        return _INST;
    }

    private String getDownloadName(String name) {
        return StringUtils.defaultString(name).replaceAll("[\\/\\:\\*\\?\\\"\\<\\>\\|\\#]", "");
    }

    public void download(String title, String description, String url, String subType, Context context) {
        String filename = getDownloadName(title) + "." + subType;
        Log.v(TAG, "Download : " + filename);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(title);
        request.setDescription(description);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);

            /*在默认的情况下，通过Download Manager下载的文件是不能被Media Scanner扫描到的 。
        进而这些下载的文件（音乐、视频等）就不会在Gallery 和  Music Player这样的应用中看到。
        为了让下载的音乐文件可以被其他应用扫描到，我们需要调用Request对象的
         */
        request.allowScanningByMediaScanner();

        /*如果我们希望下载的文件可以被系统的Downloads应用扫描到并管理，
        我们需要调用Request对象的setVisibleInDownloadsUi方法，传递参数true。*/
        request.setVisibleInDownloadsUi(true);

        //设置请求的Mime
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        request.setMimeType(mimeTypeMap.getMimeTypeFromExtension(url));

        //设置是否允许漫游网络 建立请求 默认true
        request.setAllowedOverRoaming(true);

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//            downloadManager.addCompletedDownload(title, description, isMediaScannerScannable, mimeType, path, length, showNotification);
        long mReference = downloadManager.enqueue(request);

            /*
        下载管理器中有很多下载项，怎么知道一个资源已经下载过，避免重复下载呢？
        我的项目中的需求就是apk更新下载，用户点击更新确定按钮，第一次是直接下载，
        后面如果用户连续点击更新确定按钮，就不要重复下载了。
        可以看出来查询和操作数据库查询一样的
         */
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(mReference);
        Cursor cursor = downloadManager.query(query);
        if (!cursor.moveToFirst()) {
            // 没有记录
        } else {
            //有记录
        }
    }
}
