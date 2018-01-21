package com.example.gtuandroid;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListActivityActivity extends ListActivity {

    int selectedItem = -1;
    String[] mString;
    static final int MENU_LIST1 = Menu.FIRST;
    static final int MENU_LIST2 = Menu.FIRST + 1;
    ArrayAdapter<String> mla;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        selectedItem = position;
        Toast.makeText(ListActivityActivity.this, mString[position], Toast.LENGTH_LONG).show();
        super.onListItemClick(l, v, position, id);

        //當下拉清單有改變,需要刷新執行的動作 XXX
        mla.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int idGroup1 = 0;
        int orderMenuItem1 = Menu.NONE;
        int orderMenuItem2 = Menu.NONE + 1;
        menu.add(idGroup1, MENU_LIST1, orderMenuItem1, R.string.hello_world);
        menu.add(idGroup1, MENU_LIST2, orderMenuItem2, R.string.hello_world);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case MENU_LIST1:
            mString = new String[] { "aaa", "bbb", "ccc", "ddd" };
            mla = new ArrayAdapter<String>(ListActivityActivity.this, android.R.layout.simple_list_item_1, mString);
            ListActivityActivity.this.setListAdapter(mla);
            break;
        case MENU_LIST2:
            mString = new String[] { "111", "222", "333", "444" };
            mla = new ArrayAdapter<String>(ListActivityActivity.this, android.R.layout.simple_list_item_1, mString);
            ListActivityActivity.this.setListAdapter(mla);
            break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    /**
     * 刷新ListActivity
     */
    private void updateListActivity(){
        final int WHAT_INT = 1;
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                case WHAT_INT:
                    mla = new ArrayAdapter<String>(ListActivityActivity.this, android.R.layout.simple_dropdown_item_1line, mString);
                    setListAdapter(mla);
                    Log.v("updateLIST", "### updateLIST !!!");
                    break;
                }
                super.handleMessage(msg);
            }
        };
        Thread changeListThread = new Thread(Thread.currentThread().getThreadGroup(), new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                Bundle bundle = new Bundle();
                message.setData(bundle);
                message.what = WHAT_INT;
                handler.sendMessage(message);
            }
        }, "changeListThread..");
        changeListThread.start();
    }
}
