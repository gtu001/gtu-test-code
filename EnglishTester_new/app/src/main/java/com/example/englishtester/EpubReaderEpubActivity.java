package com.example.englishtester;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.englishtester.common.ClickableSpanMethodCreater;
import com.example.englishtester.common.DialogFontSizeChange;
import com.example.englishtester.common.FloatViewChecker;
import com.example.englishtester.common.HomeKeyWatcher;
import com.example.englishtester.common.IFloatServiceAidlInterface;
import com.example.englishtester.common.ITxtReaderActivity;
import com.example.englishtester.common.TitleTextSetter;
import com.example.englishtester.common.TxtReaderAppender;
import com.example.epub.com.example.epub.base.EpubViewerMainHandler;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

public class EpubReaderEpubActivity extends Activity implements FloatViewService.Callbacks, ITxtReaderActivity, EpubViewerMainHandler.EpubActivityInterface {

    private static final String TAG = EpubReaderEpubActivity.class.getSimpleName();

    public static final String KEY_CONTENT = "EpubReaderActivity_content";

    private static final Class[] CLICKABLE_SPAN_IMPL_CLZ = new Class[]{//
            TxtReaderAppender.WordSpan.class, //
            TxtReaderAppender.SimpleUrlLinkSpan.class//
    };//

    EpubReaderEpubActivity self = EpubReaderEpubActivity.this;

    /**
     * 綁定服務器
     */
//    FloatViewService myService;
    IFloatServiceAidlInterface mService;

    /**
     * Home 鍵觀察者
     */
    HomeKeyWatcher homeKeyWatcher;

    TxtReaderActivity.PaddingAdjuster paddingAdjuster;

    /**
     * epub 服務
     */
    EpubViewerMainHandler epubViewerMainHandler;

    /**
     * 最近查詢單字
     */
    RecentTxtMarkService recentTxtMarkService;

    /**
     * 蘋果字型
     */
    TxtReaderActivity.AppleFontApplyer appleFontApplyer;

    TextView txtReaderView;
    TextView translateView;
    Button previousPageBtn;
    Button nextPageBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);

        //contentView = createContentView();
        setContentView(R.layout.activity_epub_reader);

        txtReaderView = findViewById(R.id.txtReaderView);
        translateView = findViewById(R.id.translateView);

        previousPageBtn = findViewById(R.id.previousPageBtn);
        nextPageBtn = findViewById(R.id.nextPageBtn);

        this.initView();
        this.initServices();
        this.initViewAfterService();


        // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 取得螢幕翻轉前的狀態
        final EpubReaderEpubActivity data = (EpubReaderEpubActivity) getLastNonConfigurationInstance();
        if (data != null) {// 表示不是由於Configuration改變觸發的onCreate()
            Log.v(TAG, "load old status!");
//            this.dto = data.dto;
        } else {
            // 正常執行要做的
            Log.v(TAG, "### initial ###");
            try {
                if (getIntent().getExtras().containsKey(KEY_CONTENT)) {
                    String content = getIntent().getExtras().getString(KEY_CONTENT);
//                    this.pasteFromOutsideLoad(content); //TODO
                }
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage(), ex);
            }
        }
    }

    private void initView() {
        previousPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                epubViewerMainHandler.gotoPreviousSpineSection();
            }
        });
        nextPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                epubViewerMainHandler.gotoNextSpineSection();
            }
        });
    }

    private void initViewAfterService() {
        float fontsize = new TxtReaderActivity.FontSizeApplyer().getFontSize(this, EpubReaderEpubActivity.class);
        this.txtReaderView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontsize);
        this.txtReaderView.setHighlightColor(Color.TRANSPARENT);
        this.txtReaderView.setMovementMethod(ClickableSpanMethodCreater.createMovementMethod(this, CLICKABLE_SPAN_IMPL_CLZ));
        this.txtReaderView.setPadding(paddingAdjuster.width, paddingAdjuster.height, paddingAdjuster.width, paddingAdjuster.height);

