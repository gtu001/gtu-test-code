package com.example.gtuandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RememberPosListViewActivity extends Activity {
    private ListView mListView;
    private String[] list = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    private static int position;
    private static int top;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_remember);
        mListView = (ListView)findViewById(R.id.list_view);
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list));
        mListView.setOnScrollListener(new OnScrollListener(){

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                    int visibleItemCount, int totalItemCount) {
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //記錄點選位置
                position = mListView.getFirstVisiblePosition();
                View v = mListView.getChildAt(0);
                top = (v == null) ? 0 : v.getTop();
            }
        });
        
    }
    
    @Override
    protected void onResume() {
        //回復的時候回到點選位置
        mListView.setSelectionFromTop(position, top);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}