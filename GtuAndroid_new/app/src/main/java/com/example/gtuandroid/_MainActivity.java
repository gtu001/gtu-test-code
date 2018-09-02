package com.example.gtuandroid;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.example.gtuandroid.drawerfragment.DrawerMainActivity;
import com.example.gtuandroid.listfragmgnet.ItemListActivity;
import com.example.gtuandroid.makemedia.MakeMedias;
import com.example.gtuandroid.mediaplayer.MainTabMenu;
import com.example.gtuandroid.video.VideoComponent;

public class _MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout._activity_main);

        Drawable drawable = getResources().getDrawable(R.drawable.whiteZ);
        RelativeLayout relativeLayout1 = (RelativeLayout) findViewById(R.id.relativeLayout1);
        relativeLayout1.setBackground(drawable);

        initTask();

        // 取得螢幕翻轉前的狀態 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
        final _MainActivity data = (_MainActivity) getLastNonConfigurationInstance();
        if (data != null) {// 表示不是由於Configuration改變觸發的onCreate()
            Log.v("tag", "load old status!");
            copyLastNonConfigurationInstance(this, data);
        }
        // 取得螢幕翻轉前的狀態 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    }

    void initTask() {
        final MyTask[] myTasks = new MyTask[]{ //
                new MyTask("測試用...", TestActivity.class),//
                new MyTask("傳統文字編輯", EditActivity.class),//
                new MyTask("計時器", ChronometerActivity.class),//
                new MyTask("Dialog", DialogActivity.class),//
                new MyTask("密碼顯示與隱藏", PasswordActivity.class),//
                new MyTask("存到記憶卡", MoveToSDActivity.class),//
                new MyTask("Toast", ToastActivity.class),//
                new MyTask("radio group", RadioActivity.class),//
                new MyTask("圖片顯示", ImageViewActivity.class),//
                new MyTask("下拉選單", SpinnerActivity.class),//
                new MyTask("autoComplete", AutoCompleteActivity.class),//
                new MyTask("時鐘", AnalogClockActivity.class),//
                new MyTask("日期時間picker", DateTimePickerActivity.class),//
                new MyTask("日期時間dialog", DateTimeDialogActivity.class),//
                new MyTask("處理中控制項", ProgressBarActivity.class),//
                new MyTask("Grid表格", GridViewActivity.class),//
                new MyTask("清單物件", ListViewActivity.class),//
                new MyTask("清單功能表", ListActivityActivity.class),//
                new MyTask("檔案瀏覽", FileDirectoryActivity.class),//
                new MyTask("儲存空間圖片瀏覽", BitmapActivity.class),//
                new MyTask("功能表", OptionsMenuActivity.class),//
                new MyTask("功能表V2", OptionsMenu2Activity.class),//
                new MyTask("影片", VideoActivity.class),//
                new MyTask("畫廊", GalleryActivity.class),//
                new MyTask("RatingBar 與 SeekBar", RatingSeekBarActivity.class),//
                new MyTask("資料存取", SharedPreferencesActivity.class),//
                new MyTask("SQLite", SQLiteTestActivity.class),//
                new MyTask("actionBar左上角按鈕", ActionBarActivity.class),//
                new MyTask("自訂細節的ListView", ListViewActivity2.class),//
                new MyTask("自訂GridView", GridViewActivity2.class),//
                new MyTask("測試wifi用", WifiTestActivity.class),//
                new MyTask("碎片固定", FragmentTest0Activity.class),//
                new MyTask("碎片動態", FragmentDynamicActivity.class),//
                new MyTask("碎片測試", FragmentTestActivity.class),//
                new MyTask("碎片PagerV4", FragmentPagerV4TestActivity.class),//
                new MyTask("事件bus", EventBusActivity.class),//
                new MyTask("抓取系統照片", SystemPicPathActivity.class),//
                new MyTask("文字設定超連結", HyperLinkTextActivity.class),//
                new MyTask("文字設定超連結2", LinkifyActivity.class),//
                new MyTask("剪貼簿", ClipboardActivity.class),//
                new MyTask("記住點選位置ListView", RememberPosListViewActivity.class),//
                new MyTask("載入logo", SplashScreenActivity.class),//
                new MyTask("共用application", MyAppTest1Activity.class),//
                new MyTask("ListView與SQLite", ListViewSQLiteActivity.class),//
                new MyTask("Loop List未成功", ListViewLoopActivity.class),//
                new MyTask("List載入Bmp", ListViewBmpActivity.class),//
                new MyTask("List載入Bmp V2", ListViewBmpV2Activity.class),//
                new MyTask("更新UI", HandlerTestActivity.class),//
                new MyTask("收到簡訊立即擷取內容(廣播)", SMSBroadcastReceiverActivity.class),//
                new MyTask("廣播測試", BroadcastReceiverActivity.class),//
                new MyTask("電池狀態(廣播)", GetBatteryInfoActivity.class),//
                new MyTask("倒數計時器", CountdownTimerActivity.class),//
                new MyTask("發送簡訊", SMSSenderActivity.class),//
                new MyTask("發送通知", NotificationTestActivity.class),//
                new MyTask("Tab分頁", FragmentTabsActivity.class),//
                new MyTask("服務", ServiceTestActivity.class),//
                new MyTask("服務2", ServiceTest2Activity.class),//
                new MyTask("服務IntentService", ServiceTest_IntentService_Activity.class),//
                new MyTask("可延展ExpandableListView", ExpandableListViewActivity.class),//
                new MyTask("AsyncTask測試", AsyncTaskTestActivity.class),//
                new MyTask("AsyncTask測試2(改字型)", AsyncTaskTest2Activity.class),//
                new MyTask("取得Server資訊", GetServerMessageActivity.class),//
                new MyTask("電話簿", PhoneBookActivity.class),//
                new MyTask("電話簿(聯絡人照片)", PhoneBookV2Activity.class),//
                new MyTask("讀取簡訊內容", SMSReaderActivity.class),//
                new MyTask("自訂Provider", MyProviderTestActivity.class),//
                new MyTask("自行繪製元件", ImageMyDrawViewActivity.class),//
                new MyTask("自行繪製元件移動V1", ImageMoveV1Activity.class),//
                new MyTask("自行繪製元件移動V2", ImageMoveV2Activity.class),//
                new MyTask("自行繪製元件移動V3", ImageMoveV3Activity.class),//
                new MyTask("影片撥放器", MainTabMenu.class),//
                new MyTask("檔案路徑", FilePathActivity.class),//
                new MyTask("相機", VideoComponent.class),//
                new MyTask("影音", MakeMedias.class),//
                new MyTask("內嵌網頁", WebviewActivity.class),//
                new MyTask("寄郵件Intent", SendEmailIntentActivity.class),//
                new MyTask("簡易媒體撥放", MediaTestActivity.class),//
                new MyTask("簡易相機測試", CameraTestActivity.class),//
                new MyTask("簡易相機測試(內嵌)", CameraTest2Activity.class),//
                new MyTask("懸浮視窗(Compat)", FloatViewActivity.class),//
                new MyTask("懸浮視窗(Local)", FloatView2Activity.class),//
                new MyTask("懸浮視窗(Image)", FloatView3Activity.class),//
                new MyTask("感測器(加速器)", SensorTestActivity.class),//
                new MyTask("感測器(方向)", SensorTest2Activity.class),//
                new MyTask("感測器(指南針)", SensorTest3Activity.class),//
                new MyTask("感測器(不倒翁)", SensorBudaowActivity.class),//
                new MyTask("ViewGroup測試", ViewGroupTestActivity.class),//
                new MyTask("TreeView(自訂)", TreeViewActivity.class),//
                new MyTask("進度按鈕", AnimDownloadProgressButtonActivity.class),//
                new MyTask("檔案存取", FileDataStoreActivity.class),//
                new MyTask("Xml讀取", XmlParserActivity.class),//
                new MyTask("Json讀取", JsonParserActivity.class),//
                new MyTask("閱讀器", BookReaderActivity.class),//
                new MyTask("ListFragment", ItemListActivity.class),//
                new MyTask("切換動畫", OverridePendingTransitionActivity.class),//
                new MyTask("相簿", CameraPhotoActivity.class),//
                new MyTask("監控相機", CameraReceiverActivity.class),//
                new MyTask("GoogleClient測試 TODO", GoogleApiTestActivity.class),//
                new MyTask("Google AdMob Banner", GoogleAdMobTestActivity.class),//
                new MyTask("Drawer Fragment", DrawerMainActivity.class),//
                new MyTask("放大縮小", ZoomControlActivity.class),//
                new MyTask("Tab用ActionBar", TabFragmentActivity.class),//
                new MyTask("建立設定畫面", PrefereceCategoryTestActivity.class),//
                new MyTask("導覽選單", DrawerV4TestActivity.class),//
                new MyTask("DialogFragment", FragmentDialogActivity.class),//
                new MyTask("Fragment", FragmentTest2Activity.class),//
                new MyTask("瀏覽器歷史紀錄", LoaderTestActivity.class),//
                new MyTask("監聽點擊/雙點擊,捲動,長點擊(不work)", TapGestureDetectActivity.class),//
                new MyTask("圖片任意範圍的點擊", ImageDetectTapActivity.class),//
                new MyTask("監聽按鍵", CommonKeyListenerActivity.class),//
                new MyTask("使用Tween動畫", TweenAnimationTestActivity.class),//
                new MyTask("底部導覽按鈕", BottomNavigationActivity.class),//
                new MyTask("搜尋bar清單", SearchViewActivity.class),//
                new MyTask("搜尋bar清單[Menu]", SearchViewActivity2.class),//
        };

        // ArrayAdapter<MyTask> adapter = new ArrayAdapter<MyTask>(this,
        // android.R.layout.simple_list_item_1, myTasks);
        List<Map<String, String>> taskList = new ArrayList<Map<String, String>>();
        for (MyTask t : myTasks) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("name", t.name);
            map.put("clz", t.clz.getSimpleName());
            taskList.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this,//
                taskList,//
                android.R.layout.simple_list_item_2,//
                new String[]{"name", "clz"}, //
                new int[]{android.R.id.text1, android.R.id.text2});//

        ListView listView = (ListView) findViewById(R.id.listView1);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                // 訪問外部activity的寫法 (packagename , packagename+classname)
                // intent.setClassName("com.example.gtutestandroid",
                // "com.example.gtutestandroid.MoveToSDActivity");
                intent.setClass(_MainActivity.this, myTasks[paramInt].clz);
                intent.putExtras(bundle);
                startActivityForResult(intent, myTasks[paramInt].requestCode);
            }
        });
    }

    static class MyTask {
        String name;
        Class<?> clz;
        int requestCode;
        static int REQUEST_CODE_INDEX = 100;

        public MyTask(String name, Class<?> clz) {
            super();
            this.name = name;
            this.clz = clz;
            REQUEST_CODE_INDEX++;
            this.requestCode = REQUEST_CODE_INDEX;
        }

        @Override
        public String toString() {
            return name + " - " + clz.getSimpleName();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("# onActivityResult");
        Bundle bundle = new Bundle();
        if (data != null) {
            bundle = data.getExtras();
        }
        System.out.println("requestCode = " + requestCode);
        System.out.println("resultCode = " + resultCode);
        switch (resultCode) {
            case RESULT_CANCELED:
                System.out.println("RESULT_CANCELED");
                break;
            case RESULT_FIRST_USER:
                System.out.println("RESULT_FIRST_USER");
                break;
            case RESULT_OK:
                System.out.println("RESULT_OK");
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        System.out.println("# onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    // 測試螢幕翻轉 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.v("ola_log", "# onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.v("ola_log", "landscape");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.v("ola_log", "portrait");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy(); // Always call the superclass
        // Stop method tracing that the activity started during onCreate()
        android.os.Debug.stopMethodTracing();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.v("ola_log", "onRestoreInstanceState");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.v("ola_log", "onSaveInstanceState");
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        Log.v("ola_log", "onRetainNonConfigurationInstance");
        return this;
    }

    void copyLastNonConfigurationInstance(_MainActivity current, _MainActivity lastOne) {
        for (Field f : _MainActivity.class.getDeclaredFields()) {
            try {
                boolean access = f.isAccessible();
                f.setAccessible(true);
                Object val = f.get(lastOne);
                if (val != null && val.getClass().getName().startsWith("android.widget")) {
                    continue;
                }
                f.set(current, val);
                f.setAccessible(access);
            } catch (Exception e) {
                e.printStackTrace();
                Log.v("tag", e.getMessage());
            }
        }
    }
    // 測試螢幕翻轉 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
}
