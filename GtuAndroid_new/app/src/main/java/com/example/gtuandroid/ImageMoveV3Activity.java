package com.example.gtuandroid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class ImageMoveV3Activity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐去标题栏（应用程序的名字）
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐去状态栏部分(电池等图标和一切修饰部分)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 设置显示View实例
        setContentView(new MyView(this));
    }

    private class MyView extends View {
        private int textX = 20, textY = 20;

        /**
         * 重写父类构造函数
         * 
         * @param context
         */
        public MyView(Context context) {
            super(context);
            setFocusable(true);
            // setFocusableInTouchMode(true);
        }

        /**
         * 重写父类绘图函数
         */
        @Override
        protected void onDraw(Canvas canvas) {
            // 创建一个画笔的实例
            Paint paint = new Paint();
            // 设置画笔的颜色
            paint.setColor(Color.BLUE);
            // 绘制文本
            paint.setTextSize(60F);
            canvas.drawText("Game", textX, textY, paint);
            super.onDraw(canvas);
        }

        /**
         * 重写按键按下事件函数
         */
        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            // 判定用户按下的键值是否为方向键的“上下左右”键
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                // “上”按键被点击，应该让文本的Y坐标变小
                textY -= 2;
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                // “下”按键被点击，应该让文本的Y坐标变大
                textY += 2;
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                // “左”按键被点击，应该让文本的X坐标变小
                textX -= 2;
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                // “右”按键被点击，应该让文本的X坐标变大
                textX += 2;
            }
            // 重绘画布
            invalidate();
            // postInvalidate();
            return super.onKeyDown(keyCode, event);
        }

        /**
         * 重写按键抬起事件函数
         */
        @Override
        public boolean onKeyUp(int keyCode, KeyEvent event) {
            return super.onKeyUp(keyCode, event);
        }

        /**
         * 重写触屏事件函数
         */
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            // 玩家手指点击屏幕的动作
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                textX = x;
                textY = y;
                // 玩家手指抬起离开屏幕的动作
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                textX = x;
                textY = y;
                // 玩家手指在屏幕上移动的动作
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                textX = x;
                textY = y;
            }
            // 重绘画布
            invalidate();
            // postInvalidate();
            return true;
        }
        
        // @Override
        // public boolean onTouchEvent(MotionEvent event) {
        // // 获取用户手指触屏的X坐标赋值与文本的X坐标
        // textX = (int) event.getX();
        // // 获取用户手指触屏的Y坐标赋值与文本的Y坐标
        // textY = (int) event.getY();
        // // 重绘画布
        // invalidate();
        // // postInvalidate();
        // return true;
        // }
    }

}