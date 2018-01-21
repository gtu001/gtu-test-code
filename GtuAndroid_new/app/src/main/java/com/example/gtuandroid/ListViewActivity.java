package com.example.gtuandroid;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ListViewActivity extends Activity {

    private TextView textview;
    private ListView listview;
    private ArrayAdapter<String> aryAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        
        this.setContentView(createView());

//        this.setContentView(R.layout.activity_listview);
//        back();
//        listview = (ListView) findViewById(R.id.listView1);
//        TextView textview = (TextView) findViewById(R.id.textView1);

        final String[] dayArray = new String[] { "sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday" };
        aryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dayArray);
        listview.setAdapter(aryAdapter);
        //        listview.setChoiceMode(ListView.CHOICE_MODE_NONE);
        //        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        
        //設定間隔
        listview.setDivider(new ColorDrawable(android.graphics.Color.GRAY));
        listview.setDividerHeight(5);

        listview.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                textview.setText("你選的是" + arg0.getSelectedItem().toString());
                aryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                textview.setText("@你選的是" + dayArray[arg2]);
                aryAdapter.notifyDataSetChanged();
            }
        });

        //快速捲動
        listview.setFastScrollAlwaysVisible(true);
        listview.setFastScrollEnabled(true);
    }
    
    private LinearLayout createView(){
        LinearLayout myLinearLayout = new LinearLayout(this);
        myLinearLayout.setOrientation(LinearLayout.VERTICAL);
        myLinearLayout.setBackgroundColor(android.graphics.Color.GREEN);
        textview = new TextView(this);
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        myLinearLayout.addView(textview, param1);
        listview = new ListView(this);
        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        myLinearLayout.addView(listview, param2);
        return myLinearLayout;
    }

    private void back() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                ListViewActivity.this.setResult(RESULT_CANCELED, ListViewActivity.this.getIntent());
                ListViewActivity.this.finish();
            }
        });
    }
}
