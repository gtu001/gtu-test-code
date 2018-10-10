package com.example.gtu001.qrcodemaker;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.gtu001.qrcodemaker.custom_dialog.UrlPlayerDialog;
import com.example.gtu001.qrcodemaker.services.JavaYoutubeVideoUrlHandler;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class YoutubePlayerActivity extends Activity {

    private static final String TAG = TvConnActivity.class.getSimpleName();


    private ListView listView;
    private BaseAdapter baseAdapter;
    private InitListViewHandler initListViewHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = createContentView();

        final EditText youtubeIdText = new EditText(this);
        layout.addView(youtubeIdText);

        //初始Btn狀態紐
        Button btn1 = new Button(this);
        btn1.setText("加入youtube ID");
        layout.addView(btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = initListViewHandler.add(youtubeIdText.getText().toString());
                if (result) {
                    youtubeIdText.setText("");
                }
            }
        });

        //初始listView
        listView = new ListView(this);
        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(listView, //
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, // 設定與螢幕同寬
                        ViewGroup.LayoutParams.MATCH_PARENT));
        layout.addView(scrollView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> item = (Map<String, Object>) listView.getAdapter().getItem(position);
                final YoutubeItem item2 = (YoutubeItem) item.get("item");
                UrlPlayerDialog dialog = new UrlPlayerDialog(YoutubePlayerActivity.this);

                Log.v(TAG, "====================================================================");
                Log.v(TAG, "name = " + item2.name);
                Log.v(TAG, "videoUrl = " + item2.videoUrl);
                Log.v(TAG, "====================================================================");

                Dialog _dialog = dialog.setUrl(item2.name, item2.videoUrl).build();
                _dialog.show();
            }
        });

        initListViewHandler = new InitListViewHandler();
        initListViewHandler.initListView();
    }

    private class InitListViewHandler {
        List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();

        private boolean add(String id) {
            if (StringUtils.isBlank(id)) {
                Toast.makeText(YoutubePlayerActivity.this, "id不可為空!", Toast.LENGTH_SHORT).show();
                return false;
            }
            YoutubeItem item = new YoutubeItem(id);
            boolean result = item.processSelf();
            if (!result) {
                Toast.makeText(YoutubePlayerActivity.this, "無法取得url!", Toast.LENGTH_SHORT).show();
                return false;
            }
            listItem.add(getItem(item));
            baseAdapter.notifyDataSetChanged();
            return true;
        }

        private Map<String, Object> getItem(YoutubeItem item) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("item_image", null);// 圖像資源的ID
            map.put("item_title", item.name);
            map.put("item_text", item.id);
            map.put("item_image_check", null);
            map.put("item", item);
            return map;
        }

        private SimpleAdapter createSimpleAdapter() {
            SimpleAdapter listItemAdapter = new SimpleAdapter(YoutubePlayerActivity.this, listItem,// 資料來源
                    R.layout.subview_listview, //
                    new String[]{"item_title", "item_text", "item_image", "item_image_check"}, //
                    new int[]{R.id.ItemTitle, R.id.ItemText, R.id.ItemImage, R.id.ImageView01}//
            );
            return listItemAdapter;
        }

        private void initListView() {
            baseAdapter = createSimpleAdapter();
            listView.setAdapter(baseAdapter);
            baseAdapter.notifyDataSetChanged();
        }
    }

    private class YoutubeItem {
        String name;
        String id;
        String videoUrl;

        public YoutubeItem(String id) {
            this.name = name;
            this.id = id;
            this.videoUrl = videoUrl;
        }

        public boolean processSelf() {
            final ArrayBlockingQueue queue = new ArrayBlockingQueue(1);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    JavaYoutubeVideoUrlHandler handler = new JavaYoutubeVideoUrlHandler(id, "", JavaYoutubeVideoUrlHandler.DEFAULT_USER_AGENT);
                    handler.execute();
                    YoutubeItem.this.name = handler.getTitle();
                    TreeMap<Long, String> urlMap = new TreeMap<Long, String>();
                    for (JavaYoutubeVideoUrlHandler.VideoUrlConfig vo : handler.getVideoFor91Lst()) {
                        urlMap.put(vo.getLength(), vo.getUrl());
                    }
                    String url = urlMap.get(urlMap.keySet().iterator().next());
                    YoutubeItem.this.videoUrl = url;
                    queue.offer(new Object());
                }
            }).start();

            try {
                queue.poll(5000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Log.e(TAG, "ERROR : " + e.getMessage(), e);
                return false;
            }
            return true;
        }
    }

    private LinearLayout createContentView() {
        LinearLayout layout = new LinearLayout(this);
        ScrollView scroll = new ScrollView(this);
        setContentView(scroll);
        layout.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(layout, //
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, // 設定與螢幕同寬
                        ViewGroup.LayoutParams.WRAP_CONTENT));// 高度隨內容而定
        return layout;
    }
}

