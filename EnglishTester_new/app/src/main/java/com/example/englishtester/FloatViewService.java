package com.example.englishtester;

import android.app.Activity;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.media.Ringtone;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.core.v2.DbxClientV2;
import com.example.englishtester.EnglishwordInfoDAO.EnglishWord;
import com.example.englishtester.common.AppOpenHelper;
import com.example.englishtester.common.ClipboardHelper;
import com.example.englishtester.common.DropboxUtilV2;
import com.example.englishtester.common.EnglishSearchRegexConf;
import com.example.englishtester.common.FileConstantAccessUtil;
import com.example.englishtester.common.FileUtilAndroid;
import com.example.englishtester.common.FloatServiceHolderBroadcastReceiver;
import com.example.englishtester.common.GodToast;
import com.example.englishtester.common.GoogleSearchHandler;
import com.example.englishtester.common.IFloatServiceAidlInterface;
import com.example.englishtester.common.InterstitialAdHelper;
import com.example.englishtester.common.KeyboardHelper;
import com.example.englishtester.common.LockScreenHelper;
import com.example.englishtester.common.Log;
import com.example.englishtester.common.MagnifierPosEnum;
import com.example.englishtester.common.OOMHandler;
import com.example.englishtester.common.PomodoroClockHandler;
import com.example.englishtester.common.ProcessHandler;
import com.example.englishtester.common.ReaderCommonHelper;
import com.example.englishtester.common.RepeatMoveListener;
import com.example.englishtester.common.RingNotificationHelper;
import com.example.englishtester.common.SharedPreferencesUtil;
import com.example.englishtester.common.TextToSpeechComponent;
import com.example.englishtester.common.WindowItemListDialog;
import com.example.englishtester.common.WindowItemListIconDialog;
import com.example.englishtester.common.WindowSingleInputDialog;
import com.example.englishtester.BuildConfig;
import com.example.englishtester.R;
import com.example.englishtester.common.WindowTomatoDialog;

import org.apache.commons.collections4.map.LRUMap;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gtu._work.etc.ChineseToJapanese;
import gtu._work.etc.EnglishTester_Diectory;
import gtu._work.etc.EnglishTester_Diectory.WordInfo;
import gtu._work.etc.EnglishTester_Diectory_Factory;
import gtu.util.StringUtil_;

/**
 * @author gtu001_5F
 */
public class FloatViewService extends Service {

    // 定义浮动窗口布局
    RelativeLayout contentView;
    WindowManager.LayoutParams wmParams;
    // 创建浮动窗口设置布局参数的对象
    WindowManager mWindowManager;

    private static final String TAG = FloatViewService.class.getSimpleName();


    // ----------------------------------------------------------------
    ImageView imageView1;// 放大鏡
    ImageView imageViewSearch;// 查單字
    ImageView imageViewEraser;// 橡皮擦
    ImageView imageViewDictionary;// 線上字典
    ImageView imageViewConfig;// 設定

