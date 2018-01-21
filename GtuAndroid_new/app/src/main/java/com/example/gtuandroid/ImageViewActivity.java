package com.example.gtuandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;

public class ImageViewActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageview);

        Button changePicBtn = (Button) findViewById(R.id.button1);
        final ImageView imageView = (ImageView) findViewById(R.id.imageView1);

        changePicBtn.setOnClickListener(new OnClickListener() {
            int[] drawables = new int[] { R.drawable.ic_action_search, R.drawable.ic_launcher };
            int index = 0;

            @Override
            public void onClick(View paramView) {
                imageView.setImageDrawable(getResources().getDrawable(drawables[index]));
                if (index == drawables.length - 1) {
                    index = 0;
                } else {
                    index++;
                }
            }
        });
        
        //淡入淡出
        fadeOutAndHideImage(imageView);
        
        back();
    }
    
    private void fadeOutAndHideImage(final ImageView img) {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(2000);

        fadeOut.setAnimationListener(new AnimationListener() {
            int[] imgRes = new int[] { R.drawable.ic_action_search, R.drawable.ic_launcher };
            int nowPicPos = 0;
            
            public void onAnimationEnd(Animation animation) {
                nowPicPos %= 2;
                img.setImageResource(imgRes[nowPicPos]);
                nowPicPos++;
                fadeInAndShowImage(img);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });

        img.startAnimation(fadeOut);
    }

    private void fadeInAndShowImage(final ImageView img) {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new AccelerateInterpolator());
        fadeIn.setDuration(2000);

        fadeIn.setAnimationListener(new AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                fadeOutAndHideImage(img);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });

        img.startAnimation(fadeIn);
    }

    void back() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                ImageViewActivity.this.setResult(RESULT_CANCELED, ImageViewActivity.this.getIntent());
                ImageViewActivity.this.finish();
            }
        });
    }
}
