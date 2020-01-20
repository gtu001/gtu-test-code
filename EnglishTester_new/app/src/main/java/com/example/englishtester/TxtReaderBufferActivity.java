package com.example.englishtester;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.SpannableString;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.translate.demo.TransApiNew;
import com.example.englishtester.common.ActionBarSimpleHandler;
import com.example.englishtester.common.AutoScrollDownHandler;
import com.example.englishtester.common.BackButtonPreventer;
import com.example.englishtester.common.ClickableSpanMethodCreater;
import com.example.englishtester.common.DBUtil;
import com.example.englishtester.common.DialogFontSizeChange;
import com.example.englishtester.common.FileUtilGtu;
import com.example.englishtester.common.FloatViewChecker;
import com.example.englishtester.common.FullPageMentionDialog;
import com.example.englishtester.common.HomeKeyWatcher;
import com.example.englishtester.common.IFloatServiceAidlInterface;
import com.example.englishtester.common.LoadingProgressDlg;
import com.example.englishtester.common.Log;
import com.example.englishtester.common.ReaderCommonHelper;
import com.example.englishtester.common.RecyclerPagerAdapter;
import com.example.englishtester.common.TextView4SpannableString;
import com.example.englishtester.common.TitleTextSetter;
import com.example.englishtester.common.TxtReaderAppenderSpanClass;
import com.example.englishtester.common.ViewPagerHelper;
import com.example.englishtester.common.epub.base.EpubViewerMainHandler;
import com.example.englishtester.common.interf.IDropboxFileLoadService;
import com.example.englishtester.common.interf.ITxtReaderActivity;
import com.example.englishtester.common.interf.TxtBufferActivityInterface;
import com.example.englishtester.common.txtbuffer.base.TxtBufferViewerMainHandler;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.cglib.core.internal.Function;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

public class TxtReaderBufferActivity extends FragmentActivity implements FloatViewService.Callbacks, ITxtReaderActivity, TxtBufferActivityInterface {

    private static final String TAG = TxtReaderBufferActivity.class.getSimpleName();

    public static final String KEY_CONTENT = "EpubReaderActivity_content";

    /**
     * 綁定服務器
     */
//    FloatViewService myService;
    IFloatServiceAidlInterface mService;
    HomeKeyWatcher homeKeyWatcher;
    ReaderCommonHelper.PaddingAdjuster paddingAdjuster;
    TxtBufferViewerMainHandler txtBufferViewerMainHandler;
    RecentTxtMarkService recentTxtMarkService;
    ReaderCommonHelper.AppleFontApplyer appleFontApplyer;
    ReaderCommonHelper.ScrollViewYHolder scrollViewYHolder;
    Handler handler = new Handler();
    MyPageAdapter pageAdapter;
    ActionBarSimpleHandler actionBarCustomTitleHandler;
    ReaderCommonHelper.FreeGoogleTranslateHandler freeGoogleTranslateHandler;
    Thread translateThread;
    BackButtonPreventer backButtonPreventer;
    AutoScrollDownHandler autoScrollDownHandler;
    ReaderCommonHelper.LineSpacingAdjuster mLineSpacingAdjuster;
    ReaderCommonHelper.FloatViewServiceOpenStatusReceiverHelper floatViewServiceOpenStatusReceiverHelper;
    IDropboxFileLoadService dropboxFileLoadService;

    TextView txtReaderView;
    TextView translateView;
    ScrollView scrollView1;
    ViewPager viewPager;
    ImageView floatBtn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);

        if (!FullPageMentionDialog.isAlreadyFullPageMention(this.getClass().getName(), this)) {
            FullPageMentionDialog.builder(R.drawable.full_page_mention_001, this).showDialog();
        }

        //contentView = createContentView();
        setContentView(R.layout.activity_epub_reader);

        viewPager = findViewById(R.id.viewpager);

        //懸浮按鈕
        floatBtn = (ImageView) findViewById(R.id.floatBtn);
        //懸浮按鈕
        initAutoScrollView();

        // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 取得螢幕翻轉前的狀態
        final TxtReaderBufferActivity data = (TxtReaderBufferActivity) getLastCustomNonConfigurationInstance();
        if (data != null) {// 表示不是由於Configuration改變觸發的onCreate()
            Log.v(TAG, "load old status!");

            this.restoreBackFromOrient(data);
        } else {
            // 正常執行要做的
            Log.v(TAG, "### initial ###");
            this.initServices();
        }
    }

    private void initTextViewAfterService(TextView txtReaderView, TextView translateView, Button translateBtn) {
        float fontsize = new ReaderCommonHelper.FontSizeApplyer().getFontSize(this, TxtReaderBufferActivity.class);

        txtReaderView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontsize);
        translateView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontsize);

        txtReaderView.setHighlightColor(Color.TRANSPARENT);
        txtReaderView.setMovementMethod(ClickableSpanMethodCreater.createMovementMethod(this, TxtReaderAppenderSpanClass.CLICKABLE_SPAN_IMPL_CLZ));

        paddingAdjuster.applyPadding(txtReaderView);

