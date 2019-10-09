package com.example.englishtester;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;

import com.example.englishtester.common.Log;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.englishtester.EnglishwordInfoDAO.EnglishWord;
import com.example.englishtester.EnglishwordInfoDAO.LastResultEnum;
import com.example.englishtester.common.FileConstantAccessUtil;
import com.example.englishtester.common.NetWorkUtil;
import com.example.englishtester.common.TextToSpeechComponent;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

import gtu._work.etc.EnglishTester_Diectory;
import gtu._work.etc.EnglishTester_Diectory.WordInfo;
import gtu._work.etc.EnglishTester_Diectory2;
import gtu._work.etc.EnglishTester_Diectory_Factory;

@TargetApi(11)
@SuppressLint("NewApi")
public class EnglishwordInfoActivity extends Activity {

    private static final String TAG = FloatViewActivity.class.getSimpleName();

    public static final String CURRENT_DTO = "currentDTO";
    public static final String SEARCH_ENGLISH_ID = "search_english_id";

    MainActivityDTO dto;

    String currentId;

    TextView viewInfoText;
    EditText englishDescText;

    AutoCompleteTextView autoCompleteTextView1;
    TextView englishPronounceLabel;

    Button modifyBtn;
    Button deleteBtn;
    Button englishDetailBtn;
    Button resetBtn;
    Button researchDescBtn;

    EnglishwordInfoDAO englishwordInfoDAO;
    RecentSearchService recentSearchService;
    SwitchPictureService switchPictureService;
    QuestionChoiceService questionChoiceService;
    EnglishwordInfoService englishwordInfoService;
    TextToSpeechComponent talkComponent;
    EnglishTester_Diectory_Factory diectory = new EnglishTester_Diectory_Factory();
    final Handler handler = new Handler();

    int allWordCount;

    // 秀圖 =============================
    Button previousPicBtn;
    Button nextPicBtn;
    Button searchBtn;
    ImageButton deletePicBtn;
    ImageView imageView;
    TextView hasPicLabel;
    TextView picFileLabel;
    com.ant.liao.GifView imageView_gif;

