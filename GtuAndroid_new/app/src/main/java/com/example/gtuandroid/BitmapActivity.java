package com.example.gtuandroid;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BitmapActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap);
        back();

        final TextView textView1 = (TextView) findViewById(R.id.textView1);
        final Button changeBtn = (Button) findViewById(R.id.button1);
        final ImageView imageView1 = (ImageView) findViewById(R.id.imageView1);

        File root = new File("/storage/sdcard0");
        final List<File> fileList = new ArrayList<File>();
        scanImageFile(root, fileList);

        textView1.setText("圖片檔總數  : " + fileList.size());

        changeBtn.setOnClickListener(new OnClickListener() {
            int index = 0;

            @Override
            public void onClick(View paramView) {
                if (index >= fileList.size() - 1) {
                    index = 0;
                }
                String fileName = fileList.get(index).getAbsolutePath();
                Bitmap bm = BitmapFactory.decodeFile(fileName);
                imageView1.setImageBitmap(bm);
                textView1.setText(fileName + " => " + index + " / " + fileList.size());
                index++;
            }
        });
    }

    void scanImageFile(File file, List<File> list) {
        if (file == null) {
            return;
        }
        if (file.isDirectory() && file.getName().equalsIgnoreCase(".thumbnails")) {
            return;
        } else if (file.isDirectory()) {
            if (file.listFiles() != null) {
                for (File f : file.listFiles()) {
                    this.scanImageFile(f, list);
                }
            }
        } else {
            if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png") || file.getName().endsWith(".gif") || file.getName().endsWith(".bmp")) {
                System.out.println("==>" + file.getAbsolutePath());
                list.add(file);
            }
        }
    }

    //设置图片指定大小 XXX
    protected Bitmap scaleImg(Bitmap bm, int newWidth, int newHeight) {
        // 图片源
        // Bitmap bm = BitmapFactory.decodeStream(getResources()
        // .openRawResource(id));
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 设置想要的大小
        int newWidth1 = newWidth;
        int newHeight1 = newHeight;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth1) / width;
        float scaleHeight = ((float) newHeight1) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    //圖片以畫面寬度縮放比例
    protected Bitmap scaleImg(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int newWidth1 = dm.widthPixels;
        float scaleWidth = ((float) newWidth1) / width;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleWidth);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    void back() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                BitmapActivity.this.setResult(RESULT_CANCELED, BitmapActivity.this.getIntent());
                BitmapActivity.this.finish();
            }
        });
    }
}
