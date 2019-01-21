package com.example.englishtester;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.example.englishtester.common.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.englishtester.EnglishwordInfoDAO.EnglishWord;
import com.example.englishtester.common.DialogSingleInput;
import com.example.englishtester.common.DialogSingleInput.DialogConfirmClickListener;
import com.example.englishtester.common.FullPageMentionDialog;
import com.example.englishtester.common.TextToSpeechComponent;

import org.apache.commons.collections4.map.LRUMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;

import gtu._work.etc.EnglishTester_Diectory;
import gtu._work.etc.EnglishTester_Diectory.WordInfo;
import gtu._work.etc.EnglishTester_Diectory_Factory;

public class ShowWordListActivity extends ListActivity {

    private static final String TAG = ShowWordListActivity.class.getSimpleName();

    public static String ShowWordListActivity_DTO = "ShowWordListActivity_DTO";

    List<Word> wordList;
    MainActivityDTO dto;

    // ArrayAdapter<Word> wordsArray;
    SimpleAdapter listItemAdapter;

    LoadListStatus loadListStatus = LoadListStatus.ALL;
    SelectStatus selectStatus = SelectStatus.NONE;
    SortStatus sortStatus = SortStatus.QUESTIONS;
    boolean doSort = false;
    boolean dtoEnglishFileIsErrorMixFile = false;

