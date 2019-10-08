package com.example.gtu001.qrcodemaker.dao;

import android.app.ActivityManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.gtu001.qrcodemaker.BuildConfig;
import com.example.gtu001.qrcodemaker.common.Log;

public class DBConnection extends SQLiteOpenHelper {
    private static final String TAG = DBConnection.class.getSimpleName();

    static final String DATABASE_NAME = "Youtube_DB";
    static final int DATABASE_VERSION = 1;

    public DBConnection(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // context - 用來創建/開啟資料庫
        // name - 資料庫名稱, 設定為null 表示資料庫建立於記憶體
        // factory - 用來建立Cursor所指的目標
        // version - 資料庫版本 , 可用Call Back程式 onUpdate()來更新資料庫
    }

    private static DBConnection _INST;

    public static synchronized DBConnection getInstance(Context context) {
        if (_INST == null){
            _INST = new DBConnection(context);
        }
        return _INST;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb = new StringBuilder();

        //youtube清單
        sb.append("  create table Youtube_Video (                  ");
        sb.append("      video_id text primary key not null,        ");
        sb.append("      title text ,                               ");
        sb.append("      video_url text ,                           ");
        sb.append("      click_time integer not null,                ");
        sb.append("      insert_date long not null,                  ");
        sb.append("      latest_click_date long not null               ");
        sb.append("  );                                              ");

        Log.i("haiyang:createDB 1=", sb.toString());
        db.execSQL(sb.toString());

        //APP清單
        sb.setLength(0);
        sb.append("  create table App_Info (                  ");
        sb.append("      installed_package text primary key not null,        ");
        sb.append("      source_dir text ,                               ");
        sb.append("      label text ,                           ");
        sb.append("      icon text ,                           ");
        sb.append("      tag text                            ");
        sb.append("  );                                              ");

        Log.i("haiyang:createDB 1=", sb.toString());
        db.execSQL(sb.toString());
        sb.setLength(0);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        StringBuilder sb = new StringBuilder();

        if (newVersion == 999) {
            // txt查單字紀錄表
            sb = new StringBuilder();
//            sb.append("  alter table recent_txt_mark add insert_Date_Dtype DATETIME   ");//add column
//            sb.append("  DROP table recent_txt_mark ;   ");//add column

            sb.append("  alter table recent_txt_mark add page_index integer    ");//add column


            Log.i("haiyang:createDB 1=", sb.toString());
            db.execSQL(sb.toString());
        }

        if (newVersion == 9999) {
            sb = new StringBuilder();

            sb.append("  alter table App_Info add tag text    ");//add column

            Log.i("haiyang:createDB 2=", sb.toString());
            db.execSQL(sb.toString());
        }
    }

    /**
     * drop資料表
     */
    private void dropTable(SQLiteDatabase db, String tableName) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName); //刪除舊有的資料表
    }

    /**
    * 判斷是否可存取DB
     */
    public static boolean isAccessDBPermission(Context context){
        //    If only the main process in the application database operations there, while other applications related to the process does not require operation of the database, I recommend to initialize the database plus operating conditions, like this:
        if(getCurrentProcessName(context).equals(BuildConfig.APPLICATION_ID)) {
            // initialize the database
            return true;
        }
        return false;
    }

    private static String getCurrentProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return "";
    }
}
