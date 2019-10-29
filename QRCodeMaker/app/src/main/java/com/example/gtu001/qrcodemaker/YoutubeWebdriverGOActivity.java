package com.example.gtu001.qrcodemaker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gtu001.qrcodemaker.common.LayoutViewHelper;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

public class YoutubeWebdriverGOActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = YoutubeWebdriverGOActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = LayoutViewHelper.createContentView_simple(this);

        final TextView labelText = new TextView(this);
        labelText.setText("請輸入YoutubeID:");
        layout.addView(labelText);
        final EditText youtubeIdText = new EditText(this);
        youtubeIdText.setSingleLine(true);
        layout.addView(youtubeIdText);
        Button btn1 = new Button(this);
        layout.addView(btn1);
        Button clearBtn = new Button(this);
        layout.addView(clearBtn);

        btn1.setText("確定");
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoutubeWebDriverStartMovie y = new YoutubeWebDriverStartMovie("kOHB85vDuow");
                y.playVod();
            }
        });

        clearBtn.setText("清除");
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                youtubeIdText.setText("");
            }
        });
    }


    private class YoutubeWebDriverStartMovie {
        String youtubeId;

        YoutubeWebDriverStartMovie(String youtubeId) {
            this.youtubeId = youtubeId;
        }

        private void playVod() {
            try {
                DesiredCapabilities capabilities = new DesiredCapabilities();

                capabilities.setCapability("deviceName", "Android Emulator");
                capabilities.setCapability("platformName", "Android");
                capabilities.setCapability("app", "com.example.gtu001.qrcodemaker");

                String url = "https://www.youtube.com/watch?v=" + StringUtils.trimToEmpty(youtubeId);

                AppiumDriver<WebElement> driver = new AndroidDriver<WebElement>(
                        new URL(url),
                        capabilities);

                driver.runAppInBackground(10);
                driver.launchApp();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //----------------------------------------------------------------------------------------

    static int REQUEST_CODE = 5566;
    static int MENU_FIRST = Menu.FIRST;

    /**
     * TaskInfo.XXXXXXXXXXXXXXXXXXXXX.onOptionsItemSelected(NintendoSwitchCheckerActivity.this, intent, bundle);
     */
    enum TaskInfo {
        XXXXXXXXXXXXXXXXXXXXX("XXXXXXXXXXXXXX", MENU_FIRST++, REQUEST_CODE++, FileFindActivity.class) {
            protected void onActivityResult(YoutubeWebdriverGOActivity activity, Intent intent, Bundle bundle) {
            }

            protected void onOptionsItemSelected(YoutubeWebdriverGOActivity activity, Intent intent, Bundle bundle) {
                super.onOptionsItemSelected(activity, intent, bundle);
            }
        }, //
        ;

        final String title;
        final int option;
        final int requestCode;
        final Class<?> clz;
        final boolean debugOnly;

        TaskInfo(String title, int option, int requestCode, Class<?> clz) {
            this(title, option, requestCode, clz, false);
        }

        TaskInfo(String title, int option, int requestCode, Class<?> clz, boolean debugOnly) {
            this.title = title;
            this.option = option;
            this.requestCode = requestCode;
            this.clz = clz;
            this.debugOnly = debugOnly;
        }

        protected void onOptionsItemSelected(YoutubeWebdriverGOActivity activity, Intent intent, Bundle bundle) {
            intent.setClass(activity, clz);
            intent.putExtras(bundle);
            activity.startActivityForResult(intent, requestCode);
        }

        protected void onActivityResult(YoutubeWebdriverGOActivity activity, Intent intent, Bundle bundle) {
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.v(TAG, "# onCreateOptionsMenu");
        for (TaskInfo e : TaskInfo.values()) {

            //純測試
            if (!BuildConfig.DEBUG && e.debugOnly == true) {
                continue;
            }

            menu.add(0, e.option, 0, e.title);
        }
        return true;
    }

    public void onDestory() {
        super.onDestroy();
    }
}