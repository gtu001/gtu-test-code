package com.example.gtuandroid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class FileDataStoreActivity extends Activity {

    private static final String TAG = FileDataStoreActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout contentView = createContentView();

        // sd卡儲存
        Button sdCardSaveButton = new Button(this);
        sdCardSaveButton.setText("sd卡儲存");
        contentView.addView(sdCardSaveButton);
        sdCardSaveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                try {
                    File file = new File("/sdcard/sdcard_save.txt");
                    Log.v(TAG, "SD卡路徑:" + file.getCanonicalPath());
                    
                    FileOutputStream ofs = new FileOutputStream(file);
                    ofs.write("test".getBytes());
                    ofs.flush();
                    ofs.close();

                    Log.v(TAG, "SD卡路徑:" + file.getCanonicalPath());
                    Toast.makeText(getApplicationContext(), "SD卡路徑:" + file.getCanonicalPath(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                
            }
        });

        // 本地儲存
        Button localSaveButton = new Button(this);
        localSaveButton.setText("本地儲存");
        contentView.addView(localSaveButton);
        localSaveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                try {
                    FileOutputStream ofs = openFileOutput("local_save.txt", Context.MODE_PRIVATE);
                    ofs.write("test".getBytes());
                    ofs.flush();
                    ofs.close();

                    File file = new File(FileDataStoreActivity.this.getFilesDir(), "local_save.txt");
                    
                    Log.v(TAG, "檔案" + file.exists() + " :" + file.getCanonicalPath());
                    Toast.makeText(getApplicationContext(), "檔案" + file.exists() + " :" + file.getCanonicalPath(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 本地讀取
        Button localLoadButton = new Button(this);
        localLoadButton.setText("本地讀取");
        contentView.addView(localLoadButton);
        localLoadButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                try {
                    StringBuilder sb = new StringBuilder();
                    FileInputStream openFileInput = openFileInput("local_save.txt");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput));
                    for (String line = null; (line = reader.readLine()) != null;) {
                        sb.append(line + "\n");
                    }
                    reader.close();
                    
                    Log.v(TAG, "檔案" + sb.toString());
                    Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 存取路徑
        Button localLoadButton2 = new Button(this);
        localLoadButton2.setText("存取路徑計算");
        contentView.addView(localLoadButton2);
        localLoadButton2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                String external = null;
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    external = getApplicationContext().getExternalFilesDir(null).getAbsolutePath();
                }
                String internal = getApplicationContext().getFilesDir().getAbsolutePath();
                StringBuilder sb = new StringBuilder();
                sb.append("外部:" + external + "\n");
                sb.append("內部:" + internal);
                
                Log.v(TAG, sb.toString());
                Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // 回前頁
        Button backButton = new Button(this);
        backButton.setText("回前頁");
        contentView.addView(backButton);
        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                FileDataStoreActivity instance = FileDataStoreActivity.this;
                instance.setResult(RESULT_CANCELED, instance.getIntent());
                instance.finish();
            }
        });
    }
    
    /**
     * 取得內部儲存位置的基準目錄
     */
    public static String getSaveBasePath(Context context){
        String baseFolder;
        // check if external storage is available
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            baseFolder = context.getExternalFilesDir(null).getAbsolutePath();
        } else {
            // revert to using internal storage
            baseFolder = context.getFilesDir().getAbsolutePath();
        }
        return baseFolder;
    }

    private LinearLayout createContentView() {
        LinearLayout layout = new LinearLayout(this);
        ScrollView scroll = new ScrollView(this);
        setContentView(scroll);
        layout.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(layout, //
                new LayoutParams(LayoutParams.FILL_PARENT, // 設定與螢幕同寬
                        LayoutParams.WRAP_CONTENT));// 高度隨內容而定
        return layout;
    }
}
