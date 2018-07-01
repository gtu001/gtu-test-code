package com.example.englishtester;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.example.englishtester.EnglishwordInfoDAO.EnglishWord;
import com.example.englishtester.common.HermannEbbinghaus_Memory_Service;
import com.example.englishtester.common.ServiceUtil;
import com.example.englishtester.memory.IHermannEbbinghausMemoryAidlInterface;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class StatusInfoService {

    private static final String TAG = StatusInfoService.class.getSimpleName();

    EnglishwordInfoDAO dao;
    QuestionChoiceService questionChoiceService;
    private IHermannEbbinghausMemoryAidlInterface mService;
    Context context;


    StatusInfoService(Context context) {
        this.context = context;
        dao = new EnglishwordInfoDAO(context);
        questionChoiceService = new QuestionChoiceService(context);
        this.bindServiceMethod(true);
    }

    @Target(value = {ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE,
            ElementType.PACKAGE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface StatusInfoService_DataAnn {
        String name();

        int index();
    }

    public static class StatusInfoService_Data {
        @StatusInfoService_DataAnn(name = "最早匯入時間", index = 0)
        String minInputDate; //
        @StatusInfoService_DataAnn(name = "最晚匯入時間", index = 1)
        String maxInputDate; //
        @StatusInfoService_DataAnn(name = "最早測驗時間", index = 2)
        String minExamDate; //
        @StatusInfoService_DataAnn(name = "最晚測驗時間", index = 3)
        String maxExamDate; //
        @StatusInfoService_DataAnn(name = "平均瀏覽次數", index = 4)
        String avgBrowserTime; //
        @StatusInfoService_DataAnn(name = "平均測驗次數", index = 5)
        String avgExamTime; //
        @StatusInfoService_DataAnn(name = "總瀏覽次數", index = 6)
        String totalBrowserTime; //
        @StatusInfoService_DataAnn(name = "總測驗次數", index = 7)
        String totalExamTime; //
        @StatusInfoService_DataAnn(name = "平均回答時間", index = 8)
        String avgExamOkDuring; //
        @StatusInfoService_DataAnn(name = "總測驗時間(估計)", index = 9)
        String totalExamOkDuring; //
        @StatusInfoService_DataAnn(name = "總瀏覽時間(估計)", index = 10)
        String totalBrowserDuring; //
        @StatusInfoService_DataAnn(name = "平均正確率", index = 11)
        String avgCorrectRate; //
        @StatusInfoService_DataAnn(name = "錯誤次數分布", index = 12)
        String wrongMapping; //
        @StatusInfoService_DataAnn(name = "總共單字數", index = 13)
        String totalWordCount; //
        @StatusInfoService_DataAnn(name = "尚須測驗單字數", index = 14)
        String examNotOkCount; //
        @StatusInfoService_DataAnn(name = "本日瀏覽字數", index = 15)
        String todayBrowserTime; //
        @StatusInfoService_DataAnn(name = "本日測驗字數", index = 16)
        String todayExamTime; //
        @StatusInfoService_DataAnn(name = "10日瀏覽字數", index = 17)
        String tenDayBrowserTime; //
        @StatusInfoService_DataAnn(name = "10日測驗字數", index = 18)
        String tenDayExamTime; //
        @StatusInfoService_DataAnn(name = "本月瀏覽字數", index = 19)
        String thisMonthBrowserTime; //
        @StatusInfoService_DataAnn(name = "本月測驗字數", index = 20)
        String thisMonthExamTime; //
        @StatusInfoService_DataAnn(name = "艾賓浩斯記憶起始時間", index = 21)
        String hermannEbbinghausStartTime; //
        @StatusInfoService_DataAnn(name = "艾賓浩斯記憶最後執行時間", index = 22)
        String hermannEbbinghausLastestTime; //
        @StatusInfoService_DataAnn(name = "艾賓浩斯記憶最近一次更新筆數", index = 23)
        String hermannEbbinghausLastestUpdateCount; //
        @StatusInfoService_DataAnn(name = "艾賓浩斯記憶更新次數", index = 24)
        String hermannEbbinghausRuntime; //
    }

    StatusInfoService_Data getStatusInfo() {
        StatusInfoService_Data val = new StatusInfoService_Data();

        List<EnglishWord> list = dao.queryAll(true);

        //匯入時間
        long maxDateIn = 0L;
        long minDateIn = System.currentTimeMillis();
        for (EnglishWord eng : list) {
            if (eng.insertDate != 0) {
                maxDateIn = Math.max(maxDateIn, eng.insertDate);
                minDateIn = Math.min(minDateIn, eng.insertDate);
            }
        }

        val.minInputDate = DateFormatUtils.format(minDateIn, "yyyy/MM/dd HH:mm");//最早匯入時間
        val.maxInputDate = DateFormatUtils.format(maxDateIn, "yyyy/MM/dd HH:mm");//最晚匯入時間

        //測驗時間
        long maxDate = 0L;
        long minDate = System.currentTimeMillis();
        for (EnglishWord eng : list) {
            if (eng.examTime != 0) {
                maxDate = Math.max(maxDate, eng.examDate);
                minDate = Math.min(minDate, eng.examDate);
            }
        }

        val.minExamDate = DateFormatUtils.format(minDate, "yyyy/MM/dd HH:mm"); //最早測驗時間
        val.maxExamDate = DateFormatUtils.format(maxDate, "yyyy/MM/dd HH:mm");//最晚測驗時間

        long examTimeTotal = 0;
        long browserTimeTotal = 0;
        for (EnglishWord eng : list) {
            examTimeTotal += eng.examTime;
            browserTimeTotal += eng.browserTime;
        }
        if (list.size() == 0) {
            val.avgBrowserTime = browserTimeTotal + "次";//平均瀏覽次數
            val.avgExamTime = examTimeTotal + "次";//平均測驗次數
        } else {
            val.avgBrowserTime = (browserTimeTotal / list.size()) + "次";//平均瀏覽次數
            val.avgExamTime = (examTimeTotal / list.size()) + "次";//平均測驗次數
        }

        val.totalBrowserTime = browserTimeTotal + "次"; //總瀏覽次數
        val.totalExamTime = examTimeTotal + "次"; //總測驗次數

        //平均回答時間
        long wasteTotal = 0L;
        int quesQuantity = 0;
        for (EnglishWord eng : list) {
//            if(LastResultEnum.NOANSWER.val != eng.lastResult){
            if (eng.lastDuring != 0) {
                wasteTotal += eng.lastDuring;
                quesQuantity++;
            }
        }
        long avargaeExamTime = 0;
        try {
            avargaeExamTime = (wasteTotal / quesQuantity);
            Log.v(TAG, "avargaeExamTime = " + avargaeExamTime);
        } catch (Exception ex) {
            Log.v(TAG, "1計算錯誤:" + ex.getMessage());
        }

        val.avgExamOkDuring = avargaeExamTime + "毫秒";//平均回答時間
        val.totalExamOkDuring = wasteTotalTime(avargaeExamTime * examTimeTotal);//總測驗時間(估計)
        val.totalBrowserDuring = wasteTotalTime(avargaeExamTime * browserTimeTotal);//總瀏覽時間(估計)

        //平均正確率
        double examTime = 0;
        double failTime = 0;
        for (EnglishWord eng : list) {
            examTime += eng.examTime;
            failTime += eng.failTime;
        }
        double avage = 0;
        try {
            avage = (examTime - failTime) / examTime * 100;
            if (Double.isNaN(avage) || Double.isInfinite(avage)) {
                avage = 0;
            }
        } catch (Exception ex) {
            Log.v(TAG, "2計算錯誤:" + ex.getMessage());
        }
        BigDecimal avarageCorrectBig = new BigDecimal(avage);
        avarageCorrectBig = avarageCorrectBig.setScale(2, BigDecimal.ROUND_HALF_UP);

        val.avgCorrectRate = avarageCorrectBig.toString() + "%"; //平均正確率

        //錯誤次數分布
        Multiset<Integer> errSet = HashMultiset.create();
        for (EnglishWord eng : list) {
            errSet.add(eng.failTime);
        }
        val.wrongMapping = errSet.toString();//錯誤次數分布

        //總共單字數
        val.totalWordCount = String.valueOf(list.size()); //總共單字數

        //尚須測驗單字數
        val.examNotOkCount = String.valueOf(questionChoiceService.getGeneralFilterListSize()); //尚須測驗單字數

        //測驗次數統計
        int yesterdayBeginCount = 0;
        int tendayBeginCount = 0;
        int monthdayBeginCount = 0;
        int yesterdayBeginCount1 = 0;
        int tendayBeginCount1 = 0;
        int monthdayBeginCount1 = 0;
        long yesterdayBegin = getDateBegin(0);
        long tendayBegin = getDateBegin(-10);
        long monthdayBegin = getMonthBegin();
        for (EnglishWord eng : list) {
            if (eng.lastbrowerDate >= yesterdayBegin) {
                yesterdayBeginCount++;
            }
            if (eng.lastbrowerDate >= tendayBegin) {
                tendayBeginCount++;
            }
            if (eng.lastbrowerDate >= monthdayBegin) {
                monthdayBeginCount++;
            }
            if (eng.examDate >= yesterdayBegin) {
                yesterdayBeginCount1++;
            }
            if (eng.examDate >= tendayBegin) {
                tendayBeginCount1++;
            }
            if (eng.examDate >= monthdayBegin) {
                monthdayBeginCount1++;
            }
        }

        val.todayBrowserTime = String.valueOf(yesterdayBeginCount);//本日瀏覽字數
        val.todayExamTime = String.valueOf(yesterdayBeginCount1); //本日測驗字數
        val.tenDayBrowserTime = String.valueOf(tendayBeginCount); //10日瀏覽字數
        val.tenDayExamTime = String.valueOf(tendayBeginCount1);//10日測驗字數
        val.thisMonthBrowserTime = String.valueOf(monthdayBeginCount);//本月瀏覽字數
        val.thisMonthExamTime = String.valueOf(monthdayBeginCount1);//本月測驗字數

        try {
            Map memoryStateInfo = mService.getMemoryStateInfo();
            val.hermannEbbinghausStartTime = (String) memoryStateInfo.get(HermannEbbinghaus_Memory_Service.IHermannEbbinghausMemoryAidlInterface_KEY.START_TIME);
            val.hermannEbbinghausLastestTime = (String) memoryStateInfo.get(HermannEbbinghaus_Memory_Service.IHermannEbbinghausMemoryAidlInterface_KEY.LASTEST_TIME);
            val.hermannEbbinghausRuntime = (String) memoryStateInfo.get(HermannEbbinghaus_Memory_Service.IHermannEbbinghausMemoryAidlInterface_KEY.RUNTIME);
            val.hermannEbbinghausLastestUpdateCount = (String) memoryStateInfo.get(HermannEbbinghaus_Memory_Service.IHermannEbbinghausMemoryAidlInterface_KEY.UPDATE_COUNT);
        } catch (Exception ex) {
            Log.e(TAG, "getMemoryStateInfo ERR : " + ex.getMessage(), ex);
            val.hermannEbbinghausStartTime = "NA";
            val.hermannEbbinghausLastestTime = "NA";
            val.hermannEbbinghausRuntime = "NA";
            val.hermannEbbinghausLastestUpdateCount = "NA";
        }
        return val;
    }

    private long getDateBegin(int diff) {
        java.util.Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, diff);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Log.v("===>", DateFormatUtils.format(cal.getTimeInMillis(), "yyyyMMdd HHmmss"));
        return cal.getTimeInMillis();
    }

    private long getMonthBegin() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Log.v("===>", DateFormatUtils.format(cal.getTimeInMillis(), "yyyyMMdd HHmmss"));
        return cal.getTimeInMillis();
    }

    /**
     * 計算所耗時間
     *
     * @param wasteTotal
     * @return
     */
    public static String wasteTotalTime(long wasteTotal) {
        wasteTotal = wasteTotal / 1000;
        long sec_ = wasteTotal % 60;
        wasteTotal = wasteTotal / 60;
        long min = wasteTotal % 60;
        wasteTotal = wasteTotal / 60;
        long hours = wasteTotal % 24;
        wasteTotal = wasteTotal / 24;
        return wasteTotal + "天" + hours + "時" + min + "分" + sec_ + "秒";
    }

    @Override
    public void finalize() {
        this.bindServiceMethod(false);
    }

    private void bindServiceMethod(boolean isOn) {
        if (ServiceUtil.isServiceRunning(context, HermannEbbinghaus_Memory_Service.class)) {
            Intent intent = new Intent(context, HermannEbbinghaus_Memory_Service.class);
            if (isOn) {
                context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            } else {
                context.unbindService(mConnection);
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
            mService = IHermannEbbinghausMemoryAidlInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.v(TAG, "onServiceDisconnected called");
            mService = null;
        }
    };
}
