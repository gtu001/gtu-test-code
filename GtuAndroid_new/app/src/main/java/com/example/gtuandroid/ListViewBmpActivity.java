package com.example.gtuandroid;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ListViewBmpActivity extends Activity {
    
    private List<Bitmap> bmpList;
    private MyAdapter mMyAdapter;
    private Context mContext;
    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        mContext = getApplicationContext();
        mListView = (ListView) findViewById(R.id.listView1);
        bmpList = new ArrayList<Bitmap>();
        for (int i = 0; i < 100; i++) {
            bmpList.add(null);
            handleWebPic("http://www.jamessearsward32.com/images/raccoon1.jpg", i);
        }
        mMyAdapter = new MyAdapter();
        mListView.setAdapter(mMyAdapter);
    }

    private class MyAdapter extends BaseAdapter {

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
                v = LayoutInflater.from(mContext).inflate(R.layout.subview_listview_bmp, null);
                holder = new Holder();
                holder.img = (ImageView) v.findViewById(R.id.imageView1);
                holder.text = (TextView) v.findViewById(R.id.textView1);
                v.setTag(holder);
            } else {
                holder = (Holder) v.getTag();
            }
            if (bmpList.get(position) == null) {
                holder.img.setImageResource(R.drawable.ic_launcher);
            } else {
                holder.img.setImageBitmap(bmpList.get(position));
            }
            holder.text.setText("" + position);
            holder.text.setTextColor(Color.RED);
            return v;
        }

        class Holder {
            ImageView img;
            TextView text;
        }
    }

    public void handleWebPic(final String url, final int index) {
        ThreadPoolManager.getInstance().addSelectPhotoTask(new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bmp = getUrlPic(url);
                bmpList.set(index, bmp);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mMyAdapter != null) {
                            //刷新List XXX
                            mMyAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        }));
    }

    public synchronized Bitmap getUrlPic(String url) {
        Bitmap webImg = null;
        try {
            URL imgUrl = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) imgUrl.openConnection();
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            int length = httpURLConnection.getContentLength();
            int tmpLength = 512;
            int readLen = 0, desPos = 0;
            byte[] img = new byte[length];
            byte[] tmp = new byte[tmpLength];
            if (length != -1) {
                while ((readLen = inputStream.read(tmp)) > 0) {
                    System.arraycopy(tmp, 0, img, desPos, readLen);
                    desPos += readLen;
                }
                webImg = BitmapFactory.decodeByteArray(img, 0, img.length);
                if (desPos != length) {
                    throw new IOException("Only read" + desPos + "bytes");
                }
            }
            httpURLConnection.disconnect();
        } catch (IOException e) {
            Log.e("IOException", e.toString());
        }
        return webImg;
    }

    static class ThreadPoolManager {
        private static ThreadPoolManager mThreadPoolManager;
        private final int KEEP_ALIVE_TIME = 3;
        // Sets the Time Unit to seconds
        private final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

        // Creates a thread pool manager
        private ThreadPoolManager() {
        }

        public static ThreadPoolManager getInstance() {
            if (null == mThreadPoolManager) {
                mThreadPoolManager = new ThreadPoolManager();
            }
            return mThreadPoolManager;
        }

        private final BlockingQueue<Runnable> mPhotoQueue = new LinkedBlockingQueue<Runnable>();

        private ThreadPoolExecutor mPhotoExcutor = new ThreadPoolExecutor(//
                4, // Initial pool size
                5, // Max pool size
                KEEP_ALIVE_TIME,//
                KEEP_ALIVE_TIME_UNIT,//
                mPhotoQueue);//

        public void addSelectPhotoTask(Runnable runnable) {
            mPhotoExcutor.execute(runnable);
        }
    }

    void back() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                ListViewBmpActivity.this.setResult(RESULT_CANCELED, ListViewBmpActivity.this.getIntent());
                ListViewBmpActivity.this.finish();
            }
        });
    }
}