//        参数add表示要增加的间距数值，对应android:lineSpacingExtra参数。
//        参数mult表示要增加的间距倍数，对应android:lineSpacingMultiplier参数。
        this.txtReaderView.setLineSpacing(10, 1.4f);

        this.appleFontApplyer.apply(this.txtReaderView);
        this.appleFontApplyer.apply(this.translateView);

        this.txtReaderView.setPadding(paddingAdjuster.width, paddingAdjuster.height, paddingAdjuster.width, paddingAdjuster.height);
        this.translateView.setPadding(paddingAdjuster.width, paddingAdjuster.height, paddingAdjuster.width, paddingAdjuster.height);
    }

    private void initServices() {
        epubViewerMainHandler = new EpubViewerMainHandler(txtReaderView, this);
        recentTxtMarkService = new RecentTxtMarkService(this);

        //監視home鍵
        homeKeyWatcher = new HomeKeyWatcher(this);
        homeKeyWatcher.setOnHomePressedListener(new HomeKeyWatcher.OnHomePressedListenerAdapter() {
            public void onPressed() {
            }
        });
        homeKeyWatcher.startWatch();

        this.paddingAdjuster = new TxtReaderActivity.PaddingAdjuster(this.getApplicationContext());
        this.appleFontApplyer = new TxtReaderActivity.AppleFontApplyer(this);

        this.doOnoffService(true);
    }

    /**
     * 開啟字形大小修改Dialog
     */
    private void openFontSizeDialog() {
        DialogFontSizeChange dialog = new DialogFontSizeChange(this);
        dialog.apply(txtReaderView.getTextSize(), Arrays.asList(txtReaderView, translateView), new DialogFontSizeChange.ApplyFontSize() {
            @Override
            public void applyFontSize(float fontSize) {
                new TxtReaderActivity.FontSizeApplyer().setFontSize(EpubReaderEpubActivity.this, fontSize, EpubReaderEpubActivity.class);
            }
        });
        dialog.show();
    }

    public void setTitle(String titleVal) {
        TitleTextSetter.setText(EpubReaderEpubActivity.this, titleVal);
    }

    @Override
    public IFloatServiceAidlInterface getFloatService() {
        return mService;
    }

    @Override
    public RecentTxtMarkService getRecentTxtMarkService() {
        return recentTxtMarkService;
    }

    @Override
    public int getFixScreenWidth() {
        return paddingAdjuster.width - 10;
    }

    /**
     * 從檔案設定內容
     */
    private void setTxtContentFromFile(final File epubFile, final String title, final Callable<File> txtFileGetterCall) {
        try {
            final Handler handler = new Handler();

            final AtomicReference<ProgressDialog> dialog = new AtomicReference<ProgressDialog>();
            dialog.set(new ProgressDialog(EpubReaderEpubActivity.this));
            dialog.get().setMessage("讀取中...");
            dialog.get().show();

            final AtomicReference<File> epubFileZ = new AtomicReference<>();
            epubFileZ.set(epubFile);

            new Thread(new Runnable() {

                private String fixName(String title) {
                    title = StringUtils.trimToEmpty(title);
                    Pattern ptn = Pattern.compile("(.*?)\\.(?:epub)", Pattern.CASE_INSENSITIVE);
                    Matcher mth = ptn.matcher(title);
                    if (mth.find()) {
                        return mth.group(1);
                    }
                    return title;
                }

                private void setTitleNameProcess() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String titleVal = "";
                            if (title == null) {
                                titleVal = epubFileZ.get().getName();
                            } else {
                                titleVal = title;
                            }
                            titleVal = fixName(titleVal);
                            setTitle(titleVal);
                        }
                    });
                }

                private void epubProcess() {
                    try {
                        File file = epubFileZ.get();

                        InputStream bookStream = new FileInputStream(file);
                        Book book = (new EpubReader()).readEpub(bookStream);

                        epubViewerMainHandler.initBook(book);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                translateView.setText("");
                                dialog.get().dismiss();
                            }
                        });
                    } catch (Exception ex) {
                        Log.e(TAG, "epubProcess ERR : " + ex.getMessage(), ex);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(EpubReaderEpubActivity.this, "讀取失敗", Toast.LENGTH_SHORT).show();
                                dialog.get().dismiss();
                            }
                        });
                    }
                }

                @Override
                public void run() {
                    try {
                        if (txtFileGetterCall != null) {
                            epubFileZ.set(txtFileGetterCall.call());
                        }

                        setTitleNameProcess();

                        this.epubProcess();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    } finally {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                    }
                }
            }).start();

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 啟動關閉服務
     */
    public void doOnoffService(boolean isOn) {
        if (!FloatViewChecker.isPermissionOk(this)) {
            //申請開啟懸浮視窗權限
            FloatViewChecker.applyPermission(this, FloatViewActivity.FLOATVIEW_REQUESTCODE);
        } else {
            Intent intent = new Intent(EpubReaderEpubActivity.this, FloatViewService.class);
            if (isOn) {
                startService(intent);
                bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            } else {
                stopService(intent);
                unbindService(mConnection);
            }
        }
    }

    /**
     * 設定綁定服務器連線
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.v(TAG, "onServiceConnected called");
//            FloatViewService.LocalBinder binder = (FloatViewService.LocalBinder) service;
//            myService = binder.getServiceInstance();
//            myService.registerClient(TxtReaderActivity.this);
            mService = IFloatServiceAidlInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.v(TAG, "onServiceDisconnected called");
//            myService = null;
            mService = null;
        }
    };

    @Override
    public void updateClient(long data) {
        // TODO
    }

    // --------------------------------------------------------------------

    static int REQUEST_CODE = 5566;
    static int MENU_FIRST = Menu.FIRST;

    enum TaskInfo {
        CHANGE_FONT_SIZE("改變字體大小", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(final EpubReaderEpubActivity activity, Intent intent, Bundle bundle) {
                activity.openFontSizeDialog();
            }
        }, //
        LOAD_CONTENT_FROM_FILE_RANDOM("讀取文件(epub檔)", MENU_FIRST++, REQUEST_CODE++, FileFindActivity.class) {
            protected void onActivityResult(EpubReaderEpubActivity activity, Intent intent, Bundle bundle) {
                File file = FileFindActivity.FileFindActivityStarter.getFile(intent);
                activity.setTxtContentFromFile(file, null, null);
            }

            protected void onOptionsItemSelected(EpubReaderEpubActivity activity, Intent intent, Bundle bundle) {
                bundle.putString(FileFindActivity.FILE_PATTERN_KEY, "(epub)");
                super.onOptionsItemSelected(activity, intent, bundle);
            }
        }, //
        TTTTTTTTTTTTTTTTTTTTTTTT("TEST", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(EpubReaderEpubActivity activity, Intent intent, Bundle bundle) {
                String filename = "/storage/1D0E-2671/Android/data/com.ghisler.android.TotalCommander/My Documents/books/Everybody Lies Big Data, New Data, and What the Internet - Seth Stephens-Davidowitz.epub";
                File file = new File(filename);
                activity.setTxtContentFromFile(file, null, null);
            }
        };

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

        protected void onOptionsItemSelected(EpubReaderEpubActivity activity, Intent intent, Bundle bundle) {
            intent.setClass(activity, clz);
            intent.putExtras(bundle);
            activity.startActivityForResult(intent, requestCode);
        }

        protected void onActivityResult(EpubReaderEpubActivity activity, Intent intent, Bundle bundle) {
            Log.v(TAG, "onActivityResult TODO!! = " + this.name());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(TAG, "# onOptionsItemSelected");
        super.onOptionsItemSelected(item);

        //預處理
        if (true) {
            //TODO
        }

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

        //開啟floatService
        if (requestCode == FloatViewActivity.FLOATVIEW_REQUESTCODE) {
            doOnoffService(true);
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
    @Override
    public void onResume() {
        super.onResume();
        // Resume the AdView.
    }

    @Override
    public void onPause() {
        // Pause the AdView.
        if (true) {
            //TODO
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        // Destroy the AdView.
        super.onDestroy();
    }

    // 中斷處理 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public void finish() {
        if (true) {
            //TODO
        }
        homeKeyWatcher.stopWatch();
        super.finish();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_POWER) {
            if (true) {
                //TODO
            }
        }
        return super.dispatchKeyEvent(event);
    }
    // 中斷處理 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
}
