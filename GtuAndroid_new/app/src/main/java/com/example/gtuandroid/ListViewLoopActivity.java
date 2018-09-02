package com.example.gtuandroid;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ListViewLoopActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_listview);
        back();

        ListView listview = (ListView) findViewById(R.id.listView1);
        final TextView textview = (TextView) findViewById(R.id.textView1);
        
        listview.setAdapter(this.new MyLoopAdapter(this));
    }

    class MyLoopAdapter extends BaseAdapter {
        private ArrayList<Integer> mList;
        private LayoutInflater mLayoutInflater;

        public MyLoopAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
            mList = new ArrayList<Integer>();
            for (int i = 0; i < 100; i++) {
                mList.add(i);
            }
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            Holder holder = null;
            if (null == view) {
                if (null == view) {
                    view = mLayoutInflater.inflate(R.layout.subview_listview_sqlite, null);
                    holder = new Holder();
                    holder.itemText = (TextView) view.findViewById(R.id.textView);
                    view.setTag(holder);
                }
            } else {
                holder = (Holder) view.getTag();
            }
            holder.itemText.setText(mList.get(position) + "");
            return view;
        }
        
        class Holder {
            TextView itemText;
        }
    }

    void back() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                ListViewLoopActivity.this.setResult(RESULT_CANCELED, ListViewLoopActivity.this.getIntent());
                ListViewLoopActivity.this.finish();
            }
        });
    }
}