//        参数add表示要增加的间距数值，对应android:lineSpacingExtra参数。
//        参数mult表示要增加的间距倍数，对应android:lineSpacingMultiplier参数。
        txtReaderView.setLineSpacing(10, 1.4f);

        translateView.setLineSpacing(10, 1.4f);

        appleFontApplyer.apply(txtReaderView);
        appleFontApplyer.apply(translateView);

        paddingAdjuster.applyPadding(translateView);

        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translateBtnOnClick();
            }
        });
    }

    private void translateBtnOnClick() {
        try {
            final TxtBufferViewerMainHandler.PageContentHolder holder = this.txtBufferViewerMainHandler.gotoPosition(this.txtBufferViewerMainHandler.getDto().getPageIndex());
            final String content = StringUtils.trimToEmpty(holder.getTranslateOrignText());
            final String translateDoneText = StringUtils.trimToEmpty(holder.getTranslateDoneText());

            Log.v(TAG, "# translateBtn click!");
            final String appId = BaiduApplicationActivity.getBaiduAppId(TxtReaderBufferActivity.this);
            final String securityKey = BaiduApplicationActivity.getBaiduSecret(TxtReaderBufferActivity.this);
            if (StringUtils.isBlank(appId) || StringUtils.isBlank(securityKey)) {
                Toast.makeText(TxtReaderBufferActivity.this, "尚未申請百度Api!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringUtils.isBlank(content)) {
                Toast.makeText(TxtReaderBufferActivity.this, "文章內容為空!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringUtils.isBlank(getTranslateView().getText()) && StringUtils.isNotBlank(translateDoneText)) {
                getTranslateView().setText(translateDoneText);
            }
            if (StringUtils.isNotBlank(getTranslateView().getText())) {
                Toast.makeText(TxtReaderBufferActivity.this, "已完成翻譯!", Toast.LENGTH_SHORT).show();
                return;
            }

            getTranslateBtn().setEnabled(false);
            final TransApiNew api = new TransApiNew(appId, securityKey);

            final ProgressDialog progressDialog = new ProgressDialog(TxtReaderBufferActivity.this);
            progressDialog.setMessage("處理中,請稍候...");
            progressDialog.show();

            final Handler handler = new Handler();
            if (translateThread == null || translateThread.getState() == Thread.State.TERMINATED) {
                translateThread = new Thread(new Runnable() {
                    private Map<String, String> traMap = new LinkedHashMap<String, String>();

                    private void setBackupToPageHolder(String translateDoneText) {
                        holder.setTranslateDoneText(translateDoneText);
                    }

                    private String getResultStr(String content) throws JSONException, UnsupportedEncodingException {
                        Log.v(TAG, "content - " + content);
                        String jsonString = api.getTransResult(content, "en", "cht");
                        if (StringUtils.isBlank(jsonString)) {
                            return "";
                        }
                        Log.v(TAG, "jsonString - " + jsonString);
                        final JSONObject obj = new JSONObject(jsonString);
                        JSONArray arry = obj.getJSONArray("trans_result");
                        StringBuilder sb = new StringBuilder();
                        for (int ii = 0; ii < arry.length(); ii++) {
                            JSONObject o1 = arry.getJSONObject(ii);
                            String orgin = o1.getString("src");
                            String result = o1.getString("dst");
                            //建立可按的內容
                            traMap.put(result, orgin);
                            sb.append(result + "\r\n");
                        }
                        return sb.toString();
                    }

                    private boolean isNeedSearch() {
                        if (StringUtils.isBlank(translateView.getText())) {
                            return true;
                        }
                        showToast("內容未變更, 無須重新翻譯");
                        return false;
                    }

                    private void showToast(final String message) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(TxtReaderBufferActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    /**
                     * 拿掉中文(以免跟翻譯後的中文混淆)
                     */
                    private String ridOffChinese(String orignStr) {
                        char[] cs = orignStr.toCharArray();
                        StringBuilder sb = new StringBuilder();
                        for (int ii = 0; ii < cs.length; ii++) {
                            boolean isChinese = new String(new char[]{cs[ii]}).getBytes().length >= 3;
                            if (!isChinese) {
                                sb.append(cs[ii]);
                            }
                        }
                        return sb.toString();
                    }

                    @Override
                    public void run() {
                        try {
                            if (!isNeedSearch()) {
                                return;
                            }
                            final String reulstStr = getResultStr(content);
                            Log.v(TAG, "reulstStr - " + reulstStr);
                            if (StringUtils.isBlank(reulstStr)) {
                                throw new Exception("翻譯結果為空,無法翻譯!");
                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    //translateView.setText(reulstStr);
                                    getTranslateView().setText(reulstStr);
                                    setBackupToPageHolder(reulstStr);
                                }
                            });
                        } catch (Exception e) {
                            Log.e(TAG, "ERROR : " + e.getMessage(), e);
                            showToast("翻譯失敗!");
                        } finally {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    getTranslateBtn().setEnabled(true);
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    }
                });
                translateThread.setDaemon(true);
                translateThread.start();
            }
        } catch (Exception ex) {
            Log.e(TAG, "translateBtnOnClick ERR : " + ex.getMessage(), ex);
        }
    }

    private void initServices() {
        initServiceConnection();

        dropboxFileLoadService = DropboxFileLoadService.newInstance(this, DropboxApplicationActivity.getDropboxAccessToken(this));

        txtBufferViewerMainHandler = new TxtBufferViewerMainHandler(this);
        recentTxtMarkService = new RecentTxtMarkService(this);

        this.paddingAdjuster = new ReaderCommonHelper.PaddingAdjuster(this.getApplicationContext());
        this.appleFontApplyer = new ReaderCommonHelper.AppleFontApplyer(this);
        this.scrollViewYHolder = new ReaderCommonHelper.ScrollViewYHolder(this);
        this.actionBarCustomTitleHandler = ActionBarSimpleHandler.newInstance().init(this, 0xFFc7edcc);
        this.freeGoogleTranslateHandler = new ReaderCommonHelper.FreeGoogleTranslateHandler(this);
        this.backButtonPreventer = new BackButtonPreventer(this);
        this.backButtonPreventer.setIsPreventDefaultEvent(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (!txtBufferViewerMainHandler.getDto().getGoDirectLinkStack().isEmpty()) {
                    int rollbackPageIndex = txtBufferViewerMainHandler.getDto().getGoDirectLinkStack().pop();
                    gotoViewPagerPosition(rollbackPageIndex);
                    return true;
                }
                return false;
            }
        });
        this.autoScrollDownHandler = new AutoScrollDownHandler(this, new Callable<ScrollView>() {
            @Override
            public ScrollView call() throws Exception {
                return getScrollView1();
            }
        });

        this.floatViewServiceOpenStatusReceiverHelper = new ReaderCommonHelper.FloatViewServiceOpenStatusReceiverHelper();
        this.floatViewServiceOpenStatusReceiverHelper.registerReceiver(this, new Function<Boolean, Boolean>() {
            @Override
            public Boolean apply(Boolean aBoolean) {
                if (aBoolean && autoScrollDownHandler.isRunning()) {
                    autoScrollDownHandler.stop();
                    floatBtn.setTag(true);
                } else if (floatBtn.getTag() != null && aBoolean == false) {
                    autoScrollDownHandler.start();
                    floatBtn.setTag(null);
                }
                return false;
            }
        });

        //監視home鍵
        homeKeyWatcher = new HomeKeyWatcher(this);
        homeKeyWatcher.setOnHomePressedListener(new HomeKeyWatcher.OnHomePressedListenerAdapter() {
            public void onPressed() {
                autoScrollDownHandler.stop();
                if (scrollView1 == null) {
                    return;
                }
                scrollViewYHolder.recordY(TxtReaderBufferActivity.this.getTitle().toString(), getScrollView1());
            }
        });
        homeKeyWatcher.startWatch();

        mLineSpacingAdjuster = new ReaderCommonHelper.LineSpacingAdjuster(this);

        this.doOnoffService(true);
    }


    /**
     * 開啟字形大小修改Dialog
     */
    private void openFontSizeDialog() {
        DialogFontSizeChange dialog = new DialogFontSizeChange(this);
        dialog.apply(getTxtReaderView().getTextSize(), Arrays.asList(getTxtReaderView(), getTranslateView()), new DialogFontSizeChange.ApplyFontSize() {
            @Override
            public void applyFontSize(float fontSize) {
                new ReaderCommonHelper.FontSizeApplyer().setFontSize(TxtReaderBufferActivity.this, fontSize, TxtReaderBufferActivity.class);
            }
        });
        dialog.show();
    }

    public void setTitle(String titleVal) {
        if (StringUtils.isNotBlank(this.getTitle())) {

            if (getScrollView1() != null) {
                //記錄舊的 scrollView Y
                scrollViewYHolder.recordY(this.getTitle().toString(), getScrollView1());
            }

            if (getScrollView1() != null) {
                //記錄舊的 scrollView Y
                scrollViewYHolder.restoreY(titleVal, getScrollView1());
            }
        }

        TitleTextSetter.setText(TxtReaderBufferActivity.this, titleVal);
        this.txtBufferViewerMainHandler.getDto().setFileName(titleVal);
        this.actionBarCustomTitleHandler.setText(titleVal);
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


    /**
     * 從檔案設定內容
     */
    private void setTxtContentFromFile(final File epubFile, final String title, final Callable<File> txtFileGetterCall) {
        try {
            final Handler handler = new Handler();

            final AtomicReference<ProgressDialog> dialog = new AtomicReference<ProgressDialog>();
            dialog.set(new ProgressDialog(TxtReaderBufferActivity.this));
            dialog.get().setMessage("讀取中...");
            dialog.get().show();

            final AtomicReference<File> epubFileZ = new AtomicReference<>();
            epubFileZ.set(epubFile);

            new Thread(new Runnable() {

                private String fixName(String title) {
                    title = StringUtils.trimToEmpty(title);
                    Pattern ptn = Pattern.compile("(.*?)\\.(?:htm|html|txt|properties)", Pattern.CASE_INSENSITIVE);
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
                            //TitleTextSetter.setText(TxtReaderActivity.this, titleVal);
                            actionBarCustomTitleHandler.setText(titleVal);
                        }
                    });
                }

                private void epubProcess() {
                    try {
                        File file = epubFileZ.get();

                        //設定書籍 及 初始化
                        txtBufferViewerMainHandler.initBook(file, TxtReaderBufferActivity.this);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                initViewPager();

                                viewPager.setCurrentItem(0);

                                gotoViewPagerPosition(0);

                                if (translateView != null) {
                                    translateView.setText("");
                                }

                                dialog.get().dismiss();
                            }
                        });

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pageAdapter.notifyDataSetChanged();
                                setTitle(txtBufferViewerMainHandler.getCurrentTitle(0));
                            }
                        }, 1000L);
                    } catch (Exception ex) {
                        Log.e(TAG, "epubProcess ERR : " + ex.getMessage(), ex);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(TxtReaderBufferActivity.this, "讀取失敗", Toast.LENGTH_SHORT).show();
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
            try {
                Intent intent = new Intent(this, FloatViewService.class);
                if (isOn) {
                    startService(intent);
                    bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
                } else {
                    stopService(intent);
                    unbindService(mConnection);
                }
            } catch (Exception ex) {
                Log.e(TAG, "doOnoffService ERR : " + ex.getMessage(), ex);
            }
        }
    }

    /**
     * 設定綁定服務器連線
     */
    private ServiceConnection mConnection;

    private void initServiceConnection() {
        mConnection = new ServiceConnection() {
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
    }

    /**
     * 移動到下個書籤
     */
    public void moveToBookmarkPage() {
        MoveToNextBookmarkHandler moveHandler = new MoveToNextBookmarkHandler();
        if (!moveHandler.validateInitOk()) {
            Toast.makeText(TxtReaderBufferActivity.this, "請先開啟Epub!", Toast.LENGTH_SHORT).show();
            return;
        }
        moveHandler.initReference();
        moveHandler.showDlg();
    }

    /**
     * 移動到下個書籤
     */
    public void moveToNextBookmark() {
        if (getTxtReaderView() == null || getScrollView1() == null || txtBufferViewerMainHandler == null || txtBufferViewerMainHandler.getDto() == null) {
            Toast.makeText(this, "尚未開啟書籍", Toast.LENGTH_SHORT).show();
            return;
        }
        ReaderCommonHelper.getInst().moveToNextBookmark(txtBufferViewerMainHandler.getDto(), getTxtReaderView(), getScrollView1(), this, this.getWindowManager());
    }

    @Override
    public void updateClient(long data) {
        // TODO
    }

    public void gotoViewPagerPosition(int position) {
        ViewPagerHelper.triggerPageSelected(viewPager, position);
    }

    private class MoveToNextBookmarkHandler implements DialogInterface.OnClickListener {

        private List<Row> lst = new ArrayList<>();
        private List<Map<String, Object>> lst4Adapter = new ArrayList<>();

        private class Row {
            String file_name;
            int bookmark_type;
            long insert_date;
            int page_index;

            private Row(Map<String, Object> map) {
//                {file_name=Everybody Lies Big Data, New Data, and What the Internet - Seth Stephens-Davidowitz[4], bookmark_type=2, insert_date=496398149}
                file_name = (String) map.get("file_name");
                bookmark_type = (Integer) map.get("bookmark_type");
                insert_date = (Integer) map.get("insert_date");
                page_index = (Integer) map.get("page_index");
            }

            private Map<String, Object> toAdapterMap() {
                Map<String, Object> map = new HashMap<>();
                map.put("ItemTitle", file_name);
                map.put("ItemDetail", "page " + (page_index + 1));
                map.put("ItemDetailRight", DateFormatUtils.format(insert_date, "yyyy/MM/dd HH:mm:ss"));
                map.put("ItemDetail2", "");
                return map;
            }
        }

        private boolean validateInitOk() {
            if (txtBufferViewerMainHandler != null && txtBufferViewerMainHandler.isInitDone()) {
                return true;
            }
            return false;
        }

        private void initReference() {
            String fileName = EpubViewerMainHandler.EpubPageTitleHandler.fixNameToTitle(txtBufferViewerMainHandler.getDto().getBookFile().getName());

            StringBuilder sb = new StringBuilder();
            sb.append(" select file_name , bookmark_type, max(insert_date) as insert_date, page_index      ");
            sb.append(" from recent_txt_mark                                                              ");
            sb.append(" where bookmark_type in (%s) and file_name like '%s%%'  and page_index != -1       ");
            sb.append("  group by file_name                                                               ");
            sb.append("  order by 3 desc                                                                  ");

            String bookmarkTypeStr = StringUtils.join(Arrays.asList(RecentTxtMarkDAO.BookmarkTypeEnum.BOOKMARK.getType(), RecentTxtMarkDAO.BookmarkTypeEnum.SCROLL_Y_POS.getType()), ",");

            String sql = String.format(sb.toString(), bookmarkTypeStr, fileName);
            List<Map<String, Object>> lst = DBUtil.queryBySQL_realType(sql, new String[0], TxtReaderBufferActivity.this);
            for (Map<String, Object> map : lst) {
                Row row = new Row(map);
                this.lst.add(row);
                this.lst4Adapter.add(row.toAdapterMap());
            }
        }

        private void showDlg() {
            if (lst4Adapter.isEmpty()) {
                Toast.makeText(TxtReaderBufferActivity.this, "尚無書籤紀錄", Toast.LENGTH_SHORT).show();
                return;
            }

            SimpleAdapter aryAdapter = new SimpleAdapter(TxtReaderBufferActivity.this, lst4Adapter,// 資料來源
                    R.layout.subview_dropboxlist, //
                    new String[]{"ItemTitle", "ItemDetail", "ItemDetailRight", "ItemDetail2"}, //
                    new int[]{R.id.ItemTitle, R.id.ItemDetail, R.id.ItemDetailRight, R.id.ItemDetail2}//
            );

            AlertDialog.Builder builder = new AlertDialog.Builder(TxtReaderBufferActivity.this);
            builder.setTitle("請選擇書籤");
            builder.setAdapter(aryAdapter, this);
            AlertDialog alert = builder.create();
            alert.show();
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            final Row row = lst.get(which);
            Pattern ptn = Pattern.compile("^.*\\[(.*?)\\]$");
            Matcher mth = ptn.matcher(row.file_name);

            int pos = 0;
            int dtlPos = 0;

            if (mth.find()) {

                String pageInfo = mth.group(1);

                if (pageInfo.matches("\\d+")) {
                    pos = Integer.parseInt(pageInfo);
                } else {
                    Matcher mth2 = Pattern.compile("(\\d+)\\s\\((\\d+)").matcher(pageInfo);
                    if (mth2.find()) {
                        pos = Integer.parseInt(mth2.group(1));
                        dtlPos = Integer.parseInt(mth2.group(1));
                    } else {
                        throw new RuntimeException("無法取得 SpinePos : " + pageInfo);
                    }
                }
            }

            Toast.makeText(TxtReaderBufferActivity.this, "跳至章節 " + pos + " : " + dtlPos + " : Page : " + (row.page_index + 1), Toast.LENGTH_SHORT).show();
            gotoViewPagerPosition(row.page_index);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setTitle(txtBufferViewerMainHandler.getCurrentTitle(row.page_index));
                }
            }, 1000L);
        }
    }

    // --------------------------------------------------------------------

    private void initViewpagerChildrenView(TextView txtReaderView, TextView translateView, Button translateBtn) {
        txtBufferViewerMainHandler.getDto().setTextView(txtReaderView);
        initTextViewAfterService(txtReaderView, translateView, translateBtn);
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        pageAdapter = new MyPageAdapter(this); //getSupportFragmentManager()

        viewPager.setAdapter(pageAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.v(TAG, "##### onPageScrolled");
            }

            @Override
            public void onPageSelected(final int position) {
                Log.v(TAG, "##### onPageSelected");
                txtBufferViewerMainHandler.getDto().setPageIndex(position);

                txtBufferViewerMainHandler.getDto().setGoDirectLink(false);

                initScrollView1YPos();

                setTitle(txtBufferViewerMainHandler.getCurrentTitle(position));

                mLineSpacingAdjuster.apply(getTxtReaderView());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.v(TAG, "##### onPageScrollStateChanged");
            }
        });
    }

    //懸浮按鈕
    private void initAutoScrollView() {
        final Handler handler = new Handler();

        floatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isStart = autoScrollDownHandler.toggle();
//                return true;
            }
        });

        floatBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                autoScrollDownHandler.stop();
                new AlertDialog.Builder(TxtReaderBufferActivity.this).setItems(new String[]{"靠左", "靠右", "慢", "中", "快"}, //
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, final int which) {
                                handler.post(new Runnable() {
                                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                                    @Override
                                    public void run() {
                                        RelativeLayout.LayoutParams layout = (RelativeLayout.LayoutParams) floatBtn.getLayoutParams();
                                        switch (which) {
                                            case 0:
                                                layout.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                                layout.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                                break;
                                            case 1:
                                                layout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                                layout.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                                break;
                                            case 2:
                                                autoScrollDownHandler.setSpeedType(AutoScrollDownHandler.SpeedType.SLOW);
                                                break;
                                            case 3:
                                                autoScrollDownHandler.setSpeedType(AutoScrollDownHandler.SpeedType.MIDDLE);
                                                break;
                                            case 4:
                                                autoScrollDownHandler.setSpeedType(AutoScrollDownHandler.SpeedType.FAST);
                                                break;
                                        }
                                        floatBtn.setLayoutParams(layout);
                                    }
                                });
                            }
                        }).show();
                return true;
            }
        });
    }

    @Override
    public void onWordClickBefore_TxtReaderAppender(String word) {
        if (autoScrollDownHandler.isRunning()) {
            autoScrollDownHandler.stop();
            floatBtn.setTag(true);
        }
    }

    private void progressDlgHandle(TextView txtReaderView) {
        final AtomicReference<ProgressDialog> dlg = new AtomicReference<>(LoadingProgressDlg.createSimpleLoadingDlg(TxtReaderBufferActivity.this));
        ((TextView4SpannableString) txtReaderView).getHandler().post(new Runnable() {
            @Override
            public void run() {
                dlg.get().show();
            }
        });
        ((TextView4SpannableString) txtReaderView).setOnRenderCompleteCallback(new Runnable() {
            @Override
            public void run() {
                dlg.get().dismiss();
            }
        });
    }

    private class MyViewHolder extends RecyclerPagerAdapter.ViewHolder {

        private ScrollView scrollView1;
        private TextView txtReaderView;
        private TextView translateView;
        private Button translateBtn;
        private ViewGroup container;
        private boolean isDone = false;

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class PageForwardThread extends Thread {

        TxtBufferViewerMainHandler.PageContentHolder pageHolder;
        MyViewHolder my;
        int position;
        Handler handler = new Handler();

        PageForwardThread(final MyViewHolder my, int position) {
            this.my = my;
            this.self = TxtReaderBufferActivity.this;
            this.position = position;
        }

        protected boolean debugMode = false;
        protected TxtReaderBufferActivity self;

        private AtomicReference<Boolean> firstPageInit = new AtomicReference<>();

        private void sleep_t(long t) {
            try {
                sleep(t);
            } catch (Exception e) {
            }
        }

        public void run() {
            while (true) {
                if (txtBufferViewerMainHandler != null && txtBufferViewerMainHandler.isInitDone()) {
                    if (firstPageInit.get() == null && !txtBufferViewerMainHandler.isReady4Position()) {

                        firstPageInit.set(false);
                        txtBufferViewerMainHandler.gotoFirstSpineSection(new Runnable() {
                            @Override
                            public void run() {
                                firstPageInit.set(true);
                            }
                        });

                        txtBufferViewerMainHandler.triggerEvent();

                        sleep_t(100);
                        continue;
                    } else if (firstPageInit.get() != null && !firstPageInit.get()) {

                        sleep_t(100);
                        continue;
                    }

                    //設定此頁內容
                    setTextViewContent();
                    break;
                }

                sleep_t(100);
                continue;
            }
        }

        protected void setTextViewContent() {
            long startTime = System.currentTimeMillis();

            pageHolder = txtBufferViewerMainHandler.gotoPosition(position);

            handler.post(new Runnable() {

                private void processContent() {
                    if (!debugMode) {
                        SpannableString spannable = pageHolder.getCurrentPage();
                        my.txtReaderView.setText(spannable);
                    } else {
                        String debugContent = pageHolder.getPageContent4Debug();
                        my.txtReaderView.setText(debugContent);
                    }

                    my.translateView.setText(StringUtils.trimToEmpty(pageHolder.getTranslateDoneText()));
                }

                @Override
                public void run() {
                    this.processContent();
                }
            });

            my.isDone = true;

            long duringTime = System.currentTimeMillis() - startTime;
            Log.v(TAG, "duringTime : " + duringTime);
        }
    }

    /**
     * PageAdapter
     */
    private class MyPageAdapter extends RecyclerPagerAdapter {

        TxtReaderBufferActivity self;
        boolean debugMode = false;

        MyPageAdapter(TxtReaderBufferActivity self) {
            this.self = self;
        }

        @Override
        public int getItemCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final MyViewHolder my = (MyViewHolder) holder;

            self.setToMasterView(my);

            my.txtReaderView.setTag("txtReaderView-" + position);
            my.translateView.setTag("translateView-" + position);
            my.translateBtn.setTag("translateBtn-" + position);
            my.scrollView1.setTag("scrollView1-" + position);

            //init view
            self.initViewpagerChildrenView(my.txtReaderView, my.translateView, my.translateBtn);

            final Handler handler = new Handler();

            PageForwardThread pageForwardThread = new PageForwardThread(my, position);
            pageForwardThread.start();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
            View parentView = LayoutInflater.from(self).inflate(R.layout.subview_epub_reader, container, false);

            MyViewHolder my = new MyViewHolder(parentView);
            my.container = container;
            my.scrollView1 = (ScrollView) parentView.findViewById(R.id.scrollView1);
            my.txtReaderView = (TextView) parentView.findViewById(R.id.txtReaderView);
            my.translateView = (TextView) parentView.findViewById(R.id.translateView);
            my.translateBtn = (Button) parentView.findViewById(R.id.translateBtn);

            return my;
        }
    }

    private void setToMasterView(MyViewHolder my) {
        txtReaderView = my.txtReaderView;
        translateView = my.translateView;
        scrollView1 = my.scrollView1;
    }

    private void initScrollView1YPos() {
        ScrollView scrollView = getScrollView1();
        if (scrollView != null) {
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    int initY = 0;
                    getScrollView1().scrollTo(0, initY);
                }
            });
        }
    }

    private ScrollView getScrollView1() {
        return (ScrollView) viewPager.findViewWithTag("scrollView1-" + viewPager.getCurrentItem());
    }

    private TextView getTxtReaderView() {
        return (TextView) viewPager.findViewWithTag("txtReaderView-" + viewPager.getCurrentItem());
    }

    private TextView getTranslateView() {
        return (TextView) viewPager.findViewWithTag("translateView-" + viewPager.getCurrentItem());
    }

    private Button getTranslateBtn() {
        return (Button) viewPager.findViewWithTag("translateBtn-" + viewPager.getCurrentItem());
    }


    private void restoreBackFromOrient(TxtReaderBufferActivity activity) {
        final File bookFile = activity.txtBufferViewerMainHandler.getDto().getBookFile();
        final int position = activity.txtBufferViewerMainHandler.getDto().getPageIndex();

        //停掉上階段 ↓↓↓↓↓↓
        try {
            activity.homeKeyWatcher.stopWatch();
            activity.unbindService(activity.mConnection);
        } catch (Exception ex) {
            Log.e(TAG, "restoreBackFromOrient ERR : " + ex.getMessage(), ex);
        }
        //停掉上階段 ↑↑↑↑↑↑

        this.initServices();

        final Handler handler = new Handler();

        final AtomicReference<ProgressDialog> dialog = new AtomicReference<ProgressDialog>();
        dialog.set(new ProgressDialog(TxtReaderBufferActivity.this));
        dialog.get().setMessage("讀取中...");
        dialog.get().show();

        new Thread(new Runnable() {

            private void epubProcess() {
                try {
                    //設定書籍 及 初始化
                    txtBufferViewerMainHandler.initBook(bookFile, TxtReaderBufferActivity.this);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            initViewPager();

                            viewPager.setCurrentItem(0);

                            gotoViewPagerPosition(position);

                            if (translateView != null) {
                                translateView.setText("");
                            }

                            dialog.get().dismiss();
                        }
                    });

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pageAdapter.notifyDataSetChanged();
                            setTitle(txtBufferViewerMainHandler.getCurrentTitle(0));
                        }
                    }, 1000L);
                } catch (Exception ex) {
                    Log.e(TAG, "epubProcess ERR : " + ex.getMessage(), ex);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TxtReaderBufferActivity.this, "讀取失敗", Toast.LENGTH_SHORT).show();
                            dialog.get().dismiss();
                        }
                    });
                }
            }

            @Override
            public void run() {
                try {
                    this.epubProcess();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                } finally {
                }
            }
        }).start();
    }

    public int getCurrentPageIndex() {
        return viewPager.getCurrentItem();
    }

    // ↓↓↓↓↓↓ 按兩下回前頁-------------------------------------------------------------------
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        return this.backButtonPreventer.onKeyDown(keyCode, event);
    }
    // ↑↑↑↑↑↑ 按兩下回前頁-------------------------------------------------------------------

    // --------------------------------------------------------------------

    /**
     * 讀取dropbox文件
     */
    private void loadDropboxFile(final String filename, final String fileDropboxPath) {
        try {
            final String fileExtension = FileUtilGtu.getSubName(filename);

            setTxtContentFromFile(null, filename, new Callable<File>() {
                @Override
                public File call() {
                    File loadFile = dropboxFileLoadService.downloadFile(fileDropboxPath, fileExtension);
                    return loadFile;
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
        }
    }

    // --------------------------------------------------------------------

    static int REQUEST_CODE = 5566;
    static int MENU_FIRST = Menu.FIRST;

    enum TaskInfo {
        CHANGE_FONT_SIZE("改變字體大小", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(final TxtReaderBufferActivity activity, Intent intent, Bundle bundle) {
                activity.openFontSizeDialog();
            }
        }, //
        CHANGE_SPACING_SIZE("改變行距", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(final TxtReaderBufferActivity activity, Intent intent, Bundle bundle) {
                activity.mLineSpacingAdjuster.openSpacingSizeDialog(activity.getTxtReaderView());
            }
        }, //
        CHOICE_FONT("選擇字型", MENU_FIRST++, REQUEST_CODE++, null, true) {
            protected void onOptionsItemSelected(final TxtReaderBufferActivity activity, Intent intent, Bundle bundle) {
                activity.appleFontApplyer.choiceTypeface(activity.getTxtReaderView(), activity.getTranslateView());
            }
        }, //
        DROPBOX_TXT("Dropbox文件", MENU_FIRST++, REQUEST_CODE++, DropboxHtmlListActivity.class) {
            protected void onActivityResult(TxtReaderBufferActivity activity, Intent intent, Bundle bundle) {
                String fileName = DropboxHtmlListActivity.DropboxHtmlListActivityStarter.getFileName(intent);
                String filePath = DropboxHtmlListActivity.DropboxHtmlListActivityStarter.getFile(intent);
                activity.loadDropboxFile(fileName, filePath);
            }
        }, //
        BOOKMARK_MODE("書籤模式", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(final TxtReaderBufferActivity activity, Intent intent, Bundle bundle) {
                boolean currentMode = !activity.txtBufferViewerMainHandler.getDto().getBookmarkMode();
                activity.txtBufferViewerMainHandler.getDto().setBookmarkMode(currentMode);
                Toast.makeText(activity, "書籤模式" + (currentMode ? "on" : "off"), Toast.LENGTH_SHORT).show();
            }
        }, //
        BOOKMARK_MODE_PAGES("移動到最後瀏覽頁", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(final TxtReaderBufferActivity activity, Intent intent, Bundle bundle) {
                activity.moveToBookmarkPage();
            }
        }, //
        BOOKMARK_MODE_MOVETO("移動到書籤", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(final TxtReaderBufferActivity activity, Intent intent, Bundle bundle) {
                activity.moveToNextBookmark();
            }
        }, //
        GOOGLE_FREE_TRANSLATE("Google免費翻譯", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(final TxtReaderBufferActivity activity, Intent intent, Bundle bundle) {
                final TxtBufferViewerMainHandler.PageContentHolder holder = activity.txtBufferViewerMainHandler.gotoPosition(activity.txtBufferViewerMainHandler.getDto().getPageIndex());
                final String content = StringUtils.trimToEmpty(holder.getTranslateOrignText());
                activity.freeGoogleTranslateHandler.init(content);
                activity.freeGoogleTranslateHandler.showDlg();
            }
        }, //
        MOVE_TO_STARTER("移動到頂部", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(final TxtReaderBufferActivity activity, Intent intent, Bundle bundle) {
                activity.getScrollView1().post(new Runnable() {
                    @Override
                    public void run() {
                        activity.getScrollView1().scrollTo(0, 0);
                    }
                });
            }
        }, //
        //        DEBUG_ONLY_002("________PosX", MENU_FIRST++, REQUEST_CODE++, null, true) {
//            protected void onOptionsItemSelected(EpubReaderEpubActivity activity, Intent intent, Bundle bundle) {
//                String value = activity.txtBufferViewerMainHandler.getCurrentSpinePos() + " - " + activity.viewPager.getCurrentItem();
//                Log.toast(activity, value);
//            }
//        },//
//        DEBUG_ONLY_003("________DebugX", MENU_FIRST++, REQUEST_CODE++, null, true) {
//            protected void onOptionsItemSelected(EpubReaderEpubActivity activity, Intent intent, Bundle bundle) {
//                int position = activity.viewPager.getCurrentItem();
//                TxtBufferViewerMainHandler.PageContentHolder holder = activity.txtBufferViewerMainHandler.gotoPosition(position);
//                String debugContent = holder.getPageContent4Debug();
//                Log.lineFix(TAG, debugContent);
//            }
//        },//
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

        protected void onOptionsItemSelected(TxtReaderBufferActivity activity, Intent intent, Bundle bundle) {
            intent.setClass(activity, clz);
            intent.putExtras(bundle);
            activity.startActivityForResult(intent, requestCode);
        }

        protected void onActivityResult(TxtReaderBufferActivity activity, Intent intent, Bundle bundle) {
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

            //純測試
            if (!BuildConfig.DEBUG && e.debugOnly == true) {
                continue;
            }

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

    // ＊ for FragmentActivity ＊
    @Override
    public Object onRetainCustomNonConfigurationInstance() {
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