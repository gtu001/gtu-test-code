package com.example.englishtester;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;

import com.example.englishtester.common.EnglishSearchRegexConf;
import com.example.englishtester.common.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ant.liao.GifView.GifImageType;
import com.example.englishtester.common.OOMHandler;
import com.google.common.base.Function;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.squareup.picasso.Picasso;

public class SwitchPictureService {

    private static final String TAG = SwitchPictureService.class.getSimpleName();

    Activity activity;

    Button previousPicBtn;
    Button nextPicBtn;
    ImageButton deletePicBtn;

    com.ant.liao.GifView imageView_gif;

    TextView hasPicLabel;
    TextView picFileLabel;

    String englishLabelText;

    File picDir = new File(Constant.MainActivityDTO_picDir_PATH);// 圖檔目錄
    List<File> picList;// 目前題目所搜尋到的圖片
    int picList_index = 0;// 目前瀏覽圖片index

    static Set<FileZ> allPicFileList;
    static Multimap<String, FileZ> allPicMap;

    public SwitchPictureService(Activity activity) {
        this.activity = activity;
    }

    public void init(Button previousPicBtn, Button nextPicBtn, ImageButton deletePicBtn, com.ant.liao.GifView imageView_gif, TextView hasPicLabel, TextView picFileLabel) {
        this.previousPicBtn = previousPicBtn;
        this.nextPicBtn = nextPicBtn;
        this.deletePicBtn = deletePicBtn;
        this.imageView_gif = imageView_gif;
        this.hasPicLabel = hasPicLabel;
        this.picFileLabel = picFileLabel;

        // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 上一張圖
        previousPicBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                showIndexPicture(-1);
            }
        });

        // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 下一張圖
        nextPicBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                showIndexPicture(1);
            }
        });

        // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 刪除圖片按鈕
        deletePicBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                if (picList == null || picList.isEmpty()) {
                    Toast.makeText(activity, "無任何圖片可刪除", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (picList_index >= picList.size()) {
                    Toast.makeText(activity, "發現異常!,中止刪除圖片", Toast.LENGTH_SHORT).show();
                    return;
                }
                final File file = picList.get(picList_index);
                new AlertDialog.Builder(activity)//
                        .setTitle("是否確定刪除圖片")//
                        .setMessage(file.getAbsolutePath() + "(" + (file.length() / 1024) + "k)")//
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                // file.delete();

                                // ↓↓↓↓↓↓無法刪除,改為註記要刪除圖片
                                File delFile = Constant.DELETE_PIC_MARKS_FILE;
                                Properties prop = new Properties();
                                try {
                                    prop.load(new FileInputStream(delFile));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (prop.containsKey(file.getAbsolutePath())) {
                                    Toast.makeText(activity, "圖片已取消註記刪除!", Toast.LENGTH_SHORT).show();
                                    prop.remove(file.getAbsolutePath());
                                } else {
                                    Toast.makeText(activity, "圖片已註記刪除!", Toast.LENGTH_SHORT).show();
                                    prop.setProperty(file.getAbsolutePath(), "");
                                }
                                try {
                                    prop.store(new FileOutputStream(delFile), "del pics");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                // ↑↑↑↑↑↑無法刪除,改為註記要刪除圖片

                                //showPicture(englishLabelText);
                            }
                        })//
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            }
                        })//
                        .show();
            }
        });
    }

    public void showPicture(String englishLabelText) {
        this.showPictureInner(englishLabelText);

        // 初始化圖片index
        picList_index = 0;

        // 將這個單字的圖片全部載入快取 ↓↓↓↓↓↓
        for (File file : picList) {
            scaleImg2(OOMHandler.new_decode(file, null), file);
        }
        Log.v(TAG, "快取載入完畢!!" + picList.size());
        // 將這個單字的圖片全部載入快取 ↑↑↑↑↑↑

        // 秀第一章圖
        showIndexPicture(0);

        // 設定找到圖的數量
        hasPicLabel.setText("圖" + picList.size() + "張");
    }

    public void showPictureInner(String englishLabelText) {
        if (picDir == null) {
            return;
        }
        
        // **載入系統所有圖片** TODO
        if (allPicFileList == null || allPicMap == null) {
            allPicFileList = new HashSet<FileZ>();
            loadAllPic(picDir, allPicFileList);
            allPicMap = createCacheMap(allPicFileList);
        }

        // 目前要搜尋的單字
        this.englishLabelText = StringUtils.defaultString(englishLabelText).toLowerCase();

        // **找此單字的圖片** TODO
        List<File> fileList = new ArrayList<File>();
        if(StringUtils.isNotBlank(englishLabelText)){
            if (allPicMap.containsKey(this.englishLabelText)) {
                fileList.addAll(Lists.transform(Lists.newArrayList(allPicMap.get(this.englishLabelText)), new Function<FileZ, File>() {
                    @Override
                    public File apply(FileZ paramF) {
                        return paramF.file;
                    }
                }));
            } else {
                for (FileZ fz : allPicFileList) {
                    String fileName = fz.name;
                    if (this.isMatch(fileName, this.englishLabelText)) {
                        fileList.add(fz.file);
                    }
                }
            }
        }
        picList = fileList;
    }

    public void loadAllPictureIfNeed() {
        if (allPicFileList == null) {
            allPicFileList = new HashSet<FileZ>();
            loadAllPic(picDir, allPicFileList);
            allPicMap = createCacheMap(allPicFileList);
        }
    }

    private void loadAllPic(File file, Set<FileZ> findFile) {
        if (file.isDirectory()) {
            File[] list = null;
            if ((list = file.listFiles()) != null) {
                for (File f : list) {
                    loadAllPic(f, findFile);
                }
            }
        } else {
            String name = file.getName().toLowerCase();
            if(name.indexOf(".")!=-1){
                name = name.substring(0, name.lastIndexOf("."));
                FileZ fz = new FileZ();
                fz.file = file;
                fz.name = name;
                findFile.add(fz);
            }
        }
    }

    private final static Pattern wordPattern = Pattern.compile(EnglishSearchRegexConf.getSearchRegex(false, false));

    private Multimap<String, FileZ> createCacheMap(Set<FileZ> allPicFileList) {
        Multimap<String, FileZ> cacheMap = HashMultimap.create();
        Matcher matcher = null;
        for (FileZ fz : allPicFileList) {
            matcher = wordPattern.matcher(fz.name);
            while (matcher.find()) {
                if (StringUtils.equals(matcher.group(), fz.name)) {
                    cacheMap.put(matcher.group(), fz);
                } else {
                    Log.v(TAG, String.format("%s -> %d , %d, [%s]", matcher.group(), matcher.start(), matcher.end(), fz.name));
                    String chkStart = StringUtils.defaultString(substring(fz.name, matcher.start() - 1, matcher.start()));
                    String chkEnd = StringUtils.defaultString(substring(fz.name, matcher.end(), matcher.end() + 1));
                    if (chkStart.matches("^[^a-zA-Z]?$") && chkEnd.matches("^[^a-zA-Z]?$")) {
                        cacheMap.put(matcher.group(), fz);
                    }
                }
            }
        }
        return cacheMap;
    }

    static String substring(String value, int start, int end) {
        if (value == null) {
            return "";
        }
        if (start < 0) {
            start = 0;
        }
        if (end > value.length()) {
            end = value.length();
        }
        String rtnVal = null;
        try {
            rtnVal = value.substring(start, end);
            ;
        } catch (Exception ex) {
            throw new RuntimeException(String.format("ERROR : start:%d,end:%d,str:%s", start, end, value), ex);
        }
        return rtnVal;
    }

    public void resetPicture() {
        hasPicLabel.setText("");
        picFileLabel.setText("");
    }

    // xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

    boolean isMatch(String fileName, String english) {
        Pattern ptn = Pattern.compile("(.?)" + english + "(.?)");
        Matcher matcher = ptn.matcher(fileName);
        while (matcher.find()) {
            String a1 = matcher.group(1);
            String a2 = matcher.group(2);
            String format = "[^a-zA-Z]";
            boolean a1Match = (StringUtils.isEmpty(a1) || a1.matches(format));
            boolean a2Match = (StringUtils.isEmpty(a2) || a2.matches(format));
            if (a1Match && a2Match) {
                return true;
            }
        }
        return false;
    }

    void showIndexPicture(int margin) {
        Log.v(TAG, "showIndexPicture -> " + margin);
        int tempIndex = picList_index;
        if (picList == null) {
            Log.v(TAG, "picList == null");
            resetPicture();
            return;
        }
        if (picList.isEmpty()) {
            Log.v(TAG, "picList == empty");
            resetPicture();
            return;
        }
        if (margin > 0 && (picList_index + 1 <= picList.size() - 1)) {
            Log.v(TAG, " -> add, plus");
            picList_index++;
        }
        if (margin < 0 && (picList_index - 1 >= 0)) {
            Log.v(TAG, " -> subtract, minus, deduct");
            picList_index--;
        }

        try {
            Log.v(TAG, "index -> " + picList.size() + "..." + picList_index);
            File file = picList.get(picList_index);
            String fileName = file.getAbsolutePath();
            // 最早的寫法
            // Bitmap bm = BitmapFactory.decodeFile(fileName);
            Bitmap bm = OOMHandler.new_decode(file, null);
            // imageView.setImageBitmap(scaleImg(bm));//原載圖邏輯

            scaleImg2(bm, file);
            picFileLabel.setText(file.getName() + "(" + file.length() / 1024 + "k)");
        } catch (Exception ex) {
            picList_index = tempIndex;
            resetPicture();
            ex.printStackTrace();
            Toast.makeText(activity, "錯誤: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // 使用新元件實踐秀圖
    protected void scaleImg2(Bitmap bm, File file) {
        if(bm == null){
            return;
        }

        int width = bm.getWidth();
        int height = bm.getHeight();
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int newWidth1 = dm.widthPixels;
        float scaleWidth = ((float) newWidth1) / width;
        int newHeight = (int) (scaleWidth * height);

//        Picasso.with(activity).load(file).resize(newWidth1, newHeight).into(imageView_gif);
        imageView_gif.setImageBitmap(bm);

        // 设置显示的大小，拉伸或者压缩
        imageView_gif.setShowDimension(newWidth1, newHeight);

        // 設定內部圖片長寬
        // imageView_gif.setLeft(imageView.getLeft());
        // imageView_gif.setTop(imageView.getTop());

        // 設定layout長寬
        imageView_gif.getLayoutParams().width = newWidth1;
        imageView_gif.getLayoutParams().height = newHeight;

        // 设置加载方式：先加载后显示、边加载边显示、只显示第一帧再显示
        imageView_gif.setGifImageType(GifImageType.COVER);

        // 设置Gif图片源
        try {
            imageView_gif.setGifImage(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            Log.v(TAG, e.getMessage());
            e.printStackTrace();
        }
        // 繼續
        imageView_gif.showAnimation();

        if (!file.getName().toLowerCase().endsWith(".gif")) {
            // 只秀第一格畫面
            imageView_gif.showCover();
        }
    }

    // 圖片以畫面寬度縮放比例
    protected Bitmap scaleImg(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int newWidth1 = dm.widthPixels;
        float scaleWidth = ((float) newWidth1) / width;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleWidth);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    /**
     * 當前單字是否有圖片
     */
    public boolean isHasCurrentPic(){
        boolean isHasPic = picList != null && !picList.isEmpty();
        return isHasPic;
    }

    static class FileZ {
        File file;
        String name;
    }
}
