package com.example.gtuandroid;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class PasswordActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        final EditText editText = (EditText) findViewById(R.id.editText1);
        final CheckBox chk = (CheckBox) findViewById(R.id.checkBox1);
        chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean) {
                if (chk.isChecked()) {
                    editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        back();
    }

    void back() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                PasswordActivity.this.setResult(RESULT_CANCELED, PasswordActivity.this.getIntent());
                PasswordActivity.this.finish();
            }
        });
    }
}
