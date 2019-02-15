package com.example.englishtester;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.englishtester.EnglishwordInfoDAO.EnglishWord;
import com.example.englishtester.common.DialogSingleInput;
import com.example.englishtester.common.DialogSingleInput.DialogConfirmClickListener;
import com.example.englishtester.common.DropboxUtilV2;
import com.example.englishtester.common.FileUtilGtu;
import com.example.englishtester.common.FullPageMentionDialog;
import com.example.englishtester.common.Log;
import com.example.englishtester.common.ReaderCommonHelper;
import com.example.englishtester.common.TextToSpeechComponent;
import com.example.englishtester.common.interf.IDropboxFileLoadService;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.map.LRUMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gtu._work.etc.EnglishTester_Diectory;
import gtu._work.etc.EnglishTester_Diectory.WordInfo;
import gtu._work.etc.EnglishTester_Diectory_Factory;
import gtu.util.DateUtil;

public class DropboxHtmlListActivity extends Activity implements AdapterView.OnItemClickListener { //ListActivity

    private static final String TAG = DropboxHtmlListActivity.class.getSimpleName();

    public static final String BUNDLE_FILE = "file";
    public static final String BUNDLE_FILENAME = "fileName";

    IDropboxFileLoadService dropboxFileLoadService;
    DropboxListItemLoader dropboxListItemLoader;
    DropboxHtmlService dropboxHtmlService;

