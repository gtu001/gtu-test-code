package com.example.gtu001.qrcodemaker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.gtu001.qrcodemaker.common.LayoutViewHelper;
import com.example.gtu001.qrcodemaker.common.Log;
import com.example.gtu001.qrcodemaker.custom_dialog.UrlPlayerDialog_bg;
import com.example.gtu001.qrcodemaker.util.FileUtil;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BigFileActivity extends Activity {

    private static final String TAG = TvConnActivity.class.getSimpleName();

    private ListView listView;
    private BaseAdapter baseAdapter;
    private InitListViewHandler initListViewHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = LayoutViewHelper.createContentView_simple(this);

        //初始Btn狀態紐
        Button btn2 = new Button(this);
        btn2.setText("TODO");
        layout.addView(btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        //初始listView
        listView = new ListView(this);
        layout.addView(listView, //
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
//        LayoutViewHelper.setViewHeight(listView, 1000);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> item = (Map<String, Object>) listView.getAdapter().getItem(position);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Map<String, Object> item = (Map<String, Object>) listView.getAdapter().getItem(position);
                final FileZ item2 = (FileZ) item.get("item");

                String[] items = new String[]{"移動到下載", "刪除"};

                AlertDialog dlg = new AlertDialog.Builder(BigFileActivity.this)//
                        .setTitle("選擇操作項目")//
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        initListViewHandler.moveToDownload(item2);
                                        break;
                                    case 1:
                                        boolean ok = initListViewHandler.deleteRealFile(item2.file);
                                        if (ok) {
                                            initListViewHandler.deleteFromLst(item2.file);
                                        }
                                        break;
                                    default:
                                        Toast.makeText(BigFileActivity.this, "Unknow choice " + which, Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        })//
                        .create();
                dlg.show();
                return true;
            }
        });

        initServices();
    }


    private void initServices() {
        initListViewHandler = new InitListViewHandler();
        initListViewHandler.initListView();
    }

    private class FileZ {
        File file;
        String name;
        String sizeDescription;

        FileZ(File f) {
            this.file = f;
            this.name = f.getName();
            sizeDescription = FileUtil.getSizeDescription(f.length());
        }
    }

    private class InitListViewHandler {
        List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
        Handler handler = new Handler();

        public List<FileZ> getTotalUrlList() {
            List<FileZ> lst = new ArrayList<>();
            for (Map<String, Object> m : listItem) {
                FileZ b = new FileZ(null);
                lst.add(b);
            }
            return lst;
        }

        private boolean moveToDownload(FileZ item2) {
            File newFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), item2.file.getName());
            if (newFile.exists()) {
                Toast.makeText(BigFileActivity.this, "目的已存在無法移動 : " + newFile.getName(), Toast.LENGTH_SHORT).show();
                return false;
            }
            FileUtil.moveFile(item2.file, newFile);
            Toast.makeText(BigFileActivity.this, "移動" + (newFile.exists() ? "成功" : "失敗") + " : " + newFile.getName(), Toast.LENGTH_SHORT).show();
            return newFile.exists();
        }

        private boolean deleteRealFile(File f) {
            f.delete();
            Toast.makeText(BigFileActivity.this, "刪除" + (!f.exists() ? "成功" : "失敗") + " : " + f.getName(), Toast.LENGTH_SHORT).show();
            return !f.exists();
        }

        private void deleteFromLst(File f) {
            boolean deleteOk = false;
            for (int ii = 0; ii < listItem.size(); ii++) {
                Map<String, Object> vo = listItem.get(ii);
                FileZ vo2 = (FileZ) vo.get("item");
                if (StringUtils.equals(vo2.file.getAbsolutePath(), f.getAbsolutePath())) {
                    listItem.remove(vo);
                    ii--;
                }
            }
            Toast.makeText(BigFileActivity.this, "刪除" + (deleteOk ? "成功" : "失敗"), Toast.LENGTH_SHORT).show();
            if (deleteOk) {
                baseAdapter.notifyDataSetChanged();
            }
        }

        private boolean add(File f) {
            if (f == null || !f.exists()) {
                Toast.makeText(BigFileActivity.this, "file不可為空!", Toast.LENGTH_SHORT).show();
                return false;
            }
            listItem.add(getItem2Map(new FileZ(f)));
            baseAdapter.notifyDataSetChanged();
            return true;
        }

        private Map<String, Object> getItem2Map(FileZ item) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("item_image", null);// 圖像資源的ID
            map.put("item_title", item.name);
            map.put("item_text", item.file.getAbsoluteFile());
            map.put("item_image_check", null);
            map.put("ItemTextDesc", item.sizeDescription);
            map.put("item", item);
            return map;
        }

        private SimpleAdapter createSimpleAdapter() {
            SimpleAdapter listItemAdapter = new SimpleAdapter(BigFileActivity.this, listItem,// 資料來源
                    R.layout.subview_listview, //
                    new String[]{"item_title", "item_text", "item_image", "item_image_check", "ItemTextDesc"}, //
                    new int[]{R.id.ItemTitle, R.id.ItemText, R.id.ItemImage, R.id.ImageView01, R.id.ItemTextDesc}//
            );
            return listItemAdapter;
        }

        private void reloadDBData() {
            listItem.clear();
            BigFileScanner scan = new BigFileScanner();

            List<File> lst = scan.getLst(200);
            for (File f : lst) {
                this.add(f);
            }
        }

        private void initListView() {
            baseAdapter = createSimpleAdapter();
            reloadDBData();
            listView.setAdapter(baseAdapter);
            baseAdapter.notifyDataSetChanged();
        }
    }

    private class BigFileScanner {
        List<File> fileLst = new ArrayList<File>();

        public List<File> getLst(int limit) {
            if (fileLst.size() < limit) {
                limit = fileLst.size();
            }
            return fileLst.subList(0, limit);
        }

        private BigFileScanner() {
            File rootFile = Environment.getExternalStorageDirectory();
            appendFile(rootFile, fileLst);

            Collections.sort(fileLst, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    return new Long(o1.length()).compareTo(o2.length()) * -1;
                }
            });
        }

        private void appendFile(File file, List<File> fileLst) {
            if (!file.exists()) {
                return;
            }
            if (file.isDirectory()) {
                if (file.listFiles() != null) {
                    for (File f : file.listFiles()) {
                        appendFile(f, fileLst);
                    }
                }
            } else {
                if (file.length() > (1024 * 1024)) {
                    fileLst.add(file);
                }
            }
        }
    }

}

