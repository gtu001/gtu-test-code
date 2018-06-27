package com.example.englishtester.common;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.dropbox.core.v2.DbxClientV2;
import com.example.englishtester.DropboxApplicationActivity;
import com.example.englishtester.EnglishwordInfoDAO;
import com.example.englishtester.RecentSearchDAO;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import gtu._work.etc.EnglishTester_Diectory;
import gtu._work.etc.EnglishTester_Diectory_Factory;

/**
 * Created by wistronits on 2018/6/27.
 */

public class HermannEbbinghaus_Memory_Service extends Service {

    private static final String TAG = HermannEbbinghaus_Memory_Service.class.getSimpleName();
    private static final String DATE_FORMAT = "yyyy-MM-dd_HH:mm:ss";
    private static final SimpleDateFormat SDF = new SimpleDateFormat(DATE_FORMAT);

    private RecentSearchDAO recentSearchDAO;
    private EnglishwordInfoDAO englishDAO;
    private Context context;
    private Handler handler = new Handler();
    private EnglishTester_Diectory_Factory engFactory = new EnglishTester_Diectory_Factory();
    private String dropboxAccessToken;
    private final static int UPLOAD_SIZE = 20;

    private Thread singleThread;

    //↓↓↓↓↓↓↓↓ service logical ------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "oncreat");

        context = this.getApplicationContext();
        recentSearchDAO = new RecentSearchDAO(this.getApplicationContext());
        englishDAO = new EnglishwordInfoDAO(this.getApplicationContext());
        dropboxAccessToken = DropboxApplicationActivity.getDropboxAccessToken(this.getApplicationContext());

        this.initRunningThread();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


    //↓↓↓↓↓↓↓↓ business logical ------------------------------------------------------------------------------------------------------------------------------------

