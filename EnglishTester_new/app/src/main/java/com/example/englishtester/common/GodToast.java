package com.example.englishtester.common;

import android.content.Context;
import android.content.res.Resources;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.ant.liao.GifView;
import com.example.englishtester.BuildConfig;
import com.example.englishtester.DropboxApplicationActivity;
import com.example.englishtester.DropboxFileLoadService;
import com.example.englishtester.R;
import com.example.englishtester.common.interf.IDropboxFileLoadService;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gtu.number.RandomUtil;

import static com.example.englishtester.R.drawable.close_icon;

/**
 * Created by gtu001 on 2018/1/11.
 */

public class GodToast {

    private GodImageHandler godImageHandler;

    private static GodToast _INST;
    private Context context;
    private IDropboxFileLoadService dropboxFileLoadService;
    private Handler handler = new Handler();

    private GifView imageView_gif;//

    private Toast toast;
    private FrameLayout layout;
    private File godImageConfig;

    public static GodToast getInstance(Context context) {
        if (_INST == null) {
            _INST = new GodToast(context);
        }
        return _INST;
    }

    private GodToast(Context context) {
        this.context = context;

        dropboxFileLoadService = DropboxFileLoadService.newInstance(context, DropboxApplicationActivity.getDropboxAccessToken(context));
        godImageConfig = dropboxFileLoadService.downloadGodImageFile();

        this.layout = new FrameLayout(context);

        this.godImageHandler = new GodImageHandler(context, godImageConfig);

        this.imageView_gif = new GifView(context);

        this.toast = new Toast(context);
        this.toast.setDuration(Toast.LENGTH_SHORT);//LENGTH_LONG
        this.toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        this.toast.setView(layout);

        layout.addView(imageView_gif,//
                new FrameLayout.LayoutParams(//
                        FrameLayout.LayoutParams.WRAP_CONTENT, //
                        FrameLayout.LayoutParams.WRAP_CONTENT));

        // 設定內部圖片長寬
        imageView_gif.setLeft(0);
        imageView_gif.setTop(0);
    }

    private Scale scale;

    private static final String TAG = GodToast.class.getSimpleName();

    public void show() {
        File gifFile = godImageHandler.getGifImage();

        if (gifFile == null) {
            Log.v(TAG, "圖片資源尚未完成!!", 10);
            return;
        }

        //取得圖片長寬
        fetchImageSize(gifFile);

        // 设置显示的大小，拉伸或者压缩
        if (scale != null) {
            imageView_gif.setShowDimension(scale.width, scale.height);

            // 設定layout長寬
            imageView_gif.getLayoutParams().width = scale.width;
            imageView_gif.getLayoutParams().height = scale.height;

            layout.invalidate();
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

        //很危險--↓↓↓↓↓↓
        new CountDownTimer(9000, 1000) {
            public void onTick(long millisUntilFinished) {
                toast.show();
            }

            public void onFinish() {
                toast.show();
            }
        }.start();

        //很危險--↑↑↑↑↑↑
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

        float fixRatio = ((float) width / (float) height) / 4;
        if (fixRatio > 0.35) {
            fixRatio = 0.35f;
        } else if (fixRatio < 0) {
            fixRatio = 0f;
        }

        int newWidth = (int) (sceenWidth * (0.5 + fixRatio));

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
        File tempDir;
        Context context;
        Thread downloadThread;
        List<String> godImageLst;
        File godImageConfigFile;
        private Map<String, File> imageMap;

        private List<String> getGodImageList(File godImageConfig) {
            try {
                Pattern ptn = Pattern.compile("\\<img\\ssrc\\=\"(.*?)\"\\s*\\/\\>");
                List<String> lst = FileUtils.readLines(godImageConfig, "UTF8");
                List<String> lst2 = new ArrayList<String>();
                for (int ii = 0; ii < lst.size(); ii++) {
                    Matcher mth = ptn.matcher(lst.get(ii));
                    if (mth.find()) {
                        String tmp = mth.group(1);
                        if (!lst2.contains(tmp) && tmp.endsWith(".gif")) {
                            lst2.add(tmp);
                        }
                    }
                }
                return lst2;
            } catch (IOException e) {
                Log.e(TAG, "getGodImageList ERR : " + e.getMessage(), e);
                return Collections.emptyList();
            }
        }

        private String getFileName(String url) {
            Pattern ptn = Pattern.compile("\\w+\\.gif");
            Matcher mth = ptn.matcher(url);
            if (mth.find()) {
                return mth.group();
            }
            return UUID.randomUUID().toString() + ".gif";
        }

        GodImageHandler(Context context, File godImageConfigFile) {
            this.context = context;
            this.tempDir = this.context.getCacheDir();
            this.godImageConfigFile = godImageConfigFile;
            startDownload();
        }

        public void startDownload() {
            if (godImageLst == null || godImageLst.isEmpty()) {
                godImageLst = getGodImageList(this.godImageConfigFile);
            }

            if (imageMap == null || imageMap.isEmpty()) {
                imageMap = new HashMap<String, File>();
                for (String url : godImageLst) {
                    imageMap.put(url, null);
                }
            }

            if (downloadThread == null || downloadThread.getState() == Thread.State.TERMINATED) {
                Log.v(TAG, "do startDownload !!!");
                downloadThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<String> urlList = new ArrayList<String>(imageMap.keySet());
                        urlList = RandomUtil.randomList(urlList);
                        for (String url : urlList) {
                            File gifFile = new File(tempDir, getFileName(url));
                            if (gifFile.exists() && gifFile.canRead()) {
                                imageMap.put(url, gifFile);
                                continue;
                            }
                            if (imageMap.get(url) == null) {
                                try {
                                    SimpleDownloadUtil.downloadFile(url, gifFile);
                                } catch (Exception ex) {
                                    Log.e(TAG, "startDownload ERR : " + url + " -> " + ex.getMessage(), ex);
                                }
                                imageMap.put(url, gifFile);
                                Log.v(TAG, "## download success : " + url);
                            }
                        }
                    }
                });
                downloadThread.start();
            }
        }

        private boolean isAllDone() {
            if (BuildConfig.DEBUG) {
                Log.v(TAG, "=====================================");
                for (String url : imageMap.keySet()) {
                    File f = imageMap.get(url);
                    boolean ok = f != null && f.exists() && f.canRead();
                    Log.v(TAG, "isAllDone -> " + url + " : " + (ok ? "SUCCESS" : "FAILED"));
                }
                Log.v(TAG, "thread - " + (downloadThread != null ? downloadThread.getState() : "Null"));
                Log.v(TAG, "=====================================");
            }
            for (String url : imageMap.keySet()) {
                if (imageMap.get(url) == null) {
                    return false;
                }
                File f = imageMap.get(url);
                if (!f.exists() || !f.canRead() || f.length() == 0) {
                    return false;
                }
            }
            return true;
        }

        public File getGifImage() {
            if (!isAllDone()) {
                startDownload();
            }
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