    TextView refTextView;//智慧財產權連結
    AutoCompleteTextView autoCompleteTextView1;// 單字
    EditText editText1;// 解釋
    TextView textView1;// 音標
    Button button2;// 加入單字
    Button button3;// 修改
    LinearLayout searchLayout;//單字模式
    // ----------------------------------------------------------------
    EditText noteText;//記事本內容
    ImageView imageViewNoteAction;//記事本內容
    ImageView imageViewConfig2;// 設定
    LinearLayout searchLayout2;//記事本模式
    Button redPlusBtn;//加入記事本
    Button redPlusListBtn;//開啟記事本
    Button saveNoteBtn;//儲存記事本
    ImageView imageViewEraser2;//清除記事本
    // ----------------------------------------------------------------
    EnglishwordInfoDAO englishwordInfoDAO = new EnglishwordInfoDAO(this);
    RecentSearchService recentSearchService = new RecentSearchService(this);
    AdCheckShow adCheckShow = new AdCheckShow();
    TextToSpeechComponent speechComponent;
    EnglishTester_Diectory_Factory diectory = new EnglishTester_Diectory_Factory();
    EnglishwordInfoService englishwordInfoService;
    ArrayAdapter<String> autoCompleteAdapter;
    Map<String, EnglishWord> englishMap = new LRUMap<String, EnglishWord>(50);
    ClipboardListenerHandler clipboardListenerHandler;//剪貼簿監聽器
    MagnifierPosHolder magnifierPosHolder = new MagnifierPosHolder(this);
    RedPlusBtnHandler redPlusBtnHandler = new RedPlusBtnHandler(this);
    Handler handler = new Handler();
    ModeHandler modeHandler;
    LockScreenHelper lockScreenHelper = new LockScreenHelper(this, TAG);
    AtomicReference<String> sentance4RecentSearch = new AtomicReference<String>();//查詢例句
    // ----------------------------------------------------------------


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "oncreat");
        createFloatView();
    }

    @Override
    public IBinder onBind(Intent intent) {
        //return mBinder;
        return mBinderNew;
    }

    private void createFloatView() {
        Log.v(TAG, "#### createFloatView ###");

        // 获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        Log.i(TAG, "mWindowManager--->" + mWindowManager);

        // 初始化發音工具
        speechComponent = new TextToSpeechComponent(this);
        englishwordInfoService = new EnglishwordInfoService(this);

        initWmParams();

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        contentView = (RelativeLayout) inflater.inflate(R.layout.activity_float_english_info, null);
        contentView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 如果沒有得到focus則關閉編輯視窗
                int[] left_top = new int[2];
                contentView.getLocationOnScreen(left_top);
                int left = left_top[0];
                int top = left_top[1];
                int right = left + contentView.getMeasuredWidth();
                int buttom = top + contentView.getMeasuredHeight();
//                if (!(event.getX() >= left && event.getX() <= right && //
//                        event.getY() >= top && event.getY() <= buttom)) {
//                    Log.v(TAG, "closeEditPanel!");
//                    doOpenCloseEditPanel(false);
//                }

                // 如果沒有得到focus則關閉編輯視窗
                Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                if (!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                    Log.v(TAG, "closeEditPanel!");
                    doOpenCloseEditPanel(false);
                }
                return false;
            }
        });

        initCreateView(contentView);

        initEnglishDetail();

        initServices();

        mWindowManager.addView(contentView, wmParams);

        //回復 放大鏡位置
        magnifierPosHolder.retoreXY(mWindowManager, wmParams, contentView);

        contentView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        Log.i(TAG, "Width/2--->" + imageView1.getMeasuredWidth() / 2);
        Log.i(TAG, "Height/2--->" + imageView1.getMeasuredHeight() / 2);

        // 设置监听浮动窗口的触摸移动
        imageView1.setOnTouchListener(new RepeatMoveListener(this, new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
                wmParams.x = (int) event.getRawX() - imageView1.getMeasuredWidth() / 2;
                Log.i(TAG, "RawX" + event.getRawX());
                Log.i(TAG, "X" + event.getX());
                // 减25为状态栏的高度
                wmParams.y = (int) event.getRawY() - imageView1.getMeasuredHeight() / 2 - 35;// 25
                Log.i(TAG, "RawY" + event.getRawY());
                Log.i(TAG, "Y" + event.getY());

                mWindowManager.updateViewLayout(contentView, wmParams);

                //紀錄最後位置
                magnifierPosHolder.recordXY(wmParams);
                return false;
            }
        }));

        imageView1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent = new Intent();
                // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // intent.setClass(getApplicationContext(),
                // EnglishwordInfoActivity.class);
                // startActivity(intent);
                doOpenCloseEditPanel(true);
            }
        });

        /**
         * 建立剪貼簿監聽器
         */
        clipboardListenerHandler = new ClipboardListenerHandler();
        clipboardListenerHandler.doStart(true);
    }

    /**
     * 初始化root view 配置
     */
    private void initWmParams() {
        wmParams = new WindowManager.LayoutParams();
        // 设置window type
        // 會出錯 改用右邊這兩個 LayoutParams.TYPE_TOAST or TYPE_APPLICATION_PANEL
        wmParams.type = LayoutParams.TYPE_PHONE; //LayoutParams.TYPE_PHONE;
        // 设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;

        // 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        // wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL |
        // LayoutParams.FLAG_NOT_FOCUSABLE;

        // FLAG_NOT_TOUCH_MODAL：
        // 当窗口可以获得焦点（没有设置 FLAG_NOT_FOCUSALBE
        // 选项）时，仍然将窗口范围之外的点设备事件（鼠标、触摸屏）发送给后面的窗口处理。否则它将独占所有的点设备事件，而不管它们是不是发生在窗口范围内。
        //
        // FLAG_WATCH_OUTSIDE_TOUCH：
        // 如果你设置了FLAG_NOT_TOUCH_MODAL，那么当触屏事件发生在窗口之外事，可以通过设置此标志接收到一个MotionEvent.ACTION_OUTSIDE事件。
        // 注意，你不会收到完整的down/move/up事件，只有第一次down事件时可以收到ACTION_OUTSIDE。

        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        // 调整悬浮窗显示的停靠位置为左侧置顶 (x,y左標起點位置)
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;

        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        MagnifierPosEnum.RIGHT_TOP.apply(mWindowManager, wmParams);

        // 设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
    }

    /**
     * 開啟/關閉輸入框
     */
    private void doOpenCloseEditPanel(boolean isOpen) {
        // 開啟通知
        if (isOpen) {
            ReaderCommonHelper.FloatViewServiceOpenStatusReceiverHelper.sendOpenStatusMessage(isOpen, this);
        }

        // 檢查剪貼簿是否有內容
        if (isOpen) {
            //查詢記事本的單字
            if (modeHandler.isSearchMode()) {
                checkClipboardContentForSearchEnglishId();
            } else if (modeHandler.isNotepadMode()) {
                checkClipboardContentForNoteText();
            }
        }

        //↓↓↓↓↓↓正常邏輯↓↓↓↓↓↓
        // 更新panel layout
        updatePanelLayout(isOpen);
        // 細節控制
        doOpenCloseEditPanel_detial(isOpen);
        //↑↑↑↑↑↑正常邏輯↑↑↑↑↑↑

        if (isOpen == false) {
            //顯示廣告
            adCheckShow.show();
        }

        // 開關通知
        if (!isOpen) {
            ReaderCommonHelper.FloatViewServiceOpenStatusReceiverHelper.sendOpenStatusMessage(isOpen, this);
        }
    }

    /**
     * 更新panel layout
     */
    private void updatePanelLayout(boolean isOpen) {
        if (isOpen) {
            // wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
            wmParams.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
//            searchLayout.setVisibility(View.VISIBLE);
            modeHandler.show(true);
        } else {
            wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//            searchLayout.setVisibility(View.GONE);
            modeHandler.show(false);
        }
        mWindowManager.updateViewLayout(contentView, wmParams);
    }

    /**
     * 開啟/關閉輸入框 : 細節控制
     */
    private void doOpenCloseEditPanel_detial(boolean isOpen) {
        if (isOpen) {
            autoCompleteTextView1.setFocusable(true);
            autoCompleteTextView1.setFocusableInTouchMode(true);
            autoCompleteTextView1.requestFocus();
            // autoCompleteTextView1.setText(null);//開啟時清空

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            Log.v(TAG, "keyboard = " + imm.isActive());
            if (!imm.isActive()) {
                // imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
            imm.showSoftInput(autoCompleteTextView1, InputMethodManager.SHOW_FORCED);
            imm.showSoftInputFromInputMethod(autoCompleteTextView1.getWindowToken(), InputMethodManager.SHOW_FORCED);

            KeyboardHelper.requestFocus(autoCompleteTextView1, this.getApplicationContext());
        } else {
            autoCompleteTextView1.clearFocus();
            // cleanAllInput();
            KeyboardHelper.clearFoucs(autoCompleteTextView1, this.getApplicationContext());
        }
    }

    /**
     * 清空所有輸入文字盒
     */
    private void cleanAllInput() {
        // 觸發按鈕
        imageViewEraser.performClick();
    }

    /**
     * 初始化root view
     */
    private void initCreateView(RelativeLayout contentView) {
        // 放大鏡圖示
        imageView1 = (ImageView) contentView.findViewById(R.id.imageView1);

        // 模式
        searchLayout2 = (LinearLayout) contentView.findViewById(R.id.linearLayout2);
        searchLayout2.setVisibility(View.GONE);
        {
            //記事本
            noteText = (EditText) contentView.findViewById(R.id.noteText);

            //記事本轉發功能
            imageViewNoteAction = (ImageView) contentView.findViewById(R.id.imageViewNoteAction);
            imageViewNoteAction.setOnClickListener(new OnClickListener() {
                private void addItem(String title, Integer icon, List<Map<String, Object>> list) {
                    Map<String, Object> a1 = new HashMap<String, Object>();
                    a1.put("ItemImage", (icon != null ? OOMHandler.new_decode(FloatViewService.this, icon, null) : null));
                    a1.put("ItemTitle", title);
                    list.add(a1);
                }

                @Override
                public void onClick(View v) {
                    WindowItemListIconDialog win = new WindowItemListIconDialog(mWindowManager, FloatViewService.this.getApplicationContext());

                    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                    for (NotepadDialogEnum e : NotepadDialogEnum.values()) {
                        addItem(e.label, e.getIcon(FloatViewService.this), list);
                    }

                    win.showItemListDialog("轉發", list, new OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            for (NotepadDialogEnum e : NotepadDialogEnum.values()) {
                                if (e.ordinal() == position) {
                                    e.process(FloatViewService.this);
                                }
                            }
                        }
                    });
                }
            });

            //加入記事本
            redPlusBtn = (Button) contentView.findViewById(R.id.redPlusBtn);
            redPlusBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        final String saveContent = StringUtils.trimToEmpty(noteText.getText().toString());
                        if (StringUtils.isBlank(saveContent)) {
                            Toast.makeText(FloatViewService.this, "沒有內文無須儲存!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        boolean result = redPlusBtnHandler.addNote(saveContent);
                        Toast.makeText(FloatViewService.this, (result ? "加入成功" : "記事已存在"), Toast.LENGTH_SHORT).show();
                        if (result) {
                            noteText.setText("");
                        }
                    } catch (Exception ex) {
                        Log.e(TAG, "redPlusBtn ERR : " + ex.getMessage(), ex);
                    }
                }
            });

            //開啟記事本
            redPlusListBtn = (Button) contentView.findViewById(R.id.redPlusListBtn);
            redPlusListBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        final List<String> redPlusLst = Arrays.asList(redPlusBtnHandler.getRedPlusBtnArry());
                        WindowItemListDialog win = new WindowItemListDialog(mWindowManager, getApplicationContext());
                        win.setItemTextViewStyle(new WindowItemListDialog.WindowItemListDialog_SettingTextView() {
                            @Override
                            public void apply(TextView text) {
                                text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                                text.setSingleLine(false);
                            }
                        });
                        win.showItemListDialog("記事本內容", redPlusLst, new OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String text = redPlusLst.get(position);
                                noteText.setText(text);
                                doOpenCloseEditPanel(true);
                            }
                        });
                    } catch (Exception ex) {
                        Log.e(TAG, "redPlusBtn ERR : " + ex.getMessage(), ex);
                    }
                }
            });

            //儲存記事本
            saveNoteBtn = (Button) contentView.findViewById(R.id.saveNoteBtn);
            saveNoteBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String[] arry = redPlusBtnHandler.getRedPlusBtnArry();
                        if (arry.length == 0) {
                            Toast.makeText(FloatViewService.this, "無記事內容!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        final StringBuffer saveSb = new StringBuffer();
                        for (int ii = 0; ii < arry.length; ii++) {
                            saveSb.append((ii + 1) + " . " + arry[ii] + "\r\n");
                        }

                        final Context context = FloatViewService.this.getApplicationContext();
                        String fileName = String.format("%s_%s.txt", "notepad", DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd_HHmmss"));
                        final File saveToDir = FileUtilAndroid.getExternalStoragePublicDirectory(null);

                        WindowSingleInputDialog win = new WindowSingleInputDialog(mWindowManager, FloatViewService.this.getApplicationContext());
                        win.showItemListDialog("儲存記事本", "儲存的檔名為:", fileName, new WindowSingleInputDialog.WindowSingleInputDialog_DlgConfirm() {
                            @Override
                            public void onConfirm(String inputStr, View v, WindowSingleInputDialog dlg) {
                                String fileName = inputStr;
                                if (StringUtils.isBlank(fileName)) {
                                    Toast.makeText(context, "請輸入檔名!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                try {
                                    File saveFile = new File(saveToDir, fileName);
                                    FileUtilAndroid.saveToFile(saveFile, saveSb.toString());
//                                    Toast.makeText(context, "儲存成功 : " + saveFile, Toast.LENGTH_SHORT).show();
                                    redPlusBtnHandler.clear();

                                    //上傳至dropbox
                                    redPlusBtnHandler.uploadNotePlusToDropbox(saveFile);
                                } catch (IOException e) {
                                    Toast.makeText(context, "儲存失敗!", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, e.getMessage(), e);
                                } finally {
                                    dlg.dismiss();
                                }
                            }
                        });
                    } catch (Exception ex) {
                        Log.e(TAG, "saveNoteBtn ERR : " + ex.getMessage(), ex);
                    }
                }
            });

            //清除記事本
            imageViewEraser2 = (ImageView) contentView.findViewById(R.id.imageViewEraser2);
            imageViewEraser2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    noteText.setText("");
                }
            });
        }

        // 編輯panel
        searchLayout = (LinearLayout) contentView.findViewById(R.id.linearLayout1);
        searchLayout.setVisibility(View.GONE);
        {
            // 英文單字下拉
            autoCompleteTextView1 = (AutoCompleteTextView) contentView.findViewById(R.id.autoCompleteTextView1);
            autoCompleteTextView1.setTextColor(Color.BLUE);
            autoCompleteTextView1.setThreshold(1);
            autoCompleteTextView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
                    searchEnglishId();
                }
            });
            autoCompleteTextView1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // searchEnglishId();
                }
            });
            autoCompleteTextView1.setOnKeyListener(new View.OnKeyListener() {
                int backKeyPressCount = 0;
                final int MAX_CLICK_COUNT = 1;

                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    Log.v(TAG, "keyCode = " + keyCode);
                    switch (event.getAction()) {
                        case KeyEvent.ACTION_DOWN:
                            Log.v(TAG, "down");
                            break;
                        case KeyEvent.ACTION_MULTIPLE:
                            Log.v(TAG, "multiple");
                            break;
                        case KeyEvent.ACTION_UP:
                            Log.v(TAG, "up");
                            // 按下enter
                            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                                String strVal = autoCompleteTextView1.getText().toString();
                                strVal = strVal.replaceAll("[\\r\\n]", "");
                                autoCompleteTextView1.setText(strVal);
                                searchEnglishId();
                                return true;
                            }
                            // 回上頁 -有字先清字
                            if (keyCode == KeyEvent.KEYCODE_BACK && //
                                    StringUtils.isNotBlank(autoCompleteTextView1.getText().toString())) {
                                Log.v(TAG, "key BACK - will clean autoCompleteTextView1!");
                                backKeyPressCount++;
                                if (backKeyPressCount >= MAX_CLICK_COUNT) {
                                    backKeyPressCount = 0;
                                    cleanAllInput();
                                }
                                return true;
                            }
                            // 回上頁 -沒字關panel
                            if (keyCode == KeyEvent.KEYCODE_BACK && //
                                    StringUtils.isBlank(autoCompleteTextView1.getText().toString())) {
                                Log.v(TAG, "key BACK - will close panel!");
                                backKeyPressCount++;
                                if (backKeyPressCount >= MAX_CLICK_COUNT) {
                                    backKeyPressCount = 0;
                                    doOpenCloseEditPanel(false);
                                }
                                return true;
                            }
                            // 迴車鍵
                            if (keyCode == KeyEvent.KEYCODE_DEL && //
                                    StringUtils.isBlank(autoCompleteTextView1.getText().toString())) {
                                // 沒字就reset
                                imageViewEraser.performClick();
                            }
                    }
                    return false;
                }
            });

            setEditTextAllSelection(autoCompleteTextView1);

            // 音標
            textView1 = (TextView) contentView.findViewById(R.id.textView1);
            textView1.setVisibility(View.GONE);
            textView1.setTextColor(Color.DKGRAY);
            textView1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    speechComponent.speak(autoCompleteTextView1.getText().toString());
                }
            });

            // 英文解釋文字盒
            editText1 = (EditText) contentView.findViewById(R.id.editText1);
            editText1.setVisibility(View.GONE);
            editText1.setTextColor(Color.BLACK);
            editText1.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    englishDescriptionSave();
                    return false;
                }
            });
            editText1.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
                    englishDescriptionSave();
                    return false;
                }
            });

            // 加入單字
            button2 = (Button) contentView.findViewById(R.id.button2);
            button2.setVisibility(View.GONE);
            button2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (button2.getTag() != null) {
                        WordInfo newWord = (WordInfo) button2.getTag();
                        if (StringUtils.isBlank(newWord.getEnglishId()) || StringUtils.isBlank(editText1.getText().toString())) {
                            Toast.makeText(getApplicationContext(), "單字資料不正確!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String englishIdTrim = StringUtils.trimToEmpty(newWord.getEnglishId());
                        EnglishWord addWord = new EnglishWord();
                        EnglishWord _oldOne = englishwordInfoDAO.queryOneWord(englishIdTrim);
                        boolean doUpdate = false;
                        if (_oldOne != null) {
                            addWord = _oldOne;
                            doUpdate = true;
                        }
                        addWord.englishId = englishIdTrim;
                        addWord.englishDesc = StringUtils.trimToEmpty(editText1.getText().toString());
                        addWord.pronounce = newWord.getPronounce();
                        if (!doUpdate) {
                            englishwordInfoDAO.insertWord(addWord);
                        } else {
                            englishwordInfoDAO.updateWord(addWord);
                        }
                        initEnglishDetail();
                        Toast.makeText(getApplicationContext(), "您新增了單字 :" + addWord.englishId, Toast.LENGTH_LONG).show();
                        setEnglishInfo(null, null, null, null);
                    }
                }
            });

            // 修改
            button3 = (Button) contentView.findViewById(R.id.button3);
            button3.setVisibility(View.GONE);
            button3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String englishId = StringUtils.trimToEmpty(autoCompleteTextView1.getText().toString());
                    String description = StringUtils.trimToEmpty(editText1.getText().toString());
                    if (StringUtils.isNotBlank(englishId) && StringUtils.isNotBlank(description)) {
                        EnglishWord englishWord = englishwordInfoDAO.queryOneWord(englishId);
                        boolean isInsert = false;
                        if (englishWord == null) {
                            englishWord = new EnglishWord();
                            englishWord.englishId = englishId;
                            englishWord.insertDate = System.currentTimeMillis();
                            EnglishTester_Diectory.WordInfo w1 = diectory.parseToWordInfo(englishId, FloatViewService.this, new Handler());
                            englishWord.pronounce = w1.getPronounce();
                            isInsert = true;
                        }
                        englishWord.lastbrowerDate = System.currentTimeMillis();
                        englishWord.englishDesc = description;
                        if (isInsert) {
                            englishwordInfoDAO.insertWord(englishWord);
                        } else {
                            englishwordInfoDAO.updateWord(englishWord);
                        }
                        englishMap.put(englishId, englishWord);
                        Toast.makeText(getApplicationContext(), "修改了解釋!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // 查詢放大鏡
            imageViewSearch = (ImageView) contentView.findViewById(R.id.imageViewSearch);
            imageViewSearch.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchEnglishId();
                }
            });

            // 橡皮擦
            imageViewEraser = (ImageView) contentView.findViewById(R.id.imageViewEraser);
            imageViewEraser.setVisibility(View.GONE);
            imageViewEraser.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    autoCompleteTextView1.setText("");
                    editText1.setText("");
                    textView1.setText("");
                    editText1.setVisibility(View.GONE);
                    textView1.setVisibility(View.GONE);
                    imageViewEraser.setVisibility(View.GONE);
                    button2.setVisibility(View.GONE);
                    button3.setVisibility(View.GONE);
                    imageViewDictionary.setVisibility(View.GONE);
                    refTextView.setVisibility(View.GONE);
                    setEnglishInfo(null, null, null, null);
                }
            });

            // 線上字典
            imageViewDictionary = (ImageView) contentView.findViewById(R.id.imageViewDictionary);
            imageViewDictionary.setVisibility(View.GONE);
            imageViewDictionary.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String englishId = autoCompleteTextView1.getText().toString();
                    if (StringUtils.isBlank(englishId)) {
                        Toast.makeText(getApplicationContext(), "未輸入單字!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent = new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setClass(FloatViewService.this, SearchDictionaryActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(SearchDictionaryActivity.INTENT_KEY, englishId);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            // 設定
            imageViewConfig = (ImageView) contentView.findViewById(R.id.imageViewConfig);
            imageViewConfig2 = (ImageView) contentView.findViewById(R.id.imageViewConfig2);
            // imageViewHistory.setVisibility(View.GONE);
            OnClickListener imageViewConfig_OnClickListener = new OnClickListener() {
                private void addItem(String title, Integer icon, List<Map<String, Object>> list) {
                    Map<String, Object> a1 = new HashMap<String, Object>();
                    a1.put("ItemImage", (icon != null ? OOMHandler.new_decode(FloatViewService.this, icon, null) : null));
                    a1.put("ItemTitle", title);
                    list.add(a1);
                }

                @Override
                public void onClick(View v) {
                    WindowItemListIconDialog win = new WindowItemListIconDialog(mWindowManager, FloatViewService.this.getApplicationContext());

                    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                    for (ConfigDialogEnum e : ConfigDialogEnum.values()) {
                        if (e.isDebug && BuildConfig.DEBUG) {
                            addItem(e.label, e.getIcon(FloatViewService.this), list);
                        } else if (!e.isDebug) {
                            addItem(e.label, e.getIcon(FloatViewService.this), list);
                        }
                    }

                    win.showItemListDialog("功能設定", list, new OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            for (ConfigDialogEnum e : ConfigDialogEnum.values()) {
                                if (e.ordinal() == position) {
                                    e.process(FloatViewService.this);
                                }
                            }
                        }
                    });
                }
            };
            imageViewConfig.setOnClickListener(imageViewConfig_OnClickListener);
            imageViewConfig2.setOnClickListener(imageViewConfig_OnClickListener);

            //智慧財產權連結
            refTextView = (TextView) contentView.findViewById(R.id.refTextView);
            refTextView.setVisibility(View.GONE);
        }
    }

    private void initServices() {
        modeHandler = new ModeHandler();
    }

    private enum ConfigDialogEnum {
        SEARCH_HISTORY("查詢歷史", R.drawable.icon_history, false) {
            @Override
            void process(FloatViewService self) {
                self.recentSearchHistory();
            }
        },//
        OPEN_PROGRAM("開啟主程式", R.drawable.janna, false) {
            @Override
            void process(FloatViewService self) {
                self.openMainProgram();
            }
        },//
        ICON_MOVE("放大鏡移動", R.drawable.icon_mouse_move, false) {
            @Override
            void process(FloatViewService self) {
                self.magnifierAlert();
            }
        },//
        LISTENER_CLIPBOARD("監聽記事本", null, false) {
            public Integer getIcon(FloatViewService self) {
                boolean state = self.clipboardListenerHandler.isCurrentState();
                return state ? R.drawable.switch_on_icon : R.drawable.switch_off_icon;
            }

            @Override
            void process(FloatViewService self) {
                boolean state = !self.clipboardListenerHandler.isCurrentState();
                self.clipboardListenerHandler.doStart(state);
                Toast.makeText(self, "監聽" + (state ? "on" : "off"), Toast.LENGTH_SHORT).show();
            }
        },//
        MODE_CHANGE("模式切換", null, false) {
            public Integer getIcon(FloatViewService self) {
                return self.modeHandler.getIcon(self);
            }

            @Override
            void process(FloatViewService self) {
                self.modeHandler.changeMode(null, false);
            }
        },
        LOCK_SCREEN("螢幕鎖定", R.drawable.icon_lock_screen, false) {
            @Override
            void process(FloatViewService self) {
                self.lockScreenHelper.toggle();
            }
        },//
        POMODORO_CLOCK("番茄鐘", R.drawable.icon_pomodoro_clock, false) {
            @Override
            void process(FloatViewService self) {
                PomodoroClockHandler p = new PomodoroClockHandler(self);
                if (!p.isStart()) {
                    p.start();
                } else {
                    p.cancel();
                }
            }
        },//
        EXIT_PROGRAM("關閉懸浮字典", R.drawable.icon_close_app, false) {
            @Override
            void process(FloatViewService self) {
                self.stopThisService();
            }
        },//
        ADMOB_TEST("TEST 測試廣告", R.drawable.icon_18_adults_only, true) {
            @Override
            void process(FloatViewService self) {
                self.adCheckShow.count = 21;
            }
        },//
        ;

        final String label;
        final Integer iconVal;
        final boolean isDebug;

        ConfigDialogEnum(String label, Integer iconVal, boolean isDebug) {
            this.label = label;
            this.iconVal = iconVal;
            this.isDebug = isDebug;
        }

        public Integer getIcon(FloatViewService self) {
            return this.iconVal;
        }

        abstract void process(FloatViewService self);
    }

    private enum NotepadDialogEnum {
        LINE_APP("開啟Line", R.drawable.line_app_me) {//

            @Override
            void process(FloatViewService self) {
                String inputText = StringUtils.trimToEmpty(self.noteText.getText().toString());
                ClipboardHelper.copyToClipboard(self.getApplicationContext(), inputText);
                AppOpenHelper.openApp(self.getApplicationContext(), "jp.naver.line.android", null);
            }
        },//
        LINE_SEND("發送Line", R.drawable.line_app_me) {//

            @Override
            void process(FloatViewService self) {
                String inputText = StringUtils.trimToEmpty(self.noteText.getText().toString());
                Log.lineFix(TAG, inputText);
            }
        },//
        MY_APP("開啟MyApp", R.drawable.qr_code_icon) {//

            @Override
            void process(FloatViewService self) {
                String inputText = StringUtils.trimToEmpty(self.noteText.getText().toString());
                ClipboardHelper.copyToClipboard(self.getApplicationContext(), inputText);
                Intent intent1 = new Intent();
                intent1.putExtra("youtube", inputText);
                AppOpenHelper.openApp(self.getApplicationContext(), "com.example.gtu001.qrcodemaker", intent1);
            }
        },//
        SEARCH_IN_BROWSER("以Google搜尋", R.drawable.icon_chrome) {//

            @Override
            void process(FloatViewService self) {
                GoogleSearchHandler.search(self, self.noteText.getText().toString());
            }
        },//
        OPEN_BROWSER("以瀏覽器開啟", R.drawable.icon_chrome) {
            @Override
            void process(FloatViewService self) {
                GoogleSearchHandler.openWithBrowser(self.noteText.getText().toString(), self);
            }
        },//
        OPEN_TXT_READER("以閱讀器開啟[beta]", R.drawable.icon_book_stack) {
            @Override
            void process(FloatViewService self) {
                try {
                    String inputText = StringUtils.trimToEmpty(self.noteText.getText().toString());
                    if (!inputText.matches("^https?\\:\\/.*")) {
                        inputText = "http://" + inputText;
                    }
                    Intent intent = new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setClass(self, TxtReaderActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(TxtReaderActivity.LOAD_URL_CONTENT, inputText);
                    intent.putExtras(bundle);
                    self.startActivity(intent);
                } catch (Exception ex) {
                    Log.e(TAG, this.name() + " ERR : " + ex.getMessage(), ex);
                    Toast.makeText(self, "無法開啟網頁!", Toast.LENGTH_SHORT).show();
                }
            }
        },//
        TRANSLATE_TO_JAPAN("轉換為日文", R.drawable.icon_book_stack) {
            @Override
            void process(FloatViewService self) {
                try {
                    final String chineseWord = StringUtils.trimToEmpty(self.noteText.getText().toString());
                    String japanWord = DropboxEnglishService.getRunOnUiThread(new Callable<String>() {
                        @Override
                        public String call() throws Exception {
                            try {
                                return ChineseToJapanese.parseToJapanWord(chineseWord);
                            } catch (Exception e) {
                                Log.e(TAG, "TRANSLATE_TO_JAPAN ERR : " + e.getMessage(), e);
                            } finally {
                            }
                            return "";
                        }
                    }, -1);
                    self.noteText.setText(japanWord);
                } catch (Exception ex) {
                    Log.e(TAG, this.name() + " ERR : " + ex.getMessage(), ex);
                    Toast.makeText(self, "無法轉換為日文!", Toast.LENGTH_SHORT).show();
                }
            }
        },//
        ;

        final String label;
        final Integer iconVal;

        NotepadDialogEnum(String label, Integer iconVal) {
            this.label = label;
            this.iconVal = iconVal;
        }

        public Integer getIcon(FloatViewService self) {
            return this.iconVal;
        }

        abstract void process(FloatViewService self);
    }

    private class ModeHandler {
        AtomicInteger modeholder = new AtomicInteger(0);
        int[] modeArry = new int[]{R.drawable.new_search_icon, R.drawable.icon_sticky_note};
        String[] modeArry2 = new String[]{"英文字典", "記事本"};
        ViewGroup[] modeArry3 = new ViewGroup[]{searchLayout, searchLayout2};

        public boolean isSearchMode() {
            return modeholder.get() == 0;
        }

        public boolean isNotepadMode() {
            return modeholder.get() == 1;
        }

        public boolean isSearchModeAndHidden() {
            if (isSearchMode() && searchLayout.getVisibility() == View.GONE) {
                return true;
            }
            return false;
        }

        public boolean isNotepadModeAndHidden() {
            if (isNotepadMode() && searchLayout2.getVisibility() == View.GONE) {
                return true;
            }
            return false;
        }

        public boolean isShowing() {
            for (int ii = 0; ii < modeArry3.length; ii++) {
                if (ii == modeholder.get()) {
                    return modeArry3[ii].getVisibility() == View.VISIBLE;
                }
            }
            return false;
        }

        public void show(boolean show) {
            for (int ii = 0; ii < modeArry3.length; ii++) {
                if (ii == modeholder.get() && show) {
                    modeArry3[ii].setVisibility(View.VISIBLE);
                } else {
                    modeArry3[ii].setVisibility(View.GONE);
                }
            }
        }

        public ModeHandler() {
            changeMode(0, true);
        }

        public int getIcon(FloatViewService self) {
            return modeArry[modeholder.get()];
        }

        public void changeMode(Integer useIndicateVal, boolean isInit) {
            int newVal = 0;
            if (useIndicateVal != null) {
                newVal = useIndicateVal;
            } else {
                newVal = modeholder.get() + 1;
                if (newVal >= modeArry2.length) {
                    newVal = 0;
                }
            }

            modeholder.set(newVal);
            imageView1.setImageResource(modeArry[modeholder.get()]);

            //如果切回查英文模式清掉剪貼簿
            if (this.isSearchMode()) {
                ClipboardHelper.copyToClipboard(FloatViewService.this.getApplicationContext(), "");
            }

            if (clipboardListenerHandler != null) {
                if (modeholder.get() == 0) {
                    clipboardListenerHandler.doStart(true);
                } else {
                    clipboardListenerHandler.doStart(false);
                }
            }

            if (!isInit) {
                Toast.makeText(getApplicationContext(), modeArry2[modeholder.get()] + "模式", Toast.LENGTH_SHORT).show();
            }

            show(false);
        }

        public void openUseNoteMode() {
            changeMode(1, false);
        }
    }

    /**
     * 設定focus時全選
     */
    public static void setEditTextAllSelection(EditText editText) {
        String str = editText.getText().toString();
        if (str != null && str.length() > 0) {
            editText.setSelection(0, str.length() - 1);
        }
        editText.setSelectAllOnFocus(true);
    }

    /**
     * 若英文解釋與預設的資料有不同, 顯示修該解釋儲存按鈕
     */
    private void englishDescriptionSave() {
        Log.v(TAG, "editText1 : " + editText1.getText().toString());
        Object val = editText1.getTag();
        if (val != null) {
            String description = String.valueOf(val);
            String description2 = editText1.getText().toString();
            if (!StringUtils.equals(description, description2)) {
                button3.setVisibility(View.VISIBLE);
            } else {
                button3.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 設定英文解釋,音標
     */
    private void setEnglishInfo(String englishId, String englishDesc, String prounce, WordInfo newWord) {
        // 單字為空
        if (StringUtils.isNotBlank(englishId)) {
            imageViewDictionary.setVisibility(View.VISIBLE);
        } else {
            imageViewDictionary.setVisibility(View.GONE);
        }
        // 設定英文解釋輸入框狀態
        if (StringUtils.isBlank(englishDesc)) {
            editText1.setText("");
            editText1.setVisibility(View.GONE);
            editText1.setTag("");
        } else {
            englishDesc = StringUtils.trimToEmpty(englishDesc);
            editText1.setText(englishDesc);
            editText1.setVisibility(View.VISIBLE);
            editText1.setTag(englishDesc);
        }
        // 設定音標
        if (StringUtils.isBlank(prounce)) {
            textView1.setText("");
            textView1.setVisibility(View.GONE);
        } else {
            textView1.setText("/" + prounce + "/");
            textView1.setVisibility(View.VISIBLE);
        }
        // 設定新增單字按鈕狀態
        if (newWord == null || StringUtils.isBlank(newWord.getMeaning())) {
            button2.setVisibility(View.GONE);
            button2.setTag(null);
        } else {
            button2.setVisibility(View.VISIBLE);
            button2.setTag(newWord);
        }
        // 設定修改解釋按鈕隱藏
        button3.setVisibility(View.GONE);

        // 確認橡皮擦是否顯示
        if (StringUtils.isNotBlank(englishId)) {
            imageViewEraser.setVisibility(View.VISIBLE);
        } else {
            imageViewEraser.setVisibility(View.GONE);
        }

        //設定智慧財產權參考
        this.setReferenceLink(englishId, englishDesc);
    }

    /**
     * 設定參照來源(智慧財產權)
     */
    private void setReferenceLink(String englishId, String englishDesc) {
        if (StringUtils.isNotBlank(englishId) && StringUtils.isNotBlank(englishDesc)) {
            refTextView.setText(
                    Html.fromHtml(
                            "<a href=\"http://cdict.net/?q=" + englishId + "\" >Ref.</a> "));
            refTextView.setMovementMethod(LinkMovementMethod.getInstance());
//            refTextView.setVisibility(View.VISIBLE);取消
            refTextView.setVisibility(View.GONE);
        } else {
            refTextView.setVisibility(View.GONE);
        }
    }

    /**
     * 搜尋要查詢的單字
     */
    private void searchEnglishId() {
        autoCompleteTextView1.dismissDropDown();// 關閉下拉
        final String englishId = EnglishwordInfoActivity.fixSearchWord(autoCompleteTextView1.getText().toString());
        final String sentance = sentance4RecentSearch.getAndSet("");
        EnglishWord currentWord = englishwordInfoDAO.queryOneWord(englishId);
        if (englishMap.containsKey(englishId)) {
            EnglishWord english = englishMap.get(englishId);
            setEnglishInfo(englishId, english.englishDesc, english.pronounce, null);
            recentSearchService.recordRecentSearch(englishId, sentance);
            writeSearchWordToProperties(englishId, null);
        } else if (currentWord != null) {
            EnglishWord english = currentWord;
            setEnglishInfo(englishId, english.englishDesc, english.pronounce, null);
            recentSearchService.recordRecentSearch(englishId, sentance);
            writeSearchWordToProperties(englishId, null);
        } else if (StringUtils.isNotBlank(englishId)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final WordInfo newWord = diectory.parseToWordInfo(englishId, FloatViewService.this, handler);

                    //若有解釋內容才加進cache
                    EnglishWord fixEng = transforToEnglishWord(newWord);
                    if (StringUtils.isNotBlank(fixEng.englishDesc)) {
                        englishMap.put(englishId, fixEng);
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            setEnglishInfo(englishId, newWord.getMeaning(), newWord.getPronounce(), newWord);

                            //以查到正確解釋要打開
                            if (!modeHandler.isShowing()) {
                                doOpenCloseEditPanel(true);
                            }
                        }
                    });

                    recentSearchService.recordRecentSearch(englishId, sentance);
                    writeSearchWordToProperties(englishId, newWord);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "你查了新字 : " + englishId, Toast.LENGTH_SHORT).show();
                        }
                    });

                    // 判斷是否需要找相似
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            setSimilarWordIfNeed(englishId, newWord.getMeaning());
                        }
                    });
                }
            }).start();
        }
    }


    /**
     * 轉換 WordInfo to EnglishWord
     */
    private EnglishWord transforToEnglishWord(WordInfo eng) {
        EnglishWord blank = new EnglishWord();
        blank.englishId = eng.getEnglishId();
        blank.englishDesc = eng.getMeaning();
        blank.pronounce = eng.getPronounce();
        return blank;
    }

    /**
     * 設定相似單字
     */
    private boolean setSimilarWordIfNeed(String englishId, String englishDesc) {
        englishDesc = StringUtils.trimToEmpty(englishDesc);
        if (StringUtil_.hasChinese(englishDesc)) {
            return false;
        }

        final List<EnglishWord> simList = englishwordInfoService.queryForSimilar(englishId, 5);
        List<String> simList2 = new ArrayList<String>();
        for (int ii = 0; ii < simList.size(); ii++) {
            EnglishWord eng = simList.get(ii);
            String description = ReciteMainActivity.formatChangeLine(eng.englishId, eng.englishDesc);
            simList2.add(eng.englishId + " " + description);
        }

        if (simList.isEmpty()) {
            Toast.makeText(getApplicationContext(), "查無單字!", Toast.LENGTH_SHORT).show();
            return false;
        }

        new WindowItemListDialog(mWindowManager, getApplicationContext()).showItemListDialog("您也許是要找", simList2, new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EnglishWord engWord = simList.get(position);
                autoCompleteTextView1.setText(engWord.englishId);
                searchEnglishId();
            }
        });
        return true;
    }

    /**
     * 寫入查詢單字到歷史紀錄
     */
    private void writeSearchWordToProperties(String englishId, WordInfo newWord) {
        String englishDesc = null;
        if (newWord != null) {
            englishDesc = newWord.getMeaning();
        } else {
            englishDesc = editText1.getText().toString();
        }
        Properties prop = new Properties();
        try {
            prop.load(FileConstantAccessUtil.getInputStream(this, Constant.SEARCHWORD_FILE, true));
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage(), ex);
        }
        try {
            prop.setProperty(englishId, englishDesc);
            prop.store(FileConstantAccessUtil.getOutputStream(this, Constant.SEARCHWORD_FILE), "新查的單字");
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage(), ex);
        }
    }

    /**
     * 初始化單字資料庫
     */
    private void initEnglishDetail() {
        List<String> englishList = new ArrayList<String>();
        for (EnglishWord eng : englishwordInfoDAO.queryAll(false)) {
            englishList.add(eng.englishId);
        }
        Collections.sort(englishList);
        String[] engArray = englishList.toArray(new String[0]);
        autoCompleteAdapter = new ArrayAdapter<String>(this, R.layout.subview_dropdown_simple, android.R.id.text1, engArray);//android.R.layout.simple_spinner_dropdown_item simple_dropdown_item_1line
        if (autoCompleteTextView1 != null) {
            autoCompleteTextView1.setAdapter(autoCompleteAdapter);
        }
    }

    /**
     * 移動放大鏡
     */
    private void magnifierAlert() {
        final List<Pair<String, MagnifierPosEnum>> arryList = new ArrayList<Pair<String, MagnifierPosEnum>>();
        arryList.add(ImmutablePair.of("左上", MagnifierPosEnum.LEFT_TOP));
        arryList.add(ImmutablePair.of("右上", MagnifierPosEnum.RIGHT_TOP));
        arryList.add(ImmutablePair.of("左下", MagnifierPosEnum.LEFT_BUTTOM));
        arryList.add(ImmutablePair.of("右下", MagnifierPosEnum.RIGHT_BUTTOM));
        List<String> keysArry = new ArrayList<String>();
        for (Pair<String, MagnifierPosEnum> p : arryList) {
            keysArry.add(p.getKey());
        }
        WindowItemListDialog win = new WindowItemListDialog(mWindowManager, getApplicationContext());
        win.showItemListDialog("設定放大鏡位置", keysArry, new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                arryList.get(position).getValue().apply(mWindowManager, wmParams);
                mWindowManager.updateViewLayout(contentView, wmParams);
            }
        });
    }

    /**
     * 查看查詢歷史紀錄
     */
    private void recentSearchHistory() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(FloatViewService.this, ShowWordListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ShowWordListActivity.ShowWordListActivity_DTO, null);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 開啟主程式
     */
    private void openMainProgram() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(FloatViewService.this, MainActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 從剪貼簿閱讀
     */
    private void openTxtReaderProgram() {
        String content = ClipboardHelper.copyFromClipboard(getApplication());
        if (StringUtils.isNotBlank(content)) {
            ClipboardHelper.copyToClipboard(getApplication(), "");
        } else {
//            Toast.makeText(getApplicationContext(), "剪貼簿是空的", Toast.LENGTH_SHORT).show();
            Log.v(TAG, "剪貼簿是空的");
            return;
        }
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(FloatViewService.this, TxtReaderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(TxtReaderActivity.KEY_CONTENT, content);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 檢查剪貼簿是否有內容(若有內容貼到英文單字視窗)
     */
    private void checkClipboardContentForSearchEnglishId() {
        String text = ClipboardHelper.copyFromClipboard(getApplication());
        Pattern ptn = Pattern.compile(EnglishSearchRegexConf.getSearchRegex(true, false));
        if (StringUtils.isNotBlank(text)) {
            Matcher mth = ptn.matcher(text);
            StringBuilder sb = new StringBuilder();
            while (mth.find()) {
                sb.append(mth.group());
            }
            text = sb.toString();
            if (StringUtils.isNotBlank(text)) {
                imageViewEraser.performClick();
                autoCompleteTextView1.setText(text);
                ClipboardHelper.copyToClipboard(getApplication(), "");
                searchEnglishId();
            }
        }
    }

    /**
     * 檢查剪貼簿是否有內容(若有內容貼到記事本視窗)
     */
    private void checkClipboardContentForNoteText() {
        String text = ClipboardHelper.copyFromClipboard(getApplication());
        try {
            text = StringUtils.defaultString(text);
            text = text.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
            text = text.replaceAll("\\+", "%2B");
            text = URLDecoder.decode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        text = StringUtils.trimToEmpty(text);
        String currentText = StringUtils.trimToEmpty(noteText.getText().toString());
        if (StringUtils.isNotBlank(text) && !currentText.contains(text)) {
            if (StringUtils.isNotBlank(currentText)) {
                noteText.setText(currentText + "\r\n" + text);
            } else {
                noteText.setText(text);
            }
        }
    }

    /**
     * 建立剪貼簿監聽器
     */
    private class ClipboardListenerHandler implements ClipboardManager.OnPrimaryClipChangedListener {
        final ClipboardManager clipboard;

        boolean currentState = false;

        private ClipboardListenerHandler() {
            clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        }

        public boolean isCurrentState() {
            return currentState;
        }

        public void doStart(boolean start) {
            currentState = start;
            if (start) {
                clipboard.addPrimaryClipChangedListener(this);
            } else {
                clipboard.removePrimaryClipChangedListener(this);
            }
        }

        /**
         * 是文章
         */
        private boolean isMoreThanSentance(String text) {
            Pattern ptn = Pattern.compile(EnglishSearchRegexConf.getSearchRegex(true, true));
            if (StringUtils.isNotBlank(text)) {
                Matcher mth = ptn.matcher(text);
                StringBuilder sb = new StringBuilder();
                int count = 0;
                while (mth.find()) {
                    sb.append(mth.group());
                    count++;
                }
                if (count > 5) {
                    Log.v(TAG, "超過五個字判斷為文章 : " + sb.toString());
                    return true;
                }
            }
            return false;
        }

        private void pasteToClipboard(CharSequence label, CharSequence content) {
            doStart(false);
            ClipData clip = ClipData.newPlainText(label, content);
            clipboard.setPrimaryClip(clip);
            doStart(true);
        }

        private String getPrimaryClipAsString() {
            try {
                ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                return item.getText().toString();
            } catch (Exception ex) {
                Log.e(TAG, "getPrimaryClipAsString err :" + ex.getMessage(), ex);
            }
            return "";
        }

        private String getClipboardText() {
            String pasteData = "";
            if (clipboard.hasPrimaryClip()) {
                if (clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML)) {
                    Log.v(TAG, "clipboard hasMimeType = MIMETYPE_TEXT_HTML " + clipboard.getPrimaryClip().getItemCount());
                } else if (clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_INTENT)) {
                    Log.v(TAG, "clipboard hasMimeType = MIMETYPE_TEXT_INTENT " + clipboard.getPrimaryClip().getItemCount());
                    Toast.makeText(FloatViewService.this, "MIMETYPE_TEXT_INTENT", Toast.LENGTH_SHORT).show();
                } else if (clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    Log.v(TAG, "clipboard hasMimeType = MIMETYPE_TEXT_PLAIN " + clipboard.getPrimaryClip().getItemCount());
                } else if (clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_URILIST)) {
                    Log.v(TAG, "clipboard hasMimeType = MIMETYPE_TEXT_URILIST " + clipboard.getPrimaryClip().getItemCount());
                    Toast.makeText(FloatViewService.this, "MIMETYPE_TEXT_URILIST", Toast.LENGTH_SHORT).show();
                }
                pasteData = getPrimaryClipAsString();
            }
            if (StringUtils.isNotBlank(pasteData)) {
                return pasteData;
            } else {
                return clipboard.getText().toString();
            }
        }

        private String specialTxtReaderEscape(String string) {
            Pattern ptn = Pattern.compile("^value\\:(.*)", Pattern.DOTALL | Pattern.MULTILINE);
            Matcher mth = ptn.matcher(string);
            if (mth.find()) {
                return mth.group(1);
            } else {
            }
            return string;
        }

        private boolean isURL(String text) {
            Pattern ptn = Pattern.compile("^http[s]?\\:[\\/]+.*$");
            Matcher mth = ptn.matcher(StringUtils.trimToEmpty(text));
            if (mth.find()) {
                return true;
            }
            return false;
        }

        private boolean isNumbers(String text) {
            Pattern ptn = Pattern.compile("^[\\d\\.\\-]+$");
            Matcher mth = ptn.matcher(text);
            return mth.find();
        }

        private boolean isEmail(String text) {
            Pattern ptn = Pattern.compile("^[\\w\\-\\_]+\\@[\\w\\-\\_]+\\.[\\w\\-\\_]+");
            Matcher mth = ptn.matcher(text);
            return mth.find();
        }


        @Override
        public void onPrimaryClipChanged() {
            Log.v(TAG, "### onPrimaryClipChanged");
            String text = getClipboardText();
            Log.v(TAG, "### onPrimaryClipChanged text = " + text);
            if (StringUtils.isNotBlank(text)) {
                if (isURL(text)) {
                    //不使用剪貼簿設回原值
                    modeHandler.openUseNoteMode();
                    pasteToClipboard("", text);
                    return;
                }

                if (StringUtil_.hasChinese(text)) {
                    //不使用剪貼簿設回原值
                    modeHandler.openUseNoteMode();
                    pasteToClipboard("", text);
                    return;
                }

                if (isNumbers(text) || isEmail(text)) {
                    //不使用剪貼簿設回原值
                    modeHandler.openUseNoteMode();
                    pasteToClipboard("", text);
                    return;
                }

                //如果來自txtReader or epub 需要特殊 escape
                String escapeText = specialTxtReaderEscape(text);
                if (!StringUtils.equals(escapeText, text)) {
                    pasteToClipboard("", escapeText);
                }

                if (modeHandler.isSearchModeAndHidden()) {
                    if (isMoreThanSentance(text)) {
                        openTxtReaderProgram();//翻譯本文
                    } else if (!StringUtil_.hasChinese(text)) {
                        doOpenCloseEditPanel(true);//查詢單字
                    }
                }

                if (modeHandler.isNotepadModeAndHidden()) {
                    doOpenCloseEditPanel(true);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //停掉剪貼簿監聽
        clipboardListenerHandler.doStart(false);
        if (contentView != null) {
            // 移除悬浮窗口
            mWindowManager.removeViewImmediate(contentView);
        }
    }

    /**
     * 停掉服務
     */
    public void stopThisService() {
        Log.v(TAG, "#### stopThisService");
        stopSelf();
        //強制重開關閉
        FloatServiceHolderBroadcastReceiver.setFloatViewServiceEnable(false);

        ProcessHandler.killProcessByPackage2(this, this.getPackageName(), false);
        Log.v(TAG, "# stopThisService !");
    }

    //啟動通知欄
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        FloatViewAssistService.onStartCommandForOrignService(intent, flags, startId, this);
        return START_STICKY;
    }

    //監測螢幕翻轉
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        RecaculateXyHelper helper = new RecaculateXyHelper();
        float[] position = RepeatMoveListener.POSITION.get();
        float x = -1;
        float y = -1;
        boolean lastMotionIsLandscape = false;

        if (position == null) {
            x = wmParams.x + imageView1.getMeasuredWidth() / 2;
            y = wmParams.y + imageView1.getMeasuredHeight() / 2 - 35;
        } else {
            x = position[0];
            y = position[1];
            lastMotionIsLandscape = position[2] == 1f;
        }

        boolean isToOrientationLandscape = false;
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            isToOrientationLandscape = true;
        }

        float[] newXy = helper.parseFromTo(x, y, lastMotionIsLandscape, isToOrientationLandscape);

        if (newXy != null && newXy.length == 2) {
            wmParams.x = (int) newXy[0] - imageView1.getMeasuredWidth() / 2;
            wmParams.y = (int) newXy[1] - imageView1.getMeasuredHeight() / 2 - 35;// 25
            mWindowManager.updateViewLayout(contentView, wmParams);
        }
    }

    /**
     * 重新計算x,y位置
     */
    private class RecaculateXyHelper {
        int currentHeight;
        int currentWidth;
        int longSize;
        int shortSize;

        private RecaculateXyHelper() {
            Display d = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
//            context.getResources().getDisplayMetrics()
            currentHeight = d.getHeight();
            currentWidth = d.getWidth();

            if (currentHeight > currentWidth) {
                longSize = currentHeight;
                shortSize = currentWidth;
            } else {
                longSize = currentWidth;
                shortSize = currentHeight;
            }
        }

        private float getSize(boolean isLandscape) {
            if (isLandscape) {
                return longSize;
            } else {
                return shortSize;
            }
        }

        /**
         * @param oldX                      x座標
         * @param oldY                      y座標
         * @param isOldOrientationLandscape 來源是否為landscape
         * @param isToOrientationLandscape  目的是否為landscape
         * @return 新的x, y座標
         */
        public float[] parseFromTo(float oldX, float oldY, boolean isOldOrientationLandscape, boolean isToOrientationLandscape) {
            float[] percentXy = _parseFromPercent(isOldOrientationLandscape, oldX, oldY);
            return _toOrientation(isToOrientationLandscape, percentXy[0], percentXy[1]);
        }

        private float[] _toOrientation(boolean isLandscape, float oldX, float oldY) {
            return new float[]{oldX * getSize(isLandscape), oldY * getSize(!isLandscape)};
        }

        private float[] _parseFromPercent(boolean isLandscape, float oldX, float oldY) {
            oldX = fixVal(oldX, getSize(isLandscape));
            oldY = fixVal(oldY, getSize(!isLandscape));
            return new float[]{oldX / (float) getSize(isLandscape), //
                    oldY / (float) getSize(!isLandscape)};
        }

        private float fixVal(float val, float fixVal) {
            if (val >= fixVal) {
                return fixVal;
            } else if (val <= 0) {
                return 0;
            }
            return val;
        }
    }

    /**
     * 全版廣告顯示時機
     */
    private class AdCheckShow {
        final int MAX_COUNT = 20;
        int count = 0;

        ClosePanelThread thread = null;
        InterstitialAdHelper admob = null;

        public void show() {
            if (admob == null) {
                admob = InterstitialAdHelper.newInstance(getApplicationContext());
            }

            if (thread == null || thread.getState() == Thread.State.TERMINATED) {
                thread = new ClosePanelThread();
                thread.start();
            }

            if (count > MAX_COUNT) {
                this.showAdForce();
                count = 0;
            }
        }

        public void showAdForce() {
            //admob.showAd();//目前無法顯示
            if (!BuildConfig.DEBUG) {
                InterstitialAdActivity.startThisActivity(FloatViewService.this);
            } else {
//                GodToast.getInstance(getApplicationContext()).show();//不秀A圖
                //InterstitialAdActivity.startThisActivity(FloatViewService.this);
            }
        }

        private class ClosePanelThread extends Thread {
            public void run() {
                if (count <= MAX_COUNT) {
                    count++;
                    sleepByTime(3000);
                }
                if (BuildConfig.DEBUG) {
                    debugBeforeShowAD();
                }
                Log.v(TAG, "AD prepare count : " + count);
            }

            private void debugBeforeShowAD() {
                if (count >= MAX_COUNT) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(FloatViewService.this, "Ad Ready to open", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            private void sleepByTime(long time) {
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 紀錄放大鏡最後位置
     */
    private class MagnifierPosHolder {

        final String REF_KEY = MagnifierPosHolder.class.getName();
        final String X_KEY = "X";
        final String Y_KEY = "Y";

        private Context context;

        MagnifierPosHolder(Context context) {
            this.context = context;
        }

        private void recordXY(WindowManager.LayoutParams wmParams) {
            int x = wmParams.x;
            int y = wmParams.y;
            SharedPreferencesUtil.putData(context, REF_KEY, X_KEY, String.valueOf(x));
            SharedPreferencesUtil.putData(context, REF_KEY, Y_KEY, String.valueOf(y));
        }

        private boolean retoreXY(WindowManager mWindowManager, WindowManager.LayoutParams wmParams, RelativeLayout contentView) {
            if (SharedPreferencesUtil.hasData(context, REF_KEY, X_KEY) &&  //
                    SharedPreferencesUtil.hasData(context, REF_KEY, Y_KEY)) {
                int x = Integer.parseInt(SharedPreferencesUtil.getData(context, REF_KEY, X_KEY));
                int y = Integer.parseInt(SharedPreferencesUtil.getData(context, REF_KEY, Y_KEY));
                wmParams.x = x;
                wmParams.y = y;
                mWindowManager.updateViewLayout(contentView, wmParams);
                return true;
            }
            return false;
        }
    }

    private class RedPlusBtnHandler {
        final String refKey = FloatViewService.class.getSimpleName();
        final String bundleKey = "plusNote";
        final String delimitPtn = "\\Q#,#\\E";
        final String delimit = "#,#";
        final int maxLength = 50;
        final String uploadDir = "/english_prop/";
        Context context;
        String dropboxToken;

        private RedPlusBtnHandler(Context context) {
            this.context = context;
        }

        private boolean isInArry(String[] arry, String text) {
            for (String str : arry) {
                if (StringUtils.defaultString(str).contains(text)) {
                    return true;
                }
            }
            return false;
        }

        private String[] fixLengthArry(String[] arry) {
            while (arry.length > maxLength) {
                arry = ArrayUtils.remove(arry, 0);
            }
            return arry;
        }

        public boolean addNote(String text) {
            text = StringUtils.trimToEmpty(text);
            if (StringUtils.isBlank(text)) {
                return false;
            }
            boolean result = false;
            String[] arry = getRedPlusBtnArry();
            if (!isInArry(arry, text)) {
                arry = ArrayUtils.add(arry, StringUtils.trimToEmpty(text));
                arry = fixLengthArry(arry);
                result = true;
            }
            SharedPreferencesUtil.putData(context, refKey, bundleKey, StringUtils.join(arry, delimit));
            return result;
        }

        public void clear() {
            SharedPreferencesUtil.putData(context, refKey, bundleKey, "");
        }

        public String[] getRedPlusBtnArry() {
            String currentNote = "";
            if (SharedPreferencesUtil.hasData(context, refKey, bundleKey)) {
                currentNote = SharedPreferencesUtil.getData(context, refKey, bundleKey);
            }
            String[] arry = currentNote.split(delimitPtn, -1);
            for (int ii = 0; ii < arry.length; ii++) {
                if (StringUtils.isBlank(arry[ii])) {
                    arry = ArrayUtils.remove(arry, ii);
                    ii--;
                }
            }
            return arry;
        }

        public boolean uploadNotePlusToDropbox(final File file) {
            if (StringUtils.isBlank(this.dropboxToken)) {
                this.dropboxToken = DropboxApplicationActivity.getDropboxAccessToken(context);
            }
            if (StringUtils.isBlank(this.dropboxToken)) {
                Toast.makeText(context, "dropbox尚未就緒!", Toast.LENGTH_SHORT).show();
                return false;
            }

            boolean result = DropboxEnglishService.getRunOnUiThread(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    FileInputStream inputStream = null;
                    boolean result = false;
                    try {
                        inputStream = new FileInputStream(file);
                        String name = uploadDir + file.getName();

                        DbxClientV2 client = DropboxUtilV2.getClient(dropboxToken);
                        result = DropboxUtilV2.upload(name, inputStream, client);
                    } catch (FileNotFoundException e) {
                        Log.e(TAG, "uploadNotePlusToDropbox ERR : " + e.getMessage(), e);
                    } finally {
                        try {
                            inputStream.close();
                        } catch (Exception e) {
                        }
                        if (result) {
                            file.delete();
                        }
                    }
                    return result;
                }
            }, -1);

            Toast.makeText(context, "上傳" + (result ? "成功" : "失敗") + "! : " + file.getName(), Toast.LENGTH_SHORT).show();
            return result;
        }
    }

    public void onTaskRemoved(Intent rootIntent) {
        //unregister listeners
        //do any other cleanup if required
        //stop service
        stopSelf();
    }

    // 與activity連線 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ (舊的寫法)
    Callbacks activity;

    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public FloatViewService getServiceInstance() {
            return FloatViewService.this;
        }
    }

    public void registerClient(Activity activity) {
        this.activity = (Callbacks) activity;
    }

    public interface Callbacks {
        public void updateClient(long data);
    }

    /**
     * 給外部使用查詢用
     */
    public void searchWordForActivity(String word) {
        imageViewEraser.performClick();
        word = StringUtils.trimToEmpty(word);
        //原先做法
//        autoCompleteTextView1.setText(word);
//        searchEnglishId();
//        doOpenCloseEditPanel(true);

        //新作法
        ClipboardHelper.copyToClipboard(this.getApplicationContext(), word);
    }
    // 與activity連線↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ (舊的寫法)

    // 與activity連線 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ (新的寫法)
    private final IFloatServiceAidlInterface.Stub mBinderNew = new IFloatServiceAidlInterface.Stub() {
        @Override
        public void searchWord(final String englishId, final String sentance) throws RemoteException {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //設定例句
                    sentance4RecentSearch.set(sentance);

                    //查詢
                    searchWordForActivity(englishId);
                }
            });
        }
    };
    // 與activity連線↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ (新的寫法)
}