    ListView listView;
    EditText searchTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_showword_list);

        listView = (ListView) findViewById(android.R.id.list);
        searchTextView = (EditText) findViewById(R.id.searchTextView);

        initServices();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        Log.v(TAG, "extras -- " + extras);
        if (extras != null) {
        }

        // 取得wordList
        initList();

        // 初始化事件
        initViewEvent();
    }

    private void initServices() {
        dropboxFileLoadService = DropboxFileLoadService.newInstance(this, DropboxApplicationActivity.getDropboxAccessToken(this));
        dropboxHtmlService = new DropboxHtmlService(this);
        dropboxListItemLoader = new DropboxListItemLoader(this);
    }

    private static class DropboxListItemLoader {
        Context context;
        List<Map<String, Object>> listItem;
        List<Map<String, Object>> listItemBackup;
        SimpleAdapter aryAdapter;
        IDropboxFileLoadService dropboxFileLoadService;
        DropboxHtmlService dropboxHtmlService;
        ListView listView;

        DropboxListItemLoader(DropboxHtmlListActivity self) {
            this.context = self;
            this.dropboxFileLoadService = self.dropboxFileLoadService;
            this.listView = self.listView;
            this.dropboxHtmlService = self.dropboxHtmlService;
        }

        //判斷是否有閱讀過的紀錄
        Transformer<String, String> transformer = new Transformer<String, String>() {
            @Override
            public String transform(String fileName) {
                Pattern ptn = Pattern.compile("(.*)\\.(?:txt|htm|html)");
                Matcher mth = ptn.matcher(fileName);
                if (mth.find()) {
                    fileName = mth.group(1);
                }
                ReaderCommonHelper.ScrollYService scrollYService = new ReaderCommonHelper.ScrollYService(fileName, context);
                int scrollY = scrollYService.getScrollYVO_value();
                int maxHeight = scrollYService.getMaxHeightYVO_value();
                if (scrollY == -1 || maxHeight == -1) {
                    return "";
                }
                try {
                    BigDecimal val = new BigDecimal(scrollY).divide(new BigDecimal(maxHeight), 4, RoundingMode.HALF_UP);
                    val = val.multiply(new BigDecimal(100));
                    val = val.setScale(1, RoundingMode.HALF_UP);
                    return "[已讀:" + String.valueOf(val) + "%]";
                } catch (Exception ex) {
                    return "";
                }
            }
        };

        private void loadFromDropbox() {
            final List<DropboxUtilV2.DropboxUtilV2_DropboxFile> fileLst = dropboxFileLoadService.listFileV2();
            if (fileLst == null || fileLst.isEmpty()) {
                Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                return;
            }
            Collections.sort(fileLst, new Comparator<DropboxUtilV2.DropboxUtilV2_DropboxFile>() {
                @Override
                public int compare(DropboxUtilV2.DropboxUtilV2_DropboxFile o1, DropboxUtilV2.DropboxUtilV2_DropboxFile o2) {
                    return new Long(o1.getClientModify()).compareTo(o2.getClientModify());
                }
            });
            for (int ii = 0; ii < fileLst.size(); ii++) {
                if (fileLst.get(ii).isFolder()) {
                    fileLst.remove(ii);
                    ii--;
                }
            }

            dropboxHtmlService.deleteAll();

            listItem = new ArrayList<>();
            for (DropboxUtilV2.DropboxUtilV2_DropboxFile f : fileLst) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("ItemTitle", f.getName());
                map.put("ItemDetail", DateFormatUtils.format(f.getClientModify(), "yyyy/MM/dd HH:mm:ss"));
                map.put("ItemDetail2", transformer.transform(f.getName()));
                map.put("ItemDetailRight", FileUtilGtu.getSizeDescription(f.getSize()));
                map.put("fullPath", f.getFullPath());
                listItem.add(map);

                dropboxHtmlService.addOneByDropboxVO(f);
            }
        }

        private void loadFromDB() {
            List<DropboxHtmlDAO.DropboxHtml> lst = dropboxHtmlService.findAll();

            listItem = new ArrayList<>();
            for (DropboxHtmlDAO.DropboxHtml vo : lst) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("ItemTitle", vo.fileName);
                map.put("ItemDetail", DateFormatUtils.format(vo.uploadDate, "yyyy/MM/dd HH:mm:ss"));
                map.put("ItemDetail2", transformer.transform(vo.fileName));
                map.put("ItemDetailRight", FileUtilGtu.getSizeDescription(vo.fileSize));
                map.put("fullPath", vo.fullPath);
                listItem.add(map);
            }
        }

        private void initList() {
            aryAdapter = new SimpleAdapter(context, listItem,// 資料來源
                    R.layout.subview_dropboxlist, //
                    new String[]{"ItemTitle", "ItemDetail", "ItemDetail2", "ItemDetailRight"}, //
                    new int[]{R.id.ItemTitle, R.id.ItemDetail, R.id.ItemDetail2, R.id.ItemDetailRight}//
            );
            listView.setAdapter(aryAdapter);
        }

        public void initListFromDB() {
            loadFromDB();
            initList();
        }

        public void initListFromDropbox() {
            loadFromDropbox();
            initList();
        }

        public void init() {
            initListFromDB();
            if (listItem.isEmpty()) {
                initListFromDropbox();
            }
        }

        public void search(String text) {
            String searchText = StringUtils.trimToEmpty(text.toString()).toLowerCase();
            Log.v(TAG, "[searchTextView] key : " + searchText);

            if (listItemBackup == null) {
                listItemBackup = new ArrayList<>(listItem);
            }

            if (StringUtils.isNotBlank(searchText)) {
                for (int ii = 0; ii < listItem.size(); ii++) {
                    Map<String, Object> map = listItem.get(ii);
                    String name = getSearchFileTitle(map);
                    if (!name.toLowerCase().contains(searchText)) {
                        listItem.remove(ii);
                        ii--;
                    }
                }
            } else {
                listItem = new ArrayList<>(listItemBackup);
                initList();
            }

            aryAdapter.notifyDataSetChanged();
        }

        private String getSearchFileTitle(Map<String, Object> map) {
            String name = (String) map.get("ItemTitle");
            name = StringUtils.defaultString(name).replaceAll("\\.(txt|htm|html)$", "");
            return name;
        }
    }

    private void initList() {
        dropboxListItemLoader.init();
    }

    public void initViewEvent() {
        listView.setOnItemClickListener(this);

        searchTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dropboxListItemLoader.search(searchTextView.getText().toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        final Map<String, Object> map = dropboxListItemLoader.listItem.get(position);

        //返回
        Intent intent = new Intent();
        String fileName = (String) map.get("ItemTitle");
        String fullPath = (String) map.get("fullPath");
        intent.putExtra(BUNDLE_FILE, fullPath);
        intent.putExtra(BUNDLE_FILENAME, fileName);
        this.setResult(RESULT_OK, intent);
        this.finish();
    }

    public static class DropboxHtmlListActivityStarter {
        public static String getFile(Intent data) {
            return data.getExtras().getString(BUNDLE_FILE);
        }

        public static String getFile(Bundle bundle) {
            return bundle.getString(BUNDLE_FILE);
        }

        public static String getFileName(Intent data) {
            return data.getExtras().getString(BUNDLE_FILENAME);
        }

        public static String getFileName(Bundle bundle) {
            return bundle.getString(BUNDLE_FILENAME);
        }
    }

    static int REQUEST_CODE = 5566;
    static int MENU_FIRST = Menu.FIRST;

    enum TaskInfo {
        RECENT_SEARCH("重新讀取Dropbox", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(DropboxHtmlListActivity activity, Intent intent, Bundle bundle) {
                activity.dropboxListItemLoader.initListFromDropbox();
            }
        }, //
        ;

        final String title;
        final int option;
        final int requestCode;
        final Class<?> clz;

        TaskInfo(String title, int option, int requestCode, Class<?> clz) {
            this.title = title;
            this.option = option;
            this.requestCode = requestCode;
            this.clz = clz;
        }

        protected void onOptionsItemSelected(DropboxHtmlListActivity activity, Intent intent, Bundle bundle) {
            Log.v(TAG, "onOptionsItemSelected = " + this.name());
            intent.setClass(activity, clz);
            intent.putExtras(bundle);
            activity.startActivityForResult(intent, requestCode);
        }

        protected void onActivityResult(DropboxHtmlListActivity activity, Intent intent, Bundle bundle) {
            Log.v(TAG, "onActivityResult TODO!! = " + this.name());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(TAG, "# onOptionsItemSelected");
        super.onOptionsItemSelected(item);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        for (TaskInfo task : TaskInfo.values()) {
            if (item.getItemId() == task.option) {
                task.onOptionsItemSelected(this, intent, bundle);
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "# onActivityResult");
        Bundle bundle_ = new Bundle();
        if (data != null) {
            bundle_ = data.getExtras();
        }
        final Bundle bundle = bundle_;
        Log.v(TAG, "requestCode = " + requestCode);
        Log.v(TAG, "resultCode = " + resultCode);
        for (TaskInfo t : TaskInfo.values()) {
            if (requestCode == t.requestCode) {
                switch (resultCode) {
                    case RESULT_OK:
                        t.onActivityResult(this, data, bundle);
                        break;
                }
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.v(TAG, "# onCreateOptionsMenu");
        for (TaskInfo e : TaskInfo.values()) {
            menu.add(0, e.option, 0, e.title);
        }
        return true;
    }
    // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 功能選單
}