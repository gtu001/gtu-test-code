package com.example.englishtester.common.html.image;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.englishtester.TxtReaderActivity;
import com.example.englishtester.common.OnlinePicLoader;
import com.example.englishtester.common.html.interf.ITxtReaderActivityDTO;
import com.example.epub.com.example.epub.base.EpubViewerMainHandler;

import java.io.File;

/**
 * Created by gtu001 on 2018/8/8.
 */

public class ImageLoaderCandidate4EpubHtml extends ImageLoaderCandidateAbstract implements IImageLoaderCandidate {

    private static final String TAG = ImageLoaderCandidate4WordHtml.class.getSimpleName();

    String srcData;
    String altData;
    boolean isGifFile;
    boolean isNeedLoadImage;
    File localFile;
    OnlinePicLoader onlinePicLoader;
    EpubViewerMainHandler.EpubDTO dto;

    public ImageLoaderCandidate4EpubHtml(String srcData, String altData, boolean isNeedLoadImage, OnlinePicLoader onlinePicLoader, ITxtReaderActivityDTO dto) {
        this.srcData = srcData;
        this.altData = altData;
        this.isGifFile = isGif(srcData);
        this.isNeedLoadImage = isNeedLoadImage;
        this.dto = (EpubViewerMainHandler.EpubDTO) dto;
        this.onlinePicLoader = onlinePicLoader;
    }

    @Override
    public boolean isGifFile() {
        return this.isGifFile;
    }

    @Override
    public Bitmap getResult() {
        return dto.getBitmapForEpub(this);
    }

    @Override
    public File getLocalFile() {
        return localFile;//unsupported
    }

    public String getSrcData() {
        return srcData;
    }

    public String getAltData() {
        return altData;
    }

    public boolean isNeedLoadImage() {
        return isNeedLoadImage;
    }

    public OnlinePicLoader getOnlinePicLoader() {
        return onlinePicLoader;
    }
}
