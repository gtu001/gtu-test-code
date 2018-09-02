package com.example.gtuandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;

public class AutoCompleteActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autocomplete);
        back();

        AutoCompleteTextView autoCompleteTextView1 = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        String[] autoStr = new String[] { "a", "ab", "abc", "abcd", "abcde" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, autoStr);
        autoCompleteTextView1.setAdapter(adapter);
        autoCompleteTextView1.setThreshold(1);//設定輸入幾個字觸發

        MultiAutoCompleteTextView multiAutoCompleteTextView1 = (MultiAutoCompleteTextView) findViewById(R.id.multiAutoCompleteTextView1);
        multiAutoCompleteTextView1.setAdapter(adapter);
        multiAutoCompleteTextView1.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }

    void back() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                AutoCompleteActivity.this.setResult(RESULT_CANCELED, AutoCompleteActivity.this.getIntent());
                AutoCompleteActivity.this.finish();
            }
        });
    }
}
