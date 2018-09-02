package com.example.gtuandroid.sub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.gtuandroid.R;

public class FragmentTab1 extends Fragment {
    private View v;
    private ListView listView;
    private SimpleAdapter simpleAdapter;
    private int[] image = { //
    R.drawable.cat, R.drawable.flower, R.drawable.hippo,//
            R.drawable.monkey, R.drawable.mushroom, R.drawable.panda, //
            R.drawable.rabbit, R.drawable.raccoon //
    };

    private String[] imgText = { //
    "cat", "flower", "hippo", "monkey", "mushroom", "panda", "rabbit", "raccoon" //
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_listview, container, false);
        listView = (ListView) v.findViewById(R.id.listView1);
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < image.length; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("image", image[i]);
            item.put("text", imgText[i]);
            items.add(item);
        }
        simpleAdapter = new SimpleAdapter(getActivity(), //
                items, R.layout.subview_listview_bmp, new String[] { "image", "text" },//
                new int[] { R.id.imageView1, R.id.textView1 });//
        listView.setAdapter(simpleAdapter);//

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle args = new Bundle();
                args.putInt("position", position);

                Fragment newFragment = new FragmentTab1_ImageView();

                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                newFragment.setArguments(args);

                ft.replace(R.id.realtabcontent, newFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_NONE);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        return v;
    }
}