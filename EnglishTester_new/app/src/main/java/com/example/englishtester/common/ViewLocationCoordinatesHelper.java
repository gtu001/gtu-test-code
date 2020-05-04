package com.example.englishtester.common;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

public class ViewLocationCoordinatesHelper {

    public static void onDrawCompleted(final View mainLayout, final Runnable runnable) {
        // inflate your main layout here (use RelativeLayout or whatever your root ViewGroup type is

//       final LinearLayout mainLayout = (LinearLayout ) this.getLayoutInflater().inflate(R.layout.main, null);

        // set a global layout listener which will be called when the layout pass is completed and the view is drawn
        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        //Remove the listener before proceeding
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            mainLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            mainLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }

                        // measure your views here
                        new Handler().post(runnable);
                    }
                }
        );
    }


    public static int[] getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        int width = size.x;
        return new int[]{width, height};
    }

    public static int[] getViewLocation(View myView) {
        int[] location = new int[2];
        myView.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        return location;
    }

    public static Rect getViewBound(View myView, boolean isRelativeParent) {
        Rect rectf = new Rect();
        if (isRelativeParent) {
            //For coordinates location relative to the parent
            myView.getLocalVisibleRect(rectf);
        } else {
            //For coordinates location relative to the screen/display
            myView.getGlobalVisibleRect(rectf);
        }
        return rectf;
    }
}
