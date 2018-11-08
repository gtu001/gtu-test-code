package com.example.gtu001.qrcodemaker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.gtu001.qrcodemaker.common.AppListService;
import com.example.gtu001.qrcodemaker.common.LayoutViewHelper;
import com.example.gtu001.qrcodemaker.common.Log;
import com.example.gtu001.qrcodemaker.common.SimpleAdapterDecorator;
import com.example.gtu001.qrcodemaker.services.YoutubeVideoService;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppListFilterActivity extends Activity {

    private static final String TAG = AppListFilterActivity.class.getSimpleName();


    private ListView listView;
    private BaseAdapter baseAdapter;
    private InitListViewHandler initListViewHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = LayoutViewHelper.createContentView(this);

        final EditText filterText = new EditText(this);
        layout.addView(filterText);

        //初始Btn狀態紐
        Button btn1 = new Button(this);
        btn1.setText("過濾");
        layout.addView(btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initListViewHandler.findByText(filterText.getText().toString());
            }
        });

        //初始listView
        listView = new ListView(this);
        layout.addView(listView, //
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        LayoutViewHelper.setViewHeight(listView, 2000);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> item = (Map<String, Object>) listView.getAdapter().getItem(position);
                AppListService.AppInfo app = (AppListService.AppInfo) item.get("item");
                app.run(AppListFilterActivity.this);
            }
        });

        initListViewHandler = new InitListViewHandler(this);
        initListViewHandler.init();
    }

    private class InitListViewHandler {
        List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();

        Context context;

        private InitListViewHandler(Context context) {
            this.context = context;
        }

        private Map<String, Object> getItem2Map(AppListService.AppInfo item) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("item_image", null);// 圖像資源的ID
            map.put("item_title", item.getLabel());
            map.put("item_text", item.getInstalledPackage());
            map.put("item_image_check", item.getIcon());
            map.put("item", item);
            return map;
        }

        private List<Map<String, Object>> findAll() {
            List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
            List<AppListService.AppInfo> lst = AppListService.getInstance().loadAllAppList(context);
            for (AppListService.AppInfo app : lst) {
                listItem.add(getItem2Map(app));
            }
            return listItem;
        }

        public List<Map<String, Object>> _findByText(String text) {
            if (listItem.isEmpty()) {
                listItem = findAll();
            }
            if (StringUtils.isBlank(text)) {
                return listItem;
            }

            text = text.toLowerCase();

            List<Map<String, Object>> listItem22 = new ArrayList<Map<String, Object>>();
            for (Map<String, Object> map : listItem) {
                AppListService.AppInfo app = (AppListService.AppInfo) map.get("item");

                if (app.getLabel().toLowerCase().contains(text) || //
                        app.getInstalledPackage().toLowerCase().contains(text)) {
                    listItem22.add(map);
                }
            }
            return listItem22;
        }

        private SimpleAdapter createSimpleAdapter(List<Map<String, Object>> listItem) {
            SimpleAdapter listItemAdapter = new SimpleAdapter(AppListFilterActivity.this, listItem,// 資料來源
                    R.layout.subview_listview, //
                    new String[]{"item_title", "item_text", "item_image", "item_image_check"}, //
                    new int[]{R.id.ItemTitle, R.id.ItemText, R.id.ItemImage, R.id.ImageView01}//
            );

            SimpleAdapterDecorator.apply4Bitmap(listItemAdapter);
            return listItemAdapter;
        }

        public void init() {
            baseAdapter = createSimpleAdapter(findAll());
            listView.setAdapter(baseAdapter);
            baseAdapter.notifyDataSetChanged();
        }

        public void findByText(String text) {
            baseAdapter = createSimpleAdapter(_findByText(text));
            listView.setAdapter(baseAdapter);
            baseAdapter.notifyDataSetChanged();
        }
    }
}

