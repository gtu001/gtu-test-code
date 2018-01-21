package com.example.gtuandroid;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * 自定細節的ListView
 */
public class ListViewActivity2 extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_listview);
        back();

        ListView list = (ListView) findViewById(R.id.listView1);

        // 生成動態陣列，加入資料
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("item_image", R.drawable.ic_launcher);// 圖像資源的ID
            map.put("item_title", "Level " + i);
            map.put("item_text", "Finished in 1 Min 54 Secs, 70 Moves! ");
            listItem.add(map);
        }
        // 生成適配器的Item和動態陣列對應的元素
        SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem,// 資料來源
                R.layout.subview_listview, // ListItem的XML實現
                // 動態陣列與ImageItem對應的子項
                new String[] { "item_image", "item_title", "item_text" },
                // ImageItem的XML檔裡面的一個ImageView,兩個TextView ID
                new int[] { R.id.ItemImage, R.id.ItemTitle, R.id.ItemText });

        // 添加並且顯示
        list.setAdapter(listItemAdapter);

        // 添加點擊
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                setTitle("點擊第" + arg2 + "個項目");
            }
        });

        // 添加長按點擊
        list.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("長按菜單-ContextMenu");
                menu.add(0, 0, 0, "彈出長按菜單0");
                menu.add(0, 1, 0, "彈出長按菜單1");
            }
        });
    }
    
    //長按功能表回應函數  
    @Override  
    public boolean onContextItemSelected(MenuItem item) {  
        setTitle("點擊了長按菜單裡面的第"+item.getItemId()+"個項目");   
        return super.onContextItemSelected(item);  
    }  

    void back() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                ListViewActivity2.this.setResult(RESULT_CANCELED, ListViewActivity2.this.getIntent());
                ListViewActivity2.this.finish();
            }
        });
    }
}
