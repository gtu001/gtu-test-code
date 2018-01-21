package com.example.gtuandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

public class RadioActivity extends Activity {

    RadioGroup radioGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
        final RadioButton radio0 = (RadioButton) findViewById(R.id.radio0);
        final RadioButton radio1 = (RadioButton) findViewById(R.id.radio1);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (radio0.getId() == checkedId) {
                    Toast.makeText(RadioActivity.this, "你選了帥哥", Toast.LENGTH_SHORT).show();
                }
                if (radio1.getId() == checkedId) {
                    Toast.makeText(RadioActivity.this, "你選了美女", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final ToggleButton toggleButton1 = (ToggleButton) findViewById(R.id.toggleButton1);
        toggleButton1.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(getApplicationContext(), (isChecked ? "開" : "關"), Toast.LENGTH_SHORT).show();
            }
        });

        final CheckedTextView checkedTextView = (CheckedTextView) findViewById(R.id.checkedTextView1);
        checkedTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((CheckedTextView) v).toggle();
            }
        });

        final CheckBox checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        checkBox1.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(getApplicationContext(), (isChecked ? "勾" : "沒勾"), Toast.LENGTH_SHORT).show();
            }
        });

        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder sb = new StringBuilder();
                switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.radio0:
                    sb.append("radio : 帥哥");
                    break;
                case R.id.radio1:
                    sb.append("radio : 美女");
                    break;
                }
                sb.append("\n toggle :" + toggleButton1.isChecked());
                sb.append("\n checkbox :" + checkBox1.isChecked());
                sb.append("\n checkedTextView :" + checkedTextView.isChecked() + " / " + checkedTextView.getText());
                Toast.makeText(RadioActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        back();
    }

    void back() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                RadioActivity.this.setResult(RESULT_CANCELED, RadioActivity.this.getIntent());
                RadioActivity.this.finish();
            }
        });
    }
}
