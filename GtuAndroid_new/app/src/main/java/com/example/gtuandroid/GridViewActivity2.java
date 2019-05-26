package com.example.gtuandroid;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class GridViewActivity2 extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridview);
        back();

        final GridView gridView1 = (GridView) findViewById(R.id.gridView1);
        final TextView textView1 = (TextView) findViewById(R.id.textView1);
//        Button button2x2 = (Button) findViewById(R.id.button1);
//        Button button3x3 = (Button) findViewById(R.id.button2);
        
        final ArrayList<String[]> items = new ArrayList<String[]>();
        items.add(new String[]{"1", "Hello11" , "Hello21"});//超過會折行
        items.add(new String[]{"2", "Hello12" , "Hello22"});
        items.add(new String[]{"3", "Hello13" , "Hello23"});
        items.add(new String[]{"4", "Hello14" , "Hello24"});
        items.add(new String[]{"5", "Hello15" , "Hello25"});
        gridView1.setAdapter(new GridAdapter(items));

        gridView1.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                Toast.makeText(getApplicationContext(), "pos=" + position + ",arg3=" + arg3, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private static class GridAdapter extends BaseAdapter {

        final ArrayList<String> mItems;
        final int mCount;

        private GridAdapter(final ArrayList<String[]> items) {
            int rowItem = 0;
            if(!items.isEmpty()){
                rowItem = items.get(0).length;
            }

            mCount = items.size() * rowItem;
            mItems = new ArrayList<String>(mCount);

            for (String[] item : items) {
                for (String part : item) {
                    mItems.add(part);
                }
            }
        }

        @Override
        public int getCount() {
            return mCount;
        }

        @Override
        public Object getItem(final int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(final int position) {
            return position;
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(parent.getContext())//
                        .inflate(android.R.layout.simple_list_item_1, parent, false);
            }
            final TextView text = (TextView) view.findViewById(android.R.id.text1);
            text.setText(mItems.get(position));
            return view;
        }
    }

    void back() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                GridViewActivity2.this.setResult(RESULT_CANCELED, GridViewActivity2.this.getIntent());
                GridViewActivity2.this.finish();
            }
        });
    }
}
