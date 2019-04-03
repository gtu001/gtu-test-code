package com.example.englishtester.common.html.image;

import android.graphics.Bitmap;

import com.example.englishtester.common.OOMHandler;
import com.example.englishtester.common.OnlinePicLoader;
import com.example.englishtester.common.epub.base.EpubViewerMainHandler;
import com.example.englishtester.common.interf.ITxtReaderActivityDTO;
import com.example.englishtester.common.mobi.base.MobiViewerMainHandler;

import java.io.File;

/**
 * Created by gtu001 on 2018/8/8.
 */

public class ImageLoaderCandidate4MobiHtml extends ImageLoaderCandidateAbstract implements IImageLoaderCandidate {

    private static final String TAG = ImageLoaderCandidate4WordHtml.class.getSimpleName();

    String srcData;
    String altData;
    boolean isGifFile;
    boolean isNeedLoadImage;
    File localFile;
    OnlinePicLoader onlinePicLoader;
    MobiViewerMainHandler.MobiDTO dto;

    public ImageLoaderCandidate4MobiHtml(String srcData, String altData, boolean isNeedLoadImage, OnlinePicLoader onlinePicLoader, ITxtReaderActivityDTO dto) {
        this.srcData = srcData;
        this.altData = altData;
        this.isGifFile = isGif(srcData);
        this.isNeedLoadImage = isNeedLoadImage;
        this.dto = (MobiViewerMainHandler.MobiDTO) dto;
        this.onlinePicLoader = onlinePicLoader;
    }

    @Override
    public boolean isGifFile() {
        return this.isGifFile;
    }

    @Override
    public Bitmap getResult(int fixWidth) {
        return OOMHandler.fixPicScaleFixScreenWidth_new(dto.getBitmapForMobi(this), fixWidth);
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
