package com.example.englishtester.common.html.image;

import android.graphics.Bitmap;
import com.example.englishtester.common.Log;

import java.io.File;

public interface IImageLoaderCandidate {

    public boolean isGifFile();

    public Bitmap getResult();

    public File getLocalFile();
}