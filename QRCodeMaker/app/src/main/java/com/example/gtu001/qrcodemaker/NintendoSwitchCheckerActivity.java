package com.example.gtu001.qrcodemaker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NintendoSwitchCheckerActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = NintendoSwitchCheckerActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = LayoutViewHelper.createContentView_simple(this);

        final TextView labelText = new TextView(this);
        labelText.setText("請輸入switch序號:");
        layout.addView(labelText);
        final EditText switchText = new EditText(this);
        switchText.setSingleLine(true);
        layout.addView(switchText);
        Button btn1 = new Button(this);
        layout.addView(btn1);
        Button clearBtn = new Button(this);
        layout.addView(clearBtn);
        final TextView resultText = new TextView(this);
        layout.addView(resultText);
        switchText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                float percent = NintendoSwitchPatternEnum.isCrackable(switchText.getText().toString());
                resultText.setText("可破機率:" + percent + "%");
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        btn1.setText("確定");
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float percent = NintendoSwitchPatternEnum.isCrackable(switchText.getText().toString());
                resultText.setText("可破機率:" + percent + "%");
            }
        });

        clearBtn.setText("清除");
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchText.setText("");
                resultText.setText("");
            }
        });
    }


    private enum NintendoSwitchPatternEnum {
        T1("XAJ", "100001", "100309", 100f),//
        T2("XAJ", "700001", "700435", 100f),//
        T3("XAW", "400001", "400123", 100f),//
        T4("XAJ", "400001", "400530", 100f),//
        T5("XAW", "100501", "100793", 100f),//
        T6("XAW", "700001", "700173", 100f),//
        T7("XAK", "100000", "100053", 100f),//
        ;

        final String prefix;
        final String startNum;
        final String endNum;
        final int _startNum;
        final int _endNum;
        final float percent;

        NintendoSwitchPatternEnum(String prefix, String startNum, String endNum, float percent) {
            this.prefix = prefix;
            this.startNum = startNum;
            this.endNum = endNum;
            this.percent = percent;
            this._startNum = Integer.parseInt(startNum);
            this._endNum = Integer.parseInt(endNum);
        }

        public static float isCrackable(String strVal) {
            if (StringUtils.isBlank(strVal)) {
                return 0f;
            }
            Pattern digitPtn = Pattern.compile("[a-zA-Z]{3}(\\d{1,6})");
            strVal = StringUtils.trimToEmpty(strVal).toUpperCase();
            for (NintendoSwitchPatternEnum e : NintendoSwitchPatternEnum.values()) {
                if (strVal.startsWith(e.prefix)) {
                    Matcher mth = digitPtn.matcher(strVal);
                    if (mth.find()) {
                        int val = Integer.parseInt(mth.group(1));
                        if (e._startNum <= val && val <= e._endNum) {
                            return e.percent;
                        }
                    }
                }
            }
            return 0f;
        }
    }


    static int REQUEST_CODE = 5566;
    static int MENU_FIRST = Menu.FIRST;

    /**
     * TaskInfo.XXXXXXXXXXXXXXXXXXXXX.onOptionsItemSelected(NintendoSwitchCheckerActivity.this, intent, bundle);
     */
    enum TaskInfo {
        XXXXXXXXXXXXXXXXXXXXX("XXXXXXXXXXXXXX", MENU_FIRST++, REQUEST_CODE++, FileFindActivity.class) {
            protected void onActivityResult(NintendoSwitchCheckerActivity activity, Intent intent, Bundle bundle) {
            }

            protected void onOptionsItemSelected(NintendoSwitchCheckerActivity activity, Intent intent, Bundle bundle) {
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

        protected void onOptionsItemSelected(NintendoSwitchCheckerActivity activity, Intent intent, Bundle bundle) {
            intent.setClass(activity, clz);
            intent.putExtras(bundle);
            activity.startActivityForResult(intent, requestCode);
        }

        protected void onActivityResult(NintendoSwitchCheckerActivity activity, Intent intent, Bundle bundle) {
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