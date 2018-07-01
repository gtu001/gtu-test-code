package com.example.englishtester;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.englishtester.memory.IHermannEbbinghausMemoryAidlInterface;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

@TargetApi(11)
@SuppressLint("NewApi")
public class StatusInfoActivity extends Activity {

    private static final String TAG = StatusInfoActivity.class.getSimpleName();

    StatusInfoService statusInfoService;
    String statusInfo;

    WebView webView;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        System.out.println("# onCreate");

        super.onCreate(savedInstanceState);

        LinearLayout layout = createContentView();

        statusInfoService = new StatusInfoService(this);

        webView = new WebView(this);
        WebSettings settings = webView.getSettings();
        settings.setDefaultTextEncodingName("utf-8");

        layout.addView(webView);

        // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 取得螢幕翻轉前的狀態
        final StatusInfoActivity data = (StatusInfoActivity) getLastNonConfigurationInstance();
        if (data != null) {// 表示不是由於Configuration改變觸發的onCreate()
            Log.v(TAG, "load old status!");
            statusInfo = data.statusInfo;
            webView.loadData(statusInfo, "text/html; charset=utf-8", "utf-8");
        } else {
            //正常執行要做的
            final ProgressDialog myDialog = ProgressDialog.show(this, "系統狀態", "初始化...", true);
            final Handler handler = new Handler();
            ;
            Thread thread = new Thread(Thread.currentThread().getThreadGroup(), new Runnable() {
                @Override
                public void run() {
                    final String htmlData = getHtmlData();
                    statusInfo = htmlData;

                    myDialog.cancel();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            webView.loadData(htmlData, "text/html; charset=utf-8", "utf-8");
                        }
                    });
                }
            }, "statusInfoReading...");
            thread.start();
        }
    }

    private String getTableCss() {
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        sb.append("<style type=\"text/css\">");
        try {
            reader = new BufferedReader(new InputStreamReader(this.getAssets().open("css/responstable.css")));
            for (String line = null; (line = reader.readLine()) != null; ) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
            sb.append("</style>");
            return sb.toString();
        }
    }

    private String getHtmlData() {
        StringBuffer sb = new StringBuffer();
        sb.append("<html>");
        sb.append("<head>");
        sb.append(getTableCss());
        sb.append("</head>");
        sb.append("<body>");
        sb.append("<table class='responstable'>");
        Class<StatusInfoService.StatusInfoService_Data> clz = StatusInfoService.StatusInfoService_Data.class;
        StatusInfoService.StatusInfoService_Data data = statusInfoService.getStatusInfo();

        Map<Integer, Pair<String, String>> tableData = new TreeMap<>();
//        for (Field f2 : FieldUtils.getAllFields(clz)) {
        for (Field f2 : clz.getDeclaredFields()) {
            try {
                String name = "";
                Integer index = -1;
                try {
                    StatusInfoService.StatusInfoService_DataAnn anno = f2.getAnnotation(StatusInfoService.StatusInfoService_DataAnn.class);
                    name = anno.name();
                    index = anno.index();
                } catch (Exception ex) {
                    Log.v(TAG, f2 + " - " + f2.getName() + " - " + ex.getMessage(), ex);
                    continue;
                }
//                String val = (String) FieldUtils.readDeclaredField(data, f2.getName(), true);
                f2.setAccessible(true);
                String val = (String) f2.get(data);
                tableData.put(index, ImmutablePair.of(name, val));
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }

        for (int index : tableData.keySet()) {
            Pair<String, String> pair = tableData.get(index);
            String name = pair.getKey();
            String val = pair.getValue();
            sb.append(String.format("<tr><td>%s</td><td>%s</td></tr>", name, val));
        }

        sb.append("</table>");
        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }

    // 測試螢幕翻轉 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.v(TAG, "# onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.v(TAG, "landscape");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.v(TAG, "portrait");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.v(TAG, "onRestoreInstanceState");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.v(TAG, "onSaveInstanceState");
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        Log.v(TAG, "onRetainNonConfigurationInstance");
        return this;
    }
    // 測試螢幕翻轉 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

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
