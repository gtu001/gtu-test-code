package com.example.englishtester;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.englishtester.common.ClickableSpanMethodCreater;
import com.example.englishtester.common.DialogFontSizeChange;
import com.example.englishtester.common.FloatViewChecker;
import com.example.englishtester.common.FragmentUtil;
import com.example.englishtester.common.HomeKeyWatcher;
import com.example.englishtester.common.IFloatServiceAidlInterface;
import com.example.englishtester.common.ITxtReaderActivity;
import com.example.englishtester.common.ReaderCommonHelper;
import com.example.englishtester.common.TextView4SpannableString;
import com.example.englishtester.common.TitleTextSetter;
import com.example.englishtester.common.TxtReaderAppender;
import com.example.englishtester.common.ViewGroupHelper;
import com.example.englishtester.common.ViewPagerHelper;
import com.example.englishtester.common.epub.base.EpubViewerMainHandler;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EpubReaderEpubActivity extends FragmentActivity implements FloatViewService.Callbacks, ITxtReaderActivity, EpubViewerMainHandler.EpubActivityInterface, EpubViewerMainHandler.EpubChangePageEvent {

    private static final String TAG = EpubReaderEpubActivity.class.getSimpleName();

    public static final String KEY_CONTENT = "EpubReaderActivity_content";

    private static final Class[] CLICKABLE_SPAN_IMPL_CLZ = new Class[]{//
            TxtReaderAppender.WordSpan.class, //
            TxtReaderAppender.SimpleUrlLinkSpan.class//
    };//

    /**
     * 綁定服務器
     */
//    FloatViewService myService;
    IFloatServiceAidlInterface mService;
    HomeKeyWatcher homeKeyWatcher;
    ReaderCommonHelper.PaddingAdjuster paddingAdjuster;
    EpubViewerMainHandler epubViewerMainHandler;
    RecentTxtMarkService recentTxtMarkService;
    ReaderCommonHelper.AppleFontApplyer appleFontApplyer;
    ReaderCommonHelper.ScrollViewYHolder scrollViewYHolder;

    TextView txtReaderView;
    TextView translateView;
    ScrollView scrollView1;
    ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);

        //contentView = createContentView();
        setContentView(R.layout.activity_epub_reader);

        viewPager = findViewById(R.id.viewpager);

        this.initViewPager();
        this.initServices();
        this.initTextViewAfterService();


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

    private void initTextViewAfterService() {
        if (txtReaderView == null || translateView == null) {
            Log.w(TAG, "initTextViewAfterService WARN 尚未初始化!!!");
            return;
        }

        float fontsize = new ReaderCommonHelper.FontSizeApplyer().getFontSize(this, EpubReaderEpubActivity.class);

        this.txtReaderView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontsize);
        this.translateView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontsize);

        this.txtReaderView.setHighlightColor(Color.TRANSPARENT);
        this.txtReaderView.setMovementMethod(ClickableSpanMethodCreater.createMovementMethod(this, CLICKABLE_SPAN_IMPL_CLZ));

        paddingAdjuster.applyPadding(this.txtReaderView);

