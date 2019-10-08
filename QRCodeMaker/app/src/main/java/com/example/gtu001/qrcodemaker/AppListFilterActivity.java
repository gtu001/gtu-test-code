package com.example.gtu001.qrcodemaker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.gtu001.qrcodemaker.common.AppListService;
import com.example.gtu001.qrcodemaker.common.LayoutViewHelper;
import com.example.gtu001.qrcodemaker.common.Log;
import com.example.gtu001.qrcodemaker.common.SimpleAdapterDecorator;
import com.example.gtu001.qrcodemaker.common.SingleAutoCompleteDialog;
import com.example.gtu001.qrcodemaker.common.TitleUtil;


import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import taobe.tec.jcc.JChineseConvertor;

public class AppListFilterActivity extends Activity {

    private static final String TAG = AppListFilterActivity.class.getSimpleName();


    private ListView listView;
    private BaseAdapter baseAdapter;
    private InitListViewHandler initListViewHandler;

    private Button btn1;
    private AutoCompleteTextView filterText;

    private static final int AUTO_COMPLETE_HINT_THRESHOLD = 0;

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

        filterText = new AutoCompleteTextView(this);
        layout.addView(filterText);
        filterText.setThreshold(AUTO_COMPLETE_HINT_THRESHOLD);
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
                final AppListService.AppInfo app = (AppListService.AppInfo) item.get("item");

                String[] items = new String[]{"開啟", "修改Tag"};

                new AlertDialog.Builder(AppListFilterActivity.this)//
                        .setTitle(app.getLabel())//
//                        .setMessage(app.getInstalledPackage())//
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {
                                    case 0:
                                        app.run(AppListFilterActivity.this);
                                        break;
                                    case 1:
                                        final SingleAutoCompleteDialog dialog = new SingleAutoCompleteDialog(//
                                                AppListFilterActivity.this,//
                                                app.getTag(),//
                                                AppListFilterActivity.this.initListViewHandler.tagLst,//
                                                AUTO_COMPLETE_HINT_THRESHOLD,//
                                                "修改Tag",//
                                                "修改Tag"//
                                        );
                                        dialog.confirmButton(new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                String newTag = dialog.getEditText(true, true);
                                                app.setTag(newTag);
                                                boolean updateResult = initListViewHandler.updateAppInfoTag(app);
                                                Toast.makeText(AppListFilterActivity.this, "修改" + (updateResult ? "成功" : "失敗"), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        dialog.show();
                                        break;
                                }
                            }
                        })//
                        .show();
            }
        });

        initListViewHandler = new InitListViewHandler(this);
        initListViewHandler.init(false);
    }

    private class InitListViewHandler {
        List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
        List<String> tagLst = new ArrayList<String>();

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
            map.put("item_text_desc", StringUtils.trimToEmpty(item.getTag()));
            map.put("item", item);
            return map;
        }

        private List<Map<String, Object>> findAll(boolean isReload) {
            List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
            AppListService.DataWrapper wrapper = AppListService.getInstance().loadAllAppListMaster(context, isReload);
            tagLst = wrapper.getTagLst();
            List<AppListService.AppInfo> lst = wrapper.getAppLst();
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

                boolean matchOk = false;

                if (StringUtils.trimToEmpty(s2t(app.getLabel())).toLowerCase().contains(text) || //
//                        StringUtils.trimToEmpty(app.getInstalledPackage()).toLowerCase().contains(text) ||
                        StringUtils.trimToEmpty(app.getTag()).toLowerCase().contains(text)) {
                    matchOk = true;
                }

                if (matchOk) {
                    listItem22.add(map);
                }
            }
            return listItem22;
        }

        private String s2t(String strVal) {
            try {
                return JChineseConvertor.getInstance().s2t(strVal);
            } catch (Exception ex) {
                return strVal;
            }
        }

        private SimpleAdapter createSimpleAdapter(List<Map<String, Object>> listItem) {
            SimpleAdapter listItemAdapter = new SimpleAdapter(AppListFilterActivity.this, listItem,// 資料來源
                    R.layout.subview_listview_icon, //
                    new String[]{"item_title", "item_text", "item_image", "item_image_check", "item_text_desc"}, //
                    new int[]{R.id.ItemTitle, R.id.ItemText, R.id.ItemImage, R.id.ImageView01, R.id.ItemTextDesc}//
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
                            filterText.setAdapter(getTagLstAdapter());
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

        public boolean updateAppInfoTag(AppListService.AppInfo vo) {
            boolean result = AppListService.getInstance().updateAppInfoTag(vo, context);
            if (result) {
                for (Map<String, Object> map : listItem) {
                    AppListService.AppInfo vo2 = (AppListService.AppInfo) map.get("item");
                    if (vo2 == vo) {
                        map.put("item_text_desc", StringUtils.trimToEmpty(vo2.getTag()));
                    }
                }
                baseAdapter.notifyDataSetChanged();
            }
            return result;
        }

        public ArrayAdapter<String> getTagLstAdapter() {
            return new ArrayAdapter<String>(context,
                    android.R.layout.simple_dropdown_item_1line, tagLst);
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



//import com.pchmn.materialchips.ChipsInput;
//import com.pchmn.materialchips.model.ChipInterface;
//
//        chipsInput = new ChipsInput(this);
//        layout.addView(chipsInput, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT));
//        List<ChipObj> contactList = new ArrayList<>();
//        contactList.add(new ChipObj("aaaa"));
//        contactList.add(new ChipObj("bbbb"));
//        contactList.add(new ChipObj("cccc"));
//        chipsInput.setFilterableList(contactList);
//        chipsInput.addChipsListener(new ChipsInput.ChipsListener() {
//            @Override
//            public void onChipAdded(ChipInterface chip, int newSize) {
//                // chip added
//                List<ChipObj> contactsSelected = (List<ChipObj>) chipsInput.getSelectedChipList();
//                Toast.makeText(AppListFilterActivity.this, contactsSelected.toString(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onChipRemoved(ChipInterface chip, int newSize) {
//                // chip removed
//                List<ChipObj> contactsSelected = (List<ChipObj>) chipsInput.getSelectedChipList();
//                Toast.makeText(AppListFilterActivity.this, contactsSelected.toString(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onTextChanged(CharSequence text) {
//                // text changed
//                List<ChipObj> contactsSelected = (List<ChipObj>) chipsInput.getSelectedChipList();
//                Toast.makeText(AppListFilterActivity.this, contactsSelected.toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
