package com.example.englishtester.common.html.image;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;

public interface IImageLoaderCandidate {

    public boolean isGifFile();

    public Bitmap getResult();

    public File getLocalFile();
}