package com.example.gtu001.qrcodemaker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import com.example.gtu001.qrcodemaker.common.AppListService;
import com.example.gtu001.qrcodemaker.common.LayoutViewHelper;
import com.example.gtu001.qrcodemaker.common.Log;
import com.example.gtu001.qrcodemaker.common.SimpleAdapterDecorator;
import com.example.gtu001.qrcodemaker.common.TitleUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppListFilterActivity extends Activity {

    private static final String TAG = AppListFilterActivity.class.getSimpleName();


    private ListView listView;
    private BaseAdapter baseAdapter;
    private InitListViewHandler initListViewHandler;

    private Button btn1;
    private EditText filterText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = LayoutViewHelper.createContentView_simple(this);

        TitleUtil.showTitle(this, true);

        btn1 = new Button(this);
        layout.addView(btn1);
        btn1.setText("刷新");
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskInfo.RELOAD_APP_LIST.onOptionsItemSelected(AppListFilterActivity.this, new Intent(), new Bundle());
            }
        });

        filterText = new EditText(this);
        layout.addView(filterText);
        filterText.setMaxLines(1);
        filterText.setSingleLine();
        filterText.setHint("請按Enter鍵");
        filterText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    initListViewHandler.findByText(filterText.getText().toString());
                    return true;
                }
                return false;
            }
        });

        filterText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                initListViewHandler.findByText(filterText.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
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
                AppListService.AppInfo app = (AppListService.AppInfo) item.get("item");
                app.run(AppListFilterActivity.this);
            }
        });

        initListViewHandler = new InitListViewHandler(this);
        initListViewHandler.init(false);
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

        private List<Map<String, Object>> findAll(boolean isReload) {
            List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
            List<AppListService.AppInfo> lst = AppListService.getInstance().loadAllAppListMaster(context, isReload);
            for (AppListService.AppInfo app : lst) {
                listItem.add(getItem2Map(app));
            }
            return listItem;
        }

        public List<Map<String, Object>> _findByText(String text) {
            if (listItem.isEmpty()) {
                listItem = findAll(false);
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
                    R.layout.subview_listview_icon, //
                    new String[]{"item_title", "item_text", "item_image", "item_image_check"}, //
                    new int[]{R.id.ItemTitle, R.id.ItemText, R.id.ItemImage, R.id.ImageView01}//
            );

            SimpleAdapterDecorator.apply4Bitmap(listItemAdapter);
            return listItemAdapter;
        }

        public void init(final boolean isReload) {
            final ProgressDialog proc = new ProgressDialog(context);
            proc.setIndeterminate(true);
            proc.setMessage("loading");
            proc.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    baseAdapter = createSimpleAdapter(findAll(isReload));

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            listView.setAdapter(baseAdapter);
                        }
                    });

                    baseAdapter.notifyDataSetChanged();
                    proc.dismiss();
                }
            }).start();
        }

        public void findByText(String text) {
            text = StringUtils.trimToEmpty(text);
            Log.v(TAG, "filter text : " + text);
            baseAdapter = createSimpleAdapter(_findByText(text));
            listView.setAdapter(baseAdapter);
            baseAdapter.notifyDataSetChanged();
        }
    }

    //----------------------------------------------------------------------------------------

    static int REQUEST_CODE = 5566;
    static int MENU_FIRST = Menu.FIRST;

    /**
     * TaskInfo.XXXXXXXXXXXXXXXXXXXXX.onOptionsItemSelected(NintendoSwitchCheckerActivity.this, intent, bundle);
     */
    enum TaskInfo {
        RELOAD_APP_LIST("重新整理", MENU_FIRST++, REQUEST_CODE++, FileFindActivity.class) {
            protected void onActivityResult(AppListFilterActivity activity, Intent intent, Bundle bundle) {
            }

            protected void onOptionsItemSelected(AppListFilterActivity activity, Intent intent, Bundle bundle) {
                activity.initListViewHandler.init(true);
            }
        }, //
        ;

        final String title;
        final int option;
        final int requestCode;
        final Class<?> clz;
        final boolean debugOnly;

        TaskInfo(String title, int option, int requestCode, Class<?> clz) {
            this(title, option, requestCode, clz, false);
        }

        TaskInfo(String title, int option, int requestCode, Class<?> clz, boolean debugOnly) {
            this.title = title;
            this.option = option;
            this.requestCode = requestCode;
            this.clz = clz;
            this.debugOnly = debugOnly;
        }

        protected void onOptionsItemSelected(AppListFilterActivity activity, Intent intent, Bundle bundle) {
            intent.setClass(activity, clz);
            intent.putExtras(bundle);
            activity.startActivityForResult(intent, requestCode);
        }

        protected void onActivityResult(AppListFilterActivity activity, Intent intent, Bundle bundle) {
            android.util.Log.v(TAG, "onActivityResult TODO!! = " + this.name());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        android.util.Log.v(TAG, "# onOptionsItemSelected");
        super.onOptionsItemSelected(item);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        for (AppListFilterActivity.TaskInfo task : AppListFilterActivity.TaskInfo.values()) {
            if (item.getItemId() == task.option) {
                task.onOptionsItemSelected(this, intent, bundle);
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        android.util.Log.v(TAG, "# onActivityResult");
        Bundle bundle_ = new Bundle();
        if (data != null) {
            bundle_ = data.getExtras();
        }
        final Bundle bundle = bundle_;
        android.util.Log.v(TAG, "requestCode = " + requestCode);
        android.util.Log.v(TAG, "resultCode = " + resultCode);
        for (AppListFilterActivity.TaskInfo t : AppListFilterActivity.TaskInfo.values()) {
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
        android.util.Log.v(TAG, "# onCreateOptionsMenu");
        for (AppListFilterActivity.TaskInfo e : AppListFilterActivity.TaskInfo.values()) {

            //純測試
            if (!BuildConfig.DEBUG && e.debugOnly == true) {
                continue;
            }

            menu.add(0, e.option, 0, e.title);
        }
        return true;
    }

    public void onDestory() {
        super.onDestroy();
    }
}