//        参数add表示要增加的间距数值，对应android:lineSpacingExtra参数。
//        参数mult表示要增加的间距倍数，对应android:lineSpacingMultiplier参数。
        this.txtReaderView.setLineSpacing(10, 1.4f);
        this.translateView.setLineSpacing(10, 1.4f);

        this.appleFontApplyer.apply(this.txtReaderView);
        this.appleFontApplyer.apply(this.translateView);

        paddingAdjuster.applyPadding(this.translateView);
    }

    private void initServices() {
        epubViewerMainHandler = new EpubViewerMainHandler(this, this);
        recentTxtMarkService = new RecentTxtMarkService(this);

        //監視home鍵
        homeKeyWatcher = new HomeKeyWatcher(this);
        homeKeyWatcher.setOnHomePressedListener(new HomeKeyWatcher.OnHomePressedListenerAdapter() {
            public void onPressed() {
            }
        });
        homeKeyWatcher.startWatch();

        this.paddingAdjuster = new ReaderCommonHelper.PaddingAdjuster(this.getApplicationContext());
        this.appleFontApplyer = new ReaderCommonHelper.AppleFontApplyer(this);

        this.scrollViewYHolder = new ReaderCommonHelper.ScrollViewYHolder(this);

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
                new ReaderCommonHelper.FontSizeApplyer().setFontSize(EpubReaderEpubActivity.this, fontSize, EpubReaderEpubActivity.class);
            }
        });
        dialog.show();
    }

    public void setTitle(String titleVal) {
        if (StringUtils.isNotBlank(this.getTitle())) {

            if (scrollView1 != null) {
                //記錄舊的 scrollView Y
                scrollViewYHolder.recordY(this.getTitle().toString(), scrollView1);
            }
        }

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
        return paddingAdjuster.getMaxWidth() - 10;
    }

    @Override
    public String fixNameToTitle(String orignFileName) {
        orignFileName = StringUtils.trimToEmpty(orignFileName);
        Pattern ptn = Pattern.compile("(.*?)\\.(?:epub)", Pattern.CASE_INSENSITIVE);
        Matcher mth = ptn.matcher(orignFileName);
        if (mth.find()) {
            return mth.group(1);
        }
        return orignFileName;
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
                            titleVal = fixNameToTitle(titleVal);
                            setTitle(titleVal);
                        }
                    });
                }

                private void epubProcess() {
                    try {
                        File file = epubFileZ.get();

                        epubViewerMainHandler.initBook(file);

                        ViewPagerHelper.triggerPageSelected(viewPager, null);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (translateView != null) {
                                    translateView.setText("");
                                }
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
    public void afterTxtViewChange() {
        //回復新的 scrollView Y
        scrollViewYHolder.restoreY(this.getTitle().toString(), scrollView1);
    }

    /**
     * 移動到下個書籤
     */
    public void moveToNextBookmark() {
        //TODO
    }

    @Override
    public void updateClient(long data) {
        // TODO
    }

    // --------------------------------------------------------------------

    private void initViewpagerChildrenView() {
        epubViewerMainHandler.getDto().setTextView(txtReaderView);
        initTextViewAfterService();

        homeKeyWatcher.setOnHomePressedListener(new HomeKeyWatcher.OnHomePressedListenerAdapter() {
            public void onPressed() {
                scrollViewYHolder.recordY(EpubReaderEpubActivity.this.getTitle().toString(), scrollView1);
            }
        });
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        int totalPageCount = 1000;

        MyPageAdapter pageAdapter = new MyPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pageAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int oldPosition = -1;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.v(TAG, "##### onPageScrolled");
            }

            @Override
            public void onPageSelected(final int position) {
                Log.v(TAG, "##### onPageSelected");

                final MyFragment fragment = (MyFragment) FragmentUtil.getCurrentViewPagerFragment(R.id.viewpager, viewPager, EpubReaderEpubActivity.this.getSupportFragmentManager());
                if (fragment == null) {
                    return;
                }

                scrollView1 = fragment.scrollView1;
                txtReaderView = fragment.txtReaderView;
                translateView = fragment.translateView;
                initViewpagerChildrenView();

                final ProgressDialog myDialog = ProgressDialog.show(EpubReaderEpubActivity.this, null, "讀取中...", true, false);
                myDialog.show();

                final Handler handler = new Handler();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (position > oldPosition) {
                            epubViewerMainHandler.gotoNextSpineSection();
                        } else {
                            epubViewerMainHandler.gotoPreviousSpineSection();
                        }

                        ((TextView4SpannableString) txtReaderView).setOnRenderCompleteCallback(new Runnable() {
                            @Override
                            public void run() {
                                myDialog.dismiss();
                                oldPosition = position;
                            }
                        });
                    }
                }).start();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.v(TAG, "##### onPageScrollStateChanged");
            }
        });
    }


    /**
     * Fragment
     */
    public static class MyFragment extends Fragment {

        public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

        private int pageIndex = 0;

        public static final MyFragment newInstance(String message, int pageIndex) {
            MyFragment f = new MyFragment();
            Bundle bdl = new Bundle(1);
            bdl.putString(EXTRA_MESSAGE, message);
            f.setArguments(bdl);
            f.pageIndex = pageIndex;
            return f;
        }

        private ScrollView scrollView1;
        private TextView txtReaderView;
        private TextView translateView;
        private ViewGroup container;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            String message = getArguments().getString(EXTRA_MESSAGE);

            View parentView = inflater.inflate(R.layout.subview_epub_reader, container, false);

            this.container = container;
            scrollView1 = (ScrollView) parentView.findViewById(R.id.scrollView1);
            txtReaderView = (TextView) parentView.findViewById(R.id.txtReaderView);
            translateView = (TextView) parentView.findViewById(R.id.translateView);

            return parentView;
        }
    }

    private Fragment generateFragment(int position) {
        return MyFragment.newInstance("", position);
    }

    /**
     * PageAdapter
     */
    class MyPageAdapter extends FragmentPagerAdapter {
        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return generateFragment(position);
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }
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
        BOOKMARK_MODE("書籤模式", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(final EpubReaderEpubActivity activity, Intent intent, Bundle bundle) {
                boolean currentMode = !activity.epubViewerMainHandler.getDto().getBookmarkMode();
                activity.epubViewerMainHandler.getDto().setBookmarkMode(currentMode);
                Toast.makeText(activity, "書籤模式" + (currentMode ? "on" : "off"), Toast.LENGTH_SHORT).show();
            }
        }, //
        BOOKMARK_MODE_MOVETO("移動到書籤", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(final EpubReaderEpubActivity activity, Intent intent, Bundle bundle) {
                activity.moveToNextBookmark();
            }
        }, //
        FOR_TEST("TEST", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(EpubReaderEpubActivity activity, Intent intent, Bundle bundle) {
                String filename = "/storage/1D0E-2671/Android/data/com.ghisler.android.TotalCommander/My Documents/books/Everybody Lies Big Data, New Data, and What the Internet - Seth Stephens-Davidowitz.epub";
                File file = new File(filename);
                activity.setTxtContentFromFile(file, null, null);
            }
        },//
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

//    @Override
//    public Object onRetainNonConfigurationInstance() {
//        Log.v(TAG, "onRetainNonConfigurationInstance");
//        return this;
//    }

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
