package com.example.englishtester.common;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import com.example.englishtester.common.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.englishtester.R;

public class WindowItemListIconDialog {

    private static final String TAG = WindowItemListIconDialog.class.getSimpleName();
    WindowManager mWindowManager;
    Context context;
    LinearLayout outterLayout;
    
    public WindowItemListIconDialog(final WindowManager mWindowManager, Context context){
        this.mWindowManager = mWindowManager;
        this.context = context;
    }

    public void showItemListDialog(String title, final List<Map<String,Object>> listItem, final OnItemClickListener listener) {
        LayoutInflater inflater = LayoutInflater.from(context);
        outterLayout = (LinearLayout) inflater.inflate(R.layout.activity_float_similar_view, null);

        TextView textView = (TextView) outterLayout.findViewById(R.id.textView);
        textView.setText(title);
        textView.setBackgroundColor(Color.WHITE);
        textView.setTextColor(Color.BLACK);

        ImageView imageView = (ImageView) outterLayout.findViewById(R.id.imageView);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mWindowManager.removeView(outterLayout);
            }
        });

        ListView listView = (ListView) outterLayout.findViewById(R.id.listView);

        listView.setBackgroundColor(Color.GRAY);

        SimpleAdapter aryAdapter = new SimpleAdapter(context, listItem,// 資料來源
                R.layout.subview_propview, //
                new String[] { "ItemImage", "ItemTitle" }, //
                new int[] { R.id.ItemImage, R.id.ItemTitle }//
        );

        listView.setAdapter(aryAdapter);

        outterLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    Log.v(TAG, "touch outside");
                    mWindowManager.removeView(outterLayout);
                }

                // 如果沒有得到focus則關閉編輯視窗
                Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                if (!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                    mWindowManager.removeView(outterLayout);
                }
                return false;
            }
        });

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onItemClick(parent, view, position, id);
                mWindowManager.removeView(outterLayout);
                Log.v(TAG, "#listView.setOnItemClickListener");
            }
        });
        mWindowManager.addView(outterLayout, WindowItemListDialog.getInitLayoutParams());
    }
    
    public void dismiss(){
        if(outterLayout != null){
            mWindowManager.removeViewImmediate(outterLayout);
        }
        outterLayout = null;
    }
}
