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
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.translate.demo.TransApi;
import com.example.englishtester.common.ClipboardHelper;
import com.example.englishtester.common.DialogFontSizeChange;
import com.example.englishtester.common.DropboxUtilV2;
import com.example.englishtester.common.FileConstantAccessUtil;
import com.example.englishtester.common.FileUtilAndroid;
import com.example.englishtester.common.FileUtilGtu;
import com.example.englishtester.common.FloatViewChecker;
import com.example.englishtester.common.FullPageMentionDialog;
import com.example.englishtester.common.IFloatServiceAidlInterface;
import com.example.englishtester.common.MainAdViewHelper;
import com.example.englishtester.common.SharedPreferencesUtil;
import com.example.englishtester.common.TitleTextSetter;
import com.example.englishtester.common.TxtReaderAppender;
import com.example.englishtester.common.WordHtmlParser;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TxtReaderActivity extends Activity implements FloatViewService.Callbacks {

    private static final String TAG = TxtReaderActivity.class.getSimpleName();

    public static final String KEY_CONTENT = "TxtReaderActivity_content";

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

    EditText editText1;
    Button clearBtn;
    Button confirmBtn;
    LinearLayout linearLayout2;

    TextView txtView;
    TextView translateView;
    Button translateBtn;
    LinearLayout linearLayout1;

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

        //群組2
        editText1 = (EditText) this.findViewById(R.id.editText1);
        clearBtn = (Button) this.findViewById(R.id.clearBtn);
        confirmBtn = (Button) this.findViewById(R.id.confirmBtn);
        linearLayout2 = (LinearLayout) this.findViewById(R.id.linearLayout2);

        //初始化服務
        initService();

        //群組一
        initTextView();
        initTranslateBtn();
        initTranslateView();

        //群組2
        initGroup2View();

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
        txtView.setMovementMethod(createMovementMethod(this));
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
        translateView.setMovementMethod(createMovementMethod(this));
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
        final List<String> fileInfoList = new ArrayList<String>();
        final List<DropboxUtilV2.DropboxUtilV2_DropboxFile> fileLst = dropboxFileLoadService.listFileV2();
        for (int ii = 0; ii < fileLst.size(); ii++) {
            if (fileLst.get(ii).isFolder()) {
                fileLst.remove(ii);
                ii--;
            }
        }
        for (DropboxUtilV2.DropboxUtilV2_DropboxFile f : fileLst) {
            fileInfoList.add(String.format("%s (%s)", f.getName(),//
                    FileUtilGtu.getSizeDescription(f.getSize())));
        }
        new AlertDialog.Builder(TxtReaderActivity.this)//
                .setTitle("選擇dropbox檔案")//
                .setItems(fileInfoList.toArray(new String[0]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
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
                        }
                    }
                })//
                .show();
    }

    /**
     * 設定現在檔案名稱
     */
    private void setFileName(String newName) {
        if (dto.fileName.length() > 0) {
            dto.fileName.delete(0, dto.fileName.length() - 1);
        }
        dto.fileName.append(newName);
        Log.v(TAG, "current fileName : " + dto.fileName);
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
        doOnoffService(true);
    }


    /**
     * 建立可點擊文件
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
                final TransApi api = new TransApi(appId, securityKey);

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

    private MovementMethod createMovementMethod(Context context) {
        final GestureDetector detector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return true;
            }
        });

        return new ScrollingMovementMethod() {

            @Override
            public boolean canSelectArbitrarily() {
                return true;
            }

            @Override
            public void initialize(TextView widget, Spannable text) {
                Selection.setSelection(text, text.length());
            }

            @Override
            public void onTakeFocus(TextView view, Spannable text, int dir) {
                if ((dir & (View.FOCUS_FORWARD | View.FOCUS_DOWN)) != 0) {
                    if (view.getLayout() == null) {
                        // This shouldn't be null, but do something sensible if
                        // it is.
                        Selection.setSelection(text, text.length());
                    }
                } else {
                    Selection.setSelection(text, text.length());
                }
            }

            @Override
            public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
                // check if event is a single tab
                boolean isClickEvent = detector.onTouchEvent(event);

                // detect span that was clicked
                if (isClickEvent) {
                    int x = (int) event.getX();
                    int y = (int) event.getY();

                    x -= widget.getTotalPaddingLeft();
                    y -= widget.getTotalPaddingTop();

                    x += widget.getScrollX();
                    y += widget.getScrollY();

                    Layout layout = widget.getLayout();
                    int line = layout.getLineForVertical(y);
                    int off = layout.getOffsetForHorizontal(line, x);

                    TxtReaderAppender.WordSpan[] link = buffer.getSpans(off, off, TxtReaderAppender.WordSpan.class);

                    if (link.length != 0) {
                        // execute click only for first clickable span
                        // can be a for each loop to execute every one
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            link[0].onClick(widget);
                        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            Selection.setSelection(buffer, buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]));
                        }
                        return true;
                    }
                }

                // let scroll movement handle the touch
                return super.onTouchEvent(widget, buffer, event);
            }
        };
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
        this.dto.content = content;
        TxtReaderAppender appender = new TxtReaderAppender(
                this, recentTxtMarkService, mService, dto, txtView //
        );
        if (!isHtmlFromWord) {
            txtView.setText(appender.getAppendTxt(content));
        } else {
            txtView.setText(appender.getAppendTxt_HtmlFromWord(content, paddingAdjuster.maxWidth - 10));
        }
        translateView.setText("");
        if (StringUtils.isNotBlank(content)) {
            translateBtn.setVisibility(View.VISIBLE);
        }
        showGroup(1);
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
        editText.setText("txtReaderFile" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss") + ".txt");
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
                @Override
                public void run() {
                    try {
                        if (txtFileGetterCall != null) {
                            txtFileZ.set(txtFileGetterCall.call());
                        }

                        handler.post(new Runnable() {
                            private String fixName(String title) {
                                title = StringUtils.trimToEmpty(title);
                                Pattern ptn = Pattern.compile("(.*?)\\.(?:htm|html|txt|properties)", Pattern.CASE_INSENSITIVE);
                                Matcher mth = ptn.matcher(title);
                                if (mth.find()) {
                                    return mth.group(1);
                                }
                                return title;
                            }

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

                        if (txtFileZ.get().getName().endsWith(".htm") || txtFileZ.get().getName().endsWith(".html")) {
                            WordHtmlParser wordParser = WordHtmlParser.newInstance();
                            final String content = wordParser.getFromFile(txtFileZ.get());
                            String dropboxDir = wordParser.getPicDirForDropbox();

                            if (StringUtils.isNotBlank(dropboxDir)) {
                                File dropboxPicDir = dropboxFileLoadService.downloadHtmlReferencePicDir(dropboxDir);
                                dto.dropboxDir = dropboxPicDir;
                            } else {
                                dto.dropboxDir = null;
                            }

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    setContentText(content, true);
                                    translateView.setText("");
                                }
                            });

                        } else {
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
                                }
                            });

                        }
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    } finally {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                dialog.get().dismiss();
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
        LOAD_CONTENT_FROM_FILE_RANDOM("讀取文件(其他txt檔)", MENU_FIRST++, REQUEST_CODE++, FileFindActivity.class) {
            protected void onActivityResult(TxtReaderActivity activity, Intent intent, Bundle bundle) {
                File file = FileFindActivity.FileFindActivityStarter.getFile(intent);
                activity.setTxtContentFromFile(file, null, null);
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
        private Thread translateThread; //翻譯thread
        private File dropboxDir;

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

        public File getDropboxDir() {
            return dropboxDir;
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
        super.onPause();
    }

    @Override
    public void onDestroy() {
        // Destroy the AdView.
        mAdView.destroy();
        super.onDestroy();
    }
}
