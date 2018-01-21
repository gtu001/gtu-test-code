package com.example.gtuandroid;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FilePathActivity extends Activity {

    private StringBuffer sb = new StringBuffer();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        back();

        TextView textView1 = (TextView) findViewById(R.id.text);

        // 1
        // 當 Android 系統發現空間不足時，會將存放在暫存目錄 getCacheDir() 裡面的檔案刪除。因此，App
        // 在執行時不能假設存放在該目錄裡面的檔案一定存在，也不能假設該目錄底下的檔案一定會被系統刪除，最好是在檔案不用時 App
        // 自己將它刪除，以免占用內部儲存體的空間。
        // 2
        // 外部儲存體的另一個涵義指的是所有 App 的共用空間，對 App 來說存放在外部儲存體的檔案可以分為公開檔案 (public files)
        // 與私有檔案 (private files) 兩種。擺放在
        // Environment.getExternalStoragePublicDirectory() 目錄底下的為公開檔案，擺放在
        // Context.getExternalFilesDir() 目錄底下的為私有檔案。
        // 3
        // 公開檔案就像是照片或是音樂，由目前 App 產生可以提供其他 App 使用的檔案。私有檔案就像是 App 執行時產生的暫存檔，對其他
        // App 來說並沒有使用上的價值。擺放在外部儲存體的檔案都可以被其他 App 存取，不過當 App
        // 被移除時，只有私有檔案會被移除，公開檔案並不會被移除。
        
        contextPath();
        environmentPath();
        checkExternalPublicDir();
        sb.append("SD卡狀態:" + getSDCardStatus() + "\n");
        
        textView1.setText(sb);
    }

    private void contextPath() {
        sb.append("---context---\n");
        // 取得 App 內部儲存體存放檔案的目錄 (絕對路徑)
        // 預設路徑為 /data/data/[package.name]/files/
        sb.append("FilesDir:" + this.getFilesDir() + "\n");

        // 取得 App 內部儲存體存放暫存檔案的目錄 (絕對路徑)
        // 預設路徑為 /data/data/[package.name]/cache/
        sb.append("CacheDir:" + this.getCacheDir() + "\n");

        // 取得 App 外部儲存體存放檔案的目錄 (絕對路徑)
        sb.append("ExternalFilesDir:" + this.getExternalFilesDir("type") + "\n");

        // 取得 App 外部儲存體存放暫存檔案的目錄 (絕對路徑)
        sb.append("ExternalCacheDir:" + this.getExternalCacheDir() + "\n");

        // abstract File getDir(String name, int mode)
        // 取得 App 可以擺放檔案的目錄，若該目錄不存在則建立一個新的
        // ex: getDir("music", 0) -> /data/data/[package.name]/app_music
        sb.append("Dir:" + this.getDir("music", 0) + "\n");

        // 刪除 getFilesDir() 目錄底下名稱為 name 的檔案
        sb.append("deleteFile:" + this.deleteFile("fdjlhdalalwfhdsjzsdfjasdf") + "\n");

        // 回傳 getFilesDir() 目錄底下的檔案及目錄名稱
        for (int ii = 0; ii < this.fileList().length; ii++) {
            String filePath = this.fileList()[ii];
            sb.append("fileList[" + ii + "]:" + filePath + "\n");
        }

        // abstract FileInputStream openFileInput(String name)
        // 開啟 getFilesDir() 目錄下檔名為 name 的檔案來進行讀取
        
        // abstract FileOutputStream openFileOutput(String name, int mode)
        // 在 getFilesDir() 目錄底下開啟或建立檔名為 name 的檔案來進行寫入
        
        // abstract File getFileStreamPath(String name)
        // 取得 openFileOutput() 所建立之名稱為 name 的檔案的絕對路徑
    }

    private void environmentPath() {
        sb.append("---environment---\n");
        // 取得系統的資料擺放目錄，預設位置為 /data
        sb.append("DataDirectory:" + Environment.getDataDirectory() + "\n");

        // 取得系統檔案下載或暫存檔案的擺放目錄，預設位置為 /cache
        sb.append("DownloadCacheDirectory:" + Environment.getDownloadCacheDirectory() + "\n");

        // 取得外部儲存體的根目錄，預設位置為 /mnt/sdcard
        sb.append("ExternalStorageDirectory:" + Environment.getExternalStorageDirectory() + "\n");

        // 取得外部儲存體存放公開檔案的目錄
        sb.append("ExternalStoragePublicDirectory:" + Environment.getExternalStoragePublicDirectory("type") + "\n");

        // 取得外部儲存體的狀態資訊
        sb.append("ExternalStorageState:" + Environment.getExternalStorageState() + "\n");

        // 取得檔案系統的根目錄，預設位置為 /system
        sb.append("RootDirectory:" + Environment.getRootDirectory() + "\n");

        // 判斷外部儲存體是否使用內部儲存體模擬產生
        // true: 外部儲存體不存在，而是使用內部儲存體模擬產生
        // false: 外部儲存體存在，並非使用內部儲存體模擬
        sb.append("isExternalStorageEmulated:" + Environment.isExternalStorageEmulated() + "\n");

        // 判斷外部儲存體是否可以移除，回傳值的意義如下：
        // true: 外部儲存體屬於外接式的，且可以移除
        // false: 外部儲存體內建在系統中，無法被移除
        sb.append("isExternalStorageRemovable:" + Environment.isExternalStorageRemovable() + "\n");
    }

    private void checkExternalPublicDir() {
        sb.append("---public---\n");
        sb.append("鬧鐘的音效檔:" + //
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS) + "\n");
        sb.append("相機的圖片與影片檔:" + //
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "\n");
        sb.append("使用者下載的檔案:" + //
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "\n");
        sb.append("電影檔:" + //
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "\n");
        sb.append("音樂檔:" + //
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + "\n");
        sb.append("通知音效檔:" + //
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS) + "\n");
        sb.append("一般的圖片檔:" + //
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "\n");
        sb.append("訂閱的廣播檔:" + //
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS) + "\n");
        sb.append("鈴聲檔:" + //
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES) + "\n");
        // type 參數如果為 null 時可取得擺放公開檔案的根目錄，如果 App
        // 要擺放的檔案型態不屬於上述那幾類，也可以直接將檔案擺放在根目錄。
    }

    private String getSDCardStatus() {
        String status = Environment.getExternalStorageState();
        if (Environment.MEDIA_BAD_REMOVAL.equals(status)) {
            return "外部儲存體在正常卸載之前就被拔除";
        } else if (Environment.MEDIA_CHECKING.equals(status)) {
            return "外部儲存體存在且正在進行磁碟檢查";
        } else if (Environment.MEDIA_MOUNTED.equals(status)) {
            return "外部儲存體存在且可以進行讀取與寫入";
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(status)) {
            return "外部儲存體存在但只能進行讀取";
        } else if (Environment.MEDIA_NOFS.equals(status)) {
            return "外部儲存體存在，但內容是空的或是 Android 不支援該檔案系統";
        } else if (Environment.MEDIA_REMOVED.equals(status)) {
            return "外部儲存體不存在";
        } else if (Environment.MEDIA_SHARED.equals(status)) {
            return "外部儲存體存在但未被掛載，且為 USB 的裝置";
        } else if (Environment.MEDIA_UNMOUNTABLE.equals(status)) {
            return "外部儲存體存在但不能被掛載";
        } else if (Environment.MEDIA_UNMOUNTED.equals(status)) {
            return "外部儲存體存在但未被掛載";
        } else {
            return "unknow";
        }
    }

    private void checkSDCard() {
        File mSDFile = null;
        // 檢查有沒有SD卡裝置
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_REMOVED)) {
            Toast.makeText(FilePathActivity.this, "沒有SD卡!!!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            // 取得SD卡儲存路徑
            mSDFile = Environment.getExternalStorageDirectory();
            sb.append("SD卡儲存路徑:" + mSDFile + "\n");
        }
    }

    // 檢查外部儲存體是否可以進行寫入
    private boolean isExtStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    // 檢查外部儲存體是否可以進行讀取
    private boolean isExtStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    void back() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                FilePathActivity.this.setResult(RESULT_CANCELED, FilePathActivity.this.getIntent());
                FilePathActivity.this.finish();
            }
        });
    }
}
