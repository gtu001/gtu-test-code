package com.example.gtu001.qrcodemaker;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.gtu001.qrcodemaker.common.AudioFileType;
import com.example.gtu001.qrcodemaker.common.FileConstantAccessUtil;
import com.example.gtu001.qrcodemaker.common.LayoutViewHelper;
import com.example.gtu001.qrcodemaker.config.Constant;
import com.example.gtu001.qrcodemaker.custom_dialog.FilePlayerDialog;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gtu001 on 2017/12/12.
 */

public class RecorderMainActivity extends Activity {

    private static final String TAG = RecorderMainActivity.class.getSimpleName();

    private ListView listView;
    private SimpleAdapter listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        LinearLayout layout = LayoutViewHelper.createContentView(this);

        listView = new ListView(this);
        layout.addView(listView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        listViewAdapter = createSimpleAdapter();
        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> map = (Map<String, Object>) listViewAdapter.getItem(position);
                File file = (File) map.get("file");
                if (!file.exists()) {
                    Toast.makeText(RecorderMainActivity.this, "檔案遺失!", Toast.LENGTH_SHORT).show();
                    return;
                }
                FilePlayerDialog dialog = new FilePlayerDialog(RecorderMainActivity.this);
                dialog.setFile(file);
                Dialog mDialog = dialog.build();
                mDialog.show();
                Toast.makeText(RecorderMainActivity.this, "--" + mDialog.isShowing(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private SimpleAdapter createSimpleAdapter() {
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        File recordDir = FileConstantAccessUtil.getFileDir(this, new File(Constant.RECORD_DIR));
        if (recordDir.listFiles() != null) {
            File[] fileLst = recordDir.listFiles();
            for (int i = 0; i < fileLst.length; i++) {
                File file = fileLst[i];
                if (AudioFileType.isAudioFileType(file.getName())) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("item_image", null);// 圖像資源的ID
                    map.put("item_title", file.getName());
                    map.put("item_text", DateFormatUtils.format(file.lastModified(), "yyyy/MM/dd HH:mm:ss"));
                    map.put("item_image_check", null);
                    map.put("file", file);
                    listItem.add(map);
                }
            }
        }
        Toast.makeText(this, "目錄 : " + recordDir + " 檔案數 : " + listItem.size(), Toast.LENGTH_SHORT).show();
        SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem,// 資料來源
                R.layout.subview_listview, //
                new String[]{"item_title", "item_text", "item_image", "item_image_check"}, //
                new int[]{R.id.ItemTitle, R.id.ItemText, R.id.ItemImage, R.id.ImageView01}//
        );
        return listItemAdapter;
    }
}
