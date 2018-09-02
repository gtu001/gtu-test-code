package com.example.englishtester;

import gtu._work.etc.EnglishTester_Diectory;
import gtu._work.etc.EnglishTester_Diectory.WordInfo;
import gtu.number.RandomUtil;
import gtu.number.SimilarityUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.example.englishtester.common.Log;
import android.widget.Toast;

import com.example.englishtester.EnglishwordInfoDAO.EnglishWord;
import com.example.englishtester.EnglishwordInfoDAO.EnglishWordSchema;
import com.example.englishtester.EnglishwordInfoDAO.LastResultEnum;
import com.example.englishtester.common.InitExamInterface;
import com.example.englishtester.common.NetWorkUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class EnglishwordInfoService {

    private static final String TAG = EnglishwordInfoService.class.getSimpleName();

    final EnglishwordInfoDAO dao;
    Context context;

    String currentWord;
    boolean doLog = false;

    boolean doRecordSuccessFailBox = true;

    String wrongAnswerText;

    public EnglishwordInfoService(Context context) {
        this.context = context;
        this.dao = new EnglishwordInfoDAO(context);
    }

    /**
     * 匯出有問題的字
     */
    void saveErrorWord() {
        java.util.regex.Pattern ptn = java.util.regex.Pattern.compile("[\u4e00-\u9fa5]+");

        List<EnglishWord> list = dao.queryAll(false);
        Properties prop = new Properties();
        for (EnglishWord e : list) {
            if (StringUtils.isBlank(e.englishDesc)) {
                prop.setProperty(e.englishId, e.englishDesc);
                continue;
            }
            if (e.englishDesc.indexOf("問題反應與合作提案") != -1) {
                prop.setProperty(e.englishId, e.englishDesc);
                continue;
            }
            Matcher matcher = ptn.matcher(e.englishDesc);
            StringBuilder sb = new StringBuilder();
            while (matcher.find()) {
                sb.append(matcher.group());
            }
            if (sb.length() <= 0) {
                prop.setProperty(e.englishId, e.englishDesc);
                continue;
            }
        }
        if (prop.isEmpty()) {
            Toast.makeText(context, "!!!!無錯誤格式單字", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean success = true;
        try {
            prop.store(new FileOutputStream(Constant.PropertiesFindActivity_PATH + "/errorWord.properties"), "");
        } catch (Exception e1) {
            e1.printStackTrace();
            success = false;
        }
        Toast.makeText(context, success ? "!!!!取得成功" : "!!!!取得失敗", Toast.LENGTH_SHORT).show();
    }

    /**
     * 紀錄瀏覽紀錄
     */
    void browerCurrentWord(String word, String wordDesc) {
        currentWord = word.toLowerCase();
        doLog = false;
        EnglishWord eng = dao.queryOneWord(word);
        boolean newRec = false;
        long currentTime = System.currentTimeMillis();
        if (eng == null) {
            eng = new EnglishWord();
            eng.insertDate = currentTime;
            eng.englishId = word.toLowerCase();
            eng.englishDesc = wordDesc;
            newRec = true;
            return; // 查無此單字就不做處理 TODO
        }
        // eng.browserTime = eng.browserTime + 1;
        eng.lastbrowerDate = currentTime;
        Log.v(TAG, "browserTime = " + DateFormatUtils.format(currentTime, "MM/dd HH:mm:ss"));
        if (newRec) {
            Log.v(TAG, "browerCurrentWord (New) = " + eng);
            dao.insertWord(eng);
        } else {
            Log.v(TAG, "browerCurrentWord (Update) = " + eng);
            dao.updateWord(eng);
        }
    }

    /**
     * 當按下按鈕下一題時新增上一提瀏覽次數
     */
    void nextEnglishBtn_addBrowserTime(MainActivityDTO dto) {
        if (dto.wordsList == null && dto.wordsList.isEmpty()) {
            Toast.makeText(context, "無任何題目!", Toast.LENGTH_SHORT).show();
            return;
        }
        String currentId = dto.wordsList.get(0);
        EnglishWord word = dto.englishProp.get(currentId);
        word.browserTime += 1;
        dao.updateWord(word);
    }

    /**
     * 初始化測驗
     */
    String initExam(final MainActivityDTO dto, final File file) {
        InitExamInterface initExamInst = new InitExamInterface() {
            @Override
            public void appendQuestionToEnglishProp(Map<String, EnglishWord> englishProp) throws Exception {
                if (file == null) {
                    return;
                }
                Properties prop = new Properties();
                prop.load(new FileInputStream(file));

                for (Object key : prop.keySet()) {
                    String eng = (String) key;
                    String engQuery = StringUtils.trimToEmpty(eng).toLowerCase();
                    EnglishWord word = dao.queryOneWord(engQuery);
                    // if (word == null) {//如果沒有匯入就不加入此單字
                    // word = new EnglishWord();
                    // word.englishId = eng;
                    // word.englishDesc = prop.getProperty(eng);
                    // word.insertDate = System.currentTimeMillis();
                    // }
                    // 改成沒匯入也要顯示
                    if (word != null) {
                        dto.englishProp.put(eng, word);
                    } else if (StringUtils.isNotBlank(prop.getProperty(eng))) {
                        word = new EnglishWord();
                        word.englishId = eng;
                        word.englishDesc = prop.getProperty(eng) + "_(未建檔)";
                        dto.englishProp.put(eng, word);
                        Log.v(TAG, "未建檔單字 : [" + eng + "]");
                    }
                }
            }
        };

        String messageStr = initExamInst.initExam(dto, file);
        return messageStr;
    }

    /**
     * 初始化測驗
     */
    String initExam_restoreFromInterruptInit(final MainActivityDTO dto) {
        String messageStr = null;
        File file = Constant.INTERRUPT_INIT_FILE;
        if (!file.exists()) {
            Log.v(TAG, "檔案不存在 - interruptInitFile.bak");
            return "檔案遺失回復失敗!";
        }
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            MainActivityDTO restoreBackupDto = (MainActivityDTO) ois.readObject();
            ois.close();
            if (restoreBackupDto != null) {
                for (String englishId : restoreBackupDto.wordsListCopy) {
                    EnglishWord word1 = dao.queryOneWord(StringUtils.trimToEmpty(englishId).toLowerCase());
                    if (word1 == null) {
                        continue;
                    }
                    EnglishWord word2 = restoreBackupDto.englishProp.get(englishId);
                    if (word1.browserTime != word2.browserTime) {
                        restoreBackupDto.wordsList.remove(englishId);
                        restoreBackupDto.doAnswerList.add(englishId);
                    }
                }
                dto.moveFrom(restoreBackupDto);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("檔案回復失敗! : " + ex.getMessage());
        }
        return messageStr;
    }

    /**
     * 單字瀏覽超過20次且經過十五天..才開始計入成績
     */
    private boolean isProficiencyWord(EnglishWord eng) {
        long criterionDate = System.currentTimeMillis() - ((24 * 60 * 60 * 1000) * 15);
        if (eng.browserTime > 20 && eng.insertDate < criterionDate) {
            return true;
        }
        return false;
    }

    /**
     * 答對紀錄
     */
    void correctAnswer(String word, ReciteMainActivity this_) {
        word = word.toLowerCase();
        if (StringUtils.equals(currentWord, word) && doLog == true) {
            Toast.makeText(context, wrongAnswerText, Toast.LENGTH_SHORT).show();
            return;
        }
        doLog = true;
        EnglishWord eng = dao.queryOneWord(word);
        if (eng == null) {
            // throw new RuntimeException("找不到 : " + word);
            Toast.makeText(this_, "找不到 : " + word, Toast.LENGTH_SHORT).show();
            return;
        }
        long currentTime = System.currentTimeMillis();
        eng.browserTime = eng.browserTime + 1;// 改成這裡加入
        eng.lastbrowerDate = currentTime;
        this.fixLastDuring(eng, this_);
        boolean isOkWord = this.isProficiencyWord(eng);
        if (doRecordSuccessFailBox && isOkWord) {
            eng.examTime = eng.examTime + 1;
            eng.lastResult = LastResultEnum.CORRECT.val;
            eng.examDate = currentTime;
        }
        Log.v(TAG, "correctAnswer = " + eng);
        dao.updateWord(eng);
        Toast.makeText(context, "正確, 測驗次數" + eng.examTime + "/錯誤" + eng.failTime + (!isOkWord ? ",未熟練尚不計成績" : ""), Toast.LENGTH_SHORT).show();
    }

    /**
     * 設定最大答題時間
     */
    void fixLastDuring(EnglishWord eng, ReciteMainActivity this_) {
        eng.lastDuring = System.currentTimeMillis() - this_.dto.duringTime;
        if (eng.lastDuring > Constant.FAIL_RECORD_DURNING) {
            eng.lastDuring = Constant.FAIL_RECORD_DURNING;
        }
    }

    /**
     * 答錯紀錄
     */
    void wrongAnswer(String word, ReciteMainActivity this_) {
        word = word.toLowerCase();
        if (StringUtils.equals(currentWord, word) && doLog == true) {
            return;
        }
        doLog = true;
        EnglishWord eng = dao.queryOneWord(word);
        if (eng == null) {
            // throw new RuntimeException("找不到 : " + word);
            Toast.makeText(this_, "找不到 : " + word, Toast.LENGTH_SHORT).show();
            return;
        }
        long currentTime = System.currentTimeMillis();
        eng.browserTime = eng.browserTime + 1;// 改成這裡加入
        eng.lastbrowerDate = currentTime;
        this.fixLastDuring(eng, this_);
        boolean isOkWord = this.isProficiencyWord(eng);
        if (doRecordSuccessFailBox && isOkWord) {
            eng.examTime = eng.examTime + 1;
            eng.failTime = eng.failTime + 1;
            eng.examDate = currentTime;
            eng.lastResult = LastResultEnum.WRONG.val;
        }
        Log.v(TAG, "wrongAnswer = " + eng);
        dao.updateWord(eng);
        wrongAnswerText = "錯誤, 測驗次數" + eng.examTime + "/錯誤" + eng.failTime + (!isOkWord ? ",未熟練尚不計成績" : "");
    }

    /**
     * 處理有問題的題目
     */
    void resetProblemQuestion(File file) {
        Properties prop = new Properties();
        if (!file.exists()) {
            Toast.makeText(context, "errorWord.properties檔案不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            prop.load(new FileInputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        int delCount = 0;
        int insertCount = 0;
        int updateCount = 0;
        for (Object key_ : prop.keySet()) {
            String key = (String) key_;
            String value = prop.getProperty(key);
            Log.v(TAG, "value = " + value);
            if (value.equals("#del#")) {
                dao.deleteByWord(key);
                delCount++;
                Log.v(TAG, "移除 : " + key);
                continue;
            }

            EnglishWord word = dao.queryOneWord(key);
            if (word == null) {
                word = new EnglishWord();
                word.englishId = key;
                word.englishDesc = value;
                dao.insertWord(word);
                insertCount++;
                Log.v(TAG, "insert = " + word);
            } else {
                word.englishDesc = value;
                dao.updateWord(word);
                updateCount++;
                Log.v(TAG, "update = " + word);
            }
        }
        Toast.makeText(context, String.format("!!!!修正完成 : 新增%d,修改%d,刪除%d 筆", insertCount, updateCount, delCount), Toast.LENGTH_SHORT).show();
    }

    /**
     * 匯入單字
     */
    void scanFileToEnglishword(ContextWrapper contextWrapper, final File file) {
        if (!NetWorkUtil.connectionTest(contextWrapper)) {
            Toast.makeText(context, "設定音標失敗,請確認網路連線!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Constant.DELETE_PIC_MARKS_FILE.equals(file) || !file.getName().endsWith(".properties")) {
            Toast.makeText(context, "此檔案格式不符!", Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog dialog02 = new ProgressDialog(context);
        dialog02.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog02.setMessage("匯入中...");
        dialog02.setCancelable(false);
        dialog02.show();

        final int WHAT_INT = 1;
        final String MSG_KEY = "MSG_KEY";
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case WHAT_INT:
                        Log.v(TAG, "### do set Pronounce !!!");
                        dialog02.cancel();
                        Toast.makeText(context, msg.getData().getString(MSG_KEY), Toast.LENGTH_LONG).show();
                        break;
                }
                super.handleMessage(msg);
            }
        };
        Thread scanFileToEnglishWordThread = new Thread(Thread.currentThread().getThreadGroup(), new Runnable() {
            @Override
            public void run() {
                // xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                EnglishTester_Diectory ed = new EnglishTester_Diectory();
                Properties prop = new Properties();
                try {
                    prop.load(new FileInputStream(file));
                } catch (final Exception ex) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "錯誤 :" + ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog02.cancel();
                    return;
                }
                final int MaxCount = 3000;
                if (prop.size() > MaxCount) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "檔案太大停止匯入(超過" + MaxCount + "字)", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog02.cancel();
                    return;
                }

                dialog02.setMax(prop.size());

                int newWord = 0;
                int oldWord = 0;

                long currentTime = System.currentTimeMillis();
                for (Enumeration<?> enu = prop.keys(); enu.hasMoreElements(); ) {
                    String word = (String) enu.nextElement();
                    word = StringUtils.trimToEmpty(word).toLowerCase();
                    String wordDesc = StringUtils.trimToEmpty(prop.getProperty(word));
                    if (StringUtils.isBlank(word)) {
                        continue;
                    }

                    boolean isNew = false;
                    EnglishWord endWord = dao.queryOneWord(word);
                    if (endWord == null) {
                        endWord = new EnglishWord();
                        endWord.englishId = word;
                        endWord.englishDesc = wordDesc;
                        endWord.insertDate = currentTime;
                        isNew = true;
                    }

                    if (StringUtils.isBlank(endWord.pronounce) || StringUtils.isBlank(endWord.englishDesc)) {
                        try {
                            WordInfo wordinfo = null;
                            wordinfo = ed.parseToWordInfo(word, context, null);
                            if (StringUtils.isBlank(endWord.englishDesc)) {
                                endWord.englishDesc = wordinfo.getMeaning();
                            }
                            if (StringUtils.isBlank(endWord.pronounce)) {
                                endWord.pronounce = wordinfo.getPronounce();
                            }
                        } catch (Exception ex) {
                            Log.v(TAG, "查無此單字! : " + word);
                            ex.printStackTrace();
                        }
                    }

                    if (StringUtils.isBlank(endWord.englishDesc)) {
                        continue;
                    }

                    if (isNew) {
                        Log.v(TAG, "import (New) = " + endWord);
                        dao.insertWord(endWord);
                        newWord++;
                    } else {
                        Log.v(TAG, "import (Update) = " + endWord);
                        dao.updateWord(endWord);
                        oldWord++;
                    }

                    dialog02.incrementProgressBy(1);
                }
                // xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

                String messageStr = "掃完單字數 : " + prop.size() + " 新字" + newWord + "/舊字" + oldWord;

                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString(MSG_KEY, messageStr);
                message.setData(bundle);
                message.what = WHAT_INT;
                handler.sendMessage(message);
            }
        }, "scanFileToEnglishWordThread..");
        scanFileToEnglishWordThread.start();
    }

    /**
     * 設定所有字的發音
     */
    void scanAllPronounce(ContextWrapper contextWapper) {
        if (!NetWorkUtil.connectionTest(contextWapper)) {
            Toast.makeText(context, "設定音標失敗,請確認網路連線!", Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog dialog02 = new ProgressDialog(context);
        dialog02.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog02.setMessage("設定中...");
        dialog02.setCancelable(false);
        dialog02.show();

        final int WHAT_INT = 1;
        final String MSG_KEY = "MSG_KEY";
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case WHAT_INT:
                        Log.v(TAG, "### do set Pronounce !!!");
                        dialog02.cancel();
                        Toast.makeText(context, msg.getData().getString(MSG_KEY), Toast.LENGTH_LONG).show();
                        break;
                }
                super.handleMessage(msg);
            }
        };
        Thread doSetPronounceThread = new Thread(Thread.currentThread().getThreadGroup(), new Runnable() {
            @Override
            public void run() {
                EnglishTester_Diectory ed = new EnglishTester_Diectory();

                int setOk = 0;
                int setFail = 0;
                List<EnglishWord> allList = dao.queryAll(false);

                List<EnglishWord> needSetList = new ArrayList<EnglishWord>();
                for (EnglishWord word : allList) {
                    if (StringUtils.isBlank(word.pronounce)) {
                        needSetList.add(word);
                    }
                }
                dialog02.setMax(needSetList.size());

                for (EnglishWord word : needSetList) {
                    try {
                        WordInfo wordinfo = ed.parseToWordInfo(word.englishId, context, null);
                        word.pronounce = wordinfo.getPronounce();
                        Log.v(TAG, word.englishId + "=>" + word.pronounce);
                        dao.updateWord(word);
                        setOk++;
                    } catch (Exception ex) {
                        Log.v(TAG, "查無此單字! : " + word.englishId);
                        setFail++;
                    }
                    dialog02.incrementProgressBy(1);
                }
                String messageStr = "成功設定發音:" + setOk + "/失敗:" + setFail;

                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString(MSG_KEY, messageStr);
                message.setData(bundle);
                message.what = WHAT_INT;
                handler.sendMessage(message);
            }
        }, "doSetPronounceThread..");
        doSetPronounceThread.start();
    }

    /**
     * 取得隨機三個答案
     */
    Map<String, EnglishWord> queryForFourAnswers(String englishWord, String englishDesc, Map<String, EnglishWord> englishProp) {
        Log.v(TAG, "queryForFourAnswers --- " + englishWord);
        Map<String, EnglishWord> prop = new HashMap<String, EnglishWord>();
        List<EnglishWord> list = new ArrayList<EnglishWord>();

        if (StringUtils.isBlank(englishWord)) {
            Log.v(TAG, "englishWord不應為空!");
            list = _getFillingErrorList(list);
        } else {
            QuestionFilterService questionFilter = new QuestionFilterService(englishWord, englishDesc, context);
            list = questionFilter.getMatchList();
        }

        list = QuestionFilterService.similarityWordList(englishWord, list);
        if (list.size() < 4) {
            list = new ArrayList<>(englishProp.values());
            //移除自己
            for (int ii = 0; ii < list.size(); ii++) {
                if (StringUtils.equals(englishWord, list.get(ii).getEnglishId())) {
                    list.remove(ii);
                    ii--;
                }
            }
            list = RandomUtil.randomList(list);
        }
        if (list.size() < 4) {// 情境 : 輸入亂碼
            list = _getFillingErrorList(list);
        }

        for (int ii = 0; prop.size() < 3; ii++) {
            EnglishWord e = list.get(ii);
            prop.put(e.englishId, e);
        }
        Log.v(TAG, "prop size = " + prop.size());
        return prop;
    }

    /**
     * 取得相似單字
     */
    List<EnglishWord> queryForSimilar(String englishWord, int size) {
        Log.v(TAG, "queryForSimilar --- " + englishWord);
        String prefix = englishWord.substring(0, 1);
        List<EnglishWord> list = dao.query(//
                EnglishWordSchema.ENGLISH_ID + " like '" + prefix + "%' ", null);
        Log.v(TAG, "condition = " + EnglishWordSchema.ENGLISH_ID + " like '" + prefix + "%'");
        Log.v(TAG, "query size = " + list.size());
        list = QuestionFilterService.similarityWordList(englishWord, list);
        if (size <= 0) {
            return list;
        }
        if (list.size() >= size) {
            return list.subList(0, size - 1);
        }
        return new ArrayList<EnglishWord>();
    }

    /**
     * 取得錯誤的清單(出錯無法找到類似題目時使用)
     */
    private List<EnglishWord> _getFillingErrorList(List<EnglishWord> list) {
        Map<String, EnglishWord> rtnMap = new LinkedHashMap<String, EnglishWord>();
        if (list != null) {
            for (EnglishWord e : list) {
                rtnMap.put(e.englishId, e);
            }
        }
        for (int ii = 0; rtnMap.size() < 4; ii++) {
            String msg = "錯誤,題目樣本不足!" + ii;
            EnglishWord e = new EnglishWord();
            e.englishId = msg;
            e.englishDesc = msg;
            rtnMap.put(e.englishId, e);
        }
        return new ArrayList<EnglishWord>(rtnMap.values());
    }
}