    private void initRunningThread() {
        if (singleThread == null || singleThread.getState() == Thread.State.TERMINATED) {
            singleThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        uploadMemeryBank(UPLOAD_SIZE);

                        try {
                            Thread.sleep(1 * 60 * 60 * 1000);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            });

            singleThread.start();
        }
    }

    public void uploadMemeryBank(int size) {
        int totalSize = recentSearchDAO.queryNeedUploadSize();
        Log.v(TAG, "uploadMemeryBank : 筆數 : " + totalSize);

        if (totalSize >= size) {
            Log.v(TAG, "uploadMemeryBank : start ");

            MemoryBankDropboxProcess dropboxHandler = new MemoryBankDropboxProcess();
            dropboxHandler.downloadProperties();//先下載 config

            List<RecentSearchDAO.RecentSearch> lst = recentSearchDAO.queryNeedUpload(size);

            Properties prop = this.getPropertiesFromRecentSearchLst(lst);

            dropboxHandler.uploadProperties(prop);//上傳 config

            this.updateRecentSearchLst(lst);

            Log.v(TAG, "uploadMemeryBank : end ");
        }
    }

    private void updateRecentSearchLst(List<RecentSearchDAO.RecentSearch> lst) {
        for (RecentSearchDAO.RecentSearch v : lst) {
            v.setUploadType("DONE");
            recentSearchDAO.updateWord(v);
        }
    }

    private Properties getPropertiesFromRecentSearchLst(List<RecentSearchDAO.RecentSearch> lst) {
        Properties prop = new Properties();
        for (RecentSearchDAO.RecentSearch v : lst) {
            String word = StringUtils.trimToEmpty(v.getEnglishId()).toLowerCase();
            EnglishwordInfoDAO.EnglishWord engWord = englishDAO.queryOneWord(word);

            try {
                MemData memData = null;
                if (engWord == null) {
                    EnglishTester_Diectory.WordInfo engWord2 = engFactory.parseToWordInfo(word, context, handler);
                    memData = this.getMemData(engWord2.getEnglishId(), engWord2.getMeaning(), v.getInsertDate());
                } else {
                    memData = this.getMemData(engWord.getEnglishId(), engWord.getEnglishDesc(), v.getInsertDate());
                }

                prop.setProperty(memData.getKey(), memData.toValue());
            } catch (Exception ex) {
                Log.e(TAG, "[ERROR] MemoryBank EnglishId : " + word + " " + ex.getMessage(), ex);
            }
        }
        return prop;
    }

    private class MemoryBankDropboxProcess {
        String fileName;
        File file;
        DbxClientV2 client;
        String dropboxPath;

        private MemoryBankDropboxProcess() {
            fileName = "EnglishSearchUI_MemoryBank.properties";
            file = new File(context.getCacheDir(), fileName);
            client = DropboxUtilV2.getClient(dropboxAccessToken);
            dropboxPath = "/etc_config/" + fileName;
        }

        private void downloadProperties() {
            try {
                boolean result = DropboxUtilV2.download(dropboxPath, new FileOutputStream(file), client);
                if (result && file.exists()) {
                    Log.v(TAG, "uploadProperties : 佔存檔儲存ok : " + file.length());
                } else {
                    throw new Exception("downloadProperties : 檔案下載失敗!");
                }
            } catch (Exception ex) {
                throw new RuntimeException("downloadProperties ERR : " + ex.getMessage(), ex);
            }
        }

        private boolean uploadProperties(Properties prop) {
            try {
                Properties newProp = new Properties();
                newProp.load(new FileInputStream(file));
                newProp.putAll(prop);
                newProp.store(new FileOutputStream(file), "Phone Add " + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd_HHmmss"));

                boolean result1 = DropboxUtilV2.delete(dropboxPath, client);
                Log.v(TAG, "uploadProperties DELETE : " + result1);

                boolean result2 = DropboxUtilV2.upload(dropboxPath, new FileInputStream(file), client);
                Log.v(TAG, "uploadProperties UPLOAD : " + result2);
                return result2;
            } catch (Exception ex) {
                throw new RuntimeException("uploadProperties ERR : " + ex.getMessage(), ex);
            }
        }
    }


    //↓↓↓↓↓↓↓ 與 桌面版 同 ----------------------------------------------------------------------------------------------------------

    public MemData getMemData(String key, String remark, long registerTime) {
        Date regDate = new Date(registerTime);
        MemData d = new MemData();
        d.key = key;
        d.registerTime = regDate;
        d.fixedTime = regDate;
        d.setRemark(remark);
        d.resetReviewTime();
        return d;
    }

    private enum ReviewTime {
        M1(5), // 1．第一個記憶週期：5分鐘
        M2(30), // 2．第二個記憶週期：30分鐘
        M3(12 * 60), // 3．第三個記憶週期：12小時
        M4(1 * 24 * 60), // 4．第四個記憶週期：1天
        M5(2 * 24 * 60), // 5．第五個記憶週期：2天
        M6(4 * 24 * 60), // 6．第六個記憶週期：4天
        M7(7 * 24 * 60), // 7．第七個記憶週期：7天
        M8(15 * 24 * 60), // 8．第八個記憶週期：15天
        NONE(-1),//
        ;
        final float min;

        ReviewTime(float min) {
            this.min = min;
        }

        private static ReviewTime getNext(String name) {
            ReviewTime v = ReviewTime.valueOf(name);
            if (v.ordinal() + 1 >= ReviewTime.NONE.ordinal()) {
                return ReviewTime.NONE;
            }
            return ReviewTime.values()[v.ordinal() + 1];
        }

        private static ReviewTime getInitialReviewTime() {
            return ReviewTime.values()[0];
        }
    }

    public static class MemData {
        String key;
        Date registerTime;
        Date fixedTime;
        String reviewTime;
        String remark;
        long waitingTriggerTime = -1;// 設定值於此 則可自訂trigger時間

        public MemData() {
        }

        public MemData(String key, String value) {
            try {
                String[] arry = StringUtils.trimToEmpty(value).split("\\^", -1);
                this.key = key;
                this.reviewTime = getArry(0, arry);
                this.registerTime = __getDateFromString(getArry(1, arry));
                this.fixedTime = __getDateFromString(getArry(2, arry));
                this.remark = getArry(3, arry);
            } catch (Exception e) {
                throw new RuntimeException("MemData ERR : " + e.getMessage(), e);
            }
        }

        private Date __getDateFromString(String dateStr) {
            try {
                return SDF.parse(dateStr);
            } catch (Exception ex) {
                return new Date();
            }
        }

        String toValue() {
            return this.reviewTime + "^" + DateFormatUtils.format(this.registerTime, DATE_FORMAT) + "^" + DateFormatUtils.format(this.fixedTime, DATE_FORMAT) + "^" + this.remark;
        }

        private String getArry(int index, String[] arry) {
            if (arry.length > index) {
                return arry[index];
            }
            return "";
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Date getRegisterTime() {
            return registerTime;
        }

        public void setRegisterTime(Date registerTime) {
            this.registerTime = registerTime;
        }

        public String getReviewTime() {
            return reviewTime;
        }

        public void resetReviewTime() {
            this.reviewTime = ReviewTime.getInitialReviewTime().name();
        }

        public void setReviewTime(String reviewTime) {
            this.reviewTime = reviewTime;
        }

        public String getDateFormat() {
            return DATE_FORMAT;
        }

        public String getRemark() {
            return Base64JdkUtil.decode(remark);
        }

        public void setRemark(String remark) {
            this.remark = Base64JdkUtil.encode(remark);
        }

        public long getWaitingTriggerTime() {
            return waitingTriggerTime;
        }

        public void setWaitingTriggerTime(long waitingTriggerTime) {
            this.waitingTriggerTime = waitingTriggerTime;
        }

        public boolean isCustomWaitingTrigger() {
            return this.waitingTriggerTime >= 0;
        }

        public Date getFixedTime() {
            return fixedTime;
        }

        public void setFixedTime(Date fixedTime) {
            this.fixedTime = fixedTime;
        }
    }
}
