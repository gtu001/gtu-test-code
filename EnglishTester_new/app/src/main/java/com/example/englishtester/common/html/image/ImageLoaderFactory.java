package com.example.englishtester.common.html.image;

import android.util.Log;

import com.example.englishtester.TxtReaderActivity;
import com.example.englishtester.common.OnlinePicLoader;
import com.example.englishtester.common.html.interf.ITxtReaderActivityDTO;
import com.example.epub.com.example.epub.base.EpubViewerMainHandler;

/**
 * Created by gtu001 on 2018/8/8.
 */

public class ImageLoaderFactory {

    private static final String TAG = ImageLoaderFactory.class.getSimpleName();

    boolean isNeedLoadImage;
    OnlinePicLoader onlinePicLoader;
    ITxtReaderActivityDTO dto;

    private ImageLoaderFactory(boolean isNeedLoadImage, OnlinePicLoader onlinePicLoader, ITxtReaderActivityDTO dto) {
        this.isNeedLoadImage = isNeedLoadImage;
        this.onlinePicLoader = onlinePicLoader;
        this.dto = dto;
    }

    public static ImageLoaderFactory newInstance(boolean isNeedLoadImage, OnlinePicLoader onlinePicLoader, ITxtReaderActivityDTO dto) {
        return new ImageLoaderFactory(isNeedLoadImage, onlinePicLoader, dto);
    }

    public IImageLoaderCandidate getLoader(String srcData, String altData) {
        Log.v(TAG, "#-- getLoader --start");
        Log.v(TAG, "srcData - " + srcData);
        Log.v(TAG, "altData - " + altData);
        if (this.dto instanceof TxtReaderActivity.TxtReaderActivityDTO) {
            return new ImageLoaderCandidate4WordHtml(srcData, altData, isNeedLoadImage, onlinePicLoader, dto);
        } else if (this.dto instanceof EpubViewerMainHandler.EpubDTO) {
            return new ImageLoaderCandidate4EpubHtml(srcData, altData, isNeedLoadImage, onlinePicLoader, dto);
        }
        throw new RuntimeException("無法判斷的 ImageLoader : " + dto);
    }
}
