package com.example.englishtester.common.html.image;

import android.graphics.Bitmap;

import com.example.englishtester.common.Log;

import com.example.englishtester.TxtReaderActivity;
import com.example.englishtester.common.OOMHandler;
import com.example.englishtester.common.OnlinePicLoader;
import com.example.englishtester.common.interf.ITxtReaderActivityDTO;
import com.example.englishtester.common.html.parser.HtmlWordParser;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.URLDecoder;

public class ImageLoaderCandidate4WordHtml extends ImageLoaderCandidateAbstract implements IImageLoaderCandidate {

    private static final String TAG = ImageLoaderCandidate4WordHtml.class.getSimpleName();

    String srcData;
    String altData;
    boolean isGifFile;
    File localFile;
    boolean isNeedLoadImage;
    OnlinePicLoader onlinePicLoader;
    TxtReaderActivity.TxtReaderActivityDTO dto;

    public ImageLoaderCandidate4WordHtml(String srcData, String altData, boolean isNeedLoadImage, OnlinePicLoader onlinePicLoader, ITxtReaderActivityDTO dto) {
        this.srcData = srcData;
        this.altData = altData;
        this.isGifFile = isGif(srcData);
        this.isNeedLoadImage = isNeedLoadImage;
        this.dto = (TxtReaderActivity.TxtReaderActivityDTO) dto;
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
                return localPicFile;
            }

            if (dto.getDropboxPicDir() == null && realName.contains("/")) {
                String middleDir = realName.substring(0, realName.lastIndexOf("/"));
                dto.setDropboxPicDir(new File(dto.getCacheDir(), middleDir));
            }

            String imageFileName = realName;
            if (realName.contains("/")) {
                imageFileName = realName.substring(realName.lastIndexOf("/"));
            }
            File dropboxPic = new File(dto.getDropboxPicDir(), imageFileName);
            if (dropboxPic.exists() && dropboxPic.canRead()) {
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

    public Bitmap getResult() {
        if (!isNeedLoadImage) {
            return onlinePicLoader.getNotfound404();
        }

        Bitmap tmp = null;
        if (localFile != null && localFile.isFile()) {
            tmp = OOMHandler.new_decode(localFile);
            if (tmp != null) {
                return tmp;
            }
        }

        if (isOnlineImageFromURL(altData)) {
            tmp = getPicFromURL(altData);
            if (tmp != null) {
                return tmp;
            }
        }

        if (StringUtils.isNotBlank(dto.getCurrentHtmlUrl())) {
            String prefixUrl = dto.getCurrentHtmlUrl();
            if (!prefixUrl.endsWith("/")) {
                prefixUrl = prefixUrl + "/";
            }
            tmp = getPicFromURL(prefixUrl + altData);
            if (tmp != null) {
                return tmp;
            }
        }
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
        return onlinePicLoader.getBitmapFromURL_waiting(url, 10 * 1000);
    }
}