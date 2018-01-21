package com.example.gtuandroid.sub;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gtuandroid.R;

public class FragmentTab3 extends Fragment {

    private GridView gridView;

    private int[] image = {//
    R.drawable.cat, R.drawable.flower, R.drawable.hippo,//
            R.drawable.monkey, R.drawable.mushroom, R.drawable.panda,//
            R.drawable.rabbit, R.drawable.raccoon //
    };

    private String[] imgText = {//
    "cat", "flower", "hippo", "monkey", "mushroom", "panda", "rabbit", "raccoon"//
    };//

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_gridview_simple, container, false);
        
        //會outOfMemory
//        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
//        for (int i = 0; i < image.length; i++) {
//            Map<String, Object> item = new HashMap<String, Object>();
//            item.put("image", image[i]);
//            item.put("text", imgText[i]);
//            items.add(item);
//        }
//        SimpleAdapter adapter = new SimpleAdapter(getActivity(), //
//                items, R.layout.subview_listview_bmp, new String[] { "image", "text" },//
//                new int[] { R.id.imageView1, R.id.textView1 });//

        gridView = (GridView) v.findViewById(R.id.gridview1);
        gridView.setNumColumns(3);
        gridView.setAdapter(new MyAdapter(this.getActivity().getApplicationContext()));
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "你按下 " + imgText[position], Toast.LENGTH_SHORT).show();
            }

        });
        return v;
    }
    
    class MyAdapter extends BaseAdapter {
        private LayoutInflater mLayoutInflater;

        public MyAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return image.length;
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
                    view = mLayoutInflater.inflate(R.layout.subview_listview_bmp, null);
                    holder = new Holder();
                    holder.itemText = (TextView) view.findViewById(R.id.textView1);
                    holder.imageView = (ImageView) view.findViewById(R.id.imageView1);
                    view.setTag(holder);
                }
            } else {
                holder = (Holder) view.getTag();
            }
            try{
                holder.itemText.setText(imgText[position] + "");
                holder.imageView.setImageResource(image[position]);
            }catch(Throwable ex){
                Log.e("error", ex.getMessage());
            }
            return view;
        }
        
        class Holder {
            TextView itemText;
            ImageView imageView;
        }
    }
}