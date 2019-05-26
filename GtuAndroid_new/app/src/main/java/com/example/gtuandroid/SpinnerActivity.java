package com.example.gtuandroid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SpinnerActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner);

        Button addBtn = (Button) findViewById(R.id.button1);
        Button removeBtn = (Button) findViewById(R.id.button2);
        final EditText editText = (EditText) findViewById(R.id.editText1);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner1);

        final List<String> allCountries = new ArrayList<String>();
        allCountries.addAll(Arrays.asList("台北市", "台北縣", "桃園市", "新竹市", "台中市", "台南市", "高雄市", "屏東市"));
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, allCountries);
        final ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_list_item_1);
        
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
                //editText.setText(spinner.getSelectedItem().toString());
                editText.setText(allCountries.get(paramInt));
                Log.v("test", "SelectedItem = " + paramAdapterView.getSelectedItem());
                Toast.makeText(SpinnerActivity.this, "你選的是 : " + paramAdapterView.getItemAtPosition(paramInt).toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> paramAdapterView) {
            }
        });

        addBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                String text = editText.getText().toString();
                if (text == null || text.length() == 0) {
                    return;
                }
                if (allCountries.contains(text)) {
                    return;
                }
                adapter.add(text);
                int pos = adapter.getPosition(text);
                spinner.setSelection(pos);
            }
        });

        removeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                if (spinner.getSelectedItem() != null) {
                    adapter.remove(spinner.getSelectedItem().toString());
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
                SpinnerActivity.this.setResult(RESULT_CANCELED, SpinnerActivity.this.getIntent());
                SpinnerActivity.this.finish();
            }
        });
    }
}
