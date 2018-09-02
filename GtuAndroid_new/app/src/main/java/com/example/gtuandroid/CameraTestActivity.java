package com.example.gtuandroid;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class CameraTestActivity extends Activity {

    private ImageView imageView;
    private boolean isClicked;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        
        setExample1();

        setExample2();
    }
    
    private void setExample2(){
        final MyPreview preview = new MyPreview(CameraTestActivity.this);
        setContentView(preview);
        preview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 實作失敗TODO
                // preview.takePicture(imageView);
                if (!isClicked){
                    preview.takePicture(imageView);
                    isClicked = true;
                }else{
                    preview.startPreview();
                    isClicked = false;
                }
            }
        });
    }
    
    private void setExample1(){
        LinearLayout content = createContentView();
        imageView = new ImageView(this);
        content.addView(imageView);

        Button button = new Button(this);
        button.setText("相機intent");
        content.addView(button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && data.getExtras() != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    private static class MyPreview extends android.view.SurfaceView implements SurfaceHolder.Callback {
        private static final String TAG = MyPreview.class.getSimpleName();
        SurfaceHolder mHolder;
        Camera mCamera;

        public void takePicture(final ImageView imageView) {
            ShutterCallback shutter = new ShutterCallback() {
                @Override
                public void onShutter() {
                    Log.d(TAG, "shutter");
                }
            };
            PictureCallback raw = new PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    Log.d(TAG, "raw");

                }
            };
            PictureCallback jpeg = new PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    try {
                        Log.v(TAG, "picture data = " + data);
                        if(data != null){
                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                            imageView.setImageBitmap(bitmap);
                            // saveImageToFile(data);
                        }
                    } catch (Exception ex) {
                        Log.e(TAG, "onPictureTaken");
                        ex.printStackTrace();
                        mCamera.release();
                    }
                }

                private void saveImageToFile(byte[] data) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        File file = new File("/sdcard/wjh.jpg");
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        bos.flush();
                        bos.close();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            };

            // 當開始拍照時，會依次調用shutter的onShutter()方法，raw的onPictureTaken方法,jpeg的onPictureTaken方法.
            // 三個参數的作用是shutter--拍照瞬間調用，raw--獲得沒有壓縮過的圖片數據，jpeg---返回jpeg的圖片數據
            // 當你不需要對照片進行處理，可以直接用null代替.
            // 注意，當調用camera.takePiture方法後，camera關閉了預覽，這時需要調用startPreview()來重新開启預覽。
            mCamera.takePicture(shutter, jpeg, jpeg);
        }

        public MyPreview(Context context) {
            super(context);
            mHolder = getHolder();
            mHolder.addCallback(this);// 通過addCallBack()方法將響應的接口绑定到他身上
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        public void startPreview() {
            mCamera.startPreview();
        }

        /*
         * surface 被創建後調用
         */
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mCamera = Camera.open();
            try {
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "surfaceCreated");
                mCamera.release();
                mCamera = null;
            }
        }

        /*
         * 當surfaceView發生改變後調用
         */
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            mCamera.startPreview();
        }

        /*
         * 當surfaceView銷毀時調用
         */
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    private LinearLayout createContentView() {
        LinearLayout layout = new LinearLayout(this);
        ScrollView scroll = new ScrollView(this);
        setContentView(scroll);
        layout.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(layout, //
                new LayoutParams(LayoutParams.FILL_PARENT, // 設定與螢幕同寬
                        LayoutParams.WRAP_CONTENT));// 高度隨內容而定
        return layout;
    }
}
