package com.example.englishtester.common;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ant.liao.GifView;
import com.example.englishtester.R;

import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gtu001 on 2018/1/11.
 */

public class GodToast {

    private GodImageHandler godImageHandler;

    private static GodToast _INST;
    private Context context;

    public static GodToast getInstance(Context context) {
        if (_INST == null) {
            _INST = new GodToast(context);
        }
        return _INST;
    }

    private GodToast(Context context) {
        this.context = context;
        this.godImageHandler = new GodImageHandler(context);
    }

    private Scale scale;

    private static final String TAG = GodToast.class.getSimpleName();

    public void show() {
        File gifFile = this.godImageHandler.getGifImage();

        if (gifFile == null) {
            Log.v(TAG, "圖片資源尚未完成!!", 10);
            return;
        }

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
        fetchImageSize(gifFile);

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
            imageView_gif.setGifImage(new FileInputStream(gifFile));
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

    private void fetchImageSize(File file) {
        try {
            Pair<Integer, Integer> mScale = OOMHandler.getImageScale(new FileInputStream(file));
            Scale scales = scaleImg2(mScale);
            if (scales != null) {
                scale = scales;
            }
        } catch (Exception e) {
            throw new RuntimeException("fetchImageSize ERR : " + e.getMessage(), e);
        }
    }

    // 使用新元件實踐秀圖
    protected Scale scaleImg2(Pair<Integer, Integer> bm) {
        int sceenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int sceenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        int width = bm.getLeft();
        int height = bm.getRight();

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

    private static class GodImageHandler {
        private static Map<String, File> imageMap = new HashMap<String, File>();

        static {
            imageMap.put("https://i.imgur.com/huJsCtL.gif", null);
            imageMap.put("https://i.imgur.com/4J4kQG5.gif", null);
        }

        File tempDir;
        Context context;
        Thread downloadThread;

        private String getFileName(String url) {
            Pattern ptn = Pattern.compile("\\w+\\.gif");
            Matcher mth = ptn.matcher(url);
            if (mth.find()) {
                return mth.group();
            }
            return UUID.randomUUID().toString() + ".gif";
        }

        GodImageHandler(Context context) {
            this.context = context;
            this.tempDir = this.context.getCacheDir();
            startDownload();
        }

        public void startDownload() {
            if (downloadThread == null || downloadThread.getState() == Thread.State.TERMINATED) {
                downloadThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (String url : imageMap.keySet()) {
                            File gifFile = new File(tempDir, getFileName(url));
                            if (gifFile.exists() && gifFile.canRead()) {
                                continue;
                            }
                            if (imageMap.get(url) == null) {
                                SimpleDownloadUtil.downloadFile(url, gifFile);
                                imageMap.put(url, gifFile);
                                Log.v(TAG, "## download success : " + url);
                            }
                        }
                    }
                });
                downloadThread.start();
            }
        }

        public File getGifImage() {
            List<File> files = new ArrayList<File>();
            for (String url : imageMap.keySet()) {
                if (imageMap.get(url) != null && //
                        imageMap.get(url).exists() && //
                        imageMap.get(url).canRead() //
                        ) {
                    files.add(imageMap.get(url));
                }
            }
            if (files.isEmpty()) {
                return null;
            }
            return files.get(new Random().nextInt(files.size()));
        }
    }
}
