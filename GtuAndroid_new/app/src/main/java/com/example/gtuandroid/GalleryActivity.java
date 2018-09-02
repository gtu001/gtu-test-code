package com.example.gtuandroid;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ViewSwitcher.ViewFactory;

public class GalleryActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        back();

        final Gallery gallery1 = (Gallery) findViewById(R.id.gallery);
        // 不知為何抓不到 TODO
        final ImageSwitcher imageSwitcher1 = (ImageSwitcher) findViewById(R.id.image_switcher);

        imageSwitcher1.setFactory(new ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(GalleryActivity.this);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setLayoutParams(//
                        new ImageSwitcher.LayoutParams(//
                                LayoutParams.WRAP_CONTENT, //
                                LayoutParams.WRAP_CONTENT));
                return imageView;
            }
        });

        // 建立imageSwitcher 的 fade in out 方式與初始顯示圖片
        imageSwitcher1.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        imageSwitcher1.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
        imageSwitcher1.setImageResource(R.drawable.sakura01);

        // 設定畫廊相簿Gallery 的監聽功能, 取得選擇到的照片, 顯示在imageSwitcher的 imageView Widget
        // 選擇照片的監聽功能
        gallery1.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
                // 自gallery取得選到的position交給imageSwitcher照片檔名, 會在imageView內展現
                //這個寫法無法設定圖片寬度
//                imageSwitcher1.setImageResource((int) gallery1.getItemIdAtPosition(paramInt));
                
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), (int) gallery1.getItemIdAtPosition(paramInt));
                bitmap = scaleImg(bitmap);
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);//要加getResouces否則圖片不會剛剛好
                imageSwitcher1.setImageDrawable(drawable);
            }
            
            // 圖片以畫面寬度縮放比例
            private Bitmap scaleImg(Bitmap bm) {
                int width = bm.getWidth();
                int height = bm.getHeight();
                DisplayMetrics dm = new DisplayMetrics();
                GalleryActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
                int newWidth1 = dm.widthPixels;
                float scaleWidth = ((float) newWidth1) / width;
                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleWidth);
                Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
                return newbm;
            }

            @Override
            public void onNothingSelected(AdapterView<?> paramAdapterView) {
            }
        });

        gallery1.setAdapter(new BaseAdapter() {
            Integer[] imageIds = { R.drawable.sakura01, R.drawable.sakura02, R.drawable.sakura03, R.drawable.sakura04, };

            @Override
            public int getCount() {
                return imageIds.length;
            }

            @Override
            public Object getItem(int paramInt) {
                return null;
            }

            @Override
            public long getItemId(int paramInt) {
                return imageIds[paramInt];
            }

            @Override
            public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
                ImageView imageView = new ImageView(GalleryActivity.this);
                imageView.setImageResource(imageIds[paramInt]);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setLayoutParams(new Gallery.LayoutParams(200, 150));
                return imageView;
            }
        });
    }

    void back() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                GalleryActivity.this.setResult(RESULT_CANCELED, GalleryActivity.this.getIntent());
                GalleryActivity.this.finish();
            }
        });
    }
}
