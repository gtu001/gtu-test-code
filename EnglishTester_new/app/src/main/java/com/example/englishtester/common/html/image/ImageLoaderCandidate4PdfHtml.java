package com.example.englishtester.common.html.image;

import android.graphics.Bitmap;

import com.example.englishtester.TxtReaderActivity;
import com.example.englishtester.common.Log;
import com.example.englishtester.common.OOMHandler;
import com.example.englishtester.common.OnlinePicLoader;
import com.example.englishtester.common.html.parser.HtmlWordParser;
import com.example.englishtester.common.interf.ITxtReaderActivityDTO;
import com.example.englishtester.common.mobi.base.MobiViewerMainHandler;
import com.example.englishtester.common.pdf.base.PdfViewerMainHandler;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.URLDecoder;

public class ImageLoaderCandidate4PdfHtml extends ImageLoaderCandidateAbstract implements IImageLoaderCandidate {

    private static final String TAG = ImageLoaderCandidate4WordHtml.class.getSimpleName();

    String srcData;
    String altData;
    boolean isGifFile;
    boolean isNeedLoadImage;
    File localFile;
    OnlinePicLoader onlinePicLoader;
    PdfViewerMainHandler.PdfDTO dto;

    public ImageLoaderCandidate4PdfHtml(String srcData, String altData, boolean isNeedLoadImage, OnlinePicLoader onlinePicLoader, ITxtReaderActivityDTO dto) {
        this.srcData = srcData;
        this.altData = altData;
        this.isGifFile = isGif(srcData);
        this.isNeedLoadImage = isNeedLoadImage;
        this.dto = (PdfViewerMainHandler.PdfDTO) dto;
        this.onlinePicLoader = onlinePicLoader;
    }

    @Override
    public boolean isGifFile() {
        return this.isGifFile;
    }

    @Override
    public Bitmap getResult(int fixWidth) {
        return OOMHandler.fixPicScaleFixScreenWidth_new(dto.getBitmapForPdf(this), fixWidth);
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