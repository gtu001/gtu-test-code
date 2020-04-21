package com.example.englishtester.common.html.image;

import android.graphics.Bitmap;
import com.example.englishtester.common.Log;

import java.io.File;

public interface IImageLoaderCandidate {

    boolean isGifFile();

    Bitmap getResult(int fixWidth);

    File getLocalFile();
}