package com.example.gtu001.qrcodemaker;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.gtu001.qrcodemaker.common.PermissionUtil;
import com.example.gtu001.qrcodemaker.common.ProcessHandler;
import com.example.gtu001.qrcodemaker.common.SharedPreferencesUtil;
import com.example.gtu001.qrcodemaker.common.mp3.Mp3InitListViewHandler;
import com.example.gtu001.qrcodemaker.custom_dialog.UrlPlayerDialog_bg;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mp3PlayerActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = Mp3PlayerActivity.class.getSimpleName();

    private static final String FILE_TYPE_PTN = "(avi|rmvb|rm|mp4|mp3|m4a|flv|3gp|flac|webm|wmv|mkv|m4s)";
    private static final String[] EXTENSION_DIR = new String[]{"/storage/1D0E-2671/Android/data/com.ghisler.android.TotalCommander/My Documents/"};

    private ListView listView;
    private Mp3InitListViewHandler initListViewHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = LayoutViewHelper.createContentView_simple(this);

        //初始Btn狀態紐
        Button btn1 = new Button(this);
        btn1.setText("選擇檔案");
        layout.addView(btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                TaskInfo.LOAD_CONTENT_FROM_FILE_RANDOM.onOptionsItemSelected(Mp3PlayerActivity.this, intent, bundle);
            }
        });

        //初始Btn狀態紐
        Button btn3 = new Button(this);
        btn3.setText("選擇目錄");
        layout.addView(btn3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                TaskInfo.LOAD_CONTENT_FROM_DIR.onOptionsItemSelected(Mp3PlayerActivity.this, intent, bundle);
            }
        });

        //初始Btn狀態紐
        Button btn2 = new Button(this);
        btn2.setText("開啟現在播放");
        layout.addView(btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mp3Bean mp3Bean = initListViewHandler.getCurrentPlayBean(Mp3PlayerActivity.this);

                com.example.gtu001.qrcodemaker.common.Log.v(TAG, "====================================================================");
                com.example.gtu001.qrcodemaker.common.Log.v(TAG, "name = " + (mp3Bean != null ? mp3Bean.name : "null"));
                com.example.gtu001.qrcodemaker.common.Log.v(TAG, "videoUrl = " + (mp3Bean != null ? mp3Bean.url : "null"));
                com.example.gtu001.qrcodemaker.common.Log.v(TAG, "====================================================================");

                initListViewHandler.restoreTotalUrlList(Mp3PlayerActivity.this);
                UrlPlayerDialog_bg dialog = new UrlPlayerDialog_bg(Mp3PlayerActivity.this);
                Dialog _dialog = dialog.setUrl(null, mp3Bean, initListViewHandler.getTotalUrlList()).build();
                _dialog.show();
            }
        });

        //初始Btn狀態紐
        Button btn5 = new Button(this);
        btn5.setText("停掉APP");
        layout.addView(btn5);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProcessHandler.killProcessByPackage2(Mp3PlayerActivity.this, "com.example.gtu001.qrcodemaker", false);
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
                final Mp3InitListViewHandler.FileItem item2 = (Mp3InitListViewHandler.FileItem) item.get("item");
                UrlPlayerDialog_bg dialog = new UrlPlayerDialog_bg(Mp3PlayerActivity.this);

                com.example.gtu001.qrcodemaker.common.Log.v(TAG, "====================================================================");
                com.example.gtu001.qrcodemaker.common.Log.v(TAG, "name = " + item2.name);
                com.example.gtu001.qrcodemaker.common.Log.v(TAG, "videoUrl = " + item2.videoUrl);
                com.example.gtu001.qrcodemaker.common.Log.v(TAG, "====================================================================");

                Mp3Bean bean = new Mp3Bean();
                bean.setName(item2.name);
                bean.setUrl(item2.videoUrl);

                Dialog _dialog = dialog.setUrl(null, bean, initListViewHandler.getTotalUrlList()).build();
                initListViewHandler.saveTotalUrlList(Mp3PlayerActivity.this);
                _dialog.show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Map<String, Object> item = (Map<String, Object>) listView.getAdapter().getItem(position);
                final Mp3InitListViewHandler.FileItem item2 = (Mp3InitListViewHandler.FileItem) item.get("item");

                String[] items = new String[]{"刷新", "刪除"};

                AlertDialog dlg = new AlertDialog.Builder(Mp3PlayerActivity.this)//
                        .setTitle("選擇操作項目")//
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        break;
                                    case 1:
                                        initListViewHandler.delete(item2);
                                        break;
                                    default:
                                        Toast.makeText(Mp3PlayerActivity.this, "Unknow choice " + which, Toast.LENGTH_SHORT).show();
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

        PermissionUtil.verifyPermissions(this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.PROCESS_OUTGOING_CALLS,
        }, 9999);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        final int REQUEST_READ_PHONE_STATE = 9999;
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                }
                break;
            default:
                break;
        }
    }

    private void initServices() {
        initListViewHandler = new Mp3InitListViewHandler(listView, this);
        initListViewHandler.initListView();
    }




    static int REQUEST_CODE = 5566;
    static int MENU_FIRST = Menu.FIRST;

    enum TaskInfo {
        LOAD_CONTENT_FROM_FILE_RANDOM("讀取檔案", MENU_FIRST++, REQUEST_CODE++, FileFindActivity.class) {
            protected void onActivityResult(Mp3PlayerActivity activity, Intent intent, Bundle bundle) {
                File file = FileFindActivity.FileFindActivityStarter.getFile(intent);
                activity.initListViewHandler.add(file);
            }

            protected void onOptionsItemSelected(Mp3PlayerActivity activity, Intent intent, Bundle bundle) {
                bundle.putString(FileFindActivity.FILE_PATTERN_KEY, FILE_TYPE_PTN);
                if (BuildConfig.DEBUG) {
                    bundle.putStringArray(FileFindActivity.FILE_START_DIRS, EXTENSION_DIR);
                }
                super.onOptionsItemSelected(activity, intent, bundle);
            }
        }, //
        LOAD_CONTENT_FROM_DIR("讀取目錄", MENU_FIRST++, REQUEST_CODE++, FileFindMultiActivity.class) {
            protected void onActivityResult(Mp3PlayerActivity activity, Intent intent, Bundle bundle) {
                File file = FileFindMultiActivity.FileFindActivityStarter.getFile(intent);
                if (file != null) {
                    activity.initListViewHandler.add(file);
                } else {
                    List<File> fileLst = FileFindMultiActivity.FileFindActivityStarter.getFiles(intent);
                    for (File f : fileLst) {
                        if (f.isDirectory()) {
                            if (f.listFiles() != null) {
                                for (File subFile : f.listFiles()) {
                                    activity.initListViewHandler.add(subFile);
                                }
                            }
                        } else {
                            activity.initListViewHandler.add(f);
                        }
                    }
                }
            }

            protected void onOptionsItemSelected(Mp3PlayerActivity activity, Intent intent, Bundle bundle) {
                bundle.putString(FileFindMultiActivity.FILE_PATTERN_KEY, FILE_TYPE_PTN);
                if (BuildConfig.DEBUG) {
                    bundle.putStringArray(FileFindMultiActivity.FILE_START_DIRS, EXTENSION_DIR);
                }
                super.onOptionsItemSelected(activity, intent, bundle);
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

        protected void onOptionsItemSelected(Mp3PlayerActivity activity, Intent intent, Bundle bundle) {
            intent.setClass(activity, clz);
            intent.putExtras(bundle);
            activity.startActivityForResult(intent, requestCode);
        }

        protected void onActivityResult(Mp3PlayerActivity activity, Intent intent, Bundle bundle) {
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