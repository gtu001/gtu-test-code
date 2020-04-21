package com.example.englishtester.common.html.image;

import com.example.englishtester.TxtReaderActivity;
import com.example.englishtester.common.Log;
import com.example.englishtester.common.OnlinePicLoader;
import com.example.englishtester.common.epub.base.EpubViewerMainHandler;
import com.example.englishtester.common.interf.ITxtReaderActivityDTO;
import com.example.englishtester.common.mobi.base.MobiViewerMainHandler;
import com.example.englishtester.common.txtbuffer.base.TxtBufferViewerMainHandler;

import java.net.URLDecoder;

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

    private String decodeUrl(String srcData) {
        try {
            srcData = URLDecoder.decode(srcData, "UTF8");
        } catch (Exception e) {
            Log.e(TAG, "ERR : pic_url decode = " + e.getMessage(), e);
        }
        return srcData;
    }

    public IImageLoaderCandidate getLoader(String srcData, String altData) {
        Log.v(TAG, "#-- getLoader --start");

        srcData = decodeUrl(srcData);
        altData = decodeUrl(altData);

        Log.v(TAG, "srcData - " + srcData);
        Log.v(TAG, "altData - " + altData);
        if (this.dto instanceof TxtReaderActivity.TxtReaderActivityDTO) {
            return new ImageLoaderCandidate4WordHtml(srcData, altData, isNeedLoadImage, onlinePicLoader, dto);
        } else if (this.dto instanceof TxtBufferViewerMainHandler.TxtBufferDTO) {
            return new ImageLoaderCandidate4WordHtml_BufferVer(srcData, altData, isNeedLoadImage, onlinePicLoader, dto);
        } else if (this.dto instanceof EpubViewerMainHandler.EpubDTO) {
            return new ImageLoaderCandidate4EpubHtml(srcData, altData, isNeedLoadImage, onlinePicLoader, dto);
        } else if (this.dto instanceof MobiViewerMainHandler.MobiDTO) {
            return new ImageLoaderCandidate4MobiHtml(srcData, altData, isNeedLoadImage, onlinePicLoader, dto);
        }
        throw new RuntimeException("無法判斷的 ImageLoader : " + dto);
    }
}
