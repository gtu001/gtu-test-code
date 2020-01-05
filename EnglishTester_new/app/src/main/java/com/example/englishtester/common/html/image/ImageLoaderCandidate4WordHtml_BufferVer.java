package com.example.englishtester.common.html.image;

import android.graphics.Bitmap;

import com.example.englishtester.TxtReaderActivity;
import com.example.englishtester.common.Log;
import com.example.englishtester.common.OOMHandler;
import com.example.englishtester.common.OnlinePicLoader;
import com.example.englishtester.common.html.parser.HtmlWordParser;
import com.example.englishtester.common.interf.ITxtReaderActivityDTO;
import com.example.englishtester.common.txtbuffer.base.TxtBufferViewerMainHandler;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.URLDecoder;

public class ImageLoaderCandidate4WordHtml_BufferVer extends ImageLoaderCandidateAbstract implements IImageLoaderCandidate {

    private static final String TAG = ImageLoaderCandidate4WordHtml_BufferVer.class.getSimpleName();

    String srcData;
    String altData;
    boolean isGifFile;
    File localFile;
    boolean isNeedLoadImage;
    OnlinePicLoader onlinePicLoader;
    TxtBufferViewerMainHandler.TxtBufferDTO dto;

    public static final Integer IMAGE_SIMPLE_SCALE = 1;

    public ImageLoaderCandidate4WordHtml_BufferVer(String srcData, String altData, boolean isNeedLoadImage, OnlinePicLoader onlinePicLoader, ITxtReaderActivityDTO dto) {
        this.srcData = srcData;
        this.altData = altData;
        this.isGifFile = isGif(srcData);
        this.isNeedLoadImage = isNeedLoadImage;
        this.dto = (TxtBufferViewerMainHandler.TxtBufferDTO) dto;
        this.onlinePicLoader = onlinePicLoader;
        try {
            this.localFile = getLocalFile(srcData);
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage(), ex);
        }
    }

    private File getLocalFile(String filename) {
        try {
            String realName = URLDecoder.decode(filename, HtmlWordParser.WORD_HTML_ENCODE);
            File localPicFile = new File(dto.getCurrentHtmlFile().getParentFile(), realName);
            if (localPicFile.exists() && localPicFile.canRead()) {
                Log.v(TAG, "[check local pic path]" + localPicFile);
                return localPicFile;
            }

            if (dto.getDropboxPicDir() == null && realName.contains("/")) {
                String middleDir = realName.substring(0, realName.lastIndexOf("/"));
//                dto.setDropboxPicDir(new File(dto.getCacheDir(), middleDir));
            }

            String imageFileName = realName;
            if (realName.contains("/")) {
                imageFileName = realName.substring(realName.lastIndexOf("/"));
            }
            File dropboxPic = new File(dto.getDropboxPicDir(), imageFileName);
            if (dropboxPic.exists() && dropboxPic.canRead()) {
                Log.v(TAG, "[check local pic path]" + localPicFile);
                return dropboxPic;
            }

            throw new Exception("查無檔案 : " + filename);
        } catch (Exception ex) {
            throw new RuntimeException("getLocalFile ERR : " + ex.getMessage(), ex);
        }
    }

    @Override
    public boolean isGifFile() {
        return isGifFile;
    }

    public Bitmap getResult(int fixWidth) {
        if (!isNeedLoadImage) {
            return onlinePicLoader.getNotfound404();
        }

        //local
        Bitmap tmp = null;
        if (localFile != null && localFile.isFile()) {
            try {
                tmp = OOMHandler.fixPicScaleFixScreenWidth_new(OOMHandler.new_decode(localFile, IMAGE_SIMPLE_SCALE), fixWidth);
            } catch (Exception ex) {
                Log.e(TAG, "[localFile | getResult] ERR : " + ex.getMessage(), ex);
                throw new RuntimeException("getResult ERR : " + ex.getMessage(), ex);
            }
            if (tmp != null) {
                Log.v(TAG, "[localFile]" + localFile);
                return tmp;
            }
        }

        //online
        if (isOnlineImageFromURL(altData)) {
            tmp = getPicFromURL(altData);
            if (tmp != null) {
                Log.v(TAG, "[UrlFile]" + altData);
                return tmp;
            }
        }

        //online
//        if (StringUtils.isNotBlank(dto.getCurrentHtmlUrl())) {
//            String prefixUrl = dto.getCurrentHtmlUrl();
//            if (!prefixUrl.endsWith("/")) {
//                prefixUrl = prefixUrl + "/";
//            }
//            tmp = getPicFromURL(prefixUrl + altData);
//            if (tmp != null) {
//                Log.v(TAG, "[UrlFile]" + prefixUrl + altData);
//                return tmp;
//            }
//        }
        return onlinePicLoader.getNotfound404();
    }

    @Override
    public File getLocalFile() {
        return localFile;
    }

    private boolean isOnlineImageFromURL(String url) {
        if (url.matches("https?\\:.*") || //
                url.matches("www\\..*") || //
                url.matches("\\w+\\.\\w+.*") //
                ) {
            return true;
        }
        return false;
    }

    private Bitmap getPicFromURL(String url) {
        return onlinePicLoader.getBitmapFromURL_waiting(url, 10 * 1000, IMAGE_SIMPLE_SCALE);
    }
}