    // 秀圖 =============================

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        System.out.println("# onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_english_info);
        back();

        autoCompleteTextView1 = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        englishPronounceLabel = (TextView) findViewById(R.id.englishPronounceLabel);
        searchBtn = (Button) findViewById(R.id.searchBtn);
        englishDescText = (EditText) findViewById(R.id.englishDescText);
        viewInfoText = (TextView) findViewById(R.id.viewInfoText);

        modifyBtn = (Button) findViewById(R.id.modifyBtn);
        deleteBtn = (Button) findViewById(R.id.deleteBtn);
        englishDetailBtn = (Button) findViewById(R.id.englishDetailBtn);
        resetBtn = (Button) findViewById(R.id.resetBtn);
        researchDescBtn = (Button) findViewById(R.id.researchDescBtn);

        previousPicBtn = (Button) findViewById(R.id.previousPicBtn);
        nextPicBtn = (Button) findViewById(R.id.nextPicBtn);
        deletePicBtn = (ImageButton) findViewById(R.id.deletePicBtn);
        hasPicLabel = (TextView) findViewById(R.id.hasPicLabel);
        picFileLabel = (TextView) findViewById(R.id.picFileLabel);
        imageView_gif = (com.ant.liao.GifView) findViewById(R.id.imageView_gif);

        englishwordInfoDAO = new EnglishwordInfoDAO(this);
        recentSearchService = new RecentSearchService(this);
        switchPictureService = new SwitchPictureService(this);
        questionChoiceService = new QuestionChoiceService(this);
        englishwordInfoService = new EnglishwordInfoService(this);
        talkComponent = new TextToSpeechComponent(getApplicationContext());

        switchPictureService.init(previousPicBtn, nextPicBtn, deletePicBtn, imageView_gif, hasPicLabel, picFileLabel);

        //查詢框
        autoCompleteTextView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
                onAutoCompleteTextView1Click(getAutoCompleteTextViewEnglishId(true), true);
            }
        });

        //查詢框
        autoCompleteTextView1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.v(TAG, "keyCode = " + keyCode);
                switch (event.getAction()) {
                    case KeyEvent.ACTION_DOWN:
                        break;
                    case KeyEvent.ACTION_MULTIPLE:
                        break;
                    case KeyEvent.ACTION_UP:
                        if (keyCode == KeyEvent.KEYCODE_ENTER) {
                            onAutoCompleteTextView1Click(getAutoCompleteTextViewEnglishId(true), true);
                        }
                        return true;
                }
                return false;
            }
        });

        FloatViewService.setEditTextAllSelection(autoCompleteTextView1);

        //查詢
        searchBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onAutoCompleteTextView1Click(getAutoCompleteTextViewEnglishId(false), true);
            }
        });

        //修改按鈕
        modifyBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                String englishId = StringUtils.trimToEmpty(autoCompleteTextView1.getText().toString());
                final String desc = StringUtils.trimToEmpty(englishDescText.getText().toString());

                if (StringUtils.isBlank(englishId)) {
                    Toast.makeText(EnglishwordInfoActivity.this, "單字不應為空白!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtils.isBlank(desc)) {
                    Toast.makeText(EnglishwordInfoActivity.this, "解釋不應為空白!", Toast.LENGTH_SHORT).show();
                    return;
                }

                EnglishWord word_ = englishwordInfoDAO.queryOneWord(englishId);
                final StringBuilder doInsertSb = new StringBuilder();
                if (word_ == null) {
                    word_ = new EnglishWord();
                    word_.englishId = englishId;
                    word_.insertDate = System.currentTimeMillis();
                    Log.v(TAG, "doInsert!!");
                    doInsertSb.append("insert");
                }
                word_.englishDesc = desc;

                Log.v(TAG, "doInsertEq0 = " + doInsertSb);

                final EnglishWord word = word_;

                new AlertDialog.Builder(EnglishwordInfoActivity.this)//
                        .setTitle(doInsertSb.toString().equals("insert") ? "新增單字" : "修改單字")//
                        .setMessage(englishId + "\n" + desc)//
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                if (doInsertSb.toString().equals("insert")) {
                                    englishwordInfoDAO.insertWord(word);
                                    Toast.makeText(EnglishwordInfoActivity.this, "新增成功!", Toast.LENGTH_SHORT).show();
                                } else {
                                    englishwordInfoDAO.updateWord(word);
                                    Toast.makeText(EnglishwordInfoActivity.this, "修改成功!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                }).show();
            }
        });

        //刪除按鈕
        deleteBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                final String englishId = StringUtils.trimToEmpty(autoCompleteTextView1.getText().toString());
                if (StringUtils.isBlank(englishId)) {
                    Toast.makeText(EnglishwordInfoActivity.this, "單字不應為空白!", Toast.LENGTH_SHORT).show();
                    return;
                }
                EnglishWord word_ = englishwordInfoDAO.queryOneWord(englishId);
                if (word_ == null) {
                    Toast.makeText(EnglishwordInfoActivity.this, englishId + "單字不存在", Toast.LENGTH_SHORT).show();
                    return;
                }

                new AlertDialog.Builder(EnglishwordInfoActivity.this)//
                        .setTitle("確定刪除")//
                        .setMessage(englishId + "資料將清除!")//
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                englishwordInfoDAO.deleteByWord(englishId);
                                Toast.makeText(EnglishwordInfoActivity.this, englishId + "刪除成功!", Toast.LENGTH_SHORT).show();
                                initial();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                }).show();
            }
        });

        //單字參考按鈕
        englishDetailBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                final String englishId = StringUtils.trimToEmpty(autoCompleteTextView1.getText().toString());
                if (StringUtils.isBlank(englishId)) {
                    Toast.makeText(EnglishwordInfoActivity.this, "單字不應為空白!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (NetWorkUtil.connectionTest(EnglishwordInfoActivity.this)) {
                    // startActivity(new Intent(Intent.ACTION_VIEW,
                    // Uri.parse("http://www.thefreedictionary.com/" +
                    // englishId)));
                    SearchDictionaryActivity.searchWord(englishId, EnglishwordInfoActivity.this);
                }
            }
        });

        // 重設按鈕
        resetBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                viewInfoText.setText("");
                englishDescText.setText("");
                autoCompleteTextView1.setText("");
                englishPronounceLabel.setText("");
            }
        });

        // 發音
        englishPronounceLabel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 發音
                talkComponent.speak(currentId);
            }
        });

        //重新查詢
        researchDescBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final String englishId = getAutoCompleteTextViewEnglishId(true);
                if (StringUtils.isBlank(englishId)) {
                    Toast.makeText(getApplicationContext(), "請輸入單字,不可為空白!", Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "englishId is empty");
                    return;
                }

                new AlertDialog.Builder(EnglishwordInfoActivity.this).setItems(new String[]{"簡易", "完整"}, //
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, final int which) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        final AtomicReference<String> desc = new AtomicReference<String>();
                                        switch (which) {
                                            case 0:
                                                WordInfo newWord = new EnglishTester_Diectory().parseToWordInfo(englishId, EnglishwordInfoActivity.this, handler);
                                                desc.set(newWord.getMeaning());
                                                break;
                                            case 1:
                                                EnglishTester_Diectory2.WordInfo2 newWord2 = new EnglishTester_Diectory2().parseToWordInfo(englishId);
                                                desc.set(newWord2.getMeaning2());
                                                break;
                                        }

                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                englishDescText.setText(desc.get());
                                            }
                                        });
                                    }
                                }).start();
                            }
                        }).show();
            }
        });


        // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 取得螢幕翻轉前的狀態
        final EnglishwordInfoActivity data = (EnglishwordInfoActivity) getLastNonConfigurationInstance();
        if (data != null) {// 表示不是由於Configuration改變觸發的onCreate()
            Log.v(TAG, "load old status!");
            this.dto = data.dto;
            this.currentId = data.currentId;
            this.allWordCount = data.allWordCount;
            onAutoCompleteTextView1Click(currentId, false);
        } else {
            // 正常執行要做的
            Log.v(TAG, "### initial ###");
            try {
                initial();
                dto = getIntent().getExtras().getParcelable(CURRENT_DTO);
                String searchEnglishId = getIntent().getExtras().getString(SEARCH_ENGLISH_ID);
                Log.v(TAG, "#searchEnglishId = " + searchEnglishId);
                if (searchEnglishId != null) {
                    this.currentId = searchEnglishId;
                    onAutoCompleteTextView1Click(currentId, false);
                }
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage(), ex);
            }
        }

        Log.v(TAG, "currentDTO = " + dto);
    }

    private String getAutoCompleteTextViewEnglishId(boolean fixWord) {
        if (fixWord) {
            return fixSearchWord(autoCompleteTextView1.getText().toString());
        } else {
            return StringUtils.trimToEmpty(autoCompleteTextView1.getText().toString()).toLowerCase();
        }
    }

    public static String fixSearchWord(String englishId) {
        englishId = StringUtils.trimToEmpty(englishId).toLowerCase();
        englishId = englishId.replaceAll("[^a-zA-Z]+$", "");
        englishId = englishId.replaceAll("^[^a-zA-Z]+", "");
        return englishId;
    }

    /**
     * 單字查詢
     */
    private void onAutoCompleteTextView1Click(String currentId, boolean isMakeRecord) {
        Log.v(TAG, "onItemSelected == " + currentId);

        if (StringUtils.isBlank(currentId)) {
            Toast.makeText(this, "請輸入查詢單字!", Toast.LENGTH_SHORT).show();
            return;
        }

        switchPictureService.showPicture(currentId);

        EnglishWord word = englishwordInfoDAO.queryOneWord(currentId);

        this.searchEnglishId(currentId, word, isMakeRecord);

        // 發音
        talkComponent.speak(currentId);
    }

    /**
     * 搜尋要查詢的單字
     */
    private void searchEnglishId(final String englishId, EnglishWord word, final boolean isMakeRecord) {
        if (StringUtils.isBlank(englishId)) {
            Toast.makeText(getApplicationContext(), "請輸入單字,不可為空白!", Toast.LENGTH_SHORT).show();
            Log.v(TAG, "englishId is empty");
            return;
        }
        final String englishId_ = StringUtils.trimToEmpty(englishId).toLowerCase();
        autoCompleteTextView1.dismissDropDown();// 關閉下拉
        if (word != null) {
            setEnglishInfo(word.englishDesc, getEnglishPropPronounce(word), word.englishId);
            // 紀錄單字
            if (isMakeRecord) {
                recentSearchService.recordRecentSearch(englishId_, "");
                writeSearchWordToProperties(englishId_, null);
            }

            //設定查詢紀錄
            viewHistorySetup(word);
            Log.v(TAG, word.toString());
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final WordInfo newWord = diectory.parseToWordInfo(englishId_, EnglishwordInfoActivity.this, handler);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            setEnglishInfo(newWord.getMeaning(), newWord.getPronounce(), englishId_);
                        }
                    });

                    // 紀錄單字
                    if (isMakeRecord) {
                        recentSearchService.recordRecentSearch(englishId_, "");
                        writeSearchWordToProperties(englishId_, newWord);
                    }
                    toastMessage(EnglishwordInfoActivity.this, "你查了新字!", handler);

                    //判斷是否需要判斷相似
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            setSimilarWordIfNeed(englishId_, newWord.getMeaning(), isMakeRecord);
                        }
                    });
                }
            }).start();
        }
    }

    private void toastMessage(final Context context, final String message, Handler handler) {
        if (handler != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * 設定查詢紀錄
     */
    private void viewHistorySetup(EnglishWord word) {
        StringBuilder sb = new StringBuilder();
        sb.append("瀏覽次數 : " + word.browserTime + "\n");
        sb.append("測驗次數 : " + word.examTime + "\n");
        sb.append("出錯次數 : " + word.failTime + "\n");
        sb.append("建立時間 : " + parseToDate(word.insertDate) + "\n");
        sb.append("最後瀏覽 : " + parseToDate(word.lastbrowerDate) + "\n");
        sb.append("最後測驗 : " + parseToDate(word.examDate) + "\n");
        sb.append("最後測驗耗時 : " + word.lastDuring / 1000 + "秒\n");
        sb.append("最後測驗結果 : " + LastResultEnum.getStatus(word.lastResult) + "\n");
        sb.append("是否需測驗 : " + questionChoiceService.isNeedTest(word)[1] + "\n");
        viewInfoText.setText(sb);
    }

    /**
     * 設定相似單字
     */
    private boolean setSimilarWordIfNeed(final String englishId, String englishDesc, final boolean isMakeRecord) {
        if (StringUtils.isNotBlank(englishDesc)) {
            return false;
        }
        final List<EnglishWord> simList = englishwordInfoService.queryForSimilar(englishId, 5);
        final List<String> simList2 = new ArrayList<String>();
        for (int ii = 0; ii < simList.size(); ii++) {
            EnglishWord eng = simList.get(ii);
            simList2.add(eng.englishId + " " + eng.englishDesc);
        }
        new AlertDialog.Builder(EnglishwordInfoActivity.this)//
                .setTitle("您也許是要找")//
                .setItems(simList2.toArray(new String[0]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        EnglishWord engWord = simList.get(paramInt);
                        setEnglishInfo(engWord.englishDesc, engWord.pronounce, engWord.englishId);
                        currentId = engWord.englishId;
                        onAutoCompleteTextView1Click(currentId, false);
                    }
                })//
                .show();
        return true;
    }

    /**
     * 設定單字資訊
     */
    private void setEnglishInfo(String desc, String pronounce, String englishId) {
        autoCompleteTextView1.setText(englishId);
        englishPronounceLabel.setText(pronounce);
        englishDescText.setText(desc);
    }

    /**
     * 寫入查詢單字到歷史紀錄
     */
    private void writeSearchWordToProperties(String englishId, WordInfo newWord) {
        String englishDesc = null;
        if (newWord != null) {
            englishDesc = newWord.getMeaning();
        } else {
            englishDesc = englishDescText.getText().toString();
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

    String getEnglishPropPronounce(EnglishWord word) {
        if (StringUtils.isBlank(word.pronounce)) {
            return "";
        }
        return "/" + word.pronounce + "/";
    }

    String parseToDate(long value) {
        try {
            return DateFormatUtils.format(value, "yyyy/MM/dd HH:mm:ss");
        } catch (Exception ex) {
            return "";
        }
    }

    void initial() {
        String[] allWord = englishwordInfoDAO.queryAllWord(false);
        Arrays.sort(allWord);
        allWordCount = allWord.length;
        //android.R.layout.simple_spinner_dropdown_item simple_dropdown_item_1line simple_spinner_item
        autoCompleteTextView1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, allWord));
        autoCompleteTextView1.setText("");
        autoCompleteTextView1.setThreshold(1);
        viewInfoText.setText("");
        englishDescText.setText("");
    }

    void back() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                EnglishwordInfoActivity.this.setResult(RESULT_CANCELED, EnglishwordInfoActivity.this.getIntent());
                EnglishwordInfoActivity.this.finish();
            }
        });
    }

    private void browserRecentWords() {
        if (dto == null) {
            Toast.makeText(this, "不可預期的錯誤!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (dto.wordsList == null || dto.wordsList.isEmpty()) {
            Toast.makeText(this, "尚無做任何測驗!", Toast.LENGTH_SHORT).show();
            return;
        }
        LinkedList<String> linkedList = new LinkedList<String>();
        String currentId = dto.wordsList.get(0);
        for (int size = 0, index = dto.wordsListCopy.indexOf(currentId); index >= 0; index--) {
            String word = dto.wordsListCopy.get(index);
            linkedList.push(word);
            size++;
            if (size >= 20) {
                break;
            }
        }

        final String[] wordArray = linkedList.toArray(new String[0]);

        if (wordArray == null || wordArray.length == 0) {
            Toast.makeText(this, "目前無任何瀏覽單字", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)//
                .setTitle("最近" + wordArray.length + "單字")//
                .setItems(wordArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        EnglishwordInfoActivity.this.currentId = wordArray[paramInt];
                        onAutoCompleteTextView1Click(EnglishwordInfoActivity.this.currentId, false);
                    }
                })//
                .show();
    }

    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 功能選單

    static int REQUEST_CODE = 5566;
    static int MENU_FIRST = Menu.FIRST;

    enum TaskInfo {
        INTERRUPT_INIT_FILE("瀏覽最近題目", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(final EnglishwordInfoActivity activity, Intent intent, Bundle bundle) {
                activity.browserRecentWords();
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

        protected void onOptionsItemSelected(EnglishwordInfoActivity activity, Intent intent, Bundle bundle) {
            intent.setClass(activity, clz);
            intent.putExtras(bundle);
            activity.startActivityForResult(intent, requestCode);
        }

        protected void onActivityResult(EnglishwordInfoActivity activity, Intent intent, Bundle bundle) {
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
            menu.add(0, e.option, 0, e.title);
        }
        return true;
    }

    // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 功能選單

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
}
