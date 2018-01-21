package com.example.gtuandroid;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

public class CameraTest2Activity extends Activity implements Callback, OnClickListener, AutoFocusCallback {

    // 能夠拍照了，這下子要考慮如何讓圖片更好看了，這顯然是專業人士的強項，但是我們在程序上，也可以做一些處理，
    // 向上面的那些，因为我直接把surfaceView當做整體布局，就可能出現屏幕被拉開了，不是很好看，所以這時，就可以不要把
    // surfaceView弄成整體布局，把他弄到到一個布局管理器，在設置相關的参數.
    //
    // 這是需要注意的是有些参數不能隨便亂設，
    // 如以下代碼:Camera.Parameters parames = myCamera.getParameters();//獲得参數對象
    // parames.setPictureFormat(PixelFormat.JPEG);//設置圖片格式
    // parames.setPreviewSize(640,480);//這裏面的参數只能是幾個特定的参數，否則會報錯.(176*144,320*240,352*288,480*360,640*480)
    // myCamera.setParameters(parames);
    //
    // 還有自動對焦，當然有些手機沒有這個功能，自動對焦是通過autoFocus()這個方法調用一個自動對焦的接口，並在裏面進行處理。
    // 注意，這個方法必須在startPreview()和stopPreview()中間。
    // AutoFocusCallback是自動對焦的接口，實現它必須實現public void onAutoFocus(boolean success,
    // Camera camera)這個方法，
    // 所以我們可以將拍照方法放在這裏面，然後對焦後再進行拍攝。。效果會好很多。
    //
    // 注意自動對焦需要添加<uses-feature android:name="android.hardware.camera.autofocus"
    // />
    // 下面我叫直接把上面的使用例子直接寫出。
    
    SurfaceView mySurfaceView;// surfaceView聲明
    SurfaceHolder holder;// surfaceHolder聲明
    Camera myCamera;// 相機聲明
    String filePath = "/sdcard/wjh.jpg";// 照片保存路徑
    boolean isClicked = false;// 是否點擊標識

    // 創建jpeg圖片回調數據對象

    PictureCallback jpeg = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            try{// 獲得圖片

                Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
                File file = new File(filePath);
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));

                bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);// 將圖片壓縮到流中
                bos.flush();// 輸出
                bos.close();// 關閉
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// 無標題
        // 設置拍攝方向

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_camera_test);

        // 獲得控件
        mySurfaceView = (SurfaceView) findViewById(R.id.surfaceView1);

        // 獲得句柄

        holder = mySurfaceView.getHolder();

        // 添加回調

        holder.addCallback(this);

        // 設置類型
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        // 設置監聽
        mySurfaceView.setOnClickListener(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // 設置参數並開始預覽
        Camera.Parameters params = myCamera.getParameters();
        params.setPictureFormat(PixelFormat.JPEG);
        params.setPreviewSize(640, 480);
        myCamera.setParameters(params);
        myCamera.startPreview();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 開启相機
        if (myCamera == null){
            myCamera = Camera.open();
            try {
                myCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // 關閉預覽並釋放資源
        myCamera.stopPreview();
        myCamera.release();
        myCamera = null;
    }

    @Override
    public void onClick(View v) {
        if (!isClicked) {
            myCamera.autoFocus(this);// 自動對焦
            isClicked = true;
        } else {
            myCamera.startPreview();// 開启預覽
            isClicked = false;
        }
    }

    @Override
    public void onAutoFocus(boolean success, android.hardware.Camera camera) {
        if (success) {
            // 設置参數,並拍照
            Camera.Parameters params = myCamera.getParameters();
            params.setPictureFormat(PixelFormat.JPEG);
            params.setPreviewSize(640, 480);
            myCamera.setParameters(params);
            myCamera.takePicture(null, null, jpeg);
        }
    }
}