    // service
    TextToSpeechComponent talkComponent;
    EnglishwordInfoDAO englishwordInfoDAO;
    RecentSearchService recentSearchService;
    EnglishTester_Diectory_Factory diectory = new EnglishTester_Diectory_Factory();
    EnglishDescKeeper englishDescKeeper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);

        if (!FullPageMentionDialog.isAlreadyFullPageMention(this.getClass().getName(), this)) {
            FullPageMentionDialog.builder(R.drawable.full_page_mention_001, this).showDialog();
        }

        setContentView(R.layout.activity_showword_list);

        talkComponent = new TextToSpeechComponent(getApplicationContext());
        englishwordInfoDAO = new EnglishwordInfoDAO(getApplicationContext());
        recentSearchService = new RecentSearchService(getApplicationContext());
        englishDescKeeper = new EnglishDescKeeper(getApplicationContext());

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        Log.v(TAG, "extras -- " + extras);
        if (extras != null) {
            dto = (MainActivityDTO) extras.getParcelable(ShowWordListActivity_DTO);
        }

        if (dto == null) {
            dto = new MainActivityDTO();
            this.recentSearchHistory();
            Log.v(TAG, "# recentSearchHistory go");
        } else {
            if (dto.englishFile == Constant.ERROR_MIX_FILE) {
                dtoEnglishFileIsErrorMixFile = true;
            }
        }

        // 取得wordList
        initList();

        // 重設ListView
        updateMainList();
    }

    enum LoadListStatus {
        PICKPROP_ONLY() {
            @Override
            void apply(ShowWordListActivity this_) {
                Log.v(TAG, "LoadListStatus = " + this.name());
                this_.wordList = new ArrayList<Word>();
                if (!validateListIsEmpty(this_.dto.pickProp, this_)) {
                    return;
                }
                for (Object word_ : this_.dto.pickProp.keySet()) {
                    String word = (String) word_;
                    Word w = new Word();
                    w.word = word;
                    w.desc = getEnglishPropDesc(this_, word);
                    w.check = this_.isInPickList(word);
                    this_.wordList.add(w);
                }
            }
        }, //
        ALL() {
            @Override
            void apply(ShowWordListActivity this_) {
                Log.v(TAG, "LoadListStatus = " + this.name());
                this_.wordList = new ArrayList<Word>();
                if (!validateListIsEmpty(this_.dto.wordsListCopy, this_)) {
                    return;
                }
                for (String word : this_.dto.wordsListCopy) {
                    Word w = new Word();
                    w.word = word;
                    w.desc = getEnglishPropDesc(this_, word);
                    w.check = this_.isInPickList(word);
                    this_.wordList.add(w);
                }
            }
        }, //
        CHECK_ONLY() {
            @Override
            void apply(ShowWordListActivity this_) {
                Log.v(TAG, "LoadListStatus = " + this.name());
                List<Word> newList = new ArrayList<Word>();
                for (Word word : this_.wordList) {
                    if (word.check) {
                        newList.add(word);
                    }
                }
                this_.wordList = newList;
            }
        }, //
        PRONOUNCE_ONLY() {
            @Override
            void apply(ShowWordListActivity this_) {
                Log.v(TAG, "LoadListStatus = " + this.name());
                List<Word> newList = new ArrayList<Word>();
                if (this_.dto.doPronounceList == null) {
                    Log.v(TAG, "this_.dto.doPronounceList == null");
                    Toast.makeText(this_, "特選單字清單為空!", Toast.LENGTH_SHORT).show();
                    this_.dto.doPronounceList = new ArrayList<String>();
                }
                for (int ii = 0; ii < this_.dto.doPronounceList.size(); ii++) {
                    String eng = this_.dto.doPronounceList.get(ii);
                    Word word = this_.getWord(eng);
                    newList.add(word);
                }
                this_.wordList = newList;
            }
        }, //
        NONE() {
            @Override
            void apply(ShowWordListActivity this_) {
                Log.v(TAG, "LoadListStatus = " + this.name());
            }
        }, //
        ;

        String getEnglishPropDesc(ShowWordListActivity this_, String text) {
            if (StringUtils.isBlank(text)) {
                return "空字串!";
            }
            EnglishWord word = this_.dto.englishProp.get(text);
            return word.englishDesc;
        }

        private static boolean validateListIsEmpty(Object list, ShowWordListActivity this_) {
            if (list == null) {
                Toast.makeText(this_.getApplicationContext(), "無資料可顯示!", Toast.LENGTH_SHORT).show();
                return false;
            }
            try {
                Method mth = list.getClass().getMethod("isEmpty", new Class[0]);
                Boolean v = (Boolean) mth.invoke(list, new Object[0]);
                if (v == true) {
                    Toast.makeText(this_.getApplicationContext(), "無資料可顯示!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.v(TAG, "ERROR " + e.getMessage(), e);
                return false;
            }
            return true;
        }

        abstract void apply(ShowWordListActivity this_);
    }

    enum SelectStatus {
        NONE() {
            @Override
            void apply(ShowWordListActivity this_) {
                Log.v(TAG, "SelectStatus = " + this.name());
            }
        }, //
        CHECKALL() {
            @Override
            void apply(ShowWordListActivity this_) {
                Log.v(TAG, "SelectStatus = " + this.name());
                for (Word word : this_.wordList) {
                    word.check = true;
                }
            }
        }, //
        UNCHECK() {
            @Override
            void apply(ShowWordListActivity this_) {
                Log.v(TAG, "SelectStatus = " + this.name());
                for (Word word : this_.wordList) {
                    word.check = false;
                }
            }
        }, //
        INVERSE() {
            @Override
            void apply(ShowWordListActivity this_) {
                Log.v(TAG, "SelectStatus = " + this.name());
                for (Word word : this_.wordList) {
                    word.check = !word.check;
                }
            }
        }, //
        ;

        abstract void apply(ShowWordListActivity this_);
    }

    enum SortStatus {
        NONE() {
            @Override
            void apply(ShowWordListActivity this_) {
                Log.v(TAG, "SortStatus = " + this.name());
            }
        }, //
        QUESTIONS() {
            @Override
            void apply(ShowWordListActivity this_) {
                Log.v(TAG, "SortStatus = " + this.name());
                List<String> nusortList = this_.dto.wordsListCopy;
                List<Word> wordList2 = this_.wordList;
                boolean wordListExists = wordList2 != null && !wordList2.isEmpty();
                List<Word> wordList = new ArrayList<Word>();
                for (String str : nusortList) {
                    if (wordListExists) {
                        for (Word w : wordList2) {
                            if (w.word.equals(str)) {
                                wordList.add(w);
                            }
                        }
                    } else {
                        wordList.add(this_.getWord(str));
                    }
                }
                this_.wordList = wordList;
            }
        }, //
        SORT_AZ() {
            @Override
            void apply(ShowWordListActivity this_) {
                Log.v(TAG, "SortStatus = " + this.name());
                Collections.sort(this_.wordList, new Comparator<Word>() {
                    @Override
                    public int compare(Word paramT1, Word paramT2) {
                        return paramT1.word.compareTo(paramT2.word);
                    }
                });
            }
        }, //
        ;

        abstract void apply(ShowWordListActivity this_);
    }

    /**
     * 按照字母順序排
     */
    private void initList() {
        if (dto == null) {
            dto = new MainActivityDTO();
        }
        if (dto.wordsList == null) {
            dto.wordsList = new ArrayList<String>();
        }
        if (dto.wordsListCopy == null) {
            dto.wordsListCopy = new ArrayList<String>();
        }

        Log.v(TAG, "# loadListStatus - " + loadListStatus);
        Log.v(TAG, "# selectStatus - " + selectStatus);
        Log.v(TAG, "# sortStatus - " + sortStatus);

        loadListStatus.apply(this);
        loadListStatus = LoadListStatus.NONE;

        selectStatus.apply(this);
        selectStatus = SelectStatus.NONE;

        sortStatus.apply(this);
        sortStatus = SortStatus.QUESTIONS;

        // 排列
        String currentWord = null;
        if (!dto.wordsList.isEmpty()) {
            currentWord = dto.wordsList.get(0);
        }
        for (int ii = 0; ii < wordList.size(); ii++) {
            Word word = wordList.get(ii);
            word.pos = ii + 1;
            if (StringUtils.equals(word.word, currentWord)) {
                word.current = true;
            }
        }
    }

    /**
     * 重設ListView
     */
    private void updateMainList() {
        // wordsArray = new ArrayAdapter<Word>(this,
        // android.R.layout.simple_dropdown_item_1line, wordList);//

        if (wordList == null || wordList.isEmpty()) {
            wordList = new ArrayList<Word>();
            Log.v(TAG, "wordList為空!");
            Toast.makeText(getApplicationContext(), "wordList為空!", Toast.LENGTH_SHORT).show();
        }

        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < wordList.size(); i++) {
            Word word = wordList.get(i);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("item_image", getWordIndicatePic(word));// 圖像資源的ID
            map.put("item_title", word);
            // map.put("item_text", word.desc);
            map.put("item_text", "");
            map.put("item_image_check", null);
            listItem.add(map);
        }

        listItemAdapter = new SimpleAdapter(this, listItem,// 資料來源
                R.layout.subview_listview, //
                new String[]{"item_title", "item_text", "item_image", "item_image_check"}, //
                new int[]{R.id.ItemTitle, R.id.ItemText, R.id.ItemImage, R.id.ImageView01}//
        );

        // setListAdapter(wordsArray);
        setListAdapter(listItemAdapter);
    }

    /**
     * 取得圖片
     */
    private Integer getWordIndicatePic(Word word) {
        if (dto.pickProp == null) {
            dto.pickProp = new Properties();
        }
        if (dto.pickPropList == null) {
            dto.pickPropList = new ArrayList<String>();
        }
        if (dto.doAnswerList == null) {
            dto.doAnswerList = new ArrayList<String>();
        }
        if (dto.pickProp.containsKey(word.word) || dto.pickPropList.contains(word.word)) {
            return R.drawable.icon_wrong;
        } else if (dto.doAnswerList.contains(word.word)) {
            return R.drawable.icon_right;
        } else if (word.current) {
            return R.drawable.icon_left_arrow;
        } else {
            return null;
        }
    }

    /**
     * 將勾選的加入pickProp
     */
    private void addToPickProp() {
        for (Word w : wordList) {
            if (w.check) {
                dto.pickProp.setProperty(w.word, w.desc);
            } else {
                dto.pickProp.remove(w.word);
            }
        }
    }

    /**
     * 開啟dialog
     */
    private void saveInErrorMixFile() {
        final File saveFile = Constant.ERROR_MIX_FILE;
        DialogSingleInput.builder(ShowWordListActivity.this).apply("請確認", "請確認檔案名稱", saveFile.getName(), new DialogConfirmClickListener() {
            @Override
            public void onClick(View v, Dialog dialog, String inputTextStr) {
                if (StringUtils.isNotBlank(inputTextStr)) {
                    final File newSaveFile = new File(saveFile.getParentFile(), inputTextStr);
                    if (newSaveFile.exists()) {
                        new AlertDialog.Builder(ShowWordListActivity.this)//
                                .setTitle("請確認")//
                                .setMessage("檔案已存在,是否覆蓋?")//
                                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                        saveInErrorMixFile_detail(newSaveFile);
                                    }
                                }).setNeutralButton("關閉", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            }
                        }).show();
                    } else {
                        saveInErrorMixFile_detail(newSaveFile);
                    }
                }
            }
        }).show();
    }

    /**
     * 真正儲存動作
     */
    private void saveInErrorMixFile_detail(File saveFile) {
        final Properties prop = new Properties();
        /* 加入目前檔案清單近來 */
        try {
            if (saveFile.exists()) {
                prop.load(new FileInputStream(Constant.ERROR_MIX_FILE));
            }
        } catch (Exception e1) {
            Log.e(TAG, e1.getMessage(), e1);
        }
        for (Word word : wordList) {
            if (word.check) {
                prop.setProperty(word.word, word.desc);
            }
        }
        if (prop.isEmpty()) {
            Toast.makeText(this, "未勾選任何項目!", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            prop.store(new FileOutputStream(Constant.ERROR_MIX_FILE), "save by wordList");
            Toast.makeText(ShowWordListActivity.this, "儲存成功!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(ShowWordListActivity.this, "儲存失敗:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isInPickList(String word) {
        if (dto.pickProp != null) {
            boolean has = dto.pickProp.containsKey(word);
            if (has) {
                Log.v(TAG, word + " => in pickProp => " + has);
            }
            return has;
        }
        return false;
    }

    /**
     * 取得Word
     */
    private Word getWord(String english) {
        EnglishWord eng = englishwordInfoDAO.queryOneWord(english);
        Word word = new Word();
        if (eng != null) {
            word.word = eng.englishId;
            word.desc = eng.englishDesc;
        } else {
            WordInfo info = diectory.parseToWordInfo(english, this, null);
            word.word = info.getEnglishId();
            word.desc = info.getMeaning();
        }
        return word;
    }

    static class Word {
        boolean current;
        String word;
        String desc;
        int pos;
        boolean check;

        @Override
        public String toString() {
            return StringUtils.leftPad("" + pos, 4, ' ') + " : " + word;
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        final Word word = wordList.get(position);
        String desc = word.desc;

        word.check = !word.check;

        if (word.check) {
            // 發音
            talkComponent.speak(word.word);
        }

        // wordsArray.notifyDataSetChanged();
        listItemAdapter.notifyDataSetChanged();

        // 為勾選時隱藏
        Map<String, Object> itemMap = (Map<String, Object>) l.getItemAtPosition(position);
        if (word.check) {

            //過濾若沒有解釋要即時翻譯
            desc = englishDescKeeper.getDesc(word.word, word.desc);
            word.desc = desc;

            itemMap.put("item_text", desc);
            itemMap.put("item_image_check", R.drawable.icon_check);
        } else {
            itemMap.put("item_text", "");
            itemMap.put("item_image_check", null);
        }
    }

    private class EnglishDescKeeper {
        EnglishTester_Diectory diectory = new EnglishTester_Diectory();
        Map<String, WordInfo> englishMap = new LRUMap<String, WordInfo>(50);
        Context context;
        Handler handler = new Handler();

        private EnglishDescKeeper(Context context) {
            this.context = context;
        }

        private String getDesc(String english, String desc) {
            if (StringUtils.isNotBlank(desc)) {
                return desc;
            }
            english = StringUtils.trimToEmpty(english).toLowerCase();
            if (englishMap.containsKey(english)) {
                return englishMap.get(english).getMeaning();
            } else {
                WordInfo word = diectory.parseToWordInfo(english, context, handler);
                englishMap.put(english, word);
                return word.getMeaning();
            }
        }
    }

    /**
     * 刷新ListActivity
     */
    private void updateListActivity() {
        final int WHAT_INT = 1;
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case WHAT_INT:
                        // wordsArray = new
                        // ArrayAdapter<Word>(ShowWordListActivity.this,
                        // android.R.layout.simple_dropdown_item_1line, wordList);
                        // setListAdapter(wordsArray);

                        updateMainList();

                        Log.v(TAG, "### updateLIST !!!");
                        break;
                }
                super.handleMessage(msg);
            }
        };
        Thread changeListThread = new Thread(Thread.currentThread().getThreadGroup(), new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                Bundle bundle = new Bundle();
                message.setData(bundle);
                message.what = WHAT_INT;
                handler.sendMessage(message);
            }
        }, "changeListThread..");
        changeListThread.start();
    }

    /**
     * 查詢最近查詢紀錄
     */
    private void recentSearchHistory() {
        final ProgressDialog myDialog = ProgressDialog.show(this, "查詢資料中", "初始化...", true);
        final Handler handler = new Handler();
        DropboxEnglishService.getRunOnUiThread(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                //刪除超過n的歷史資料
                recentSearchService.deleteOldData(200);

                //查詢最近的歷史紀錄
                List<String> list = recentSearchService.recentSearchHistory(500);

                dto.wordsList = list;
                dto.wordsListCopy = list;

                Map<String, EnglishWord> englishMap = new HashMap<String, EnglishWord>();
                for (String word : dto.wordsList) {
                    EnglishWord word2 = englishwordInfoDAO.queryOneWord(word);
                    if (word2 == null) {
                        word2 = new EnglishWord();
                        word2.englishId = word;
//                        WordInfo info = diectory.parseToWordInfo(word, getApplicationContext());
//                        if (info != null) {
//                            word2.englishDesc = info.getMeaning();
//                            word2.pronounce = info.getPronounce();
//                        }
                    }
                    englishMap.put(word, word2);
                }

                dto.englishProp = englishMap;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //取消dialog
                        myDialog.cancel();
                    }
                });
                return null;
            }
        }, -1L);
    }

    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 功能選單
    static int REQUEST_CODE = 5566;
    static int MENU_FIRST = Menu.FIRST;

    enum TaskInfo {
        RECENT_SEARCH("查詢歷史紀錄", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(ShowWordListActivity activity, Intent intent, Bundle bundle) {
                activity.sortStatus = SortStatus.QUESTIONS;
                activity.recentSearchHistory();
                activity.initList();
                activity.updateListActivity();
            }
        }, //
        SORT_AZ("順向排序", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(ShowWordListActivity activity, Intent intent, Bundle bundle) {
                activity.sortStatus = SortStatus.SORT_AZ;
                activity.initList();
                activity.updateListActivity();
            }
        }, //
        SORT_QUESTIONS("題目排序", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(ShowWordListActivity activity, Intent intent, Bundle bundle) {
                activity.sortStatus = SortStatus.QUESTIONS;
                activity.initList();
                activity.updateListActivity();
            }
        }, //
        PICKPROP_ONLY("只顯示錯誤清單", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(ShowWordListActivity activity, Intent intent, Bundle bundle) {
                activity.loadListStatus = LoadListStatus.PICKPROP_ONLY;
                activity.initList();
                activity.updateListActivity();
            }
        }, //
        CHECK_ONLY("只顯示勾選項目", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(ShowWordListActivity activity, Intent intent, Bundle bundle) {
                activity.loadListStatus = LoadListStatus.CHECK_ONLY;
                activity.initList();
                activity.updateListActivity();
            }
        }, //
        PRONOUNCE_ONLY("顯示特選清單", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(ShowWordListActivity activity, Intent intent, Bundle bundle) {
                activity.loadListStatus = LoadListStatus.PRONOUNCE_ONLY;
                activity.initList();
                activity.updateListActivity();
            }
        }, //
        CHECK_ALL("全選", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(ShowWordListActivity activity, Intent intent, Bundle bundle) {
                activity.selectStatus = SelectStatus.CHECKALL;
                activity.initList();
                activity.updateListActivity();
            }
        }, //
        UN_CHECK("全取消", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(ShowWordListActivity activity, Intent intent, Bundle bundle) {
                activity.selectStatus = SelectStatus.UNCHECK;
                activity.initList();
                activity.updateListActivity();
            }
        }, //
        SELECT_UNSELECT("反向選擇", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(ShowWordListActivity activity, Intent intent, Bundle bundle) {
                activity.selectStatus = SelectStatus.INVERSE;
                activity.initList();
                activity.updateListActivity();
            }
        }, //
        SAVE_TO_ERROR_MIX("儲存勾選到錯誤清單", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(ShowWordListActivity activity, Intent intent, Bundle bundle) {
                activity.saveInErrorMixFile();
            }
        }, //
        RESET_PICKPROP("回主功能", MENU_FIRST++, REQUEST_CODE++, MainActivity.class) {
            protected void onOptionsItemSelected(ShowWordListActivity activity, Intent intent, Bundle bundle) {
                activity.addToPickProp();
                Intent intent2 = new Intent();
                Bundle bundle2 = new Bundle();
                bundle2.putParcelable(ShowWordListActivity_DTO, activity.dto);
                intent2.putExtras(bundle2);
                activity.setResult(RESULT_OK, intent2);
                activity.finish();
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

        protected void onOptionsItemSelected(ShowWordListActivity activity, Intent intent, Bundle bundle) {
            Log.v(TAG, "onOptionsItemSelected = " + this.name());
            intent.setClass(activity, clz);
            intent.putExtras(bundle);
            activity.startActivityForResult(intent, requestCode);
        }

        protected void onActivityResult(ShowWordListActivity activity, Intent intent, Bundle bundle) {
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
    // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 功能選單
}
