package com.example.englishtester;

import android.content.Context;
import android.os.Environment;

import com.example.englishtester.common.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.security.auth.login.LoginException;

public class Constant {

    private static final String TAG = Constant.class.getSimpleName();

    static {
        Properties prop = new Properties();
        try {
            Log.v("!!!!!!", "externalStorageDirectory = " + Environment.getExternalStorageDirectory()); ///storage/sdcard0

            String prefix = Environment.getExternalStorageDirectory().getAbsolutePath();

            //確定SD卡可讀寫
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                Log.v(TAG, "可讀寫SD卡!");
            }

            //prop.load(Constant.class.getResource("constant.properties").openStream());

            /*
            PropertiesFindActivity_PATH = mkdir(prefix + "/" + prop.getProperty("PropertiesFindActivity_PATH"));
            PicFindActivity_PATH = mkdir(prefix + "/" + prop.getProperty("PicFindActivity_PATH"));

            MainActivityDTO_picDir_PATH = mkdir(prop.getProperty("MainActivityDTO_picDir_PATH_forSDCard"));

            Dropbox_accessToken = prop.getProperty("Dropbox_accessToken");
            */

            PropertiesFindActivity_PATH = mkdir(prefix + "/" + "/My Documents/english/");

            PropertiesFindActivity_Config_PATH = mkdir(prefix + "/" + "/My Documents/english/config/");

            PicFindActivity_PATH = mkdir(prefix + "/" + "/My Documents/english/");

//            MainActivityDTO_picDir_PATH = mkdir("/storage/sdcard1/Android/data/com.ghisler.android.TotalCommander/My Documents/english/english_pic");
            MainActivityDTO_picDir_PATH = mkdir("/storage/1D0E-2671/Android/data/com.ghisler.android.TotalCommander/My Documents/english/english_pic");

        } catch (Exception e) {
            Log.e(TAG, "Load Constant ERROR : " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
        Log.v(TAG, "====>" + prop);
    }

    /**
     * 取得題庫的預設目錄
     */
    public static String PropertiesFindActivity_PATH;
    /**
     * 取得設定檔預設目錄
     */
    public static String PropertiesFindActivity_Config_PATH;
    /**
     * 翻譯與dropbox token設定
     */
    public static File APP_CONFIG_FILE = new File(Constant.PropertiesFindActivity_Config_PATH + "/appConfig.properties");
    /**
     * 取得圖檔的預設目錄
     */
    public static String PicFindActivity_PATH;
    /**
     * 預設圖檔位置
     */
    public static String MainActivityDTO_picDir_PATH;
    /**
     * 預設錯誤儲存檔案
     */
    public static File ERROR_MIX_FILE = new File(Constant.PropertiesFindActivity_PATH + "/errorMix.properties");
    /**
     * 題目備份檔
     */
    public static File EXPORT_FILE_XML = new File(Constant.PropertiesFindActivity_Config_PATH + "/exportFileXml.bin");
    /**
     * 題目備份檔
     */
    public static File EXPORT_FILE_JSON = new File(Constant.PropertiesFindActivity_Config_PATH + "/exportFileJson.bin");
    /**
     * 題目備份檔
     */
    public static File EXPORT_FILE_JAVA = new File(Constant.PropertiesFindActivity_Config_PATH + "/exportFileJava.bin");
    /**
     * 題目備份檔 (主檔)
     */
    public static File EXPORT_FILE = EXPORT_FILE_JAVA;
    /**
     * 中斷恢復檔
     */
    public static File INTERRUPT_FILE = new File(Constant.PropertiesFindActivity_Config_PATH + "/interruptFile.bak");
    /**
     * 中斷恢復檔
     */
    public static File INTERRUPT_INIT_FILE = new File(Constant.PropertiesFindActivity_Config_PATH + "/interruptInitFile.bak");
    /**
     * 測驗耗時上限
     */
    public static long FAIL_RECORD_DURNING = 60000;
    /**
     * 沒有圖片的單字匯出黨
     */
    public static File EXPORT_NO_PICTURES_FILE = new File(Constant.PropertiesFindActivity_PATH + "/noPicture.properties");
    /**
     * 註記要刪除的圖片
     */
    public static File DELETE_PIC_MARKS_FILE = new File(Constant.PropertiesFindActivity_Config_PATH + "/deletePicMarks.properties");
    /**
     * 最新查詢過的單字檔案
     */
    public static File SEARCHWORD_FILE = new File(Constant.PropertiesFindActivity_PATH + "/newSearchWord.properties");

    /**
     * AppConfig 百度appid
     */
    public static String APPCONFIG__BAIDU_APPID_KEY = "baidu_appid";
    /**
     * AppConfig 百度app secret
     */
    public static String APPCONFIG__BAIDU_APPSECRET_KEY = "baidu_key";
    /**
     * AppConfig dropbox token
     */
    public static String APPCONFIG__DROPBOX_TOKEN_KEY = "dropbox_token";

    //method 放在此線下 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    private static String mkdir(String paths) {
        File file = new File(paths);
        if (!file.exists()) {
            boolean makeDefaultDir = file.mkdirs();
            Log.v(TAG, "建立基礎目錄 - " + makeDefaultDir + " - " + file);
        }
        return paths;
    }

    private static Properties _APP_CONFIG;

    public static Properties getAppConfig() {
        if (_APP_CONFIG == null) {
            Properties prop = new Properties();
            try {
                if (!Constant.APP_CONFIG_FILE.exists()) {
                    return prop;
                }
                prop.load(new FileInputStream(Constant.APP_CONFIG_FILE));
                Log.v(TAG, "appConfig : " + prop);
                if (prop.isEmpty()) {
                    Log.e(TAG, "appConfig 無設定內容");
                }
            } catch (Throwable e) {
                Log.e(TAG, "appConfig 讀取失敗!", e);
            }
            _APP_CONFIG = prop;
        }
        return _APP_CONFIG;
    }
}
