package com.example.gtu001.qrcodemaker;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gtu001.qrcodemaker.common.LayoutViewHelper;
import com.example.gtu001.qrcodemaker.common.PermissionUtil;
import com.example.gtu001.qrcodemaker.common.ProcessHandler;
import com.example.gtu001.qrcodemaker.custom_dialog.UrlPlayerDialog_bg;
import com.example.gtu001.qrcodemaker.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HtmlLoaderActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = HtmlLoaderActivity.class.getSimpleName();

    private static final String FILE_TYPE_PTN = "(htm|html)";
    private static final String[] EXTENSION_DIR = new String[]{"/storage/1D0E-2671/Android/data/com.ghisler.android.TotalCommander/My Documents/"};

    private WebView webView;

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
                HtmlLoaderActivity.TaskInfo.LOAD_HTML_FROM_FILE.onOptionsItemSelected(HtmlLoaderActivity.this, intent, bundle);
            }
        });

        this.initWebView(layout);
    }


    /**
     * 初始化WebView
     */
    private void initWebView(LinearLayout contentView) {
        webView = new WebView(this);
        contentView.addView(webView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        webView.getSettings().setJavaScriptEnabled(true);
        // webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        Log.v(TAG, "default Agent : " + System.getProperty("http.agent"));
        webView.getSettings().setUserAgentString("Mozilla/5.0 (iPhone; CPU iPhone OS 8_2 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) Version/8.0 Mobile/12D508");
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


    private void loadHtmlToWebView(File file) {
        try {
            String htmlContent = FileUtil.loadFileToString(file);
            webView.loadData(htmlContent, "text/html", "UTF8");
        } catch (Exception e) {
            Log.e(TAG, "loadHtmlToWebView ERR : " + e.getMessage(), e);
            Toast.makeText(this, "loadHtmlToWebView ERR : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    static int REQUEST_CODE = 5566;
    static int MENU_FIRST = Menu.FIRST;

    enum TaskInfo {
        LOAD_HTML_FROM_FILE("讀取檔案", MENU_FIRST++, REQUEST_CODE++, FileFindActivity.class) {
            protected void onActivityResult(HtmlLoaderActivity activity, Intent intent, Bundle bundle) {
                File file = FileFindActivity.FileFindActivityStarter.getFile(intent);
                activity.loadHtmlToWebView(file);
            }

            protected void onOptionsItemSelected(HtmlLoaderActivity activity, Intent intent, Bundle bundle) {
                bundle.putString(FileFindActivity.FILE_PATTERN_KEY, FILE_TYPE_PTN);
                if (BuildConfig.DEBUG) {
                    bundle.putStringArray(FileFindActivity.FILE_START_DIRS, EXTENSION_DIR);
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

        protected void onOptionsItemSelected(HtmlLoaderActivity activity, Intent intent, Bundle bundle) {
            intent.setClass(activity, clz);
            intent.putExtras(bundle);
            activity.startActivityForResult(intent, requestCode);
        }

        protected void onActivityResult(HtmlLoaderActivity activity, Intent intent, Bundle bundle) {
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