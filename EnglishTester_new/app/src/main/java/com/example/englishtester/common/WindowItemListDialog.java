package com.example.englishtester.common;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;

import com.example.englishtester.common.Log;

import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.englishtester.R;

import java.util.ArrayList;
import java.util.List;

public class WindowItemListDialog {

    private static final String TAG = WindowItemListDialog.class.getSimpleName();

    WindowManager mWindowManager;
    Context context;
    LinearLayout outterLayout;

    //textView 設定style
    WindowItemListDialog_SettingTextView interfs;
    //設定ScrollListener
    AbsListView.OnScrollListener scrollListener;
    //是否自動關閉
    boolean autoClose = true;

    MyAdapter myAdapter;

    public WindowItemListDialog(final WindowManager mWindowManager, Context context) {
        this.mWindowManager = mWindowManager;
        this.context = context;
    }

    public void showItemListDialog(String title, final List<String> simList2, final OnItemClickListener listener) {
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

//        ArrayAdapter<String> aryAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, simList2.toArray(new String[0]));
//        ArrayAdapter<String> aryAdapter = new ArrayAdapter<String>(context, R.layout.subview_dropdown_simple, android.R.id.text1, simList2.toArray(new String[0]));
        ArrayAdapter<String> aryAdapter = new ArrayAdapter<String>(context, R.layout.subview_propview_singletext, R.id.ItemTitle, simList2.toArray(new String[0])) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(R.id.ItemTitle);
                return view;
            }
        };
//        listView.setAdapter(aryAdapter);

        this.myAdapter = new MyAdapter(context, simList2, interfs);
        listView.setAdapter(myAdapter);

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

                if (autoClose) {
                    mWindowManager.removeView(outterLayout);
                }
                Log.v(TAG, "#listView.setOnItemClickListener");
            }
        });

        if (scrollListener != null) {
            listView.setOnScrollListener(scrollListener);
        }
        mWindowManager.addView(outterLayout, getInitLayoutParams());
    }

    public interface WindowItemListDialog_SettingTextView {
        void apply(TextView text);
    }

    public static class MyAdapter extends BaseAdapter {
        List<String> bmpList;
        Context mContext;
        WindowItemListDialog_SettingTextView interf;

        MyAdapter(Context mContext, List<String> bmpList, WindowItemListDialog_SettingTextView interfs) {
            this.bmpList = bmpList;
            this.mContext = mContext;
            this.interf = interfs;
        }

        @Override
        public int getCount() {
            return bmpList.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup arg2) {
            View v = view;
            Holder holder;
            if (v == null) {
                v = LayoutInflater.from(mContext).inflate(R.layout.subview_propview_singletext, null);
                holder = new Holder();
                holder.text = (TextView) v.findViewById(R.id.ItemTitle);
                v.setTag(holder);
            } else {
                holder = (Holder) v.getTag();
            }
            holder.text.setText(bmpList.get(position));

            //自訂style
            if (interf != null) {
                interf.apply(holder.text);
            }
            return v;
        }

        class Holder {
            TextView text;
        }

        public List<String> getBmpList() {
            return this.bmpList;
        }
    }

    public static LayoutParams getInitLayoutParams() {
        WindowManager.LayoutParams innerParams = new WindowManager.LayoutParams();
        //innerParams.type = LayoutParams.TYPE_PHONE;  //TYPE_PHONE
        innerParams.format = PixelFormat.RGBA_8888;
        innerParams.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        innerParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        innerParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        innerParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //wmParams.type = LayoutParams.TYPE_PHONE; //LayoutParams.TYPE_PHONE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            innerParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            innerParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        return innerParams;
    }

    public void dismiss() {
        if (outterLayout != null) {
            mWindowManager.removeViewImmediate(outterLayout);
        }
        outterLayout = null;
    }

    /**
     * textView 設定style
     */
    public void setItemTextViewStyle(WindowItemListDialog_SettingTextView interfs) {
        this.interfs = interfs;
    }

    /**
     * 設定點擊ItemList選項
     */
    public void setItemListOnScrollListener(AbsListView.OnScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    public MyAdapter getMyAdapter() {
        return this.myAdapter;
    }

    /**
     * 設定是否自動關閉
     */
    public void setAutoClose(boolean autoClose) {
        this.autoClose = autoClose;
    }
}
