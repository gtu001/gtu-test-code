package com.example.englishtester;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import com.example.englishtester.common.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.englishtester.EnglishwordInfoDAO.EnglishWord;
import com.example.englishtester.MainActivityDTO.CurrentMode;
import com.example.englishtester.common.ExampleSentenceDialogHelper;
import com.example.englishtester.common.FullPageMentionDialog;
import com.example.englishtester.common.NetWorkUtil;
import com.example.englishtester.common.TextToSpeechComponent;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gtu._work.etc.EnglishTester_Diectory;
import gtu.number.RandomUtil;

public class ReciteMainActivity extends Activity {

    private static final String TAG = ReciteMainActivity.class.getSimpleName();

    // TextView englishLabel;
    EnglishBean englishBean;
    TextView englishPronounceLabel;
    TextView answerLabel;
    TextView questionNumLabel;
    TextView hasPicLabel;
    TextView picFileLabel;
    TextView errorResultLabel;
    Button showAnswerBtn;
    Button showQuestionBtn;
    Button answerBtn1;
    Button answerBtn2;
    Button answerBtn3;
    Button answerBtn4;
    Button previousPicBtn;
    Button nextPicBtn;
    Button nextEnglishBtn;
    Button savePickPropBtn;
    Button recentQuestionBtn;
    ImageButton deletePicBtn;
    CheckBox showPicCheckBox;
    CheckBox doRecordSuccessFailBox;
    com.ant.liao.GifView imageView_gif;

    // service
    EnglishwordInfoService englishwordInfoService;
    QuestionChoiceService questionChoiceService;
    SwitchPictureService switchPictureService;
    FontSizeColorService fontSizeColorService;
    TextToSpeechComponent talkComponent;
    ListenModeService listenModeService;
    SpecialChoiceAddObj specialChoiceAddObj;
    DumpDataService dumpDataServie;
    RecentSearchService recentSearchService;

    MainActivityDTO dto;

    @TargetApi(16)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "# onCreate");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recite_main);

        if (!FullPageMentionDialog.isAlreadyFullPageMention(this.getClass().getName(), this)) {
            FullPageMentionDialog.builder(R.drawable.full_page_mention_001, this).showDialog();
        }

        // setTheme(android.R.style.Theme_Black);
