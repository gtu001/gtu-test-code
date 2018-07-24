package com.example.englishtester;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.translate.demo.TransApiNew;
import com.example.englishtester.common.ClickableSpanMethodCreater;
import com.example.englishtester.common.ClipboardHelper;
import com.example.englishtester.common.DialogFontSizeChange;
import com.example.englishtester.common.DropboxUtilV2;
import com.example.englishtester.common.FileConstantAccessUtil;
import com.example.englishtester.common.FileUtilAndroid;
import com.example.englishtester.common.FileUtilGtu;
import com.example.englishtester.common.FloatViewChecker;
import com.example.englishtester.common.FullPageMentionDialog;
import com.example.englishtester.common.GodToast;
import com.example.englishtester.common.HomeKeyWatcher;
import com.example.englishtester.common.IFloatServiceAidlInterface;
import com.example.englishtester.common.MainAdViewHelper;
import com.example.englishtester.common.SharedPreferencesUtil;
import com.example.englishtester.common.TitleTextSetter;
import com.example.englishtester.common.TxtCoordinateFetcher;
import com.example.englishtester.common.TxtReaderAppender;
import com.example.englishtester.common.WebViewHtmlFetcher;
import com.example.englishtester.common.HtmlWordParser;
import com.google.android.gms.ads.NativeExpressAdView;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TxtReaderActivity extends Activity implements FloatViewService.Callbacks {

    private static final String TAG = TxtReaderActivity.class.getSimpleName();

    public static final String KEY_CONTENT = "TxtReaderActivity_content";

    private static final Class[] CLICKABLE_SPAN_IMPL_CLZ = new Class[]{//
            TxtReaderAppender.WordSpan.class, //
            TxtReaderAppender.SimpleUrlLinkSpan.class//
    };//

    /**
     * 綁定服務器
     */
//    FloatViewService myService;
    IFloatServiceAidlInterface mService;

    /**
     * 取得dropbox txt服務
     */
    DropboxFileLoadService dropboxFileLoadService;
    /**
     * 最近查詢單字
     */
    RecentTxtMarkService recentTxtMarkService;
    /**
     * 蘋果字型
     */
    AppleFontApplyer appleFontApplyer;
    /**
     * 邊界調整
     */
    PaddingAdjuster paddingAdjuster;
    /**
     * 紀錄scroll位置
     */
    ScrollViewYHolder scrollViewYHolder;
    /**
     * Home 鍵觀察者
     */
    HomeKeyWatcher homeKeyWatcher;
    /**
     * 讀取網頁內容
     */
    WebViewHtmlFetcher webViewHtmlFetcher;

    EditText editText1;
    Button clearBtn;
    Button confirmBtn;
    LinearLayout linearLayout2;

    TextView txtView;
    TextView translateView;
    Button translateBtn;
    LinearLayout linearLayout1;

    ScrollView scrollView1;

    WebView webView;

    private NativeExpressAdView mAdView;

    TxtReaderActivityDTO dto = new TxtReaderActivityDTO();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);

        if (!FullPageMentionDialog.isAlreadyFullPageMention(this.getClass().getName(), this)) {
            FullPageMentionDialog.builder(R.drawable.full_page_mention_001, this).showDialog();
        }

        //contentView = createContentView();
        setContentView(R.layout.activity_txt_reader);
        mAdView = (NativeExpressAdView) findViewById(R.id.adView);

        //初始化廣告框
        MainAdViewHelper.getInstance().initAdView(mAdView, this);

        //群組一
        txtView = (TextView) this.findViewById(R.id.txtReaderView);
        translateView = (TextView) this.findViewById(R.id.translateView);
        translateBtn = (Button) this.findViewById(R.id.translateBtn);
        linearLayout1 = (LinearLayout) this.findViewById(R.id.linearLayout1);

        //for GIF Span
        dto.txtView = txtView;

        //群組2
        editText1 = (EditText) this.findViewById(R.id.editText1);
        clearBtn = (Button) this.findViewById(R.id.clearBtn);
        confirmBtn = (Button) this.findViewById(R.id.confirmBtn);
        linearLayout2 = (LinearLayout) this.findViewById(R.id.linearLayout2);
        webView = new WebView(this);

        //卷軸
        scrollView1 = (ScrollView) this.findViewById(R.id.scrollView1);

        //初始化服務
        initService();

        initView();

        //顯示第二群組
        showGroup(2);

        // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 取得螢幕翻轉前的狀態
        final TxtReaderActivity data = (TxtReaderActivity) getLastNonConfigurationInstance();
        if (data != null) {// 表示不是由於Configuration改變觸發的onCreate()
            Log.v(TAG, "load old status!");
            this.dto = data.dto;
        } else {
            // 正常執行要做的
            Log.v(TAG, "### initial ###");
            try {
                if (getIntent().getExtras().containsKey(KEY_CONTENT)) {
                    String content = getIntent().getExtras().getString(KEY_CONTENT);
                    this.pasteFromOutsideLoad(content);
                }
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage(), ex);
            }
        }
    }

    /**
     * 初始化view
     */
    private void initView() {
        //群組一
        initTextView();
        initTranslateBtn();
        initTranslateView();

        //群組2
        initGroup2View();

        //卷軸
        initScrollView();
    }

    /**
     * 紀錄卷軸位置
     */
    private void initScrollView() {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            return;
        }
        scrollView1.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                String fileName = dto != null ? StringUtils.isNotBlank(dto.getFileName()) ? dto.getFileName().toString() : "" : "";
                Log.v(TAG, "[ScrollView Y] " + fileName + " -> " + scrollY);
            }
        });
    }

    /**
     * 第一群組與第二群組顯示切換
     */
    private void showGroup(int group) {
        switch (group) {
            case 1:
                linearLayout1.setVisibility(View.VISIBLE);
                linearLayout2.setVisibility(View.GONE);
                break;
            case 2:
                linearLayout1.setVisibility(View.GONE);
                linearLayout2.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 初始化群組2
     */
    private void initGroup2View() {
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText1.setText("");
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText1.getText().toString();
                if (StringUtils.isBlank(text)) {
                    Toast.makeText(TxtReaderActivity.this, "請輸入英文句子!", Toast.LENGTH_SHORT).show();
                    return;
                }
                setContentText(text, false);
                setFileName("selfTyping");
            }
        });
        clearBtn.setPadding(5, 5, 5, 5);
        confirmBtn.setPadding(5, 5, 5, 5);
    }

    /**
     * 初始化閱讀器
     */
    private void initTextView() {
        float fontsize = new FontSizeApplyer().getFontSize(this);
        txtView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontsize);
        txtView.setHighlightColor(Color.TRANSPARENT);
        txtView.setMovementMethod(ClickableSpanMethodCreater.createMovementMethod(this, CLICKABLE_SPAN_IMPL_CLZ));
        txtView.setPadding(paddingAdjuster.width, paddingAdjuster.height, paddingAdjuster.width, paddingAdjuster.height);
