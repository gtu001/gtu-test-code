package com.example.englishtester;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.englishtester.EnglishwordInfoDAO.EnglishWord;
import com.example.englishtester.EnglishwordInfoDAO.EnglishWordSchema;
import com.example.englishtester.EnglishwordInfoDAO.LastResultEnum;
import com.example.englishtester.common.FileConstantAccessUtil;

public class QuestionChoiceService {

    private static final String TAG = QuestionChoiceService.class.getSimpleName();

    final EnglishwordInfoDAO dao;
    Context context;

    private static long ONE_MONTH_BEFORE_TIME = 0L;

    public QuestionChoiceService(Context context) {
        this.context = context;
        this.dao = new EnglishwordInfoDAO(context);
    }

    Map<String, EnglishWord> queryForExam_NewWordAll() {
        Log.v(TAG, "queryForExam_NewWordAll");
        List<EnglishWord> list = generalFilterList();
        List<EnglishWord> newList = new ArrayList<EnglishWord>();
        for (EnglishWord e : list) {
            if (e.examDate == 0 || e.examTime == 0) {
                newList.add(e);
            }
        }
        Map<String, EnglishWord> prop = filterFetchNeed(newList, 200);
        return prop;
    }

    private Map<String, EnglishWord> filterFetchNeed(List<EnglishWord> newList, int limit) {
        Map<String, EnglishWord> prop = new HashMap<String, EnglishWord>();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -5);
        long timeBeforeLong = cal.getTimeInMillis();
        for (int ii = 0; ii < newList.size(); ii++) {
            EnglishWord word = newList.get(ii);
            if (timeBeforeLong < word.lastbrowerDate) {
                Log.v(TAG, "remove " + word.englishId + " --> " + word.lastbrowerDate);
                newList.remove(ii);
                ii--;
            }
        }
        Collections.sort(newList, new Comparator<EnglishWord>() {
            @Override
            public int compare(EnglishWord paramT1, EnglishWord paramT2) {
                // 瀏覽次數少的優先
                // if (paramT1.browserTime < paramT2.browserTime) {
                // return -1;
                // }
                // if (paramT1.browserTime > paramT2.browserTime) {
                // return 1;
                // }
                // 久沒瀏覽的優先
                if (paramT1.lastbrowerDate < paramT2.lastbrowerDate) {
                    return -1;
                }
                if (paramT1.lastbrowerDate > paramT2.lastbrowerDate) {
                    return 1;
                }
                return 0;
            }
        });
        int size = 0;
        for (int ii = 0; ii < newList.size(); ii++) {
            EnglishWord word = newList.get(ii);
            prop.put(word.englishId, word);
            size++;
            if (size >= limit) {
                break;
            }
        }
        return prop;
    }

    Map<String, EnglishWord> queryForExam_LongtimeNoExam50() {
        Log.v(TAG, "queryForExam_LongtimeNoExam50");
        Map<String, EnglishWord> prop = new HashMap<String, EnglishWord>();
        List<EnglishWord> list = generalFilterList();

        // //五小時內側過免測
        // Calendar cal = Calendar.getInstance();
        // cal.add(Calendar.HOUR, -5);
        // long timeBeforeLong = cal.getTimeInMillis();
        // for(int ii = 0 ; ii < list.size() ; ii ++){
        // EnglishWord word = list.get(ii);
        // if(timeBeforeLong < word.lastbrowerDate){
        // Log.v(TAG, "remove "+word.englishId + " --> " + word.lastbrowerDate);
        // list.remove(ii);
        // ii --;
        // }
        // }
        // //以最後測驗時間排續
        // Collections.sort(list, new Comparator<EnglishWord>() {
        // @Override
        // public int compare(EnglishWord paramT1, EnglishWord paramT2) {
        // if (paramT1.examDate < paramT2.examDate) {
        // return -1;
        // }
        // if (paramT1.examDate > paramT2.examDate) {
        // return 1;
        // }
        // return 0;
        // }
        // });

        Collections.sort(list, new Comparator<EnglishWord>() {
            @Override
            public int compare(EnglishWord paramT1, EnglishWord paramT2) {
                if (paramT1.lastbrowerDate < paramT2.lastbrowerDate) {
                    return -1;
                }
                if (paramT1.lastbrowerDate > paramT2.lastbrowerDate) {
                    return 1;
                }
                return 0;
            }
        });

        int size = 0;
        for (EnglishWord e : list) {
            // if (e.examDate == 0) {
            // continue;
            // }
            prop.put(e.englishId, e);
            size++;
            if (size == 50) {
                break;
            }
        }
        Log.v(TAG, "prop size = " + prop.size());
        return prop;
    }

    Map<String, EnglishWord> queryForExam_LongtimeNoBrowser() {
        Log.v(TAG, "queryForExam_LongtimeNoBrowser");
        // List<EnglishWord> list = generalFilterList();
        List<EnglishWord> list = dao.queryAll(false);
        Map<String, EnglishWord> prop = filterFetchNeed(list, 200);
        return prop;
    }

    Map<String, EnglishWord> queryForExam_Wrong() {
        Log.v(TAG, "queryForExam_Wrong");
        Map<String, EnglishWord> prop = new HashMap<String, EnglishWord>();
        List<EnglishWord> list = dao.query(//
                EnglishWordSchema.LAST_RESULT + "=" + LastResultEnum.WRONG.val, null);
        for (EnglishWord e : list) {
            prop.put(e.englishId, e);
        }
        Log.v(TAG, "prop size = " + prop.size());
        return prop;
    }

    int getGeneralFilterListSize() {
        List<EnglishWord> list = generalFilterList();
        return list.size();
    }

    private long getOneMonthBeforeTime() {
        if (ONE_MONTH_BEFORE_TIME == 0L) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            ONE_MONTH_BEFORE_TIME = cal.getTimeInMillis();
        }
        return ONE_MONTH_BEFORE_TIME;
    }

    Object[] isNeedTest(EnglishWord e) {
        int maxExamTime = 5;

        long oneMonthBeforeTime = getOneMonthBeforeTime();

        float result = ((float) e.failTime / (float) e.examTime);

        boolean ok1 = e.examTime >= maxExamTime;// 以測驗過5次以上
        boolean ok2 = result <= 0.3f;// 錯誤率小於30%
        boolean ok3 = e.examDate > oneMonthBeforeTime;// 一個月內測過
        boolean ok4 = e.lastResult == LastResultEnum.CORRECT.val;// 最後一次答對
        boolean ok5 = this.isRescentTime(e);// 短時間內側過

        if (ok4 && ok5) {
            Log.v(TAG, "最近測驗 : " + e.englishId);
            return new Object[] { false, "最近測驗過" };
        }

        // if (ok1 && ok2 && ok3 && ok4) {
        if (ok1 && ok2 && ok4) {
            Log.v(TAG, "移掉熟字 : " + e.englishId);
            return new Object[] { false, "不用" };
        }

        return new Object[] { true, "需要" };
    }

    private List<EnglishWord> generalFilterList() {
        List<EnglishWord> list = dao.queryAll(false);
        int remove = 0;
        for (int ii = 0; ii < list.size(); ii++) {
            EnglishWord e = list.get(ii);

            Object[] testResult = isNeedTest(e);
            if (((Boolean) testResult[0]) == false) {
                list.remove(ii);
                ii--;
                remove++;
            }
        }
        Log.v(TAG, "remove : " + remove);
        return list;
    }

    boolean isRescentTime(EnglishWord e) {
        long recentTime = 0;
        boolean findOk = false;
        for (RecentTestTime x : RecentTestTime.values()) {
            if (x.time == (e.examTime - e.failTime)) {
                findOk = true;
                recentTime = x.recentTime;
                break;
            }
        }
        if (!findOk) {
            recentTime = RecentTestTime.Other.recentTime;
        }
        recentTime = System.currentTimeMillis() - recentTime;
        if (e.examDate > recentTime) {
            return true;
        }
        return false;
    }

    /**
     * 尋找單字無圖片資料
     */
    protected void writeNoPicturesWords(final SwitchPictureService switchPictureService) {
        final String fileName = Constant.EXPORT_NO_PICTURES_FILE.getName();
        final ProgressDialog myDialog = ProgressDialog.show(context, "掃描"+fileName+"單字", "計算中...", true);
        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // List<EnglishWord> list = dao.queryAll();
                File searchFile = FileConstantAccessUtil.getFile(context, Constant.EXPORT_NO_PICTURES_FILE);
                if (!searchFile.exists()) {
                    myDialog.cancel();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, ""+fileName+"不存在", Toast.LENGTH_LONG).show();
                        }
                    });
                    return;
                }
                Properties srcProp = new Properties();
                try {
                    srcProp.load(new FileInputStream(searchFile));
                } catch (Exception e1) {
                    Log.e(TAG, e1.getMessage(), e1);
                }

                Properties prop = new Properties();
                for (Enumeration enu = srcProp.keys(); enu.hasMoreElements();) {
                    String word = (String) enu.nextElement();
                    switchPictureService.showPictureInner(word);
                    if (switchPictureService.picList.isEmpty()) {
                        prop.setProperty(word, srcProp.getProperty(word));
                    }
                }
                try {
                    prop.store(new FileOutputStream(searchFile), "no picture");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.v(TAG, e.getMessage(), e);
                }
                myDialog.cancel();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "無圖片單字檔案寫入成功!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        thread.start();
    }

    enum RecentTestTime {
        Zero(0, 3 * 60 * 60 * 1000), //
        One(1, 3 * 60 * 60 * 1000), //
        Two(2, 5 * 60 * 60 * 1000), //
        Three(3, 8 * 60 * 60 * 1000), //
        Four(4, 8 * 60 * 60 * 1000), //
        Five(5, 3 * 24 * 60 * 60 * 1000), //
        Six(6, 3 * 24 * 60 * 60 * 1000), //
        Other(999, 3 * 24 * 60 * 60 * 1000), //
        ;

        final int time;
        final long recentTime;

        RecentTestTime(int time, long recentTime) {
            this.time = time;
            this.recentTime = recentTime;
        }
    }
}
