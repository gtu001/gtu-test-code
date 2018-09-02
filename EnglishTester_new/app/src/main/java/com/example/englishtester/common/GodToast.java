package com.example.englishtester.common;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import com.example.englishtester.common.Log;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ant.liao.GifView;
import com.example.englishtester.R;

/**
 * Created by gtu001 on 2018/1/11.
 */

public class GodToast {

    private static final GodToast _INST = new GodToast();

    public static GodToast getInstance() {
        return _INST;
    }

    private Scale scale;

    private static final String TAG = GodToast.class.getSimpleName();

    public void show(Context context) {
        FrameLayout layout = new FrameLayout(context);

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setView(layout);

        GifView imageView_gif = new GifView(context);

        layout.addView(imageView_gif,//
                new FrameLayout.LayoutParams(//
                        FrameLayout.LayoutParams.WRAP_CONTENT, //
                        FrameLayout.LayoutParams.WRAP_CONTENT));

        //取得圖片長寬
        fetchImageSize(context);

        // 設定內部圖片長寬
        imageView_gif.setLeft(0);
        imageView_gif.setTop(0);

        // 设置显示的大小，拉伸或者压缩
        if (scale != null) {
            imageView_gif.setShowDimension(scale.width, scale.height);

            // 設定layout長寬
            imageView_gif.getLayoutParams().width = scale.width;
            imageView_gif.getLayoutParams().height = scale.height;
        }

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

        // 繼續
        imageView_gif.showAnimation();
        toast.show();
    }

    private void fetchImageSize(Context context) {
        if (scale != null) {
            return;
        }
        Bitmap bm1 = OOMHandler.new_decode(context, R.drawable.god1);
        Scale scales = scaleImg2(bm1);
        if (scales != null) {
            scale = scales;
        }
    }

    // 使用新元件實踐秀圖
    protected Scale scaleImg2(Bitmap bm) {
        if (bm == null) {
            return null;
        }

        int sceenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int sceenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        int width = bm.getWidth();
        int height = bm.getHeight();

        int newWidth = sceenWidth / 2;

        double scale = (double) newWidth / (double) width;
        int newHeight = (int) (height * scale);

        Scale s = new Scale();
        s.width = newWidth;
        s.height = newHeight;
        return s;
    }

    private class Scale {
        int width;
        int height;
    }
}