//        setTheme(android.R.style.Theme_DeviceDefault_Light);

        viewInit();

        // service init
        // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
        dto = new MainActivityDTO();

        initSetupService();

        // 重新顯示
        // deletePicBtn.setVisibility(View.GONE);
        // service init
        // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

        answerBtnShowHide(false);

        // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 秀問題
        showQuestionBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                if (!valdiateEnglishLabel()) {
                    return;
                }

                dto.currentMode = CurrentMode.QUESTION;

                // 重設答案顏色
                fontSizeColorService.applyColor(dto.isWhiteBackground);

                // 秀問題
                answerBtnShowHide(true);

                // 隱藏答案
                answerLabelShow(false);

                // 隱藏錯誤結果
                errorResultLabel.setVisibility(View.GONE);
            }
        });

        // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 秀答案
        showAnswerBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                if (!valdiateEnglishLabel()) {
                    return;
                }

                dto.currentMode = CurrentMode.ANSWER;

                // 清空現在題目
                dto.currentText = "";

                // 重設答案顏色
                fontSizeColorService.applyColor(dto.isWhiteBackground);

                // 顯示答案
                answerLabelShow(true);

                // 隱藏問題
                answerBtnShowHide(false);

                // 隱藏錯誤結果
                errorResultLabel.setVisibility(View.GONE);

                // 開啟單字教學
                showWordInstruction();
            }
        });

        // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 下個單字
        nextEnglishBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                if (!valdiateEnglishLabel()) {
                    return;
                }

                // 紀錄瀏覽次數
                englishwordInfoService.nextEnglishBtn_addBrowserTime(dto);

                nextQuestion(true);
            }
        });

        // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 選擇題按鈕
        final Button[] answerBtns = new Button[]{answerBtn1, answerBtn2, answerBtn3, answerBtn4};
        for (int ii = 0; ii < 4; ii++) {
            final Button btn = answerBtns[ii];
            final int index = ii;
            btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View paramView) {
                    Log.v("click ans", "" + index);

                    // 重設按鈕顏色
                    fontSizeColorService.applyColor(dto.isWhiteBackground);

                    // 聽力測驗模式- 隱藏單字
                    listenModeService.hide();

                    if (dto.englishProp == null || dto.englishProp.size() < 4) {
                        Toast.makeText(ReciteMainActivity.this, "答案樣本數太少,請測多於四題!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String text = englishBean.englishWord;

                    if (index == dto.correctBtnNum) {
                        // 設定答案按鈕顏色
                        btn.setBackgroundColor(Color.GREEN);
                        btn.setTextColor(Color.BLACK);

                        // ↓↓↓↓↓↓↓↓↓↓ 判斷是否換題
                        if (dto.currentText != null && dto.currentText.equalsIgnoreCase(text)) {
                            // 答對紀錄
                            englishwordInfoService.correctAnswer(text, ReciteMainActivity.this);

                            dto.currentText = null;

                            // 換題
                            nextQuestion(true);

                            // 重設答案
                            resetAnswersText();

                            // 答對第二次 - 關閉秀圖
                            showPicCheckBoxAction(false);

                            // 答對第二次時 秀全部答案
                            showOnlyCorrectAnswerBtn(false);
                        } else {
                            dto.currentText = text;

                            // 答對第一次 - 顯示秀圖
                            showPicCheckBoxAction(true);

                            // 答對第一次時 只秀答對的按鈕 其他隱藏(有圖片才隱藏)
                            if (switchPictureService.isHasCurrentPic()) {
                                showOnlyCorrectAnswerBtn(true);
                            }

                            // 練聽力模式 - 顯示單字資訊
                            listenModeService.show();

                            // 答對第一次念發音
                            if (englishBean.isEnglish == false) {
                                if (dto.isAutoPronounce) {
                                    talkComponent.speak(englishBean.englishWord);
                                }
                            }

                            // 答對時重設答按鈕文字
                            resetWrongButtonText(btn);

                            // 一秒鐘後自動再按一次
                            clickRightButtonAgain(btn);
                        }
                        // ↑↑↑↑↑↑↑↑↑↑ 判斷是否換題
                    } else {
                        // 答錯處理
                        // 設定答案按鈕顏色
                        btn.setBackgroundColor(Color.RED);
                        btn.setTextColor(Color.BLACK);

                        // 答錯時重設答錯按鈕文字
                        resetWrongButtonText(btn);

                        // 新增PickRrop
                        addPickProperties(text);

                        // 紀錄發音次數 + 加入特選
                        specialChoiceAddObj.doCount(((EnglishWord) btn.getTag()).englishId);

                        // 答錯紀錄
                        englishwordInfoService.wrongAnswer(text, ReciteMainActivity.this);

                        // 清空現在題目
                        dto.currentText = "";
                    }
                }
            });
        }

        // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 儲存狀態按鈕
        savePickPropBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                // 另存答錯的項目
                savePickProp();

                // 顯示錯誤結果
                showErrorResultLabel();

                // 隱藏答案
                answerLabelShow(false);

                // 隱藏問題
                answerBtnShowHide(false);
            }
        });

        // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 顯示最近題目按鈕
        recentQuestionBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                if (dto == null) {
                    Toast.makeText(ReciteMainActivity.this, "不可預期的錯誤!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (dto.wordsList == null || dto.wordsList.isEmpty()) {
                    Toast.makeText(ReciteMainActivity.this, "尚無做任何測驗!", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ReciteMainActivity.this, "目前無任何瀏覽單字", Toast.LENGTH_SHORT).show();
                    return;
                }
                new AlertDialog.Builder(ReciteMainActivity.this)//
                        .setTitle("最近" + wordArray.length + "單字")//
                        .setItems(wordArray, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                String currentId = wordArray[paramInt];
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putString(EnglishwordInfoActivity.SEARCH_ENGLISH_ID, currentId);
                                TaskInfo.WORD_INFO.onOptionsItemSelected(ReciteMainActivity.this, intent, bundle);
                            }
                        })//
                        .show();
            }
        });

        // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 單字
        englishBean.get().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                // 發音
                if (StringUtils.isNotBlank(englishBean.englishWord)) {
                    talkComponent.speak(englishBean.englishWord);
                }

                // 紀錄發音次數 + 加入特選
                specialChoiceAddObj.doCount(englishBean.englishWord);
            }
        });

        // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 圖片群組控制
        showPicCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean) {
                showPicCheckBoxAction(null);
            }
        });

        // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 是否紀錄測驗結果
        doRecordSuccessFailBox.setChecked(true);
        doRecordSuccessFailBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean) {
                setDoRecordSuccessFailBoxStatus(null);
            }
        });

        // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 顯示例句
        questionNumLabel.setOnClickListener(new OnClickListener() {
            String englishId;
            ExampleSentenceDialogHelper helper;

            @Override
            public void onClick(View v) {
                final String englishId = englishBean.englishWord;
                WindowManager mWindowManager = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);
                if (helper == null || StringUtils.isBlank(this.englishId) || !StringUtils.equalsIgnoreCase(this.englishId, englishId)) {
                    this.englishId = englishId;
                    helper = new ExampleSentenceDialogHelper();
                }
                helper.exeute(englishId, ReciteMainActivity.this, mWindowManager);
            }
        });

        // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 取得螢幕翻轉前的狀態
        final ReciteMainActivity data = (ReciteMainActivity) getLastNonConfigurationInstance();
        if (data != null) {// 表示不是由於Configuration改變觸發的onCreate()
            Log.v(TAG, "load old status!");
            this.dto = data.dto;
            initExam(null, false, null);
        } else {
            // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 若前次為中斷狀態,詢問是否回復
            resumeFromInterrupt();
        }

        // 狀態回復後重色底色
        changeBackGround();

        Toast.makeText(this, "網路連接狀態:" + (NetWorkUtil.connectionTest(this) ? "開" : "關"), Toast.LENGTH_SHORT).show();
    }

    private void initSetupService() {
        englishwordInfoService = new EnglishwordInfoService(this);
        questionChoiceService = new QuestionChoiceService(this);
        switchPictureService = new SwitchPictureService(this);
        switchPictureService.init(previousPicBtn, nextPicBtn, deletePicBtn, imageView_gif, hasPicLabel, picFileLabel);
        talkComponent = new TextToSpeechComponent(getApplicationContext());
        fontSizeColorService = new FontSizeColorService(this, (RelativeLayout) findViewById(R.id.mainLayout));
        listenModeService = new ListenModeService(englishBean.get(), englishPronounceLabel, dto);
        specialChoiceAddObj = new SpecialChoiceAddObj();
        dumpDataServie = new DumpDataService(this);
        recentSearchService = new RecentSearchService(this);
    }

    private void changeBackGround() {
        fontSizeColorService.applyColor(dto.isWhiteBackground);
    }

    /**
     * 當作達摩式 : true - 表示只秀答對答案 , false - 全部顯示
     */
    private void showOnlyCorrectAnswerBtn(boolean showCorrectOnly) {
        final Button[] answerBtns = new Button[]{answerBtn1, answerBtn2, answerBtn3, answerBtn4};
        if (dto.currentMode == CurrentMode.ANSWER) {
            for (int ii = 0; ii < answerBtns.length; ii++) {
                answerBtns[ii].setVisibility(View.GONE);
            }
        } else {
            if (showCorrectOnly) {
                for (int ii = 0; ii < answerBtns.length; ii++) {
                    if (dto.correctBtnNum == ii) {
                        answerBtns[ii].setVisibility(View.VISIBLE);
                    } else {
                        answerBtns[ii].setVisibility(View.GONE);
                    }
                }
            } else {
                for (int ii = 0; ii < answerBtns.length; ii++) {
                    answerBtns[ii].setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void resumeFromInterrupt() {
        final File interruptFile = Constant.INTERRUPT_FILE;
        if (interruptFile.exists()) {
            new AlertDialog.Builder(ReciteMainActivity.this)//
                    .setTitle("之前執行中斷!")//
                    .setMessage("是否回復之前狀態?,時間:" + DateFormatUtils.format(interruptFile.lastModified(), "yyyy/MM/dd HH:mm:ss"))//
                    .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            initExam(null, false, new InitExamAction() {
                                @Override
                                public void beforeProcess() {
                                    try {
                                        // 資料不正確
                                        // ObjectInputStream ois = new
                                        // ObjectInputStream(new
                                        // FileInputStream(interruptFile));
                                        // dto = (MainActivityDTO)
                                        // ois.readObject();
                                        // ois.close();
                                        // interruptFile.delete();

                                        englishwordInfoService.initExam_restoreFromInterruptInit(dto);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                void afterProcess() {
                                    ReciteMainActivity.this.setDoRecordSuccessFailBoxStatus(false);

                                    // 狀態回復後重色底色
                                    ReciteMainActivity.this.changeBackGround();
                                }
                            });
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    interruptFile.delete();
                }
            }).show();
        }
    }

    String getEnglishPropDesc(String text) {
        EnglishWord word = dto.englishProp.get(text);
        return word.englishDesc;
    }

    String getEnglishPropPronounce(String text) {
        EnglishWord word = dto.englishProp.get(text);
        if (StringUtils.isBlank(word.pronounce)) {
            return "";
        }
        return "/" + word.pronounce + "/";
    }

    void viewInit() {
        TextView englishLabel = (TextView) findViewById(R.id.englishLabel);
        englishPronounceLabel = (TextView) findViewById(R.id.englishPronounceLabel);

        englishBean = new EnglishBean(englishLabel, englishPronounceLabel);

        answerLabel = (TextView) findViewById(R.id.answerLabel);

        questionNumLabel = (TextView) findViewById(R.id.questionNumLabel);
        hasPicLabel = (TextView) findViewById(R.id.hasPicLabel);
        picFileLabel = (TextView) findViewById(R.id.picFileLabel);

        errorResultLabel = (TextView) findViewById(R.id.errorResultLabel);

        imageView_gif = (com.ant.liao.GifView) findViewById(R.id.imageView_gif);

        showAnswerBtn = (Button) findViewById(R.id.showAnswerBtn);
        showQuestionBtn = (Button) findViewById(R.id.showQuestionBtn);

        previousPicBtn = (Button) findViewById(R.id.previousPicBtn);
        nextPicBtn = (Button) findViewById(R.id.nextPicBtn);
        nextEnglishBtn = (Button) findViewById(R.id.nextEnglishBtn);

        savePickPropBtn = (Button) findViewById(R.id.savePickPropBtn);
        recentQuestionBtn = (Button) findViewById(R.id.recentQuestionBtn);

        showPicCheckBox = (CheckBox) findViewById(R.id.showPicCheckBox);
        doRecordSuccessFailBox = (CheckBox) findViewById(R.id.doRecordSuccessFailBox);

        answerBtn1 = (Button) findViewById(R.id.answerBtn1);
        answerBtn2 = (Button) findViewById(R.id.answerBtn2);
        answerBtn3 = (Button) findViewById(R.id.answerBtn3);
        answerBtn4 = (Button) findViewById(R.id.answerBtn4);

        deletePicBtn = (ImageButton) findViewById(R.id.deletePicBtn);
    }

    void initExam(final File englishFile, final boolean doInitExam, final InitExamAction initExamAction) {
        final ProgressDialog myDialog = ProgressDialog.show(this, "載入題庫", "初始化...", true);
        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder hasErrorSb = new StringBuilder();
                final StringBuilder messageSb = new StringBuilder();

                try {
                    if (initExamAction != null) {
                        initExamAction.beforeProcess();
                    }

                    if (doInitExam) {
                        String message = englishwordInfoService.initExam(dto, englishFile);
                        if (StringUtils.isNotBlank(message)) {
                            messageSb.append(message);
                        }
                    }

                    // 載入圖片
                    switchPictureService.loadAllPictureIfNeed();
                } catch (Exception ex) {
                    hasErrorSb.append(ex.getMessage());
                }

                myDialog.cancel();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (hasErrorSb.length() > 0) {
                            Toast.makeText(getApplicationContext(), hasErrorSb.toString(), Toast.LENGTH_LONG).show();
                            return;
                        }

                        if (dto.wordsList != null && dto.englishProp != null) {
                            // 取得第一題
                            nextQuestion(false);

                            // 隱藏問題
                            answerBtnShowHide(false);

                            // 隱藏答案
                            answerLabelShow(false);

                            if (messageSb.length() > 0) {
                                Toast.makeText(getApplicationContext(), messageSb.toString(), Toast.LENGTH_LONG).show();
                            }
                        }

                        if (initExamAction != null) {
                            initExamAction.afterProcess();
                        }
                    }
                });
            }
        });
        thread.start();
    }

    void nextQuestion(boolean removeFirst) {
        // 紀錄作答的項目
        dto.doAnswerList.add(englishBean.englishWord);

        if (removeFirst) {
            if (dto.wordsList.size() > 0) {
                dto.wordsList.remove(0);
            }
        }

        // 設定計時器從現在開始計時
        dto.duringTime = System.currentTimeMillis();

        // 設定題目數
        questionNumLabel.setText(dto.wordsList.size() + "/" + dto.englishProp.size());

        // 設定初始圖片
        switchPictureService.resetPicture();

        // 重設答案顏色
        fontSizeColorService.applyColor(dto.isWhiteBackground);

        // 練聽力模式 - 隱藏單字資訊
        listenModeService.hide();

        // 清空現在題目
        dto.currentText = "";

        // 顯示題目
        if (dto.wordsList.size() > 0) {
            String text = dto.wordsList.get(0);

            // 設定單字
            englishBean.setTextEnglish(text, getEnglishPropDesc(text));

            // 設定音標
            englishPronounceLabel.setText(getEnglishPropPronounce(text));

            // 設定答案
            answerLabel.setText(getEnglishPropDesc(text));

            // 紀錄瀏覽紀錄
            this.dto.transactionId = System.currentTimeMillis();
            englishwordInfoService.browerCurrentWord(text, getEnglishPropDesc(text));
        } else {
            Toast.makeText(this, "已無題目!!", Toast.LENGTH_SHORT).show();

            // 另存答錯的項目
            savePickProp();
        }

        // 掃圖 (是英文才掃)
        Pattern ptn = Pattern.compile("[a-zA-Z]+");
        if (ptn.matcher(englishBean.englishWord).find()) {
            switchPictureService.showPicture(englishBean.englishWord);
        }

        // 發音
        // 考英文模式下才發音
        if (englishBean.isEnglish) {
            if (dto.isAutoPronounce) {
                talkComponent.speak(englishBean.englishWord);
            }
        }
    }

    void showErrorResultLabel() {
        if (dto.pickProp == null) {
            return;
        }
        errorResultLabel.setVisibility(View.VISIBLE);
        List<String> list = new ArrayList<String>();
        for (Object key : dto.pickProp.keySet()) {
            list.add((String) key);
        }
        Collections.sort(list);
        StringBuilder sb = new StringBuilder();
        for (Object key : list) {
            String value = dto.pickProp.getProperty((String) key);
            sb.append(key + "\n" + value + "\n\n");
        }
        errorResultLabel.setText(sb);
    }

    void savePickProp() {
        if (dto.pickProp == null || dto.englishProp == null) {
            return;
        }
        if (dto.englishFile == null) {
            dto.englishFile = Constant.ERROR_MIX_FILE;
        }
        String timeStr = DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmm");
        String fileName = dto.englishFile.getName().replaceAll(".properties", "");
        final String fileName1 = fileName + "_" + timeStr + ".properties";
        int score = 0;
        try {
            score = (int) (((float) (dto.doAnswerList.size() - dto.pickProp.size()) / (float) dto.doAnswerList.size()) * 100);
        } catch (Exception ex) {
        }
        String message = String.format("%d%% , (錯誤 %d/答題 %d)", //
                score, dto.pickProp.size(), dto.doAnswerList.size());
        String errorSave = "是否要儲存答錯項目, 檔名將存為 : \n" + fileName1;
        if (dto.pickProp.size() == 0) {
            errorSave = "";
        }
        final String errorSave_ = errorSave;
        new AlertDialog.Builder(this)//
                .setTitle("測驗結算")//
                .setMessage("正確率: " + message + "\n"//
                        + errorSave_)//
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        // final File saveFile = new
                        // File(dto.englishFile.getParentFile(), fileName1);

                        if (dto.pickProp == null || dto.pickProp.isEmpty()) {
                            Toast.makeText(ReciteMainActivity.this, "單字清單為空不需儲存!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        AccessController.doPrivileged(new PrivilegedAction<Object>() {
                            public Object run() {
                                try {
                                    File saveFile = Constant.ERROR_MIX_FILE;
                                    Properties saveProp = new Properties();
                                    if (saveFile.exists()) {
                                        saveProp.load(new FileInputStream(saveFile));
                                    }
                                    saveProp.putAll(dto.pickProp);
                                    saveProp.store(new FileOutputStream(saveFile), "不會的項目");

                                    Toast.makeText(ReciteMainActivity.this, "儲存 : " + saveFile.getName(), Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Toast.makeText(ReciteMainActivity.this, "錯誤 :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                return null;
                            }
                        });
                    }
                })//
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                })//
                .show();
    }

    void answerBtnShowHide(boolean show) {
        // 清空以選擇的答案
        dto.currentText = "";

        int showOrNot = -1;
        if (show) {
            showOrNot = View.VISIBLE;
            Log.v(TAG, "visible : " + showOrNot);

            if (!valdiateEnglishLabel()) {
                return;
            }
            // 重設答案
            resetAnswersText();
        } else {
            showOrNot = View.GONE;
            Log.v(TAG, "gone : " + showOrNot);
        }
        answerBtn1.setVisibility(showOrNot);
        answerBtn2.setVisibility(showOrNot);
        answerBtn3.setVisibility(showOrNot);
        answerBtn4.setVisibility(showOrNot);
    }

    void answerLabelShow(boolean show) {
        if (show) {
            previousPicBtn.setVisibility(View.VISIBLE);
            if (dto.showAnswerLabel) {
                answerLabel.setVisibility(View.VISIBLE);
                dto.showAnswerLabel = false;
            } else {
                answerLabel.setVisibility(View.GONE);
                dto.showAnswerLabel = true;
            }

            // 圖片群組控制
            showPicCheckBoxAction(null);

            nextEnglishBtn.setVisibility(View.VISIBLE);
            showPicCheckBox.setVisibility(View.VISIBLE);
        } else {
            answerLabel.setVisibility(View.GONE);
            imageView_gif.setVisibility(View.GONE);
            previousPicBtn.setVisibility(View.GONE);
            nextPicBtn.setVisibility(View.GONE);
            nextEnglishBtn.setVisibility(View.GONE);
            picFileLabel.setVisibility(View.GONE);
            showPicCheckBox.setVisibility(View.GONE);
            // deletePicBtn.setVisibility(View.GONE); //TODO
            deletePicBtn.setVisibility(View.GONE);
        }
    }

    void showPicCheckBoxAction(Boolean showPicGroup) {
        if (showPicGroup == null) {
            showPicGroup = showPicCheckBox.isChecked();
        }
        boolean isHasPic = switchPictureService.isHasCurrentPic();
        if (showPicGroup && isHasPic) {
            imageView_gif.setVisibility(View.VISIBLE);
            previousPicBtn.setVisibility(View.VISIBLE);
            nextPicBtn.setVisibility(View.VISIBLE);
            picFileLabel.setVisibility(View.VISIBLE);
            // deletePicBtn.setVisibility(View.VISIBLE);//TODO
            deletePicBtn.setVisibility(View.VISIBLE);
        } else {
            imageView_gif.setVisibility(View.GONE);
            previousPicBtn.setVisibility(View.GONE);
            nextPicBtn.setVisibility(View.GONE);
            picFileLabel.setVisibility(View.GONE);
            // deletePicBtn.setVisibility(View.GONE);//TODO
            deletePicBtn.setVisibility(View.GONE);
        }
    }

    boolean valdiateEnglishLabel() {
        String question = englishBean.englishWord;
        Log.v(TAG, "question = " + question);
        //Log.v(TAG, "dto.englishProp = " + dto.englishProp);
        if (dto.englishProp == null || dto.englishProp.isEmpty()) {
            Toast.makeText(ReciteMainActivity.this, "沒題目了!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (dto.wordsList.isEmpty()) {
            Toast.makeText(ReciteMainActivity.this, "沒有題目!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    void resetAnswersText() {
        if (englishBean.isEnglish) {
            resetAnswerText_normal();
        } else {
            resetAnswerText_reverse();
        }
    }

    /**
     * 正確按鈕在按一次
     */
    private void clickRightButtonAgain(final Button button) {
        if (dto.isAutoChangeQuestion) {
            final long WAIT_TIME = 500;
            final Handler handler = new Handler();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(WAIT_TIME);
                            } catch (Exception e) {
                            }
                            button.performClick();
                        }
                    });
                }
            });
            thread.start();
        } else {
            Toast.makeText(this, "再按一次正確答案..", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 看單字猜解釋
     */
    private void resetAnswerText_normal() {
        if (dto.englishProp.size() < 4) {
            Toast.makeText(this, "題目樣本數不足無法產生題目", Toast.LENGTH_SHORT).show();
            return;
        }

        String word = englishBean.englishWord;
        Button[] answerBtn = new Button[]{answerBtn1, answerBtn2, answerBtn3, answerBtn4};

        String answer = getEnglishPropDesc(word);

        Map<String, EnglishWord> answerMap = englishwordInfoService.queryForFourAnswers(word, answer, dto.englishProp);

        dto.correctBtnNum = RandomUtil.rangeInteger(0, 3);
        Log.v(TAG, "正確答案=====>" + dto.correctBtnNum);

        List<String> hasList = new ArrayList<String>();
        hasList.add(word);

        Iterator<String> otherEnglishIt = answerMap.keySet().iterator();
        for (int ii = 0; ii < 4; ii++) {
            if (ii == dto.correctBtnNum) {
                answerBtn[dto.correctBtnNum].setText(formatChangeLine(word, answer));
                answerBtn[dto.correctBtnNum].setTag(dto.englishProp.get(word));
                continue;
            }

            String key = otherEnglishIt.next();
            String value = answerMap.get(key).englishDesc;
            answerBtn[ii].setText(formatChangeLine(key, value));
            answerBtn[ii].setTag(answerMap.get(key));
            hasList.add(key);
        }
    }

    /**
     * 看解釋猜單字
     */
    private void resetAnswerText_reverse() {
        if (dto.englishProp.size() < 4) {
            Toast.makeText(this, "題目樣本數不足無法產生題目", Toast.LENGTH_SHORT).show();
            return;
        }

        String word = englishBean.englishWord;
        Button[] answerBtn = new Button[]{answerBtn1, answerBtn2, answerBtn3, answerBtn4};

        String answer = getEnglishPropDesc(word);

        Map<String, EnglishWord> answerMap = englishwordInfoService.queryForFourAnswers(word, answer, dto.englishProp);
        Iterator<String> iterator = answerMap.keySet().iterator();

        dto.correctBtnNum = RandomUtil.rangeInteger(0, 3);
        Log.v(TAG, "正確答案=====>" + dto.correctBtnNum);

        for (int ii = 0; ii < 4; ii++) {
            if (ii == dto.correctBtnNum) {
                answerBtn[dto.correctBtnNum].setText(word);
                answerBtn[dto.correctBtnNum].setTag(dto.englishProp.get(word));
                continue;
            } else {
                String engWord = iterator.next();
                answerBtn[ii].setText(engWord);
                answerBtn[ii].setTag(answerMap.get(engWord));
            }
        }
    }

    /**
     * 重新format答案
     */
    static String formatChangeLine(String question, String answer) {
        Pattern ptn2 = Pattern.compile("\\[[^\\]]*\\]");
        Pattern chinesePattern = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher mth2 = ptn2.matcher(answer);
        StringBuffer sb2 = new StringBuffer();
        while (mth2.find()) {
            if (chinesePattern.matcher(mth2.group()).find()) {
                mth2.appendReplacement(sb2, mth2.group());
            } else {
                mth2.appendReplacement(sb2, "");
            }
        }
        mth2.appendTail(sb2);

        answer = sb2.toString();
        answer = EnglishTester_Diectory.meaningUnescape(answer);
        answer = answer.replaceAll("-", "");

        Pattern ptn = Pattern.compile(question, Pattern.CASE_INSENSITIVE);
        Matcher matcher = ptn.matcher(answer);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "");
        }
        matcher.appendTail(sb);
        answer = sb.toString();

        answer = answer.replaceAll("\n", "");
        return answer;
    }

    void showWordInstruction() {
        /*
         * String text = englishLabel.getText().toString(); if
         * (connectionTest()) { startActivity(new Intent(Intent.ACTION_VIEW,
         * Uri.parse("http://www.thefreedictionary.com/" + text))); }
         */
        if (NetWorkUtil.connectionTest(this)) {
            SearchDictionaryActivity.searchWord(englishBean.englishWord, this);
        }
    }

    void setDoRecordSuccessFailBoxStatus(Boolean bool) {
        if (bool != null) {
            doRecordSuccessFailBox.setChecked(bool);
        }
        if (doRecordSuccessFailBox.isChecked()) {
            Toast.makeText(ReciteMainActivity.this, "紀錄測驗成果 開啟", Toast.LENGTH_SHORT).show();
            englishwordInfoService.doRecordSuccessFailBox = true;
        } else {
            Toast.makeText(ReciteMainActivity.this, "紀錄測驗成果 關閉", Toast.LENGTH_SHORT).show();
            englishwordInfoService.doRecordSuccessFailBox = false;
        }
    }

    /**
     * 增加錯誤清單
     */
    private void addPickProperties(String text) {
        dto.pickProp.setProperty(text, getEnglishPropDesc(text));
        if (dto.pickPropList == null) {
            dto.pickPropList = new ArrayList<String>();
        }
        dto.pickPropList.add(text);
    }

    static int REQUEST_CODE = 5566;
    static int MENU_FIRST = Menu.FIRST;

    enum TaskInfo {
        CHOICE_ENG_PROP("選擇題庫", MENU_FIRST++, REQUEST_CODE++, PropertiesFindActivity.class) {
            protected void onOptionsItemSelected(ReciteMainActivity activity, Intent intent, Bundle bundle) {
                activity.setDoRecordSuccessFailBoxStatus(false);
                super.onOptionsItemSelected(activity, intent, bundle);
            }

            protected void onActivityResult(final ReciteMainActivity activity, Intent intent, Bundle bundle) {
                final File file = (File) bundle.get(PropertiesFindActivity.BUNDLE_FILE);
                Log.v(TAG, "file =>" + file);
                activity.initExam(file, true, null);
            }
        }, //
        CHOICE_SEARCH_HISTORY("查詢歷史紀錄測驗", MENU_FIRST++, REQUEST_CODE++, PropertiesFindActivity.class) {
            protected void onOptionsItemSelected(final ReciteMainActivity activity, Intent intent, Bundle bundle) {
                activity.initExam(null, false, new InitExamAction() {
                    @Override
                    public void beforeProcess() {
                        try {
                            //刪除舊資料只保留n筆
                            activity.recentSearchService.deleteOldData(200);
                            //查詢最近n筆
                            List<EnglishWord> wordList = activity.recentSearchService.recentSearchHistoryForWord(100);

                            MainActivityDTO dto = activity.dto;
                            dto.englishProp = new HashMap<String, EnglishWord>();
                            for (EnglishWord w : wordList) {
                                dto.englishProp.put(w.englishId, w);
                            }

                            dto.wordsList = new ArrayList<String>();
                            dto.pickProp = new Properties();
                            dto.doAnswerList = new ArrayList<String>();

                            for (String text : dto.englishProp.keySet()) {
                                dto.wordsList.add(text);
                            }
                            dto.wordsList = RandomUtil.randomList(dto.wordsList);
                            dto.wordsListCopy = (List<String>) ((ArrayList<String>) dto.wordsList).clone();
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                });
            }
        }, //
        // CHOICE_PICS("選擇圖庫", MENU_FIRST++, REQUEST_CODE++,
        // PicFindActivity.class){
        // protected void onActivityResult(MainActivity activity, Intent
        // intent,
        // Bundle bundle) {
        // File file = (File) bundle.get(PropertiesFindActivity.BUNDLE_FILE);
        // Log.v(TAG, "file =>" + file);
        // activity.switchPictureService.picDir = file;
        // }
        // }, //
        WORD_INFO("單字資訊", MENU_FIRST++, REQUEST_CODE++, EnglishwordInfoActivity.class) {
            protected void onOptionsItemSelected(ReciteMainActivity activity, Intent intent, Bundle bundle) {
                bundle.putParcelable(EnglishwordInfoActivity.CURRENT_DTO, activity.dto);
                super.onOptionsItemSelected(activity, intent, bundle);
            }
        }, //
        SHOW_WORD_LIST("目前單字清單", MENU_FIRST++, REQUEST_CODE++, ShowWordListActivity.class) {
            protected void onOptionsItemSelected(ReciteMainActivity activity, Intent intent, Bundle bundle) {
                // if (activity.dto.englishProp == null ||
                // activity.dto.englishProp.size() == 0) {
                // Toast.makeText(activity, "沒有任何題目可瀏覽!",
                // Toast.LENGTH_SHORT).show();
                // return;
                // }
                bundle.putParcelable(ShowWordListActivity.ShowWordListActivity_DTO, activity.dto);
                super.onOptionsItemSelected(activity, intent, bundle);
            }

            protected void onActivityResult(ReciteMainActivity activity, Intent intent, Bundle bundle) {
                Log.v(TAG, "onActivityResult TODO!! = " + this.name());
                activity.dto = bundle.getParcelable(ShowWordListActivity.ShowWordListActivity_DTO);
            }
        }, //
        RECENT_ERROR("答錯單字測驗", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(final ReciteMainActivity activity, Intent intent, Bundle bundle) {
                activity.initExam(null, true, new InitExamAction() {
                    @Override
                    public void beforeProcess() {
                        activity.dto.englishProp = activity.questionChoiceService.queryForExam_Wrong();
                    }
                });
                activity.setDoRecordSuccessFailBoxStatus(false);
            }
        }, //
        NEW_WORD_EXAM_ALL("新進單字測驗", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(final ReciteMainActivity activity, Intent intent, Bundle bundle) {
                activity.initExam(null, true, new InitExamAction() {
                    @Override
                    public void beforeProcess() {
                        activity.dto.englishProp = activity.questionChoiceService.queryForExam_NewWordAll();
                    }
                });
                activity.setDoRecordSuccessFailBoxStatus(false);
            }
        }, //
        LONGTIME_NO_EXAM("精選50單字測驗", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(final ReciteMainActivity activity, Intent intent, Bundle bundle) {
                activity.initExam(null, true, new InitExamAction() {
                    @Override
                    public void beforeProcess() {
                        activity.dto.englishProp = activity.questionChoiceService.queryForExam_LongtimeNoExam50();
                    }
                });
                activity.setDoRecordSuccessFailBoxStatus(true);
            }
        }, //
        LONGTIME_NO_BROWSER("不熟單字200", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(final ReciteMainActivity activity, Intent intent, Bundle bundle) {
                activity.initExam(null, true, new InitExamAction() {
                    @Override
                    public void beforeProcess() {
                        activity.dto.englishProp = activity.questionChoiceService.queryForExam_LongtimeNoBrowser();
                    }
                });
                activity.setDoRecordSuccessFailBoxStatus(false);
            }
        }, //
        JP50_EXAM("日文50音", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(final ReciteMainActivity activity, Intent intent, Bundle bundle) {
                activity.initExam(null, true, new InitExamAction() {
                    @Override
                    public void beforeProcess() {
                        activity.dto.englishProp = activity.questionChoiceService.queryForExam_Jp50();
                    }
                });
                activity.setDoRecordSuccessFailBoxStatus(false);
            }
        }, //
        //        IMPORT_WORD("匯入單字", MENU_FIRST++, REQUEST_CODE++, PropertiesFindActivity.class) {
//            protected void onActivityResult(ReciteMainActivity activity, Intent intent, Bundle bundle) {
//                Log.v(TAG, "importWord back!");
//                File file = (File) bundle.get(PropertiesFindActivity.BUNDLE_FILE);
//                activity.englishwordInfoService.scanFileToEnglishword(activity, file);
//            }
//        }, //
        // EXPORT_ERROR_WORD("匯出格式錯誤單字", MENU_FIRST++, REQUEST_CODE++, null)
        // {
        // protected void onOptionsItemSelected(MainActivity activity, Intent
        // intent, Bundle bundle) {
        // activity.englishwordInfoService.saveErrorWord();
        // }
        // }, //
        // FIX_ERROR_WORDLIST("修正錯誤單字", MENU_FIRST++, REQUEST_CODE++,
        // PropertiesFindActivity.class){
        // protected void onActivityResult(final MainActivity activity,
        // Intent
        // intent, final Bundle bundle) {
        // new AlertDialog.Builder(activity)//
        // .setTitle("確定修改?")//
        // .setMessage("資料將無法回復!")//
        // .setPositiveButton("確定", new DialogInterface.OnClickListener() {
        // @Override
        // public void onClick(DialogInterface paramDialogInterface, int
        // paramInt) {
        // File file = (File) bundle.get(PropertiesFindActivity.BUNDLE_FILE);
        // activity.englishwordInfoService.resetProblemQuestion(file);
        // }
        // })
        // .setNegativeButton("取消", new DialogInterface.OnClickListener() {
        // @Override
        // public void onClick(DialogInterface paramDialogInterface, int
        // paramInt) {
        // }
        // })
        // .show();
        // }
        // }, //
//        STATUS_INFO("系統狀態", MENU_FIRST++, REQUEST_CODE++, StatusInfoActivity.class), //
//        EXPORT_RESULT("匯出紀錄", MENU_FIRST++, REQUEST_CODE++, null) {
//            protected void onOptionsItemSelected(final ReciteMainActivity activity,
//                                                 Intent intent, final Bundle bundle) {
//                activity.dumpDataServie.exportResult();
//            }
//        }, //
//        IMPORT_RESULT("匯入紀錄", MENU_FIRST++, REQUEST_CODE++, null) {
//            protected void onOptionsItemSelected(final ReciteMainActivity activity,
//                                                 Intent intent, final Bundle bundle) {
//                activity.dumpDataServie.importResult();
//            }
//        }, //
        // SET_PRONOUNCE("設置發音", MENU_FIRST++, REQUEST_CODE++, null){
        // protected void onOptionsItemSelected(final MainActivity activity,
        // Intent intent, final Bundle bundle) {
        // activity.englishwordInfoService.scanAllPronounce(activity);
        // }
        // }, //
        INTERRUPT_INIT_FILE("繼續上次未完成", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(final ReciteMainActivity activity, Intent intent, Bundle bundle) {
                activity.initExam(null, false, new InitExamAction() {
                    @Override
                    public void beforeProcess() {
                        activity.englishwordInfoService.initExam_restoreFromInterruptInit(activity.dto);
                    }

                    @Override
                    void afterProcess() {
                        activity.setDoRecordSuccessFailBoxStatus(false);
                        // //狀態回復後重色底色
                        // activity.changeBackGround();
                    }
                });
                activity.setDoRecordSuccessFailBoxStatus(false);

                // 狀態回復後重色底色
                activity.changeBackGround();
            }
        }, //
        CHANGE_BACKGROUND("改變底色", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(final ReciteMainActivity activity, Intent intent, Bundle bundle) {
                activity.dto.isWhiteBackground = !activity.dto.isWhiteBackground;
                activity.changeBackGround();
            }
        }, //
        CHANGE_FONTSIZE("改變字型大小", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(final ReciteMainActivity activity, Intent intent, Bundle bundle) {
                activity.fontSizeColorService.changeFontsize();
            }
        }, //
        EXPORT_NO_PICTURES("匯出無圖片單字", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(final ReciteMainActivity activity, Intent intent, Bundle bundle) {
                activity.questionChoiceService.writeNoPicturesWords(activity.switchPictureService);
            }
        }, //
        // LISTEN_TRAINING_MODE("聽力練習模式", MENU_FIRST++, REQUEST_CODE++, null)
        // {
        // protected void onOptionsItemSelected(final MainActivity activity,
        // Intent intent, Bundle bundle) {
        // activity.dto.isListenTestMode = !activity.dto.isListenTestMode;
        // Toast.makeText(activity, "聽力練習模式:" +
        // (activity.dto.isListenTestMode ?
        // "開" : "關"), Toast.LENGTH_SHORT).show();
        // if (activity.dto.isListenTestMode) {
        // activity.listenModeService.hide();
        // } else {
        // activity.listenModeService.show();
        // }
        // }
        // }, //
        ENG_DESC_MODE("練習模式切換", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(final ReciteMainActivity activity, Intent intent, Bundle bundle) {
                if (activity.englishBean != null) {
                    activity.englishBean.isEnglish = !activity.englishBean.isEnglish;
                    Toast.makeText(activity, "練習模式:" + (activity.englishBean.isEnglish ? "英文" : "解釋"), Toast.LENGTH_SHORT).show();
                }
            }
        }, //
        AUTO_CHANGE_QUESTION("自動換題", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(final ReciteMainActivity activity, Intent intent, Bundle bundle) {
                activity.dto.isAutoChangeQuestion = !activity.dto.isAutoChangeQuestion;
                Toast.makeText(activity, "換題:" + (activity.dto.isAutoChangeQuestion ? "開" : "關"), Toast.LENGTH_SHORT).show();
            }
        }, //
        AUTO_PRONOUNCE("自動發音", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(final ReciteMainActivity activity, Intent intent, Bundle bundle) {
                activity.dto.isAutoPronounce = !activity.dto.isAutoPronounce;
                Toast.makeText(activity, "自動發音:" + (activity.dto.isAutoPronounce ? "開" : "關"), Toast.LENGTH_SHORT).show();
            }
        }, //
        START_FLOATSERVICE("啟動懸浮字典", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onOptionsItemSelected(final ReciteMainActivity activity, Intent intent, Bundle bundle) {
                activity.startService(new Intent(activity, FloatViewService.class));
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

        protected void onOptionsItemSelected(ReciteMainActivity activity, Intent intent, Bundle bundle) {
            intent.setClass(activity, clz);
            intent.putExtras(bundle);
            activity.startActivityForResult(intent, requestCode);
        }

        protected void onActivityResult(ReciteMainActivity activity, Intent intent, Bundle bundle) {
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
        // 重設測驗時間
        resetTimer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.v(TAG, "# onCreateOptionsMenu");
        for (TaskInfo e : TaskInfo.values()) {
            menu.add(0, e.option, 0, e.title);
        }
        return true;
    }

    void resetTimer() {
        if (this.dto.duringTime != 0) {
            this.dto.duringTime = System.currentTimeMillis();
        }
    }

    /**
     * 重設按下按鈕時顯示內容
     */
    private void resetWrongButtonText(Button btn) {
        EnglishWord eng = (EnglishWord) btn.getTag();
        String btnAppendix = StringUtils.trimToEmpty(eng.getBtnAppendix());
        String engText = eng.englishId + "\n" + formatChangeLine(eng.englishId, eng.englishDesc) + btnAppendix;
        btn.setText(engText);
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
        if (dto.wordsList != null && !dto.wordsList.isEmpty()) {
            File file = Constant.INTERRUPT_FILE;
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
                oos.writeObject(dto);
                oos.flush();
                oos.close();
                Log.v(TAG, "write interrupt backup!!");
            } catch (Exception e) {
                Toast.makeText(this, "備份狀態失敗!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
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

    static abstract class InitExamAction {
        abstract void beforeProcess();

        void afterProcess() {
        }
    }

    private static class EnglishBean {
        TextView englishLabel;
        TextView englishPronounceLabel;

        String englishWord;
        String englishDesc;
        boolean isEnglish = true;// 考英文或考解釋

        public EnglishBean(TextView englishLabel, TextView englishPronounceLabel) {
            this.englishLabel = englishLabel;
            this.englishPronounceLabel = englishPronounceLabel;
        }

        public TextView get() {
            return englishLabel;
        }

        public void setTextEnglish(String english, String desc) {
            this.englishWord = english;
            this.englishDesc = ReciteMainActivity.formatChangeLine(english, desc);
            if (isEnglish) {
                englishLabel.setText(this.englishWord);
            } else {
                englishLabel.setText(this.englishDesc);
                englishPronounceLabel.setTextColor(Color.WHITE);
            }
        }
    }

    /**
     * 統計發音案件呼叫次數 (註記發音五次以上的單字)
     */
    private class SpecialChoiceAddObj {
        Map<String, Integer> map = new HashMap<String, Integer>();

        private static final int TRIGGER_CLICK_COUNT = 5;

        public void doCount(String english) {
            if (dto.doPronounceList == null) {
                dto.doPronounceList = new ArrayList<String>();
            }
            addCount(english);
            if (map.get(english) >= TRIGGER_CLICK_COUNT) {
                addPronounceList(english);
            }
        }

        private void addCount(String english) {
            int pronounceCount = 0;
            if (map.containsKey(english)) {
                pronounceCount = map.get(english);
                pronounceCount++;
            }
            map.put(english, pronounceCount);
        }

        private void addPronounceList(String english) {
            if (!dto.doPronounceList.contains(english)) {
                dto.doPronounceList.add(english);
                Toast.makeText(getApplicationContext(), "新增特選 : " + english, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
