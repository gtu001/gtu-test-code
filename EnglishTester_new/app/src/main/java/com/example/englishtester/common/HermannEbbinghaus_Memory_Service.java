package com.example.englishtester.common;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.core.v2.DbxClientV2;
import com.example.englishtester.DropboxApplicationActivity;
import com.example.englishtester.EnglishwordInfoDAO;
import com.example.englishtester.RecentSearchDAO;
import com.example.englishtester.memory.IHermannEbbinghausMemoryAidlInterface;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

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
    private AtomicReference<MemoryStateInfo> state = new AtomicReference<MemoryStateInfo>();
    private static final long SERVICE_PERIOD = 1 * 60 * 60 * 1000;
    ;

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
        return mBinderNew;
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
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                uploadMemeryBank(UPLOAD_SIZE);

                addRuntimeRecord();

                handler.postDelayed(this, SERVICE_PERIOD);
            }
        };

        handler.postDelayed(runnable, 1);
    }

    public void uploadMemeryBank(int size) {
        int totalSize = recentSearchDAO.queryNeedUploadSize();
        Log.v(TAG, "uploadMemeryBank : 筆數 : " + totalSize);

        if (totalSize >= size) {
            Log.v(TAG, "uploadMemeryBank : start ");

            MemoryBankDropboxProcess dropboxHandler = new MemoryBankDropboxProcess();
            dropboxHandler.downloadProperties();//先下載 config

            List<RecentSearchDAO.RecentSearch> lst = recentSearchDAO.queryNeedUpload_byRegisterTime(getInsertTimeBegin());

            getMemoryStateInfo().updateCount = lst.size();

            Properties prop = this.getPropertiesFromRecentSearchLst(lst);

            dropboxHandler.uploadProperties(prop);//上傳 config

            this.updateRecentSearchLst(lst);

            Log.v(TAG, "uploadMemeryBank : end ");
        }
    }

    @Override
    public void onStart(Intent intent, int startid) {
        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
    }

    private long getInsertTimeBegin() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTimeInMillis();
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

        AtomicBoolean idDownloadPropertiesOk = new AtomicBoolean(false);
        AtomicBoolean idUploadPropertiesOk = new AtomicBoolean(false);

        private MemoryBankDropboxProcess() {
            fileName = "EnglishSearchUI_MemoryBank.properties";
            file = new File(context.getCacheDir(), fileName);
            client = DropboxUtilV2.getClient(dropboxAccessToken);
            dropboxPath = "/etc_config/" + fileName;
        }

        private void downloadProperties() {
            getFutureResult(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    try {
                        boolean result = DropboxUtilV2.download(dropboxPath, new FileOutputStream(file), client);
                        if (result && file.exists()) {
                            Log.v(TAG, "downloadProperties : 佔存檔儲存ok : " + file.length());
                            idDownloadPropertiesOk.set(true);
                        } else {
                            throw new Exception("downloadProperties : 檔案下載失敗!");
                        }
                    } catch (Exception ex) {
                        throw new RuntimeException("downloadProperties ERR : " + ex.getMessage(), ex);
                    }
                    return null;
                }
            });
        }

        private void uploadProperties(final Properties prop) {
            getFutureResult(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    try {
                        Properties newProp = new Properties();
                        newProp.load(new FileInputStream(file));
                        newProp.putAll(prop);
                        newProp.store(new FileOutputStream(file), "Phone Add " + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd_HHmmss"));

                        boolean result1 = DropboxUtilV2.delete(dropboxPath, client);
                        Log.v(TAG, "uploadProperties DELETE : " + result1);

                        boolean result2 = DropboxUtilV2.upload(dropboxPath, new FileInputStream(file), client);
                        Log.v(TAG, "uploadProperties UPLOAD : " + result2);

                        idUploadPropertiesOk.set(result2);
                    } catch (Exception ex) {
                        throw new RuntimeException("uploadProperties ERR : " + ex.getMessage(), ex);
                    }
                    return null;
                }
            });
        }

        private <T> T getFutureResult(Callable<T> callable) {
            final int TIMEOUT = 30000;
            ExecutorService service = Executors.newCachedThreadPool();
            Future<T> future = service.submit(callable);
            try {
//                return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
                return future.get();
            } catch (Exception e) {
                e.printStackTrace();
                future.cancel(true);
                return null;
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

    private MemoryStateInfo getMemoryStateInfo() {
        if (state.get() == null) {
            state.set(new MemoryStateInfo());
        }
        return state.get();
    }

    private void addRuntimeRecord() {
        getMemoryStateInfo().runtime++;
        getMemoryStateInfo().lastestTime = new Date();
    }

    // 與activity連線 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ (新的寫法)

    public interface IHermannEbbinghausMemoryAidlInterface_KEY {
        String START_TIME = "startTime";
        String LASTEST_TIME = "lastestTime";
        String RUNTIME = "runtime";
        String UPDATE_COUNT = "updateCount";
    }

    private final IHermannEbbinghausMemoryAidlInterface.Stub mBinderNew = new IHermannEbbinghausMemoryAidlInterface.Stub() {
        @Override
        public Map getMemoryStateInfo() throws RemoteException {
            Map<String, String> infoMap = new HashMap<>();
            String startTime = "NA";
            String lastestTime = "NA";
            String runtime = "NA";
            String updateCount = "NA";
            if (state.get() != null) {
                startTime = DateFormatUtils.format(state.get().startTime, "yyyy/MM/dd HH:mm:ss");
                lastestTime = DateFormatUtils.format(state.get().lastestTime, "yyyy/MM/dd HH:mm:ss");
                runtime = String.valueOf(state.get().runtime);
                updateCount = String.valueOf(state.get().updateCount);
            }
            infoMap.put(IHermannEbbinghausMemoryAidlInterface_KEY.START_TIME, startTime);
            infoMap.put(IHermannEbbinghausMemoryAidlInterface_KEY.LASTEST_TIME, lastestTime);
            infoMap.put(IHermannEbbinghausMemoryAidlInterface_KEY.RUNTIME, runtime);
            infoMap.put(IHermannEbbinghausMemoryAidlInterface_KEY.UPDATE_COUNT, updateCount);
            return infoMap;
        }
    };
    // 與activity連線↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ (新的寫法)

    private class MemoryStateInfo {
        Date startTime;
        Date lastestTime;
        int runtime = 0;
        int updateCount = 0;

        MemoryStateInfo() {
            startTime = new Date();
        }
    }
}
