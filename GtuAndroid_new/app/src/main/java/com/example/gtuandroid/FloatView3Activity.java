package com.example.gtuandroid;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;

import com.example.gtuandroid.sub.MoveImageView;

public class FloatView3Activity extends Activity {

    WindowManager.LayoutParams mWindowParams = new WindowManager.LayoutParams();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 获取Service
        WindowManager mWindowManager = (WindowManager) getSystemService("window");

        // 设置窗口类型，一共有三种Application windows, Sub-windows, System windows
        // API中以TYPE_开头的常量有23个
//        mWindowParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        mWindowParams.type = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        // 设置期望的bitmap格式
        mWindowParams.format = PixelFormat.RGBA_8888;

        // 以下属性在Layout Params中常见重力、坐标，宽高
        mWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
        mWindowParams.x = 100;
        mWindowParams.y = 100;

        mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        // 自定义图片视图
        MoveImageView imageView = new MoveImageView(this);
        imageView.setImageResource(R.drawable.ic_launcher);
        imageView.setTitleHeight(getWindow());

        // 添加指定视图
        mWindowManager.addView(imageView, mWindowParams);
    }
}
