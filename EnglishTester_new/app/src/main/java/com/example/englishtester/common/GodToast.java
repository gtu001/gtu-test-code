package com.example.englishtester.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.ant.liao.GifView;
import com.example.englishtester.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by gtu001 on 2018/1/11.
 */

public class GodToast {

    private static final GodToast _INST = new GodToast();

    public static GodToast getInstance(){
        return _INST;
    }

    private static final String TAG = GodToast.class.getSimpleName();

    public void show(Context context) {
        GifView imageView_gif = new GifView(context);

        // 设置显示的大小，拉伸或者压缩
//        imageView_gif.setShowDimension(newWidth1, newHeight);

        // 設定內部圖片長寬
        // imageView_gif.setLeft(imageView.getLeft());
        // imageView_gif.setTop(imageView.getTop());

        // 設定layout長寬
//        imageView_gif.getLayoutParams().width = newWidth1;
//        imageView_gif.getLayoutParams().height = newHeight;

        // 设置加载方式：先加载后显示、边加载边显示、只显示第一帧再显示
        imageView_gif.setGifImageType(GifView.GifImageType.COVER);

        // 设置Gif图片源
        try {
//            imageView_gif.setGifImage(new FileInputStream(file));
            imageView_gif.setGifImage(R.drawable.god1);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

//        if (!file.getName().toLowerCase().endsWith(".gif")) {
//            // 只秀第一格畫面
//            imageView_gif.showCover();
//        }

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setView(imageView_gif);
        toast.show();

        // 繼續
        imageView_gif.showAnimation();
    }
}
