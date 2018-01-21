package com.example.gtuandroid;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewActivity extends Activity {

    ArrayAdapter<String> aryAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridview);
        back();

        final GridView gridView1 = (GridView) findViewById(R.id.gridView1);
        final TextView textView1 = (TextView) findViewById(R.id.textView1);
        Button button2x2 = (Button) findViewById(R.id.button1);
        Button button3x3 = (Button) findViewById(R.id.button2);

        final String[] twoPlus = new String[] { "1", "2", "3", "4" };
        final String[] threePlus = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9" };

        button2x2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                gridView1.setNumColumns(2);//以兩欄的方式呈現
                aryAdapter = new ArrayAdapter<String>(GridViewActivity.this, android.R.layout.simple_list_item_1, twoPlus);
                gridView1.setAdapter(aryAdapter);
                gridView1.setSelection(2);
                gridView1.refreshDrawableState();
            }
        });
        button3x3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                gridView1.setNumColumns(3);//以兩欄的方式呈現
                aryAdapter = new ArrayAdapter<String>(GridViewActivity.this, android.R.layout.simple_list_item_1, threePlus);
                gridView1.setAdapter(aryAdapter);

                gridView1.setAdapter(new ImageAdapter(GridViewActivity.this));
            }
        });
        gridView1.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                switch (aryAdapter.getCount()) {
                case 4:
                    textView1.setText(twoPlus[position]);
                    break;
                case 9:
                    textView1.setText(threePlus[position]);
                    break;
                }
            }
        });
    }

    static Integer[] mThumbIds = { //
        android.R.drawable.btn_star, //
        android.R.drawable.btn_star, //
        android.R.drawable.btn_star, //
        android.R.drawable.btn_star, //
        android.R.drawable.btn_star, //
        android.R.drawable.btn_star, //
        android.R.drawable.btn_star, //
        android.R.drawable.btn_star, //
        android.R.drawable.btn_star, //
        };

    static class ImageAdapter extends BaseAdapter {
        private Context context;

        ImageAdapter(Context c) {
            context = c;
        }

        @Override
        public int getCount() {
            return mThumbIds.length;
        }

        @Override
        public Object getItem(int paramInt) {
            return null;//return the actual object at the specified position in the adapter, but it's ignored for this example
        }

        @Override
        public long getItemId(int paramInt) {
            return 0;//return the row id of the item
        }

        @Override
        public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
            ImageView imageView;
            if (paramView == null) {
                //if it's not recycled, initialize some attributes
                imageView = new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(85, 85));//設定網格選單每一個欄位大小85x85
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//設定每個圖像安置在中央
                imageView.setPadding(8, 8, 8, 8);//設定上下左右間隙8
            } else {
                imageView = (ImageView) paramView;
            }
            imageView.setImageResource(mThumbIds[paramInt]);
            return imageView;
        }
    }

    void back() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                GridViewActivity.this.setResult(RESULT_CANCELED, GridViewActivity.this.getIntent());
                GridViewActivity.this.finish();
            }
        });
    }
}
