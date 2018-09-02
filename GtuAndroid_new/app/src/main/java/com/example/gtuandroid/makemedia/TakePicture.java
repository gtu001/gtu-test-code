package com.example.gtuandroid.makemedia;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

public class TakePicture extends View implements SurfaceHolder.Callback, Camera.AutoFocusCallback {

    private SurfaceView tempSV;
    private ImageView tempIV;
    Camera camera;
    private SurfaceHolder sHolder;

    public TakePicture(Context context, SurfaceView sv, ImageView iv) {
        super(context);
        this.tempSV = sv;
        this.tempIV = iv;
        initViews();
    }

    // 取得各個視窗元件（物件）
    private void initViews() {
        tempSV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.autoFocus(TakePicture.this); // 拍照前先自動定焦
            }
        });
        sHolder = tempSV.getHolder();
        /*
         * 呼叫addCallback()即是向SurfaceHolder.Callback物件註冊，
         * 如果SurfaceView物件有變化，會自動呼叫對應的方法，所以 必須先實作 SurfaceHolder.Callback共3個方法：
         * surfaceChanged()、surfaceCreated()、surfaceDestroyed()
         */
        sHolder.addCallback(this);
        // SurfaceView物件使用記憶體的方式
        sHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // 按下 ImageView 恢復相機 Preview
        tempIV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tempSV.setVisibility(View.VISIBLE);
                tempIV.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open(); // 取得Camera物件
        try {
            camera.setPreviewDisplay(holder); // 在SurfaceView上顯示相機預覽
        } catch (IOException exception) {
            camera.release();
            camera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // 取得相機參數
        Camera.Parameters parameters = camera.getParameters();
        // 取得照片尺寸
        List<Size> supportedPictureSizes = parameters.getSupportedPictureSizes();
        int sptw = supportedPictureSizes.get(supportedPictureSizes.size() - 1).width;
        int spth = supportedPictureSizes.get(supportedPictureSizes.size() - 1).height;
        for (int i = 0; i < supportedPictureSizes.size(); i++) {
            System.out.println(i + ": getSupportedPictureSizes(w, h)= (" + supportedPictureSizes.get(i).width + ", " + supportedPictureSizes.get(i).height + ")");
        }
        // 取得預覽尺寸
        List<Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        int prvw = supportedPreviewSizes.get(0).width;
        int prvh = supportedPreviewSizes.get(0).height;
        for (int i = 0; i < supportedPreviewSizes.size(); i++) {
            System.out.println(i + ": getSupportedPreviewSizes(w, h)= (" + supportedPreviewSizes.get(i).width + ", " + supportedPreviewSizes.get(i).height + ")");
        }
        // 設定照片尺寸
        parameters.setPictureSize(sptw, spth);
        // 設定預覽尺寸
        parameters.setPreviewSize(prvw, prvh);
        // 儲存設定
        camera.setParameters(parameters);
        camera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        /*
         * 當按下回上一頁按鈕，也就是已經離開這個應用程式的畫面，surfaceDestroyed()方法會被呼叫， 此時應該釋放camera資源。
         */
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    public void bmpSavedToSD(Bitmap image_saved) {

        FileOutputStream fOut;
        try {
            String tmp = "/sdcard/myimage2.jpg";
            fOut = new FileOutputStream(tmp);
            image_saved.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            System.out.println(tmp);
            try {
                fOut.flush();
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 先使用相機的自動定焦功能，然後才拍照
    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        if (success) {
            camera.takePicture(null, null, jpeg);
        }
    }

    Camera.PictureCallback jpeg = new Camera.PictureCallback() {
        // 拍照時，onPictureTaken()會被自動呼叫
        public void onPictureTaken(byte[] imgData, Camera camera) {
            if (imgData != null) {
                Bitmap picture = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
                bmpSavedToSD(picture);
                System.out.println("picture(w, h)= (" + picture.getWidth() + ", " + picture.getHeight() + ")");
                tempIV.setImageBitmap(picture); // 將照片呈現在ImageView上
                tempIV.setVisibility(View.VISIBLE);
                tempSV.setVisibility(View.INVISIBLE);
            }
        }
    };
}