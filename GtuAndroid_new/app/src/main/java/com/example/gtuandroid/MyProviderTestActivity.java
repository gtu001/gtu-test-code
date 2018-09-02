package com.example.gtuandroid;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MyProviderTestActivity extends Activity {

    interface MediaSchema {
        String TABLE_NAME = "MediaTable"; // Table Name
        String _ID = "_id"; // ID
        String MEDIA_NAME = "media_name"; // Media Name
        String MIME_TYPE = "mime_type"; // MIME Type
    }

    String[][] mediaData = { //
    { "myimage.jpg", "image/jpeg" }, //
            { "myvideo.mp4", "video/mp4" }, //
            { "myaudio.mp3", "audio/mp3" } //
    };

    Uri[] uri_returned = new Uri[mediaData.length];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        back();

        // 取得Content Provider的Uri
        getIntent().setData(Uri.parse("content://com.gjun.provider.myprovider"));
        Uri uri = getIntent().getData();
        Log.i("MyProviderTestActivity", uri.toString());

        getContentResolver().delete(uri, null, null);
        // 新增全部
        testInsert(uri);
        // 一次查詢全部
        Log.i("MyProviderTestActivity", "Query All:");
        testQuery(uri);
        // 分批查詢
        Log.i("MyProviderTestActivity", "Query Each:");
        for (int i = 0; i < uri_returned.length; i++)
            testQuery(uri_returned[i]);
        // 修改某一筆
        Log.i("MyProviderTestActivity", "Update Someitem:");
        testUpdate(0);
        testQuery(uri_returned[0]);
    }

    private void testInsert(Uri uri) {
        ContentValues[] values = new ContentValues[mediaData.length];
        for (int i = 0; i < mediaData.length; i++) {
            values[i] = new ContentValues();
            values[i].put(MediaSchema.MEDIA_NAME, mediaData[i][0]);
            values[i].put(MediaSchema.MIME_TYPE, mediaData[i][1]);
            uri_returned[i] = getContentResolver().insert(uri, values[i]);
            Log.i("MyProviderTestActivity", uri_returned[i].toString());
        }
    }

    private void testQuery(Uri uri) {
        Cursor cursor = managedQuery(uri, null, null, null, null);
        if (cursor != null) {
            Log.i("MyProviderTestActivity", uri.toString());
            cursor.moveToFirst();
            CharSequence[] list = new CharSequence[cursor.getCount()];
            Log.i("MyProviderTestActivity", "cursor's count = " + cursor.getCount());
            for (int i = 0; i < list.length; i++) {
                Log.i("MyProviderTestActivity", "_ID = " + cursor.getString(0)); // _ID
                Log.i("MyProviderTestActivity", "MEDIA_NAME = " + cursor.getString(1)); // MEDIA_NAME
                Log.i("MyProviderTestActivity", "MIME_TYPE = " + cursor.getString(2)); // MIME_TYPE
                cursor.moveToNext();
            }
            cursor.close();
        } else
            Log.i("MyProviderTestActivity", "cursor==null");
    }

    private void testUpdate(int i) {
        ContentValues values = new ContentValues();
        values = new ContentValues();
        values.put(MediaSchema.MEDIA_NAME, "newimage.jpg");
        values.put(MediaSchema.MIME_TYPE, mediaData[i][1]);
        if (i < uri_returned.length) {
            String selection = uri_returned[i].getPathSegments().get(1);
            int c = getContentResolver().update(uri_returned[i], values, selection, null);
            Log.i("MyProviderTestActivity", c + "");
        }
    }

    void back() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                MyProviderTestActivity.this.setResult(RESULT_CANCELED, MyProviderTestActivity.this.getIntent());
                MyProviderTestActivity.this.finish();
            }
        });
    }
}