package com.example.gtuandroid;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SharedPreferencesActivity extends Activity {
    EditText editText1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_preferences);
        back();

        editText1 = (EditText) findViewById(R.id.editText1);

        //初始化的時候將儲存的資料取出來
        SharedPreferences settings = getSharedPreferences(SETTING_PREF, 0);
        String text = settings.getString(EDIT_TEXT1, "");
        editText1.setText(text);
    }

    static String SETTING_PREF = "SETTING_Pref";
    static String EDIT_TEXT1 = "editText1";

    @Override
    protected void onStop() {
        super.onStop();
        //在離開前將資料存起來
        SharedPreferences settings = getSharedPreferences(SETTING_PREF, 0);
        // 0 - mode private , 1 - mode world readable , 2 - mode world writeable
        SharedPreferences.Editor editor = settings.edit();
        String text = editText1.getText().toString();
        editor.putString(EDIT_TEXT1, text);
        editor.commit();
        Toast.makeText(this, "儲存edit_text1 = " + text, Toast.LENGTH_SHORT).show();
    }

    void back() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                SharedPreferencesActivity.this.setResult(RESULT_CANCELED, SharedPreferencesActivity.this.getIntent());
                SharedPreferencesActivity.this.finish();
            }
        });
    }
}
