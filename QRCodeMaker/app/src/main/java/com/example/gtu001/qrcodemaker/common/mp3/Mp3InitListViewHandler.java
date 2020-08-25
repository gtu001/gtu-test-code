package com.example.gtu001.qrcodemaker.common.mp3;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.gtu001.qrcodemaker.Mp3Bean;
import com.example.gtu001.qrcodemaker.R;
import com.example.gtu001.qrcodemaker.common.Log;
import com.example.gtu001.qrcodemaker.common.SharedPreferencesUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class Mp3InitListViewHandler {
    private static final String TAG = Mp3InitListViewHandler.class.getSimpleName();

    public static final String REF_KEY = "Map3PlayerActivity_RefKey";
    public static final String BUNDLE_KEY_SAVELST = "saveLst";
    public static final String BUNDLE_KEY_CURRENT = "current";

    private BaseAdapter baseAdapter;
    List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
    Context context;
    private ListView listView;

    public Mp3InitListViewHandler(ListView listView, Context context) {
        this.listView = listView;
        this.context = context;
    }

    public void delete(FileItem item2) {
        for (int ii = 0; ii < listItem.size(); ii++) {
            Map<String, Object> m = listItem.get(ii);
            Mp3Bean b = new Mp3Bean();
            FileItem y = (FileItem) m.get("item");
            if (y == item2) {
                listItem.remove(ii);
                ii--;
            }
        }
        baseAdapter.notifyDataSetChanged();
    }

    public List<Mp3Bean> getTotalUrlList() {
        List<Mp3Bean> lst = new ArrayList<>();
        for (Map<String, Object> m : listItem) {
            Mp3Bean b = new Mp3Bean();
            FileItem y = (FileItem) m.get("item");
            b.setName(y.name);
            b.setUrl(y.videoUrl);
            lst.add(b);
        }
        return lst;
    }


    public void restoreTotalUrlList(Context context) {
        if (!SharedPreferencesUtil.hasData(context, REF_KEY, BUNDLE_KEY_SAVELST)) {
            return;
        }
        listItem = new ArrayList<Map<String, Object>>();
        JSONArray arry = JSONArray.fromObject(SharedPreferencesUtil.getData(context, REF_KEY, BUNDLE_KEY_SAVELST));
        for (int ii = 0; ii < arry.size(); ii++) {
            JSONObject obj = arry.getJSONObject(ii);
            FileItem f = new FileItem(obj.getString("name"), obj.getString("url"));
            listItem.add(getItem2Map(f));
        }
        initListView();
    }

    public static boolean saveCurrentMp3(String name, String url, int currentPosition, Context context) {
        boolean isNameChange = false;
        if (SharedPreferencesUtil.hasData(context, REF_KEY, BUNDLE_KEY_CURRENT)) {
            JSONObject jsonObj = JSONObject.fromObject(SharedPreferencesUtil.getData(context, REF_KEY, BUNDLE_KEY_CURRENT));
            isNameChange = !StringUtils.equals(name, jsonObj.getString("name"));
        }
        JSONObject obj = new JSONObject();
        obj.put("name", name);
        obj.put("url", url);
        obj.put("currentPosition", currentPosition);
        SharedPreferencesUtil.putData(context, REF_KEY, BUNDLE_KEY_CURRENT, obj.toString());
        return isNameChange;
    }

    public void saveTotalUrlList(Context context) {
        JSONArray arry = new JSONArray();
        for (Map<String, Object> m : listItem) {
            FileItem y = (FileItem) m.get("item");
            JSONObject obj = new JSONObject();
            obj.put("name", y.name);
            obj.put("url", y.videoUrl);
            arry.add(obj);
        }
        SharedPreferencesUtil.putData(context, REF_KEY, BUNDLE_KEY_SAVELST, arry.toString());
    }

    public Mp3Bean getCurrentPlayBean(Context context) {
        if (!SharedPreferencesUtil.hasData(context, REF_KEY, BUNDLE_KEY_CURRENT)) {
            Toast.makeText(context, "沒有一次撥放紀錄!", Toast.LENGTH_LONG).show();
            Mp3Bean bean = new Mp3Bean();
            bean.setName("查無檔案");
            bean.setUrl("查無檔案");
            return bean;
        }
        String parseString = SharedPreferencesUtil.getData(context, REF_KEY, BUNDLE_KEY_CURRENT);
        JSONObject obj = JSONObject.fromObject(parseString);
        Mp3Bean bean = new Mp3Bean();
        bean.setName(obj.getString("name"));
        bean.setUrl(obj.getString("url"));
        bean.setLastPosition(String.valueOf(obj.getInt("currentPosition")));
        return bean;
    }

    private Map<String, Object> getItem2Map(FileItem item) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("item_image", null);// 圖像資源的ID
        map.put("item_title", item.name);
        String itemText = item.videoUrl;
        if (item.file != null) {
            itemText = item.file.toString();
        }
        map.put("item_text", itemText);
        map.put("item_image_check", null);
        map.put("item", item);
        return map;
    }

    private SimpleAdapter createSimpleAdapter() {
        SimpleAdapter listItemAdapter = new SimpleAdapter(context, listItem,// 資料來源
                R.layout.subview_listview, //
                new String[]{"item_title", "item_text", "item_image", "item_image_check"}, //
                new int[]{R.id.ItemTitle, R.id.ItemText, R.id.ItemImage, R.id.ImageView01}//
        );
        return listItemAdapter;
    }

    public void initListView() {
        baseAdapter = createSimpleAdapter();
        listView.setAdapter(baseAdapter);
        baseAdapter.notifyDataSetChanged();
    }

    public boolean add(File file) {
        FileItem vo = new FileItem(file);
        listItem.add(getItem2Map(vo));
        baseAdapter.notifyDataSetChanged();
        return true;
    }

    public static class FileItem {
        public String name;
        public File file;
        public String videoUrl;

        public FileItem(String name, String videoUrl) {
            this.name = name;
            this.videoUrl = videoUrl;
        }

        public FileItem(File file) {
            this.name = file.getName();
            this.file = file;
            try {
                this.videoUrl = file.toURL().toString();
            } catch (Exception e) {
                Log.e(TAG, "FileItem <init> ERR : " + e.getMessage(), e);
            }
        }
    }
}