//        参数add表示要增加的间距数值，对应android:lineSpacingExtra参数。
//        参数mult表示要增加的间距倍数，对应android:lineSpacingMultiplier参数。
        txtView.setLineSpacing(10, 1.4f);
        appleFontApplyer.apply(txtView);
    }

    /**
     * 初始化翻譯氣
     */
    private void initTranslateView() {
        float fontsize = new FontSizeApplyer().getFontSize(this);
        translateView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontsize);
        translateView.setHighlightColor(Color.TRANSPARENT);
        translateView.setMovementMethod(ClickableSpanMethodCreater.createMovementMethod(this, CLICKABLE_SPAN_IMPL_CLZ));
        translateView.setPadding(paddingAdjuster.width, paddingAdjuster.height, paddingAdjuster.width, paddingAdjuster.height);
//        参数add表示要增加的间距数值，对应android:lineSpacingExtra参数。
//        参数mult表示要增加的间距倍数，对应android:lineSpacingMultiplier参数。
        translateView.setLineSpacing(10, 1.4f);
        appleFontApplyer.apply(translateView);
    }

    /**
     * 字型大小修改
     */
    private class FontSizeApplyer {
        private static final float DEFAULT_FONTSIZE = 48f;

        private float getFontSize(ContextWrapper context) {
            if (SharedPreferencesUtil.hasData(context, TxtReaderActivity.class.getSimpleName(), "fontSize")) {
                return Float.parseFloat(SharedPreferencesUtil.getData(context, TxtReaderActivity.class.getSimpleName(), "fontSize"));
            } else {
                return DEFAULT_FONTSIZE;
            }
        }

        private void setFontSize(ContextWrapper context, float size) {
            SharedPreferencesUtil.putData(context, TxtReaderActivity.class.getSimpleName(), "fontSize", String.valueOf(size));
        }
    }


    /**
     * 讀取dropbox文件
     */
    private void loadDropboxList() {
        final List<DropboxUtilV2.DropboxUtilV2_DropboxFile> fileLst = dropboxFileLoadService.listFileV2();
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

        List<Map<String, Object>> listItem = new ArrayList<>();
        for (DropboxUtilV2.DropboxUtilV2_DropboxFile f : fileLst) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ItemTitle", f.getName());
            map.put("ItemDetail", DateFormatUtils.format(f.getClientModify(), "yyyy/MM/dd HH:mm:ss"));
            map.put("ItemDetailRight", FileUtilGtu.getSizeDescription(f.getSize()));
            listItem.add(map);
        }

        SimpleAdapter aryAdapter = new SimpleAdapter(this, listItem,// 資料來源
                R.layout.subview_dropboxlist, //
                new String[]{"ItemTitle", "ItemDetail", "ItemDetailRight"}, //
                new int[]{R.id.ItemTitle, R.id.ItemDetail, R.id.ItemDetailRight}//
        );

        AlertDialog.Builder builder = new AlertDialog.Builder(TxtReaderActivity.this);
        builder.setTitle("選擇dropbox檔案");
        builder.setAdapter(aryAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int paramInt) {
                        final String filename = fileLst.get(paramInt).getName();
                        final String fileDropboxPath = fileLst.get(paramInt).getFullPath();
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
                            dialog.dismiss();
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * 設定現在檔案名稱
     */
    private void setFileName(final String newName) {
        final String oldFileName = dto.fileName.toString();

        if (dto.fileName.length() > 0) {
            dto.fileName.delete(0, dto.fileName.length() - 1);
        }
        dto.fileName.append(newName);
        Log.v(TAG, "current fileName : " + dto.fileName);

        dto.scrollRecordApplyer = new Runnable() {
            @Override
            public void run() {
                Log.v(TAG, "[scrollRecordApplyer] run!");

                if (!StringUtils.isBlank(oldFileName)) {
                    //記錄舊的 scrollView Y
                    scrollViewYHolder.recordY(oldFileName, scrollView1);
                }

                //回復新的 scrollView Y
                scrollViewYHolder.restoreY(newName, scrollView1);
            }
        };
    }

    /**
     * 初始化服務
     */
    private void initService() {
        dropboxFileLoadService = new DropboxFileLoadService(this, DropboxApplicationActivity.getDropboxAccessToken(this));
        recentTxtMarkService = new RecentTxtMarkService(this);

        appleFontApplyer = new AppleFontApplyer();
        paddingAdjuster = new PaddingAdjuster();

        // 刪除舊資料
        recentTxtMarkService.deleteOldData();
        scrollViewYHolder = new ScrollViewYHolder(recentTxtMarkService);

        //監視home鍵
        homeKeyWatcher = new HomeKeyWatcher(this);
        homeKeyWatcher.setOnHomePressedListener(new HomeKeyWatcher.OnHomePressedListenerAdapter() {
            public void onPressed() {
                scrollViewYHolder.recordY(dto.getFileName().toString(), scrollView1);
            }
        });
        homeKeyWatcher.startWatch();

        //網頁取得器
        webViewHtmlFetcher = new WebViewHtmlFetcher(this);
        doOnoffService(true);
    }


    /**
     * 建立可點擊文件(中文翻譯部分)
     */
    private SpannableString getAppendTxtForTranslateView(final String chineseContent, Map<String, String> traMap) {
        SpannableString ss = new SpannableString(chineseContent);
        for (final String chinesePara : traMap.keySet()) {
            final String englishOrign = traMap.get(chinesePara);
            int start = chineseContent.indexOf(chinesePara);
            if (start == -1) {
                Log.v(TAG, " find Error -- " + chinesePara);
                continue;
            }
            int end = start + chinesePara.length();
            TxtReaderAppender.WordSpan clickableSpan = new TxtReaderAppender.WordSpan(0) {
                @Override
                public void updateDrawState(TextPaint ds) {
                    // ds.bgColor = Color.WHITE;
                    ds.setColor(Color.BLACK);
                    ds.setUnderlineText(false);
                }

                @Override
                public void onClick(View view) {
                    Log.v(TAG, "onClick -- " + englishOrign);
                    //Toast.makeText(TxtReaderActivity.this, englishOrign, Toast.LENGTH_SHORT).show();
                    new AlertDialog.Builder(TxtReaderActivity.this)//
                            .setTitle("原文")//
                            .setMessage(englishOrign)//
                            .show();//
                }
            };
            ss.setSpan(clickableSpan, start, end, Spanned.SPAN_COMPOSING);// SPAN_EXCLUSIVE_EXCLUSIVE
        }
        return ss;
    }

    /**
     * 初始化翻譯按鈕
     */
    private void initTranslateBtn() {
        translateBtn.setText("全文翻譯");
        translateBtn.setPadding(5, 5, 5, 5);
        translateBtn.setVisibility(View.GONE);
        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "# translateBtn click!");
                final String appId = BaiduApplicationActivity.getBaiduAppId(TxtReaderActivity.this);
                final String securityKey = BaiduApplicationActivity.getBaiduSecret(TxtReaderActivity.this);
                if (StringUtils.isBlank(appId) || StringUtils.isBlank(securityKey)) {
                    Toast.makeText(TxtReaderActivity.this, "尚未申請百度Api!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtils.isBlank(dto.content)) {
                    Toast.makeText(TxtReaderActivity.this, "文章內容為空", Toast.LENGTH_SHORT).show();
                    return;
                }
                //translateBtn.setEnabled(false);
                final TransApiNew api = new TransApiNew(appId, securityKey);

                final ProgressDialog progressDialog = new ProgressDialog(TxtReaderActivity.this);
                progressDialog.setMessage("處理中,請稍候...");
                progressDialog.show();

                final Handler handler = new Handler();
                if (dto.translateThread == null || dto.translateThread.getState() == Thread.State.TERMINATED) {
                    dto.translateThread = new Thread(new Runnable() {
                        private Map<String, String> traMap = new LinkedHashMap<String, String>();

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
                            if (dto.contentCopy == null || !StringUtils.equals(dto.contentCopy, dto.content)) {
                                dto.contentCopy = dto.content;
                                return true;
                            }
                            showToast("內容未變更, 無須重新翻譯");
                            return false;
                        }

                        private void showToast(final String message) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(TxtReaderActivity.this, message, Toast.LENGTH_SHORT).show();
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
                                final String reulstStr = getResultStr(dto.content);
                                Log.v(TAG, "reulstStr - " + reulstStr);
                                if (StringUtils.isBlank(reulstStr)) {
                                    throw new Exception("翻譯結果為空,無法翻譯!");
                                }
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        //translateView.setText(reulstStr);
                                        translateView.setText(getAppendTxtForTranslateView(reulstStr, traMap));
                                    }
                                });
                            } catch (Exception e) {
                                Log.e(TAG, "ERROR : " + e.getMessage(), e);
                                showToast("翻譯失敗!");
                                dto.contentCopy = null;
                            } finally {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        translateBtn.setEnabled(true);
                                        progressDialog.dismiss();
                                    }
                                });
                            }
                        }
                    });
                    dto.translateThread.setDaemon(true);
                    dto.translateThread.start();
                }
            }
        });
    }

    /**
     * 啟動關閉服務
     */
    public void doOnoffService(boolean isOn) {
        if (!FloatViewChecker.isPermissionOk(this)) {
            //申請開啟懸浮視窗權限
            FloatViewChecker.applyPermission(this, FloatViewActivity.FLOATVIEW_REQUESTCODE);
        } else {
            Intent intent = new Intent(TxtReaderActivity.this, FloatViewService.class);
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

    private LinearLayout createContentView() {
        LinearLayout layout = new LinearLayout(this);
        ScrollView scroll = new ScrollView(this);
        setContentView(scroll);
        layout.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(layout, //
                new LayoutParams(LayoutParams.FILL_PARENT, // 設定與螢幕同寬
                        LayoutParams.WRAP_CONTENT));// 高度隨內容而定
        return layout;
    }

    @Override
    public void updateClient(long data) {
        // TODO
    }

    /**
     * 從剪貼簿貼上純文字檔
     */
    private void pasteFromClipboard() {
        String text = ClipboardHelper.copyFromClipboard(this);
        ClipboardHelper.copyToClipboard(this, "");
        if (StringUtils.isBlank(text)) {
            Toast.makeText(this, "剪貼簿為空!", Toast.LENGTH_SHORT).show();
            return;
        }
        setFileName("clipboard");
        setContentText(text, false);
    }

    /**
     * 從外部讀取文件內容
     */
    private void pasteFromOutsideLoad(String content) {
        if (StringUtils.isBlank(content)) {
            Toast.makeText(this, "內容為空!", Toast.LENGTH_SHORT).show();
            return;
        }
        setFileName("clipboard");
        setContentText(content, false);
    }

    /**
     * 設定英文內容
     */
    private void setContentText(String content, boolean isHtmlFromWord) {
        TxtReaderAppender appender = new TxtReaderAppender(
                this, recentTxtMarkService, mService, dto, txtView //
        );
        if (!isHtmlFromWord) {
            txtView.setText(appender.getAppendTxt(content));
            this.dto.content = content;
        } else {
            txtView.setText(appender.getAppendTxt_HtmlFromWord(content, paddingAdjuster.maxWidth - 10));
            if (dto.currentHtmlFile != null) {
                this.dto.content = HtmlWordParser.newInstance().getFromFile(dto.currentHtmlFile, true, "");
            } else if (StringUtils.isNotBlank(dto.currentHtmlContent)) {
                this.dto.content = HtmlWordParser.newInstance().getFromContent(dto.currentHtmlContent, true, "");
            }
        }
        translateView.setText("");
        if (StringUtils.isNotBlank(content)) {
            translateBtn.setVisibility(View.VISIBLE);
        }
        showGroup(1);

        //更新卷軸
        if (dto.scrollRecordApplyer != null) {
            dto.scrollRecordApplyer.run();
        }
    }

    /**
     * 開啟字形大小修改Dialog
     */
    private void openFontSizeDialog() {
        DialogFontSizeChange dialog = new DialogFontSizeChange(this);
        dialog.apply(txtView.getTextSize(), Arrays.asList(txtView, translateView), new DialogFontSizeChange.ApplyFontSize() {
            @Override
            public void applyFontSize(float fontSize) {
                new FontSizeApplyer().setFontSize(TxtReaderActivity.this, fontSize);
            }
        });
        dialog.show();
    }

    /**
     * 儲存翻譯結果
     */
    private void saveContentToFile() {
        final String engContent = txtView.getText().toString();
        final String chnContent = translateView.getText().toString();
        if (StringUtils.isBlank(engContent)) {
            Toast.makeText(this, "沒有內容無須儲存!", Toast.LENGTH_SHORT).show();
            return;
        }

        final File saveToDir = FileConstantAccessUtil.getFileDir(this, new File(Constant.PropertiesFindActivity_Config_PATH));
        View parentView = LayoutInflater.from(this).inflate(R.layout.subview_single_edittext, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(parentView);
        final EditText editText = (EditText) parentView.findViewById(android.R.id.edit);
        String tmpFileName = StringUtils.isNotBlank(dto.fileName) ? dto.fileName.toString() : "txtReaderFile";
        editText.setText(tmpFileName + "_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss") + ".txt");
        builder.setTitle("儲存內容");
        builder.setMessage("路徑為 : " + saveToDir);
        builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String fileName = editText.getText().toString();
                if (StringUtils.isBlank(fileName)) {
                    Toast.makeText(TxtReaderActivity.this, "請輸入檔名!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String saveContent = engContent + "\n\n" + chnContent;
                try {
                    File saveFile = new File(saveToDir, fileName);
                    FileUtilAndroid.saveToFile(saveFile, saveContent);
                    Toast.makeText(TxtReaderActivity.this, "儲存成功 : " + saveFile, Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(TxtReaderActivity.this, "儲存失敗!", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    private void loadHtmlFromUrl() {
        View parentView = LayoutInflater.from(this).inflate(R.layout.subview_single_edittext, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(parentView);
        final EditText editText = (EditText) parentView.findViewById(android.R.id.edit);
        editText.setText("http://www.ign.com");
        builder.setTitle("開啟HTML");
        builder.setMessage("開啟HTML : ");
        builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {

            private String getURL() {
                String url = editText.getText().toString();
                url = StringUtils.trimToEmpty(url).replaceAll("[\r\n]*", "");
                return url;
            }

            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String url = getURL();
                editText.setText(url);
                if (StringUtils.isBlank(url)) {
                    Toast.makeText(TxtReaderActivity.this, "請輸入URL!", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    final AtomicReference<ProgressDialog> progDlg = new AtomicReference<ProgressDialog>();
                    progDlg.set(new ProgressDialog(TxtReaderActivity.this));
                    progDlg.get().setMessage("讀取中...");
                    progDlg.get().show();

                    webViewHtmlFetcher.applyConfig(true, new WebViewHtmlFetcher.HtmlGet() {
                        @Override
                        public void action(final String contentOrign, final Handler handler) {
                            try {
                                dto.currentHtmlFile = null;
                                dto.dropboxPicDir = null;
                                dto.currentHtmlUrl = url;

                                for (int ii = 0; ii < 10; ii++)
                                    Log.v(TAG, "[HtmlWordParser START]");

                                HtmlWordParser wordParser = HtmlWordParser.newInstance();
                                final String content = wordParser.getFromContentDebug(contentOrign, false);

                                for (int ii = 0; ii < 10; ii++)
                                    Log.v(TAG, "[HtmlWordParser END]");

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        setContentText(content, true);
                                        translateView.setText("");
                                        progDlg.get().dismiss();
                                    }
                                });
                            } catch (Exception ex) {
                                Log.e(TAG, "loadHtmlFromUrl ERR : " + ex.getMessage(), ex);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(TxtReaderActivity.this, "不可預期的失敗!!", Toast.LENGTH_SHORT).show();
                                        progDlg.get().dismiss();
                                    }
                                });
                            }
                        }
                    });

                    webViewHtmlFetcher.gotoUrl(url);
                } catch (Exception e) {
                    Toast.makeText(TxtReaderActivity.this, "讀取失敗!", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    /**
     * 讀取翻譯文字檔
     */
    private void loadContentFromFile() {
        File txtDirRoot = new File(Constant.PropertiesFindActivity_Config_PATH);
        File[] lists = txtDirRoot.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".txt");
            }
        });

        final List<String> fileList = new ArrayList<String>();
        final List<String> fileInfoList = new ArrayList<String>();
        final Map<String, File> fileMap = new LinkedHashMap<String, File>();
        for (File f : lists) {
            fileList.add(f.getName());
            fileInfoList.add(String.format("%s (%s)", f.getName(),//
                    FileUtilGtu.getSizeDescription(f.length())));
            fileMap.put(f.getName(), f);
        }

        new AlertDialog.Builder(TxtReaderActivity.this)//
                .setTitle("選擇dropbox檔案")//
                .setItems(fileInfoList.toArray(new String[0]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        String filename = fileList.get(paramInt);
                        File txtFile = fileMap.get(filename);
                        setTxtContentFromFile(txtFile, null, null);
                    }
                })//
                .show();
    }

    /**
     * 從檔案設定內容
     */
    private void setTxtContentFromFile(final File txtFile, final String title, final Callable<File> txtFileGetterCall) {
        try {
            final Handler handler = new Handler();

            final AtomicReference<ProgressDialog> dialog = new AtomicReference<ProgressDialog>();
            dialog.set(new ProgressDialog(TxtReaderActivity.this));
            dialog.get().setMessage("讀取中...");
            dialog.get().show();

            final AtomicReference<File> txtFileZ = new AtomicReference<>();
            txtFileZ.set(txtFile);

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
                                titleVal = txtFileZ.get().getName();
                            } else {
                                titleVal = title;
                            }
                            titleVal = fixName(titleVal);
                            setFileName(titleVal);
                            TitleTextSetter.setText(TxtReaderActivity.this, titleVal);
                        }
                    });
                }

                private void htmlProcess() {
                    //給html抓圖用
                    dto.currentHtmlFile = txtFileZ.get();
                    dto.currentHtmlContent = null;
                    dto.currentHtmlUrl = null;

                    for (int ii = 0; ii < 10; ii++)
                        Log.v(TAG, "[HtmlWordParser START]");

                    HtmlWordParser wordParser = HtmlWordParser.newInstance();
                    final String content = wordParser.getFromFile(txtFileZ.get());
                    String dropboxPicDir = wordParser.getPicDirForDropbox();

                    for (int ii = 0; ii < 10; ii++)
                        Log.v(TAG, "[HtmlWordParser END]");

                    Log.v(TAG, "[setTxtContentFromFile] dropboxPicDir = " + dropboxPicDir);

                    if (StringUtils.isNotBlank(dropboxPicDir)) {
                        File dropboxPicDirF = dropboxFileLoadService.downloadHtmlReferencePicDir(dropboxPicDir, -1);
                        dto.dropboxPicDir = dropboxPicDirF;
                    } else {
                        dto.dropboxPicDir = null;
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            setContentText(content, true);
                            translateView.setText("");
                            dialog.get().dismiss();
                        }
                    });
                }

                private void txtProcess() throws IOException {
                    String content = FileUtilAndroid.loadFileToString(txtFileZ.get());

                    final StringBuilder engSb = new StringBuilder();
                    final StringBuilder chsSb = new StringBuilder();

                    for (char c : content.toCharArray()) {
                        if (new String(new char[]{c}).getBytes().length >= 3) {
                            chsSb.append(c);
                            if (c == '。') {
                                chsSb.append("。\n");
                            }
                        } else {
                            engSb.append(c);
                        }
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            setContentText(engSb.toString(), false);
                            translateView.setText(chsSb.toString());
                            dialog.get().dismiss();
                        }
                    });
                }

                @Override
                public void run() {
                    try {
                        if (txtFileGetterCall != null) {
                            txtFileZ.set(txtFileGetterCall.call());
                        }

                        setTitleNameProcess();

                        if (txtFileZ.get().getName().endsWith(".htm") || txtFileZ.get().getName().endsWith(".html")) {
                            htmlProcess();
                        } else {
                            txtProcess();
                        }
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

    private class PaddingAdjuster {
        Display d;
        int width;
        int height;
        int maxWidth;
        int maxHeight;

        PaddingAdjuster() {
            d = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            width = (int) ((double) d.getWidth() * 0.1 / 2);
            height = (int) ((double) d.getHeight() * 0.1 / 2);

            maxWidth = d.getWidth() - (2 * width);
            maxHeight = d.getHeight() - (2 * height);
        }
    }

    private class AppleFontApplyer {
        Typeface myriadProRegular;

        AppleFontApplyer() {
            myriadProRegular = Typeface.createFromAsset(getAssets(), "fonts/Myriad Pro Regular.ttf");
        }

        void apply(TextView view) {
            view.setTypeface(myriadProRegular);
        }
    }

    /**
     * 紀錄 scrollView位置
     */
    private class ScrollViewYHolder {
        private final String TAG = ScrollViewYHolder.class.getSimpleName();

        RecentTxtMarkService recentTxtMarkService;

        ScrollViewYHolder(RecentTxtMarkService recentTxtMarkService) {
            this.recentTxtMarkService = recentTxtMarkService;
        }

        private boolean recordY(String fileName, ScrollView scrollView1) {
            Log.v(TAG, "[recordY] start ... " + fileName);
            if (StringUtils.isNotBlank(fileName)) {
                boolean updateResult = recentTxtMarkService.updateScrollViewYPos(fileName, scrollView1.getScrollY());
                Log.v(TAG, "[recordY] " + (updateResult ? "success" : "failed") + " ... " + fileName + " -> " + scrollView1.getScrollY());
                return updateResult;
            }
            return false;
        }

        private void restoreY(final String fileName, final ScrollView scrollView1) {
            Log.v(TAG, "[restoreY] start ... " + fileName);
            final int posY = recentTxtMarkService.getScrollViewYPos(fileName);
            scrollView1.post(new Runnable() {
                @Override
                public void run() {
                    scrollView1.scrollTo(0, posY);
                    Log.v(TAG, "[restoreY] : " + fileName + " -> " + posY);
                }
            });
        }
    }

    /**
     * 移動到下個書籤
     */
    public void moveToNextBookmark() {
        if (dto.getBookmarkHolder() == null || dto.getBookmarkHolder().isEmpty()) {

            if (!BuildConfig.DEBUG) {
                Toast.makeText(this, "目前沒有書籤紀錄!", Toast.LENGTH_SHORT).show();
            } else {
                //debug ↓↓↓↓↓↓↓↓
                List<RecentTxtMarkDAO.RecentTxtMark> qList = recentTxtMarkService.getFileMark(dto.getFileName().toString());
                if (qList.isEmpty()) {
                    Toast.makeText(this, "ERR mark size EMPTY !!", Toast.LENGTH_SHORT).show();
                } else {
                    int bookmarkCount = 0;
                    int searchCount = 0;
                    for (RecentTxtMarkDAO.RecentTxtMark v : qList) {
                        if (RecentTxtMarkDAO.BookmarkTypeEnum.BOOKMARK.isMatch(v.getBookmarkType())) {
                            bookmarkCount++;
                        } else {
                            searchCount++;
                        }
                    }
                    Toast.makeText(this, "ERR 書簽 : " + bookmarkCount + "/ 查詢 : " + searchCount + " , all : " + qList.size(), Toast.LENGTH_SHORT).show();
                }
                //debug ↑↑↑↑↑↑↑↑
            }
            return;
        }

        int currentId = dto.currentBookmarkId;
        List<Integer> lst = new ArrayList<>(dto.getBookmarkHolder().keySet());
        if (!lst.contains(currentId)) {
            currentId = lst.get(0);
        } else {
            int tmpId = lst.indexOf(currentId);
            if (tmpId + 1 >= lst.size()) {
                currentId = lst.get(0);
            } else {
                currentId = lst.get(tmpId + 1);
            }
        }

        dto.currentBookmarkId = currentId;
        final TxtReaderAppender.WordSpan spanObject = dto.getBookmarkHolder().get(currentId);
        TxtCoordinateFetcher coordinate = new TxtCoordinateFetcher(this.txtView, spanObject, this.getWindowManager());

        final Rect rect = coordinate.getCoordinate();

        scrollView1.post(new Runnable() {
            @Override
            public void run() {
                int offsetHeight = TxtReaderActivity.this.getResources().getDisplayMetrics().heightPixels / 2;
                int newSrollY = scrollView1.getScrollY() + rect.top - offsetHeight;
                scrollView1.scrollTo(rect.left, newSrollY);
                Toast.makeText(TxtReaderActivity.this, "移到 : " + spanObject.getWord(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --------------------------------------------------------------------

    static int REQUEST_CODE = 5566;
    static int MENU_FIRST = Menu.FIRST;

    enum TaskInfo {
        PASTE_TXT("從剪貼簿文件", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(final TxtReaderActivity activity, Intent intent, Bundle bundle) {
                activity.pasteFromClipboard();
            }
        }, //
        DROPBOX_TXT("Dropbox文件", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(final TxtReaderActivity activity, Intent intent, Bundle bundle) {
                activity.loadDropboxList();
            }
        }, //
        CHANGE_FONT_SIZE("改變字體大小", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(final TxtReaderActivity activity, Intent intent, Bundle bundle) {
                activity.openFontSizeDialog();
            }
        }, //
        SAVE_CONTENT_TO_FILE("儲存結果", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(final TxtReaderActivity activity, Intent intent, Bundle bundle) {
                activity.saveContentToFile();
            }
        }, //
        LOAD_CONTENT_FROM_FILE("讀取文件", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(final TxtReaderActivity activity, Intent intent, Bundle bundle) {
                activity.loadContentFromFile();
            }
        }, //
        LOAD_CONTENT_FROM_FILE_RANDOM("讀取文件(其他txt,htm檔)", MENU_FIRST++, REQUEST_CODE++, FileFindActivity.class) {
            protected void onActivityResult(TxtReaderActivity activity, Intent intent, Bundle bundle) {
                File file = FileFindActivity.FileFindActivityStarter.getFile(intent);
                activity.setTxtContentFromFile(file, null, null);
            }
        }, //
        BOOKMARK_MODE("書籤模式", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(final TxtReaderActivity activity, Intent intent, Bundle bundle) {
                boolean currentMode = !activity.dto.bookmarkMode.get();
                activity.dto.bookmarkMode.set(currentMode);
                Toast.makeText(activity, "書籤模式" + (currentMode ? "on" : "off"), Toast.LENGTH_SHORT).show();
            }
        }, //
        BOOKMARK_MODE_MOVETO("移動到書籤", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(final TxtReaderActivity activity, Intent intent, Bundle bundle) {
                activity.moveToNextBookmark();
            }
        }, //
        LOAD_HTML_FROM_URL("開啟網頁", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(final TxtReaderActivity activity, Intent intent, Bundle bundle) {
                activity.loadHtmlFromUrl();
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

        protected void onOptionsItemSelected(TxtReaderActivity activity, Intent intent, Bundle bundle) {
            intent.setClass(activity, clz);
            intent.putExtras(bundle);
            activity.startActivityForResult(intent, requestCode);
        }

        protected void onActivityResult(TxtReaderActivity activity, Intent intent, Bundle bundle) {
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

    public static class TxtReaderActivityDTO implements Serializable {
        private final StringBuilder fileName = new StringBuilder();
        private String content;//英文本文
        private String contentCopy;//英文本文備份(用來判斷是否翻譯過)
        private transient Thread translateThread; //翻譯thread
        private File dropboxPicDir;//設定dropbox下載圖片的目錄
        private File currentHtmlFile;//給html抓圖用
        private String currentHtmlContent;//原始html內文
        private String currentHtmlUrl;//html來源url
        private transient Runnable scrollRecordApplyer;
        private transient TextView txtView;//傳遞原文View
        private AtomicBoolean bookmarkMode = new AtomicBoolean(false);//是否開啟bookmark mode
        private transient Map<Integer, TxtReaderAppender.WordSpan> bookmarkHolder;
        private int currentBookmarkId = -1;

        public StringBuilder getFileName() {
            return fileName;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getContentCopy() {
            return contentCopy;
        }

        public void setContentCopy(String contentCopy) {
            this.contentCopy = contentCopy;
        }

        public File getDropboxPicDir() {
            return dropboxPicDir;
        }

        public File getCurrentHtmlFile() {
            return currentHtmlFile;
        }

        public void setDropboxPicDir(File dropboxPicDir) {
            this.dropboxPicDir = dropboxPicDir;
        }

        public TextView getTxtView() {
            return txtView;
        }

        public boolean getBookmarkMode() {
            return bookmarkMode.get();
        }

        public Map<Integer, TxtReaderAppender.WordSpan> getBookmarkHolder() {
            return bookmarkHolder;
        }

        public void setBookmarkHolder(Map<Integer, TxtReaderAppender.WordSpan> bookmarkHolder) {
            this.bookmarkHolder = bookmarkHolder;
        }

        public String getCurrentHtmlUrl() {
            return currentHtmlUrl;
        }
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
        mAdView.resume();
    }

    @Override
    public void onPause() {
        // Pause the AdView.
        mAdView.pause();
        scrollViewYHolder.recordY(dto.getFileName().toString(), scrollView1);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        // Destroy the AdView.
        mAdView.destroy();
        super.onDestroy();
    }

    // 中斷處理 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public void finish() {
        scrollViewYHolder.recordY(dto.getFileName().toString(), scrollView1);
        homeKeyWatcher.stopWatch();
        super.finish();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_POWER) {
            scrollViewYHolder.recordY(dto.getFileName().toString(), scrollView1);
        }
        return super.dispatchKeyEvent(event);
    }
    // 中斷處理 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
}
